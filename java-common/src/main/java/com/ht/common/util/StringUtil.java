
package com.ht.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 功能描述：关于字符串的一些实用操作
 * @author Administrator
 * @Date Jul 18, 2008
 * @Time 2:19:47 PM
 * @version 1.0
 */
public class StringUtil {

	final static char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B',
			'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
			'X', 'Y', 'Z' };
	/** 截取字符串长度 **/
	public static final int STRING_LENGTH = 120;

	/**
	 * 功能描述：分割字符串
	 * @param str String 原始字符串
	 * @param splitsign String 分隔符
	 * @return String[] 分割后的字符串数组
	 */
	public static String[] split(String str, String splitsign) {
		int index;
		if (str == null || splitsign == null) {
			return null;
		}
		ArrayList<String> al = new ArrayList<String>();
		while ((index = str.indexOf(splitsign)) != -1) {
			al.add(str.substring(0, index));
			str = str.substring(index + splitsign.length());
		}
		al.add(str);
		return (String[]) al.toArray(new String[0]);
	}

	/**
	 * 功能描述：替换字符串
	 * @param from String 原始字符串
	 * @param to String 目标字符串
	 * @param source String 母字符串
	 * @return String 替换后的字符串
	 */
	public static String replace(String from, String to, String source) {
		if (source == null || from == null || to == null)
			return null;
		StringBuffer str = new StringBuffer("");
		int index = -1;
		while ((index = source.indexOf(from)) != -1) {
			str.append(source.substring(0, index) + to);
			source = source.substring(index + from.length());
			index = source.indexOf(from);
		}
		str.append(source);
		return str.toString();
	}

	/**
	 * 替换字符串，能能够在HTML页面上直接显示(替换双引号和小于号)
	 * @param str String 原始字符串
	 * @return String 替换后的字符串
	 */
	public static String htmlencode(String str) {
		if (str == null) {
			return null;
		}
		return replace("\"", "&quot;", replace("<", "&lt;", str));
	}

	/**
	 * 替换字符串，将被编码的转换成原始码（替换成双引号和小于号）
	 * @param str String
	 * @return String
	 */
	public static String htmldecode(String str) {
		if (str == null) {
			return null;
		}
		return replace("&quot;", "\"", replace("&lt;", "<", str));
	}

	private static final String _BR = "<br/>";

	/**
	 * 功能描述：在页面上直接显示文本内容，替换小于号，空格，回车，TAB
	 * @param str String 原始字符串
	 * @return String 替换后的字符串
	 */
	public static String htmlshow(String str) {
		if (str == null) {
			return null;
		}
		str = replace("<", "&lt;", str);
		str = replace(" ", "&nbsp;", str);
		str = replace("\r\n", _BR, str);
		str = replace("\n", _BR, str);
		str = replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;", str);
		return str;
	}

	/**
	 * 功能描述：返回指定字节长度的字符串
	 * @param str String 字符串
	 * @param length int 指定长度
	 * @return String 返回的字符串
	 */
	public static String toLength(String str, int length) {
		if (str == null) {
			return null;
		}
		if (length <= 0) {
			return "";
		}
		try {
			if (str.getBytes("GBK").length <= length) {
				return str;
			}
		} catch (Exception e) {
		}
		StringBuffer buff = new StringBuffer();
		int index = 0;
		char c;
		length -= 3;
		while (length > 0) {
			c = str.charAt(index);
			if (c < 128) {
				length--;
			} else {
				length--;
				length--;
			}
			buff.append(c);
			index++;
		}
		buff.append("...");
		return buff.toString();
	}

	/**
	 * 功能描述：判断是否为整数
	 * @param str 传入的字符串
	 * @return 是整数返回true,否则返回false
	 */
	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断是否为浮点数，包括double和float
	 * @param str 传入的字符串
	 * @return 是浮点数返回true,否则返回false
	 */
	public static boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?\\d+\\.\\d+$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 判断是不是合法字符 c 要判断的字符
	 */
	public static boolean isLetter(String str) {
		if (str == null || str.length() < 0) {
			return false;
		}
		Pattern pattern = Pattern.compile("[\\w\\.-_]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 从指定的字符串中提取Email content 指定的字符串
	 * @param content
	 * @return
	 */
	public static String parse(String content) {
		String email = null;
		if (content == null || content.length() < 1) {
			return email;
		}
		// 找出含有@
		int beginPos;
		int i;
		String token = "@";
		String preHalf = "";
		String sufHalf = "";
		beginPos = content.indexOf(token);
		if (beginPos > -1) {
			// 前项扫描
			String s = null;
			i = beginPos;
			while (i > 0) {
				s = content.substring(i - 1, i);
				if (isLetter(s))
					preHalf = s + preHalf;
				else
					break;
				i--;
			}
			// 后项扫描
			i = beginPos + 1;
			while (i < content.length()) {
				s = content.substring(i, i + 1);
				if (isLetter(s))
					sufHalf = sufHalf + s;
				else
					break;
				i++;
			}
			// 判断合法性
			email = preHalf + "@" + sufHalf;
			if (isEmail(email)) {
				return email;
			}
		}
		return null;
	}

	/**
	 * 功能描述：判断输入的字符串是否符合Email样式.
	 * @param email 传入的字符串
	 * @return 是Email样式返回true,否则返回false
	 */
	public static boolean isEmail(String email) {
		if (email == null || email.length() < 1 || email.length() > 256) {
			return false;
		}
		Pattern pattern = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
		return pattern.matcher(email).matches();
	}

	/**
	 * 功能描述：判断输入的字符串是否为纯汉字
	 * @param str 传入的字符串
	 * @return 如果是纯汉字返回true,否则返回false
	 */
	public static boolean isChinese(String str) {
		Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
		return pattern.matcher(str).matches();
	}

	/**
	 * 功能描述：是否为空白,包括null和""
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return str == null || str.trim().length() == 0;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	/**
	 * 功能描述：判断是否为质数
	 * @param x
	 * @return
	 */
	public static boolean isPrime(int x) {
		if (x <= 7) {
			if (x == 2 || x == 3 || x == 5 || x == 7)
				return true;
		}
		int c = 7;
		if (x % 2 == 0)
			return false;
		if (x % 3 == 0)
			return false;
		if (x % 5 == 0)
			return false;
		int end = (int) Math.sqrt(x);
		while (c <= end) {
			if (x % c == 0) {
				return false;
			}
			c += 4;
			if (x % c == 0) {
				return false;
			}
			c += 2;
			if (x % c == 0) {
				return false;
			}
			c += 4;
			if (x % c == 0) {
				return false;
			}
			c += 2;
			if (x % c == 0) {
				return false;
			}
			c += 4;
			if (x % c == 0) {
				return false;
			}
			c += 6;
			if (x % c == 0) {
				return false;
			}
			c += 2;
			if (x % c == 0) {
				return false;
			}
			c += 6;
		}
		return true;
	}

	/**
	 * 功能描述：人民币转成大写
	 * @param str 数字字符串
	 * @return String 人民币转换成大写后的字符串
	 */
	public static String hangeToBig(String str) {
		double value;
		try {
			value = Double.parseDouble(str.trim());
		} catch (Exception e) {
			return null;
		}
		char[] hunit = { '拾', '佰', '仟' }; // 段内位置表示
		char[] vunit = { '万', '亿' }; // 段名表示
		char[] digit = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' }; // 数字表示
		long midVal = (long) (value * 100); // 转化成整形
		String valStr = String.valueOf(midVal); // 转化成字符串
		String head = valStr.substring(0, valStr.length() - 2); // 取整数部分
		String rail = valStr.substring(valStr.length() - 2); // 取小数部分
		String prefix = ""; // 整数部分转化的结果
		String suffix = ""; // 小数部分转化的结果
		// 处理小数点后面的数
		if (rail.equals("00")) { // 如果小数部分为0
			suffix = "整";
		} else {
			suffix = digit[rail.charAt(0) - '0'] + "角" + digit[rail.charAt(1) - '0'] + "分"; // 否则把角分转化出来
		}
		// 处理小数点前面的数
		char[] chDig = head.toCharArray(); // 把整数部分转化成字符数组
		char zero = '0'; // 标志'0'表示出现过0
		byte zeroSerNum = 0; // 连续出现0的次数
		for (int i = 0; i < chDig.length; i++) { // 循环处理每个数字
			int idx = (chDig.length - i - 1) % 4; // 取段内位置
			int vidx = (chDig.length - i - 1) / 4; // 取段位置
			if (chDig[i] == '0') { // 如果当前字符是0
				zeroSerNum++; // 连续0次数递增
				if (zero == '0') { // 标志
					zero = digit[0];
				} else if (idx == 0 && vidx > 0 && zeroSerNum < 4) {
					prefix += vunit[vidx - 1];
					zero = '0';
				}
				continue;
			}
			zeroSerNum = 0; // 连续0次数清零
			if (zero != '0') { // 如果标志不为0,则加上,例如万,亿什么的
				prefix += zero;
				zero = '0';
			}
			prefix += digit[chDig[i] - '0']; // 转化该数字表示
			if (idx > 0)
				prefix += hunit[idx - 1];
			if (idx == 0 && vidx > 0) {
				prefix += vunit[vidx - 1]; // 段结束位置应该加上段名如万,亿
			}
		}
		if (prefix.length() > 0)
			prefix += '圆'; // 如果整数部分存在,则有圆的字样
		return prefix + suffix; // 返回正确表示
	}

	/**
	 * 功能描述：去掉字符串中重复的子字符串
	 * @param str 原字符串，如果有子字符串则用空格隔开以表示子字符串
	 * @return String 返回去掉重复子字符串后的字符串
	 */
	@SuppressWarnings("unused")
	private static String removeSameString(String str) {
		Set<String> mLinkedSet = new LinkedHashSet<String>();// set集合的特征：其子集不可以重复
		String[] strArray = str.split(" ");// 根据空格(正则表达式)分割字符串
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < strArray.length; i++) {
			if (!mLinkedSet.contains(strArray[i])) {
				mLinkedSet.add(strArray[i]);
				sb.append(strArray[i] + " ");
			}
		}
		System.out.println(mLinkedSet);
		return sb.toString();
	}

	/**
	 * 功能描述：过滤特殊字符
	 * @param src
	 * @return
	 */
	public static String encoding(String src) {
		if (StringUtils.isEmpty(src)) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		src = src.trim();
		for (int pos = 0; pos < src.length(); pos++) {
			switch (src.charAt(pos)) {
				case '\"':
					result.append("&quot;");
					break;
				case '<':
					result.append("&lt;");
					break;
				case '>':
					result.append("&gt;");
					break;
				case '\'':
					result.append("&apos;");
					break;
				case '&':
					result.append("&amp;");
					break;
				case '%':
					result.append("&pc;");
					break;
				case '_':
					result.append("&ul;");
					break;
				case '#':
					result.append("&shap;");
					break;
				case '?':
					result.append("&ques;");
					break;
				default:
					result.append(src.charAt(pos));
					break;
			}
		}
		return result.toString();
	}

	/**
	 * 功能描述：判断是不是合法的手机号码
	 * @param handset
	 * @return boolean
	 */
	public static boolean isHandset(String handset) {
		try {
			String regex = "^1[\\d]{10}$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(handset);
			return matcher.matches();
		} catch (RuntimeException e) {
			return false;
		}
	}

	/**
	 * 功能描述：反过滤特殊字符
	 * @param src
	 * @return
	 */
	public static String decoding(String src) {
		if (src == null)
			return "";
		String result = src;
		result = result.replace("&quot;", "\"").replace("&apos;", "\'");
		result = result.replace("&lt;", "<").replace("&gt;", ">");
		result = result.replace("&amp;", "&");
		result = result.replace("&pc;", "%").replace("&ul", "_");
		result = result.replace("&shap;", "#").replace("&ques", "?");
		return result;
	}

	public static String gZip(String input) {
		byte[] bytes = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(bos);
			gzip.write(input.getBytes("utf-8"));
			gzip.finish();
			gzip.close();
			bytes = bos.toByteArray();
			bos.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return Base64Util.encode(bytes);
	}

	/***
	 * 解压GZip
	 * @param input
	 * @return
	 */
	public static String unGZip(String input) {
		if (isBlank(input)) {
			return input;
		}
		byte[] bytes = null;
		String out = "";
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(Base64Util.decodeBuffer(input));
			GZIPInputStream gzip = new GZIPInputStream(bis);
			byte[] buf = new byte[1024];
			int num = -1;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((num = gzip.read(buf, 0, buf.length)) != -1) {
				baos.write(buf, 0, num);
			}
			bytes = baos.toByteArray();
			baos.flush();
			baos.close();
			gzip.close();
			bis.close();
			out = new String(bytes, "utf-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return out;
	}

	/**
	 * 判断两个字符串是否不相同
	 * @param source
	 * @param target
	 * @return
	 */
	public static boolean notEquals(String source, String target) {
		if (source == null && target == null)
			return false;
		if (source == null && target != null)
			return false;
		if (source != null && target == null)
			return false;
		return !source.equals(target);
	}

	/**
	 * 按指定长度生成随机字符串 2016年8月17日
	 * @param length
	 * @return
	 */
	public static String generator(int length) {
		Random random = new Random();
		char[] cs = new char[length];
		for (int i = 0; i < cs.length; i++) {
			cs[i] = digits[random.nextInt(digits.length)];
		}
		return new String(cs);
	}

	/**
	 * 利用正则表达式将每个中文字符转换为"**" 匹配中文字符的正则表达式： [\u4e00-\u9fa5]
	 * 匹配双字节字符(包括汉字在内)：[^\x00-\xff]
	 * @param validateStr
	 * @return
	 */
	public static int getTrueLength(String validateStr) {
		String temp = validateStr.replaceAll("[^\\x00-\\xff]", "**");
		return temp.length();
	}

	/**
	 * @方法功能描述 截取指定长度的字符串并在结尾以 ... 填充
	 * @创建时间 2016年9月6日下午1:14:38
	 * @param str
	 * @param len
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getLimitLength(String str, int len) throws UnsupportedEncodingException {
		if (isBlank(str)) {
			return str;
		}
		String symbol = "...";
		String encode = "GBK";
		int counterOfDoubleByte = 0;
		byte[] b = str.getBytes(encode);
		if (b.length <= len)
			return str;
		for (int i = 0; i < len; i++) {
			if (b[i] < 0) {
				counterOfDoubleByte++;
			}
		}
		if (counterOfDoubleByte % 2 == 0)
			return new String(b, 0, len, encode) + symbol;
		else
			return new String(b, 0, len - 1, encode) + symbol;
	}

	/**
	 * 获取随机数
	 * @param length
	 * @return
	 */
	public static int getRandom(int length) {
		if (length <= 0)
			return 0;
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}

	public static String hidePhone(String phone) {
		if (StringUtils.isEmpty(phone) || phone.length() != 11)
			return phone;
		try {
			return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
		} catch (Exception e) {
			return phone;
		}
	}

	/**
	 * 校验手机号码是否合法
	 * @param phone
	 * @return
	 */
	public static boolean validPhone(String phone) {
		return phone.matches("^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])\\d{8}$");
	}

	/**
	 * null转空字符串 redis会用到
	 * @param str
	 * @return
	 */
	public static String nullToBlank(String str) {
		if (isBlank(str)) {
			return "";
		}
		return str;
	}

	/*
	 * @param str 要转换的字符串
	 * @return 首字符为大写的字符串，如果原字符串为 <code>null</code> ，则返回 <code>null</code>
	 */
	public static String capitalize(String str) {
		int strLen;
		if ((str == null) || ((strLen = str.length()) == 0)) {
			return str;
		}
		return new StringBuffer(strLen).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1))
				.toString();
	}

	/**
	 * 根据分隔符翻转字符串
	 * @description TODO
	 * @date 2016年11月23日下午4:51:22
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @param str
	 * @param split
	 * @return
	 */
	public static String reversalStr(final String str, String split) {
		if (StringUtil.isBlank(str) || StringUtil.isBlank(split)) {
			return str;
		}
		String[] ss = str.split(split);
		if (ss.length == 0) {
			return str;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = ss.length - 1; i >= 0; i--) {
			sb.append(ss[i] + "-");
		}
		return sb.toString().substring(0, sb.length() - 1);
	}

	/**
	 * 电话号码验证
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) {
		Pattern p1 = null, p2 = null;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		return b;
	}

	/**
	 * 手机号验证
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	/**
	 * @description 两个字符串是否相同
	 * @date 2016年12月5日下午4:39:21
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @param str1
	 * @param str2
	 * @return 相同 false 不相同 true
	 */
	public static boolean isNotEquals(String str1, String str2) {
		if (StringUtil.isBlank(str1) && StringUtil.isBlank(str2)) {
			return false;
		}
		if (StringUtil.isNotBlank(str1)) {
			return !str1.equals(str2);
		} else {
			return str2.equals(str1);
		}
	}

	/**
	 * @description 去除所有的html元素，并截取前N个字符
	 * @date 2016年12月10日上午10:11:35
	 * @author TanShen-519
	 * @since 1.0.0
	 * @param input
	 * @return
	 */
	public static String removeHTMLTag(String input, int length) {
		if (input == null || StringUtils.EMPTY.equals(input.trim())) {
			return StringUtils.EMPTY;
		}
		// 去掉所有html元素
		String str = StringEscapeUtils.unescapeHtml4(input).replaceAll("<[^>]*>", StringUtils.EMPTY);
		int len = str.length();
		if (len <= length || len <= 3) {
			return str;
		} else {
			str = str.substring(0, length - 3);
			return StringUtils.join(str, "...");
		}
	}

	/**
	 * @description 过滤html字符串中图片路径，返回路径List
	 * @date 2017年1月4日上午10:03:11
	 * @author tanshen-519
	 * @since 1.0.0
	 * @param content
	 * @return
	 */
	public static List<String> filterImg(String content) {
		List<String> list = new ArrayList<String>();
		String regex = "<img(.*?)src=[\'\"](.*?)[\'\"](.*?)/>";
		Matcher m = Pattern.compile(regex).matcher(content == null ? "" : content);
		while (m.find()) {
			list.add(m.group(2));
		}
		return list;
	}

	/**
	 * @description 过滤html字符串中视频路径，返回视频路径List
	 * @date 2017年1月4日上午10:04:24
	 * @author tanshen-519
	 * @since 1.0.0
	 * @param content
	 * @return
	 */
	public static List<String> filterVideo(String content) {
		List<String> list = new ArrayList<String>();
		String regex = "<video(.*?)src=[\'\"](.*?)[\'\"](.*?)>";
		Matcher m = Pattern.compile(regex).matcher(content == null ? "" : content);
		while (m.find()) {
			list.add(m.group(2));
		}
		return list;
	}

	/**
	 * @author: wangjian-358
	 * @date: 2017/2/27 13:25
	 * @description: 判断一个字符串是否为一个绝对 http 地址
	 * @param url
	 * @return
	 */
	public static boolean isHttpUrl(String url) {
		if (StringUtils.isBlank(url)) {
			return false;
		}
		if (url.length() > 7 && url.substring(7).toLowerCase() == "http://") {
			return true;
		}
		return false;
	}

	/**
	 * @param args
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		String result = "<video class='edui-upload-video video-js vjs-default-skin video-js' controls='' preload='none' width='420' height='280' src='http://192.168.10.115:5088/files/article/video/276ac79cc26b4d8b99d0c0cca286b487.mp4' data-setup='{}'><source src='http://192.168.10.115:5088/files/article/video/276ac79cc26b4d8b99d0c0cca286b487.mp4' type='video/mp4'></video>";
		;
		String regex = "<video(.*?)src=[\'\"](.*?)[\'\"](.*?)>";
		Matcher m = Pattern.compile(regex).matcher(result == null ? "" : result);
		while (m.find()) {
			// imgSrc.add(m.group(1));
			System.out.println(m.group(2));
		}
	}
}
