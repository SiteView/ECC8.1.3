package com.siteview.ecc.treeview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.siteview.base.tree.INode;

public class EccTreeItem implements Serializable,Cloneable{
	public static final int STATUS_OK=1;
	public static final int STATUS_BAD=2;
	public static final int STATUS_ERROR=4;
	public static final int STATUS_WARNING=8;
	public static final int STATUS_DISABLED=16;
	public static final int STATUS_NULL=32;
	public static final int STATUS_ALL=STATUS_OK|STATUS_BAD|STATUS_ERROR|STATUS_WARNING|STATUS_DISABLED|STATUS_NULL;
	private String title;
	private String id;
	private String itemId;	
	private EccTreeItem parent;
	private String type;
	private boolean isMonitorTreeNode;
	
	private List<EccTreeItem> childRen = new ArrayList<EccTreeItem>();
	
	private INode value;
	private int status=11;
	
	public INode getValue() {
		return value;
	}

	public void setValue(INode value) {
		this.value = value;
	}
	
	public int getStatus() {
		return status;
	}
	/*
	 * changed return true
	 */
	public boolean refreshStatus() 
	{
		int _status=0;
		int oldStatus=status;
		for(EccTreeItem child:childRen)
			_status|=child.getStatus();

		if((_status&STATUS_NULL)==STATUS_NULL)
			status=STATUS_NULL;
		else if((_status&STATUS_BAD)==STATUS_BAD)
			status=STATUS_BAD;
		else if((_status&STATUS_ERROR)==STATUS_ERROR)
			status=STATUS_ERROR;
		else if((_status&STATUS_WARNING)==STATUS_WARNING)
			status=STATUS_WARNING;
		else if((_status&STATUS_OK)==STATUS_OK)
			status=STATUS_OK;
		else if((_status&STATUS_DISABLED)==STATUS_DISABLED)
			status=STATUS_DISABLED;
		return oldStatus!=status;
	}
	public void setStatus(int status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
		isMonitorTreeNode=(type.equals(INode.SE)||type.equals(INode.GROUP)||type.equals(INode.ENTITY)||type.equals(INode.MONITOR));
	}
	

	public List<EccTreeItem> getChildRen() 
	{
		return childRen;
	}

	public void setChildRen(List<EccTreeItem> childRen) {
		this.childRen = childRen;
	}

	public String toString() {
		return title;
	}

	public EccTreeItem(EccTreeItem parent, String id, String title,String type) {
		this.parent = parent;
		this.id = id;
		this.title = title;
		this.type = type;
		if(type.equals(INode.GROUP)||type.equals(INode.ENTITY)||type.equals(INode.SE))
			this.status=EccTreeItem.STATUS_OK;
		isMonitorTreeNode=(type.equals(INode.SE)||type.equals(INode.GROUP)||type.equals(INode.ENTITY)||type.equals(INode.MONITOR));
	}

	public EccTreeItem getParent() {
		return parent;
	}

	public void setParent(EccTreeItem parent) {
		this.parent = parent;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public void addChild(EccTreeItem treeItem)
	{
		getChildRen().add(treeItem);
	}
	public void deleteChild(EccTreeItem treeItem)
	{
		getChildRen().remove(treeItem);
	}
	public void updateChild(String id,EccTreeItem theItem)
	{
		for(int i=0;i<getChildRen().size();i++)
		{
			if(getChildRen().get(i).getId().equals(id))
			{
				//树变化时，只改它自己 （还有孙子呢）
				getChildRen().get(i).setTitle(theItem.getTitle());
//				getChildRen().remove(i);
//				getChildRen().add(i,theItem);
			}
		}
	}
	
	/*是否监测树节点*/
	public boolean isMonitorTreeNode()
	{
		return isMonitorTreeNode;
	}
	
	public Object cloneThis(){
		try {
			return this.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
