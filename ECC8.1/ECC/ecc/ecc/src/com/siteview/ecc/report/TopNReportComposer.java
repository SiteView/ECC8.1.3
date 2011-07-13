package com.siteview.ecc.report;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.UserRightId;
import com.siteview.base.manage.View;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.report.beans.TopNBean;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.report.common.SelectableListheader;
import com.siteview.ecc.report.models.TopNListitemModel;
import com.siteview.ecc.report.topnreport.TopNLogBean;
import com.siteview.ecc.report.topnreport.TopNLogListmodel;
import com.siteview.ecc.reportserver.TopNReport;
import com.siteview.ecc.util.Toolkit;
public class TopNReportComposer extends GenericForwardComposer
{
	
	private Div					topn_time;
	
	private Listbox				topNList;
	
	private Listbox				topNGenerateTime;
	
	private Combobox			genlistboxPageSize;
	
	private static final String	INI_FILE	= "topnreportset.ini";
	Window						topNReport;
	int							t			= 0;
	private static final String	EditTOPN	= "/main/report/edittopnreport.zul";
	private IniFile				iniFile;
	private Button              addTopN;
	private Button              delete;
	private boolean				editFlag = true;//�û��Ƿ��� �༭topN�����Ȩ��
	public void onInit() throws Exception{
		try{
			View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
			if(!view.isAdmin()){//�ǹ���Ա�û�
				IniFile userIniFile = view.getUserIni();
				String addTopN_str = userIniFile.getValue(userIniFile.getSections(), UserRightId.DoTopNReportlistAdd);
				String deleteTopN_str = userIniFile.getValue(userIniFile.getSections(), UserRightId.DoTopNReportlistDel);
				String editTopN_str = userIniFile.getValue(userIniFile.getSections(), UserRightId.DoStatisticReportlistEdit);
				if(!"1".equals(addTopN_str)){
					addTopN.setDisabled(true);
				}
				if(!"1".equals(deleteTopN_str)){
					delete.setDisabled(true);
				}
				if(!"1".equals(editTopN_str)){
					editFlag = false;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * ���TOPN����
	 * 
	 * @param event
	 * @throws InterruptedException
	 * @throws SuspendNotAllowedException
	 */
	public void onClick$addTopN(Event event) throws SuspendNotAllowedException, InterruptedException
	{
		final Window win = (Window) Executions.createComponents(EditTOPN, null, null);
		win.setAttribute("isedit", false);
		win.setAttribute("tTopNReportComposer", this);
		win.setAttribute("iniFile", iniFile);
		win.doModal();
		
		refreshInifile();
	}
		

	/*
	 * ˢ��topnreportset.ini
	 */
	private void refreshInifile()
	{
		this.iniFile = new IniFile(INI_FILE);
		try
		{
			this.iniFile.load();
		} catch (Exception e)
		{
		}
		
	}
	
	public void onChange$genlistboxPageSize()
	{

		if(genlistboxPageSize.getSelectedItem()==null)
		{
			genlistboxPageSize.setSelectedIndex(0);
		}
		String pgsz = (String)genlistboxPageSize.getSelectedItem().getValue();
		try {
				int pageSize=Math.abs(Integer.parseInt(pgsz));
				Toolkit.getToolkit().setCookie("topNGenerateTimePageSize",pgsz,Integer.MAX_VALUE);
				topNGenerateTime.setPageSize(pageSize);
				topNGenerateTime.getPaginal().setPageSize(pageSize);
				
		} catch (Exception e) {
			
		}

	}
	
	/*
	 * ˢ��topNList
	 */
	private void refreshiControl()
	{
		ChartUtil.clearListbox(topNList);
		TopNListitemModel model = new TopNListitemModel(topNReport);
//		ChartUtil.makelistData(topNList, model, model);
		
		try {
			for(TopNBean m : model.getTopNData()){
				Image image = null;
				if(m.getDeny().equals("����")){
					image = new Image("/main/images/button/ico/enable_bt.gif");
				}else{
					image = new Image("/main/images/button/ico/disable_bt.gif");
				}
				Listitem item = ChartUtil.addRow(topNList, m, m.getTitle(),m.getDescript(),m.getPeriod()
						,new Image("/main/images/filetype/"+m.getFiletype()+".gif"),image,m.getEditImage());
				item.setId(m.getSection());
				item.addForward("onClick", topNReport, "onSelecttopNList");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String section = (String)Executions.getCurrent().getDesktop().getSession().getAttribute("topNReportlit_id");
		
		if(null == section)
			return;
		
		Listitem selectedItem = null;
		for(Object obj : topNList.getItems()){
			if(obj instanceof Listitem){
				Listitem tmpItem = (Listitem)obj;
				if(tmpItem.getId().equals(section)){
					selectedItem = tmpItem;
				}
			}
		}
		
		if(null != selectedItem){
			selectedItem.setSelected(true);
			Events.sendEvent(new Event(Events.ON_SELECT,topNList));
			topNList.setActivePage(selectedItem);
		}
	}
	
	public void onCreate$topNReport(Event event)
	{
		refreshInifile();
		refreshiControl();
		topNList.getPagingChild().setMold("os");
		topNGenerateTime.getPagingChild().setMold("os");
		String pgsz=Toolkit.getToolkit().getCookie("topNGenerateTimePageSize");
		if(pgsz==null)
			pgsz="10";
		genlistboxPageSize.setValue(pgsz);
		int pageSize=Math.abs(Integer.parseInt(pgsz));
		topNGenerateTime.setPageSize(pageSize);
		topNGenerateTime.getPaginal().setPageSize(pageSize);
//		if (topNList.getItemCount() > 0)
//		{
//			topNList.setSelectedIndex(0);
//			String reportid = this.iniFile.getSectionList().get(0);
//			IniFile iniGen = new IniFile("reportTopN." + reportid + ".ini");
//			try
//			{
//				iniGen.load();
//			} catch (Exception e)
//			{
//				return;
//			}
//			topn_time.setVisible(true);
//			TopNLogListmodel model1 = new TopNLogListmodel(iniGen, this);
//			topNGenerateTime.setModel(model1);
//			topNGenerateTime.setItemRenderer(model1);
//		}
		SelectableListheader.addPopupmenu(topNList);
		SelectableListheader.addPopupmenu(topNGenerateTime);
	}
	
	/*
	 * ����ɾ����־
	 */
	public void onClick$btnBatchDeleteGen(Event event) throws Exception
	{
		if (topNGenerateTime.getSelectedCount() == 0)
		{
			
			try
			{
				Messagebox.show("��ѡ�񱨸�!", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			return;
		}
		try
		{
			int ret = Messagebox.show("��ȷ��Ҫɾ��ѡ�еļ�¼��", "��ʾ", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
			
			if (ret == Messagebox.CANCEL)
				return;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		Listitem topNlist = topNList.getSelectedItem();
		Iterator it = topNGenerateTime.getSelectedItems().iterator();
		try
		{
			while (it.hasNext())
			{
				Listitem item = (Listitem) it.next();
				TopNLogBean logbean = (TopNLogBean) item.getValue();
				String section = logbean.getTitle();
				String filetype = logbean.getFiletype();
				String finame = TopNLogListmodel.getfilename(section, filetype);
				String reportid = topNlist.getId();
				IniFile inifile = new IniFile("reportTopN." + reportid + ".ini");
				inifile.load();
				for(String key : inifile.getSectionList()){
					if(key.equals(section)){
						inifile.deleteSection(section);
						inifile.saveChange();
						File file = new File(finame);
						File folder = new File(finame+"_files");
						if (file.exists())
						{
							file.delete();
						}
						if(folder.exists())
						{
							Toolkit.getToolkit().deleteFolder(finame+"_files");
						}
					}
				}
			}
		} catch (Exception e)
		{
			throw e;
		}
		// ˢ������
		onSelecttopNList(event);
		
	}
	
	/**
	 * �����б����¼�,��ʾ�Զ����ɵ�TOPN����
	 * 
	 * @param event
	 */
	public void onSelecttopNList(Event event)
	{
		Listitem item = topNList.getSelectedItem();
		if (topNGenerateTime.getItemCount() > 0)
		{
			topNGenerateTime.getItems().clear();
		}
		if (item == null)
		{
			return;
		}
		String reportid = item.getId();
		IniFile iniGen = new IniFile("reportTopN." + reportid + ".ini");
		try
		{
			iniGen.load();
		} catch (Exception e)
		{
			return;
		}
		topn_time.setVisible(true);
		TopNLogListmodel model = new TopNLogListmodel(iniGen, this);
		topNGenerateTime.setModel(model);
		topNGenerateTime.setItemRenderer(model);
		
	}
	
	/**
	 * �༭TOP_N����
	 * 
	 * @param section
	 * @throws Exception
	 */
	public void onTpenEditTop_NReport(Event section) throws Exception
	{
		View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
		if(this.editFlag){
			final Window win = (Window) Executions.createComponents(EditTOPN, null, null);
			win.setAttribute("isedit", true);
			win.setAttribute("tTopNReportComposer", this);
			win.setAttribute("iniFile", iniFile);
			win.setAttribute("currsection", section.getData());
			try
			{
				win.doModal();
			} catch (Exception e)
			{
				return;
			}
			refreshInifile();
			String loginname = view.getLoginName();
			String minfo = loginname + " " + "��" + OpObjectId.topn_report.name + "�н�����  " + OpTypeId.edit.name + "������";
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.topn_report);
		}else
		{
			try{
				Messagebox.show("�û�:"+view.getLoginName()+" û��  �༭TopN����  ��Ȩ��!", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}catch(Exception e){}
		}

	}
	
	/**
	 * ����ɾ��topN����
	 */
	public void onClick$delete(Event event)
	{
		if(topNList.getSelectedCount()<=0){
			try {
				Messagebox.show("��ѡ�񱨸�!", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e) {}
			return;
		}
		try{
			int ret = Messagebox.show("��ȷ��Ҫɾ��ѡ�еļ�¼��", "ѯ��", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
			if (ret == Messagebox.CANCEL)
				return;
			Set<Listitem> items = topNList.getSelectedItems();
			if (items == null || items.size() == 0)
				return;
			Iterator itr = items.iterator();
			this.iniFile = new IniFile(INI_FILE);
			if (iniFile == null)
				return;
			for (; itr.hasNext();)
			{
				Listitem item = (Listitem) itr.next();
				iniFile.deleteSection(item.getId());
				IniFile iniGen = new IniFile("reportTopN." + item.getId() + ".ini");
				try{
					iniGen.load();
					}catch(Exception e){
						
					}
				List<String> sections = iniGen.getSectionList();
				for(String section : sections)
				{
					if (section.equalsIgnoreCase("TempSection(Please_modify_it)"))
						continue;
					Map<String, String> value = iniGen.getFmap().get(section);
					String fileType = value.get("fileType");
					String filePath = TopNLogListmodel.getfilename(section, fileType);
					File f = new File(filePath);
					if(!f.exists()) continue;
					f.delete();
					if(fileType.equals("html"))
					{
						f.delete();
						Toolkit.getToolkit().deleteFolder(filePath+"_files");
					}
					iniGen.deleteSection(section);
				}
				iniGen.saveChange();
			}
			
			iniFile.saveChange();//1252396986281
		} catch (Exception e)
		{
			e.printStackTrace();
		}
			View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
			String loginname = view.getLoginName();
			String minfo = loginname + " " + "��" + OpObjectId.topn_report.name + "�н�����  " + OpTypeId.del.name + "������";
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del, OpObjectId.topn_report);
			// ˢ��ҳ��
			refreshInifile();
			refreshiControl();
			this.topNGenerateTime.getItems().clear();


	}
	
	/**
	 * ��������ť����
	 * 
	 * @param event
	 */
	public void onClick$batchAllow(Event event)
	{
		if(topNList.getSelectedCount()<=0){
			try {
				Messagebox.show("��ѡ�񱨸�!", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		
		Set<Listitem> items = topNList.getSelectedItems();
		if (items == null || items.size() == 0)
			return;
		Iterator itr = items.iterator();
		this.iniFile = new IniFile(INI_FILE);
		if (iniFile == null)
			return;
		try
		{
			iniFile.load();
		} catch (Exception e1)
		{
			e1.printStackTrace();
		}
		for (; itr.hasNext();)
		{
			Listitem item = (Listitem) itr.next();
			try
			{
				iniFile.setKeyValue(item.getId(), "Deny", "No");// ����
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			iniFile.saveChange();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		String minfo = loginname + " " + "��" + OpObjectId.topn_report.name + "�н�����  " + OpTypeId.enable.name + "������";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.enable, OpObjectId.topn_report);
		// ˢ��ҳ��
		refreshiControl();
	}
	
	/**
	 * ������ֹ����
	 * 
	 * @param event
	 */
	public void onClick$batchForbid(Event event)
	{
		if(topNList.getSelectedCount()<=0){
			try {
				Messagebox.show("��ѡ�񱨸�!", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		
		Set<Listitem> items = topNList.getSelectedItems();
		if (items == null || items.size() == 0)
			return;
		Iterator itr = items.iterator();
		this.iniFile = new IniFile(INI_FILE);
		try
		{
			iniFile.load();
		} catch (Exception e1)
		{
			e1.printStackTrace();
		}
		for (; itr.hasNext();)
		{
			Listitem item = (Listitem) itr.next();
			try
			{
				iniFile.setKeyValue(item.getId(), "Deny", "Yes");// ��ֹ
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		try
		{
			iniFile.saveChange();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		String minfo = loginname + " " + "��" + OpObjectId.topn_report.name + "�н�����  " + OpTypeId.diable.name + "������";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.diable, OpObjectId.topn_report);
		
		// ˢ��ҳ��
		refreshiControl();
	}
	
	/**
	 * ˢ���б�
	 * 
	 * @param event
	 */
	public void onClick$refresh(Event event)
	{
		refreshiControl();
	}
	
	/**
	 * ���ɱ��水ť�¼�
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onClick$CreateReportBtn(Event event) throws Exception
	{
		
		if (topNList.getSelectedItem() == null)
		{
			try
			{
				Messagebox.show("��ѡ�񱨸�!", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			return;
		}
		
		View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		Listitem item = topNList.getSelectedItem();
		String section = item.getId();
		this.iniFile = new IniFile(INI_FILE);
		try
		{
			this.iniFile.load();
		} catch (Exception e)
		{
		}
		Map<String, String> reportDefine = this.iniFile.getSectionData(section);
		String Period = reportDefine.get("Period");
		String filetype = reportDefine.get("fileType");
		if (filetype == null)
		{
			filetype = "html";
		}
		Date tmStart = null;
		Date tmEnd = new Date();
		
		if (Period.equals("Month"))
			tmStart = Toolkit.getToolkit().delDay(new Date(), 30);
		else if (Period.equals("Week"))
			tmStart = Toolkit.getToolkit().delDay(new Date(), 7);
		else if (Period.equals("Day"))
			tmStart = Toolkit.getToolkit().delDay(new Date(), 1);
		else
		{
			Toolkit.getToolkit().showError("�������䲻֧��:");
			return;
		}
		
		TopNReport tmpTopNReport = new TopNReport(section, reportDefine, tmStart, tmEnd, view, false);

//	    tmpTopNReport.createReport();
		Thread thread = new Thread(tmpTopNReport);
		thread.setName("TopNReport -- TopNReport.java");
		thread.start();
		final Window win = (Window) Executions.createComponents("/main/progress/topnprogress.zul", null, null);
		win.setAttribute("topnreport", tmpTopNReport);
		win.setAttribute("filetype", filetype);
		win.setAttribute("reportname", tmpTopNReport.strReportName);
		win.doModal();
		// AMedia(String name, String format, String ctype, URL url, String charset)
		// ˢ����־����
		
		//addlog
//		View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
		String minfo=loginname+" "+"��"+OpObjectId.topn_report.name+"�н������ֶ����ɱ������.";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.topn_report);
		onSelecttopNList(event);
	}
	
	public class ontime implements EventListener
	{
		TopNReport topNReport ;
		 public ontime(TopNReport topNReport)
		 {
			 this.topNReport=topNReport;
		 }
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			// TODO Auto-generated method stub
			try
			{
				final Window win = (Window) Executions.createComponents("/main/progress/topnprogress.zul", null, null);
				win.setAttribute("topnreport", topNReport);
				win.doModal();
			}catch(Exception e)
			{}
		}
	
	}
	
}
