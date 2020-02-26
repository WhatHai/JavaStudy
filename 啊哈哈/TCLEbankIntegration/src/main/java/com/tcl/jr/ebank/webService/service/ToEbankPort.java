package com.tcl.jr.ebank.webService.service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.jdom2.Document;
import org.jdom2.Element;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tcl.jr.ebank.HttpClientUtil;
import com.tcl.jr.ebank.webService.bean.PIRequest;
import com.tcl.jr.ebank.webService.bean.PIResponse;
import com.tcl.jr.ebank.webService.util.EncryptionUtil;
import com.tcl.jr.ebank.webService.util.PropertiesUtil;
import com.tcl.jr.ebank.webService.util.xmlSElementUtil;

public class ToEbankPort {
	/**
	 * 
	 */
	private static final String REQ_HEADER_PARAM = PropertiesUtil.getValue("REQ_HEADER_PARAM");
	private static final String REQ_REQUEST_PARAM = PropertiesUtil.getValue("REQ_REQUEST_PARAM");
	private static final String payment_Url = PropertiesUtil.getValue("payment_Url");
	private static final String foreign_payment_Url = PropertiesUtil.getValue("foreign_payment_Url");
	private static final String payment_QueryUrl = PropertiesUtil.getValue("payment_QueryUrl");
	private static final String ebank_appno = PropertiesUtil.getValue("ebank_appno");
	private static final String ebank_IP = PropertiesUtil.getValue("ebank_IP");
	private static final String ebank_port = PropertiesUtil.getValue("ebank_port");
	private static final String ebank_password = PropertiesUtil.getValue("ebank_password");
	private static final String ebank_alias = PropertiesUtil.getValue("ebank_alias");
	private static final String ebank_appNo = PropertiesUtil.getValue("ebank_appNo");
	
	 // xml 根节点
    public static final String TRANSACTION = "TRANSACTION";
    // xml 请求body节点
    public static final String REQUEST = "REQUEST";
    // xml 响应body节点
    public static final String RESPONSE = "RESPONSE";
    // header节点
    public static final String HEADER = "HEADER";
	
    
    public String exchange(String reqContent, String url, String appno,String ip,String port){
    	 String remoteUrl = "http://" + ip + ":" + port + "/eai/" + "foreignPayment";
         String cerPath = "../TCLEbankIntegration/src/main/resources/auge.cer";
         String keyPath = "../TCLEbankIntegration/src/main/resources/jdh.keystore";
         
         HttpClientUtil client = new HttpClientUtil();
         String encrypted = "";
         try {
        	 //reqContent="<?xml version=\"1.0\" encoding=\"UTF-8\"?><TRANSACTION><HEADER><CHANNEL>EAI</CHANNEL><REQ_TYPE>payment</REQ_TYPE><APPNO>auge</APPNO><VERSION>2</VERSION><USER_NAME>xuxh</USER_NAME><REQ_IP>10.0.14.60</REQ_IP><REQ_INDEX>10.0.14.601537171937001</REQ_INDEX><REQ_TIME>180919041216</REQ_TIME></HEADER><REQUEST><PROVINCENAME>广东省</PROVINCENAME><RECORGNAME>工行惠州分行</RECORGNAME><VOUCHER_ID>Fk20180919161216T9zmq4ycKTKvT5w4</VOUCHER_ID><ADDRESS>10.0.14.60</ADDRESS><TRANAMT>731701.78</TRANAMT><TRANDATE>2022-06-06</TRANDATE><DBTYPE>mysql</DBTYPE><RECACCTNO>2008020119024239435</RECACCTNO><TRANTYPE>203</TRANTYPE><CHANNEL>EAI</CHANNEL><EXPLMODE>1</EXPLMODE><PAYMODE>1</PAYMODE><POSTSCRIPT /><APPROVALPROCESS>0</APPROVALPROCESS><PAYACCTNO>10101100103587010001</PAYACCTNO><RECBANKNAME>工商银行</RECBANKNAME><URGFLAG>0</URGFLAG><RECACCTNAME>惠州TCL房地产开发有限公司</RECACCTNAME><CITYNAME>惠州市</CITYNAME><DIGEST>其他</DIGEST></REQUEST></TRANSACTION>";
//        	reqContent="<?xml version=\"1.0\" encoding=\"UTF-8\"?><TRANSACTION><HEADER><REQ_TYPE>foreignPayment</REQ_TYPE><VERSION>2</VERSION><REQ_TIME>20170811173605</REQ_TIME><USER_NAME>zengql</USER_NAME><REQ_IP>10.0.14.47</REQ_IP><CUST_ID>zhanglq</CUST_ID><REQ_INDEX>10.0.14.471497605016247</REQ_INDEX><REQCODE>auge</REQCODE></HEADER><REQUEST><CHANNEL>EAI</CHANNEL><PAYACCTNO>1010011102100005</PAYACCTNO><TRANTYPE>201</TRANTYPE><PAYMODE>1</PAYMODE><TRANAMT>1.0</TRANAMT><DIGEST>gongchengkuan</DIGEST><PAYUSE>gongchengkuan</PAYUSE><RECACCTNO>4010011196100001</RECACCTNO><RECACCTINFO>TCL Corporate Research (Hong Kong) Co.,Limited</RECACCTINFO><DEALDATE>2017-04-20</DEALDATE><DEALTIME>08:09:14</DEALTIME><DBTYPE>ORACLE</DBTYPE><DBVERSION>9.2.0.4.0</DBVERSION><ADDRESS>10.0.14.66</ADDRESS><VOUCHER_ID>Fk20180919161216T9zmq4ycKTKvT5w1</VOUCHER_ID></REQUEST></TRANSACTION>";
			encrypted = EncryptionUtil.encryptData(reqContent, keyPath, ebank_password, ebank_alias, ebank_appNo, cerPath);
			System.out.println("加密后："+encrypted);
			} catch (Exception e) {
				e.printStackTrace();
			} // 使用企业私钥对报文签名
         
        JSONObject EncryptedJSON = JSON.parseObject(encrypted);
        client.setCharset("UTF-8");
        
        System.out.println("--remoteUrl --"+remoteUrl);
        boolean isConnected = client.callHttpPost(remoteUrl, "data=" + EncryptedJSON.getString("datacontent") + "&sign="
                + EncryptedJSON.getString("sign") + "&aeskey=" + EncryptedJSON.getString("aeskey") + "&appno="+appno);
        if (!isConnected) {
            return "连接出错！";
        }
        // 接收返回信息lk
        String retContent = client.getResContent();

        if (null == retContent || "".equals(retContent)) {
            return "";
        }
//        System.out.println(retContent);
        try {
            JSONObject retJson = JSON.parseObject(retContent);
            retContent = EncryptionUtil.decryptData(retJson.getString("datacontent"), retJson.getString("sign"), retJson.getString("aeskey"), keyPath, ebank_alias, ebank_password, cerPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("--retContent --"+retContent);
        return retContent;
    }
    
    
	public String parseJsonToDoc(JSONObject headerJson, JSONObject bodyJson){
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
	        
	        String content = xmlSElementUtil.xml2String(doc);
	        System.out.println("content:"+content);
	        return content;
	}
	
	public PIResponse reqForienMess(PIRequest request,String reqType){
		PIResponse response = new PIResponse();
		
		String[] headFields = REQ_HEADER_PARAM.split(",");
		System.out.println("REQ_HEADER_NECCESS:"+REQ_HEADER_PARAM);
		
		JSONObject jsobj = JSONObject.parseObject(JSONObject.toJSON(request).toString());
		Set<String> keys = jsobj.keySet();
		SimpleDateFormat reqForamt = new SimpleDateFormat("yyMMddhhmmss");
		Date reqDate = new Date();
		
		String localIP = xmlSElementUtil.getLocalIP();
		
		JSONObject header = new JSONObject();
		JSONObject body = new JSONObject();
		
		header.put("REQ_TYPE", reqType);
		header.put("REQ_TIME", reqForamt.format(reqDate));
		header.put("REQ_IP", localIP);
		header.put("REQ_TIME", reqForamt.format(reqDate));
		header.put("REQ_INDEX",localIP + System.currentTimeMillis());
		
		//handler headFields
		for(int i=0; i<headFields.length; i++){
			String oneField = headFields[i];
			String[] oneFieldSplit = oneField.split(":");
			header.put(oneFieldSplit[0], oneFieldSplit[1]);
		}
		
		System.out.println("------header----------"+header);
		//handler requestFields
		for (String oneField : keys) {  
		      System.out.println(oneField);  
		      String oneFieldValue = jsobj.getString(oneField);
		      body.put(oneField.toUpperCase(), oneFieldValue);
		}  
		System.out.println("------body----------"+body);
		String returnContent = parseJsonToDoc(header,body);
		String changeContent = exchange(returnContent,"foreignPayment",ebank_appno,ebank_IP,ebank_port);
		
		System.out.println("------changeContent----------"+changeContent);
		return response;
	}
	
	public static void main(String args[]){
		PIRequest request = new PIRequest();
//		request.setCHANNEL("EAI");
		request.setPAYACCTNO("10101100103587010001");
//		request.setCHANNEL("EAI");
		request.setTRANTYPE("201");
		request.setPAYMODE("1");
		request.setTRANAMT("1.0");
		request.setDIGEST("gongchengkuan");
//		request.setPAYUSE("gongchengkuan");
		request.setRECACCTNO("4010011196100001");
		request.setRECACCTINFO("TCL Corporate Research (Hong Kong) Co.,Limited");
//		request.setDEALDATE("2017-04-20");
//		request.setDEALTIME("08:09:14");
//		request.setDBTYPE("ORACLE");
//		request.setDBVERSION("9.2.0.4.0");
//		request.setADDRESS("10.0.14.66");
		request.setVOUCHER_ID("Fk20180919161216T9zmq4ycKTKvT3w9");
		
//		request.setAPPROVALPROCESS("0");
		new ToEbankPort().reqForienMess(request, "foreignPayment");
	}
	
}
