package com.ht.test.redis;

import java.util.Random;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;




/** 
 * @author huobao_accp@163.com  
 * @date: 2016-11-26 下午5:27:30 @version 1.0
 * @TODO 霍宝
 */
@Service
public class ServicesTest2 {

	/**
	 * @TODO TODO
	 */
	// 例如name为 ：张三 实际在缓存中的key为===>>facade:city:type:张三
	//存储1分钟
	@Cacheable(value = CacheConstant.EXPIRE_MINUTE, key = MyTestConfig.KEY_FACADE_CITY_TYPE + "#name")
	public String get(String name) {
		return "测试数据" + new Random().nextInt();
	}

	// 例如name为 ：张三 实际在缓存中的key为===>>facade:city:type:张三
	//默认时效
	@CachePut(key = MyTestConfig.KEY_FACADE_CITY_TYPE + "#name")
	public String put(String name) {
		return "测试数据" + new Random().nextInt();
	}

	// 例如name为 ：张三 实际在缓存中的key为===>>facade:city:type:张三
	//
	@CacheEvict(key = MyTestConfig.KEY_FACADE_CITY_TYPE + "#name")
	public String delete(String name) {
		return "测试数据" + new Random().nextInt();
	}
	
	@Cacheable(value = CacheConstant.EXPIRE_HOUR, key = MyTestConfig.KEY_FACADE_CITY_TYPE + "#name")
	public String get2(String name) {
		return "测试数据" + new Random().nextInt();
	}
}
