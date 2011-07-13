package com.siteview.ecc.alert.dao.bean;


public class SMSAlert extends BaseAlert{
    private String otherNumber;
    private String receiverAddress;
    private String sendMode;
    private String smsNumber;
    private String SMSTemplate;
    private String stopTimes;
    private String upgradeTimes;
    private String watchSheet;
    private String alertPloy;
    
	public BaseAlert getBaseInfo() {
		return this;
	}
	public String getOtherNumber() {
		return otherNumber;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public String getSendMode() {
		return sendMode;
	}
	public String getSmsNumber() {
		return smsNumber;
	}
	public String getSMSTemplate() {
		return SMSTemplate;
	}
	public String getStopTimes() {
		return stopTimes;
	}
	public String getUpgradeTimes() {
		return upgradeTimes;
	}
	public String getWatchSheet() {
		return watchSheet;
	}
	public void setBaseInfo(BaseAlert baseInfo) {
		this.setValues(baseInfo);
	}
	public void setOtherNumber(String otherNumber) {
		this.otherNumber = otherNumber;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public void setSendMode(String sendMode) {
		this.sendMode = sendMode;
	}
	public void setSmsNumber(String smsNumber) {
		this.smsNumber = smsNumber;
	}
	public void setSMSTemplate(String template) {
		SMSTemplate = template;
	}
	public void setStopTimes(String stopTimes) {
		this.stopTimes = stopTimes;
	}
	public void setUpgradeTimes(String upgradeTimes) {
		this.upgradeTimes = upgradeTimes;
	}
	public void setWatchSheet(String watchSheet) {
		this.watchSheet = watchSheet;
	}
	public String getAlertPloy() {
		return alertPloy;
	}
	public void setAlertPloy(String alertPloy) {
		this.alertPloy = alertPloy;
	}

}
