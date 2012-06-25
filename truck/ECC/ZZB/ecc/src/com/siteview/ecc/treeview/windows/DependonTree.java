package com.siteview.ecc.treeview.windows;

import java.util.LinkedList;

import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Window;

import com.siteview.base.tree.INode;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccTreeModel;
import com.siteview.ecc.treeview.EccWebAppInit;

public class DependonTree extends GenericForwardComposer implements TreeitemRenderer
{
	@Override
	public void render(Treeitem item, Object data) throws Exception
	{
		EccTreeModel treeModel = (EccTreeModel) item.getTree().getModel();
		EccTreeItem node = (EccTreeItem) data;
		item.setLabel(node.toString());
		item.setValue(data);
		
		if (node.getType().equals(INode.GROUP) || node.getType().equals(INode.ENTITY) || node.getType().equals(INode.MONITOR))
			item.setImage(EccWebAppInit.getInstance().getImage(node.getType(), node.getStatus()));
		else
			item.setImage(EccWebAppInit.getInstance().getImage(node.getType()));
	}
	
	Button			btnok;
	Button			btclose;
	Button			btnno;
	Window			wdependon;
	Tree			dependtree;
	Panelchildren  pc;
	
	Treeitem		item;
	Textbox			tb;
	String			sbpath;
	EccTreeModel	treeModel;
	String			svid	= "";
	
	public DependonTree()
	{
	}
	
	public void onCreate$wdependon()
	{
		btnok.setDisabled(true);
		Tree tree=(Tree)wdependon.getDesktop().getPage("eccmain").getFellow("tree");
		int maxhint = 0;
		try
		{
			int maxh = (Integer) tree.getAttribute("desktopHeight");
			maxhint = maxh - 200;
			if (maxhint>375)
			{
				maxhint=375;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (maxhint == 0)
		{
			pc.setStyle("margin:5px 5px 5px 5px;overflow-y:auto;max-height:375px;");
		} else
		{
			pc.setStyle("margin:5px 5px 5px 5px;overflow-y:auto;max-height:" + maxhint + "px;");
		}
		tb = (Textbox) wdependon.getAttribute("tb");
		treeModel = EccTreeModel.getMonitorTreeInstance(session);
		treeModel.setDisplayMonitor(true);
		dependtree.setModel(treeModel);
		dependtree.setTreeitemRenderer(this);
		if (dependtree.getTreechildren() != null && dependtree.getTreechildren().getVisibleChildrenIterator().hasNext())
		{
			Treeitem topNode = (Treeitem) dependtree.getTreechildren().getVisibleChildrenIterator().next();
			dependtree.selectItem(topNode);
			topNode.setOpen(true);
		}
		wdependon.setPosition("center");
	}
	
	public void onSelect$dependtree()
	{
		item = dependtree.getSelectedItem();
		EccTreeItem treeitem = (EccTreeItem) item.getValue();
		if (treeitem.getType().equals(INode.MONITOR))
		{
			btnok.setDisabled(false);
			svid = treeitem.getId();
			
			LinkedList<String> sblist = new LinkedList<String>();
			StringBuilder sb = new StringBuilder();
			sblist.addFirst(item.getLabel());
			while (item.getParentItem() != null)
			{
				item = item.getParentItem();
				sblist.addFirst(item.getLabel());
			}
			int ic = sblist.size() - 1;
			int i = 0;
			for (String key : sblist)
			{
				if (i < ic)
				{
					sb.append(key + ":");
				} else
				{
					sb.append(key);
				}
				i++;
			}
			
			sbpath = sb.toString();
		} else
		{
			btnok.setDisabled(true);
		}
	}
	
	public void onClick$btnno()
	{
		tb.setValue("");
		tb.setAttribute("value", "");
		wdependon.detach();
	}
	
	public void onClick$btnok()
	{
		tb.setValue(sbpath);
		tb.setAttribute("value", svid);
		wdependon.detach();
	}
	
}
