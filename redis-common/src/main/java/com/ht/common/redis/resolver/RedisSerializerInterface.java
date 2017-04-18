
package com.ht.common.redis.resolver;

import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author huobao_accp@163.com
 * @date: 2016-11-26 下午4:47:30 @version 1.0
 * @TODO 霍宝
 */
public interface RedisSerializerInterface {

	/**
	 * @Title: getHashKeySerializer
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @return 设定文件
	 * @return RedisSerializer<?> 返回类型
	 * @author huobao_accp@163.com
	 * @throws
	 */
	public RedisSerializer<?> getHashKeySerializer();

	/**
	 * @Title: getKeySerializer
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @return 设定文件
	 * @return RedisSerializer<?> 返回类型
	 * @author huobao_accp@163.com
	 * @throws
	 */
	public RedisSerializer<?> getKeySerializer();

	/**
	 * @Title: getStringSerializer
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @return 设定文件
	 * @return RedisSerializer<String> 返回类型
	 * @author huobao_accp@163.com
	 * @throws
	 */
	public RedisSerializer<String> getStringSerializer();

	/**
	 * @Title: getDefaultSerializer
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @return 设定文件
	 * @return RedisSerializer<?> 返回类型
	 * @author huobao_accp@163.com
	 * @throws
	 */
	public RedisSerializer<?> getDefaultSerializer();

	/**
	 * @Title: getValueSerializer
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @return 设定文件
	 * @return RedisSerializer<?> 返回类型
	 * @author huobao_accp@163.com
	 * @throws
	 */
	public RedisSerializer<?> getValueSerializer();

	/**
	 * @Title: getEnableDefaultSerializer
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param @return 设定文件
	 * @return boolean 返回类型
	 * @author huobao_accp@163.com
	 * @throws
	 */
	public boolean getEnableDefaultSerializer();
}
