/**
 * 
 */
package com.siteview.ecc.treeview.report;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Iframe;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;

import com.siteview.base.tree.INode;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.treeview.RightClickMenuHandler;
import com.siteview.ecc.treeview.controls.BaseTreeitem;

/**
 * @author : di.tang
 * @date: 2009-3-18
 * @company: siteview
 */
public class PredigestTreeNodeRender implements TreeitemRenderer {

	RightClickMenuHandler rightClickMenuHandler = null;
	private boolean havecheckmark = true;//�������Ƿ���Ҫ��ѡ��,Ĭ��Ϊ��Ҫ,�������Ҫ�ö�ѡ���ʹ�õڶ����������Կ�
	PredigestTreeEventListener a = null;// ��ѡ������¼�
	Tree tree2;
	Treeitem bb;

	public RightClickMenuHandler getRightClickMenuHandler() {
		if (rightClickMenuHandler == null)
			rightClickMenuHandler = new RightClickMenuHandler();
		return rightClickMenuHandler;
	}

	/**
	 *Ĭ�ϵĹ��캯��,���Ӷ�ѡ��
	 */
	public PredigestTreeNodeRender() {

	}

	/**
	 * �˹��캯���ɿ����Ƿ���븴ѡ��
	 * 
	 * @param thbΪfalse��ʾ���Ӹ�ѡ��
	 *            ,Ϊtrue��ʾ���븴ѡ��
	 */
	public PredigestTreeNodeRender(boolean thb) {
		this.havecheckmark = thb;
	}

	@Override
	public void render(Treeitem item, Object data) throws Exception {
		// PredigestTreeModel treeModel = (PredigestTreeModel) item.getTree()
		// .getModel();
		PredigestTreeItem node = (PredigestTreeItem) data;

		item.setLabel(node.toString());
		item.setValue(data);
		if (node.getType().equals(INode.GROUP)
				|| node.getType().equals(INode.ENTITY)
				|| node.getType().equals(INode.MONITOR))
			// item.setImage(EccWebAppInit.getImage(node.getType(), node
			// .getStatus()));
			item.setImage(EccWebAppInit.getInstance().getImage(node.getType(), "ok"));

		else
			item.setImage(EccWebAppInit.getInstance().getImage(node.getType()));
		/*
		 * INode inode=treeModel.getView().getNode(node.getId());
		 * if(inode!=null) {
		 * item.setImage(EccWebAppInit.getImage(node.getType(),
		 * inode.getStatus())); } else {
		 * item.setImage(EccWebAppInit.getImage(node.getType())); }
		 */

		// ��ʱ������
		// item.getTreerow().addEventListener("onRightClick",
		// getRightClickMenuHandler());
		// 20090324 modify by di.tang
		if (this.havecheckmark) {
			// if (false) {
			// ���Ӷ�ѡ���¼�;
			a = new PredigestTreeEventListener();
			a.setChildrenSameSelf((BaseTreeitem)item.getParentItem());
			item.getTreerow().addEventListener("onClick", a);

		}

	}

	public void refleshiframe(Iframe PeriodofQueryReportIframe, String nodeid,
			Datebox starttime, Datebox endtime,String comparetype) {
		String url = "/main/report/periodoftimereportiframesrc.zul?starttime="
				+ starttime.getValue() + "&endtime=" + endtime.getValue()
				+ "&nodeid=" + nodeid+"&comparetype="+comparetype;
		Executions.getCurrent().getDesktop().getSession().setAttribute("myREPORTNODEID", nodeid);
		PeriodofQueryReportIframe.setVisible(true);
		PeriodofQueryReportIframe.setSrc(url);
	}
}