package com.siteview.actions;

import java.util.ArrayList;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Path;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Space;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.VirtualItem;
import com.siteview.base.tree.INode;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.controlpanel.ControlLayoutComposer;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.util.Toolkit;
import com.siteview.ecc.util.TooltipPopupBase;

public class ActionPopup extends TooltipPopupBase {

	private ActionPopupListener openListener = new ActionPopupListener();
	private boolean autoAppendBathMenu = false;

	public boolean isAutoAppendBathMenu() {
		return autoAppendBathMenu;
	}

	public void setAutoAppendBathMenu(boolean autoAppendBathMenu) {
		this.autoAppendBathMenu = autoAppendBathMenu;
	}

	public ActionPopup() {
	    
		super();
		super.setWidth("150px");
	}

	public void onCreate() {
		super.onCreate();
		addEventListener("onOpen", openListener);
	}


	public void refresh(EccTreeItem eccTreeitem) {

		while (getRows().getLastChild() != null)
			getRows().removeChild(getRows().getLastChild());

		String svid = eccTreeitem.getId();
		String type = eccTreeitem.getType();
		String actions[] = EccActionManager.getInstance().getActionList(type);
		if(svid!=null && type!=null && svid.startsWith("i") && type.equals(INode.GROUP))
			actions= new String[0];

		super.setImage(EccWebAppInit.getInstance().getImage(type,
				eccTreeitem.getStatus()));
		super.setTitle(eccTreeitem.getTitle());

		
		if(eccTreeitem.getType().equals(INode.MONITOR))
			addRow(eccTreeitem, "monitorDetail");
		
		StringBuffer addedActions = new StringBuffer();
		for (String action : actions) {
			if (action.equals("-") && getRows().getLastChild() != null)
			{
				addSplitLine();
				continue;
			}

			if (!getUserRight().havePopupMenuRight(svid, action))
				continue;
			addRow(eccTreeitem, action);

			addedActions.append(action).append(",");
		}
		appendDisableOrStartupMenu(eccTreeitem, addedActions.toString());
		if (autoAppendBathMenu)
			appendBathMenu(eccTreeitem, addedActions.toString());

		appendHelp(eccTreeitem);
		
		closeTooltip();
	}
	public void addSplitLine()
	{
		Component com=getRows().getLastChild();
		if(com!=null)
		if(com instanceof Row)
			((Row)com).setStyle("border-bottom:1px solid #AAAABB;");
	}
	private void appendHelp(EccTreeItem eccTreeitem)
	{
		if(eccTreeitem.getType().equals(INode.SE))
		{
			addSplitLine();
			Row row=addRow(eccTreeitem, "help");
			row.setAction("onClick:showHelp(9)");
		}
		else if(eccTreeitem.getType().equals(INode.GROUP))
		{
			addSplitLine();
			Row row=addRow(eccTreeitem, "help");
			row.setAction("onClick:showHelp(10)");
		}
		else if(eccTreeitem.getType().equals(INode.ENTITY))
		{
			addSplitLine();
			Row row=addRow(eccTreeitem, "help");
			row.setAction("onClick:showHelp(11)");
		}
		else if(eccTreeitem.getType().equals(INode.MONITOR))
		{
			addSplitLine();
			Row row=addRow(eccTreeitem, "help");
			row.setAction("onClick:showHelp(12)");
		}
	}
	/*仅admin具备批量操作功能*/
	private void appendBathMenu(EccTreeItem eccTreeitem, String addedActions) 
	{
		if(!getUserRight().isAdmin())
			return;
		
		IniFile ini_general = new IniFile("general.ini");
		try{
			ini_general.load();
		}catch(Exception e){}
		String ismaster_str = ini_general.getValue("version","ismaster");
		if(ismaster_str != null && "1".equals(ismaster_str)) return;// 大猫
		
		addSplitLine();
		if (eccTreeitem.getType().equals(INode.SE)) {
			addRow(eccTreeitem, "batchDel");
		} else if (eccTreeitem.getType().equals(INode.GROUP)) {
				addRow(eccTreeitem, "batchDel");
				addRow(eccTreeitem, "batchStartup");
				addRow(eccTreeitem, "batchDisable");
		} else if (eccTreeitem.getType().equals(INode.ENTITY)) {
				addRow(eccTreeitem, "batchDel");
				addRow(eccTreeitem, "batchRefresh");
				addRow(eccTreeitem, "batchStartup");
				addRow(eccTreeitem, "batchDisable");
		}
		
	}

	private void appendDisableOrStartupMenu(EccTreeItem eccTreeitem,
			String addedActions) {
		if (eccTreeitem.getType().equals(INode.GROUP)
				|| eccTreeitem.getType().equals(INode.ENTITY)||eccTreeitem.getType().equals(INode.MONITOR)) 
		{
			if (addedActions.indexOf("edit") != -1) 
			{
				addSplitLine();
				if(("group".equals(eccTreeitem.getType()) || "entity".equals(eccTreeitem.getType())) && 
						eccTreeitem.getChildRen()!=null && eccTreeitem.getChildRen().size() > 0 &&
						eccTreeitem.getChildRen().get(0).getStatus() == EccTreeItem.STATUS_DISABLED){
					addRow(eccTreeitem, "startup");
				}else{
					if (eccTreeitem.getStatus() == EccTreeItem.STATUS_DISABLED)
						addRow(eccTreeitem, "startup");

					if (eccTreeitem.getStatus() != EccTreeItem.STATUS_DISABLED)
							addRow(eccTreeitem, "disabled");
				}
			}
		}
	}

	private Row addRow(EccTreeItem eccTreeitem, String action) {
		String actionName = EccActionManager.getInstance()
				.getActionName(action);
		String actionImage = EccWebAppInit.getInstance().getActionImage(action);
		Row row = new Row();
		// row.setStyle("cursor:pointer;border:none");
		row.setParent(getRows());
		Div div=new Div();
		div.setParent(row);
		
		Image img = new Image(actionImage);
		div.appendChild(img);
		div.appendChild(new Space());
		img.setStyle("padding-left:10px");
		img.setAlign("absmiddle");
		Label lbl = new Label(actionName);
		div.appendChild(lbl);
		row.setZclass("ecc_menu_row");
		lbl.setZclass("ecc_menu_item");
		row.setAttribute("eccTreeItem", eccTreeitem);

		ActionClickListener actionClickListener = new ActionClickListener(this,
				action);

		row.addEventListener("onClick", actionClickListener);
		row.setAttribute("actionCode", action);
		row.setStyle("height:22px");

		if(getRows().getChildren().size()==1)
			lbl.setStyle("height:22px;font-weight:bold;color:#18599C");
		else
			lbl.setStyle("height:22px");
		
		return row;
	}

	private void closeTooltip() {
		try {
			//Page page = Executions.getCurrent().getDesktop().getPage("controlPage");
			//if (page != null) {
				Component com = Path.getComponent("//controlPage/nodeInfoTooltip");
				//Component com = page.getFellow("nodeInfoTooltip");
				if (com != null)
					((Popup) com).close();
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
