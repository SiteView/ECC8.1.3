package com.siteview.actions;

public class LoginUserRight extends UserRight {
	public LoginUserRight(String userid) {
		super(userid);
	}

	private String loginIp;

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
}
