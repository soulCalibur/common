/*
 * Project Name: sns-parent File Name: AxisVO Package Name:
 * com.hhly.sns.vo.echarts Date: 2016/11/22 17:33 Creator: wangjian-358
 * ------------------------------ 修改人: 修改时间: 修改内容:
 */

package com.ht.common.mode;

/**
 * @author wangjian-358
 * @description echarts图的X/Y轴数据模型
 * @date 2016/11/22 17:33
 * @see
 */
public class AxisVO {

	private String type;
	private Object[] data;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object[] getData() {
		return data;
	}

	public void setData(Object[] data) {
		this.data = data;
	}
}
