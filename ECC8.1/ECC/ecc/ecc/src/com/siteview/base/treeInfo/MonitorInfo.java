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
	 * ��ȡ�ü������ �����ģ����
	 */	
	public MonitorTemplate getMonitorTemplate()
	{
		String type= getMonitorType();
		if(type==null || type.isEmpty())
			return null;
		return TemplateManager.getMonitorTemplate(type);
	}
	
	/**
	 * ��ȡ�ü������ �����ģ��id
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
		return m_info.deleteNode(view);
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
	 * ˢ�±��ڵ��µ����м����,�÷�����������
	 * <br/> ˢ�µĽ������ѭ������ getRefreshedData ȡ��
	 * @return  ��Ϣ���е����ƣ����� getRefreshedData ʱ�봫��
	 */
	public String refresh(String[] id)throws Exception
	{
		return m_info.refresh();
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
	 * ��ֹ������������б༭Ȩ��
	 * <br/><br/>if (start != null && end != null) 
	 * <br/>&nbsp &nbsp  ��ʱ�ν�ֹ
	 * <br/>else
	 * <br/>&nbsp &nbsp  �����ý�ֹ
	 */	
	public boolean disableMonitor(Date start, Date end ,View view)throws Exception
	{
		return m_info.disableMonitor(start,end,view);
	}
	/**
	 * ��ֹ������������б༭Ȩ��
	 * <br/><br/>if (start != null && end != null) 
	 * <br/>&nbsp &nbsp  ��ʱ�ν�ֹ
	 * <br/>else
	 * <br/>&nbsp &nbsp  �����ý�ֹ
	 */		
	public boolean disableMonitor(String[] id,Date start, Date end ,View view)throws Exception
	{
		return m_info.disableMonitor(start,end,view);
	}
	/**
	 * ���ü�����������б༭Ȩ��
	 */		
	public boolean enableMonitor(View view)throws Exception
	{
		return m_info.enableMonitor(view);
	}
	/**
	 * ���ü�����������б༭Ȩ��
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
	 * �ܷ��ֹ�����ü��
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
	//ͳ���������
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
	 * ��ʾ��ֹ��������
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
