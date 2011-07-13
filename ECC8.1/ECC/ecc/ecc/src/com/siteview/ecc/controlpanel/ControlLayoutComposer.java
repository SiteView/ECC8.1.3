package com.siteview.ecc.controlpanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.East;
import org.zkoss.zkex.zul.South;
import org.zkoss.zkex.zul.West;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Span;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.event.PagingEvent;

import com.siteview.actions.ActionMenuDiv;
import com.siteview.actions.ActionMenuOpenListener;
import com.siteview.actions.ButtonGroup;
import com.siteview.actions.ImageButton;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.queue.IQueueEvent;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.EntityInfo;
import com.siteview.base.treeInfo.GroupInfo;
import com.siteview.base.treeInfo.SeInfo;
import com.siteview.ecc.report.common.SelectableListheader;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.timer.NodeInfoBean;
import com.siteview.ecc.timer.TimerListener;
import com.siteview.ecc.treeview.EccHeaderComposer;
import com.siteview.ecc.treeview.EccLayoutComposer;
import com.siteview.ecc.treeview.EccOpenedTreeView;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccTreeModel;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.treeview.windows.ConstantValues;
import com.siteview.ecc.util.FavouriteSelect;
import com.siteview.ecc.util.TitleChangedPanel;
import com.siteview.ecc.util.Toolkit;

public class ControlLayoutComposer extends GenericForwardComposer implements
		TimerListener,java.io.Serializable {

	@Override
	public synchronized void notifyChange(IQueueEvent e) {
		if(e instanceof ChangeDetailEvent){
			ChangeDetailEvent event = (ChangeDetailEvent)e;
			if(event.getType() == ChangeDetailEvent.TYPE_DELETE){
				deleteRefresh(event.getData());
			}else if(event.getType() == ChangeDetailEvent.TYPE_MODIFY){
				modifyRefresh(event.getData());
			}else if(event.getType() == ChangeDetailEvent.TYPE_ADD){
				addRefresh(event.getData());
			}else{
				autoRefresh();
			}
		}
		else{//add 针对 自动刷新功能失效
			autoRefresh();
		}		
	}

	final public int VIEW_LIST = 0;
	final public int VIEW_ICON = 1;
	final public int VIEW_TREE = 2;
	
	private Label curNodeTitle;
	private Image curNodeImg;
	private Div curNodeInfo;
	private Div controlDiv;

	private ActionMenuDiv actionMenuDiv;
	private SeTableModel seTableModel;
	private GroupTableModel groupTableModel;
	private EntityTableModel entityTableModel;
	private MonitorTableModel monitorTableModel;
	private EventListener clickListener;

	private ImageButton btnDisplayInherit;
	private ImageButton btnHiddenInherit;
	private ImageButton btnListView;
	private ImageButton btnIconView;
	private ImageButton btnTreeView;
	private ImageButton btnRightActionVisible;
	private ImageButton btnLeftTreeVisible;

	private Image homeImg;
	private Combobox boxPageSize;

	private Listbox slider_timer;
	private Listbox group_listbox;
	private Listbox monitor_listbox;
	private Listbox entity_listbox;
	private FavouriteSelect favSelect;
	private EccOpenedTreeView opendTree;
	View eccView;
	EccTreeItem selTreeItem;
	Tree tree;
	EccTreeModel eccTreeModle;

	private East actionEast;
	private Borderlayout controlLayout;

	private int viewIconOrList = 0; /* 是否显示为缩略图 */
	private boolean displayInherit = false; /* 是否显示派生 */
	private int filter = EccTreeItem.STATUS_ALL; /* 过滤条件 */

	private ActionMenuOpenListener actionMenuOpenListener = new ActionMenuOpenListener();
	private HashMap<String, Listbox> listBoxMap = new HashMap<String, Listbox>();

	private Panel seListPanel;
	private Panel groupListPanel;
	private Panel entityListPanel;
	private Panel monitorListPanel;

	private Panel seIconPanel;
	private Panel groupIconPanel;
	private Panel entityIconPanel;
	private Panel monitorIconPanel;

	Include monitorinclude;
	South southid;
	TitleChangedPanel propPanel;
	boolean isAutoRefresh;
	boolean isSelected;
	boolean isRefresh;
	int selectedIndex;
	Listitem selectedItem;
	boolean monitorEmpty;
	boolean subRefresh = true;

	private Borderlayout WMonitorInfo;

	public Panel getSeIconPanel() {
		if (seIconPanel == null) {
			seIconPanel = makePanel("");
		}
		return seIconPanel;
	}

	public Panel getGroupIconPanel() {
		if (groupIconPanel == null) {
			groupIconPanel = makePanel("子组");
		}
		return groupIconPanel;
	}

	public Panel getEntityIconPanel() {
		if (entityIconPanel == null) {
			entityIconPanel = makePanel("设备");
		}

		return entityIconPanel;
	}

	public Panel getMonitorIconPanel() {
		if (monitorIconPanel == null)
			monitorIconPanel = makePanel("监测器");

		monitorIconPanel.setVisible(true);
		return monitorIconPanel;
	}

	public Panel getSePanel() {
		if (seListPanel == null) {
			seListPanel = makePanel("");
			Listbox listbox = getListBox(seListPanel, INode.SE, seTableModel);

		} else
			getListBox(seListPanel, INode.SE, seTableModel);

		seListPanel.setVisible(true);
		return seListPanel;
	}

	public Panel getGroupPanel() {
		if (groupListPanel == null) {
			groupListPanel = makePanel("子组");
			Listbox listbox = getListBox(groupListPanel, INode.GROUP,
					groupTableModel);
			this.group_listbox = listbox;
		} else
			this.group_listbox = getListBox(groupListPanel, INode.GROUP, groupTableModel);

		groupListPanel.setVisible(true);
		return groupListPanel;
	}

	public Panel getEntityPanel() {
		if (entityListPanel == null) {
			entityListPanel = makePanel("设备");
			Listbox listbox = getListBox(entityListPanel, INode.ENTITY,
					entityTableModel);
			this.entity_listbox = listbox;
		} else
			this.entity_listbox = getListBox(entityListPanel, INode.ENTITY, entityTableModel);

		entityListPanel.setVisible(true);
		return entityListPanel;
	}

	public Panel getMonitorPanel() {
		if (monitorListPanel == null) {
			monitorListPanel = makePanel("监测器");
			Listbox listbox = getListBox(monitorListPanel, INode.MONITOR,
					monitorTableModel);
			this.monitor_listbox =listbox;
		} else{
			this.monitor_listbox = getListBox(monitorListPanel, INode.MONITOR, monitorTableModel);
		}
			
		//在Listbox中选中记忆的监测器节点
		try{
		Session session = Executions.getCurrent().getDesktop().getSession();
		Integer selListItemIndex = (Integer)session.getAttribute("selListItemIndex");
		
		//读取doMap中的信息
		HashMap<String, String> doMap = (HashMap<String, String>)session.getAttribute("doMap");
		String dowhat = "";
		if(doMap!=null){
			dowhat = doMap.get("dowhat");
		}
		
		if("addMonitor".equals(dowhat)){
			monitor_listbox.setSelectedIndex(selListItemIndex - 1);
		}else if(session.getAttribute(ConstantValues.LatestBrowseMonitorId)!= null){
			session.removeAttribute(ConstantValues.LatestBrowseMonitorId);
			EccTreeItem selectedItem = (EccTreeItem)session.getAttribute("selectedItem");
			EccTreeItem parentItem = selectedItem.getParent();
			int index = parentItem.getChildRen().indexOf(selectedItem);
			if(index != -1){
				monitor_listbox.setSelectedIndex(index);
			}else{
				Messagebox.show("无法跳转，该监测器可能已被删除", "提示", Messagebox.OK, Messagebox.INFORMATION);
				monitor_listbox.setSelectedIndex(0);
			}
			
		}else if(selListItemIndex != null && monitor_listbox.getItemCount() >= selListItemIndex){
			EccTreeItem selectedItem = (EccTreeItem)session.getAttribute("selectedItem");
			Listitem listItem = (Listitem)session.getAttribute("selectedListItem");
			if(listItem != null && selectedItem != null){
				EccTreeItem selTreeEccItem = (EccTreeItem)listItem.getValue();
				if(selTreeEccItem != null && selTreeEccItem.getId().equals(selectedItem.getId())){
					monitor_listbox.setSelectedIndex(selListItemIndex);
				}else if(monitor_listbox.getItemCount() > 0){
					monitor_listbox.setSelectedIndex(0);
				}
			}else if(monitor_listbox.getItemCount() > 0){
				monitor_listbox.setSelectedIndex(0);
			}
		}else if(monitor_listbox.getItemCount() > 0){
			monitor_listbox.setSelectedIndex(0);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		monitorListPanel.setVisible(true);
		return monitorListPanel;
	}

	public int getFilter() {
		return filter;
	}

	public void setFilter(int filter) {
		if (this.filter != filter) {
			this.filter = filter;
			if (viewIconOrList == VIEW_LIST) {
				if (dispList(INode.SE))
					seTableModel.setFilter(filter);
				if (dispList(INode.GROUP))
					groupTableModel.setFilter(filter);
				if (dispList(INode.ENTITY))
					entityTableModel.setFilter(filter);
				if (dispList(INode.MONITOR))
					monitorTableModel.setFilter(filter);

			} else if (viewIconOrList == VIEW_ICON)/* 非绑定,所以要用语句刷新 */
			{
				if (dispList(INode.SE)) {
					seTableModel.setFilter(filter);
					refreshIcon(getSeIconPanel(), seTableModel, seTableModel);
				}
				if (dispList(INode.GROUP)) {
					groupTableModel.setFilter(filter);
					refreshIcon(getGroupIconPanel(), groupTableModel,
							groupTableModel);
				}
				if (dispList(INode.ENTITY)) {
					entityTableModel.setFilter(filter);
					refreshIcon(getEntityIconPanel(), entityTableModel,
							entityTableModel);
				}
				if (dispList(INode.MONITOR)) {
					monitorTableModel.setFilter(filter);
					refreshIcon(getMonitorIconPanel(), monitorTableModel,
							monitorTableModel);
				}

			} else if (viewIconOrList == VIEW_TREE)
				opendTree.refreshStatus(filter);

		}
	}

	public boolean isDisplayInherit() {
		return displayInherit;
	}

	public void setDisplayInherit(boolean displayInherit) {
		if (this.displayInherit != displayInherit) {
			this.displayInherit = displayInherit;
			if (viewIconOrList == VIEW_LIST) {
				if (dispList(INode.SE))
					seTableModel.setInherit(displayInherit);

				if (dispList(INode.GROUP))
					groupTableModel.setInherit(displayInherit);

				if (dispList(INode.ENTITY))
					entityTableModel.setInherit(displayInherit);

				if (dispList(INode.MONITOR))
					monitorTableModel.setInherit(displayInherit);

				getGroupPanel()
						.setVisible(dispList(INode.GROUP) ? true : false);
				getEntityPanel().setVisible(
						dispList(INode.ENTITY) ? true : false);
				getMonitorPanel().setVisible(
						dispList(INode.MONITOR) ? true : false);
				if (displayInherit)
					if (getMonitorPanel().getParent() == null)
						getMonitorPanel().setParent(controlDiv);
			} else if (viewIconOrList == VIEW_ICON) {
				if (dispList(INode.SE)) {
					seTableModel.setInherit(displayInherit);
					refreshIcon(getSeIconPanel(), seTableModel, seTableModel);
				}

				if (dispList(INode.GROUP)) {
					groupTableModel.setInherit(displayInherit);
					refreshIcon(getGroupIconPanel(), groupTableModel,
							groupTableModel);
				}

				if (dispList(INode.ENTITY)) {
					entityTableModel.setInherit(displayInherit);
					refreshIcon(getEntityIconPanel(), entityTableModel,
							entityTableModel);
				}

				if (dispList(INode.MONITOR)) {
					monitorTableModel.setInherit(displayInherit);
					refreshIcon(getMonitorIconPanel(), monitorTableModel,
							monitorTableModel);
				}

				getGroupIconPanel().setVisible(
						dispList(INode.GROUP) ? true : false);
				getEntityIconPanel().setVisible(
						dispList(INode.ENTITY) ? true : false);
				getMonitorIconPanel().setVisible(
						dispList(INode.MONITOR) ? true : false);
				if (displayInherit)
					if (getMonitorIconPanel().getParent() == null)
						getMonitorIconPanel().setParent(controlDiv);

			} else if (viewIconOrList == VIEW_TREE) {
				opendTree.refreshDisplayDetail(displayInherit);
			}
		}
	}

	public int getViewIconOrList() {
		return viewIconOrList;
	}

	public void setViewIconOrList(int viewIconOrList) {
		if(isRefresh){
			refreshControlDiv();
		}else{
			if (this.viewIconOrList != viewIconOrList) {
				this.viewIconOrList = viewIconOrList;
				refreshControlDiv();
			}
		}
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		self.setAttribute("Composer", this);

		final EccTimer timer = (EccTimer) comp.getDesktop().getPage("eccmain")
				.getFellow("header_timer");
		if (timer != null)
			timer.addTimerListener("eccControlPanel", this);

		this.tree = (Tree) page.getDesktop().getPage("eccmain").getFellow(
				"tree");

		Components.addForwards(comp, this);

		createFavSelect();

		final West westTree = (West) comp.getDesktop().getPage("eccmain")
				.getFellow("westTree");

		btnLeftTreeVisible.setDefaultPressed(true);
		btnLeftTreeVisible.setUseCookied(false);
		btnRightActionVisible.setDefaultPressed(true);
		btnRightActionVisible.setUseCookied(true);
		btnRightActionVisible.setClicked(false);

		westTree.setSize(btnLeftTreeVisible.isClicked() ? "195px" : "1px");
		actionEast.setSize(btnRightActionVisible.isClicked() ? "140px" : "1px");

		btnLeftTreeVisible.setClickListener(new EventListener() {
			public void onEvent(Event event) throws Exception {
				westTree.setSize(btnLeftTreeVisible.isClicked() ? "195px"
						: "1px");
			}
		});

		btnRightActionVisible.setClickListener(new EventListener() {
			public void onEvent(Event event) throws Exception {
				actionEast.setSize(btnRightActionVisible.isClicked() ? "140px"
						: "1px");
			}
		});

		final ButtonGroup group1 = new ButtonGroup(false);
		btnHiddenInherit.setDefaultPressed(true);
		group1.addButton(btnHiddenInherit).addButton(btnDisplayInherit);

		displayInherit = btnDisplayInherit.isClicked();
		group1.setClickListener(new EventListener() {
			public void onEvent(Event event) throws Exception {
				setDisplayInherit(group1.getCurrentButton().equals(
						btnDisplayInherit) ? true : false);

			}
		});

		final ButtonGroup group2 = new ButtonGroup(true, "controlPanelViewSet");
		group2.addButton(btnListView).addButton(btnIconView).addButton(
				btnTreeView);
		btnListView.setDefaultPressed(true);

		if (btnIconView.isClicked())
			setViewIconOrList(VIEW_ICON);
		else if (btnListView.isClicked())
			setViewIconOrList(VIEW_LIST);
		else
			setViewIconOrList(VIEW_TREE);

		group2.setClickListener(new EventListener() {
			public void onEvent(Event event) throws Exception {
				if (group2.getCurrentButton().equals(btnIconView))
					setViewIconOrList(VIEW_ICON);
				else if (group2.getCurrentButton().equals(btnListView))
					setViewIconOrList(VIEW_LIST);
				else
					setViewIconOrList(VIEW_TREE);
			}
		});

		slider_timer.addEventListener("onSelect", new EventListener() {
			public void onEvent(Event event) throws Exception {
				Toolkit.getToolkit().setCookie("slider_timer",
						slider_timer.getSelectedItem().getValue().toString(),
						desktop, 99999999);
				timer.setDelay(1000 * Integer.parseInt(slider_timer
						.getSelectedItem().getValue().toString()));
			}
		});

		homeImg.addEventListener("onClick", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				EccTreeItem eccTreeItem = (EccTreeItem) event.getTarget()
						.getAttribute("eccTreeItem");
				Toolkit.getToolkit().setCookie("homeEccTreeItem",
						eccTreeItem.getId(), desktop, Integer.MAX_VALUE);
				Toolkit.getToolkit().showOK(
						"已设置，登录自动显示节点:" + eccTreeItem.getTitle());
			}
		});
		curNodeTitle.addEventListener("onClick", actionMenuOpenListener);

		String value = Toolkit.getToolkit().getCookie("slider_timer", desktop);
		if (value != null)
			for (Object item : slider_timer.getItems())
				if (((Listitem) item).getValue().equals(value)) {
					slider_timer.selectItem((Listitem) item);
					timer.setDelay(1000 * Integer.parseInt(value));
					break;
				}

		propPanel.addCollapsibleListener(new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				boolean close = (Boolean) event.getData();
				if (close)
					southid.setSize("22px");
				else
					southid.setSize("263px");

			}
		});

		actionEast.addEventListener("onSize", new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {

			}
		});
		createPageSizeSelect();
		refresh();

	}
	private void createPageSizeSelect()
	{
		String pgsz=Toolkit.getToolkit().getCookie("controlPanelListPageSize");
		if(pgsz==null)
			pgsz="15";
		boxPageSize.setValue(pgsz);
		
		boxPageSize.addEventListener("onChange",  new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception 
			{
				
				String pgsz = ((Combobox) event.getTarget()).getValue();
				try {
						int pageSize=Math.abs(Integer.parseInt(pgsz));
						Toolkit.getToolkit().setCookie("controlPanelListPageSize",pgsz,Integer.MAX_VALUE);
						Session session = Executions.getCurrent().getDesktop().getSession();
						session.setAttribute("pageSize", pgsz);
						for(Listbox list:listBoxMap.values())
						{
							list.setPageSize(pageSize);
							list.getPaginal().setPageSize(pageSize);
						}
						((Combobox)event.getTarget()).setValue(String.valueOf(pageSize));
						isRefresh = true;
						if(btnTreeView.isClicked()){
							setViewIconOrList(VIEW_TREE);
						}else if(btnIconView.isClicked()){
							setViewIconOrList(VIEW_ICON);
						}
						isRefresh = false;
				} catch (Exception e) {
					event.stopPropagation();
				}

			}
		});
	}

	public void onCreate$controlDiv() {
		Clients.showBusy(null, false);/* 取消滚动球球 */
	}

	public void refreshCurNodeDiv() {
		if (selTreeItem == null || eccView == null)
			return;
		if (selTreeItem.getId() == null)
			return;
		INode selINode = eccView.getNode(selTreeItem.getId());
		if (selINode == null && !selTreeItem.getType().equals("WholeView"))
			return;
		INode info = null;
		if (selTreeItem.getType().equals(INode.SE)) {
			info = eccView.getSeInfo(selINode);
		} else if (selTreeItem.getType().equals(INode.GROUP)) {
			info = eccView.getGroupInfo(selINode);
		} else if (selTreeItem.getType().equals(INode.ENTITY)) {
			info = eccView.getEntityInfo(selINode);
		}
		if (selTreeItem.getType().equals(INode.GROUP)
				|| selTreeItem.getType().equals(INode.ENTITY)
				|| selTreeItem.getType().equals(INode.MONITOR))
			curNodeImg.setSrc(EccWebAppInit.getInstance().getImage(
					selTreeItem.getType(), selTreeItem.getStatus()));
		else
			curNodeImg.setSrc(EccWebAppInit.getInstance().getImage(
					selTreeItem.getType()));

		curNodeTitle.setValue(selTreeItem.getTitle());

		while (curNodeInfo.getLastChild() != null)
			curNodeInfo.removeChild(curNodeInfo.getLastChild());
		makeINodeDescDiv(desktop.getSession(), info).setParent(curNodeInfo);
	}
	
	/**
	 * 刷新NodeInfo的refreshCurNodeDiv方法
	 * @param eti
	 */
	public void refreshCurNodeDiv(EccTreeItem eti) {
		if (selTreeItem == null || eccView == null)
			return;
		if (selTreeItem.getId() == null)
			return;
		INode selINode = eccView.getNode(selTreeItem.getId());
		if (selINode == null && !selTreeItem.getType().equals("WholeView"))
			return;
		INode info = null;
		if (selTreeItem.getType().equals(INode.SE)) {
			info = eccView.getSeInfo(selINode);
		} else if (selTreeItem.getType().equals(INode.GROUP)) {
			info = eccView.getGroupInfo(selINode);
		} else if (selTreeItem.getType().equals(INode.ENTITY)) {
			info = eccView.getEntityInfo(selINode);
		}
		if (selTreeItem.getType().equals(INode.GROUP)
				|| selTreeItem.getType().equals(INode.ENTITY)
				|| selTreeItem.getType().equals(INode.MONITOR))
			curNodeImg.setSrc(EccWebAppInit.getInstance().getImage(
					selTreeItem.getType(), selTreeItem.getStatus()));
		else
			curNodeImg.setSrc(EccWebAppInit.getInstance().getImage(
					selTreeItem.getType()));

		curNodeTitle.setValue(selTreeItem.getTitle());

		refreshNodeInfo(eti);
	}
	
	public void refreshNodeInfo(EccTreeItem eccTreeItem) {
		while (curNodeInfo.getLastChild() != null){
			curNodeInfo.removeChild(curNodeInfo.getLastChild());
		}
		makeNodeInfo(eccTreeItem).setParent(curNodeInfo);
	}

	public void refreshControlDiv() {
		while (controlDiv.getLastChild() != null)
			controlDiv.removeChild(controlDiv.getLastChild());

		if (viewIconOrList == VIEW_LIST) {
			creatListView();
			session.removeAttribute("selectedItem");   //去掉记忆功能
			refreshSouthByParent(selTreeItem);
		} else if (viewIconOrList == VIEW_ICON) {
			creatIconView();
			session.removeAttribute("selectedItem");   //去掉记忆功能
			refreshSouthByParent(selTreeItem);
		}
		else if (viewIconOrList == VIEW_TREE) {
			creatTreeView();                             
			session.removeAttribute("selectedItem");	////去掉记忆功能
			refreshSouthByParent(selTreeItem);
		}
		// Clients.showBusy("",false);/*隐藏滚动球球*/
	}

	private void creatIconView() {
		if (dispList(INode.SE)) {
			Panel panel = getSeIconPanel();
			refreshIcon(panel, seTableModel, seTableModel);
			panel.setParent(controlDiv);
		}
		if (dispList(INode.GROUP)) {
			Panel panel = getGroupIconPanel();
			refreshIcon(panel, groupTableModel, groupTableModel);
			panel.setParent(controlDiv);
		}
		if (dispList(INode.ENTITY)) {
			Panel panel = getEntityIconPanel();
			refreshIcon(panel, entityTableModel, entityTableModel);
			panel.setParent(controlDiv);

		}
		if (dispList(INode.MONITOR)) {
			Panel panel = getMonitorIconPanel();
			refreshIcon(panel, monitorTableModel, monitorTableModel);
			panel.setParent(controlDiv);
		}
	}

	private void refreshIcon(Panel panel, ListModelList listModel,
			IconRenderer iconRender) {
		
		while (panel.getPanelchildren().getLastChild() != null)
			panel.getPanelchildren().removeChild(
					panel.getPanelchildren().getLastChild());
		
		Grid grid = new Grid();
		grid.setStyle("border:none");
		
		int pageSize = 15;
		Session session = Executions.getCurrent().getDesktop().getSession();
		String pgzs = "";
		pgzs = (String)session.getAttribute("pageSize");
		if(pgzs==null||pgzs.isEmpty()){
			pgzs = Toolkit.getToolkit().getCookie("controlPanelListPageSize");
		}
		if (pgzs != null)
			pageSize = Integer.parseInt(pgzs);

		grid.setMold("paging");
		grid.getPagingChild().setMold("os");
		grid.setPageSize(pageSize / 5);
		
		Rows rows =  new Rows();
		grid.appendChild(rows);
		Row row = new Row();
		int i = 0;
		int totalSize = 0;
		for (Object obj : listModel) {
			IconCell cell = new IconCell();
			totalSize++;
			cell.setValue(obj);
			cell
					.addEventListener("onDoubleClick",
							getClickTitleEventListener());
			cell.getNameLabel().setAttribute("eccTreeItem", obj);

			cell.getNameLabel().addEventListener("onClick",
					getClickTitleEventListener());
			cell.setAttribute("eccTreeItem", obj);
			cell.addEventListener("onRightClick", actionMenuOpenListener);
			cell.getDropDownImage().setAttribute("eccTreeItem", obj);
			cell.getDropDownImage().addEventListener("onClick",
					actionMenuOpenListener);
			cell.addEventListener("onClick", new EventListener(){
				@Override
				public void onEvent(Event event) throws Exception {
					EccTreeItem item=(EccTreeItem)event.getTarget().getAttribute("eccTreeItem");
					actionMenuDiv.refreshMonitor(item);
				
				}
			});
			cell.addEventListener("onClick", getSouthListener());
			if(i==5){
				row.setStyle("border:none");
				rows.appendChild(row);
				row = new Row();
				i = 0;
			}
			row.appendChild(cell);
			i++;
			
			iconRender.renderIcon(cell, obj);
		}
		if(5-i > 0){
			while(i < 5){
				Panel p = new Panel();
				p.setStyle("width:85px;height:50px;float:left;padding:5px;margin:5px 5px 5px 5px");
				row.setStyle("border:none");
				row.appendChild(p);
				i++;
			}
		}
		rows.appendChild(row);
		grid.setParent(panel.getPanelchildren());
		grid.getPagingChild().setIconGrid(true,5,totalSize);
		panel.setOpen(listModel.getSize()>0?true:false);
	}

	/* 点击列表行记录 */
	private EventListener getSouthListener() {
		return new EventListener() {

			@Override
			public void onEvent(Event event) throws Exception {
				EccTreeItem eccTreeItem = null;
				if (event.getTarget() instanceof Listbox) {
					Listitem item = ((Listbox) event.getTarget())
							.getSelectedItem();
					if (item == null)
						return;
					eccTreeItem = (EccTreeItem) item.getValue();
					selectedIndex = ((Listbox) event.getTarget()).getSelectedIndex();
					selectedItem = item;
					Session session = Executions.getCurrent().getDesktop().getSession();
					session.setAttribute("selectedItem", eccTreeItem);
					session.setAttribute("selListItemIndex", new Integer(selectedIndex));
					session.setAttribute("selectedListItem", selectedItem);
					isSelected = true;
				} else {
					eccTreeItem = (EccTreeItem) event.getTarget().getAttribute(
							"eccTreeItem");
				}
				
				if("monitor".equals(eccTreeItem.getType())){
					LinkedList<EccTreeItem> recentlyViewMonitors = (LinkedList<EccTreeItem>)session.getAttribute("recentlyViewMonitors");
					if(recentlyViewMonitors == null || recentlyViewMonitors.isEmpty()){
						//若recentlyViewMonitors为null或里面没有值，初始化，并添加第一个
						recentlyViewMonitors = new LinkedList<EccTreeItem>();
						recentlyViewMonitors.addFirst(eccTreeItem);
					}else{
						//遍历recentlyViewMonitors，删除掉相同的节点
						int index = -1;
						for(int i=0;i<recentlyViewMonitors.size();i++){
							if(eccTreeItem.getId().equals(recentlyViewMonitors.getFirst().getId())){
								//若recentlyViewMonitors中第一个节点与当前节点好似同一节点，则不需进行添加
								break;
							}
							EccTreeItem _item = recentlyViewMonitors.get(i);
							if(_item.getId().equals(eccTreeItem.getId())){
								index = i;
							}
						}
						if(index != -1){
							recentlyViewMonitors.remove(index);
						}

						if(eccTreeItem.getId().equals(recentlyViewMonitors.getFirst().getId())){
							//若recentlyViewMonitors中第一个节点与当前节点好似同一节点，则不需进行添加
						}else{
							recentlyViewMonitors.addFirst(eccTreeItem);
						}
					}
					
					session.setAttribute("recentlyViewMonitors", recentlyViewMonitors);
					EccLayoutComposer elc = (EccLayoutComposer)Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("main").getAttribute("Composer");
					elc.refreshRecentlyViewMonirot();
				}
				
				refreshSouth(eccTreeItem, false);
				isSelected = false;
			}
		};
	}

	/* 根据上级节点是否为设备决定 */
	private void refreshSouthByParent(EccTreeItem eccTreeItem) {
		if(eccTreeItem == null){
			return;
		}
		Session session = Executions.getCurrent().getDesktop().getSession();
		EccTreeItem itemInSession = (EccTreeItem)session.getAttribute("selectedItem");
		//还必须在监测器列表里面存在,为了避免设备下的监测器都删完了，但是简单报表还在显示modify by qimin.xiong
		if(itemInSession != null 
				&& eccTreeItem.getChildRen().contains(itemInSession)
				&& "monitor".equals(itemInSession.getType()) && itemInSession.getParent().getId().equals(eccTreeItem.getId())){
			eccTreeItem = itemInSession;
		}
		if (eccTreeItem!=null && eccTreeItem.getChildRen().size() > 0){
			refreshSouthWithFilter(eccTreeItem, true);
		}else{
			if(eccTreeItem != null && "monitor".equals(eccTreeItem.getType())){
				if (southid.getSize().equals("0px")){
					southid.setSize("263px");
					monitorEmpty = false;
				}
				propPanel.setTitle(eccTreeItem.getParent().getTitle() + ":"
						+ eccTreeItem.getTitle());
				MonitorReport mr = (MonitorReport) WMonitorInfo
						.getAttribute("Composer");
				mr.RefreshData(eccTreeItem.getValue().getSvId());
				monitorEmpty = false;
				return;
			}else{
				refreshSouthWithFilter(null, true);
			}
		}
	}
	
	/**
	 * 删除节点时调用，刷新界面下部信息
	 * @param eccTreeItem
	 */
	private void deleteRefreshSouthByParent(EccTreeItem eccTreeItem){
		Session session = Executions.getCurrent().getDesktop().getSession();
		EccTreeItem itemInSession = (EccTreeItem)session.getAttribute("selectedItem");
		if(itemInSession != null && "monitor".equals(itemInSession.getType()) && itemInSession.getParent().getId().equals(eccTreeItem.getId())){
			eccTreeItem = itemInSession.getParent().getChildRen().get(0);
		}
		//选中当前Listbox中的第一个节点（选中当前节点的父节点的第一个子节点）
		this.selectedIndex = 0;
		session.setAttribute("selListItemIndex", new Integer(selectedIndex));
		if("monitor".equals(eccTreeItem.getType())){
			this.monitor_listbox.setSelectedIndex(0);
		}
		if("entity".equals(eccTreeItem.getType())){
			this.entity_listbox.setSelectedIndex(0);
		}
		if("gruop".equals(eccTreeItem.getType())){
			this.group_listbox.setSelectedIndex(0);
		}
		
		if (eccTreeItem!=null && eccTreeItem.getChildRen().size() > 0){
			refreshSouthWithFilter(eccTreeItem, true);
		}else{
			if(eccTreeItem != null && "monitor".equals(eccTreeItem.getType())){
				if (southid.getSize().equals("0px")){
					southid.setSize("263px");
				}
				propPanel.setTitle(eccTreeItem.getParent().getTitle() + ":"
						+ eccTreeItem.getTitle());
				MonitorReport mr = (MonitorReport) WMonitorInfo
						.getAttribute("Composer");
				mr.RefreshData(eccTreeItem.getValue().getSvId());
				return;
			}else{
				refreshSouthWithFilter(null, true);
			}
		}
	}

	public void refreshSouth(EccTreeItem eccTreeItem, boolean autoSelectRow) {
		if(eccTreeItem != null && "monitor".equals(eccTreeItem.getType())){
			actionMenuDiv.refreshMonitor(eccTreeItem);
		}
		if (eccTreeItem != null && autoSelectRow) {
			Listbox listbox = listBoxMap.get(eccTreeItem.getType());

			if (listbox != null)
				for (int i = 0; i < listbox.getModel().getSize(); i++) {
					if (eccTreeItem.equals(listbox.getModel().getElementAt(i))) {
						listbox.setSelectedIndex(i);
						break;
					}
				}
		}

		if (eccTreeItem == null || !eccTreeItem.getType().equals(INode.MONITOR)) {
			if (!southid.getSize().equals("0px"))
				southid.setSize("0px");
			return;
		}

		if (southid.getSize().equals("0px"))
			southid.setSize("263px");

		propPanel.setTitle(eccTreeItem.getParent().getTitle() + ":"
				+ eccTreeItem.getTitle());
		MonitorReport mr = (MonitorReport) WMonitorInfo
				.getAttribute("Composer");
		mr.RefreshData(eccTreeItem.getValue().getSvId());
	}
	
	public void refreshSouthWithFilter(EccTreeItem eccTreeItems, boolean autoSelectRow) {		
		if(eccTreeItems == null||eccTreeItems.getChildRen().size()<1){
			if (!southid.getSize().equals("0px")){
				monitorEmpty = true;
				southid.setSize("0px");
			}
			return;
		}
		Session session = Executions.getCurrent().getDesktop().getSession();
		for(int i=0;i<eccTreeItems.getChildRen().size();i++){
			EccTreeItem eccTreeItem = eccTreeItems.getChildRen().get(i);
			//add 添加子组
			if((filter&eccTreeItem.getStatus())==eccTreeItem.getStatus()){	
				
				if (eccTreeItem != null && eccTreeItem.getType().equals(INode.GROUP)) {
					if(session.getAttribute(ConstantValues.RefreshGroupId)!= null ){
						String id = (String)session.getAttribute(ConstantValues.RefreshGroupId);
						if(eccTreeItem.getId().equals(id)){
							if(this.group_listbox != null){
								int size = this.group_listbox.getItemCount();
								this.group_listbox.setSelectedIndex(size-1);
								session.removeAttribute(ConstantValues.RefreshGroupId);
							}
						}
					}
				}
			}
			//add 添加设备
			if((filter&eccTreeItem.getStatus())==eccTreeItem.getStatus()){				
				if (eccTreeItem != null && eccTreeItem.getType().equals(INode.ENTITY)) {
					if(session.getAttribute(ConstantValues.RefreshEntityId)!= null ){
						String id = (String)session.getAttribute(ConstantValues.RefreshEntityId);
						if(eccTreeItem.getValue().getSvId().equals(id)){
							if(this.entity_listbox != null){
								int size = this.entity_listbox.getItemCount();
								this.entity_listbox.setSelectedIndex(size-1);
								session.removeAttribute(ConstantValues.RefreshEntityId);
							}
						}
					}
				}
			}

			if((filter&eccTreeItem.getStatus())==eccTreeItem.getStatus()){
				if (eccTreeItem == null || !eccTreeItem.getType().equals(INode.MONITOR)) {
					if (!southid.getSize().equals("0px")){
						monitorEmpty = true;
						southid.setSize("0px");
					}
					continue;
				}
				
				if (southid.getSize().equals("0px")){
					monitorEmpty = false;
					southid.setSize("263px");
				}
					
				
				MonitorReport mr = (MonitorReport) WMonitorInfo.getAttribute("Composer");
				if(session.getAttribute(ConstantValues.RefreshMonitorId)!= null ){
					String id = (String)session.getAttribute(ConstantValues.RefreshMonitorId);
					if(eccTreeItem.getValue().getSvId().equals(id)){
						propPanel.setTitle(eccTreeItem.getParent().getTitle() + ":"+ eccTreeItem.getTitle());
						mr.RefreshData(id);
						monitorEmpty = false;
//						session.removeAttribute(ConstantValues.RefreshMonitorId);
						session.setAttribute("selFavItem", eccTreeItem);
						actionMenuDiv.onEvent(eccTreeItem);
						
						if(this.monitor_listbox != null){
							
							int sizee=Integer.parseInt(id
							.substring(
									id.lastIndexOf(".") > 0 ? id
											.lastIndexOf(".")+1
											: 0, id
											.length()));
							
//							System.out
//									.println(">>>>>>>>>>>>>>>>>>>>>>>"
//											+ id
//													.substring(
//															id.lastIndexOf(".") > 0 ? id
//																	.lastIndexOf(".")+1
//																	: 0, id
//																	.length()));
//							int size = this.monitor_listbox.getItemCount();
//							System.out.println("size::" + size);
							this.monitor_listbox.setSelectedIndex(sizee-1);
							
						}

						break;
					}
				}else if(session.getAttribute(ConstantValues.LinkedMonitorId)!= null){
					String id = (String)session.getAttribute(ConstantValues.LinkedMonitorId);
					if(eccTreeItem.getValue().getSvId().equals(id)){
						propPanel.setTitle(eccTreeItem.getParent().getTitle() + ":"+ eccTreeItem.getTitle());
						mr.RefreshData(id);
						monitorEmpty = false;
						session.removeAttribute(ConstantValues.LinkedMonitorId);
						if(this.monitor_listbox != null){
							for (int m = 0; m < monitor_listbox.getModel().getSize(); m++) {
								EccTreeItem  eccTreeItemSpecial  = (EccTreeItem )monitor_listbox.getModel().getElementAt(m);
								if(id.equals(eccTreeItemSpecial.getId())){
									monitor_listbox.setSelectedIndex(m);
									session.setAttribute(ConstantValues.Index_LinkedMonitor, m);
									break;
								}
							}
						}
						break;
					}
				}else
				{
					propPanel.setTitle(eccTreeItem.getParent().getTitle() + ":"+ eccTreeItem.getTitle());
					mr.RefreshData(eccTreeItem.getValue().getSvId());
					monitorEmpty = false;
					session.setAttribute("selFavItem", eccTreeItem);
					break;
				}

			}else{
				if (!southid.getSize().equals("0px")){
					monitorEmpty = true;
					southid.setSize("0px");
				}
			}
		}
		
	}

	private void creatTreeView() {
		opendTree = new EccOpenedTreeView();
		opendTree.setZclass("z-dottree");
		opendTree.setVflex(true);
		new Treechildren().setParent(opendTree);
		// opendTree.setSelectListener(getClickEventListener());
		opendTree.addEventListener("onDoubleClick",
				getClickTitleEventListener());
		opendTree.setSelTreeItem(this.selTreeItem);
		opendTree.setFilter(filter);
		opendTree.setDisplayDetail(displayInherit);
		opendTree.setParent(controlDiv);
		
		int pageSize = 15;
		Session session = Executions.getCurrent().getDesktop().getSession();
		String pgzs = "";
		pgzs = (String)session.getAttribute("pageSize");
		if(pgzs==null||pgzs.isEmpty()){
			pgzs = Toolkit.getToolkit().getCookie("controlPanelListPageSize");
		}
		if (pgzs != null)
			pageSize = Integer.parseInt(pgzs);
		opendTree.setMold("paging");
		opendTree.getPaginal().setPageSize(pageSize);
		opendTree.getPagingChild().setMold("os");
		opendTree.setPageSize(pageSize);
		opendTree.initTree();
	}

	private Listbox getListBox(Panel parent, String type, EccListModel listModel) {
		Listbox list = listBoxMap.get(type);
		if (list == null) {
			list = makeList(listModel, parent);
			listBoxMap.put(type, list);
		} else {
			if (!listModel.equals(list.getModel()))
				list.setModel(listModel);
		}
		if (!parent.getPanelchildren().equals(list.getParent())) {
			parent.getPanelchildren().appendChild(list);
		}

		parent.setOpen(list.getModel().getSize() > 0?true:false);
		
		if(isAutoRefresh && isSelected){
			try{
				if(list.getItemCount() >= selectedIndex)
				list.setSelectedIndex(selectedIndex);
			}catch(Exception e){}
			if(selectedItem!=null){
				selTreeItem = (EccTreeItem) selectedItem.getValue();
			}
		}
		
		return list;
	}

	private void creatListView() {
		//getPageSizeControl().setParent(controlDiv);
		if (dispList(INode.SE)) {
			getSePanel().setParent(controlDiv);
		}
		if (dispList(INode.GROUP)) {
			getGroupPanel().setParent(controlDiv);
		}
		if (dispList(INode.ENTITY)) {
			getEntityPanel().setParent(controlDiv);
			refreshActionMenuByEntity();
		}
		if (dispList(INode.MONITOR)) {
			getMonitorPanel().setParent(controlDiv);
			refreshActionMenuByMonitor();
		}
		
	}

	public ArrayList<EccTreeItem> getCheckedEccTreeItem() {
		ArrayList<EccTreeItem> list = new ArrayList<EccTreeItem>();
		Iterator iterator = listBoxMap.keySet().iterator();
		while (iterator.hasNext()) {
			Object key = iterator.next();
			Listbox listbox = listBoxMap.get(key);
			for (Object item : listbox.getSelectedItems().toArray())
				list.add((EccTreeItem) ((Listitem) item).getValue());
		}

		return list;
	}

	public HashMap<String, Listbox> getListBoxMap() {
		return listBoxMap;
	}

	public void refresh() {
		try{
			if (page.getDesktop() == null)
				return;
			if (page.getDesktop().getPage("eccmain") == null)
				return;
			refreshData();
			refreshCurNodeDiv();
			refreshControlDiv();
			
			refreshSouthByParent(selTreeItem);
			
			HashMap<String, String> doMap = (HashMap<String, String>)session.getAttribute("doMap");
			if(doMap != null){
				String dowhat = doMap.get("dowhat");
				if("addDevice".equals(dowhat)){
					String svId = doMap.get("svId");
					int size = selTreeItem.getChildRen().size();
					EccTreeItem item;
					if(size == 0){
//						item = selTreeItem.getChildRen().get(size);
					}else{
						item = selTreeItem.getChildRen().get(size - 1);
						actionMenuDiv.refreshByParams(selTreeItem, item);
					}
				}
				if("addMonitor".equals(dowhat)){
					String svId = doMap.get("svId");
					int size = selTreeItem.getChildRen().size();
					EccTreeItem item;
					if(size == 0){
//						item = selTreeItem.getChildRen().get(size);
					}else{
						item = selTreeItem.getChildRen().get(size - 1);
						actionMenuDiv.refreshByParams(selTreeItem, item);
					}
					
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public void refresh(EccTreeItem eti) {
		if (page.getDesktop() == null)
			return;
		if (page.getDesktop().getPage("eccmain") == null)
			return;
		refreshData();
		refreshCurNodeDiv(eti);
		refreshControlDiv();
		
		refreshSouthByParent(selTreeItem);
	}
	
	/**
	 * 点击最近浏览的监测器节点的刷新方法
	 * @param eti
	 */
	public void refreshByClickRecentlyViewMonitor(EccTreeItem eti){
		if (page.getDesktop() == null)
			return;
		if (page.getDesktop().getPage("eccmain") == null)
			return;
		refreshData();
		refreshCurNodeDiv(eti);
		refreshControlDiv();
		
		refreshSouthByParent(eti);
	}
	
	private synchronized void autoRefresh() {
		isAutoRefresh = true;
		if (page.getDesktop() == null)
			return;
		if (page.getDesktop().getPage("eccmain") == null)
			return;
		
		refreshData();
		refreshCurNodeDiv();
		refreshControlDiv();
		
		refreshSouthByParent(selTreeItem);
		isAutoRefresh = false;
	}
	
	private synchronized void addRefresh(INode node){
		isAutoRefresh = true;
		if (page.getDesktop() == null)
			return;
		if (page.getDesktop().getPage("eccmain") == null)
			return;
		refreshData();
		refreshCurNodeDiv();
		refreshControlDiv();
		
		refreshSouthByParent(selTreeItem);
		EccTreeItem item = null;
		
		if("group".equals(node.getType()) && groupTableModel.size() != 0){
			item = (EccTreeItem)groupTableModel.get(groupTableModel.size() - 1);
		}
		if("entity".equals(node.getType()) && entityTableModel.size() != 0){
			item = (EccTreeItem)entityTableModel.get(entityTableModel.size() - 1);
		}
		if("monitor".equals(node.getType()) && monitorTableModel.size() != 0){
			item = (EccTreeItem)monitorTableModel.get(monitorTableModel.size() - 1);
		}
		actionMenuDiv.refreshByParams(selTreeItem, item);
		isAutoRefresh = false;
	
	}
	
	private synchronized void modifyRefresh(INode node) {
		isAutoRefresh = true;
		EccTreeItem tempItem = null;
		if (page.getDesktop() == null)
			return;
		if (page.getDesktop().getPage("eccmain") == null)
			return;
		refreshData();
		refreshCurNodeDiv();
		refreshControlDiv();

		Session session = Executions.getCurrent().getDesktop().getSession();
		EccTreeItem itemInSession = (EccTreeItem)session.getAttribute("selectedItem");
		EccTreeItem itemFromTree = (EccTreeItem)session.getAttribute("itemFromTree");
		if(itemFromTree != null && itemFromTree.getId().equals(selTreeItem.getId())){
			selTreeItem.setTitle(itemFromTree.getTitle());
		}
		
		if(itemInSession!=null && itemInSession.getType().equals(node.getType()) && itemInSession.getParent().getId().equals(node.getParentSvId())){
			if(itemInSession.getId().equals(node.getSvId())){
				selTreeItem = new EccTreeItem(itemInSession.getParent(), node.getSvId(), node.getName(), node.getType());
				selTreeItem.setValue(node);
				session.setAttribute("selectedItem", selTreeItem);
			}else{
				selTreeItem = itemInSession;
			}
			refreshSouthByParent(selTreeItem);
			isAutoRefresh = false;
		}else{
			refreshSouthByParent(selTreeItem);
			isAutoRefresh = false;
		}
	}
	
	private void deleteRefresh(INode node) {
		if (page.getDesktop() == null)
			return;
		if (page.getDesktop().getPage("eccmain") == null)
			return;
		//根据具体情况设置左边树中选中的节点
		//若所删除节点的父节点还有子节点，则仍然选择该父节点
		//若所删除节点的父节点已没有子节点，则选中该节点父节点的父节点
		Treeitem treeItem = tree.getSelectedItem();
		
		if(treeItem == null){
			Session session = Executions.getCurrent().getDesktop().getSession();
			Treeitem parentTreeItem = (Treeitem)session.getAttribute("selectedParentTreeItem");
			tree.selectItem(parentTreeItem);
		}else{		
			EccTreeItem tempItem = (EccTreeItem)treeItem.getValue();
			if(tempItem.getId().equals(node.getSvId())){
				tree.selectItem(treeItem.getParentItem());
			}else{
				if(tempItem.getId().equals(node.getParentSvId())){
					if(tempItem.getChildRen().isEmpty()){
						tree.selectItem(treeItem.getParentItem());
					}
				}
			}
		}
		
		deleteRefreshData();
		refreshCurNodeDiv();
		refreshControlDiv();
		
		EccTreeItem item = null;
		if("group".equals(node.getType()) && groupTableModel.size() != 0){
			item = (EccTreeItem)groupTableModel.get(0);
		}
		if("entity".equals(node.getType()) && entityTableModel.size() != 0){
			item = (EccTreeItem)entityTableModel.get(0);
		}
		if("monitor".equals(node.getType()) && monitorTableModel.size() != 0){
			item = (EccTreeItem)monitorTableModel.get(0);
		}
		
		actionMenuDiv.refreshByParams(selTreeItem, item);
		deleteRefreshSouthByParent(selTreeItem);
	}
	
	public void refreshByView(Tree tree, EccTreeItem eti){
		if (page.getDesktop() == null)
			return;
		if (page.getDesktop().getPage("eccmain") == null)
			return;
		refreshDataByView(tree, eti);
		refreshCurNodeDiv(eti);
		refreshControlDiv();
		if("monitor".equals(selTreeItem.getType())){
			actionMenuDiv.refreshMonitor(selTreeItem);
		}else{
			actionMenuDiv.refreshAll(selTreeItem);
		}
		refreshSouthByParent(selTreeItem);
	}

	public void refresh(SelectEvent e) {
		SelectEvent selEvent = (SelectEvent) e;
		Treeitem treeitem = (Treeitem) selEvent.getSelectedItems().iterator().next();
		EccTreeItem eti = (EccTreeItem) treeitem.getValue();
		
		refresh(eti);
		Listitem listitem=new Listitem();
		listitem.setAttribute("eccTreeItem", selTreeItem);
		if(!monitorEmpty){
			actionMenuDiv.onEvent(e);
		}else{
			actionMenuDiv.listPanelUnVisible(eti);
		}
	}

	private void createFavSelect() {
		favSelect.addEventListener("onSelect", new EventListener() {
			public void onEvent(Event event) throws Exception {
				// Clients.showBusy("", true);
				monitorEmpty = false;
				Object obj = favSelect.getSelectedItem().getValue();
				int filter = Integer.parseInt(obj.toString());
				setFilter(filter);
				favSelect.setAction("action:favSelect.blur()");
				session.removeAttribute("doMap");
				session.removeAttribute("selectedItem");
				refresh(selTreeItem);

				Listitem listitem=new Listitem();
				listitem.setAttribute("eccTreeItem", selTreeItem);
				if(!monitorEmpty){
					actionMenuDiv.onEvent(new MouseEvent("onClick",listitem,null));
				}else{
					actionMenuDiv.listPanelUnVisible(selTreeItem);
				}
			}

		});
	}

	private void expandTreeNodeDEL(ArrayList<EccTreeItem> curPath,
			Treeitem topNode, EccTreeItem bottom) {
		topNode.setOpen(true);

		if (topNode.getTreechildren() == null
				|| bottom.equals(topNode.getValue())) {
			this.selTreeItem = (EccTreeItem) topNode.getValue();
			tree.selectItem(topNode);
			return;
		}

		Iterator iterator = topNode.getTreechildren()
				.getVisibleChildrenIterator();
		while (iterator.hasNext()) {
			Treeitem node = (Treeitem) iterator.next();
			if (curPath.contains(node.getValue())) {
				expandTreeNodeDEL(curPath, node, bottom);
				return;
			}
		}
	}

	private EccTreeItem findHomeNodeDEL() {
		EccTreeItem curNode = null;
		ArrayList<EccTreeItem> curPath = new ArrayList<EccTreeItem>();

		String nodeId = Toolkit.getToolkit().getCookie("homeEccTreeItem",
				desktop);
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
			Treeitem topNode = topNode = (Treeitem) iterator.next();
			expandTreeNodeDEL(curPath, topNode, curPath.get(curPath.size() - 1));
		}

		return this.selTreeItem;
	}

	public void refreshData() {
		this.eccTreeModle = (EccTreeModel) tree.getModel();
		this.eccView = Toolkit.getToolkit().getSvdbView(page.getDesktop());
		if (this.eccView == null)
			return;

		if (tree.getSelectedItem() != null)
			this.selTreeItem = (EccTreeItem) tree.getSelectedItem().getValue();
		else 
			return;
		
		this.homeImg.setAttribute("eccTreeItem", this.selTreeItem);
		this.curNodeTitle.setAttribute("eccTreeItem", this.selTreeItem);

		this.seTableModel = (SeTableModel) getEccListModel(INode.SE,
				this.seTableModel, eccView, selTreeItem, displayInherit, filter);
		this.groupTableModel = (GroupTableModel) getEccListModel(INode.GROUP,
				this.groupTableModel, eccView, selTreeItem, displayInherit,
				filter);
		this.entityTableModel = (EntityTableModel) getEccListModel(
				INode.ENTITY, this.entityTableModel, eccView, selTreeItem,
				displayInherit, filter);
		this.monitorTableModel = (MonitorTableModel) getEccListModel(
				INode.MONITOR, this.monitorTableModel, eccView, selTreeItem,
				displayInherit, filter);

		this.seTableModel.addClickTitleListener(this
				.getClickTitleEventListener());
		this.groupTableModel.addClickTitleListener(this
				.getClickTitleEventListener());
		this.entityTableModel.addClickTitleListener(this
				.getClickTitleEventListener());
		this.monitorTableModel.addClickTitleListener(this
				.getClickTitleEventListener());

	}
	
	private void refreshDataByView(Tree tree, EccTreeItem eti) {
		this.eccTreeModle = (EccTreeModel) tree.getModel();
		this.eccView = Toolkit.getToolkit().getSvdbView(page.getDesktop());
		if (this.eccView == null)
			return;

		if (tree.getSelectedItem() != null)
			this.selTreeItem = (EccTreeItem) tree.getSelectedItem().getValue();
		else 
			selTreeItem = eti;
		if("monitor".equals(eti.getType())){
			selTreeItem = eti;
		}

		this.homeImg.setAttribute("eccTreeItem", this.selTreeItem);
		this.curNodeTitle.setAttribute("eccTreeItem", this.selTreeItem);

		this.seTableModel = (SeTableModel) getEccListModel(INode.SE,
				this.seTableModel, eccView, selTreeItem, displayInherit, filter);
		this.groupTableModel = (GroupTableModel) getEccListModel(INode.GROUP,
				this.groupTableModel, eccView, selTreeItem, displayInherit,
				filter);
		this.entityTableModel = (EntityTableModel) getEccListModel(
				INode.ENTITY, this.entityTableModel, eccView, selTreeItem,
				displayInherit, filter);
		this.monitorTableModel = (MonitorTableModel) getEccListModel(
				INode.MONITOR, this.monitorTableModel, eccView, selTreeItem,
				displayInherit, filter);

		this.seTableModel.addClickTitleListener(this
				.getClickTitleEventListener());
		this.groupTableModel.addClickTitleListener(this
				.getClickTitleEventListener());
		this.entityTableModel.addClickTitleListener(this
				.getClickTitleEventListener());
		this.monitorTableModel.addClickTitleListener(this
				.getClickTitleEventListener());

	}
	
	private void deleteRefreshData() {
		this.eccTreeModle = (EccTreeModel) tree.getModel();
		this.eccView = Toolkit.getToolkit().getSvdbView(page.getDesktop());
		if (this.eccView == null)
			return;
		//根据父节点中所选的节点，获得selTreeItem
		selTreeItem = (EccTreeItem)tree.getSelectedItem().getValue();
		
		this.homeImg.setAttribute("eccTreeItem", this.selTreeItem);
		this.curNodeTitle.setAttribute("eccTreeItem", this.selTreeItem);

		this.seTableModel = (SeTableModel) getEccListModel(INode.SE,
				this.seTableModel, eccView, selTreeItem, displayInherit, filter);
		this.groupTableModel = (GroupTableModel) getEccListModel(INode.GROUP,
				this.groupTableModel, eccView, selTreeItem, displayInherit,
				filter);
		this.entityTableModel = (EntityTableModel) getEccListModel(
				INode.ENTITY, this.entityTableModel, eccView, selTreeItem,
				displayInherit, filter);
		this.monitorTableModel = (MonitorTableModel) getEccListModel(
				INode.MONITOR, this.monitorTableModel, eccView, selTreeItem,
				displayInherit, filter);

		this.seTableModel.addClickTitleListener(this
				.getClickTitleEventListener());
		this.groupTableModel.addClickTitleListener(this
				.getClickTitleEventListener());
		this.entityTableModel.addClickTitleListener(this
				.getClickTitleEventListener());
		this.monitorTableModel.addClickTitleListener(this
				.getClickTitleEventListener());

	}

	private EccListModel getEccListModel(String type, EccListModel model,
			View eccView, EccTreeItem selTreeItem, boolean displayInherit,
			int filter) {
		if (model == null) {
			if (type.equals(INode.SE))
				model = new SeTableModel(eccView, selTreeItem, displayInherit,
						filter);
			else if (type.equals(INode.GROUP))
				model = new GroupTableModel(eccView, selTreeItem,
						displayInherit, filter);
			else if (type.equals(INode.ENTITY))
				model = new EntityTableModel(eccView, selTreeItem,
						displayInherit, filter);
			else if (type.equals(INode.MONITOR))
				model = new MonitorTableModel(eccView, selTreeItem,
						displayInherit, filter);
		} else {
			model.view = eccView;
			model.parentNode = selTreeItem;
			model.inherit = displayInherit;
			model.filter = filter;
			model.refresh();
		}
		return model;
	}

	/*
	 * noteType :INode.SE,INode.MONITOR, etc.
	 */
	private boolean dispList(String listType) {
		if (selTreeItem == null)
			return false;
		if (selTreeItem.getType().equals("WholeView")) {
			if (listType.equals(INode.SE))
				return true;
		} else if (selTreeItem.getType().equals(INode.SE)) {
			if (listType.equals(INode.GROUP))
				return true;
			else if (listType.equals(INode.MONITOR) && isDisplayInherit())
				return true;
			else if (listType.equals(INode.ENTITY))
				return true;
		} else if (selTreeItem.getType().equals(INode.GROUP)) {
			if (listType.equals(INode.GROUP))
				return true;
			else if (listType.equals(INode.MONITOR) && isDisplayInherit())
				return true;
			else if (listType.equals(INode.ENTITY))
				return true;
		} else if (selTreeItem.getType().equals(INode.ENTITY)) {
			if (listType.equals(INode.MONITOR))
				return true;
		}

		return false;
	}

	/*
	 * <panel maximizable="false" minimizable="false" border="normal"
	 * collapsible="true" closable="false">
	 */
	private Panel makePanel(String title) {
		Panel panel = new Panel();
		panel.setTitle(title);

		panel
				.setStyle("overflow-x:hidden;border:none;padding-right:17px;marging:0 20px 0 0");
		panel.setFramable(false);
		panel.setCollapsible(true);
		panel.setBorder("none");
		panel.setSclass("ecc-panel");

		Panelchildren panelchildren = new Panelchildren();
		panelchildren.setParent(panel);

		return panel;

	}

	private Listbox makeList(EccListModel listModel, Panel panel) {
		final Listbox listbox = new Listbox();

		EccListhead head = new EccListhead();
		head.initHeader(listModel);
		listbox.appendChild(head);
		SelectableListheader.addPopupmenu(listbox);

		int pageSize = 15;
		String pgzs = Toolkit.getToolkit()
				.getCookie("controlPanelListPageSize");
		if (pgzs != null)
			pageSize = Integer.parseInt(pgzs);

		listbox.setMold("paging");
		listbox.getPagingChild().setMold("os");
		listbox.getPaginal().setPageSize(pageSize);
		listbox.setPageSize(pageSize);

		listbox.setWidth("100%");
		listbox.setModel(listModel);
		listbox.setItemRenderer(listModel);
		listbox.setStyle("overflow-x:hidden;border:none");
		listbox.setMultiple(true);
		listbox.setCheckmark(true);
		listbox.addEventListener("onSelect", actionMenuDiv.selectListEventListener());
		listbox.addEventListener("onSelect", getSouthListener());
		listbox.addEventListener("onPaging", getListBoxPagingListener());
		listbox.setFixedLayout(true);
		listbox.setVflex(false);/* 出滚动条 */
		return listbox;
	}

	/* 分页事件 */
	public EventListener getListBoxPagingListener() {
		EventListener l = new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {

				PagingEvent e = (PagingEvent) event;
				Listbox listbox = (Listbox) e.getTarget();
				if (listbox.equals(listBoxMap.get(INode.MONITOR))) {
					int pageIdx = e.getActivePage();
					int idx = pageIdx * listbox.getPageSize();
					EccTreeItem item = (EccTreeItem) listbox.getModel()
							.getElementAt(idx);
					
					refreshSouth(item, true);
					Listitem listitem=new Listitem();
					listitem.setAttribute("eccTreeItem", item);
//					actionMenuDiv.onEvent(new MouseEvent("onClick",listitem,null));
				}
			}
		};
		return l;
	}

	/* 点击列表或者下划线展开事件 */
	public EventListener getClickTitleEventListener() {
		if (clickListener == null)
			clickListener = new EventListener() {
				public void onEvent(Event event) {
					INode iinfo = null;
					EccTreeItem item = null;
					if (event.getTarget() instanceof Label) {
						item = (EccTreeItem) ((Label) event.getTarget())
								.getAttribute("eccTreeItem");
						iinfo = item.getValue();
					} else if (event.getTarget() instanceof Listcell) {
						item = (EccTreeItem) ((Listcell) event.getTarget())
								.getValue();
						iinfo = item.getValue();
					} else if (event.getTarget() instanceof Listbox) {
						Listbox list = (Listbox) event.getTarget();
						Listitem listitem = list.getSelectedItem();
						item = ((EccTreeItem) listitem.getValue());
						iinfo = item.getValue();
					} else if (event.getTarget() instanceof IconCell) {
						IconCell cell = (IconCell) event.getTarget();
						item = ((EccTreeItem) cell.getValue());
						iinfo = item.getValue();
					} else if (event.getTarget() instanceof EccOpenedTreeView) {

						item = ((EccTreeItem) ((EccOpenedTreeView) event
								.getTarget()).getSelectedItem().getValue());
						iinfo = item.getValue();
					}

					if (iinfo == null)
						return;

					if (!iinfo.getType().equals(INode.MONITOR)) {
						if (Toolkit.getToolkit()
								.autoExpandTreeNode(tree, iinfo, item.getItemId()))
							refresh(item);

						actionMenuDiv.refreshAll(item);
						Session session = Executions.getCurrent().getDesktop().getSession();
						session.setAttribute("selectedItem", item);
						
						if("entity".equals(item.getType()) && item.getChildRen() != null && !item.getChildRen().isEmpty()){
							EccTreeItem _eccItem = item.getChildRen().get(0);
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
							EccLayoutComposer elc = (EccLayoutComposer)Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("main").getAttribute("Composer");
							elc.refreshRecentlyViewMonirot();
						}
					}

				}
			};

		return clickListener;
	}

	/*
	 * private Panel makeIconPanel(String title) { Panel panel = new Panel();
	 * panel.setStyle("padding-right:10px;marging:0 20px 0 0");
	 * panel.setBorder(null); panel.setTitle(title); panel.setFramable(true);
	 * panel.setCollapsible(true); panel.setBorder("normal"); Panelchildren
	 * panelchildren = new Panelchildren(); panelchildren.setParent(panel);
	 * panelchildren.setStyle("padding-left:50px;");
	 * 
	 * return panel; }
	 */

	private Div makeINodeDescDiv(Session session, INode node) {
		Div div = new Div();
		if (node == null)
			return div;
		if (node instanceof GroupInfo) {

			makeStatusSumLabel("设备","/main/control/images/entity.gif",-1,
					((GroupInfo) node).get_sub_entity_sum(session),false).setParent(div);

			
			makeStatusSumLabel("监测器","/main/control/images/monitor.gif",EccTreeItem.STATUS_ALL,
					((GroupInfo) node).get_sub_monitor_sum(session),true).setParent(div);
			makeStatusSumLabel("正常","/main/control/images/state_green.gif",EccTreeItem.STATUS_OK,
					((GroupInfo) node).get_sub_monitor_ok_sum(session),true).setParent(div);

			makeStatusSumLabel("错误","/main/control/images/state_red.gif",
					EccTreeItem.STATUS_BAD | EccTreeItem.STATUS_ERROR
							| EccTreeItem.STATUS_NULL,
					((GroupInfo) node).get_sub_monitor_error_sum(session),true).setParent(
					div);

			makeStatusSumLabel("危险","/main/control/images/state_yellow.gif",EccTreeItem.STATUS_WARNING,
					((GroupInfo) node).get_sub_monitor_warning_sum(session),true)
					.setParent(div);

			makeStatusSumLabel("禁止","/main/control/images/state_stop.gif",EccTreeItem.STATUS_DISABLED,
					((GroupInfo) node).get_sub_monitor_disable_sum(session),true)
					.setParent(div);

			return div;
		} else if (node instanceof EntityInfo) {
			makeStatusSumLabel("监测器","/main/control/images/monitor.gif",EccTreeItem.STATUS_ALL,
					((EntityInfo) node).get_sub_monitor_sum(session),true).setParent(div);

			makeStatusSumLabel("正常","/main/control/images/state_green.gif",EccTreeItem.STATUS_OK,
					((EntityInfo) node).get_sub_monitor_ok_sum(session),true)
					.setParent(div);

			makeStatusSumLabel("错误","/main/control/images/state_red.gif",
					EccTreeItem.STATUS_BAD | EccTreeItem.STATUS_ERROR
							| EccTreeItem.STATUS_NULL,
					((EntityInfo) node).get_sub_monitor_error_sum(session),true).setParent(
					div);

			makeStatusSumLabel("危险","/main/control/images/state_yellow.gif",EccTreeItem.STATUS_WARNING,
					((EntityInfo) node).get_sub_monitor_warning_sum(session),true)
					.setParent(div);

			makeStatusSumLabel("禁止","/main/control/images/state_stop.gif",EccTreeItem.STATUS_DISABLED,
					((EntityInfo) node).get_sub_monitor_disable_sum(session),true)
					.setParent(div);
			return div;
		} else if (node instanceof SeInfo) {
			makeStatusSumLabel("设备","/main/control/images/entity.gif",-1,
					((SeInfo) node).get_sub_entity_sum(session),false).setParent(div);

			makeStatusSumLabel("监测器","/main/control/images/monitor.gif",EccTreeItem.STATUS_ALL,
					((SeInfo) node).get_sub_monitor_sum(session),true).setParent(div);

			makeStatusSumLabel("正常","/main/control/images/state_green.gif",EccTreeItem.STATUS_OK,
					((SeInfo) node).get_sub_monitor_ok_sum(session),true).setParent(div);

			makeStatusSumLabel("错误","/main/control/images/state_red.gif",
					EccTreeItem.STATUS_BAD | EccTreeItem.STATUS_ERROR
							| EccTreeItem.STATUS_NULL,
					((SeInfo) node).get_sub_monitor_error_sum(session),true).setParent(div);

			makeStatusSumLabel("危险","/main/control/images/state_yellow.gif",EccTreeItem.STATUS_WARNING,
					((SeInfo) node).get_sub_monitor_warning_sum(session),true).setParent(
					div);

			makeStatusSumLabel("禁止","/main/control/images/state_stop.gif",EccTreeItem.STATUS_DISABLED,
					((SeInfo) node).get_sub_monitor_disable_sum(session),true).setParent(
					div);
			return div;
		} else
			return div;
	}
	
	private Div makeNodeInfo(EccTreeItem eccTreeItem){
		Div div = new Div();
		if (eccTreeItem == null)
			return div;
		
		EccHeaderComposer ehc = (EccHeaderComposer)desktop.getPage("eccmain").getFellow("eccHeaderComposer").getAttribute("Composer");
		NodeInfoBean bean = ehc.refreshNodeInfo(eccTreeItem);

		if ("se".equals(eccTreeItem.getType())) {
			makeStatusSumLabel("设备","/main/control/images/entity.gif",-1,
					bean.getDevice(),false).setParent(div);
		} else if ("group".equals(eccTreeItem.getType())) {
			makeStatusSumLabel("设备","/main/control/images/entity.gif",-1,
					bean.getDevice(),false).setParent(div);
		}

		makeStatusSumLabel("监测器","/main/control/images/monitor.gif",EccTreeItem.STATUS_ALL,
				bean.getAll(),true).setParent(div);
		makeStatusSumLabel("正常","/main/control/images/state_green.gif",EccTreeItem.STATUS_OK,
				bean.getOk(),true).setParent(div);
		makeStatusSumLabel("错误","/main/control/images/state_red.gif",
				EccTreeItem.STATUS_BAD | EccTreeItem.STATUS_ERROR | EccTreeItem.STATUS_NULL,
				bean.getError(),true).setParent(div);
		makeStatusSumLabel("危险","/main/control/images/state_yellow.gif",EccTreeItem.STATUS_WARNING,
				bean.getWarning(),true).setParent(div);
		makeStatusSumLabel("禁止","/main/control/images/state_stop.gif",EccTreeItem.STATUS_DISABLED,
				bean.getDisabled(),true).setParent(div);
		return div;
	}

	private Span makeStatusSumLabel(String title,String image,int status, int num,boolean link) {
		Span span = new Span();
		
		Label lbl = new Label(title + "(" + String.valueOf(num) + ")");
		lbl.setAttribute("status", status);
		
		if (link){
			lbl.setStyle("text-decoration: underline");
			lbl.setClass("pointer");
			lbl.addEventListener("onClick", new EventListener() {
				@Override
				public void onEvent(Event event) throws Exception {
					Object status = ((Label) event.getTarget())
							.getAttribute("status");
					for (Object obj : favSelect.getItems())
						if (status.equals(((Comboitem) obj).getValue())) {
							favSelect.setSelectedItem((Comboitem) obj);
							Session session = Executions.getCurrent().getDesktop().getSession();
							EccTreeItem eccItem = (EccTreeItem)tree.getSelectedItem().getValue();
							session.removeAttribute("doMap");
							session.removeAttribute("selectedItem");
							Events.postEvent(new Event("onSelect", favSelect));
							break;
						}

				}
			});
		}else{
			lbl.setStyle("");
		}
		makeSummaryImg(image).setParent(span);
		lbl.setParent(span);
		return span;
	}

	private Image makeSummaryImg(String src) {
		Image img = new Image(src);
		img.setAlign("absmiddle");
		img.setStyle("padding-bottom:1px;padding-left:5px;padding-right:1px");
		return img;
	}
	
	public ActionMenuDiv getActionMenuDiv() {
		return actionMenuDiv;
	}
	
	public void setIsSelected(boolean isSelected){
		this.isSelected = isSelected;
	}
	
	public FavouriteSelect getFavSelect(){
		return this.favSelect;
	}
	
	private void refreshActionMenuByEntity(){
		HashMap<String, String> doMap = (HashMap<String, String>)session.getAttribute("doMap");
		if(doMap !=null){
			String dowhat = doMap.get("dowhat");
			String svId = doMap.get("svId");
			EccTreeItem item = null;
			
			if("editDevice".equals(dowhat)){
				if(selTreeItem.getId().equals(svId)){
					//selTreeItem就是编辑的设备时
					actionMenuDiv.refreshByParams(selTreeItem.getParent(), selTreeItem);
				}else if(selTreeItem.getParent().getId().equals(svId)){
					//是编辑的设备的子节点时
					actionMenuDiv.refreshByParams(selTreeItem.getParent().getParent(), selTreeItem.getParent());
				}else{
					//sel是编辑的设备的父节点时
					for(EccTreeItem _item : selTreeItem.getChildRen()){
						if(_item.getId().equals(svId)){
							actionMenuDiv.refreshByParams(selTreeItem, _item);
							break;
						}
					}
				}
			}
		}else{
			return;
		}
	}
	
	private void refreshActionMenuByMonitor(){
		HashMap<String, String> doMap = (HashMap<String, String>)session.getAttribute("doMap");
		if(doMap !=null){
			String dowhat = doMap.get("dowhat");
			String svId = doMap.get("svId");
			EccTreeItem item = null;
			
			if("editDevice".equals(dowhat)){
				if(selTreeItem.getId().equals(svId)){
					if(selTreeItem.getChildRen()!=null && selTreeItem.getChildRen().size()==0){
						for(EccTreeItem _item : selTreeItem.getParent().getChildRen()){
							if(_item.getId().equals(svId)){
								item = _item;
								break;
							}
						}
						actionMenuDiv.refreshByParams(item);
					}else{
						EccTreeItem _item = (EccTreeItem)session.getAttribute("selectedItem");
						if(_item != null && "monitor".equals(_item.getType())){
							actionMenuDiv.refreshByParams(selTreeItem, _item);
						}else{
							actionMenuDiv.refreshByParams(selTreeItem);
						}
					}
				}else{
					if(selTreeItem.getParent().getId().equals(svId)){
						actionMenuDiv.refreshByParams(selTreeItem.getParent(), selTreeItem);
					}else{
						for(EccTreeItem _item : selTreeItem.getChildRen()){
							if(_item.getId().equals(svId)){
								actionMenuDiv.refreshByParams(_item);
								break;
							}
						}
					}					
				}
			}
		}else{
			return;
		}
	
	}
}
