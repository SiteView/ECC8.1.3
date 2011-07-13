package com.siteview.base.treeInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Session;

import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.tree.MonitorNode;
import com.siteview.base.tree.SeNode;
import com.siteview.base.treeEdit.EntityEdit;
import com.siteview.base.treeEdit.GroupEdit;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.util.Toolkit;

public class SeInfo extends SeNode  implements IInfo
{
	private SeInfoInner	m_info	= null;
	
	public SeInfo(INode node)
	{
		super(node);
		m_info= new SeInfoInner(this);
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
	@Override
	public int get_sub_entity_sum(View view) {
		if (view == null) return 0;
		return BaseTools.getAllEntites(view, this.getSvId()).size();
	}

	@Override
	public int get_sub_monitor_disable_sum(View view) {
		if (view == null) return 0;
		List<String> monitors = BaseTools.getAllMonitors(view, this.getSvId());
		int sum = 0;
		for (String id : monitors){
			MonitorNode node = view.getMonitorNode(id);
			if(node == null){
				continue;
			}
			if (INode.DISABLE.equals(node.getStatus())){
				sum ++;
			}
		}
		return sum;
	}

	@Override
	public int get_sub_monitor_error_sum(View view) {
		if (view == null) return 0;
		List<String> monitors = BaseTools.getAllMonitors(view, this.getSvId());
		int sum = 0;
		for (String id : monitors){
			MonitorNode node = view.getMonitorNode(id);
			if(node == null){
				continue;
			}
			if (INode.ERROR.equals(node.getStatus()) || INode.BAD.equals(node.getStatus()) || INode.NULL.equals(node.getStatus())){
				sum ++;
			}
		}
		return sum;
	}

	@Override
	public int get_sub_monitor_ok_sum(View view) {
		if (view == null) return 0;
		List<String> monitors = BaseTools.getAllMonitors(view, this.getSvId());
		int sum = 0;
		for (String id : monitors){
			MonitorNode node = view.getMonitorNode(id);
			if(node == null){
				continue;
			}
			if (INode.OK.equals(node.getStatus())){
				sum ++;
			}
		}
		return sum;
	}

	@Override
	public int get_sub_monitor_sum(View view) {
		if (view == null) return 0;
		return BaseTools.getAllMonitors(view, this.getSvId()).size();
	}

	@Override
	public int get_sub_monitor_warning_sum(View view) {
		if (view == null) return 0;
		List<String> monitors = BaseTools.getAllMonitors(view, this.getSvId());
		int sum = 0;
		for (String id : monitors){
			MonitorNode node = view.getMonitorNode(id);
			if(node == null){
				continue;
			}
			if (INode.WARNING.equals(node.getStatus())){
				sum ++;
			}
		}
		return sum;
	}
	
	public int get_sub_entity_sum(Session session)
	{
		return this.get_sub_entity_sum(Toolkit.getToolkit().getSvdbView(session));
	}
	public int get_sub_monitor_sum(Session session)
	{
		return get_sub_monitor_sum(Toolkit.getToolkit().getSvdbView(session));
	}
	public int get_sub_monitor_disable_sum(Session session)
	{
		return  get_sub_monitor_disable_sum(Toolkit.getToolkit().getSvdbView(session));
	}
	public int get_sub_monitor_error_sum(Session session)
	{
		return get_sub_monitor_error_sum(Toolkit.getToolkit().getSvdbView(session));
	}
	public int get_sub_monitor_warning_sum(Session session)
	{
		return get_sub_monitor_warning_sum(Toolkit.getToolkit().getSvdbView(session));
	}
	public int get_sub_monitor_ok_sum(Session session)
	{
		return get_sub_monitor_ok_sum(Toolkit.getToolkit().getSvdbView(session));
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
	 * 不允许从界面删除一个 se
	 */	
	public boolean canDeleteNode()
	{
		return m_info.canDeleteNode();
	}
	/**
	 * 不允许从界面删除一个 se
	 */	
	public boolean deleteNode(View view)throws Exception
	{
		throw new Exception(" Refuse to delete node in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	/**
	 * 不允许从界面删除 se
	 */	
	public boolean deleteNode(String[] id,View view)throws Exception
	{
		return m_info.deleteNode(id,view);
	}	
	
	/**
	 * 不允许刷新整个 se
	 */	
	public boolean canRefresh()
	{
		return false;
	}
	
	/**
	 * 不允许刷新整个 se
	 */	
	public String refresh()throws Exception
	{
		throw new Exception(" Refuse to refresh in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	/**
	 * 不允许刷新 se
	 */	
	public String refresh(String[] id)throws Exception
	{
		throw new Exception(" Refuse to refresh in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	/**
	 * 不允许刷新 se
	 */	
	public RetMapInMap getRefreshedData(String queueName)throws Exception
	{
		throw new Exception(" Refuse to refresh in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	
	/**
	 * 不允许从 se 禁止监测
	 */	
	public boolean disableMonitor(Date start, Date end, View view)throws Exception
	{
		throw new Exception(" Refuse to disableMonitor in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	/**
	 * 不允许从 se 禁止监测
	 */		
	public boolean disableMonitor(String[] id,Date start, Date end, View view)throws Exception
	{
		throw new Exception(" Refuse to disableMonitor in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	/**
	 * 不允许从 se 启用监测
	 */		
	public boolean enableMonitor(View view)throws Exception
	{
		throw new Exception(" Refuse to enableMonitor in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	/**
	 * 不允许从 se 启用监测
	 */		
	public boolean enableMonitor(String[] id, View view)throws Exception
	{
		throw new Exception(" Refuse to enableMonitor in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	
	/**
	 * 不允许从 se 禁止或启用监测
	 */	
	public boolean canDisableOrEnableMonitor(View view)
	{
		return false;
	}
	
	/**
	 * 不允许从 se 禁止或启用监测
	 */
	public String getLableofDisableOrEnable()
	{
		return null;
	}
	
	/**
	 * 是否有权限 AddGroup()
	 */	
	public boolean canAddGroup()
	{
		return m_info.canAddGroup();
	}

	/**
	 * 是否有权限 AddDevice()
	 */	
	public boolean canAddDevice()
	{
		return m_info.canAddDevice();
	}
	
	public boolean change(SeInfo node)
	{
		return m_info.change(node);
	}
	
	/**
	 * 添加一个设备
	 * @param templateId 欲创建监测器的模板id
	 * @return 返回一个 EntityEdit ，以进行编辑
	 */	
	public EntityEdit AddDevice(String templateId) throws Exception
	{
		return m_info.AddDevice(templateId);
	}	

	/**
	 * 添加一个组
	 * @return 返回一个 GroupEdit ，以进行编辑
	 */	
	public GroupEdit AddGroup() throws Exception
	{
		return m_info.AddGroup();
	}
	
	
	public class SeInfoInner extends NodeInfo
	{
		public SeInfoInner(INode node)
		{
			super(node);
		}
	}


	
}
