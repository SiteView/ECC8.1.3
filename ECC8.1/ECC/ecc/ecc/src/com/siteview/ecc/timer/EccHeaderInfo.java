package com.siteview.ecc.timer;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;
import com.siteview.base.queue.QueueManager;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.SeInfo;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.util.Toolkit;

public class EccHeaderInfo implements Serializable{
	private static final long serialVersionUID = 7372168893357664305L;
	Desktop desktop=null;
	private int all=0;
	private int warning=0;
	private int error=0;
	private int ok=0;
	private int online=0;
	private String loginName=null;
	
	private int nodeInfo_all = 0;
	private int nodeInfo_ok = 0;
	private int nodeInfo_warning = 0;
	private int nodeInfo_error = 0;
	private int nodeInfo_disabled = 0;
	private int nodeInfo_device = 0;

	public int getOnline() {
		return online;
	}

	public EccHeaderInfo(Desktop desktop)
	{
		super();
		this.desktop=desktop;
		refresh();
		this.online++;
	}
	
	public void refresh()
	{
		this.all=0;
		this.ok=0;
		this.warning=0;
		this.error=0;
		this.online=QueueManager.getInstance().getQueusCount();
		View view=Toolkit.getToolkit().getSvdbView(desktop.getSession());;
		if(view!=null)
		{
			this.loginName=view.getLoginName();
			INode[] nodes=view.getSe();
			if(nodes==null)
				return;
			for(INode iNode:nodes)
			{
				SeInfo info=view.getSeInfo(iNode);
				all+=info.get_sub_monitor_sum(desktop.getSession());
				ok+=info.get_sub_monitor_ok_sum(desktop.getSession());
				warning+=info.get_sub_monitor_warning_sum(desktop.getSession());
				error+=info.get_sub_monitor_error_sum(desktop.getSession());
			}
		}
	}
	
	/**
	 * 根据传入参数tree获得其下不同状态监测器的数目
	 * @param tree
	 */
	public void refreshByView(Tree tree){
		this.all=0;
		this.ok=0;
		this.warning=0;
		this.error=0;
		this.online=QueueManager.getInstance().getQueusCount();
		
		Iterator it = tree.getFirstChild().getChildren().iterator();
		while(it.hasNext()){
			Object obj = it.next();
			if(obj instanceof Treeitem){
				EccTreeItem eccItem = (EccTreeItem)((Treeitem) obj).getValue();
				getSubItem(eccItem);
			}
		}
	}
	
	private void getSubItem(EccTreeItem eccItem){
		if(eccItem == null){
			return;
		}
		if("monitor".equals(eccItem.getType())){
			all++;
			
			if(EccTreeItem.STATUS_OK == eccItem.getStatus()){
				ok++;
			}else if(EccTreeItem.STATUS_WARNING == eccItem.getStatus()){
				warning++;
			}else if(EccTreeItem.STATUS_ERROR == eccItem.getStatus() || EccTreeItem.STATUS_BAD == eccItem.getStatus() || EccTreeItem.STATUS_NULL == eccItem.getStatus()){
				error++;
			}
		}else{
			if(eccItem.getChildRen() != null && eccItem.getChildRen().size() > 0){
				List<EccTreeItem> list = eccItem.getChildRen();
				for(EccTreeItem _item : list){
					this.getSubItem(_item);
				}
			}
		}
	}
	
	/**
	 * 根据传入的eccTreeItem获得其下不同状态监测器的数目
	 * @param eti
	 * @return bean
	 */
	public NodeInfoBean refreshNodeInfo(EccTreeItem eccTreeItem){
		NodeInfoBean bean = new NodeInfoBean();
		nodeInfo_all = 0;
		nodeInfo_ok = 0;
		nodeInfo_warning = 0;
		nodeInfo_error = 0;
		nodeInfo_disabled = 0;
		nodeInfo_device = 0;

		getSubItemWithBean(eccTreeItem);
		bean.setAll(nodeInfo_all);
		bean.setOk(nodeInfo_ok);
		bean.setWarning(nodeInfo_warning);
		bean.setError(nodeInfo_error);
		bean.setDisabled(nodeInfo_disabled);
		bean.setDevice(nodeInfo_device);
		return bean;
	}
	
	/**
	 * 
	 * @param eccItem
	 * @param bean
	 * @return bean
	 */
	private void getSubItemWithBean(EccTreeItem eccItem){
		if(eccItem == null){
			return;
		}
		if("monitor".equals(eccItem.getType())){
			nodeInfo_all++;
			
			if(EccTreeItem.STATUS_OK == eccItem.getStatus()){
				nodeInfo_ok++;
			}else if(EccTreeItem.STATUS_WARNING == eccItem.getStatus()){
				nodeInfo_warning++;
			}else if(EccTreeItem.STATUS_ERROR == eccItem.getStatus() || EccTreeItem.STATUS_BAD == eccItem.getStatus() || EccTreeItem.STATUS_NULL == eccItem.getStatus()){
				nodeInfo_error++;
			}else if(EccTreeItem.STATUS_DISABLED == eccItem.getStatus()){
				nodeInfo_disabled++;
			}
		}else{
			if(eccItem.getChildRen() != null && eccItem.getChildRen().size() > 0){
				List<EccTreeItem> list = eccItem.getChildRen();
				for(EccTreeItem _item : list){
					if("entity".equals(_item.getType())){
						nodeInfo_device++;
					}
					this.getSubItemWithBean(_item);
				}
			}
		}
	}
	
	public String getLoginName()
	{
		return loginName;
	}
	public int getAll() {
		return all;
	}
	public void setAll(int all) {
		this.all = all;
	}
	public int getWarning() {
		return warning;
	}
	public void setWarning(int warning) {
		this.warning = warning;
	}

	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public int getOk() {
		return ok;
	}
	public void setOk(int ok) {
		this.ok = ok;
	}

	public int getNodeInfo_all() {
		return nodeInfo_all;
	}

	public void setNodeInfo_all(int nodeInfo_all) {
		this.nodeInfo_all = nodeInfo_all;
	}

	public int getNodeInfo_ok() {
		return nodeInfo_ok;
	}

	public void setNodeInfo_ok(int nodeInfo_ok) {
		this.nodeInfo_ok = nodeInfo_ok;
	}

	public int getNodeInfo_warning() {
		return nodeInfo_warning;
	}

	public void setNodeInfo_warning(int nodeInfo_warning) {
		this.nodeInfo_warning = nodeInfo_warning;
	}

	public int getNodeInfo_error() {
		return nodeInfo_error;
	}

	public void setNodeInfo_error(int nodeInfo_error) {
		this.nodeInfo_error = nodeInfo_error;
	}

	public int getNodeInfo_disabled() {
		return nodeInfo_disabled;
	}

	public void setNodeInfo_disabled(int nodeInfo_disabled) {
		this.nodeInfo_disabled = nodeInfo_disabled;
	}
}
