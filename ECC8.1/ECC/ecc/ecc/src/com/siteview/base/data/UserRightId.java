package com.siteview.base.data;

import java.io.Serializable;


//�������еĹؼ��ֶ��Ǵ� cs �汾�� ���������ģ� �кܶ�Ĺؼ��� �ظ���Ҳ��һЩȱʧ��ʹ�øùؼ��� �ֵ� ���ܻ����һЩbug��

public class UserRightId implements Serializable {
	


	public static final String ECCRoot					= "";//ECC��
	
	public static final String VirtualGroup             = "";//������
	
	public static final String WholeView 				= "m_allview";//������ͼ
	public static final String TreeView 				= "m_tree-it's_a_button_now-no_needing_anymore";//��״��ͼ
	public static final String MonitorBrower 			= "m_monitorDisplay";//��������
	
	public static final String SetMonitor 				= "m_allview";//��������� /*��������ͼ�Ĺؼ����ظ�*/
	public static final String TopoView					= "m_tuop";//������ͼ
//	public static final String AlertRule				= "m_AlertRuleAdd";//��������/*����ӱ�������Ĺؼ����ظ�*/ //old
	public static final String AlertRule				= "m_AlertRule";//�������� //new
	public static final String AlertLog 				= "m_alertLogs";//������־
	public static final String AlertStrategy 			= "m_alertStrategy";//��������
	
//	public static final String ReportStatistic  	 	= "m_reportlistAdd";//ͳ�Ʊ���/*����ӱ���Ĺؼ����ظ�*/ //old
	public static final String ReportStatistic  	 	= "m_StatisticReport";//ͳ�Ʊ���
//	public static final String ReportTrend 				= "m_SetshowSystemReport";//���Ʊ���/*�ؼ����ظ�*/ //old
	public static final String ReportTrend 				= "m_TrendReport";//���Ʊ���
	public static final String ReportTopN 				= "m_topnadd";//TopN����
	public static final String ReportStatus				= "m_ShowStatusReport";//״̬ͳ�Ʊ��� new add
	public static final String ReportStatus2			= "m_ShowStatusReport";//״̬ͳ�Ʊ��� new add
//	public static final String ReportContrast			= "m_SetshowSystemReport";//�Աȱ���/*�ؼ����ظ�*/
	public static final String ReportContrast			= "m_ContrastReport";//�Աȱ���/*�ؼ����ظ�*/
//	public static final String ReportTimeContrast		= "m_SetshowSystemReport";//ʱ�ζԱȱ���/*�ؼ����ظ�*/ //old	
	public static final String ReportTimeContrast		= "m_TimeContrastReport";//ʱ�ζԱȱ���
	public static final String MonitorInfo 				= "m_logshower";//�������Ϣ����
	public static final String SysLogQuery 				= "m_syslogquery";//SysLog��ѯ
	
	public static final String SetGeneral 				= "m_general";//��������
	public static final String SetMail 					= "m_mailsetting";//�ʼ�����
	public static final String SetSms 					= "m_smssetting";//��������
	public static final String SetMaintain 				= "m_maintainsetting";//ֵ������� /*�����ӵ�*/
	public static final String UserManager 				= "m_usermanager";//�û����� /*�����ӵ�*/
	public static final String TaskAbsolute 			= "m_taskabsolute";//����ʱ������ƻ�/*�����ӵ�*/
	public static final String TaskPeriod 				= "m_taskperiod";//ʱ�������ƻ�/*�����ӵ�*/
	public static final String TaskRelative 			= "m_taskrelative";//���ʱ������ƻ�/*�����ӵ�*/
	public static final String SysLogSet 				= "m_syslogset";//SysLog����/*�����ӵ�*/
	public static final String OperateLog 				= "m_operatelog";//�û�������־/*�����ӵ�*/
	public static final String m_userOtherPublic 				= "m_userOtherPublic";//�����Ʒ
	public static final String SystemDiagnosis 			= "m_system_diagnosis";//ϵͳ���/*�����ӵ�*/
	
	public static final String DoAlertRuleAdd 			= "m_AlertRuleAdd";//��ӱ�������
	public static final String DoAlertRuleDel 			= "m_AlertRuleDel";//ɾ����������
	public static final String DoAlertRuleEdit 			= "m_AlertRuleEdit";//�༭��������
	
/*	public static final String DoReportlistAdd 			= "m_reportlistAdd";//��ӱ���
	public static final String DoReportlistDel			= "m_reportlistDel";//ɾ������
	public static final String DoReportlistEdit 		= "m_reportlistEdit";//�༭����
*/
	//ͳ�Ʊ���Ȩ������
	public static final String DoStatisticReportlistAdd 			= "m_statisticReportlistAdd";//���ͳ�Ʊ���
	public static final String DoStatisticReportlistDel				= "m_statisticReportlistDel";//ɾ��ͳ�Ʊ���
	public static final String DoStatisticReportlistEdit 			= "m_statisticReportlistEdit";//�༭ͳ�Ʊ���

	//TopN����Ȩ������
	public static final String DoTopNReportlistAdd 					= "m_topNReportlistAdd";//���ͳ�Ʊ���
	public static final String DoTopNReportlistDel					= "m_topNReportlistDel";//ɾ��ͳ�Ʊ���
	public static final String DoTopNReportlistEdit 				= "m_topNReportlistEdit";//�༭ͳ�Ʊ���
/*
	public static final ZulItem				VirtualGroup		= new ZulItem("VirtualGroup", "������", "");
	
	public static final ZulItem				WholeView			= new ZulItem("WholeView", "������ͼ", "m_allview");
	public static final ZulItem				TreeView			= new ZulItem("TreeView", "��״��ͼ", "m_tree-it's_a_button_now-no_needing_anymore");
	public static final ZulItem				MonitorBrower		= new ZulItem("MonitorBrower", "��������", "m_monitorDisplay");// add by yuandong
	
	//"���������"���ܵ�Ȩ�ޣ�����Ա�����д˹��ܣ�������ǹ���Ա������Ҫ��һ��monitorӵ�б༭Ȩ�ޡ�������"������ͼ"Ȩ�ޡ����������д˹���
	public static final ZulItem				SetMonitor			= new ZulItem("SetMonitor", "���������", "m_allview");// add by yuandong
	public static final ZulItem				TopoView			= new ZulItem("TopoView", "������ͼ", "m_tuop");
	
	public static final ZulItem				Alert				= new ZulItem("Alert", "����", "");	
	// m_AlertRuleAdd, m_AlertRuleDel,m_AlertRuleEdit
	public static final ZulItem				AlertRule			= new ZulItem("AlertRule", "��������", "m_AlertRuleAdd");
	public static final ZulItem				AlertLog			= new ZulItem("AlertLog", "������־", "m_alertLogs");
	
	public static final ZulItem				Report				= new ZulItem("Report", "����", "");
	// m_reportlistAdd,m_reportlistDel,m_reportlistEdit
	public static final ZulItem				ReportStatistic		= new ZulItem("ReportStatistic", "ͳ�Ʊ���", "m_reportlistAdd");	
	public static final ZulItem				ReportTrend			= new ZulItem("ReportTrend", "���Ʊ���", "m_SetshowSystemReport");
	public static final ZulItem				ReportTopN			= new ZulItem("ReportTopN", "TopN����", "m_topnadd");
	public static final ZulItem				ReportContrast		= new ZulItem("ReportContrast", "�Աȱ���", "m_SetshowSystemReport");
	public static final ZulItem				ReportTimeContrast	= new ZulItem("ReportTimeContrast", "ʱ�ζԱȱ���", "m_SetshowSystemReport");
	public static final ZulItem				MonitorInfo			= new ZulItem("MonitorInfo", "�������Ϣ����", "m_logshower");
	public static final ZulItem				SysLogQuery			= new ZulItem("SysLogQuery", "SysLog��ѯ", "m_syslogquery");//change
	
	public static final ZulItem				Set					= new ZulItem("Set", "����", "");
	public static final ZulItem				SetGeneral			= new ZulItem("SetGeneral", "��������", "m_general");
	public static final ZulItem				SetMail				= new ZulItem("SetMail", "�ʼ�����", "m_mailsetting");
	public static final ZulItem				SetSms				= new ZulItem("SetSms", "��������", "m_smssetting");
	public static final ZulItem				SetMaintain			= new ZulItem("SetMaintain", "ֵ�������", "m_maintainsetting");//change
	public static final ZulItem				UserManager			= new ZulItem("UserManager", "�û�����", "m_usermanager");//change
	//public static final ZulItem				importDataBase		= new ZulItem("importDataBase", "���ݿ�����", "only_authorize_to_admin");
	
	public static final ZulItem				Task				= new ZulItem("Task", "����ƻ�", "");//change
	public static final ZulItem				TaskAbsolute		= new ZulItem("TaskAbsolute", "����ʱ������ƻ�", "m_taskabsolute");//change
	public static final ZulItem				TaskPeriod			= new ZulItem("TaskPeriod", "ʱ�������ƻ�", "m_taskperiod");//change
	public static final ZulItem				TaskRelative		= new ZulItem("TaskRelative", "���ʱ������ƻ�", "m_taskrelative");//change
	public static final ZulItem				SysLogSet			= new ZulItem("SysLogSet", "SysLog����", "m_syslogset");//change
	public static final ZulItem				BackupRestore		= new ZulItem("BackupRestore", "���ݺͻָ�", "only_authorize_to_admin-it_is_not_supoort");
	public static final ZulItem				OperateLog			= new ZulItem("OperateLog", "�û�������־", "m_operatelog");//change
	public static final ZulItem				License				= new ZulItem("License", "������", "");
	public static final ZulItem				About				= new ZulItem("About", "��Ʒ����", "");
	
	public static final ZulItem				OtherUnknown		= new ZulItem("OtherUnknown", "����δ֪", "");
*/

}
