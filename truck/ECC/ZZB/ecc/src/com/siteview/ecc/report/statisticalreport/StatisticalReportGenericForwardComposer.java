package com.siteview.ecc.report.statisticalreport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jfree.data.xy.XYDataset;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.Report;
import com.siteview.base.data.ReportDate;
import com.siteview.ecc.report.Constand;
import com.siteview.ecc.report.common.CreateReport;
import com.siteview.ecc.report.common.CreateReportImpl;
import com.siteview.ecc.reportserver.sendMail;
import com.siteview.ecc.util.Toolkit;

/**
 * 生成统计报告
 * 
 * @company: siteview
 * @author:di.tang
 * @date:2009-4-1
 */
 

public class StatisticalReportGenericForwardComposer extends GenericForwardComposer {
	private final static Logger logger = Logger.getLogger(StatisticalReportGenericForwardComposer.class);
	/*
	 * private Map<Integer, Map<String, String>> listimage;
	 * private Date startdate; private Date enddate; private String comparetype;
	 */
	private IniFile inifile = null;
	private CreateReport cr = null;
	Window createReportWindow;
	Panelchildren datapanel;
	Date stime = new Date();
	Date etime = new Date();
	String starttime = null;
	String endtime = null;
	String reporttype = null;
	String sections = null;
	Datebox createBegin_Date;
	Datebox createEnd_Date;
	Timebox createBegin_Time;
	Timebox createEnd_Time;
	private ReportDate rd = null;

	public StatisticalReportGenericForwardComposer() {
		getIniFile();
	}

	/**
	 * 读取报告配置文件
	 */
	private void getIniFile() {
		inifile = new IniFile(Constand.reportinifilename);
		try {
			inifile.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.cr = new CreateReportImpl();
	}

	/**
	 * 初始化
	 */
	public void onCreate$createReportWindow() {
		// 用户选中的报告
		sections = (String) Executions.getCurrent().getDesktop().getSession().getAttribute("createReportSection");
		String cbd = (String) Executions.getCurrent().getParameter("cbd");
		String ced = (String) Executions.getCurrent().getParameter("ced");
		createListboxAndImage(cbd, ced);
	}

	/**
	 * 生成表和图
	 * 
	 * @param cbd
	 * @param ced
	 */
	private void createListboxAndImage(String cbd, String ced) {
		if (sections != null) {
			starttime = inifile.getValue(sections, "StartTime");
			endtime = inifile.getValue(sections, "EndTime");
			reporttype = inifile.getValue(sections, "Period");
		} else {
			return;
		}
		if (sections != null && cbd != null && ced != null) {// 重新获取数据时执行
			doRegetDatatime(cbd, ced);
		}
		// 取报告相关设置;
		if (sections != null && cbd == null) {
			dotheTimePart();
		}
		if (false) {// 原读取REPORT数据方法
			try {
				String GroupRight = inifile.getValue(sections, "GroupRight");
				String[] nodeids = GroupRight.split(",");
				starttime = stime.toString();
				endtime = etime.toString();
				// 读取报告数据
				List<Report> rlist = cr.getReportData(nodeids, starttime, endtime);
				// 生成报告
				try {
					// ***********生成各Listbox
					Panel prun = cr.createRuntableGrid(rlist, 0, "运行情况表"); // 生成运行表
					Panel mrun = cr.createMonitorInfoGrid(rlist, 0, "监测器信息表");// 生成监测器信息表
					// 组织界面显示要素
					datapanel.appendChild(prun);
					datapanel.appendChild(mrun);
				} catch (java.lang.OutOfMemoryError e) {
					e.printStackTrace();
				}
				// *********生成各图表
				if (rlist != null && rlist.size() > 0) {
					Vbox imageVbox = getAllImagemap(rlist, reporttype, stime, etime);
					imageVbox.setAlign("center");
					datapanel.appendChild(imageVbox);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {// 新读取REPORT数据方法
			try {
				String GroupRight = inifile.getValue(sections, "GroupRight");
				// 读取报告数据
				Map<String, Map<String, String>> fmap = null;
				rd = new ReportDate(stime, etime);
				try {
					long start = System.currentTimeMillis();
					fmap = rd.getReportDate(GroupRight);
					logger.info("xxxxxxxxxxxxxxx:" + (System.currentTimeMillis() - start));
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 生成报告
				try {
					// ***********生成各Listbox
					long start = System.currentTimeMillis();
					Panel prun = cr.createRuntableGrid(GroupRight, rd, 0, "运行情况表"); // 生成运行表
					Panel mrun = cr.createMonitorInfoGrid(GroupRight, rd, 0, "监测器信息表");// 生成监测器信息表
					// 组织界面显示要素
					datapanel.appendChild(prun);
					datapanel.appendChild(mrun);
					logger.info("xxxxxxxxxxxxxxx1:" + (System.currentTimeMillis() - start));
				} catch (java.lang.OutOfMemoryError e) {
					e.printStackTrace();
				}
				// *********生成各图表
				if (fmap != null && fmap.size() > 0) {
					long start = System.currentTimeMillis();
					Vbox imageVbox = this.getAllImagemap(GroupRight, rd, reporttype, stime, etime);
					imageVbox.setAlign("center");
					datapanel.appendChild(imageVbox);
					logger.info("xxxxxxxxxxxxxxx2:" + (System.currentTimeMillis() - start));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 生成报表界面,重新获取数据按钮事件
	 * 
	 * @param event
	 */
	public void onRegetdata(Event event) {
		String cbd = createBegin_Date.getText() + " " + createBegin_Time.getText();
		String ced = createEnd_Date.getText() + " " + createEnd_Time.getText();
		try {
			List children = datapanel.getChildren();
			List os = new ArrayList();
			for (int a = 0; a < children.size(); a++) {
				os.add(children.get(a));
			}
			for (int b = 0; b < os.size(); b++) {
				datapanel.removeChild((Component) (os.get(b)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		createListboxAndImage(cbd, ced);
	}

	/**
	 * 导出报表按钮事件
	 * 
	 * @param event
	 */
	public void onExportReport(Event event) {
		try {
			Map pmap = new HashMap();
			pmap.put("startdate", this.stime);
			pmap.put("enddate", this.etime);
			pmap.put("reporttime", reporttype);
			pmap.put("reporttitle", inifile.getValue(sections, "Title"));

			pmap.put("ExportReportDate", this.rd);
			final Window win = (Window) Executions.createComponents("/main/report/exportreport/exportreport.zul", null, pmap);
			Executions.getCurrent().getDesktop().getSession().setAttribute("THEWINDOW", win);
			win.doModal();
		} catch (SuspendNotAllowedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 打印按钮事件
	 * 
	 * @param event
	 */
	public void onPrintReport(Event event) {

	}

	private void dotheTimePart() {
		Calendar c = Calendar.getInstance();
		if (reporttype.equals("日报")) {
			reporttype = Constand.reporttype_dayreport;
			int ah = Integer.parseInt(endtime.substring(0, endtime.indexOf(":")));
			int eh = Integer.parseInt(endtime.substring(endtime.indexOf(":") + 1));
			c.set(Calendar.HOUR_OF_DAY, ah);
			c.set(Calendar.MINUTE, eh);
			etime = c.getTime();
			c.add(Calendar.DAY_OF_MONTH, -1);
			stime = c.getTime();

		} else if (reporttype.equals("周报")) {
			reporttype = Constand.reporttype_weekreport;
			int a = Integer.parseInt(inifile.getValue(sections, "WeekEndTime"));
			c.setTime(new Date());
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			etime = c.getTime();
			c.add(Calendar.DATE, -7);
			stime = c.getTime();
		} else if (reporttype.equals("月报")) {
			reporttype = Constand.reporttype_monthreport;
			c.setTime(new Date());
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			etime = c.getTime();
			c.add(Calendar.MONTH, -1);
			stime = c.getTime();
		}
	}

	private void doRegetDatatime(String st, String et) {
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			stime = sdf.parse(st);
			etime = sdf.parse(et);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (reporttype.equals("日报")) {
			reporttype = Constand.reporttype_dayreport;
		} else if (reporttype.equals("周报")) {
			reporttype = Constand.reporttype_weekreport;
		} else if (reporttype.equals("月报")) {
			reporttype = Constand.reporttype_monthreport;
		}
	}

	/**
	 * 将所有图片组装成Imagemap放在Vbox中
	 * 
	 * @param rlist
	 * @param start
	 * @param end
	 * @return
	 */
	public Vbox getAllImagemap(List<Report> rlist, String reporttype, Date start, Date end) {
		Vbox v = new Vbox();
		try {
			Map<String, Map<Date, String>> imgdatas = new HashMap<String, Map<Date, String>>();
			// 获取各图表数据
			Map<Integer, Map<String, String>> imgeLinkedHashMap = cr.getImagelist(rlist.get(0));
			for (int key : imgeLinkedHashMap.keySet()) {
				for (Report report : rlist) {
					Map<Date, String> imgdata = report.getReturnValueDetail(key);
					imgdatas.put(report.getReturnValue("MonitorName", key), imgdata);
				}
				XYDataset xd = cr.buildDataset(imgdatas);

				Map<String, String> keyvalue = imgeLinkedHashMap.get(key);

				try {
					//Imagemap temmap = cr.buildImageMap(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), xd, 10, 100,
					//		start, end, 0, true, 740, 350, reporttype);// 建各图
					//v.appendChild(temmap);
					v.appendChild(Toolkit.getToolkit().createImage(cr.buildImageBuffer(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), xd, 10, 100,
							start, end, 0, true, 740, 350, reporttype)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
		}
		return v;
	}

	public Vbox getAllImagemap(String nodeids, ReportDate rd, String reporttype, Date start, Date end) {
		Vbox v = new Vbox();
		v.setAlign("center");
		try {
			Map<String, Map<Date, String>> imgdatas = new HashMap<String, Map<Date, String>>();
			// 获取各图表数据
			String[] nodeid = nodeids.split(",");
			int i = 0;
			for (String id : nodeid) {
				Map<Integer, Map<String, String>> imgeLinkedHashMap = cr.getImagelist(id, rd);
				for (int key : imgeLinkedHashMap.keySet()) {
					Map<Date, String> imgdata = rd.getReturnValueDetail(id, key);
					imgdatas.put(rd.getReturnValue(id, "MonitorName", key), imgdata);
					XYDataset xd = cr.buildDataset(imgdatas);
					Map<String, String> keyvalue = imgeLinkedHashMap.get(key);
					try {
						/*Imagemap temmap = cr.buildImageMap(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), xd, 10,
								100, start, end, 0, true, 740, 350, reporttype);// 建各图
						v.appendChild(temmap);*/
						
						v.appendChild(Toolkit.getToolkit().createImage(cr.buildImageBuffer(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), xd, 10, 100,
								start, end, 0, true, 740, 350, reporttype)));
						
						
						i++;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// logger.info(i);
				if (i > 100)
					break;

			}

		} catch (Exception e) {
		}
		return v;
	}

	/**
	 * 生成统计报表界面,发送邮件按钮事件
	 * 
	 * @param event
	 */
	public void onSendEmail(Event event) {
		String strMailTo = inifile.getValue(sections, "EmailSend");
		String title = inifile.getValue(sections, "Title");
		String strZipDirPath = null;// "c:\\xxx.zip";// FIXME 需要发送附件到对方邮箱
		if (strMailTo == null || strMailTo.equals("") || strMailTo.contains(" ")) {
			try {
				Messagebox.show("该报告中没有相关E_MAIL地址信息!", "提示", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e) {
			}
			return;
		}
		com.siteview.ecc.email.SendTestImpl send = new com.siteview.ecc.email.SendTestImpl();
		String INI_FILE = "email.ini";
		IniFile file = new IniFile(INI_FILE);
		try {
			file.load();
		} catch (Exception e) {
		}
		String strMailServer = file.getValue("email_config", "server");
		String strBackupServer = file.getValue("email_config", "backupserver");
		String strMailFrom = file.getValue("email_config", "from");
		String strUser = file.getValue("email_config", "user");
		String strPassword = file.getValue("email_config", "password");
		try {
			// 邮件发送
			String mailbody = "report";
			sendMail themail = new sendMail(strMailServer);
			themail.setNeedAuth(true);
			themail.setSubject(title);
			themail.setBody(mailbody);
			themail.setTo(strMailTo);
			themail.setFrom(strMailFrom);
			themail.addFileAffix(strZipDirPath);
			themail.setNamePass(strUser, strPassword);
			boolean ret = themail.sendout();
			if (ret) {
				Messagebox.show("邮件发送成功!", "提示", Messagebox.OK, Messagebox.INFORMATION);
			} else {
				try {
					Messagebox.show("发送邮件不成功!", "提示", Messagebox.OK, Messagebox.INFORMATION);
				} catch (InterruptedException e1) {
				}
			}
		} catch (Exception e) {
			try {
				Messagebox.show("发送邮件不成功:" + e.getMessage(), "提示", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e1) {
			}
		}

	}
}
