/*
 * Project Name: redis-cache
 * File Name: CacheResolver.java
 * Package Name: com.hhly.common.components.redis
 * Date: 2016-12-17下午2:21:07
 * Creator: bb.h
 * ------------------------------
 * 修改人: 
 * 修改时间: 
 * 修改内容: 
 */

package com.ht.common.redis.resolver;

import java.util.Collection;

import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;


/**
 * @description TODO
 * @author bb.h
 * @date 2016-12-17下午2:21:07
 * @see
 */
public class CommonCacheResolver extends SimpleCacheResolver {

	private String defaultCacheName;

	/**
	 * @param cacheManager
	 */
	public CommonCacheResolver(CacheManager cacheManager) {
		super(cacheManager);
	}

	/** 
	 * @description: TODO 
	 * @see org.springframework.cache.interceptor.SimpleCacheResolver#getCacheNames(org.springframework.cache.interceptor.CacheOperationInvocationContext) 
	 */ 
	@Override
	protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
		// TODO Auto-generated method stub
		Collection<String> collection = super.getCacheNames(context);
		if (collection == null || collection.isEmpty()) {
			if (defaultCacheName != null && collection != null) {
				collection.add(defaultCacheName);
			}
		}
		return super.getCacheNames(context);
	}

	public void setDefaultCacheName(String defaultCacheName) {
		this.defaultCacheName = defaultCacheName;
	}
}
