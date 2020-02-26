package com.tcl.jr.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public class logControl {

	@Autowired
	private LogService logService;
 
	@RequestMapping("/logService")
	@ResponseBody
	public String getUser() {
		long logCount = logService.getAllLog();
		return "count:"+logCount;
	}
}
