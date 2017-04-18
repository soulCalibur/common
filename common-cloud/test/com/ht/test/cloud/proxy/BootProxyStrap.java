/*
 * @Project Name: ht_common
 * @File Name: BootProxyStrap.java
 * @Package Name: com.ht.test.cloud.proxy
 * @Date: 2017-4-14下午3:14:58
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.cloud.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-4-14下午3:14:58
 * @see
 */
@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
@ComponentScan({"com.ht.test.cloud.proxy.filter"})
@PropertySource(value=
{ "register.yml","com/ht/test/cloud/proxy/proxy.properties"})  

public class BootProxyStrap {
	  public static void main(String[] args) {
	        SpringApplication.run(BootProxyStrap.class, args);
	    }
}
