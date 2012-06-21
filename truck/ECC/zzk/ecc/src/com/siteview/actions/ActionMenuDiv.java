package com.siteview.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zkex.zul.East;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Row;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;

import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.queue.GrantChangeEvent;
import com.siteview.base.queue.IQueueEvent;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.timer.TimerListener;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.windows.ConstantValues;
import com.siteview.ecc.util.TitleChangedPanel;
import com.siteview.ecc.util.Toolkit;

public class ActionMenuDiv extends Div implements EventListener,TimerListener {
	public ActionMenuDiv() {
		super();
		treeMenu.setDisplayTitle(false);
		listMenu.setDisplayTitle(false);
	}

	EccTreeItem treeItem = null;
	EccTreeItem listItem = null;
	ActionPopup treeMenu = new ActionPopup();
	ActionPopup listMenu = new ActionPopup();
	
	TitleChangedPanel treePanel = new TitleChangedPanel();
	TitleChangedPanel listPanel = new TitleChangedPanel();

	@Override
	public void notifyChange(IQueueEvent event) {
		if(event instanceof GrantChangeEvent)
		{
			refreshMenu();
			
		}else if(event instanceof ChangeDetailEvent)
		{
			if(treeItem!=null)
			if(treeItem.getId().equals(((ChangeDetailEvent)event).getSvid()))
			{
				refreshMenu();
			}
			if(listItem!=null)
			if(listItem.getId().equals(((ChangeDetailEvent)event).getSvid()))
				{
					refreshMenu();
				}
		}
		
	}


	public void onEvent(Event event) {
		EccTreeItem item = null;
		if (event instanceof SelectEvent) {
			Component com = ((SelectEvent) event).getReference();
			if (com instanceof Treeitem)
				item = (EccTreeItem) ((Treeitem) com).getValue();
			else if (com instanceof Listitem)
				item = (EccTreeItem) ((Listitem) com).getValue();
		} else {
			Component com = event.getTarget();
			item = (EccTreeItem) com.getAttribute("eccTreeItem");
		}

		if (item != null)
			if (item.isMonitorTreeNode()||item.getType().equals("WholeView")) 
			{

				if (event.getTarget() instanceof Tree) 
				{
					refreshAll(item);
					return;
				} else {
					Session session = Executions.getCurrent().getDesktop().getSession();
					if(session.getAttribute("selFavItem")!=null){
						this.listItem = (EccTreeItem)session.getAttribute("selFavItem");
					}else{
						this.listItem=item.getChildRen().get(0);
					}
					
					listPanel.setTitle(listItem.getTitle());
					listPanel.getCaption().setTooltiptext(listItem.getTitle());
					listMenu.refresh(listItem);
					listPanel.setVisible(true);
					return;
				}
			}
	

	}
	/**
	 * 此方法 专用于 新增监测器后的 界面刷新
	 * kai.zhang
	 * @param EccTreeItem
	 */
	public void onEvent(EccTreeItem item) {
	
		if (item != null){
			if (item.isMonitorTreeNode()||item.getType().equals("WholeView")) 
			{
					Session session = Executions.getCurrent().getDesktop().getSession();
					if(session.getAttribute("selFavItem")!=null){
						this.listItem = (EccTreeItem)session.getAttribute("selFavItem");
					}else{
						this.listItem=item.getChildRen().get(0);
					}
					
					listPanel.setTitle(listItem.getTitle());
					listPanel.getCaption().setTooltiptext(listItem.getTitle());
					listMenu.refresh(listItem);
					listPanel.setVisible(true);
			}
		}
	}
	/**
	 * 整体视图中点击列表每一行非下划线部分触发的事件
	 * 因onEvent方法调用的地方太多，为了不影响其他功能的使用，故新建此方法
	 * @param event
	 */
	public EventListener selectListEventListener(){
		return new EventListener(){
			@Override
			public void onEvent(Event event) throws Exception {
				EccTreeItem item = null;
				if (event instanceof SelectEvent) {
					Component com = ((SelectEvent) event).getReference();
					if (com instanceof Treeitem)
						item = (EccTreeItem) ((Treeitem) com).getValue();
					else if (com instanceof Listitem)
						item = (EccTreeItem) ((Listitem) com).getValue();
				} else {
					Component com = event.getTarget();
					item = (EccTreeItem) com.getAttribute("eccTreeItem");
				}

				if (item != null){
					if (item.isMonitorTreeNode()||item.getType().equals("WholeView")) 
					{
						if(item.getTitle().length()>12){
							listPanel.setTitle(item.getTitle().substring(0, 12)+"...");
						}else{
							listPanel.setTitle(item.getTitle());
						}
						listPanel.getCaption().setTooltiptext(item.getTitle());
						listMenu.refresh(item);
						listPanel.setVisible(true);
	
					}
					
					Session session = Executions.getCurrent().getDesktop().getSession();
					EccTreeItem itemInSession = (EccTreeItem)session.getAttribute("selectedItem");
					if("monitor".equals(item.getType())){
						session.setAttribute("selectedItem", item);
						if(itemInSession == null || !item.getParent().getId().equals(itemInSession.getParent().getId())){
							session.removeAttribute("doMap");
						}
					}else if("entity".equals(item.getType())){
						session.setAttribute("selectedItem", item);
						if(item.getChildRen()==null || item.getChildRen().size()<=0){
							session.setAttribute("selectedItem", null);
							session.removeAttribute("doMap");
						}
					}else{
						session.setAttribute("selectedItem", null);
						session.removeAttribute("doMap");
					}
				}
			}};
	}

	public void refreshAll(EccTreeItem item)
	{
		this.treeItem=item;
		treePanel.setTitle(treeItem.getTitle());
		treePanel.getCaption().setTooltiptext(treeItem.getTitle());
		treeMenu.refresh(treeItem);
		treePanel.setVisible(true);
		
		if(treeItem.getChildRen().size()<=0)
		{
			listPanel.setVisible(false);
			return;
		}
		
		Session session = Executions.getCurrent().getDesktop().getSession();
		EccTreeItem itemInSession = (EccTreeItem)session.getAttribute("selectedItem");
		if(itemInSession != null){
			try{
				if(itemInSession.getParent().getId().equals(treeItem.getId())){
					listItem = itemInSession;
				}else{
					listItem = treeItem.getChildRen().get(0);
				}
			}catch(Exception e){
				listItem = treeItem.getChildRen().get(0);
			}
		}else{
			listItem = treeItem.getChildRen().get(0);
		}
		listMenu.refresh(listItem);
		listPanel.setVisible(true);
		if(listItem.getTitle().length()>12){
			listPanel.setTitle(listItem.getTitle().substring(0, 12)+"...");
		}else{
			listPanel.setTitle(listItem.getTitle());
		}
		listPanel.getCaption().setTooltiptext(listItem.getTitle());
	}
	
	public void refreshMonitor(EccTreeItem item){
		this.treeItem = item.getParent();
		treePanel.setTitle(treeItem.getTitle());
		treePanel.getCaption().setTooltiptext(treeItem.getTitle());
		treeMenu.refresh(treeItem);
		treePanel.setVisible(true);
		
		if(treeItem.getChildRen().size()<=0)
		{
			listPanel.setVisible(false);
			return;
		}
		
		listItem = item;
		listMenu.refresh(listItem);
		listPanel.setVisible(true);
		if(listItem.getTitle().length()>12){
			listPanel.setTitle(listItem.getTitle().substring(0, 12)+"...");
		}else{
			listPanel.setTitle(listItem.getTitle());
		}
		listPanel.getCaption().setTooltiptext(listItem.getTitle());
	}
	
	/**
	 * 根据所传参数精准刷新右边操作菜单
	 * @param item
	 * @param subItem
	 */
	public void refreshByParams(EccTreeItem item, EccTreeItem subItem){
		this.treeItem = item;
		treePanel.setTitle(treeItem.getTitle());
		treePanel.getCaption().setTooltiptext(treeItem.getTitle());
		treeMenu.refresh(treeItem);
		treePanel.setVisible(true);
		
		if(subItem == null)
		{
			listPanel.setVisible(false);
			return;
		}
		
		listItem = subItem;
		listMenu.refresh(listItem);
		listPanel.setVisible(true);
		if(listItem.getTitle().length()>12){
			listPanel.setTitle(listItem.getTitle().substring(0, 12)+"...");
		}else{
			listPanel.setTitle(listItem.getTitle());
		}
		listPanel.getCaption().setTooltiptext(listItem.getTitle());
		
		HashMap<String, EccTreeItem> map = new HashMap<String, EccTreeItem>();
		map.put("treeItem", treeItem);
		map.put("listItem", listItem);
		Session session = Executions.getCurrent().getDesktop().getSession();
		session.setAttribute("actionMenuDivMemory", map);
	}
	
	public void refreshByParams(EccTreeItem item){
		this.treeItem = item;
		treePanel.setTitle(treeItem.getTitle());
		treePanel.getCaption().setTooltiptext(treeItem.getTitle());
		treeMenu.refresh(treeItem);
		treePanel.setVisible(true);
		
		if(treeItem.getChildRen().size()<=0)
		{
			listPanel.setVisible(false);
			return;
		}
		
		listItem = treeItem.getChildRen().get(0);
		listMenu.refresh(listItem);
		listPanel.setVisible(true);
		if(listItem.getTitle().length()>12){
			listPanel.setTitle(listItem.getTitle().substring(0, 12)+"...");
		}else{
			listPanel.setTitle(listItem.getTitle());
		}
		listPanel.getCaption().setTooltiptext(listItem.getTitle());
		
		HashMap<String, EccTreeItem> map = new HashMap<String, EccTreeItem>();
		map.put("treeItem", treeItem);
		map.put("listItem", listItem);
		Session session = Executions.getCurrent().getDesktop().getSession();
		session.setAttribute("actionMenuDivMemory", map);
	}
	
	public void onCreate() {
		

		treePanel.setClosable(false);
		treePanel.setCollapsible(true);
		treePanel.setFramable(false);
		treePanel.setParent(this);
		treePanel.appendChild(new Panelchildren());

		listPanel.setClosable(false);
		listPanel.setCollapsible(true);
		listPanel.setFramable(false);
		listPanel.setParent(this);
		listPanel.appendChild(new Panelchildren());

		treeMenu.setAutoAppendBathMenu(true);
		
		//Separator se=new Separator();
		//se.setBar(true);
		//new Separator().setParent(panel1.getPanelchildren());
		//new Separator().setParent(panel.getPanelchildren());
		

		
		
		treeMenu.getGrid().setStyle("border:none");
		listMenu.getGrid().setStyle("border:none");
		
		treeMenu.getGrid().setParent(treePanel.getPanelchildren());
		listMenu.getGrid().setParent(listPanel.getPanelchildren());

		refreshMenu();
		
		EccTimer eccTimer = (EccTimer) getDesktop().getPage("eccmain")
		.getFellow("header_timer");
		
		eccTimer.addTimerListener("actionMenuDiv", this);
		this.refresh();
	}
	
	private void refresh(){
		Session session = Executions.getCurrent().getDesktop().getSession();
		HashMap<String, EccTreeItem> map = (HashMap<String, EccTreeItem>)session.getAttribute("actionMenuDivMemory");
		if(map == null){
			return;
		}
		treeItem = map.get("treeItem");
		listItem = map.get("listItem");
		treeMenu.refresh(this.treeItem);
		treePanel.setVisible(true);
		treePanel.setTitle(treeItem.getTitle());
		treePanel.getCaption().setTooltiptext(treeItem.getTitle());
		
		listMenu.refresh(listItem);
		listPanel.setVisible(true);
		if(listItem.getTitle().length()>12){
			listPanel.setTitle(listItem.getTitle().substring(0, 12)+"...");
		}else{
			listPanel.setTitle(listItem.getTitle());
		}
		listPanel.getCaption().setTooltiptext(listItem.getTitle());
	}

	public void refreshMenu() {
		//某些处理不需要在actionMenuDiv中自动刷新
		Session session = Executions.getCurrent().getDesktop().getSession();
		HashMap<String, String> doMap = (HashMap<String, String>)session.getAttribute("doMap");
		if(doMap !=null && ("editDevice".equals(doMap.get("dowhat")) || "addDevice".equals(doMap.get("dowhat"))
				 || "addGroup".equals(doMap.get("dowhat")) || "addMonitor".equals(doMap.get("dowhat"))
				 || "delGroup".equals(doMap.get("dowhat")) || "delDevice".equals(doMap.get("dowhat"))
				 || "delMonitor".equals(doMap.get("dowhat")))){
			return;
		}
		if(doMap != null && 
				("disabled".equals(doMap.get("dowhat")) || "startup".equals(doMap.get("dowhat")))){
			refreshDisabledOrStartup(doMap);
			return;
		}
		try {
			Tree tree = (Tree) getDesktop().getPage("eccmain")
					.getFellow("tree");
			
			
			if (tree.getSelectedItem() != null)
				this.treeItem = (EccTreeItem) tree.getSelectedItem().getValue();
			else
			{
				Iterator iterator = tree.getTreechildren().getVisibleChildrenIterator();
				if (iterator.hasNext())
				{
					Treeitem topNode = (Treeitem) iterator.next();
					topNode.setOpen(true);
					this.treeItem = (EccTreeItem) topNode.getValue();
				}
				
			}
			
			EccTreeItem itemInSession = (EccTreeItem)session.getAttribute("selectedItem");
			doMap = (HashMap<String, String>)session.getAttribute("doMap");
			String dowhat = "";
			String svId = "";
			String type = "";
			if(doMap!=null){
				dowhat = doMap.get("dowhat");
				svId = doMap.get("svId");
				type = doMap.get("type");
				if(svId != null && svId.indexOf(",") != -1){
					svId = svId.substring(0, svId.indexOf(","));
				}

				if("group".equals(type) || "entity".equals(type)){
					if("disabled".equals(dowhat)){
						treeItem.setStatus(16);
					}else if("startup".equals(dowhat)){
						treeItem.setStatus(1);
					}
				}
			}
			
			if(itemInSession != null && itemInSession.getType().equals(treeItem.getType())){
				treeItem = itemInSession.getParent();
			}
			
			if(this.treeItem!=null)
			{
				treeMenu.refresh(this.treeItem);
				treePanel.setVisible(true);
				treePanel.setTitle(treeItem.getTitle());
				treePanel.getCaption().setTooltiptext(treeItem.getTitle());
			}else
			{
				treePanel.setVisible(false);
			}
			
			
			if (this.treeItem != null)
			{	
				if(treeItem.getChildRen()!=null && this.treeItem.getChildRen().size()>0)
				{
					if(itemInSession != null){
						for(EccTreeItem item : treeItem.getChildRen()){
							if(item.getId().equals(itemInSession.getId())){
								listItem = itemInSession;
								break;
							}
						}
					}else{
						Object objectIndex = session.getAttribute(ConstantValues.Index_LinkedMonitor);
						session.removeAttribute(ConstantValues.Index_LinkedMonitor);
						if(objectIndex!= null){
							int index = -1;
							try{
								index = Integer.parseInt(String.valueOf(objectIndex));
							}catch(Exception e){
								index = 0;
							}
							if(index > -1){
								listItem = treeItem.getChildRen().get(index);
							}else{
								listItem = treeItem.getChildRen().get(0);
							}
						}else{
							listItem = treeItem.getChildRen().get(0);
						}
					}
					if("disabled".equals(dowhat)){
						listItem.setStatus(16);
					}else if("startup".equals(dowhat)){
						listItem.setStatus(1);
					}
					if(listItem == null){
						listPanel.setVisible(false);
						return;
					}
					
					listMenu.refresh(listItem);
					listPanel.setVisible(true);
					listPanel.setTitle(listItem.getTitle());
					listPanel.getCaption().setTooltiptext(listItem.getTitle());
				}
				else{
					listPanel.setVisible(false);
				}
			}else
			{
				listPanel.setVisible(false);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 在某节点下没有可显示的子节点时刷新菜单的方法
	 * @param eccItem
	 */
	public void listPanelUnVisible(EccTreeItem eccItem){
		if("group".equals(eccItem.getType()) || "se".equals(eccItem.getType())){
			treeItem = eccItem;
			treeMenu.refresh(this.treeItem);
			treePanel.setVisible(true);
			treePanel.setTitle(treeItem.getTitle());
			treePanel.getCaption().setTooltiptext(treeItem.getTitle());
			
			if(eccItem.getChildRen() != null && !eccItem.getChildRen().isEmpty()){
				listItem = eccItem.getChildRen().get(0);
				listMenu.refresh(listItem);
				listPanel.setVisible(true);
				listPanel.setTitle(listItem.getTitle());
				listPanel.getCaption().setTooltiptext(listItem.getTitle());
			}else{
				listPanel.setVisible(false);
			}
		}else if("entity".equals(eccItem.getType())){
			treeItem = eccItem;
			treeMenu.refresh(this.treeItem);
			treePanel.setVisible(true);
			treePanel.setTitle(treeItem.getTitle());
			treePanel.getCaption().setTooltiptext(treeItem.getTitle());
			
			listPanel.setVisible(false);
		}else if("monitor".equals(eccItem.getType())){
			treeItem = eccItem.getParent();
			treeMenu.refresh(this.treeItem);
			treePanel.setVisible(true);
			treePanel.setTitle(treeItem.getTitle());
			treePanel.getCaption().setTooltiptext(treeItem.getTitle());
			
			listPanel.setVisible(false);
		}
	}
	
	/**
	 * 禁止和启动监测器、设备、组的菜单刷新方法
	 * @param doMap
	 */
	private void refreshDisabledOrStartup(HashMap<String, String> doMap){
		String dowhat = doMap.get("dowhat");
		String svId = doMap.get("svId");
		String type = doMap.get("type");
		if(svId == null	|| type == null){
			return;
		}
		if(svId.indexOf(",") != -1){
			svId = svId.substring(0, svId.indexOf(","));
		}
		
		int status = 0;
		if("disabled".equals(dowhat)){
			status = 16;
		}else if("startup".equals(dowhat)){
			status = 1;
		}
		
		Tree tree = (Tree) getDesktop().getPage("eccmain").getFellow("tree");
		if (tree.getSelectedItem() != null)
			this.treeItem = (EccTreeItem) tree.getSelectedItem().getValue();
		else
		{
			Iterator iterator = tree.getTreechildren().getVisibleChildrenIterator();
			if (iterator.hasNext())
			{
				Treeitem topNode = (Treeitem) iterator.next();
				topNode.setOpen(true);
				this.treeItem = (EccTreeItem) topNode.getValue();
			}
		}
		
		Session session = Executions.getCurrent().getDesktop().getSession();
		EccTreeItem itemInSession = (EccTreeItem)session.getAttribute("selectedItem");
		if(itemInSession != null){
			if("group".equals(type)){
				if(svId.equals(itemInSession.getId())){
					listItem = itemInSession;
					listItem.setStatus(status);
				}else if(svId.equals(itemInSession.getParent().getId())){
					treeItem.setStatus(status);
					listItem = itemInSession;
					listItem.setStatus(status);
				}			
			}else if("entity".equals(type)){
				if(svId.equals(itemInSession.getId())){
					listItem = itemInSession;
					listItem.setStatus(status);
				}else if(svId.equals(itemInSession.getParent().getId())){
					treeItem.setStatus(status);
					listItem = itemInSession;
					listItem.setStatus(status);
				}
			}else if("monitor".equals(type) && svId.equals(itemInSession.getId())){
				listItem = itemInSession;
				listItem.setStatus(status);
			}
			
			if(type.equals(treeItem.getType()) && svId.equals(treeItem.getId())){
				treeItem.setStatus(status);
				listItem.setStatus(status);
			}else{
				listItem.setStatus(status);
			}
		}else{
			if(type.equals(treeItem.getType()) && svId.equals(treeItem.getId())){
				treeItem.setStatus(status);
				listItem.setStatus(status);
			}else{
				listItem.setStatus(status);
			}
		}
		
		treeMenu.refresh(this.treeItem);
		treePanel.setVisible(true);
		treePanel.setTitle(treeItem.getTitle());
		treePanel.getCaption().setTooltiptext(treeItem.getTitle());
		
		listMenu.refresh(listItem);
		listPanel.setVisible(true);
		listPanel.setTitle(listItem.getTitle());
		listPanel.getCaption().setTooltiptext(listItem.getTitle());
	}
}
