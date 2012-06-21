package com.siteview.base.data;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;

public class ReportManager
{
	private final static Logger logger = Logger.getLogger(ReportManager.class);
	private final static Map<String,Report>	m_simple_report	= new ConcurrentHashMap<String,Report>();
	private final static Map<String,Date>	m_control		= new ConcurrentHashMap<String,Date>();
	
	/**
	 * 获取某个简单报告，即最新40条监测数据的报告
	 */
	public static Report getSimpleReport(MonitorInfo info) throws Exception
	{
		m_control.put("dateOfVisit", new Date());
		Report r = null;
		if (m_simple_report.containsKey(info.getSvId()))
		{
			r = m_simple_report.get(info.getSvId());
			if( r!=null && !r.isExpired(info) )
				return r;
		}
		r = new Report(info);
		r.load();
		m_simple_report.put(info.getSvId(), r);
		return r;
	}
	
	/**
	 * 这个方法，目前弃用。原本的目的：像 SimpleReport 一样 cache 一下，提高二次访问时的速度。
	 * 但统计报告的查询时间基本不会相同，即 cache 命中率基本为 0 。可以在 UI 层cache 。
	 * <br/>获取报告
	 * @param beginDate 开始时间，比如为昨天
	 * @param endDate 终止时间，比如为今天
	 */
	public static Report getReport(INode node, Date beginDate, Date endDate) throws Exception
	{
		Report r = new Report(node, beginDate, endDate);
		r.load();
		return r;
	}
	
	static
	{

		Timer timer = new Timer(true);
		timer.schedule(new DeleteCache(), 3600 * 1000, 1200 * 1000);
		m_control.put("dateOfVisit", new Date());
	}
	
	static public class DeleteCache extends java.util.TimerTask
	{
		public void run()
		{
			try
			{
				Date vd= m_control.get("dateOfVisit");
				Date nowd= new Date();
				Long t= nowd.getTime()- vd.getTime();
				if( t > 3600 * 1000 )
					m_simple_report.clear();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args)
	{
		View w=null;
		try
		{
			String session = Manager.createView("admin", "siteview");
			w= Manager.getView(session);
			//w.getChangeTree();
		} catch (Exception e)
		{
			e.printStackTrace();
		}	
		if(w==null)
			return;
		INode n= w.getNode("1.64.10.6");
		if(n==null)
			return;
		MonitorInfo info= w.getMonitorInfo(n);
		if(info==null)
			return;
		
		try
		{
			Date begin= new Date(2009-1900, 2-1 ,10, 8, 42, 00);
			Date end= new Date(2009-1900, 2-1 ,26, 15, 04, 50);
			logger.info("   @@@@@@@      begin:   "+begin.toLocaleString());
			logger.info("   @@@@@@@        end:   "+end.toLocaleString());
//			Report r= getReport(n, begin,end);
			
			Report r= getSimpleReport(info);

			Map<Date, Report.DstrItem> dstr = r.getDstr();
			r.display();
			for (Date d : dstr.keySet())
			{
				Report.DstrItem item = dstr.get(d);
				logger.info(d.toLocaleString()+"  status:"+ item.status+"  value:"+ item.value);
			}
			logger.info("--------------------------");
			Map<Date, String> detail = r.getReturnValueDetail(0);
			for (Date d : detail.keySet())
				logger.info(d.toLocaleString()+"  value:"+ detail.get(d));		
			
			for (int i = 0; i < 3; i++)
			{
				try
				{
					Thread.sleep(2000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
//				r = getSimpleReport(info);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}

	}
}
