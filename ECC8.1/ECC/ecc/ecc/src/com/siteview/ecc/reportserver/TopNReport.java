package com.siteview.ecc.reportserver;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.zkoss.zul.Image;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.Report;
import com.siteview.base.data.ReportDate;
import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.template.TemplateManager;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.report.topnreport.TopNReportDatasource;
import com.siteview.ecc.report.topnreport.TopNReportListBean;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svdb.UnivData;

public class TopNReport implements Runnable
{
	//
	
	private String										sessionId;
	private String										error_message;
	
	public String										strReportName;												// 根据时间段等生成的报告名称
																													
	public ArrayList<Report>							lstMonitorReport;											// 根据用户选择的监测器类型和监测器串
	// 过滤出正确的参与Topn报告的数据列表
	
	// private String strReportSort; // 柱状图的排序方式
	// private String strReportCount; // 柱状图的数量确
	private String										title;
	private String										ttitle;													// 图的标题
	private String										ytitle;													// 图y标题
	// 报告属性
	private String										reportid;
	private View										view;
	private String[]									lstIds;													// 用户选择的监测器
	private ReportDate									rd;
	private Date										tmStart;
	private Date										tmEnd;
	private String										strCount;													// 柱状图的数量
	private String										strSelType;												// 用户选择的监测器类型
	private String										strMark;													// 用户选择的监测器返回值名称
	private String										strSort;													// 柱状图的排序方式
	private String										strget;
	private String										fileType;
	private String										reportTitle;
	private List<String>								listnid	= new ArrayList<String>();							// 前n名的id串
	private Boolean										auto	= true;
	// 手动生成报告是否完成
	private Boolean										finish	= false;

	public void setFinish(Boolean finish)
	{
		this.finish = finish;
	}
	private Map<String, Map<String, String>>	mapData	= new HashMap<String, Map<String, String>>();
	private List list = new ArrayList();
	
	private int progressvalue=1;
	public int getProgressvalue()
	{
		return  this.progressvalue;
	}
    private Boolean finishGerateFile=false;
    public Boolean getFinishGerateFile()
    {
    	return this.finishGerateFile;
    }

	public TopNReport(String ReportId, Map<String, String> DefineMap, Date tmStart, Date tmEnd, View w, Boolean auto)
	{
		this.reportid = ReportId;
		this.view = w;
		this.tmStart = tmStart;
		this.tmEnd = tmEnd;
		String tmpTitle = DefineMap.get("Title");
		int index = tmpTitle.indexOf("|");
		if(index>0){
			this.reportTitle = tmpTitle.substring(0,index);
		}else{
			this.reportTitle = tmpTitle;
		}
			
		this.title = this.reportTitle.replace(' ', '_').replace(':', '_').replace('*', '_').replace('/', '_').replace('\\', '_').replace('?', '_').replace('|', '_').replace('<',
				'_').replace('>', '_');
		this.strReportName = String.valueOf(this.tmStart.getTime()) + "-" + String.valueOf(this.tmEnd.getTime()) + "TopN";
		this.lstIds = DefineMap.get("GroupRight").split(",");
		this.strSelType = DefineMap.get("Type");
		this.strMark = DefineMap.get("Mark");
		this.ttitle = this.strSelType;
		this.ytitle = this.strMark;
		this.finishGerateFile=false;
		this.finish=false;
		this.strCount = DefineMap.get("Count");
		String trSort = DefineMap.get("Sort");
		if (trSort == null || trSort.equals(""))
		{
			trSort = "升序";
		}
		this.strSort = trSort;
		this.reportTitle = this.reportTitle + "(" + trSort + ")";
		String trget = DefineMap.get("GetValue");
		if (trget == null || trget.equals(""))
		{
			trget = "平均";
		}
		this.strget = trget;
		String strReportType = DefineMap.get("fileType");
		if (strReportType == null || strReportType.equals(""))
		{
			strReportType = "html";
		}
		this.fileType = strReportType;
		this.auto = auto;
	}
	
	private void readData()
	{
		String tmpnodeids = "";
		try
		{
			try
			{
				int nCount = Integer.parseInt(strCount);
				if (nCount == 0)
				{
					return;
				}
			} catch (Exception ex)
			{
				return;
			}
			if ("".equals(strSelType) || strSelType == null)
				return;
			
			if ("".equals(strMark) || strMark == null)
				return;
			
			// 根据用户选择的监测器类型和监测器串来过滤出属于此类型的监测器id串
			Map<String, Map<String, String>> monitoidToType = QueryTopnMonitorInfo();
			String strTmp = "";
			for (int i = 0; i < lstIds.length; i++)
			{
				String mid = lstIds[i];
				if (!"".equals(mid) && monitoidToType.keySet().contains(mid))
				{
					try
					{// 检查是否同类型监测器
						String templateid = view.getMonitorInfo(view.getNode(mid)).getMonitorType();
						String type = findById(templateid);
						if (strSelType.equals(type))
						{
							strTmp += mid;
							strTmp += ",";
						}
					} catch (Exception e)
					{
						e.printStackTrace();
						continue;
					}
				}
			}
			lstIds = strTmp.split(",");
			for (int i = 0; i < this.lstIds.length; i++)
			{
				INode n = view.getNode(lstIds[i]);
				if (n == null)
				{
					error_message = "节点不存在或无权访问！";
					continue;
				}
				if (!n.getType().equals(INode.MONITOR))
				{
					error_message = "节点类型非法！";
					continue;
				}
				MonitorInfo info = view.getMonitorInfo(n);
				if (info == null)
				{
					error_message = "节点不存在或无权访问！";
					continue;
				}
				tmpnodeids = lstIds[i] + "," + tmpnodeids;
			}
			lstIds = tmpnodeids.split(",");
			// 分批传输
			int pl = (lstIds.length / 20);
			int	vaule=100;
			if(pl>0)
		  	vaule=100/pl;
			else
				pl=1;
			
			for (int pn = 0; pn <pl; pn++)
			{
				// 手动结束
				if (finish)
				{
					return;
				}
				int fromindex = pn * 20;
				if (pn > 0)
				{
					fromindex = fromindex + 1;
				}
				int toindex = 0;
				if (pn < pl-1)
				{
					toindex = fromindex + 20;
				} else
				{
					toindex = lstIds.length;
				}
				if(fromindex==toindex)
				{
					return;
				}
				String tmpids = "";
				for (int tn = fromindex; tn < toindex; tn++)
				{
					tmpids = tmpids + lstIds[tn] + ",";
				}
				rd = new ReportDate(tmStart, tmEnd);
				rd.getReportDate(tmpids, "", false, "");
				for (String key : this.mapData.keySet())
				{
					tmpids = tmpids + key + ",";
					if (!rd.getM_fmap().containsKey(key))
					{
						rd.getM_fmap().put(key, mapData.get(key));
					}
				}
			    this.mapData.clear();
				buildSortData(rd, tmpids.split(","));
				this.progressvalue=vaule++;
			}		
			int vbb=this.mapData.size();
			rd.setM_fmap(this.mapData);
			this.progressvalue=100;
		} catch (Exception ex)
		{
			ex.printStackTrace();
			this.finish=true;
			error_message = ex.getMessage();
		}
		
	}
	
	private void buildSortData(ReportDate reportdate, String[] tmpids)
	{
		
		double dMaxorAverage = 0;
		String strMaxorAverage = "", strName = "", returnName = "";
		int datacounts = 0;
		List list = new ArrayList();
		
		for (String sid : tmpids)
		{
			if (sid.equals(""))
			{
				continue;
			}
			for (int i = 0; i < reportdate.getReturnSize(sid); i++)
			{
				returnName = reportdate.getReturnValue(sid, "ReturnName", i);
				// 不是选择的返回值则不用显示了
				if (!returnName.equals(strMark))
				{
					continue;
				}
				// 最新值或平均值等(Y轴)
				if (strget.equals("平均"))
				{
					strMaxorAverage = reportdate.getReturnValue(sid, "average", i);
				} else
				{
					strMaxorAverage = reportdate.getReturnValue(sid, "latest", i);
				}
				try
				{
					dMaxorAverage = Double.parseDouble(strMaxorAverage);
				} catch (Exception e)
				{
					dMaxorAverage = 0;
				}
			}
			strName = reportdate.getPropertyValue(sid, "MonitorName");
			// 监测器名称(X轴)
			datacounts++;
			list.add(new ChartObjectData(dMaxorAverage, Integer.toString(datacounts), strName, strSort, sid));
		}
		// 进行排序
		Collections.sort(list, new ChartObjectData());
		this.list=list;
		/*
		 * 添加数据进数据集
		 */
		Iterator iter = list.iterator();
		int i = 1;
		int count = Integer.parseInt(strCount);
		while (iter.hasNext())
		{
			
			if (i > count)
			{// 超过最多显示数量了
				break;
			}
			
			ChartObjectData threeDataTmp = (ChartObjectData) iter.next();
			String id = threeDataTmp.id;
			mapData.put(id, reportdate.getM_fmap().get(id));
			for (int j = 0; j < reportdate.getReturnSize(id); j++)
			{
				String mkey = "(Return_" + new Integer(j ).toString() + ")" + id;
				if(reportdate.getM_fmap().containsKey(mkey) )
				{
			      mapData.put(mkey, reportdate.getM_fmap().get(mkey));
				}
			}
			i++;
		}
		
	}
	
	/**
	 * 
	 * 实现排序用的
	 * 
	 */
	class ChartObjectData implements Comparator
	{
		String	rowKey; // 行标签
		String	colKey; // 列标签
		double	value;	// 值
		String	sort;
		String	id;
		
		ChartObjectData()
		{
		}
		
		ChartObjectData(double value, String rowKey, String colKey, String sort, String id)
		{
			this.value = value;
			this.rowKey = rowKey;
			this.colKey = colKey;
			this.sort = sort;
			this.id = id;
		}
		
		/*
		 * 比较的算法
		 */
		public int compare(Object obj1, Object obj2)
		{
			
			double a = ((ChartObjectData) obj1).value;
			double b = ((ChartObjectData) obj2).value;
			String sort = ((ChartObjectData) obj2).sort;
			if (sort.equals("降序"))
			{
				if (a > b)
				{
					return 0;
				} else
				{
					return 1;
				}
			} else
			{
				if (a > b)
				{
					return 1;
				} else
				{
					return 0;
				}
			}
		}
		
	}
	
	// 构造Topn报告图片数据
	private DefaultCategoryDataset buildDataset()
	{
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
//		
//		double dMaxorAverage = 0;
//		String strMaxorAverage = "", strName = "", returnName = "";
//		int datacounts = 0;
//		List list = new ArrayList();
//		
//		for (String sid : lstIds)
//		{
//			if (sid.equals(""))
//			{
//				continue;
//			}
//			for (int i = 0; i < rd.getReturnSize(sid); i++)
//			{
//				returnName = rd.getReturnValue(sid, "ReturnName", i);
//				// 不是选择的返回值则不用显示了
//				if (!returnName.equals(strMark))
//				{
//					continue;
//				}
//				// 最新值或平均值等(Y轴)
//				if (strget.equals("平均"))
//				{
//					strMaxorAverage = rd.getReturnValue(sid, "average", i);
//				} else
//				{
//					strMaxorAverage = rd.getReturnValue(sid, "latest", i);
//				}
//				try
//				{
//					dMaxorAverage = Double.parseDouble(strMaxorAverage);
//				} catch (Exception e)
//				{
//					dMaxorAverage = 0;
//				}
//			}
//			strName = rd.getPropertyValue(sid, "MonitorName");
//			// 监测器名称(X轴)
//			datacounts++;
//			list.add(new ChartObjectData(dMaxorAverage, Integer.toString(datacounts), strName, strSort, sid));
//		}
//		// 进行排序
//		Collections.sort(list, new ChartObjectData());
		
		/*
		 * 添加数据进数据集
		 */
		Iterator iter = this.list.iterator();
		int i = 1;
		int count = Integer.parseInt(strCount);
		listnid.clear();
		while (iter.hasNext())
		{
			
			if (i > count)
			{// 超过最多显示数量了
				break;
			}
			
			ChartObjectData threeDataTmp = (ChartObjectData) iter.next();
			listnid.add(threeDataTmp.id);
			System.out.println("("+i+")"+threeDataTmp.colKey + " **********************************************************");
			dataset.addValue(threeDataTmp.value, i + "", "("+i+")"+threeDataTmp.colKey);
			i++;
		}
		
		return dataset;
	}
	
	/**
	 * 手动生成TOPN报告的图片
	 * 
	 * @param title
	 * @param xtitle
	 * @param ytitle
	 * @param width
	 * @param height
	 * @return
	 */
	public Image buildImage(String title, String xtitle, String ytitle, int width, int height)
	{
		
		// 构造Topn报告图片数据
		DefaultCategoryDataset dataset = buildDataset();
		// 主标题 + X标题 + Y标题 + 数据
		JFreeChart chart = ChartFactory.createBarChart3D(title, xtitle, ytitle, dataset, PlotOrientation.VERTICAL, false, false, false);
		
		chart.setBackgroundPaint(new Color(0xE1E1E1));
		CategoryPlot plot = chart.getCategoryPlot();
		// 设置Y轴显示整数
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		// 横坐标竖显示
		rangeAxis.setVerticalTickLabels(true);
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		CategoryAxis domainAxis = plot.getDomainAxis();
		// 设置距离图片左端距离
		domainAxis.setLowerMargin(0.05);
		BarRenderer3D renderer = new BarRenderer3D();
		// 设置柱的颜色
		renderer.setSeriesPaint(0, new Color(0xff00));
		plot.setRenderer(renderer);
		// 存储
		// chart.createBufferedImage(width, height);
		// ChartUtilities.getImageMap(name, info)
		ChartRenderingInfo info = new ChartRenderingInfo();
		BufferedImage bi = chart.createBufferedImage(width, height, BufferedImage.TRANSLUCENT, info);
		
		return Toolkit.getToolkit().createImage(bi);
	}
	
	private List buildBean(String tag)
	{
		List list = new ArrayList();
		if ("StatisticsBean".equals(tag))
		{
			for (String sid : listnid)
			{
				if (sid.equals(""))
				{
					continue;
				}
				for (int i = 0; i < rd.getReturnSize(sid); i++)
				{
					String drawmeasure = rd.getReturnValue(sid, "sv_drawmeasure", i);
					if (!"1".equals(drawmeasure))
					{
						continue;
					}
					String returnName = rd.getReturnValue(sid, "ReturnName", i);
					// 不是选择的返回值则不用显示了
					if (!returnName.equals(strMark))
					{
						continue;
					}
					String max = rd.getReturnValue(sid, "max", i);
					String min = rd.getReturnValue(sid, "min", i);
					String latest = rd.getPropertyValue(sid, "latestDstr");
					if (latest == null)
						latest = "";
					String average = rd.getReturnValue(sid, "average", i);
					String monitorname = rd.getPropertyValue(sid, "MonitorName");
					String entityname = monitorname.substring(0, monitorname.indexOf(":"));
					monitorname = monitorname.substring(monitorname.indexOf(":") + 1);
					list.add(new TopNReportListBean(entityname, monitorname, max, average, min, latest));
				}
			}
		}
		return list;
	}
	
	public List buildCategoryBufferImage(String title, String subtite, String valuelabel, int pngwidth, int pngheight, boolean isstream)
	{
		List list = null;
		DefaultCategoryDataset data = buildDataset();
		JFreeChart basechart = ChartFactory.createBarChart3D(null, null, valuelabel, data, PlotOrientation.VERTICAL, false, true, false);
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
		
		basechart.setBackgroundPaint(Color.WHITE);
		CategoryPlot plot = basechart.getCategoryPlot();
		plot.setForegroundAlpha(1.0f);// 背景表格透明度
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.BLACK);// 纵坐标格线颜色
		plot.setDomainGridlinePaint(Color.BLACK);// 横坐标格线颜色
		plot.setDomainGridlinesVisible(true);// 显示横坐标格线
		plot.setRangeGridlinesVisible(true);// 显示纵坐标格线
		plot.setAxisOffset(new RectangleInsets(0.3, 0.9, 0.9, 0.3));// 设置曲线图与xy轴的距离
		// 曲线颜色
		plot.getRenderer().setSeriesPaint(0, new Color(95, 165, 95));
		plot.getRenderer().setStroke(new BasicStroke(1.5f));
		BarRenderer3D renderer = (BarRenderer3D) plot.getRenderer();
		// 设置bar的最小宽度，以保证能显示数值
		renderer.setMinimumBarLength(0.01);
		// 最大宽度
		renderer.setMaximumBarWidth(0.07);
		
		// 设置内边框
		// plot.setOutlineVisible(true);
		plot.setOutlineStroke(new BasicStroke(1.0f));
		plot.setOutlinePaint(new Color(160, 128, 64));
		// 设置外边框
		basechart.setBorderVisible(true);
		CategoryAxis axis = (CategoryAxis) plot.getDomainAxis();
		axis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 3.0));
		
		axis.setMaximumCategoryLabelWidthRatio(axis.getMaximumCategoryLabelWidthRatio() + 1f);
	    
		NumberAxis valueaxis = (NumberAxis) plot.getRangeAxis();
		
		// if (min == 0)
		// {
		// valueaxis.setRange(0, max);
		// } else
		// {
		// valueaxis.setRange(min, max);
		// }
		// valueaxis.setAutoRange(false);
		// 设置轴线粗细
		valueaxis.setAxisLineStroke(new BasicStroke(1.0f));
		valueaxis.setAxisLinePaint(Color.BLACK);
		// 设置刻度
		// if (max == 1 || step == 10)
		// {
		// valueaxis.setAutoTickUnitSelection(false);
		// NumberTickUnit nt = new NumberTickUnit(step);
		// valueaxis.setTickUnit(nt);
		// }
		// 值中文文
		valueaxis.setLabelFont(new Font("黑体", Font.PLAIN, 12));
		ChartRenderingInfo info = new ChartRenderingInfo();
		BufferedImage buim = basechart.createBufferedImage(pngwidth, pngheight, BufferedImage.SCALE_FAST, info);
		
//		OutputStream output = null;
//		try {
//			output = new FileOutputStream("d:\\test.gif");
//			ImageIO.write(buim, "GIF", output);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally {
//			if (output != null)
//				try {
//					output.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//		}
		
		if (isstream)
		{
			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			ImageOutputStream imOut = null;
			try
			{
				imOut = ImageIO.createImageOutputStream(bs);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try
			{
				ImageIO.write(buim, "GIF", imOut);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // scaledImage1为BufferedImage，jpg为图像的类型
			InputStream istream = new ByteArrayInputStream(bs.toByteArray());
			list = new ArrayList<InputStream>();
			list.add(istream);
		} else
		{
			String n = String.valueOf(System.currentTimeMillis());
			String strImagePath = Constand.topnreportsavepath + this.strReportName + ".html_files\\" + n + ".gif";
			new com.siteview.ecc.simplereport.CreateImage().create(buim, strImagePath);
			list = new ArrayList<String>();
			list.add("./" + this.strReportName + ".html_files/" + n + ".gif");
		}
		return list;
	}
	
	public void createReport() throws IOException
	{
		String type = this.fileType;
		doReport(type);
	}
	
	public void doReport(String type) throws IOException
	{
		
		FileInputStream fis = null;
		
		try
		{
			// 获取数据
			readData();
			if(this.finish)
			{
				return;
			}
			String subtitle = "时段：" + Toolkit.getToolkit().formatDate(tmStart) + "~" + Toolkit.getToolkit().formatDate(tmEnd);

			TopNReportDatasource ds2 = null;
			TopNReportDatasource ds1 = null;
			List<String> nulllist = new ArrayList<String>();
			nulllist.add("d");
			if ("html".equals(type))
			{
				ds1 = new TopNReportDatasource(buildCategoryBufferImage(ttitle, "", ytitle, 500, 500, false));
			} else
			{
				ds1 = new TopNReportDatasource(buildCategoryBufferImage(ttitle, "", ytitle, 500, 500, true));
			}
			
			ds2 = new TopNReportDatasource(buildBean("StatisticsBean"));
			
			Map parameters = new HashMap();
			parameters.put("ReportTitle", reportTitle);
			parameters.put("subReportTitle", subtitle);
			String path = EccWebAppInit.getWebDir();
			parameters.put("SUBREPORT_DIR", path + "/main/report/topnreport/");
			parameters.put("subDS1", ds1);
			parameters.put("subDS2", ds2);
			/*
			 * if (type.equals("xls")) { parameters.put("IS_IGNORE_PAGINATION", true); }
			 */
			if ("html".equals(type))
			{
				parameters.put("IS_IGNORE_PAGINATION", false);
				JasperPrint jasperPrint = JasperFillManager.fillReport(path + "/main/report/topnreport/topnreport.jasper", parameters, new TopNReportDatasource(nulllist));
				JRHtmlExporter exporter = new JRHtmlExporter();
				File htmlfile = new File(Constand.topnreportsavepath, strReportName + ".html");
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, htmlfile.toString());// 要把报表输出到的文件
				exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
//				exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "<div style=\"page-break-after:always\"></div>");
//				exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);// 输出引擎是否用小图片矫正
				// exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,Constand.statreportsavepath+ strReportName+ ".html_files?image=");
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				// exporter.setParameter(JRExporterParameter.OUTPUT_WRITER,
				// response.getOutputStream());
				exporter.exportReport();
				
				// 
				/*
				 * JasperRunManager.runReportToHtmlFile(path + "/main/report/topnreport/topnreport.jasper", Constand.statreportsavepath + strReportName + ".html", parameters, ds2);
				 */
			} else if ("pdf".equals(type))
			{
				JasperRunManager.runReportToPdfFile(path + "/main/report/topnreport/topnreport0.jasper", Constand.topnreportsavepath + strReportName + ".pdf", parameters,
						new TopNReportDatasource(nulllist));
			} else if ("xls".equals(type))
			{
				com.siteview.ecc.report.xls.JRXlsExporter exporter = new com.siteview.ecc.report.xls.JRXlsExporter();
				ByteArrayOutputStream oStream = new ByteArrayOutputStream();
				JasperPrint jasperPrint = JasperFillManager.fillReport(path + "/main/report/topnreport/topnreport0.jasper", parameters, new TopNReportDatasource(nulllist));
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
				exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
				exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				exporter.exportReport();
				byte[] bytes = oStream.toByteArray();
				OutputStream out = new FileOutputStream(Constand.topnreportsavepath + strReportName + ".xls");
				try
				{
					out.write(bytes);
				} finally
				{
					out.close();
				}
			} else
			{
				
			}
			
			writeGenIni(this.reportid, strReportName, this.fileType);
			this.finishGerateFile=true;
//			if (!auto)
//			{
//				String filename = TopNLogListmodel.getfilename(strReportName, fileType);
//				if (filename.equals(""))
//				{
//					return;
//				}
//				// AMedia(String name, String format, String ctype, URL url, String charset)
//				
//				if (fileType.equals("html"))
//				{
//					String webpath = Constand.topnreportwebpath + strReportName + ".html";
//					Executions.getCurrent().sendRedirect(webpath, "_blank");
//				} else if (fileType.equals("pdf"))
//				{
//					fis = new FileInputStream(filename);
//					Filedownload.save(new AMedia(strReportName + ".pdf", "pdf", "application/pdf", fis));
//				} else if (fileType.equals("xls"))
//				{
//					fis = new FileInputStream(filename);
//					Filedownload.save(new AMedia(strReportName + ".xls", "xls", "application/vnd.ms-excel", fis));
//				}
//			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
		} finally
		{
			try
			{
				if (fis != null)
					fis.close();
			} catch (Exception e)
			{
			}
		}
	}
	
	private void writeGenIni(String reportID, String reportFileID, String fileType)
	{
		IniFile iniGen = new IniFile("reportTopN." + reportID + ".ini");
		try
		{
			iniGen.load();
		} catch (Exception e1)
		{
		}
		try
		{
			iniGen.createSection(reportFileID);
			String loginname=null;
			try
			{
			loginname= view.getLoginName();
			}catch(Exception ex)
			{}
			if (loginname==null)
			{
				loginname="admin";
			}
			iniGen.setKeyValue(reportFileID, "creator", loginname);
			iniGen.setKeyValue(reportFileID, "fileType", fileType);
			iniGen.saveChange();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//
	public Map<String, Map<String, String>> QueryTopnMonitorInfo() throws Exception
	{
		Map<String, Map<String, String>> m_fmap = new HashMap<String, Map<String, String>>();
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "QueryInfo");
		ndata.put("needkey", "sv_monitortype,sv_name");
		ndata.put("needtype", "monitor");
		
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception("Failed to load :" + ret.getEstr());
		
		// 统计所有已用的监测器类型 及 选监测器类型 + 选监测器指标的简单演示 ...
		// 原c#这部分是测试代码, 暂时无用。
		
		return ret.getFmap();
	}
	
	private String findById(String id)
	{
		MonitorTemplate temple = TemplateManager.getMonitorTemplate(id);
		if (temple == null)
			return null;
		return temple.get_sv_label();
	}


	@Override
	public void run()
	{
		// TODO Auto-generated method stub
		try
		{
			if(!auto)
			{
				createReport();
			}
		}
		catch(Exception e)
		{}
		
	}

	
}
