package com.siteview.ecc.report;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Imagemap;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.Report;
import com.siteview.base.data.ReportDate;
import com.siteview.base.manage.View;
import com.siteview.ecc.report.common.CreateReport;
import com.siteview.ecc.report.common.CreateReportImpl;
import com.siteview.ecc.reportserver.TopNReport;
import com.siteview.ecc.reportserver.sendMail;
import com.siteview.ecc.util.Toolkit;

/**
 * �ֶ�����TOPN���洦����
 * 
 * @company siteview
 * @author di.tang
 * @date 2009-4-27
 */
public class CreateTopNReport extends GenericForwardComposer {
	Div datapanel;
	Window createReportWindow;
	IniFile Top_NIniFile = null;
	String currsection = null;
	Date tmStart = null;
	Date tmEnd = new Date();
	ReportDate rd = null;

	private String getValue(String key) {
		return Top_NIniFile.getValue(currsection, key);
	}

	/**
	 * ȡ���� 20090531 add by di.tang
	 */
	private void getReportDate() {
		String strIds = getValue("GroupRight");
		CreateReport cr = new CreateReportImpl();
		this.rd = new ReportDate(this.tmStart, this.tmEnd);
		try {
			rd.getReportDate(strIds);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void dealtmStart() {
		String strSelType = getValue("Period");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		if (strSelType.equals("�ձ�")) {
			c.add(Calendar.DAY_OF_MONTH, -1);
		} else if (strSelType.equals("�ܱ�")) {
			c.add(Calendar.DATE, -7);
		} else if (strSelType.equals("�±�")) {
			c.add(Calendar.MONTH, -1);
		}
		tmStart = c.getTime();

	}

	public void onCreate$createReportWindow(CreateEvent event) {
		Top_NIniFile = (IniFile) event.getArg().get("Top_NIniFile");
		currsection = (String) createReportWindow.getAttribute("currsection");
		dealtmStart();
		getReportDate();// ȡ����
		doReport();
	}

	/**
	 * ����topn�������,���»�ȡ���ݰ�ť�¼�
	 * 
	 * @param event
	 */
	public void onRegetdata(Event event) {
		getReportDate();// ȡ����
		try {
			List lc = datapanel.getChildren();
			Image im = (Image) lc.get(0);
			Listbox lb = (Listbox) lc.get(1);
			datapanel.removeChild(im);
			datapanel.removeChild(lb);
		} catch (Exception e) {
		}
		Div d = (Div) createReportWindow.getFellow("p1").getFellow("pc1").getFellow("d1");
		Datebox createBegin_Date = (Datebox) d.getFellow("createBegin_Date");
		Datebox createEnd_Date = (Datebox) d.getFellow("createEnd_Date");
		Timebox createBegin_Time = (Timebox) d.getFellow("createBegin_Time");
		Timebox createEnd_Time = (Timebox) d.getFellow("createEnd_Time");
		Calendar c = Calendar.getInstance();
		c.setTime(createBegin_Date.getValue());
		Date da = createBegin_Time.getValue();
		Calendar b = Calendar.getInstance();
		b.setTime(da);
		c.set(Calendar.HOUR_OF_DAY, b.get(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, b.get(Calendar.MINUTE));
		c.set(Calendar.SECOND, 0);
		tmStart = c.getTime();
		c = Calendar.getInstance();
		c.setTime(createEnd_Date.getValue());
		da = createEnd_Time.getValue();
		b = Calendar.getInstance();
		b.setTime(da);
		c.set(Calendar.HOUR_OF_DAY, b.get(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, b.get(Calendar.MINUTE));
		c.set(Calendar.SECOND, 0);
		tmEnd = c.getTime();
		doReport();
	}

	/**
	 * ����topn�������,�����ʼ���ť�¼�
	 * 
	 * @param event
	 */
	public void onSendEmail(Event event) {
		String strMailTo = getValue("EmailSend");
		String title = getValue("Title");
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

	// ������ť�¼�,ִ�е���TOPN������
	public void onExportTopNReport(Event event) {
		try {
			HashMap pmap = new HashMap();
			pmap.put("startdate", this.tmStart);
			pmap.put("enddate", this.tmEnd);
			pmap.put("reporttitle", getValue("Title"));
			pmap.put("ExportReportDate", this.rd);
			pmap.put("Mark", getValue("Mark"));
			pmap.put("Sort", getValue("Sort"));
			pmap.put("Count", getValue("Count"));
			final Window win = (Window) Executions.createComponents("/main/report/topnreport/exportreport.zul", null, pmap);
			Executions.getCurrent().getDesktop().getSession().setAttribute("THEWINDOW", win);
			win.doModal();
		} catch (SuspendNotAllowedException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void doReport() {
		String Generate = getValue("Generate");

		String strName = null;
		String strSelType = getValue("Type");
		String strMark = getValue("Mark");
		String strSort = getValue("Sort");
		String strCount = getValue("Count");
		View w = null;
		try {
			w = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop().getSession());
		} catch (Exception e) {
			e.printStackTrace();
		}
		String strIds = getValue("GroupRight");
		strName = getValue("Title");
		if (strSort==null||strSort.equals(""))
		{
			strSort="����";
		}
		String strget=getValue("GetValue");
		if(strget==null||strget.equals(""))
		{
			strget="����";
		}
//		TopNReport tn = new TopNReport(strName, strIds, strSelType, strMark, strSort, strget,strCount, tmStart, tmEnd, w);
//		// ����ͼƬ
//		Image im = tn.buildImage(strName, "", strMark, 765, 370);
		//datapanel.appendChild(im);
		datapanel.setAlign("center");
		// ���������б�
		//List<Report> lstmonitorreport = tn.lstMonitorReport;
		Listbox gr = new Listbox();

		gr.setRows(3);
		// gr.setFixedLayout(true);
		Listhead lh = new Listhead();
		Listheader lhr = new Listheader("�豸����");
		lhr.setSort("auto");
		lhr.setWidth("30%");
		lh.appendChild(lhr);
		lhr = new Listheader("���������");
		lhr.setSort("auto");
		lhr.setWidth("30%");
		lh.appendChild(lhr);
		lhr = new Listheader("���ֵ");
		lhr.setSort("auto");
		lhr.setWidth("10%");
		lh.appendChild(lhr);
		lhr = new Listheader("ƽ��ֵ ");
		lhr.setSort("auto");
		lhr.setWidth("10%");
		lh.appendChild(lhr);
		lhr = new Listheader("��Сֵ");
		lhr.setSort("auto");
		lhr.setWidth("10%");
		lh.appendChild(lhr);
		lhr = new Listheader("���һ������");
		lhr.setSort("auto");
		lh.appendChild(lhr);
		gr.appendChild(lh);
		int x = 1;
		int datacounts = 0;
		// �豸���� ��������� ���ֵ ƽ��ֵ ��Сֵ ���һ������
//		for (Report r : lstmonitorreport) {
//			Listitem item = new Listitem();
//			datacounts++;
//			if (Integer.parseInt(strCount) < datacounts) {// ���������ʾ������
//				break;
//			}
//			for (int i = 0; i < r.getReturnSize(); i++) {
//				String returnName = r.getReturnValue("ReturnName", i);
//				// ����ѡ��ķ���ֵ������ʾ��
//				if (!returnName.equals(strMark)) {
//					continue;
//				}
//				x = i;
//				try {
//					Listcell l1 = null;
//					try {
//						l1 = new Listcell(r.getReturnValue("MonitorName", x).substring(0, r.getReturnValue("MonitorName", x).indexOf(":")));
//						item.appendChild(l1);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					l1 = new Listcell(r.getReturnValue("MonitorName", x));
//					item.appendChild(l1);
//					l1 = new Listcell(r.getReturnValue("max", x));
//					item.appendChild(l1);
//					l1 = new Listcell(r.getReturnValue("average", x));
//					item.appendChild(l1);
//					l1 = new Listcell(r.getReturnValue("min", x));
//					item.appendChild(l1);
//					l1 = new Listcell(r.getReturnValue("latest", x));
//					item.appendChild(l1);
//					gr.appendChild(item);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
		datapanel.appendChild(gr);
	}
}
