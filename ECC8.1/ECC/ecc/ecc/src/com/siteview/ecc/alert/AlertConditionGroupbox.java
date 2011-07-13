package com.siteview.ecc.alert;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;

import com.siteview.ecc.alert.dao.bean.BaseAlert;
import com.siteview.ecc.alert.dao.type.AlertTimes;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.util.Message;

public class AlertConditionGroupbox extends Groupbox {
	private static final long serialVersionUID = -1159615812829064919L;
	public Radio getSelectRadio1(){
		return (Radio)BaseTools.getComponentById(this,"selectradio1");
	}
	public Radio getSelectRadio2(){
		return (Radio)BaseTools.getComponentById(this,"selectradio2");
	}
	public Radio getSelectRadio3(){
		return (Radio)BaseTools.getComponentById(this,"selectradio3");
	}
	public Intbox getAwayCount(){
		return (Intbox)BaseTools.getComponentById(this,"awayCount");
	}
	public Intbox getOnlyOne(){
		return (Intbox)BaseTools.getComponentById(this,"onlyOne");
	}
	public Intbox getSelect1(){
		return (Intbox)BaseTools.getComponentById(this,"select1");
	}
	public Intbox getSelect2(){
		return (Intbox)BaseTools.getComponentById(this,"select2");
	}
	public Combobox getAlertEvent(){
		return (Combobox)BaseTools.getComponentById(this,"alertEvent");
	}
	public void onCreate() throws Exception{
		try{
			BaseAlert basealert = (BaseAlert) this.getDesktop().getExecution().getAttribute("alertinformation");
			if (basealert == null) return;
			
			getAlertEvent().setValue(basealert.getCategory().toString());
			
			if(basealert.getTimes() == AlertTimes.Always){
				getSelectRadio1().setSelected(true);
			}else if(basealert.getTimes() == AlertTimes.Only){
				getSelectRadio2().setSelected(true);
			}else if(basealert.getTimes() == AlertTimes.Select){
				getSelectRadio3().setSelected(true);
			}
			
			getAwayCount().setValue(new Integer(basealert.getAlways()));
			getOnlyOne().setValue(new Integer(basealert.getOnly()));
			getSelect1().setValue(new Integer(basealert.getSelect1()));
			getSelect2().setValue(new Integer(basealert.getSelect2()));
		}catch (Exception e){
			Message.showError(e.getMessage());
		}
	}

}
