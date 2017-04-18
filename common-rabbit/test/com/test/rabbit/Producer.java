/*
 * @Project Name: zy-ht
 * @File Name: Producer.java
 * @Package Name: com.test.rabbit
 * @Date: 2017-3-31上午10:59:28
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.test.rabbit;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.lang3.SerializationUtils;

/**
 * @description 消息生产者
 * @author bb.h
 * @date 2017-3-31上午10:59:28
 * @see
 */
public class Producer extends EndPoint {
	  public Producer(String endPointName) throws IOException{
	        super(endPointName);
	    }
	 
	    public void sendMessage(Serializable object) throws IOException {
	        channel.basicPublish("",endPointName, null, SerializationUtils.serialize(object));
	    }  
}
