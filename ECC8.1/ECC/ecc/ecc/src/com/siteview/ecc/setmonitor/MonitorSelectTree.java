/**
 * 
 */
package com.siteview.ecc.setmonitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import com.siteview.base.data.VirtualView;
import com.siteview.base.manage.View;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.alert.control.CheckableTreeitem;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccTreeModel;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.treeview.controls.BaseTreeitem;
import com.siteview.ecc.util.Toolkit;

public class MonitorSelectTree extends Tree {
	private static final long serialVersionUID = -6802096244149353160L;
	private static final String expandAll	 	= "全部展开";
	private static final String closeAll	 	= "全部收缩";
	private static final String batchSelect 	= "批量选择";
	private boolean checkable = true;

	private EccTreeModel treemodel = null;

	public EccTreeModel getTreemodel() {
		return treemodel;
	}

	private String viewName = null;

	/**
	 * 取得视图名称
	 * 
	 * @return 视图名称
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * 设置视图名称
	 * 
	 * @param viewName
	 *            视图名称
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
		initTree();
	}

	private String type = null;

	/**
	 * 设置显示的检测器类型
	 * 
	 * @param monitorType
	 *            检测器类型
	 */
	public void setMonitorType(String monitorType) {
		this.type = monitorType;
		initTree();
	}

	/**
	 * 取得设置的检测器类型
	 * 
	 * @return 检测器类型
	 */
	public String getMonitorType() {
		return type;
	}

	/**
	 * 是否有复选框
	 * 
	 * @return true/false
	 */
	public boolean isCheckable() {
		return checkable;
	}

	/**
	 * 设置是否有复选框
	 * 
	 * @param checkable
	 *            是否有复选框
	 */
	public void setCheckable(boolean checkable) {
		this.checkable = checkable;
	}

	// id列表
	private List<String> selectedIds = new ArrayList<String>();
	Menu smenu = new Menu(); 

	/**
	 * 组件创建事件 初始化信息
	 */
	public void onCreate() throws Exception {
		try {
			// 将已经选择的ids，给添加到id列表中去
			String target = (String) this.getDesktop().getExecution()
					.getAttribute("all_selected_ids");
			if (target == null) {
				target = (String) this.getVariable("all_selected_ids", true);
			}
			if (target != null) {
				String[] idsArray = target.split(",");
				for (String idstr : idsArray) {
					if (idstr == null)
						continue;
					if ("".equals(idstr))
						continue;
					selectedIds.add(idstr);
				}
			}
			// 初始化树
			initTree();
			Menupopup popup = getMenupopup();
			popup.appendChild(smenu);
			this.setContext(popup);
			popup.setPage(this.getPage());
			popup.setParent(getRoot());
			this.addEventListener(Events.ON_SELECT, new TypeMenuOpenListener());
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
					Messagebox.ERROR);
		}
	}
	
	private Menupopup getMenupopup(){
		Menupopup menu = new Menupopup();
	//	this.addMenupopupItem(menu, expandAll,new ExpandAllListener());
	//	this.addMenupopupItem(menu, closeAll, new CloseAllListener());
		return menu;
	}
	private Menuitem addMenupopupItem(Menupopup menupopup ,String label,EventListener listener){
		Menuitem mitem = new Menuitem();
		mitem.setLabel(label.toString());
		mitem.addEventListener(Events.ON_CLICK, listener);
		menupopup.appendChild(mitem);
		return mitem;
	}
	class ExpandAllListener implements EventListener{
		@Override
		public void onEvent(Event event) throws Exception {
			Treeitem item = getSelectedItem();
			open(item);
		}
	}
	class CloseAllListener implements EventListener{
		@Override
		public void onEvent(Event arg0) throws Exception {
			Treeitem item = getSelectedItem();
			item.setOpen(false);
		}
	}
	
	class BatchSelectListener implements EventListener{
		String type ;
		BatchSelectListener(String type){
			this.type = type;
		}
		@Override
		public void onEvent(Event arg0) throws Exception {
			try{
				Treeitem item = getSelectedItem();
				if(item==null) return;
				EccTreeItem eccItem = (EccTreeItem)item.getValue();
				List<String> ids = getAllMonitors(treemodel,eccItem);
				StringBuilder sb = new StringBuilder();
				View v = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
				for(String id : ids){
					INode node = v.getNode(id);
					MonitorInfo minfo = v.getMonitorInfo(node);
					if(minfo.getMonitorType().equals(type)){
						sb.append(id).append(",");
					}
				}
				setAttribute(sb.toString());
				onCreate();
				Events.sendEvent(arg0.getPage().getFellow("setBatchWin"),
						new Event("onCheckItem", arg0.getPage().getFellow(
								"setBatchWin")));//发送check操作事件
			}catch(Exception e){
				e.printStackTrace();
			}


		}
	}
	public void setAttribute(String all_selected_ids){
		this.getDesktop().getExecution().setAttribute("all_selected_ids", all_selected_ids);
	}
	class TypeMenuOpenListener implements EventListener{
		@Override
		public void onEvent(Event arg0) throws Exception {
			try{
				Treeitem item = getSelectedItem();
				if(item==null) return;
				EccTreeItem eccItem = (EccTreeItem)item.getValue();
				if(eccItem == null ) return;
				Menupopup mpp = smenu.getMenupopup();
				if(mpp==null) {
					mpp = new Menupopup();
					smenu.appendChild(mpp);
				}
				
				mpp.getChildren().clear();
				Set<MonitorTemplateItem> monitorType = new TreeSet<MonitorTemplateItem>(new Comparator<MonitorTemplateItem>(){
					@Override
					public int compare(MonitorTemplateItem o1,
							MonitorTemplateItem o2) {
						return o1.label.compareTo(o2.label);
					}
				});
				findMonitorType(monitorType,eccItem);
				for(MonitorTemplateItem type : monitorType){
					Menuitem mi = addMenupopupItem(mpp,type.label,new BatchSelectListener(type.id));
					mi.setId(type.id);
				}
				if(monitorType.size()>16){
					mpp.setStyle("overflow-y:auto;height:400px");
				}else{
					int height = monitorType.size()*24;
					if(height<24) height = 24;
					mpp.setStyle("overflow-y:auto;height:"+String.valueOf(height)+"px");
				}
				smenu.setWidth("100px");
				smenu.setLabel(batchSelect);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public void findMonitorType(Set<MonitorTemplateItem> monitorType, EccTreeItem items){
		if(items==null) return;
		List<EccTreeItem> subItems = items.getChildRen();
		for(EccTreeItem item : subItems){
			INode node = item.getValue();
			if(node==null) continue;
			if(node.getType().equals(INode.MONITOR)){
				MonitorTemplate tmp = treemodel.getView().getMonitorInfo(node).getMonitorTemplate();
				if(tmp == null) continue;
				if(tmp.get_sv_id() == null) continue;
				if(tmp.get_sv_name() == null) continue;

				
				monitorType.add(new MonitorTemplateItem(tmp.get_sv_id(),tmp.get_sv_name()));
			}else{
				findMonitorType(monitorType,item);
			}
		}
	}
	/**
	 * Treeitem给展开的时候的执行代码
	 */
	public class TreeitemOpenListener implements org.zkoss.zk.ui.event.EventListener {
		private BaseTreeitem treeitem = null;
		private EccTreeItem node = null;
		private MonitorSelectTree tree;

		public TreeitemOpenListener(BaseTreeitem treeitem,
				MonitorSelectTree tree) throws Exception {
			this.treeitem = treeitem;
			this.tree = tree;
			Object obj = treeitem.getValue();
			// 不带有节点信息
			if (obj instanceof EccTreeItem) {
				node = (EccTreeItem) obj;
			} else {
				throw new Exception("该节点不包含预定的合法的数据:" + obj != null ? obj
						.getClass().getName() : "NULL");
			}
		}

		@Override
		public void onEvent(Event event) throws Exception {
			try {
				if (node == null)
					return;

				// 过滤不需要处理的节点
				Treechildren mytreechildren = treeitem.getTreechildren();
				// 无子节点或者是处于收缩状态的节点
				if (mytreechildren == null
						|| mytreechildren.getChildren().size() > 0
						|| treeitem.isOpen() == false)
					return;
				// 是监测器节点的情况
				if (INode.MONITOR.equals(node.getType())) {
					return;
				}

				List<EccTreeItem> sons = node.getChildRen();
				// 查找其儿子
				if (sons != null && sons.size() > 0) {

					for (EccTreeItem son : sons) {
						if (son == null)
							continue;
						// 如果是监视器 -- 页节点
						if (INode.MONITOR.equals(son.getType())) {
							if (this.tree.getMonitorType() != null) {
								MonitorInfo monitorinfo = this.tree
										.getTreemodel().getView()
										.getMonitorInfo(son.getValue());
								if (!this.tree.getMonitorType().equals(
										monitorinfo.getMonitorType())) {
									continue;
								}
							}
							BaseTreeitem tii = getTreeitem(son);
							tii.setParent(mytreechildren);
						} else {
							if (existChildren(son)) {
								// 如果存在儿子
								BaseTreeitem tii = getTreeitem(son);
								tii.setParent(mytreechildren);
								Treechildren newtreechildren = new Treechildren();
								tii.appendChild(newtreechildren);// 给以可以展开的属性
								tii
										.addEventListener(Events.ON_OPEN,
												new TreeitemOpenListener(tii,
														this.tree));// 添加展开事件
							}
						}

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
						Messagebox.ERROR);
			}
		}

	}

	/**
	 * 当Treeitem给选择上的时候，执行
	 */
	public class TreeitemCheckListener implements
			org.zkoss.zk.ui.event.EventListener {
		private BaseTreeitem treeitem = null;
		private EccTreeItem localnode = null;

		public TreeitemCheckListener(BaseTreeitem treeitem) throws Exception {
			this.treeitem = treeitem;

			Object obj = treeitem.getValue();
			// 不带有节点信息
			if (obj instanceof EccTreeItem) {
				localnode = (EccTreeItem) obj;
			} else {
				throw new Exception("该节点不包含预定的合法的数据:" + obj != null ? obj
						.getClass().getName() : "NULL");
			}
		}

		@Override
		public void onEvent(Event event) throws Exception {
			try {
				if (this.treeitem.isChecked()) {
					this.addMonitorsToList(localnode);
					// 此处加上选中事件处理
					
				} else {
					this.removeMonitorsFromList(localnode);
					// 此处加上取消选中事件处理
					
				}
				Events.sendEvent(event.getPage().getFellow("setBatchWin"),
						new Event("onCheckItem", event.getPage().getFellow(
								"setBatchWin")));//发送check操作事件

				reflash();

			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
						Messagebox.ERROR);
			}
		}

		/**
		 * 增加检测器节点id到清单中
		 */
		private void addMonitorsToList(EccTreeItem node) {
			for (String id : getAllMonitors(treemodel, node)) {
				if (selectedIds.contains(id))
					continue;
				selectedIds.add(id);
			}
		}

		/**
		 * 从清单中，清除节点node.id
		 */
		private void removeMonitorsFromList(EccTreeItem node) {
			for (String id : getAllMonitors(treemodel, node)) {
				selectedIds.remove(id);
			}
		}

		/**
		 * 刷新节点的选择 -- 父节点和子节点们
		 */
		private void reflash() {
			allLookupChildren(this.treeitem);
			allLookupParent(this.treeitem);
		}

		/**
		 * 刷新节点的选择 -- 子节点们
		 */
		private void allLookupChildren(Treeitem treeitem) {
			if (treeitem.getTreechildren() == null)
				return;
			for (Object item : treeitem.getTreechildren().getItems()) {
				if (item instanceof Treeitem) {
					EccTreeItem node = (EccTreeItem) ((BaseTreeitem) item)
							.getValue();
					((BaseTreeitem) item).setChecked(existNode(node));
					this.allLookupChildren((Treeitem) item);
				}
			}
		}

		/**
		 * 刷新节点的选择 -- 父节点们
		 */
		private void allLookupParent(BaseTreeitem treeitem) {
			if (treeitem == null)
				return;
			BaseTreeitem item = treeitem.getParentItem();
			if (item == null)
				return;
			EccTreeItem node = (EccTreeItem) item.getValue();
			item.setChecked(existNode(node));
			this.allLookupParent(item);
		}
	}

	/**
	 * 初始化树上的数据
	 * 
	 */
	public void initTree() {
	    
		Session session = Executions.getCurrent().getDesktop().getSession();
		Object selectedViewNameObject = session.getAttribute("selectedViewName");
		if(selectedViewNameObject!=null){
			String selectedViewName =(String)selectedViewNameObject;
			if(selectedViewName != null && !selectedViewName.isEmpty()){
				this.viewName = selectedViewName;
			}
		}

		this.initTree(this.getViewName());
	}

	private void initTree(String virtualName) {
		// 取得view
		this.treemodel = EccTreeModel.getInstanceForAlertByViewName(
				getDesktop().getSession(), virtualName);
		this.treemodel.setDisplayMonitor(true);

		// 清除树里的节点
		clearTree();

		Treechildren treechildren = this.getTreechildren();
		treechildren.setParent(this);

		EccTreeItem root = this.treemodel.getRoot();
		if (root == null)
			return;

		// 缺省视图的时候，取第一个根
		if (VirtualView.DefaultView.equals(virtualName) || virtualName == null) {
			for (EccTreeItem item : root.getChildRen()) {
				root = item;
				break;
			}
		}

		// 显示第一层节点
		for (EccTreeItem item : root.getChildRen()) {
			try {
				BaseTreeitem ti = getTreeitem(item);
				ti.setParent(treechildren);
				if (existChildren(item)) {
					// 存在子节点，则添加可展开属性和展开事件
					Treechildren mytreechildren = new Treechildren();
					ti.appendChild(mytreechildren);
					ti.addEventListener(Events.ON_OPEN,
							new TreeitemOpenListener(ti, this));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 展开节点，如果是缺省视图，展开第一层
		if (VirtualView.DefaultView.equals(virtualName) || virtualName == null) {
			this.open(1);
		}
	}

	private void open(int level) {
		Treechildren treechildren = this.getTreechildren();

		for (int mylevel = 0; mylevel < level; mylevel++) {
			for (Object object : treechildren.getItems()) {
				if (object instanceof Treeitem) {
					Treeitem item = (Treeitem) object;
					item.setOpen(true);
					Events.sendEvent(item, new Event(Events.ON_OPEN, item));
					treechildren = item.getTreechildren();
					if (treechildren == null
							|| treechildren.getChildren().size() == 0) {
						continue;
					}
					break;
				}
			}
		}
	}
	private void open(Treeitem tii) {
		if(tii==null) return;
		Treechildren tch = tii.getTreechildren();
		tii.setOpen(true);
		Events.sendEvent(tii, new Event(Events.ON_OPEN, tii));
		if(tch==null || tch.getItems().size()<=0) return;
		Collection collections = tch.getItems();
		Object[] objs = collections.toArray();
		for(Object obj : objs){
			if(obj instanceof Treeitem){
				Treeitem ti = (Treeitem)obj;
				open(ti);
			}
		}
	}
	/**
	 * 清除树里的节点
	 */
	private void clearTree() {
		this.clear();
		if (this.getTreechildren() == null) {
			new Treechildren().setParent(this);
		}
	}

	/**
	 * 节点node是否存在儿子
	 * 
	 * @param node
	 *            节点
	 * @return 是、否
	 */
	private boolean existChildren(EccTreeItem node) {
		List<String> ids = getAllMonitors(treemodel, node);
		if (ids == null || ids.size() == 0)
			return false;
		return true;
		/*
		 * if (node.getChildRen()!=null && node.getChildRen().size() > 0){
		 * return true; } return false;
		 */
	}

	public List<String> getAllMonitors(EccTreeModel treemodel, EccTreeItem node) {
		List<String> retlist = BaseTools.getAllMonitors(getMonitorType(),
				treemodel, node);
		return retlist;
	}

	/**
	 * 通过node，取得初始化了属性的Treeitem
	 * 
	 * @throws Exception
	 */
	public BaseTreeitem getTreeitem(EccTreeItem node) throws Exception {
		BaseTreeitem tii = this.isCheckable() ? new CheckableTreeitem()
				: new BaseTreeitem();
		this.setTreeitem(tii, node);
		tii.addEventListener(Events.ON_CHECK, new TreeitemCheckListener(tii));

		return tii;
	}

	/**
	 * 设置treeitem的初始化属性
	 * 
	 * @param tii
	 *            ：需要初始化属性的treeitem
	 * @param node
	 *            : 其node值
	 */
	public void setTreeitem(BaseTreeitem tii, EccTreeItem node) {
		tii.setLabel(node.toString());
		// tii.setId(node.getId());
		tii.setImage(getImage(node));
		tii.setOpen(false);
		tii.setChecked(this.existNode(node));
		tii.setValue(node);
	}

	/**
	 * 清单中是否存在该id
	 * 
	 * @param id
	 * @return 是、否
	 */
	public boolean existIdById(String id) {
		if (id == null)
			return false;
		for (String idstr : this.selectedIds) {
			if (this.isChildId(id, idstr))
				return true;
			if (id.equals(idstr))
				return true;
		}
		return false;
	}

	private boolean existNode(EccTreeItem node) {
		if (node == null)
			return false;
		if (node.getId() == null)
			return false;
		for (String idstr : this.selectedIds) {
			if (this.isChildId(node, idstr))
				return true;
			if (node.getId().equals(idstr))
				return true;
		}
		return false;
	}

	/**
	 * 判断是否是parentid的子孙
	 * 
	 * @param parentid
	 *            ：父id
	 * @param id
	 *            : 子孙id
	 * @return 是、否
	 */
	private boolean isChildId(String parentid, String id) {
		if (parentid == null)
			return false;
		if (id == null)
			return false;

		EccTreeItem node = treemodel.findNode(parentid);

		return isChildId(node, id);
		// if (parentid.startsWith("i")) return true;
		// return (id.startsWith(parentid + "."));
	}

	private boolean isChildId(EccTreeItem parentnode, String id) {
		if (parentnode == null)
			return false;
		if (id == null)
			return false;

		for (EccTreeItem son : parentnode.getChildRen()) {
			if (id.equals(son.getId()))
				return true;
			if (isChildId(son, id))
				return true;
		}
		return false;
		// if (parentid.startsWith("i")) return true;
		// return (id.startsWith(parentid + "."));
	}

	/**
	 * 取得node的表示用图像的链接
	 * 
	 * @param node
	 * @return 图像的链接
	 */
	private String getImage(EccTreeItem node) {
		if (node.getType().equals(INode.GROUP)
				|| node.getType().equals(INode.ENTITY)
				|| node.getType().equals(INode.MONITOR))
			return EccWebAppInit.getInstance().getImage(node.getType(),
					node.getStatus());
		else
			return EccWebAppInit.getInstance().getImage(node.getType());
	}

	/**
	 * 取得设置的全部id值
	 * 
	 * @return List<String>
	 */
	public List<String> getSelectedIds() {
		return selectedIds;
	}

	/**
	 * 取得设置的全部id值
	 * 
	 * @return String 形如"id1,id2,id3,"的字符串
	 */
	public String getAllSelectedIds() {
		StringBuffer sb = new StringBuffer();
		for (String obj : getSelectedIds()) {
			if (sb.length() > 0)
				sb.append(",");
			sb.append(obj);
		}
		if (sb.length() == 0) {
			return null;
		}
		sb.append(",");
		return sb.toString();
	}
	
	static class MonitorTemplateItem{
		String id="";
		String label = "";
		public MonitorTemplateItem(String id, String label) {
			super();
			this.id = id;
			this.label = label;
		}
	}
}
