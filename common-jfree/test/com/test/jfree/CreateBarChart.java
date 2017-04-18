package com.test.jfree;

import java.awt.Color;
import java.awt.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

public class CreateBarChart {
	/**  
	 * �����ݼ���
	 * @return org.jfree.data.category.CategoryDataset 
	 */
	private static CategoryDataset getDataset() {
		double[][] data = new double[][] { { 751, 800, 260, 600, 200 },
				{ 400, 560, 240, 300, 150 }, { 600, 450, 620, 220, 610 } };
		String[] rowKeys = { "CPU", "Ӳ��", "�ڴ�" };
		String[] columnKeys = { "1", "2", "3", "4", "5" };
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(
				rowKeys, columnKeys, data);
		return dataset;
	}
	/**  
	 * �����״ͼ��
	 */
	public static void makeBarChart3D() {
		String title = "���������������";
		// �����ݼ�   
		CategoryDataset dataset = getDataset();
		JFreeChart chart = ChartFactory.createBarChart3D(title, // ͼ�����   
				"���", 						// Ŀ¼�����ʾ��ǩ   
				"����", 						// ��ֵ�����ʾ��ǩ   
				dataset, 					// ��ݼ�   
				PlotOrientation.VERTICAL,	// ͼ�?��ˮƽ����ֱ   
				true, 						// �Ƿ���ʾͼ��   
				true, 						// �Ƿ���ɹ��ߣ���ʾ��   
				true 						// �Ƿ����URL����   
				);
		// ���ñ�������   
		Font font = new Font("����", Font.BOLD, 18);
		TextTitle textTitle = new TextTitle(title);
		textTitle.setFont(font);
		chart.setTitle(textTitle);
//		chart.setTextAntiAlias(false);
		// ���ñ���ɫ   
		chart.setBackgroundPaint(new Color(255, 255, 255));
		// ����ͼ������   
		LegendTitle legend = chart.getLegend(0);
		legend.setItemFont(new Font("����", Font.TRUETYPE_FONT, 14));
		// �����״ͼ��Plot����   
		CategoryPlot plot = chart.getCategoryPlot();
		// ȡ�ú���   
		CategoryAxis categoryAxis = plot.getDomainAxis();
		// ���ú�����ʾ��ǩ������   
		categoryAxis.setLabelFont(new Font("����", Font.BOLD, 16));
		// ���ú����ǵ�����  
		categoryAxis.setTickLabelFont(new Font("����", Font.TRUETYPE_FONT, 16));
		// ȡ������   
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		// ����������ʾ��ǩ������   
		numberAxis.setLabelFont(new Font("����", Font.BOLD, 16));
		/**************************************************************/
		ChartFrame frame = new ChartFrame(title, chart, true);
		frame.pack();
		frame.setVisible(true);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// 3D��״ͼ   
		makeBarChart3D();
	}

}
