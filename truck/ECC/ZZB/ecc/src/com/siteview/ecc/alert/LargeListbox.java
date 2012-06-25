package com.siteview.ecc.alert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.event.PagingEvent;

import com.siteview.actions.EccActionManager;
import com.siteview.base.tree.INode;
import com.siteview.ecc.alert.control.TooltipPopup;
import com.siteview.ecc.alert.dao.Constand;
import com.siteview.ecc.alert.dao.bean.AccessControl;
import com.siteview.ecc.alert.dao.bean.AlertLogItem;
import com.siteview.ecc.alert.dao.bean.AlertLogQueryCondition;
import com.siteview.ecc.alert.dao.bean.ListBean;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.alert.util.DictionaryFactory;
import com.siteview.ecc.monitorbrower.EntityLinkFuntion;
import com.siteview.ecc.monitorbrower.MonitorDetailLinkFuntion;
import com.siteview.ecc.report.common.SelectableListheader;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccTreeModel;
import com.siteview.ecc.treeview.windows.ConstantValues;
import com.siteview.ecc.util.Toolkit;

public class LargeListbox {
	private int currentpageno = 1;
	private int TotalSize = 30000;
	private AlertLogQueryCondition aCondition = null;
	private Component c = null;
	private String tempMonitorId = "";

	public void setTotalSize(int totalSize) {
		TotalSize = totalSize;
	}

	private Div di = null;

	public LargeListbox(Component c) {
		di = (Div) BaseTools.getComponentById(c, "lidiv");
		this.c = c;
		clear();
		Listbox li = new Listbox();
		li.setFixedLayout(true);
		li.setMultiple(true);
		Listhead lhd = getListHead();
		lhd.setParent(li);
		li.setParent(di);
	}

	private void clear() {
		List chiles = di.getChildren();
		List sd = new ArrayList();
		for (Object o : chiles) {
			sd.add(o);
		}
		for (Object o : sd) {
			di.removeChild((Component) o);
		}
	}

	public void redraw(AlertLogQueryCondition queryCondition, int noFrom, int onTo) throws Exception {
		this.aCondition = queryCondition;
		clear();
		// 获取数据
		ListBean result = DictionaryFactory.getIAlertLogDao().queryAlertLog(new AccessControl(), queryCondition, noFrom, onTo);

		// 重新生成listbox 和paging
		Listbox li = this.getListbox(result);
		li.setParent(di);
		li.setMold("paging");
		Paging pa = new Paging();
		pa.setPageSize(Constand.recordecount);
		pa.setDetailed(true); // 显示记录数
		pa.setWidth("100%");
		pa.setAutohide(true);
//		pa.setVisible(true);
		this.setTotalSize(result == null ? 0 : result.getTotalNumber());
		pa.setTotalSize(TotalSize);
		// li.setPaginal(pa);
		if (noFrom != 0)
			pa.setActivePage(currentpageno);
		pa.setParent(di);
		// 翻页事件
		pa.addEventListener("onPaging", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				PagingEvent pevt = (PagingEvent) event;
				int pagesize = Constand.recordecount;
				int pgno = pevt.getActivePage();
				currentpageno = pgno;
				int ofs = pgno * pagesize;
				redraw(aCondition, ofs + 1, ofs + pagesize);
			}
		});
		SelectableListheader.addPopupmenu(li);
	}

	private Listbox getListbox(ListBean result) {
		Listbox li = new Listbox();
		li.setFixedLayout(true);
		li.setMultiple(true);
		Listhead lhd = getListHead();
		lhd.setParent(li);
		AlertLogItem alertLogItem = new AlertLogItem();
		for (Map<String, String> map : result.getList()) {
			alertLogItem.init(map);
			String monitorName = alertLogItem.getMonitorName();
			String entityName  = alertLogItem.getEntityName();
//			getMonitorIdByEntityName(entityName,monitorName);	
//			String monitorId = this.tempMonitorId;
			getAllMonitorInfo(entityName,monitorName);
			String monitorId = this.tempMonitorId;
			this.tempMonitorId = "";
			if(monitorId == null ||"".equals(monitorId)){
				Listitem listitem = BaseTools.setRow(li
						, alertLogItem, Toolkit.getToolkit().formatDate(alertLogItem.getAlertTime())
						, alertLogItem.getAlertName()
						, alertLogItem.getEntityName()
						, alertLogItem.getMonitorName()
						, alertLogItem.getAlertType().getComponent()
						, alertLogItem.getAlertReceiver()
						, alertLogItem.getAlertStatus().getComponent());
				listitem.setTooltip(getLogPopup(alertLogItem));
			}else{
				String entityId = monitorId.substring(0, monitorId.lastIndexOf("."));
				Listitem listitem = BaseTools.setRow(li
						, alertLogItem, Toolkit.getToolkit().formatDate(alertLogItem.getAlertTime())
						, alertLogItem.getAlertName()
						, BaseTools.getWithEntityLink(entityName,new EntityLinkFuntion(entityId,monitorId))
						, BaseTools.getWithMonitorLink(monitorName,new MonitorDetailLinkFuntion(entityId,monitorId,"btndetail"))							
						, alertLogItem.getAlertType().getComponent()
						, alertLogItem.getAlertReceiver()
						, alertLogItem.getAlertStatus().getComponent());
				listitem.setTooltip(getLogPopup(alertLogItem));
			}
		}
		return li;
	}

	private Listhead getListHead() {
		Listhead lh = new Listhead();
		lh.setSizable(true);
		Listheader lr1 = getListheader("报警时间", "17%", "auto");
		Listheader lr2 = getListheader("报警名称", "18%", "auto");
		Listheader lr3 = getListheader("设备名称", "15%", "auto");
		Listheader lr4 = getListheader("监测器名称", "20%", "auto");
		Listheader lr5 = getListheader("报警类型", "10%", "auto");
		lr5.setId("alertlogtype_header");
		Listheader lr6 = getListheader("报警接收人", "10%", "auto");
		Listheader lr7 = getListheader("报警状态", "10%", "auto");
		lr7.setId("alertlogstatus_header");
		lr1.setParent(lh);
		lr2.setParent(lh);
		lr3.setParent(lh);
		lr4.setParent(lh);
		lr5.setParent(lh);
		lr6.setParent(lh);
		lr7.setParent(lh);

		setSortAscending(lr5, 4, false);
		setSortDescending(lr5, 4, true);
		setSortAscending(lr7, 6, false);
		setSortDescending(lr7, 6, true);
		return lh;
	}

	private Listheader getListheader(String label, String width, String sort) {
		Listheader lr1 = new Listheader(label);
		lr1.setWidth(width);
		lr1.setSort(sort);
		return lr1;
	}

	private void setSortAscending(Listheader lr, int s, boolean n) {
		lr.setSortAscending(BaseTools.getSortComparator(s, n));
	}

	private void setSortDescending(Listheader lr, int s, boolean n) {
		lr.setSortAscending(BaseTools.getSortComparator(s, n));
	}

	public TooltipPopup getLogPopup(AlertLogItem alertlogitem) {
		TooltipPopup tooltippopup = new TooltipPopup();
		tooltippopup.onCreate();
		tooltippopup.setStyle("border:none;color:#FFFFFF;background-color:#717171");
		this.getTooltiptext(tooltippopup, alertlogitem);
		tooltippopup.setParent(this.c);
		return tooltippopup;
	}

	private void getTooltiptext(TooltipPopup tooltippopup, AlertLogItem alertlogitem) {
		tooltippopup.setTitle(alertlogitem.getAlertName());
		tooltippopup.setImage(alertlogitem.getAlertType().getImage());
		tooltippopup.addDescription("报警时间", Toolkit.getToolkit().formatDate(alertlogitem.getAlertTime()));
		tooltippopup.addDescription("设备名称", alertlogitem.getEntityName());
		tooltippopup.addDescription("监测器名称", alertlogitem.getMonitorName());
		tooltippopup.addDescription("报警接收人", alertlogitem.getAlertReceiver());
		tooltippopup.addDescription("报警状态", alertlogitem.getAlertStatus().toString());
	}
/*	public void getMonitorIdByEntityName(String entityName,String monitorName)
	{
//		String entityName = "192.168.0.15(Windows)";
//		String monitorName = "Service:Portable Media Serial Number Service";
		try{
			Object tmpobj = Executions.getCurrent().getDesktop().getSession().getAttribute("CurrentWindow");
//			Object tmpobj = event.getTarget().getDesktop().getSession().getAttribute("CurrentWindow");
			if(tmpobj!=null)
			{
				Executions.getCurrent().getDesktop().getSession().removeAttribute("CurrentWindow");
			}
			Include eccBody = (Include) (Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("eccBody"));
			Tree tree = (Tree) (Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("tree"));
			
			Collection children = tree.getTreechildren().getChildren();
			Treeitem root = null;
			for(Object obj : children)
			{
				if(obj instanceof Treeitem)
				{
					root = (Treeitem)obj;
					getTreeItem(root,entityName,monitorName);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return;
		}
		return ;
	}
	
	
	public void getTreeItem(Treeitem root,String entityName,String monitorName)
	{
		if(root==null) return ;
		root.setOpen(true);
		
		Treechildren tChildren = root.getTreechildren();
		if(tChildren==null) return;
		
		Collection children = tChildren.getItems();
		Object[] objArr = children.toArray();
		if(children==null || children.size()<=0) return ;
		
		for(Object obj : objArr)
		{
			if(obj instanceof Treeitem)
			{
				Treeitem item = (Treeitem)obj;
				EccTreeItem eccItem = (EccTreeItem)item.getValue();
				if(eccItem.getTitle().equals(entityName))//找到匹配的entity
				{
					if(eccItem.getChildRen()!= null && eccItem.getChildRen().size()>0){
						for(EccTreeItem sonEccItem :eccItem.getChildRen()){
							if(monitorName.equals(sonEccItem.getTitle())){//找到匹配的 monitor
								tempMonitorId = sonEccItem.getId();
								root.setOpen(false);
								return;
							}
						}
					}
				}else
				{
					getTreeItem(item,entityName,monitorName);
				}
			}
		}
		root.setOpen(false);
	}*/
	
	public void getAllMonitorInfo(String entityName,String monitorName){
		try{
			Tree tree = (org.zkoss.zul.Tree) Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("tree");
			EccTreeModel model = (EccTreeModel)tree.getModel();
			EccTreeItem root = model.getRoot();
			if (root == null)
				return ;
			
			//整体视图节点
			for (EccTreeItem item : root.getChildRen()){
				root = item;
				findMonitorId(item,entityName,monitorName);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	public void findMonitorId(EccTreeItem item,String entityName,String monitorName) 
	{	
		if(item == null) return;
		for(EccTreeItem itm : item.getChildRen()){
			if(itm.getValue() == null) continue;
			if(INode.ENTITY.equals(itm.getValue().getType())){//类型是 entity
				if(entityName.equals(itm.getTitle())){//找到entityName 相同
					for(EccTreeItem sonEccItem :itm.getChildRen()){
						if(monitorName.equals(sonEccItem.getTitle())){//找到匹配的 monitor
							tempMonitorId = sonEccItem.getId();
							return;
						}
					}
				}
			}else{
				findMonitorId(itm, entityName, monitorName); 
			}
		}
	}
}