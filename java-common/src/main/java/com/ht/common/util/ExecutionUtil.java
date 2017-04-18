/*
 * @Project Name: ht
 * @File Name: ExecutionUtil.java
 * @Package Name: com.ht.common.util
 * @Date: 2017-3-30上午10:19:15
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-3-30上午10:19:15
 * @see
 */
public class ExecutionUtil {
	public static boolean success(String execution,String value){
		if(execution==null){
			return true;
		}
		Pattern p = Pattern.compile(execution);  
		Matcher m = p.matcher(value);  
		return m.matches();  
		
	}
}
