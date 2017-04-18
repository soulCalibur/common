/*
 * Project Name: cmp-ice
 * File Name: ServerHelper.java
 * Package Name: com.hhly.common.components.ice.provider.loader
 * Date: 2016年11月26日下午6:51:52
 * Creator: shenxiaoping
 * ------------------------------
 * 修改人: 
 * 修改时间: 
 * 修改内容: 
 */

package com.ht.common.ice.server.plugin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ice.Communicator;
import Ice.DispatchStatus;
import Ice.Identity;
import Ice.InitializationData;
import Ice.Object;
import Ice.ObjectAdapter;
import Ice.Request;
import Ice.Util;
import IceInternal.Incoming;

import com.ht.common.ice.mode.OutType;
import com.ht.common.ice.mode.ResponseStatus;
import com.ht.common.ice.mode.ResponseView;
import com.ht.common.ice.server.interceptor.ExceptionInterceptor;
import com.ht.common.ice.util.ExecutionUtil;
import com.ht.common.ice.util.ICEUtil;
import com.ht.common.ice.util.PropertiesLoadUtil;

/**
 * @description 
 * @author Allen Shen
 * @date 2016年11月26日下午6:51:52
 * @see
 */
public class AdapterFactory {
	private final static Logger LOG = LoggerFactory.getLogger(AdapterFactory.class);
	private static CoreDispatch coreDispatch;
	/**
	 * 服务注册端点
	 */
	private static ObjectAdapter adapter;
	private static Communicator ic;
	/**
	 * @description 发布ice server
	 * @date 2016年11月28日上午11:19:07
	 * @author shenxiaoping
	 * @since 1.0.0 
	 * @param serviceName
	 * @param serverBean
	 * @return
	 */
	
	public static void load(String config){
		if(adapter == null){
			PropertiesLoadUtil.load(config);
			String endpoints=PropertiesLoadUtil.getString(config, "ice.servers.endpoints");
			Map<String, String> args=new HashMap<>();
			ic = getCommunicator(args);
			adapter = ic.createObjectAdapterWithEndpoints(ICEUtil.getAdapterRandomName(),endpoints);
			coreDispatch = new CoreDispatch();
		}
	}
	public static void  closeAdapter(){
		if(adapter!=null&&adapter.isDeactivated()){
			adapter.destroy();
		}
		if(ic!=null&&!ic.isShutdown()){
			ic.shutdown();
			ic.destroy();
		}
		LOG.info("=============has stop adapter===============");
	}
	public static void  startAdapter(){
		adapter.activate();
		new ICEThread().start();
		LOG.info("=============has start [{}] {} adapter ===============",adapter.getName(),adapter.getPublishedEndpoints());
	}

	public static Communicator getCommunicator(Map<String, String> args) {
		InitializationData initData = new InitializationData();
		initData.properties = Util.createProperties();
		if (args != null && !args.isEmpty()) {
			for (Entry<String, String> temp : args.entrySet()) {
				initData.properties.setProperty(temp.getKey(), temp.getValue());
			}
		}
		return Ice.Util.initialize(initData);
	}
	
	
	static class ICEThread extends Thread{
		
		@Override
		public void run() {
			try {
				ic.waitForShutdown();
				LOG.info("has shutdown ice adapter successfully!");
			} catch (Exception e) {
				LOG.error("********* has shutdwon ice adapter ************",e);
			}
		}
		
	}

	public static boolean registryServer (String serviceName,Ice.Object serverBean){
		Identity id = ic.stringToIdentity(serviceName);
		adapter.add(serverBean, id);
		LOG.info(" ############## ice 服务 {} 注册成功  ##############", serviceName);
		return true;
	}
	public static boolean registryServerWithPlugin (String serviceName,Ice.Object serverBean){
		Identity id = ic.stringToIdentity(serviceName);
		coreDispatch.addDispatch(id, serverBean);
		adapter.add(coreDispatch, id);
		LOG.info(" ############## Dispatch ice 服务 [{}] 注册成功  ##############", serviceName);
		return true;
	}
	public static void addExInterceptor(ExceptionInterceptor bean) {
		coreDispatch.addExInterceptor(bean);
	}
}
class CoreDispatch extends Ice.DispatchInterceptor{
	private final static Logger LOG = LoggerFactory.getLogger(IceSpringPluginServer.class);
	/**
	 * serialVersionUID: TODO
	 */
	
	private static final long serialVersionUID = -4351470112958809490L;
	/**
	 * 服务注册端点
	 */
	
	private static final Map<Ice.Identity,Ice.Object> id2ObjectMap=new HashMap<Ice.Identity, Ice.Object>();
	private static final List<ExceptionInterceptor> exceptionInterceptors=new ArrayList<>();
	public synchronized void addExInterceptor(ExceptionInterceptor value){
		testExceptionInterceptor(value);
		exceptionInterceptors.add(value);
	}
	private void  testExceptionInterceptor(ExceptionInterceptor value){
		ExecutionUtil.success(value.executionServerName(), "demo");
		if(value.executionClass()!=null){
			value.executionClass().contains(Exception.class);
			
		}
		ExecutionUtil.success(value.executionMethod(), "demo");
	}
	public synchronized void addDispatch(Ice.Identity id,Ice.Object proxy){
		id2ObjectMap.put(id, proxy);
	}
	 
	@Override
	public DispatchStatus dispatch(Request request) {
		DispatchStatus status = null;
		try {
			Object temp = id2ObjectMap.get(request.getCurrent().id);
			status = temp.ice_dispatch(request);
		} catch (Exception ex) {
			ResponseView view=null;
			String name=request.getCurrent().id.name;
			String method=request.getCurrent().operation;
			LOG.warn(" request ice server from "+name+"  "+method+" function client from net =>> " +request.getCurrent().con._toString().intern().replaceAll("\r|\n", "\t"));
			for (ExceptionInterceptor exceptionInterceptor : exceptionInterceptors) {
				if(!ExecutionUtil.success(exceptionInterceptor.executionServerName(), name)){
					continue;
				}
				if(exceptionInterceptor.executionClass()!=null){
					boolean flag=false;
					for (Class<? extends Exception> temp : exceptionInterceptor.executionClass()) {
						if(temp.isAssignableFrom(ex.getClass())){
							flag=true;
							break;
						}
					}
					if(!flag){
						continue;
					}
					
				}
				if(!ExecutionUtil.success(exceptionInterceptor.executionMethod(), method)){
					continue;
				}
				LOG.debug("执行 ice 异常拦截器 {"+exceptionInterceptor.getClass().toString()+"}");
				if(exceptionInterceptor.isDebug()){
					view=exceptionInterceptor.responseDebug(name,method,ex);
				}else {
					view=exceptionInterceptor.response(ex);
				}
				if(view!=null){
					if(view.getResponseStatus()==ResponseStatus.OK){
						IceInternal.Incoming __inS =(Incoming) request;
						IceInternal.BasicStream __os = __inS.os();
						 if(view.getOutType()==OutType.SERIALIZABLE){
							__os.writeSerializable((Serializable)view.getData());
						}else if(view.getOutType()==OutType.STRING){
							__os.writeString((String)view.getData());
						}else if(view.getOutType()==OutType.FLOAT){
							__os.writeFloat((Float)view.getData());
						}
						 LOG.debug("ice 异常拦截器访问成功 响应输出{"+view.getData()+"}");
						return Ice.DispatchStatus.DispatchOK;
					}
					
				}
			}
			throw ex;
		}
		return status;
	}
	
}