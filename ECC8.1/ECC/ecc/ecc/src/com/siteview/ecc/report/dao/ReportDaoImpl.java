/**
 * ecc:����11:14:42
 */
package com.siteview.ecc.report.dao;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.UserRightId;
import com.siteview.base.manage.View;
import com.siteview.ecc.alert.SelectTree;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.alert.util.LocalIniFile;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.progress.BlankWindow;
import com.siteview.ecc.progress.ProgressWindow;
import com.siteview.ecc.report.Constand;
import com.siteview.ecc.report.common.GroupLinkListener;
import com.siteview.ecc.reportserver.StatsReport;
import com.siteview.ecc.util.LinkCheck;
import com.siteview.ecc.util.Toolkit;

/**
 * ������DAO,������REPORTTABLEDAOIMPL.cs��Ӧ,��Ӧ˵���μ�REPORTTABLEDAOIMPL.cs
 * 
 * @author : di.tang
 * @date: 2009-3-19
 * @company: siteview
 */
public class ReportDaoImpl extends GenericAutowireComposer {
	Panel simplemonitorreports; // �򵥼����ʱ���
	Listbox simplemonitorreportslistbox;
	Button simpleMonitorButton;
	Window reportwindow;
	Panel pa1;
	Panel pa2;
	Listbox direportlistbox;
	Textbox Title;
	Listbox Period;
	Listbox ComboGraphic;
	Textbox EmailSend;
	Checkbox GroupRight; // = ",1.9.5.1,"//FIXME δ�����ϱ�ʾ����,
	Listbox WeekEndTime;
	Checkbox ListDanger;
	// FIXME SumCheckItem= "Ping,|���ɹ���(%),��������ʱ��(ms),״ֵ̬(200��ʾ�ɹ� 300��ʾ����),"
	Checkbox ListAlert;
	// FIXME StatusResult= "No"
	Checkbox Graphic;
	Textbox Descript;
	Checkbox ListError;
	Listbox EndTime1;
	Listbox EndTime2;
	Listbox StartTime;
	Listbox Generate;
	Checkbox Parameter;
	private IniFile ini = null;
	private LocalIniFile dataFile = null;
	Include eccBody;
	Iframe PeriodofQueryReportIframe;
	Datebox starttime;
	Datebox endtime;
	
	//�¼ӱ��� ������
	Datebox Begin_Date;
	Datebox Begin_Time;
	Datebox End_Date;
	Datebox End_Time;
	
	
	Listbox comparetype;
	Tree monitortree;
	Tree tree;
	Window addreportwindow;
	Window createReportWindow;
	Combobox viewNamecombobox;
	Window simplemonitorreportwindow;
	Iframe simplemonitorreportsinclude;
	ReportDaoImpl rd = null;
	Listbox fileType;
	Checkbox Deny;
	Label 	 groupLink;
	
	public void onCreate$groupLink(Event event){
		boolean isLink = LinkCheck.getLinkCheck().CanSeeLink(UserRightId.WholeView);
		if(isLink){
			String style = "color:#18599C;cursor:pointer;text-decoration: underline;";
			groupLink.setStyle(style);
		}
	}
	public void onClick$groupLink(Event e){
		Comboitem item = viewNamecombobox.getSelectedItem();
		if(item!=null){
			groupLink.addEventListener(Events.ON_CLICK,new GroupLinkListener(item.getLabel()));
		}
	}
	public ReportDaoImpl() {
		if (this.tree != null) {
			Treeitem ti = (Treeitem) tree.getRoot();
			ti.setOpen(true);
		}
	}

	private IniFile getIniFile() {
		if (ini == null) {
			ini = new IniFile(Constand.reportinifilename);
			try {
				ini.load();
			} catch (Exception e) {
			}
		}
		return ini;
	}

	private LocalIniFile getSimpleMonitorIniFile() {
		if (dataFile == null) 
		{
			dataFile = new LocalIniFile(Constand.simplemonitorinfoinifilename);
			try {
				dataFile.load();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dataFile;
	}

	/**
	 * ȡͳ�Ʊ����б�����
	 * 
	 * @return
	 */
	public List getReportList() {
		List li = null;
		try {
			getIniFile();
			List<String> l = ini.getSectionList();

			if (l != null && l.size() > 0) {
				Map<String, String> ret = null;
				li = new ArrayList();
				String r = null;
				for (int i = 0; i < l.size(); i++) {
					String[] a = new String[7];
					ret = ini.getSectionData((String) l.get(i));
					if (ret != null) 
					{
						a[4] = (String) l.get(i);// sections ��Ϊlistitem
						// id,����ɾ��,�޸ĵȲ���
						r = (String) ret.get("Title");
						a[0] = r;

						String title = ret.get("Title");
						a[1] = title;// ����

						a[2] = ret.get("Period");// ʱ���
						if (ret.get("Deny")!=null && ret.get("Deny").equals("Yes")) {// ״̬
							a[3] = "��ֹ";
						} else {
							a[3] = "����";
						}
						a[5] = ret.get("fileType");
						if (a[5] == null)
							a[5] = "html";
						a[6]=l.get(i);
						/*
						 * for (String key : ret.keySet()) {
						 * logger.info("        " + key + "= \"" +
						 * ret.get(key) + "\""); }
						 */

						li.add(a);
					}
				}
			}
			// l = ini.getKeyList("66591164");
			// logger.info(" IniFile keys: " + li);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// Debug.log("ȡͳ�Ʊ����б�������:"+e.getMessage());
		}
		return li;
	}

	/**
	 * �����ӱ��水ť���¼�,����ӱ���Ľ���
	 * 
	 * @throws Exception
	 */
	public void onAddReport() throws Exception {
		// HashMap pmap = new HashMap();
		// pmap.put("starttime", starttime.getValue());

		final Window win = (Window) Executions.createComponents(
				"/main/report/addreport.zul", null, null);
		win.setMaximizable(false);// ���������
		win.setAttribute("ReportDaoImpl", this);
		this.addreportwindow = win;
		try {
			win.doModal();
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "����", Messagebox.OK,
					Messagebox.ERROR);
		}

	}

	/**
	 * ������汨�水ť�¼�,ִ�б��汨������
	 */
	public void onSaveReport(Event event) {
		rd = (ReportDaoImpl) addreportwindow.getAttribute("ReportDaoImpl");
		getIniFile();
		long timeMillis = System.currentTimeMillis();
		String sections = String.valueOf(timeMillis);

		try {
			String title = Title.getValue().trim();
			if("".equals(title)){
				Messagebox.show("������ⲻ��Ϊ��,����������!", "��ʾ", Messagebox.OK,
						Messagebox.INFORMATION);
				Title.focus();
				return;
			}
			
			for(String section : ini.getSectionList()){
				String existName = ini.getValue(section, "Title");
				if(title.equals(existName)){
					Messagebox.show("���������Ѵ���,����������!", "��ʾ", Messagebox.OK,
							Messagebox.INFORMATION);
					Title.focus();
					return;
				}
			}
			String nodeids = getNodeids();
			if (nodeids == null) {
				Messagebox.show("��ѡ��������", "��ʾ", Messagebox.OK,
						Messagebox.INFORMATION);
				return;
			}
			String email = EmailSend.getValue();
			if (!"".equals(email)&&!BaseTools.validateEmail(email)) {
					Messagebox.show("E_Mail��ʽ����ȷ,����������!", "��ʾ", Messagebox.OK,
							Messagebox.INFORMATION);
					EmailSend.focus();
					return;				
			}
			ini.createSection(sections);
			ini.setKeyValue(sections, "creatTime", Toolkit.getToolkit().formatDate(new Date(timeMillis)));
			ini.setKeyValue(sections, "MonitorNumber",((SelectTree) monitortree).getSelectedIds().size());
			ini.setKeyValue(sections, "Title", Title.getValue()+"|"+sections);
			if(email!=null)	ini.setKeyValue(sections, "EmailSend", EmailSend.getValue());

			//ini.setKeyValue(sections, "Title", Title.getValue() + "|"		+ sections);
			
			String deny = Deny.isChecked()?"Yes":"No";
			ini.setKeyValue(sections, "Deny", deny);
			
			Listitem a = Period.getSelectedItem();
			
			Date tmStart=Begin_Date.getValue();
//			Date timeStart=Begin_Time.getValue();
			Date tmEnd=End_Date.getValue();
//			Date timeEnd=End_Time.getValue();
			String stringbuffer = null; 
			if(!"Other".equals(a.getValue().toString())){
				if(a==null)
					ini.setKeyValue(sections, "Period", "0");
				ini.setKeyValue(sections, "Period", a.getValue().toString());
			}else {
				if(a==null){
					ini.setKeyValue(sections, "Period", "0");
				}else {
	//				stringbuffer=a.getValue().toString()+"@timestart@"+tmStart+timeStart+"@timeend@"+tmEnd+timeEnd;
					stringbuffer=a.getValue().toString()+"@timestart@"+tmStart+"@timeend@"+tmEnd;
					ini.setKeyValue(sections, "Period", stringbuffer);
				}
			}
			if (Graphic.isChecked()) {
				ini.setKeyValue(sections, "Graphic", "Yes");
				a = ComboGraphic.getSelectedItem();
				ini.setKeyValue(sections, "ComboGraphic", a.getLabel());
			} else {
				ini.setKeyValue(sections, "Graphic", "No");
			}
			a= fileType.getSelectedItem();
			if(a==null)
			{
				a=fileType.getItemAtIndex(0);
			}
			ini.setKeyValue(sections, "fileType", a.getValue().toString());
			a = WeekEndTime.getSelectedItem();
			ini.setKeyValue(sections, "WeekEndTime", a.getValue().toString());
			if (ListDanger.isChecked()) {
				ini.setKeyValue(sections, "ListDanger", "Yes");
			} else {
				ini.setKeyValue(sections, "ListDanger", "No");
			}
			if (ListError.isChecked()) {
				ini.setKeyValue(sections, "ListError", "Yes");
			} else {
				ini.setKeyValue(sections, "ListError", "No");
			}
			ini.setKeyValue(sections, "ListAlert", "Yes");
			ini.setKeyValue(sections, "Descript", Descript.getValue());
			a = EndTime1.getSelectedItem();
			if (a == null) {
				a = EndTime1.getItemAtIndex(0);
			}
			Listitem b = EndTime2.getSelectedItem();
			ini.setKeyValue(sections, "EndTime", a.getLabel() + ":"
					+ b.getLabel());
			// ListClicket
			// StartTime
			// StatusResult
			// SumCheckItem
			// cksum
			a = Generate.getSelectedItem();
			if (a == null) {
				a = Generate.getItemAtIndex(0);
			}
			ini.setKeyValue(sections, "Generate", a.getLabel());
			if (Parameter.isChecked()) {
				ini.setKeyValue(sections, "Parameter", "Yes");
			} else {
				ini.setKeyValue(sections, "Parameter", "No");
			}

			ini.setKeyValue(sections, "GroupRight", nodeids);
			b = null;
			ini.saveChange();

			if (this.addreportwindow != null) {
				addreportwindow.setAttribute("saveOK", "true");
				this.addreportwindow.detach();
			}
			
			Executions.getCurrent().getDesktop().getSession().setAttribute("statistical_report_id", sections);
			View view = Toolkit.getToolkit().getSvdbView(
					event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			String minfo = loginname + " " + "��"
					+ OpObjectId.statistic_report.name + "�н�����  "
					+ OpTypeId.add.name + "������";
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add,
					OpObjectId.statistic_report);
		} catch (WrongValueException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ɾ�������б�,����ѡ�б����,�Ա������ɾ������
	 * 
	 * @throws InterruptedException
	 */
	public void onDeleteReport(Event event) throws InterruptedException {
		// String strPath = EccWebAppInit.getWebDir() + "\\main\\tuoplist\\";
		// String strName = "", strTmp = "";
		if (direportlistbox.getSelectedItems() == null
				|| direportlistbox.getSelectedCount() < 1) {
			try {
				Messagebox.show("��ѡ�񱨸�!", "��ʾ", Messagebox.OK,
						Messagebox.INFORMATION);
			} catch (InterruptedException e) {
			}
			return;
		}
		int ret = Messagebox.show("��ȷ��Ҫɾ��ѡ�еļ�¼��", "��ʾ", Messagebox.OK
				| Messagebox.CANCEL, Messagebox.QUESTION);
		if (ret == Messagebox.CANCEL)
			return;
		getIniFile();
		List<Listitem> tl = new ArrayList<Listitem>();
		for (Object o : direportlistbox.getSelectedItems()) {
			try {
				Listitem a = (Listitem) o;
				tl.add(a);
				ini.deleteSection(a.getId());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try {
			ini.saveChange();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Listitem l : tl) {
			direportlistbox.removeChild(l);
		}
		View view = Toolkit.getToolkit().getSvdbView(
				event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		String minfo = loginname + " " + "��" + OpObjectId.statistic_report.name
				+ "�н�����  " + OpTypeId.del.name + "������";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del,
				OpObjectId.statistic_report);
	}

	/**
	 * �޸ı���
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onModifyReport(Event event) throws Exception {
		String section = (String) event.getData();
		if (section != null) {
			getIniFile();
			HashMap pmap = new HashMap();
			pmap.put("THEREPORTINI", ini);
			pmap.put("THEREPORTSECTION", section);
			pmap.put("ReportDaoImpl", this);
			final Window win = (Window) Executions.createComponents(
					"/main/report/modifyreport.zul", null, pmap);
			win.setMaximizable(false);// ���������
			try {
				win.doModal();
			} catch (Exception e) {
				Messagebox.show(e.getMessage(), "����", Messagebox.OK,
						Messagebox.ERROR);
			}
		}
		View view = Toolkit.getToolkit().getSvdbView(
				event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		String minfo = loginname + " " + "��" + OpObjectId.statistic_report.name
				+ "�н�����  " + OpTypeId.edit.name + "������";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit,
				OpObjectId.statistic_report);
	}

	/**
	 * ������ֹ����
	 * 
	 * @param event
	 */
	public void onDenyeport(Event event) {
		if (direportlistbox.getSelectedItems() == null) {
			return;
		}
		getIniFile();
		List<Listitem> tl = new ArrayList<Listitem>();
		for (Object o : direportlistbox.getSelectedItems()) {
			try {
				Listitem a = (Listitem) o;
				ini.setKeyValue(a.getId(), "Deny", "Yes");

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		try {
			ini.saveChange();
		} catch (Exception e) {
			e.printStackTrace();
		}
		View view = Toolkit.getToolkit().getSvdbView(
				event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		String minfo = loginname + " " + "��" + OpObjectId.statistic_report.name
				+ "�н�����  " + OpTypeId.diable.name + "������";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.diable,
				OpObjectId.statistic_report);

		String targetUrl = "/main/report/statisticalreport.zul";
		eccBody = (Include) (this.desktop.getPage("eccmain")
				.getFellow("eccBody"));
		eccBody.setSrc(null);
		eccBody.setSrc(targetUrl);
	}

	/**
	 * ��������
	 * 
	 * @param event
	 */
	public void onAlloweport(Event event) {
		if (direportlistbox.getSelectedItems() == null) {
			return;
		}
		getIniFile();
		List<Listitem> tl = new ArrayList<Listitem>();
		for (Object o : direportlistbox.getSelectedItems()) {
			try {
				Listitem a = (Listitem) o;
				ini.setKeyValue(a.getId(), "Deny", "No");

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		try {
			ini.saveChange();
		} catch (Exception e) {
			e.printStackTrace();
		}
		View view = Toolkit.getToolkit().getSvdbView(
				event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		String minfo = loginname + " " + "��" + OpObjectId.statistic_report.name
				+ "�н�����  " + OpTypeId.enable.name + "������";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.enable,
				OpObjectId.statistic_report);

		String targetUrl = "/main/report/statisticalreport.zul";
		eccBody = (Include) (this.desktop.getPage("eccmain")
				.getFellow("eccBody"));
		eccBody.setSrc(null);
		eccBody.setSrc(targetUrl);
	}

	/**
	 * ˢ�±���
	 * 
	 * @param event
	 */
	public void onRefreshReport(Event event) {

		String targetUrl = "/main/report/statisticalreport.zul";
		eccBody = (Include) (this.desktop.getPage("eccmain")
				.getFellow("eccBody"));
		eccBody.setSrc(null);
		eccBody.setSrc(targetUrl);
		/*
		 * try { final Component componentresult = Executions.createComponents(
		 * targetUrl, page, null); } catch (Exception e) { e.printStackTrace();
		 * }
		 */
		/*
		 * try { Messagebox.show("���� onCreate �¼�����"); } catch
		 * (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
	}

	/**
	 * ��Ӧ�ֶ����ɱ��水ť�¼� �����ֶ����ɱ���
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCreateReport(Event event) throws Exception {
		Listitem selelist = direportlistbox.getSelectedItem(); // ��ȡ�û�ѡ�еı���
		if (selelist == null || !selelist.isSelected()) {
			try {
				Messagebox.show("��ѡ�񱨸�����!", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e) {
			}
		} else {
			
			String reportID=selelist.getId();
			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			
			Map<String,String> reportDefine=this.getIniFile().getSectionData(reportID);
			String Period=reportDefine.get("Period");
			Date tmStart=null;
			Date tmEnd=new Date();
			
			if(Period.equals("Month"))
				tmStart=Toolkit.getToolkit().delDay(new Date(), 30);
			else if(Period.equals("Week"))
				tmStart=Toolkit.getToolkit().delDay(new Date(), 7);
			else if(Period.equals("Day"))
				tmStart=Toolkit.getToolkit().delDay(new Date(), 1);
			else if(Period.equals("Other")){
				tmStart=Begin_Date.getValue();
				tmEnd=End_Date.getValue();
//				tmStart=Toolkit.getToolkit().delDay(new Date(), 1);
			}
			else
			{
				Toolkit.getToolkit().showError("�������䲻֧��:");
				return;
			}
			StatsReport createReport=new StatsReport(reportID,reportDefine,tmStart,tmEnd,view,Calendar.getInstance());
			ProgressWindow win = (ProgressWindow)page.getAttribute("progressWin");
			if(win==null)
			{
				win = (ProgressWindow)Executions.createComponents("/main/progress/index.zul", null, null);
				page.setAttribute("progressWin",win);
			}
			win.addProgress(createReport);
			win.setVisible(true);
			win.doModal();
			
			String minfo=loginname+" ��"+OpObjectId.statistic_report.name+"�н������ֶ����ɱ��������";
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.statistic_report);
		}
	}

	public void onCreateReportDEL(Event event) throws Exception {
		Listitem selelist = direportlistbox.getSelectedItem(); // ��ȡ�û�ѡ�еı���
		if (selelist == null || !selelist.isSelected()) {
			try {
				Messagebox.show("��ѡ�񱨸�����!", "��ʾ", Messagebox.OK,
						Messagebox.INFORMATION);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			Executions.getCurrent().getDesktop().getSession().setAttribute(
					"createReportSection", selelist.getId());
			final Window win = (Window) Executions.createComponents(
					"/main/report/createreport.zul", null, null);
			win.setMaximizable(false);
			win.setClosable(true);
			try {
				// win.doHighlighted();
				win.doModal();
			} catch (Exception e) {
				Messagebox.show(e.getMessage(), "����", Messagebox.OK,
						Messagebox.ERROR);
			}
		}
		View view = Toolkit.getToolkit().getSvdbView(
				event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		String minfo = loginname + " " + "��" + OpObjectId.statistic_report.name
				+ "�н�����  " + "�ֶ����ɱ��������";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.enable,
				OpObjectId.statistic_report);
	}

	/**
	 * �򵥼����Ϣ
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCreateSimpleInfo(Event event) throws Exception {
		Listitem selelist = direportlistbox.getSelectedItem(); // ��ȡ�û�ѡ�еı���
		if (selelist == null || !selelist.isSelected()) {
			try {
				Messagebox.show("��ѡ�񱨸�����!", "��ʾ", Messagebox.OK,
						Messagebox.INFORMATION);
				return;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			HashMap hmap = new HashMap();
			hmap.put("createSimpleInfoSection", selelist.getId());
			List clist = selelist.getChildren();
			if (clist != null) {
				Listcell lc = (Listcell) clist.get(1);
				requestScope.put("simpletitle", lc.getLabel());
			}
			getIniFile();
			String GroupRight = this.ini.getValue(selelist.getId(),
					"GroupRight");
			hmap.put("GroupRight", GroupRight);
			final Window win = (Window) Executions.createComponents(
					"/main/report/simplemonitorinfo.zul", null, hmap);
			// win.setMaximizable(true);
			// win.setMinimizable(true);
			try {
				win.doModal();
			} catch (SuspendNotAllowedException e) {
				Messagebox.show(e.getMessage(), "����", Messagebox.OK,
						Messagebox.ERROR);
			}
		}

	}

	/**
	 * ��������б���ʾ�����ɵı���,��ȷ���Ƿ���ʾ"�򵥼������Ϣ"��ť
	 * 
	 * @param event
	 */
	public void onLookReports(Event event) 
	{
		String reportId = (String) event.getData();
		String title = getIniFile().getValue(reportId, "Title");
		
		//��ֹ��Ϊ����ı�������ʱ����ͬ���������ļ��������ݶ�ʧ
		String createTime = getIniFile().getValue(reportId, "creatTime");
		Date createDate = new Date();
		try {
			createDate = Toolkit.getToolkit().parseDate(createTime);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String createTimeInMillis = createDate.getTime() + "";
		
		
		// ��ʾ����
		IniFile genIni = new IniFile("report."+reportId+".ini");
		try
		{
			genIni.load();
		}catch(Exception e)
		{
			return;
		}
		List<Listitem> r = new ArrayList<Listitem>();
		for (Object o : simplemonitorreportslistbox.getChildren()) {
			if(o instanceof Listitem)
				r.add((Listitem) o);
		}
		for (Listitem w : r) {
			simplemonitorreportslistbox.removeChild(w);
		}
		String timepart = null;
		simpleMonitorButton.setVisible(true);
		for (String creatKey : genIni.getSectionList()) 
		{
			String creator=genIni.getValue(creatKey, "creator");
			if(creator==null)
				continue;

			String fileType=genIni.getValue(creatKey, "fileType");
			
			Listitem li = new Listitem();
			li.setHeight("15px");
					// ��ʾ��ϸ��������
					
			li.setLabel("������:"+creator+",����ʱ��:"+Toolkit.getToolkit().formatDate(new Date(Long.parseLong(creatKey))));
			li.setImage("/main/images/filetype/"+fileType+".gif");
			li.setCheckable(true);
			reportwindow.setAttribute("fileType",fileType);
			reportwindow.setAttribute("title",title+Toolkit.getToolkit().formatDate(Long.parseLong(creatKey)));
			reportwindow.setAttribute("createTimeInMillis", createTimeInMillis);
			
			if(new File(StatsReport.getCreateFile(createTimeInMillis, creatKey,fileType)).exists())
				li.addForward("onClick", reportwindow,"onClickSimplemonitorreportslistbox", creatKey);
			else
				li.setStyle("color:gray");
			
			simplemonitorreportslistbox.appendChild(li);
		}
		simplemonitorreportslistbox.setCheckmark(true);
		simplemonitorreportslistbox.setFixedLayout(true);
		simplemonitorreportslistbox.setMultiple(true);
		simplemonitorreportslistbox.setMold("paging");
		simplemonitorreportslistbox.setPageSize(15);
		
	}

	/**
	 * ����Զ����ɵı����б��¼�,��simplemonitorreportsinclude����ʾ��ϸ�ı�������
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onClickSimplemonitorreportslistbox(Event event)
			throws Exception {
		String reportFileID = (String) event.getData();
		String fileType=event.getTarget().getAttribute("fileType").toString();
		String title=event.getTarget().getAttribute("title").toString();
		String createTimeInMillis = event.getTarget().getAttribute("createTimeInMillis").toString();
		
		String downLoadUrl=StatsReport.getDownLoadUrl(createTimeInMillis,reportFileID,fileType);
		if(downLoadUrl.equals(""))
		{
			Toolkit.getToolkit().showError("��Ч�ı�������!");
			return;
		}
		
		//AMedia(String name, String format, String ctype, URL url,	String charset)
		if(fileType.equals("html"))
		{
			final BlankWindow win = (BlankWindow) Executions.createComponents(
					"/main/progress/blankwin.zul", null, null);
			
			win.setTitle(title+"["+downLoadUrl+"]");
			win.setVisible(true);
			win.showUrl(downLoadUrl);
			win.doModal();
		}
		else if(fileType.equals("pdf")){
			File file = new File(StatsReport.getCreateFile(createTimeInMillis, reportFileID,fileType));
			if(file.exists())
				Filedownload.save(new AMedia(title+".pdf","pdf", "application/pdf",new FileInputStream(file)));
		}
		else if(fileType.equals("xls")){
			File file = new File(StatsReport.getCreateFile(createTimeInMillis, reportFileID,fileType));
			if(file.exists())
				Filedownload.save(new AMedia(title+".xls","xls", "application/vnd.ms-excel",new FileInputStream(file)));
		
		}
	}

	public void onCreate$simplemonitorreportwindow(Event event) {
		Object obj = simplemonitorreportwindow.getAttribute("url");
		if (obj != null) {
			String url = obj.toString();
			simplemonitorreportsinclude.setSrc(url);
		}
	}

	/**
	 * ���ɱ������,���»�ȡ���ݰ�ť�¼�
	 * 
	 * @param event
	 */
	public void onRegetdata(Event event) {
		Iframe createreportIframe = (Iframe) createReportWindow
				.getFellow("createreportIframe");
		Div d = (Div) createReportWindow.getFellow("p1").getFellow("pc1")
				.getFellow("d1");
		Datebox createBegin_Date = (Datebox) d.getFellow("createBegin_Date");
		Datebox createEnd_Date = (Datebox) d.getFellow("createEnd_Date");
		Timebox createBegin_Time = (Timebox) d.getFellow("createBegin_Time");
		Timebox createEnd_Time = (Timebox) d.getFellow("createEnd_Time");
		String para = "?cbd=" + createBegin_Date.getText() + " "
				+ createBegin_Time.getText() + "&ced="
				+ createEnd_Date.getText() + " " + createEnd_Time.getText();
		String src = "/main/report/createreportiframesrc.zul" + para;
		createreportIframe.setSrc(src);
	}

	/**
	 * ʱ�ζԱȱ������,��ѯ��ť�¼�
	 * 
	 * @param event
	 */
	public void onPeriodofQueryReport(Event event) {
		String nodeid = (String) Executions.getCurrent().getDesktop()
				.getSession().getAttribute("myREPORTNODEID");

		try {
			// Messagebox.show(nodeid);
			if (nodeid == null || nodeid.equals("")) {
				Messagebox.show("��û��ѡ������!", "��ʾ", Messagebox.OK,
						Messagebox.INFORMATION);
				return;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = "/main/report/periodoftimereportiframesrc.zul?starttime="
				+ starttime.getValue() + "&endtime=" + endtime.getValue()
				+ "&comparetype=" + comparetype.getSelectedItem().getValue()
				+ "&nodeid=" + nodeid;
		PeriodofQueryReportIframe.setVisible(true);
		PeriodofQueryReportIframe.setSrc(url);

	}

	public void onCheckThisBox(Event event) {
		if (Parameter.isChecked()) {
			try {
				Messagebox.show("ѡ���������ɱ�������н�ռ�ô�����ϵͳ��Դ�����ܵ���ϵͳ����!", "��ʾ",
						Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * �������水ť�¼�
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onExportReport(Event event) throws Exception {
		String nodeid = (String) Executions.getCurrent().getDesktop()
				.getSession().getAttribute("myREPORTNODEID");
		try {
			if (nodeid == null || nodeid.equals("")) {
				Messagebox.show("��ѡ��������", "��ʾ", Messagebox.OK,
						Messagebox.INFORMATION);
				return;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		HashMap pmap = new HashMap();
		pmap.put("starttime", starttime.getValue());
		final Window win = (Window) Executions.createComponents(
				"/main/report/exportreport/exportreport.zul", null, pmap);
		// �������
		Executions.getCurrent().getDesktop().getSession().setAttribute(
				"starttime", starttime.getValue().toGMTString());
		Executions.getCurrent().getDesktop().getSession().setAttribute(
				"endtime", endtime.getValue().toGMTString());
		Executions.getCurrent().getDesktop().getSession().setAttribute(
				"comparetype", this.comparetype.getSelectedItem().getValue());
		// win.setMaximizable(true);

		try {
			// win.doHighlighted();
			win.doModal();
		} catch (SuspendNotAllowedException e) {
			Messagebox.show(e.getMessage(), "����", Messagebox.OK,
					Messagebox.ERROR);
		}
	}

	public void onSelectGroupName() {
		String av = (String) viewNamecombobox.getSelectedItem().getValue();
		((SelectTree) monitortree).setViewName(av);
	}

	/**
	 * ��ȡѡ��ļ�����ڵ�
	 * 
	 * @return
	 */
	public String getNodeids() {
		String nodeids = null;
		try {
			SelectTree sTree = (SelectTree) monitortree;
			nodeids = sTree.getAllSelectedIds();
		} catch (NullPointerException e) {

		}
		return nodeids;
	}

}
