/**
 * File Name:HelloMode.java
 * Package Name:com.ht.test.cloud.facade
 * Date:2017-4-15上午2:59:53
 * Copyright (c) 2017, chenzhou1025@126.com All Rights Reserved.
 *
*/

package com.ht.test.cloud.facade.mode;
/**
 * ClassName:HelloMode <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     2017-4-15 上午2:59:53 <br/>
 * @author   Administrator
 * @version  
 * @since    JDK 1.6
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
