
package com.ht.common.redis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;

/**
 * @description 手动缓存操作类
 * @author bb.h
 * @date 2016-12-19下午6:57:47
 */
public class RedisTemplateDriver {

	private Map<String, RedisAtomicInteger> redisAtomicIntegerMapping;
	private RedisTemplate<Object, Object> redisTemplate;

	public Set<?> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	public RedisTemplateDriver(RedisTemplate<Object, Object> redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
		this.redisAtomicIntegerMapping = new HashMap<String, RedisAtomicInteger>();
	}

	/**
	 * 写入缓存
	 * @param key
	 * @param value
	 * @param expire
	 */
	public void set(final String key, final Object value, final long expire) {
		redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
	}

	/**
	 * @description 写入缓存，不会自动超时
	 * @date 2017年1月16日下午5:39:44
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @param key
	 * @param value
	 */
	public void set(final String key, final Object value) {
		redisTemplate.opsForValue().set(key, value);
	}

	/**
	 * @description 设置超时（小时）
	 * @date 2017年1月17日下午7:51:53
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @param key
	 * @param hours
	 * @return boolean
	 */
	public boolean expireForHours(String key, int hours) {
		return expireForMinute(key, hours * 60);
	}

	/**
	 * @description 设置超时时间(分钟)
	 * @date 2017年1月18日上午9:47:17
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @param key
	 * @param minute
	 * @return
	 */
	public boolean expireForMinute(String key, int minute) {
		return redisTemplate.expire(key, minute, TimeUnit.MINUTES);
	}

	/**
	 * 读取缓存
	 * @param key
	 * @return
	 */
	public Object getObj(final String key) {
		return redisTemplate.boundValueOps(key).get();
	}

	/**
	 * 删除，根据key精确匹配
	 * @param key
	 */
	public void del(final String... key) {
		redisTemplate.delete(Arrays.asList(key));
	}

	public void del(final String key) {
		redisTemplate.delete(key);
	}

	/**
	 * 批量删除，根据key模糊匹配
	 * @param pattern
	 */
	public void delpn(final String... pattern) {
		for (String kp : pattern) {
			redisTemplate.delete(redisTemplate.keys(kp + "*"));
		}
	}

	/**
	 * key是否存在
	 * @param key
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * @description value 自动增长
	 * @date 2017-1-6下午1:12:41
	 * @author bb.h
	 * @since 1.0.0
	 * @param key
	 * @return
	 */
	public int auomicInteger(String key) {
		if (redisAtomicIntegerMapping.get(key) == null) {
			redisAtomicIntegerMapping.put(key, new RedisAtomicInteger(key, getConn()));
		}
		return redisAtomicIntegerMapping.get(key).incrementAndGet();
	}

	public RedisConnectionFactory getConn() {
		return redisTemplate.getConnectionFactory();
	}

	// 尽量不要影响到上面的用法，上面方法支持注解，下面暂不支持注解
	// =========↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓==xchd==↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓===========//
	/**
	 * @description 有序集合的添加
	 * @date 2017年1月13日上午11:00:04
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @param key
	 * @param value
	 * @param score
	 * @return
	 */
	public boolean zadd(String key, Object value, double score) {
		if (key == null) {
			return false;
		}
		return redisTemplate.opsForZSet().add(key, value, score);
	}

	/**
	 * @description 获取分数
	 * @date 2017年2月18日下午3:54:02
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @param key
	 * @param member
	 * @return
	 */
	public Double zscore(String key, Object member) {
		if (key == null) {
			return (double) 0;
		}
		Double score = redisTemplate.opsForZSet().score(key, member);
		return score == null ? 0 : score;
	}

	/**
	 * @description 有序集合移除
	 * @date 2017年1月14日下午4:34:23
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @param key
	 * @param value
	 * @return
	 */
	public long zrem(String key, Object value) {
		if (key == null) {
			return 0;
		}
		return redisTemplate.opsForZSet().remove(key, value);
	}

	/**
	 * @description 增加评分
	 * @date 2017年1月13日下午2:46:52
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @param key
	 * @param value
	 * @param delta
	 * @return
	 */
	public double incrementScore(String key, Object value, double delta) {
		if (key == null) {
			return 0;
		}
		return redisTemplate.opsForZSet().incrementScore(key, value, delta);
	}

	/**
	 * @description 根据评分获取有序集合
	 * @date 2017年1月16日下午4:31:52
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<TypedTuple<Object>> rangeForScore(String key, long start, long end) {
		Set<TypedTuple<Object>> set = redisTemplate.opsForZSet().rangeWithScores(key, start, end);
		return set;
	}

	// =========↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓==List==↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓===========//
	/**
	 * @description 从列表(list)尾部插入数据
	 * @date 2017年1月16日下午4:36:21
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @param key
	 * @param values
	 */
	public Long rpush(String key, Object... values) {
		return redisTemplate.opsForList().rightPushAll(key, values);
	}

	public Long rpush(String key, Object value) {
		return redisTemplate.opsForList().rightPush(key, value);
	}

	/**
	 * @description 列表(list)长度
	 * @date 2017年1月16日下午4:45:24
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @param key
	 * @return
	 */
	public long llen(String key) {
		return redisTemplate.opsForList().size(key);
	}

	/**
	 * @description 从列表(list)取出值
	 * @date 2017年1月16日下午4:45:58
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Object> lrange(String key, long start, long end) {
		return redisTemplate.opsForList().range(key, start, end);
	}
}
