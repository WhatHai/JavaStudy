package com.tcl.jr.ebank.webService.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesUtil {

	private static Properties properties;

	static {
		try {
			properties = PropertiesLoaderUtils.loadAllProperties("Web_Field.properties");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getValue(String key) {
		String value = properties.getProperty(key);
		return value;
	}

	public static Properties getProperties() {
		return properties;
	}

	public static void main(String args[]){
		PropertiesUtil pu = new PropertiesUtil();
		String param = properties.get("REQ_HEADER_NECCESSARY").toString();
		System.out.println("param==="+param);
	}
}
