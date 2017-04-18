/*
 * @Project Name: mytemp
 * @File Name: Asdasd.java
 * @Package Name: com.bbh.commons.ice.exception.imp
 * @Date: 2017-3-29上午11:52:03
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.ice.server;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ht.common.ice.mode.OutType;
import com.ht.common.ice.mode.ResponseStatus;
import com.ht.common.ice.mode.ResponseView;
import com.ht.common.ice.server.interceptor.ExceptionInterceptor;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-3-29上午11:52:03
 * @see
 */
//@Component
public class IceExceptionImpl implements ExceptionInterceptor{
	private final static Logger LOG = LoggerFactory.getLogger(IceExceptionImpl.class);
	@Override
	public ResponseView responseDebug(String serverName, String method, Exception e) {
		LOG.info("异常:"+serverName+"=="+method);
		return new ResponseView(ResponseStatus.OK, OutType.STRING, "--------------------");
	}
	@Override
	public ResponseView response(Exception e) {
		return new ResponseView(ResponseStatus.OK, OutType.STRING, "--------------------");
	}
	@Override
	public boolean isDebug() {
		return false;
	}
	@Override
	public String executionServerName() {
		return "^[A-Za-z]+$";
	}
	@Override
	public String executionMethod() {
		return "^[A-Za-z]+$";
	}
	@Override
	public List<Class<? extends Exception>> executionClass() {
		List<Class<? extends Exception>> data=new ArrayList<>();
		data.add(Exception.class);
		return data; 
	}
}
