package com.siteview.ecc.treeview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import com.siteview.actions.EccActionManager;
import com.siteview.base.manage.View;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.controlpanel.ControlLayoutComposer;
import com.siteview.ecc.monitorbrower.MonitorBean;
import com.siteview.ecc.treeview.windows.ConstantValues;
import com.siteview.ecc.util.Toolkit;


public class RecentViewFindEccTreeItem {
	
	private String id;
	private EccTreeItem eccTreeItem = null;
	
	public RecentViewFindEccTreeItem(String monitorId){
		this.id = monitorId;
		findEccTreeitem();
	}
	
	
	public void findEccTreeitem(){
		try{
			String entityId = id.substring(0, id.lastIndexOf("."));
			if(entityId.contains(".") == false) return ;
			Tree tree = (org.zkoss.zul.Tree) Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("tree");
			EccTreeModel model = (EccTreeModel)tree.getModel();
			EccTreeItem root = model.getRoot();
			if (root == null)
				return ;
			
			//整体视图节点
			for (EccTreeItem item : root.getChildRen()){
				root = item;
				findMonitorId(item,entityId,id);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	
	public void findMonitorId(EccTreeItem item,String entityId,String monitorId) 
	{	
		if(item == null) return;
		if(this.eccTreeItem != null) return;
		for(EccTreeItem itm : item.getChildRen()){
			if(itm.getValue() == null) continue;
			if(INode.ENTITY.equals(itm.getValue().getType())){
				if(entityId.equals(itm.getId())){
					for(EccTreeItem sonEccItem :itm.getChildRen()){
						if(monitorId.equals(sonEccItem.getId())){
							this.eccTreeItem = sonEccItem;
							return;
						}
					}
				}
			}else{
				findMonitorId(itm, entityId, monitorId); 
			}
		}
	}
	
/*	public void findEccTreeitem(){
		Tree tree = (Tree) (Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("tree"));
		Collection children = tree.getTreechildren().getChildren();
		Treeitem root = null;
		String entityId = id.substring(0, id.lastIndexOf("."));
		for(Object obj : children){
			if(obj instanceof Treeitem){
				root = (Treeitem)obj;
				getTreeItem(root,entityId);
				if(this.eccTreeItem != null) break;
			}
		}
	
	}

	public Treeitem getTreeItem(Treeitem root,String entityId){
		if(root==null) return null;
		root.setOpen(true);
		
		Treechildren tChildren = root.getTreechildren();
		if(tChildren==null) return null;
		
		Collection children = tChildren.getItems();
		Object[] objArr = children.toArray();
		if(children==null || children.size()<=0) return null;
		
		
		for(Object obj : objArr)
		{
			if(obj instanceof Treeitem)
			{
				Treeitem item = (Treeitem)obj;
				EccTreeItem eccItem = (EccTreeItem)item.getValue();
				if(eccItem.getId().equals(entityId))
				{
					for(EccTreeItem sonEccTreeItem:eccItem.getChildRen()){
						if(id.equals(sonEccTreeItem.getId())){
							this.eccTreeItem = sonEccTreeItem;
							root.setOpen(true);
							return null;
						}
					}
				}else
				{
					getTreeItem(item,entityId);
				}
			}
		}
		root.setOpen(false);
		return null;
	}*/
	
	public EccTreeItem getEccTreeItem(){
		return this.eccTreeItem;
	}
}
