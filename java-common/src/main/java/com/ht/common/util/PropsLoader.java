package com.ht.common.util;

import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PropsLoader {

	private static final Logger logger = LoggerFactory.getLogger(PropsLoader.class) ;
	
	private List<String> locations;
	public Properties props;
	
	public PropsLoader(List<String> locations) {
		super();
		this.locations = locations;
		loadProps();
	}

	/**
	 * 支持热加载，如果需要热加载，调用PropsLoader对象的loadProps即可
	 */
	public void loadProps(){
		
		props = new Properties();
		if(locations != null && locations.size() > 0){
			for (String path : locations) {
				try {
					copyProp(props, readProp(path));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 获取properties的值
	 * 如果没有对应的key则返回空字符串
	 * @param key
	 * @return
	 */
	public String getProperty(String key){
		try {
			return props.getProperty(key);
		} catch (Exception e) {
			logger.error("不存在"+key+"的值");
			return "";
		}
		
	}
	
	/**
	 * 获取properties的值
	 * 如果不存在则返回默认值
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getProperty(String key , String defaultValue){
		try {
			return props.getProperty(key);
		} catch (Exception e) {
			logger.error("不存在"+key+"的值");
			return defaultValue;
		}
		
	}
	
	private void copyProp(Properties rootProp,Properties subProp){
		Iterator<Object> iter = subProp.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			rootProp.setProperty(key, subProp.getProperty(key));
		}
	}
	
	private Properties readProp(String classPathLocation) throws Exception{
		Properties prop = new Properties();
		InputStream in = PropsLoader.class.getResourceAsStream("/"+classPathLocation);
		prop.load(in);
		in.close();
		return prop;
	}
	
	public List<String> getLocations() {
		return locations;
	}

	public void setLocations(List<String> locations) {
		this.locations = locations;
	}
	
}
