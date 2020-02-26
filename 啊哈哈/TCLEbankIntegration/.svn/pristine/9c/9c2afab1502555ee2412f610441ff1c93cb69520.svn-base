package com.tcl.jr.ebank.webService.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PIRequest implements java.io.Serializable{
	/**
	 * （流水号SAP公司++凭证号+行项目+时间戳）
	 * 调用方系统的业务流水号
	 */
	@XmlElement(nillable = false, required = false)
	String VOUCHER_ID;
	/**
	 * 付款账号
	 *（通讯在资金池开立的内部银行帐户）
	 */
	@XmlElement(nillable = true, required = false)
	String PAYACCTNO;
	/**
	 * (Concur AP付款：对外支付,业务由用户决定，在推送AP付款之前，需要有屏幕让用户选取)
	 * 201-内部转账,203-对外支付
	 */
	@XmlElement(nillable = false, required = true)
	String TRANTYPE; 
	/**
	 * 境外结算方式：“1”-电汇,“2”-参与行转账, “3”-本地行转账
	 */
	@XmlElement(required = true)
	String PAYMODE;
	/**
	 * 转账金额
	 */
	String TRANAMT;
	//用途
	String DIGEST;
	//收款人账号
	String RECACCTNO;
	//收款行SWIFT CODE
	String RECBANKSWCD;
	/**
	 * 收款人名称及地址
	 *（收款人名称+地址放在一起）
	 */
	String RECACCTINFO;
	//收款人名称
	String RECACCTNAME;
	//收款人地址
	String RECADDR;
	//收款行名称
	String RECBANKNAME;
	//收款行地址
	String RECBANKADDR;
	//收款行国家/地区
	String RECBKSTATE;
	//收款行国家/地区
	String RECBKSTCWRDS;
	//代理行账号
	String AGTBKACCTNO;
	//代理行Swift Code
	String AGTBKSWCD;
	//代理行名称
	String AGTBKNAME;
	//代理行地址
	String AGTBKADDR;
	//代理行国家代码
	String AGTSTATECODE;
	//代理行国家
	String AGTSTATE;
	// 对银行指示
	String TOBANKINFO;
	//费用承担方
	String CHARGEBEARS;
	//收款银行编号
	String RECBANKID;
	
	
	/*public String getTRANDATE() {
		return TRANDATE;
	}
	public void setTRANDATE(String tRANDATE) {
		TRANDATE = tRANDATE;
	}
	public String getDBTYPE() {
		return DBTYPE;
	}
	public void setDBTYPE(String dBTYPE) {
		DBTYPE = dBTYPE;
	}
	public String getADDRESS() {
		return ADDRESS;
	}
	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}
	public String getAPPROVALPROCESS() {
		return APPROVALPROCESS;
	}
	public void setAPPROVALPROCESS(String aPPROVALPROCESS) {
		APPROVALPROCESS = aPPROVALPROCESS;
	}
	public String getCITYNAME() {
		return CITYNAME;
	}
	public void setCITYNAME(String cITYNAME) {
		CITYNAME = cITYNAME;
	}
	public String getURGFLAG() {
		return URGFLAG;
	}
	public void setURGFLAG(String uRGFLAG) {
		URGFLAG = uRGFLAG;
	}
	public String getRECORGNAME() {
		return RECORGNAME;
	}
	public void setRECORGNAME(String rECORGNAME) {
		RECORGNAME = rECORGNAME;
	}
	public String getPROVINCENAME() {
		return PROVINCENAME;
	}
	public void setPROVINCENAME(String pROVINCENAME) {
		PROVINCENAME = pROVINCENAME;
	}
	public String getEXPLMODE() {
		return EXPLMODE;
	}
	public void setEXPLMODE(String eXPLMODE) {
		EXPLMODE = eXPLMODE;
	}*/
	public String getCHANNEL() {
		return CHANNEL;
	}
	public void setCHANNEL(String cHANNEL) {
		CHANNEL = cHANNEL;
	}
	/*//待确认
	String DBTYPE;
	//待确认
	String ADDRESS;
	//待确认
	String APPROVALPROCESS;
	//待确认
	String CITYNAME;
	//待确认
	String URGFLAG;
	//待确认
	String RECORGNAME;
	//待确认
	String PROVINCENAME;
	//待确认
	String EXPLMODE;
	//待确认
	String TRANDATE;
	*/	
	//待确认
	String CHANNEL;
	
	public String getVOUCHER_ID() {
		return VOUCHER_ID;
	}
	public void setVOUCHER_ID(String vOUCHER_ID) {
		VOUCHER_ID = vOUCHER_ID;
	}
	
	public String getPAYACCTNO() {
		return PAYACCTNO;
	}
	public void setPAYACCTNO(String pAYACCTNO) {
		PAYACCTNO = pAYACCTNO;
	}
	public String getTRANTYPE() {
		return TRANTYPE;
	}
	public void setTRANTYPE(String tRANTYPE) {
		TRANTYPE = tRANTYPE;
	}
	public String getPAYMODE() {
		return PAYMODE;
	}
	public void setPAYMODE(String pAYMODE) {
		PAYMODE = pAYMODE;
	}
	public String getTRANAMT() {
		return TRANAMT;
	}
	public void setTRANAMT(String tRANAMT) {
		TRANAMT = tRANAMT;
	}
	public String getDIGEST() {
		return DIGEST;
	}
	public void setDIGEST(String dIGEST) {
		DIGEST = dIGEST;
	}
	public String getRECACCTNO() {
		return RECACCTNO;
	}
	public void setRECACCTNO(String rECACCTNO) {
		RECACCTNO = rECACCTNO;
	}
	public String getRECBANKSWCD() {
		return RECBANKSWCD;
	}
	public void setRECBANKSWCD(String rECBANKSWCD) {
		RECBANKSWCD = rECBANKSWCD;
	}
	public String getRECACCTINFO() {
		return RECACCTINFO;
	}
	public void setRECACCTINFO(String rECACCTINFO) {
		RECACCTINFO = rECACCTINFO;
	}
	public String getRECACCTNAME() {
		return RECACCTNAME;
	}
	public void setRECACCTNAME(String rECACCTNAME) {
		RECACCTNAME = rECACCTNAME;
	}
	public String getRECADDR() {
		return RECADDR;
	}
	public void setRECADDR(String rECADDR) {
		RECADDR = rECADDR;
	}
	public String getRECBANKNAME() {
		return RECBANKNAME;
	}
	public void setRECBANKNAME(String rECBANKNAME) {
		RECBANKNAME = rECBANKNAME;
	}
	public String getRECBANKADDR() {
		return RECBANKADDR;
	}
	public void setRECBANKADDR(String rECBANKADDR) {
		RECBANKADDR = rECBANKADDR;
	}
	public String getRECBKSTATE() {
		return RECBKSTATE;
	}
	public void setRECBKSTATE(String rECBKSTATE) {
		RECBKSTATE = rECBKSTATE;
	}
	public String getRECBKSTCWRDS() {
		return RECBKSTCWRDS;
	}
	public void setRECBKSTCWRDS(String rECBKSTCWRDS) {
		RECBKSTCWRDS = rECBKSTCWRDS;
	}
	public String getAGTBKACCTNO() {
		return AGTBKACCTNO;
	}
	public void setAGTBKACCTNO(String aGTBKACCTNO) {
		AGTBKACCTNO = aGTBKACCTNO;
	}
	public String getAGTBKSWCD() {
		return AGTBKSWCD;
	}
	public void setAGTBKSWCD(String aGTBKSWCD) {
		AGTBKSWCD = aGTBKSWCD;
	}
	public String getAGTBKNAME() {
		return AGTBKNAME;
	}
	public void setAGTBKNAME(String aGTBKNAME) {
		AGTBKNAME = aGTBKNAME;
	}
	public String getAGTBKADDR() {
		return AGTBKADDR;
	}
	public void setAGTBKADDR(String aGTBKADDR) {
		AGTBKADDR = aGTBKADDR;
	}
	public String getAGTSTATECODE() {
		return AGTSTATECODE;
	}
	public void setAGTSTATECODE(String aGTSTATECODE) {
		AGTSTATECODE = aGTSTATECODE;
	}
	public String getAGTSTATE() {
		return AGTSTATE;
	}
	public void setAGTSTATE(String aGTSTATE) {
		AGTSTATE = aGTSTATE;
	}
	public String getTOBANKINFO() {
		return TOBANKINFO;
	}
	public void setTOBANKINFO(String tOBANKINFO) {
		TOBANKINFO = tOBANKINFO;
	}
	public String getCHARGEBEARS() {
		return CHARGEBEARS;
	}
	public void setCHARGEBEARS(String cHARGEBEARS) {
		CHARGEBEARS = cHARGEBEARS;
	}
	public String getRECBANKID() {
		return RECBANKID;
	}
	public void setRECBANKID(String rECBANKID) {
		RECBANKID = rECBANKID;
	}
	public String getPARTNERBKBRNO() {
		return PARTNERBKBRNO;
	}
	public void setPARTNERBKBRNO(String pARTNERBKBRNO) {
		PARTNERBKBRNO = pARTNERBKBRNO;
	}
	//收款银行分支行编号
	String PARTNERBKBRNO;
}
