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
	private boolean havecheckmark = true;//界面上是否需要多选框,默认为需要,如果不需要用多选框可使用第二构函数加以控
	PredigestTreeEventListener a = null;// 多选框监听事件
	Tree tree2;
	Treeitem bb;

	public RightClickMenuHandler getRightClickMenuHandler() {
		if (rightClickMenuHandler == null)
			rightClickMenuHandler = new RightClickMenuHandler();
		return rightClickMenuHandler;
	}

	/**
	 *默认的构造函数,不加多选框
	 */
	public PredigestTreeNodeRender() {

	}

	/**
	 * 此构造函数可控制是否加入复选框
	 * 
	 * @param thb为false表示不加复选框
	 *            ,为true表示加入复选框
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

		// 暂时不开启
		// item.getTreerow().addEventListener("onRightClick",
		// getRightClickMenuHandler());
		// 20090324 modify by di.tang
		if (this.havecheckmark) {
			// if (false) {
			// 增加多选框事件;
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