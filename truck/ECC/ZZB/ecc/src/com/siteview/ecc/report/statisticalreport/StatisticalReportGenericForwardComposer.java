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
 * ����ͳ�Ʊ���
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
	 * ��ȡ���������ļ�
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
	 * ��ʼ��
	 */
	public void onCreate$createReportWindow() {
		// �û�ѡ�еı���
		sections = (String) Executions.getCurrent().getDesktop().getSession().getAttribute("createReportSection");
		String cbd = (String) Executions.getCurrent().getParameter("cbd");
		String ced = (String) Executions.getCurrent().getParameter("ced");
		createListboxAndImage(cbd, ced);
	}

	/**
	 * ���ɱ��ͼ
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
		if (sections != null && cbd != null && ced != null) {// ���»�ȡ����ʱִ��
			doRegetDatatime(cbd, ced);
		}
		// ȡ�����������;
		if (sections != null && cbd == null) {
			dotheTimePart();
		}
		if (false) {// ԭ��ȡREPORT���ݷ���
			try {
				String GroupRight = inifile.getValue(sections, "GroupRight");
				String[] nodeids = GroupRight.split(",");
				starttime = stime.toString();
				endtime = etime.toString();
				// ��ȡ��������
				List<Report> rlist = cr.getReportData(nodeids, starttime, endtime);
				// ���ɱ���
				try {
					// ***********���ɸ�Listbox
					Panel prun = cr.createRuntableGrid(rlist, 0, "���������"); // �������б�
					Panel mrun = cr.createMonitorInfoGrid(rlist, 0, "�������Ϣ��");// ���ɼ������Ϣ��
					// ��֯������ʾҪ��
					datapanel.appendChild(prun);
					datapanel.appendChild(mrun);
				} catch (java.lang.OutOfMemoryError e) {
					e.printStackTrace();
				}
				// *********���ɸ�ͼ��
				if (rlist != null && rlist.size() > 0) {
					Vbox imageVbox = getAllImagemap(rlist, reporttype, stime, etime);
					imageVbox.setAlign("center");
					datapanel.appendChild(imageVbox);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {// �¶�ȡREPORT���ݷ���
			try {
				String GroupRight = inifile.getValue(sections, "GroupRight");
				// ��ȡ��������
				Map<String, Map<String, String>> fmap = null;
				rd = new ReportDate(stime, etime);
				try {
					long start = System.currentTimeMillis();
					fmap = rd.getReportDate(GroupRight);
					logger.info("xxxxxxxxxxxxxxx:" + (System.currentTimeMillis() - start));
				} catch (Exception e) {
					e.printStackTrace();
				}
				// ���ɱ���
				try {
					// ***********���ɸ�Listbox
					long start = System.currentTimeMillis();
					Panel prun = cr.createRuntableGrid(GroupRight, rd, 0, "���������"); // �������б�
					Panel mrun = cr.createMonitorInfoGrid(GroupRight, rd, 0, "�������Ϣ��");// ���ɼ������Ϣ��
					// ��֯������ʾҪ��
					datapanel.appendChild(prun);
					datapanel.appendChild(mrun);
					logger.info("xxxxxxxxxxxxxxx1:" + (System.currentTimeMillis() - start));
				} catch (java.lang.OutOfMemoryError e) {
					e.printStackTrace();
				}
				// *********���ɸ�ͼ��
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
	 * ���ɱ������,���»�ȡ���ݰ�ť�¼�
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
	 * ��������ť�¼�
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
	 * ��ӡ��ť�¼�
	 * 
	 * @param event
	 */
	public void onPrintReport(Event event) {

	}

	private void dotheTimePart() {
		Calendar c = Calendar.getInstance();
		if (reporttype.equals("�ձ�")) {
			reporttype = Constand.reporttype_dayreport;
			int ah = Integer.parseInt(endtime.substring(0, endtime.indexOf(":")));
			int eh = Integer.parseInt(endtime.substring(endtime.indexOf(":") + 1));
			c.set(Calendar.HOUR_OF_DAY, ah);
			c.set(Calendar.MINUTE, eh);
			etime = c.getTime();
			c.add(Calendar.DAY_OF_MONTH, -1);
			stime = c.getTime();

		} else if (reporttype.equals("�ܱ�")) {
			reporttype = Constand.reporttype_weekreport;
			int a = Integer.parseInt(inifile.getValue(sections, "WeekEndTime"));
			c.setTime(new Date());
			c.set(Calendar.HOUR_OF_DAY, 0);
			c.set(Calendar.MINUTE, 0);
			c.set(Calendar.SECOND, 0);
			etime = c.getTime();
			c.add(Calendar.DATE, -7);
			stime = c.getTime();
		} else if (reporttype.equals("�±�")) {
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
		if (reporttype.equals("�ձ�")) {
			reporttype = Constand.reporttype_dayreport;
		} else if (reporttype.equals("�ܱ�")) {
			reporttype = Constand.reporttype_weekreport;
		} else if (reporttype.equals("�±�")) {
			reporttype = Constand.reporttype_monthreport;
		}
	}

	/**
	 * ������ͼƬ��װ��Imagemap����Vbox��
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
			// ��ȡ��ͼ������
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
					//		start, end, 0, true, 740, 350, reporttype);// ����ͼ
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
			// ��ȡ��ͼ������
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
								100, start, end, 0, true, 740, 350, reporttype);// ����ͼ
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
	 * ����ͳ�Ʊ������,�����ʼ���ť�¼�
	 * 
	 * @param event
	 */
	public void onSendEmail(Event event) {
		String strMailTo = inifile.getValue(sections, "EmailSend");
		String title = inifile.getValue(sections, "Title");
		String strZipDirPath = null;// "c:\\xxx.zip";// FIXME ��Ҫ���͸������Է�����
		if (strMailTo == null || strMailTo.equals("") || strMailTo.contains(" ")) {
			try {
				Messagebox.show("�ñ�����û�����E_MAIL��ַ��Ϣ!", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
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
			// �ʼ�����
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
				Messagebox.show("�ʼ����ͳɹ�!", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
			} else {
				try {
					Messagebox.show("�����ʼ����ɹ�!", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				} catch (InterruptedException e1) {
				}
			}
		} catch (Exception e) {
			try {
				Messagebox.show("�����ʼ����ɹ�:" + e.getMessage(), "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e1) {
			}
		}

	}
}
