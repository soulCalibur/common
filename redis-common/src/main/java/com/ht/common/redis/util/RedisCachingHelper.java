
package com.ht.common.redis.util;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huobao_accp@163.com
 * @date: 2016-11-26 下午4:17:56 @version 1.0
 * @TODO 霍宝
 */
public class RedisCachingHelper {

	private final static Logger LOG = LoggerFactory.getLogger(RedisCachingHelper.class);

	/**
	 * @description TODO
	 * @date 2016-12-17下午3:48:03
	 * @author bb.h
	 * @since 1.0.0
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unused")
	public static InputStream getResource(String name) {
		LOG.info("load resource [{}] from classpath ", name);
		ClassLoader cl = RedisCachingHelper.class.getClassLoader();
		System.out.println(cl.getResource("").getPath());
		if (cl == null) {
			return ClassLoader.getSystemResourceAsStream(name);
		}
		return cl.getResourceAsStream(name);
	}
	// public static void main(String[] args) {
	// System.out.println(getResource("xml/redis.properties"));
	// }
}
