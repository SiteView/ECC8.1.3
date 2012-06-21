package com.siteview.ecc.monitorbrower;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.siteview.base.data.QueryInfo;
import com.siteview.base.data.ReportDate;
import com.siteview.base.data.Report.DstrItem;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;
import com.siteview.ecc.alert.util.LocalIniFile;
import com.siteview.ecc.start.EccStarter;
import com.siteview.ecc.start.StarterListener;

public class MonitorInfoStatistics implements StarterListener {
	private final static Logger logger = Logger.getLogger(MonitorInfoStatistics.class);

	private View view;
	private static LocalIniFile iniFile = new LocalIniFile(
			"MonitorBrowseData.ini");
	static {
		try {
			iniFile.load();
		} catch (Exception e) {
		}
	}

	@Override
	public void destroyed(EccStarter starter) {
	}

	@Override
	public void startInit(EccStarter starter) {
		logger.info("statisticalMonitorInfo start....");
		if(Boolean.parseBoolean(starter.getInitParameter("statisticalMonitorInfo"))){
			Thread statisticalThread =new MonthStatisticalThread();
			statisticalThread.setPriority(Thread.MIN_PRIORITY);
			statisticalThread.setDaemon(true);
			statisticalThread.start();
			
			Thread hourStatisticalThread =new HourStatisticalThread();
			hourStatisticalThread.setPriority(Thread.MIN_PRIORITY);
			hourStatisticalThread.setDaemon(true);
			hourStatisticalThread.start();
		}
		logger.info("statisticalMonitorInfo end....");
	}

	// 保持 admin 用户处于激活状态
	static class KeepAlive extends java.util.TimerTask {
		private Timer timer = null;
		boolean working = true;
		View view;

		public KeepAlive(Timer t, View v) {
			timer = t;
			view = v;
		}

		public void run() {
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
			while (working) {
				try {
					view.setVisit(); // 保持 admin 用户处于激活状态
					Thread.sleep(10 * 1000);
				} catch (InterruptedException e) {
				}
			}
		}

		public void logOut() {
			working = false;
			super.cancel();
			timer.cancel();
		}
	}

	private String getAllMonitorId() throws Exception{
		StringBuffer sb = new StringBuffer();
		QueryInfo qi = new QueryInfo();
		qi.needkey="sv_monitortype";
		qi.setNeedType_monitor();
		Map<String, Map<String, String>> td = qi.load();
		for(String key:td.keySet()){
			sb.append(key).append(",");
		}
		return sb.toString();
	}
	public void MonthStart() {
		String session = null;
		KeepAlive kalive = null;
		try {
			session = Manager.forceLoginAsAdmin();// 以 admin 身份登录
			view = Manager.getView(session);
			if (view == null)
				return;
			Timer timer = new Timer(true);
			kalive = new KeepAlive(timer, view);
			timer.schedule(kalive, 0);// 保持 admin 用户的激活状态
			int i = 0;
			Date currentTime = new Date();
			Date now = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(now);
			c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
			ReportDate report = new ReportDate(c.getTime(),now);
			report.getReportDate(this.getAllMonitorId());
			String[] nodes = report.getNodeidsArray();
			iniFile.setKeyValue("latestTime", "time", currentTime.toLocaleString());
			for(String tdKey :nodes){
				Map<Date, DstrItem> dstr = report.getDstr(tdKey);
				int ok=0,warning=0,error=0,disable=0,nullCount=0,bad=0;
				Date okTime=null,warningTime=null,errorTime=null,disableTime=null,nullTime=null,badTime=null;
				for (Date dstrKey : dstr.keySet()) {
					DstrItem dstrValue = dstr.get(dstrKey);
					if(dstrValue==null) continue;
					if(dstrValue.status.equals("ok")){
						if(okTime==null) okTime=dstrKey;
						ok++;
					}else if(dstrValue.status.equals("warning")){
						if(warningTime==null) warningTime=dstrKey;
						warning++;
					}else if(dstrValue.status.equals("error")){
						if(errorTime==null) errorTime=dstrKey;
						error++;
					}else if(dstrValue.status.equals("disable")){
						if(disableTime==null) disableTime=dstrKey;
						disable++;
					}else if(dstrValue.status.equals("null")){
						if(nullTime==null) nullTime=dstrKey;
						nullCount++;
					}else if(dstrValue.status.equals("bad")){
						if(badTime==null) badTime=dstrKey;
						bad++;
					}
					
				}
				if(ok>0 && okTime!=null){
					iniFile.setKeyValue(tdKey, "okCount", ok+"");
					iniFile.setKeyValue(tdKey, "okTime", okTime.toLocaleString());
				}
				if(warning>0 && warningTime!=null){
					iniFile.setKeyValue(tdKey, "warningCount", warning+"");
					iniFile.setKeyValue(tdKey, "warningTime", 	warningTime.toLocaleString());
				}
				if(error>0 && errorTime!=null){
					iniFile.setKeyValue(tdKey, "errorCount", error+"");
					iniFile.setKeyValue(tdKey, "errorTime", errorTime.toLocaleString());
				}
				if(disable>0 && disableTime!=null){
					iniFile.setKeyValue(tdKey, "disableCount", disable+"");
					iniFile.setKeyValue(tdKey, "disableTime", disableTime.toLocaleString());
				}
				if(nullCount>0 && nullTime!=null){
					iniFile.setKeyValue(tdKey, "nullCount", nullCount+"");
					iniFile.setKeyValue(tdKey, "nullTime", nullTime.toLocaleString());
				}
				if(bad>0 && badTime!=null){
					iniFile.setKeyValue(tdKey, "badCount", bad+"");
					iniFile.setKeyValue(tdKey, "badTime", badTime.toLocaleString());
				}
			}
			iniFile.saveChange();
			kalive.logOut(); // 不再 保持 admin 用户的激活状态
			Manager.invalidateView(session); // 登出 admin 用户
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void HourStart() {
		String session = null;
		KeepAlive kalive = null;
		try {
			session = Manager.forceLoginAsAdmin();// 以 admin 身份登录
			view = Manager.getView(session);
			if (view == null)
				return;
			Timer timer = new Timer(true);
			kalive = new KeepAlive(timer, view);
			timer.schedule(kalive, 0);// 保持 admin 用户的激活状态
			int i = 0;
			Date currentTime = new Date();
			Date now = new Date();
			Calendar c = Calendar.getInstance();
			c.setTime(now);
			c.set(Calendar.HOUR_OF_DAY, c.get(Calendar.HOUR_OF_DAY) - 1);
			logger.info("**********************"+c.getTime());
			ReportDate report = new ReportDate(c.getTime(),now);
			report.getReportDate(this.getAllMonitorId());
			String[] nodes = report.getNodeidsArray();
			iniFile.setKeyValue("latestTime", "time", currentTime.toLocaleString());
			for(String tdKey :nodes){
				Map<Date, DstrItem> dstr = report.getDstr(tdKey);
				int ok=0,warning=0,error=0,disable=0,nullCount=0,bad=0;
				Date okTime=null,warningTime=null,errorTime=null,disableTime=null,nullTime=null,badTime=null;
				for (Date dstrKey : dstr.keySet()) {
					DstrItem dstrValue = dstr.get(dstrKey);
					if(dstrValue==null) continue;
					if(dstrValue.status.equals("ok")){
						if(okTime==null) okTime=dstrKey;
						ok++;
					}else if(dstrValue.status.equals("warning")){
						if(warningTime==null) warningTime=dstrKey;
						warning++;
					}else if(dstrValue.status.equals("error")){
						if(errorTime==null) errorTime=dstrKey;
						error++;
					}else if(dstrValue.status.equals("disable")){
						if(disableTime==null) disableTime=dstrKey;
						disable++;
					}else if(dstrValue.status.equals("null")){
						if(nullTime==null) nullTime=dstrKey;
						nullCount++;
					}else if(dstrValue.status.equals("bad")){
						if(badTime==null) badTime=dstrKey;
						bad++;
					}
					
				}
				String okString = iniFile.getValue(tdKey, "okCount")==null?"0":iniFile.getValue(tdKey, "okCount");
				if(ok>0 && okTime!=null && okString!=null){
					iniFile.setKeyValue(tdKey, "okCount", (ok+Integer.parseInt(okString))+"");
					iniFile.setKeyValue(tdKey, "okTime", okTime.toLocaleString());
				}
				String warningString = iniFile.getValue(tdKey, "warningCount")==null?"0":iniFile.getValue(tdKey, "warningCount");
				if(warning>0 && warningTime!=null && warningString!=null){
					iniFile.setKeyValue(tdKey, "warningCount", (warning+Integer.parseInt(warningString))+"");
					iniFile.setKeyValue(tdKey, "warningTime", 	warningTime.toLocaleString());
				}
				String errorString = iniFile.getValue(tdKey, "errorCount")==null?"0":iniFile.getValue(tdKey, "errorCount");
				if(error>0 && errorTime!=null&&errorString!=null){
					iniFile.setKeyValue(tdKey, "errorCount", (error+Integer.parseInt(errorString))+"");
					iniFile.setKeyValue(tdKey, "errorTime", errorTime.toLocaleString());
				}
				String disableString = iniFile.getValue(tdKey, "disableCount")==null?"0":iniFile.getValue(tdKey, "disableCount");
				if(disable>0 && disableTime!=null&&disableString!=null){
					iniFile.setKeyValue(tdKey, "disableCount", (disable+Integer.parseInt(disableString))+"");
					iniFile.setKeyValue(tdKey, "disableTime", disableTime.toLocaleString());
				}
				String nullString = iniFile.getValue(tdKey, "nullCount")==null?"0":iniFile.getValue(tdKey, "nullCount");
				if(nullCount>0 && nullTime!=null&&nullString!=null){
					iniFile.setKeyValue(tdKey, "nullCount", (nullCount+Integer.parseInt(nullString))+"");
					iniFile.setKeyValue(tdKey, "nullTime", nullTime.toLocaleString());
				}
				String badString = iniFile.getValue(tdKey, "badCount")==null?"0":iniFile.getValue(tdKey, "badCount");
				if(bad>0 && badTime!=null&&badString!=null){
					iniFile.setKeyValue(tdKey, "badCount", (bad+Integer.parseInt(badString))+"");
					iniFile.setKeyValue(tdKey, "badTime", badTime.toLocaleString());
				}
			}
			kalive.logOut(); // 不再 保持 admin 用户的激活状态
			Manager.invalidateView(session); // 登出 admin 用户
			iniFile.saveChange();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	class MonthStatisticalThread extends Thread{
		private final String module = MonthStatisticalThread.class.getName();
		public MonthStatisticalThread(){
			this.setName("Queue 24 hours proccess thread");
		}
		public void run(){
			while(true){
				try {
					sleep(proccess());  //休息
				} catch (Exception e) {
					try {
						sleep(1000 * 10);
					} catch (InterruptedException e1) {
					}
				}
				
			}
		}
		private Timer timer = new Timer(true);
		//每隔24小时统计一次监测数据
		private long proccess() throws Exception{
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.HOUR_OF_DAY, 24);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			timer.schedule(new MonthTask(), cal.getTime());
			return (1000 * 60 * 60 * 24);
		}
	}
	class HourStatisticalThread extends Thread{
		private final String module = HourStatisticalThread.class.getName();
		public HourStatisticalThread(){
			this.setName("Queue 1 hours proccess thread");
		}
		public void run(){
			while(true){
				try {
					sleep(proccess());  //休息
				} catch (Exception e) {
					try {
						sleep(1000 * 10);
					} catch (InterruptedException e1) {
					}
				}
			}
		}
		private Timer timer = new Timer(true);
		//每隔1小时统计一次监测数据
		private long proccess() throws Exception{
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.HOUR_OF_DAY, 1);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			logger.info(cal.getTime().toLocaleString());
			timer.schedule(new HourTask(), cal.getTime());
			return (1000 *60 * 60 * 1);
		}
	}
	class MonthTask extends TimerTask{
		public final String module = MonthTask.class.getName();
		@Override
		public void run() {
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
			MonthStart();
		}
	}
	class HourTask extends TimerTask{
		public final String module = HourTask.class.getName();
		@Override
		public void run() {
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
			HourStart();
		}
	}
}
