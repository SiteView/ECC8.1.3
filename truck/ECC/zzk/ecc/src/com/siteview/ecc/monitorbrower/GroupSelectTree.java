package com.siteview.ecc.monitorbrower;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import com.siteview.base.data.VirtualView;
import com.siteview.base.tree.INode;
import com.siteview.ecc.alert.control.CheckableTreeitem;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccTreeModel;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.treeview.controls.BaseTreeitem;

public class GroupSelectTree extends Tree {
	private static final long serialVersionUID = -6802096244149353160L;

	private boolean checkable = true;

	private EccTreeModel treemodel = null;

	public EccTreeModel getTreemodel() {
		return treemodel;
	}

	private String viewName = null;

	/**
	 * ȡ����ͼ����
	 * 
	 * @return ��ͼ����
	 */
	public String getViewName() {
		return viewName;
	}

	/**
	 * ������ͼ����
	 * 
	 * @param viewName
	 *            ��ͼ����
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
		initTree();
	}

	private String type = null;

	/**
	 * ������ʾ�ļ��������
	 * 
	 * @param monitorType
	 *            ���������
	 */
	public void setMonitorType(String monitorType) {
		this.type = monitorType;
		initTree();
	}

	/**
	 * ȡ�����õļ��������
	 * 
	 * @return ���������
	 */
	public String getMonitorType() {
		return type;
	}

	/**
	 * �Ƿ��и�ѡ��
	 * 
	 * @return true/false
	 */
	public boolean isCheckable() {
		return checkable;
	}

	/**
	 * �����Ƿ��и�ѡ��
	 * 
	 * @param checkable
	 *            �Ƿ��и�ѡ��
	 */
	public void setCheckable(boolean checkable) {
		this.checkable = checkable;
	}

	// id�б�
	private List<String> selectedIds = new ArrayList<String>();

	/**
	 * ��������¼� ��ʼ����Ϣ
	 */
	public void onCreate() throws Exception {
		try {
			// ���Ѿ�ѡ���ids������ӵ�id�б���ȥ
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

			// ��ʼ����
			initTree();
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "����", Messagebox.OK,
					Messagebox.ERROR);
		}
	}

	/**
	 * Treeitem��չ����ʱ���ִ�д���
	 */
	class TreeitemOpenListener implements org.zkoss.zk.ui.event.EventListener {
		private BaseTreeitem treeitem = null;
		private EccTreeItem node = null;
		private GroupSelectTree tree;

		public TreeitemOpenListener(BaseTreeitem treeitem, GroupSelectTree tree)
				throws Exception {
			this.treeitem = treeitem;
			this.tree = tree;
			Object obj = treeitem.getValue();
			// �����нڵ���Ϣ
			if (obj instanceof EccTreeItem) {
				node = (EccTreeItem) obj;
			} else {
				throw new Exception("�ýڵ㲻����Ԥ���ĺϷ�������:" + obj != null ? obj
						.getClass().getName() : "NULL");
			}
		}

		@Override
		public void onEvent(Event event) throws Exception {
			try {
				if (node == null)
					return;

				// ���˲���Ҫ����Ľڵ�
				Treechildren mytreechildren = treeitem.getTreechildren();
				// ���ӽڵ�����Ǵ�������״̬�Ľڵ�
				if (mytreechildren == null
						|| mytreechildren.getChildren().size() > 0
						|| treeitem.isOpen() == false)
					return;
				// �Ǽ�����ڵ�����

				List<EccTreeItem> sons = node.getChildRen();
				// ���������
				if (sons != null && sons.size() > 0) {

					for (EccTreeItem son : sons) {
						if (son == null)
							continue;
						// ����Ǽ����� -- ҳ�ڵ�
						if (INode.GROUP.equals(son.getType())) {
							if (existChildren(son) && hasGroup(son)) {
								// ������ڶ���
								BaseTreeitem tii = getTreeitem(son);
								tii.setParent(mytreechildren);
								Treechildren newtreechildren = new Treechildren();
								tii.appendChild(newtreechildren);// ���Կ���չ��������
								tii
										.addEventListener(Events.ON_OPEN,
												new TreeitemOpenListener(tii,
														this.tree));// ���չ���¼�
							} else {
								BaseTreeitem tii = getTreeitem(son);
								tii.setParent(mytreechildren);
							}
						} else {// ����Group�����Ǵ����ӽڵ�
							continue;
						}

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show(e.getMessage(), "����", Messagebox.OK,
						Messagebox.ERROR);
			}
		}

	}

	public boolean hasGroup(EccTreeItem node) {
		boolean flag = false;
		List<EccTreeItem> sons = node.getChildRen();
		for (EccTreeItem son : sons) {
			if (son.getType().equals(INode.GROUP)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * ��Treeitem��ѡ���ϵ�ʱ��ִ��
	 */
	private class TreeitemCheckListener implements
			org.zkoss.zk.ui.event.EventListener {
		private BaseTreeitem treeitem = null;
		private EccTreeItem localnode = null;

		public TreeitemCheckListener(BaseTreeitem treeitem) throws Exception {
			this.treeitem = treeitem;

			Object obj = treeitem.getValue();
			// �����нڵ���Ϣ
			if (obj instanceof EccTreeItem) {
				localnode = (EccTreeItem) obj;
			} else {
				throw new Exception("�ýڵ㲻����Ԥ���ĺϷ�������:" + obj != null ? obj
						.getClass().getName() : "NULL");
			}
		}

		@Override
		public void onEvent(Event event) throws Exception {
			try {
				if (this.treeitem.isChecked()) {
					selectChildren(treeitem);
				} else {
					disselectChildren(treeitem);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show(e.getMessage(), "����", Messagebox.OK,
						Messagebox.ERROR);
			}
		}

		/**
		 * ���Ӽ�����ڵ�id���嵥��
		 */
		private void addGroupsToList(String name) {
			if (selectedIds.contains(name))
				return;
			selectedIds.add(name);
		}

		/**
		 * ���嵥�У�����ڵ�node.id
		 */
		private void removeGroupsFromList(String name) {
			selectedIds.remove(name);
		}


		/**
		 * ˢ�½ڵ��ѡ�� -- �ӽڵ���
		 */
		private void selectChildren(BaseTreeitem treeitem) throws Exception {
			INode node = ((EccTreeItem)treeitem.getValue()).getValue();
			if(node.getType().equals(INode.GROUP))
				addGroupsToList(node.getName());
			if (treeitem.getTreechildren() == null)
				return;
			for (Object item : treeitem.getTreechildren().getItems()) {
				if (item instanceof BaseTreeitem) {
					((BaseTreeitem) item).setChecked(true);
					selectChildren((BaseTreeitem) item);
				}
			}
		}

//		public void allLookdownChildren(BaseTreeitem treeitem) throws Exception {
//			BaseTreeitem itm = treeitem.getParentItem();
//			if (itm == null)
//				return;
//			addGroupsToList(((EccTreeItem) treeitem.getValue()).getTitle());
//			itm.setChecked(true);
//			allLookdownChildren(itm);
//		}
//
//		public void allLookdownParent(BaseTreeitem treeitem) throws Exception {
//			BaseTreeitem itm = treeitem.getParentItem();
//			if (itm == null)
//				return;
//			this.removeGroupsFromList(((EccTreeItem) treeitem.getValue())
//					.getTitle());
//			itm.setChecked(false);
//			allLookdownChildren(itm);
//		}

		/**
		 * ˢ�½ڵ��ѡ�� -- ���ڵ���
		 */
		private void disselectChildren(BaseTreeitem treeitem) throws Exception {
			INode node = ((EccTreeItem)treeitem.getValue()).getValue();
			if(node.getType().equals(INode.GROUP))
				removeGroupsFromList(node.getName());
			if (treeitem.getTreechildren() == null)
				return;
			for (Object item : treeitem.getTreechildren().getItems()) {
				if (item instanceof BaseTreeitem) {
					((BaseTreeitem) item).setChecked(false);
					disselectChildren((BaseTreeitem) item);
				}
			}
		}
	}

	/**
	 * ��ʼ�����ϵ�����
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
		// ȡ��view
		this.treemodel = EccTreeModel.getInstanceForAlertByViewName(
				getDesktop().getSession(), virtualName);
		this.treemodel.setDisplayMonitor(true);

		// �������Ľڵ�
		clearTree();

		Treechildren treechildren = this.getTreechildren();
		treechildren.setParent(this);

		EccTreeItem root = this.treemodel.getRoot();
		if (root == null)
			return;

		// ȱʡ��ͼ��ʱ��ȡ��һ����
		if (VirtualView.DefaultView.equals(virtualName) || virtualName == null) {
			for (EccTreeItem item : root.getChildRen()) {
				root = item;
				break;
			}
		}

		// ��ʾ��һ��ڵ�
		for (EccTreeItem item : root.getChildRen()) {
			try {
				BaseTreeitem ti = getTreeitem(item);
				ti.setParent(treechildren);
				if (existChildren(item)) {
					// �����ӽڵ㣬����ӿ�չ�����Ժ�չ���¼�
					Treechildren mytreechildren = new Treechildren();
					ti.appendChild(mytreechildren);
					ti.addEventListener(Events.ON_OPEN,
							new TreeitemOpenListener(ti, this));
					ti.addEventListener(Events.ON_CHECK, new TreeitemCheckListener(ti));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// չ���ڵ㣬�����ȱʡ��ͼ��չ����һ��
		if (VirtualView.DefaultView.equals(virtualName) || virtualName == null) {
			this.open(2);
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

	/**
	 * �������Ľڵ�
	 */
	private void clearTree() {
		this.clear();
		if (this.getTreechildren() == null) {
			new Treechildren().setParent(this);
		}
	}

	/**
	 * �ڵ�node�Ƿ���ڶ���
	 * 
	 * @param node
	 *            �ڵ�
	 * @return �ǡ���
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

	private List<String> getAllMonitors(EccTreeModel treemodel, EccTreeItem node) {
		List<String> retlist = BaseTools.getAllMonitors(this.getMonitorType(),
				treemodel, node);
		return retlist;
	}

	/**
	 * ͨ��node��ȡ�ó�ʼ�������Ե�Treeitem
	 * 
	 * @throws Exception
	 */
	private BaseTreeitem getTreeitem(EccTreeItem node) throws Exception {
		BaseTreeitem tii = this.isCheckable() ? new CheckableTreeitem()
				: new BaseTreeitem();
		this.setTreeitem(tii, node);
		tii.addEventListener(Events.ON_CHECK, new TreeitemCheckListener(tii));
		return tii;
	}

	/**
	 * ����treeitem�ĳ�ʼ������
	 * 
	 * @param tii
	 *            ����Ҫ��ʼ�����Ե�treeitem
	 * @param node
	 *            : ��nodeֵ
	 */
	private void setTreeitem(BaseTreeitem tii, EccTreeItem node) {
		tii.setLabel(node.toString());
		// tii.setId(node.getId());
		tii.setImage(getImage(node));
		tii.setOpen(false);
		tii.setChecked(this.existNode(node));
		tii.setValue(node);
	}

	// /**
	// * �嵥���Ƿ���ڸ�id
	// * @param id
	// * @return �ǡ���
	// */
	// public boolean existIdById(String id){
	// if (id == null) return false;
	// for (String idstr : this.selectedIds){
	// if (this.isChildId(id,idstr)) return true;
	// if (id.equals(idstr)) return true;
	// }
	// return false;
	// }
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
	 * �ж��Ƿ���parentid������
	 * 
	 * @param parentid
	 *            ����id
	 * @param id
	 *            : ����id
	 * @return �ǡ���
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
	 * ȡ��node�ı�ʾ��ͼ�������
	 * 
	 * @param node
	 * @return ͼ�������
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
	 * ȡ�����õ�ȫ��idֵ
	 * 
	 * @return List<String>
	 */
	public List<String> getSelectedIds() {
		return selectedIds;
	}

	/**
	 * ȡ�����õ�ȫ��idֵ
	 * 
	 * @return String ����"id1,id2,id3,"���ַ���
	 */
	public String getAllSelectedIds() {
		StringBuffer sb = new StringBuffer();
		for (String obj : getSelectedIds()) {
			if (sb.length() > 0)
				sb.append(";");
			sb.append(obj);
		}
		if (sb.length() == 0) {
			return null;
		}
		sb.append(";");
		return sb.toString();
	}
}
