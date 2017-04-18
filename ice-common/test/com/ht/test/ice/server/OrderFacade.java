/*
 * Project Name: ice-example
 * File Name: OrderImpl.java
 * Package Name: com.hhly.common.components.ice.example.server
 * Date: 2016年12月6日下午6:53:26
 * Creator: shenxiaoping
 * ------------------------------
 * 修改人: 
 * 修改时间: 
 * 修改内容: 
 */

package com.ht.test.ice.server;

import org.springframework.stereotype.Component;

import Ice.Current;

import com.ht.test.facade._OrderFacadeDisp;

/**
 * @description 
 * @author Allen Shen
 * @date 2016年12月6日下午6:53:26
 * @see
 */
@Component
public class OrderFacade extends _OrderFacadeDisp{

	/**
	 * serialVersionUID: TODO
	 */
	private static final long serialVersionUID = 3787647670221287118L;

	@Override
	public String doCheckData(int orderId, String code, Current __current) {
		int num = Integer.parseInt(code);
		if(num<10){
			throw new EX("111111111111111","aa");
		}
		return "当前订单:["+orderId+"],代码:["+num+"]";
	}


	
}
