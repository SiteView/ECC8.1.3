package com.siteview.base.treeInfo;

import java.util.Date;
import java.util.Map;

import org.zkoss.zk.ui.Session;

import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.template.TemplateManager;
import com.siteview.base.tree.INode;
import com.siteview.base.tree.MonitorNode;
import com.siteview.ecc.alert.util.LocalIniFile;

public class MonitorInfo extends MonitorNode  implements IInfo
{
	private MonitorInfoInner	m_info	= null;
	
	public MonitorInfo(INode node)
	{
		super(node);
		m_info= new MonitorInfoInner(this);
	}
	
	public void setRawMap(Map<String, String> map)
	{
		m_info.setRawMap(map);
	}
	
	public Map<String, String> getRawMap()
	{
		return m_info.getRawMap();
	}
	
	public void setIniValue(Map<String, String> inivalue)
	{
		m_info.setIniValue(inivalue);
	}
	
	public int get_sub_entity_sum(Session session)
	{
		return -1;
	}
	public int get_sub_monitor_sum(Session session)
	{
		return -1;
	}
	public int get_sub_monitor_disable_sum(Session session)
	{
		return -1;
	}
	public int get_sub_monitor_error_sum(Session session)
	{
		return -1;
	}
	public int get_sub_monitor_warning_sum(Session session)
	{
		return -1;
	}
	public int get_sub_monitor_ok_sum(Session session)
	{
		return -1;
	}	
	@Override
	public int get_sub_entity_sum(View view) {
		return -1;
	}

	@Override
	public int get_sub_monitor_disable_sum(View view) {
		return -1;
	}

	@Override
	public int get_sub_monitor_error_sum(View view) {
		return -1;
	}

	@Override
	public int get_sub_monitor_ok_sum(View view) {
		return -1;
	}

	@Override
	public int get_sub_monitor_sum(View view) {
		return -1;
	}

	@Override
	public int get_sub_monitor_warning_sum(View view) {
		return -1;
	}
	
	/**
	 * 获取该监测器的 监测器模板类
	 */	
	public MonitorTemplate getMonitorTemplate()
	{
		String type= getMonitorType();
		if(type==null || type.isEmpty())
			return null;
		return TemplateManager.getMonitorTemplate(type);
	}
	
	/**
	 * 获取该监测器的 监测器模板id
	 */	
	public String getMonitorType()
	{
		Map<String, String> rmap = m_info.getRawMap();
		if(rmap == null)
			return "";
		String v = rmap.get("sv_monitortype");
		if(v == null)
			return "";
		return v;
	}
	
	/**
	 * 是否管理员 (管理员对整棵树所有节点，都拥有全部权限)
	 */	
	public boolean isAdmin()
	{
		return m_info.isAdmin();
	}
	
	/**
	 * 是否有权限编辑该节点
	 */	
	public boolean canEdit()
	{
		return m_info.canEdit();
	}	
	
	/**
	 * 是否有权限 deleteNode()
	 */	
	public boolean canDeleteNode()
	{
		return m_info.canDeleteNode();
	}
	
	/**
	 * 删除本节点及其所有子孙
	 */	
	public boolean deleteNode(View view)throws Exception
	{
		return m_info.deleteNode(view);
	}
	/**
	 * 根据传入的id ，批量删除子孙
	 */	
	public boolean deleteNode(String[] id,View view)throws Exception
	{
		return m_info.deleteNode(view);
	}
	
	/**
	 * 是否有权限 refresh()
	 */	
	public boolean canRefresh()
	{
		return m_info.canRefresh();
	}
	
	/**
	 * 刷新本节点下的所有监测器,该方法立即返回
	 * <br/> 刷新的结果，需循环调用 getRefreshedData 取得
	 * @return  消息队列的名称，调用 getRefreshedData 时须传入
	 */	
	public String refresh()throws Exception
	{
		return m_info.refresh();
	}
	/**
	 * 刷新本节点下的所有监测器,该方法立即返回
	 * <br/> 刷新的结果，需循环调用 getRefreshedData 取得
	 * @return  消息队列的名称，调用 getRefreshedData 时须传入
	 */
	public String refresh(String[] id)throws Exception
	{
		return m_info.refresh();
	}
	/**
	 * 循环调用本方法以取得刷新的结果，循环间隔1-3秒，总时间不要超过60秒，其间如果有异常则退出循环
	 * <br/> 比如在 3 次调用后，已经取得全部结果，则在第 3 次调用时会抛出异常
	 * @param  queueName 消息队列的名称
	 * @return 返回的刷新数据在　RefreshData_1, RefreshData_2... 中, 其中 dstr 等监测信息
	 */	
	public RetMapInMap getRefreshedData(String queueName)throws Exception
	{
		return m_info.getRefreshedData(queueName);
	}
	
	/**
	 * 禁止监测器，必须有编辑权限
	 * <br/><br/>if (start != null && end != null) 
	 * <br/>&nbsp &nbsp  是时段禁止
	 * <br/>else
	 * <br/>&nbsp &nbsp  是永久禁止
	 */	
	public boolean disableMonitor(Date start, Date end ,View view)throws Exception
	{
		return m_info.disableMonitor(start,end,view);
	}
	/**
	 * 禁止监测器，必须有编辑权限
	 * <br/><br/>if (start != null && end != null) 
	 * <br/>&nbsp &nbsp  是时段禁止
	 * <br/>else
	 * <br/>&nbsp &nbsp  是永久禁止
	 */		
	public boolean disableMonitor(String[] id,Date start, Date end ,View view)throws Exception
	{
		return m_info.disableMonitor(start,end,view);
	}
	/**
	 * 启用监测器，必须有编辑权限
	 */		
	public boolean enableMonitor(View view)throws Exception
	{
		return m_info.enableMonitor(view);
	}
	/**
	 * 启用监测器，必须有编辑权限
	 */		
	public boolean enableMonitor(String[] id ,View view)throws Exception
	{
		return m_info.enableMonitor(view);
	}
	
	public boolean change(MonitorInfo node)
	{
		if(m_info.change(node))
			return true;		
		
		if(node.getDstr().compareTo(this.getDstr())!=0)
			return true;
		if(node.getCreateTime().compareTo(this.getCreateTime())!=0)
			return true;

		return false;
	}
		
	public String getCreateTime()
	{
		Map<String, String> rmap= m_info.getRawMap();
		if(rmap==null)
			return "";
		String v= rmap.get("creat_time");
		if(v==null)
			return "";
		return v;
	}
	
	public String getDstr()
	{
		Map<String, String> rmap = m_info.getRawMap();
		if(rmap == null)
			return "";
		String v = rmap.get("dstr");
		if(v == null)
			return "";
		return v;
	}
	
	/**
	 * 能否禁止或启用监测
	 */	
	public boolean canDisableOrEnableMonitor(View view)
	{
		return m_info.canDisableOrEnableMonitor(view);
	}

	private static LocalIniFile iniFile = new LocalIniFile("MonitorBrowse.ini");
	static{
		try {
			iniFile.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//统计浏览次数
	public void incBrowserCount() throws Exception{
		int count = getBrowserCount();
		synchronized(iniFile){
			iniFile.setKeyValue(this.getSvId(), "monitorBrowseCount", ++count);
			iniFile.setKeyValue(this.getSvId(), "monitorBrowseNewDate", new Date().toLocaleString());
			iniFile.saveChange();
		}
	}
	
	public int getBrowserCount(){
		int count = 0;
		try{
			count = Integer.parseInt(iniFile.getValue(this.getSvId(), "monitorBrowseCount"));
		}catch(Exception e){
			
		}
		return count;
		
	}
	
	
	/**
	 * 显示禁止还是启用
	 */
	public String getLableofDisableOrEnable()
	{
		return m_info.getLableofDisableOrEnable();
	}
	
	public class MonitorInfoInner extends NodeInfo
	{
		public MonitorInfoInner(INode node)
		{
			super(node);
		}
	}

}
