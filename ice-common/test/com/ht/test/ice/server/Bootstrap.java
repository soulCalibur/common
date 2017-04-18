/*
 * Project Name: ice-example
 * File Name: Main.java
 * Package Name: com.hhly.common.components.ice.example
 * Date: 2016年11月29日下午5:21:08
 * Creator: shenxiaoping
 * ------------------------------
 * 修改人: 
 * 修改时间: 
 * 修改内容: 
 */

package com.ht.test.ice.server;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.ht.common.ice.server.plugin.IceSpringPluginServer;


/**
 * @description
 * @author Allen Shen
 * @date 2016年11月29日下午5:21:08
 * @see
 */
//@EnableAutoConfiguration//boot功能
@Configuration//声明这个类是配置类
//@EnableWebMvc//开启对webMvc的支持 boot拦截器功能失效
@ComponentScan({"com.ht.test.ice.server","com.ht.common.ice.server.plugin"})//(组件)的扫描,加载
//@PropertySource(value="env/ice-server.properties")  
public class Bootstrap {
	
	@Bean
	public IceSpringPluginServer iceSpringPluginServer(){
		IceSpringPluginServer server=new IceSpringPluginServer();
		server.setConfig("env/ice-server.properties");
		return server;
	}
	public static void main(String[] args) throws IOException {
		ApplicationContext context = new AnnotationConfigApplicationContext(Bootstrap.class);
		System.out.println("服务启动static成功!");
		System.in.read();
	}
}
