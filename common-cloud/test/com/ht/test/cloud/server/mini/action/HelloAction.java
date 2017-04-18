/*
 * @Project Name: ht_common
 * @File Name: HelloAction.java
 * @Package Name: com.ht.test.cloud.server
 * @Date: 2017-4-13下午5:56:22
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.cloud.server.mini.action;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ht.test.cloud.server.mode.NodeGet;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-4-13下午5:56:22
 * @see
 */
@RestController
public class HelloAction {
	 @RequestMapping("/helloAction")
	 public String helloAction(@RequestParam(required=false) String name,@RequestParam(required=false) Integer sex) {
		 System.out.println("访问节点:==>>"+NodeGet.NODEID);
	        return "查询参数条件:[name:"+name+"],[sex:"+sex+"]";
	 }
}
