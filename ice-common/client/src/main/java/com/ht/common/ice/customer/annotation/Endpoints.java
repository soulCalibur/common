/*
 * Project Name: cmp-ice
 * File Name: ICEClient.java
 * Package Name: com.hhly.common.components.ice.annotation
 * Date: 2016年11月26日下午3:44:00
 * Creator: shenxiaoping
 * ------------------------------
 * 修改人: 
 * 修改时间: 
 * 修改内容: 
 */

package com.ht.common.ice.customer.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ht.common.ice.customer.mode.DefaultPrxHelper;

/**
 * @description ice client 注解
 * @author Allen Shen
 * @date 2016年11月26日下午3:44:00
 * @see
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Endpoints {
	
	
	/**
	 * @description 系统默认ice endpoints 通配
	 * 当配置文件缺失endpoints单一配置之后 将使用此属性构造出
	 * ice.provider.endpoints.${endpointsSystem}.default
	 * 作为key 重新获取endpoints
	 *  作用于 减少一个系统相同的值只是不同的key需要配置多条数据的问题
	 * @date 2016-12-10下午3:44:54
	 * @author bb.h
	 * @since 1.0.0 
	 * @return
	 */
	String system();
	/**
	 * @description 服务名称
	 * @date 2016年11月28日下午6:58:29
	 * @author shenxiaoping
	 * @since 1.0.0
	 * @return
	 */
	String name() default "";

	/**
	 * @description ice针对每个服务生成的proxyHelper类, 默认可以不指定
	 * @date 2016年11月28日下午6:57:48
	 * @author shenxiaoping
	 * @since 1.0.0
	 * @return
	 */
	abstract Class<? extends Ice.ObjectPrxHelperBase> proxy() default DefaultPrxHelper.class;
}
