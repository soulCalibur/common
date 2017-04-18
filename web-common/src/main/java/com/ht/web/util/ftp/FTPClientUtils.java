/*
 * @Project Name: sns-web-utils
 * @File Name: FTPUtils
 * @Package Name: com.hhly.sns.util.FTP
 * @Date: 2017/1/3 14:39
 * @Creator: shenxiaoping-549
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.web.util.ftp;

import com.ht.web.util.ConfigLoader;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * @author shenxiaoping-549
 * @description FTP  client  工具类
 * @date 2017/1/3 14:39
 * @see
 */
public class FTPClientUtils {

	private final static Logger LOG = LoggerFactory.getLogger(FTPClientUtils.class);

	private static FTPClientPool pool ;
	private static FTPConf conf;
	private static FTPClientUtils instance = null;
	private FTPClientUtils() {
		try {
			Properties pro = ConfigLoader.getInstance().load("env/fs.properties");
			initFTPConf(pro);
			pool = new FTPClientPool(conf);
			FTPClientHelper.setEnv(conf,pool);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static FTPClientUtils instance(){
		synchronized (FTPClientUtils.class){
			if(instance ==  null){
				instance = new FTPClientUtils();
			}
			return instance;
		}
	}

	private void initFTPConf(Properties pro) throws Exception{
		conf = new FTPConf();
		String ftpHost = ConfigLoader.getString(pro, "ftp.host");
		Integer ftpPort = ConfigLoader.getInt(pro, "ftp.port");
		if(StringUtils.isBlank(ftpHost) || ftpPort == null){
			LOG.error("fail to get ftp host or ftp port , please check!");
			throw new Exception("None any Ftp Config, please check!");
		}

		conf.setHost(ftpHost);
		conf.setPort(ftpPort);
		conf.setUser(ConfigLoader.getString(pro,"ftp.user"));
		conf.setPwd(ConfigLoader.getString(pro,"ftp.pwd"));
		conf.setRetryCnt(ConfigLoader.getInt(pro,"ftp.retryCnt",3));
		conf.setTimeoutMins(ConfigLoader.getInt(pro,"ftp.timeoutMs",30));
		conf.setFsRoot(ConfigLoader.getString(pro,"ftp.fsRoot","/home/ng-files"));
		conf.setMaxTotal(ConfigLoader.getInt(pro,"ftp.maxTotal",50));
		conf.setMaxIdle(ConfigLoader.getInt(pro,"ftp.maxIdle",5));
		conf.setMinIdle(ConfigLoader.getInt(pro,"ftp.minIdle",5));

		LOG.info("FTP CONFIG===>{}",conf.toString());
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/3 15:07
	 * @description:  上传文件
	 * @param localFilePath
	 * @param remoteFileDir
	 * @param remoteFilePath
	 * @return
	 */
	public boolean upload(String localFilePath, String remoteFileDir, String remoteFilePath) {
		try {
			FTPClient ftpClient = getFtpClient();
			if (ftpClient != null) {
				return FTPClientHelper.put(ftpClient, localFilePath, remoteFileDir, remoteFilePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("fail to upload file from [{}]  to [{}]",localFilePath,remoteFileDir+"/"+remoteFilePath,e);
		}
		return false;
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/18 12:03
	 * @description: 多文件上传
	 * @param localFiles
	 * @param remoteFileDir
	 * @return
	 */
	public List<String> upload(List<File> localFiles, String remoteFileDir) {
		try {
			FTPClient ftpClient = getFtpClient();
			if (ftpClient != null) {
				return FTPClientHelper.mput(ftpClient, localFiles, remoteFileDir);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("fail to upload file from [{}]  to [{}]",localFiles,remoteFileDir,e);
		}
		return null;
	}

	public boolean upload(String localFilePath, String remoteFileDir, String remoteFilePath, boolean isNewFile) {
		String remoteFullFilePath = remoteFileDir + "/" + remoteFilePath;
		try {
			FTPClient ftpClient = getFtpClient();
			if (ftpClient != null) {
				if(!isNewFile){
					ftpClient.deleteFile(remoteFullFilePath);
				}
				return FTPClientHelper.put(ftpClient, localFilePath, remoteFileDir, remoteFilePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("fail to upload file from [{}]  to [{}]",localFilePath, remoteFullFilePath,e);
		}
		return false;
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/3 15:08
	 * @description:  上传文件
	 * @param localFilePath
	 * @param remoteFileFullPath
	 * @return
	 */
	public boolean upload(String localFilePath, String remoteFileFullPath) {
		if (StringUtils.isBlank(remoteFileFullPath)) {
			return true;
		}
		String remoteFilePath = remoteFileFullPath;
		String remoteDirPath = "/";
		int index = remoteFileFullPath.lastIndexOf("/");
		if (index > -1) {
			remoteDirPath = remoteFileFullPath.substring(0, index);
			remoteFilePath = remoteFileFullPath.substring(index + 1);
		}
		return upload(localFilePath, remoteDirPath, remoteFilePath);
	}

	public boolean commonUpload(FTPClient ftpClient,String localFilePath, String remoteDirPath,String remoteFilePath, boolean isReleaseConnection){
		if (StringUtils.isBlank(remoteDirPath) || StringUtils.isBlank(remoteFilePath)) {
			LOG.info("remote dir or file is null==>dir:{},file:{}",remoteDirPath,remoteFilePath);
			return true;
		}

		try {
			if (ftpClient != null) {
				return FTPClientHelper.put(ftpClient,localFilePath, remoteDirPath, remoteFilePath,isReleaseConnection);
			}
			else{
				throw new Exception("ftpClient is null");
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("fail to upload file from [{}]  to [{}]",localFilePath,remoteFilePath+"/"+remoteFilePath,e);
		}

		return false;
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/3 15:08
	 * @description: 下载文件
	 * @param localFilePath
	 * @param remoteFilePath
	 * @return
	 */
	public boolean download(String localFilePath, String remoteFilePath) {
		FTPClient ftpClient = getFtpClient();
		if (ftpClient != null) {
			return FTPClientHelper.download(ftpClient, localFilePath, remoteFilePath);
		}
		return false;
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/3 15:08
	 * @description: 删除文件
	 * @param remoteFilePath
	 * @return
	 */
	public boolean delete(String remoteFilePath) throws Exception{
		FTPClient ftpClient = getFtpClient();
		if (ftpClient != null) {
			return FTPClientHelper.delete(ftpClient, remoteFilePath);
		}
		return false;
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/3 15:08
	 * @description:  从池中获取ftp链接
	 * @return
	 */
	public FTPClient getFtpClient() {
		FTPClient ftpClient = null;
		try {
			ftpClient = pool.getResource();
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("fail to get ftp client from ftp pool!",e);
		}
		return ftpClient;
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/18 10:33
	 * @description: 返回文件的目录和文件名， [0]: 目录   [1] :　文件名称
	 * @param fileFullPath
	 * @return
	 */
	public static String[] getDirAndName(String fileFullPath){
		String[] res = null;
		int index = fileFullPath.lastIndexOf("/");
		if (index > -1) {
			res = new String[2];
			res[0] = fileFullPath.substring(0, index == 0 ? 1 : index);
			res[1] = fileFullPath.substring(index + 1);
		}

		return res;
	}

	public static String getUUIDFileName(File file){
		String fileExt = "";
		String fileName = file.getName();
		int dotIndex = fileName.lastIndexOf(".");
		if(dotIndex > -1){
			fileExt = fileName.substring(dotIndex);
		}
		return UUID.randomUUID().toString().replace("-","")+fileExt;
	}



}
