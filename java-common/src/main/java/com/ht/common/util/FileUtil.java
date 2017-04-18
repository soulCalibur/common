
package com.ht.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类说明：文件操作工具类
 * <p>
 * @author guoya-420
 * @create_date 2016-8-31
 */
public class FileUtil {

	private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 创建目录
	 * @param dir 目录
	 */
	public static void mkdir(String dir) {
		try {
			String dirTemp = dir;
			File dirPath = new File(dirTemp);
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}
		} catch (Exception e) {
			log.error("创建目录操作出错: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Java文件操作 获取文件扩展名
	 * @param filename
	 * @return
	 */
	public static String getExtName(String filename) {
		if (filename != null && filename.length() > 0) {
			int dot = filename.lastIndexOf('.');
			if (dot > -1 && dot < (filename.length() - 1)) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	/**
	 * Java文件操作获取文件名
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		if (filePath != null && filePath.length() > 0) {
			int dot = filePath.lastIndexOf('\\');
			if (dot > -1 && dot < (filePath.length() - 1)) {
				return filePath.substring(filePath.lastIndexOf("\\") + 1);
			}
		}
		return filePath;
	}

	/**
	 * @param filename
	 * @param unit
	 * @return
	 */
	public static long getFileSize(String filename, int unit) {
		File file = new File(filename);
		return file.length() / unit;
	}

	/**
	 * Java文件操作 获取不带扩展名的文件名
	 * @param filename
	 * @return
	 */
	public static String getFileNameNoEx(String filename) {
		if (filename != null && filename.length() > 0) {
			int dot = filename.lastIndexOf('.');
			if (dot > -1 && dot < filename.length()) {
				return filename.substring(0, dot);
			}
		}
		return filename;
	}

	/**
	 * 新建文件
	 * @param fileName String 包含路径的文件名 如:E:\phsftp\src\123.txt
	 * @param content String 文件内容
	 */
	public static void createNewFile(String fileName, String content) {
		try {
			String fileNameTemp = fileName;
			File filePath = new File(fileNameTemp);
			if (!filePath.exists()) {
				filePath.createNewFile();
			}
			FileWriter fw = new FileWriter(filePath);
			PrintWriter pw = new PrintWriter(fw);
			String strContent = content;
			pw.println(strContent);
			pw.flush();
			pw.close();
			fw.close();
		} catch (Exception e) {
			log.error("新建文件操作出错: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 创建文件
	 * @param fileName
	 */
	public static void createNewFile(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
		} catch (Exception e) {
			log.error("新建文件操作出错: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 * @param fileName 包含路径的文件名
	 */
	public static void delFile(String fileName) {
		try {
			String filePath = fileName;
			File delFile = new File(filePath);
			delFile.delete();
		} catch (Exception e) {
			log.error("删除文件操作出错: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件夹
	 * @param folderPath 文件夹路径
	 */
	public static void delFolder(String folderPath) {
		try {
			// 删除文件夹里面所有内容
			delAllFile(folderPath);
			String filePath = folderPath;
			File myFilePath = new File(filePath);
			// 删除空文件夹
			myFilePath.delete();
		} catch (Exception e) {
			log.error("删除文件夹操作出错" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件夹里面的所有文件
	 * @param path 文件夹路径
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] childFiles = file.list();
		File temp = null;
		for (int i = 0; i < childFiles.length; i++) {
			// File.separator与系统有关的默认名称分隔符
			// 在UNIX系统上，此字段的值为'/'；在Microsoft Windows系统上，它为 '\'。
			if (path.endsWith(File.separator)) {
				temp = new File(path + childFiles[i]);
			} else {
				temp = new File(path + File.separator + childFiles[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + childFiles[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + childFiles[i]);// 再删除空文件夹
			}
		}
	}

	/**
	 * 复制单个文件
	 * @param srcFile 包含路径的源文件 如：E:/phsftp/src/abc.txt
	 * @param dirDest 目标文件目录；若文件目录不存在则自动创建 如：E:/phsftp/dest
	 * @throws IOException
	 */
	public static void copyFile(String srcFile, String dirDest) {
		try {
			FileInputStream in = new FileInputStream(srcFile);
			mkdir(dirDest);
			FileOutputStream out = new FileOutputStream(dirDest + "/" + new File(srcFile).getName());
			int len;
			byte buffer[] = new byte[1024];
			while ((len = in.read(buffer)) != -1) {
				out.write(buffer, 0, len);
			}
			out.flush();
			out.close();
			in.close();
		} catch (Exception e) {
			log.error("复制文件操作出错:" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 复制文件夹
	 * @param oldPath String 源文件夹路径 如：E:/phsftp/src
	 * @param newPath String 目标文件夹路径 如：E:/phsftp/dest
	 * @return boolean
	 */
	public static void copyFolder(String oldPath, String newPath) {
		try {
			// 如果文件夹不存在 则新建文件夹
			mkdir(newPath);
			File file = new File(oldPath);
			String[] files = file.list();
			File temp = null;
			for (int i = 0; i < files.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + files[i]);
				} else {
					temp = new File(oldPath + File.separator + files[i]);
				}
				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
					byte[] buffer = new byte[1024 * 2];
					int len;
					while ((len = input.read(buffer)) != -1) {
						output.write(buffer, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + files[i], newPath + "/" + files[i]);
				}
			}
		} catch (Exception e) {
			log.error("复制文件夹操作出错:" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 移动文件到指定目录
	 * @param oldPath 包含路径的文件名 如：E:/phsftp/src/ljq.txt
	 * @param newPath 目标文件目录 如：E:/phsftp/dest
	 */
	public static void moveFile(String oldPath, String newPath) {
		copyFile(oldPath, newPath);
		delFile(oldPath);
	}

	/**
	 * 移动文件到指定目录，不会删除文件夹
	 * @param oldPath 源文件目录 如：E:/phsftp/src
	 * @param newPath 目标文件目录 如：E:/phsftp/dest
	 */
	public static void moveFiles(String oldPath, String newPath) {
		copyFolder(oldPath, newPath);
		delAllFile(oldPath);
	}

	/**
	 * 移动文件到指定目录，会删除文件夹
	 * @param oldPath 源文件目录 如：E:/phsftp/src
	 * @param newPath 目标文件目录 如：E:/phsftp/dest
	 */
	public static void moveFolder(String oldPath, String newPath) {
		copyFolder(oldPath, newPath);
		delFolder(oldPath);
	}

	/**
	 * 压缩文件
	 * @param srcDir 压缩前存放的目录
	 * @param destDir 压缩后存放的目录
	 * @throws Exception
	 */
	public static void yaSuoZip(String srcDir, String destDir) throws Exception {
		String tempFileName = null;
		byte[] buf = new byte[1024 * 2];
		int len;
		// 获取要压缩的文件
		File[] files = new File(srcDir).listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isFile()) {
					FileInputStream fis = new FileInputStream(file);
					BufferedInputStream bis = new BufferedInputStream(fis);
					if (destDir.endsWith(File.separator)) {
						tempFileName = destDir + file.getName() + ".zip";
					} else {
						tempFileName = destDir + "/" + file.getName() + ".zip";
					}
					FileOutputStream fos = new FileOutputStream(tempFileName);
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					ZipOutputStream zos = new ZipOutputStream(bos);// 压缩包
					ZipEntry ze = new ZipEntry(file.getName());// 压缩包文件名
					zos.putNextEntry(ze);// 写入新的ZIP文件条目并将流定位到条目数据的开始处
					while ((len = bis.read(buf)) != -1) {
						zos.write(buf, 0, len);
						zos.flush();
					}
					bis.close();
					zos.close();
				}
			}
		}
	}

	/**
	 * 读取数据
	 * @param inSream
	 * @param charsetName
	 * @return
	 * @throws Exception
	 */
	public static String readData(InputStream inSream, String charsetName) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inSream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		inSream.close();
		return new String(data, charsetName);
	}

	/**
	 * 一行一行读取文件，适合字符读取，若读取中文字符时会出现乱码
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static Set<String> readFile(String path) throws Exception {
		Set<String> datas = new HashSet<String>();
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		String line = null;
		while ((line = br.readLine()) != null) {
			datas.add(line);
		}
		br.close();
		fr.close();
		return datas;
	}

	/**
	 * 读取tomcat日志前N行,
	 * @param filePath
	 * @param rows
	 * @return
	 */
	public static String getLogString(String filePath, int rows) {
		long begin = System.currentTimeMillis();
		try (RandomAccessFile rf = new RandomAccessFile(filePath, "r");) {
			long len = rf.length();// 文件长度
			long start = rf.getFilePointer();// 返回此文件中的当前偏移量。
			long nextend = start + len - 1;
			String line;
			rf.seek(nextend);// 设置到此文件开头测量到的文件指针偏移量，在该位置发生下一个读取或写入操作。
			int c = -1;
			int i = 0;// 计算器
			StringBuffer sb = new StringBuffer(rows * 250);// 保存结果
			while (nextend > start) {
				c = rf.read();
				if (c == '\n' || c == '\r') {
					line = rf.readLine();
					if (line != null) {
						if (line.contains("Exception")) {// 异常标红
							sb.append("<font color='red'>" + new String(line.getBytes("ISO-8859-1"), "utf-8")
									+ "</font><br/>");
						} else {
							sb.append(new String(line.getBytes("ISO-8859-1"), "utf-8") + "</font><br/>");
						}
						i++;
					}
					nextend--;
				}
				nextend--;
				rf.seek(nextend);
				if (i > rows) {
					break;
				}
			}
			log.info(System.currentTimeMillis() - begin + "ms");
			return sb.toString();
		} catch (Exception e) {
			log.error("读取tomcat文件异常", e);
			return "";
		}
	}

	/**
	 * @description 写分块的临时文件
	 * @date 2017年1月7日下午6:20:53
	 * @author chenqiuping-516
	 * @since 1.0.0
	 * @param outPath 临时存放路径
	 * @param inputStream 文件流
	 * @param name 临时文件名
	 * @throws IOException
	 */
	public static String writeTmpFile(String outPath, InputStream inputStream, String name) throws IOException {
		RandomAccessFile raFile = null;
		try {
			File dirFile = new File(outPath);
			if (!dirFile.exists()) {
				dirFile.mkdirs();
			}
			File blockFile = new File(outPath + name);
			// 以读写的方式打开目标文件
			raFile = new RandomAccessFile(blockFile, "rw");
			byte[] buf = new byte[1024];
			int length = 0;
			while ((length = inputStream.read(buf)) != -1) {
				raFile.write(buf, 0, length);
			}
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (raFile != null) {
					raFile.close();
				}
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			}
		}
		return outPath + name;
	}

	/**
	 * @description 分块上传生成分块的文件块名
	 * @date 2017年1月7日下午6:22:33
	 * @author chenqiuping-516
	 * @since 1.0.0
	 * @param chunk 文件分块当前块
	 * @param chunks 文件总块数
	 * @return
	 */
	public static String getBlockName(int chunk, String chunks) {
		chunk++;
		String blockName = "";
		if (chunk <= 9) {
			blockName = "000" + chunk + "_" + chunks;
		} else if (chunk <= 99) {
			blockName = "00" + chunk + "_" + chunks;
		} else if (chunk <= 999) {
			blockName = "0" + chunk + "_" + chunks;
		} else {
			blockName = chunk + "_" + chunks;
		}
		return blockName;
	}

	/**
	 * @description 获取文件的分块的文件名
	 * @date 2017年2月22日下午3:50:17
	 * @author guoya-420
	 * @since 1.0.0
	 * @param blockIndex
	 * @param blockNum
	 * @param fileType
	 * @return
	 */
	public static String getBlockName(int blockIndex, int totalBlock, String fileType) {
		String blockName = "";
		if (blockIndex <= 9) {
			blockName = "000" + blockIndex + "_" + totalBlock + "_" + fileType;
		} else if (blockIndex <= 99) {
			blockName = "00" + blockIndex + "_" + totalBlock + "_" + fileType;
		} else if (blockIndex <= 999) {
			blockName = "0" + blockIndex + "_" + totalBlock + "_" + fileType;
		} else {
			blockName = blockIndex + "_" + totalBlock + "_" + fileType;
		}
		return blockName;
	}

	public static void main(String[] args) {
		System.out.println(getBlockName(1,5,"mp4"));
	}
}
