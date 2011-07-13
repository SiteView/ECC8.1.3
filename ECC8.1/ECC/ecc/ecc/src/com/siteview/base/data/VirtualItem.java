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

	public static final ZulItem ECCRoot = new ZulItem("ECCRoot", "ECC根", "");

	/**
	 * 不同的组下的设备或监测器，可以组织在一个虚拟组下
	 */
	public static final ZulItem				VirtualGroup		= new ZulItem("VirtualGroup", "虚拟组", "");
	
	public static final ZulItem				WholeView			= new ZulItem("WholeView", "整体视图", "m_allview");
	public static final ZulItem				TreeView			= new ZulItem("TreeView", "树状视图", "m_tree-it's_a_button_now-no_needing_anymore");
	public static final ZulItem				MonitorBrower		= new ZulItem("MonitorBrower", "监测器浏览", "m_monitorDisplay");// add by yuandong
	
	//"监测器设置"功能的权限：管理员总是有此功能，如果不是管理员则至少要对一个monitor拥有编辑权限、而且有"整体视图"权限————才有此功能
	public static final ZulItem				SetMonitor			= new ZulItem("SetMonitor", "监测器设置", "m_allview");// add by yuandong
	public static final ZulItem				TopoView			= new ZulItem("TopoView", "拓扑视图", "m_tuop");
	
	public static final ZulItem				Alert				= new ZulItem("Alert", "报警", "");	
	// m_AlertRuleAdd, m_AlertRuleDel,m_AlertRuleEdit
//	public static final ZulItem				AlertRule			= new ZulItem("AlertRule", "报警规则", "m_AlertRuleAdd");//old//报警规则的关键字 和添加报警规则的关键字重复
	public static final ZulItem				AlertRule			= new ZulItem("AlertRule", "报警规则", "m_AlertRule");//new
	public static final ZulItem				AlertLog			= new ZulItem("AlertLog", "报警日志", "m_alertLogs");
	public static final ZulItem				AlertStrategy		= new ZulItem("AlertStrategy", "报警策略", "m_alertStrategy");
	
	public static final ZulItem				Report				= new ZulItem("Report", "报表", "");
	// m_reportlistAdd,m_reportlistDel,m_reportlistEdit
//	public static final ZulItem				ReportStatistic		= new ZulItem("ReportStatistic", "统计报告", "m_statisticReportlistAdd");	//old
	public static final ZulItem				ReportStatistic		= new ZulItem("ReportStatistic", "统计报告", "m_StatisticReport");	
//	public static final ZulItem				ReportTrend			= new ZulItem("ReportTrend", "趋势报告", "m_SetshowSystemReport");	//old
	public static final ZulItem				ReportTrend			= new ZulItem("ReportTrend", "趋势报告", "m_TrendReport");
	public static final ZulItem				ReportTopN			= new ZulItem("ReportTopN", "TopN报告", "m_topnadd");
	public static final ZulItem				ReportStatus		= new ZulItem("ReportStatus","状态统计报告","m_ShowStatusReport");//qimin.xiong
	public static final ZulItem				ReportStatus2		= new ZulItem("ReportStatus2","状态统计报告2","m_ShowStatusReport2");//qimin.xiong
//	public static final ZulItem				ReportContrast		= new ZulItem("ReportContrast", "对比报告", "m_SetshowSystemReport");	//old
	public static final ZulItem				ReportContrast		= new ZulItem("ReportContrast", "对比报告", "m_ContrastReport");
//	public static final ZulItem				ReportTimeContrast	= new ZulItem("ReportTimeContrast", "时段对比报告", "m_SetshowSystemReport");//old
	public static final ZulItem				ReportTimeContrast	= new ZulItem("ReportTimeContrast", "时段对比报告", "m_TimeContrastReport");
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
	public static final ZulItem				m_userOtherPublic			= new ZulItem("m_userOtherPublic", "产品URL地址", "m_userOtherPublic");//change
	public static final ZulItem				SystemDiagnosis		= new ZulItem("SystemDiagnosis", "系统诊断", "m_system_diagnosis");
	public static final ZulItem				License				= new ZulItem("License", "软件许可", "");
	public static final ZulItem				About				= new ZulItem("About", "产品介绍", "");
	
	public static final ZulItem				OtherUnknown		= new ZulItem("OtherUnknown", "其他未知", "");

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
	 * 取得虚拟节点的类型，可能的值为: VirtualView.INode(监测节点) 和 VirtualView.Item(固定节点)
	 */
	public String getType() {
		return m_type;
	}

	/**
	 * 如果该虚拟节点为监测节点, 则取得其 sv_id, 该值可能为null
	 */
	public String getSvId() {
		return m_sv_id;
	}

	/**
	 * 取得虚拟节点的id
	 */
	public String getItemId() {
		return m_item_id;
	}

	/**
	 * 取得虚拟节点父亲的id
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
	 * 追加一个子节点 id
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
	 * 删除一个子节点 id
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
	 * 取得子节点 id 列表
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
	 * 如果该虚拟节点为固定节点,则取得其数据, 该值可能为null
	 */
	public Map<String, String> getItemData() {
		return m_map;
	}

	/**
	 * 取得固定节点数据中的某个值
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
	 * 该节点是否设置为 “自动添加所有子监测器“
	 */
	public boolean isWithAllSubMonitor() {
		return getItemDataByKey("withAllSubMonitor").equals("true");
	}

	/**
	 * 取得固定节点数据中的 ZulName 值
	 */
	public String getItemDataZulName() {
		return getItemDataByKey("zul_name");
	}

	/**
	 *取得固定节点数据中的 ZulType，每个节点的该值都不同
	 */
	public String getItemDataZulType() {
		return getItemDataByKey("zul_type");
	}
	
}
