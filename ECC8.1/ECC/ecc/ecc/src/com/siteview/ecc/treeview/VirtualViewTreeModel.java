package com.siteview.ecc.treeview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.event.TreeDataEvent;

import com.siteview.base.data.VirtualItem;
import com.siteview.base.data.VirtualView;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.queue.IQueueEvent;
import com.siteview.base.tree.IForkNode;
import com.siteview.base.tree.INode;
import com.siteview.ecc.timer.TimerListener;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svecc.zk.test.SVDBViewFactory;

public class VirtualViewTreeModel extends EccTreeModel  
{	
	public VirtualViewTreeModel(EccTreeItem root) 
	{
		super(root);
	}
	
	public static void removeInstance(Session session)
	{
		session.removeAttribute("virtualViewEccTreeModel_"+VirtualView.DefaultView);
	}
	public static void removeInstance(Session session,String rootid)
	{
		session.removeAttribute("virtualViewEccTreeModel_"+rootid);
	}
	public static EccTreeModel getInstance(Session session) 
	{
		return getInstance(session,VirtualView.DefaultView);
	}
	public static VirtualViewTreeModel getInstance(Session session,String rootid) 
	{
		VirtualViewTreeModel model=(VirtualViewTreeModel)session.getAttribute("virtualViewEccTreeModel_"+rootid);
		if(model==null)
		{
			model = new VirtualViewTreeModel(new EccTreeItem(null,rootid,"整体树",""));
//			session.setAttribute("virtualViewEccTreeModel_"+rootid, model);
		}
	
		return model;
	}
	
	public void findNodeWithSvid(ArrayList<EccTreeItem> nodes, EccTreeItem node, String nodeId)
	{
		if (nodes == null || node==null)
			return;
		
		if (node.getId().equals(nodeId))
			nodes.add(node);
		
		for (EccTreeItem item : node.getChildRen())
			findNodeWithSvid(nodes, item, nodeId);
	}
	
	public void findNodeWithSvid(ArrayList<EccTreeItem> nodes, String nodeId)
	{
		findNodeWithSvid(nodes, super.getRoot(),nodeId);
	}
	
	public void changeTree(ChangeDetailEvent changeEvent)
	{
		ArrayList<EccTreeItem> nodes = new ArrayList<EccTreeItem>();

		INode node = changeEvent.getData();
		EccLayoutComposer elc = (EccLayoutComposer)Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("main").getAttribute("Composer");
		elc.refreshData();

		EccTreeItem newRoot = (EccTreeItem)((Treeitem)elc.getTree().getFirstChild().getChildren().get(0)).getValue();
		if (changeEvent.getType() == ChangeDetailEvent.TYPE_DELETE)
		{
			findNodeWithSvid(nodes, newRoot, changeEvent.getSvid());
			for (EccTreeItem target : nodes)
			{
				if (target == null){
					continue;
				}
				EccTreeItem targetParent = target.getParent();
				int idx = targetParent.getChildRen().indexOf(target);
				targetParent.getChildRen().remove(target);
				updateStatus(targetParent, true);				
				if (node.getType().equals(INode.MONITOR))
				{
					if (this.isDisplayMonitor())
						fireEvent(targetParent, idx, idx, TreeDataEvent.INTERVAL_REMOVED);
				} else
					fireEvent(targetParent, idx, idx, TreeDataEvent.INTERVAL_REMOVED);
			}
		} else if (changeEvent.getType() == ChangeDetailEvent.TYPE_MODIFY)
		{
			findNodeWithSvid(nodes, newRoot, changeEvent.getSvid());
			for (EccTreeItem target : nodes)
			{
				if (target == null)
					continue;
				EccTreeItem targetParent = target.getParent();				
				target.setValue(changeEvent.getData());

				int status = Toolkit.getToolkit().changeStatusToInt(node.getStatus());
				if (!node.getName().equals(target.getTitle())) /* 标题变化 */
				{
					target.setTitle(node.getName());
					int idx = targetParent.getChildRen().indexOf(target);

					if (node.getType().equals(INode.MONITOR))
					{
						if (this.isDisplayMonitor())
							fireEvent(targetParent, idx, idx, TreeDataEvent.CONTENTS_CHANGED);
					} else
						fireEvent(targetParent, idx, idx, TreeDataEvent.CONTENTS_CHANGED);
				}

				if (target.getStatus() != status)/* 状态变化,重新计算父节点状态 */
				{
					target.setStatus(status);
					updateStatus(targetParent, true);
				}
				//刷新
				int idx = targetParent.getChildRen().indexOf(target);
				fireEvent(targetParent, idx, idx, TreeDataEvent.CONTENTS_CHANGED);
			}
		}else if(changeEvent.getType() == ChangeDetailEvent.TYPE_ADD){
			if("monitor".equals(node.getType())){
				EccTreeItem target = findNode(newRoot, node.getSvId());
				if(target!=null){
					return;
				}
				EccTreeItem targetParent = findNode(newRoot, node.getParentSvId());
				EccTreeItem newItem = new EccTreeItem(targetParent, node.getSvId(), node.getName(), node.getType());
				newItem.setValue(node);
				targetParent.addChild(newItem);

				int status = Toolkit.getToolkit().changeStatusToInt(node.getStatus());
				if (newItem.getStatus() != status)/* 状态变化,重新计算父节点状态 */
				{
					newItem.setStatus(status);
					updateStatus(targetParent, true);
				}
				//刷新
				int idx = targetParent.getChildRen().indexOf(newItem);
				fireEvent(targetParent, idx, idx, TreeDataEvent.INTERVAL_ADDED);
				
				if(targetParent == null){
					System.out.println("targetParent == null");
					return;
				}
				if(targetParent.getItemId() == null){
					System.out.println("targetParent'ItemId == null");
					return;
				}

				try {
					this.getVirtualView().addINode(targetParent.getItemId(), node, false);
					this.getVirtualView().saveChange();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if("entity".equals(node.getType())){
				EccTreeItem target = findNode(newRoot,node.getSvId());
				if(target!=null){
					return;
				}
				EccTreeItem targetParent = findNode(newRoot, node.getParentSvId());
				EccTreeItem newItem = new EccTreeItem(targetParent, node.getSvId(), node.getName(), node.getType());
				newItem.setValue(node);
				targetParent.addChild(newItem);

				int status = Toolkit.getToolkit().changeStatusToInt(node.getStatus());
				if (newItem.getStatus() != status)/* 状态变化,重新计算父节点状态 */
				{
					newItem.setStatus(status);
					updateStatus(targetParent, true);	
				}
				//刷新
				int idx = targetParent.getChildRen().indexOf(newItem);
				fireEvent(targetParent, idx, idx, TreeDataEvent.INTERVAL_ADDED);

				try {
					this.getVirtualView().addINode(targetParent.getItemId(), node, true);
					this.getVirtualView().saveChange();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if("group".equals(node.getType())){
				EccTreeItem target = findNode(newRoot, node.getSvId());
				if(target!=null){
					return;
				}
				EccTreeItem targetParent = findNode(newRoot, node.getParentSvId());
				EccTreeItem newItem = new EccTreeItem(targetParent, node.getSvId(), node.getName(), node.getType());
				newItem.setValue(node);
				targetParent.addChild(newItem);
				
				int status = Toolkit.getToolkit().changeStatusToInt(node.getStatus());
				if (newItem.getStatus() != status)/* 状态变化,重新计算父节点状态 */
				{
					//newItem.setStatus(status);	//新添加的组一定是OK状态
					updateStatus(targetParent, true);					
				}
				//刷新
				int idx = targetParent.getChildRen().indexOf(newItem);
				fireEvent(targetParent, idx, idx, TreeDataEvent.INTERVAL_ADDED);

				try {
					if(targetParent.getItemId() == null){
						return;
					}
					this.getVirtualView().addINode(targetParent.getItemId(), node, false);
					this.getVirtualView().saveChange();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
