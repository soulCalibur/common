package com.ht.common.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ht.common.mode.FileBlock;

/**
 * 类说明：文件续传操作工具类
 * @author guoya-420
 * @create_date 2016-11-01
 */
public class FileContinueUpload {
	private static Logger logger = LoggerFactory.getLogger(FileContinueUpload.class);

	@SuppressWarnings("unused")
	private static String getHash(String fileName, String hashType) throws Exception {
		InputStream fis = new FileInputStream(fileName);
		byte buffer[] = new byte[1024];
		MessageDigest md5 = MessageDigest.getInstance(hashType);
		for (int numRead = 0; (numRead = fis.read(buffer)) > 0;) {
			md5.update(buffer, 0, numRead);
		}
		fis.close();
		return toHexString(md5.digest());
	}

	public static String toHexString(byte[] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);

		for (int i = 0; i < bArray.length; ++i) {
			String sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 分割大文件返回文件的分块信息
	 * @param file
	 * @param splitSzie
	 * @return
	 */
	public static FileBlock getFileBlockBean(FileBlock fileBlock, long splitSzie) {
		try {
			long fileSize = fileBlock.getFileSize();
			short blockNumber = fileBlock.getBlockNumber();
			short blockIndex = fileBlock.getBlockIndex();
			long blockSize=splitSzie;
			// 文件按分割大小进行文件分割
			if (blockIndex==0 || blockIndex < blockNumber) {
				if (fileSize > splitSzie) {
					if (blockIndex == 0) {
						if (fileSize % splitSzie == 0) {
							blockNumber = (short) (fileSize / splitSzie);
						} else {
							blockNumber = (short) (fileSize / splitSzie + 1);
						}
					}
				} else {
					blockSize=fileSize;
					blockNumber=1;//否则默认为1块
				}
				// 返回文件的分块信息
				fileBlock.setBlockNumber(blockNumber);// 分块数量
				fileBlock.setBlockSize(blockSize);//每块大小
				fileBlock.setBlockIndex((short) (blockIndex + 1));// 当前文件的分块数
				fileBlock.setFileSize(fileSize);// 文件总大小(单位Byte)
			}

		} catch (Exception e) {
			logger.error("获取文件分块信息异常" + e.getMessage());
		}
		return fileBlock;
	}
	
	/**
	 * 分割大文件返回文件的分块信息
	 * @param file
	 * @param splitSzie
	 * @return
	 */
	public static FileBlock getFileBlockBean(FileBlock fileBlock) {
		try {
			long fileSize = fileBlock.getFileSize();
			short blockNumber = fileBlock.getBlockNumber();
			short blockIndex = fileBlock.getBlockIndex();
			long blockSize=fileBlock.getBlockSize();;
			// 文件按分割大小进行文件分割
			if (blockIndex==0 || blockIndex < blockNumber) {
				if (fileSize > blockSize) {
					if (blockIndex == 0) {
						if (fileSize % blockSize == 0) {
							blockNumber = (short) (fileSize / blockSize);
						} else {
							blockNumber = (short) ((fileSize / blockSize) + 1);
						}
					}
				} else {
					blockSize=fileSize;
					blockNumber=1;//否则默认为1块
				}
				// 返回文件的分块信息
				fileBlock.setBlockNumber(blockNumber);// 分块数量
				fileBlock.setBlockSize(blockSize);//每块大小
				fileBlock.setBlockIndex((short) (blockIndex + 1));// 当前文件的分块数
				fileBlock.setFileSize(fileSize);// 文件总大小(单位Byte)
			}

		} catch (Exception e) {
			logger.error("获取文件分块信息异常" + e.getMessage());
		}
		return fileBlock;
	}
	
	/**
	 * 按块写入文件
	 * @param out
	 * @param fileSplitBean
	 */
	public static void writeFile(File tmpFile, FileBlock fileBlock, InputStream in) {
		try {
			long blockStartSize = (fileBlock.getBlockIndex() - 1) * fileBlock.getBlockSize();
			// 负责读取数据
			RandomAccessFile raf = new RandomAccessFile(tmpFile.getPath(), "rw");
			raf.seek(blockStartSize);
        	// 缓存区大小
            int bufSize = 1024 * 16;
            byte[] buf = new byte[bufSize];
            int bytes = 0;
            while ((bytes = in.read(buf)) > 0) {
                if (bytes < bufSize) {
                    byte[] lastBuf = new byte[bytes];
                    System.arraycopy(buf, 0, lastBuf, 0, bytes);
                    raf.write(lastBuf);
                } else {
                    raf.write(buf);
                }
            }
			in.close();
			raf.close();
		} catch (FileNotFoundException e) {
			logger.error("file is not exists" + e);
		} catch (IOException e) {
			logger.error("write file happen exception" + e);
		}
	}
	
	/**
	 * 按块写入多个块文件
	 * @param out
	 * @param fileSplitBean
	 */
	public static void writeTmpFile(File tmpFile, FileBlock fileBlock, InputStream in) {
		try {
			String filePath=tmpFile.getPath();
			int blockIndex=fileBlock.getBlockIndex();
			String extName=FileUtil.getExtName(tmpFile.getName());
			String fileName=FileUtil.getFileNameNoEx(tmpFile.getName())+"_"+blockIndex+"."+extName;
			filePath=filePath.replace(FileUtil.getFileName(filePath), fileName);
			// 负责读取数据
			File file=new File(filePath);
			FileUtil.createNewFile(filePath);
			OutputStream out=new FileOutputStream(file);
			// 缓存区大小
            int bufSize = 1024 * 16;
            byte[] buf = new byte[bufSize];
            int bytes = 0;
            while ((bytes = in.read(buf)) > 0) {
                if (bytes < bufSize) {
                    byte[] lastBuf = new byte[bytes];
                    System.arraycopy(buf, 0, lastBuf, 0, bytes);
                    out.write(lastBuf);
                } else {
                	out.write(buf);
                }
            }
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			logger.error("file is not exists" + e);
		} catch (IOException e) {
			logger.error("write file happen exception" + e);
		}
	}
	
	public static void appendFile(File file, InputStream in) {
		
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				logger.error("文件不存在，创建文件失败：" + e);
			}
		}

        try {
        	// 缓存区大小
            int bufSize = 1024 * 16;
            byte[] buf = new byte[bufSize];
            int bytes = 0;
            while ((bytes = in.read(buf)) > 0) {
                if (bytes < bufSize) {
                    byte[] lastBuf = new byte[bytes];
                    System.arraycopy(buf, 0, lastBuf, 0, bytes);
                    FileUtils.writeByteArrayToFile(file, lastBuf, true);
                } else {
                    FileUtils.writeByteArrayToFile(file, buf, true);
                }
            }
            in.close();
        } catch (IOException e) {
        	logger.error("file append error:" + e);
        }
    }
	
	/**
	 * crc32验证文件的完整性
	 * @param fileUri
	 * @return
	 */
    public static String getCRC32(String fileUri) {  
        CRC32 crc32 = new CRC32();  
        FileInputStream fileinputstream = null;  
        CheckedInputStream checkedinputstream = null;  
        String crc = null;  
        try {  
            fileinputstream = new FileInputStream(new File(fileUri));  
            checkedinputstream = new CheckedInputStream(fileinputstream, crc32);  
            while (checkedinputstream.read() != -1) {  
            }  
            crc = Long.toHexString(crc32.getValue()).toUpperCase();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (fileinputstream != null) {  
                try {  
                    fileinputstream.close();  
                } catch (IOException e2) {  
                    e2.printStackTrace();  
                }  
            }  
            if (checkedinputstream != null) {  
                try {  
                    checkedinputstream.close();  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return crc;  
    }  
    
    public static void main(String[] args) {  
        String uri = "D:\\FileBlock\\5gJYoLkomuQdOiZ14532.mp4.001";  
        System.out.println(getCRC32(uri));  
    }  
}
