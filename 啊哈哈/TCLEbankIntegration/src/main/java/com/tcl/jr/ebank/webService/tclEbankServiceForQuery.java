package com.tcl.jr.ebank.webService;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import com.tcl.jr.ebank.webService.bean.PIRequestQuery;
import com.tcl.jr.ebank.webService.bean.PIResponseQuery;
import com.tcl.jr.ebank.webService.bean.QueryHeader;
import com.tcl.jr.ebank.webService.bean.QueryResponse;

@WebService
public class tclEbankServiceForQuery {

	@Resource
	private WebServiceContext wscontext;
	
	 public PIResponseQuery PIRESPONSEQUERY(PIRequestQuery REQUEST)
	  {
		 PIResponseQuery pq = new PIResponseQuery();
		 QueryResponse response = new QueryResponse();
		 response.setMESSAGE("returnSuccess...");
		 response.setINFOMSG("2234434");
		 response.setLATSTATTIME("abbccddef");
		 
		 pq.setRESPONSE(response);
		 return pq;
	  }
}
