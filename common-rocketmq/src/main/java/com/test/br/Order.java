/*
 * @Project Name: incubator-rocketmq
 * @File Name: Order.java
 * @Package Name: com.test.br
 * @Date: 2017-4-17����1:33:25
 * @Creator: bb.h
 * @line------------------------------
 * @�޸���:
 * @�޸�ʱ��:
 * @�޸�����:
 */

package com.test.br;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @description TODO
 * @author bb.h
 * @date 2017-4-17����1:33:25
 * @see
 */
public class Order {

	private String status;
	private String remark;
	private Double moeny;
	private Long orderId;

	public String getStatus() {
		return status;
	}

	public String getRemark() {
		return remark;
	}

	public Double getMoeny() {
		return moeny;
	}

	public Long getOrderId() {
		return orderId;
	}

	public static Map<Long, Order> getCache() {
		return cache;
	}

	static Map<Long, Order> cache = new HashMap<Long, Order>();
	static {
		Random random = new Random();
		for (int i = 0; i < 500; i++) {
			Long orderId = UUID.randomUUID().getLeastSignificantBits();
			int code = random.nextInt(5);
			String remark = "未知";
			switch (code) {
				case 1:
					remark = "支付成功";
					break;
				case 2:
					remark = "支付失败";
					break;
				case 3:
					remark = "处理中";
					break;
				case 4:
					remark = "取消支付";
					break;
				case 5:
					remark = "支付失败";
					break;
				default:
					break;
			}
			Order value = new Order(code + "", remark, new Double(random.nextInt()), orderId);
			cache.put(orderId, value);
		}
	}

	/**
	 * @description TODO
	 * @date 2017-4-17����1:33:25
	 * @author bb.h
	 * @since 1.0.0
	 * @param args
	 */
	public static Order getOrder() {
		Long[] keys = cache.keySet().toArray(new Long[0]);
		Random random = new Random();
		Long randomKey = keys[random.nextInt(keys.length)];
		return cache.get(randomKey);
	}

	
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Order(String status, String remark, Double moeny, Long orderId) {
		super();
		this.status = status;
		this.remark = remark;
		this.moeny = moeny;
		this.orderId = orderId;
	}
}
