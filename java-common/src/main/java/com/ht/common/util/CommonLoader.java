/*
 * @Project Name: sns-web-utils
 * @File Name: CommonLoader
 * @Package Name: com.hhly.base.util
 * @Date: 2017/1/4 14:50
 * @Creator: shenxiaoping-549
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author shenxiaoping-549
 * @description  通用配置文件加载器,不依赖spring 容器
 * @date 2017/1/4 14:50
 * @see
 */
public class CommonLoader {

	private final static Logger LOG = LoggerFactory.getLogger(CommonLoader.class);

	private final static CommonLoader instance = new CommonLoader();

	private CommonLoader(){

	}

	public static CommonLoader getInstance(){
		return instance;
	}

	public Properties load(String configPath) {
		InputStream inputStreamConfig = null;
		try {
			inputStreamConfig = getClass().getClassLoader().getResourceAsStream(configPath);
			Properties configProperties  = new Properties();
			configProperties.load(inputStreamConfig);
			return configProperties;
		} catch (Exception e) {
			LOG.error("fail to load config properties file", e);
		} finally {
			if (isNotNull(inputStreamConfig)) {
				try {
					inputStreamConfig.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return null;
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
	public static String getString(Properties configProperties, String key) {
		String tmp = configProperties.getProperty(key);
		try {
			if (tmp == null) {
				LOG.error("###error####  can not find constant--key=" + key);
				return null;
			} else {
				tmp = tmp.trim();
			}
			tmp = new String(tmp.getBytes("iso-8859-1"), "utf8");
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

		return tmp;
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
			LOG.error(e.getMessage(), e);
		}

		return tmp;
	}

	public static Integer getInt(Properties configProperties,String key) {
		return Integer.valueOf(getString(configProperties,key));
	}

	public static Integer getInt(Properties configProperties,String key,int defaultValue) {
		String str = getString(configProperties,key);
		try {
			return Integer.valueOf(str);
		} catch (NumberFormatException e) {
			LOG.error(e.getMessage(),e);
			return defaultValue;
		}
	}

}
