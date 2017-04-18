/*
 * @Project Name: zy-ht
 * @File Name: BootCore.java
 * @Package Name: com.ht.test.web.core
 * @Date: 2017-3-31下午4:57:08
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.web.core;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-3-31下午4:57:08
 * @see
 */
@Component
public class BootCore extends WebMvcConfigurerAdapter implements ApplicationContextAware{
private ApplicationContext context;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		Map<String, HandlerInterceptor> data = context.getBeansOfType(HandlerInterceptor.class);
		for (Entry<String, HandlerInterceptor> temp : data.entrySet()) {
			registry.addInterceptor(temp.getValue()).addPathPatterns("/**");
			super.addInterceptors(registry);
		}
	
	}

	@Override
	public void setApplicationContext(ApplicationContext paramApplicationContext) throws BeansException {
		// TODO Auto-generated method stub
		this.context=paramApplicationContext;
	}
}
