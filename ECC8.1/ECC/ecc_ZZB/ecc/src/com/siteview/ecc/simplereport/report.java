package com.siteview.ecc.simplereport;

import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.jfree.data.xy.XYDataset;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Listbox;

import com.siteview.base.data.Report;
import com.siteview.base.data.ReportManager;
import com.siteview.base.data.Report.DstrItem;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svecc.zk.test.SVDBViewFactory;

public class report extends GenericForwardComposer
{
	
	Jasperreport	jreport;
	Window			wreport;
	Listbox			format;
	Iframe          report;
	
	//
	private String	monitorId;
	private String	sessionId;
	String			error_message;
	Report			simpleReport;
	String			monitorName;
	
	public report()
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
		monitorName = simpleReport.getPropertyValue("MonitorName");
		
	}
	
	private List buildBean(String tag)
	{
		
		List list = new ArrayList();
		if (tag.equals("MonitorBean"))
		{
			String okPercent = simpleReport.getPropertyValue("okPercent");
			String warnPercent = simpleReport.getPropertyValue("warnPercent");
			String errorPercent = simpleReport.getPropertyValue("errorPercent");
			Map<Date, DstrItem> dstrs = simpleReport.getDstr();
			
			float dis = simpleReport.getDisablePercentOfSimpleReport();
			String disablePercent = Float.toString(dis);
			String errorCondition = simpleReport.getPropertyValue("errorCondition");
			
			list.add(new com.siteview.ecc.simplereport.MonitorBean(monitorName, okPercent, warnPercent, errorPercent, disablePercent, errorCondition));
			
		}
		if (tag.equals("StatisticsBean"))
		{
			
			for (int i = 0; i < simpleReport.getReturnSize(); i++)
			{
				String drawmeasure = simpleReport.getReturnValue("sv_drawmeasure", i);
				drawmeasure = drawmeasure.isEmpty() ? "0" : drawmeasure;
				if (!drawmeasure.equals("1"))
				{
					continue;
				}
				String returnName = simpleReport.getReturnValue("ReturnName", i);
				String max = simpleReport.getReturnValue("max", i);
				String latest = simpleReport.getReturnValue("latest", i);
				String average = simpleReport.getReturnValue("average", i);
				list.add(new com.siteview.ecc.simplereport.StatisticsBean(monitorName, returnName, max, average, latest));
			}
			
		}
		if (tag.equals("HistoryBean"))
		{
			
			Map<Date, DstrItem> dstrs = simpleReport.getDstr();
			List errorlist = new ArrayList();
			List dangerlist = new ArrayList();
			List oklist = new ArrayList();
			List disablelist = new ArrayList();
			List elselist = new ArrayList();
			for (Date D : dstrs.keySet())
			{
				String state = dstrs.get(D).status;
				String dstr = dstrs.get(D).value;
				if (state.equals("ok"))
				{
					state = "正常";
					oklist.add(new com.siteview.ecc.simplereport.HistoryBean(monitorName, D.toLocaleString(), dstr, state));
				} else if (state.equals("error"))
				{
					state = "错误";
					errorlist.add(new com.siteview.ecc.simplereport.HistoryBean(monitorName, D.toLocaleString(), dstr, state));
				} else if (state.equals("disable"))
				{
					state = "禁止";
					disablelist.add(new com.siteview.ecc.simplereport.HistoryBean(monitorName, D.toLocaleString(), dstr, state));
				} else if (state.equals("warning"))
				{
					state = "危险";
					dangerlist.add(new com.siteview.ecc.simplereport.HistoryBean(monitorName, D.toLocaleString(), dstr, state));
				} else
				{
					elselist.add(new com.siteview.ecc.simplereport.HistoryBean(monitorName, D.toLocaleString(), dstr, state));
				}
				
				// list.add(new com.siteview.ecc.simplereport.HistoryBean(monitorName, D.toLocaleString(), dstr, state));
			}
			if (errorlist.size() > 0)
			{
				Iterator item = errorlist.iterator();
				while (item.hasNext())
				{
					list.add((HistoryBean) item.next());
				}
			}
			if (dangerlist.size() > 0)
			{
				Iterator item = dangerlist.iterator();
				while (item.hasNext())
				{
					list.add((HistoryBean) item.next());
				}
			}
			if (oklist.size() > 0)
			{
				Iterator item = oklist.iterator();
				while (item.hasNext())
				{
					list.add((HistoryBean) item.next());
				}
			}
			if (disablelist.size() > 0)
			{
				Iterator item = disablelist.iterator();
				while (item.hasNext())
				{
					list.add((HistoryBean) item.next());
				}
			}
			if (elselist.size() > 0)
			{
				Iterator item = elselist.iterator();
				while (item.hasNext())
				{
					list.add((HistoryBean) item.next());
				}
			}
			
		}
		
		if (tag.equals("ImageBean"))
		{
			list = buildimage();
			
		}
		if (tag.equals("ImageBeanpdf"))
		{
	
			list= buildstreamimage();
		}
		return list;
		
	}
	
	private List<String> buildimage()
	{
		if (error_message != null)
		{
			return null;
		}
		Map<Integer, Map<String, String>> listimage = SimpleReport.getImagelist(simpleReport);
		if (listimage.size() == 0)
		{
			return null;
		}
		
		List<String> list = new ArrayList<String>();
		
		for (int key : listimage.keySet())
		{
			String id = monitorId + sessionId + key;
			Map<Date, String> imgdata = this.simpleReport.getReturnValueDetail(key);
			Map<String, String> keyvalue = listimage.get(key);
			XYDataset data = SimpleReport.buildDataset(imgdata);
			BufferedImage temmap = null;
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
				temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data, 10, 100, maxd, 0, true, 563, 200);
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
					temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data, 20, maxvalue, maxd, minvalue, true, 563,
							200);
				} else
				{
					temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data, 20, maxvalue, maxd, 0, true, 563, 200);
				}
			}
			
			session.setAttribute(id, temmap);
			list.add("/ecc/main/report/createImage.jsp?id=" + id);
		}
		return list;
	}
	
	private List<InputStream> buildstreamimage()
	{
		if (error_message != null)
		{
			return null;
		}
		Map<Integer, Map<String, String>> listimage = SimpleReport.getImagelist(simpleReport);
		if (listimage.size() == 0)
		{
			return null;
		}
		
		List<InputStream> list = new ArrayList<InputStream>();
		
		for (int key : listimage.keySet())
		{
			String id = monitorId + sessionId + key;
			Map<Date, String> imgdata = this.simpleReport.getReturnValueDetail(key);
			Map<String, String> keyvalue = listimage.get(key);
			XYDataset data = SimpleReport.buildDataset(imgdata);
			BufferedImage temmap = null;
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
				temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data, 10, 100, maxd, 0, true, 563, 200);
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
					temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data, 20, maxvalue, maxd, minvalue, true, 563,
							200);
				} else
				{
					temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data, 20, maxvalue, maxd, 0, true, 563, 200);
				}
			}
			
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
				ImageIO.write(temmap, "GIF", imOut);
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // scaledImage1为BufferedImage，jpg为图像的类型
			InputStream istream = new ByteArrayInputStream(bs.toByteArray());
			list.add(istream);
		}
		return list;
	}
	
	
	public void onCreate$wreport()
	{
		buildReport("html");
		
	}
	
	public void onSelect$format()
	{
		
		buildReport((String) format.getSelectedItem().getValue());
	}
	
	private void buildReport(String type)
	{
		if (error_message != null && !error_message.isEmpty())
		{
			monitorName = error_message;
			Map parameters = new HashMap();
			parameters.put("ReportTitle", monitorName);
			String path = EccWebAppInit.getWebDir();
			parameters.put("SUBREPORT_DIR", path + "/main/report/");
			jreport.setSrc("/main/report/report.jasper");
			jreport.setParameters(parameters);
			jreport.setType(type);
			return;
		}
		Map<Date, DstrItem> dstrs = simpleReport.getDstr();
		if (dstrs.isEmpty())
			return;
		
		Iterator itm = dstrs.keySet().iterator();
		String mindate = ((Date) itm.next()).toLocaleString();
		String maxdate = simpleReport.getPropertyValue("latestCreateTime");
		String subtitle = "时段：" + mindate + "~" + maxdate;
		
		Reportdatasource ds = new Reportdatasource(buildBean("HistoryBean"));
		Reportdatasource subds1 = new Reportdatasource(buildBean("MonitorBean"));
		Reportdatasource subds2 =new Reportdatasource(buildBean("StatisticsBean"));
		Reportdatasource subds3 = null;
		if (type.equals("html"))
		{
			subds3= new Reportdatasource(buildBean("ImageBean"));
		}
		else
		{
			subds3= new Reportdatasource(buildBean("ImageBeanpdf"));
		}
		
		Map parameters = new HashMap();
		parameters.put("ReportTitle", monitorName);
		parameters.put("subReportTitle", subtitle);
		String path = EccWebAppInit.getWebDir();
		parameters.put("SUBREPORT_DIR", path + "/main/report/");
		if (type.equals("html"))
		{
			parameters.put("IS_IGNORE_PAGINATION", true);
			parameters.put("SUBREPORT_DIRfilename", path + "/main/report/report_subreport2.jasper");
		}
		else
		{
			parameters.put("SUBREPORT_DIRfilename", path + "/main/report/report_subreport3.jasper");
		}
		if (type.equals("xls"))
		{
			parameters.put("IS_IGNORE_PAGINATION", true);
		}
		parameters.put("subDS1", subds1);
		parameters.put("subDS2", subds2);
		parameters.put("subDS3", subds3);
		
		
		jreport.setSrc("/main/report/report.jasper");
		jreport.setParameters(parameters);
		jreport.setDatasource(ds);
		jreport.setType(type);
//		jreport.getOuterAttrs();
//		jreport.getContext();
//		jreport.getContent().getByteData();
//		frm.getContent().getByteData();
//		jreport.getId();
//
//		
////		try{
////			Writer out=new FileWriter("c:\\111.pdf");
////			jreport.redraw(out);
////			}catch(Exception e){}	
//			
//		try 
//		{
//			doReport(type);
//		}
//		catch (IOException e) 
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
