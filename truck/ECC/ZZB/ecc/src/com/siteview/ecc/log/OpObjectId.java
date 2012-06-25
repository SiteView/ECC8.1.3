package com.siteview.ecc.log;

public class OpObjectId
{
	public static final OpObjectId	login				= new OpObjectId("0", "登录");
	public static final OpObjectId	se					= new OpObjectId("1", "SE");
	public static final OpObjectId	group				= new OpObjectId("2", "组");
	public static final OpObjectId	entity				= new OpObjectId("3", "设备");
	public static final OpObjectId	monitor				= new OpObjectId("4", "监测器");
	public static final OpObjectId	general_set			= new OpObjectId("5", "基本设置");
	public static final OpObjectId	customer_name		= new OpObjectId("6", "客户名称");
	public static final OpObjectId	server_name			= new OpObjectId("7", "监测服务器名称");
	public static final OpObjectId	alert_rule			= new OpObjectId("8", "报警规则");
	public static final OpObjectId	tupo_view			= new OpObjectId("9", "拓扑视图");
	public static final OpObjectId	syslog_set			= new OpObjectId("10", "SysLog设置");
	public static final OpObjectId	user_manage			= new OpObjectId("11", "用户管理");
	public static final OpObjectId	mail_set			= new OpObjectId("12", "邮件设置");
	public static final OpObjectId	time_task			= new OpObjectId("13", "时间段任务计划");
	public static final OpObjectId	absolute_task		= new OpObjectId("14", "绝对时间任务计划");
	public static final OpObjectId	sms_set				= new OpObjectId("15", "短信设置");
	public static final OpObjectId	topn_report			= new OpObjectId("16", "TopN报告");
	public static final OpObjectId	statistic_report	= new OpObjectId("17", "统计报告");
	public static final OpObjectId	duty_set			= new OpObjectId("18", "值班表配置");
	public static final OpObjectId	virtual_groupnode	= new OpObjectId("19", "虚拟组");
	public static final OpObjectId	relative_task		= new OpObjectId("20", "相对时间任务计划");
	public static final OpObjectId  alert_strategy      = new OpObjectId("21", "报警策略");
	public static final OpObjectId  monitor_browser     = new OpObjectId("22", "监测器浏览");
	public static final OpObjectId  monitor_set         = new OpObjectId("23", "监测器设置");
	public static final OpObjectId  email_template      = new OpObjectId("24", "邮件模板");
	public static final OpObjectId  message_template    = new OpObjectId("25", "短信模板");
	public static final OpObjectId  status_report       = new OpObjectId("26", "状态统计报告");//"ReportStatus","状态统计报告","m_ShowStatusReport");//qimin.xiong
    public static final OpObjectId  system_diagnosis    = new OpObjectId("27", "系统诊断");//"SystemDiagnosis", "系统诊断", "m_system_diagnosis"
    public static final OpObjectId  ipchoose		    = new OpObjectId("28", "IP地址");//"SystemDiagnosis", "系统诊断", "m_system_diagnosis"
    public static final OpObjectId  topn_set    		= new OpObjectId("29", "TopN设置");
    public String id;
	public String name;
	
	public OpObjectId(String id, String name)
	{
		this.id= id;
		this.name= name;
	}
}
