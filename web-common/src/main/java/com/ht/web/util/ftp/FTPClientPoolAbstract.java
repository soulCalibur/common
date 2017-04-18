/*
 * @Project Name: sns-web-utils
 * @File Name: FTPClientPoolAbstract
 * @Package Name: com.hhly.sns.util.FTP
 * @Date: 2017/1/3 11:09
 * @Creator: shenxiaoping-549
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.web.util.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author shenxiaoping-549
 * @description  FTP 链接池通用类
 * @date 2017/1/3 11:09
 * @see
 */
public class FTPClientPoolAbstract extends Pool<FTPClient> {

	public FTPClientPoolAbstract() {
		super();
	}

	public FTPClientPoolAbstract(GenericObjectPoolConfig poolConfig, PooledObjectFactory<FTPClient> factory) {
		super(poolConfig, factory);
	}

	@Override
	protected void returnBrokenResource(FTPClient resource) {
		super.returnBrokenResource(resource);
	}

	@Override
	protected void returnResource(FTPClient resource) {
		super.returnResource(resource);
	}
}
