package com.siteview.ecc.treeview;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.siteview.actions.ActionMenuOpenListener;
import com.siteview.actions.ActionPopup;
import com.siteview.base.tree.INode;
import com.siteview.ecc.controlpanel.ControlLayoutComposer;
import com.siteview.ecc.util.Toolkit;
import com.siteview.ecc.util.TooltipPopup;

public class EccOpenedTreeView extends Tree {
	private boolean hasItem;
	private int filter = EccTreeItem.STATUS_ALL;
	private EccTreeItem selTreeItem;
	private EccTreeModel treeModel=null;
	EventListener selectListener;
	private boolean displayDetail=false;
	private ActionMenuOpenListener actionMenuOpenListener=new ActionMenuOpenListener();
	private int maxLevel = 0;
	private int level = 0;
	private int maxCellNum = 0;
	
	public void setDisplayDetail(boolean displayDetail) {
			this.displayDetail = displayDetail;
	}
	public void setSelTreeItem(EccTreeItem selTreeItem) {
		this.selTreeItem = selTreeItem;
	}
	public void setFilter(int filter) {
		this.filter = filter;
	}

	public void setSelectListener(EventListener selectListener) {
		this.selectListener = selectListener;
	}

	public int getStatus() {
		return filter;
	}
	public void refreshDisplayDetail(boolean displayDetail) {

		if (this.displayDetail !=displayDetail) 
		{
			this.displayDetail = displayDetail;
			clear();
			refresh();
		}
}
	public void refreshStatus(int status) {

			if (this.filter !=status) 
			{
				this.filter = status;
				clear();
				refresh();
			}
	}

	public EccOpenedTreeView() {
		super();
	}
	public void onCreate()
	{
		initTree();
	}
	public void initTree() 
	{
		this.treeModel=(EccTreeModel)((Tree)getDesktop().getPage("eccmain").getFellow("tree")).getModel();
		refresh();
		//根据节点数等参数生成innerWidth
		this.setInnerWidth(40 + level * 30 + 200 + maxCellNum * 25 + "");
		//this.addEventListener("onSelect",this.getSelectionEventListener());
	}
	public void refresh()
	{		
		this.hasItem=false;
		if(selTreeItem!=null)
		{
			createTreeitem(getTreechildren(), selTreeItem);
		}else
		{
			for (EccTreeItem se : treeModel.getRoot().getChildRen().get(0).getChildRen()) {
				createTreeitem(getTreechildren(), se);
			}
		}
		if(level > maxLevel){
			maxLevel = level;
		}
	}
	public boolean createTreeitem(Treechildren parent, EccTreeItem eccTreeItem) 
	{
		if(this.hasItem)
			if(!displayDetail&&eccTreeItem.getChildRen().size()<=0)
				return false;
		
		Treeitem item = new Treeitem();
		item.setLabel("");
		item.setValue(eccTreeItem);
		
		Treerow row=item.getTreerow();
		
		Treecell cell= null;
		if (row.getFirstChild() instanceof Treecell){
			cell = (Treecell)row.getFirstChild();
		}else{
			cell=new Treecell();
		}
		
		row.insertBefore(cell,row.getFirstChild());
		Image image=new Image(EccWebAppInit.getInstance()
				.getImage(eccTreeItem.getType(),eccTreeItem.getStatus()));
		image.setTooltip((TooltipPopup)Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("nodeInfoTooltip"));
		image.setAttribute("eccTreeItem", eccTreeItem);
		image.setAlign("absmiddle");
		
		cell.appendChild(image);
		cell.appendChild(new Label(eccTreeItem.getTitle()));
		
		ActionPopup popup=(ActionPopup)getDesktop().getPage("eccmain").getFellow("action_popup");
		item.getTreerow().setAttribute("eccTreeItem", eccTreeItem);
		item.setContext(popup);

		item.getTreerow().addEventListener("onDoubleClick",this.getSelectionEventListener());
		item.getTreerow().addEventListener("onClick",this.getShowEventListener());
		
		boolean allMonitor= true;
		level = 0;
		int cellNum = 0;
		for(EccTreeItem sonitem: eccTreeItem.getChildRen())
		{
			if(!sonitem.getType().equals(INode.MONITOR))
			{
				allMonitor= false;
				break;
			}
		}

		cellNum = 0;
		for(EccTreeItem sonitem: eccTreeItem.getChildRen())
		{
			if(sonitem.getType().equals(INode.MONITOR))
			{
				cellNum++;
			}
		}
		if(cellNum > maxCellNum){
			maxCellNum = cellNum;
		}
		
		if (allMonitor || INode.ENTITY.equals(eccTreeItem.getType()))
		{
			if(creMonitorImage(item, eccTreeItem)||displayDetail||!this.hasItem)
			{
				item.setParent(parent);
				this.hasItem=true;
				level++;
				return true;
			}
		}
		else
		{
			if(displayDetail||eccTreeItem.getChildRen().size()>0)
			{
				Treechildren mytreechildren = new Treechildren();
				item.appendChild(mytreechildren);
				boolean addedChild=false;
				for (EccTreeItem treeItem : eccTreeItem.getChildRen()) {
					if(createTreeitem(mytreechildren, treeItem)){
						addedChild=true;
					}
				}
				if(displayDetail||addedChild||!this.hasItem)
				{
					item.setParent(parent);
					this.hasItem=true;
					level++;
					return true;
				}
			}

		}
		return false;
	}

	private boolean creMonitorImage(Treeitem entityItem, EccTreeItem eccTreeItem) 
	{
		boolean addedCell=false;
		for (EccTreeItem monitor: eccTreeItem.getChildRen())
		{
			if ((filter&monitor.getStatus())==monitor.getStatus())
			{
				addCell(entityItem, monitor);
				addedCell=true;
			}
		}
		return addedCell;
	}

	private void addCell(Treeitem entityItem, EccTreeItem monitor) 
	{
		Component parent=(Component)entityItem.getTreerow().getChildren().get(0);
		//new Image("/main/images/none.png").setParent(parent);/*增加间隔*/
		Separator separator=new Separator("vertical");
		separator.setWidth("5px");
		separator.setParent(parent);
		Image image=new Image();
		image.setAlign("absmiddle");
		image.setSrc(EccWebAppInit.getInstance().getStatusImage(Toolkit.getToolkit().changeStatusToString(monitor.getStatus())));
		image.setParent(parent);
		image.setHover(Toolkit.getToolkit().getHoverImage(image.getSrc()));
		image.setTooltip((TooltipPopup)Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("nodeInfoTooltip"));
		image.setAttribute("INode", monitor.getValue());
		image.setAttribute("eccTreeItem", monitor);
		image.addEventListener("onRightClick", actionMenuOpenListener);
		image.addEventListener("onClick", new EventListener(){

			@Override
			public void onEvent(Event event) throws Exception {
				EccTreeItem monitor=(EccTreeItem)event.getTarget().getAttribute("eccTreeItem");
				
				if (event.getTarget().getDesktop().hasPage("controlPage")) {
					Page page = event.getTarget().getDesktop().getPage("controlPage");
					if (page.hasFellow("controlLayout")) {

						ControlLayoutComposer clc = ((ControlLayoutComposer) page
								.getFellow("controlLayout").getAttribute(
										"Composer"));

						clc.refreshSouth(monitor,false);
					}
				}
			}});
	}
	public EventListener getSelectionEventListener() 
	{
		if (selectListener == null)
		{
			
			selectListener = new EventListener() 
			{
				public void onEvent(Event event) 
				{
					//EccOpenedTreeView theTree=(EccOpenedTreeView)event.getTarget();
					//EccTreeItem treeItem=(EccTreeItem)theTree.getSelectedItem().getValue();
					EccTreeItem treeItem=(EccTreeItem)event.getTarget().getAttribute("eccTreeItem");
					INode iinfo =treeItem.getValue();
					Toolkit.getToolkit().expandTreeAndShowList(event.getTarget().getDesktop(),iinfo);
				}
			};
		}
		return selectListener;
	}
	
	public EventListener getShowEventListener() 
	{
		if (selectListener == null)
		{
			
			selectListener = new EventListener() 
			{
				public void onEvent(Event event) 
				{
					EccTreeItem monitor=(EccTreeItem)event.getTarget().getAttribute("eccTreeItem");
					
					if (event.getTarget().getDesktop().hasPage("controlPage")) {
						Page page = event.getTarget().getDesktop().getPage("controlPage");
						if (page.hasFellow("controlLayout")) {

							ControlLayoutComposer clc = ((ControlLayoutComposer) page
									.getFellow("controlLayout").getAttribute(
											"Composer"));
							clc.getActionMenuDiv().onEvent(event);
							clc.refreshSouth(monitor,false);
						}
					}
				}
			};
		}
		return selectListener;
	}
}
