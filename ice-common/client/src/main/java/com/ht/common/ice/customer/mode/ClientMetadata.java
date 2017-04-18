/**
 * File Name:ClientMada.java
 * Package Name:com.ht.common.ice.customer.mode
 * Date:2017-4-4下午3:00:17
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ht.common.ice.customer.mode;

import java.lang.reflect.Field;

import Ice.Object;

/**
 * ClassName:ClientMada <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2017-4-4 下午3:00:17 <br/>
 * @author   Administrator
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public  class  ClientMetadata{
	private Ice.Object bean;
	private String metadataName;
	private Field filed;
	public ClientMetadata(Object bean, Field filed) {
		super();
		this.bean = bean;
		this.filed = filed;
	}
	public Ice.Object getBean() {
		return bean;
	}
	public Field getFiled() {
		return filed;
	}
	public String getMetadataName() {
		return metadataName;
	}
	public void setMetadataName(String metadataName) {
		this.metadataName = metadataName;
	}
	
	
}

