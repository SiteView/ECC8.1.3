package com.siteview.base.treeInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Session;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.tree.GroupNode;
import com.siteview.base.tree.INode;
import com.siteview.base.tree.MonitorNode;
import com.siteview.base.treeEdit.EntityEdit;
import com.siteview.base.treeEdit.GroupEdit;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.util.Toolkit;

public class GroupInfo extends GroupNode implements IInfo
{	
	private GroupInfoInner	m_info	= null;
	
	public GroupInfo(INode node)
	{
		super(node);
		m_info= new GroupInfoInner(this);
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
		return get_sub_entity_sum(Toolkit.getToolkit().getSvdbView(session));
	}
	public int get_sub_monitor_sum(Session session)
	{
		return get_sub_monitor_sum(Toolkit.getToolkit().getSvdbView(session));
	}
	public int get_sub_monitor_disable_sum(Session session)
	{
		return get_sub_monitor_disable_sum(Toolkit.getToolkit().getSvdbView(session));
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
			if (INode.ERROR.equals(node.getStatus()) || INode.BAD.equals(node.getStatus())){
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
	public String getSvDescription()
	{
		return m_info.getSvDescription();	
	}
	
	public String getSvDependsOn()
	{
		return  m_info.getSvDependsOn();
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
		return m_info.deleteNode(id,view);
	}
	
	/**
	 * 是否有权限 refresh()
	 */	
	public boolean canRefresh()
	{
		return m_info.canRefresh();
	}
	
	/**
	 * 不允许刷新整个 group
	 */	
	public String refresh()throws Exception
	{
		throw new Exception(" Refuse to refresh in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	/**
	 * 不允许刷新 group
	 */	
	public String refresh(String[] id)throws Exception
	{
		throw new Exception(" Refuse to refresh in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	/**
	 * 不允许刷新 group
	 */	
	public RetMapInMap getRefreshedData(String queueName)throws Exception
	{
		throw new Exception(" Refuse to refresh in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
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
	
	/**
	 * 是否有权限 PasteDevice()
	 */	
	public boolean canPasteDevice()
	{
		return m_info.canPasteDevice();
	}

	/**
	 * 禁止自身及所有子监测器，必须有编辑权限
	 * <br/><br/>if (start != null && end != null) 
	 * <br/>&nbsp &nbsp  是时段禁止
	 * <br/>else
	 * <br/>&nbsp &nbsp  是永久禁止
	 */	
	public boolean disableMonitor(Date start, Date end,View view)throws Exception
	{
		return m_info.disableMonitor(start,end,view);
	}
	/**
	 * 根据传入的id ，批量禁止自身及所有子监测器，必须有编辑权限
	 * <br/><br/>if (start != null && end != null) 
	 * <br/>&nbsp &nbsp  是时段禁止
	 * <br/>else
	 * <br/>&nbsp &nbsp  是永久禁止
	 */		
	public boolean disableMonitor(String[] id,Date start, Date end, View view)throws Exception
	{
		return m_info.disableMonitor(id,start,end, view);
	}
	/**
	 * 启用自身及所有子监测器，必须有编辑权限
	 */		
	public boolean enableMonitor(View view)throws Exception
	{
		return m_info.enableMonitor(view);
	}
	/**
	 * 根据传入的id ，批量启用自身及所有子监测器，必须有编辑权限
	 */		
	public boolean enableMonitor(String[] id, View view)throws Exception
	{
		return m_info.enableMonitor(id,view);
	}
	
	public boolean change(GroupInfo node)
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
	

	/**
	 * 粘贴一个设备到该组下
	 * @param sourceEntityId 源设备ID
	 * @return 新entity的 id
	 */	
	public String PasteDevice(String sourceEntityId) throws Exception
	{
		if( !canPasteDevice() )
			throw new Exception(" Refuse to PasteDevice, id: " + super.getSvId() + " (" + super.getType() + ")");
		
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "EntityCopy");
		ndata.put("sourceId", sourceEntityId);
		ndata.put("targetParentId", super.getSvId());
		ndata.put("autoCreateTable", "true");
		RetMapInMap rmap= ManageSvapi.GetUnivData(ndata);
		if(!rmap.getRetbool())
			throw new Exception(" Failed to PasteDevice, since:"+ rmap.getEstr());
		
		String newid= null;
		Map<String, Map<String, String>> fmap= rmap.getFmap();
		if (fmap != null && !fmap.isEmpty())
		{
			Map<String, String> ret= fmap.get("return");
			if (ret != null && !ret.isEmpty())
				newid= ret.get("newid");
		}
		if(newid==null || newid.isEmpty())
			throw new Exception(" newid is emtpty, but saving is succeeded! ");
		Manager.instantUpdate();
		return newid;
	}
	
	/**
	 * 能否禁止或启用监测
	 */	
	public boolean canDisableOrEnableMonitor(View view)
	{
		return m_info.canDisableOrEnableMonitor(view);
	}
	
	/**
	 * 显示禁止还是启用
	 */
	public String getLableofDisableOrEnable()
	{
		return m_info.getLableofDisableOrEnable();
	}
	
	public class GroupInfoInner extends NodeInfo
	{
		public GroupInfoInner(INode node)
		{
			super(node);
		}
	}


}
