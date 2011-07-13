package com.siteview.ecc.util;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Row;

import com.siteview.base.tree.INode;
import com.siteview.ecc.controlpanel.IconCell;
import com.siteview.ecc.treeview.EccTreeItem;

public class PopupOpenListener implements EventListener {

	@Override
	public void onEvent(Event event) throws Exception 
	{
		TooltipPopup popup=(TooltipPopup)event.getTarget();
		Component obj=((OpenEvent)event).getReference();
		if(obj==null||!(obj instanceof Component))
			return;
		EccTreeItem value=(EccTreeItem)obj.getAttribute("eccTreeItem");
		popup.refresh(value);
	}
	Component findValidParent(Component child)
	{
		Component parent=child.getParent();
		if(parent==null)
			return null;
		if(parent instanceof IconCell||parent instanceof Row||parent instanceof Listitem)
			return parent;
		else
			return findValidParent(parent);
	}
}
