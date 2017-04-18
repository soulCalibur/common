/*
 * @Project Name: ht_common
 * @File Name: HelloMode.java
 * @Package Name: com.ht.test.cloud.server.mode
 * @Date: 2017-4-14上午9:33:32
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.ht.test.cloud.server.mode;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-4-14上午9:33:32
 * @see
 */
public class HelloMode {
private String name;
private String age;
public HelloMode(){
	
}
public HelloMode(String name, String age) {
	super();
	this.name = name;
	this.age = age;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public String getAge() {
	return age;
}

public void setAge(String age) {
	this.age = age;
}
	
}
