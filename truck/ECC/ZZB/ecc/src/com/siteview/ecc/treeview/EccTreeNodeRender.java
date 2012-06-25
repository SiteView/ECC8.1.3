package com.siteview.ecc.treeview;

import java.awt.BorderLayout;
import java.util.LinkedHashMap;

import org.zkoss.web.servlet.dsp.action.Page;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Include;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;

import com.siteview.actions.ActionMenuOpenListener;
import com.siteview.actions.ActionPopup;
import com.siteview.base.data.VirtualItem;
import com.siteview.base.data.VirtualView;
import com.siteview.base.manage.View;
import com.siteview.base.template.EntityTemplate;
import com.siteview.base.template.TemplateManager;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.EntityInfo;
import com.siteview.base.treeInfo.GroupInfo;
import com.siteview.base.treeInfo.SeInfo;

public class EccTreeNodeRender implements TreeitemRenderer
{
	private View	view;
	@Override
	public void render(Treeitem item, Object data) throws Exception
	{
		EccTreeModel treeModel = (EccTreeModel) item.getTree().getModel();
		EccTreeItem node = (EccTreeItem) data;
		item.setLabel(node.toString());
		item.setValue(data);
		if(node.getType().equals(INode.GROUP)||node.getType().equals(INode.ENTITY)||node.getType().equals(INode.MONITOR))
			item.setImage(EccWebAppInit.getInstance().getImage(node.getType(),node.getStatus()));
		else
			item.setImage(EccWebAppInit.getInstance().getImage(node.getType()));
		ActionPopup popup=(ActionPopup)item.getDesktop().getPage("eccmain").getFellow("action_popup");
		popup.setAutoAppendBathMenu(true);
		item.getTreerow().setAttribute("eccTreeItem", node);
		item.setContext(popup);
		//item.getParent().addEventListener("onRightClick", this.getActionMenuCliclListener());
	}
	
	
}
