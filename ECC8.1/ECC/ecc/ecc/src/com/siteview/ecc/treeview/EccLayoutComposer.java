package com.siteview.ecc.treeview;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.zkoss.lang.reflect.FusionInvoker;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.ComposerExt;
import org.zkoss.zk.ui.util.DesktopCleanup;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.South;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.event.TreeDataEvent;
import org.zkoss.zul.event.TreeDataListener;

import com.siteview.actions.EccActionManager;
import com.siteview.base.data.VirtualView;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;
import com.siteview.base.queue.QueueManager;
import com.siteview.base.tree.INode;
import com.siteview.ecc.controlpanel.ControlLayoutComposer;
import com.siteview.ecc.general.ShowInVirtualViewBean;
import com.siteview.ecc.report.top10.DisplayComponent;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.treeview.windows.ConstantValues;
import com.siteview.ecc.util.FavouriteSelect;
import com.siteview.ecc.util.Toolkit;

public class EccLayoutComposer extends GenericForwardComposer implements
		ComposerExt, TreeDataListener {


	private Tree tree;
	private Tree treeEditting;
	private Include eccBody;
	private Listbox viewSelect;
	private Borderlayout main;
	private Combobox locateEntityCombox;
//	private Combobox idCombox;
	private HashMap<String, List> history = new HashMap<String, List>();

	private Image imgClear;
	private Image btnRefreshTree;
//	private Image idImgClear;

	private EccTimer header_timer;
	private Image btnLogout;
	private Image btnHelp;
	
	private Tab tab_monitor;
	private Tab tab_summaryview;
	
	private boolean isClickImg;
	private boolean isClickIpImg;
	private String selectedItemId;
	private Listbox recentlyViewMonitor;
	private South south;
	
	@Override
	public void onChange(TreeDataEvent event) {
		if (event.getType() == TreeDataEvent.INTERVAL_ADDED) {
			EccTreeItem parentNode = (EccTreeItem) event.getParent();
			if (parentNode.getType().equals(INode.ENTITY))
				return;/* 添加的是监测器,则不处理 */
			Treeitem parentItem = findTreeItem(tree.getSelectedItem(),
					parentNode);/* 找到父节点 */
			if (parentItem != null)
				parentItem.setOpen(true);
			// int idx=(event.getIndexFrom()>=0)?event.getIndexFrom():0;
			// EccTreeItem addedNode=parentNode.getChildRen().get(idx);
			// Toolkit.getToolkit().autoExpandTreeNode(tree,
			// addedNode.getValue());
		}

	}

	private Treeitem findTreeItem(Treeitem parentItem, EccTreeItem eccTreeItem) {
		if (eccTreeItem.equals(parentItem.getValue()))
			return parentItem;

		if (parentItem.getTreechildren() != null)
			for (Object item : parentItem.getTreechildren().getItems()) {
				Treeitem finded = findTreeItem((Treeitem) item, eccTreeItem);
				if (finded != null)
					return finded;
			}
		return null;
	}

	@Override
	public void doFinally() throws Exception {
		// TODO Auto-generated method stub
	}

	/**
	 * 根据source中的信息得到相应节点信息，并跳转显示。
	 * 根据event是否为null以及事件产生来源分别调用对应的方法。
	 * @param source
	 * @param event
	 */
	public void locateEntity(String source, Event event) {
		if (source.trim().length() == 0) {
			setDisplayByMap(new HashMap<String, EccTreeItem>(), event);
			return;
		}
		
		HashMap<String, EccTreeItem> dispMap = new HashMap<String, EccTreeItem>();
		if(event != null && "locateEntityCombox".equals(event.getTarget().getId())){
			dispMap = findEntity(((EccTreeModel) tree.getModel()).getRoot(), source,new HashMap<String, EccTreeItem>());
		}else if(event == null || "idCombox".equals(event.getTarget().getId())){
			dispMap = findMonitor(((EccTreeModel)tree.getModel()).getRoot(), source, new HashMap<String, EccTreeItem>());
		}
		setDisplayByMap(dispMap, event);
	}

	/**
	 * 根据事件类型和dispMap中关于节点的信息进行跳转。
	 * @param dispMap
	 * @param event
	 */
	private void setDisplayByMap(HashMap<String, EccTreeItem> dispMap, Event event) {
		Treeitem firstEntityNode = null;
		Treeitem[] treeNodes = new Treeitem[tree.getItems().size()];
		tree.getItems().toArray(treeNodes);
		for (Treeitem item : treeNodes) {
			if (!isMonitorNode(item))
				continue;
			if (dispMap.size() == 0) {
				item.setVisible(true);
				continue;
			}

			if(event == null){
				if (firstEntityNode == null)
					firstEntityNode = openTreeItemById(item, dispMap, firstEntityNode);
				else
					openTreeItemById(item, dispMap, null);
			}
			if(event != null && "locateEntityCombox".equals(event.getTarget().getId())){
				if (firstEntityNode == null)
					firstEntityNode = openTreeItem(item, dispMap, firstEntityNode);
				else
					openTreeItem(item, dispMap, null);
			}
			if(event != null && "idCombox".equals(event.getTarget().getId())){
				if (firstEntityNode == null)
					firstEntityNode = openTreeItemById(item, dispMap, firstEntityNode);
				else
					openTreeItemById(item, dispMap, null);
			}
		}
		
		if (firstEntityNode != null){
			Toolkit.getToolkit().expandTreeAndShowList(tree.getDesktop(),((EccTreeItem) firstEntityNode.getValue()).getValue());
			EccTreeItem monitorItem = getMonitorItemById(firstEntityNode, dispMap);
			ControlLayoutComposer composer = null;
			if(event != null){
				composer = (ControlLayoutComposer) event.getTarget().getDesktop().getPage("controlPage").getFellow("controlLayout").getAttribute("Composer");
			}else{
				composer = (ControlLayoutComposer) Executions.getCurrent().getDesktop().getPage("controlPage").getFellow("controlLayout").getAttribute("Composer");
			}
			composer.refreshByView(tree, monitorItem);
		}else if(event != null && !isClickImg && "locateEntityCombox".equals(event.getTarget().getId())){
			try {
				Messagebox.show("该设备不存在", "提示", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			locateEntityCombox.setValue("");
			imgClear.setVisible(false);
		}
//		else if(event == null || (!isClickIpImg && "idCombox".equals(event.getTarget().getId()))){
//			try {
//				Messagebox.show("该监测器不存在", "提示", Messagebox.OK, Messagebox.INFORMATION);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			idCombox.setValue("");
//			idImgClear.setVisible(false);
//		}
	}

	private Treeitem openTreeItem(Treeitem treeitem,
			HashMap<String, EccTreeItem> dispMap, Treeitem firstEntityNode) {
		EccTreeItem eccTreeItem = (EccTreeItem) treeitem.getValue();
		if (dispMap.get(eccTreeItem.getId()) == null)
			((Treeitem) treeitem).setVisible(false);
		else {
			treeitem.setVisible(true);
			if (eccTreeItem.getType().equals(INode.ENTITY))
				return treeitem;
			treeitem.setOpen(true);
			if (treeitem.getTreechildren() != null) {
				Object items[] = new Object[treeitem.getTreechildren()
						.getItems().size()];
				treeitem.getTreechildren().getItems().toArray(items);
				for (Object o : items) {
					if (firstEntityNode == null)
						firstEntityNode = openTreeItem((Treeitem) o, dispMap,
								firstEntityNode);
					else
						openTreeItem((Treeitem) o, dispMap, null);
				}
			}
		}
		return firstEntityNode;
	}
	
	/**
	 * 迭代treeitem下所有节点，找到dispMap中存在的设备节点并返回
	 * @param treeitem
	 * @param dispMap
	 * @param firstEntityNode
	 * @return firstEntityNode
	 */
	private Treeitem openTreeItemById(Treeitem treeitem, HashMap<String, EccTreeItem> dispMap, Treeitem firstEntityNode){
		EccTreeItem eccTreeItem = (EccTreeItem) treeitem.getValue();
		if (dispMap.get(eccTreeItem.getId()) == null)
			treeitem.setOpen(false);
		else {
			treeitem.setVisible(true);
			if (eccTreeItem.getType().equals(INode.ENTITY)){
				return treeitem;
			}
			treeitem.setOpen(true);
			if (treeitem.getTreechildren() != null) {
				Object items[] = new Object[treeitem.getTreechildren()
						.getItems().size()];
				treeitem.getTreechildren().getItems().toArray(items);
				for (Object o : items) {
					if (firstEntityNode == null)
						firstEntityNode = openTreeItemById((Treeitem) o, dispMap,
								firstEntityNode);
					else
						openTreeItemById((Treeitem) o, dispMap, null);
				}
			}
		}
		return firstEntityNode;
	}
	
	/**
	 * 迭代firstEntityNode下所有子节点，找到选中的监测器节点并返回
	 * @param treeitem
	 * @param dispMap
	 * @return monitorItem
	 */
	private EccTreeItem getMonitorItemById(Treeitem treeitem, HashMap<String, EccTreeItem> dispMap){
		EccTreeItem monitorItem = null;
		List<EccTreeItem> list = ((EccTreeItem) treeitem.getValue()).getChildRen();
		for(EccTreeItem item : list){
			if (dispMap.get(item.getId()) != null){
				monitorItem = item;
			}
		}
		if(monitorItem == null){
			EccTreeItem eccItem = (EccTreeItem)treeitem.getValue();
			if(dispMap.get(eccItem.getId()) != null){
				monitorItem = eccItem;
			}
		}
		return monitorItem; 
	}

	private boolean isMonitorNode(Treeitem node) {
		Object nodeValue = node.getValue();
		if (nodeValue == null || !(nodeValue instanceof EccTreeItem))
			return false;
		if (((EccTreeItem) nodeValue).isMonitorTreeNode())
			return true;

		return false;
	}

	private HashMap<String, EccTreeItem> findEntity(EccTreeItem startNode,
			String entityName, HashMap<String, EccTreeItem> dispMap) {
		for (EccTreeItem item : ((EccTreeModel) tree.getModel())
				.getAllEntity(entityName)) {
			if (item.getTitle().equals(entityName))
				addDispMapByPath(item, dispMap);
		}
		return dispMap;
	}
	
	/**
	 * 根据monitorId在startNode下找到符合条件的节点存入dispMap中，并返回。
	 * 根据monitorId查询监测器不进行模糊查询
	 * @param startNode
	 * @param monitorId
	 * @param dispMap
	 * @return dispMap
	 */
	private HashMap<String, EccTreeItem> findMonitor(EccTreeItem startNode, String monitorId, HashMap<String, EccTreeItem> dispMap){
		for (EccTreeItem item : ((EccTreeModel) tree.getModel())
				.getAllMonitor(monitorId, false)) {
			if (item.getId().equals(monitorId))
				addDispMapByPath(item, dispMap);
		}
		return dispMap;
	}

	private void addDispMapByPath(EccTreeItem treeitem,
			HashMap<String, EccTreeItem> dispMap) {
		dispMap.put(treeitem.getId(), treeitem);
		if (treeitem.getParent() != null)
			addDispMapByPath(treeitem.getParent(), dispMap);
	}

	public void onSelect$tree(SelectEvent arg0) {
		try{
			SelectEvent e = (SelectEvent) arg0;
			if (!e.getSelectedItems().isEmpty()) {

				Treeitem treeitem = (Treeitem) e.getSelectedItems().iterator()
						.next();
				treeitem.setOpen(true);
				EccTreeItem obj = (EccTreeItem) treeitem.getValue();
				Session session = Executions.getCurrent().getDesktop().getSession();
				session.removeAttribute(ConstantValues.RefreshMonitorId);
				if("entity".equals(obj.getType()) && obj.getChildRen()!=null && obj.getChildRen().size()>0){
					EccTreeItem itemInSession = (EccTreeItem)session.getAttribute("selectedItem");
					if(itemInSession == null || !obj.getId().equals(itemInSession.getParent().getId())){
						session.setAttribute("selectedItem", obj.getChildRen().get(0));
						session.removeAttribute("doMap");
					}else if(obj.getId().equals(itemInSession.getParent().getId())){
						//不对session进行操作
					}else{
						session.setAttribute("selectedItem", obj.getChildRen().get(0));
					}
					
					EccTreeItem _eccItem = obj.getChildRen().get(0);
					LinkedList<EccTreeItem> recentlyViewMonitors = (LinkedList<EccTreeItem>)session.getAttribute("recentlyViewMonitors");
					if(recentlyViewMonitors == null || recentlyViewMonitors.isEmpty()){
						//若recentlyViewMonitors为null或里面没有值，初始化，并添加第一个
						recentlyViewMonitors = new LinkedList<EccTreeItem>();
						recentlyViewMonitors.addFirst(_eccItem);
					}else{
						//遍历recentlyViewMonitors，删除掉相同的节点
						int index = -1;
						for(int i=0;i<recentlyViewMonitors.size();i++){
							if(_eccItem.getId().equals(recentlyViewMonitors.getFirst().getId())){
								//若recentlyViewMonitors中第一个节点与当前节点好似同一节点，则不需进行添加
								break;
							}
							EccTreeItem _item = recentlyViewMonitors.get(i);
							if(_item.getId().equals(_eccItem.getId())){
								index = i;
							}
						}
						if(index != -1){
							recentlyViewMonitors.remove(index);
						}
						
						if(_eccItem.getId().equals(recentlyViewMonitors.getFirst().getId())){
							//若recentlyViewMonitors中第一个节点与当前节点好似同一节点，则不需进行添加
						}else{
							recentlyViewMonitors.addFirst(_eccItem);
						}
					}
					session.setAttribute("recentlyViewMonitors", recentlyViewMonitors);
				}else if("entity".equals(obj.getType()) && obj.getChildRen()!=null && obj.getChildRen().size()<=0){
					session.setAttribute("selectedItem", null);
					session.removeAttribute("doMap");
				}else{
					session.removeAttribute("doMap");
				}
				this.selectedItemId = obj.getItemId();
				//将在树中点击Treeitem节点的父节点保存到session中
				session.setAttribute("selectedTreeItem", treeitem);
				session.setAttribute("selectedParentTreeItem", treeitem.getParentItem());
				
				String viewSelected = viewSelect.getSelectedItem().getLabel();
				String url = "";
				//如果使用了虚拟视图并且点击树中的功能节点，跳转到/main/showInVirtualView.zul页面
				if(!("monirot".equals(obj.getType()) || "entity".equals(obj.getType()) || "group".equals(obj.getType()) || "se".equals(obj.getType())) 
						&& !VirtualView.DefaultView.equals(viewSelected) && obj.getChildRen() != null && !obj.getChildRen().isEmpty()){
					url = "/main/showInVirtualView.zul" + "?" + obj.getChildRen();
					ShowInVirtualViewBean bean = new ShowInVirtualViewBean();
					bean.setNodeName(obj.getTitle());
					ArrayList<String> sonNames = new ArrayList<String>();
					for(EccTreeItem item : obj.getChildRen()){
						sonNames.add(item.getTitle());
					}
					bean.setSonNames(sonNames);
					session.setAttribute("showInVirtualViewBean", bean);
				}else{
					url = EccActionManager.getInstance().getUrl(obj.getType());
				}
				// String bookmark=makeBookMark(obj);
				
				this.refreshRecentlyViewMonirot();
				if (url.equals("/main/control/eccbody.zul")) {
					if (desktop.hasPage("controlPage")) {
						Page page = desktop.getPage("controlPage");
						if (page.hasFellow("controlLayout")) {

							ControlLayoutComposer clc = ((ControlLayoutComposer) page
									.getFellow("controlLayout").getAttribute(
											"Composer"));
							//筛选session中的数据
							EccTreeItem eccItem = (EccTreeItem)session.getAttribute("selectedItem");
							if(eccItem != null){
								session.setAttribute("selectedItem", this.getEccTreeItemWithFilter(eccItem, clc));
							}
							clc.setIsSelected(false);
							clc.refresh(e);
							return;
						}
					}
					
				}
				eccBody.setSrc(url);
				// Clients.showBusy("", true);/* 显示滚动球球 */
				// Events.echoEvent("onOk", eccBody, null);

				/*
				 * eccBody.setSrc(new StringBuffer(url).append("?type=").append(
				 * obj.getType()).append("&id=").append(obj.getId()) .toString());
				 */
				// e.getTarget().getDesktop().setBookmark(bookmark);
				// history.put(bookmark, eccBody.getChildren());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private String makeBookMark(EccTreeItem item) {
		if (item.getType().equals(INode.ENTITY)
				|| item.getType().equals(INode.SE)
				|| item.getType().equals(INode.GROUP)
				|| item.getType().equals(INode.MONITOR)) {
			return new StringBuilder(item.getType()).append(",").append(
					item.getId()).toString();
		} else {
			return item.getType();
		}
	}

	public void onOk$eccBody() {
		Clients.showBusy(null, false);/* 取消滚动球球 */
	}

	//	
	public void onSelect$viewSelect(SelectEvent event) {
		Listitem item = viewSelect.getSelectedItem();
		if (item != null) {
			Session zkuiSession = Executions.getCurrent().getDesktop().getSession();

			if (!item.getLabel().equals(((EccTreeModel) tree.getModel()).getRoot().getId())) {
				EccTreeModel.removeInstance(session, item.getLabel());
				if (item.getLabel().equals(VirtualView.DefaultView)){
					zkuiSession.setAttribute("selectedViewName", item.getLabel());
					
					tree.setModel(com.siteview.ecc.treeview.EccTreeModel.getInstance(session, item.getLabel()));
					zkuiSession.setAttribute("eccWholeViewTreeModel", com.siteview.ecc.treeview.EccTreeModel.getInstance(session, item.getLabel()));
				}
				else{
					zkuiSession.setAttribute("selectedViewName", item.getLabel());
					tree.setModel(com.siteview.ecc.treeview.VirtualViewTreeModel
									.getInstance(session, item.getLabel()));
					zkuiSession.setAttribute("eccWholeViewTreeModel", com.siteview.ecc.treeview.VirtualViewTreeModel
							.getInstance(session, item.getLabel()));
				}
			}
		}

		header_timer.addTimerListener("eccTreeMenu", (EccTreeModel) tree
				.getModel());

		Treechildren tchildren = (Treechildren)tree.getFirstChild();
		Treeitem treeitem = (Treeitem)tchildren.getFirstChild();
		treeitem.setOpen(true);
		EccTreeItem eti = null;
		if("WholeView".equals(((EccTreeItem)treeitem.getValue()).getType())){
			Treechildren subTchildren = (Treechildren)treeitem.getTreechildren();
			if(subTchildren != null){
				Treeitem subTreeitem = (Treeitem)subTchildren.getFirstChild();
				eti = (EccTreeItem) subTreeitem.getValue();
				tree.selectItem(subTreeitem);
			}else{
				eti = (EccTreeItem) treeitem.getValue();
				tree.selectItem(treeitem);
			}
		}else{
			eti = (EccTreeItem)treeitem.getValue();
		}

		String url = EccActionManager.getInstance().getUrl(eti.getType());		
		if (url.equals("/main/control/eccbody.zul")) {
			if (desktop.hasPage("controlPage")) {
				Page page = desktop.getPage("controlPage");
				if (page.hasFellow("controlLayout")) {
					ControlLayoutComposer clc = ((ControlLayoutComposer) page
							.getFellow("controlLayout").getAttribute(
							"Composer"));
					clc.refreshByView(tree, eti);
					
					EccHeaderComposer ehc = (EccHeaderComposer)desktop.getPage("eccmain").getFellow("eccHeaderComposer").getAttribute("Composer");
					ehc.refreshByView(tree);
					return;
				}
			}

		}
		eccBody.setSrc(url);
	}

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent,
			ComponentInfo compInfo) {
		return compInfo;
	}

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		Object obj = FusionInvoker.newInstance(new Object[] { comp, this });
		comp.setVariable("main", obj, true);
		main = (Borderlayout) comp;
	}

	@Override
	public boolean doCatch(Throwable ex) throws Exception {
		ex.printStackTrace();
		return false;
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		self.setAttribute("Composer", this);

		header_timer.addTimerListener("headerInfo", (EccHeaderComposer)desktop.getPage("eccmain").getFellow("eccHeaderComposer").getAttribute("Composer"));

		Components.addForwards(comp, this);
		// 获取基础树和虚拟视图列表
		Object usersessionid = this.desktop.getSession().getAttribute(
				"usersessionid");
		if (usersessionid == null) {
			Executions.getCurrent().sendRedirect("/index.jsp", "_top");
			return;
		}
		this.desktop.getSession().setAttribute("desktop_"+(String)usersessionid, this.desktop);
		View w = Manager.getView(usersessionid.toString());
		if (w == null)
			return;

		// 暂时将默认视图设为基础树
		int nSelIndex = 0;
		List<VirtualView> av = w.getAllVirtualView();
		Collections.sort(av, new Comparator<VirtualView>(){
			@Override
			public int compare(VirtualView o1, VirtualView o2) {
				return o1.getViewName().compareToIgnoreCase(o2.getViewName());
			}
		});
		String viewName_str = "";
		try{
			viewName_str =(String)session.getAttribute("selectedViewName");
		}catch(Exception e){}
		
		if(viewName_str == null || "".equals(viewName_str)){
			for (VirtualView v : av) {
				viewSelect.appendItem(v.getViewName(), v.getViewName());
				if (v.getViewName().equals(VirtualView.DefaultView))
					nSelIndex = viewSelect.getItemCount() - 1;
			}
			viewSelect.setSelectedIndex(nSelIndex);
		}else{
			for (VirtualView v : av) {
				viewSelect.appendItem(v.getViewName(), v.getViewName());
				if (v.getViewName().equals(viewName_str))
					nSelIndex = viewSelect.getItemCount() - 1;
			}
			viewSelect.setSelectedIndex(nSelIndex);
		}

/*		for (VirtualView v : av) {
			viewSelect.appendItem(v.getViewName(), v.getViewName());
			if (v.getViewName().equals(VirtualView.DefaultView))
				nSelIndex = viewSelect.getItemCount() - 1;
		}
		viewSelect.setSelectedIndex(nSelIndex);
*/

		EccTreeModel model = EccTreeModel.getInstance(session);
		tree.setModel(model);
		model.addTreeDataListener(this);
		tree.setTreeitemRenderer(new EccTreeNodeRender());

		header_timer.addTimerListener("eccTreeMenu", model);

		ArrayList<String> list = new ArrayList<String>();
		for (EccTreeItem item : ((EccTreeModel) tree.getModel()).getAllEntity("")) {
			if (!list.contains(item.getTitle()))
				list.add(item.getTitle());
		}
		Collections.sort(list);
		locateEntityCombox.setModel(new SimpleListModel(list));
		
		ArrayList<String> idList = new ArrayList<String>();
		for (EccTreeItem item : ((EccTreeModel) tree.getModel()).getAllMonitor("", false)){
			if (!list.contains(item.getId()))
				list.add(item.getId());
		}
		Collections.sort(idList);
//		idCombox.setModel(new SimpleListModel(idList));
		
		final EccLayoutComposer eccLayoutComposer = this;

		EventListener evl = new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getTarget() instanceof Image){
					isClickImg = true;
					locateEntityCombox.setValue("");
				}
				Clients.showBusy("", true);/* 显示滚动球球 */
				Events.echoEvent("onOk", eccBody, null);
				try{
					eccLayoutComposer.locateEntity(locateEntityCombox.getValue(), event);
				}catch(Exception e){
					e.printStackTrace();
				}
				if (locateEntityCombox.getValue().length() > 0)
					imgClear.setVisible(true);
				else
					imgClear.setVisible(false);
				isClickImg = false;
			}
		};
		locateEntityCombox.addEventListener("onChange", evl);
		imgClear.addEventListener("onClick", evl);
		
//		EventListener ipEvent = new EventListener(){
//			@Override
//			public void onEvent(Event event) throws Exception {
//				if (event.getTarget() instanceof Image){
//					isClickIpImg = true;
//					idCombox.setValue("");
//				}
//				Clients.showBusy("", true);/* 显示滚动球球 */
//				Events.echoEvent("onOk", eccBody, null);
//				eccLayoutComposer.locateEntity(idCombox.getValue(), event);
//				if (idCombox.getValue().length() > 0)
//					idImgClear.setVisible(true);
//				else
//					idImgClear.setVisible(false);
//				isClickIpImg = false;
//			}};
//		idCombox.addEventListener("onChange", ipEvent);
//		idImgClear.addEventListener("onClick", ipEvent);

		/*
		 * 不需要bookmark,影响效率 main.addEventListener("onBookmarkChange", new
		 * EventListener() { public void onEvent(Event event) throws Exception {
		 * String bookmark=((BookmarkEvent)event).getBookmark();
		 * 
		 * while (eccBody.getLastChild() != null)
		 * eccBody.removeChild(eccBody.getLastChild());
		 * 
		 * List children=history.get(bookmark); if(children!=null) for(Object
		 * comp:children) ((Component)comp).setParent(eccBody); } });
		 */

		desktop.addListener(new DesktopCleanup() {
			@Override
			public void cleanup(Desktop desktop) throws Exception {
				QueueManager.getInstance().removeQueue(header_timer);
			}
		});
		btnLogout.addEventListener("onClick", new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				QueueManager.getInstance().removeQueue(header_timer);
				Executions.sendRedirect("/main/logout.jsp");
			}
		});
/*		btnHelp.addEventListener("onClick", new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				Executions.getCurrent().sendRedirect("javascript:window.showHelp('/main/help.chm')");
			}
		});
*/
		btnRefreshTree.addEventListener("onClick", new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				clickOnRefreshImage();
			}
		});

		tab_monitor.addEventListener("onSelect", new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				clickOnRefreshImage();
			}
		});
		tab_summaryview.addEventListener("onSelect", new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				Include cc = (Include)tab_summaryview.getFellow("include_summaryview");
				String src = cc.getSrc();
				cc.setSrc(null);
				cc.setSrc(src);
			}
		});

		getRoot(self).addEventListener("onClientInfo", new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {

				org.zkoss.zk.ui.event.ClientInfoEvent e = (org.zkoss.zk.ui.event.ClientInfoEvent) event;
				tree.setAttribute("screenHeight", e.getScreenHeight());
				tree.setAttribute("screenWidth", e.getScreenWidth());
				tree.setAttribute("desktopHeight", e.getDesktopHeight());
				tree.setAttribute("desktopWidth", e.getDesktopWidth());
			}
		});
		
		Treeitem item=findHomeNode(model);
		if(item!=null)
		{
			HashMap map=new HashMap();
			map.put(item,1);
			SelectEvent se=new SelectEvent("onSelect",item,map.keySet());
			onSelect$tree(se);
		}
		
		//初始化recentlyViewMonitor
		recentlyViewMonitor.setStyle("overflow-x:hidden;border:none");
	}
	
	/**
	 * recentlyViewMonitor的刷新方法
	 */
	public void refreshRecentlyViewMonirot(){
		Session session = Executions.getCurrent().getDesktop().getSession();
		LinkedList<EccTreeItem> recentlyViewMonitors = (LinkedList<EccTreeItem>)session.getAttribute("recentlyViewMonitors");

			RecentViewListModel model = new RecentViewListModel(recentlyViewMonitors);
			recentlyViewMonitor.setModel(model);
			recentlyViewMonitor.setItemRenderer(model);
	}
	
	private Treeitem findHomeNode(EccTreeModel eccTreeModle) {
		
		EccTreeItem curNode = null;
		ArrayList<EccTreeItem> curPath = new ArrayList<EccTreeItem>();

		String nodeId = Toolkit.getToolkit().getCookie("homeEccTreeItem",desktop);
		if (nodeId != null) {
			curNode = eccTreeModle.findNode(nodeId);
			if (curNode != null)
				eccTreeModle.getPathList(curPath, curNode);
		}

		if (curPath.size() == 0) {
			curPath.add(eccTreeModle.getRoot());
			EccTreeItem root = (EccTreeItem) eccTreeModle.getRoot();
			if (root.getChildRen().size() > 0) {
				EccTreeItem top = root.getChildRen().get(0);
				curPath.add(top);
				if (top.getChildRen().size() > 0)
					curPath.add(top.getChildRen().get(0));
			}
		}

		Iterator iterator = tree.getTreechildren().getVisibleChildrenIterator();
		if (iterator.hasNext()) {
			Treeitem topNode = (Treeitem) iterator.next();
			return expandTreeNode(curPath, topNode, curPath.get(curPath.size() - 1));
		}

		return null;
	}
	private Treeitem expandTreeNode(ArrayList<EccTreeItem> curPath,
			Treeitem topNode, EccTreeItem bottom) {

		topNode.setOpen(true);

		if (topNode.getTreechildren() == null
				|| bottom.equals(topNode.getValue())) {
			tree.selectItem(topNode);
			return topNode;
		}

		Iterator iterator = topNode.getTreechildren()
				.getVisibleChildrenIterator();
		while (iterator.hasNext()) {
			Treeitem node = (Treeitem) iterator.next();
			if (curPath.contains(node.getValue())) {
				return expandTreeNode(curPath, node, bottom);
			}
		}
		
		return null;
	}
	void clickOnRefreshImage() {

		Treeitem oldSelTreeItem = tree.getSelectedItem();
		EccTreeItem oldSelEccTreeItem = null;

		// 也许没有选中任何一个树节点
		if (oldSelTreeItem != null)
			oldSelEccTreeItem = (EccTreeItem) oldSelTreeItem.getValue();

		String usersessionid = (String) session.getAttribute("usersessionid");
		if (usersessionid == null) {
			Executions.getCurrent().sendRedirect("/index.jsp", "_top");
			return;
		}
		View w = Manager.getView(usersessionid.toString());
		if (w == null)
			return;

		// 原来选中的视图名称
		String viewName = viewSelect.getSelectedItem().getLabel();
		EccTreeModel.removeInstance(session, viewName);

		// 清空视图列表
		int count = viewSelect.getItemCount();
		for (int i = count - 1; i >= 0; --i)
			viewSelect.removeItemAt(i);

		// 更新视图列表
		int nSelIndex = -1;
		int nDefaultViewIndex = 0;
		List<VirtualView> av = null;
		try {
			av = w.getAllVirtualViewThrowException();
		} catch (Exception e) {
//			System.err.println("Failed to load allVirtualViewName.ini, use WholeView.");
			e.printStackTrace();
		}
		if(av != null){
			for (VirtualView v : av) {
				viewSelect.appendItem(v.getViewName(), v.getViewName());
				if (v.getViewName().equals(viewName))
					nSelIndex = viewSelect.getItemCount() - 1;

				if (v.getViewName().equals(VirtualView.DefaultView))
					nDefaultViewIndex = viewSelect.getItemCount() - 1;
			}
			// 如果原来选中的视图已经被删除，则选中默认视图
			if (nSelIndex == -1)
				nSelIndex = nDefaultViewIndex;

			viewSelect.setSelectedIndex(nSelIndex);
			viewName = viewSelect.getSelectedItem().getLabel(); // 更新所选中视图的名称
		}else{
			viewName = VirtualView.DefaultView;
		}

		// 重新设置 tree model
		if (viewName.equals(VirtualView.DefaultView))
			tree.setModel(com.siteview.ecc.treeview.EccTreeModel.getInstance(
					session, viewName));
		else
			tree.setModel(com.siteview.ecc.treeview.VirtualViewTreeModel
					.getInstance(session, viewName));

		com.siteview.base.manage.Manager.instantUpdate();

		header_timer.addTimerListener("eccTreeMenu", (EccTreeModel) tree
				.getModel());

		if (tree.getItems() != null && tree.getItems().iterator() != null) {
			Treeitem topNode = (Treeitem) tree.getItems().iterator().next();
			if (topNode != null)
				topNode.setSelected(true);
		}

		if (oldSelEccTreeItem != null) {
			if (Toolkit.getToolkit().autoExpandTreeNode(tree,
					oldSelEccTreeItem.getValue()) == false)
				eccBody.setSrc("/main/control/eccbody.zul?time="
						+ System.currentTimeMillis());
		}
	}
	
	public void refreshData(){
		if(selectedItemId==null){
			try {
				Thread.sleep(100);
				clickOnRefreshImage();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Tree getTree(){
		return this.tree;
	}

	public Component getRoot(Component com) {
		if (com.getParent() == null)
			return com;
		return getRoot(com.getParent());
	}
	
	/**
	 * 根据svid查询对应的监测器，并跳转到该监测器显示界面
	 * @param svid
	 */
	public void findItemById(String svid) {
		
		if (svid.trim().length() == 0) {
			setDisplayByMap(new HashMap<String, EccTreeItem>(), null);
			return;
		}
		
		HashMap<String, EccTreeItem> dispMap = new HashMap<String, EccTreeItem>();
		dispMap = findMonitor(((EccTreeModel)tree.getModel()).getRoot(), svid, new HashMap<String, EccTreeItem>());
		setDisplayByMap(dispMap, null);
	}
	
	/**
	 * 根据页面上监测器状态筛选条件与传入eccItem的状态进行判断，返回符合筛选条件的eccItem，反之返回null
	 * @param eccItem
	 * @param clc
	 * @return eccItem or null
	 */
	private EccTreeItem getEccTreeItemWithFilter(EccTreeItem eccItem, ControlLayoutComposer clc){
		FavouriteSelect favSelect = clc.getFavSelect();
		Object obj = favSelect.getSelectedItem().getValue();
		int filter = Integer.parseInt(obj.toString());
		for(int i=0;i<eccItem.getParent().getChildRen().size();i++){
			EccTreeItem eccTreeItem = eccItem.getParent().getChildRen().get(i);
			if((filter&eccTreeItem.getStatus())==eccTreeItem.getStatus()){
				return eccTreeItem;
			}
			continue;
		}
		return null;
	}

}
