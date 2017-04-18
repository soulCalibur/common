/*
 * @Project Name: sns-web-utils
 * @File Name: FSHelper
 * @Package Name: com.hhly.sns.util
 * @Date: 2017/1/17 10:35
 * @Creator: shenxiaoping-549
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.web.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * @author shenxiaoping-549
 * @description  FSUtil的帮助类
 * @date 2017/1/17 10:35
 * @see
 */
public class FSHelper {

	private final static Logger LOG = LoggerFactory.getLogger(FSHelper.class);

	private final static Properties SETTING = ConfigLoader.getInstance().load("env/fs.properties");
	//文件服务器地址
	public final static String FS_URL = ConfigLoader.getString(SETTING,"fs.url");

	//文件服务器公网地址
	public final static String FS_URL_PUBLIC = ConfigLoader.getString(SETTING,"fs.url.public");

	//文件服务器http的用户名
	public final static String FS_USER = ConfigLoader.getString(SETTING,"fs.user");

	//文件服务器http上传的密码
	public final static String FS_PWD = ConfigLoader.getString(SETTING,"fs.pwd");

	/**
	 * 采用FTP上传的最小尺寸, 以M为单位
	 */
	public final static long  FTP_MIN_SIZE = ConfigLoader.getInt(SETTING,"upload.ftp.minsize.mega",10) * 1048576L;

	public final static String FS_V_PATH = "/f/";

	/**
	 * 删除文件操作的URL
	 */
	public final static String FS_HANDLER_DEL_FILE = ConfigLoader.getStrs(SETTING, new String[]{"fs.handler.url","fs.handler.del"},"/","http://192.168.10.115:9090//fs/fop/delete");


	public enum UploadTypeEnum {
		GENE_FILE_NAME,
		CUST_FILE_NAME,
		TEMP_SLICE
	}


	/**
	 * @author: shenxiaoping-549
	 * @date: 2016/12/28 17:19
	 * @description: 获取文件名称
	 * @param file
	 * @return
	 */
	public static String getFileName2FS(File file, String custFileName, FSHelper.UploadTypeEnum type){
		String fileExt = "";
		String fileName = file.getName();
		if(type == FSHelper.UploadTypeEnum.CUST_FILE_NAME){
			return custFileName;
		}
		int dotIndex = fileName.lastIndexOf(".");
		if(dotIndex > -1){
			fileExt = fileName.substring(dotIndex);
		}
		return UUID.randomUUID().toString().replace("-","")+fileExt;
	}



	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/11 9:52
	 * @description:  判断文件是否已日期开头的
	 * @param dirPath
	 * @return
	 */
	public static boolean isStartWithDate(String dirPath){
		if(StringUtils.isBlank(dirPath)){
			LOG.error(" the directory is blank");
			return false;
		}
		int len = dirPath.length();
		int  sIndex = -1;
		for (int i = 0; i < len; i++) {
			if(dirPath.charAt(i) != '/'){
				sIndex = i;
				break;
			}
		}
		int dateEndIndex = sIndex + 8;
		if(sIndex == -1 || dateEndIndex > len){
			LOG.error("illegle directory ==> {}",dirPath);
			return false;
		}
		for (int j = sIndex + 1; j < dateEndIndex; j++) {
			char c = dirPath.charAt(j);
			if( c < '0' || c > '9' ){
				LOG.error("the path [{}] does not start with yyyyMMdd",dirPath);
				return false;
			}
		}
		return true;
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/10 17:47
	 * @description:  获取远程目录前缀
	 * @return
	 */
	public static String getRemotePrefix(){
		return DateUtils.formatDate(new Date(),"yyyyMMdd") + "/";
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/10 19:04
	 * @description: 获取远程目录
	 * @param remoteDirPath
	 * @return
	 */
	public static String getRemoteDirPath(String remoteDirPath, FSHelper.UploadTypeEnum type){
		String remotePrefix = (type == FSHelper.UploadTypeEnum.GENE_FILE_NAME  ? getRemotePrefix() : "");
		return remoteDirPath != null ?  remotePrefix + remoteDirPath : remotePrefix;
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/17 10:57
	 * @description: 增加用户名和密码
	 * @param params
	 */
	public static void addUserPwd(List<NameValuePair> params) {
		params.add(new BasicNameValuePair("fs-sns-u", FSHelper.FS_USER));
		params.add(new BasicNameValuePair("fs-sns-p",FSHelper.FS_PWD));
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/18 11:13
	 * @description:  检查文件是否存在
	 * @param localFiles
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List<File>[] getBigSmallFiles(List<File> localFiles) {

		List<File>[] res =null;
		if (localFiles == null || localFiles.size() == 0) {
			LOG.error("====== no files=====");
			return null;
		}
		int size = localFiles.size();
		for (int i = 0; i < size; i++) {
			File _file = localFiles.get(i);
			if (_file ==  null || !_file.exists()) {
				LOG.error("===file does not exist : {}",_file.getAbsoluteFile());
				return null;
			}
		}

		List<File> big = new ArrayList<>();
		List<File> small = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			File sFile = localFiles.get(i);
			//long fileSize = sFile.length();
			if( size < FSHelper.FTP_MIN_SIZE ){
				small.add(sFile);
			}
			else{
				small.add(null);
				big.add(sFile);
			}
		}
		res = new ArrayList[2];
		res[0] = big;
		res[1] = small;
		return res;
	}




}
