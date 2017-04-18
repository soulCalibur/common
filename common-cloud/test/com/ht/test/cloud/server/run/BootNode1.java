/*
 * @Project Name: ht_common
 * @File Name: BootNode1.java
 * @Package Name: com.ht.test.cloud.server
 * @Date: 2017-4-13下午6:05:19
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.cloud.server.run;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-4-13下午6:05:19
 * @see
 */
@SpringBootApplication
@EnableEurekaClient
@ComponentScan("com.ht.test.cloud.server.mini.action")
@PropertySource(value=
		{
		 "register.yml"
		,"com/ht/test/cloud/server/run/node1.properties"
		})  
public class BootNode1 {
	 public static void main(String[] args) {
	        SpringApplication.run(BootNode1.class, args);
	 }
}
