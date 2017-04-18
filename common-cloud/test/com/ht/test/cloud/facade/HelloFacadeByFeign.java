/*
 * @Project Name: ht_common
 * @File Name: HelloFacade.java
 * @Package Name: com.ht.test.cloud.server.facade
 * @Date: 2017-4-14上午9:30:15
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.cloud.facade;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ht.test.cloud.facade.breaker.HelloFacadeBreak;
import com.ht.test.cloud.server.mode.HelloMode;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-4-14上午9:30:15
 * @see
 */
@FeignClient(value = "SERVER-ID",fallback = HelloFacadeBreak.class)
public interface HelloFacadeByFeign {
	 @RequestMapping(value = "/helloFacade",method = RequestMethod.GET)
	 public	HelloMode helloFacade(@RequestParam(required=false,value="name") String name,@RequestParam(required=false,value="sex") Integer sex);
}
