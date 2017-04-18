/*
 * @Project Name: ht_common
 * @File Name: Action.java
 * @Package Name: com.ht.test.cloud.consumption.ribbon
 * @Date: 2017-4-14上午9:42:45
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.cloud.consumption.feign.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ht.test.cloud.facade.HelloFacadeByFeign;
import com.ht.test.cloud.server.mode.HelloMode;
import com.ht.test.cloud.server.mode.NodeGet;

/**
 * @description 消费服务接口
 * @author bb.h
 * @date 2017-4-14上午9:42:45
 * @see
 */
@RestController
public class HelloActionByFeign {

	@Autowired
	private HelloFacadeByFeign facadeByFeign;
	
	
	
    @RequestMapping(value = "/helloActionByFeign")
    public String test(@RequestParam(required=false) String name,@RequestParam(required=false) Integer sex,@RequestParam(required=false) String ext){
    	HelloMode resultDemoFeign = facadeByFeign.helloFacade(name, sex);
    	if(resultDemoFeign==null){
    		resultDemoFeign=new HelloMode("默认", "默认");
    	}
    	String res=resultDemoFeign.getName()+"<[#]>"+resultDemoFeign.getAge()+"来自节点==>>"+NodeGet.NODEID;
		return res;
    }
}
