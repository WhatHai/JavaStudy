package com.tcl.jr.log.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tcl.jr.log.bean.WebLogExample;
import com.tcl.jr.log.mapper.WebLogMapper;

@Service("logService")
public class LogServiceImpl implements LogService{

	@Autowired
	private WebLogMapper webLogMapper;
 
	@Override
	public long getAllLog() {
		return webLogMapper.countByExample(new WebLogExample());
	}

}
