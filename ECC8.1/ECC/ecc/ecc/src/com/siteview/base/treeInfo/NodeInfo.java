package com.siteview.base.treeInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TreeSet;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.IniFileKeyValue;
import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.tree.EntityNode;
import com.siteview.base.tree.GroupNode;
import com.siteview.base.tree.IForkNode;
import com.siteview.base.tree.INode;
import com.siteview.base.tree.MonitorNode;
import com.siteview.base.treeEdit.EntityEdit;
import com.siteview.base.treeEdit.GroupEdit;
import com.siteview.base.treeEdit.MonitorEdit;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.util.Toolkit;

public abstract class NodeInfo implements IInfo
{
	//��һ��Ŀ��
	//�豸(������Ӧ��ʵ�壬�Ը�nnm��dm��itsmʵ�ֲ�Ʒ����)
	//�û������� (��party����)
	
//seȨ�� ��	addsongroup=0,adddevice=0,  se_edit=0,
//��Ȩ�� ��		                                                                                                    ճ��(�豸)                        ˢ��
//��Ȩ�� ��	addsongroup=0,adddevice=0,  editgroup=0,copydevice=0,   delgroup=0,    grouprefresh=0,
//�豸Ȩ�� ��	addmonitor=0,               editdevice=0,testdevice=0,  deldevice=0,   devicerefresh=0,
//�����Ȩ�� ��		                    editmonitor=0,              delmonitor=0,  monitorrefresh=0,
//

	private boolean			m_can_edit				= false;
	private boolean			m_can_delete			= false;
	private boolean			m_can_refresh			= false;
	
	private boolean			m_can_add_device		= false;
	private boolean			m_can_add_group			= false;
	private boolean			m_can_copy_device		= false;
	
	private boolean			m_can_test_device		= false;
	private boolean			m_can_add_monitor		= false;
	
	private boolean			m_is_nAdmin				= false;
	
	private INode			m_inode					= null;
	Map<String, String>	m_raw_map				= null;
	Map<String, String>	m_inivalue				= null;
	
	public NodeInfo(INode node)
	{
		m_inode= node;
	}
	
	public void setRawMap(Map<String, String> map)
	{
		m_raw_map= map;
	}
	
	public Map<String, String> getRawMap()
	{
		return m_raw_map;
	}
	
	public void setIniValue(Map<String, String> inivalue)
	{
		m_inivalue= inivalue;	
		String temp= m_inivalue.get("nAdmin"); 
		if(temp!=null && temp.compareTo("true")==0)
		{
			m_is_nAdmin = true;
			if (m_inode.getType().compareTo(INode.SE) == 0)
			{
				m_can_edit = true;
				m_can_add_group = true;
				m_can_add_device = true;
				return;
			}
			
			m_can_edit			= true;
			m_can_delete		= true;
			m_can_refresh		= true;
			if (m_inode.getType().compareTo(INode.GROUP) == 0)
			{
				m_can_add_device = true;
				m_can_copy_device = true;
				m_can_add_group = true;
			}
			else if (m_inode.getType().compareTo(INode.ENTITY) == 0)
			{
				m_can_add_monitor	= true;
				m_can_test_device	= true;
			}
			return;
		}
		try
		{
			if (m_inode.getType().compareTo(INode.SE) == 0)
			{
				String value = m_inivalue.get(m_inode.getSvId());
				if(value==null)
					return;
				
				if (value.contains("se_edit=1"))
					m_can_edit = true;
				if (value.contains("addsongroup=1"))
					m_can_add_group = true;
				if (value.contains("adddevice=1"))
					m_can_add_device = true;
				return;
			}
			

			String pid = new String(m_inode.getSvId());
			do
			{
				String value= m_inivalue.get(pid);
				if (value != null && !value.isEmpty())
				{
					if (m_inode.getType().compareTo(INode.GROUP) == 0)
					{
						if (value.contains("editgroup=1"))
							m_can_edit = true;
						if (value.contains("delgroup=1"))
							m_can_delete = true;
						if (value.contains("grouprefresh=1"))
							m_can_refresh = true;
						if (value.contains("addsongroup=1"))
							m_can_add_group = true;
						if (value.contains("adddevice=1"))
							m_can_add_device = true;
						if (value.contains("copydevice=1"))
							m_can_copy_device = true;
						
						return;
					}
					else if (m_inode.getType().compareTo(INode.ENTITY) == 0)
					{
						if (value.contains("editdevice=1"))
							m_can_edit = true;
						if (value.contains("deldevice=1"))
							m_can_delete = true;
						if (value.contains("devicerefresh=1"))
							m_can_refresh = true;
						if (value.contains("addmonitor=1"))
							m_can_add_monitor = true;
						if (value.contains("testdevice=1"))
							m_can_test_device = true;
						
						if (value.contains("editdevice") || value.contains("deldevice") || value.contains("devicerefresh") || value.contains("addmonitor") || value.contains("testdevice")  )
							return;
					}
					else if (m_inode.getType().compareTo(INode.MONITOR) == 0)
					{
						if (value.contains("editmonitor=1"))
							m_can_edit = true;
						if (value.contains("delmonitor=1"))
							m_can_delete = true;
						if (value.contains("monitorrefresh=1"))
							m_can_refresh = true;
						
						if (value.contains("editmonitor") || value.contains("delmonitor") || value.contains("monitorrefresh") )
							return;
					}

				}
				pid = pid.substring(0, pid.lastIndexOf("."));
			}while (pid.contains("."));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
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
		return BaseTools.getAllEntites(view, m_inode.getSvId()).size();
	}

	@Override
	public int get_sub_monitor_disable_sum(View view) {
		if (view == null) return 0;
		List<String> monitors = BaseTools.getAllMonitors(view, m_inode.getSvId());
		int sum = 0;
		for (String id : monitors){
			MonitorNode node = view.getMonitorNode(id);
			if (INode.DISABLE.equals(node.getStatus())){
				sum ++;
			}
		}
		return sum;
	}

	@Override
	public int get_sub_monitor_error_sum(View view) {
		if (view == null) return 0;
		List<String> monitors = BaseTools.getAllMonitors(view, m_inode.getSvId());
		int sum = 0;
		for (String id : monitors){
			MonitorNode node = view.getMonitorNode(id);
			if (INode.ERROR.equals(node.getStatus())){
				sum ++;
			}
		}
		return sum;
	}

	@Override
	public int get_sub_monitor_ok_sum(View view) {
		if (view == null) return 0;
		List<String> monitors = BaseTools.getAllMonitors(view, m_inode.getSvId());
		int sum = 0;
		for (String id : monitors){
			MonitorNode node = view.getMonitorNode(id);
			if (INode.OK.equals(node.getStatus())){
				sum ++;
			}
		}
		return sum;
	}

	@Override
	public int get_sub_monitor_sum(View view) {
		if (view == null) return 0;
		return BaseTools.getAllMonitors(view, m_inode.getSvId()).size();
	}

	@Override
	public int get_sub_monitor_warning_sum(View view) {
		if (view == null) return 0;
		List<String> monitors = BaseTools.getAllMonitors(view, m_inode.getSvId());
		int sum = 0;
		for (String id : monitors){
			MonitorNode node = view.getMonitorNode(id);
			if (INode.WARNING.equals(node.getStatus())){
				sum ++;
			}
		}
		return sum;
	}
	

	public String getSvDescription()
	{
		if(m_raw_map==null)
			return "";
		String v= m_raw_map.get("sv_description");
		if(v==null)
			return "";
		return v;	
	}
	
	public String getSvDependsOn()
	{
		if(m_raw_map==null)
			return "";
		String v= m_raw_map.get("sv_dependson_svname");
		if(v==null)
			return "";
		return v;	
	}
	
	public boolean change(IInfo node)
	{	
		if( ((INode)node).getType().compareTo(m_inode.getType())!=0)
			return true;
		if( ((INode)node).getName().compareTo(m_inode.getName())!=0)
			return true;
		Session session = Executions.getCurrent().getDesktop().getSession();
		if ( m_inode.getType().compareTo(INode.MONITOR) != 0)
		{
			if (node.get_sub_entity_sum(session) != this.get_sub_entity_sum(session))
				return true;
			if (node.get_sub_monitor_sum(session) != this.get_sub_monitor_sum(session))
				return true;
			if (node.get_sub_monitor_disable_sum(session) != this.get_sub_monitor_disable_sum(session))
				return true;
			if (node.get_sub_monitor_error_sum(session) != this.get_sub_monitor_error_sum(session))
				return true;
			if (node.get_sub_monitor_ok_sum(session) != this.get_sub_monitor_ok_sum(session))
				return true;
			if (node.get_sub_monitor_warning_sum(session) != this.get_sub_monitor_warning_sum(session))
				return true;
		}
		
		if ( m_inode.getType().compareTo(INode.SE) == 0)
			return false;
		if( ((INode)node).getStatus().compareTo(m_inode.getStatus())!=0)
			return true;
		return false;
	}
	
	/**
	 * �Ƿ����Ա (����Ա�����������нڵ㣬��ӵ��ȫ��Ȩ��)
	 */	
	public boolean isAdmin()
	{
		return m_is_nAdmin;
	}
	
	/**
	 * �Ƿ���Ȩ�ޱ༭�ýڵ�
	 */	
	public boolean canEdit()
	{
		return m_can_edit;
	}
	
	/**
	 * �Ƿ���Ȩ�� deleteNode()
	 */	
	public boolean canDeleteNode()
	{
		return m_can_delete;
	}
	
	/**
	 * �Ƿ���Ȩ�� refresh()
	 */	
	public boolean canRefresh()
	{
		return m_can_refresh;
	}
	
	/**
	 * �Ƿ���Ȩ�� AddGroup()
	 */	
	public boolean canAddGroup()
	{
		return m_can_add_group;
	}
	
	/**
	 * �Ƿ���Ȩ�� AddDevice()
	 */	
	public boolean canAddDevice()
	{
		return m_can_add_device;
	}
	
	/**
	 * �Ƿ���Ȩ�� PasteDevice()
	 */	
	public boolean canPasteDevice()
	{
		return m_can_copy_device;
	}
	
	/**
	 * �Ƿ���Ȩ�� AddMonitor()
	 */	
	public boolean canAddMonitor()
	{
		return m_can_add_monitor;
	}
		
	/**
	 * �Ƿ���Ȩ�� TestDevice()
	 */	
	public boolean canTestDevice()
	{
		return m_can_test_device;
	}
	
	/**
	 * ���һ����
	 * @return ����һ�� GroupEdit ���Խ��б༭
	 */	
	public GroupEdit AddGroup() throws Exception
	{
		if (!canAddDevice())
			throw new Exception(" Refuse to add group under node, id: " + m_inode.getSvId() + " (" + m_inode.getType() + ")");

		GroupNode n= new GroupNode();
		n.setId(getNextSubSvid(m_inode));
		n.setStatus("");
		n.setName("");
		n.setType(INode.GROUP);
		GroupEdit e = new GroupEdit(n);
		e.initWholeData(m_inode.getSvId());
		
		Map<String, String> ini = new HashMap<String, String>(); 
		ini.put("nAdmin","true");
		e.setIniValue(ini);
		return e;
	}
	
	/**
	 * ���һ���豸
	 * @param templateId �������������ģ��id
	 * @return ����һ�� EntityEdit ���Խ��б༭
	 */	
	public EntityEdit AddDevice(String templateId) throws Exception
	{
		if (!canAddDevice())
			throw new Exception(" Refuse to add entity under node, id: " + m_inode.getSvId() + " (" + m_inode.getType() + ")");

		EntityNode n= new EntityNode();
		n.setId(getNextSubSvid(m_inode));
		n.setStatus("");
		n.setName("");
		n.setType(INode.ENTITY);
		EntityEdit e = new EntityEdit(n);
		e.initWholeData(m_inode.getSvId(), templateId);
		
		Map<String, String> ini = new HashMap<String, String>(); 
		ini.put("nAdmin","true");
		e.setIniValue(ini);
		
		//UI code construct other data by template
		return e;
	}
	
	/**
	 * ���һ�������
	 * @param templateId �������������ģ��id
	 * @return ����һ�� MonitorEdit ���Խ��б༭
	 */	
	public MonitorEdit AddMonitor(String templateId)throws Exception
	{
		if (!canAddMonitor())
			throw new Exception(" Refuse to add monitor under node, id: " + m_inode.getSvId() + " (" + m_inode.getType() + ")");

		MonitorNode n= new MonitorNode();
		n.setId(getNextSubSvid(m_inode));
		n.setStatus("");
		n.setName("");
		n.setType(INode.MONITOR);
		MonitorEdit e = new MonitorEdit(n);
		e.initWholeData(m_inode.getSvId(), templateId);
		
		Map<String, String> ini = new HashMap<String, String>(); 
		ini.put("nAdmin","true");
		e.setIniValue(ini);
		
		//UI code construct other data by template
		return e;
	}
	
	public String getNextSubSvid(INode node)
	{
		try
		{
			if(node.getType().equals(INode.MONITOR))
				return node.getSvId()+".0";
			IForkNode f = (IForkNode) node;
			List<String> a = f.getSonList();
			int size = a.size();
			String subid= node.getSvId()+".0";
			if( size>0 )
				subid= a.get(size-1);
			
			String num= subid.substring(subid.lastIndexOf(".")+1);
			Integer i= new Integer(size+10000);
			try
			{
				i= new Integer(num);
			} catch (Exception e)
			{
			}
			++i;
			
			String newid= node.getSvId() + "." + i.toString();
			String newSonId= f.getSonId()+ "," +newid;
			
			f.setSonId(newSonId);
			return newid;
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return node.getSvId()+".0";
		}	
	}

//	//������������ˢ�¼����
//	dowhat= refreshMonitors ,   id= X1,X2, ... X10 ,  parentid= XXX ,   instantReturn= true/false;  (Ĭ��Ϊ false ����������Ϊ false );
//	                     // parentid ������ id �Ĺ�ͬ����   // ��ѡ���������أ���ú�����ȴ�,ֱ����Ϣ���� ��֪ȫ��ˢ�½���ʱ����ͳһ�������µļ������
//	                                                        // ��ѡ���������أ��������һ������������ģ��ԭ cgi ��������ȡ�ü������Ч��
//	                                                        // �����ص� return ���� queueName= XXX ,������һ������.    
//	dowhat= GetLatestrefresh ,  queueName= XXX ;   // ���ص� return ���� isDone= true/false  ���������øú���ֱ�� isDone==true (������ bool ״̬Ϊ false ) ��ÿ�η���ֵ�л��� dstr �ȼ����Ϣ
//												   // ���ص�ˢ�������ڡ�refreshData ��
//
//	dowhat= Getrefreshed ,queueName= XXX ,parentid= XXX ;// ���ص� return ���� isDone= true/false  ���������øú���ֱ�� isDone==true (������ bool ״̬Ϊ false ) ��ÿ�η���ֵ�л��� dstr �ȼ����Ϣ
//												         // ���ص�ˢ�������ڡ�refreshData_1, refreshData_2... ��
//												         // parentid Խ��������Խ�ͣ��� "1" �ͱ� "1.2.2" ���ܵ�
	
	/**
	 * ˢ�±��ڵ��µ����м����,�÷�����������
	 * <br/> ˢ�µĽ������ѭ������ getRefreshedData ȡ��
	 * @return  ��Ϣ���е����ƣ����� getRefreshedData ʱ�봫��
	 */	
	public String refresh()throws Exception
	{
		if(!m_inode.getType().equals(INode.ENTITY) && !m_inode.getType().equals(INode.MONITOR))
			throw new Exception(" Refuse to group/se refresh, id: " + m_inode.getSvId() + " (" + m_inode.getType() + ")");
		
		String[] id = null;
		if(m_inode.getType().equals(INode.MONITOR))
			id = new String[] { m_inode.getSvId() };
		else
		{
			IForkNode f = (IForkNode) m_inode;
			List<String> a = f.getSonList();
			int size = a.size();
			id= new String[size];
			for (int i = 0; i < size; ++i)
			{
				String svid= a.get(i);
				id[i]= svid;
			}
		}
		return refresh(id);
	}
	
	/**
	 * ���ݴ����id ������ˢ�¼����,�÷�����������
	 * <br/> ˢ�µĽ������ѭ������ getRefreshedData ȡ��
	 * @return  ��Ϣ���е����ƣ����� getRefreshedData ʱ�봫��
	 */	
	public String refresh(String[] id)throws Exception
	{
		if( !canRefresh() )
			throw new Exception(" Refuse to refresh self-node or sub-node, id: " + m_inode.getSvId() + " (" + m_inode.getType() + ")");
		
		String idstr = "";
		int size = id.length;
		for (int i = 0; i < size; ++i)
			idstr += id[i] + ",";
			
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "RefreshMonitors");
		ndata.put("id", idstr);
		ndata.put("parentid", m_inode.getParentSvId());
		ndata.put("instantReturn", "true");
		RetMapInMap rmap= ManageSvapi.GetUnivData(ndata);
		if(!rmap.getRetbool())
			throw new Exception(" Failed to refresh Monitor, since:"+ rmap.getEstr());
	     
		String queueName="";
		Map<String, Map<String, String>> fmap= rmap.getFmap();
		if(fmap.containsKey("return"))
		{
			Map<String, String> r= fmap.get("return");
			if(r.containsKey("queueName"))
				queueName= r.get("queueName");
		}
		if(queueName.isEmpty())
			throw new Exception(" Failed to get queueName! ");
		else
		{
		    Timer timer = new Timer(true);
			timer.schedule(new DeleteQueue(queueName,timer), 180 * 1000);
		}		
		return queueName;
	}
	
	static class DeleteQueue extends java.util.TimerTask
	{
		private String queueName="";
		private Timer timer=null;
		public DeleteQueue(String name,Timer t)
		{
			queueName= name;
			timer= t;
		}
		public void run()
		{
			Map<String, String> ndata = new HashMap<String, String>();
			ndata.put("dowhat", "DeleteQueue");
			ndata.put("queuename",queueName);
			ManageSvapi.GetUnivData(ndata);		
			super.cancel();
			timer.cancel();
		}
	}
	
	/**
	 * ѭ�����ñ�������ȡ��ˢ�µĽ����ѭ�����1-3�룬��ʱ�䲻Ҫ����60�룬���������쳣���˳�ѭ��
	 * <br/> ������ 3 �ε��ú��Ѿ�ȡ��ȫ����������ڵ� 3 �ε���ʱ���׳��쳣
	 * @param  queueName ��Ϣ���е�����
	 * @return ���ص�ˢ�������ڡ�RefreshData_1, RefreshData_2... ��, ������ dstr �ȼ����Ϣ
	 */	
	public RetMapInMap getRefreshedData(String queueName)throws Exception
	{
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetRefreshed");
		ndata.put("parentid", m_inode.getParentSvId());
		ndata.put("queueName",queueName);
		RetMapInMap rmap= ManageSvapi.GetUnivData(ndata);

		if( !rmap.getRetbool() )
			Manager.instantUpdate();
		return rmap;
	}
	
	/**
	 * ��ֹ���������Ӽ�����������б༭Ȩ��
	 * <br/><br/>if (start != null && end != null) 
	 * <br/>&nbsp &nbsp  ��ʱ�ν�ֹ
	 * <br/>else
	 * <br/>&nbsp &nbsp  �����ý�ֹ
	 */	
	public boolean disableMonitor(Date start, Date end, View view)throws Exception
	{
		if(m_inode.getType().equals(INode.SE))
			throw new Exception(" Refuse to se diable, id: " + m_inode.getSvId() + " (" + m_inode.getType() + ")");
	
		String[] id = new String[] { m_inode.getSvId() };
		return disableMonitor(id,start,end,view);
	}
	/**
	 * ���ݴ����id ��������ֹ���������Ӽ�����������б༭Ȩ��
	 * <br/><br/>if (start != null && end != null) 
	 * <br/>&nbsp &nbsp  ��ʱ�ν�ֹ
	 * <br/>else
	 * <br/>&nbsp &nbsp  �����ý�ֹ
	 */		
	public boolean disableMonitor(String[] id, Date start, Date end,View view)throws Exception
	{
		if( !canEdit() )
			throw new Exception(" Refuse to edit, id: " + m_inode.getSvId() + " (" + m_inode.getType() + ")");
		if( !canDisableOrEnableMonitor(view) )
			throw new Exception(" Refuse to enable/disable so frequent, id: " + m_inode.getSvId() + " (" + m_inode.getType() + ")");
	
		Map<String, String> ndata = new HashMap<String, String>();
		String dowhat= "DisableForever";
		if (start != null && end != null)
		{
			dowhat= "DisableTemp";
			if (start.compareTo(end) >= 0)
				throw new Exception(" Date of start >= date of end! ");

			String by= new Integer(start.getYear()+1900).toString();
			String bm= new Integer(start.getMonth()+1).toString();
			String bd= new Integer(start.getDate()).toString();
			String bh= new Integer(start.getHours()).toString();
			String bmin= new Integer(start.getMinutes()).toString();
			String sv_starttime= by + "-" + bm + "-" +bd + "-" +bh + ":" +bmin;
			
			by= new Integer(end.getYear()+1900).toString();
			bm= new Integer(end.getMonth()+1).toString();
			bd= new Integer(end.getDate()).toString();
			bh= new Integer(end.getHours()).toString();
			bmin= new Integer(end.getMinutes()).toString();
			String sv_endtime= by + "-" + bm + "-" +bd + "-" +bh + ":" +bmin;
			
			ndata.put("sv_starttime", sv_starttime);
			ndata.put("sv_endtime", sv_endtime);
		}
		ndata.put("dowhat", dowhat);
		int size = id.length;
		for (int i = 0; i < size; ++i)
			ndata.put(id[i], "");
		RetMapInMap rmap= ManageSvapi.GetUnivData(ndata);
		if(!rmap.getRetbool())
			throw new Exception(" Failed to disableMonitor, since:"+ rmap.getEstr());
		
		view.putDateOfDisOrEnable(m_inode);
		Manager.instantUpdate();		
	    Timer timer = new Timer(true);
		timer.schedule(new DelayUpdate(timer), 20 * 1000);
		return true;
	}
	/**
	 * �������������Ӽ�����������б༭Ȩ��
	 */		
	public boolean enableMonitor(View view)throws Exception
	{
		if(m_inode.getType().equals(INode.SE))
			throw new Exception(" Refuse to se enable, id: " + m_inode.getSvId() + " (" + m_inode.getType() + ")");
		
		String[] id = new String[] { m_inode.getSvId() };
		return enableMonitor(id,view);
	}
	
	private void buildMonitorId(View view, String id, ArrayList<String> ids)
	{
		if(id==null || view==null)
			return;
		INode node= view.getNode(id);
		if(node==null)
			return;
		if(node.getType().equals(INode.MONITOR))
		{
			ids.add(id);
			return;
		}
		IForkNode f = (IForkNode) node;
		List<String> a = f.getSonList();
		int size = a.size();
		for (int i = 0; i < size; ++i)
			buildMonitorId(view, a.get(i), ids);
	}
	
	/**
	 * ���ݴ����id �������������������Ӽ�����������б༭Ȩ��
	 */		
	public boolean enableMonitor(String[] id,View view)throws Exception
	{
		if( !canEdit() )
			throw new Exception(" Refuse to edit, id: " + m_inode.getSvId() + " (" + m_inode.getType() + ")");		
		if( !canDisableOrEnableMonitor(view) )
			throw new Exception(" Refuse to enable/disable so frequent, id: " + m_inode.getSvId() + " (" + m_inode.getType() + ")");
		
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "Enable");
		int size = id.length;
		for (int i = 0; i < size; ++i)
			ndata.put(id[i], "");
		RetMapInMap rmap= ManageSvapi.GetUnivData(ndata);
		if(!rmap.getRetbool())
			throw new Exception(" Failed to enableMonitor, since:"+ rmap.getEstr());
		
		try
		{
			ArrayList<String> ids= new ArrayList<String>(); 
			int size2 = id.length;
			for (int i2 = 0; i2 < size2; ++i2)
				buildMonitorId(view, id[i2], ids);
			refresh( ids.toArray(new String[ids.size()]) );
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		view.putDateOfDisOrEnable(m_inode);
		Manager.instantUpdate();		
	    Timer timer = new Timer(true);
		timer.schedule(new DelayUpdate(timer), 20 * 1000);
		return true;
	}
	
	static class DelayUpdate extends java.util.TimerTask
	{
		private Timer	timer	= null;
		
		public DelayUpdate(Timer t)
		{
			timer = t;
		}
		
		public void run()
		{
			Manager.instantUpdate();
			super.cancel();
			timer.cancel();
		}
	}
	
	/**
	 * ɾ�����ڵ㼰����������
	 */	
	public boolean deleteNode(View view)throws Exception
	{
		String[] id= new String[]{m_inode.getSvId()};
		return deleteNode(id, view);
	}
	/**
	 * ���ݴ����id ������ɾ������
	 */	
	public boolean deleteNode(String[] id,View view) throws Exception
	{
		if(!m_inode.getType().equals(INode.SE) && !canDeleteNode() )
			throw new Exception(" Refuse to delete self-node or sub-node, id: " + m_inode.getSvId() + " (" + m_inode.getType() + ")");
	
		TreeSet<String> parent= new TreeSet<String>();
		TreeSet<String> parentDot= new TreeSet<String>(); 
		int size= id.length;
		if(size<1)
			throw new Exception(" node size < 1 ! ");
		String pid= m_inode.getSvId()+ ".";
		String idtodel="";
		for(int index=0; index<size; ++index)
		{
			String sonid= id[index];
			if(sonid==null || sonid.isEmpty())
				continue;
			if(!sonid.startsWith(pid) && !sonid.equals(m_inode.getSvId()))
				throw new Exception(" id:" +sonid+ " isn't sub-node of the node, id: " + m_inode.getSvId() + " (" + m_inode.getType() + ")");
			parent.add(sonid);
			parentDot.add(sonid+".");
			idtodel+= sonid+",";
		}
		if(view==null)
			throw new Exception(" view==null ! ");
		
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "DelChildren");
		ndata.put("autoDelTable", "true");
		ndata.put("parentid", idtodel);
		RetMapInMap rmap= ManageSvapi.GetUnivData(ndata);
		if(!rmap.getRetbool())
			throw new Exception(" Failed to DelChildren, since:"+ rmap.getEstr());
		
		try
		{
			String inikeyToDel="";
			IniFile userini= view.getUserIni();
			List<String> keys= userini.getKeyList(userini.getSections());
			for(String key:keys)
			{
				if(parent.contains(key))
				{
					inikeyToDel+= key+",";
					continue;
				}
				for(String pidDot:parentDot)
				{
					if(key.startsWith(pidDot))
					{
						inikeyToDel+= key+",";
						break;
					}	
				}
			}
			if (!inikeyToDel.isEmpty())
			{
				Map<String, String> ndata2 = new HashMap<String, String>();
				ndata2.put("dowhat", "DeleteIniFileKeys");
				ndata2.put("filename", "user.ini");
				ndata2.put("user", "default");
				ndata2.put("section", userini.getSections());
				ndata2.put("keys", inikeyToDel);
				ManageSvapi.GetUnivData(ndata2);
			}
			
			String iniKeyTodel;
			for (int delIndex = 0; delIndex <= 1; ++delIndex)
			{
				if(delIndex==0)
					iniKeyTodel= "groupright";
				else
					iniKeyTodel= "ungroupright";
				
				String groupright = userini.getValue(userini.getSections(), iniKeyTodel);
				String newgroupright = groupright;
				if (groupright!=null && !groupright.isEmpty())
				{
					String[] rs = groupright.split(",");
					int rssize = rs.length;
					for (int i = 0; i < rssize; ++i)
					{
						String key = rs[i];
						if (parent.contains(key))
						{
							newgroupright = newgroupright.replace(key + ",", "");
							continue;
						}
						for (String pidDot : parentDot)
						{
							if (key.startsWith(pidDot))
							{
								newgroupright = newgroupright.replace(key + ",", "");
								break;
							}
						}
					}
					if (newgroupright.compareTo(groupright) != 0)
					{
						IniFileKeyValue ini = new IniFileKeyValue("user.ini", userini.getSections(), iniKeyTodel);
						ini.setValue(newgroupright);
					}
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Manager.instantUpdate();
		return true;
	}
	
	/**
	 * �ܷ��ֹ�����ü��
	 */	
	public boolean canDisableOrEnableMonitor(View view)
	{
		if( !canEdit() )
			return false;
		
//		Date nowdate= new Date();
//		Date latestdate= view.getLatestDateOfDisOrEnable(m_inode);
//		if(latestdate==null)
//			return true;
//		
//		long t= nowdate.getTime() - latestdate.getTime();
//		if (t < 5 * 60 * 1000)
//			return false;
		return true;
	}
	
	/**
	 * ��ʾ��ֹ��������
	 */
	public String getLableofDisableOrEnable()
	{
		String dis= "��ֹ";
		if(m_raw_map==null)
			return dis;
		String v= m_raw_map.get("status_disable");
		if(v==null)
			return dis;
		if(v.contains(INode.DISABLE))
			return "����";
		return dis;
	}


}
