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
	 * �Ƿ���Ȩ�� deleteNode()
	 */	
	public boolean canDeleteNode()
	{
		return m_info.canDeleteNode();
	}
	
	/**
	 * ɾ�����ڵ㼰����������
	 */	
	public boolean deleteNode(View view)throws Exception
	{
		return m_info.deleteNode(view);
	}
	/**
	 * ���ݴ����id ������ɾ������
	 */	
	public boolean deleteNode(String[] id,View view)throws Exception
	{
		return m_info.deleteNode(id,view);
	}
	
	/**
	 * �Ƿ���Ȩ�� refresh()
	 */	
	public boolean canRefresh()
	{
		return m_info.canRefresh();
	}
	
	/**
	 * ������ˢ������ group
	 */	
	public String refresh()throws Exception
	{
		throw new Exception(" Refuse to refresh in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	/**
	 * ������ˢ�� group
	 */	
	public String refresh(String[] id)throws Exception
	{
		throw new Exception(" Refuse to refresh in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
	}
	/**
	 * ������ˢ�� group
	 */	
	public RetMapInMap getRefreshedData(String queueName)throws Exception
	{
		throw new Exception(" Refuse to refresh in this way, id: " + super.getSvId() + " (" + super.getType() + ")");
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
	
	/**
	 * �Ƿ���Ȩ�� PasteDevice()
	 */	
	public boolean canPasteDevice()
	{
		return m_info.canPasteDevice();
	}

	/**
	 * ��ֹ���������Ӽ�����������б༭Ȩ��
	 * <br/><br/>if (start != null && end != null) 
	 * <br/>&nbsp &nbsp  ��ʱ�ν�ֹ
	 * <br/>else
	 * <br/>&nbsp &nbsp  �����ý�ֹ
	 */	
	public boolean disableMonitor(Date start, Date end,View view)throws Exception
	{
		return m_info.disableMonitor(start,end,view);
	}
	/**
	 * ���ݴ����id ��������ֹ���������Ӽ�����������б༭Ȩ��
	 * <br/><br/>if (start != null && end != null) 
	 * <br/>&nbsp &nbsp  ��ʱ�ν�ֹ
	 * <br/>else
	 * <br/>&nbsp &nbsp  �����ý�ֹ
	 */		
	public boolean disableMonitor(String[] id,Date start, Date end, View view)throws Exception
	{
		return m_info.disableMonitor(id,start,end, view);
	}
	/**
	 * �������������Ӽ�����������б༭Ȩ��
	 */		
	public boolean enableMonitor(View view)throws Exception
	{
		return m_info.enableMonitor(view);
	}
	/**
	 * ���ݴ����id �������������������Ӽ�����������б༭Ȩ��
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
	

	/**
	 * ճ��һ���豸��������
	 * @param sourceEntityId Դ�豸ID
	 * @return ��entity�� id
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
	 * �ܷ��ֹ�����ü��
	 */	
	public boolean canDisableOrEnableMonitor(View view)
	{
		return m_info.canDisableOrEnableMonitor(view);
	}
	
	/**
	 * ��ʾ��ֹ��������
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
