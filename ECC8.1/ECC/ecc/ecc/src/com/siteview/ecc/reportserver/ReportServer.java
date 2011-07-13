
package com.siteview.ecc.reportserver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.siteview.base.data.IniFile;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;
import com.siteview.ecc.start.EccStarter;
import com.siteview.ecc.start.StarterListener;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svdb.UnivData;

public class ReportServer implements StarterListener
{
	private static final long serialVersionUID = 6861669116407188316L;
	private static final SimpleDateFormat END_TIME_FORMAT = new SimpleDateFormat("H:mm");
	private static final int StatReport = 1;
	private static final int TopNReport = 2;
    private static final String SEND_MAIL_BODY = "《%1%》由 Ecc ReportServer 自动发送，报告在附件中。请勿回复！";
    private static final String SEND_MAIL_SUBJECT = "SiteView ECC 《%1%》 邮件";
	
	private View view = null;
	private MailInfBean mailInfo = null;
	private Calendar now = null;
	
	@Override
	public void startInit(EccStarter starter)
	{
		if(Boolean.parseBoolean(starter.getInitParameter("reportServerStart")))
			startReportServer();
	}
	
	@Override
	public void destroyed(EccStarter starter)
	{
	}
	
	
	public ReportServer()
	{
		now = Calendar.getInstance();
		now.set(Calendar.SECOND, 0);
	}
	private final static Thread scheduleThread = new ScheduleThread();
	private static void startReportServer()
	{
		scheduleThread.start();
	}
	
	private static class ScheduleThread extends Thread {
		public void run() {
			while(true){
				try {
					long sleepTime = getSleepTime();
					Calendar calNow = Calendar.getInstance();
					calNow.add(Calendar.MILLISECOND, (int) sleepTime);
					Toolkit.getToolkit().getLoger().info("下一次报告服务启动时间：" + calNow.getTime());
					// 整点执行一次任务，
					sleep(sleepTime);
					new TaskThread().start();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private static long getSleepTime(){
		Calendar calNow = Calendar.getInstance();
		Calendar nextCal = (Calendar) calNow.clone();
		nextCal.add(Calendar.HOUR, 1);
		nextCal.set(Calendar.MINUTE, 0);
		nextCal.set(Calendar.SECOND, 0);
		return nextCal.getTimeInMillis() - calNow.getTimeInMillis();
	}
	
	private static class TaskThread extends Thread
	{
		public void run()
		{
			ReportServer reportServer = new ReportServer();
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
			reportServer.start();
		}
	}
	
	public void start() {
		String session = null;
		try {
			session = Manager.forceLoginAsAdmin();// 以 admin 身份登录
			view = Manager.getView(session);
			if (view == null)
				return;

			Toolkit.getToolkit().getLoger().info("开始报告服务...,Now=" + now.getTime());
			Thread threads[]={
					new ReportThread(StatReport,this),
					new ReportThread(TopNReport,this)
			};
			for (Thread thread : threads){
				thread.start();
			}
			while(this.isAlive(threads)){
				// 保持 admin 用户处于激活状态
				view.setVisit();
				Thread.sleep(10000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			Manager.invalidateView(session); // 登出 admin 用户
		}

	}
	
	private boolean isAlive(Thread[] threads){
		for (Thread thread : threads){
			if (thread.isAlive()){
				return true;
			}
		}
		return false;
	}
	
	private static class ReportThread extends Thread{
		private int reporttype = 0;
		private ReportServer server = null;
		public ReportThread(int reporttype,ReportServer server){
			this.reporttype = reporttype;
			this.server = server;
		}
		public void run(){
			long l = System.currentTimeMillis();
			Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
			if (StatReport == this.reporttype){
				this.server.writeAllStatReportHTML();// 生成统计报告
				Toolkit.getToolkit().getLoger().info("统计报告生成完毕,花费时间:" + (System.currentTimeMillis() - l)	/ (60 * 1000) + "min");
			}else if (TopNReport == this.reporttype){
				this.server.writeAllTopNReportHTML();// 生成TOP_N报告
				Toolkit.getToolkit().getLoger().info("TopN报告生成完毕,花费时间:" + (System.currentTimeMillis() - l)	/ (60 * 1000) + "min");
			}
		}
	}
	
	
	private ReturnBean getReportCondition(Map<String, String> map,int reportType) {
		ReturnBean bean = null;
		if (reportType != StatReport && reportType != TopNReport){
			//统计类型不匹配
			return bean;
		}
		if (map == null)
			return bean;

		if ("yes".equalsIgnoreCase(map.get("Deny")))
			return bean;

		try {
			// 未到生成时间 则不生成 执行下一个报告
			String strGenerate = map.get("Generate");
			int nHours = Integer.parseInt(strGenerate);
			if (nHours != now.get(Calendar.HOUR_OF_DAY))
				return bean;
		} catch (Exception e) {
			return bean;
		}

		// 获取报表生成时间段
		if ("Day".equals(map.get("Period"))) {
			Calendar cal = (Calendar) now.clone();
			if (reportType == StatReport){
				try {
					//统计报告生成到EndTime结束时间为止的24小时报告
					Date endTime = END_TIME_FORMAT.parse(map.get("EndTime"));
					cal.setTime(endTime);
					cal.set(Calendar.YEAR, now.get(Calendar.YEAR));
					cal.set(Calendar.MONTH, now.get(Calendar.MONTH));
					cal.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH));
					cal.set(Calendar.SECOND, 0);
				} catch (Exception e) {
					e.printStackTrace();
					return bean;
				}
			}else if (reportType == TopNReport){
				//到当前时间为止的24小时数据
			}
			bean = new ReturnBean();
			bean.setEndTime(cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, -1);
			bean.setBeginTime(cal.getTime());
		} else if ("Week".equals(map.get("Period"))) {
			Calendar cal = (Calendar) now.clone();
			try{
				int nWeekDay = Integer.parseInt(map.get("WeekEndTime"));
				if ((cal.get(Calendar.DAY_OF_WEEK)-1) != nWeekDay) {
					return bean;
				}
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
			}catch(Exception e){
				e.printStackTrace();
				return bean;
			}

			bean = new ReturnBean();
			bean.setEndTime(cal.getTime());
			cal.add(Calendar.DAY_OF_MONTH, -7);
			bean.setBeginTime(cal.getTime());
		} else if ("Month".equals(map.get("Period"))) {
			if (now.get(Calendar.DAY_OF_MONTH) != 1){
				//不是月初1号，忽略
				return bean;
			}
			Calendar cal = (Calendar) now.clone();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);

			bean = new ReturnBean();
			bean.setEndTime(cal.getTime());
			cal.add(Calendar.MONTH, -1);
			bean.setBeginTime(cal.getTime());
		}
		return bean;
	}
	
	/**
	 * 生成统计报告
	 */
	private void writeAllStatReportHTML()
	{
		
			IniFile ini = new IniFile("reportset.ini");
			try{
				ini.load();
			}catch(Exception e){
				return;
			}
			
			// 根据用户报告配置 生成所有报告
			for (String key : ini.getSectionList())
			{
				try
				{
					Thread.yield();
					Map<String, String> map = ini.getSectionData(key);
					ReturnBean bean = getReportCondition(map,StatReport);
					if (bean == null) continue;
							
					StatsReport tmpReport = new StatsReport(key,map,bean.getBeginTime(),bean.getEndTime(),view,this.now);
					tmpReport.doReport();

					String strReportType = map.get("fileType");
					if (strReportType==null||strReportType.equals(""))
					{
						strReportType="html";
					}
					
					//防止因为定义的报告生成时间相同，而覆盖文件导致数据丢失
					String createTime = map.get("creatTime");
					Date createDate = Toolkit.getToolkit().parseDate(createTime);
					String createTimeInMillis = createDate.getTime() + "";
					
					// 报告发送地址
					String strToInfo = map.get("EmailSend");
					if (!"".equals(strToInfo) && strToInfo != null)
					{
						//String path = EccWebAppInit.getWebDir();
						String strZipDirPath = Constand.statreportsavepath + "statreport";
						// 清除目录等
						com.siteview.ecc.tuopu.MakeTuopuData.delFolder(strZipDirPath);
						com.siteview.ecc.tuopu.MakeTuopuData.createFolder(strZipDirPath);
						com.siteview.ecc.tuopu.MakeTuopuData.delFile(strZipDirPath + ".zip");
						
//						String fileName=Toolkit.getToolkit().formatDate(new Date(Long.parseLong(tmpReport.getReportFileID())),"yyyyMMdd_")+tmpReport.getReportFileID();
						String fileName = createTimeInMillis + "_" + tmpReport.getReportFileID();
						if ("html".equals(strReportType))
						{
							// 组建压缩包目录
//							String strSrcDirPath = Constand.statreportsavepath + fileName + ".html_files";
//							String strSrcHtmlPath = Constand.statreportsavepath + fileName + ".html";
//							
//							String strDestDirPath = Constand.statreportsavepath + "statreport\\" + tmpReport.getReportFileID() + ".html_files";
//							String strDestHtmlPath = Constand.statreportsavepath + "statreport\\" + tmpReport.getReportFileID() + ".html";
//							com.siteview.ecc.tuopu.MakeTuopuData.copyFile(strSrcHtmlPath, strDestHtmlPath);
//							com.siteview.ecc.tuopu.MakeTuopuData.copyFolder(strSrcDirPath, strDestDirPath);
							  String srcFolder = Constand.statreportsavepath+fileName;
//							  File f2 = new File(srcFolder + ".zip");
//							  if(!f2.exists()){
//								  DirectoryZip zip = new DirectoryZip();
//								  zip.zip(srcFolder, srcFolder + ".zip");
//							  }
							  strZipDirPath = srcFolder;
						} else if ("pdf".equals(strReportType))
						{
							String strSrcHtmlPath = Constand.statreportsavepath + fileName + ".pdf";
							String strDestHtmlPath = Constand.statreportsavepath + "statreport\\" + fileName + ".pdf";
							com.siteview.ecc.tuopu.MakeTuopuData.copyFile(strSrcHtmlPath, strDestHtmlPath);
						} else if ("xls".equals(strReportType))
						{
							String strSrcHtmlPath = Constand.statreportsavepath + fileName + ".xls";
							String strDestHtmlPath = Constand.statreportsavepath + "statreport\\" + fileName + ".xls";
							com.siteview.ecc.tuopu.MakeTuopuData.copyFile(strSrcHtmlPath, strDestHtmlPath);
						}else{
							continue;
						}
						
						this.sendMail(strZipDirPath, strToInfo, getContent(SEND_MAIL_SUBJECT,"统计报告(" + getTitle(map) + ")"), getContent(SEND_MAIL_BODY,"统计报告(" + getTitle(map) + ")"));
					}
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
	}
	
	/**
	 * 生成TOPN报告
	 */
	private void writeAllTopNReportHTML()
	{
			IniFile ini = new IniFile("topnreportset.ini");
			try{
				ini.load();
			} catch (Exception e)
			{
				return;
			}
//			iniGen = new LocalIniFile("mmctopnreportgenerate.ini");
//			try
//			{
//				iniGen.load();
//			} catch (Exception e)
//			{
//			}
			// 根据用户报告配置 生成所有报告
			for (String key : ini.getSectionList())
			{
				try
				{
					Thread.yield();
					Map<String, String> map = ini.getSectionData(key);
					ReturnBean bean = getReportCondition(map,TopNReport);
					if (bean == null) continue;
					
					String strGroupRight = map.get("GroupRight");
					if (strGroupRight == null || "".equals(strGroupRight))
						continue;
						
					// 该 生成此报告 了
					TopNReport tmpTopNReport = null;
					// 1. 生成TOPN报告
//					tmpTopNReport = new TopNReport(strTitle, strGroupRight, strSelType, strMark, strSort,strget, strCount, tmStart, tmEnd, view);
					tmpTopNReport = new TopNReport(key, map, bean.getBeginTime(), bean.getEndTime(), view,true);
					tmpTopNReport.createReport();
					
	
							// 2. 写信息到写mmctopnreportgenerate.ini
	//						String strGenKey = "";
	//						String strGenValue = "";
	//						String strTmp = "";
	//						String strGenSection = "";
	//						// section节格式：section$开始时间$截止时间$
	//						strGenSection = key + "$" + DateToString(tmStart) + "$" + DateToString(tmEnd) + "$";
	//						
	//						try
	//						{
	//							if (iniGen.getSectionList().contains(strGenSection))
	//							{
	//								iniGen.deleteSection(strGenSection);
	//							}
	//							iniGen.createSection(strGenSection);
	//							// 键值格式：$返回值名称$
	//							strTmp = map.get("Mark");
	//							if ("".equals(strTmp))
	//							{
	//								strTmp = map.get("Type");
	//							}
	//							strGenKey = "$" + strTmp + "$";
	//							// 写Ini
	//							iniGen.setKeyValue(strGenSection, strGenKey, strGenValue);
	//							iniGen.saveChange();
	//						} catch (Exception e)
	//						{
	//							e.printStackTrace();
	//						}
					// 本版新加功能 得到报告文件输出类型 默认类型为html
					String strReportType = map.get("fileType");
					if (strReportType==null || strReportType.equals(""))
					{
						strReportType="html";
					}

					// 3.发送EMail到指定邮箱
					// 报告发送地址
					String strToInfo = map.get("EmailSend");
					if (!"".equals(strToInfo) && strToInfo != null)
					{
						String strZipDirPath = Constand.topnreportsavepath + "sendemailtopnreport";
						// 清除目录等
						com.siteview.ecc.tuopu.MakeTuopuData.delFolder(strZipDirPath);
						com.siteview.ecc.tuopu.MakeTuopuData.createFolder(strZipDirPath);
						com.siteview.ecc.tuopu.MakeTuopuData.delFile(strZipDirPath + ".zip");
						if ("html".equals(strReportType))
						{
							// 组建压缩包目录
							String strSrcDirPath = Constand.topnreportsavepath + tmpTopNReport.strReportName + ".html_files";
							String strSrcHtmlPath = Constand.topnreportsavepath + tmpTopNReport.strReportName + ".html";
							
							String strDestDirPath = Constand.topnreportsavepath + "sendemailtopnreport\\" + tmpTopNReport.strReportName + ".html_files";
							String strDestHtmlPath = Constand.topnreportsavepath + "sendemailtopnreport\\" + tmpTopNReport.strReportName + ".html";
							com.siteview.ecc.tuopu.MakeTuopuData.copyFile(strSrcHtmlPath, strDestHtmlPath);
							com.siteview.ecc.tuopu.MakeTuopuData.copyFolder(strSrcDirPath, strDestDirPath);
						} else if ("pdf".equals(strReportType))
						{
							String strSrcHtmlPath = Constand.topnreportsavepath + tmpTopNReport.strReportName + ".pdf";
							String strDestHtmlPath = Constand.topnreportsavepath + "sendemailtopnreport\\" + tmpTopNReport.strReportName + ".pdf";
							com.siteview.ecc.tuopu.MakeTuopuData.copyFile(strSrcHtmlPath, strDestHtmlPath);
						} else if ("xls".equals(strReportType))
						{
							String strSrcHtmlPath = Constand.topnreportsavepath + tmpTopNReport.strReportName + ".xls";
							String strDestHtmlPath = Constand.topnreportsavepath + "sendemailtopnreport\\" + tmpTopNReport.strReportName + ".xls";
							com.siteview.ecc.tuopu.MakeTuopuData.copyFile(strSrcHtmlPath, strDestHtmlPath);
						}else{
							continue;
						}
						
						this.sendMail(strZipDirPath, strToInfo, getContent(SEND_MAIL_SUBJECT,"TopN报告(" + getTitle(map) + ")"), getContent(SEND_MAIL_BODY,"TopN报告(" + getTitle(map) + ")"));
					}
				} catch (Exception e){
					e.printStackTrace();
				}
			}
	}
	private static String getContent(String sourceStr,String replaceStr){
		return sourceStr.replace("%1%", replaceStr);
	}
	private static String getTitle(Map<String, String> map){
		String title = map.get("Title");
		if (title==null)return "无名称";
		String arrayTitle[] = title.split("[|]");
		for (String retTitle : arrayTitle){
			return retTitle;
		}
		return "无名称";
	}
    private void sendMail(String strZipDirPath,String strToInfo,String mailSubject,String mailbody) throws Exception{	
    	MailInfBean mailInfo = getEmailInfo();
    	if (mailInfo == null) throw new Exception("getEmailInfo Faulure!");
		DirectoryZip zip = new DirectoryZip();
		zip.zip(strZipDirPath, strZipDirPath + ".zip");
		// 邮件发送
		sendMail themail = new sendMail(mailInfo.getSmtpServerInfo());
		themail.setNeedAuth(true);
		if (themail.setSubject(mailSubject) == false)
			throw new Exception("setSubject Failure!");
		if (themail.setBody(mailbody) == false)
			throw new Exception("setBody Failure!");
		if (themail.setTo(strToInfo) == false)
			throw new Exception("setTo Failure!");
		if (themail.setFrom(mailInfo.getFromInfo()) == false)
			throw new Exception("setFrom Failure!");
		if (themail.addFileAffix(strZipDirPath + ".zip") == false)
			throw new Exception("addFileAffix Failure!");
		themail.setNamePass(mailInfo.getFromUserInfo(), mailInfo.getFromPwdInfo());
		if (themail.sendout() == false)
			throw new Exception("setNamePass Failure!");
    }

	
	// / <summary>
	// / 获取新的邮件发送配置信息
	// / </summary>
	private MailInfBean getEmailInfo()
	{
		if (mailInfo!=null) return mailInfo;
		try
		{
			// 获取邮件发送所需基本参数
			IniFile iniEmail = new IniFile("email.ini");
			iniEmail.load();
			Map<String, String> map = iniEmail.getSectionData("email_config");
			mailInfo = new MailInfBean();
			mailInfo.setSmtpServerInfo(map.get("server"));
			// strToInfo = tmpfile["email_config"]["to"];
			mailInfo.setFromInfo(map.get("from"));
			mailInfo.setFromUserInfo(map.get("user"));
			mailInfo.setFromPwdInfo(map.get("password"));
			if (mailInfo.getFromPwdInfo() != null && !"".equals(mailInfo.getFromPwdInfo()))
			{
				String password = mailInfo.getFromPwdInfo();
				String retPassword = UnivData.decrypt(password);
				mailInfo.setFromPwdInfo(retPassword == null ? "" : retPassword);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return mailInfo;
	}
	
}
