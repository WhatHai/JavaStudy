package com.tcl.jr.ebank.webService.bean;

public class PIRequestQuery {

	/**
	 * 结算流水号
	 */
	private String CMSTRANNO;
	/**
	 * 对接系统流水号
	 */
	private String VOUCHER_ID;
	
	public String getCMSTRANNO() {
		return CMSTRANNO;
	}
	public void setCMSTRANNO(String cMSTRANNO) {
		CMSTRANNO = cMSTRANNO;
	}
	public String getVOUCHER_ID() {
		return VOUCHER_ID;
	}
	public void setVOUCHER_ID(String vOUCHER_ID) {
		VOUCHER_ID = vOUCHER_ID;
	}
}
