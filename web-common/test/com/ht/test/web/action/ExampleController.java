/*
 * @Project Name: zy-ht
 * @File Name: GreetingController.java
 * @Package Name: com.ht.test.web.action
 * @Date: 2017-3-31下午3:42:59
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.web.action;

import javax.validation.Valid;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-3-31下午3:42:59
 * @see
 */
@RestController
public class ExampleController {
	 @RequestMapping("/hello")
	 @Valid
	    String hello(@RequestParam(value="name",required=false)  @NotEmpty(message = "地址不能为空") String name) {
	     System.out.println("1=============>>>ExampleController");
		 return "Hello, " + name + "!";
	    }
}
