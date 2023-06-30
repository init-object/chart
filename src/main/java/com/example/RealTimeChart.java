package com.example;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import org.knowm.xchart.AnnotationTextPanel;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.LegendLayout;
import org.knowm.xchart.style.Styler.LegendPosition;
 
/**
 * Logarithmic Y-Axis
 *
 * <p>
 * Demonstrates the following:
 *
 * <ul>
 * <li>Logarithmic Y-Axis
 * <li>Building a Chart with ChartBuilder
 * <li>Place legend at Inside-NW position
 */
public class RealTimeChart {
 
	private SwingWrapper<XYChart> swingWrapper;
	private XYChart chart;
	private JFrame frame;
 
	private String title;// 标题
	private String seriesName;// 系列，此处只有一个系列。若存在多组数据，可以设置多个系列
	private List<Double> seriesData;// 系列的数据
	private int size = 1000;// 最多显示多少数据，默认显示1000个数据
 
	public int getSize() {
		return size;
	}
 
	public void setSize(int size) {
		this.size = size;
	}
 
	public String getSeriesName() {
		return seriesName;
	}
 
	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}
 
	public String getTitle() {
		return title;
	}
 
	public void setTitle(String title) {
		this.title = title;
	}
 
	/**
	 * 实时绘图
	 * 
	 * @param seriesName
	 * @param title
	 */
	public RealTimeChart(String title, String seriesName) {
		super();
		this.seriesName = seriesName;
		this.title = title;
	}
 
	public RealTimeChart(String title, String seriesName, int size) {
		super();
		this.title = title;
		this.seriesName = seriesName;
		this.size = size;
	}
 
	public void plot(double data) {
		if (seriesData == null) {
			seriesData = new LinkedList<>();
		}
 
		if (seriesData.size() == this.size) {
			seriesData.clear();
		}
 
		seriesData.add(data);
 
		if (swingWrapper == null) {
 
			// Create Chart
			chart = new XYChartBuilder().width(600).height(450).theme(ChartTheme.Matlab).title(title).build();
			chart.addSeries(seriesName, null, seriesData);
			chart.getStyler().setToolTipsEnabled(true);
			chart.getStyler().setToolTipsAlwaysVisible(true);
			chart.getStyler().setYAxisMax(4.0d);
			// chart.getStyler().setToolTipFont( new Font("Verdana", Font.BOLD, 9));
			// chart.getStyler().setToolTipHighlightColor(Color.CYAN);
			// chart.getStyler().setToolTipBorderColor(Color.BLACK);
			// chart.getStyler(). setToolTipBackgroundColor(Color.LIGHT_GRAY);
			chart.getStyler().setToolTipType(Styler.ToolTipType.yLabels);
			chart.getStyler().setyAxisTickLabelsFormattingFunction(RealTimeChart::doubleFormatInteger);
			chart.getStyler().setLegendPosition(LegendPosition.OutsideS);// 设置legend的位置为外底部
			chart.getStyler().setLegendLayout(LegendLayout.Horizontal);// 设置legend的排列方式为水平排列

 
			swingWrapper = new SwingWrapper<XYChart>(chart);
			frame = swingWrapper.displayChart();
			// frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// 防止关闭窗口时退出程序
		} else {
 
			// Update Chart
			chart.updateXYSeries(seriesName, null, seriesData, null);
			swingWrapper.repaintChart();
		}
	}

	public static String doubleFormatInteger(double number) {
		String numberStr;
		if (((int) number * 1000) == (int) (number * 1000)) {
			//如果是一个整数
			numberStr = String.valueOf((int) number);
		} else {
			DecimalFormat df = new DecimalFormat("######0.0000");
			numberStr = df.format(number);
		}
		return numberStr;
	}

	public void addAvgPlane(Double avg){
		chart.addAnnotation(
				new AnnotationTextPanel(
						"AVG: " + doubleFormatInteger(avg),
						480,
						380,
						true));
	}

}