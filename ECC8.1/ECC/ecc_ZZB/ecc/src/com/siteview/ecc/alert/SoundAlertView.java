package com.siteview.ecc.alert;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.siteview.ecc.alert.dao.bean.AccessControl;
import com.siteview.ecc.alert.dao.bean.BaseAlert;
import com.siteview.ecc.alert.dao.bean.SoundAlert;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.alert.util.DictionaryFactory;
import com.siteview.ecc.util.Toolkit;

public class SoundAlertView extends AbstractWindow {

	private static final long serialVersionUID = -8320453436709586892L;
	private SoundAlert alertinformation = null;
	
	public BaseAlert getAlertinformation() {
		return alertinformation;
	}
	public void setAlertinformation(SoundAlert alertinformation) {
		this.alertinformation = alertinformation;
	}
	class OkClickListener implements EventListener {
		SoundAlertView view = null;
		public OkClickListener(SoundAlertView view){
			this.view = view;
		}
		@Override
		public void onEvent(Event event) throws Exception {
			try{
				view.setBaseAlertInformation(alertinformation);

				alertinformation.setServerName(getAlertServerName().getValue());
				alertinformation.setLoginName(getAlertLoginName().getValue());
				alertinformation.setLoginPassword(getAlertPassword().getValue());
				if (EmailAlertView.CMD_ADD.equals(this.view.getVariable(EmailAlertView.COMMAND_VAR, true))){
					DictionaryFactory.getIAlertDao().addAlert(new AccessControl(), alertinformation);
				}else if (EmailAlertView.CMD_EDIT.equals(this.view.getVariable(EmailAlertView.COMMAND_VAR, true))){
					DictionaryFactory.getIAlertDao().updateAlert(new AccessControl(), alertinformation, true);
				}
				Toolkit.getToolkit().setCookie("alert_rul_imfomation", alertinformation.getId(), 99999999);
				view.detach();
			}catch (Exception e){
				Messagebox.show(e.getMessage(), "´íÎó", Messagebox.OK, Messagebox.ERROR);
			}
		}
	}
	public void onCreate() throws Exception{
		try{
			getOkButton().addEventListener(Events.ON_CLICK, new OkClickListener(this));
			
			if (CMD_ADD.equals(this.getVariable(COMMAND_VAR, true))){
				this.setAlertinformation(new SoundAlert());
			}else if (CMD_EDIT.equals(this.getVariable(COMMAND_VAR, true))){
				this.setAlertinformation((SoundAlert) this.getVariable("alertinformation",true));
				getAlertServerName().setText(alertinformation.getServerName());
				getAlertLoginName().setText(alertinformation.getLoginName());
				getAlertPassword().setText(alertinformation.getLoginPassword());
				setBaseAlertComponentInformation(this.getAlertinformation());
				
			}
		}catch (Exception e){
			Messagebox.show(e.getMessage(), "´íÎó", Messagebox.OK, Messagebox.ERROR);
		}
	}
	/**
	 * ·þÎñÆ÷Ãû
	 * @return Textbox
	 */
	public Textbox getAlertServerName(){
		return (Textbox)BaseTools.getComponentById(this,"alertServerName");
	}
	/**
	 * µÇÂ¼Ãû
	 * @return Textbox
	 */
	public Textbox getAlertLoginName(){
		return (Textbox)BaseTools.getComponentById(this,"alertLoginName");
	}
	/**
	 * µÇÂ¼ÃÜÂë
	 * @return Textbox
	 */
	public Textbox getAlertPassword(){
		return (Textbox)BaseTools.getComponentById(this,"alertPassword");
	}
}
