/*
 * @Project Name: sns-web-utils
 * @File Name: FTPClientPool
 * @Package Name: com.hhly.sns.util.FTP
 * @Date: 2017/1/3 11:06
 * @Creator: shenxiaoping-549
 * @line------------------------------
 * @修改人:
 * @修改时间:
 * @修改内容:
 */

package com.ht.web.util.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * @author shenxiaoping-549
 * @description FTP 连接池
 * @date 2017/1/3 11:06
 * @see
 */
public class FTPClientPool extends FTPClientPoolAbstract {

	public FTPClientPool(FTPConf conf) {
		this.internalPool = new GenericObjectPool<FTPClient>(new FTPClientFactory(conf), conf);
	}

	@Override
	public FTPClient getResource() {
		return super.getResource();
	}

	@Override
	protected void returnResource(FTPClient resource) {
		if (resource != null && resource.isAvailable() && resource.isConnected()) {
			super.returnResource(resource);
		} else {
			returnBrokenResource(resource);
		}
	}

	@Override
	protected void returnBrokenResource(FTPClient resource) {
		if (resource != null) {
			try {
				resource.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
			super.returnBrokenResource(resource);
		}
	}
}
