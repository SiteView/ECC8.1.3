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
			//��ʹ��ͼ����ͼʱ�Ҽ�����ڵ�ͼ�����ͼ���Ե�С��ͷʱ��������Ľڵ����session��
			Session session = Executions.getCurrent().getDesktop().getSession();
			session.setAttribute("selectedItem", com.getAttributes().get("eccTreeItem"));
			}catch(Exception e){}
		}

}
