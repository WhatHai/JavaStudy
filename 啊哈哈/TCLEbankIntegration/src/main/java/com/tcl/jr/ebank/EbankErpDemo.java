package com.tcl.jr.ebank;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tcl.jr.ebank.webService.util.EncryptionUtil;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.*;

public class EbankErpDemo {

    // xml 根节点
    public static final String TRANSACTION = "TRANSACTION";
    // xml 请求body节点
    public static final String REQUEST = "REQUEST";
    // xml 响应body节点
    public static final String RESPONSE = "RESPONSE";
    // header节点
    public static final String HEADER = "HEADER";
    // header-请求时间
    public static final String REQTIME = "REQ_TIME";
    // 用户号
    public static final String USERNAME = "USER_NAME";
    // header-请求IP
    public static final String REQIP = "REQ_IP";
    // header-请求类型
    public static final String REQTYPE = "REQ_TYPE";
    // 请求序号
    public static final String REQINDEX = "REQ_INDEX";
    // xml body节点
    public static final String paymentTranType = "FK";
    // APPNO
    public static final String APPNO = "APPNO";
    // 财企直连版本号
    public static final String VERSION = "VERSION";

    public String eaiTest(JSONObject headerJson, JSONObject bodyJson, String url) throws Exception {
        Set<String> headerSet = headerJson.keySet();
        Set<String> bodySet = bodyJson.keySet();
        Element firstNode = new Element(TRANSACTION);
        Element header = new Element(HEADER);
        for (String key : headerSet) {
            Element temp = new Element(key);
            temp.addContent(headerJson.getString(key));
            header.addContent(temp);
        }
        firstNode.addContent(header);
        Element bodyNode = new Element(REQUEST);
        Document doc = new Document(firstNode);
        firstNode.addContent(bodyNode);
        for (String key : bodySet) {
            Element temp = new Element(key);
            temp.addContent(bodyJson.getString(key));
            bodyNode.addContent(temp);
        }

        String reqContent = xml2String(doc);
        System.out.println(reqContent);
        String[] appnos = {"jdh", "FSSC", "BMS", "FSSCH", "WD"};
        System.out.println("--69 before exchange--");
        String retContent = exchange(reqContent, url, "WD","10.0.17.164","7001");
        System.out.println("return data: "+retContent);
        if (null != retContent && !"".equals(retContent)) {
            Document retDoc = string2Xml(retContent);
            //处理返回数据 返回数据存放在map中
            Map<String, String> retMap = parseDocument(retDoc);
//            System.out.println(retMap.toString());
            return retMap.toString();
        }
        return "ERROR";
    }

    private static int getNum() {
        int max = 4;
        int min = 0;
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;

    }


    public String exchange(String reqContent, String url, String appno,String ip,String port) throws UnsupportedEncodingException {
//        String ip = "127.0.0.1";// 结算中心ip
//        String port = "80";// 结算中心port
//        String remoteUrl = "http://" + ip + ":" + port + "/eai/" + url;
//        String ip = "10.0.17.161";// 结算中心ip
//        String port = "7001";// 结算中心port
        String remoteUrl = "http://" + ip + ":" + port + "/eai/" + url;
        System.out.println("remoteUrl------------"+remoteUrl);
        String cerPath = "../TCLEbankIntegration/src/main/resources/auge.cer";
        String keyPath = "../TCLEbankIntegration/src/main/resources/jdh.keystore";
//        String cerPath="auge.cer";
//        String keyPath="jdh.keystore";
//         通过http发送拼好的上送xml
       reqContent="<?xml version=\"1.0\" encoding=\"UTF-8\"?><TRANSACTION><HEADER><CHANNEL>EAI</CHANNEL><REQ_TYPE>payment</REQ_TYPE><APPNO>auge</APPNO><VERSION>2</VERSION><USER_NAME>xuxh</USER_NAME><REQ_IP>10.0.14.60</REQ_IP><REQ_INDEX>10.0.14.601537171937001</REQ_INDEX><REQ_TIME>180919041216</REQ_TIME></HEADER><REQUEST><PROVINCENAME>广东省</PROVINCENAME><RECORGNAME>工行惠州分行</RECORGNAME><VOUCHER_ID>Fk20180919161216T9zmq4ycKTKvT1w4</VOUCHER_ID><ADDRESS>10.0.14.60</ADDRESS><TRANAMT>731701.78</TRANAMT><TRANDATE>2022-06-06</TRANDATE><DBTYPE>mysql</DBTYPE><RECACCTNO>2008020119024239435</RECACCTNO><TRANTYPE>203</TRANTYPE><CHANNEL>EAI</CHANNEL><EXPLMODE>1</EXPLMODE><PAYMODE>1</PAYMODE><POSTSCRIPT /><APPROVALPROCESS>0</APPROVALPROCESS><PAYACCTNO>10101100103587010001</PAYACCTNO><RECBANKNAME>工商银行</RECBANKNAME><URGFLAG>0</URGFLAG><RECACCTNAME>惠州TCL房地产开发有限公司</RECACCTNAME><CITYNAME>惠州市</CITYNAME><DIGEST>其他</DIGEST></REQUEST></TRANSACTION>";
        //reqContent="<?xml version=\"1.0\" encoding=\"UTF-8\"?><TRANSACTION><HEADER><REQ_TYPE>foreignPayment</REQ_TYPE><VERSION>2</VERSION><REQ_TIME>20170811173605</REQ_TIME><USER_NAME>zengql</USER_NAME><REQ_IP>10.0.14.47</REQ_IP><CUST_ID>zhanglq</CUST_ID><REQ_INDEX>10.0.14.471497605016247</REQ_INDEX><REQCODE>auge</REQCODE></HEADER><REQUEST><CHANNEL>EAI</CHANNEL><PAYACCTNO>4020011196100001</PAYACCTNO><TRANTYPE>201</TRANTYPE><PAYMODE>1</PAYMODE><TRANAMT>5311.24</TRANAMT><DIGEST>gongchengkuan</DIGEST><PAYUSE>gongchengkuan</PAYUSE><RECACCTNO>4010011196100001</RECACCTNO><RECACCTINFO>TCL Corporate Research (Hong Kong) Co.,Limited</RECACCTINFO><DEALDATE>2017-04-20</DEALDATE><DEALTIME>08:09:14</DEALTIME><DBTYPE>ORACLE</DBTYPE><DBVERSION>9.2.0.4.0</DBVERSION><ADDRESS>10.0.14.66</ADDRESS><VOUCHER_ID>Fk20180919161216T9zmq4ycKTKvT4w6</VOUCHER_ID></REQUEST></TRANSACTION>";
        HttpClientUtil client = new HttpClientUtil();
        String encrypted = "";
        try {
            encrypted = EncryptionUtil.encryptData(reqContent, keyPath, "123456", "jdh", "jdh", cerPath);// 使用企业私钥对报文签名
            System.out.println("加密后："+encrypted);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JSONObject EncryptedJSON = JSON.parseObject(encrypted);
        client.setCharset("UTF-8");
        //若使用https时如果出现javax.net.ssl.SSLProtocolException: handshake alert:  unrecognized_name时，取消注释
        //System.setProperty("jsse.enableSNIExtension", "false");

        System.out.println("--remoteUrl --"+remoteUrl);
        boolean isConnected = client.callHttpPost(remoteUrl, "data=" + EncryptedJSON.getString("datacontent") + "&sign="
                + EncryptedJSON.getString("sign") + "&aeskey=" + EncryptedJSON.getString("aeskey") + "&appno="+appno);
        if (!isConnected) {
            return "连接失败！";
        }
        // 接收返回信息lk
        String retContent = client.getResContent();

        if (null == retContent || "".equals(retContent)) {
            return "";
        }
//        System.out.println(retContent);
        try {
            JSONObject retJson = JSON.parseObject(retContent);
            retContent = EncryptionUtil.decryptData(retJson.getString("datacontent"), retJson.getString("sign"), retJson.getString("aeskey"), keyPath, "jdh", "123456", cerPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retContent;
    }

    /**
     * xml to string
     *
     * @param doc
     * @return
     */
    private String xml2String(Document doc) {
        Format format = Format.getCompactFormat();
        format.setEncoding("UTF-8");
        XMLOutputter xmlout = new XMLOutputter(format);
        return xmlout.outputString(doc);
    }

    /**
     * string to xml
     *
     * @param xmlStr
     * @return
     * @throws Exception
     */
    private Document string2Xml(String xmlStr) {
        Reader in = new StringReader(xmlStr);
        Document doc = null;
        try {
            doc = (new SAXBuilder()).build(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * 解析返回报文Document
     *
     * @param doc
     * @return
     */
    private Map<String, String> parseDocument(Document doc) {
        Element root = doc.getRootElement();
        Map<String, String> elementMap = new HashMap<String, String>();

        Element header = root.getChild(HEADER);
        if (null != header) {
            List<Element> headerList = header.getChildren();
            for (Element e : headerList) {
                elementMap.put(e.getName(), e.getText());
            }
        }

        Element response = root.getChild(RESPONSE);
        if (null != response) {
            List<Element> responseList = response.getChildren();
            for (Element e : responseList) {
                elementMap.put(e.getName(), e.getText());
            }
        }
        return elementMap;
    }

    /**
     * 解析循环报文 将非循环部分处理成Map 将循环部分处理成List
     *
     * @param str
     * @param list
     * @return
     */
    private Map<String, String> parseStringDocument2List(String str, List<Map<String, String>> list) {

        Document doc = string2Xml(str);

        Map<String, String> notCircleMap = new HashMap<String, String>();
        Element root = doc.getRootElement();

        Element header = root.getChild(HEADER);
        if (null != header) {
            List<Element> headerList = header.getChildren();
            for (Element e : headerList) {
                notCircleMap.put(e.getName(), e.getText());
            }
        }

        Element secondNode = root.getChild(RESPONSE);
        if (null != secondNode) {

            List<Element> bodyList = secondNode.getChildren();
            for (int i = 0; i < bodyList.size(); i++) {
                Element thirdNode = bodyList.get(i);
                if (thirdNode.getChildren().size() == 0) {
                    notCircleMap.put(thirdNode.getName(), thirdNode.getText());
                } else {
                    Map<String, String> circleMap = new HashMap<String, String>();
                    List<Element> circleList = thirdNode.getChildren();
                    for (int j = 0; j < circleList.size(); j++) {
                        Element element = circleList.get(j);
                        circleMap.put(element.getName(), element.getText());
                    }
                    list.add(circleMap);
                    list.add(circleMap);
                }
            }
        }
        return notCircleMap;
    }

    public static void main(String[] args) throws Exception {

        System.out.println(new EbankErpDemo().payment());
//        System.out.println(new EbankErpDemo().achPayment());
//        System.out.println(new EbankErpDemo().paymentTranQuery());
//        System.out.println(new EbankErpDemo().foreignPayment());
//        System.out.println(new EbankErpDemo().todayTranQuery());
//        System.out.println(new EbankErpDemo().historyTranQuery());
//        String len="--------len_out_of_30-----------";
//        System.out.println(len.length());


    }

    public String achPayment() throws Exception {
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String ip = InetAddress.getLocalHost().getHostAddress();
        Date reqDate = new Date();
        SimpleDateFormat reqForamt = new SimpleDateFormat("yyMMddhhmmss");
        header.put(REQTYPE, "foreignPayment");
        header.put(REQTIME, reqForamt.format(reqDate));
        header.put(REQIP, ip);
        header.put(REQINDEX, ip + System.currentTimeMillis());
        header.put("USER_NAME", "jr01521");
        header.put("CHANNEL", "EAI");
        header.put("APPNO","WD");
        body.put("PAYACCTNO", "40101100008011010001");
        body.put("TRANAMT", "78.00");
        body.put("TRANDATE","2018-08-10");
        body.put("QSNUM","123");
        body.put("DIGEST", "hahahahahahah");
        body.put("PAYUSE","hahahahahahahahha");
        body.put("RECACCTNO", "40101100008011010002");
        body.put("VOUCHER_ID", "d61-9f453101033ca");
        body.put("RECACCTNAME", "a 3");
        body.put("RECADDR", "a 3");
        body.put("EMAILS","123@qq.com");
        body.put("APPROVALPROCESS", "0");
        body.put("POSTSCRIPT","23dadsa");
        return eaiTest(header, body, "achPayment");
    }

    public String foreignPayment() throws Exception {
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String ip = InetAddress.getLocalHost().getHostAddress();
        Date reqDate = new Date();
        SimpleDateFormat reqForamt = new SimpleDateFormat("yyMMddhhmmss");
        header.put(REQTYPE, "foreignPayment");
        header.put(REQTIME, reqForamt.format(reqDate));
        header.put(REQIP, ip);
        header.put(REQINDEX, ip + System.currentTimeMillis());
        header.put("VERSION", 2);
        header.put("APPNO", "qweqweqeq");
        header.put("USER_NAME", "jr01521");
        header.put("CHANNEL", "EAI");

        body.put("PAYACCTNO", "40101100008011010001");
//        body.put("PAYACCTNO", "-------------");
        body.put("TRANTYPE", "203");
        body.put("PAYMODE", "1");
        body.put("TRANAMT", "78.00");
        body.put("DIGEST", "hahahahahahah");
//        body.put("PAYUSE","hahahahahahahahha");
        body.put("RECBKSTATE", "HK");
        body.put("RECBANKSWCD", "123123");
        body.put("CHARGEBEARS", "A");
        body.put("RECACCTNO", "40101100008011010084");
//        body.put("EMAILS","   qweqwe@qq.com");
        body.put("VOUCHER_ID", UUID.randomUUID().toString().substring(20));
        body.put("RECACCTNAME", "TCL集团财务");
//        body.put("RECADDR", "a 3");
        body.put("APPROVALPROCESS", "0");


        return eaiTest(header, body, "foreignPayment");
    }

    public String payment() throws Exception {
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String ip = InetAddress.getLocalHost().getHostAddress();
        Date reqDate = new Date();
        SimpleDateFormat reqForamt = new SimpleDateFormat("yyMMddhhmmss");

        header.put("REQ_TYPE", "payment");
        header.put("REQ_TIME", reqForamt.format(reqDate));
        header.put("REQ_IP", ip);
        header.put("VERSION", 2);
        header.put("APPNO", "auge");
        header.put("USER_NAME", "xuxh");
        header.put("CHANNEL", "EAI");
        header.put("REQ_INDEX", ip + System.currentTimeMillis());

        body.put("CHANNEL", "EAI");
        body.put("PAYACCTNO", "10101100103587010001");

        body.put("RECACCTNAME", "惠州TCL房地产开发有限公司");
        body.put("RECACCTNO", "2008020119024239435");
        body.put("RECORGNAME", "工行惠州分行");
        body.put("RECBANKNAME", "工商银行");
        body.put("PROVINCENAME", "广东省");
        body.put("CITYNAME", "惠州 市");
        body.put("TRANAMT", "731701.78");
        body.put("PAYMODE", "1");
        body.put("TRANTYPE", "203");
        body.put("URGFLAG", "0");
        body.put("EXPLMODE", "1");
        body.put("TRANDATE", "2022-06-06");
        body.put("DIGEST", "其他");
        body.put("DBTYPE", "mysql");
        body.put("ADDRESS", ip);
        body.put("VOUCHER_ID", UUID.randomUUID().toString().substring(20));
        body.put("POSTSCRIPT", "test");
        body.put("APPROVALPROCESS", "1");

        return eaiTest(header, body, "foreignPayment");
    }

    public String paymentTranQuery() throws Exception {
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String ip = InetAddress.getLocalHost().getHostAddress();
        Date reqDate = new Date();
        SimpleDateFormat reqForamt = new SimpleDateFormat("yyMMddhhmmss");

        header.put("REQ_TYPE", "paymentQuery");
        header.put("REQ_TIME", reqForamt.format(reqDate));
        header.put("REQ_IP", ip);
        header.put("VERSION", 2);
        header.put("APPNO", "auge");
        header.put("USER_NAME", "jr08981");
        header.put("CHANNEL", "EAI");
        header.put("REQ_INDEX", ip + System.currentTimeMillis());

//        body.put("CMSTRANNO","FK123456789");
        body.put("VOUCHER_ID", "7894123wqow5612e1e78666");
        return eaiTest(header, body, "paymentQuery");
    }

    public String todayTranQuery() throws Exception {
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String ip = InetAddress.getLocalHost().getHostAddress();
        Date reqDate = new Date();
        SimpleDateFormat reqForamt = new SimpleDateFormat("yyMMddhhmmss");

        header.put("REQ_TYPE", "todayTranQuery");
        header.put("REQ_TIME", reqForamt.format(reqDate));
        header.put("REQ_IP", ip);
        header.put("VERSION", 2);
        header.put("APPNO", "auge");
        header.put("USER_NAME", "jr07967");
        header.put("CHANNEL", "EAI");
        header.put("REQ_INDEX", ip + System.currentTimeMillis());

        body.put("TRANTIME", "12:00:00");
        body.put("CUSTID", "110100");
        return eaiTest(header, body, "todayTranQuery");
    }

    public String historyTranQuery() throws Exception {
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        String ip = InetAddress.getLocalHost().getHostAddress();
        Date reqDate = new Date();
        SimpleDateFormat reqForamt = new SimpleDateFormat("yyMMddhhmmss");
        header.put("REQ_TYPE", "historyTranQuery");
        header.put("REQ_TIME", reqForamt.format(reqDate));
        header.put("REQ_IP", ip);
        header.put("VERSION", 2);
        header.put("APPNO", "auge");
        header.put("USER_NAME", "jr07967");
        header.put("CHANNEL", "EAI");
        header.put("REQ_INDEX", ip + System.currentTimeMillis());

        body.put("STARTDATE", "2018-9-25");
        body.put("ENDDATE", "2023-11-25");
        return eaiTest(header, body, "historyTranQuery");
    }


}
