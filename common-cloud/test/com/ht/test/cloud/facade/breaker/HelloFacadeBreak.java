/*
 * @Project Name: ht_common
 * @File Name: HelloFacadeBreak.java
 * @Package Name: com.ht.test.cloud.facade.breaker
 * @Date: 2017-4-14下午2:57:32
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.cloud.facade.breaker;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ht.test.cloud.facade.HelloFacadeByFeign;
import com.ht.test.cloud.server.mode.HelloMode;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-4-14下午2:57:32
 * @see
 */
@Component
public class HelloFacadeBreak implements HelloFacadeByFeign{

	@Override
	@RequestMapping(value = "/helloFacade", method = RequestMethod.GET)
	public HelloMode helloFacade(@RequestParam(required = false, value = "name") String name,@RequestParam(required = false, value = "sex") Integer sex) {
		return new HelloMode("helloFacade", "服务不可用");
	}
}
