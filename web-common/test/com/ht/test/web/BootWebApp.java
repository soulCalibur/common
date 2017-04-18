/*
 * @Project Name: zy-ht
 * @File Name: Application.java
 * @Package Name: com.ht.test
 * @Date: 2017-3-31下午3:07:37
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
/**
 * @description TODO
 * @author bb.h
 * @date 2017-3-31下午3:07:37
 * @see
 */
@EnableAutoConfiguration//boot功能
@Configuration//声明这个类是配置类
//@EnableWebMvc//开启对webMvc的支持 boot拦截器功能失效
@ComponentScan({"com.ht.test.web","com.ht.web.interceptor"})//(组件)的扫描,加载
@PropertySource(value="resources/spring-boot.properties")  
public class BootWebApp {
	 public static void main(String[] args) {  
	        SpringApplication.run(BootWebApp.class, args);  
	    }   
}

