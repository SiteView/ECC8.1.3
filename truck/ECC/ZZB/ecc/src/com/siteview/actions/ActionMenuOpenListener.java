package com.siteview.actions;

import java.io.Serializable;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import com.siteview.ecc.treeview.EccTreeItem;

public class ActionMenuOpenListener implements EventListener,Serializable {

		@Override
		public void onEvent(Event event) throws Exception {
			try{
			Component com=event.getTarget();
			ActionPopup popup=(ActionPopup)com.getDesktop().getPage("eccmain").getFellow("action_popup");
			//ActionPopup popup=(ActionPopup)com.getFellow("action_popup");
			popup.open(com);
			//在使用图标视图时右键点击节点图标或点击图标旁的小箭头时，将点击的节点放入session中
			Session session = Executions.getCurrent().getDesktop().getSession();
			session.setAttribute("selectedItem", com.getAttributes().get("eccTreeItem"));
			}catch(Exception e){}
		}

}
