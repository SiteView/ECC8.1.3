package com.siteview.ecc.alert.control;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Bandpopup;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Vbox;

public abstract class AbstractCheckBandBox extends Bandbox {
	private static final long serialVersionUID = 8214756239696918067L;
	class ButtonOKClickListener implements org.zkoss.zk.ui.event.EventListener {
		private Bandbox bandbox = null;
		private Listbox listbox = null;
		public ButtonOKClickListener(Bandbox bandbox,Listbox listbox){
			this.bandbox = bandbox;
			this.listbox = listbox;
		}
		@Override
		public void onEvent(Event event) throws Exception {
			StringBuffer stringBuffer = new StringBuffer();
			for (Object obj : listbox.getItems()){
				if (obj instanceof Listitem){
					Listitem item = (Listitem)obj;
					if (item.isSelected()){
						if (stringBuffer.length()>0) stringBuffer.append(",");
						stringBuffer.append(item.getLabel());
					}
				}
			}
			bandbox.setValue(stringBuffer.toString()); 
			bandbox.closeDropdown();
		}
		
	}
	class ListBoxSelectListener implements org.zkoss.zk.ui.event.EventListener {
		private Listbox listbox = null;
		public ListBoxSelectListener(Listbox listbox){
			this.listbox = listbox;
		}
		@Override
		public void onEvent(Event event) throws Exception {
			if (event instanceof SelectEvent){
				SelectEvent selectevent = (SelectEvent)event;
				Component comp = selectevent.getReference();
				if (comp instanceof Listitem){
					Listitem listitem = (Listitem)comp;
					if ("其他".equals(listitem.getValue())){
						for (Object obj : listbox.getItems()){
							if ((obj instanceof Listitem) == false) continue;
							if (listitem.equals(obj) == false){
								((Listitem)obj).setSelected(false);
							}
						}
					}else{
						for (Object obj : listbox.getItems()){
							if ((obj instanceof Listitem) == false) continue;
							if ("其他".equals(((Listitem)obj).getValue())){
								((Listitem)obj).setSelected(false);
							}
						}
					}
				}
			}
		}
		
	}
	private Listbox listbox = null;
	public void onCreate(){
		Bandpopup bandpopup = new Bandpopup();
		bandpopup.setParent(this);
		Vbox vbox = new Vbox();
		String bandboxwidth = this.getWidth();
		bandboxwidth = bandboxwidth.replaceAll("px", "");
		try{
			int width = new Integer(bandboxwidth);
			vbox.setWidth("" + (width + 17) + "px");
		}catch(Exception e){
			
		}
		vbox.setParent(bandpopup);
		
		listbox = new Listbox();
		listbox.setRows(5);
		listbox.setStyle("overflow-x:auto");
		listbox.setMultiple(true);
		listbox.setCheckmark(true);
		listbox.setParent(vbox);
		final Bandbox bandBox = this;
		listbox.addEventListener(Events.ON_SELECT, new ListBoxSelectListener(listbox));
		listbox.addEventListener(Events.ON_SELECT, new EventListener(){

			@Override
			public void onEvent(Event arg0) throws Exception {
				StringBuffer stringBuffer = new StringBuffer();
				for (Object obj : listbox.getItems()){
					if (obj instanceof Listitem){
						Listitem item = (Listitem)obj;
						if (item.isSelected()){
							if (stringBuffer.length()>0) stringBuffer.append(",");
							stringBuffer.append(item.getLabel());
						}
					}
				}
				bandBox.setValue(stringBuffer.toString()); 
//				bandBox.closeDropdown();
			}							
		});

//		Button button = BaseTools.getDefaultButton();
//		
//		button.setImage("/main/images/button/ico/ok_bt.gif");
//		button.setLabel("确  认");
//
//		button.setParent(vbox);
//		
//		button.addEventListener("onClick", new ButtonOKClickListener(this,listbox));
//		this.addEventListener(Events.ON_BLUR, new ButtonOKClickListener(this,listbox));
	}
	public void onOpen() throws Exception{
		listbox.getItems().clear();
		
		this.setRow(listbox,this.getValue().contains("其他"), "其他", "其他");

		for (String address : getSelectArray()){
			this.addAlertReceiver(listbox,address,isExist(this.getValue(),address));
		}
		
		if(listbox.getSelectedItems().size()<=0){
			for(Object obj : listbox.getItems()){
				if(obj instanceof Listitem){
					Listitem tmpItem = (Listitem)obj;
					if(tmpItem.getValue().toString().equals("其他")){
						tmpItem.setSelected(true);
						this.setValue("其他");
						break;
					}
				}
			}
		}			
	}

	private boolean isExist(String longtext,String value){
		return longtext.startsWith(value + ",")
			|| longtext.equals(value)
			|| longtext.contains("," + value + ",")
			|| longtext.endsWith("," + value)
			;
	}
	private void addAlertReceiver(Listbox listbox,String receiverAddress,boolean isSelected) throws Exception{
		if (receiverAddress == null ) return;
		for (Object obj : listbox.getItems()){
			if (obj instanceof Listitem){
				Listitem item = (Listitem) obj;
				if (item.getLabel()!=null && item.getLabel().equals(receiverAddress)){
					return;
				}
			}
		}
		this.setRow(listbox,isSelected, receiverAddress, receiverAddress);
	}
	
	public void setRow(Listbox list,boolean isSelected,Object value,String...cols)
	{
		
		Listitem item = new Listitem();
		item.setSelected(isSelected);
		item.setValue(value);
		for (String col : cols){
			Listcell cell = new Listcell();
			cell.setLabel(col);
			item.appendChild(cell);
		}
		list.appendChild(item);
	}
	
	public abstract String[] getSelectArray() ;
}
