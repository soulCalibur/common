/*
 * @Project Name: sns-web-utils
 * @File Name: GetLocationUtils.java
 * @Package Name: com.hhly.sns.util
 * @Date: 2017年2月13日下午3:24:26
 * @Creator: xuchuandi-394
 * @line------------------------------
 * @修改人:
 * @修改时间:
 * @修改内容:
 */

package com.ht.web.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description http://blog.csdn.net/u011072139/article/details/46428065
 * @author xuchuandi-394
 * @date 2017年2月13日下午3:24:26
 * @see
 */
public class LocationUtil {

	private static final Logger logger = LoggerFactory.getLogger(LocationUtil.class);

	public static void main(String[] args) {
		// lat 39.97646
		// log 116.3039
		String add = getAddrFull("114.023531", "22.546433");
		System.out.println(add);
	}

	/**
	 * @description 获取地址
	 * @date 2017年2月13日下午3:44:48
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @param log
	 * @param lat
	 * @return {"queryLocation":[39.97646,116.3039],"addrList":[{"type":"poi",
	 *         "status":1,"name":"海淀南路31号院1号楼","id":"ANB000A9UZ4E","admCode":
	 *         "110108","admName":"北京市,北京市,海淀区,","addr":"","nearestPoint":[116.
	 *         30395,39.97609],"distance":37.336}]}
	 */
	public static String getAddrFull(String log, String lat) {
		// lat 小 log 大
		// 参数解释: 纬度,经度 type 001 (100代表道路，010代表POI，001代表门址，111可以同时显示前三项)
		String urlString = "http://gc.ditu.aliyun.com/regeocoding?l=" + lat + "," + log + "&type=010";
		String res = "";
		java.io.BufferedReader in = null;
		try {
			URL url = new URL(urlString);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				res += line + "\n";
			}
			conn.disconnect();
		} catch (Exception e) {
			logger.error("error in wapaction,and e is ", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error("关闭资源异常", e);
				}
			}
		}
		return res;
	}

	/**
	 * @see #getAddrFull(String, String)
	 * @description TODO
	 * @date 2017年2月22日下午6:45:45
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @param log
	 * @param lat
	 * @return
	 */
	public static String getAddrFull(BigDecimal log, BigDecimal lat) {
		if (log == null || lat == null) {
			return null;
		}
		return getAddrFull(log.toString(), lat.toString());
	}


}
