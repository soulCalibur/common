/*
 * @Project Name: ht_common
 * @File Name: ServiceRibbonApplication.java
 * @Package Name: com.ht.test.cloud.ribbon
 * @Date: 2017-4-13下午5:06:36
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.cloud.consumption.ribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

/**
 * @description 消费者服务 ribbon 模式
 * @author bb.h
 * @date 2017-4-13下午5:06:36
 * @see
 */

@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
@ComponentScan({"com.ht.test.cloud.consumption.ribbon.action"})
@PropertySource(value=
{
 "register.yml"
,"com/ht/test/cloud/consumption/ribbon/ribbon.properties"
})  

public class BootRibbonServerStrap {
    public static void main(String[] args) {
        SpringApplication.run(BootRibbonServerStrap.class, args);
    }

    @Bean
    @LoadBalanced
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
