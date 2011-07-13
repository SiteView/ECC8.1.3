/**
 * ecc:上午11:14:42
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
 * 报表部分DAO,功能与REPORTTABLEDAOIMPL.cs对应,相应说明参见REPORTTABLEDAOIMPL.cs
 * 
 * @author : di.tang
 * @date: 2009-3-19
 * @company: siteview
 */
public class ReportDaoImpl extends GenericAutowireComposer {
	Panel simplemonitorreports; // 简单监测器时间段
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
	Checkbox GroupRight; // = ",1.9.5.1,"//FIXME 未在树上标示出来,
	Listbox WeekEndTime;
	Checkbox ListDanger;
	// FIXME SumCheckItem= "Ping,|包成功率(%),数据往返时间(ms),状态值(200表示成功 300表示出错),"
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
	
	//新加报表 。。。
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
	 * 取统计报表列表数据
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
						a[4] = (String) l.get(i);// sections 作为listitem
						// id,用于删除,修改等操作
						r = (String) ret.get("Title");
						a[0] = r;

						String title = ret.get("Title");
						a[1] = title;// 标题

						a[2] = ret.get("Period");// 时间段
						if (ret.get("Deny")!=null && ret.get("Deny").equals("Yes")) {// 状态
							a[3] = "禁止";
						} else {
							a[3] = "允许";
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
			// Debug.log("取统计报告列表发生错误:"+e.getMessage());
		}
		return li;
	}

	/**
	 * 点击添加报告按钮的事件,打开添加报告的界面
	 * 
	 * @throws Exception
	 */
	public void onAddReport() throws Exception {
		// HashMap pmap = new HashMap();
		// pmap.put("starttime", starttime.getValue());

		final Window win = (Window) Executions.createComponents(
				"/main/report/addreport.zul", null, null);
		win.setMaximizable(false);// 不允许最大化
		win.setAttribute("ReportDaoImpl", this);
		this.addreportwindow = win;
		try {
			win.doModal();
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
					Messagebox.ERROR);
		}

	}

	/**
	 * 点击保存报告按钮事件,执行保存报告任务
	 */
	public void onSaveReport(Event event) {
		rd = (ReportDaoImpl) addreportwindow.getAttribute("ReportDaoImpl");
		getIniFile();
		long timeMillis = System.currentTimeMillis();
		String sections = String.valueOf(timeMillis);

		try {
			String title = Title.getValue().trim();
			if("".equals(title)){
				Messagebox.show("报告标题不能为空,请重新输入!", "提示", Messagebox.OK,
						Messagebox.INFORMATION);
				Title.focus();
				return;
			}
			
			for(String section : ini.getSectionList()){
				String existName = ini.getValue(section, "Title");
				if(title.equals(existName)){
					Messagebox.show("报告名称已存在,请重新输入!", "提示", Messagebox.OK,
							Messagebox.INFORMATION);
					Title.focus();
					return;
				}
			}
			String nodeids = getNodeids();
			if (nodeids == null) {
				Messagebox.show("请选择监测器！", "提示", Messagebox.OK,
						Messagebox.INFORMATION);
				return;
			}
			String email = EmailSend.getValue();
			if (!"".equals(email)&&!BaseTools.validateEmail(email)) {
					Messagebox.show("E_Mail格式不正确,请重新输入!", "提示", Messagebox.OK,
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
			String minfo = loginname + " " + "在"
					+ OpObjectId.statistic_report.name + "中进行了  "
					+ OpTypeId.add.name + "操作。";
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add,
					OpObjectId.statistic_report);
		} catch (WrongValueException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 删除报告列表,用于选中报告后,对报告进行删除操作
	 * 
	 * @throws InterruptedException
	 */
	public void onDeleteReport(Event event) throws InterruptedException {
		// String strPath = EccWebAppInit.getWebDir() + "\\main\\tuoplist\\";
		// String strName = "", strTmp = "";
		if (direportlistbox.getSelectedItems() == null
				|| direportlistbox.getSelectedCount() < 1) {
			try {
				Messagebox.show("请选择报告!", "提示", Messagebox.OK,
						Messagebox.INFORMATION);
			} catch (InterruptedException e) {
			}
			return;
		}
		int ret = Messagebox.show("你确认要删除选中的记录吗？", "提示", Messagebox.OK
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
		String minfo = loginname + " " + "在" + OpObjectId.statistic_report.name
				+ "中进行了  " + OpTypeId.del.name + "操作。";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del,
				OpObjectId.statistic_report);
	}

	/**
	 * 修改报表
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
			win.setMaximizable(false);// 不允许最大化
			try {
				win.doModal();
			} catch (Exception e) {
				Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
						Messagebox.ERROR);
			}
		}
		View view = Toolkit.getToolkit().getSvdbView(
				event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		String minfo = loginname + " " + "在" + OpObjectId.statistic_report.name
				+ "中进行了  " + OpTypeId.edit.name + "操作。";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit,
				OpObjectId.statistic_report);
	}

	/**
	 * 批量禁止报告
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
		String minfo = loginname + " " + "在" + OpObjectId.statistic_report.name
				+ "中进行了  " + OpTypeId.diable.name + "操作。";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.diable,
				OpObjectId.statistic_report);

		String targetUrl = "/main/report/statisticalreport.zul";
		eccBody = (Include) (this.desktop.getPage("eccmain")
				.getFellow("eccBody"));
		eccBody.setSrc(null);
		eccBody.setSrc(targetUrl);
	}

	/**
	 * 批量允许
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
		String minfo = loginname + " " + "在" + OpObjectId.statistic_report.name
				+ "中进行了  " + OpTypeId.enable.name + "操作。";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.enable,
				OpObjectId.statistic_report);

		String targetUrl = "/main/report/statisticalreport.zul";
		eccBody = (Include) (this.desktop.getPage("eccmain")
				.getFellow("eccBody"));
		eccBody.setSrc(null);
		eccBody.setSrc(targetUrl);
	}

	/**
	 * 刷新报表
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
		 * try { Messagebox.show("窗口 onCreate 事件触发"); } catch
		 * (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
	}

	/**
	 * 对应手动生成报告按钮事件 用于手动生成报告
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCreateReport(Event event) throws Exception {
		Listitem selelist = direportlistbox.getSelectedItem(); // 获取用户选中的报告
		if (selelist == null || !selelist.isSelected()) {
			try {
				Messagebox.show("请选择报告名称!", "提示", Messagebox.OK, Messagebox.INFORMATION);
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
				Toolkit.getToolkit().showError("报告区间不支持:");
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
			
			String minfo=loginname+" 在"+OpObjectId.statistic_report.name+"中进行了手动生成报告操作。";
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.statistic_report);
		}
	}

	public void onCreateReportDEL(Event event) throws Exception {
		Listitem selelist = direportlistbox.getSelectedItem(); // 获取用户选中的报告
		if (selelist == null || !selelist.isSelected()) {
			try {
				Messagebox.show("请选择报告名称!", "提示", Messagebox.OK,
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
				Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
						Messagebox.ERROR);
			}
		}
		View view = Toolkit.getToolkit().getSvdbView(
				event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		String minfo = loginname + " " + "在" + OpObjectId.statistic_report.name
				+ "中进行了  " + "手动生成报表操作。";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.enable,
				OpObjectId.statistic_report);
	}

	/**
	 * 简单监测信息
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onCreateSimpleInfo(Event event) throws Exception {
		Listitem selelist = direportlistbox.getSelectedItem(); // 获取用户选中的报告
		if (selelist == null || !selelist.isSelected()) {
			try {
				Messagebox.show("请选择报告名称!", "提示", Messagebox.OK,
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
				Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
						Messagebox.ERROR);
			}
		}

	}

	/**
	 * 点击报告列表显示已生成的报告,并确定是否显示"简单监测器信息"按钮
	 * 
	 * @param event
	 */
	public void onLookReports(Event event) 
	{
		String reportId = (String) event.getData();
		String title = getIniFile().getValue(reportId, "Title");
		
		//防止因为定义的报告生成时间相同，而覆盖文件导致数据丢失
		String createTime = getIniFile().getValue(reportId, "creatTime");
		Date createDate = new Date();
		try {
			createDate = Toolkit.getToolkit().parseDate(createTime);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String createTimeInMillis = createDate.getTime() + "";
		
		
		// 显示报告
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
					// 显示详细报告内容
					
			li.setLabel("创建人:"+creator+",创建时间:"+Toolkit.getToolkit().formatDate(new Date(Long.parseLong(creatKey))));
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
	 * 点击自动生成的报告列表事件,在simplemonitorreportsinclude中显示详细的报告内容
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
			Toolkit.getToolkit().showError("无效的报表联结!");
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
	 * 生成报表界面,重新获取数据按钮事件
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
	 * 时段对比报告界面,查询按钮事件
	 * 
	 * @param event
	 */
	public void onPeriodofQueryReport(Event event) {
		String nodeid = (String) Executions.getCurrent().getDesktop()
				.getSession().getAttribute("myREPORTNODEID");

		try {
			// Messagebox.show(nodeid);
			if (nodeid == null || nodeid.equals("")) {
				Messagebox.show("您没有选择监测器!", "提示", Messagebox.OK,
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
				Messagebox.show("选择此项后，生成报告过程中将占用大量的系统资源，可能导致系统错误!", "提示",
						Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 导出报告按钮事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onExportReport(Event event) throws Exception {
		String nodeid = (String) Executions.getCurrent().getDesktop()
				.getSession().getAttribute("myREPORTNODEID");
		try {
			if (nodeid == null || nodeid.equals("")) {
				Messagebox.show("请选择监测器！", "提示", Messagebox.OK,
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
		// 保存参数
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
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
					Messagebox.ERROR);
		}
	}

	public void onSelectGroupName() {
		String av = (String) viewNamecombobox.getSelectedItem().getValue();
		((SelectTree) monitortree).setViewName(av);
	}

	/**
	 * 获取选择的监测器节点
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
