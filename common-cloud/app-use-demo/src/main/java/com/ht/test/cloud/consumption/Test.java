/*
 * @Project Name: ht_common
 * @File Name: Test.java
 * @Package Name: com.ht.test.cloud.config.register
 * @Date: 2017-4-14下午4:36:00
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.cloud.consumption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @description TODO
 * @author bb.h
 * @date 2017-4-14下午4:36:00
 * @see
 */

@SpringBootApplication
@RestController
@ComponentScan("aaaa")
public class Test {
	 @Value("${name:a!}")
	    String bar;

	    @RequestMapping("/")
	    String hello() {
	        return "Hello " + bar + "!";
	    }
		  @Autowired
		  void setEnviroment(Environment env) {
		      System.out.println("foo" + env.getProperty("foo"));
		  }
	    public static void main(String[] args) {
	        SpringApplication.run(Test.class, args);
	    }
}
