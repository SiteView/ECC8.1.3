package com.siteview.ecc.monitorbrower;

import java.util.ArrayList;
import java.util.Collection;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Include;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import com.siteview.actions.EccActionManager;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.windows.ConstantValues;

public class EntityLinkFuntion implements EventListener
{
	private String 	entityId;
	private String  monitorId;
	private boolean globalFlag = false;
	
	private ArrayList<Treeitem> treeItemList = new ArrayList<Treeitem>();
	
	public EntityLinkFuntion(String entityId,String monitorId)
	{
		this.entityId = entityId;
		this.monitorId = monitorId;
	}
	
	@Override
	public void onEvent(Event event) throws Exception
	{
		try{
			Tab tab_monitor = (Tab) (event.getTarget().getDesktop().getPage("eccmain").getFellow("tab_monitor"));

			tab_monitor.setSelected(true);
			
			
			Object tmpobj = event.getTarget().getDesktop().getSession().getAttribute("CurrentWindow");
			if(tmpobj!=null)
			{
				event.getTarget().getDesktop().getSession().removeAttribute("CurrentWindow");
			}
			Include eccBody = (Include) (event.getTarget().getDesktop().getPage("eccmain").getFellow("eccBody"));
			Tree tree = (Tree) (event.getTarget().getDesktop().getPage("eccmain").getFellow("tree"));
			
			Collection children = tree.getTreechildren().getChildren();
			Treeitem root = null;
			for(Object obj : children)
			{
				if(obj instanceof Treeitem)
				{
					root = (Treeitem)obj;
					getTreeItem(root,entityId);
				}
			}
			if(this.globalFlag){
				for(Object obj : children)
				{
					if(obj instanceof Treeitem)
					{
						root = (Treeitem)obj;
						openTreeItem(root);
					}
				}
				String url = EccActionManager.getInstance().getUrl("entity");
				eccBody.setSrc(url);
			}else{
				try{
					Messagebox.show("获取该设备(编号为："+this.entityId+")数据出现异常,该设备可能已经被删除", "提示", Messagebox.OK,Messagebox.INFORMATION);
				}catch(Exception ee){}
			}
			
		}catch(Exception e){
			e.printStackTrace();
			try{
				Messagebox.show("获取该设备(编号为："+this.entityId+")数据出现异常,该设备可能已经被删除", "提示", Messagebox.OK,Messagebox.INFORMATION);
			}catch(Exception ee){}
		}
	}
	
	public Treeitem getTreeItem(Treeitem root,String entityId)
	{
		if(root==null) return null;
		boolean flag = root.isOpen();
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
					flag = true;
					globalFlag = true;
					item.setSelected(true);
					Session session = Executions.getCurrent().getDesktop().getSession();
					session.setAttribute(ConstantValues.LinkedMonitorId, monitorId);
					treeItemList.add(item);
					Treeitem parentTreeItem = item.getParentItem();
					while(parentTreeItem!=null){
						treeItemList.add(parentTreeItem);
						parentTreeItem = parentTreeItem.getParentItem();
					}
					return item;
				}else
				{
					getTreeItem(item,entityId);
				}
			}
		}
		root.setOpen(flag);
		return null;
	}
	
	public void openTreeItem(Treeitem root){
		
		if(root==null) return;
		Treechildren tChildren = root.getTreechildren();
		if(tChildren==null) return;
		Collection children = tChildren.getItems();
		Object[] objArr = children.toArray();
		if(children==null || children.size()<=0) return ;
		for(Object obj : objArr)
		{
			if(obj instanceof Treeitem)
			{
				Treeitem item = (Treeitem)obj;
				openTreeItem(item);
			}
		}
		for(Treeitem treeitem:treeItemList){
			if(treeitem.equals(root)){
				treeitem.setOpen(true);
			}
		}
		return ;
	}
}
