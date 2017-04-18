/*
 * Project Name: sns-parent
 * File Name: SeriesVO
 * Package Name: com.hhly.sns.vo.echarts
 * Date: 2016/11/22 17:48
 * Creator: wangjian-358
 * ------------------------------
 * 修改人:
 * 修改时间:
 * 修改内容:
 */

package com.ht.common.mode;

/**
 * @author wangjian-358
 * @description echarts图的数据对象
 * @date 2016/11/22 17:48
 * @see
 */
public class SeriesVO {

	private String name;
	private Object[] data;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public Object[] getData() {
        return data;
    }

    public void setData(Object[] data) {
        this.data = data;
    }
}
