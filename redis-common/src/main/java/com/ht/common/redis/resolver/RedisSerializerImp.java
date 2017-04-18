
package com.ht.common.redis.resolver;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @author huobao_accp@163.com
 * @date: 2016-11-26 下午4:57:04 @version 1.0
 * @TODO 霍宝
 */
public class RedisSerializerImp implements RedisSerializerInterface {

	@Override
	public RedisSerializer<?> getHashKeySerializer() {
		// TODO Auto-generated method stub
		return new StringRedisSerializer();
	}

	@Override
	public RedisSerializer<?> getKeySerializer() {
		// TODO Auto-generated method stub
		return new StringRedisSerializer();
	}

	@Override
	public RedisSerializer<String> getStringSerializer() {
		// TODO Auto-generated method stub
		return new StringRedisSerializer();
	}

	@Override
	public RedisSerializer<?> getDefaultSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RedisSerializer<?> getValueSerializer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getEnableDefaultSerializer() {
		// TODO Auto-generated method stub
		return false;
	}
	/**
	 * @TODO TODO
	 */
}
