package com.siteview.actions;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import com.siteview.base.data.VirtualItem;
import com.siteview.base.data.VirtualView;
import com.siteview.base.tree.INode;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccTreeModel;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.treeview.controls.BaseTreeitem;
public class CheckAbledTree extends Tree{
	private final static Logger logger = Logger.getLogger(CheckAbledTree.class);

	EccTreeModel treeModel=null;
	EventListener checkItemListener=null;
	private List<CheckAbleTreeitem> list=new ArrayList<CheckAbleTreeitem>();
	
	public void onCreate()
	{
		treeModel=GrantTreeModel.getInstance(getDesktop().getSession(), VirtualView.DefaultView);
		refresh();
	}
	public void refresh()
	{
		for (EccTreeItem se : treeModel.getRoot().getChildRen()) 
			createTreeitem(getTreechildren(), se);
	}
	private EventListener getCheckItemListener()
	{
		if(checkItemListener==null)
		{
			checkItemListener=new EventListener(){

				@Override
				public void onEvent(Event event) throws Exception {
					BaseTreeitem treeitem=(BaseTreeitem)event.getTarget();
					EccTreeItem eccTreeItem =(EccTreeItem)event.getData();
					checkChildrenAuto(treeitem);
					if(treeitem.isChecked())
						checkParentAuto(treeitem);
				}
				
			};
		}
		return checkItemListener;
	}
	private void checkChildrenAuto(BaseTreeitem treeitem)
	{
		if(treeitem.getTreechildren()!=null)
		for(Object item:treeitem.getTreechildren().getItems()){
			((BaseTreeitem)item).setChecked(treeitem.isChecked());
		}
			
	}
	private void checkParentAuto(BaseTreeitem treeitem)
	{
		if(treeitem.getParentItem()==null)
			return;
		treeitem.getParentItem().setChecked(true);
		checkParentAuto(treeitem.getParentItem());
			
	}
	public void createTreeitem(Treechildren parent, EccTreeItem eccTreeItem) 
	{
		if(eccTreeItem.getType().equals(VirtualItem.SetMonitor.zulType))
			return;
		
		CheckAbleTreeitem item = new CheckAbleTreeitem();
		item.addEventListener("onCheck", getCheckItemListener());
		item.setValue(eccTreeItem);


		if(eccTreeItem.getType().equals(INode.GROUP)||eccTreeItem.getType().equals(INode.ENTITY)||eccTreeItem.getType().equals(INode.MONITOR))
			item.setSrc(EccWebAppInit.getInstance().getImage(eccTreeItem.getType(),eccTreeItem.getStatus()));
		else
			item.setSrc(EccWebAppInit.getInstance().getImage(eccTreeItem.getType()));
		item.setLabel(eccTreeItem.getTitle());
		
		item.setParent(parent);
		if(eccTreeItem.getType().equals(INode.ENTITY)){
			list.add(item);//ÐÂÔö
			return;
		}
		
		if(!eccTreeItem.getType().equals(INode.SE))
			expandTreeMode(item,eccTreeItem);
		else
		{
			createDropdownImage(item,eccTreeItem);
		}
		list.add(item);
	}
	private void expandTreeMode(Treeitem item,EccTreeItem eccTreeItem)
	{
		if(eccTreeItem.getType().equals(INode.GROUP)||eccTreeItem.getType().equals(INode.ENTITY)||eccTreeItem.getType().equals(INode.MONITOR))
		{
			item.setOpen(false);
		}
		if(eccTreeItem.getChildRen().size()>0)
		{
			Treechildren mytreechildren = new Treechildren();
			item.appendChild(mytreechildren);
			for (EccTreeItem treeItem : eccTreeItem.getChildRen()) {
					createTreeitem(mytreechildren, treeItem);
			}
		}
	}
	private void createDropdownImage(final CheckAbleTreeitem item,EccTreeItem eccTreeItem)
	{
		
//		final Image image=new Image("/main/images/ic_menu.gif");
//		image.setHover("/main/images/ic_menu_hover.gif");
//		image.setAttribute("eccTreeItem", item.getValue());
//		Treecell cell=(Treecell)item.getTreerow().getChildren().get(0);
//		cell.appendChild(new Space());
//		cell.appendChild(image);
		
//		image.addEventListener("onClick", new EventListener(){
//			@Override
//			public void onEvent(Event event) throws Exception {
//				EccTreeItem eccTreeItem=(EccTreeItem)event.getTarget().getAttribute("eccTreeItem");
				expandTreeMode(item,eccTreeItem);
//				afterExpandMonitorNode(item);
//				image.setVisible(false);
//			}});
		
	}
	public void afterExpandMonitorNode(CheckAbleTreeitem item)
	{
		logger.info("for overide by subclass!!");
	}
	
	public void getLeftAllCheck(boolean isChecked){
		for(CheckAbleTreeitem item:list){
			item.checkbox.setChecked(isChecked);	
		}
			
	}
}
