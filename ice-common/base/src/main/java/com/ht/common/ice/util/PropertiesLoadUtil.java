/*
 * @Project Name: sns-web-utils
 * @File Name: ConfigLoader
 * @Package Name: com.hhly.base.util
 * @Date: 2017/1/4 14:50
 * @Creator: shenxiaoping-549
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.common.ice.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shenxiaoping-549
 * @description  通用配置文件加载器,不依赖spring 容器
 * @date 2017/1/4 14:50
 * @see
 */
public class PropertiesLoadUtil {

	private final static Logger LOG = LoggerFactory.getLogger(PropertiesLoadUtil.class);
	private final static Map<String,Properties> cache=new HashMap<>();
	private static final synchronized void addCache(String key, Properties properties){
		cache.put(key, properties);
	}
	private static final synchronized void delCache(String key){
		cache.remove(key);
	}
	public static boolean load(String propertiesFile) {
		return load(propertiesFile, false);
	}
	public static boolean load(String propertiesFile,boolean reload) {
		if(reload){
			delCache(propertiesFile);
		}else if(cache.get(propertiesFile)!=null){
			return true;
		}
		
		InputStream inputStreamConfig = null;
		try {
			inputStreamConfig = PropertiesLoadUtil.class.getClassLoader().getResourceAsStream(propertiesFile);
			Properties configProperties  = new Properties();
			configProperties.load(inputStreamConfig);
			addCache(propertiesFile, configProperties);
		} catch (Exception e) {
			LOG.error("fail to load config properties file==>{}",propertiesFile, e);
		} finally {
			if (isNotNull(inputStreamConfig)) {
				try {
					inputStreamConfig.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if(cache.get(propertiesFile)==null){
			return false;
		}
		return true;
	}

	public static boolean isNull(Object... objects) {

		boolean isNull = true;
		for (Object o : objects) {
			if (o != null) {
				isNull = false;
				break;
			}
		}
		return isNull;
	}

	public static boolean isNotNull(Object... objects) {
		return !isNull(objects);
	}

	/**
	 * <p>
	 * 根据key获取对应的value
	 * </p>
	 */
	public static String getString(String propertiesFile, String key) {
		if(cache.get(propertiesFile)==null){
			return null;
		}else {
			return cache.get(propertiesFile).getProperty(key);
		}
	}

	/**
	 * @description: 获取多个key的值，并链接起来
	 * @param configProperties
	 * @param keys
	 * @param joinSybmol
	 * @return
	 */
	public static String getStrs(String propertiesFile,String[] keys,String joinSybmol){
		StringBuilder sb = new StringBuilder();
		if(keys == null || keys.length == 0){
			return null;
		}
		int len = keys.length;
		int index = len - 1;
		for (int i = 0; i < len; i++) {
			String res = getString(propertiesFile, keys[i]);
			if(res == null){
				return null;
			}
			sb.append(res);
			if(i < index){
				sb.append(joinSybmol);
			}
		}
		return sb.toString();
	}

	public static String getStrs(String propertiesFile, String[] keys,String joinSymbol,String defaultValue){
		String res = getStrs(propertiesFile,keys,joinSymbol);
		if(StringUtils.isBlank(res)){
			res = defaultValue;
		}
		return res;
	}

	public static String getString(Properties configProperties,String key,String defaultValue) {
		String tmp = configProperties.getProperty(key);
		try {
			if (tmp == null) {
				LOG.warn("###error####  can not find constant--key=" + key);
				return defaultValue;
			} else {
				tmp = tmp.trim();
			}
			tmp = new String(tmp.getBytes("iso-8859-1"), "utf8");
		} catch (Exception e) {
			LOG.warn(e.getMessage(), e);
		}

		return tmp;
	}
	public static String getString(String propertiesFile,String key, String defaultStr) {
		Object res = getString(propertiesFile, key);
		return res != null ? (String) res : defaultStr;
	}

	public static Integer getInt(String propertiesFile,String key) {
		return Integer.valueOf(getString(propertiesFile,key));
	}
	
	public static Integer getInt(String propertiesFile,String key, Integer def) {
		String value = getString(propertiesFile,key);
		return value != null ? Integer.valueOf(value) : def;
	}

}
