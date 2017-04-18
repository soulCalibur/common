
package com.ht.common.util;

/**
 * @author bb.h
 * @时间：2016-11-14 下午3:26:28
 * @说明: 坐标操作工具类
 */
public class DistanceUtil {

	private static final double EARTH_RADIUS = 6371000;// 赤道半径(单位m)

	/**
	 * 转化为弧度(rad)
	 */
	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 基于googleMap中的算法得到两经纬度之间的距离,计算精度与谷歌地图的距离精度差不多，相差范围在0.2米以下
	 * @param lon1 第一点的精度
	 * @param lat1 第一点的纬度
	 * @param lon2 第二点的精度
	 * @param lat3 第二点的纬度
	 * @return 返回的距离，单位m
	 */
	public static double getDistance(double lon1, double lat1, double lon2, double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lon1) - rad(lon2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	/**
	 * 生成以中心点为中心的四方形经纬度
	 * @param lat 纬度
	 * @param lon 精度
	 * @param raidus 半径（以米为单位）
	 * @return
	 */
	public static double[] getAround(double lat, double lon, int raidus) {
		Double latitude = lat;
		Double longitude = lon;
		Double degree = (24901 * 1609) / 360.0;
		double raidusMile = raidus;
		Double dpmLat = 1 / degree;
		Double radiusLat = dpmLat * raidusMile;
		Double minLat = latitude - radiusLat;
		Double maxLat = latitude + radiusLat;
		Double mpdLng = degree * Math.cos(latitude * (Math.PI / 180));
		Double dpmLng = 1 / mpdLng;
		Double radiusLng = dpmLng * raidusMile;
		Double minLng = longitude - radiusLng;
		Double maxLng = longitude + radiusLng;
		return new double[] { minLat, minLng, maxLat, maxLng };
	}

	public static void main(String[] args) {
		double lon1 = 109.0145193757;
		double lat1 = 34.236080797698;
		double lon2 = 108.9644583556;
		double lat2 = 34.286439088548;
		double dist;
		String geocode;
		dist = getDistance(lon1, lat1, lon2, lat2);
		System.out.println("两点相距：" + dist + " 米");
		Geohash geohash = new Geohash();
		geocode = geohash.encode(lat1, lon1);
		System.out.println("当前位置编码：" + geocode);
		geocode = geohash.encode(lat2, lon2);
		System.out.println("远方位置编码：" + geocode);
	}
}
