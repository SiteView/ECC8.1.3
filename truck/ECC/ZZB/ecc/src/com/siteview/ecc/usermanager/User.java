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
	private String nIsUse;//�Ƿ�����
	private String nAdmin;//�Ƿ��ǹ���Ա�û�
	private String nIndex;//section����
	@Override
	public int compareTo(User o) {
		// TODO Auto-generated method stub
		return 0;
	}

}