package com.siteview.ecc.treeview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.event.TreeDataEvent;

import com.siteview.base.data.VirtualItem;
import com.siteview.base.data.VirtualView;
import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.queue.IQueueEvent;
import com.siteview.base.tree.IForkNode;
import com.siteview.base.tree.INode;
import com.siteview.ecc.timer.TimerListener;
import com.siteview.ecc.util.Toolkit;

public class EccTreeModel extends AbstractTreeModel implements TimerListener ,Cloneable{
	private final static Logger logger = Logger.getLogger(EccTreeModel.class);
	protected EccTreeItem root = null;
	protected VirtualView virtualView = null;
	protected boolean canChangeTree = true;
	boolean displayMonitor = false;

	public boolean isDisplayMonitor() {
		return displayMonitor;
	}

	public void setDisplayMonitor(boolean displayMonitor) {
		this.displayMonitor = displayMonitor;
	}

	@Override
	public void notifyChange(IQueueEvent e) {
		if (!canChangeTree)
			return;
		if (e instanceof ChangeDetailEvent){
			ChangeDetailEvent event = (ChangeDetailEvent)e;
			changeTree(event);
		}
	}

	protected EccTreeModel(EccTreeItem root) {
		super(root);
		this.root = root;
	}

	public VirtualView getVirtualView() {
		return virtualView;
	}

	public View getView() {
		return Toolkit.getToolkit().getSvdbView(getDesktop().getSession());
	}

	public static void removeInstance(Session session) {
		session.removeAttribute("eccTreeModel_" + VirtualView.DefaultView);
	}

	public static void removeInstance(Session session, String rootid) {
		session.removeAttribute("eccTreeModel_" + rootid);
	}

	public static EccTreeModel getInstance(Session session) {
		Object selectedViewNameObject = session.getAttribute("selectedViewName");
		if(selectedViewNameObject == null){
			return getInstance(session, VirtualView.DefaultView);
		}else{
			String viewName_str = (String)selectedViewNameObject;
			if("".equals(viewName_str)){
				return getInstance(session, VirtualView.DefaultView);
			}else{
				return getInstance(session, viewName_str);
			}
		}
	}

	public static EccTreeModel getInstance(HttpSession session) {
		return getInstance(session, VirtualView.DefaultView);
	}

	/*
	 * 获得只含有SE监测器节点的树
	 */
	public static EccTreeModel getMonitorTreeInstance(Session session) {
		EccTreeModel eccTreeModel = EccTreeModel.getInstance(session);
		EccTreeItem SeRoot = findSeParent(eccTreeModel.root);
		if (SeRoot != null)
			return new EccTreeModel(SeRoot);
		return null;
	}

	public static EccTreeItem findSeParent(EccTreeItem treeItem) {
		for (EccTreeItem item : treeItem.getChildRen()) {
			if (item.getType().equals(INode.SE))
				return treeItem;
			else {
				EccTreeItem parent = findSeParent(item);
				if (parent != null)
					return parent;
			}

		}
		return null;
	}

	public static EccTreeModel getInstance(HttpSession session, String rootid) {
		EccTreeModel model = (EccTreeModel) session
				.getAttribute("eccTreeModel_" + rootid);
		if (model == null) {
			model = new EccTreeModel(new EccTreeItem(null, rootid, "整体树", ""));
			session.setAttribute("eccTreeModel_" + rootid, model);
		}
		return model;
	}

	public static EccTreeModel getInstance(Session session, String rootid) {
		return getInstance((HttpSession) session.getNativeSession(), rootid);
	}

	public static String getVirtualItemId(String strId) {
		// 这项设置务必保留，否则视图的编辑部分会出问题。现在的代码里依赖这个 i 了。
		return "i" + strId;
	}

	public void disableChangeOfTreeEditting() {
		canChangeTree = false;
	}

	/**
	 * 为虚拟视图编辑页面的那棵树，增加树节点
	 */
	public void setTreeEdittingNode() {
		EccTreeItem newroot = new EccTreeItem(null, root.getId(), "虚拟视图编辑", "");
		newroot.addChild(new EccTreeItem(newroot,
				getVirtualItemId(VirtualView.GarbageType), VirtualView.Garbage,
				VirtualView.GarbageType));
		newroot.addChild(new EccTreeItem(newroot,
				getVirtualItemId(VirtualView.NewVirGroupType),
				VirtualView.NewVirGroup, VirtualView.NewVirGroupType));
		root.setTitle(root.getId());
		root.setType(VirtualView.ViewEdittingType);
		root.setItemId(VirtualView.ViewEdittingType);
		newroot.addChild(root);
		root = newroot;
	}

	public EccTreeItem getRoot() {
		if (root.getChildRen().size() == 0) {
			long l = System.currentTimeMillis();
			if (getView() == null)
				return null;

			// 1、构造整棵树，包括“报表”等功能菜单节点，和非默认视图的整体视图
			String defaultViewId = "";
			List<VirtualView> av = getView().getAllVirtualView();
			for (VirtualView v : av) {
				if (v.getViewName().equals(root.getId())) {
					virtualView = v;
					defaultViewId = buildSonVirtualItem(root, v, null);
				}
			}

			// 2、默认视图， 追加构造整体视图
			EccTreeItem defaultViewItem = findNode(defaultViewId);
			if (root.getId().equals(VirtualView.DefaultView)) {
				INode[] ses = getView().getSe();
				if (ses == null)
					return null;
				for (INode se : ses)
					constructNode(defaultViewItem, getView().getNode(
							se.getSvId()));
			}

			logger.info("花费时间：构造树全部数据="
					+ (System.currentTimeMillis() - l) + "mms");
		}
		return root;
	}

	public String buildSonVirtualItem(EccTreeItem parent, VirtualView vv,
			VirtualItem v) {
		String defaultViewId = null;
		String defaultViewId_from_son = null;

		List<VirtualItem> vis = null;
		if (v == null)
			vis = vv.getTopItems();
		else
			vis = vv.getSonItems(v);
		if (vis == null)
			return null;

		if (vis.size() != 0) {
			for (VirtualItem vi : vis) {
				EccTreeItem child = null;
				// 节点是虚拟项目
				if (vi.getType().equals(VirtualView.Item)) {
					// 获取整体视图节点ID
					if (vi.getItemDataZulType().equals(
							VirtualItem.WholeView.zulType))
						defaultViewId = getVirtualItemId(vi.getItemId());

					String type = vi.getItemDataZulType();
					if (type.equals(VirtualItem.VirtualGroup.zulType))
						type = INode.GROUP;
					else if (!this.isContainMenu()
							&& !type.equals(VirtualItem.WholeView.zulType))
						continue;

					child = new EccTreeItem(parent, getVirtualItemId(vi
							.getItemId()), vi.getItemDataZulName(), type);
				}

				// 节点是监测器数据等
				if (vi.getType().equals(VirtualView.INode)) {
					INode node = getView().getNode(vi.getSvId());
					child = new EccTreeItem(parent, node.getSvId(), node
							.getName(), node.getType());
					child.setStatus(Toolkit.getToolkit().changeStatusToInt(
							node.getStatus()));
					child.setValue(node);
				}
				if (child == null)
					continue;
				child.setItemId(vi.getItemId());
				parent.addChild(child);
				defaultViewId_from_son = buildSonVirtualItem(child, vv, vi);
				parent.refreshStatus();
			}
		}
		if (defaultViewId != null)
			return defaultViewId;
		else
			return defaultViewId_from_son;
	}
	
	int constructNode(EccTreeItem parent,INode node) {
		if(parent==null)
			return 0;
		
		EccTreeItem ti = new EccTreeItem(parent, node.getSvId(), node
				.getName(),node.getType());
		ti.setValue(node);
		parent.addChild(ti);

		if (INode.MONITOR.equals(node.getType())) {
			ti.setStatus(Toolkit.getToolkit().changeStatusToInt(
					node.getStatus()));
			// if(ti.getStatus()!=EccTreeItem.STATUS_OK)/*状态非正常*/
			return 1;
		}

		IForkNode f = (IForkNode) node;
		List<String> ids = f.getSonList();
		int count = 0;
		for (String id : ids) {
			// 递归构造所有儿子
			INode n = getView().getNode(id);
			if (n == null)
				continue;

			count += constructNode(ti, n);
		}

		if (count > 0)
			updateStatus(ti, false);/* 当且仅当有monitor添加并完成,才启动父节点的状态更新 */

		return 0;
	}

	/*
	 * 重新计算父节点的状态,不处理参数treeItem的状态更新问题
	 */
	public void updateStatus(EccTreeItem node, boolean fireEvent) {
		if (node == null)
			return;
		if (node.refreshStatus()) /* 状态改变 */
		{
			if (fireEvent)
				notifyStatusChange(node);
			updateStatus(node.getParent(), fireEvent); /* 递归更新父亲状态 */
		}
	}

	private void notifyStatusChange(EccTreeItem treeItem) {
		try{
			if(treeItem.getTitle()==null)
				treeItem.setTitle("");
			EccTreeItem targetParent = treeItem.getParent();
			int idx = targetParent.getChildRen().indexOf(treeItem);
			super.fireEvent(targetParent, idx, idx, TreeDataEvent.CONTENTS_CHANGED);
		}catch(Exception e){}

	}

	@Override
	public Object getChild(Object parent, int childIdx) {
		try{
			return ((EccTreeItem) parent).getChildRen().get(childIdx);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent == null)
			return 0;
		return ((EccTreeItem) parent).getChildRen().size();
	}

	@Override
	public boolean isLeaf(Object node) {
		if (!displayMonitor
				&& ((EccTreeItem) node).getType().equals(INode.ENTITY))
			return true;
		if (getChildCount(node) == 0)
			return true;
		return false;
	}

	private Desktop getDesktop() {
		return Executions.getCurrent().getDesktop();
	}

	private boolean isTop(String id) {
		return root.getId().equals(id);
	}

	/*
	 * 递归查找某节点的子孙节点
	 * 
	 * @param parent从该节点(含)开始查找
	 * 
	 * @param nodeId查找目标的节点ID
	 */
	public EccTreeItem findNode(EccTreeItem node, String nodeId) {
		// logger.info(node.getId() + node.getTitle());
		if (node.getId().equals(nodeId))
			return node;
		else {
			for (EccTreeItem item : node.getChildRen()) {
				EccTreeItem tmpItem = findNode(item, nodeId);
				if (tmpItem != null)
					return tmpItem;
			}
		}

		return null;
	}

	/*
	 * 递归查找全树子孙节点
	 * 
	 * @param nodeId查找目标的节点ID
	 */
	public EccTreeItem findNode(String nodeId) {
		return findNode(root, nodeId);
	}
	
	/**
	 * 在使用虚拟视图时，根据svId和itemId查找目标节点
	 * @param nodeId
	 * @return EccTreeItem
	 */
	public EccTreeItem findNodeInVirtrualView(String nodeId, String itemId){
		return findNodeInVirtrualView(root, nodeId, itemId);
	}
	
	/**
	 * 递归查找某节点的子孙节点
	 * @param node从该节点(含)开始查找
	 * @param nodeId查找目标的节点ID
	 * @param itemId目标节点的itemId
	 * @return EccTreeItem
	 */
	public EccTreeItem findNodeInVirtrualView(EccTreeItem node, String nodeId, String itemId){
		if(itemId == null){
			EccTreeItem tmpItem = findNode(node, nodeId);
			return tmpItem;
		}
		if (node.getId().equals(nodeId) && itemId.equals(node.getItemId()))
			return node;
		else {
			for (EccTreeItem item : node.getChildRen()) {
				EccTreeItem tmpItem = findNodeInVirtrualView(item, nodeId, itemId);
				if (tmpItem != null)
					return tmpItem;
			}
		}
		return null;
	}
	
	public void changeTree(ChangeDetailEvent event){
		if(event.getType() == ChangeDetailEvent.TYPE_ADD){
			changeTreeAdd(event);
		}else if(event.getType() == ChangeDetailEvent.TYPE_MODIFY){
			changeTreeModify(event);
		}else if(event.getType() == ChangeDetailEvent.TYPE_DELETE){
			changeTreeDelete(event);
		}}
	
	private void changeTreeAdd(ChangeDetailEvent changeEvent){
		if (!canChangeTree)
			return;

		INode node = changeEvent.getData();
		EccLayoutComposer elc = (EccLayoutComposer)Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("main").getAttribute("Composer");
		elc.refreshData();


		if (virtualView != null) {
			String vname = virtualView.getViewName();
			if (vname != null && !vname.equals(VirtualView.DefaultView))
				return;
		}
		EccTreeItem target = findNode(changeEvent.getSvid());
		if (target != null)
			return;
		EccTreeItem targetParent = findNode(changeEvent.getData()
				.getParentSvId());
		EccTreeItem newItem = new EccTreeItem(targetParent, node.getSvId(),
				node.getName(), node.getType());
		newItem.setValue(node);
		targetParent.addChild(newItem);

		//读取doMap中的内容并根据dowhat的内容进行操作
		Session session = Executions.getCurrent().getDesktop().getSession();
		HashMap<String, String> doMap = (HashMap<String, String>)session.getAttribute("doMap");
		String dowhat = "";
		if(doMap!=null){
			dowhat = doMap.get("dowhat");
		}
		if("addMonitor".equals(dowhat)){
			session.setAttribute("selectedItem", newItem);
			session.setAttribute("selListItemIndex", targetParent.getChildRen().size());
		}
		
		int idx = targetParent.getChildRen().indexOf(newItem);
		if (node.getType().equals(INode.MONITOR)) {
			int status = Toolkit.getToolkit().changeStatusToInt(
					node.getStatus());
			newItem.setStatus(status);
			updateStatus(targetParent, true);
			if (isDisplayMonitor())
				fireEvent(targetParent, idx, idx,
						TreeDataEvent.INTERVAL_ADDED);
		} else {
			fireEvent(targetParent, idx, idx, TreeDataEvent.INTERVAL_ADDED);
		}
	}
	
	private void changeTreeModify(ChangeDetailEvent changeEvent){
		if (!canChangeTree)
			return;

		INode node = changeEvent.getData();
		EccLayoutComposer elc = (EccLayoutComposer)Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("main").getAttribute("Composer");
		elc.refreshData();

		EccTreeItem newroot = (EccTreeItem)((Treeitem)elc.getTree().getFirstChild().getChildren().get(0)).getValue();
		EccTreeItem targetParent = findNode(newroot,node.getParentSvId());
		EccTreeItem target = null;
		if(targetParent!=null){
			target = new EccTreeItem(targetParent, node.getSvId(), node.getName(), node.getType());
			target.setValue(node);
		}else{
			target = findNode(changeEvent.getSvid());
		}
		
		if (target == null)
			return;

		int status = Toolkit.getToolkit().changeStatusToInt(
				node.getStatus());
		if (!node.getName().equals(target.getTitle())) /* 标题变化 */
		{
			target.setTitle(node.getName());
			int idx = targetParent.getChildRen().indexOf(target);

			if (node.getType().equals(INode.MONITOR)) {
				if (this.isDisplayMonitor())
					fireEvent(targetParent, idx, idx,
							TreeDataEvent.CONTENTS_CHANGED);
			} else
				fireEvent(targetParent, idx, idx,
						TreeDataEvent.CONTENTS_CHANGED);
		}

		if (target.getStatus() != status)/* 状态变化,重新计算父节点状态 */
		{
			if(target.getStatus() != 2 || status == 1 || status == 16){		//没有别的状态比定义错误优先级高
				if(target.getStatus() != 32 && status == 4){
					target.setStatus(status);
				}else if(target.getStatus() ==16){		//启动监测器情况
					target.setStatus(status);
				}else if(status == 16 && target.getStatus() != 16){		//禁止监测器情况
					target.setStatus(status);
				}
			}
			if(targetParent == null || targetParent.getChildRen() == null){
				return;
			}
			int idx = targetParent.getChildRen().indexOf(target);
			updateStatus(targetParent, true);
			if (node.getType().equals(INode.MONITOR)) {
				if (this.isDisplayMonitor())
				{fireEvent(targetParent, idx, idx,TreeDataEvent.CONTENTS_CHANGED);}
			}
			else
			{
				try{
					fireEvent(targetParent, idx, idx,TreeDataEvent.CONTENTS_CHANGED);
				}catch(Exception e){}
			}
		}
		Session session = Executions.getCurrent().getDesktop().getSession();
		session.setAttribute("itemFromTree", target);
	}
	
	private void changeTreeDelete(ChangeDetailEvent changeEvent){
		if (!canChangeTree)
			return;

		INode node = changeEvent.getData();

		if (virtualView != null) {
			String vname = virtualView.getViewName();
			if (vname != null && !vname.equals(VirtualView.DefaultView))
				return;
		}
		EccTreeItem target = findNode(changeEvent.getSvid());
		if (target == null)
			return;
		EccTreeItem targetParent = target.getParent();
		int idx = targetParent.getChildRen().indexOf(target);
		targetParent.getChildRen().remove(target);
		updateStatus(targetParent, true);
		if (node.getType().equals(INode.MONITOR)) {
			if (this.isDisplayMonitor())
				fireEvent(targetParent, idx, idx,
						TreeDataEvent.INTERVAL_REMOVED);
		} else
			fireEvent(targetParent, idx, idx,
					TreeDataEvent.INTERVAL_REMOVED);
	}

	protected void fireEvent(Object node, int indexFrom, int indexTo,
			int evtType) {
		try{
			if(node==null){
				return;
			}
			super.fireEvent(node, indexFrom, indexTo, evtType);
		} catch (Exception e) {
		}
	}

	public boolean getPathList(ArrayList<EccTreeItem> pathList,
			EccTreeItem curNode) {
		if (curNode == null)
			return false;
		pathList.add(0, curNode);
		EccTreeItem parentNode = curNode.getParent();
		if (parentNode != null) {
			if (getPathList(pathList, parentNode) == false)
				return false;
		}
		return true;
	}

	public ArrayList<EccTreeItem> getAllEntity(String like) {
		ArrayList<EccTreeItem> resultList = new ArrayList<EccTreeItem>();
		makeAllEntity(getRoot(), resultList, like);
		return resultList;

	}

	private void makeAllEntity(EccTreeItem startItem,
			ArrayList<EccTreeItem> resultList, String like) {
		for (EccTreeItem item : startItem.getChildRen()) {
			if (item.getType().equals(INode.ENTITY)) {
				if (like.length() == 0 || item.getTitle().startsWith(like))
					resultList.add(item);
			} else
				makeAllEntity(item, resultList, like);
		}
	}
	
	/**
	 * 根据svid返回ArrayList<EccTreeItem>
	 * @param svid
	 * @return resultList
	 */
	public ArrayList<EccTreeItem> getAllMonitor(String svid, boolean isFuzzy){
		ArrayList<EccTreeItem> resultList = new ArrayList<EccTreeItem>();
		makeAllMonitor(getRoot(), resultList, svid, isFuzzy);
		return resultList;
	}
	
	/**
	 * 在stratItem下根据svid找到所有符合条件的EccTreeItem存入resultList中并返回，
	 * isFuzzy为true则为模糊查询
	 * @param startItem
	 * @param resultList
	 * @param svid
	 * @param isFuzzy
	 */
	private void makeAllMonitor(EccTreeItem startItem, ArrayList<EccTreeItem> resultList, String svid, boolean isFuzzy){
		for (EccTreeItem item : startItem.getChildRen()) {
			if(isFuzzy){
				if (item.getType().equals(INode.MONITOR)) {
					if (svid.length() == 0 || item.getId().startsWith(svid))
						resultList.add(item);
				} else
					makeAllMonitor(item, resultList, svid, isFuzzy);
			}else{
				if (item.getType().equals(INode.MONITOR)) {
					if (svid.length() == 0 || item.getId().equals(svid))
						resultList.add(item);
				} else
					makeAllMonitor(item, resultList, svid, isFuzzy);
			}
		}
	}

	public static EccTreeModel getInstanceForAlertByViewName(Session session,
			String viewName) {
		if (viewName == null)
			viewName = VirtualView.DefaultView;
		EccTreeModel model = new EccTreeModel(new EccTreeItem(null, viewName,
				"选择树", ""));
		if (model != null)
			model.setContainMenu(false);
		return model;
	}

	/**
	 * 判断是否显示功能菜单项 -- 标志
	 */
	private boolean containMenu = true;

	/**
	 * 判断是否显示功能菜单项
	 */
	public boolean isContainMenu() {
		return containMenu;
	}

	public void setContainMenu(boolean containMenu) {
		this.containMenu = containMenu;
	}
	
	public EccTreeModel clone(){
		try {
			return (EccTreeModel)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}
