package com.siteview.ecc.alert.control;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Paging;

public abstract class AbstractListbox extends Listbox 
{
	private static final long serialVersionUID = 4635669172089977790L;

	public abstract List<String> getListheader();
	
	public abstract void renderer();
	
	protected List<String> listhead;
	
	private List<Menuitem> menuItems = null;
	
	public void onCreate(){
		if(this.getChildren()!=null){
			for(Object obj :this.getChildren().toArray()){
				if(obj instanceof Paging){
					continue;
				}
				this.removeChild((Component)obj);
			}
		}
		listhead = getListheader();
		Menupopup popup =new Menupopup();
		menuItems = new ArrayList<Menuitem>();
		for(String label : getListheader())	{
			Menuitem mitem = new Menuitem();
			mitem.setLabel(label);
			mitem.setCheckmark(true);
			if(listhead.contains(label)){
				mitem.setChecked(true);
			}
			mitem.setAutocheck(true);
			mitem.addEventListener(Events.ON_CHECK, new ClickMenuitemListener());
			mitem.setParent(popup);
			menuItems.add(mitem);
		}
		
		Listhead head = new Listhead();
		head.setSizable(true);
		for(String label : listhead){
			Listheader header = new Listheader();
			header.setSort("auto");
			header.setLabel(label);
			head.appendChild(header);
//			head.setParent(this);
		}
		this.appendChild(head);

		for(Object obj : head.getChildren()){
			if(obj instanceof Listheader){
				Listheader header =(Listheader)obj;
				header.setContext(popup);
				popup.setParent(header);
			}
		}
		renderer();
	}
	
	class ClickMenuitemListener implements EventListener{
		@Override
		public void onEvent(Event target) throws Exception {
			int size = 0;
			for (Menuitem tmpitem : menuItems){
				if (tmpitem == null) continue;
				if(tmpitem.isChecked()){
					size++;
				}
			}
			if(size<=0) {
				((Menuitem)target.getTarget()).setChecked(true);
				return;
			}
			List<String> headnames = new ArrayList<String>();
			for (Object obj : getListhead().getChildren()){
				if (obj == null) continue;
				if ((obj instanceof Listheader) == false) continue;
				Listheader listheader = (Listheader) obj;
				if (listheader.getLabel() == null) continue;
				
				listheader.setVisible(false);
				for (Menuitem item : menuItems){
					if (item == null) continue;
					if(item.isChecked() && listheader.getLabel().equals(item.getLabel())){
						listheader.setVisible(true);
						headnames.add(item.getLabel());
					}
				}
			}
			listhead = headnames;
		}
	}
}

