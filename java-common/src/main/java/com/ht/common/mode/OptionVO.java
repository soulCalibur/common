/*
 * Project Name: sns-parent
 * File Name: OptionVO
 * Package Name: com.hhly.sns.vo.echarts
 * Date: 2016/11/22 17:50
 * Creator: wangjian-358
 * ------------------------------
 * 修改人:
 * 修改时间:
 * 修改内容:
 */

package com.ht.common.mode;

/**
 * @author wangjian-358
 * @description echarts图的option数据对象
 * @date 2016/11/22 17:50
 * @see
 */
public class OptionVO {

	private AxisVO[] xAxisVO;
	private AxisVO[] yAxisVO;
	private SeriesVO[] seriesVO;

    public AxisVO[] getxAxisVO() {
        return xAxisVO;
    }

    public void setxAxisVO(AxisVO[] xAxisVO) {
        this.xAxisVO = xAxisVO;
    }

    public AxisVO[] getyAxisVO() {
        return yAxisVO;
    }

    public void setyAxisVO(AxisVO[] yAxisVO) {
        this.yAxisVO = yAxisVO;
    }

    public SeriesVO[] getSeriesVO() {
        return seriesVO;
    }

    public void setSeriesVO(SeriesVO[] seriesVO) {
        this.seriesVO = seriesVO;
    }
}
