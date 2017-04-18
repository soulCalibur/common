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
public class RUN2 {
	static AnnotationConfigApplicationContext context;
	/**
	 * @TODO TODO
	 */
	public static void main(String[] args) {
		 context=new AnnotationConfigApplicationContext(RedisCachingConfig.class,RUN.class); 
		context.start();
		
		ServicesTest servicesTest=context.getBean(ServicesTest.class);
		//函数返回值随机变化
		String name="绿帽子";
		String result=null;
		
	
		 result=servicesTest.get(name);
		System.out.println("get第1次调用结果："+result);
		
		result=servicesTest.get(name);
		System.out.println("get第2次调用结果："+result);
		
		result=servicesTest.delete(name);
		System.out.println("delete第一次调用结果："+result);
		
		
		result=servicesTest.get(name);
		System.out.println("get第3次调用结果："+result);
		result=servicesTest.get(name);
		System.out.println("get第4次调用结果："+result);
		
		 result=servicesTest.put(name);
		System.out.println("put第1次调用结果："+result);
		
		result=servicesTest.get(name);
		System.out.println("get第5次调用结果："+result);
		
		RedisTemplateDriver driver=	context.getBean(RedisTemplateDriver.class);
		String key="facade:city:type:"+name+"";
		driver.set(key, "手动操作数据", 1000);
		
		result=servicesTest.get(name);
		System.out.println("get第6次调用结果："+result);
		driver.del(key);
		
		result=servicesTest.get(name);
		System.out.println("get第7次调用结果："+result);
		driver.del(key);
	}
}
