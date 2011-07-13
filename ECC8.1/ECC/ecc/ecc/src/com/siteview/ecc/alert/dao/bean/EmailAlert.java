package com.siteview.ecc.alert.dao.bean;


public class EmailAlert extends BaseAlert{
    private String emailAddresss;
    private String emailTemplate;
    private String otherAddress;
    private String receiverAddress;
    private String stopTimes;
    private String upgradeTimes;
    private String watchSheet;
    private String alertPloy;
    
	public BaseAlert getBaseInfo() {
		return this;
	}
	public String getEmailAddresss() {
		return emailAddresss;
	}
	public String getEmailTemplate() {
		return emailTemplate;
	}
	public String getOtherAddress() {
		return otherAddress;
	}
	public String getReceiverAddress() {
		return receiverAddress;
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
	public void setEmailAddresss(String emailAddresss) {
		this.emailAddresss = emailAddresss;
	}
	public void setEmailTemplate(String emailTemplate) {
		this.emailTemplate = emailTemplate;
	}
	public void setOtherAddress(String otherAddress) {
		this.otherAddress = otherAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
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
