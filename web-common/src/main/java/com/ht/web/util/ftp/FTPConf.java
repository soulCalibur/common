/*
 * @Project Name: sns-web-utils
 * @File Name: FTPConf
 * @Package Name: com.hhly.sns.util.FTP
 * @Date: 2017/1/2 11:30
 * @Creator: shenxiaoping-549
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.web.util.ftp;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * @author shenxiaoping-549
 * @description FTP 配置类
 * @date 2017/1/2 11:30
 * @see
 */
public class FTPConf extends GenericObjectPoolConfig {

	/**
	 * FTP 地址
	 */
	private String host;
	/**
	 * FTP 端口
	 */
	private Integer port = 21;
	/**
	 * FTP 用户名
	 */
	private String user;
	/**
	 * FTP 密码
	 */
	private String pwd;
	/**
	 * 重试次数
	 */
	private int retryCnt = 3;
	/**
	 * 编码格式
	 */
	private String encoding = "UTF-8";
	/**
	 * 超时时间
	 */
	private int timeoutMins = 3;
	/**
	 * ftp用户根目录
	 */
	private String fsRoot = "/home/ng-files";

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public int getRetryCnt() {
		return retryCnt;
	}

	public void setRetryCnt(int retryCnt) {
		this.retryCnt = retryCnt;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public String getFsRoot() {
		return fsRoot;
	}

	public void setFsRoot(String fsRoot) {
		this.fsRoot = fsRoot;
	}

	public int getTimeoutMins() {
		return timeoutMins;
	}

	public void setTimeoutMins(int timeoutMins) {
		this.timeoutMins = timeoutMins;
	}

	@Override public String toString() {
		final StringBuilder sb = new StringBuilder("FTPConf{");
		sb.append("host='").append(host).append('\'');
		sb.append(", port=").append(port);
		sb.append(", user='").append(user).append('\'');
		sb.append(", pwd='").append(pwd).append('\'');
		sb.append(", retryCnt=").append(retryCnt);
		sb.append(", encoding='").append(encoding).append('\'');
		sb.append(", timeoutMins=").append(timeoutMins);
		sb.append(", fsRoot='").append(fsRoot).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
