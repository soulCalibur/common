/*
 * Project Name: sns-base
 * File Name: EchartsUtil
 * Package Name: com.hhly.sns.base.util
 * Date: 2016/11/22 19:58
 * Creator: wangjian-358
 * ------------------------------
 * 修改人: 汪剑
 * 修改时间: 2016/11/23 12:03
 * 修改内容: 增加以数组对象构建数据方法
 */

package com.ht.common.util;

import java.util.List;

import com.ht.common.mode.AxisType;
import com.ht.common.mode.AxisVO;
import com.ht.common.mode.OptionVO;
import com.ht.common.mode.SeriesVO;

/**
 * @author wangjian-358
 * @description echarts工具类，构建echarts需要的数据模型用以输出
 * @date 2016/11/22 19:58
 * @see
 */
public class EchartsUtil {

    /**
     * @author: wangjian-358
     * @date: 2016/11/23 10:41
     * @description:  构建charts数据
     * @param xAxisVO x轴对象
     * @param yAxisVO y轴对象
     * @param seriesVO charts数据对象
     * @return
     */
    public static OptionVO BuildData(AxisVO xAxisVO, AxisVO yAxisVO, SeriesVO seriesVO) {
        AxisVO[] axisVOsX = new AxisVO[] { xAxisVO };
        AxisVO[] axisVOsY = new AxisVO[] { yAxisVO };
        SeriesVO[] seriesVOs = new SeriesVO[] { seriesVO };
        return BuildData(axisVOsX, axisVOsY, seriesVOs);
    }

    /**
     * @author: wangjian-358
     * @date: 2016/11/23 11:50
     * @description:  构建charts数据
     * @param xAxisVO x轴对象
     * @param yAxisVO y轴对象
     * @param seriesVOs charts数据对象数组
     * @return
     */
    public static OptionVO BuildData(AxisVO xAxisVO, AxisVO yAxisVO, SeriesVO[] seriesVOs) {
        AxisVO[] axisVOsX = new AxisVO[] { xAxisVO };
        AxisVO[] axisVOsY = new AxisVO[] { yAxisVO };
        return BuildData(axisVOsX, axisVOsY, seriesVOs);
    }
    /**
     * @author: wangjian-358
     * @date: 2016/11/23 11:50
     * @description:  构建charts数据
     * @param xAxisVOs x轴对象数组
     * @param yAxisVOs y轴对象数组
     * @param seriesVOs charts数据对象数组
     * @return
     */
    public static OptionVO BuildData(AxisVO[] xAxisVOs, AxisVO[] yAxisVOs, SeriesVO[] seriesVOs) {
        OptionVO optionVO = new OptionVO();
        optionVO.setSeriesVO(seriesVOs);
        optionVO.setxAxisVO(xAxisVOs);
        optionVO.setyAxisVO(yAxisVOs);
        return optionVO;
    }

    /**
     * @author: wangjian-358
     * @date: 2016/11/23 11:50
     * @description:  构建charts数据
     * @param xAxisVOList x轴对象数组
     * @param yAxisVOList y轴对象数组
     * @param seriesVOList charts数据对象数组
     * @return
     */
    public static OptionVO BuildData(List<AxisVO> xAxisVOList, List<AxisVO> yAxisVOList, List<SeriesVO> seriesVOList) {
        AxisVO[] xAxisVOs = (AxisVO[]) xAxisVOList.toArray(new AxisVO[xAxisVOList.size()]);
        AxisVO[] yAxisVOs = (AxisVO[]) yAxisVOList.toArray(new AxisVO[yAxisVOList.size()]);
        SeriesVO[] seriesVOs = (SeriesVO[]) seriesVOList.toArray(new SeriesVO[seriesVOList.size()]);
        return BuildData(xAxisVOs, yAxisVOs, seriesVOs);
    }
    
    /**
	 * 
	 * @description 构建OptionVO
	 * @date 2016年11月25日下午7:19:52
	 * @author zengly507
	 * @since 1.0.0 
	 * @param dateType
	 * @param data
	 * @param DateNum1
	 * @param DateNum2
	 * @return
	 */
    public static OptionVO bulidOptionVO(String[] data,List<SeriesVO> list) {
		
		AxisVO xAxisVO = bulidAxisVO(AxisType.CATEGORY.getName(), data);
		
		AxisVO yAxisVO = bulidAxisVO(AxisType.CATEGORY.getName(), null);
		
		SeriesVO[] seriesVOs = (SeriesVO[]) list.toArray(new SeriesVO[list.size()]);
		
		return EchartsUtil.BuildData(xAxisVO, yAxisVO, seriesVOs);
	}
    
    /**
	 * 
	 * @description 构建 SeriesVO
	 * @date 2016年11月25日下午2:24:44
	 * @author zengly507
	 * @since 1.0.0 
	 * @param name
	 * @param obj
	 * @return
	 */
    public static SeriesVO bulidSeriesVO(String name,Object[] obj) {
		SeriesVO seriesVO = new SeriesVO();
		seriesVO.setName(name);
		seriesVO.setData(obj);
		return seriesVO;
	}
	
	/**
	 * 
	 * @description  构建 AxisVO
	 * @date 2016年11月25日下午2:29:28
	 * @author zengly507
	 * @since 1.0.0 
	 * @param type
	 * @param obj
	 * @return
	 */
    public static AxisVO bulidAxisVO(String type,Object[] obj) {
		AxisVO axisVO = new AxisVO();
		if(StringUtil.isNotBlank(type)){
			axisVO.setType(type);
		}
		if(obj != null){
			axisVO.setData(obj);
		}
		return axisVO;
	}
}
