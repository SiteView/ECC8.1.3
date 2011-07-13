package com.siteview.ecc.alert;

import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;

import com.siteview.base.manage.View;
import com.siteview.ecc.alert.control.ScriptCombobox;
import com.siteview.ecc.alert.dao.bean.AccessControl;
import com.siteview.ecc.alert.dao.bean.BaseAlert;
import com.siteview.ecc.alert.dao.bean.ScriptAlert;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.alert.util.DictionaryFactory;
import com.siteview.ecc.util.Message;
import com.siteview.ecc.util.Toolkit;

public class ScriptAlertView extends AbstractWindow {

	private static final long serialVersionUID = -8320453436709586892L;
	private ScriptAlert alertinformation = null;

	public BaseAlert getAlertinformation() {
		return alertinformation;
	}

	public void setAlertinformation(ScriptAlert alertinformation) {
		this.alertinformation = alertinformation;
	}

	class OkClickListener implements org.zkoss.zk.ui.event.EventListener {
		ScriptAlertView view = null;

		public OkClickListener(ScriptAlertView view) {
			this.view = view;
		}

		@Override
		public void onEvent(Event event) throws Exception {
			try {
				view.setBaseAlertInformation(alertinformation);
				alertinformation.setScriptServer(view.getScriptServer()
						.getText());
				ScriptCombobox ss = new ScriptCombobox();
				Map<String, String> map = ss.getSelectArray();
				String serverid = view.getScriptServer().getValue();
				Set<String> kset = map.keySet();
				for (String ks : kset) {
					if (serverid.equals(map.get(ks))) {
						alertinformation.setServerID(ks);
					}
				}
				alertinformation.setScriptFile(getScriptTemplate().getValue());
				alertinformation.setScriptParam(getScriptParam().getValue());
				alertinformation.setAlertPloy(view.getAlertPloy().getValue());
				if (EmailAlertView.CMD_ADD.equals(this.view.getVariable(
						EmailAlertView.COMMAND_VAR, true))) {
					DictionaryFactory.getIAlertDao().addAlert(
							new AccessControl(), alertinformation);
				} else if (EmailAlertView.CMD_EDIT.equals(this.view
						.getVariable(EmailAlertView.COMMAND_VAR, true))) {
					DictionaryFactory.getIAlertDao().updateAlert(
							new AccessControl(), alertinformation, true);
				}
				Toolkit.getToolkit().setCookie("alert_rul_imfomation",
						alertinformation.getId(), 99999999);
				view.detach();
			} catch (Exception e) {
				Message.showError(e.getMessage());
			}
		}
	}

	public void onCreate() throws Exception {
		try {
			getOkButton().addEventListener(Events.ON_CLICK,
					new OkClickListener(this));

			if (CMD_ADD.equals(this.getVariable(COMMAND_VAR, true))) {
				this.setAlertinformation(new ScriptAlert());
			} else if (CMD_EDIT.equals(this.getVariable(COMMAND_VAR, true))) {
				this.setAlertinformation((ScriptAlert) this.getVariable(
						"alertinformation", true));
				getScriptParam().setText(alertinformation.getScriptParam());
				getScriptTemplate().setText(alertinformation.getScriptFile());
				getScriptServer().setValue(alertinformation.getScriptServer());
				getAlertPloy().setValue(alertinformation.getAlertPloy());
				setBaseAlertComponentInformation(this.getAlertinformation());

			}
		} catch (Exception e) {
			Message.showError(e.getMessage());
		}
	}

	/**
	 * 附加参数
	 * 
	 * @return Textbox
	 */
	public Textbox getScriptParam() {
		return (Textbox) BaseTools.getComponentById(this, "scriptParam");
	}

	/**
	 * 选择脚本
	 * 
	 * @return Combobox
	 */
	public Combobox getScriptTemplate() {
		return (Combobox) BaseTools.getComponentById(this, "scriptTemplate");
	}

	/**
	 * 选择服务器
	 * 
	 * @return Combobox
	 */
	public Combobox getScriptServer() {
		return (Combobox) BaseTools.getComponentById(this, "scriptServer");
	}

	/**
	 * 告警策略
	 */
	public Combobox getAlertPloy() {
		return (Combobox) BaseTools.getComponentById(this, "alertPloy");
	}
}
