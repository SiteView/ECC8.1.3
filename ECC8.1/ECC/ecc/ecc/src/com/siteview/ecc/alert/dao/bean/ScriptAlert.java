package com.siteview.ecc.alert.dao.bean;


public class ScriptAlert extends BaseAlert{
    private String scriptFile;
    private String scriptParam;
    private String scriptServer;
    private String serverID;
    private String alertPloy;
    
	public BaseAlert getBaseInfo() {
		return this;
	}
	public String getScriptFile() {
		return scriptFile;
	}
	public String getScriptParam() {
		return scriptParam;
	}
	public String getScriptServer() {
		return scriptServer;
	}
	public String getServerID() {
		return serverID;
	}
	public void setBaseInfo(BaseAlert baseInfo) {
		this.setValues(baseInfo);
	}
	public void setScriptFile(String scriptFile) {
		this.scriptFile = scriptFile;
	}
	public void setScriptParam(String scriptParam) {
		this.scriptParam = scriptParam;
	}
	public void setScriptServer(String scriptServer) {
		this.scriptServer = scriptServer;
	}
	public void setServerID(String serverID) {
		this.serverID = serverID;
	}
	public String getAlertPloy() {
		return alertPloy;
	}
	public void setAlertPloy(String alertPloy) {
		this.alertPloy = alertPloy;
	}

}
