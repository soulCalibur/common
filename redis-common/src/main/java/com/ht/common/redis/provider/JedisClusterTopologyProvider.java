/*
 * Project Name: redis-cache
 * File Name: JedisClusterTopologyProvider.java
 * Package Name: com.hhly.common.components.redis
 * Date: 2016-12-19下午6:24:12
 * Creator: bb.h
 * ------------------------------
 * 修改人: 
 * 修改时间: 
 * 修改内容: 
 */

package com.ht.common.redis.provider;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.data.redis.ClusterStateFailureException;
import org.springframework.data.redis.connection.ClusterTopology;
import org.springframework.data.redis.connection.ClusterTopologyProvider;
import org.springframework.data.redis.connection.RedisClusterNode;
import org.springframework.data.redis.connection.convert.Converters;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

/**
 * @description TODO
 * @author bb.h
 * @date 2016-12-19下午6:24:12
 * @see
 */
public class JedisClusterTopologyProvider implements ClusterTopologyProvider {

	private final Object lock = new Object();
	private final JedisCluster cluster;
	private long time = 0;
	private ClusterTopology cached;

	/**
	 * Create new {@link JedisClusterTopologyProvider}.s
	 *
	 * @param cluster
	 */
	public JedisClusterTopologyProvider(JedisCluster cluster) {
		this.cluster = cluster;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.redis.connection.ClusterTopologyProvider#getTopology()
	 */
	@SuppressWarnings("deprecation")
	@Override
	public ClusterTopology getTopology() {

		if (cached != null && time + 100 > System.currentTimeMillis()) {
			return cached;
		}

		Map<String, Exception> errors = new LinkedHashMap<String, Exception>();

		for (Entry<String, JedisPool> entry : cluster.getClusterNodes().entrySet()) {

			Jedis jedis = null;

			try {
				jedis = entry.getValue().getResource();

				time = System.currentTimeMillis();
				Set<RedisClusterNode> nodes = Converters.toSetOfRedisClusterNodes(jedis.clusterNodes());

				synchronized (lock) {
					cached = new ClusterTopology(nodes);
				}
				return cached;
			} catch (Exception ex) {
				errors.put(entry.getKey(), ex);
			} finally {
				if (jedis != null) {
					entry.getValue().returnResource(jedis);
				}
			}
		}

		StringBuilder sb = new StringBuilder();
		for (Entry<String, Exception> entry : errors.entrySet()) {
			sb.append(String.format("\r\n\t- %s failed: %s", entry.getKey(), entry.getValue().getMessage()));
		}
		throw new ClusterStateFailureException(
				"Could not retrieve cluster information. CLUSTER NODES returned with error." + sb.toString());
	}
}
