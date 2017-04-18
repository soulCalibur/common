
package com.ht.test.redis;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.ht.common.redis.RedisCachingConfig;
import com.ht.common.redis.RedisTemplateDriver;

/**
 * @author huobao_accp@163.com
 * @date: 2015-9-10 下午3:03:53 @version 1.0
 * @TODO 霍宝
 */
@Configuration
@ComponentScan("com.hhly.sns.test.cache")
public class RUN {

	/**
	 * @TODO TODO
	 */
	public static void main(String[] args) {
		@SuppressWarnings("resource")
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RedisCachingConfig.class,
				RUN.class);
		context.start();
		ServicesTest servicesTest = context.getBean(ServicesTest.class);
		// 函数返回值随机变化
		String name = "绿帽子";
		String result = null;
		result = servicesTest.get(name);
		System.out.println("get第1次调用结果：" + result);
		result = servicesTest.get(name);
		System.out.println("get第2次调用结果：" + result);
		result = servicesTest.delete(name);
		System.out.println("delete第一次调用结果：" + result);
		result = servicesTest.get(name);
		System.out.println("get第3次调用结果：" + result);
		result = servicesTest.get(name);
		System.out.println("get第4次调用结果：" + result);
		result = servicesTest.put(name);
		System.out.println("put第1次调用结果：" + result);
		result = servicesTest.get(name);
		System.out.println("get第5次调用结果：" + result);
		RedisTemplateDriver driver = context.getBean(RedisTemplateDriver.class);
		String key = "facade:city:type:" + name + "";
		driver.set(key, "手动操作数据", 1000);
		result = servicesTest.get(name);
		System.out.println("get第6次调用结果：" + result);
		// driver.del(key);
		result = servicesTest.get(name);
		System.out.println("get第7次调用结果：" + result);
		// RedisTemplateDriver driver;
		System.out.println(driver.auomicInteger("redis:atomic2"));
		// driver.del(key);
	}
	// public static void main(String[] args) {
	//
	// RedisClusterConfiguration clusterConfiguration=new
	// RedisClusterConfiguration();
	// clusterConfiguration.addClusterNode(new RedisNode("192.168.10.234",
	// 8001));
	// clusterConfiguration.addClusterNode(new RedisNode("192.168.10.234",
	// 8002));
	// clusterConfiguration.addClusterNode(new RedisNode("192.168.10.234",
	// 8003));
	// clusterConfiguration.addClusterNode(new RedisNode("192.168.10.234",
	// 8004));
	// clusterConfiguration.addClusterNode(new RedisNode("192.168.10.234",
	// 8005));
	//
	// CommonJedisConnectionFactory connectionFactory = new
	// CommonJedisConnectionFactory(clusterConfiguration);
	// connectionFactory.setPoolConfig(new JedisPoolConfig());
	// connectionFactory.setSoTimeout(5000);
	// connectionFactory.setConnectionTimeout(5000);
	// connectionFactory.setMaxAttempts(11);
	// connectionFactory.setPassword("hhly");
	// connectionFactory.afterPropertiesSet();
	//
	//
	//
	// RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<Object,
	// Object>();
	// redisTemplate.setHashKeySerializer(new StringRedisSerializer());
	// redisTemplate.setKeySerializer(new StringRedisSerializer());
	// redisTemplate.setStringSerializer(new StringRedisSerializer());
	// redisTemplate.setConnectionFactory(connectionFactory);
	// redisTemplate.afterPropertiesSet();
	// //
	// RedisTemplateDriver driver=new RedisTemplateDriver(redisTemplate);
	// driver.set("BB:VVV", "cccccccccc", 50000);
	// //driver.set("aa:BB:CC:1", "aaaa", 5000);
	// System.out.println(driver.getObj("BB:VVV"));
	//
	//
	// }
}
