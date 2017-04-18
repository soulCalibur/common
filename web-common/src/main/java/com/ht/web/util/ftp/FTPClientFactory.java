/*
 * @Project Name: sns-web-utils
 * @File Name: FTPClientFactory
 * @Package Name: com.hhly.sns.util.FTP
 * @Date: 2017/1/3 11:13
 * @Creator: shenxiaoping-549
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.web.util.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

/**
 * @author shenxiaoping-549
 * @description
 * @date 2017/1/3 11:13
 * @see
 */
public class FTPClientFactory implements PooledObjectFactory<FTPClient> {

	private FTPConf  conf;

	public FTPClientFactory(FTPConf conf){
		this.conf = conf;
	}


	@Override
	public PooledObject<FTPClient> makeObject() throws Exception {
		FTPClient ftp =  FTPClientHelper.initFTPClient(conf);
		return new DefaultPooledObject<FTPClient>(ftp);
	}

	@Override
	public void destroyObject(PooledObject<FTPClient> p) throws Exception {
		FTPClient ftp = p.getObject();
		try {
			if(ftp != null && ftp.isConnected()){
				ftp.logout();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				ftp.disconnect();
				ftp = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean validateObject(PooledObject<FTPClient> p) {
		FTPClient ftp = p.getObject();
		try {
			return ftp.sendNoOp();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void activateObject(PooledObject<FTPClient> p) throws Exception {

	}

	@Override
	public void passivateObject(PooledObject<FTPClient> p) throws Exception {
		FTPClient client = p.getObject();
		if(client != null && client.isConnected() && client.isAvailable()){
			client.changeWorkingDirectory(conf.getFsRoot());
		}
	}
}
