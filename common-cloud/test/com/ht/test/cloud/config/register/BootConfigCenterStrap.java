/*
 * @Project Name: ht_common
 * @File Name: EurekaServerApplication.java
 * @Package Name: com.ht.test.cloud.config.register
 * @Date: 2017-4-14下午4:25:34
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.cloud.config.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-4-14下午4:25:34
 * @see
 */
@EnableAutoConfiguration
@EnableConfigServer
@ComponentScan({"aaaaaaaaaaaaaaaaaa.bbbbbbbbbbbbbb"})
@PropertySource(value={"com/ht/test/cloud/config/register/config_center.properties"})  
public class BootConfigCenterStrap {
	 public static void main(String[] args) {
	        SpringApplication.run(BootConfigCenterStrap.class, args);
	    }
}
