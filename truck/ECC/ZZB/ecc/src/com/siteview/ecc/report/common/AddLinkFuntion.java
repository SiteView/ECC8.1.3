package com.siteview.ecc.report.common;

import java.util.ArrayList;
import java.util.Collection;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Include;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import com.siteview.actions.EccActionManager;
import com.siteview.ecc.treeview.EccTreeItem;

public class AddLinkFuntion implements EventListener
{
	
	private String 	targetUrl;
	
	public AddLinkFuntion(UrlPropertiesType targetUrl, String subMenuId) {
		super();
		this.targetUrl = targetUrl.toString();
		this.subMenuId = subMenuId;
	}

	private String 	subMenuId=null;//对应页面中的子菜单
	
	private ArrayList<Treeitem> treeItemList = new ArrayList<Treeitem>();
//	
	public AddLinkFuntion(UrlPropertiesType targetUrl)
	{
		this.targetUrl = targetUrl.toString();
	}
	
	@Override
	public void onEvent(Event event) throws Exception
	{
		try{
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
					getTreeItem(root,targetUrl);
				}
			}
			for(Object obj : children)
			{
				if(obj instanceof Treeitem)
				{
					root = (Treeitem)obj;
					openTreeItem(root);
				}
			}
			
			String url = EccActionManager.getInstance().getUrl(targetUrl);
			if(this.subMenuId!=null&&!this.subMenuId.equals("")){
				url = url+"?subMenuId="+this.subMenuId;
			}
			eccBody.setSrc(url);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public Treeitem getTreeItem(Treeitem root,String type)
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
				if(eccItem.getType().equals(type))
				{
					flag = true;
					item.setSelected(true);
					treeItemList.add(item);
					Treeitem parentTreeItem = item.getParentItem();
					while(parentTreeItem!=null){
						treeItemList.add(parentTreeItem);
						parentTreeItem = parentTreeItem.getParentItem();
					}
					return item;
				}else
				{
					getTreeItem(item,type);
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
