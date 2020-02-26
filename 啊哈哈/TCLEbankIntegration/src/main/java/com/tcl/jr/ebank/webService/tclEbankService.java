package com.tcl.jr.ebank.webService;

import com.alibaba.fastjson.JSONObject;
import com.tcl.jr.ebank.webService.bean.HEADER;
import com.tcl.jr.ebank.webService.bean.PIRequest;
import com.tcl.jr.ebank.webService.bean.PIResponse;
import com.tcl.jr.ebank.webService.bean.RequestDom;
import com.tcl.jr.ebank.webService.bean.ResponseDom;
import com.tcl.jr.ebank.webService.bean.TransactionRequest;
import com.tcl.jr.ebank.webService.bean.TransactionResponse;
import com.tcl.jr.ebank.webService.service.ToEbankPort;
import com.tcl.jr.ebank.webService.util.xmlSElementUtil;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.jdom2.Document;
import org.jdom2.Element;


@WebService
public class tclEbankService {

  @Resource
  private WebServiceContext wscontext;

 /* public String getIP()
  {
    MessageContext mc = MessageContext.getCurrentContext();
    HttpServletRequest request = (HttpServletRequest)mc.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
    System.out.println("remote  ip:  " + request.getRemoteAddr());
    return request.getRemoteAddr();
  }*/

  public tclEbankService(){
	  
  }
  
  public PIResponse PUSHDATATOWEBSYS(PIRequest request)
  {
	
      String cctno = request.getAGTBKACCTNO();
      //Document doc = new Document(firstNode);
	  PIResponse response = new PIResponse();
	  
	  response = new ToEbankPort().reqForienMess(request,"payMent");
	  response.setCMSTRANNO("FK17060500000402");
	  response.setMESSAGE("Return Success.");
	  String ip = new xmlSElementUtil().getRemoteIP();
	  System.out.println("cctno:"+cctno);
	  System.out.println("Client IP:"+ip);
	
	  return response;
  }
  
  public static void main(String args[]){
	//  new ToEbankPort().reqForienMess();
      
	  PIRequest piq = new PIRequest();
	  HEADER header = new HEADER();
	  header.setCUST_ID("33333");
	  piq.setAGTBKACCTNO("334343244");
	  piq.setAGTBKADDR("ddff");
	 
	  new tclEbankService().PUSHDATATOWEBSYS(piq);
	  
  }
}