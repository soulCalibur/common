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
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionCheckListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageAccessor;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.example.transaction.TransactionCheckListenerImpl;
import org.apache.rocketmq.namesrv.NamesrvStartup;

import com.alibaba.fastjson.JSON;


/**
 * @description 事务
 * @author bb.h
 * @date 2017-4-18上午11:14:45
 */
public class TransactionRunTest {
	protected static final String topic = "TransactionRunTest2";
	static {
		System.setProperty("rocketmq.namesrv.addr", "127.0.0.1:9876");
		//运行环境
		System.setProperty("rocketmq.home.dir", "E:/work/common/common-rocketmq/pack");
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
		Thread.sleep(3000);
		// ---------------------------------------
		new Thread(new Runnable() {

			@Override
			public void run() {
				BrokerStartup.main(null);
			}
		}).start();
		// ---------------------------------------
		Order.getOrder();
		Thread.sleep(3000);
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
		new Thread(new Runnable() {
			@Override
			public void run() {
				int reqcount=random.nextInt(20);
				try {
					TransactionCheckListener transactionCheckListener = new TransactionCheckListenerImpl();
					final TransactionMQProducer producer =new TransactionMQProducer(topic);
					producer.setTransactionCheckListener(transactionCheckListener);
					producer.start();
					Order o = Order.getOrder();
					String  msssage=JSON.toJSONString(o);
					Message msg = new Message(topic,msssage.getBytes());
					
					 MessageAccessor.putProperty(msg, "TRAN_MSG", "true");
					 MessageAccessor.putProperty(msg, "PGROUP",producer.getProducerGroup());
					 SendResult sendResult = producer.send(msg);
					
					 System.out.println("发送事务消息:"+sendResult.getSendStatus()+"==>>"+msssage.substring(0, 20));
					
					if(random.nextBoolean()){
						System.out.println("提交消息事务:"+msssage.substring(0, 20));
						producer.getDefaultMQProducerImpl().endTransaction(sendResult, LocalTransactionState.COMMIT_MESSAGE, null);
					}else {
						System.out.println("回滚消息事务:"+msssage.substring(0, 20));
						producer.getDefaultMQProducerImpl().endTransaction(sendResult, LocalTransactionState.ROLLBACK_MESSAGE, null);
					}
				
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
