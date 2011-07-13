package com.siteview.ecc.log;

public class OpObjectId
{
	public static final OpObjectId	login				= new OpObjectId("0", "��¼");
	public static final OpObjectId	se					= new OpObjectId("1", "SE");
	public static final OpObjectId	group				= new OpObjectId("2", "��");
	public static final OpObjectId	entity				= new OpObjectId("3", "�豸");
	public static final OpObjectId	monitor				= new OpObjectId("4", "�����");
	public static final OpObjectId	general_set			= new OpObjectId("5", "��������");
	public static final OpObjectId	customer_name		= new OpObjectId("6", "�ͻ�����");
	public static final OpObjectId	server_name			= new OpObjectId("7", "������������");
	public static final OpObjectId	alert_rule			= new OpObjectId("8", "��������");
	public static final OpObjectId	tupo_view			= new OpObjectId("9", "������ͼ");
	public static final OpObjectId	syslog_set			= new OpObjectId("10", "SysLog����");
	public static final OpObjectId	user_manage			= new OpObjectId("11", "�û�����");
	public static final OpObjectId	mail_set			= new OpObjectId("12", "�ʼ�����");
	public static final OpObjectId	time_task			= new OpObjectId("13", "ʱ�������ƻ�");
	public static final OpObjectId	absolute_task		= new OpObjectId("14", "����ʱ������ƻ�");
	public static final OpObjectId	sms_set				= new OpObjectId("15", "��������");
	public static final OpObjectId	topn_report			= new OpObjectId("16", "TopN����");
	public static final OpObjectId	statistic_report	= new OpObjectId("17", "ͳ�Ʊ���");
	public static final OpObjectId	duty_set			= new OpObjectId("18", "ֵ�������");
	public static final OpObjectId	virtual_groupnode	= new OpObjectId("19", "������");
	public static final OpObjectId	relative_task		= new OpObjectId("20", "���ʱ������ƻ�");
	public static final OpObjectId  alert_strategy      = new OpObjectId("21", "��������");
	public static final OpObjectId  monitor_browser     = new OpObjectId("22", "��������");
	public static final OpObjectId  monitor_set         = new OpObjectId("23", "���������");
	public static final OpObjectId  email_template      = new OpObjectId("24", "�ʼ�ģ��");
	public static final OpObjectId  message_template    = new OpObjectId("25", "����ģ��");
	public static final OpObjectId  status_report       = new OpObjectId("26", "״̬ͳ�Ʊ���");//"ReportStatus","״̬ͳ�Ʊ���","m_ShowStatusReport");//qimin.xiong
    public static final OpObjectId  system_diagnosis    = new OpObjectId("27", "ϵͳ���");//"SystemDiagnosis", "ϵͳ���", "m_system_diagnosis"
    public static final OpObjectId  ipchoose		    = new OpObjectId("28", "IP��ַ");//"SystemDiagnosis", "ϵͳ���", "m_system_diagnosis"
    public static final OpObjectId  topn_set    		= new OpObjectId("29", "TopN����");
    public String id;
	public String name;
	
	public OpObjectId(String id, String name)
	{
		this.id= id;
		this.name= name;
	}
}
