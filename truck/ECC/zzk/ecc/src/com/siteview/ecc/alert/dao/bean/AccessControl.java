package com.siteview.ecc.alert.dao.bean;

public class AccessControl {
	private boolean IsIDCUser;
	private boolean LimitServer;
	private String SVDBServer;
	private boolean LimitUser;
	private String UserID;

	/**
	 * 是否是IDC用户
	 * 
	 * @return 是/否
	 */
	public boolean isIDCUser() {
		return IsIDCUser;
	}

	/**
	 * 设置是否是IDC用户
	 * 
	 * @param isIDCUser
	 *            是/否
	 */
	public void setIDCUser(boolean isIDCUser) {
		IsIDCUser = isIDCUser;
	}

	/**
	 * 是否要定向SVDB服务器地址(主要是针对IDC用户)
	 * 
	 * @return 是/否
	 */
	public boolean isLimitServer() {
		return LimitServer;
	}

	/**
	 * 设置是否要定向SVDB服务器地址(主要是针对IDC用户)
	 * 
	 * @param limitServer
	 *            是/否
	 */
	public void setLimitServer(boolean limitServer) {
		LimitServer = limitServer;
	}

	/**
	 * 定向SVDB服务器地址(主要是针对IDC用户)
	 * 
	 * @return 定向SVDB服务器地址
	 */
	public String getSVDBServer() {
		return SVDBServer;
	}

	/**
	 * 设置定向SVDB服务器地址(主要是针对IDC用户)
	 * 
	 * @param server
	 *            定向SVDB服务器地址
	 */
	public void setSVDBServer(String server) {
		SVDBServer = server;
	}

	/**
	 * 是否要限定用户(主要是针对IDC用户)
	 * 
	 * @return 是/否
	 */
	public boolean isLimitUser() {
		return LimitUser;
	}

	/**
	 * 设置是否要限定用户(主要是针对IDC用户)
	 * 
	 * @param limitUser
	 *            是/否
	 */
	public void setLimitUser(boolean limitUser) {
		LimitUser = limitUser;
	}

	/**
	 * 用户(主要是针对IDC用户)
	 * 
	 * @return 用户ID
	 */
	public String getUserID() {
		return UserID;
	}

	/**
	 * 设置用户(主要是针对IDC用户)
	 * 
	 * @param userID
	 *            用户ID
	 */
	public void setUserID(String userID) {
		UserID = userID;
	}

}
