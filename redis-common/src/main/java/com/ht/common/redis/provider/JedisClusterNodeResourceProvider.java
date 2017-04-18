/*
 * Project Name: redis-cache
 * File Name: JedisClusterNodeResourceProvider.java
 * Package Name: com.hhly.common.components.redis
 * Date: 2016-12-19下午6:28:09
 * Creator: bb.h
 * ------------------------------
 * 修改人: 
 * 修改时间: 
 * 修改内容: 
 */

package com.ht.common.redis.provider;

import java.util.Map;

import org.springframework.data.redis.connection.ClusterNodeResourceProvider;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.util.Assert;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * @description TODO
 * @author bb.h
 * @date 2016-12-19下午6:28:09
 * @see
 */
public class JedisClusterNodeResourceProvider implements ClusterNodeResourceProvider {

	private final JedisCluster cluster;

	/**
	 * Creates new {@link JedisClusterNodeResourceProvider}.
	 *
	 * @param cluster must not be {@literal null}.
	 */
	public JedisClusterNodeResourceProvider(JedisCluster cluster) {
		this.cluster = cluster;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.ClusterNodeResourceProvider#getResourceForSpecificNode(org.springframework.data.redis.connection.RedisClusterNode)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Jedis getResourceForSpecificNode(RedisClusterNode node) {

		JedisPool pool = getResourcePoolForSpecificNode(node);
		if (pool != null) {
			return pool.getResource();
		}

		throw new IllegalArgumentException(String.format("Node %s is unknown to cluster", node));
	}

	protected JedisPool getResourcePoolForSpecificNode(RedisNode node) {

		Assert.notNull(node, "Cannot get Pool for 'null' node!");

		Map<String, JedisPool> clusterNodes = cluster.getClusterNodes();
		if (clusterNodes.containsKey(node.asString())) {
			return clusterNodes.get(node.asString());
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.ClusterNodeResourceProvider#returnResourceForSpecificNode(org.springframework.data.redis.connection.RedisClusterNode, java.lang.Object)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void returnResourceForSpecificNode(RedisClusterNode node, Object client) {
		getResourcePoolForSpecificNode(node).returnResource((Jedis) client);
	}

}

