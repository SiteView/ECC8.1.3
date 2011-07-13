package com.siteview.base.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * public VirtualItem(String itemId, String type, String svId, String zulName,
 * String zultype, HashMap<String, String> map)
 */
public class VirtualItem implements Serializable {
	private Map<String, String> m_map;
	private String m_type;
	private String m_sv_id;
	private String m_item_id;
	private String m_son_id;
	private Integer m_nextSonId = 1;

	public static final ZulItem ECCRoot = new ZulItem("ECCRoot", "ECC��", "");

	/**
	 * ��ͬ�����µ��豸��������������֯��һ����������
	 */
	public static final ZulItem				VirtualGroup		= new ZulItem("VirtualGroup", "������", "");
	
	public static final ZulItem				WholeView			= new ZulItem("WholeView", "������ͼ", "m_allview");
	public static final ZulItem				TreeView			= new ZulItem("TreeView", "��״��ͼ", "m_tree-it's_a_button_now-no_needing_anymore");
	public static final ZulItem				MonitorBrower		= new ZulItem("MonitorBrower", "��������", "m_monitorDisplay");// add by yuandong
	
	//"���������"���ܵ�Ȩ�ޣ�����Ա�����д˹��ܣ�������ǹ���Ա������Ҫ��һ��monitorӵ�б༭Ȩ�ޡ�������"������ͼ"Ȩ�ޡ����������д˹���
	public static final ZulItem				SetMonitor			= new ZulItem("SetMonitor", "���������", "m_allview");// add by yuandong
	public static final ZulItem				TopoView			= new ZulItem("TopoView", "������ͼ", "m_tuop");
	
	public static final ZulItem				Alert				= new ZulItem("Alert", "����", "");	
	// m_AlertRuleAdd, m_AlertRuleDel,m_AlertRuleEdit
//	public static final ZulItem				AlertRule			= new ZulItem("AlertRule", "��������", "m_AlertRuleAdd");//old//��������Ĺؼ��� ����ӱ�������Ĺؼ����ظ�
	public static final ZulItem				AlertRule			= new ZulItem("AlertRule", "��������", "m_AlertRule");//new
	public static final ZulItem				AlertLog			= new ZulItem("AlertLog", "������־", "m_alertLogs");
	public static final ZulItem				AlertStrategy		= new ZulItem("AlertStrategy", "��������", "m_alertStrategy");
	
	public static final ZulItem				Report				= new ZulItem("Report", "����", "");
	// m_reportlistAdd,m_reportlistDel,m_reportlistEdit
//	public static final ZulItem				ReportStatistic		= new ZulItem("ReportStatistic", "ͳ�Ʊ���", "m_statisticReportlistAdd");	//old
	public static final ZulItem				ReportStatistic		= new ZulItem("ReportStatistic", "ͳ�Ʊ���", "m_StatisticReport");	
//	public static final ZulItem				ReportTrend			= new ZulItem("ReportTrend", "���Ʊ���", "m_SetshowSystemReport");	//old
	public static final ZulItem				ReportTrend			= new ZulItem("ReportTrend", "���Ʊ���", "m_TrendReport");
	public static final ZulItem				ReportTopN			= new ZulItem("ReportTopN", "TopN����", "m_topnadd");
	public static final ZulItem				ReportStatus		= new ZulItem("ReportStatus","״̬ͳ�Ʊ���","m_ShowStatusReport");//qimin.xiong
	public static final ZulItem				ReportStatus2		= new ZulItem("ReportStatus2","״̬ͳ�Ʊ���2","m_ShowStatusReport2");//qimin.xiong
//	public static final ZulItem				ReportContrast		= new ZulItem("ReportContrast", "�Աȱ���", "m_SetshowSystemReport");	//old
	public static final ZulItem				ReportContrast		= new ZulItem("ReportContrast", "�Աȱ���", "m_ContrastReport");
//	public static final ZulItem				ReportTimeContrast	= new ZulItem("ReportTimeContrast", "ʱ�ζԱȱ���", "m_SetshowSystemReport");//old
	public static final ZulItem				ReportTimeContrast	= new ZulItem("ReportTimeContrast", "ʱ�ζԱȱ���", "m_TimeContrastReport");
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
	public static final ZulItem				m_userOtherPublic			= new ZulItem("m_userOtherPublic", "��ƷURL��ַ", "m_userOtherPublic");//change
	public static final ZulItem				SystemDiagnosis		= new ZulItem("SystemDiagnosis", "ϵͳ���", "m_system_diagnosis");
	public static final ZulItem				License				= new ZulItem("License", "������", "");
	public static final ZulItem				About				= new ZulItem("About", "��Ʒ����", "");
	
	public static final ZulItem				OtherUnknown		= new ZulItem("OtherUnknown", "����δ֪", "");

	public final static Map<String, ZulItem> allZulItem = new HashMap<String, ZulItem>();
	static {
		allZulItem.put("VirtualGroup", VirtualGroup);

		allZulItem.put("WholeView", WholeView);
		allZulItem.put("TreeView", TreeView);
		allZulItem.put("TopoView", TopoView);

		allZulItem.put("MonitorBrower", MonitorBrower);
		allZulItem.put("SetMonitor", SetMonitor);

		allZulItem.put("Alert", Alert);
		allZulItem.put("AlertRule", AlertRule);
		allZulItem.put("AlertLog", AlertLog);
		allZulItem.put("AlertStrategy", AlertStrategy);

		allZulItem.put("Report", Report);
		allZulItem.put("ReportStatistic", ReportStatistic);
		allZulItem.put("ReportTrend", ReportTrend);
		allZulItem.put("ReportTopN", ReportTopN);
		allZulItem.put("ReportStatus", ReportStatus);
		allZulItem.put("ReportStatus2", ReportStatus2);
		allZulItem.put("ReportContrast", ReportContrast);
		allZulItem.put("ReportTimeContrast", ReportTimeContrast);
		allZulItem.put("MonitorInfo", MonitorInfo);
		allZulItem.put("SysLogQuery", SysLogQuery);

		allZulItem.put("Set", Set);
		allZulItem.put("SetGeneral", SetGeneral);
		allZulItem.put("SetMail", SetMail);
		allZulItem.put("SetSms", SetSms);
		allZulItem.put("SetMaintain", SetMaintain);
		allZulItem.put("UserManager", UserManager);
		//allZulItem.put("importDataBase", importDataBase);

		allZulItem.put("Task", Task);
		allZulItem.put("TaskAbsolute", TaskAbsolute);
		allZulItem.put("TaskPeriod", TaskPeriod);
		allZulItem.put("TaskRelative", TaskRelative);//add by kaizhang

		allZulItem.put("SysLogSet", SysLogSet);
		allZulItem.put("BackupRestore", BackupRestore);
		allZulItem.put("OperateLog", OperateLog);
		allZulItem.put("m_userOtherPublic", m_userOtherPublic);
		allZulItem.put("SystemDiagnosis", SystemDiagnosis);
		
		allZulItem.put("License", License);
		allZulItem.put("About", About);
	}

	public VirtualItem(String itemId, String type, String svId, String zulName,
			String zulType, Map<String, String> map) {
		m_item_id = itemId;
		m_type = type;
		m_sv_id = svId;
		m_map = map;
		if (m_map != null) {
			m_map.put("zul_name", zulName);
			m_map.put("zul_type", zulType);
		}
	}

	/**
	 * ȡ������ڵ�����ͣ����ܵ�ֵΪ: VirtualView.INode(���ڵ�) �� VirtualView.Item(�̶��ڵ�)
	 */
	public String getType() {
		return m_type;
	}

	/**
	 * ���������ڵ�Ϊ���ڵ�, ��ȡ���� sv_id, ��ֵ����Ϊnull
	 */
	public String getSvId() {
		return m_sv_id;
	}

	/**
	 * ȡ������ڵ��id
	 */
	public String getItemId() {
		return m_item_id;
	}

	/**
	 * ȡ������ڵ㸸�׵�id
	 */
	public String getParentItemId() {
		if (m_item_id == null)
			return null;

		String id = new String(m_item_id);
		try {
			if (id.contains("."))
				return id.substring(0, id.lastIndexOf("."));
			else
				return "";
		} catch (Exception e) {
			return id;
		}
	}

	/**
	 * ׷��һ���ӽڵ� id
	 */
	public void addSonId(String id) {
		if (id == null || id.isEmpty())
			return;
		if (m_son_id == null)
			m_son_id = id + ",";
		else
			m_son_id += id + ",";

		try {
			String subid = id.substring(id.lastIndexOf(".") + 1);
			Integer index = new Integer(subid);
			m_nextSonId = index;
			++m_nextSonId;
		} catch (NumberFormatException e) {
		}
	}
	
	/**
	 * ɾ��һ���ӽڵ� id
	 */
	public void eraseSonId(String id) {
		if (id == null || id.isEmpty() || m_son_id == null || m_son_id.isEmpty())
			return;
		
		if(m_son_id.startsWith(id+","))
		{
			m_son_id= m_son_id.substring((id+",").length());
			if( m_son_id.isEmpty() )
				m_son_id= null;
		}
		else
			m_son_id= m_son_id.replace("," + id + ",", ",");
	}

	public Integer recommendNextSonId() {
		Integer index = m_nextSonId;
		++m_nextSonId;
		return index;
	}

	/**
	 * ȡ���ӽڵ� id �б�
	 */
	public ArrayList<String> getSonList() {
		ArrayList<String> a = new ArrayList<String>();
		if (m_son_id == null || m_son_id.isEmpty())
			return a;

		String[] s = m_son_id.split(",");
		int size = s.length;
		for (int i = 0; i < size; ++i)
			a.add(s[i]);
		return a;
	}

	/**
	 * ���������ڵ�Ϊ�̶��ڵ�,��ȡ��������, ��ֵ����Ϊnull
	 */
	public Map<String, String> getItemData() {
		return m_map;
	}

	/**
	 * ȡ�ù̶��ڵ������е�ĳ��ֵ
	 */
	public String getItemDataByKey(String key) {
		if (m_map == null)
			return "";
		String name = m_map.get(key);
		if (name == null)
			return "";
		return name;
	}

	/**
	 * �ýڵ��Ƿ�����Ϊ ���Զ���������Ӽ������
	 */
	public boolean isWithAllSubMonitor() {
		return getItemDataByKey("withAllSubMonitor").equals("true");
	}

	/**
	 * ȡ�ù̶��ڵ������е� ZulName ֵ
	 */
	public String getItemDataZulName() {
		return getItemDataByKey("zul_name");
	}

	/**
	 *ȡ�ù̶��ڵ������е� ZulType��ÿ���ڵ�ĸ�ֵ����ͬ
	 */
	public String getItemDataZulType() {
		return getItemDataByKey("zul_type");
	}
	
}
