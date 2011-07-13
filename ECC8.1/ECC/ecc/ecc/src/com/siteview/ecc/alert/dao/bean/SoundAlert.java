package com.siteview.ecc.alert.dao.bean;


public class SoundAlert  extends BaseAlert{
    private String loginName;
	private String loginPassword;
	private String serverName;
	private String alertPloy;
	
	public BaseAlert getBaseInfo() {
		return this;
	}
	public String getLoginName() {
		return loginName;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public String getServerName() {
		return serverName;
	}
	public void setBaseInfo(BaseAlert baseInfo) {
		this.setValues(baseInfo);
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	public String getAlertPloy() {
		return alertPloy;
	}
	public void setAlertPloy(String alertPloy) {
		this.alertPloy = alertPloy;
	}

}
