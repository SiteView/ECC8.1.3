package com.siteview.base.cache;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javolution.util.FastList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.siteview.base.cache.bean.ReportData;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.svdb.UnivData;

public class ReportCacheThread extends Thread {
	private static final Log log = LogFactory.getLog(ReportCacheThread.class);
	private static final SimpleDateFormat SDF = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss"); 
	private static Date last = null;
	private View view = null;

	private static final Map<String,List<Map<String, String>>> reportCache = new EhCacheMap<String,List<Map<String, String>>>("ReportCacheThread.reportCache");
	
	public ReportCacheThread() throws ParseException{
		last = SDF.parse("1970-01-01 00:00:00");
	}
	
	public static List<Map<String, String>> getReport(String id,Date begin,Date end){
		return null;
	}
	
	public void run(){
		while(true){
			String session = null;
			try {
				session = Manager.forceLoginAsAdmin();// 以 admin 身份登录
				view = Manager.getView(session);
				if (view == null)
					return;

				log.info("开始后台缓存服务...,Now=" + new Date() );
				Thread thread = new CacheThread(view);
				thread.start();
				while(thread.isAlive()){
					// 保持 admin 用户处于激活状态
					view.setVisit();
					Thread.sleep(10000);
				}
				log.info("后台缓存服务结束，下次运行时间为5分钟后,Now=" + new Date() );
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				Manager.invalidateView(session); // 登出 admin 用户
			}
			try {
				sleep(5*60*1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	static class CacheThread extends Thread{
		private View view = null;
		public CacheThread(View view){
			this.view = view;
		}
		public void run(){
			Date now = new Date();
			while(last.after(now) == false){
				Calendar end = Calendar.getInstance();
				//end.setTime(last);
				//end.add(Calendar.DATE, 1);
				List<String> nodeids = new ArrayList<String>();
				for( INode node : view.getSe()){
					nodeids.addAll(BaseTools.getAllMonitors(view, node.getSvId()));
				}
				//List<Thread> threadpool = new ArrayList<Thread>();
				for(String id : nodeids){
					try {
						List<Map<String, String>> list = queryReportData(id,last,end.getTime());
						/*
						List<Map<String, String>> cachelist = reportCache.get(id);
						if (cachelist == null){
							cachelist = new ArrayList<Map<String, String>>();
						}
						cachelist.addAll(list);
						reportCache.put(id, cachelist);
						System.out.println("cacthed=" + id);
						*/
						
						List<ReportData> datalist = convert(id,list);
						
						for(ReportData report : datalist){
							if (report == null) continue;
							if (report.getId() == null) continue;
							if (report.getCreateTime() == null) continue;
							ReportData data = DaoFactory.getReportDataDao().getReportData(report.getId(), report.getCreateTime());
							if (data == null){
								DaoFactory.getReportDataDao().insert(report);
							}else{
								DaoFactory.getReportDataDao().update(report);
							}
						}
						
						//DaoFactory.getReportDataDao().update(datalist);
						System.out.println("cacthed=" + id);
						/*
						boolean isContinue = false;
						while(isContinue){
							isContinue = false;
							for (Thread th : threadpool){
								if (th.isAlive() == false) {
									threadpool.remove(th);
									isContinue = true;
								}
							}
							if (isContinue) {
								continue;
							}
							if (threadpool.size()>10){
								isContinue = true;
								Thread.sleep(100);
								continue;
							}
						}
						Thread thread = new GetDataFromSvdb(id,last,end.getTime());
						thread.start();
						threadpool.add(thread);
						*/
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				last = end.getTime();
			}
		}
	}
	
	static class GetDataFromSvdb extends Thread{
		String id = null;
		Date begin = null;
		Date end = null;
		public GetDataFromSvdb(String id,Date begin,Date end){
			this.id = id ;
			this.begin = begin;
			this.end = end;
		}
		public void run(){
			try {
	
				List<Map<String, String>> list = queryReportData(id,begin,end);
				/*
				List<Map<String, String>> cachelist = reportCache.get(id);
				if (cachelist == null){
					cachelist = new ArrayList<Map<String, String>>();
				}
				cachelist.addAll(list);
				reportCache.put(id, cachelist);
				*/
				
				List<ReportData> reports = convert(id,list);
				for(ReportData report : reports){
					if (report == null) continue;
					if (report.getId() == null) continue;
					if (report.getCreateTime() == null) continue;
					ReportData data = DaoFactory.getReportDataDao().getReportData(report.getId(), report.getCreateTime());
					if (data == null){
						DaoFactory.getReportDataDao().insert(report);
					}else{
						DaoFactory.getReportDataDao().update(report);
					}
				}
				
				System.out.println("cacthed=" + id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static List<ReportData> convert(String monitorid,List<Map<String, String>> list) throws ParseException{
		List<ReportData> retlist = new FastList<ReportData>();
		for (Map<String,String>  map : list){
			ReportData data = new ReportData();
			data.setId(monitorid);
			data.setCreateTime(SDF.parse(map.get("creat_time")));
			data.setValue(map);
			retlist.add(data);
		}
		return retlist;
	}
	
	private static List<Map<String, String>> queryReportData(String monitorid,Date begin,Date end) throws Exception
	{
		return UnivData.queryRecordsByTime(monitorid, begin, end);
	}
	
	

}
