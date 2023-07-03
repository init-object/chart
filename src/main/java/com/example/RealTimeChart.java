package com.example;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.*;

import org.knowm.xchart.AnnotationTextPanel;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
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


	JLabel yMaxLabel = new JLabel("y轴最大值");
	JTextField yMaxTextField = new JTextField(10);
	JLabel yMinLabel = new JLabel("y轴最小值");
	JTextField yMinTextField = new JTextField(10);
	JLabel decimalFormatLabel = new JLabel("保留n位小数");
	JComboBox<Integer> decimalFormatBox = new JComboBox<>();
	JButton button = new JButton("重新绘制");

	private static int decimalFormat = 4;

	private AnnotationTextPanel avgTextPanel;
 
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

			// Container contentPane = frame.getContentPane();
			// contentPane.setLayout(new FlowLayout());
			JPanel pane = new JPanel();
			pane.add(yMaxLabel);
			pane.add(yMaxTextField);
			pane.add(yMinLabel);
			pane.add(yMinTextField);
			pane.add(decimalFormatLabel);
			decimalFormatBox.addItem(1);
			decimalFormatBox.addItem(2);
			decimalFormatBox.addItem(3);
			decimalFormatBox.addItem(4);
			decimalFormatBox.addItem(5);
			decimalFormatBox.addItem(6);
			decimalFormatBox.setSelectedIndex(4-1);
			setDoubleFormat(4);
			pane.add(decimalFormatBox);
			pane.add(button);
			button.addActionListener((e) -> {
				onButtonOk();
			});
			frame.add(pane, BorderLayout.NORTH);

			frame.pack();
			// frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// 防止关闭窗口时退出程序
		} else {
 
			// Update Chart
			chart.updateXYSeries(seriesName, null, seriesData, null);
			XYSeries xySeries = chart.getSeriesMap()
									 .get(seriesName);
			double yMin = xySeries.getYMin();
			double yMax = xySeries.getYMax();
			double yDiff = yMax - yMin;
			yMax = yMax + yDiff * 5;
			yMin = (yMin - yDiff * 5) > 0 ? (yMin - yDiff * 5) : 0;
			yMaxTextField.setText(doubleFormatInteger(yMax));
			yMinTextField.setText(doubleFormatInteger(yMin));
			repaintChart(xySeries, yMin, yMax);

		}
	}

	private void repaintChart(XYSeries xySeries,
						   double yMin,
						   double yMax) {
		chart.getStyler().setYAxisMax(yMax);
		chart.getStyler().setYAxisMin(yMin);
		double avg = Arrays.stream(xySeries.getYData())
						   .average()
						   .getAsDouble();
		addAvgPlane(avg);
		swingWrapper.repaintChart();
	}

	private void onButtonOk() {
		String yMaxText = yMaxTextField.getText();
		String yMinText = yMinTextField.getText();
		if ("".equals(yMaxText) || "".equals(yMinText)) {
			Object[] options = { "OK ", "CANCEL " };
			JOptionPane.showOptionDialog(null, "您还没有输入 ", "提示", JOptionPane.DEFAULT_OPTION,
										 JOptionPane.WARNING_MESSAGE,null, options, options[0]);
		}else if (App.isNumeric(yMaxText) && App.isNumeric(yMinText)){
			double yMax = Double.parseDouble(yMaxText);
			double yMin = Double.parseDouble(yMinText);
			if (yMax < yMin) {
				Object[] options = { "OK ", "CANCEL " };
				JOptionPane.showOptionDialog(null, "y轴最小值不可大于最大值 ", "提示", JOptionPane.DEFAULT_OPTION,
											 JOptionPane.WARNING_MESSAGE,null, options, options[0]);
				return;
			}
			Integer selectedItem = (Integer)decimalFormatBox.getSelectedItem();
			setDoubleFormat(selectedItem);
			XYSeries xySeries = chart.getSeriesMap()
									 .get(seriesName);
			double yMinData = xySeries.getYMin();
			double yMaxData = xySeries.getYMax();
			if (yMax < yMaxData) {
				Object[] options = { "OK ", "CANCEL " };
				JOptionPane.showOptionDialog(null, "y轴最大值不可小于数据最大值 " + doubleFormatInteger(yMaxData), "提示", JOptionPane.DEFAULT_OPTION,
											 JOptionPane.WARNING_MESSAGE,null, options, options[0]);
				return;
			}
			if (yMin > yMinData) {
				Object[] options = { "OK ", "CANCEL " };
				JOptionPane.showOptionDialog(null, "y轴最小值不可大于数据最小值 " + doubleFormatInteger(yMinData), "提示", JOptionPane.DEFAULT_OPTION,
											 JOptionPane.WARNING_MESSAGE,null, options, options[0]);
				return;
			}
			repaintChart(xySeries, yMin, yMax);
		} else {
			Object[] options = { "OK ", "CANCEL " };
			JOptionPane.showOptionDialog(null, "请输入数字", "提示", JOptionPane.DEFAULT_OPTION,
										 JOptionPane.WARNING_MESSAGE,null, options, options[0]);
		}
	}

	public static void setDoubleFormat(int value) {
		decimalFormat = value;
	}

	public static String doubleFormatInteger(double number) {
		StringBuilder pattern = new StringBuilder(".");
		for (int i = 0; i < decimalFormat; i++) {
			pattern.append("#");
		}
		DecimalFormat df = new DecimalFormat(pattern.toString());
		return df.format(number);

	}

	public void addAvgPlane(Double avg){
		if (avgTextPanel == null) {
			chart.getStyler().setAnnotationTextPanelFontColor(Color.RED);
			chart.getStyler().setAnnotationTextPanelFont(new Font("Verdana", Font.BOLD, 14));
			avgTextPanel = new AnnotationTextPanel(
					"AVG: " + doubleFormatInteger(avg),
					480,
					380,
					true);
			chart.addAnnotation(avgTextPanel);
		}else {
			avgTextPanel.setLines(Arrays.asList(("AVG: " + doubleFormatInteger(avg)).split("\\n")));
		}

	}

}