package com.tcl.jr.ebank.webService.util;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.MessageContext;
import org.apache.axis.transport.http.HTTPConstants;
import org.jdom2.Document;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.alibaba.fastjson.JSONObject;

public class xmlSElementUtil {
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
    
    public xmlSElementUtil(){
    	
    }
    
    /**
     * xml to string
     *
     * @param doc
     * @return
     */
    public static String xml2String(Document doc) {
        Format format = Format.getCompactFormat();
        format.setEncoding("UTF-8");
        XMLOutputter xmlout = new XMLOutputter(format);
        return xmlout.outputString(doc);
    }
    
    public static String getRemoteIP()
    {
    	String remoteAddr = "";
    	try{
		      MessageContext mc = MessageContext.getCurrentContext();
		      HttpServletRequest request = (HttpServletRequest)mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
		      System.out.println("remote  ip:  " + request.getRemoteAddr());
		      remoteAddr = request.getRemoteAddr();
	      }catch(Exception e){
	    	  e.printStackTrace();
	      }
      return remoteAddr;
    }
    
    public static String getLocalIP()
    {
	      String currAddr = "";
	      try{
	    	  MessageContext mc = MessageContext.getCurrentContext();
		      HttpServletRequest request = (HttpServletRequest)mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
		      currAddr = request.getLocalAddr();
		      System.out.println("currAddr  ip:  " +currAddr);
	      }catch(Exception e){
	    	  e.printStackTrace();
	      }
      return currAddr;
    }
    
    public String parseStr(){
        JSONObject header = new JSONObject();
        JSONObject body = new JSONObject();
        Date reqDate = new Date();
        SimpleDateFormat reqForamt = new SimpleDateFormat("yyMMddhhmmss");
        header.put(REQTYPE, "foreignPayment");
        header.put(REQTIME, reqForamt.format(reqDate));
        header.put(REQIP, "127.0.0.1");
        header.put(REQINDEX, System.currentTimeMillis());
        
        JSONObject str = JSONObject.parseObject(JSONObject.toJSON(header).toString());
        String parsString = str.toJSONString();
        System.out.println("parsString:  " + parsString);
        return parsString;
    }
    
    public static void main(String args[]){
    	new xmlSElementUtil().getLocalIP();
    }
}
