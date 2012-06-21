/**
 * 
 */
package com.siteview.ecc.treeview.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.event.TreeDataEvent;

import com.siteview.base.data.VirtualItem;
import com.siteview.base.data.VirtualView;
import com.siteview.base.manage.View;
import com.siteview.base.tree.IForkNode;
import com.siteview.base.tree.INode;

import com.siteview.ecc.util.Toolkit;

/**
 * ���ɼ򻯵���(�����������ͼ),��ӦEccTreeModel.java �����EccTreeModel.java��������,ȥ��geRoot()��ز���
 * 
 * @author : di.tang
 * @date: 2009-3-18
 * @company: siteview
 */
public class PredigestTreeModel extends AbstractTreeModel {
	private static final long serialVersionUID = -4250798414652355695L;
	PredigestTreeItem root = null;
	private View view = null;
	private Desktop _deskTop;

	public PredigestTreeModel(PredigestTreeItem root) {
		super(root);
		this.root = root;
	}

	public View getView() {
		if (view == null) {
			view = Toolkit.getToolkit().getSvdbView(getDesktop().getSession());
			//if (view != null)
			//	view.getChangeTree();
		}

		return view;

	}

	public static PredigestTreeModel getInstance(String rootid) {
		ArrayList<PredigestTreeItem> list = new ArrayList<PredigestTreeItem>();
		PredigestTreeModel testTree = new PredigestTreeModel(
				new PredigestTreeItem(null, rootid, "������", ""));
		// testTree.getDesktop().enableServerPush(true);
		// new Thread(testTree).start();
		return testTree;
	}

	public static String getVirtualItemId(String strId) {
		// ���ܵ���������ĿѰ���׽ڵ������⣬ ��û�漰�� ��ʱ�����ǡ�
		return "i" + strId;
	}

	public PredigestTreeItem getRoot() {
		if (root.getChildRen().size() == 0) {
			if (getView() == null)
				return null;

			/*
			 * INode[] ses = getView().getSe(); if (ses == null) return root;
			 */

			// 1����ͨ������ͼ�£� ����������ͼ + �������������ڵ㣬 Ĭ��������£� �������������ڵ�
			String defaultViewId = VirtualView.DefaultView;
			/*ArrayList<VirtualView> av = view.getAllVirtualView();
			for (VirtualView v : av) {
				if (v.getViewName().equals(root.getId())) {
					ArrayList<VirtualItem> vis = v.getTopItems();
					for (VirtualItem vi : vis) {
						if (vi.getType().equals(VirtualView.Item)) {

							// ��ȡ������ͼ�ڵ�ID
							if (vi.getItemDataZulType().equals(
									VirtualItem.WholeView.zulType))
								defaultViewId = getVirtualItemId(vi.getItemId());

							PredigestTreeItem child = new PredigestTreeItem(
									getVirtualItemId(vi.getParentItemId()),
									getVirtualItemId(vi.getItemId()), vi
											.getItemDataZulName(), vi
											.getItemDataZulType());
							root.addChild(child);

							buildSonVirtualItem(child, v, vi, "     ");
						}

						if (vi.getType().equals(VirtualView.INode)) {
							INode node = view.getNode(vi.getSvId());

							PredigestTreeItem child = new PredigestTreeItem(
									node.getParentSvId(), node.getSvId(), node
											.getName(), node.getType());
							child.setValue(node);
							root.addChild(child);

							buildSonVirtualItem(child, v, vi, "     ");
						}
					}
				}
			}*/

			// 2��Ĭ��������£� ����������ͼ��������ݣ�
			PredigestTreeItem defaultViewItem = findNode(defaultViewId);
			if (root.getId().equals(VirtualView.DefaultView)) {
				INode[] ses = view.getSe();

				if (ses == null)
					return null;

				for (INode se : ses) {
					INode node = getView().getNode(se.getSvId());

					constructNode(defaultViewItem, node);
				}
			}

		}
		return root;
	}

	public void buildSonVirtualItem(PredigestTreeItem parent, VirtualView vv,
			VirtualItem v, String head) {
		ArrayList<VirtualItem> vis = vv.getSonItems(v);
		if (vis.size() > 0) {
			for (VirtualItem vi : vis) {
				// �ڵ���������Ŀ
				if (vi.getType().equals(VirtualView.Item)) {
					PredigestTreeItem child = new PredigestTreeItem(
							getVirtualItemId(vi.getParentItemId()),
							getVirtualItemId(vi.getItemId()), vi
									.getItemDataZulName(), vi
									.getItemDataZulType());
					parent.addChild(child);

					buildSonVirtualItem(child, vv, vi, "     ");
				}

				// �ڵ��Ǽ�������ݵ�
				if (vi.getType().equals(VirtualView.INode)) {
					INode node = view.getNode(vi.getSvId());

					PredigestTreeItem child = new PredigestTreeItem(node
							.getParentSvId(), node.getSvId(), node.getName(),
							node.getType());
					child
							.setStatus(Toolkit.getToolkit().changeStatusToInt(node
									.getStatus()));
					child.setValue(node);
					parent.addChild(child);
					buildSonVirtualItem(child, vv, vi, "     ");
				}
			}
		}
	}

	int constructNode(PredigestTreeItem parent, INode node) {
		PredigestTreeItem ti = new PredigestTreeItem(parent.getId(), node
				.getSvId(), node.getName(), node.getType());
		ti.setValue(node);
		parent.addChild(ti);

		if (INode.MONITOR.equals(node.getType())) {
			ti.setStatus(Toolkit.getToolkit().changeStatusToInt(node.getStatus()));
			// if(ti.getStatus()!=PredigestTreeItem.STATUS_OK)/*״̬������*/
			return 1;
		}

		IForkNode f = (IForkNode) node;
		List<String> ids = f.getSonList();
		int count = 0;
		for (String id : ids) {
			// �ݹ鹹�����ж���
			INode n = view.getNode(id);
			if (n == null)
				continue;

			count += constructNode(ti, n);
		}

//		if (count > 0)
//			updateStatus(f.getSvId(), false);/* ���ҽ�����monitor��Ӳ����,���������ڵ��״̬���� */

		return 0;
	}

//	/*
//	 * ���¼��㸸�ڵ��״̬,���������treeItem��״̬��������
//	 */
//	private void updateStatus(PredigestTreeItem node, boolean fireEvent) {
//		// if (node.refreshStatus()) /* ״̬�ı� */
//		// {
//		if (fireEvent)
//			notifyStatusChange(node);
//		updateStatus(node.getParentid(), fireEvent); /* �ݹ���¸���״̬ */
//		// }
//	}
//
//	private void updateStatus(String nodeId, boolean fireEvent) {
//		PredigestTreeItem node = findNode(nodeId);
//
//		if (node == null || root.equals(node))
//			return;
//
//		updateStatus(node, fireEvent);
//	}
//
//	private void notifyStatusChange(PredigestTreeItem treeItem) {
//		PredigestTreeItem targetParent = findNode(treeItem.getParentid());
//		int idx = targetParent.getChildRen().indexOf(treeItem);
//		super.fireEvent(targetParent, idx, idx, TreeDataEvent.CONTENTS_CHANGED);
//
//	}

	@Override
	public Object getChild(Object parent, int childIdx) {
		if (((PredigestTreeItem) parent).getChildRen().size() == 0)
			return null;
		return ((PredigestTreeItem) parent).getChildRen().get(childIdx);
	}

	@Override
	public int getChildCount(Object parent) {
		if (parent == null)
			return 0;
		return ((PredigestTreeItem) parent).getChildRen().size();
	}

	@Override
	public boolean isLeaf(Object node) {
//		if (((PredigestTreeItem) node).getType().equals(INode.MONITOR))
//			return true;
		if (getChildCount(node) == 0)
			return true;
		else
			return false;
//		boolean allMonitor = true;
//		for (PredigestTreeItem item : ((PredigestTreeItem) node).getChildRen()) {
//			if (!item.getType().equals(INode.MONITOR)) {
//				allMonitor = false;
//				break;
//			}
//		}
//		return allMonitor;
	}

	

	private Desktop getDesktop() {
		if (_deskTop == null)
			_deskTop = Executions.getCurrent().getDesktop();
		return _deskTop;
	}

	private boolean isTop(String id) {
		return root.getId().equals(id);
	}

	/*
	 * �ݹ����ĳ�ڵ������ڵ�
	 * 
	 * @param parent�Ӹýڵ�(��)��ʼ����
	 * 
	 * @param nodeId����Ŀ��Ľڵ�ID
	 */
	public PredigestTreeItem findNode(PredigestTreeItem node, String nodeId) {
		// logger.info(node.getId() + node.getTitle());
		if (node.getId().equals(nodeId))
			return node;
		else {
			for (PredigestTreeItem item : node.getChildRen()) {
				PredigestTreeItem tmpItem = findNode(item, nodeId);
				if (tmpItem != null)
					return tmpItem;
			}
		}

		return null;
	}

	/*
	 * �ݹ����ȫ������ڵ�
	 * 
	 * @param nodeId����Ŀ��Ľڵ�ID
	 */
	public PredigestTreeItem findNode(String nodeId) {
		return findNode(root, nodeId);
	}

//	public void changeTree() {
//		List<String> ids = view.getChangeTree();
//		if (ids == null)
//			return;
//
//		HashMap statusChnagedNode = new HashMap();
//		for (String id : ids) {
//			INode node = view.getNode(id);
//			PredigestTreeItem target = findNode(id);
//			PredigestTreeItem targetParent = findNode(target.getParentid());
//			if (targetParent == null)/* ���ڵ�û��,���ܻ�ûչ���Ľڵ�仯��,���Ե����� */
//				continue;
//			if (node == null)/* ɾ�� */
//			{
//				targetParent.getChildRen().remove(target);
//				super.fireEvent(targetParent, 0, targetParent.getChildRen()
//						.size() - 1, TreeDataEvent.INTERVAL_REMOVED);
//				if (target.getType().equals(INode.MONITOR))
//					statusChnagedNode.put(targetParent.getId(), targetParent);
//			}
//			if (target == null)/* ��� */
//			{
//				PredigestTreeItem newItem = new PredigestTreeItem(node
//						.getParentSvId(), node.getSvId(), node.getName(), node
//						.getType());
//				targetParent.addChild(newItem);
//				super.fireEvent(targetParent, 0, targetParent.getChildRen()
//						.size() - 1, TreeDataEvent.INTERVAL_ADDED);
//				if (target.getType().equals(INode.MONITOR))
//					statusChnagedNode.put(targetParent.getId(), targetParent);
//			} else/* �޸� */
//			{
//				// targetParent.updateChild(id, new
//				// PredigestTreeItem(node.getParentSvId(),node.getSvId(),node.getName(),node.getType()));
//				int status = Toolkit.changeStatusToInt(node.getStatus());
//				if (!node.getName().equals(target.getTitle())) /* ����仯 */
//				{
//					target.setTitle(node.getName());
//					int idx = targetParent.getChildRen().indexOf(target);
//					fireEvent(targetParent, idx, idx,
//							TreeDataEvent.CONTENTS_CHANGED);
//				}
//				if (target.getType().equals(INode.MONITOR)) /* ��monitor��Ҫ����״̬ */
//				{
//					if (target.getStatus() != status)/* ״̬�仯,���¼��㸸�ڵ�״̬ */
//					{
//						target.setStatus(status);
//						statusChnagedNode.put(targetParent.getId(),
//								targetParent);
//					}
//				}
//				/* ������Ϣ�仯������ */
//			}
//		}
//		Set iterator = statusChnagedNode.keySet();
//		for (Object key : iterator) {
//			updateStatus((PredigestTreeItem) statusChnagedNode.get(key), true);
//		}
//
//	}

	public void getPathList(ArrayList<PredigestTreeItem> pathList,
			PredigestTreeItem curNode) {
		pathList.add(0, curNode);
		String parentid = curNode.getParentid();
		PredigestTreeItem parentNode = findNode(parentid);
		if (parentNode != null) {
			getPathList(pathList, parentNode);
		}
	}

}
