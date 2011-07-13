package com.siteview.ecc.alert;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.siteview.base.data.UserRightId;
import com.siteview.ecc.alert.dao.bean.AccessControl;
import com.siteview.ecc.alert.dao.bean.BaseAlert;
import com.siteview.ecc.alert.dao.bean.SMSAlert;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.alert.util.DictionaryFactory;
import com.siteview.ecc.report.common.AddLinkFuntion;
import com.siteview.ecc.report.common.UrlPropertiesType;
import com.siteview.ecc.util.Message;
import com.siteview.ecc.util.Toolkit;

public class SmsAlertView extends AbstractWindow{
	
	private static final long serialVersionUID = -8320453436709586892L;
	private SMSAlert alertinformation = null;
	
	public BaseAlert getAlertinformation() {
		return alertinformation;
	}
	public void setAlertinformation(SMSAlert alertinformation) {
		this.alertinformation = alertinformation;
	}
	//、报警接收手机号选择链接到短信设置
	public Label alerReceiverSmsLink(){
		return (Label)BaseTools.getComponentById(this,"alerReceiverSmsLink");
	}
	//短信模板链接到短信设置中的短信模板设置
	public Label smsTemplateLink(){
		return (Label)BaseTools.getComponentById(this,"smsTemplateLink");
	}
	//值班报警列表链接到值班表设
	public Label watchSheetLink(){
		return (Label)BaseTools.getComponentById(this,"watchSheetLink");
	}
	/**
	 * 发送方式
	 * @return Combobox
	 */
	public Combobox getSmsModesCombobox(){
		return (Combobox)BaseTools.getComponentById(this,"smsModes");
	}
	/**
	 * 短信模板 -- Web
	 * @return Combobox
	 */
	public Combobox getSmsTemplateWebCombobox(){
		return (Combobox)BaseTools.getComponentById(this,"smsTemplateWeb");
	}
	/**
	 * 短信模板
	 * @return Combobox
	 */
	public Combobox getSmsTemplateCombobox(){
		return (Combobox)BaseTools.getComponentById(this,"smsTemplate");
	}
	public Div getSmsTemplateDiv(){
		return (Div)BaseTools.getComponentById(this,"smsTemplateDiv");
	}
	
	/**
	 * 升级接受人地址
	 * @return Textbox
	 */
	public Textbox getUpdateReceiver(){
		return (Textbox)BaseTools.getComponentById(this,"updateReceiver");
	}
	/**
	 * 升级次数
	 * @return Intbox
	 */
	public Intbox getUpdateCount(){
		return (Intbox)BaseTools.getComponentById(this,"updateCount");
	}
	/**
	 * 停止次数
	 * @return Intbox
	 */
	public Intbox getStopCount(){
		return (Intbox)BaseTools.getComponentById(this,"stopCount");
	}
	/**
	 * 值班报警列表
	 * @return Combobox
	 */
	public Combobox getWatchSheet(){
		return (Combobox)BaseTools.getComponentById(this,"watchSheet");
	}
	/**
	 * 报警接收手机号
	 * @return Bandbox
	 */
	public Bandbox getAlertReceiver(){
		return (Bandbox)BaseTools.getComponentById(this,"alertReceiver");
	}
	/**
	 * 其它手机号
	 * @return Textbox
	 */
	public Textbox getOtherTelNo(){
		return (Textbox)BaseTools.getComponentById(this,"otherTelNo");
	}
	
	/**
	 * 告警策略
	 */
	public Combobox getAlertPloy(){
		return (Combobox)BaseTools.getComponentById(this,"alertPloy");
	}
	class OkClickListener implements org.zkoss.zk.ui.event.EventListener {
		SmsAlertView view = null;
		public OkClickListener(SmsAlertView view){
			this.view = view;
		}
		@Override
		public void onEvent(Event event) throws Exception {
			try{
				if(view.getStopCount().getValue()<view.getUpdateCount().getValue()){
					Message.showInfo("停止次数不能小于升级次数！");
					return;
				}
				if(getAlertReceiver().getValue().equals("其他") && getOtherTelNo().getValue().equals("")){
					Message.showInfo("报警短信接收地址不能为空！");
					return;
				}
//				if(!"".equals(getOtherTelNo().getValue())&&!BaseTools.validatePhoneNO(getOtherTelNo().getValue())){
//					Message.showInfo("您输入的手机号码有误，或者暂不支持该手机号！");
//					return;
//				}
				view.setBaseAlertInformation(alertinformation);

				if ("Web".equals(alertinformation.getSendMode())){
					alertinformation.setSMSTemplate(view.getSmsTemplateWebCombobox().getValue());
				}else{
					alertinformation.setSMSTemplate(view.getSmsTemplateCombobox().getValue());
				}

				alertinformation.setSmsNumber(view.getAlertReceiver().getValue());
				alertinformation.setSendMode(view.getSmsModesCombobox().getValue());
				alertinformation.setOtherNumber(view.getOtherTelNo().getValue());
				alertinformation.setReceiverAddress(view.getUpdateReceiver().getValue());
				alertinformation.setStopTimes(view.getStopCount().getValue().toString());
				alertinformation.setUpgradeTimes(view.getUpdateCount().getValue().toString());
				alertinformation.setWatchSheet("空".equals(view.getWatchSheet().getValue().trim())?"":view.getWatchSheet().getValue());
				alertinformation.setAlertPloy(view.getAlertPloy().getValue());
				if (EmailAlertView.CMD_ADD.equals(this.view.getVariable(EmailAlertView.COMMAND_VAR, true))){
					DictionaryFactory.getIAlertDao().addAlert(new AccessControl(), alertinformation);
				}else if (EmailAlertView.CMD_EDIT.equals(this.view.getVariable(EmailAlertView.COMMAND_VAR, true))){
					DictionaryFactory.getIAlertDao().updateAlert(new AccessControl(), alertinformation, true);
				}
				Toolkit.getToolkit().setCookie("alert_rul_imfomation", alertinformation.getId(), 99999999);
				view.detach();
			}catch (Exception e){
				Message.showError(e.getMessage());
			}
		}
	}
	
	class SmsModesComboboxSelectListener implements org.zkoss.zk.ui.event.EventListener {
		SmsAlertView view = null;
		
		public SmsModesComboboxSelectListener(SmsAlertView view){
			this.view = view;
		}
		@Override
		public void onEvent(Event event) throws Exception {
			try{
				if ("Web".equals(view.getSmsModesCombobox().getText())){
					this.view.getSmsTemplateWebCombobox().setVisible(true);
					this.view.getSmsTemplateCombobox().setVisible(false);
				}else{
					this.view.getSmsTemplateWebCombobox().setVisible(false);
					this.view.getSmsTemplateCombobox().setVisible(true);
				}
			}catch (Exception e){
				Message.showError(e.getMessage());
			}
		}
	}
	public void onCreate() throws Exception{
		try{
			BaseTools.checkLinkLabel(alerReceiverSmsLink(),UserRightId.SetSms,new AddLinkFuntion(UrlPropertiesType.SetSms));
			BaseTools.checkLinkLabel(smsTemplateLink(),UserRightId.SetSms,new AddLinkFuntion(UrlPropertiesType.SetSms,"msgTemplateSet"));
			BaseTools.checkLinkLabel(watchSheetLink(),UserRightId.SetMaintain,new AddLinkFuntion(UrlPropertiesType.SetSms));
			
			getOkButton().addEventListener(Events.ON_CLICK, new OkClickListener(this));
			getSmsModesCombobox().addEventListener(Events.ON_CHANGE, new SmsModesComboboxSelectListener(this));
			if (CMD_ADD.equals(this.getVariable(COMMAND_VAR, true))){
				this.setAlertinformation(new SMSAlert());
				getAlertReceiver().setValue("其他");
			}else if (CMD_EDIT.equals(this.getVariable(COMMAND_VAR, true))){
				this.setAlertinformation((SMSAlert) this.getVariable("alertinformation",true));
				getOtherTelNo().setText(alertinformation.getOtherNumber());
				getSmsModesCombobox().setText(alertinformation.getSendMode());
				
				if ("Web".equals(alertinformation.getSendMode())){
					getSmsTemplateWebCombobox().setText(alertinformation.getSMSTemplate());
				}else{
					getSmsTemplateCombobox().setText(alertinformation.getSMSTemplate());
				}
				
				getAlertReceiver().setText(alertinformation.getSmsNumber());
				
				getUpdateReceiver().setValue(alertinformation.getReceiverAddress());
				getAlertPloy().setValue(alertinformation.getAlertPloy());
				try{
					getStopCount().setValue(new Integer(alertinformation.getStopTimes()));
				}catch(Exception e){
					
				}
				try{
					getUpdateCount().setValue(new Integer(alertinformation.getUpgradeTimes()));
				}catch(Exception e){
					
				}
				getWatchSheet().setValue(alertinformation.getWatchSheet()==null || "".equals(alertinformation.getWatchSheet()) ? "空" : alertinformation.getWatchSheet());
				
				setBaseAlertComponentInformation(this.getAlertinformation());
			}
		}catch (Exception e){
			Message.showError(e.getMessage());
		}
	}
}
