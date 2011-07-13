package com.siteview.ecc.simplereport;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.encoders.EncoderUtil;
import org.jfree.chart.encoders.ImageFormat;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYAnnotationEntity;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.TextAnchor;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Area;
import org.zkoss.zul.Imagemap;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Row;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.siteview.base.data.Report;
import com.siteview.base.data.ReportManager;
import com.siteview.base.data.Report.DstrItem;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svecc.zk.test.SVDBViewFactory;



public class SimpleReport extends GenericForwardComposer
{
	// 绑定
	Window													wSimpleReport;
	Vbox													maptable;
	Label													monitorname;
	Label													datefromto;
	Listbox													mlist;
	Listbox													slist;
	
	Listbox													errorlist;
	Listbox													oklist;
	Listbox													disablelist;
	Listbox													dangerlist;
	
	// 其他
	// private Imagemap chartImagemap;
	// private JFreeChart basechart;
	private Report											simpleReport;
	private Map<Integer, Map<String, String>>	listimage;
	private Row												temprow;
	List<ReportListmodel>									monitorReports	= new ArrayList<ReportListmodel>();
	private String											monitorName;
	
	private String											monitorId;
	private String											sessionId;
	private String											error_message;
	
	public SimpleReport() throws Exception
	{
		
		monitorId = Executions.getCurrent().getParameter("monitorId");
		sessionId = Executions.getCurrent().getParameter("sid");
		View w = null;
		try
		{
			w = SVDBViewFactory.getView(sessionId);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (w == null)
		{
			error_message = "未登录或无效的会话！";
			return;
		}
		INode n = w.getNode(monitorId);
		if (n == null)
		{
			error_message = "节点不存在或无权访问！";
			return;
		}
		if (!n.getType().equals(INode.MONITOR))
		{
			error_message = "节点类型非法！";
			return;
		}
		MonitorInfo info = w.getMonitorInfo(n);
		if (info == null)
		{
			error_message = "节点不存在或无权访问！";
			return;
		}
		try
		{
			simpleReport = ReportManager.getSimpleReport(info);
		} catch (Exception ex)
		{
			error_message = ex.getMessage();
		}
		
	}
	
	/**
	 * 
	 * @param listb
	 * @param model
	 * @param rend
	 */
	public void MakelistData(Listbox listb, ReportListmodel model, ListitemRenderer rend)
	{
		listb.setModel(model);
		listb.setItemRenderer(rend);
	}
	
	/**
	 * 得到image的索引值
	 * 
	 * @return
	 */
	public static Map<Integer, Map<String, String>> getImagelist(Report simpleReport1)
	{
		Map<Integer, Map<String, String>> listimage1 = new LinkedHashMap<Integer, Map<String, String>>();
		for (int i = 0; i < simpleReport1.getReturnSize(); i++)
		{
			String svdrawimage = simpleReport1.getReturnValue("sv_drawimage", i);
			svdrawimage = svdrawimage.isEmpty() ? "0" : svdrawimage;
			String svdrawmeasure = simpleReport1.getReturnValue("sv_drawmeasure", i);
			svdrawmeasure = svdrawmeasure.isEmpty() ? "0" : svdrawmeasure;
			String svprimary = simpleReport1.getReturnValue("sv_primary", i);
			svprimary = svprimary.isEmpty() ? "0" : svprimary;
			HashMap<String, String> keyvalue = new HashMap<String, String>();
			String max = simpleReport1.getReturnValue("max", i);
			String min = simpleReport1.getReturnValue("min", i);
			String average = simpleReport1.getReturnValue("average", i);
			String latestCreateTime = simpleReport1.getPropertyValue("latestCreateTime");
			// keyvalue.put("latest",simpleReport.getReturnValue("latest", i));
			keyvalue.put("subtitle", "最大值" + max + "平均值" + average + "最小值" + min);
			keyvalue.put("title", simpleReport1.getReturnValue("ReturnName", i));
			keyvalue.put("maxvalue", max);
			keyvalue.put("minvalue", min);
			keyvalue.put("maxdate", latestCreateTime);
			// && svprimary.equals("1")
			if (svdrawmeasure.equals("1"))
			{
				listimage1.put(i, keyvalue);
			}
			
		}
		
		return listimage1;
	}
	
	public void onCreate$wSimpleReport()
	{
		if (error_message != null)
		{
			monitorname.setValue(error_message);
			return;
		}
		if (simpleReport != null)
		{
			String name = simpleReport.getPropertyValue("MonitorName");
			if (name == null)
			{
				name = "";
			}
			monitorname.setValue(name);
			Map<Date, DstrItem> dstrs = simpleReport.getDstr();
			if(dstrs.isEmpty())
				return;
			Iterator itm = dstrs.keySet().iterator();
			String mindate = Toolkit.getToolkit().formatDate((Date) itm.next());
			String maxdate = simpleReport.getPropertyValue("latestCreateTime");
			datefromto.setValue("时段:" + mindate + "~" + maxdate);
			// buildChart();
			listimage = getImagelist(simpleReport);
			buildImageMaps();
			ReportListmodel reportListmodel = new com.siteview.ecc.simplereport.ReportListmodel("MonitorBean", simpleReport);
			MakelistData(mlist, reportListmodel, reportListmodel);
			ReportListmodel reportListmodeltj = new com.siteview.ecc.simplereport.ReportListmodel("StatisticsBean", simpleReport);
			MakelistData(slist, reportListmodeltj, reportListmodeltj);
			ReportListmodel reportListmodelok = new com.siteview.ecc.simplereport.ReportListmodel("HistoryBean", simpleReport);
			MakelistData(oklist, reportListmodelok, reportListmodelok);
			ReportListmodel reportListmodelerror = new com.siteview.ecc.simplereport.ReportListmodel("HistoryBeanerror", simpleReport);
			MakelistData(errorlist, reportListmodelerror, reportListmodelerror);
			ReportListmodel reportListmodeldanger = new com.siteview.ecc.simplereport.ReportListmodel("HistoryBeandanger", simpleReport);
			MakelistData(dangerlist, reportListmodeldanger, reportListmodeldanger);
			ReportListmodel reportListmodeldisable = new com.siteview.ecc.simplereport.ReportListmodel("HistoryBeandisable", simpleReport);
			MakelistData(disablelist, reportListmodeldisable, reportListmodeldisable);
		}
	}
	
	/**
	 * 构建chart集合
	 */
	private void buildImageMaps()
	{
		for (int key : this.listimage.keySet())
		{
			Map<Date, String> imgdata = this.simpleReport.getReturnValueDetail(key);
			Map<String, String> keyvalue = this.listimage.get(key);
			XYDataset data = buildDataset(imgdata);
			Imagemap temmap = null;
			String maxdate = keyvalue.get("maxdate");
			Date maxd = null;
			if (!maxdate.isEmpty())
			{
				try
				{
					maxd = Toolkit.getToolkit().parseDate(maxdate);
				} catch (ParseException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (keyvalue.get("title").contains("%"))
			{
				temmap = buildImageMapDEL(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data, 10, 100, maxd, 0, true,600,300);
			} else
			{
				double maxvalue = Double.parseDouble(keyvalue.get("maxvalue"));
				double minvalue = Double.parseDouble(keyvalue.get("minvalue"));
				maxvalue = maxvalue * 1.1;
				if (maxvalue < 1)
				{
					maxvalue = 1;
				}
				if (keyvalue.get("minvalue").contains("-"))
				{
					temmap = buildImageMapDEL(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data, 20, maxvalue, maxd, minvalue, true,600,300);
				} else
				{
					temmap = buildImageMapDEL(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data, 20, maxvalue, maxd, 0, true,600,300);
				}
			}
			maptable.appendChild(temmap);
		}
		
	}
	
	/**
	 * 构建数据
	 * 
	 * @param imgdata
	 * @return
	 */
	public static XYDataset buildDataset(Map<Date, String> imgdata)
	{
		TimeSeries timeseries = new TimeSeries("刷新时间", org.jfree.data.time.Second.class);
	
		for (Date date1 : imgdata.keySet())
		{
			int ss = date1.getSeconds();
			int mm = date1.getMinutes();
			int hh = date1.getHours();
			int d = date1.getDate();
			int m = date1.getMonth() + 1;
			int y = date1.getYear() + 1900;
			
			org.jfree.data.time.Second ttime = new Second(ss, mm, hh, d, m, y);
			String value = imgdata.get(date1);
			// equals("(status)bad")||value.trim().equals("(status)disable") (status)null
			if (value.trim().startsWith("(status)"))
			{
				timeseries.add(ttime, null);
			} else
			{
				if (value.isEmpty())
				{
					timeseries.add(ttime, null);
				} else
				{
					timeseries.add(ttime, Double.parseDouble(value));
				}
				
			}
		}

		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(timeseries);
		return timeseriescollection;
		
	}
	
	/**
	 * 生成图片函数
	 * 
	 * @param title
	 *            主标题
	 * @param subtite
	 *            副标题
	 * @param valuelabel
	 *            值label显示
	 * @param data
	 *            数据xydataset
	 * @param max
	 *            y轴最大
	 * @param step
	 *            刻度
	 * @param maxdate
	 *            最大日期
	 * @return 图。png
	 */
	public static Imagemap buildImageMapDEL(String title, String subtite, String valuelabel, XYDataset data, double step, double max, Date maxdate, double min, boolean xlabel,int pngwidth,int pngheight)
	{
		Imagemap chartImagemap = new Imagemap();
		JFreeChart basechart = ChartFactory.createTimeSeriesChart("", "", valuelabel, data, false, true, false);
	
		// 设置标题子标题
		TextTitle texttitle = new TextTitle(title, new Font("黑体", Font.PLAIN, 12));
		texttitle.setPaint(new Color(30, 91, 153));
		basechart.setTitle(texttitle);
		TextTitle subtexttitle = new TextTitle(subtite, new Font("黑体", Font.PLAIN, 12));
		subtexttitle.setPaint(new Color(30, 91, 153));
		basechart.addSubtitle(subtexttitle);
		TextTitle subtexttitle1 = new TextTitle("", new Font("黑体", Font.PLAIN, 12));
		subtexttitle1.setPaint(new Color(30, 91, 153));
		basechart.addSubtitle(subtexttitle1);
		//
		basechart.setBackgroundPaint(Color.WHITE);
		XYPlot plot = (XYPlot) basechart.getPlot();
		plot.setForegroundAlpha(1.0f);// 背景表格透明度
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.BLACK);// 纵坐标格线颜色
		plot.setDomainGridlinePaint(Color.BLACK);// 横坐标格线颜色
		plot.setDomainGridlinesVisible(true);// 显示横坐标格线
		plot.setRangeGridlinesVisible(true);// 显示纵坐标格线
		plot.setAxisOffset(new RectangleInsets(0.3, 0.9, 0.9, 0.3));// 设置曲线图与xy轴的距离
		// 曲线颜色
		plot.getRenderer().setSeriesPaint(0, new Color(95, 165, 95));
		plot.getRenderer().setSeriesPaint(1, Color.red);
		// 设置内边框
		//plot.setOutlineVisible(true);
		plot.setOutlineStroke(new BasicStroke(1.0f));
		plot.setOutlinePaint(new Color(160, 128, 64));
		// 设置外边框
		basechart.setBorderVisible(true);
		// 日期格式设置
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		if (xlabel)
		{
			axis.setDateFormatOverride(new SimpleDateFormat("yy/MM/dd hh:mm:ss"));
			axis.setLabelFont(new Font("黑体", Font.PLAIN, 10));
			axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
			axis.setVerticalTickLabels(true);
			// axis.setMinorTickCount(10);
			if (maxdate != null)
			{
				axis.setMaximumDate(maxdate);
			}
			axis.setAutoTickUnitSelection(true);
			axis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());// 設置標準的時間刻度單位
		} else
		{
			axis.setTickLabelsVisible(false);
		}
		// 设置轴线粗细
		axis.setAxisLineStroke(new BasicStroke(1.0f));
		axis.setAxisLinePaint(Color.BLACK);
		// 日期换行
		// axis.setAutoTickUnitSelection(true);
		// axis.setTickUnit(new DateTickUnit(1, 2, new SimpleDateFormat("MM/dd")));
		// xis.setVerticalTickLabels(true);
		// 设置最大值和最小值
		ValueAxis valueaxis = plot.getRangeAxis();
		if (min == 0)
		{
			valueaxis.setRange(0, max);
		} else
		{
			valueaxis.setRange(min, max);
		}
		valueaxis.setAutoRange(false);
		// 设置轴线粗细
		valueaxis.setAxisLineStroke(new BasicStroke(1.0f));
		valueaxis.setAxisLinePaint(Color.BLACK);
		// 设置刻度
		if (max == 1 || step == 10)
		{
			valueaxis.setAutoTickUnitSelection(false);
			NumberTickUnit nt = new NumberTickUnit(step);
			((NumberAxis) valueaxis).setTickUnit(nt);
		}
		// 值中文文
		valueaxis.setLabelFont(new Font("黑体", Font.PLAIN, 12));
		// 设置实际点的值
		// XYLineAndShapeRenderer XYLineAndShapeRenderer = (XYLineAndShapeRenderer) plot.getRenderer();
		// XYLineAndShapeRenderer.setBaseShapesVisible(true); // series点（即数据点）可见
		// XYLineAndShapeRenderer.setBaseLinesVisible(true); // series点（即数据点）间有连线可见
		// XYLineAndShapeRenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		// XYLineAndShapeRenderer.setBaseItemLabelsVisible(true);// 显示折点数据
		// 设置提示
		ChartRenderingInfo info = new ChartRenderingInfo();
		BufferedImage bi = basechart.createBufferedImage(pngwidth, pngheight, BufferedImage.SCALE_FAST, info);
		for (Iterator it = info.getEntityCollection().getEntities().iterator(); it.hasNext();)
		{
			ChartEntity ce = (ChartEntity) it.next();
			if (ce instanceof XYAnnotationEntity)
			{
				Area area = new Area();
				area.setParent(chartImagemap);
				area.setCoords(ce.getShapeCoords());
				area.setShape(ce.getShapeType());
				// area.setId(chartImagemap.getId()+'_'+((PieSectionEntity)ce).getSectionIndex());
				// area.setTooltiptext(((XYAnnotationEntity)ce).getSectionKey().toString());
			}
		}
		try
		{
			byte[] bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);
			AImage image = new AImage("Chart", bytes);
			chartImagemap.setContent(image);
		} catch (IOException ex)
		{
			return null;
		}
		return chartImagemap;
	}
	public static BufferedImage buildBufferImage(String title, String subtite, String valuelabel, XYDataset data, double step, double max, Date maxdate, double min, boolean xlabel,int pngwidth,int pngheight)
	{
		Imagemap chartImagemap = new Imagemap();
		JFreeChart basechart = ChartFactory.createTimeSeriesChart("", "", valuelabel, data, false, true, false);
		// 设置标题子标题
		TextTitle texttitle = new TextTitle(title, new Font("黑体", Font.PLAIN, 12));
		texttitle.setPaint(new Color(30, 91, 153));
		basechart.setTitle(texttitle);
		TextTitle subtexttitle = new TextTitle(subtite, new Font("黑体", Font.PLAIN, 12));
		subtexttitle.setPaint(new Color(30, 91, 153));
		basechart.addSubtitle(subtexttitle);
		TextTitle subtexttitle1 = new TextTitle("", new Font("黑体", Font.PLAIN, 12));
		subtexttitle1.setPaint(new Color(30, 91, 153));
		basechart.addSubtitle(subtexttitle1);
	
		//
		basechart.setBackgroundPaint(Color.WHITE);
		XYPlot plot = (XYPlot) basechart.getPlot();
		plot.setForegroundAlpha(1.0f);// 背景表格透明度
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.BLACK);// 纵坐标格线颜色
		plot.setDomainGridlinePaint(Color.BLACK);// 横坐标格线颜色
		plot.setDomainGridlinesVisible(true);// 显示横坐标格线
		plot.setRangeGridlinesVisible(true);// 显示纵坐标格线
		plot.setAxisOffset(new RectangleInsets(0.3, 0.9, 0.9, 0.3));// 设置曲线图与xy轴的距离
		//plot.setAxisOffset(new RectangleInsets(1.0, 0.9, 2.0, 2.0));// 设置曲线图与xy轴的距离
		// 曲线颜色
		plot.getRenderer().setSeriesPaint(0, new Color(95, 165, 95));
		plot.getRenderer().setStroke(new BasicStroke(2.0f));
		// 设置内边框
		//plot.setOutlineVisible(true);
		plot.setOutlineStroke(new BasicStroke(1.0f));
		plot.setOutlinePaint(new Color(160, 128, 64));
		// 设置外边框
		basechart.setBorderVisible(true);
		// 日期格式设置
		DateAxis axis = (DateAxis) plot.getDomainAxis();
		if (xlabel)
		{
			axis.setDateFormatOverride(new SimpleDateFormat("MM/dd hh:mm"));
			axis.setLabelFont(new Font("黑体", Font.PLAIN, 10));
			axis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
			//axis.setVerticalTickLabels(true);
			// axis.setMinorTickCount(10);
			if (maxdate != null)
			{
				try {
					axis.setMaximumDate(maxdate);
				} catch (Exception e) { 
					axis.setMaximumDate(new Date());
				}
			}
			axis.setAutoTickUnitSelection(true);
			axis.setStandardTickUnits(DateAxis.createStandardDateTickUnits());// 設置標準的時間刻度單位
		} else
		{
			axis.setTickLabelsVisible(false);
		}
		// 设置轴线粗细
		axis.setAxisLineStroke(new BasicStroke(1.0f));
		axis.setAxisLinePaint(Color.BLACK);
		
		// 日期换行
		// axis.setAutoTickUnitSelection(true);
		// axis.setTickUnit(new DateTickUnit(1, 2, new SimpleDateFormat("MM/dd")));
		// xis.setVerticalTickLabels(true);
		// 设置最大值和最小值
		ValueAxis valueaxis = plot.getRangeAxis();
		if (min == 0)
		{
			valueaxis.setRange(0, max);
		} else
		{
			valueaxis.setRange(min, max);
		}
		valueaxis.setAutoRange(false);
		// 设置轴线粗细
		valueaxis.setAxisLineStroke(new BasicStroke(1.0f));
		valueaxis.setAxisLinePaint(Color.BLACK);
		// 设置刻度
		if (max == 1 || step == 10)
		{
			valueaxis.setAutoTickUnitSelection(false);
			NumberTickUnit nt = new NumberTickUnit(step);
			((NumberAxis) valueaxis).setTickUnit(nt);
		}
		// 值中文文
		valueaxis.setLabelFont(new Font("黑体", Font.PLAIN, 12));
		// 设置实际点的值
		// XYLineAndShapeRenderer XYLineAndShapeRenderer = (XYLineAndShapeRenderer) plot.getRenderer();
		// XYLineAndShapeRenderer.setBaseShapesVisible(true); // series点（即数据点）可见
		// XYLineAndShapeRenderer.setBaseLinesVisible(true); // series点（即数据点）间有连线可见
		// XYLineAndShapeRenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator());
		// XYLineAndShapeRenderer.setBaseItemLabelsVisible(true);// 显示折点数据
		// 设置提示
		ChartRenderingInfo info = new ChartRenderingInfo();
		return basechart.createBufferedImage(pngwidth, pngheight, BufferedImage.SCALE_FAST, info);
	}
	
	/*
	 * 构建柱状图数据
	 */
	public static  DefaultCategoryDataset  buildCategoryDataset(Map<Date, String> imgdata)
	{
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		StringBuilder sb = new StringBuilder();
		for (Date date1 : imgdata.keySet())
		{
			
			String value = imgdata.get(date1);
			// equals("(status)bad")||value.trim().equals("(status)disable") (status)null
			SimpleDateFormat dateformat= new SimpleDateFormat("MM/dd hh:mm");
			String lable=dateformat.format(date1);
			try
			{
			if (value.trim().startsWith("(status)"))
			{
				dataset.addValue(0, "", lable);
			} else
			{
				if (value.isEmpty())
				{
					dataset.addValue(0,"", lable);
				} else
				{
					dataset.addValue(Double.parseDouble(value), "", lable);
				}
				
			}
			}catch(Exception ex)
			{
				
			}
		}
		return dataset;
		
	}
	public static BufferedImage buildCategoryBufferImage(String title,
			String subtite, String valuelabel, DefaultCategoryDataset data,
			double step, double max, Date maxdate, double min, boolean xlabel,
			int pngwidth, int pngheight) {
		JFreeChart basechart = ChartFactory.createBarChart3D(null, null,
				valuelabel, data, PlotOrientation.VERTICAL, false, true, false);
		// 设置标题子标题
		TextTitle texttitle = new TextTitle(title, new Font("黑体", Font.PLAIN,
				12));
		texttitle.setPaint(new Color(30, 91, 153));
		basechart.setTitle(texttitle);
		TextTitle subtexttitle = new TextTitle(subtite, new Font("黑体",
				Font.PLAIN, 12));
		subtexttitle.setPaint(new Color(30, 91, 153));
		basechart.addSubtitle(subtexttitle);
		TextTitle subtexttitle1 = new TextTitle("", new Font("黑体", Font.PLAIN,
				12));
		subtexttitle1.setPaint(new Color(30, 91, 153));
		basechart.addSubtitle(subtexttitle1);

		basechart.setBackgroundPaint(Color.WHITE);
		CategoryPlot plot = basechart.getCategoryPlot();
		plot.setForegroundAlpha(1.0f);// 背景表格透明度
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.BLACK);// 纵坐标格线颜色
		plot.setDomainGridlinePaint(Color.BLACK);// 横坐标格线颜色
		
		plot.setDomainGridlinesVisible(false);// 显示横坐标格线
		plot.setRangeGridlinesVisible(true);// 显示纵坐标格线
		
		
		plot.setAxisOffset(new RectangleInsets(0.3, 0.9, 0.9, 0.3));// 设置曲线图与xy轴的距离
		// 曲线颜色
		plot.getRenderer().setSeriesPaint(0, new Color(95, 165, 95));
		plot.getRenderer().setStroke(new BasicStroke(1.5f));
		BarRenderer3D renderer = (BarRenderer3D) plot.getRenderer();
		// 设置bar的最小宽度，以保证能显示数值
		// 最大宽度
		renderer.setMaximumBarWidth(0.01);
		renderer.setBasePositiveItemLabelPosition(new ItemLabelPosition(
				ItemLabelAnchor.OUTSIDE12, TextAnchor.BASELINE_CENTER)); 
		renderer.setItemMargin(1);
		//
		CategoryAxis axis = (CategoryAxis) plot.getDomainAxis();
		axis.setCategoryLabelPositions(CategoryLabelPositions.UP_45); 
		if (data.getColumnCount() > 30) axis.setVisible(false);
		// axis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI
		// / 2.0));
		// axis.setMaximumCategoryLabelLines(100);
		// axis.setMaximumCategoryLabelWidthRatio(100);

		// 设置内边框
		// plot.setOutlineVisible(true);
		plot.setOutlineStroke(new BasicStroke(1.0f));
		plot.setOutlinePaint(new Color(160, 128, 64));
		
		// 设置外边框
		basechart.setBorderVisible(true);

		NumberAxis valueaxis = (NumberAxis) plot.getRangeAxis();
		if (min == 0) {
			valueaxis.setRange(0, max);
		} else {
			valueaxis.setRange(min, max);
		}
		// valueaxis.setAutoRange(false);
		// 设置轴线粗细
		valueaxis.setAxisLineStroke(new BasicStroke(1.0f));
		valueaxis.setAxisLinePaint(Color.BLACK);
		valueaxis.setAutoTickUnitSelection(true);
		// 设置刻度
		if (max == 1 || step == 10) {
			 valueaxis.setAutoTickUnitSelection(false);
			 NumberTickUnit nt = new NumberTickUnit(step);
			 valueaxis.setTickUnit(nt);
		}
		// 值中文文
		valueaxis.setLabelFont(new Font("黑体", Font.PLAIN, 12));
		ChartRenderingInfo info = new ChartRenderingInfo();
		if (data.getColumnCount() > 30) {
			BufferedImage bi = basechart.createBufferedImage(pngwidth, pngheight + 20,
					BufferedImage.SCALE_FAST, info);
			return imageAppendDataaxis(bi,"" + data.getColumnKey(0),"" + data.getColumnKey(data.getColumnCount() - 1));
		}
		return basechart.createBufferedImage(pngwidth, pngheight + 20,
				BufferedImage.SCALE_FAST, info);
	}

	public static BufferedImage imageAppendDataaxis(BufferedImage img, String beginStr, String endStr) {
		BufferedImage image = new BufferedImage(img.getWidth(), img.getHeight() + 20,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D) image.getGraphics();
		g2.setBackground(Color.WHITE);
		for (int i=0;i<image.getWidth();i++) {
			g2.drawLine(i, 0, i,image.getHeight());
		}
		Graphics2D dg2 = (Graphics2D) img.getGraphics();
		g2.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
		g2.setColor(Color.WHITE);
		//去掉源图片最下方的黑色横线
		g2.drawLine(0, img.getHeight() - 1 , img.getWidth(), img.getHeight() - 1);
		g2.drawLine(0, img.getHeight() , img.getWidth(), img.getHeight());
		
		g2.setColor(Color.BLACK);
		//增加源图片最左右两边的黑色竖线
		g2.drawLine(0, 0, 0, image.getHeight());
		g2.drawLine(image.getWidth()-1, 0, image.getWidth()-1, image.getHeight());
		g2.drawLine(0, image.getHeight()-1, image.getWidth(), image.getHeight()-1);
		//两个起始值
		if (beginStr.length() > 11) beginStr = beginStr.substring(0, 11);
		if (endStr.length() > 11) endStr = endStr.substring(0, 11);
		g2.drawString(beginStr, 40, img.getHeight() + 10);
		g2.drawString(endStr, img.getWidth() - 80, img.getHeight() + 10);
		g2.dispose();
		return image;
	}
	/*
	 * private void buildChart() { chartImagemap = new Imagemap(); chartImagemap.setParent(maptable); basechart = ChartFactory.createTimeSeriesChart("", "", "CPU综合使用率(%)",
	 * createDataset(), false, true, false); // 标题子标题 TextTitle title = new TextTitle("CPU综合使用率(%)", new Font("黑体", Font.PLAIN, 12)); title.setPaint(new Color(30, 91, 153));
	 * basechart.setTitle(title); TextTitle subtitle = new TextTitle("最大平均最小", new Font("黑体", Font.PLAIN, 12)); subtitle.setPaint(new Color(30, 91, 153));
	 * basechart.addSubtitle(subtitle);
	 * 
	 * basechart.setBackgroundPaint(Color.WHITE); XYPlot plot = (XYPlot) basechart.getPlot(); plot.setForegroundAlpha(1.0f);// 背景表格透明度
	 * 
	 * // 底文字 // basechart.getLegend().setItemFont(new Font("宋体", Font.PLAIN, 9)); // 背景 plot.setBackgroundPaint(Color.white); // plot.setDomainGridlineStroke(new
	 * BasicStroke(1.0f)); // plot.setRangeGridlineStroke(new BasicStroke(1.0f)); plot.setRangeGridlinePaint(Color.BLACK);// 纵坐标格线颜色 plot.setDomainGridlinePaint(Color.BLACK);//
	 * 横坐标格线颜色 plot.setDomainGridlinesVisible(true);// 显示横坐标格线 plot.setRangeGridlinesVisible(true);// 显示纵坐标格线 plot.setAxisOffset(new RectangleInsets(0.3, 0.9, 0.9, 0.3));//
	 * 设置曲线图与xy轴的距离 // 曲线颜色 plot.getRenderer().setSeriesPaint(0, new Color(95, 165, 95)); // 渐变 // plot.setBackgroundPaint(new GradientPaint(0, 0, Color.white, 0, 1000,
	 * Color.blue)); // 设置内边框 plot.setOutlineVisible(true); plot.setOutlineStroke(new BasicStroke(1.0f)); plot.setOutlinePaint(new Color(160, 128, 64)); // 设置外边框
	 * basechart.setBorderVisible(true); // 日期格式设置 DateAxis axis = (DateAxis) plot.getDomainAxis(); axis.setDateFormatOverride(new SimpleDateFormat("MM/dd/yy hh:mm:ss"));
	 * axis.setLabelFont(new Font("黑体", Font.PLAIN, 10)); // 设置轴线粗细 axis.setAxisLineStroke(new BasicStroke(1.0f)); axis.setAxisLinePaint(Color.BLACK); // 日期换行 //
	 * axis.setAutoTickUnitSelection(true); // axis.setTickUnit(new DateTickUnit(1, 2, new SimpleDateFormat("MM/dd"))); // xis.setVerticalTickLabels(true); // 设置最大值和最小值 ValueAxis
	 * valueaxis = plot.getRangeAxis(); valueaxis.setRange(0.0D, 100D); valueaxis.setAutoRange(false); // 设置轴线粗细 valueaxis.setAxisLineStroke(new BasicStroke(1.0f));
	 * valueaxis.setAxisLinePaint(Color.BLACK); // 设置刻度 valueaxis.setAutoTickUnitSelection(false); NumberTickUnit nt = new NumberTickUnit(10d); ((NumberAxis)
	 * valueaxis).setTickUnit(nt);
	 * 
	 * // 值中文文 valueaxis.setLabelFont(new Font("黑体", Font.PLAIN, 12)); // 设置实际点的值 XYLineAndShapeRenderer XYLineAndShapeRenderer = (XYLineAndShapeRenderer) plot.getRenderer();
	 * XYLineAndShapeRenderer.setBaseShapesVisible(true); // series点（即数据点）可见 XYLineAndShapeRenderer.setBaseLinesVisible(true); // series点（即数据点）间有连线可见
	 * XYLineAndShapeRenderer.setBaseItemLabelGenerator(new StandardXYItemLabelGenerator()); XYLineAndShapeRenderer.setBaseItemLabelsVisible(true);// 显示折点数据 // 设置提示
	 * 
	 * ChartRenderingInfo info = new ChartRenderingInfo(); BufferedImage bi = basechart.createBufferedImage(500, 300, BufferedImage.TRANSLUCENT, info);
	 * 
	 * for (Iterator it = info.getEntityCollection().getEntities().iterator(); it.hasNext();) { ChartEntity ce = (ChartEntity) it.next(); if (ce instanceof XYAnnotationEntity) {
	 * Area area = new Area(); area.setParent(chartImagemap); area.setCoords(ce.getShapeCoords()); area.setShape(ce.getShapeType()); //
	 * area.setId(chartImagemap.getId()+'_'+((PieSectionEntity)ce).getSectionIndex()); // area.setTooltiptext(((XYAnnotationEntity)ce).getSectionKey().toString()); } } try { byte[]
	 * bytes = EncoderUtil.encode(bi, ImageFormat.PNG, true);
	 * 
	 * AImage image = new AImage("Chart", bytes); chartImagemap.setContent(image); } catch (IOException ex) { }
	 * 
	 * }
	 */

}
