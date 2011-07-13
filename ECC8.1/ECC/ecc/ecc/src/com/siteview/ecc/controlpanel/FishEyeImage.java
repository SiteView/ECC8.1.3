package com.siteview.ecc.controlpanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

import com.siteview.base.data.Report;
import com.siteview.base.tree.INode;
import com.siteview.ecc.report.common.CreateReport;
import com.siteview.ecc.report.common.CreateReportImpl;
import com.siteview.ecc.reportserver.Constand;
import com.siteview.ecc.simplereport.SimpleReport;

/**
 * 鱼眼视图图片处理类
 * 
 * @author di.tang
 * 
 */
public class FishEyeImage {
	INode i = null;
	String path = Constand.fisheyeimagesavepath;
	String pngname;

	JFreeChart chart = null;
	XYDataset xd = null;
	Map<String, Map<Date, String>> imgdatas = null;
	Report simplereport = null;
	double maxvalue = 100;
	double minvalue = 0;
	double step = 10;

	public FishEyeImage(String pngname, INode inode, Date stime, Date etime) {
		this.pngname = pngname;
		this.i = inode;
		initData(stime, etime);
	}

	/**
	 * 初始化数据
	 * 
	 * @param stime
	 * @param etime
	 */
	private void initData(Date stime, Date etime) {
		simplereport = new Report(this.i, stime, etime);
		try {
			simplereport.load();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		if (simplereport == null || simplereport.getReturnSize() < 1) {
			return;
		} else {
			imgdatas = new HashMap<String, Map<Date, String>>();
			Map<Integer, Map<String, String>> listimage = SimpleReport.getImagelist(simplereport);
			Map<String, String> keyvalue = null;
			for (int key : listimage.keySet()) {
				Map<Date, String> imgdata = simplereport.getReturnValueDetail(key);
				keyvalue = listimage.get(key);
				if (keyvalue == null)
					continue;
				imgdatas.put(keyvalue.get("title"), imgdata);
				try {
					if (maxvalue <= Double.parseDouble(keyvalue.get("maxvalue"))) {
						maxvalue = Double.parseDouble(keyvalue.get("maxvalue")) + 100;
					}
					if (minvalue > Double.parseDouble(keyvalue.get("minvalue"))) {
						minvalue = Double.parseDouble(keyvalue.get("minvalue"));
					}
				} catch (Exception e) {
				}

			}
		}
		String title = this.i.getName();
		String subtitle = "";
		String Ylabel = "";

		double a = (maxvalue - minvalue) / 10;
		if (step < a)
			step = a;
		// 得到XYDataset
		xd = getXYDataset(imgdatas);
		// getJFreechart

	}

	public JFreeChart getJFreeChart(Date stime, Date etime, boolean flag) {
		chart = getJFreechart("", "", "", xd, step, maxvalue, minvalue, stime, etime, flag);
		return chart;
	}

	private XYDataset getXYDataset(Map<String, Map<Date, String>> imgdatas) {
		CreateReport cr = new CreateReportImpl();
		return cr.buildDataset(imgdatas);
	}

	public void setFilename(String filename) {
		pngname = filename + ".gif";// 保存为位置
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}
		pngname = path + pngname;
	}

	public JFreeChart getJFreechart(String title, String subtite, String Ylabel, XYDataset data, double step, double max, double min,
			Date startdate, Date enddate, boolean flag) {
		JFreeChart basechart = ChartFactory.createTimeSeriesChart("", "", Ylabel, data, flag, true, false);
		// 设置标题子标题
		TextTitle texttitle = new TextTitle(title, new Font("黑体", Font.PLAIN, 18));
		texttitle.setPaint(new Color(30, 91, 153));
		basechart.setTitle(texttitle);
		try {
			TextTitle subtexttitle = new TextTitle(subtite, new Font("黑体", Font.PLAIN, 12));
			subtexttitle.setPaint(new Color(30, 91, 153));
			basechart.addSubtitle(subtexttitle);
		} catch (Exception e) {
		}
		TextTitle subtexttitle1 = new TextTitle("", new Font("黑体", Font.PLAIN, 12));
		subtexttitle1.setPaint(new Color(30, 91, 153));
		basechart.addSubtitle(subtexttitle1);
		//
		basechart.setBackgroundPaint(Color.WHITE);
		XYPlot plot = (XYPlot) basechart.getPlot();
		plot.setBackgroundAlpha(0.0f);
		//plot.setBackgroundPaint(Color.white);
		//plot.setAngleGridlinesVisible(false);
		
		//plot.setForegroundAlpha(1.0f);// 背景表格透明度
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.BLACK);// 纵坐标格线颜色
		plot.setDomainGridlinePaint(Color.BLACK);// 横坐标格线颜色
		plot.setDomainGridlinesVisible(true);// 显示横坐标格线
		plot.setRangeGridlinesVisible(true);// 显示纵坐标格线
		plot.setAxisOffset(new RectangleInsets(0.3, 0.9, 0.9, 0.3));// 设置曲线图与xy轴的距离
		// 曲线颜色
		plot.getRenderer().setSeriesPaint(0, new Color(95, 165, 95));
		plot.getRenderer().setSeriesPaint(1, Color.red);
		plot.getRenderer().setSeriesPaint(2, Color.yellow);
		plot.getRenderer().setSeriesPaint(3, Color.blue);
		plot.getRenderer().setSeriesPaint(4, Color.green);
		plot.setOutlineStroke(new BasicStroke(1.0f));
		plot.setOutlinePaint(new Color(160, 128, 64));
		// 设置外边框
		basechart.setBorderVisible(true);
		// 日期格式设置
		DateAxis axis = (DateAxis) plot.getDomainAxis();

		axis.setAutoTickUnitSelection(false);// 不要自动设置刻度
		DateTickUnit dt = null;
		// 設置標準的時間刻度單位
		// 间隔为2小时
		dt = new DateTickUnit(DateTickUnit.HOUR, 2);
		axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
		axis.setVisible(flag);
		axis.setTickUnit(dt);
		axis.setVerticalTickLabels(true);// 横显还是竖显 false为横显
		axis.setLabelFont(new Font("黑体", Font.PLAIN, 10));
		axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
		if (enddate != null) {
			axis.setMaximumDate(enddate);
		}
		if (startdate != null) {
			axis.setMinimumDate(startdate);
		}

		// 设置轴线粗细
		axis.setAxisLineStroke(new BasicStroke(2.0f));
		axis.setAxisLinePaint(Color.BLACK);
		// 设置最大值和最小值
		ValueAxis valueaxis = plot.getRangeAxis();
		valueaxis.setRange(min, max);
		valueaxis.setAutoRange(false);
		// 设置轴线粗细
		valueaxis.setAxisLineStroke(new BasicStroke(1.0f));
		valueaxis.setAxisLinePaint(Color.BLACK);
		// 设置刻度
		// Y轴
		valueaxis.setAutoTickUnitSelection(false);
		valueaxis.setVisible(flag);
		NumberTickUnit nt = new NumberTickUnit(step);

		((NumberAxis) valueaxis).setTickUnit(nt);
		// 值中文文
		valueaxis.setLabelFont(new Font("黑体", Font.PLAIN, 12));
		// 设置背景图片
		try {
			/*PolarPlot polarplot = (PolarPlot) basechart.getPlot();
			polarplot.setBackgroundAlpha(0.0f);
			polarplot.setBackgroundPaint(Color.white);

			polarplot.setAngleGridlinesVisible(false);*/

			Image image = ImageIO.read(new File(com.siteview.ecc.reportserver.Constand.fisheyeimagesavepath + "fb1.gif"));
			basechart.setBackgroundImage(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * void setBackgroundImageAlignment(int
		 * alignment)背景图片对齐方式（参数常量在org.jfree.ui.Align类中定义） void
		 * setBackgroundImageAlpha(float alpha)背景图片透明度（0.0～1.0）
		 */
		return basechart;
	}

	public void createPng(JFreeChart basechart, int width, int height) {
		try {
			ChartUtilities.saveChartAsPNG(new File(pngname), basechart, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
