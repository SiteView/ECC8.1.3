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
	//�����������ֻ���ѡ�����ӵ���������
	public Label alerReceiverSmsLink(){
		return (Label)BaseTools.getComponentById(this,"alerReceiverSmsLink");
	}
	//����ģ�����ӵ����������еĶ���ģ������
	public Label smsTemplateLink(){
		return (Label)BaseTools.getComponentById(this,"smsTemplateLink");
	}
	//ֵ�౨���б����ӵ�ֵ�����
	public Label watchSheetLink(){
		return (Label)BaseTools.getComponentById(this,"watchSheetLink");
	}
	/**
	 * ���ͷ�ʽ
	 * @return Combobox
	 */
	public Combobox getSmsModesCombobox(){
		return (Combobox)BaseTools.getComponentById(this,"smsModes");
	}
	/**
	 * ����ģ�� -- Web
	 * @return Combobox
	 */
	public Combobox getSmsTemplateWebCombobox(){
		return (Combobox)BaseTools.getComponentById(this,"smsTemplateWeb");
	}
	/**
	 * ����ģ��
	 * @return Combobox
	 */
	public Combobox getSmsTemplateCombobox(){
		return (Combobox)BaseTools.getComponentById(this,"smsTemplate");
	}
	public Div getSmsTemplateDiv(){
		return (Div)BaseTools.getComponentById(this,"smsTemplateDiv");
	}
	
	/**
	 * ���������˵�ַ
	 * @return Textbox
	 */
	public Textbox getUpdateReceiver(){
		return (Textbox)BaseTools.getComponentById(this,"updateReceiver");
	}
	/**
	 * ��������
	 * @return Intbox
	 */
	public Intbox getUpdateCount(){
		return (Intbox)BaseTools.getComponentById(this,"updateCount");
	}
	/**
	 * ֹͣ����
	 * @return Intbox
	 */
	public Intbox getStopCount(){
		return (Intbox)BaseTools.getComponentById(this,"stopCount");
	}
	/**
	 * ֵ�౨���б�
	 * @return Combobox
	 */
	public Combobox getWatchSheet(){
		return (Combobox)BaseTools.getComponentById(this,"watchSheet");
	}
	/**
	 * ���������ֻ���
	 * @return Bandbox
	 */
	public Bandbox getAlertReceiver(){
		return (Bandbox)BaseTools.getComponentById(this,"alertReceiver");
	}
	/**
	 * �����ֻ���
	 * @return Textbox
	 */
	public Textbox getOtherTelNo(){
		return (Textbox)BaseTools.getComponentById(this,"otherTelNo");
	}
	
	/**
	 * �澯����
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
					Message.showInfo("ֹͣ��������С������������");
					return;
				}
				if(getAlertReceiver().getValue().equals("����") && getOtherTelNo().getValue().equals("")){
					Message.showInfo("�������Ž��յ�ַ����Ϊ�գ�");
					return;
				}
//				if(!"".equals(getOtherTelNo().getValue())&&!BaseTools.validatePhoneNO(getOtherTelNo().getValue())){
//					Message.showInfo("��������ֻ��������󣬻����ݲ�֧�ָ��ֻ��ţ�");
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
				alertinformation.setWatchSheet("��".equals(view.getWatchSheet().getValue().trim())?"":view.getWatchSheet().getValue());
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
				getAlertReceiver().setValue("����");
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
				getWatchSheet().setValue(alertinformation.getWatchSheet()==null || "".equals(alertinformation.getWatchSheet()) ? "��" : alertinformation.getWatchSheet());
				
				setBaseAlertComponentInformation(this.getAlertinformation());
			}
		}catch (Exception e){
			Message.showError(e.getMessage());
		}
	}
}
