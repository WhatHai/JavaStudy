package com.tcl.jr;

import org.junit.Test;

import com.tcl.jr.ebank.EbankErpDemo;

public class test {

	@Test
	public void TestFunction(){
		
	}
	
	public static void main(String args[]) throws Exception{
		System.out.println("JUnit test333");
		new EbankErpDemo().historyTranQuery();
	}
}
