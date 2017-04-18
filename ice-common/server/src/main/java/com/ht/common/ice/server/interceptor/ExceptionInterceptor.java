/*
 * @Project Name: mytemp
 * @File Name: ExceptionInterceptor.java
 * @Package Name: com.bbh.commons.ice.server
 * @Date: 2017-3-28下午6:41:02
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.common.ice.server.interceptor;

import java.util.List;

import com.ht.common.ice.mode.ResponseView;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-3-28下午6:41:02
 * @see
 */
public interface ExceptionInterceptor {
	/**
	 * @description TODO
	 * @date 2017-3-30上午10:34:41
	 * @author bb.h
	 * @since 1.0.0 
	 * @param serverName
	 * @param method
	 * @param e
	 * @return
	 */
	public ResponseView responseDebug(String serverName, String method, Exception e);
	/**
	 * @description TODO
	 * @date 2017-3-30上午10:34:39
	 * @author bb.h
	 * @since 1.0.0 
	 * @param e
	 * @return
	 */
	public ResponseView response(Exception e);
	
	/**
	 * @description 劫持异常类型
	 * @date 2017-3-30上午10:37:53
	 * @author bb.h
	 * @since 1.0.0 
	 * @return
	 */
	public List<Class<? extends Exception>> executionClass();
	
	/**
	 * @description TODO
	 * @date 2017-3-30上午10:34:34
	 * @author bb.h
	 * @since 1.0.0 
	 * @return
	 */
	public boolean isDebug();
	/**
	 * @description TODO
	 * @date 2017-3-30上午10:34:29
	 * @author bb.h
	 * @since 1.0.0 
	 * @return 正则表达式
	 */
	public String executionServerName();
	/**
	 * @description TODO
	 * @date 2017-3-30上午10:34:32
	 * @author bb.h
	 * @since 1.0.0 
	 * @return 正则表达式
	 */
	public String executionMethod();
}
