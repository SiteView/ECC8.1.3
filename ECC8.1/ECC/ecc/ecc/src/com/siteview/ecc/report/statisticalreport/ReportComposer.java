package com.siteview.ecc.report.statisticalreport;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.crypto.Data;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.ComposerExt;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;
import org.zkoss.zul.event.PagingEvent;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.UserRightId;
import com.siteview.base.manage.View;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.progress.ProgressWindow;
import com.siteview.ecc.report.common.SelectableListheader;
import com.siteview.ecc.reportserver.StatsReport;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svdb.SvdbItemComparator;

public class ReportComposer extends GenericForwardComposer implements
ComposerExt ,ListitemRenderer {
	private final static Logger logger = Logger.getLogger(ReportComposer.class);
	Listbox direportlistbox;
	Listbox genlistbox;	
	IniFile reportIni;
	Combobox genlistboxPageSize;
	Button btnBatchDeleteGen;
	Button addStatisticButton;	//添加报告按钮
	Button deleStatisticButton;	//删除报告按钮
	
	boolean editFlag = true;

	
	HashMap<String,IniFile> reportGenIniMap=new HashMap<String,IniFile>();
	
	public void onInit() throws Exception{
		try{
			View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
			if(!view.isAdmin()){//非管理员用户
				IniFile userIniFile = view.getUserIni();
				String addTopN_str = userIniFile.getValue(userIniFile.getSections(), UserRightId.DoStatisticReportlistAdd);
				String deleteTopN_str = userIniFile.getValue(userIniFile.getSections(), UserRightId.DoStatisticReportlistDel);
				String editTopN_str = userIniFile.getValue(userIniFile.getSections(), UserRightId.DoStatisticReportlistEdit);
				if(!"1".equals(addTopN_str)){
					addStatisticButton.setDisabled(true);
				}
				if(!"1".equals(deleteTopN_str)){
					deleStatisticButton.setDisabled(true);
				}
				if(!"1".equals(editTopN_str)){
					editFlag = false;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void createPageSizeSelect()
	{
		String pgsz=Toolkit.getToolkit().getCookie("genlistboxPageSize");
		if(pgsz==null)
			pgsz="10";
		genlistboxPageSize.setValue(pgsz);
		
		genlistboxPageSize.addEventListener("onChange",  new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception 
			{
				
				String pgsz = ((Combobox) event.getTarget()).getValue();
				try {
						int pageSize=Math.abs(Integer.parseInt(pgsz));
						Toolkit.getToolkit().setCookie("genlistboxPageSize",pgsz,Integer.MAX_VALUE);
						genlistbox.setPageSize(pageSize);
						genlistbox.getPaginal().setPageSize(pageSize);
						((Combobox)event.getTarget()).setValue(String.valueOf(pageSize));
				} catch (Exception e) {
					event.stopPropagation();
				}

			}
		});
	}
	@Override
	public void doAfterCompose(Component comp) throws Exception {

		super.doAfterCompose(comp);
		self.setAttribute("Composer", this);
		direportlistbox.getPagingChild().setMold("os");
		direportlistbox.addEventListener("onPaging", getListBoxPagingListener());
		createDireportlistboxHeader();
		createGenlistboxHeader();

		refresh();
		
		createPageSizeSelect();
		
		btnBatchDeleteGen.addEventListener("onClick", new EventListener(){

			@Override
			public void onEvent(Event event) throws Exception {
				
				if(genlistbox.getSelectedCount()<=0){
					try {
						Messagebox.show("请选择报告!", "提示", Messagebox.OK, Messagebox.INFORMATION);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return;
				}
				
				try
				{
					int ret = Messagebox.show("你确认要删除选中的报告日志记录吗？", "提示", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION);
					
					if (ret == Messagebox.CANCEL)
						return;
				}catch(Exception e)
				{
					Toolkit.getToolkit().showError(e.getMessage());
				}
				
				ReportItem reportItem=null;
				Iterator iterator=genlistbox.getSelectedItems().iterator();
				
				
				while(iterator.hasNext())
				{
					ReportGenItem reportGenItem=(ReportGenItem)((Listitem)iterator.next()).getAttribute("reportGenItem");
					reportItem=reportGenItem.getReportItem();
					
					//防止因为定义的报告生成时间相同，而覆盖文件导致数据丢失
					String createTime = reportItem.getCreatTime();
					Date createDate = Toolkit.getToolkit().parseDate(createTime);
					String createTimeInMillis = createDate.getTime() + "";
					
					deleteReportFile(createTimeInMillis, reportGenItem.getGenID(), reportGenItem.getFileType());
					IniFile reportGenIni=getReportGenIni(reportItem.getReportID());
					if(!reportGenIni.getSectionList().contains(reportGenItem.getGenID())) continue; 
					reportGenIni.deleteSection(reportGenItem.getGenID());
					reportGenIni.saveChange();
				}
				if(reportItem!=null)
				{
					refreshGenlistbox(reportItem);
				}
				
				
			}});
		SelectableListheader.addPopupmenu(direportlistbox);
		SelectableListheader.addPopupmenu(genlistbox);
	}
	public void refresh()
	{
		this.reportIni=null;/*refresh*/
		ReportModel modelAndRender=new ReportModel(getReportIni());
		direportlistbox.setModel(modelAndRender);
		direportlistbox.setItemRenderer(this);
		
		if(modelAndRender.getSize()>0){
			String reportId = (String)Executions.getCurrent().getDesktop().getSession().getAttribute("statistical_report_id");
			
			if(reportId==null){
				return;
			}
			ReportItem ritem = new ReportItem(reportId,reportIni.getFmap().get(reportId));
			ritem = ritem==null?(ReportItem)modelAndRender.get(0):ritem;
			
			refreshGenlistbox(ritem);
		}
		else 
		{
			List l = genlistbox.getItems();
			if(l!=null) l.clear();
		}
		
	}
	/* 分页事件 */
	public EventListener getListBoxPagingListener() {
		EventListener l = new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {

				PagingEvent e = (PagingEvent) event;
				Listbox listbox = (Listbox) e.getTarget();

					int pageIdx = e.getActivePage();
					int idx = pageIdx * listbox.getPageSize();
					ReportItem item = (ReportItem) listbox.getModel().getElementAt(idx);
					refreshGenlistbox(item);
					
			}
		};
		return l;
	}
	public void refreshGenlistbox(ReportItem item)
	{
		boolean find=false;
		for (int i = 0; i < direportlistbox.getModel().getSize(); i++) 
		{
			if (item.equals(direportlistbox.getModel().getElementAt(i))) {
				direportlistbox.setSelectedIndex(i);
				ReportGenModel reportGenModel=new ReportGenModel(this,item);
				genlistbox.setItemRenderer(reportGenModel);
				genlistbox.setModel(reportGenModel);
				find=true;
				break;
			}
		}
		
		if(!find)
			{
				ReportGenModel reportGenModel=(ReportGenModel)genlistbox.getModel();
				if(reportGenModel!=null)
					reportGenModel.clear();
			}
	}
	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent,
			ComponentInfo compInfo) {
		return compInfo;
	}
	public void createDireportlistboxHeader()
	{
		
		Listhead listhead=new Listhead();
		//listhead.setSizable(true);
		makeListheader("Title","名称","left","200px",false,true).setParent(listhead);
		makeListheader("Descript","描述","left","",false,true).setParent(listhead);
		makeListheader("MonitorNumber","监测器数","left","60px",false,true).setParent(listhead);
		makeListheader("Period","时间段","center","80px",false,true).setParent(listhead);
		makeListheader("creatTime","定义时间","center","120px",false,true).setParent(listhead);
		makeListheader("fileType","格式","center","60px",false,true).setParent(listhead);
		makeListheader("Deny","状态","center","60px",false,true).setParent(listhead);
		makeListheader("edit","编辑","center","80px",false,true).setParent(listhead);
		
		listhead.setParent(direportlistbox);
	}
	public void createGenlistboxHeader()
	{
		Listhead listhead=new Listhead();
		//listhead.setSizable(true);
		makeListheader("fileType","格式","center","80px",false,true).setParent(listhead);
		makeListheader("title","标题","left","",false,true).setParent(listhead);
		makeListheader("Period","时间段","center","60px",false,true).setParent(listhead);
		makeListheader("genID","生成日期","center","120px",false,true).setParent(listhead);
		makeListheader("creator","创建者","center","100px",false,true).setParent(listhead);
		makeListheader("valid","有效性","center","80px",false,true).setParent(listhead);
		makeListheader("operate","操作","center","80px",false,true).setParent(listhead);
		
		listhead.setParent(genlistbox);	
	}
	private Listheader makeListheader(String propKey,String label,String align,String width,boolean isNumber,boolean ascending)
	{
		Listheader listheader=new Listheader(label);
		listheader.setAlign(align);
		listheader.setWidth(width);
		if(propKey!=null)
		{
			listheader.setSortAscending(new SvdbItemComparator(propKey, isNumber, ascending));
			listheader.setSortDescending(new SvdbItemComparator(propKey, isNumber,!ascending));
		}else
			listheader.setSort(null);
		return listheader;
	}
	public void onAddReport() {
		final Window win = (Window) Executions.createComponents(
				"/main/report/addreport.zul", null, null);
			win.setMaximizable(false);// 不允许最大化
		try {
			win.doModal();
			if(win.getAttribute("saveOK")!=null)
				refresh();
		} catch (Exception e) {
			Toolkit.getToolkit().showError(e.getMessage());
		}
	}
	
	/*
	 * 1244550912926.html_files
	 * 1244550912926.html
	 */
	public void deleteReportFile(String createTimeInMillis, String genid,String fileType)
	{
		
		String pathFile=StatsReport.getCreateFile(createTimeInMillis, genid, fileType);
		File file=new File(pathFile);
		if(file.exists())
		{
			file.delete();
		}
		if(fileType.equals("html"))
		{
			file=new File(pathFile+"_files");//目录下有文件不能删除
			Toolkit.getToolkit().deleteFolder(pathFile+"_files");
//			if(file.exists())
//			{
//				file.delete();//
//			}
		}
		
	}
	/*删除报告定义*/
	public void onDeleteReport(Event event) {

		View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
		if(direportlistbox.getSelectedCount()<=0){
			try {
				Messagebox.show("请选择报告!", "提示", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}

		try
		{
			int ret = Messagebox.show("你确认要删除选中的"+direportlistbox.getSelectedCount()+"条记录吗,相关生成报告记录也将被删除？", "提示", Messagebox.OK
					| Messagebox.CANCEL, Messagebox.QUESTION);
			
			if (ret == Messagebox.CANCEL)
				return;
		}catch(Exception e)
		{
			Toolkit.getToolkit().showError(e.getMessage());
		}
			
		try {
			for(Object o:direportlistbox.getSelectedItems().toArray())
			{		
				ReportItem reportItem =(ReportItem)((Listitem) o).getAttribute("reportItem");
				getReportIni().deleteSection(reportItem.getReportID());
				
				//防止因为定义的报告生成时间相同，而覆盖文件导致数据丢失
				String createTime = reportItem.getCreatTime();
				Date createDate = Toolkit.getToolkit().parseDate(createTime);
				String createTimeInMillis = createDate.getTime() + "";
				
				IniFile reportGemIni=getReportGenIni(reportItem.getReportID());
				if(reportGemIni!=null)
				{
					for(String genID:reportGemIni.getSectionList())
					{	
						if(genID.equals("TempSection(Please_modify_it)")) continue;
						String fileType=reportGemIni.getValue(genID,"fileType");
						deleteReportFile(createTimeInMillis, genID,fileType);
					}
				}
			}
			getReportIni().saveChange();
			refresh();
			
		} catch (Exception e) {
			Toolkit.getToolkit().showOK(e.getMessage());
			e.printStackTrace();
			return;
		}

		String loginname = view.getLoginName();
		String minfo = loginname + " " + "在" + OpObjectId.statistic_report.name
				+ "中进行了  " + OpTypeId.del.name + "操作。";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del,
				OpObjectId.statistic_report);
	}
	public IniFile getReportIni()
	{
		if(reportIni==null)
		{
			reportIni=new IniFile("reportset.ini");
			try{reportIni.load();}
			catch(Exception e){
			};
		}
		if(reportIni!=null&&reportIni.getFmap()==null)
		{
			try{reportIni.load();}
			catch(Exception e){e.printStackTrace();};
		}
		return reportIni;
	}
	public IniFile getReportGenIni(String reportID)
	{
		IniFile reportGenIni=reportGenIniMap.get(reportID);
		if(reportGenIni==null)
		{
			reportGenIni= new IniFile("report."+reportID+".ini");
			try{
				reportGenIni.load();
				reportGenIniMap.put(reportID,reportGenIni);
				}
			catch(Exception e){
				logger.info("没有文件"+"report."+reportID+".ini");
			};

			
		}
		if(reportGenIni.getFmap()==null)
		{
			try{
				reportGenIni.load();
			}
			catch(Exception e){
//				e.printStackTrace();
			};
		}
		return reportGenIni;
	}

	/**
	 * 允许
	 * 
	 * @param event
	 */
	public void onAlloweport(Event event) 
	{
		if(direportlistbox.getSelectedItem()==null){
			try {
				Messagebox.show("请选择报告!", "提示", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		
		try 
			{
				for(Object o:direportlistbox.getSelectedItems().toArray())
				{
					ReportItem reportItem =(ReportItem)((Listitem) o).getAttribute("reportItem");
					getReportIni().setKeyValue(reportItem.getReportID(), "Deny", "No");
					getReportIni().saveChange();
				}
				refresh();
			} catch (Exception e) {
				Toolkit.getToolkit().showError(e.getMessage());
				return;
			}

		View view = Toolkit.getToolkit().getSvdbView(
				event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		String minfo = loginname + " " + "在" + OpObjectId.statistic_report.name
				+ "中进行了  " + OpTypeId.enable.name + "操作。";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.enable,
				OpObjectId.statistic_report);

	}
	
	/**
	 * 禁止报告
	 * 
	 * @param event
	 */
	public void onDenyeport(Event event) {
		if(direportlistbox.getSelectedItem()==null){
			try {
				Messagebox.show("请选择报告!", "提示", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}

			try 
			{
				for(Object o:direportlistbox.getSelectedItems().toArray())
				{
					ReportItem reportItem =(ReportItem)((Listitem) o).getAttribute("reportItem");	
					getReportIni().setKeyValue(reportItem.getReportID(), "Deny", "Yes");
					getReportIni().saveChange();
				}
				refresh();

			} catch (Exception e) {
				Toolkit.getToolkit().showError(e.getMessage());
				return;
			}

		View view = Toolkit.getToolkit().getSvdbView(
				event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		String minfo = loginname + " " + "在" + OpObjectId.statistic_report.name
				+ "中进行了  " + OpTypeId.diable.name + "操作。";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.diable,
				OpObjectId.statistic_report);

	}
	public void onRefreshReport(Event event) {
		refresh();
	}
	
	/*生成报告*/
	public void onCreateReport(Event event) throws Exception {
		if(direportlistbox.getSelectedItem()==null){
			try {
				Messagebox.show("请选择报告!", "提示", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		ReportItem reportItem =(ReportItem)((Listitem) direportlistbox.getSelectedItem()).getAttribute("reportItem");
			
		View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
		String loginname = view.getLoginName();
			
			Map<String,String> reportDefine=this.getReportIni().getSectionData(reportItem.getReportID());
			String Period=reportDefine.get("Period");
			Date tmStart=null;
			Date tmEnd=new Date();
			
			if(Period.equals("Month"))
				tmStart=Toolkit.getToolkit().delDay(new Date(), 30);
			else if(Period.equals("Week"))
				tmStart=Toolkit.getToolkit().delDay(new Date(), 7);
			else if(Period.equals("Day"))
				tmStart=Toolkit.getToolkit().delDay(new Date(), 1);
			else if(Period.split("Other").length>0){
				String [] datas=Period.split("@timestart@");
				if(datas.length>0){
					String[] data=datas[1].split("@timeend@");
					if(data.length>0){
//						SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//						tmStart=sdf.parse(data[0]);
//						tmEnd=sdf.parse(data[1]);
//						System.out.println(tmStart + " ---> "+tmEnd);
						
						tmStart=new Date(data[0]);
						tmEnd=new Date(data[1]);
						System.out.println(tmStart  + " new Date ---> "+tmEnd);

					}
				}

//				tmStart=Begin_Date.getValue();
//				tmEnd=End_Date.getValue();
//				tmStart=Toolkit.getToolkit().delDay(new Date(), 1);
			}
			else
			{
				Toolkit.getToolkit().showError("报告区间不支持:");
				return;
			}
			String [] time=(new SimpleDateFormat("yyyy/MM/dd").format(tmEnd)).toString().split("/");
			StringBuffer stringbuffer=new StringBuffer();
			Executions.getCurrent().getDesktop().getSession().setAttribute("STATETIMES",stringbuffer.append(new SimpleDateFormat("yyyy/MM/dd").format(tmStart)).append("-").append(new SimpleDateFormat("yyyy/MM/dd").format(tmEnd)).toString());
			StatsReport createReport=new StatsReport(reportItem.getReportID(),reportDefine,tmStart,tmEnd,view,Calendar.getInstance());
			//StatsReport 是一个实现了  IEccProgressmeter接口的类 //run()方法
			ProgressWindow win = (ProgressWindow)page.getAttribute("progressWin");
			if(win==null)
			{
				win = (ProgressWindow)Executions.createComponents("/main/progress/index.zul", null, null);
				page.setAttribute("progressWin",win);
			}
			String minfo=loginname+" 在"+OpObjectId.statistic_report.name+"中进行了手动生成报告操作。";
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.statistic_report);

			win.addProgress(createReport);//win增加 addProgress 
			win.setVisible(true);
			win.doModal();
			
			try
			{
				this.reportGenIniMap.remove(reportItem.getReportID());/*refresh*/
				refreshGenlistbox(reportItem);
			}catch(Exception e){}
			
			
	}
	
	/**
	 * 修改报表
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onModifyReport(Event event) throws Exception {
		
		// 不要自己操作 user.ini, 基于下面的类做 “用户管理”
		// 使用 view.getAllUserEdit(), createUserEdit,deleteUserEdit 以及
		// com.siteview.base.data.UserEdit 中的方法-->
		View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());

		if(editFlag){
			ReportItem reportItem = (ReportItem) event.getTarget().getAttribute("reportItem");

			HashMap pmap = new HashMap();
			pmap.put("THEREPORTINI", getReportIni());
			pmap.put("reportItem", reportItem);

			final Window win = (Window) Executions.createComponents("/main/report/modifyreport.zul", null, pmap);
			win.setMaximizable(false);// 不允许最大化
			try {
				win.doModal();
				if(win.getAttribute("saveOK")!=null)
					refresh();
			} catch (Exception e) {
				Toolkit.getToolkit().showError(e.getMessage());
				return;
			}
			String loginname = view.getLoginName();
			String minfo = loginname + " " + "在" + OpObjectId.statistic_report.name
					+ "中进行了  " + OpTypeId.edit.name + "操作。";
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit,
					OpObjectId.statistic_report);
		}else
		{
			try{
				Messagebox.show("用户:"+view.getLoginName()+" 没有  编辑统计报告  的权限!", "提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}catch(Exception e){}
		}

	}
	public void refreshGenData(Listbox genlistbox,ReportItem item)
	{
		ReportGenModel reportGenModel=new ReportGenModel(this,item);
		
		
			//Listhead head=new Listhead();
			//head.setParent(listbox);
			
			//createListHeader(head);
			
			int pageSize = Integer.parseInt(genlistboxPageSize.getValue());
			String pgzs = Toolkit.getToolkit()
				.getCookie("detailListPageSize");
			if (pgzs != null)
			pageSize = Integer.parseInt(pgzs);

			genlistbox.setMold("paging");
			genlistbox.getPagingChild().setMold("os");
			genlistbox.getPaginal().setPageSize(pageSize);
			genlistbox.setPageSize(pageSize);

			

			genlistbox.setStyle("overflow-x:hidden;border:none");
			
			
		//listbox.addEventListener("onSelect", actionMenuDiv);
		//listbox.addEventListener("onSelect", getSouthListener());
		//listbox.addEventListener("onPaging", getListBoxPagingListener());
			//listbox.setFixedLayout(true);
			//listbox.setVflex(false);/* 出滚动条 */
			
		
			genlistbox.setModel(reportGenModel);
			genlistbox.setItemRenderer(reportGenModel);
		
	}
	@Override
	public void render(Listitem row, Object data) throws Exception 
	{
		final ReportComposer reportComposer=this;
		row.setCheckable(true);
		ReportItem item=(ReportItem)data;
		row.setValue(item);
		row.setAttribute("reportItem", data);
		row.addEventListener("onClick",new EventListener(){
				@Override
				public void onEvent(Event event) throws Exception {
					try {
					ReportItem item=(ReportItem)event.getTarget().getAttribute("reportItem");
					refreshGenData((Listbox)event.getTarget().getPage().getFellow("genlistbox"),item);
					} catch (Exception e) {
						e.printStackTrace();
						throw e;
					}
				}});
		
        String fileType=item.getFileType();
        if(fileType==null)
        	fileType="html";
        
		new Listcell(item.getTitle()).setParent(row);
		new Listcell(item.getDescript()).setParent(row);
		new Listcell(item.getMonitotNumber()).setParent(row);
		new Listcell(item.getPeriod()).setParent(row);
		new Listcell(item.getCreatTime()).setParent(row);
		

		Listcell cell=new Listcell();
		cell.setParent(row);
		Image img=new Image("/main/images/filetype/"+fileType+".gif");
		img.setTooltiptext(fileType);
		img.setParent(cell);
		
		
		cell=new Listcell();
		if("Yes".equals(item.getDeny()))
		{
			img=new Image("/main/images/button/ico/disable_bt.gif");
			img.setTooltiptext("禁止");
		}
		else
		{
			img=new Image("/main/images/button/ico/enable_bt.gif");
			img.setTooltiptext("允许");

		}
		img.setParent(cell);
		cell.setParent(row);
		
		cell=new Listcell();
		//新增加的一种判断
		img=new Image("/main/images/alert/edit.gif");
		try{
			View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
			if(!view.isAdmin()){//非管理员用户
				IniFile userIniFile = view.getUserIni();
				String editTopN_str = userIniFile.getValue(userIniFile.getSections(), UserRightId.DoStatisticReportlistEdit);
				if(!"1".equals(editTopN_str)){
					editFlag = false;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(!editFlag){
			img=new Image("/main/images/alert/edit_false.gif");
		}
		img.setParent(cell);
		cell.setParent(row);
		img.setAttribute("reportItem", item);
		img.addEventListener("onClick",new EventListener(){
			@Override
			public void onEvent(Event event) throws Exception {
				ReportItem reportItem=(ReportItem)event.getTarget().getAttribute("reportItem");
				reportComposer.onModifyReport(event);
			}});
     }
}
