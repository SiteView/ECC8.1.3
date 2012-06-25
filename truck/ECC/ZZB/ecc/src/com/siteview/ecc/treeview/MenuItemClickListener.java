package com.siteview.ecc.treeview;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Treeitem;

import com.siteview.base.manage.View;
import com.siteview.base.template.EntityTemplate;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.EntityInfo;

public class MenuItemClickListener implements EventListener
{
	private Include	eccBody;
	private String	entityId;
	private String	tag;
	
	public MenuItemClickListener(Include eccBody, String tag)
	{
		this.eccBody = eccBody;
		this.tag = tag;
		
	}
	
	public MenuItemClickListener(Include eccBody, String entityId, String tag)
	{
		this.eccBody = eccBody;
		this.entityId = entityId;
		this.tag = tag;
		
	}
	
	public void onEvent(Event arg0) throws Exception
	{
		// TODO Auto-generated method stub
		if (tag.equals("addentity"))
		{
			eccBody.setSrc("/main/TreeView/EntityList.zul");
		}
		if (tag.equals("addmonitor"))
		{
			eccBody.setSrc("/main/TreeView/MonitorList.zul?entityid=" + this.entityId);
		}
		if (tag.equals("addgroup"))
		{
			eccBody.setSrc("/main/TreeView/WAddGroup.zul");
		}
		
	}
	
}
