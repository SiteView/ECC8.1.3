package com.siteview.ecc.alert.dao.bean;

public class AccessControl {
	private boolean IsIDCUser;
	private boolean LimitServer;
	private String SVDBServer;
	private boolean LimitUser;
	private String UserID;

	/**
	 * �Ƿ���IDC�û�
	 * 
	 * @return ��/��
	 */
	public boolean isIDCUser() {
		return IsIDCUser;
	}

	/**
	 * �����Ƿ���IDC�û�
	 * 
	 * @param isIDCUser
	 *            ��/��
	 */
	public void setIDCUser(boolean isIDCUser) {
		IsIDCUser = isIDCUser;
	}

	/**
	 * �Ƿ�Ҫ����SVDB��������ַ(��Ҫ�����IDC�û�)
	 * 
	 * @return ��/��
	 */
	public boolean isLimitServer() {
		return LimitServer;
	}

	/**
	 * �����Ƿ�Ҫ����SVDB��������ַ(��Ҫ�����IDC�û�)
	 * 
	 * @param limitServer
	 *            ��/��
	 */
	public void setLimitServer(boolean limitServer) {
		LimitServer = limitServer;
	}

	/**
	 * ����SVDB��������ַ(��Ҫ�����IDC�û�)
	 * 
	 * @return ����SVDB��������ַ
	 */
	public String getSVDBServer() {
		return SVDBServer;
	}

	/**
	 * ���ö���SVDB��������ַ(��Ҫ�����IDC�û�)
	 * 
	 * @param server
	 *            ����SVDB��������ַ
	 */
	public void setSVDBServer(String server) {
		SVDBServer = server;
	}

	/**
	 * �Ƿ�Ҫ�޶��û�(��Ҫ�����IDC�û�)
	 * 
	 * @return ��/��
	 */
	public boolean isLimitUser() {
		return LimitUser;
	}

	/**
	 * �����Ƿ�Ҫ�޶��û�(��Ҫ�����IDC�û�)
	 * 
	 * @param limitUser
	 *            ��/��
	 */
	public void setLimitUser(boolean limitUser) {
		LimitUser = limitUser;
	}

	/**
	 * �û�(��Ҫ�����IDC�û�)
	 * 
	 * @return �û�ID
	 */
	public String getUserID() {
		return UserID;
	}

	/**
	 * �����û�(��Ҫ�����IDC�û�)
	 * 
	 * @param userID
	 *            �û�ID
	 */
	public void setUserID(String userID) {
		UserID = userID;
	}

}
