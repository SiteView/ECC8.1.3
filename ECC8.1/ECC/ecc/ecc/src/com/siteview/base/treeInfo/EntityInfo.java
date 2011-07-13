package com.siteview.base.treeInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.zkoss.zk.ui.Session;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.template.EntityTemplate;
import com.siteview.base.template.TemplateManager;
import com.siteview.base.tree.EntityNode;
import com.siteview.base.tree.INode;
import com.siteview.base.tree.MonitorNode;
import com.siteview.base.treeEdit.MonitorEdit;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.util.Toolkit;


public class EntityInfo extends EntityNode implements IInfo 
{
	private EntityInfoInner	m_info	= null;
		
	public EntityInfo(INode node)
	{
		super(node);
		m_info= new EntityInfoInner(this);
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
	 * ˢ�±��ڵ��µ����м����,�÷�����������
	 * <br/> ˢ�µĽ������ѭ������ getRefreshedData ȡ��
	 * @return  ��Ϣ���е����ƣ����� getRefreshedData ʱ�봫��
	 */	
	public String refresh()throws Exception
	{
		return m_info.refresh();
	}
	/**
	 * ���ݴ����id ������ˢ�¼����,�÷�����������
	 * <br/> ˢ�µĽ������ѭ������ getRefreshedData ȡ��
	 * @return  ��Ϣ���е����ƣ����� getRefreshedData ʱ�봫��
	 */	
	public String refresh(String[] id)throws Exception
	{
		return m_info.refresh(id);
	}
	
	/**
	 * ѭ�����ñ�������ȡ��ˢ�µĽ����ѭ�����1-3�룬��ʱ�䲻Ҫ����60�룬���������쳣���˳�ѭ��
	 * <br/> ������ 3 �ε��ú��Ѿ�ȡ��ȫ����������ڵ� 3 �ε���ʱ���׳��쳣
	 * @param  queueName ��Ϣ���е�����
	 * @return ���ص�ˢ�������ڡ�RefreshData_1, RefreshData_2... ��, ���� dstr �ȼ����Ϣ
	 */	
	public RetMapInMap getRefreshedData(String queueName)throws Exception
	{
		return m_info.getRefreshedData(queueName);
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
	public boolean disableMonitor(String[] id,Date start, Date end,View view)throws Exception
	{
		return m_info.disableMonitor(id,start,end,view);
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
	public boolean enableMonitor(String[] id,View view)throws Exception
	{
		return m_info.enableMonitor(id,view);
	}
	
	public boolean change(EntityInfo node)
	{
		if(m_info.change(node))
			return true;		
		
		if(node.getDeviceType().compareTo(this.getDeviceType())!=0)
			return true;
		if(node.getIpAdress().compareTo(this.getIpAdress())!=0)
			return true;

		return false;
	}
		
	/**
	 * �Ƿ���Ȩ�� AddMonitor()
	 */	
	public boolean canAddMonitor()
	{
		return m_info.canAddMonitor();
	}
	
	/**
	 * �Ƿ���Ȩ�� TestDevice()
	 */	
	public boolean canTestDevice()
	{
		return m_info.canTestDevice();
	}
	
	
	/**
	 * ���һ�������
	 * @param templateId �������������ģ��id
	 * @return ����һ�� MonitorEdit ���Խ��б༭
	 */	
	public MonitorEdit AddMonitor(String templateId)throws Exception
	{
		return m_info.AddMonitor(templateId);
	}
	
	/**
	 * ����һ���豸
	 */	
	public void TestDevice(View view)throws Exception
	{
		if( !canTestDevice() )
			throw new Exception(" Refuse to TestDevice, id: " + super.getSvId() + " (" + super.getType() + ")");
		
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "TestEntity");
		ndata.put("entityId", super.getSvId());
		
		view.putTestDeviceData((INode)this, null);
		Timer timer = new Timer(true);
		timer.schedule(new StartTestDevice(timer, ndata, (INode)this, view),0);
	}
	
	public Map<String, String> getTestDeviceData(View view)throws Exception
	{
		RetMapInMap rmap= view.getTestDeviceData((INode)this);
		if(rmap==null)
			return null;
		
		if(!rmap.getRetbool())
			throw new Exception(" Failed to TestDevice, since:"+ rmap.getEstr());
		Map<String,Map<String, String>> fmap= rmap.getFmap();
		if(fmap.containsKey("DynamicData"))
			return fmap.get("DynamicData");
		else
			throw new Exception(" DynamicData of TestDevice is null! ");
	}
	
	static class StartTestDevice extends java.util.TimerTask
	{
		private Timer					m_timer	= null;
		private Map<String, String>	m_ndata;
		private View					m_view;
		private INode					m_node;
		
		public StartTestDevice(Timer t, Map<String, String> data, INode node, View view)
		{
			m_timer = t;
			m_ndata= data;
			m_view= view;
			m_node= node;
		}
		
		public void run()
		{
			try
			{
				if(m_ndata!=null && m_view!=null && m_node!=null)
				{
					RetMapInMap rmap= ManageSvapi.GetUnivData(m_ndata);
					m_view.putTestDeviceData(m_node, rmap);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			super.cancel();
			m_timer.cancel();
		}
	}
	
	/**
	 * ճ��һ������������豸��
	 * @param sourceMonitorId Դ�����ID
	 * @return ��monitor�� id
	 */	
	public String PasteMonitor(String sourceMonitorId) throws Exception
	{
		if( !canAddMonitor() )
			throw new Exception(" Refuse to AddMonitor, id: " + super.getSvId() + " (" + super.getType() + ")");
		
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "MonitorCopy");
		ndata.put("sourceId", sourceMonitorId);
		ndata.put("targetParentId", super.getSvId());
		ndata.put("autoCreateTable", "true");
		RetMapInMap rmap= ManageSvapi.GetUnivData(ndata);
		if(!rmap.getRetbool())
			throw new Exception(" Failed to PasteMonitor, since:"+ rmap.getEstr());
		
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
	
	public String getIpAdress()
	{
		Map<String, String> rmap= m_info.getRawMap();
		if(rmap==null)
			return "";
		String v= rmap.get("_MachineName");
		if(v==null)
			return "";
		return v;
	}
	public String getNetWork()
	{
		Map<String, String> rmap= m_info.getRawMap();
		if(rmap==null)
			return "";
		String v= rmap.get("sv_network");
		if(v==null)
			return "";
		return v;
	}

	public String getCreatTime()
	{
		Map<String, String> rmap= m_info.getRawMap();
		if(rmap==null)
			return "";
		String v= rmap.get("creat_timeb");
		if(v==null)
			return "";
		return v;
	}

	/**
	 * ��ȡ���豸�� �豸ģ����
	 */	
	public EntityTemplate getDeviceTemplate()
	{
		String type= getDeviceType();
		if(type==null || type.isEmpty())
			return null;
		return TemplateManager.getEntityTemplate(type);
	}
	
	/**
	 * ��ȡ���豸�� �豸ģ��id
	 */	
	public String getDeviceType()
	{
		Map<String, String> rmap= m_info.getRawMap();
		if(rmap==null)
			return "";
		String v= rmap.get("sv_devicetype");
		if(v==null)
			return "";
		return v;
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
	
	public class EntityInfoInner extends NodeInfo
	{
		public EntityInfoInner(INode node)
		{
			super(node);
		}
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
}
