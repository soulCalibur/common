package com.test.jfree;

import java.awt.Color;
import java.awt.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

public class CreateLineChart {
	/**  
	 * �����ݼ���
	 * @return org.jfree.data.category.CategoryDataset 
	 */
	private static CategoryDataset getDataset() {
		double[][] data = new double[][] { { 751, 800, 260, 600, 200 },
				{ 400, 560, 240, 300, 150 }, { 600, 450, 620, 220, 610 } };
		String[] rowKeys = { "CPU", "7", "8" };
		String[] columnKeys = { "1", "2", "3", "4", "5" };
		CategoryDataset dataset = DatasetUtilities.createCategoryDataset(rowKeys, columnKeys, data);
		return dataset;
	}
	/**  
	 * �������ͼ��
	 */
	public static void makeLineChart() {
		String title = "���������������";
		// �����ݼ�   
		CategoryDataset dataset = getDataset();
		JFreeChart chart = ChartFactory.createLineChart(title, // ͼ�����   
				"111",						// Ŀ¼�����ʾ��ǩ   
				"dddd",						// ��ֵ�����ʾ��ǩ   
				dataset,					// ��ݼ�   
				PlotOrientation.VERTICAL, 	// ͼ�?��ˮƽ����ֱ   
				true,						// �Ƿ���ʾͼ��   
				true,						// �Ƿ���ɹ��ߣ���ʾ��   
				false						// �Ƿ����URL����   
				);
		chart.setTextAntiAlias(false);
		// ���ñ���ɫ   
		chart.setBackgroundPaint(Color.WHITE);
		// ����ͼ���������  
		Font font = new Font("222", Font.BOLD, 20);
		TextTitle textTitle = new TextTitle(title);
		textTitle.setFont(font);
		chart.setTitle(textTitle);
		// ����X��Y�������   
		Font labelFont = new Font("222", Font.BOLD, 16);
		chart.setBackgroundPaint(Color.WHITE);
		// ����ͼ������   
		LegendTitle legend = chart.getLegend(0);
		legend.setItemFont(new Font("11", Font.TRUETYPE_FONT, 14));
		// ���plot
		CategoryPlot categoryplot = (CategoryPlot) chart.getPlot();
		// x�� ����������Ƿ�ɼ�   
		categoryplot.setDomainGridlinesVisible(true);
		// y�� ���������Ƿ�ɼ�   
		categoryplot.setRangeGridlinesVisible(true);
		// y�� ����ɫ��   
		categoryplot.setRangeGridlinePaint(Color.WHITE);
		// x�� ����ɫ��   
		categoryplot.setDomainGridlinePaint(Color.WHITE);
		// ���ñ���ɫ   
		categoryplot.setBackgroundPaint(Color.lightGray);
		// ����������֮��ľ��� 
		CategoryAxis domainAxis = categoryplot.getDomainAxis();
		// ���ú����ǩ��������   
		domainAxis.setLabelFont(labelFont);
		// ���ú�����ֵ��ǩ����   
		domainAxis.setTickLabelFont(new Font("222", Font.TRUETYPE_FONT, 14));
		// �����ϵ�   
		domainAxis.setCategoryLabelPositions(CategoryLabelPositions.STANDARD);
		// ���þ���ͼƬ��˾���   
		domainAxis.setLowerMargin(0.0);
		// ���þ���ͼƬ�Ҷ˾���   
		domainAxis.setUpperMargin(0.0);
		NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
		// ����������ʾ��ǩ������   
		numberaxis.setLabelFont(labelFont);
		numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		numberaxis.setAutoRangeIncludesZero(true);
		// ���renderer ע���������������͵�lineandshaperenderer����   
		LineAndShapeRenderer lineandshaperenderer = (LineAndShapeRenderer) categoryplot
				.getRenderer();
		// series �㣨����ݵ㣩�ɼ�   
		lineandshaperenderer.setBaseShapesVisible(true);
		// series �㣨����ݵ㣩�������߿ɼ�   
		lineandshaperenderer.setBaseLinesVisible(true);
		/*******************************************************/
		ChartFrame frame = new ChartFrame(title, chart, true);
		frame.pack();
		frame.setVisible(true);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// ����ͼ   
		makeLineChart();
	}

}
