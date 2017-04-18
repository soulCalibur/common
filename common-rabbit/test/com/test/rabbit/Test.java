/*
 * @Project Name: zy-ht
 * @File Name: Test.java
 * @Package Name: com.test.rabbit
 * @Date: 2017-3-31上午11:02:00
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.test.rabbit;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-3-31上午11:02:00
 * @see
 */
public class Test {
	 public Test() throws Exception{
         
	        QueueConsumer consumer = new QueueConsumer("queue");
	        Thread consumerThread = new Thread(consumer);
	        consumerThread.start();
	         
	        Producer producer = new Producer("queue");
	         
	        for (int i = 0; i < 1000000; i++) {
	            HashMap<String,Integer> message = new HashMap<String,Integer>();
	            message.put("message number", i);
	            producer.sendMessage(message);
	            System.out.println("Message Number "+ i +" sent.");
	        }
	    }
	     
	    /**
	     * @param args
	     * @throws SQLException
	     * @throws IOException
	     */
	    public static void main(String[] args) throws Exception{
	      new Test();
	    }
}
