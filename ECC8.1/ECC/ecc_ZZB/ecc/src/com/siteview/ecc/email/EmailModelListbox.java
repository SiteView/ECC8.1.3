package com.siteview.ecc.email;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.siteview.ecc.alert.control.AbstractListbox;


public class EmailModelListbox extends AbstractListbox {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2073544338484744308L;
	private ArrayList<EmailBean> EmailBeans;
	private Object indexObject ;
	
	@Override
	public List<String> getListheader() {
		return new ArrayList<String>(Arrays.asList(new String[] {"Ãû³Æ"
																,"×´Ì¬"
																,"µç×ÓÓÊ¼þµØÖ·"
																,"±à¼­"}));
	}

 	@Override
	public void renderer() {
 		if(EmailBeans == null) return;
		try {
			for(EmailBean tmpKey : EmailBeans){
				Listitem item = new Listitem();
				item.setHeight("28px");
				item.setValue(tmpKey);
				item.setId(tmpKey.getSection());
				for(String head : listhead){
					if(head.equals("Ãû³Æ")){
						Listcell cell = new Listcell(tmpKey.getName());
						cell.setTooltiptext(tmpKey.getName());
						cell.setImage("/main/images/email.gif");
						cell.setParent(item);
					}
					if(head.equals("×´Ì¬")){
						Listcell cell = null;
						if("1".equals(tmpKey.getBcheck())){
							cell = new Listcell("½ûÖ¹");
							cell.setImage("/main/images/button/ico/disable_bt.gif");
							cell.setTooltiptext("½ûÖ¹");
						}else{
							cell = new Listcell("ÔÊÐí");
							cell.setImage("/main/images/button/ico/enable_bt.gif");
							cell.setTooltiptext("ÔÊÐí");
						}
						cell.setParent(item);
					}
					if(head.equals("µç×ÓÓÊ¼þµØÖ·")){
						Listcell cell = new Listcell(tmpKey.getMailList());
						cell.setImage("/images/email2.gif");
						cell.setTooltiptext(tmpKey.getMailList());
						cell.setParent(item);
					}
					if(head.equals("±à¼­")){
						Listcell cell = new Listcell();
						cell.setImage("/main/images/alert/edit.gif");
						cell.setParent(item);
						final String section = tmpKey.getSection();
						cell.addEventListener(Events.ON_CLICK, new EventListener()
						{
							@Override
							public void onEvent(Event event) throws Exception
							{
								final Window win2 = (Window) Executions.createComponents("/main/setting/editEmailSet.zul", null, null);
								win2.setAttribute(EmailConstant.EmailEditSection, section);
								win2.doModal();
							}
						});
					}
				}
				item.setParent(this);
				if(indexObject != null && tmpKey.getSection().equals((String)indexObject)){
					this.setSelectedItem(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<EmailBean> getEmailBeans() {
		return EmailBeans;
	}

	public void setEmailBeans(ArrayList<EmailBean> emailBeans) {
		EmailBeans = emailBeans;
	}
	
	public Object getIndexObject() {
		return indexObject;
	}

	public void setIndexObject(Object indexObject) {
		this.indexObject = indexObject;
	}

}
