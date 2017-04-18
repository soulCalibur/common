
package com.ht.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 安全相关工具类
 * @author wangjian-358
 */
public class SecurityUtil {

	private final static Logger LOG = LoggerFactory.getLogger(SecurityUtil.class);
	private final static String AES_KEY_16 = "f69ae5a048d1yfZX";

	//	public static void main(String[] args) throws Exception {
	//		String res = encodeAES("123456");
	//		System.out.println(res);
	//		System.out.println(decodeAES(res));
	//	}
	/**
	 * MD5 加密方法
	 * @param plainText 输入需要加密的字符串
	 * @return 加密后得到的字符串 2016年7月20日
	 */
	public static String md5(String plainText) {
		String result = null;
		// 首先判断是否为空
		if (StringUtils.isEmpty(plainText)) {
			return null;
		}
		try {
			// 首先进行实例化和初始化
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 得到一个操作系统默认的字节编码格式的字节数组
			byte[] btInput = plainText.getBytes();
			// 对得到的字节数组进行处理
			md.update(btInput);
			// 进行哈希计算并返回结果
			byte[] btResult = md.digest();
			// 进行哈希计算后得到的数据的长度
			StringBuffer sb = new StringBuffer();
			for (byte b : btResult) {
				int bt = b & 0xff;
				if (bt < 16) {
					sb.append(0);
				}
				sb.append(Integer.toHexString(bt));
			}
			result = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * sha1 加密方法
	 * @param inStr
	 * @return 2016年7月20日
	 */
	public static String sha1(String inStr) {
		MessageDigest sha = null;
		try {
			sha = MessageDigest.getInstance("SHA");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		byte[] byteArray = inStr.getBytes();
		byte[] md5Bytes = sha.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	/**
	 * @param content
	 * @return
	 * @throws Exception
	 * @Description AES 加密数据
	 */
	public static String encodeAES(String content) throws Exception {
		return encryptAES(content, AES_KEY_16);
	}

	/**
	 * @param content
	 * @return
	 * @throws Exception
	 * @Description AES 解密数据
	 */
	public static String decodeAES(String content) throws Exception {
		return decryptAES(content, AES_KEY_16);
	}

	/**
	 * @param content
	 * @param sKey : 密匙，密匙16位
	 * @return
	 * @throws Exception
	 * @Description AEC 加密
	 */
	public static String encryptAES(String content, String sKey) throws Exception {
		if (sKey == null) {
			LOG.error("Key为空null");
			return null;
		}
		// 判断Key是否为16位
		if (sKey.length() != 16) {
			LOG.error("Key[{}]长度不是16位", sKey);
			return null;
		}
		byte[] raw = sKey.getBytes("utf-8");
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(content.getBytes("utf-8"));
		return byte2Hex(encrypted);
	}

	/**
	 * @param content
	 * @param sKey
	 * @return
	 * @Description AES 解密算法
	 */
	public static String decryptAES(String content, String sKey) {
		try {
			if (sKey == null) {
				LOG.error("Key为空null");
				return null;
			}
			// 判断Key是否为16位
			if (sKey.length() != 16) {
				LOG.error("Key[{}]长度不是16位", sKey);
				return null;
			}
			byte[] raw = sKey.getBytes("utf-8");
			SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted1 = hex2Byte(content);
			try {
				byte[] original = cipher.doFinal(encrypted1);
				String originalString = new String(original, "utf-8");
				return originalString;
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				return null;
			}
		} catch (Exception ex) {
			LOG.error(ex.getMessage(), ex);
			return null;
		}
	}

	// byte[] 数据转16进制
	public static String byte2Hex(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	// 16进制转 byte[]
	public static byte[] hex2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
}
