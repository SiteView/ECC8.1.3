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
	 * �Ƿ����Ա (����Ա�����������нڵ㣬��ӵ��ȫ��Ȩ��)
	 */	
	public boolean isAdmin()
	{
		return m_info.isAdmin();
	}
	
	/**
	 * �Ƿ���Ȩ�ޱ༭�ýڵ�
	 */	
	public boolean canEdit()
	{
		return m_info.canEdit();
	}	
	

	/**
	 * ������ӽ���ɾ��һ�� se
	 */	
	public boolean canDeleteNode()
	{
		return m_info.canDeleteNode();
	}
	/**
	 * ������ӽ���ɾ��һ�� se
	 */	
	public boolean deleteNode(View view)throws Exception
	{
		throw new Exception(" Refuse to delete node in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	/**
	 * ������ӽ���ɾ�� se
	 */	
	public boolean deleteNode(String[] id,View view)throws Exception
	{
		return m_info.deleteNode(id,view);
	}	
	
	/**
	 * ������ˢ������ se
	 */	
	public boolean canRefresh()
	{
		return false;
	}
	
	/**
	 * ������ˢ������ se
	 */	
	public String refresh()throws Exception
	{
		throw new Exception(" Refuse to refresh in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	/**
	 * ������ˢ�� se
	 */	
	public String refresh(String[] id)throws Exception
	{
		throw new Exception(" Refuse to refresh in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	/**
	 * ������ˢ�� se
	 */	
	public RetMapInMap getRefreshedData(String queueName)throws Exception
	{
		throw new Exception(" Refuse to refresh in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	
	/**
	 * ������� se ��ֹ���
	 */	
	public boolean disableMonitor(Date start, Date end, View view)throws Exception
	{
		throw new Exception(" Refuse to disableMonitor in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	/**
	 * ������� se ��ֹ���
	 */		
	public boolean disableMonitor(String[] id,Date start, Date end, View view)throws Exception
	{
		throw new Exception(" Refuse to disableMonitor in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	/**
	 * ������� se ���ü��
	 */		
	public boolean enableMonitor(View view)throws Exception
	{
		throw new Exception(" Refuse to enableMonitor in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	/**
	 * ������� se ���ü��
	 */		
	public boolean enableMonitor(String[] id, View view)throws Exception
	{
		throw new Exception(" Refuse to enableMonitor in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	
	/**
	 * ������� se ��ֹ�����ü��
	 */	
	public boolean canDisableOrEnableMonitor(View view)
	{
		return false;
	}
	
	/**
	 * ������� se ��ֹ�����ü��
	 */
	public String getLableofDisableOrEnable()
	{
		return null;
	}
	
	/**
	 * �Ƿ���Ȩ�� AddGroup()
	 */	
	public boolean canAddGroup()
	{
		return m_info.canAddGroup();
	}

	/**
	 * �Ƿ���Ȩ�� AddDevice()
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
	 * ���һ���豸
	 * @param templateId �������������ģ��id
	 * @return ����һ�� EntityEdit ���Խ��б༭
	 */	
	public EntityEdit AddDevice(String templateId) throws Exception
	{
		return m_info.AddDevice(templateId);
	}	

	/**
	 * ���һ����
	 * @return ����һ�� GroupEdit ���Խ��б༭
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
