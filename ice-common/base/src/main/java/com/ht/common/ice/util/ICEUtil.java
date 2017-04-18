/*
 * Project Name: cmp-ice File Name: ICEUtil.java Package Name:
 * com.hhly.common.components.ice.util Date: 2016年11月26日下午5:06:04 Creator:
 * shenxiaoping ------------------------------ 修改人: 修改时间: 修改内容:
 */

package com.ht.common.ice.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ice.Communicator;
import Ice.InitializationData;
import Ice.ObjectPrx;
import Ice.ObjectPrxHelperBase;
import Ice.Properties;

/**
 * @description ICE 工具类
 * @author Allen Shen
 * @date 2016年11月26日下午5:06:04
 * @see
 */
public class ICEUtil {

	private final static Logger LOG = LoggerFactory.getLogger(ICEUtil.class);

	public static String getICEServiceName(String serviceName, Class<?> clz) {
		if (serviceName.length() == 0) {
			String orgName = clz.getSimpleName();
			return Character.toLowerCase(orgName.charAt(0)) + orgName.substring(1);
		}
		return serviceName;
	}

	public static Method getMethod(Object obj, String methodName, Class<?>[] types) {
		try {
			return obj.getClass().getMethod(methodName, types);
		} catch (NoSuchMethodException | SecurityException e) {
			LOG.error("fail to get method instance--{}", methodName);
		}
		return null;
	}

	/**
	 * @description 分散压力,重新排序
	 * @date 2016年12月1日下午5:47:27
	 * @author shenxiaoping
	 * @since 1.0.0
	 * @param endpoints
	 * @return
	 */
	public static String[] getOptimalEndpoints(String endpoints) {
		String[] eps = endpoints.split(":");
		if (eps.length < 2) {
			return new String[] { endpoints };
		} else {
			return getSortedEndpoints(eps);
		}
	}

	private static String[] getSortedEndpoints(String[] endpoints) {
		int size = endpoints.length;
		String[] sortEndpoints = new String[size];
		for (int i = 0; i < size; i++) {
			StringBuilder sb = new StringBuilder();
			for (int j = i; j < i + size; j++) {
				sb.append(":").append(endpoints[j % size]);
			}
			if (sb.length() > 0) {
				sortEndpoints[i] = sb.deleteCharAt(0).toString();
			}
		}
		return sortEndpoints;
	}
	
	/**
	 * 
	 * @description 获取该类所有的属性，包括所有父类的属性
	 * @date 2017年2月16日下午3:07:24
	 * @author shengxiaping-549
	 * @since 1.0.0 
	 * @param bean
	 * @return
	 */
	public static Field[] getAllFields(Object bean) {
		List<Field> list = new ArrayList<>();
		Class<?> clz = bean.getClass();
		while(clz != Object.class){
			Field[] fields = clz.getDeclaredFields();
			if(fields != null && fields.length > 0){
				list.addAll(Arrays.asList(fields));
			}
			clz = clz.getSuperclass();
		}
		return list.toArray(new Field[list.size()]);
	}
		
	public static String getAdapterRandomName(){
		return System.currentTimeMillis()+"_Adapter";
	}
	public static ObjectPrxHelperBase createProxyHelper(String endpoint, String iceServerName,Class<? extends Ice.ObjectPrxHelperBase>proxyHelperClz,int timeout,Properties properties) {
			Communicator cm=getCommunicator(iceServerName, endpoint,properties);
			Ice.ObjectPrx base = cm.propertyToProxy(iceServerName + ".Proxy").ice_twoway().ice_timeout(timeout);
			ObjectPrxHelperBase proxy = null;
			try {
				// uncheckedCast 不会联系服务器，而是会无条件地返回具有所请求的类型的代理 。 危险但是有用，因省去了和服务的交互确认
				// checkedCast 会联系服务器。这是必要的，
				Method cast = proxyHelperClz.getDeclaredMethod("uncheckedCast",ObjectPrx.class);
				proxy = (ObjectPrxHelperBase) cast.invoke(null, base);
			} catch (NoSuchMethodException | SecurityException
					| IllegalAccessException | IllegalArgumentException
					| InvocationTargetException  e) {
				LOG.error("fail to invoke uncheckedCast", e);
			}
		return proxy;
	}
	
	public static List<ObjectPrxHelperBase> createProxyHelpers(String endpoints, String iceServerName,Class<? extends Ice.ObjectPrxHelperBase>proxyHelperClz,int timeout,Properties properties) {
		List<ObjectPrxHelperBase> result = new ArrayList<>();
		String[] points = ICEUtil.getOptimalEndpoints(endpoints);
		for (int i = 0; i < points.length; i++) {
			ObjectPrxHelperBase proxy=createProxyHelper(points[i], iceServerName, proxyHelperClz, timeout, properties);
			result.add(proxy);
		}
		return result;
	}
	public static Communicator getCommunicator(String serviceName,String endpoints,Properties properties) {
		// crate a new communicator & cache it.
		InitializationData initData = new InitializationData();
		initData.properties =properties;
		StringBuilder sbld = new StringBuilder();
		sbld.append(serviceName).append(":").append(endpoints);
		initData.properties.setProperty(serviceName + ".Proxy", sbld.toString());
		Communicator cm = Ice.Util.initialize(initData);
		return cm;
	}
	@SuppressWarnings("unchecked")
	public static Class<? extends Ice.ObjectPrxHelperBase> getPrxHelperClass(Class<Ice.Object> service) {
		String path = service.getPackage().getName() + "."+ service.getSimpleName() + "PrxHelper";
		try {
			return (Class<? extends ObjectPrxHelperBase>) Class.forName(path);
		} catch (ClassNotFoundException e) {
			LOG.error("fail to get class:{}", path, e);
		}
		return null;
	}
}
 