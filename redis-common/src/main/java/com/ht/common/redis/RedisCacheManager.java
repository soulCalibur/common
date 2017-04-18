
package com.ht.common.redis;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.core.RedisOperations;

import com.ht.common.redis.constant.Contents;

/**
 * @author huobao_accp@163.com
 * @date: 2015-9-10 下午2:29:22 @version 1.0
 * @TODO spring jedis 连接管道
 */
public class RedisCacheManager extends org.springframework.data.redis.cache.RedisCacheManager {

	private static final Logger LOG = LoggerFactory.getLogger(RedisCacheManager.class);

	/**
	 * <p>
	 * Title:
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * @param redisOperations
	 * @author huobao_accp@163.com
	 */
	public RedisCacheManager(RedisOperations<?, ?> redisOperations) {
		super(redisOperations);
	}

	/**
	 * @description 设置是否在缓存找不到的时候 自动重新构造缓存器 
	 * @date 2016-12-17下午3:47:32
	 * @author bb.h
	 * @since 1.0.0 
	 * @param autoValue
	 */
	public void initConfig(boolean autoValue) {
		if (autoValue) {
			Collection<String> cacheNames = new ArrayList<String>();
			cacheNames.add("default");
			setCacheNames(cacheNames);
		}
	}

	/*
	 * (non Javadoc)
	 * @Title: getCache
	 * @Description: 获取缓存器
	 * @param name
	 * @return
	 * @see
	 * org.springframework.data.redis.cache.RedisCacheManager#getCache(java.
	 * lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Cache getCache(String name) {
		// try to get cache by name
		Cache cache = super.getCache(name);
		if (cache != null) {
			return cache;
		} else {
			long expiration = 0;
			String prefixName = name;
			int index = name.lastIndexOf(Contents.KVS_TIME);
			if (index > 0) {
				prefixName = name.substring(0, index);
				expiration = getExpiration(name, index);
			}
			if (expiration == 0) {
				expiration = computeExpiration(name);
			}
			byte[] prefix = isUsePrefix() ? getCachePrefix().prefix(prefixName) : null;
			cache = new RedisCache(name, prefix, getRedisOperations(), 6000);
			addCache(cache);
		}
		return super.getCache(name);
	}

	/**
	 * @description 解析时间表达式
	 * @date 2016-12-17下午3:47:28
	 * @author bb.h
	 * @since 1.0.0 
	 * @param name
	 * @param separatorIndex
	 * @return
	 */
	private long getExpiration(final String name, final int separatorIndex) {
		long expiration = 0;
		String expirationAsString = name.substring(separatorIndex + 1);
		try {
			expiration = Integer.parseInt(expirationAsString);
		} catch (NumberFormatException ex) {
			LOG.error(String.format("Cannnot separate expiration time from cache: '%s'", name), ex);
		}
		return expiration;
	}
}
