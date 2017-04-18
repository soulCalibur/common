
package com.ht.common.redis;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.ht.common.redis.constant.Contents;
import com.ht.common.redis.resolver.CommonCacheResolver;
import com.ht.common.redis.resolver.RedisSerializerImp;
import com.ht.common.redis.resolver.RedisSerializerInterface;
import com.ht.common.redis.util.RedisCachingHelper;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

/**
 * @author huobao_accp@163.com
 * @date: 2015-9-10 下午2:29:22 @version 1.0
 * @TODO 霍宝
 */
@EnableCaching
public class RedisCachingConfig implements CachingConfigurer {

	private static final Logger LOG = LoggerFactory.getLogger(RedisCachingConfig.class);
	private RedisTemplate<Object, Object> redisTemplate;
	@Autowired(required = false)
	RedisSerializerInterface serializerInterface;
	private String configPathName;
	private String defaultCacheName;
	private String connType;
	private String redisCacheMode;
	private String redisNodes;
	private String keyValuePrefix;
	private int maxTotal;
	private long defaultExpireTime;
	private int maxIdle;
	private int minIdle;
	private boolean testOnCreate;
	private boolean testOnBorrow;
	private boolean testOnReturn;
	private boolean testWhileIdle;
	private String openValueAuto;
	private CacheManager cacheManager;
	private String password;

	/**
	 * @Title: redisTemplate @Description: 创建一个RedisTemplate @param @return
	 *         设定文件 @return RedisTemplate<?,?> 返回类型 @author
	 *         huobao_accp@163.com @throws
	 */
	public void singletonRedisTemplate() {
		if (redisTemplate != null) {
			return;
		}
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMinIdle(minIdle);
		jedisPoolConfig.setTestOnCreate(testOnCreate);
		jedisPoolConfig.setTestOnBorrow(testOnBorrow);
		jedisPoolConfig.setTestOnReturn(testOnReturn);
		jedisPoolConfig.setTestWhileIdle(testWhileIdle);
		JedisConnectionFactory connectionFactory = null;
		String[] nodes = redisNodes.split(Contents.GROUP);
		if (Contents.CLUSTER.equals(connType)) {
			LOG.info("use [CLUSTER] mode config redis from {} ", redisNodes);
			RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
			for (String string : nodes) {
				LOG.info("check node  {} ", string);
				String[] kvs = string.split(Contents.KVS_IP);
				RedisNode node = new RedisNode(kvs[0], Integer.parseInt(kvs[1]));
				redisClusterConfiguration.addClusterNode(node);
			}
			// 此模式下 如果要验证密码 需要走自定义工厂接口
			if (password != null) {
				CommonJedisConnectionFactory connectionFactoryTemp = new CommonJedisConnectionFactory(
						redisClusterConfiguration);
				connectionFactoryTemp.setPoolConfig(new JedisPoolConfig());
				connectionFactoryTemp.setPassword(password);
				connectionFactoryTemp.afterPropertiesSet();
				connectionFactory = connectionFactoryTemp;
			} else {
				connectionFactory = new JedisConnectionFactory(redisClusterConfiguration);
			}
		} else if (Contents.SHARDINFO.equals(connType)) {
			LOG.info("use [{}] mode config redis from [{}] ", connType, redisNodes);
			String[] kvs = nodes[0].split(Contents.KVS_IP);
			JedisShardInfo shardInfo = new JedisShardInfo(kvs[0], Integer.parseInt(kvs[1]));
			connectionFactory = new JedisConnectionFactory(shardInfo);
		} else {
			connectionFactory = new JedisConnectionFactory();
		}
		connectionFactory.setPoolConfig(jedisPoolConfig);
		connectionFactory.afterPropertiesSet();
		redisTemplate = new RedisTemplate<Object, Object>();
		redisTemplate.setConnectionFactory(connectionFactory);
		if (serializerInterface == null) {
			LOG.warn(" not found RedisSerializerInterface  imp class then will go to RedisSerializerImp");
			serializerInterface = new RedisSerializerImp();
		}
		if (serializerInterface.getHashKeySerializer() != null) {
			redisTemplate.setHashKeySerializer(serializerInterface.getHashKeySerializer());
		}
		if (serializerInterface.getKeySerializer() != null) {
			redisTemplate.setKeySerializer(serializerInterface.getKeySerializer());
		}
		if (serializerInterface.getStringSerializer() != null) {
			redisTemplate.setStringSerializer(serializerInterface.getStringSerializer());
		}
		if (serializerInterface.getValueSerializer() != null) {
			redisTemplate.setValueSerializer(serializerInterface.getValueSerializer());
		}
		if (serializerInterface.getDefaultSerializer() != null) {
			redisTemplate.setEnableDefaultSerializer(serializerInterface.getEnableDefaultSerializer());
			redisTemplate.setDefaultSerializer(serializerInterface.getDefaultSerializer());
		}
		redisTemplate.afterPropertiesSet();
	}

	/**
	 * @description 注入RedisTemplate封装工具bean
	 * @date 2016-12-19下午6:51:20
	 * @author bb.h
	 * @since 1.0.0
	 * @return
	 */
	@Bean
	public RedisTemplateDriver redisTemplateDriver() {
		return new RedisTemplateDriver(redisTemplate);
	}

	/**
	 * @description: 注入开启缓存
	 * @see org.springframework.cache.annotation.CachingConfigurer#cacheManager()
	 */
	@Override
	public CacheManager cacheManager() {
		RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		cacheManager.setDefaultExpiration(defaultExpireTime);
		// 将value注解拼接为key的前缀
		if (keyValuePrefix != null) {
			LOG.info("use keyValuePrefix config  [{}] ", keyValuePrefix);
			cacheManager.setUsePrefix(true);
			DefaultRedisCachePrefix cachePrefix = new DefaultRedisCachePrefix(keyValuePrefix);
			cacheManager.setCachePrefix(cachePrefix);
		}
		// 缓存集群点
		if (redisCacheMode != null) {
			LOG.info("setting redis  timeout data [{}] ", redisCacheMode);
			Map<String, Long> expires = new HashMap<String, Long>();
			String[] values = redisCacheMode.split(Contents.GROUP);
			for (String string : values) {
				String[] kvs = string.split(Contents.KVS_TIME);
				expires.put(kvs[0], Long.parseLong(kvs[1]));
			}
			cacheManager.setExpires(expires);
		}
		// 是否支持动态注入value添加到cache集合
		if ("true".equals(openValueAuto)) {
			cacheManager.initConfig(true);
		}
		cacheManager.afterPropertiesSet();
		this.cacheManager = cacheManager;
		return cacheManager;
	}

	/**
	 * @description: 注解cahce 中value 空值支持
	 * @see org.springframework.cache.annotation.CachingConfigurer#cacheResolver()
	 */
	@Override
	public CacheResolver cacheResolver() {
		// 设置默认缓存名称 解决value为空时候的错误
		CommonCacheResolver cacheResolver = new CommonCacheResolver(cacheManager);
		if (defaultCacheName != null) {
			LOG.info("setting defaut  cache auto create by [{}]", defaultCacheName);
			cacheResolver.setDefaultCacheName(defaultCacheName);
		}
		return cacheResolver;
	}

	@Override
	public CacheErrorHandler errorHandler() {
		return null;
	}

	@Override
	public KeyGenerator keyGenerator() {
		return null;
	}

	/**
	 * @description 加载配置信息 并初始化Template
	 * @date 2016-12-17下午3:47:46
	 * @author bb.h
	 * @since 1.0.0
	 */
	@PostConstruct
	private void loadProperties() {
		Properties prop = new Properties();
		InputStream inputStream = null;
		try {
			if (configPathName == null) {
				inputStream = RedisCachingHelper.getResource(Contents.CONFIG_PATH_NAME);
			} else {
				inputStream = RedisCachingHelper.getResource(configPathName);
			}
			prop.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
		}
		password = prop.getProperty(Contents.AUTH);
		openValueAuto = prop.getProperty(Contents.OPEN_VALUE_AUTO);
		defaultCacheName = prop.getProperty(Contents.DEFAULT_CACHE_NAME);
		connType = prop.getProperty(Contents.CONN_TYPE);
		redisNodes = prop.getProperty(Contents.NODES);
		redisCacheMode = prop.getProperty(Contents.CACHE_MODE);
		keyValuePrefix = prop.getProperty(Contents.KEY_VALUE_PREFIX_NAME);
		maxTotal = Integer.parseInt(prop.getProperty(Contents.MAXTOTAL, "8"));
		maxIdle = Integer.parseInt(prop.getProperty(Contents.MAXIDLE, "8"));
		minIdle = Integer.parseInt(prop.getProperty(Contents.MINIDLE, "0"));
		testOnCreate = Boolean.parseBoolean(prop.getProperty(Contents.TEST_ON_CREATE, "0"));
		testOnBorrow = Boolean.parseBoolean(prop.getProperty(Contents.TEST_ON_BORROW, "0"));
		testOnReturn = Boolean.parseBoolean(prop.getProperty(Contents.TEST_ON_RETURN, "0"));
		testWhileIdle = Boolean.parseBoolean(prop.getProperty(Contents.TEST_WHILE_IDLE, "0"));
		defaultExpireTime = Long.parseLong(prop.getProperty(Contents.DEFAULT_EXPIRE_TIME, "0"));
		singletonRedisTemplate();
	}

	public void setConfigPathName(String configPathName) {
		this.configPathName = configPathName;
	}
}
