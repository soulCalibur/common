/*
 * @Project Name: sns-dependency
 * @File Name: AxisType
 * @Package Name: com.hhly.sns.biz.enums
 * @Date: 2017/3/11 17:24
 * @Creator: wangjian-358
 * @line------------------------------
 * @修改人: 
 * @修改时间: 
 * @修改内容: 
 */
package com.ht.common.mode;

/**
 * @author wangjian-358
 * @description echarts坐标轴枚举
 * @date 2017/3/11 17:24
 * @see
 */
public enum AxisType {
    LINEAR("linear"), LOGARITHMIC("logarithmic"), DATETIME("datetime"), CATEGORY("category");

    private String name;

    private AxisType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
