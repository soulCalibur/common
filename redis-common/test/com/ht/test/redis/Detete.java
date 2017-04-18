/*
 * @Project Name: lib
 * @File Name: Detete.java
 * @Package Name: com.hhly.sns.test
 * @Date: 2017-3-15上午11:41:56
 * @Creator: bb.h
 * @line------------------------------
 * @修改人:
 * @修改时间:
 * @修改内容:
 */

package com.ht.test.redis;

import java.util.Set;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ht.common.redis.RedisCachingConfig;
import com.ht.common.redis.RedisTemplateDriver;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-3-15上午11:41:56
 * @see
 */
public class Detete {

	/**
	 * @TODO TODO
	 */
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RedisCachingConfig.class,
				RUN.class);
		context.start();
		RedisTemplateDriver driver = context.getBean(RedisTemplateDriver.class);
		Set<?> aaa = driver.keys("*userInfo*");
		for (Object object : aaa) {
			System.out.println(object);
		}
		driver.del("facade:user:user:userInfo:hhly90261");
		System.exit(0);
	}
}
