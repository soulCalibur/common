package com.test.jfree;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class CreateTimeSeriesChart {
	public static void makeTimeSeriesChart() {
		// ʵ��TimeSeries����
		TimeSeries timeseries = new TimeSeries("Data");
		// ʵ��Day
		Day day = new Day(1,1,2009);
		double d = 3000D;
		// ���һ��365������
		for(int i = 0 ; i < 365 ; i++){
			// ����������
			d = d+(Math.random() - 0.5) * 10;
			// ����ݼ�����������
			timeseries.add(day,d);
			day = (Day)day.next();
		}
		// ����TimeSeriesCollection���϶���
		TimeSeriesCollection dataset = new TimeSeriesCollection(timeseries);
		// ���ʱ��ͼ   
		JFreeChart chart = ChartFactory.createTimeSeriesChart("��ָ֤��ʱ��ͼ",//����
				"����",	//ʱ�����ǩ
				"ָ��",	//������ǩ
				dataset,//��ݼ���				
				true,	//�Ƿ���ʾͼ���ʶ
				true,	//�Ƿ���ʾtooltips
				false);	//�Ƿ�֧�ֳ�����
		String title = "��ָ֤��ʱ��ͼ";
		// ����ͼ������   
		LegendTitle legend = chart.getLegend(0);
		legend.setItemFont(new Font("����", Font.TRUETYPE_FONT, 15));
		// ���ñ�������   
		Font font = new Font("����", Font.BOLD, 20);
		TextTitle textTitle = new TextTitle(title);
		textTitle.setFont(font);
		chart.setTitle(textTitle);
		// Plot ����Ļ�ȡ����
		XYPlot plot = chart.getXYPlot();
		// X �����Ļ�ȡ����
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		// ����������ʾ��ʽ
		axis.setDateFormatOverride(new SimpleDateFormat("MM-dd-yyyy"));
		// ����X���ǩ����   
		axis.setLabelFont(new Font("����", Font.BOLD, 14));
		// Y �����Ļ�ȡ����   
		NumberAxis numAxis = (NumberAxis) plot.getRangeAxis();
		// ����Y���ǩ����   
		numAxis.setLabelFont(new Font("����", Font.BOLD, 14));
		/***************************************************************/
		ChartFrame cf = new ChartFrame("ʱ��ͼ", chart);
		cf.pack();
		cf.setVisible(true);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ʱ��ͼ   
		makeTimeSeriesChart();
	}

}
