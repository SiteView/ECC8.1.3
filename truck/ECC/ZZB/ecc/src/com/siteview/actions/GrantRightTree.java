package com.siteview.actions;

import java.util.HashSet;
import java.util.Iterator;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import com.siteview.base.data.ZulItem;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccTreeModel;
import com.siteview.ecc.treeview.controls.BaseTreeitem;
public class GrantRightTree extends CheckAbledTree{
	private UserRight userRight=null;
	public UserRight getUserRight() {
		return userRight;
	}
	public void onCreate()
	{
		super.onCreate();
		refreshCheckByGrantRight();
		autoSelectFirst();
	}
	private void autoSelectFirst()
	{
		Treeitem selTreeitem=(Treeitem)getTreechildren().getFirstChild();
		if(selTreeitem==null)
			return;
		selTreeitem.setOpen(true);
		
		//modified by xiongqimin 2009-9-10
		Treechildren tch = selTreeitem.getTreechildren();
		if(tch==null)
		{
			return;
		}
		///////////////////////////////////////
		
		Iterator iterator=tch.getVisibleChildrenIterator();
		if(iterator.hasNext())
		{
			selTreeitem=(Treeitem)iterator.next();
		}
		
		
		super.selectItem(selTreeitem);
		
		
		HashSet selSet=new HashSet();
		selSet.add(selTreeitem);
		SelectEvent selectEvent=new SelectEvent("onSelect",this,selSet);
		Events.postEvent(selectEvent);
	}
	public void setUserid(String userid)
	{
		if(userid==null)
			return;
		
		userRight=new LoginUserRight(userid.toString());
		refreshCheckByGrantRight();
		autoSelectFirst();
	}
	public void afterExpandMonitorNode(CheckAbleTreeitem item)
	{
		checkOrNotByCanSee(item,true);
	}
	
	/*可见性设置*/
	private void refreshCheckByGrantRight()
	{
		for(Object treeitem:getTreechildren().getItems())
		{
			checkOrNotByCanSee((BaseTreeitem)treeitem,false);
		}
	}
	/*可见性设置*/
	private void checkOrNotByCanSee(BaseTreeitem treeItem,boolean applyToChildren)
	{
		EccTreeItem eccTreeItem=(EccTreeItem)treeItem.getValue();
		if(!userRight.canSeeTreeNode(eccTreeItem))
			treeItem.setChecked(false);
		else
		{
			treeItem.setChecked(true);
			setParentNodeDisplayBySon(treeItem);
		}

		if(applyToChildren)
		if(treeItem.getTreechildren()!=null)
		for(Object treeitem:treeItem.getTreechildren().getItems())
		{
			checkOrNotByCanSee((BaseTreeitem)treeitem,true);
		}
	}
	/*特殊逻辑,儿子可见,则父亲全部可见*/
	private void setParentNodeDisplayBySon(BaseTreeitem treeitem)
	{
		if(treeitem.getParentItem()!=null)
			if(treeitem.getParentItem().getValue() instanceof EccTreeItem)
				treeitem.getParentItem().setChecked(true);
	}

	public String getUserid()
	{
		if(userRight!=null)
			return userRight.getUserid();
		else
			return null;
	}

}
