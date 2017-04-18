/*
 * @Project Name: ht_common
 * @File Name: EurekaserverApplication.java
 * @Package Name: com.ht.test.cloud.server
 * @Date: 2017-4-13下午2:28:28
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.cloud.center.register;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.PropertySource;

/**
 * @description 服务注册中心
 * @author bb.h
 * @date 2017-4-13下午2:28:28
 * @see
 */
@EnableEurekaServer
@SpringBootApplication
@PropertySource(value={"com/ht/test/cloud/center/register/register_center.properties"})  
public class BootRegisterCenterEurekStrap {
    public static void main(String[] args) {
        SpringApplication.run(BootRegisterCenterEurekStrap.class, args);
    }
}