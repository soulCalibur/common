/*
 * Project Name: redis-cache
 * File Name: CommonJedisConnectionFactory.java
 * Package Name: com.hhly.common.components.redis
 * Date: 2016-12-19下午12:20:43
 * Creator: bb.h
 * ------------------------------
 * 修改人: 
 * 修改时间: 
 * 修改内容: 
 */

package com.ht.common.redis;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.redis.ExceptionTranslationStrategy;
import org.springframework.data.redis.PassThroughExceptionTranslationStrategy;
import org.springframework.data.redis.connection.ClusterCommandExecutor;
import org.springframework.data.redis.connection.ClusterNodeResourceProvider;
import org.springframework.data.redis.connection.ClusterTopologyProvider;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisClusterConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConverters;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import com.ht.common.redis.provider.JedisClusterNodeResourceProvider;
import com.ht.common.redis.provider.JedisClusterTopologyProvider;

/**
 * @description redis 集群连接构造管理
 * @author bb.h
 * @date 2016-12-19下午12:20:43
 * @see
 */
public class CommonJedisConnectionFactory extends JedisConnectionFactory{
	
	private static final Logger LOG = LoggerFactory.getLogger(CommonJedisConnectionFactory.class);
	private static final ExceptionTranslationStrategy EXCEPTION_TRANSLATION = new PassThroughExceptionTranslationStrategy(JedisConverters.exceptionConverter());
	private JedisCluster cluster;
	private int soTimeout;
	private int maxAttempts=1;
	private String password;
	private int connectionTimeout;
	private JedisPoolConfig poolConfig;
	private RedisClusterConfiguration clusterConfiguration;

	private ClusterCommandExecutor executor;


	/** 
	 * @description: 初始化构造连接器 
	 * @see org.springframework.data.redis.connection.jedis.JedisConnectionFactory#afterPropertiesSet() 
	 */ 
	public void afterPropertiesSet(){
		if(this.cluster!=null){
			LOG.warn(" JedisCluster in use a cluster {}",cluster);
			return;
		}
		Set<HostAndPort> jedisClusterNode=new HashSet<>();
		if(clusterConfiguration==null){
			throw new InvalidDataAccessApiUsageException("RedisClusterConfiguration cannot be empty"); 
		}
		LOG.info("init JedisCluster from com.hhly.common.components.redis.CommonJedisConnectionFactory with auth {}",password);
		
		for (RedisNode redisNode : clusterConfiguration.getClusterNodes()) {
			jedisClusterNode.add(new HostAndPort(redisNode.getHost(),redisNode.getPort()));
		}
		JedisCluster cluster=null;
		 if( password != null && password.length() > 0 ){
			 try {
				 	cluster =new  JedisCluster(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts,password, poolConfig);
			 } catch (Exception e) {
				 throw new InvalidDataAccessApiUsageException("Jedis Cluster conntion auth password need jedis 2.9.0  version jar "); 
			 }
			}else {
				cluster=new  JedisCluster(jedisClusterNode, connectionTimeout, soTimeout, maxAttempts, poolConfig);
			}
		 
		this.cluster=cluster;
		
		ClusterTopologyProvider clusterTopologyProvider = new JedisClusterTopologyProvider(cluster);
		ClusterNodeResourceProvider nodeResourceProvider = new JedisClusterNodeResourceProvider(cluster);
		this.executor = new ClusterCommandExecutor(clusterTopologyProvider,nodeResourceProvider, EXCEPTION_TRANSLATION);
	}

	@Override
	public RedisConnection getConnection() {
		if (this.cluster != null) {
			return getClusterConnection();
		}else {
			return super.getConnection();
		}
	
	}

	@Override
	public RedisClusterConnection getClusterConnection() {
		if(cluster==null){
			throw new InvalidDataAccessApiUsageException("Cluster is not configured!"); 
		}
		JedisClusterConnection clusterConnection= new JedisClusterConnection(cluster, executor);
		return clusterConnection;
	}
	

	public CommonJedisConnectionFactory(JedisPoolConfig poolConfig,RedisClusterConfiguration clusterConfiguration) {
		super();
		this.poolConfig = poolConfig;
		this.clusterConfiguration = clusterConfiguration;
	}
	
	public CommonJedisConnectionFactory(RedisClusterConfiguration clusterConfiguration) {
		super();
		this.clusterConfiguration = clusterConfiguration;
	}

	public CommonJedisConnectionFactory(JedisCluster cluster) {
		super();
		this.cluster = cluster;
	}

	
	public int getSoTimeout() {
		return soTimeout;
	}

	
	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	
	public int getMaxAttempts() {
		return maxAttempts;
	}

	
	public void setMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
	}

	
	public String getPassword() {
		return password;
	}

	
	public void setPassword(String password) {
		this.password = password;
	}

	
	public int getConnectionTimeout() {
		return connectionTimeout;
	}
	
	public void setExecutor(ClusterCommandExecutor executor) {
		this.executor = executor;
	}
	
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	
	public void setPoolConfig(JedisPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}
	
}
