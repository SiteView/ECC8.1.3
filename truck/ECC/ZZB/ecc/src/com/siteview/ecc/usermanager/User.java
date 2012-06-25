/**
 * 
 */
package com.siteview.ecc.usermanager;

public class User implements java.lang.Comparable<User> {
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getNIsUse() {
		return nIsUse;
	}
	public void setNIsUse(String isUse) {
		nIsUse = isUse;
	}
	public String getNAdmin() {
		return nAdmin;
	}
	public void setNAdmin(String admin) {
		nAdmin = admin;
	}
	public String getNIndex() {
		return nIndex;
	}
	public void setNIndex(String index) {
		nIndex = index;
	}
	private String userName;
	private String loginName;
	private String nIsUse;//是否启用
	private String nAdmin;//是否是管理员用户
	private String nIndex;//section序列
	@Override
	public int compareTo(User o) {
		// TODO Auto-generated method stub
		return 0;
	}

}