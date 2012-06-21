package com.siteview.actions;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.OpenEvent;

import com.siteview.ecc.treeview.EccTreeItem;

public class ActionPopupListener implements EventListener,java.io.Serializable{

	@Override
	public void onEvent(Event event) throws Exception 
	{
		ActionPopup popup=(ActionPopup)event.getTarget();
		Component obj=((OpenEvent)event).getReference();
		if(obj==null)
			return;
		Object value=obj.getAttribute("eccTreeItem");
		
		if(value!=null)
		{
			if(value instanceof EccTreeItem)
				popup.refresh(((EccTreeItem)value));
		}
	}
}
