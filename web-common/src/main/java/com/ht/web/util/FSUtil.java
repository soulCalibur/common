/*
 * @Project Name: clean-cache
 * @File Name: FSUtil
 * @Package Name: com.hhly.sns.util
 * @Date: 2016/12/28 17:28
 * @Creator: shenxiaoping-549
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.web.util;

import com.ht.web.util.ftp.FTPClientHelper;
import com.ht.web.util.ftp.FTPClientUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * @author shenxiaoping-549
 * @description  文件服务器工具
 * @date 2016/12/28 17:28
 * @see
 */
public class FSUtil {

	 private final static Logger LOG = LoggerFactory.getLogger(FSUtil.class);

	/**
	 * @author: shenxiaoping-549
	 * @date: 2016/12/28 16:19
	 * @description: 通过HTTP的方式上传文件到文件服务器根目录下
	 * @param file : 需要上传的文件
	 * @return ： 文件全路径
	 */
	public static String uploadFile2FS(File file) throws Exception{
		return uploadFile2FS(file,null);
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2016/12/28 16:18
	 * @description:  通过HTTP的方式上传文件到文件服务器
	 * @param file : 需要上传的文件
	 * @param targetDirPath : 文件目录地址
	 * @return 文件全路径
	 */
	public static String uploadFile2FS(File file,String targetDirPath) throws Exception{
		return uploadRoute(file.getAbsolutePath(),targetDirPath,file.getName(),FSHelper.UploadTypeEnum.GENE_FILE_NAME);
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/5 17:45
	 * @description: 文件上传,不会带上日期前缀
	 * @param file
	 * @param remoteDirPath
	 * @param remoteFileName  : 自定义文件名
	 * @return
	 * @throws Exception
	 */
	public static String uploadFile2FS(File file,String remoteDirPath,String remoteFileName) throws Exception{
		return uploadRoute(file.getAbsolutePath(),remoteDirPath,remoteFileName,FSHelper.UploadTypeEnum.CUST_FILE_NAME);
	}


	public static String uploadFile2FS(String filePath,String targetDirPath) throws Exception{
		return uploadFile2FS(new File(filePath),targetDirPath);
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/3 13:18
	 * @description:  强制上传文件，当有文件重名的时候，直接覆盖
	 * @param file
	 * @param targetDirPath
	 * @return
	 * @throws Exception
	 */
	public static String uploadFile2FSWithOverride(File file,String targetDirPath) throws Exception{
		if(FSHelper.isStartWithDate(targetDirPath)){
			return uploadRoute(file.getAbsolutePath(),targetDirPath,file.getName(),FSHelper.UploadTypeEnum.CUST_FILE_NAME);
		}
		throw new Exception("the remote path [{"+targetDirPath+"}] is illegal,please check!");
	}


	/**
	 * @author: shenxiaoping-549
	 * @date: 2016/12/28 16:17
	 * @description: 通过HTTP的方式上传文件到文件服务器
	 * @param url ： 文件服务器地址
	 * @param file ： 需要上传的文件
	 * @param targetDirPath ： 上传文件的目录地址
	 * @return 文件全路径
	 */
	public static String uploadFile2FS(String url,String pulicUrl,File file,String targetDirPath,String fileName,FSHelper.UploadTypeEnum type) throws Exception{
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("finalPath", targetDirPath));
		FSHelper.addUserPwd(params);
		String path = HttpUtil.uploadFileByPost(url,file, fileName,params);
		if(path == null ||  path.length()==0){
			throw new Exception("=========upload failed============>"+file.getName());
		}
		return FSHelper.FS_V_PATH + targetDirPath + "/" + fileName;
	}


	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/17 10:50
	 * @description: 删除文件服务器上的文件
	 * @param filePath
	 */
	public static boolean  delete(String filePath){
		//boolean isDel = true;
		List<NameValuePair> params = new ArrayList<>();
		params.add(new BasicNameValuePair("filePath", filePath));
		FSHelper.addUserPwd(params);
		return  HttpUtil.post(FSHelper.FS_HANDLER_DEL_FILE,params) !=  null;
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/19 15:57
	 * @description:  上传多目录多文件到文件服务器
	 * @param localFiles : Map<String, List<File>>  key: 文件服务器目录， List<File>  : 需要上传的本地文件
	 * @return   Map<String,List<String>>
	 *     key: 文件服务器目录， List<String> : 上传后返回在文件服务器上的可访问路径，顺序和出入时一直
	 * @throws Exception
	 */
	public static Map<String,List<String>> uploadMultiFiles2Fs(Map<String,List<File>> localFiles) throws Exception {
		if(localFiles == null || localFiles.isEmpty()){
			LOG.warn("============no files========= ");
			return null;
		}

		Map<String,List<String>>  res = new HashMap<>();
		Iterator<Map.Entry<String, List<File>>>  it = localFiles.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, List<File>> next = it.next();
			String targetDir = next.getKey();
			List<File> uploadFiles = next.getValue();
			try {
				List<String> _filePath = uploadMultiFiles2Fs(uploadFiles,targetDir);
				res.put(targetDir,_filePath);
				System.out.println("===============upload to " + targetDir + "successfully!=============");
			} catch (Exception e) {
				res.clear();
				res = null;
				LOG.error("fail to upload to the remote dir [{}] ", targetDir, e);
				throw new Exception(e.getMessage());
			}
		}
		return res;
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/19 16:56
	 * @description:
	 * @param localFiles : List<Object[]> : Object[] : object[0] 远程服务器目录（String类型）， object[1] 待上传的本地文件（File类型）
	 * @return
	 */
	public static List<String> uploadMultiFiles2FS(List<Object[]> localFiles) throws Exception{
		//long startMs = System.currentTimeMillis();
		List<String> res = new ArrayList<>();
		FTPClient ftp = null;
		FSHelper.UploadTypeEnum type = FSHelper.UploadTypeEnum.GENE_FILE_NAME;

		//文件服务器存放文件的目录缓存
		Map<String,String> dirPathCache = new HashMap<>();
		try {
			for (int i = 0; i < localFiles.size(); i++) {
				Object[] dirFile = localFiles.get(i);
				String dirPath = (String) dirFile[0];
				File _sFile = (File) dirFile[1];
				String _sFilePath = _sFile.getAbsolutePath();
				String uploadPath = null;
				if(_sFile != null && !StringUtils.isBlank(dirPath)){
					String targetDirPath = dirPathCache.get(dirPath);
					if(targetDirPath  == null){
						targetDirPath = FSHelper.getRemoteDirPath(dirPath, type);
						dirPathCache.put(dirPath,targetDirPath);
					}

					String newFileName = FSHelper.getFileName2FS(_sFile, _sFile.getName(), type);

					long fileSize = _sFile.length();
					if( fileSize < FSHelper.FTP_MIN_SIZE ){
						System.err.println("-------------upload by http--------------");
						LOG.debug("========upload by http===========>" + _sFilePath );
						//http 上传
						uploadPath = uploadFile2FS(FSHelper.FS_URL, FSHelper.FS_URL_PUBLIC, _sFile, targetDirPath,newFileName, type);

					}
					else{
						System.err.println("-------------upload by ftp --------------");
						LOG.debug("==================upload by ftp===============>"+ _sFilePath);
						FTPClientUtils ftpClient = FTPClientUtils.instance();
						ftp = ftpClient.getFtpClient();
						boolean isOk = ftpClient.commonUpload(ftp,_sFilePath,targetDirPath,newFileName,false);
						if(isOk){
							uploadPath = FSHelper.FS_V_PATH + targetDirPath + '/' + newFileName;
						}
						else{
							LOG.error("fail to upload file by ftp ==> {}", _sFilePath);
							throw new Exception("fail to upload file by ftp");
						}

					}
					res.add(uploadPath);
				}

			}
		} finally {
			if(ftp != null){
				FTPClientHelper.release(ftp);
			}
			dirPathCache.clear();
			dirPathCache = null;
		}

		return res;
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/18 12:18
	 * @description: 多文件上传, 文件名称自动生成
	 * @param localFiles
	 * @param targetDirPath
	 * @return 文件在FS上的相对路径
	 * @throws Exception
	 */
	public static List<String> uploadMultiFiles2Fs(List<File> localFiles, String targetDirPath) throws Exception {
		//long startMs = System.currentTimeMillis();

		FSHelper.UploadTypeEnum type = FSHelper.UploadTypeEnum.GENE_FILE_NAME;

		//samllFiles 列表中对应大文件的地方用null 占位符，以便最后合并
		List<File>[] bigSmalls = FSHelper.getBigSmallFiles(localFiles);

		List<File> bigFiles = bigSmalls[0];
		List<File> smallFiles = bigSmalls[1];

		//文件服务器存放文件的目录
		targetDirPath = FSHelper.getRemoteDirPath(targetDirPath, type);

		boolean hasSmallFiles = false;
		//处理小文件
		List<String> smallFSFilePath = new ArrayList<String>(smallFiles.size());
		for (int i = 0; i < smallFiles.size(); i++) {
			File _sFile = smallFiles.get(i);
			String uploadPath = null;
			if(_sFile != null){
				hasSmallFiles = true;
				String newFileName = FSHelper.getFileName2FS(_sFile, _sFile.getName(), type);
				System.err.println("-------------upload by http--------------");
				LOG.debug("========upload by http===========");
				//http 上传
				uploadPath = uploadFile2FS(FSHelper.FS_URL, FSHelper.FS_URL_PUBLIC, _sFile, targetDirPath,
						newFileName, type);
			}
			smallFSFilePath.add(uploadPath);
		}

		//处理大文件
		System.err.println("-------------upload by ftp --------------");
		LOG.debug("==================upload by ftp===============");
		FTPClientUtils ftpClient = FTPClientUtils.instance();

		//获取文件
		List<String> bigFileNames = ftpClient.upload(bigFiles,targetDirPath);
		int bigFileSize = bigFileNames.size();
		for (int j = 0; j < bigFileSize; j++) {
			bigFileNames.set(j, FSHelper.FS_V_PATH + targetDirPath + '/' + bigFileNames.get(j));
		}

		//clean resources
		bigFiles.clear();
		bigFiles = null;
		smallFiles.clear();
		smallFiles = null;

		List<String> res  = null;
		int smallFileSize = smallFSFilePath.size();
		if(bigFileSize == 0 && hasSmallFiles){
			res = smallFSFilePath;
		}
		else if( !hasSmallFiles && bigFileSize > 0){
			res = bigFileNames;
		}
		else if(smallFileSize > 0 && smallFileSize > 0){
			//按顺序合并
			int bigCnt = -1;
			for (int k = 0; k < smallFSFilePath.size(); k++) {
				if(smallFSFilePath.get(k) == null){
					smallFSFilePath.set(k,bigFileNames.get(++bigCnt));
				}
			}
			res = smallFSFilePath;
		}

		return res;
	}


	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/4 17:57
	 * @description:  根据文件大小选择上传途径
	 * @param localFilePath
	 * @param targetDirPath
	 * @param targetFileName
	 * @return
	 * @throws Exception
	 */
	private static String uploadRoute(String localFilePath,String targetDirPath, String targetFileName, FSHelper.UploadTypeEnum type) throws Exception {
		int retryCnt = 3;
		String path = null;
		while( retryCnt-- > 0){
			path = doUploadRoute(localFilePath,targetDirPath,targetFileName,type);
			if(StringUtils.isNotBlank(path)){
				LOG.info("upload successfully after retrying {} times", (2-retryCnt));
				break;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {

			}
		}

		return path;
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/4 17:57
	 * @description:  根据文件大小选择上传途径
	 * @param localFilePath
	 * @param targetDirPath
	 * @param targetFileName
	 * @return
	 * @throws Exception
	 */
	private static String doUploadRoute(String localFilePath,String targetDirPath, String targetFileName, FSHelper.UploadTypeEnum type) throws Exception {
		long startMs = System.currentTimeMillis();
		File file = new File(localFilePath);
		if(file == null || !file.exists() || file.isDirectory()){
			throw new Exception("fail to upload , file does not exist or file is a directory!");
		}
		String uploadPath = null;
		long size = file.length();
		boolean isUploadByHttp = size < FSHelper.FTP_MIN_SIZE;

		//加上时间路径
		targetDirPath = FSHelper.getRemoteDirPath(targetDirPath, type);
		String newFileName = FSHelper.getFileName2FS(file,targetFileName,type);
		if(isUploadByHttp){
			System.err.println("-------------upload by http--------------");
			LOG.debug("========upload by http===========");
			//http 上传
			uploadPath = uploadFile2FS(FSHelper.FS_URL,FSHelper.FS_URL_PUBLIC,file,targetDirPath,newFileName,type);
		}
		if(StringUtils.isBlank(uploadPath)){
			System.err.println("-------------upload by ftp --------------");
			LOG.debug("==================upload by ftp===============");
			FTPClientUtils ftpClient = FTPClientUtils.instance();
			//ftp 上传
			boolean isOk = ftpClient.upload(localFilePath,targetDirPath,newFileName,type == FSHelper.UploadTypeEnum.GENE_FILE_NAME);
			if(isOk){
				uploadPath = FSHelper.FS_V_PATH + targetDirPath + '/' + newFileName;
			}
			else{
				LOG.error("fail to upload file by ftp");
				throw new Exception("fail to upload file by ftp");
			}
		}

		long endMs = System.currentTimeMillis();
		System.out.println("cost == "+(endMs - startMs)+"  millisecond == ");
		LOG.info("total cost==== {} millisecond ====== to upload local file ==>{} ",(endMs - startMs),localFilePath);
        LOG.info("upload to remote fs path ==>{}", uploadPath);
		return uploadPath;
	}




	public static void main(String[] args) throws Exception {
//		String url = "";
//		url = FSUtil.uploadFile2FS(new File("D:\\data\\ng-files-log\\logs\\error\\nginx-upload-server-2016-12-28.0.txt"),"/sport/article/i");
//		System.err.println(url);
//		for (int i = 0; i < 1; i++) {
//			String file = "E:\\softs\\TortoiseSVN-1.9.4.27285-x64-svn-1.9.4.msi";
//			file = "D:\\dev-tools\\maven-3.2.1\\lib\\wagon-http-2.6-shaded.jar";
//			url = FSUtil.uploadFile2FS(new File(file),"test/900");
//			System.err.println(url);
//		}

//		url ="/f/20170117/sport/article/i/xac3360eb3c9497292692d78b334343c.jpg";
//		boolean isDel = FSUtil.delete(url);
//		System.err.println("isDel===>"+isDel);

	}

}
