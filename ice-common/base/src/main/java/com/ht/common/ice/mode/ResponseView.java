/*
 * @Project Name: ht
 * @File Name: ResponseView.java
 * @Package Name: com.ht.commons.ice.mode
 * @Date: 2017-3-29下午5:21:02
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.common.ice.mode;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-3-29下午5:21:02
 * @see
 */
public class ResponseView {
	
	private ResponseStatus responseStatus;
	private OutType outType;
	private Object data;
	
	public ResponseView(ResponseStatus responseStatus, OutType outType, Object data) {
		super();
		this.responseStatus = responseStatus;
		this.outType = outType;
		this.data = data;
	}

	public OutType getOutType() {
		return outType;
	}

	
	public Object getData() {
		return data;
	}

	public ResponseStatus getResponseStatus() {
		return responseStatus;
	}
}
