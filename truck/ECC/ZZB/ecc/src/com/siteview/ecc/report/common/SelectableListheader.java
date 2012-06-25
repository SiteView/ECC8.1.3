package com.siteview.ecc.report.common;

import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;

public class SelectableListheader {
	public static void addPopupmenu(Listbox listbox){
		Menupopup popup =new Menupopup();
		Listhead head = listbox.getListhead();
		List<?> list = head.getChildren();
		for(Object obj : list){
			if((obj instanceof Listheader) == true){
				final Listheader h = (Listheader)obj;
				final Menuitem mitem = new Menuitem();
				mitem.setLabel(h.getLabel());
				mitem.setChecked(true);
				mitem.setCheckmark(true);
				mitem.setAutocheck(true);
				mitem.addEventListener(Events.ON_CHECK, new EventListener(){
					@Override
					public void onEvent(Event event) throws Exception {
						int hiddenCount = getVisibleListheaderCount(h.getListbox());
						if(hiddenCount==1) mitem.setChecked(true);
						if(!mitem.isChecked() && hiddenCount>1){
							h.setVisible(false);
						}else{
							h.setVisible(true);
						}
					}
				});
				mitem.setParent(popup);
			}
		}
		for(Object obj : list){
			if((obj instanceof Listheader) == true){
				Listheader h = (Listheader)obj;
				h.setContext(popup);
				popup.setParent(h);
			}
		}
	}
	
	
	public static int getVisibleListheaderCount(Listbox listbox){
		int count = 0;
		Listhead listhead = listbox.getListhead();
		for(Object obj : listhead.getChildren()){
			if(true == (obj instanceof Listheader)){
				if(((Listheader)obj).isVisible()){
					count++;
				}
			}
		}
		return count;
	}
}
