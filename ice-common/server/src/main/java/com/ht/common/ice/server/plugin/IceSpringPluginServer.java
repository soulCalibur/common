/*
 * @Project Name: mytemp
 * @File Name: IceSpringPluginServer.java
 * @Package Name: com.bbh.commons.ice.server
 * @Date: 2017-3-29上午9:44:46
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.common.ice.server.plugin;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import Ice.Object;

import com.ht.common.ice.server.interceptor.ExceptionInterceptor;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-3-29上午9:44:46
 * @see
 */
@Configuration
//@Component
@ComponentScan({"com.ht.common.components.ice.server.loader"})
public class IceSpringPluginServer implements InitializingBean, ApplicationContextAware{
	private final static Logger LOG = LoggerFactory.getLogger(IceSpringPluginServer.class);
	private static IceSpringPluginServer self;
	private ApplicationContext context;
	private String config;
	public void installBean() {
		AdapterFactory.load(config);
		boolean userPlugin=false;
		// 装载ice异常拦截器
		Map<String, ExceptionInterceptor> interceptors = context.getBeansOfType(ExceptionInterceptor.class);
		if (interceptors != null && !interceptors.isEmpty()) {
			userPlugin=true;
			Collection<ExceptionInterceptor> temp = interceptors.values();
			for (ExceptionInterceptor bean : temp) {
				AdapterFactory.addExInterceptor(bean);
			}
		}
		// 装载ice服务
		Map<String, Object> iceServers = context.getBeansOfType(Object.class);
		if (iceServers == null || iceServers.isEmpty()) {
			LOG.info("********** no found ice server ************");
			AdapterFactory.closeAdapter();
		} else {
			for (Entry<String, Object> bean : iceServers.entrySet()) {
				if(userPlugin){
					AdapterFactory.registryServerWithPlugin(bean.getKey(), bean.getValue());
				}else {
					AdapterFactory.registryServer(bean.getKey(), bean.getValue());
				}
			}
			AdapterFactory.startAdapter();
		}
	}
	
	public static IceSpringPluginServer install(ApplicationContext context){
		if(self==null){
			self=new IceSpringPluginServer();
			self.setContext(context);
			self.installBean();
		}
		return self;
	}
	private void setContext(ApplicationContext context) {
		this.context=context;
	}
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		setContext(context);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		installBean();
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

}
