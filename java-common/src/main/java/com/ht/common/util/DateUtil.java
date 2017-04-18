
package com.ht.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述：完成与日期相关的各种操作
 * <p>
 * 包括将日期格式化、从字符串中解析出对应的日期、对日期的加减操作等
 * @author maluming 2011-4-14
 * @see
 * @since 1.0
 */
public class DateUtil {

	private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);
	public static final String YYYYMMDD = "yyyy-MM-dd";
	public static final String YYYYMMDD_ZH = "yyyy年MM月dd日";
	public static final int FIRST_DAY_OF_WEEK = Calendar.MONDAY; // 中国周一是一周的第一天
	public static final String YYYYMMDD_TZ = "yyyy-MM-dd'T'HH:mm:ss'Z'";// RFC3339

	/**
	 * 功能描述：按照给出格式解析出日期
	 * @param dateStr String 字符型日期
	 * @param format String 格式
	 * @return Date 日期
	 */
	public static Date parseDate(String dateStr, String format) {
		Date date = null;
		try {
			date = FastDateFormat.getInstance(format).parse(dateStr);
		} catch (ParseException e) {
			logger.error("日期解析异常：", e);
		}
		return date;
	}

	/**
	 * 功能描述：格式化日期
	 * @param dateStr String 字符型日期：YYYY-MM-DD 格式
	 * @return Date
	 */
	public static Date parseDate(String dateStr) {
		return parseDate(dateStr, YYYYMMDD);
	}

	/**
	 * 功能描述：格式化输出日期
	 * @param date Date 日期
	 * @param format String 格式
	 * @return 字符型日期
	 */
	public static String format(Date date) {
		return format(date, YYYYMMDD);
	}

	/**
	 * 功能描述：格式化输出日期 RFC3339格式
	 * @param date Date 日期
	 * @param format String 格式
	 * @return 字符型日期
	 */
	public static String formatRFC3339(Date date) {
		return format(date, YYYYMMDD_TZ);
	}

	/**
	 * 功能描述：格式化输出日期
	 * @param date Date 日期
	 * @param format String 格式
	 * @return 字符型日期
	 */
	public static String format(Date date, String format) {
		String result = StringUtils.EMPTY;
		try {
			if (date != null) {
				result = DateFormatUtils.format(date, format);
			}
		} catch (RuntimeException e) {
			logger.error("日期格式出错：", e);
		}
		return result;
	}

	/**
	 * 功能描述：返回字符型日期
	 * @param date 日期
	 * @return 返回字符型日期 yyyy/MM/dd 格式
	 */
	public static String getDate(Date date) {
		return format(date, "yyyy/MM/dd");
	}

	/**
	 * 功能描述：返回字符型时间
	 * @param date Date 日期
	 * @return 返回字符型时间 HH:mm:ss 格式
	 */
	public static String getTime(Date date) {
		return format(date, "HH:mm:ss");
	}

	/**
	 * 功能描述：返回字符型日期时间
	 * @param date Date 日期
	 * @return 返回字符型日期时间 yyyy/MM/dd HH:mm:ss 格式
	 */
	public static String getDateTime(Date date) {
		return format(date, "yyyy/MM/dd HH:mm:ss");
	}

	public static String getMillisDateTime(Date date) {
		return format(date, "yyyy/MM/dd HH:mm:ss.SSS");
	}

	/**
	 * 功能描述：取得指定月份的第一天
	 * @param strdate String 字符型日期
	 * @return String yyyy-MM-dd 格式
	 */
	public static String getMonthBegin(String strdate) {
		Date date = parseDate(strdate);
		return format(date, "yyyy-MM") + "-01";
	}

	/**
	 * 功能描述：常用的格式化日期
	 * @param date Date 日期
	 * @return String 日期字符串 yyyy-MM-dd格式
	 */
	public static String formatDate(Date date) {
		return formatDateByFormat(date, "yyyy-MM-dd");
	}

	/**
	 * 功能描述：以指定的格式来格式化日期
	 * @param date Date 日期
	 * @param format String 格式
	 * @return String 日期字符串
	 */
	public static String formatDateByFormat(Date date, String format) {
		String result = "";
		if (date != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				result = sdf.format(date);
			} catch (Exception ex) {
			}
		}
		return result;
	}

	/**
	 * @param date
	 * @param time
	 * @param fromDate
	 * @return
	 * @description 日期转换
	 * @date 2017年3月6日上午9:52:37
	 * @author zengly-507
	 * @since 1.0.0
	 */
	public static Date timeToDate(String date, String time, String fromDate) {
		Date result = null;
		if (StringUtils.isNoneBlank(time)) {
			String startTime = StringUtils.join(date, " ", time);
			result = DateUtil.parseDate(startTime, fromDate);
		}
		return result;
	}

	/**
	 * 计算2个日期之间的相隔天数
	 * @param d1 日期1
	 * @param d2 日期2
	 * @return 日期1和日期2相隔天数
	 */
	public static int getDaysBetween(Calendar d1, Calendar d2) {
		if (d1.after(d2)) {
			// swap dates so that d1 is start and d2 is end
			Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
		int y2 = d2.get(Calendar.YEAR);
		if (d1.get(Calendar.YEAR) != y2) {
			d1 = (Calendar) d1.clone();
			do {
				days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
				d1.add(Calendar.YEAR, 1);
			} while (d1.get(Calendar.YEAR) != y2);
		}
		return days;
	}

	/**
	 * 计算2个日期之间的工作天数（去除周六周日）
	 * @param d1 日期1
	 * @param d2 日期2
	 * @return 日期1和日期2之间的工作天数
	 */
	public int getWorkingDay(Calendar d1, Calendar d2) {
		int result = -1;
		if (d1.after(d2)) {
			// swap dates so that d1 is start and d2 is end
			Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		// int betweendays = getDaysBetween(d1, d2);
		// int charge_date = 0;
		// 开始日期的日期偏移量
		int charge_start_date = 0;
		// 结束日期的日期偏移量
		int charge_end_date = 0;
		int stmp;
		int etmp;
		stmp = 7 - d1.get(Calendar.DAY_OF_WEEK);
		etmp = 7 - d2.get(Calendar.DAY_OF_WEEK);
		// 日期不在同一个日期内
		if (stmp != 0 && stmp != 6) {// 开始日期为星期六和星期日时偏移量为0
			charge_start_date = stmp - 1;
		}
		if (etmp != 0 && etmp != 6) {// 结束日期为星期六和星期日时偏移量为0
			charge_end_date = etmp - 1;
		}
		// }
		result = (getDaysBetween(this.getNextMonday(d1), this.getNextMonday(d2)) / 7) * 5 + charge_start_date
				- charge_end_date;
		// System.out.println("charge_start_date>" + charge_start_date);
		// System.out.println("charge_end_date>" + charge_end_date);
		// System.out.println("between day is-->" + betweendays);
		return result;
	}

	/**
	 * 获取当前星期
	 * @param date 当前日期
	 * @param character zh : 标识中文 ， en : 标识英文（默认）
	 * @return 当前日期
	 */
	public static String getChineseWeek(Calendar date, String character) {
		String dayNames[] = { "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" };
		if ("zh".equals(character)) {
			dayNames[0] = "星期日";
			dayNames[1] = "星期一";
			dayNames[2] = "星期二";
			dayNames[3] = "星期三";
			dayNames[4] = "星期四";
			dayNames[5] = "星期五";
			dayNames[6] = "星期六";
		}
		int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
		// System.out.println(dayNames[dayOfWeek - 1]);
		return dayNames[dayOfWeek - 1];
	}

	/**
	 * @description 获取当前日期-星期几
	 * @date 2016年12月13日下午4:13:35
	 * @author guoya-420
	 * @since 1.0.0
	 * @param date
	 * @param character
	 * @param format
	 * @return
	 */
	public static String getDateAndWeekDay(Date date, String character, String format) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 星期几
		String weekDay = getChineseWeek(cal, character);
		String currentDate = format(date, format);
		String dateAndWeek = currentDate + " " + weekDay;
		return dateAndWeek;
	}

	/**
	 * 获得日期的下一个星期一的日期
	 * @param date
	 * @return
	 */
	public Calendar getNextMonday(Calendar date) {
		Calendar result = null;
		result = date;
		do {
			result = (Calendar) result.clone();
			result.add(Calendar.DATE, 1);
		} while (result.get(Calendar.DAY_OF_WEEK) != 2);
		return result;
	}

	/**
	 * 计算两个日期之间的非工作日天数
	 * @param d1 日期1
	 * @param d2 日期2
	 * @return 日期1与日期2之间的非工作天数
	 */
	public int getHolidays(Calendar d1, Calendar d2) {
		return getDaysBetween(d1, d2) - this.getWorkingDay(d1, d2);
	}

	/**
	 * 日期相加减
	 * @param date
	 * @param day 可以是负数
	 * @return
	 */
	public static Date addDay(Date date, int day) {
		if (date == null)
			date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, day);
		return calendar.getTime();
	}

	/**
	 * @description 加上分钟数
	 * @date 2017年1月18日下午12:20:04
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @param date
	 * @param minute
	 * @return
	 */
	public static Date addMinute(Date date, int minute) {
		if (date == null)
			date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minute);
		return calendar.getTime();
	}

	/**
	 * 格式化solr时间
	 * @param date
	 * @return
	 */
	public static String formatSolrDate(Date date) {
		if (date == null) {
			date = new Date();
		}
		return format(date, "yyyyMMddHHmmss");
	}

	/**
	 * @description 获取当前日期
	 * @date 2016年11月24日下午5:43:05
	 * @author guoya-420
	 * @since 1.0.0
	 * @return
	 */
	public static Date getDay() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date time = null;
		try {
			time = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * @description 获取系统当前时间
	 * @date 2016年11月25日上午10:03:47
	 * @author zengly507
	 * @since 1.0.0
	 * @return
	 */
	public static String getCurrData(String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);// 设置日期格式
		return df.format(new Date());
	}

	public static String getCurrData() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
		return df.format(new Date());
	}

	/**
	 * 获得指定日期的前一天
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayBefore(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day - 1);
		String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayBefore;
	}

	/**
	 * 获得指定日期的后一天
	 * @param specifiedDay
	 * @return
	 */
	public static String getSpecifiedDayAfter(String specifiedDay) {
		Calendar c = Calendar.getInstance();
		Date date = null;
		try {
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		c.setTime(date);
		int day = c.get(Calendar.DATE);
		c.set(Calendar.DATE, day + 1);
		String dayAfter = new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
		return dayAfter;
	}

	/**
	 * @description 根据当前日期 获取这周日
	 * @date 2016年11月25日下午12:07:44
	 * @author zengly507
	 * @since 1.0.0
	 * @param specifiedDay
	 * @return
	 * @throws ParseException
	 */
	public static String getWeekdaysLast(String specifiedDay) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		Calendar cal = Calendar.getInstance();
		Date time = sdf.parse(specifiedDay);
		cal.setTime(time);
		System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		cal.add(Calendar.DATE, 6);
		return sdf.format(cal.getTime());
	}

	/**
	 * @description 获取当前日期所在周的周一
	 * @date 2016年11月25日下午12:13:00
	 * @author zengly507
	 * @since 1.0.0
	 * @param specifiedDay
	 * @return
	 * @throws ParseException
	 */
	public static String getWeekdaysFist(String specifiedDay) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		Calendar cal = Calendar.getInstance();
		Date time = sdf.parse(specifiedDay);
		cal.setTime(time);
		System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		return sdf.format(cal.getTime());
	}

	/**
	 * @description 获取当前日期上周的周一日期
	 * @date 2016年11月25日下午12:13:00
	 * @author zengly507
	 * @since 1.0.0
	 * @param specifiedDay
	 * @return
	 * @throws ParseException
	 */
	public static String getWeekdaysLastFist(String specifiedDay) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		Calendar cal = Calendar.getInstance();
		Date time = sdf.parse(specifiedDay);
		cal.setTime(time);
		System.out.println("要计算日期为:" + sdf.format(cal.getTime())); // 输出要计算日期
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day - 7);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		return sdf.format(cal.getTime());
	}

	/**
	 * format date
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String formatDate(Date date, String pattern) {
		String strDate = null;
		try {
			if (pattern == null) {
				pattern = YYYYMMDD;
			}
			SimpleDateFormat format = new SimpleDateFormat(pattern);
			strDate = format.format(date);
		} catch (Exception e) {
		}
		return strDate;
	}

	/**
	 * 取得日期：年
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int year = c.get(Calendar.YEAR);
		return year;
	}

	/**
	 * 取得日期：月
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		return month + 1;
	}

	/**
	 * 取得日期：年
	 * @param date
	 * @return
	 */
	public static int getDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int da = c.get(Calendar.DAY_OF_MONTH);
		return da;
	}

	/**
	 * 取得当天日期是周几
	 * @param date
	 * @return
	 */
	public static int getWeekDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int week_of_year = c.get(Calendar.DAY_OF_WEEK);
		return week_of_year - 1;
	}

	/**
	 * 取得一年的第几周
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int week_of_year = c.get(Calendar.WEEK_OF_YEAR);
		return week_of_year;
	}

	/**
	 * getWeekBeginAndEndDate
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getWeekBeginAndEndDate(Date date, String pattern) {
		Date monday = getMondayOfWeek(date);
		Date sunday = getSundayOfWeek(date);
		return formatDate(monday, pattern) + " - " + formatDate(sunday, pattern);
	}

	/**
	 * 根据日期取得对应周周一日期
	 * @param date
	 * @return
	 */
	public static Date getMondayOfWeek(Date date) {
		Calendar monday = Calendar.getInstance();
		monday.setTime(date);
		monday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
		monday.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return monday.getTime();
	}

	/**
	 * 根据日期取得对应周周日日期
	 * @param date
	 * @return
	 */
	public static Date getSundayOfWeek(Date date) {
		Calendar sunday = Calendar.getInstance();
		sunday.setTime(date);
		sunday.setFirstDayOfWeek(FIRST_DAY_OF_WEEK);
		sunday.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		return sunday.getTime();
	}

	/**
	 * 取得月的剩余天数
	 * @param date
	 * @return
	 */
	public static int getRemainDayOfMonth(Date date) {
		int dayOfMonth = getDayOfMonth(date);
		int day = getPassDayOfMonth(date);
		return dayOfMonth - day;
	}

	/**
	 * 取得月已经过的天数
	 * @param date
	 * @return
	 */
	public static int getPassDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取得月天数
	 * @param date
	 * @return
	 */
	public static int getDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取得月第一天
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	/**
	 * 取得月最后一天
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	/**
	 * 取得季度第一天
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfSeason(Date date) {
		return getFirstDateOfMonth(getSeasonDate(date)[0]);
	}

	/**
	 * 取得季度最后一天
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfSeason(Date date) {
		return getLastDateOfMonth(getSeasonDate(date)[2]);
	}

	/**
	 * 取得季度天数
	 * @param date
	 * @return
	 */
	public static int getDayOfSeason(Date date) {
		int day = 0;
		Date[] seasonDates = getSeasonDate(date);
		for (Date date2 : seasonDates) {
			day += getDayOfMonth(date2);
		}
		return day;
	}

	/**
	 * 取得季度剩余天数
	 * @param date
	 * @return
	 */
	public static int getRemainDayOfSeason(Date date) {
		return getDayOfSeason(date) - getPassDayOfSeason(date);
	}

	/**
	 * 取得季度已过天数
	 * @param date
	 * @return
	 */
	public static int getPassDayOfSeason(Date date) {
		int day = 0;
		Date[] seasonDates = getSeasonDate(date);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		if (month == Calendar.JANUARY || month == Calendar.APRIL || month == Calendar.JULY || month == Calendar.OCTOBER) {// 季度第一个月
			day = getPassDayOfMonth(seasonDates[0]);
		} else if (month == Calendar.FEBRUARY || month == Calendar.MAY || month == Calendar.AUGUST
				|| month == Calendar.NOVEMBER) {// 季度第二个月
			day = getDayOfMonth(seasonDates[0]) + getPassDayOfMonth(seasonDates[1]);
		} else if (month == Calendar.MARCH || month == Calendar.JUNE || month == Calendar.SEPTEMBER
				|| month == Calendar.DECEMBER) {// 季度第三个月
			day = getDayOfMonth(seasonDates[0]) + getDayOfMonth(seasonDates[1]) + getPassDayOfMonth(seasonDates[2]);
		}
		return day;
	}

	/**
	 * 取得季度月
	 * @param date
	 * @return
	 */
	public static Date[] getSeasonDate(Date date) {
		Date[] season = new Date[3];
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int nSeason = getSeason(date);
		if (nSeason == 1) {// 第一季度
			c.set(Calendar.MONTH, Calendar.JANUARY);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.FEBRUARY);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.MARCH);
			season[2] = c.getTime();
		} else if (nSeason == 2) {// 第二季度
			c.set(Calendar.MONTH, Calendar.APRIL);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.MAY);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.JUNE);
			season[2] = c.getTime();
		} else if (nSeason == 3) {// 第三季度
			c.set(Calendar.MONTH, Calendar.JULY);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.AUGUST);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.SEPTEMBER);
			season[2] = c.getTime();
		} else if (nSeason == 4) {// 第四季度
			c.set(Calendar.MONTH, Calendar.OCTOBER);
			season[0] = c.getTime();
			c.set(Calendar.MONTH, Calendar.NOVEMBER);
			season[1] = c.getTime();
			c.set(Calendar.MONTH, Calendar.DECEMBER);
			season[2] = c.getTime();
		}
		return season;
	}

	/**
	 * 1 第一季度 2 第二季度 3 第三季度 4 第四季度
	 * @param date
	 * @return
	 */
	public static int getSeason(Date date) {
		int season = 0;
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		switch (month) {
			case Calendar.JANUARY:
			case Calendar.FEBRUARY:
			case Calendar.MARCH:
				season = 1;
				break;
			case Calendar.APRIL:
			case Calendar.MAY:
			case Calendar.JUNE:
				season = 2;
				break;
			case Calendar.JULY:
			case Calendar.AUGUST:
			case Calendar.SEPTEMBER:
				season = 3;
				break;
			case Calendar.OCTOBER:
			case Calendar.NOVEMBER:
			case Calendar.DECEMBER:
				season = 4;
				break;
			default:
				break;
		}
		return season;
	}

	/**
	 * @description 获取两个时间之间的小时差 PS:计算错误 修改为double getTimeDiff方法
	 * @date 2016年12月7日下午6:03:09
	 * @author guoya-420
	 * @since 1.0.0
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static double getDiffHour(String startDate, String endDate) {
		Date date1 = DateUtil.parseDate(startDate, "HH:mm");
		Date date2 = DateUtil.parseDate(endDate, "HH:mm");
		long diff = date2.getTime() - date1.getTime();
		double diffHours = diff / ((double) 60 * 60 * 1000);
		return diffHours;
	}

	/**
	 * @description 获取time的时间差
	 * @date 2017-2-16上午10:15:21
	 * @author bb.h
	 * @since 1.0.0
	 * @param startDate 起始时间 格式HH:mm
	 * @param endDate 结束时间 格式HH:mm
	 * @param mode Calendar.MINUTE Calendar.HOUR
	 * @return Calendar.MINUTE分钟差 Calendar.HOUR小时差
	 */
	public static double getTimeDiff(String startDate, String endDate, int mode) {
		Date date1 = DateUtil.parseDate(startDate, "HH:mm");
		Date date2 = DateUtil.parseDate(endDate, "HH:mm");
		return getTimeDiff(date1, date2, mode);
	}

	/**
	 * @description 获取date的时间差
	 * @date 2017-2-16上午10:35:44
	 * @author bb.h
	 * @since 1.0.0
	 * @param startDate 起始时间
	 * @param endDate 结束时间
	 * @param mode Calendar.MINUTE Calendar.HOUR
	 * @return Calendar.MINUTE分钟差 Calendar.HOUR小时差
	 */
	public static double getTimeDiff(Date startDate, Date endDate, int mode) {
		double diff = endDate.getTime() - startDate.getTime();
		if (mode == Calendar.MINUTE) {
			return diff / (60 * 1000);
		} else if (mode == Calendar.HOUR) {
			return diff / (60 * 60 * 1000);
		}
		return diff;
	}

	/**
	 * @description 两个时间的间隔秒数
	 * @date 2017年1月6日下午6:14:17
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @param startDate
	 * @param endDate
	 * @return 正整数
	 */
	public static int getDiffSecond(Date startDate, Date endDate) {
		long interval = (endDate.getTime() - startDate.getTime()) / 1000;
		return Math.abs((int) interval);
	}

	/**
	 * @description 获取当天的开始时间
	 * @date 2016年12月9日下午6:01:24
	 * @author TanShen-519
	 * @since 1.0.0
	 * @return
	 */
	public static Date getTodayStarTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date start = calendar.getTime();
		return start;
	}

	/**
	 * @description 获取当天的结束时间
	 * @date 2016年12月9日下午6:01:54
	 * @author TanShen-519
	 * @since 1.0.0
	 * @return
	 */
	public static Date getTodayLasTime() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.SECOND, -1);
		Date end = calendar.getTime();
		return end;
	}

	/**
	 * @description 获取当前的时间
	 * @date 2016年12月21日下午12:18:38
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @return
	 */
	public static Date getDateTime() {
		return new Date();
	}

	/**
	 * @description 判断时间俩组时间是否存在交集
	 * @date 2017-2-5下午2:52:28
	 * @author bb.h
	 * @since 1.0.0
	 * @param leftStartDate
	 * @param leftEndDate
	 * @param rightStartDate
	 * @param rightEndDate
	 * @return true:有交集 false:无交集
	 */
	public static boolean isOverlap(Date leftStartDate, Date leftEndDate, Date rightStartDate, Date rightEndDate) {
		if (leftStartDate == null || leftEndDate == null || rightStartDate == null || rightEndDate == null) {
			return false;
		}
		return ((leftStartDate.getTime() >= rightStartDate.getTime()) && leftStartDate.getTime() < rightEndDate
				.getTime())
				|| ((leftStartDate.getTime() > rightStartDate.getTime()) && leftStartDate.getTime() <= rightEndDate
						.getTime())
				|| ((rightStartDate.getTime() >= leftStartDate.getTime()) && rightStartDate.getTime() < leftEndDate
						.getTime())
				|| ((rightStartDate.getTime() > leftStartDate.getTime()) && rightStartDate.getTime() <= leftEndDate
						.getTime());
	}

	/**
	 * @description 时间戳转date
	 * @date 2017年2月20日下午12:08:12
	 * @author xuchuandi-394
	 * @since 1.0.0
	 * @param time
	 * @return
	 */
	public static Date timeStampToDate(long time) {
		return new Date(time);
	}

	public static void main(String[] args) {
		Date date = timeStampToDate(System.currentTimeMillis());
		String str = format(date, "yyyy-MM-dd HH:mm:ss");
		Date d = parseDate(str, "yyyy-MM-dd HH:mm:ss");
		System.out.println(str);
		System.out.println(format(d));
	}
}
