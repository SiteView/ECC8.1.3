package com.siteview.ecc.alert;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.siteview.base.data.UserRightId;
import com.siteview.ecc.alert.dao.bean.AccessControl;
import com.siteview.ecc.alert.dao.bean.BaseAlert;
import com.siteview.ecc.alert.dao.bean.EmailAlert;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.alert.util.DictionaryFactory;
import com.siteview.ecc.report.common.AddLinkFuntion;
import com.siteview.ecc.report.common.UrlPropertiesType;
import com.siteview.ecc.util.LinkCheck;
import com.siteview.ecc.util.Message;
import com.siteview.ecc.util.Toolkit;

public class EmailAlertView extends AbstractWindow {
	private static final long serialVersionUID = -8320453436709586892L;
	private EmailAlert alertinformation = null;
	
	public BaseAlert getAlertinformation() {
		return alertinformation;
	}
	public void setAlertinformation(EmailAlert alertinformation) {
		this.alertinformation = alertinformation;
	}
	class OkClickListener implements org.zkoss.zk.ui.event.EventListener {
		EmailAlertView view = null;
		public OkClickListener(EmailAlertView view){
			this.view = view;
		}
		@Override
		public void onEvent(Event event) throws Exception {
			try{
				if(view.getStopCount().getValue()<view.getUpdateCount().getValue()){
					Message.showInfo("ֹͣ��������С������������");
					return;
				}
				//�µĹ����ʼ����յ�ַ �� ֵ�౨���б� ���ٴ���һ�� �� ����Ҫ��
				if(getWatchSheet().getValue().equals("��")&&getAlertReceiver().getValue().equals("����") && getOtherEmailAddress().getValue().isEmpty()){
					Message.showInfo("�����ʼ����յ�ַ����Ϊ�գ�");
					return;
				}
				if(!getOtherEmailAddress().getValue().isEmpty() && !BaseTools.validateEmail(getOtherEmailAddress().getValue())){
					Message.showInfo("�����ʼ���ַ��ʽ����ȷ��");
					return;
				}
				view.setBaseAlertInformation(alertinformation);
				alertinformation.setEmailAddresss(view.getAlertReceiver().getValue());
				alertinformation.setEmailTemplate(view.getEmailTemplate().getValue());
				alertinformation.setOtherAddress(view.getOtherEmailAddress().getValue());
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
				if("�����뱨������".equals(e.getMessage())){
					this.view.getAlertName().setFocus(true);
				}
			}
		}
	}

	public void onCreate(Event event) throws Exception{
		try{
			BaseTools.checkLinkLabel(emailReceiveLink(),UserRightId.SetMail,new AddLinkFuntion(UrlPropertiesType.SetMail));
			BaseTools.checkLinkLabel(emailTemplateLink(),UserRightId.SetMail,new AddLinkFuntion(UrlPropertiesType.SetMail,"templateSetBtn"));
			BaseTools.checkLinkLabel(wacthSheetLink(),UserRightId.SetMaintain,new AddLinkFuntion(UrlPropertiesType.SetMaintain));
			
			getOkButton().addEventListener(Events.ON_CLICK, new OkClickListener(this));
			getAlertReceiver().setValue("����");
			if (CMD_ADD.equals(this.getVariable(COMMAND_VAR, true))){
				this.setAlertinformation(new EmailAlert());
			}else if (CMD_EDIT.equals(this.getVariable(COMMAND_VAR, true))){
				this.setAlertinformation((EmailAlert) this.getVariable("alertinformation",true));
				getAlertReceiver().setValue(alertinformation.getEmailAddresss());
				getEmailTemplate().setValue(alertinformation.getEmailTemplate());
				getOtherEmailAddress().setValue(alertinformation.getOtherAddress());
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
	 * �����ʼ����ܵ�ַ
	 * @return Bandbox
	 */
	public Bandbox getAlertReceiver(){
		return (Bandbox)BaseTools.getComponentById(this,"alertReceiver");
	}
	/**
	 * �����ʼ���ַ
	 * @return Textbox
	 */
	public Textbox getOtherEmailAddress(){
		return (Textbox)BaseTools.getComponentById(this,"otherEmailAddress");
	}
	/**
	 * Emailģ��
	 * @return Combobox
	 */
	public Combobox getEmailTemplate(){
		return (Combobox)BaseTools.getComponentById(this,"emailTemplate");
	}
	/**
	 * ���������˵�ַ
	 * @return Textbox
	 */
	public Textbox getUpdateReceiver(){
		return (Textbox)BaseTools.getComponentById(this,"updateReceiver");
	}
	/**
	 * ֵ�౨���б�
	 * @return Combobox
	 */
	public Combobox getWatchSheet(){
		return (Combobox)BaseTools.getComponentById(this,"watchSheet");
	}
	/**
	 * �澯����
	 */
	public Combobox getAlertPloy(){
		return (Combobox)BaseTools.getComponentById(this,"alertPloy");
	}
	//�ʼ����յ�ַѡ�����ӵ��ʼ�����
	public Label emailReceiveLink(){
		return (Label)BaseTools.getComponentById(this,"emailReceiveLink");
	}
	//Emailģ�����ӵ��ʼ������е��ʼ�ģ������
	public Label emailTemplateLink(){
		return (Label)BaseTools.getComponentById(this,"emailTemplateLink");
	}
	
	//ֵ�౨���б����ӵ�ֵ�������
	public Label wacthSheetLink(){
		return (Label)BaseTools.getComponentById(this,"wacthSheetLink");
	}
}
