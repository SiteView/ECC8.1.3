package com.siteview.ecc.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.UserRightId;
import com.siteview.base.manage.View;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.template.TemplateManager;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.report.beans.MonitorBean;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.report.common.GroupLinkListener;
import com.siteview.ecc.report.models.MonitorModel;
import com.siteview.ecc.report.topnreport.TopnSelectTree;
import com.siteview.ecc.util.LinkCheck;
import com.siteview.ecc.util.Toolkit;

public class EditTopNReport extends GenericForwardComposer
{
	private Window		edittopn;
	//监测器树
	Tree			monitortree;
	// 标题
	private Textbox		title;
	// 报告描述
	private Textbox		descript;
	// 监测器类型
	private Listbox		monitorType;
	// 选择指标
	private Listbox		target;
	// 排序方式
	private Radiogroup	orderby;
	// 数量
	private Spinner		count;
	// 报告类型
	private Listbox		reportType;
	// 报告生成时间
	private Listbox		reportTime;
	// 以E-MAIL方式发送报告
	private Textbox		emailSend;
	// 禁止报告
	private Checkbox	Deny;
	// 周报截止时间
	private Listbox		weekEndTime;
	//文件格式
	private Listbox    lfileType;
	//取值方式
	private Listbox    lgetValue;
	//view
	private Combobox viewNamecombobox;
	private IniFile		iniFile		= null;
	private String		currsection	= null;
	private String		anObject	= null;
	private String		tnObject	= null;
	TopNReportComposer	tTopNReportComposer;
	private  Label		groupLink;
	boolean isEdit=false;

	public EditTopNReport() {
		
	}
	
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
	/**
	 * 获取选择的监测器节点
	 * 
	 * @return
	 */
	public String getNodeids() {
		String nodeids = null;
		try {
			TopnSelectTree sTree = (TopnSelectTree) monitortree;
			nodeids = sTree.getAllSelectedIds();
		} catch (NullPointerException e) {

		}
		return nodeids;
	}
	public void onClick$OK(Event event) {
	
		String ids = getNodeids();
		if (ids == null || !ids.endsWith("")) {
			try {
				Messagebox.show("请选择监测器！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		String section = currsection;
		String tit = title.getValue().trim() ;
		if(tit.equals(""))
		{
			try {
				Messagebox.show("标题不能为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				title.focus();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return;
		}
		if(!isEdit){
		    for(String sec : iniFile.getSectionList()){
		    	String tmpTitle = iniFile.getValue(sec, "Title");
		    	tmpTitle = tmpTitle.substring(0,tmpTitle.indexOf("|")).trim();
		    	if(tmpTitle.equals(tit))
		    	{
					try {
						Messagebox.show("报告标题已存在，请重新输入！", "提示", Messagebox.OK, Messagebox.INFORMATION);
						title.focus();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					return;
		    	}
		    }
	    }
		tit = tit+"|"+section;/////////////////////////////////////////////////////////////////////////////
		String desc = descript.getValue();
		String monitortype="";
		if (monitorType.getSelectedItem() == null)
		{
			if (monitorType.getItemCount()>0)
			monitortype=(String)monitorType.getItemAtIndex(0).getValue();
			
		}else
		{
			monitortype=(String)monitorType.getSelectedItem().getValue();
		}
		String tar="";
		if(target.getSelectedItem() == null)
		{
			if (target.getItemCount()>0)
				tar=(String)target.getItemAtIndex(0).getValue();
		}else
		{
			tar=(String)target.getSelectedItem().getValue();
		}
		String oreder = orderby.getSelectedItem().getLabel();
		String cou = Integer.toString(count.getValue());
		String rt="";
		if(reportType.getSelectedItem() == null)
		{
			if(reportType.getItemCount()>0)
			rt=(String)reportType.getItemAtIndex(0).getValue();
		}else
		{
			rt=(String)reportType.getSelectedItem().getValue();
		}
		String rtime="0";
		if(reportTime.getSelectedItem() == null)
		{
			if (reportTime.getItemCount()>0)
				rtime= reportTime.getItemAtIndex(0).getValue().toString();
			
		}else
		{
			rtime=reportTime.getSelectedItem().getValue().toString();
		}
		String es ="";
		try {
			if (emailSend.getValue() != null && !emailSend.getValue().equals("")) {
				String constr = "/.+@.+\\.[a-z]+/";
				emailSend.setConstraint(constr);
				es= emailSend.getValue();
			}
		} catch (Exception x) {
			try {
				Messagebox.show("E_Mail格式不正确,请重新输入!", "提示", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e) {
			}
			return;
		}	
		String wkt = "0";
		if (!weekEndTime.isDisabled()) {
			wkt = weekEndTime.getSelectedItem() == null ? "0" : (String) (weekEndTime.getSelectedItem().getValue());
		}
		String denny = "No";
		if (Deny.isChecked()) {
			denny = "Yes";// 禁止报告
		} else {
			denny = "No";
		}
		String getValue = (String)lgetValue.getSelectedItem().getValue();// 
		if (getValue==null||getValue.equals(""))
		{
			getValue="平均";
		}
		String filetype=(String)lfileType.getSelectedItem().getValue();// 
		if (filetype==null||filetype.equals(""))
		{
			filetype="html";
		}
		
		
		try {
			if (!isEdit)
			{
			iniFile.createSection(section);
			}
			iniFile.setKeyValue(section, "Title", tit);
			iniFile.setKeyValue(section, "Descript", desc);
			iniFile.setKeyValue(section, "Type", monitortype);
			iniFile.setKeyValue(section, "Mark", tar);
			iniFile.setKeyValue(section, "Sort", oreder);
			iniFile.setKeyValue(section, "Count", cou);
			iniFile.setKeyValue(section, "Period", rt);
			iniFile.setKeyValue(section, "Generate", rtime);
			iniFile.setKeyValue(section, "EmailSend", es);
			iniFile.setKeyValue(section, "WeekEndTime", wkt);
			iniFile.setKeyValue(section, "Deny", denny);
			iniFile.setKeyValue(section, "GetValue", getValue);
			iniFile.setKeyValue(section, "GroupRight", ids);
			iniFile.setKeyValue(section, "fileType", filetype);
			//******保存monitor type值*******,形如:5,44
			iniFile.setKeyValue(section, "MonitorTypdId", reportType.getSelectedItem().getId());
			iniFile.saveChange();
			
			if (!isEdit)
			{
				//addlog
				View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
				String loginname = view.getLoginName();
				String minfo=loginname+" "+"在"+OpObjectId.topn_report.name+"中进行了"+OpTypeId.add.name+"操作,"+OpTypeId.add.name+"了"+title.getValue();
				AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.topn_report);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Executions.getCurrent().getDesktop().getSession().setAttribute("topNReportlit_id", section);
		edittopn.detach();
		// 刷新TOPN报告列表
		tTopNReportComposer.onClick$refresh(event);
	}

	public void onClick$Cancel(Event event) {
		edittopn.detach();
	}

	public void onCreate$edittopn(CreateEvent event) {
		try{
			init();
			iniFile=(IniFile) edittopn.getAttribute("iniFile");
			tTopNReportComposer = (TopNReportComposer) edittopn.getAttribute("tTopNReportComposer");
			this.isEdit=(Boolean)edittopn.getAttribute("isedit");
			// 显示当前报告对应的监测器节点
			if (isEdit) //编辑
			{
			edittopn.setTitle("编辑topn报告");
			currsection = (String) edittopn.getAttribute("currsection");
			String ids = getValue("GroupRight");
			this.execution.setAttribute("all_selected_ids", ids);
			String t="";
			t = getValue("Type");
			anObject = t;
			
			List typeList = monitorType.getItems();
			for(Object obj : typeList){
				if(obj instanceof Listitem){
					Listitem tmpListitem = (Listitem)obj;
					if(tmpListitem.getLabel().equals(t)){
						tmpListitem.setSelected(true);
						Events.postEvent("onSelect", monitorType, null) ;
						break;
					}
				}
			}
			// FIXME 监测器指标不知如何初始化值
			/*
			 * typeModel model2 = new typeModel(anObject); makelistData(target, model2, model2);
			 */
			t = getValue("Mark");
			tnObject = t;
			//********** monitor type 值
			
			//String monitortypeid =monitorType.getSelectedItem().getId();
	
			((TopnSelectTree) this.monitortree).onCreate();
			// /*******************************
			t = getValue("Title");
			int index = t.indexOf("|");
			if(index>0){
				t = t.substring(0,index);
			}
			title.setValue(t);
			
			t = getValue("Descript");
			descript.setValue(t);
			
			t = getValue("Sort");
			for (Object x : orderby.getChildren()) {
				if (((Radio) x).getLabel().equals(t)) {
					orderby.setSelectedItem((Radio) x);
				}
			}
			t = getValue("Period");
			setListSelectItem(reportType, t);
			if (t.equals("Week")) {
				weekEndTime.setDisabled(false);
			} else {
				weekEndTime.setDisabled(true);
			}
			t = getValue("Generate");
			setListSelectItem(reportTime, t);
			t = getValue("WeekEndTime");
			setListSelectItem2(weekEndTime, t);
			t = getValue("Deny");
			if (t.equals("Yes")) {
				Deny.setChecked(true);
			} else {
				Deny.setChecked(false);
			}
			t = getValue("EmailSend");
			emailSend.setValue(t);
			t = getValue("Count");
			count.setValue(Integer.parseInt(t));
			t=getValue("fileType");
			if (t!=null)
			{
				for(int i=0;i<lfileType.getChildren().size();i++)
				{
						if(lfileType.getItemAtIndex(i).getValue().toString().equals(t))
						{
							lfileType.setSelectedIndex(i);
							break;
						}
				}
			}
			t=getValue("GetValue");
			if(t!=null)
			{
				for(int i=0;i<lgetValue.getChildren().size();i++)
				{
						if(lgetValue.getItemAtIndex(i).getValue().toString().equals(t))
						{
							lgetValue.setSelectedIndex(i);
							break;
						}
				}
			}
			}else
			{
				this.currsection=String.valueOf(System.currentTimeMillis());
				if (monitorType.getChildren().size()>0)
				monitorType.setSelectedIndex(0);
				Events.postEvent("onSelect", monitorType, null) ;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * @param o
	 * @param oname
	 */
	public void onSelect$viewNamecombobox(Event event){
		String av = (String) viewNamecombobox.getSelectedItem().getValue();
		((TopnSelectTree) monitortree).setViewName(av);
	}
	private String getValue(String name) {
		return this.iniFile.getValue(currsection, name);
	}

	private void setListSelectItem(Listbox li, String value) {
		for (Object o : li.getItems()) {
			try {
				if (((Listitem) o).getValue().equals(value)) {
					li.setSelectedItem((Listitem) o);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setListSelectItem2(Listbox li, String value) {
		for (Object o : li.getItems()) {
			try {
				if (((Listitem) o).getValue().equals(value)) {
					li.setSelectedItem((Listitem) o);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void onSelect$tree(Event e){
		String monitorType = ((TopnSelectTree) monitortree).getMonitorType();
		Listitem item = ChartUtil.findItem(this.monitorType, monitorType);
		this.monitorType.setSelectedItem(item);
		Events.sendEvent(new Event(Events.ON_SELECT,this.monitorType));
	}
	/**
	 * 监测器指标
	 * 
	 * @param event
	 */
	public void onSelect$monitorType(Event event) {
		try{
		Listitem item = monitorType.getSelectedItem();
		if (item != null) {
			
			((TopnSelectTree)monitortree).setMonitorType(item.getId());
			typeModel model = new typeModel(item.getId());
			ChartUtil.makelistData(target, model, model);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void onSelect$reportType(Event event) {
		Listitem item = reportType.getSelectedItem();
		String l = item.getValue().toString();
		if (!l.equals("Week")) {
			weekEndTime.setDisabled(true);
		} else
			weekEndTime.setDisabled(false);
	}
	private View v = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
	
	public void init() throws Exception{
		Set<Type> returnValue = new HashSet<Type>();
		for(MonitorBean mb : new MonitorModel().getAllMonitorInfo()){
			INode node = v.getNode(mb.getId());
			MonitorInfo info = v.getMonitorInfo(node);
			if(info == null) continue;
			MonitorTemplate tmp = info.getMonitorTemplate();
			if(tmp == null) continue;
			returnValue.add(new Type(tmp.get_sv_id(),tmp.get_sv_label()));
		}
		//排序	
		ArrayList<Type> returnValueList = new ArrayList<Type>();
		
		for(Type monitorType : returnValue){
			returnValueList.add(monitorType);
		}
		Collections.sort(returnValueList, new Comparator<Type>(){

			@Override 
			public int compare(Type o1,Type o2){// TODO Auto-generated method stub
				
				return o1.name.compareTo(o2.name);
			}
			
		});
		
		for(Type monitorType : returnValueList){
		Listitem item = new Listitem();
		item.setId(monitorType.id);
		item.setLabel(monitorType.name);
		item.setValue(monitorType.name);
		this.monitorType.appendChild(item);
	}
		
		
//		Collections.sort(list, new Comparator<Date>()
//				{
//					public int compare(Date o1, Date o2)
//					{
//						return o1.compareTo(o2);
//					}
//				});
		
		
		///////////////////////////////////
//		for(Type monitorType : returnValue){
//			Listitem item = new Listitem();
//			item.setId(monitorType.id);
//			item.setLabel(monitorType.name);
//			item.setValue(monitorType.name);
//			this.monitorType.appendChild(item);
//		}
	}
	
	class Type {
		String id;
		String name;
		public Type(String id, String name) {
			super();
			this.id = id;
			this.name = name;
		}
		
		public boolean equals(Object obj){
			if (this == obj) {
			    return true;
			}
			if (obj instanceof Type) {
				Type another = (Type)obj;
			    return another.id.equals(id);
			}
			return false;
		}
		public int hashCode(){
			return this.id.hashCode();
		}
	}

	/**
	 * 监测器指标model,renderer
	 * 
	 * @author Administrator
	 * 
	 */
	class typeModel extends ListModelList implements ListitemRenderer {

		public typeModel(String id) {
			super();
			MonitorTemplate monitorTemplate = TemplateManager.getMonitorTemplate(id);
			List<Map<String, String>> tt = monitorTemplate.get_Return_Items();
			List<String> li = new ArrayList<String>();
			for (Map<String, String> t : tt) {
				li.add(t.get("sv_label"));
			}
			addAll(li);
		}

		@Override
		public void render(Listitem arg0, Object arg1) throws Exception {
			Listitem item = arg0;
			String m = (String) arg1;
			if (m.equals(tnObject)) {
				item.setSelected(true);
			}
			item.setValue(m);
			Listcell tmp=new Listcell(m);
			tmp.setParent(item);
		}
	}
	
}
