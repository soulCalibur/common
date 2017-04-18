/*
 * @Project Name: ht_common
 * @File Name: ServiceFeignApplication.java
 * @Package Name: com.ht.test.cloud.consumption.feign
 * @Date: 2017-4-13下午6:25:05
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.cloud.consumption.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * @description 消费者服务 Feign 模式
 * @author bb.h
 * @date 2017-4-13下午6:25:05
 * @see
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
@EnableHystrixDashboard
@EnableFeignClients(basePackages={"com.ht.test.cloud.facade"})
@ComponentScan({"com.ht.test.cloud.consumption.feign.action","com.ht.test.cloud.facade.breaker"})
@PropertySource(value=
{
 "register.yml"
,"com/ht/test/cloud/consumption/feign/feign.properties"
})  

public class BootFeignServerStrap {

    public static void main(String[] args) {
        SpringApplication.run(BootFeignServerStrap.class, args);
    }
}
