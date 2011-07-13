package com.siteview.base.manage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.tree.EntityNode;
import com.siteview.base.tree.GroupNode;
import com.siteview.base.tree.IForkNode;
import com.siteview.base.tree.INode;
import com.siteview.base.tree.MonitorNode;
import com.siteview.base.tree.SeNode;
import com.siteview.base.treeInfo.EntityInfo;
import com.siteview.base.treeInfo.GroupInfo;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.base.treeInfo.SeInfo;

public class ProfileData implements Serializable
{
	private String m_LoginName;
	private String m_session;
	private Map<String, String> m_login_data = new ConcurrentHashMap<String, String>();
	
	// such as ("login_date",Date), ("client_ip",String) ("absent_times",Integer)
	private Map<String,Object>		m_other_info= new ConcurrentHashMap<String,Object>();  
	
	private Map<String,Date>			m_latest_DisOrEnable= new ConcurrentHashMap<String,Date>();
	private Map<String,RetMapInMap>	m_test_device_data= new ConcurrentHashMap<String,RetMapInMap>();
	private Map<String,RetMapInMap>	m_dynamic_data= new ConcurrentHashMap<String,RetMapInMap>();

	private List<ChangeDetailEvent>	m_tree_change_event= new CopyOnWriteArrayList<ChangeDetailEvent>();
	private List<String>			m_tree_change_id= new CopyOnWriteArrayList<String>(); 	
	private TreeSet<String>							m_tree_id,m_tree_id_latest;
	private List<SeNode>			m_senode,m_senode_latest;
	private Map<String, INode>		m_inode;
	private Map<String, INode>     m_inode_latest=new ConcurrentHashMap<String, INode>();
	
	private List<String>			m_focus_id= new CopyOnWriteArrayList<String>();          
	private List<String>			m_treeinfo_change_id= new CopyOnWriteArrayList<String>();
	
	private Map<String, SeInfo>		m_seinfo = new ConcurrentHashMap<String, SeInfo>();
	private Map<String, SeInfo>		m_seinfo_latest = new ConcurrentHashMap<String, SeInfo>();
	private Map<String, GroupInfo>	m_groupinfo = new ConcurrentHashMap<String, GroupInfo>();
	private Map<String, GroupInfo>	m_groupinfo_latest = new ConcurrentHashMap<String, GroupInfo>();
	private Map<String, EntityInfo>	m_entityinfo = new ConcurrentHashMap<String, EntityInfo>();
	private Map<String, EntityInfo>	m_entityinfo_latest = new ConcurrentHashMap<String, EntityInfo>();
	private Map<String, MonitorInfo>	m_monitorinfo = new ConcurrentHashMap<String, MonitorInfo>();
	private Map<String, MonitorInfo>	m_monitorinfo_latest = new ConcurrentHashMap<String, MonitorInfo>();
	
	private ServerData	m_server_data;
	
	public ProfileData(ServerData sd, String LoginName, String session, Map<String, String> ldata)
	{
		this.m_server_data = sd;
		this.m_LoginName = LoginName;
		this.m_session = session;
		m_login_data.putAll(ldata);
	}
	
	public String getLoginName()
	{
		return m_LoginName;
	}
	
	public String getSession()
	{
		return m_session;
	}
	

	
	public INode[] getSe()
	{
		if(m_senode_latest==null)
			return null;
		return (SeNode[])m_senode_latest.toArray(new SeNode[m_senode_latest.size()]);
	}
	
	public INode getNode(String id)
	{
		if(id==null || id.isEmpty())
			return null;	
		if( !id.contains(".") )
			return getSeNode(id);		
		if(m_inode_latest==null)
			return null;
		return m_inode_latest.get(id);
	}

	public SeNode getSeNode(String id)
	{
		if(id==null || id.isEmpty())
			return null;
		if(id.contains("."))
			return null;
		if(m_senode_latest==null)
			return null;		
		
		int size = m_senode_latest.size();
		for (int i = 0; i < size; ++i)
		{
			SeNode tn = m_senode_latest.get(i);
			if(tn==null)
				continue;
			String tid = tn.getSvId();
			if (tid == null)
				continue;
			if (tid.compareTo(id) == 0)
				return tn;
		}
		return null;
	}
	
	public GroupNode getGroupNode(String id)
	{
		if(id==null || id.isEmpty())
			return null;
		if(m_inode_latest==null)
			return null;
		INode node= m_inode_latest.get(id);
		if(node==null || node.getType().compareTo(INode.GROUP)!=0 )
			return null;
		return (GroupNode)node;	
	}

	public EntityNode getEntityNode(String id)
	{
		if(id==null || id.isEmpty())
			return null;
		if(m_inode_latest==null)
			return null;
		INode node= m_inode_latest.get(id);
		if(node==null || node.getType().compareTo(INode.ENTITY)!=0 )
			return null;
		return (EntityNode)node;
	}

	public MonitorNode getMonitorNode(String id)
	{
		if(id==null || id.isEmpty())
			return null;
		if(m_inode_latest==null)
			return null;
		INode node= m_inode_latest.get(id);
		if(node==null || node.getType().compareTo(INode.MONITOR)!=0 )
			return null;
		return (MonitorNode)node;
	}
	
	public SeInfo GetSeInfo(INode node)
	{
		if(node==null || m_seinfo_latest==null)
			return null;
		SeInfo e = m_seinfo_latest.get(node.getSvId());
		if(e==null || e.getType().compareTo(INode.SE)!=0 )
			return null;	
		return e;
	}
	
	public GroupInfo GetGroupInfo(INode node)
	{
		if(node==null || m_groupinfo_latest==null)
			return null;
		GroupInfo e = m_groupinfo_latest.get(node.getSvId());
		if(e==null || e.getType().compareTo(INode.GROUP)!=0 )
			return null;	
		return e;
	}
	
	public EntityInfo GetEntityInfo(INode node)
	{
		if(node==null || m_entityinfo_latest==null)
			return null;
		EntityInfo e = m_entityinfo_latest.get(node.getSvId());
		if(e==null || e.getType().compareTo(INode.ENTITY)!=0 )
			return null;	
		return e;
	}
	
	public MonitorInfo GetMonitorInfo(INode node)
	{
		if(node==null || m_monitorinfo_latest==null)
			return null;
		MonitorInfo e = m_monitorinfo_latest.get(node.getSvId());
		if(e==null || e.getType().compareTo(INode.MONITOR)!=0 )
			return null;	
		return e;
	}
	
	
	
	public void setVisit()
	{
		m_other_info.put("absent_times",Integer.valueOf(0));
	}
	
	public int getHowManyTimesOfAbsent()
	{
		if(!m_other_info.containsKey("absent_times"))
			return 0;
		Integer t=(Integer)m_other_info.get("absent_times");
		return t.intValue();
	}
	
	public void setOnceMoreAbsent()
	{
		if(!m_other_info.containsKey("absent_times"))
			m_other_info.put("absent_times",Integer.valueOf(0));
		
		Integer t= (Integer)m_other_info.get("absent_times");
		++t;
		m_other_info.put("absent_times",t);
	}
	
	public void setFocusNode(String[] ids)
	{
		CopyOnWriteArrayList<String> fid = new CopyOnWriteArrayList<String>();
		if (ids != null)
		{
			int size = ids.length;
			for (int i = 0; i < size; ++i)
			{
				String id = ids[i];
				if (id != null && !id.isEmpty())
					fid.add(id);
			}
		}
		m_focus_id.retainAll(fid);		
		m_focus_id.addAll(fid);
		updateTreeInfo();
	}
	
	public List<ChangeDetailEvent> getTreeChange()
	{
		setVisit();

		Object obj= m_other_info.get("tree_got_news");
		if(obj==null)
			return null;
		Boolean has= (Boolean)obj;
		if(has.booleanValue()==false)
			return null;

		m_senode= m_senode_latest;
		m_inode= m_inode_latest;
		m_tree_id= m_tree_id_latest;
		
		if(m_tree_change_event==null || m_tree_change_event.isEmpty())
			return null;
//		ArrayList<String> a= new ArrayList<String>(m_tree_change_id);
		ArrayList<ChangeDetailEvent> av= new ArrayList<ChangeDetailEvent>(m_tree_change_event);	
		m_tree_change_id.clear();
		m_tree_change_event.clear();
		m_other_info.put("tree_got_news", Boolean.FALSE);
		return av;		
	}
	
	public List<String> getTreeInfoChange()
	{
		setVisit();
		
		Object obj= m_other_info.get("treeinfo_got_news");
		if(obj==null)
			return null;
		Boolean has= (Boolean)obj;
		if(has.booleanValue()==false)
			return null;

		if(m_treeinfo_change_id==null || m_treeinfo_change_id.isEmpty())
			return null;
		
		m_seinfo.clear();
		m_seinfo.putAll(m_seinfo_latest);
		
		m_groupinfo.clear();
		m_groupinfo.putAll(m_groupinfo_latest);    
		m_entityinfo.clear();
		m_entityinfo.putAll(m_entityinfo_latest);  
		m_monitorinfo.clear();
		m_monitorinfo.putAll(m_monitorinfo_latest);
		
		ArrayList<String> a= new ArrayList<String>(m_treeinfo_change_id);
		m_treeinfo_change_id.clear();
		m_other_info.put("treeinfo_got_news", Boolean.valueOf(false));
		return a;	
	}
	
	private INode getNodeForEvent(CopyOnWriteArrayList<SeNode> senode, Map<String, INode> inode, String id)
	{
		if(id==null || id.isEmpty())
			return null;	
		if( !id.contains(".") )
			return getSeNodeForEvent(senode, id);		
		if(inode==null)
			return null;
		return inode.get(id);
	}

	private SeNode getSeNodeForEvent(CopyOnWriteArrayList<SeNode> senode, String id)
	{
		if(id==null || id.isEmpty())
			return null;
		if(id.contains("."))
			return null;
		if(senode==null)
			return null;		
		
		int size = senode.size();
		for (int i = 0; i < size; ++i)
		{
			SeNode tn = senode.get(i);
			if(tn==null)
				continue;
			String tid = tn.getSvId();
			if (tid == null)
				continue;
			if (tid.equals(id))
				return tn;
		}
		return null;
	}
	
	private INode getNodeOldForEvent(String id)
	{
		if(id==null || id.isEmpty())
			return null;	
		if( !id.contains(".") )
			return getSeNodeOldForEvent(id);		
		if(m_inode==null)
			return null;
		return m_inode.get(id);
	}

	private SeNode getSeNodeOldForEvent(String id)
	{
		if(id==null || id.isEmpty())
			return null;
		if(id.contains("."))
			return null;
		if(m_senode==null)
			return null;		
		
		int size = m_senode.size();
		for (int i = 0; i < size; ++i)
		{
			SeNode tn = m_senode.get(i);
			if(tn==null)
				continue;
			String tid = tn.getSvId();
			if (tid == null)
				continue;
			if (tid.compareTo(id) == 0)
				return tn;
		}
		return null;
	}
	
	public void updateTree()
	{
		TreeSet<String>	tree_index= new TreeSet<String>();
		CopyOnWriteArrayList<String> chid= new CopyOnWriteArrayList<String>();
		CopyOnWriteArrayList<SeNode> senode= new CopyOnWriteArrayList<SeNode>();
		Map<String, INode> inode= new HashMap<String, INode>();
		buildTree(chid, tree_index, senode, inode);
		setDeleteList(chid, tree_index);
		
		List<ChangeDetailEvent>	change_event= new ArrayList<ChangeDetailEvent>();
		for(String id:chid)
		{
			INode oldnode= getNodeOldForEvent(id);
			INode newnode= getNodeForEvent(senode, inode, id);
			ChangeDetailEvent event= new ChangeDetailEvent();
			event.setSvid(id);
			
			if(oldnode!=null && newnode!=null)
			{
				event.setData(newnode);
				event.setType(ChangeDetailEvent.TYPE_MODIFY);
				change_event.add(event);
				continue;
			}
			if(oldnode==null && newnode!=null)
			{
				event.setData(newnode);
				event.setType(ChangeDetailEvent.TYPE_ADD);
				change_event.add(event);
				continue;
			}
			if(oldnode!=null && newnode==null)
			{
				event.setData(oldnode);
				event.setType(ChangeDetailEvent.TYPE_DELETE);
				change_event.add(event);
				continue;
			}
		}
		
		if(chid.isEmpty())
		{	
			m_tree_change_id.clear();
			m_tree_change_event.clear();
		}
		else
		{
			m_tree_change_id.retainAll(chid);			
			m_tree_change_id.addAll(chid);
			m_tree_change_event.retainAll(change_event);
			m_tree_change_event.addAll(change_event);
		}
		m_senode_latest= senode;
		m_inode_latest.clear();
		m_inode_latest.putAll(inode);
		m_tree_id_latest= tree_index;
		
		m_other_info.put("tree_got_news", Boolean.valueOf(true));
	}
	
	public void updateTreeInfo()
	{
		CopyOnWriteArrayList<String> chid= new CopyOnWriteArrayList<String>();
		for(String id: m_focus_id)
		{
			INode node= m_server_data.getNode(m_LoginName, id);
			if(node==null)
				continue;
			
			if(node.getType().compareTo(INode.SE)==0)
			{
				if(SeInfoChange(node))
					chid.add(id);
			} else if(node.getType().compareTo(INode.GROUP)==0)
			{
				if(GroupInfoChange(node))
					chid.add(id);	
			}else if(node.getType().compareTo(INode.ENTITY)==0)
			{
				if(EntityInfoChange(node))
					chid.add(id);	
			}else if(node.getType().compareTo(INode.MONITOR)==0)
			{
				if(MonitorInfoChange(node))
					chid.add(id);	
			}
		}

		m_treeinfo_change_id.clear();
		if (!chid.isEmpty())
		{
			m_treeinfo_change_id.retainAll(chid);			
			m_treeinfo_change_id.addAll(chid);
			m_other_info.put("treeinfo_got_news", Boolean.valueOf(true));
		}
	}
	
	private void buildTree(CopyOnWriteArrayList<String> chid, TreeSet<String> tree_index, CopyOnWriteArrayList<SeNode> senode, Map<String, INode> inode)
	{
		SeNode[] s = m_server_data.getSe(m_LoginName);
		if(s==null)
			return;
		int size = s.length;
		for (int i = 0; i < size; ++i)
		{
			if(s[i]==null)
				continue;
			tree_index.add(s[i].getSvId());			
			senode.add(s[i]);
			setChangeList(chid,s[i]);
			buildNode(chid, tree_index, inode, s[i]);
		}
	}
	
	private void buildNode(CopyOnWriteArrayList<String> chid, TreeSet<String> tree_index, Map<String, INode> inode, INode n)
	{
		Thread.yield();
		if (n.getType().compareTo(INode.MONITOR)==0)
			return;
		
		IForkNode f = (IForkNode) n;
		List<String> a = f.getSonList();
		if(a==null)
			return;
		
		int size = a.size();
		for (int i = 0; i < size; ++i)
		{
			String id= a.get(i);
			INode node = m_server_data.getNode(m_LoginName,id);
			if (node != null)
			{
				tree_index.add(id);				
				inode.put(id, node);
				setChangeList(chid,node);
				buildNode(chid, tree_index, inode, node);				
			}
		}
	}
	
	private void setChangeList(CopyOnWriteArrayList<String> chid,INode node)
	{
		String id= node.getSvId();
		if(id==null)
			return;
		String type= node.getType();
		if(type==null)
			return;
		
		INode oldn=null;
		if (type.compareTo(INode.SE) == 0)
		{
			if (m_senode != null)
			{
				int size = m_senode.size();
				for (int i = 0; i < size; ++i)
				{
					INode tn = m_senode.get(i);
					String tid = tn.getSvId();
					if (tid == null)
						continue;
					if (tid.compareTo(id) == 0)
					{
						oldn = tn;
						break;
					}
				}
			}
		}
		else if(m_inode!=null)
			oldn= m_inode.get(id);
		
		if(INodeChange(oldn,node))
			chid.add(id);
	}
	
	private void setDeleteList(CopyOnWriteArrayList<String> chid, TreeSet<String> tree_index)
	{
		if(m_tree_id==null)
			return;
		for(String id:m_tree_id)
		{
			if( !tree_index.contains(id) )
				chid.add(id);
		}
	}
	
	private boolean INodeChange(INode n1, INode n2)
	{
		if(n1==null || n2==null)
			return true;
		if(n1.getType().compareTo(n2.getType())!=0)
			return true;
		if(n1.getName().compareTo(n2.getName())!=0)
			return true;	
		if(n1.getStatus().compareTo(n2.getStatus())!=0)
			return true;
		return false;
	}
	
	private boolean SeInfoChange(INode node)
	{
		if(node==null)
			return false;
		String id= node.getSvId();
		if(id==null || id.isEmpty())
			return false;
		String type= node.getType();
		if(type==null || type.compareTo(INode.SE)!=0)
			return false;
		
		SeInfo e= new SeInfo(node);
		Map<String, String> inivalue= m_server_data.getIniValue(m_LoginName, e.getSvId());
		if(inivalue==null)
			return false;
		e.setIniValue(inivalue);
		e.setRawMap(m_server_data.getRawMapClone(e.getSvId()));
		
		boolean bret= false;
		SeInfo eold= m_seinfo.get(id);
		if(eold==null)
			bret= true;
		if(!bret)
			bret= e.change(eold); 

		m_seinfo_latest.put(id, e);
		return bret;
	}
	
	private boolean GroupInfoChange(INode node)
	{
		if(node==null)
			return false;
		String id= node.getSvId();
		if(id==null || id.isEmpty())
			return false;
		String type= node.getType();
		if(type==null || type.compareTo(INode.GROUP)!=0)
			return false;
		
		GroupInfo e= new GroupInfo(node);
		Map<String, String> inivalue= m_server_data.getIniValue(m_LoginName, e.getSvId());
		if(inivalue==null)
			return false;
		e.setIniValue(inivalue);
		e.setRawMap(m_server_data.getRawMapClone(e.getSvId()));
		
		boolean bret= false;
		GroupInfo eold= m_groupinfo.get(id);
		if(eold==null)
			bret= true;
		if(!bret)
			bret= e.change(eold); 
		
		m_groupinfo_latest.put(id, e);
		return bret;
	}
	private boolean EntityInfoChange(INode node)
	{
		if(node==null)
			return false;
		String id= node.getSvId();
		if(id==null || id.isEmpty())
			return false;
		String type= node.getType();
		if(type==null || type.compareTo(INode.ENTITY)!=0)
			return false;
		
		EntityInfo e= new EntityInfo(node);
		Map<String, String> inivalue= m_server_data.getIniValue(m_LoginName, e.getSvId());
		if(inivalue==null)
			return false;
		e.setIniValue(inivalue);		
		e.setRawMap(m_server_data.getRawMapClone(e.getSvId()));
		
		boolean bret= false;
		EntityInfo eold= m_entityinfo.get(id);
		if(eold==null)
			bret= true;
		if(!bret)
			bret= e.change(eold); 

		m_entityinfo_latest.put(id, e);
		return bret;
	}
	private boolean MonitorInfoChange(INode node)
	{
		if(node==null)
			return false;
		String id= node.getSvId();
		if(id==null || id.isEmpty())
			return false;
		String type= node.getType();
		if(type==null || type.compareTo(INode.MONITOR)!=0)
			return false;
		
		MonitorInfo e= new MonitorInfo(node);
		Map<String, String> inivalue= m_server_data.getIniValue(m_LoginName, e.getSvId());
		if(inivalue==null)
			return false;
		e.setIniValue(inivalue);		
		e.setRawMap(m_server_data.getRawMapClone(e.getSvId()));
		
		boolean bret= false;
		MonitorInfo eold= m_monitorinfo.get(id);
		if(eold==null)
			bret= true;
		if(!bret)
			bret= e.change(eold); 

		m_monitorinfo_latest.put(id, e);
		return bret;
	}
	
	public Map<String, String> getLoginData()
	{
		Map<String, String> ret= new HashMap<String, String>(m_login_data);
		return ret;
	}
	
	public Date getLatestDateOfDisOrEnable(INode node)
	{
		return m_latest_DisOrEnable.get(node.getSvId());
	}
	public void putDateOfDisOrEnable(INode node)
	{
		Date d= new Date();
		m_latest_DisOrEnable.put(node.getSvId(),d);
	}
	
	public RetMapInMap getTestDeviceData(INode node)
	{
		return m_test_device_data.get(node.getSvId());
	}
	public void putTestDeviceData(INode node, RetMapInMap data)
	{
		if(data==null)
			m_test_device_data.remove(node.getSvId());
		else
			m_test_device_data.put(node.getSvId(),data);
	}
	
	public RetMapInMap getDynamicData(INode node)
	{
		return m_dynamic_data.get(node.getSvId());
	}
	public void putDynamicData(INode node, RetMapInMap data)
	{
		if(data==null)
			m_dynamic_data.remove(node.getSvId());
		else
			m_dynamic_data.put(node.getSvId(),data);
	}
}
