/*
 * @Project Name: incubator-rocketmq
 * @File Name: RunTest.java
 * @Package Name: com.test.br
 * @Date: 2017-4-17����1:12:36
 * @Creator: bb.h
 * @line------------------------------
 * @�޸���:
 * @�޸�ʱ��:
 * @�޸�����:
 */

package com.test.br;

import java.util.List;
import java.util.Random;

import org.apache.rocketmq.broker.BrokerStartup;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.namesrv.NamesrvStartup;

import com.alibaba.fastjson.JSON;


public class RunTest {
	protected static final String topic = "my-message-topic";
	static {
		System.setProperty("rocketmq.namesrv.addr", "127.0.0.1:9876");
		System.setProperty("rocketmq.home.dir", "E:\\work\\incubator-rocketmq\\target\\apache-rocketmq-all");
	}
	public static void main(String[] args) throws Exception {
		// ---------------------------------------
		new Thread(new Runnable() {

			@Override
			public void run() {
				NamesrvStartup.main(null);
			}
		}).start();
		// ---------------------------------------
		// ---------------------------------------
		new Thread(new Runnable() {

			@Override
			public void run() {
				BrokerStartup.main(null);
			}
		}).start();
		// ---------------------------------------
		Order.getOrder();
		Thread.sleep(5000);
		// ---------------------------------------
		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					  DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("CID_JODIE_1");
				        consumer.subscribe(topic, "*");
				        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
				        consumer.registerMessageListener(new MessageListenerConcurrently() {
				            @Override
				            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				            	StringBuffer log=new StringBuffer();
				            	log.append("\n\n============================================================");
				            	log.append("\n收到消息:" + new String(msgs.get(0).getBody()) + "");
				            	log.append("\n============================================================\n");
				            	System.out.println(log);
				                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				            }
				        });
				        consumer.start();
				        System.out.printf(" 消费端启动ok %n");
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			}
		}).start();
		// ---------------------------------------
		Thread.sleep(5000);
		// ---------------------------------------
		final Random random=new Random();
		final String[] tags = new String[] {"TagA", "TagB", "TagC", "TagD", "TagE"};
		new Thread(new Runnable() {

			@Override
			public void run() {
				int reqcount=random.nextInt(20);
				try {
					MQProducer producer = new DefaultMQProducer("please_rename_unique_group_name");
					producer.start();
		                int orderId = 1 % 10;
		                Order o = Order.getOrder();
		                	String  msssage=JSON.toJSONString(o);
						//Message msg = new Message(topic, tags[i % tags.length], "KEY" + i,msssage.getBytes());
						Message msg = new Message(topic,msssage.getBytes());
						SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
		                    @Override
		                    public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
		                        return mqs.get(0);
		                    }
		                }, o.getOrderId());

		            producer.shutdown();
		        
				} catch (Exception e) {
					e.printStackTrace();
				}
			try {
				Thread.sleep(reqcount*300);
				this.run();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			}
		}).start();
		// ---------------------------------------
	}
}
