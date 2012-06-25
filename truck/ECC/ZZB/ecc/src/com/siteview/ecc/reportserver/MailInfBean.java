package com.siteview.ecc.reportserver;

public class MailInfBean {
	private String smtpServerInfo = null;
	public String getSmtpServerInfo() {
		return smtpServerInfo;
	}
	public void setSmtpServerInfo(String smtpServerInfo) {
		this.smtpServerInfo = smtpServerInfo;
	}
	public String getFromInfo() {
		return fromInfo;
	}
	public void setFromInfo(String fromInfo) {
		this.fromInfo = fromInfo;
	}
	public String getFromUserInfo() {
		return fromUserInfo;
	}
	public void setFromUserInfo(String fromUserInfo) {
		this.fromUserInfo = fromUserInfo;
	}
	public String getFromPwdInfo() {
		return fromPwdInfo;
	}
	public void setFromPwdInfo(String fromPwdInfo) {
		this.fromPwdInfo = fromPwdInfo;
	}
	private String fromInfo = null;
	private String fromUserInfo = null;
	private String fromPwdInfo = null;

}
