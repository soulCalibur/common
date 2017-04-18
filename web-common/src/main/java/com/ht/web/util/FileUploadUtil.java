/*
 * @Project Name: sns-parent
 * @File Name: FileUploadUtil
 * @Package Name:
 * @com.hhly.sns.util
 * @Date: 2016/12/6 14:02
 * @Creator: wangjian-358
 * @line------------------------------
 * @修改人:
 * @修改时间:
 * @修改内容:
 */

package com.ht.web.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * @author wangjian-358
 * @description 文件上传工具类
 * @date 2016/12/6 14:02
 * @see
 */
public class FileUploadUtil {

	private final static Properties SETTING = ConfigLoader.getInstance().load("env/fs.properties");
	// 合并分块文件
	private final static String MERGE_URL = ConfigLoader.getString(SETTING, "fs.mergeUrl");
	// 获取视频封面
	private final static String VIDEO_COVERURL = ConfigLoader.getString(SETTING, "fs.videoCoverUrl");
	// 获图片封面
	private final static String IMAGE_COVERURL = ConfigLoader.getString(SETTING, "fs.imageCoverUrl");
	private static final Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);
	// 定义允许上传的文件类型
	private static Map<String, String> allowExtMap = null;
	// 文件保存根目录
	private String folderName = "upload";
	// 文件保存路径
	private String savePath = "";
	// 是否按资源类型创建目录
	private boolean resFolder = true;
	// 是否按年月创建目录
	private boolean dateFolder = true;
	private String rootPath = "";

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public void setResFolder(boolean resFolder) {
		this.resFolder = resFolder;
	}

	public void setDateFolder(boolean dateFolder) {
		this.dateFolder = dateFolder;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	// 不要改变map的key
	static {
		allowExtMap = new HashMap<String, String>();
		// 图片资源
		allowExtMap.put("image", "gif,jpg,jpeg,png,bmp");
		// 视频资源
		allowExtMap.put("video", "wav,wma,wmv,mid,avi,mpg,mp4,rm,mov");
		// 其他资源
		allowExtMap.put("other", "txt,rar,zip");
	}

	public FileUploadUtil() {
	}

	/**
	 * 检查路径，若不存在文件夹则创建
	 * @param folderPath
	 */
	public void chkFilePath(String folderPath) {
		File fileFolder = new File(folderPath.toString());
		if (!fileFolder.exists()) {
			fileFolder.mkdirs();
		}
	}

	/**
	 * 检查文件名后缀
	 * @param fileExt
	 * @return
	 */
	public boolean chkFileExt(String fileExt) {
		if (StringUtils.isBlank(fileExt)) {
			return false;
		}
		Set<Map.Entry<String, String>> set = allowExtMap.entrySet();
		Iterator<Map.Entry<String, String>> it = set.iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			String value = entry.getValue();
			String[] arrExt = value.split(",");
			if (Arrays.<String>asList(arrExt).contains(fileExt)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @方法功能描述 生成新文件名
	 * @创建时间 2016年8月30日下午1:18:25
	 * @return
	 */
	public String getNewFileName() {
		return generator(16);
	}

	/**
	 * @description 生成唯一文件名称UUID+随机码
	 * @date 2017年1月20日上午11:43:01
	 * @author lubo551
	 * @since 1.0.0
	 * @return
	 */
	public static String getUUIDFileName() {
		return UUID.randomUUID().toString().replace("-", "") + new Random(System.nanoTime()).nextInt(100);
	}

	/**
	 * @方法功能描述 取得文件扩展名
	 * @创建时间 2016年8月30日下午1:28:00
	 * @param fileName
	 * @return 文件扩展名不带"."符号且小写，如：jpg
	 */
	public static String getFileExt(String fileName) {
		if (fileName.indexOf(".") != -1) {
			return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		}
		return "";
	}

	/**
	 * @方法功能描述 对文件保存路径的预处理
	 * @创建时间 2016年8月30日下午5:53:30
	 * @param fileName
	 * @param customFolder 自定义目录，支持多级目录，格式为：a.b.c
	 * @return
	 */
	public String initFilePath(String fileName, String customFolder) {
		StringBuffer filePath = new StringBuffer();
		// 自定义根目录
		if (!StringUtils.isBlank(folderName)) {
			filePath.append(folderName + File.separator);
		}
		// 资源类型目录
		if (resFolder) {
			filePath.append(getFileType(fileName) + File.separator);
		}
		// 自定义目录
		if (!StringUtils.isBlank(customFolder)) {
			String[] paths = customFolder.split("\\.");
			for (String str : paths) {
				filePath.append(str + File.separator);
			}
		}
		// 年月目录
		if (dateFolder) {
			filePath.append(dateFormat(new Date(), "yyyyMM") + File.separator);
		}
		// 验证文件保存路径，如文件夹不存在则创建
		chkFilePath(rootPath + filePath.toString());
		return filePath.toString();
	}

	/**
	 * @方法功能描述 根据文件名取得 fileType
	 * @创建时间 2016年8月30日下午1:31:03
	 * @param fileName
	 * @return
	 */
	public static String getFileType(String fileName) {
		if (StringUtils.isNoneBlank(fileName)) {
			String fileExt = getFileExt(fileName);
			Set<Map.Entry<String, String>> set = allowExtMap.entrySet();
			Iterator<Map.Entry<String, String>> it = set.iterator();
			while (it.hasNext()) {
				Map.Entry<String, String> entry = it.next();
				String key = entry.getKey();
				String value = entry.getValue();
				String[] arrExt = value.split(",");
				if (Arrays.<String>asList(arrExt).contains(fileExt)) {
					return key;
				}
			}
		}
		return null;
	}

	public List<String> uploadFiles(HttpServletRequest request) {
		return this.uploadFiles(request, null);
	}

	/**
	 * @方法功能描述 上传文件
	 * @创建时间 2016年8月30日下午5:55:03
	 * @param request
	 * @param customFolder 自定义目录，支持多级目录，格式为：a.b.c
	 * @return
	 */
	public List<String> uploadFiles(HttpServletRequest request, String customFolder) {
		if (StringUtils.isBlank(rootPath)) {
			rootPath = request.getSession().getServletContext().getRealPath("/");
		}
		List<String> uploadedName = new ArrayList<String>();
		// 创建一个通用的多部分解析器;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession()
				.getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				List<MultipartFile> multipartFiles = multiRequest.getFiles(iter.next());
				for (MultipartFile file : multipartFiles) {
					if (file != null) {
						// 取得当前上传文件的文件名称
						if (file instanceof CommonsMultipartFile) {
							CommonsMultipartFile cf = (CommonsMultipartFile) file;
							DiskFileItem fi = (DiskFileItem) cf.getFileItem();
							File localFile = fi.getStoreLocation();
							if (!localFile.exists()) {
								try {
									writeFile(localFile, file.getInputStream());
								} catch (IOException e) {
									logger.error(e.getMessage(), e);
								}
							}
							String curFimeName = file.getOriginalFilename();
							if (!StringUtils.isBlank(curFimeName)) {
								String fileExt = getFileExt(curFimeName);
								// 检验允许的文件类型
								if (chkFileExt(fileExt)) {
									String filePath = localFile.getParent();
									// 重命名上传后的文件名
									String fileName = getUUIDFileName() + "." + fileExt;
									String decs = filePath + File.separator + fileName;
									logger.info("==>文件路径" + decs);
									localFile.renameTo(new File(decs));
									uploadedName.add(decs);
								} else {
									logger.info("上传文件[" + curFimeName + "]不合法");
								}
							}
						}
					}
				}
			}
		}
		return uploadedName;
	}

	/**
	 * @author: wangjian-358
	 * @date: 2016/12/9 11:45
	 * @description: 获取request对象里上传的文件数
	 * @param request
	 * @return
	 */
	public int checktFileNum(HttpServletRequest request) {
		int fileNumbers = 0;
		// 创建一个通用的多部分解析器;
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession()
				.getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		if (multipartResolver.isMultipart(request)) {
			// 转换成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			// 取得request中的所有文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				List<MultipartFile> multipartFiles = multiRequest.getFiles(iter.next());
				if (multipartFiles != null && !multipartFiles.isEmpty()) {
					fileNumbers = fileNumbers + multipartFiles.size();
				}
			}
		}
		return fileNumbers;
	}

	/**
	 * 判断是否存在文件上传
	 * @param request
	 * @return
	 */
	public boolean existsFileUpload(HttpServletRequest request) {
		boolean existsFile = false;
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
		// 取得request中的所有文件名
		Iterator<String> iter = multiRequest.getFileNames();
		// 判断 request 是否有文件上传
		if (iter.hasNext()) {
			existsFile = true;
		}
		return existsFile;
	}

	/**
	 * @author: wangjian-358
	 * @date: 2016/12/9 11:44
	 * @description: 格式化时间参数
	 * @param date 需要格式化的时间参数
	 * @param format 格式化的格式，如：yyyy-MM
	 * @return 格式化后的字符串
	 */
	private String dateFormat(Date date, String format) {
		String result = "";
		try {
			if (date != null) {
				DateFormat df_format = new SimpleDateFormat(format);
				result = df_format.format(date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @author: wangjian-358
	 * @date: 2016/12/9 11:43
	 * @description: 生成随机字符串
	 * @param length 需要生成的字符串长度
	 * @return 一个随机生成的字符串
	 */
	private String generator(int length) {
		String sources = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		int codesLen = sources.length();
		Random rand = new Random(System.currentTimeMillis());
		StringBuilder verifyCode = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
		}
		return verifyCode.toString();
	}

	/**
	 * @description 获取文件名
	 * @date 2016年12月8日下午4:40:16
	 * @author tanshen-519
	 * @since 1.0.0
	 * @param fileName
	 * @return
	 */
	public String getFileNameByPath(String fileName) {
		return fileName.substring(fileName.lastIndexOf("\\") + 1);
	}

	/**
	 * @description 分片上传完成，请求合并文件，返回文件上传到文件服务器的路径
	 * @date 2017年1月6日下午5:15:06
	 * @author chenqiuping-516
	 * @since 1.0.0
	 * @param url 分块文件在文件服务器目录
	 * @param folder 合并之后存放的文件目录
	 * @param fileType 文件的格式
	 * @return
	 */
	public static String getUploadInfo(String url, String folder, String fileType) {
		List<NameValuePair> ListNameValuePair = new ArrayList<NameValuePair>();
		ListNameValuePair.add(new BasicNameValuePair("sliceFilesRootPath", url));
		ListNameValuePair.add(new BasicNameValuePair("remoteDirPath", folder));
		ListNameValuePair.add(new BasicNameValuePair("fileExt", fileType));
		String result = HttpUtil.post(MERGE_URL, null, ListNameValuePair);
		return result;
	}

	/**
	 * @description 获取视频封面
	 * @date 2017年1月6日下午5:15:06
	 * @author chenqiuping-516
	 * @since 1.0.0
	 * @param url 分块文件在文件服务器目录
	 * @param folder 合并之后存放的文件目录
	 * @param fileType 文件的格式
	 * @return
	 */
	public static String getVideoCover(String path) {
		List<NameValuePair> ListNameValuePair = new ArrayList<NameValuePair>();
		ListNameValuePair.add(new BasicNameValuePair("filePath", path));
		ListNameValuePair.add(new BasicNameValuePair("second", "10"));
		String result = HttpUtil.post(VIDEO_COVERURL, null, ListNameValuePair);
		return result;
	}

	/**
	 * @description 获取文件服务器图片资源请求接口
	 * @date 2017年1月19日下午4:00:13
	 * @author guoya-420
	 * @since 1.0.0
	 * @param path
	 * @param width
	 * @param height
	 * @return
	 */
	public static String httpImageCover(String path, Integer width, Integer height) {
		List<NameValuePair> ListNameValuePair = new ArrayList<NameValuePair>();
		ListNameValuePair.add(new BasicNameValuePair("imgPath", path));
		if (width != null) {
			ListNameValuePair.add(new BasicNameValuePair("width", String.valueOf(width)));
		}
		if (height != null) {
			ListNameValuePair.add(new BasicNameValuePair("height", String.valueOf(height)));
		}
		String result = HttpUtil.post(IMAGE_COVERURL, null, ListNameValuePair);
		return result;
	}

	/**
	 * @description 获取文件服务器视频资源请求接口
	 * @date 2017年1月19日下午4:00:50
	 * @author guoya-420
	 * @since 1.0.0
	 * @param filePath
	 * @param second
	 * @param width
	 * @param height
	 * @return
	 */
	public static String httpVideoCover(String filePath, Integer second, Integer width, Integer height) {
		List<NameValuePair> ListNameValuePair = new ArrayList<NameValuePair>();
		ListNameValuePair.add(new BasicNameValuePair("filePath", filePath));
		if (second != null) {
			ListNameValuePair.add(new BasicNameValuePair("second", String.valueOf(second)));
		}
		if (width != null) {
			ListNameValuePair.add(new BasicNameValuePair("width", String.valueOf(width)));
		}
		if (height != null) {
			ListNameValuePair.add(new BasicNameValuePair("height", String.valueOf(height)));
		}
		String result = HttpUtil.post(VIDEO_COVERURL, null, ListNameValuePair);
		return result;
	}

	/**
	 * @description 获取上传的二进制图片
	 * @date 2016年12月9日上午9:06:42
	 * @author tanshen-519
	 * @since 1.0.0
	 * @param request
	 * @param customFolder
	 * @return
	 */
	public String uploadFileData(HttpServletRequest request, String customFolder) {
		if (StringUtils.isBlank(rootPath)) {
			rootPath = request.getSession().getServletContext().getRealPath("/");
		}
		String base64Code = request.getParameter("upfile");
		// 取得当前上传文件的文件名称
		String curFimeName = getNewFileName();
		String fileExt = "jpg";
		// 重命名上传后的文件名
		String fileName = curFimeName + "." + fileExt;
		savePath = initFilePath(fileName, customFolder);
		// 定义文件保存路径
		File localFile = new File(rootPath + savePath + fileName);
		// 将上传文件写到服务器上指定的文件。
		Base64 base64 = new Base64();
		byte[] bytes = base64.decode(base64Code.getBytes());
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(bytes);
			byte[] buffer = new byte[1024];
			FileOutputStream out = new FileOutputStream(localFile);
			int byteread = 0;
			while ((byteread = in.read(buffer)) != -1) {
				out.write(buffer, 0, byteread); // 文件写操作
				out.flush();
			}
			out.close();
		} catch (Exception e) {
		}
		return savePath + fileName;
	}

	/**
	 * @description 上传多目录多文件到文件服务器
	 * @date 2017年1月19日下午6:07:08
	 * @author guoya-420
	 * @since 1.0.0
	 * @param request
	 * @param moudleName
	 * @return
	 */
	public List<String> uploadFileFtp(HttpServletRequest request, String moudleName) {
		List<String> serverFiles = new ArrayList<String>();
		try {
			List<String> files = uploadFiles(request, null);
			if (files != null && files.size() > 0) {
				List<Object[]> fileList = new ArrayList<Object[]>();
				Object[] obj = null;
				for (String filePath : files) {
					StringBuilder targetDirPath = new StringBuilder("sport" + '/' + moudleName);
					// 获取文件类型
					String filetype = getFileType(filePath);
					switch (filetype) {
						case "video":
							targetDirPath.append("/" + "v");
							break;
						case "image":
							targetDirPath.append("/" + "i");
							break;
					}
					obj = new Object[2];
					obj[0] = targetDirPath.toString();
					obj[1] = new File(filePath);
					fileList.add(obj);
				}
				// 上传本地文件到文件服务器
				serverFiles = FSUtil.uploadMultiFiles2FS(fileList);
			}
		} catch (Exception e) {
			logger.error("上传文件到ftp服务器异常==>{}", e.getMessage(), e);
		}
		return serverFiles;
	}

	public void writeFile(File outPath, InputStream inputStream) throws IOException {
		OutputStream out = null;
		try {
			out = new FileOutputStream(outPath);
			byte[] buf = new byte[1024];
			int length = 0;
			while ((length = inputStream.read(buf)) != -1) {
				out.write(buf, 0, length);
			}
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		} finally {
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
