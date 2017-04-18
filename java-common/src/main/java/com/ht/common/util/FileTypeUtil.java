package com.ht.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.common.mode.FileType;


/**
 * 
 * @类描述 根据文件头数据获取文件类型
 * @作者 wangjian-358
 * @创建时间 2016年11月7日下午12:59:06
 */
public class FileTypeUtil {
	
	private static  Logger logger = LoggerFactory.getLogger(FileTypeUtil.class);
	
	public final static Map<String, String> FILE_TYPE_MAP = new HashMap<String, String>();
   
	private FileTypeUtil() {
	}
   
	// 初始化文件类型信息
	static {
		FILE_TYPE_MAP.put("2e524d46", "rmvb"); // rmvb/rm相同
		FILE_TYPE_MAP.put("464c5601", "flv"); // flv与f4v相同
		FILE_TYPE_MAP.put("00000020", "mp4");
		FILE_TYPE_MAP.put("0000001c", "mp4");
		FILE_TYPE_MAP.put("000001ba", "mpg"); //
		FILE_TYPE_MAP.put("3026b275", "wmv"); // wmv与asf相同
		FILE_TYPE_MAP.put("52494646", "avi");
		FILE_TYPE_MAP.put("6D6F6F76", "mov"); // Quicktime (mov)
		FILE_TYPE_MAP.put("2E7261FD", "ram"); // Real Audio (ram)
	}

	/**
	 * 得到上传文件的文件头
	 * 
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * 根据制定文件的文件头判断其文件类型
	 * @param in
	 * @return
	 */
	public static String getFileType(InputStream in) {
		String res = null;
		try {
			byte[] b = new byte[10];
			in.read(b, 0, b.length);
			String fileCode = bytesToHexString(b);
			System.out.println(fileCode);

			// 这种方法在字典的头代码不够位数的时候可以用，但是速度相对慢一点
			Iterator<String> keyIter = FILE_TYPE_MAP.keySet().iterator();
			while (keyIter.hasNext()) {
				String key = keyIter.next();
				if (key.toLowerCase().startsWith(fileCode.toLowerCase())
						|| fileCode.toLowerCase().startsWith(key.toLowerCase())) {
					res = FILE_TYPE_MAP.get(key);
					break;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}
   
	
	/**
	 * 根据制定文件的文件头判断其文件类型
	 * @param <FileType>
	 * @param in
	 * @return
	 */
	public static  String getFileType(InputStream in,List<FileType> fileTypeList) {
		String res = null;
		try {
			byte[] b = new byte[10];
			in.read(b, 0, b.length);
			String fileCode = bytesToHexString(b);
			boolean isExists=false;
			System.out.println(fileCode);
			for(FileType fileType:fileTypeList){
				String key =fileType.getFileCode();
				if (key.toLowerCase().startsWith(fileCode.toLowerCase())
						|| fileCode.toLowerCase().startsWith(key.toLowerCase())) {
					res = fileType.getFileType();
					isExists=true;
					break;
				}
			}
			if(!isExists){
				logger.info(fileCode+"文件头码不存在文件列表中!");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return res;
	}
	
	public static void main(String[] args) throws Exception {

		long start = System.currentTimeMillis();
		InputStream in=new FileInputStream(new File("E:\\video\\V61124-164036.mp4"));
		String type = getFileType(in);
		System.out.println("type : " + type);
		long end = System.currentTimeMillis();
		System.out.println(end - start);
	}
}