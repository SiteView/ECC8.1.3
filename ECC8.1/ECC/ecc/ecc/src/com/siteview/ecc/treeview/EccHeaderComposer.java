package com.siteview.ecc.treeview;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.impl.Attributes;
import org.zkoss.zk.ui.util.ComposerExt;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tree;

import com.siteview.base.data.VirtualView;
import com.siteview.base.queue.IQueueEvent;
import com.siteview.ecc.timer.EccHeaderInfo;
import com.siteview.ecc.timer.NodeInfoBean;
import com.siteview.ecc.timer.TimerListener;

public class EccHeaderComposer extends GenericForwardComposer implements
		ComposerExt, TimerListener 
	{
	private EccHeaderInfo eccheaderinfo = null;
	private Label loginName;
	private Label onlineInfo;
	private Label total_monitor;
	private Label total_ok;
	private Label total_warning;
	private Label total_error;
	private Tree tree_withView;
	
	@Override
	public void notifyChange(IQueueEvent event) {
		String viewName = (String)Executions.getCurrent().getDesktop().getSession().getAttribute("selectedViewName");
		if(viewName == null || VirtualView.DefaultView.equals(viewName)){
			eccheaderinfo.refresh();
		}else{
			refreshByView(tree_withView);
		}
		refreshHeaderInfo();
	}

	private void refreshHeaderInfo() {
		loginName.setValue(eccheaderinfo.getLoginName());
		onlineInfo.setValue(String.valueOf(eccheaderinfo.getOnline()));
		total_monitor.setValue(String.valueOf(eccheaderinfo.getAll()));
		total_ok.setValue(String.valueOf(eccheaderinfo.getOk()));
		total_warning.setValue(String.valueOf(eccheaderinfo.getWarning()));
		total_error.setValue(String.valueOf(eccheaderinfo.getError()));
		
	}
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		self.setAttribute("Composer", this);
		eccheaderinfo = new EccHeaderInfo(desktop);
		refreshHeaderInfo();
	}
	
	/**
	 * 应用虚拟视图后刷新最上方的监测器信息
	 */
	public void refreshByView(Tree tree){
		this.tree_withView = tree;
		eccheaderinfo.refreshByView(tree);
		refreshHeaderInfo();
	}
	
	/**
	 * 刷新NodeInfo
	 * @param eccTreeItem
	 * @return NodeInfoBean
	 */
	public NodeInfoBean refreshNodeInfo(EccTreeItem eccTreeItem){
		return eccheaderinfo.refreshNodeInfo(eccTreeItem);
	}
}
