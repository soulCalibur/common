/*
 * Project Name: ice-example
 * File Name: ClientExample.java
 * Package Name: com.hhly.common.components.ice.example
 * Date: 2016年11月29日下午5:20:59
 * Creator: shenxiaoping
 * ------------------------------
 * 修改人: 
 * 修改时间: 
 * 修改内容: 
 */

package com.ht.test.ice.client;

import org.springframework.stereotype.Component;

import com.ht.common.ice.customer.annotation.Endpoints;
import com.ht.test.facade.OrderFacade;

/**
 * @description 
 * @author Allen Shen
 * @date 2016年11月29日下午5:20:59
 * @see
 */
@Component
public class ClientExampleImpl{
	
	@Endpoints(system="order")
	private OrderFacade orderaaaaaaaaaaa;
	public String successTest(){
		return orderaaaaaaaaaaa.doCheckData(888, "9527");
	}
	public String errorTest(){
		return orderaaaaaaaaaaa.doCheckData(8888, "参数不是数字错误码");
	}
	public String test(int num){
		return orderaaaaaaaaaaa.doCheckData(888, num+"");
	}
}
