package com.siteview.ecc.controlpanel;

import java.util.ArrayList;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import com.siteview.actions.EccActionManager;
import com.siteview.base.tree.INode;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccTreeModel;
import com.siteview.ecc.treeview.EccWebAppInit;

public class ListEventDelegate extends GenericForwardComposer
{
	public void onCreate()
	{
		
		final Button btnHiddenInherit=(Button)desktop.getPage("controllPage").getFellow("btnHiddenInherit");
		final Button btnDisplayInherit=(Button)desktop.getPage("controllPage").getFellow("btnDisplayInherit");
		final EccListModel model=(EccListModel)((Listbox)self).getModel();
		btnDisplayInherit.addEventListener("onClick", new EventListener()
		{
			public void onEvent(Event event) throws Exception 
			{
				model.setInherit(true);
			}
		});
		btnHiddenInherit.addEventListener("onClick", new EventListener(){
			public void onEvent(Event event) throws Exception 
			{
				model.setInherit(false);
			}
		});
	}

	public void onSelect(SelectEvent event)
	{
		Listbox list=(Listbox)event.getTarget();
		Listitem listitem=list.getSelectedItem();
		INode iinfo=(INode)listitem.getValue();
		Tree tree=(Tree)desktop.getPage("eccmain").getFellow("tree");
		EccTreeModel eccTreeModle=(EccTreeModel)tree.getModel();
		
		EccTreeItem curNode=eccTreeModle.findNode(iinfo.getSvId());
		ArrayList<EccTreeItem> pathList=new ArrayList<EccTreeItem>();
		eccTreeModle.getPathList(pathList,curNode);

		Treechildren treechildren=tree.getTreechildren();/*从树的根开始处理*/
		Treeitem treeitem=null;
		EccTreeItem eccTreeitem=null;
		for(EccTreeItem needOpen:pathList) /*所有需要展开的节点在pathList中*/
		{
			for(Object obj:treechildren.getItems())
			{	
				treeitem=(Treeitem)obj;
				eccTreeitem=(EccTreeItem)treeitem.getValue();
				if(needOpen.getId().equals(eccTreeitem.getId()))
				{
					//Events.postEvent("onOpen", treeitem,null);
					treeitem.setOpen(true);
					treechildren=treeitem.getTreechildren();
					break;
				}
			}
			
			
		}
		
		if(treeitem!=null&&eccTreeitem!=null)
		{
			//Events.postEvent("onSelect", treeitem,null);
			tree.selectItem(treeitem);
			Include eccBody=(Include)desktop.getPage("eccmain").getFellow("eccBody");
			eccBody.setSrc(new StringBuffer(EccActionManager.getInstance().getUrl(eccTreeitem.getType())).append("?type=").append(eccTreeitem.getType()).append("&id=").append(eccTreeitem.getId()).toString());

		}
		/*if(iinfo!=null&&tree.getSelectedItem()!=null)
		{
			tree.getSelectedItem().setOpen(true);
			for(Object obj:tree.getSelectedItem().getTreechildren().getItems())
			{
				EccTreeItem eccTreeitem=(EccTreeItem)((Treeitem)obj).getValue();
				
				if(iinfo.getSvId().equals(eccTreeitem.getId()))
				{
					
					//Events.postEvent("onSelect", (Treeitem)obj,null);
					//tree.selectItem((Treeitem)obj);
					//((Treeitem)obj).setOpen(true);
					
				}
			}
		}*/


	}
}
