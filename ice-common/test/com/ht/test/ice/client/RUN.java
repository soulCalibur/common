/*
 * @Project Name: mytemp
 * @File Name: RUN.java
 * @Package Name: com.hhly.common.components.ice.example.client
 * @Date: 2017-3-28下午4:55:24
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.ice.client;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-3-28下午4:55:24
 * @see
 */
@Configuration//声明这个类是配置类
@ComponentScan({"com.ht.test.ice.client","com.ht.common.ice.customer"})//(组件)的扫描,加载
public class RUN {
	static ApplicationContext context;
		public static void main(String[] args){
			 try {
				 context = new AnnotationConfigApplicationContext(RUN.class);
				 ClientExampleImpl data = context.getBean(ClientExampleImpl.class);
				 String aaa=data.test(2);
				// String aaa=data.successTest();
				//String aaa=data.errorTest();
				System.out.println(aaa);
				System.exit(0);
			} catch (BeansException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 System.exit(0);
		}
}
