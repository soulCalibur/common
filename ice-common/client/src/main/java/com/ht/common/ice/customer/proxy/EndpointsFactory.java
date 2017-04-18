/*
 * @Project Name: cmp-ice
 * @File Name: PropertiesUtil.java
 * @Package Name: com.hhly.common.components.ice.util
 * @Date: 2016年11月28日下午4:11:20
 * @Creator: shenxiaoping-549
 * @line------------------------------
 * @修改人:
 * @修改时间:
 * @修改内容:
 */

package com.ht.common.ice.customer.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import Ice.ObjectPrxHelperBase;
import Ice.Util;

import com.ht.common.ice.customer.annotation.Endpoints;
import com.ht.common.ice.customer.mode.ClientMetadata;
import com.ht.common.ice.customer.mode.DefaultPrxHelper;
import com.ht.common.ice.util.ICEUtil;
import com.ht.common.ice.util.PropertiesLoadUtil;

/**
 * @description 仅仅加载ICE配置到内存中
 * @author Allen Shen
 * @date 2016年11月28日下午4:11:20
 * @see
 */
public class EndpointsFactory {

	private final static Logger LOG = LoggerFactory.getLogger(EndpointsFactory.class);
	private final static String ICE_CONFIG_PATH = "env/ice-client.properties";
	private final static ConcurrentHashMap<String, AtomicInteger> ICE_PROXY_INDEXERS = new ConcurrentHashMap<>();
	/**
	 * ICE proxy object cache。
	 */
	private final static ConcurrentHashMap<String, List<ObjectPrxHelperBase>> ICE_PROXY_CACHE = new ConcurrentHashMap<>();

	/**
	 * 客户端配置对应service ICE 的 endpoints 地址. 例如：
	 * ice.provider.endpoints.orderService=orderService:tcp -h 192.168.32.100 -p
	 * 1000
	 */
	public final static String ICE_PROVIDER_ENDPOINTS_PREFIX = "ice.provider.endpoints";

	/**
	 * ICE 客户端配置 前缀
	 */
	public final static String ICE_PROVIDER_PREFIX = "ice.provider";

	/**
	 * ice 请求超时默认值
	 */
	public final static Integer ICE_TIEMOUT_MS_DEFAULT = 5000;

	/**
	 * ICE 调用告警阈值，毫秒为单位
	 */
	public final static String ICE_PROVIDER_COST_THRESHOLD_SECONDES = "ice.provider.cost.max.seconds";

	/**
	 * 最大线程数
	 */
	public final static String ICE_PROVIDER_THREAD_MAX = "Ice.ThreadPool.Client.SizeMax";

	public final static String ICE_THREAD_MAX = "30";
	/**
	 * 最小线程数
	 */
	public final static String ICE_PROVIDER_THREAD_CORE = "Ice.ThreadPool.Client.Size";

	public final static String ICE_THREAD_CORE = "10";
	/**
	 * 最大传输大小，单位是kB, 默认4M
	 */
	public final static String ICE_MSG_MAX_SIZE = "4096";

	public final static String ICE_PROVIDER_MSG_MAX_SIZE = "Ice.MessageSizeMax";
	private static Object doIceInvoke(Class<Ice.Object> service, Endpoints endpoints,Method method, Object[] args)throws Exception {
		int costThreshold = 1000 * PropertiesLoadUtil.getInt(ICE_CONFIG_PATH,EndpointsFactory.ICE_PROVIDER_COST_THRESHOLD_SECONDES, 10);
		long startMS = System.currentTimeMillis();
		Object result = null;
		ObjectPrxHelperBase iceProxy = null;
		try {
			// get ice proxy from the cache
			String iceServerName = EndpointsFactory.getServiceName(service,endpoints);
			int index=atomicIndex(iceServerName);
			List<ObjectPrxHelperBase> prxhelpers = ICE_PROXY_CACHE.get(iceServerName);
			if (prxhelpers == null || prxhelpers.isEmpty()) {
				Class<? extends Ice.ObjectPrxHelperBase> proxyHelperClz = endpoints.proxy();
				if (proxyHelperClz == DefaultPrxHelper.class) {
					proxyHelperClz=ICEUtil.getPrxHelperClass(service);
				}
				String endpointsStr=getEndpointsStr(service, endpoints);
				Ice.Properties properties = Util.createProperties();
				StringBuilder sbld = new StringBuilder();
				sbld.append(iceServerName).append(":").append(endpointsStr);
				properties.setProperty(iceServerName + ".Proxy", sbld.toString());
				properties.setProperty("Ice.ThreadPool.Client.Size",PropertiesLoadUtil.getString(ICE_CONFIG_PATH,ICE_PROVIDER_THREAD_CORE,ICE_THREAD_CORE));
				properties.setProperty("Ice.ThreadPool.Client.SizeMax",PropertiesLoadUtil.getString(ICE_CONFIG_PATH,ICE_PROVIDER_THREAD_MAX, ICE_THREAD_MAX));
				properties.setProperty("Ice.MessageSizeMax", PropertiesLoadUtil.getString(ICE_CONFIG_PATH,ICE_PROVIDER_MSG_MAX_SIZE, ICE_MSG_MAX_SIZE));
				int timeout=PropertiesLoadUtil.getInt(ICE_CONFIG_PATH,ICE_PROVIDER_PREFIX + ".timeout.ms",ICE_TIEMOUT_MS_DEFAULT);
				prxhelpers= ICEUtil.createProxyHelpers(endpointsStr, iceServerName, proxyHelperClz,timeout,properties);
				setICEProxyCache(iceServerName, prxhelpers);
			}
			iceProxy = prxhelpers.get(index % prxhelpers.size());
			LOG.info("get the ice proxy facade---[{}]----", iceProxy);
			Method targetProxyMethod = ICEUtil.getMethod(iceProxy,method.getName(), method.getParameterTypes());
			LOG.info("iceProxy [{}] will invoke [{}]", iceProxy,targetProxyMethod);
			long _s = System.currentTimeMillis();
			result = targetProxyMethod.invoke(iceProxy, args);
			long _e = System.currentTimeMillis();
			long _cost = _e - _s;
			if (_cost > costThreshold) {
				LOG.warn("\n\n [ice_invoke_issue] performance issue: cost {} milliseconds during invoked proxy method",_cost);
			}
			LOG.info("get the ice facade result:--{}--", result);
		} catch (InvocationTargetException e) {
			if (e.getTargetException() instanceof Ice.ConnectionRefusedException) {
				LOG.error("ice 请求连接失败 服务来源==>[{}]", iceProxy);
				return null;
			} else if (e.getTargetException() instanceof Ice.TimeoutException) {
				LOG.error(" ice 请求超时 服务来源==>[{}]", iceProxy);
				return null;
			} else if (e.getTargetException() instanceof Ice.UnknownLocalException) {
				InvocationTargetException ex=(InvocationTargetException)e;
				LOG.error(" ice ==>[{}]", ex.getLocalizedMessage());
				return null;
			}

			throw e;
		} catch (Exception e) {
			LOG.error("fail to invoke the ice server for facade==>[{}], error msg==>[{}]",iceProxy, e.getMessage(), e);
			throw e;
		}

		long endMS = System.currentTimeMillis();
		long cost = endMS - startMS;
		if (cost > costThreshold) {
			LOG.warn("\n\n [ice_invoke_issue] performance issue : cost {} milliseconds . over the threshold.\n\n",cost);
		}
		return result;
	}

	public static ClientMetadata[] getClientMetadataFields(Object target) {
		Class<?> clz = AopUtils.getTargetClass(target);
		List<ClientMetadata> list = new ArrayList<ClientMetadata>();
		Field[] fields = clz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if (Ice.Object.class.isAssignableFrom(field.getType())) {
				@SuppressWarnings("unchecked")
				final Class<Ice.Object> beanType = (Class<Ice.Object>) field.getType();
				final Endpoints endpoints = field.getAnnotation(Endpoints.class);
				if (endpoints != null) {
					//检查配置完整性 
					String endpointsStr =getEndpointsStr(beanType, endpoints);
					if (endpointsStr == null) {
						System.exit(1);
					}
					Enhancer enhancer = new Enhancer();
					enhancer.setInterfaces(new Class[] { beanType });
					enhancer.setCallback(new MethodInterceptor() {
						@Override
						public Object intercept(Object obj, Method method, Object[] args,MethodProxy proxy) throws Throwable {
							return doIceInvoke(beanType, endpoints, method, args);
						}
					});
					Ice.Object proxy = (Ice.Object) enhancer.create();
					ClientMetadata matadata = new ClientMetadata(proxy, field);
					list.add(matadata);
				}
			}
		}
		return list.toArray(new ClientMetadata[list.size()]);
	}
	private static String getServiceName(Class<?> service, Endpoints provider) {
		String customerName = provider.name();
		return StringUtils.isBlank(customerName) ? StringUtils.uncapitalize(service.getSimpleName()) : customerName;
	}

private static String getEndpointsStr(Class<?> service,Endpoints provider){
	String serviceName = getServiceName(service, provider);
	String selfKey = ICE_PROVIDER_ENDPOINTS_PREFIX + "." + serviceName;
	String endpointsStr = PropertiesLoadUtil.getString(ICE_CONFIG_PATH, selfKey);
	if (endpointsStr == null) {
		if (provider.system().length() > 0) {
			String defaultKey = ICE_PROVIDER_ENDPOINTS_PREFIX + "."+ provider.system() + ".default";
			LOG.info("Attempt to automatically [" + selfKey+ "] configure default system properties  new is ["+ defaultKey + "]");
			endpointsStr = PropertiesLoadUtil.getString(ICE_CONFIG_PATH, defaultKey);
		}
	}
	if (endpointsStr == null) {
	LOG.error("fail to get the endpoints of {},please check your configuration for endpoints",serviceName);
	}
	return endpointsStr;

}


private synchronized static void setICEProxyCache(String serviceName,List<ObjectPrxHelperBase> prxHelpers) {
		List<ObjectPrxHelperBase> singleProxies = ICE_PROXY_CACHE.get(serviceName);
		if (singleProxies == null) {
			ICE_PROXY_CACHE.put(serviceName, prxHelpers);
		}
	}

	/**
	 * 获取 proxy 对象
	 * 
	 * @param serviceName
	 * @return
	 */
	private static int atomicIndex(String serviceName) {
		// 根据serviceName的index值获取下一个proxy对象
		AtomicInteger resIndex = ICE_PROXY_INDEXERS.get(serviceName);
		if (resIndex == null) {
			resIndex = new AtomicInteger(0);
			ICE_PROXY_INDEXERS.put(serviceName, resIndex);
		}
		int index = resIndex.incrementAndGet();
		if (index > 10000 || index < 0) {
			resIndex.set(-1);
			index = 0;
		}
		return index;
	}
	public static void loadIceConfig() {
		if (!PropertiesLoadUtil.load(ICE_CONFIG_PATH)) {
			LOG.error("fail to load {} file", ICE_CONFIG_PATH);
			System.exit(1);
		}
	}
}