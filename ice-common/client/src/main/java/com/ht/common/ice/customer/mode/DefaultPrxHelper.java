/**
 * File Name:DefaultPrxHelper.java
 * Package Name:com.ht.common.ice.customer.mode
 * Date:2017-4-4下午6:48:57
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ht.common.ice.customer.mode;
/**
 * ClassName:DefaultPrxHelper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2017-4-4 下午6:48:57 <br/>
 * @author   Administrator
 * @version  
 * @since    JDK 1.6
 * @see 	 
 */
public class DefaultPrxHelper extends Ice.ObjectPrxHelperBase{
	/**
	 * serialVersionUID: TODO
	 */
	private static final long serialVersionUID = 1426786089035411489L;
private String endpoint;

public String getEndpoint() {
	return endpoint;
}

public void setEndpoint(String endpoint) {
	this.endpoint = endpoint;
}

}

