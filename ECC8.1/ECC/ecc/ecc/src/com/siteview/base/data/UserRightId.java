package com.siteview.base.data;

import java.io.Serializable;


//下面所列的关键字都是从 cs 版本中 拷贝过来的， 有很多的关键字 重复，也有一些缺失。使用该关键字 字典 可能会产生一些bug。

public class UserRightId implements Serializable {
	


	public static final String ECCRoot					= "";//ECC根
	
	public static final String VirtualGroup             = "";//虚拟组
	
	public static final String WholeView 				= "m_allview";//整体视图
	public static final String TreeView 				= "m_tree-it's_a_button_now-no_needing_anymore";//树状视图
	public static final String MonitorBrower 			= "m_monitorDisplay";//监测器浏览
	
	public static final String SetMonitor 				= "m_allview";//监测器设置 /*和整体视图的关键字重复*/
	public static final String TopoView					= "m_tuop";//拓扑视图
//	public static final String AlertRule				= "m_AlertRuleAdd";//报警规则/*和添加报警规则的关键字重复*/ //old
	public static final String AlertRule				= "m_AlertRule";//报警规则 //new
	public static final String AlertLog 				= "m_alertLogs";//报警日志
	public static final String AlertStrategy 			= "m_alertStrategy";//报警策略
	
//	public static final String ReportStatistic  	 	= "m_reportlistAdd";//统计报告/*和添加报告的关键字重复*/ //old
	public static final String ReportStatistic  	 	= "m_StatisticReport";//统计报告
//	public static final String ReportTrend 				= "m_SetshowSystemReport";//趋势报告/*关键字重复*/ //old
	public static final String ReportTrend 				= "m_TrendReport";//趋势报告
	public static final String ReportTopN 				= "m_topnadd";//TopN报告
	public static final String ReportStatus				= "m_ShowStatusReport";//状态统计报告 new add
	public static final String ReportStatus2			= "m_ShowStatusReport";//状态统计报告 new add
//	public static final String ReportContrast			= "m_SetshowSystemReport";//对比报告/*关键字重复*/
	public static final String ReportContrast			= "m_ContrastReport";//对比报告/*关键字重复*/
//	public static final String ReportTimeContrast		= "m_SetshowSystemReport";//时段对比报告/*关键字重复*/ //old	
	public static final String ReportTimeContrast		= "m_TimeContrastReport";//时段对比报告
	public static final String MonitorInfo 				= "m_logshower";//监测器信息报告
	public static final String SysLogQuery 				= "m_syslogquery";//SysLog查询
	
	public static final String SetGeneral 				= "m_general";//基本设置
	public static final String SetMail 					= "m_mailsetting";//邮件设置
	public static final String SetSms 					= "m_smssetting";//短信设置
	public static final String SetMaintain 				= "m_maintainsetting";//值班表设置 /*新增加的*/
	public static final String UserManager 				= "m_usermanager";//用户管理 /*新增加的*/
	public static final String TaskAbsolute 			= "m_taskabsolute";//绝对时间任务计划/*新增加的*/
	public static final String TaskPeriod 				= "m_taskperiod";//时间段任务计划/*新增加的*/
	public static final String TaskRelative 			= "m_taskrelative";//相对时间任务计划/*新增加的*/
	public static final String SysLogSet 				= "m_syslogset";//SysLog设置/*新增加的*/
	public static final String OperateLog 				= "m_operatelog";//用户操作日志/*新增加的*/
	public static final String m_userOtherPublic 				= "m_userOtherPublic";//另外产品
	public static final String SystemDiagnosis 			= "m_system_diagnosis";//系统诊断/*新增加的*/
	
	public static final String DoAlertRuleAdd 			= "m_AlertRuleAdd";//添加报警规则
	public static final String DoAlertRuleDel 			= "m_AlertRuleDel";//删除报警规则
	public static final String DoAlertRuleEdit 			= "m_AlertRuleEdit";//编辑报警规则
	
/*	public static final String DoReportlistAdd 			= "m_reportlistAdd";//添加报告
	public static final String DoReportlistDel			= "m_reportlistDel";//删除报告
	public static final String DoReportlistEdit 		= "m_reportlistEdit";//编辑报告
*/
	//统计报告权限设置
	public static final String DoStatisticReportlistAdd 			= "m_statisticReportlistAdd";//添加统计报告
	public static final String DoStatisticReportlistDel				= "m_statisticReportlistDel";//删除统计报告
	public static final String DoStatisticReportlistEdit 			= "m_statisticReportlistEdit";//编辑统计报告

	//TopN报告权限设置
	public static final String DoTopNReportlistAdd 					= "m_topNReportlistAdd";//添加统计报告
	public static final String DoTopNReportlistDel					= "m_topNReportlistDel";//删除统计报告
	public static final String DoTopNReportlistEdit 				= "m_topNReportlistEdit";//编辑统计报告
/*
	public static final ZulItem				VirtualGroup		= new ZulItem("VirtualGroup", "虚拟组", "");
	
	public static final ZulItem				WholeView			= new ZulItem("WholeView", "整体视图", "m_allview");
	public static final ZulItem				TreeView			= new ZulItem("TreeView", "树状视图", "m_tree-it's_a_button_now-no_needing_anymore");
	public static final ZulItem				MonitorBrower		= new ZulItem("MonitorBrower", "监测器浏览", "m_monitorDisplay");// add by yuandong
	
	//"监测器设置"功能的权限：管理员总是有此功能，如果不是管理员则至少要对一个monitor拥有编辑权限、而且有"整体视图"权限————才有此功能
	public static final ZulItem				SetMonitor			= new ZulItem("SetMonitor", "监测器设置", "m_allview");// add by yuandong
	public static final ZulItem				TopoView			= new ZulItem("TopoView", "拓扑视图", "m_tuop");
	
	public static final ZulItem				Alert				= new ZulItem("Alert", "报警", "");	
	// m_AlertRuleAdd, m_AlertRuleDel,m_AlertRuleEdit
	public static final ZulItem				AlertRule			= new ZulItem("AlertRule", "报警规则", "m_AlertRuleAdd");
	public static final ZulItem				AlertLog			= new ZulItem("AlertLog", "报警日志", "m_alertLogs");
	
	public static final ZulItem				Report				= new ZulItem("Report", "报表", "");
	// m_reportlistAdd,m_reportlistDel,m_reportlistEdit
	public static final ZulItem				ReportStatistic		= new ZulItem("ReportStatistic", "统计报告", "m_reportlistAdd");	
	public static final ZulItem				ReportTrend			= new ZulItem("ReportTrend", "趋势报告", "m_SetshowSystemReport");
	public static final ZulItem				ReportTopN			= new ZulItem("ReportTopN", "TopN报告", "m_topnadd");
	public static final ZulItem				ReportContrast		= new ZulItem("ReportContrast", "对比报告", "m_SetshowSystemReport");
	public static final ZulItem				ReportTimeContrast	= new ZulItem("ReportTimeContrast", "时段对比报告", "m_SetshowSystemReport");
	public static final ZulItem				MonitorInfo			= new ZulItem("MonitorInfo", "监测器信息报告", "m_logshower");
	public static final ZulItem				SysLogQuery			= new ZulItem("SysLogQuery", "SysLog查询", "m_syslogquery");//change
	
	public static final ZulItem				Set					= new ZulItem("Set", "设置", "");
	public static final ZulItem				SetGeneral			= new ZulItem("SetGeneral", "基本设置", "m_general");
	public static final ZulItem				SetMail				= new ZulItem("SetMail", "邮件设置", "m_mailsetting");
	public static final ZulItem				SetSms				= new ZulItem("SetSms", "短信设置", "m_smssetting");
	public static final ZulItem				SetMaintain			= new ZulItem("SetMaintain", "值班表设置", "m_maintainsetting");//change
	public static final ZulItem				UserManager			= new ZulItem("UserManager", "用户管理", "m_usermanager");//change
	//public static final ZulItem				importDataBase		= new ZulItem("importDataBase", "数据库设置", "only_authorize_to_admin");
	
	public static final ZulItem				Task				= new ZulItem("Task", "任务计划", "");//change
	public static final ZulItem				TaskAbsolute		= new ZulItem("TaskAbsolute", "绝对时间任务计划", "m_taskabsolute");//change
	public static final ZulItem				TaskPeriod			= new ZulItem("TaskPeriod", "时间段任务计划", "m_taskperiod");//change
	public static final ZulItem				TaskRelative		= new ZulItem("TaskRelative", "相对时间任务计划", "m_taskrelative");//change
	public static final ZulItem				SysLogSet			= new ZulItem("SysLogSet", "SysLog设置", "m_syslogset");//change
	public static final ZulItem				BackupRestore		= new ZulItem("BackupRestore", "备份和恢复", "only_authorize_to_admin-it_is_not_supoort");
	public static final ZulItem				OperateLog			= new ZulItem("OperateLog", "用户操作日志", "m_operatelog");//change
	public static final ZulItem				License				= new ZulItem("License", "软件许可", "");
	public static final ZulItem				About				= new ZulItem("About", "产品介绍", "");
	
	public static final ZulItem				OtherUnknown		= new ZulItem("OtherUnknown", "其他未知", "");
*/

}
