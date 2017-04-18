/*
 * @Project Name: ht_common
 * @File Name: EX.java
 * @Package Name: com.ht.test.ice.server
 * @Date: 2017-4-12下午3:22:44
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.ice.server;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-4-12下午3:22:44
 * @see
 */
public class EX extends Ice.UnknownLocalException {
	/**
	 * serialVersionUID: TODO
	 */
	private static final long serialVersionUID = -26624848274089703L;
	
	
	
	
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return super.getMessage();
	}
	@Override
	public String getLocalizedMessage() {
		// TODO Auto-generated method stub
		return super.getLocalizedMessage();
	}
	@Override
	public synchronized Throwable getCause() {
		// TODO Auto-generated method stub
		return super.getCause();
	}

	private String msg;
	private String code;
	public EX(String msg, String code) {
		super();
		this.msg = msg;
		this.code = code;
		this.unknown = msg;
	}
	@Override
	public String toString() {
		return "哈哈哈哈哈哈哈";
	}
	
	
}
