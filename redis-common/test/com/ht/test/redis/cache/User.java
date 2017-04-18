package com.ht.test.redis.cache;

import java.io.Serializable;

/** 
 * @author huobao_accp@163.com  
 * @date: 2016-11-26 下午5:29:39 @version 1.0
 * @TODO 霍宝
 */
public class User implements Serializable{
	/**
	 * serialVersionUID: TODO
	 */
	private static final long serialVersionUID = -803648200252550005L;
	/**
	 * @TODO TODO
	 */
	private String name;
	private String age;
	private String sex;
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
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public User(String name, String age, String sex) {
		super();
		this.name = name;
		this.age = age;
		this.sex = sex;
	}
	
}
