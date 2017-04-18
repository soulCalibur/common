/*
 * @Project Name: sns-web-utils
 * @File Name: FTPHelper
 * @Package Name: com.hhly.sns.util.FTP
 * @Date: 2017/1/2 11:33
 * @Creator: shenxiaoping-549
 * @line------------------------------
 * @修改人:
 * @修改时间:
 * @修改内容:
 */

package com.ht.web.util.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author shenxiaoping-549
 * @description
 * @date 2017/1/2 11:33
 * @see
 */
public class FTPClientHelper {

	private final static Logger logger = LoggerFactory.getLogger(FTPClientHelper.class);
	private static String FS_ROOT_PATH = "/home/ng-files";
	private static FTPConf CONF;
	private static FTPClientPool POOL;

	public static void setEnv(FTPConf conf, FTPClientPool pool) {
		CONF = conf;
		POOL = pool;
		FS_ROOT_PATH = conf.getFsRoot();
	}

	/**
	 * @return
	 * @author: shenxiaoping-549
	 * @date: 2016/12/30 14:20
	 * @description: 初始化FTP并建立连接
	 */
	public static FTPClient initFTPClient(FTPConf conf) {
		FTPClient ftp = null;
		int retryCnt = 3;
		try {
			ftp = doConnect(conf);
			while (ftp == null && retryCnt > 0) {
				ftp = doConnect(conf);
				retryCnt--;
			}
			if (ftp != null) {
				ftp.setControlEncoding(conf.getEncoding());
				ftp.enterLocalPassiveMode();
				ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
				ftp.setSoTimeout(conf.getTimeoutMins() * 60 * 1000);
				logger.info("==============connected FTP Server=============");
			} else {
				throw new Exception("fail to connect to ftp server");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("fail to getFtpClient ftp configuration,please check FTP setting.", e);
		}
		return ftp;
	}

	/**
	 * @return
	 * @author: shenxiaoping-549
	 * @date: 2016/12/30 14:20
	 * @description: 建立连接
	 */
	private static FTPClient doConnect(FTPConf conf) {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(conf.getHost(), conf.getPort());
			int reply = ftpClient.getReplyCode();
			if (FTPReply.isPositiveCompletion(reply)) {
				if (ftpClient.login(conf.getUser(), conf.getPwd())) {
					return ftpClient;
				}
			} else {
				ftpClient.disconnect();
				logger.error("FTP server refused connection.");
			}
		} catch (IOException e) {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e1) {
					logger.error("Could not disconnect from server.", e1);
				}
			}
			logger.error("Could not connect to server.", e);
		}
		return null;
	}

	public static boolean download(FTPClient ftp, String localPath, String remotePath) {
		File file = new File(localPath);
		try {
			OutputStream out = new FileOutputStream(file);
			return ftp.retrieveFile(remotePath, out);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("fail to get file [{}]", remotePath, e);
		} finally {
			release(ftp);
		}
		return false;
	}

	public static boolean delete(FTPClient ftp, String remotePath) throws Exception {
		try {
			return ftp.deleteFile(remotePath);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("fail to delete file [{}]", remotePath, e);
			throw new Exception(e);
		} finally {
			release(ftp);
		}
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/18 10:58
	 * @description: 多文件上传
	 * @param ftp
	 * @param localFiles
	 * @param remoteDirPath
	 * @return
	 */
	public static List<String> mput(FTPClient ftp, List<File> localFiles, String remoteDirPath) {
		List<String> fileNameList = new ArrayList<>();
		boolean isAllUpload = true;
		try {
			if (localFiles == null || localFiles.size() == 0) {
				logger.error("no any file to upload,please check!");
				isAllUpload = false;
			} else {
				for (int i = 0; i < localFiles.size(); i++) {
					File localFile = localFiles.get(i);
					String localFilePath = localFile.getAbsolutePath();
					String fileName = FTPClientUtils.getUUIDFileName(localFile);
					if (!doPut(ftp, localFile, remoteDirPath, fileName, false)) {
						logger.error("fail to upload file ==> [{}]", localFilePath);
						isAllUpload = false;
						break;
					}
					fileNameList.add(fileName);
				}
			}
		} catch (Exception e) {
			logger.error("fail to upload file", e);
			isAllUpload = false;
		} finally {
			release(ftp);
		}
		return isAllUpload ? fileNameList : null;
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2016/12/30 14:21
	 * @description: 上传文件
	 * @param localFilePath
	 * @param remoteDirPath
	 * @param remoteFileName
	 * @return 是否上传成功
	 */
	public static boolean put(FTPClient ftp, String localFilePath, String remoteDirPath, String remoteFileName) {
		return doPut(ftp, new File(localFilePath), remoteDirPath, remoteFileName, true);
	}

	/**
	 * 上传文件不释放链接
	 * @param ftp
	 * @param localFilePath
	 * @param remoteDirPath
	 * @param remoteFileName
	 * @return
	 */
	public static boolean put(FTPClient ftp, String localFilePath, String remoteDirPath, String remoteFileName,
			boolean isReleaseConn) {
		return doPut(ftp, new File(localFilePath), remoteDirPath, remoteFileName, isReleaseConn);
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/18 11:28
	 * @description: 上传文件
	 * @param ftp
	 * @param localFile
	 * @param remoteDirPath
	 * @param remoteFileName
	 * @return
	 */
	public static boolean put(FTPClient ftp, File localFile, String remoteDirPath, String remoteFileName) {
		return doPut(ftp, localFile, remoteDirPath, remoteFileName, true);
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/18 10:20
	 * @description: 文件上传实现方法
	 * @param ftp
	 * @param localFile
	 * @param remoteDirPath
	 * @param remoteFileName
	 * @param isRelaseFtp
	 * @return
	 */
	private static boolean doPut(FTPClient ftp, File localFile, String remoteDirPath, String remoteFileName,
			boolean isRelaseFtp) {
		boolean isUpload = false;
		// File localFile = new File(localFilePath);
		String localFilePath = localFile.getAbsolutePath();
		if (!localFile.exists()) {
			logger.error("localFile[{}] does not exist!", localFilePath);
			return false;
		}
		if (localFile.isDirectory()) {
			logger.error("{} must be a localFile not folder", localFilePath);
			return false;
		}
		if (!doMakdir(ftp, remoteDirPath, false)) {
			logger.error("fail to create remote directory {}", remoteDirPath);
			return false;
		}
		InputStream in = null;
		try {
			in = new FileInputStream(localFilePath);
			isUpload = ftp.storeFile(remoteFileName, in);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("fail to upload localFile=>{}", localFilePath);
		} finally {
			if (isRelaseFtp) {
				try {
					release(ftp);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
			}
		}
		return isUpload;
	}

	public static boolean mkdir(FTPClient ftp, String remote) {
		return doMakdir(ftp, remote, true);
	}

	/**
	 * @param remote
	 * @return
	 * @throws IOException
	 * @author: shenxiaoping-549
	 * @date: 2016/12/30 11:25
	 * @description: 创建目录
	 */
	private static boolean doMakdir(FTPClient ftp, String remote, boolean isRelease) {
		boolean isSucc = true;
		remote = remote.charAt(remote.length() - 1) == '/' ? remote : remote + '/';
		remote = remote.replaceAll("/+", "/");
		try {
			ftp.changeWorkingDirectory(FS_ROOT_PATH);
			if (!remote.equalsIgnoreCase("/") && !ftp.changeWorkingDirectory(remote)) {
				int start = remote.charAt(0) == '/' ? 1 : 0;
				int end = remote.indexOf("/", start);
				while (true) {
					String subDirectory = remote.substring(start, end);
					if (!ftp.changeWorkingDirectory(subDirectory)) {
						if (ftp.makeDirectory(subDirectory)) {
							ftp.changeWorkingDirectory(subDirectory);
						} else if (!ftp.changeWorkingDirectory(subDirectory)) {
							System.err.println("创建目录失败:" + remote);
							logger.error("fail to create directory {}", remote);
							return false;
						}
					}
					start = end + 1;
					end = remote.indexOf("/", start);
					if (end <= start) {
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("fail to create directory {}", remote);
			isSucc = false;
		} finally {
			if (isRelease) {
				release(ftp);
			}
		}
		return isSucc;
	}

	/**
	 * @throws Exception
	 * @author: shenxiaoping-549
	 * @date: 2016/12/30 14:21
	 * @description: 断开连接
	 */
	public static void disconnect(FTPClient ftpClient) throws Exception {
		try {
			ftpClient.logout();
			if (ftpClient.isConnected()) {
				ftpClient.disconnect();
				ftpClient = null;
			}
		} catch (IOException e) {
			throw new Exception("Could not disconnect from server.", e);
		}
	}

	/**
	 * @author: shenxiaoping-549
	 * @date: 2017/1/5 16:34
	 * @description: 释放ftp回文件池
	 * @param ftp
	 */
	public static void release(FTPClient ftp) {
		try {
			ftp.changeWorkingDirectory(CONF.getFsRoot());
			POOL.returnResource(ftp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
