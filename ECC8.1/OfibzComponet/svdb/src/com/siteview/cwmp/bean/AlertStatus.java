package com.siteview.cwmp.bean;

public enum AlertStatus {
	NoProcess,				//处理
	Revert,					//已恢复
	Warning,				//警告
	Processed;				//已处理
	
	public static AlertStatus getAlertStatus(String name){
		if (Revert.toString().equals(name)) return Revert;
		if (Warning.toString().equals(name)) return Warning;
		if (Processed.toString().equals(name)) return Processed;
		return NoProcess;
	}
}
