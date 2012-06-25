package com.siteview.ecc.report;

import java.util.List;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.siteview.ecc.alert.control.TooltipPopup;


public class SyslogItemRender extends ListModelList implements ListitemRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public SyslogItemRender(List table) {
		addAll(table);
	}


@Override
public void render(Listitem arg0, Object arg1) throws Exception {
//	String inTime;
//	String sourceIP;
//	String facility;
//	String leave;
//	String sysLogmsg;
	Listitem item = arg0;
	SysLogValueList m = (SysLogValueList) arg1;
	TooltipPopup popup = new TooltipPopup();
	popup.onCreate();
	popup.setStyle("border:none;color:#FFFFFF;background-color:#717171");
	popup.setTitle(m.getInTime());
	popup.addDescription("���ʱ��", m.getInTime());
	popup.addDescription("IP��ַ", m.getSourceIP());
	popup.addDescription("�豸", m.getFacility());
	popup.addDescription("����", m.getLeave());
	popup.addDescription("ϵͳ��־��Ϣ", m.getSysLogmsg());
	Listcell c1 = new Listcell (m.getInTime());
	c1.setPopup(popup);
	popup.setParent(c1);
	c1.setParent(item);	
	Listcell c2 = new Listcell (m.getSourceIP());
	c2.setPopup(popup);
	popup.setParent(c2);
	c2.setParent(item);
	Listcell c3=new Listcell (m.getFacility());
	c3.setPopup(popup);
	popup.setParent(c3);
	c3.setParent(item);
	Listcell c4=new Listcell (m.getLeave());
	c4.setPopup(popup);
	popup.setParent(c4);
	c4.setParent(item);
	Listcell c5=new Listcell (m.getSysLogmsg());
	c5.setPopup(popup);
	popup.setParent(c5);
	c5.setParent(item);
	
}

}
