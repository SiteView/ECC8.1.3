package com.siteview.ecc.report.common;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Tab;

public class GroupLinkListener implements EventListener {

	private String comboItemLabel;

	public GroupLinkListener(String comboItemLabel) {
		super();
		this.comboItemLabel = comboItemLabel;
	}


	@Override
	public void onEvent(Event event) throws Exception {
		Tab eccMainTabbox = (Tab)Executions.getCurrent().getDesktop().getPage(
				"eccmain").getFellow("tab_view");
		eccMainTabbox.setSelected(true);
		Events.sendEvent(new Event(Events.ON_SELECT, eccMainTabbox));
		Combobox virtualViewLayout = (Combobox) (Executions.getCurrent().getDesktop()
				.getPage("eccmain").getFellow("viewSelectEditting"));
		for (Object obj : virtualViewLayout.getItems()) {
			if ((obj instanceof Comboitem) == true) {
				Comboitem citem = (Comboitem) obj;
				if (citem.getLabel().equals(comboItemLabel)) {
					virtualViewLayout.setSelectedItem(citem);
					Events.postEvent(new Event(Events.ON_CHANGE, virtualViewLayout));
					break;
				}
			}
		}
		event.getTarget().getRoot().detach();
	}

}
