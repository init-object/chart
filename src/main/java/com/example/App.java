package com.example;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args)throws Exception {
        // File1 file1 = new File1();
        // File2 file2 = new File2();
        render("1.txt", "2.txt");

    }

    public static void render(String file1, String file2) throws Exception {
        // 读取数据组1和数据组2的值
        ArrayList<Double> rValues1 = readData(file1);
        ArrayList<Double> rValues2 = readData(file2);

        if (rValues1.size() != rValues2.size()) {
            JOptionPane.showConfirmDialog(null, "两组数据中的数据量不一致", "警告", JOptionPane.CLOSED_OPTION);
            return;
        }
        // 计算数据组1和数据组2的R值的商
        ArrayList<Double> ratios = new ArrayList<>();
        for (int i = 0; i < rValues1.size(); i++) {
            if (rValues2.get(i) == 0) {
                continue;
            }
            double ratio = rValues1.get(i) / rValues2.get(i);
            ratios.add(ratio);
        }

        // 创建一个折线图
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Ratios");
        for (int i = 0; i < ratios.size(); i++) {
            series.add(i, ratios.get(i));
        }
        dataset.addSeries(series);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Ratios of R Values",
                "Index",
                "Ratio",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false);
        chart.setBackgroundPaint(Color.white);

        // 保存图表为PNG文件
        ChartUtilities.saveChartAsPNG(new File("chart.png"), chart, 800, 600);

        RealTimeChart chart2 = new RealTimeChart("1/2 Chart", "1/2", 100);
        Double sum = 0d;
        for (Double data : ratios) {
            chart2.plot(data);
            sum += data;
        }
        Double avg = sum / ratios.size();
        chart2.addAvgPlane(avg);
    }

    private static ArrayList<Double> readData(String filename) throws Exception {
        ArrayList<Double> data = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            if (parts.length >= 3) {
                int rIndex = parts[2].indexOf("R:");
                if (rIndex > -1 && parts[2].endsWith("mm")) {
                    String valueStr = parts[2].substring(rIndex + 2, parts[2].length() - 2);
                    if (isNumeric(valueStr)) {
                        double value = Double.parseDouble(valueStr);
                        data.add(value);
                    }

                }
            }

        }
        reader.close();
        return data;
    }

    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i)) && !('.' == str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static ArrayList<Double> extractRValues(ArrayList<Double> data) {
        ArrayList<Double> rValues = new ArrayList<>();
        for (int i = 0; i < data.size(); i += 2) {
            rValues.add(data.get(i));
        }
        return rValues;
    }
}
