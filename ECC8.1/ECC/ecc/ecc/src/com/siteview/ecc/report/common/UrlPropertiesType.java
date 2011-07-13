package com.siteview.ecc.report.common;

import com.siteview.actions.EccActionManager;

public enum UrlPropertiesType {
	WholeView,
	se,
	group,
	entity,
	monitor,
	TreeView,
	
	MonitorBrower,
	SetMonitor,
	
	Alert,
	AlertRule,
	AlertLog,
	
	About,
	License,
	OperateLog,
	BackupRestore,
	SysLogSet,
	
	Report,
	ReportStatistic,
	ReportTrend,
	ReportTopN,
	ReportContrast,
	MonitorInfo,
	ReportTimeContrast,
	SysLogQuery,
	
	Set,
	SetGeneral,
	SetMail,
	SetSms,
	SetMaintain,
	UserManager,
	Task,
	TaskAbsolute,
	TaskPeriod,
	TaskRelative,
	user_grant,
	
	importDataBase;
	
	public String toString(){
		return super.toString();
	}
}
