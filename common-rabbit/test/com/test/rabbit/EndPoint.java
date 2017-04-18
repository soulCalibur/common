/*
 * @Project Name: zy-ht
 * @File Name: EndPoint.java
 * @Package Name: com.test.rabbit
 * @Date: 2017-3-31上午10:52:03
 * @Creator: bb.h
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */

package com.test.rabbit;
import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/**
 * @description EndPoint类型的队列
 * @author bb.h
 * @date 2017-3-31上午10:52:03
 * @see
 */
public class EndPoint {
	 protected Channel channel;
	    protected Connection connection;
	    protected String endPointName;
	     
	    public EndPoint(String endpointName) throws IOException{
	         this.endPointName = endpointName;
	         
	         //Create a connection factory
	         ConnectionFactory factory = new ConnectionFactory();
	         
	         //hostname of your rabbitmq server
	         factory.setHost("127.0.0.1");
	         factory.setPort(5672);
	         factory.setUsername("abc");
	         factory.setPassword("abc@abc");
	         
	         //getting a connection
	         connection = factory.newConnection();
	         
	         //creating a channel
	         channel = connection.createChannel();
	         
	         //declaring a queue for this channel. If queue does not exist,
	         //it will be created on the server.
	         channel.queueDeclare(endpointName, false, false, false, null);
	    }
	     
	     
	    /**
	     * 关闭channel和connection。并非必须，因为隐含是自动调用的。
	     * @throws IOException
	     */
	     public void close() throws IOException{
	         this.channel.close();
	         this.connection.close();
	     }
}
