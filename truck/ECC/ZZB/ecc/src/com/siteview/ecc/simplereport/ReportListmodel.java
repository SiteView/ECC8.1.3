package com.siteview.ecc.simplereport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.siteview.base.data.Report;
import com.siteview.base.data.Report.DstrItem;
import com.siteview.ecc.util.Toolkit;

public class ReportListmodel extends ListModelList implements ListitemRenderer
{
	/**
	 * 
	 */
	private Report			simpleReport;
	private String			monitorName;
	
	
	public ReportListmodel(String tag, Report r)
	{
		super();
		this.simpleReport = r;
		com.siteview.base.manage.View w = null;
		
		monitorName = simpleReport.getPropertyValue("MonitorName");
		buildBean(tag);
		
	}
	
	private void buildBean(String tag)
	{
		if (tag.equals("MonitorBean"))
		{
			String okPercent = simpleReport.getPropertyValue("okPercent");
			String warnPercent = simpleReport.getPropertyValue("warnPercent");
			String errorPercent = simpleReport.getPropertyValue("errorPercent");
			Map<Date, DstrItem> dstrs =simpleReport.getDstr();
			
			
			float dis=simpleReport.getDisablePercentOfSimpleReport();
			String disablePercent =Float.toString(dis) ;
			String errorCondition = simpleReport.getPropertyValue("errorCondition");
			List list = new ArrayList();
			list.add(new com.siteview.ecc.simplereport.MonitorBean(monitorName, okPercent, warnPercent, errorPercent, disablePercent, errorCondition));
			clear();
			addAll(list);
		}
		if (tag.equals("StatisticsBean"))
		{
			clear();
			List list = new ArrayList();
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
			addAll(list);
		}
		if (tag.equals("StatisticsBean1"))
		{
			clear();
			List list = new ArrayList();
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
				list.add(new com.siteview.ecc.simplereport.StatisticsBean( returnName, max, average, latest));
			}
			addAll(list);
		}
		if (tag.equals("HistoryBean"))
		{
			clear();
			List list = new ArrayList();
			Map<Date, DstrItem> dstrs = simpleReport.getDstr();
			for (Date D : dstrs.keySet())
			{
				if (dstrs.get(D).status.equals("ok"))
				{
					list.add(new com.siteview.ecc.simplereport.HistoryBean(monitorName,Toolkit.getToolkit().formatDate(D), dstrs.get(D).value,dstrs.get(D).status));
				}
				
			}
			addAll(list);
		}
		if (tag.equals("HistoryBeandisable"))
		{
			clear();
			List list = new ArrayList();
			Map<Date, DstrItem> dstrs = simpleReport.getDstr();
			for (Date D : dstrs.keySet())
			{
				if (dstrs.get(D).status.equals("disable"))
				{
					list.add(new com.siteview.ecc.simplereport.HistoryBean(monitorName,Toolkit.getToolkit().formatDate(D), dstrs.get(D).value,dstrs.get(D).status));
				}
				
			}
			addAll(list);
		}
		
		if (tag.equals("HistoryBeanerror"))
		{
			clear();
			List list = new ArrayList();
			Map<Date, DstrItem> dstrs = simpleReport.getDstr();
			for (Date D : dstrs.keySet())
			{
				if (dstrs.get(D).status.equals("error"))
				{
					list.add(new com.siteview.ecc.simplereport.HistoryBean(monitorName,Toolkit.getToolkit().formatDate(D), dstrs.get(D).value,dstrs.get(D).status));
				}
				
			}
			addAll(list);
		}
		
		if (tag.equals("HistoryBeandanger"))
		{
			clear();
			List list = new ArrayList();
			Map<Date, DstrItem> dstrs = simpleReport.getDstr();
			for (Date D : dstrs.keySet())
			{
				if (dstrs.get(D).status.equals("warning"))
				{
					list.add(new com.siteview.ecc.simplereport.HistoryBean(monitorName,Toolkit.getToolkit().formatDate(D), dstrs.get(D).value,dstrs.get(D).status));
				}
				
			}
			addAll(list);
		}
		
		
	}
	
	@Override
	public void render(Listitem arg0, Object arg1) throws Exception
	{
		// TODO Auto-generated method stub
		Listitem item = arg0;
		if (arg1 instanceof MonitorBean)
		{
			MonitorBean m = (MonitorBean) arg1;
			new Listcell(m.getName()).setParent(item);
			new Listcell(m.getNomal()).setParent(item);
			new Listcell(m.getDanger()).setParent(item);
			new Listcell(m.getError()).setParent(item);
			new Listcell(m.getDisable()).setParent(item);
			new Listcell(m.getErrorvalue()).setParent(item);
		}
		if (arg1 instanceof StatisticsBean)
		{
			StatisticsBean s = (StatisticsBean) arg1;
			if(s.getName()!=null)
			{
			new Listcell(s.getName()).setParent(item);
			}
			new Listcell(s.getReturnvalue()).setParent(item);
			new Listcell(s.getMaxvalue()).setParent(item);
			new Listcell(s.getAveragevalue()).setParent(item);
			new Listcell(s.getLastestvalue()).setParent(item);
		}
		if (arg1 instanceof HistoryBean)
		{
			HistoryBean h = (HistoryBean) arg1;
			new Listcell(h.getDate()).setParent(item);
			new Listcell(h.getName()).setParent(item);
			new Listcell(h.getDestr()).setParent(item);
			
		}
	}
	
}
