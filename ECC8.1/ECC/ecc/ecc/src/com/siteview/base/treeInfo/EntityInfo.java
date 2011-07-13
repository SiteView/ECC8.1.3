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
	 * 刷新本节点下的所有监测器,该方法立即返回
	 * <br/> 刷新的结果，需循环调用 getRefreshedData 取得
	 * @return  消息队列的名称，调用 getRefreshedData 时须传入
	 */	
	public String refresh()throws Exception
	{
		return m_info.refresh();
	}
	/**
	 * 根据传入的id ，批量刷新监测器,该方法立即返回
	 * <br/> 刷新的结果，需循环调用 getRefreshedData 取得
	 * @return  消息队列的名称，调用 getRefreshedData 时须传入
	 */	
	public String refresh(String[] id)throws Exception
	{
		return m_info.refresh(id);
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
	public boolean disableMonitor(String[] id,Date start, Date end,View view)throws Exception
	{
		return m_info.disableMonitor(id,start,end,view);
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
	 * 是否有权限 AddMonitor()
	 */	
	public boolean canAddMonitor()
	{
		return m_info.canAddMonitor();
	}
	
	/**
	 * 是否有权限 TestDevice()
	 */	
	public boolean canTestDevice()
	{
		return m_info.canTestDevice();
	}
	
	
	/**
	 * 添加一个监测器
	 * @param templateId 欲创建监测器的模板id
	 * @return 返回一个 MonitorEdit ，以进行编辑
	 */	
	public MonitorEdit AddMonitor(String templateId)throws Exception
	{
		return m_info.AddMonitor(templateId);
	}
	
	/**
	 * 测试一个设备
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
	 * 粘贴一个监测器到该设备下
	 * @param sourceMonitorId 源监测器ID
	 * @return 新monitor的 id
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
	 * 获取该设备的 设备模板类
	 */	
	public EntityTemplate getDeviceTemplate()
	{
		String type= getDeviceType();
		if(type==null || type.isEmpty())
			return null;
		return TemplateManager.getEntityTemplate(type);
	}
	
	/**
	 * 获取该设备的 设备模板id
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
