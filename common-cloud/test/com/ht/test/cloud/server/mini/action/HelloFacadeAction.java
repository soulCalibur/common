/*
 * @Project Name: ht_common
 * @File Name: HelloFacadeAction.java
 * @Package Name: com.ht.test.cloud.server.action
 * @Date: 2017-4-14上午9:30:52
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.cloud.server.mini.action;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ht.test.cloud.facade.HelloFacadeByFeign;
import com.ht.test.cloud.server.mode.HelloMode;
import com.ht.test.cloud.server.mode.NodeGet;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-4-14上午9:30:52
 * @see
 */
@RestController
public class HelloFacadeAction implements HelloFacadeByFeign{

	@Override
	@RequestMapping(value = "/helloFacade", method = RequestMethod.GET)
	public HelloMode helloFacade(@RequestParam(required = false) String name,
			@RequestParam(required = false) Integer sex) {
		 System.out.println("访问节点:==>>"+NodeGet.NODEID);
	        return new HelloMode("查询参数条件:[name:"+name+"]","[sex:"+sex+"]");
	}

	
}
