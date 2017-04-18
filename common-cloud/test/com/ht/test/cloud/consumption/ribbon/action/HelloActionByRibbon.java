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

package com.ht.test.cloud.consumption.ribbon.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

/**
 * @description 消费服务接口
 * @author bb.h
 * @date 2017-4-14上午9:42:45
 * @see
 */
@RestController
public class HelloActionByRibbon {
	@Autowired
	private RestTemplate restTemplate;
	
	 @HystrixCommand(fallbackMethod = "helloActionByRibbonError")
    @RequestMapping(value = "/helloActionByRibbon")
    public String hi(@RequestParam(required=false) String name,@RequestParam(required=false) Integer sex,@RequestParam(required=false) String ext){
    	return restTemplate.getForObject("http://SERVER-ID/helloAction",String.class);
    }
	 public String helloActionByRibbonError(String name,Integer sex,String ext) {
	        return "服务不可用";
	}
}
