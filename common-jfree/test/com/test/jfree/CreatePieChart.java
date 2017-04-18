package com.test.jfree;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;

public class CreatePieChart {
	/**  
	 * �����ݼ���
	 * @return org.jfree.data.general.DefaultPieDataset  
	 */
	private static DefaultPieDataset getDataSet() {
		DefaultPieDataset dfp = new DefaultPieDataset();
		dfp.setValue("�з���Ա", 35);
		dfp.setValue("�г��߻���Ա", 10);
		dfp.setValue("�г��ƹ���Ա", 25);
		dfp.setValue("����ά����Ա", 5);
		dfp.setValue("������Ա", 15);
		return dfp;
	} 
	/**  
	 * ��ɱ�״ͼ��
	 */
	public static void makePieChart3D() {
		String title = "��������Ա����";
		// �����ݼ�   
		DefaultPieDataset dataset = getDataSet();
		// ����chart��������һ��jfreechartʵ�� 
		JFreeChart chart = ChartFactory.createPieChart3D(title, // ͼ�����   
				dataset, 	// ͼ����ݼ�   
				true, 		// �Ƿ���ʾͼ��   
				false, 		// �Ƿ���ɹ��ߣ���ʾ��   
				false 		// �Ƿ����URL����   
				);
		// ����pieChart�ı���������   
		Font font = new Font("����", Font.BOLD, 25);
		TextTitle textTitle = new TextTitle(title);
		textTitle.setFont(font);
		chart.setTitle(textTitle);
//		chart.setTextAntiAlias(false);
		// ���ñ���ɫ  
		chart.setBackgroundPaint(new Color(255, 255, 255));
		// ����ͼ������   
		LegendTitle legend = chart.getLegend(0);
		legend.setItemFont(new Font("����", 1, 15));
		// ���ñ�ǩ����   
		PiePlot plot = (PiePlot) chart.getPlot();
		plot.setLabelFont(new Font("����", Font.TRUETYPE_FONT, 12));
		// ָ��ͼƬ��͸����(0.0-1.0)   
		plot.setForegroundAlpha(0.95f);
		// ͼƬ����ʾ�ٷֱ�:�Զ��巽ʽ��{0} ��ʾѡ� {1} ��ʾ��ֵ�� {2} ��ʾ��ռ���� ,С������λ   
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0}={1}({2})", NumberFormat.getNumberInstance(),
				new DecimalFormat("0.00%")));
		// ͼ����ʾ�ٷֱ�:�Զ��巽ʽ�� {0} ��ʾѡ� {1} ��ʾ��ֵ�� {2} ��ʾ��ռ����   
		plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(
				"{0} ({2})"));
		// ���õ�һ�������濪ʼ��λ�ã�Ĭ����12���ӷ���   
		plot.setStartAngle(90);
		/***********************************************************/
		ChartFrame frame = new ChartFrame(title, chart, true);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 3D��״ͼ   
		makePieChart3D();
	}

}
