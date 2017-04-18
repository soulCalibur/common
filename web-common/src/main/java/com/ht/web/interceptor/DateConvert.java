/*
 * @Project Name: api
 * @File Name: DateConvert.java
 * @Package Name: com.hhly.sns.api.interceptor
 * @Date: 2017-2-16下午3:11:50
 * @Creator: bb.h
 * @line------------------------------
 * @修改人:
 * @修改时间:
 * @修改内容:
 */

package com.ht.web.interceptor;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.datetime.DateFormatter;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-2-16下午3:11:50
 * @see
 */
public class DateConvert extends DateFormatter {

	private static Logger logger = LoggerFactory.getLogger(DateConvert.class);

	@Override
	public Date parse(String text, Locale locale) throws ParseException {
		try {
			return new Date(Long.parseLong(text));
		} catch (NumberFormatException e) {
			logger.error("接口入参时间格式不是时间戳 调用默认逻辑转化");
			return super.parse(text, locale);
		}
	}
}
