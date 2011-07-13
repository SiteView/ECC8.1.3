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
	
	public String										strReportName;												// ����ʱ��ε����ɵı�������
																													
	public ArrayList<Report>							lstMonitorReport;											// �����û�ѡ��ļ�������ͺͼ������
	// ���˳���ȷ�Ĳ���Topn����������б�
	
	// private String strReportSort; // ��״ͼ������ʽ
	// private String strReportCount; // ��״ͼ������ȷ
	private String										title;
	private String										ttitle;													// ͼ�ı���
	private String										ytitle;													// ͼy����
	// ��������
	private String										reportid;
	private View										view;
	private String[]									lstIds;													// �û�ѡ��ļ����
	private ReportDate									rd;
	private Date										tmStart;
	private Date										tmEnd;
	private String										strCount;													// ��״ͼ������
	private String										strSelType;												// �û�ѡ��ļ��������
	private String										strMark;													// �û�ѡ��ļ��������ֵ����
	private String										strSort;													// ��״ͼ������ʽ
	private String										strget;
	private String										fileType;
	private String										reportTitle;
	private List<String>								listnid	= new ArrayList<String>();							// ǰn����id��
	private Boolean										auto	= true;
	// �ֶ����ɱ����Ƿ����
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
			trSort = "����";
		}
		this.strSort = trSort;
		this.reportTitle = this.reportTitle + "(" + trSort + ")";
		String trget = DefineMap.get("GetValue");
		if (trget == null || trget.equals(""))
		{
			trget = "ƽ��";
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
			
			// �����û�ѡ��ļ�������ͺͼ�����������˳����ڴ����͵ļ����id��
			Map<String, Map<String, String>> monitoidToType = QueryTopnMonitorInfo();
			String strTmp = "";
			for (int i = 0; i < lstIds.length; i++)
			{
				String mid = lstIds[i];
				if (!"".equals(mid) && monitoidToType.keySet().contains(mid))
				{
					try
					{// ����Ƿ�ͬ���ͼ����
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
					error_message = "�ڵ㲻���ڻ���Ȩ���ʣ�";
					continue;
				}
				if (!n.getType().equals(INode.MONITOR))
				{
					error_message = "�ڵ����ͷǷ���";
					continue;
				}
				MonitorInfo info = view.getMonitorInfo(n);
				if (info == null)
				{
					error_message = "�ڵ㲻���ڻ���Ȩ���ʣ�";
					continue;
				}
				tmpnodeids = lstIds[i] + "," + tmpnodeids;
			}
			lstIds = tmpnodeids.split(",");
			// ��������
			int pl = (lstIds.length / 20);
			int	vaule=100;
			if(pl>0)
		  	vaule=100/pl;
			else
				pl=1;
			
			for (int pn = 0; pn <pl; pn++)
			{
				// �ֶ�����
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
				// ����ѡ��ķ���ֵ������ʾ��
				if (!returnName.equals(strMark))
				{
					continue;
				}
				// ����ֵ��ƽ��ֵ��(Y��)
				if (strget.equals("ƽ��"))
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
			// ���������(X��)
			datacounts++;
			list.add(new ChartObjectData(dMaxorAverage, Integer.toString(datacounts), strName, strSort, sid));
		}
		// ��������
		Collections.sort(list, new ChartObjectData());
		this.list=list;
		/*
		 * ������ݽ����ݼ�
		 */
		Iterator iter = list.iterator();
		int i = 1;
		int count = Integer.parseInt(strCount);
		while (iter.hasNext())
		{
			
			if (i > count)
			{// ���������ʾ������
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
	 * ʵ�������õ�
	 * 
	 */
	class ChartObjectData implements Comparator
	{
		String	rowKey; // �б�ǩ
		String	colKey; // �б�ǩ
		double	value;	// ֵ
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
		 * �Ƚϵ��㷨
		 */
		public int compare(Object obj1, Object obj2)
		{
			
			double a = ((ChartObjectData) obj1).value;
			double b = ((ChartObjectData) obj2).value;
			String sort = ((ChartObjectData) obj2).sort;
			if (sort.equals("����"))
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
	
	// ����Topn����ͼƬ����
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
//				// ����ѡ��ķ���ֵ������ʾ��
//				if (!returnName.equals(strMark))
//				{
//					continue;
//				}
//				// ����ֵ��ƽ��ֵ��(Y��)
//				if (strget.equals("ƽ��"))
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
//			// ���������(X��)
//			datacounts++;
//			list.add(new ChartObjectData(dMaxorAverage, Integer.toString(datacounts), strName, strSort, sid));
//		}
//		// ��������
//		Collections.sort(list, new ChartObjectData());
		
		/*
		 * ������ݽ����ݼ�
		 */
		Iterator iter = this.list.iterator();
		int i = 1;
		int count = Integer.parseInt(strCount);
		listnid.clear();
		while (iter.hasNext())
		{
			
			if (i > count)
			{// ���������ʾ������
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
	 * �ֶ�����TOPN�����ͼƬ
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
		
		// ����Topn����ͼƬ����
		DefaultCategoryDataset dataset = buildDataset();
		// ������ + X���� + Y���� + ����
		JFreeChart chart = ChartFactory.createBarChart3D(title, xtitle, ytitle, dataset, PlotOrientation.VERTICAL, false, false, false);
		
		chart.setBackgroundPaint(new Color(0xE1E1E1));
		CategoryPlot plot = chart.getCategoryPlot();
		// ����Y����ʾ����
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		// ����������ʾ
		rangeAxis.setVerticalTickLabels(true);
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		CategoryAxis domainAxis = plot.getDomainAxis();
		// ���þ���ͼƬ��˾���
		domainAxis.setLowerMargin(0.05);
		BarRenderer3D renderer = new BarRenderer3D();
		// ����������ɫ
		renderer.setSeriesPaint(0, new Color(0xff00));
		plot.setRenderer(renderer);
		// �洢
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
					// ����ѡ��ķ���ֵ������ʾ��
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
		// ���ñ����ӱ���
		TextTitle texttitle = new TextTitle(title, new Font("����", Font.PLAIN, 12));
		texttitle.setPaint(new Color(30, 91, 153));
		basechart.setTitle(texttitle);
		TextTitle subtexttitle = new TextTitle(subtite, new Font("����", Font.PLAIN, 12));
		subtexttitle.setPaint(new Color(30, 91, 153));
		basechart.addSubtitle(subtexttitle);
		TextTitle subtexttitle1 = new TextTitle("", new Font("����", Font.PLAIN, 12));
		subtexttitle1.setPaint(new Color(30, 91, 153));
		basechart.addSubtitle(subtexttitle1);
		
		basechart.setBackgroundPaint(Color.WHITE);
		CategoryPlot plot = basechart.getCategoryPlot();
		plot.setForegroundAlpha(1.0f);// �������͸����
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinePaint(Color.BLACK);// �����������ɫ
		plot.setDomainGridlinePaint(Color.BLACK);// �����������ɫ
		plot.setDomainGridlinesVisible(true);// ��ʾ���������
		plot.setRangeGridlinesVisible(true);// ��ʾ���������
		plot.setAxisOffset(new RectangleInsets(0.3, 0.9, 0.9, 0.3));// ��������ͼ��xy��ľ���
		// ������ɫ
		plot.getRenderer().setSeriesPaint(0, new Color(95, 165, 95));
		plot.getRenderer().setStroke(new BasicStroke(1.5f));
		BarRenderer3D renderer = (BarRenderer3D) plot.getRenderer();
		// ����bar����С��ȣ��Ա�֤����ʾ��ֵ
		renderer.setMinimumBarLength(0.01);
		// �����
		renderer.setMaximumBarWidth(0.07);
		
		// �����ڱ߿�
		// plot.setOutlineVisible(true);
		plot.setOutlineStroke(new BasicStroke(1.0f));
		plot.setOutlinePaint(new Color(160, 128, 64));
		// ������߿�
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
		// �������ߴ�ϸ
		valueaxis.setAxisLineStroke(new BasicStroke(1.0f));
		valueaxis.setAxisLinePaint(Color.BLACK);
		// ���ÿ̶�
		// if (max == 1 || step == 10)
		// {
		// valueaxis.setAutoTickUnitSelection(false);
		// NumberTickUnit nt = new NumberTickUnit(step);
		// valueaxis.setTickUnit(nt);
		// }
		// ֵ������
		valueaxis.setLabelFont(new Font("����", Font.PLAIN, 12));
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
			} // scaledImage1ΪBufferedImage��jpgΪͼ�������
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
			// ��ȡ����
			readData();
			if(this.finish)
			{
				return;
			}
			String subtitle = "ʱ�Σ�" + Toolkit.getToolkit().formatDate(tmStart) + "~" + Toolkit.getToolkit().formatDate(tmEnd);

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
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, htmlfile.toString());// Ҫ�ѱ�����������ļ�
				exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING, "UTF-8");
//				exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "<div style=\"page-break-after:always\"></div>");
//				exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);// ��������Ƿ���СͼƬ����
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
		
		// ͳ���������õļ�������� �� ѡ��������� + ѡ�����ָ��ļ���ʾ ...
		// ԭc#�ⲿ���ǲ��Դ���, ��ʱ���á�
		
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
