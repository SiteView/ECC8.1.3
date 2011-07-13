package com.siteview.base.manage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.IniFileKeyValue;
import com.siteview.base.tree.EntityNode;
import com.siteview.base.tree.GroupNode;
import com.siteview.base.tree.INode;
import com.siteview.base.tree.MonitorNode;
import com.siteview.base.tree.SeNode;

public class ServerData implements Serializable
{
	private List<String> m_tree_index = new CopyOnWriteArrayList<String>();
	private Map<String, String> m_son_id = new ConcurrentHashMap<String, String>	();
	private Map<String, Map<String, String>> m_tree_map = new ConcurrentHashMap<String, Map<String, String>>();

	// <index of ini , >
	private Map<String, Map<String, String>> m_user_ini = new ConcurrentHashMap<String, Map<String, String>>();
	private Map<String, IniFile> m_user_ini_publish = new ConcurrentHashMap<String, IniFile>();

	// <LoginName, index of ini>
	private Map<String, String> m_name_index =  new ConcurrentHashMap<String, String>();

	private Map<String, Object> m_other_data =new ConcurrentHashMap<String, Object>();
	
	
	private void setSonId()
	{
		Map<String, String> son_id= new HashMap<String, String>	();
		for (String id : m_tree_index)
		{
			try
			{
				if (id.contains("."))
				{
					String pid = id.substring(0,id.lastIndexOf("."));
					String str = null;
					String old= son_id.get(pid);
					if (old == null || old.isEmpty())
						str = id;
					else
						str = old + "," + id;
					son_id.put(pid, str);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		for (String key : son_id.keySet())
		{
			try
			{
//				logger.info(key+"'s son_id: "+son_id.get(key));
				m_son_id.put(key, son_id.get(key));
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	
	public SeNode[] getSe(String LoginName)
	{
		List<SeNode> se = new ArrayList<SeNode>();
		for (String id : m_tree_index)
		{
			if(!id.contains("."))
			{
				SeNode s= getSeNode(LoginName, id);
				if(s!=null)
					se.add(s);
			}
		}
		return (SeNode[])se.toArray(new SeNode[se.size()]);
	}
	
	public SeNode getSeNode(String LoginName, String id)
	{
		if(LoginName==null || id==null || LoginName.isEmpty() || id.isEmpty())
			return null;
		if(!canVisitNode(LoginName,id) || !m_tree_map.containsKey(id))
			return null;
		Map<String, String> data= m_tree_map.get(id);

		String type= data.get("type");
		if(type==null)
			return null;
		
		if (type.compareTo(INode.SE)==0)
		{
			SeNode node = new SeNode();
			node.setType(INode.SE);
			
			String status= data.get("status");
			if(status==null)
				status= "";
			node.setStatus(status);
			
			String sv_name= data.get("sv_name");
			if(sv_name==null)
				sv_name= "";
			node.setName(sv_name);
			
			String sonid= m_son_id.get(id);
			if(sonid==null)
				sonid= "";
			node.setSonId(sonid);
			
			node.setId(id);
			return node;
		}	
		return null;
	}
	
	public GroupNode getGroupNode(String LoginName, String id)
	{
		if(LoginName==null || id==null || LoginName.isEmpty() || id.isEmpty())
			return null;
		if(!canVisitNode(LoginName,id) || !m_tree_map.containsKey(id))
			return null;
		Map<String, String> data = m_tree_map.get(id);
		
		String type = data.get("type");
		if (type == null)
			return null;
		
		if (type.compareTo(INode.GROUP)==0)			
		{
			GroupNode node = new GroupNode();
			node.setType(INode.GROUP);
			
			String status = data.get("status");
			if (status == null)
				status = "";
			node.setStatus(status);
			
			status = data.get("status_disable");
			if(status!=null && status.contains(INode.DISABLE))
				node.setStatus(INode.DISABLE);
			
			String sv_name = data.get("sv_name");
			if (sv_name == null)
				sv_name = "";
			node.setName(sv_name);
			
			String sonid = m_son_id.get(id);
			if (sonid == null)
				sonid = "";
			node.setSonId(sonid);
			
			node.setId(id);
			return node;
		}
		return null;
	}
	
	public EntityNode getEntityNode(String LoginName, String id)
	{
		if(LoginName==null || id==null || LoginName.isEmpty() || id.isEmpty())
			return null;
		if(!canVisitNode(LoginName,id) || !m_tree_map.containsKey(id))
			return null;
		Map<String, String> data = m_tree_map.get(id);
		
		String type = data.get("type");
		if (type == null)
			return null;
		
		if (type.compareTo(INode.ENTITY)==0)		
		{
			EntityNode node = new EntityNode();
			node.setType(INode.ENTITY);
			
			String status = data.get("status");
			if (status == null)
				status = "";
			node.setStatus(status);
			
			status = data.get("status_disable");
			if(status!=null && status.contains(INode.DISABLE))
				node.setStatus(INode.DISABLE);
			
			String sv_name = data.get("sv_name");
			if (sv_name == null)
				sv_name = "";
			node.setName(sv_name);
			
			String sonid = m_son_id.get(id);
			if (sonid == null)
				sonid = "";
			node.setSonId(sonid);
			
			node.setId(id);
			return node;
		}
		return null;
	}
	public MonitorNode getMonitorNode(String LoginName, String id)
	{
		if(LoginName==null || id==null || LoginName.isEmpty() || id.isEmpty())
			return null;
		if(!canVisitNode(LoginName,id) || !m_tree_map.containsKey(id))
			return null;
		Map<String, String> data = m_tree_map.get(id);
		
		String type = data.get("type");
		if (type == null)
			return null;
		
		if (type.compareTo(INode.MONITOR)==0)		
		{
			MonitorNode node = new MonitorNode();
			node.setType(INode.MONITOR);
			
			String status = data.get("status");
			if (status == null)
				status = "";
			node.setStatus(status);
			
			String status2 = data.get("status_disable");
			if(status2!=null && status2.contains(INode.DISABLE))
				node.setStatus(INode.DISABLE);
			
			String sv_name = data.get("sv_name");
			if (sv_name == null)
				sv_name = "";
			node.setName(sv_name);
			
			node.setId(id);
			return node;
		}
		return null;
	}
	
	public INode getNode(String LoginName, String id)
	{
		if(LoginName==null || id==null || LoginName.isEmpty() || id.isEmpty())
			return null;
		if(!canVisitNode(LoginName,id) || !m_tree_map.containsKey(id))
			return null;
		Map<String, String> data= m_tree_map.get(id);

		String type= data.get("type");
		if(type==null)
			return null;
		
		if (type.compareTo(INode.SE)==0)
			return getSeNode(LoginName, id);
		else if (type.compareTo(INode.GROUP)==0)
			return getGroupNode(LoginName, id);
		else if (type.compareTo(INode.ENTITY)==0)
			return getEntityNode(LoginName, id);
		else if (type.compareTo(INode.MONITOR)==0)	
			return getMonitorNode(LoginName, id);
			
		return null;
	}
	
	public Map<String, String> getRawMapClone(String id)
	{
		if(id==null || id.isEmpty())
			return null;	
		if(!m_tree_map.containsKey(id) || m_tree_map.get(id)==null)
			return null;
		
		Map<String, String> ret= new HashMap<String, String>(m_tree_map.get(id));
		return ret;
	}
	
	public String getSonId(String id)
	{
		if(id==null || id.isEmpty())
			return "";		
		String value= m_son_id.get(id);
		if(value==null)
			return "";
		return new String(value); 
	}
	
	public boolean isAdmin(String LoginName)
	{
		if(LoginName==null || LoginName.isEmpty())
			return false;
		String index= m_name_index.get(LoginName);
		if(index==null)
			return false;
		Map<String, String> cmap= m_user_ini.get(index);
		if(cmap==null)
			return false;
		String nAdmin = cmap.get("nAdmin");
		if (nAdmin != null && nAdmin.compareTo("1") == 0)
			return true;
		else
			return false;
	}
	
	public IniFile getUserIni(String LoginName)
	{
		if(LoginName==null)
			return null;
		String index= m_name_index.get(LoginName);
		if(index==null)
			return null;
		return m_user_ini_publish.get(index);
	}
	
	
	public Map<String, IniFile> getAllUserIni()
	{
		Map<String, IniFile> a= new HashMap<String, IniFile>(m_user_ini_publish);
		return a;
	}
	
	public Map<String, String> getIniValue(String LoginName, String id)
	{
		if(LoginName==null || id==null || LoginName.isEmpty() || id.isEmpty())
			return null;
		String index= m_name_index.get(LoginName);
		if(index==null)
			return null;
		
		Map<String, String> ret = new HashMap<String, String>(); 
		
		Map<String, String> cmap= m_user_ini.get(index);
		if(cmap==null)
			return null;
		String nAdmin = cmap.get("nAdmin");
		if (nAdmin != null && nAdmin.compareTo("1") == 0)
		{
			ret.put("nAdmin","true");
			return ret;
		}
		if(!canVisitNode(LoginName,id))
			return null;
		
		String value= cmap.get(id);
		if (value != null && !value.isEmpty())
			ret.put(id, value);
		
		try
		{
			String pid = new String(id);
			while (pid.contains("."))
			{
				pid = pid.substring(0, pid.lastIndexOf("."));
				value= cmap.get(pid);
				if (value != null && !value.isEmpty())
					ret.put(pid, value);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}		
		return ret;
	}
	
	
	public boolean canVisitNode(String LoginName, String id)
	{
		if(LoginName==null || id==null || LoginName.isEmpty() || id.isEmpty())
			return false;
		String index= m_name_index.get(LoginName);
		if(index==null)
			return false;
		
		Map<String, String> cmap= m_user_ini.get(index);
		if(cmap==null)
			return false;
		String nAdmin = cmap.get("nAdmin");
		if(nAdmin!=null && nAdmin.compareTo("1")==0)
			return true;

		String nIsUse = cmap.get("nIsUse");
		if(nIsUse==null || nIsUse.compareTo("1")!=0)
			return false;
		
		String groupright = cmap.get("groupright");
		String ungroupright = cmap.get("ungroupright");
		groupright = "," + groupright + ",";
		ungroupright = "," + ungroupright + ",";
		
		String sv_id_c = "," + id + ",";
		String sv_id_c2= "," + id + ".";
		
		if (ungroupright.contains(sv_id_c))
			return false;
		if (groupright.contains(sv_id_c2))
			return true;
		if (groupright.contains(sv_id_c))
			return true;
		
		try
		{
			String pid = new String(id);
			while (pid.contains("."))
			{
				pid = pid.substring(0, pid.lastIndexOf("."));
				String pid_c = "," + pid + ",";
				if (ungroupright.contains(pid_c))
					return false;				
				if (groupright.contains(pid_c))
					return true;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	//when create a new LoginName, that must be checked whether has existed. 
	//m_name_index.get(LoginName) ,maybe null, meaning that has been deleted.
	public void updateUserIni(RetMapInMap userini)
	{
		if(!userini.getRetbool())
			return;
		
		TreeSet<String> todel= new TreeSet<String>();
		for(String key: m_user_ini.keySet())
			todel.add(key);
		
		Map<String, Map<String, String>> fmap= userini.getFmap();
		for(String index: fmap.keySet())
		{
			Map<String, String> tmap= fmap.get(index);
			if(tmap==null)
				continue;
	
			String LoginName= tmap.get("LoginName");
			if(LoginName==null || LoginName.isEmpty())
				continue;			
			String Password= tmap.get("Password");
			String nAdmin = tmap.get("nAdmin");
			String LDAPSecurityPrincipal = tmap.get("LDAPSecurityPrincipal");
			String LDAPProviderUrl = tmap.get("LDAPProviderUrl");
			if(nAdmin!=null && nAdmin.equals("1") && Password!=null)
			{
				if( (LDAPProviderUrl == null || LDAPProviderUrl.isEmpty()) && (LDAPSecurityPrincipal == null || LDAPSecurityPrincipal.isEmpty()) )
				{
					m_other_data.put("adminLoginName", LoginName);
					m_other_data.put("adminPassword", Password);
				}
			}
			
			Map<String, String> cmap= new ConcurrentHashMap<String, String>(tmap);
			m_user_ini.put(index, cmap);
			
			Map<String,String> m= new HashMap<String,String>(cmap); 
			IniFile ini= new IniFile("user.ini", index, m);
			m_user_ini_publish.put(index, ini);
			
			m_name_index.put(LoginName, index);
			todel.remove(index);
		}
		for (String key : todel)
		{
			m_user_ini.remove(key);
			m_user_ini_publish.remove(key);
		}
	}

	public List<Map<String, String>> getRawTreeData()
	{
		if (m_other_data.containsKey("GetTreeData"))
			return (List<Map<String, String>>) m_other_data.get("GetTreeData");
		else
			return null;
	}
	
	public void updateTreeData(RetMapInVector tree,  Map<String, Map<String, String>> plusInfo)
	{
		if(tree.getRetbool()==false)
			return;
		
		TreeSet<String> todel= new TreeSet<String>(m_tree_index);
		CopyOnWriteArrayList<String> temp_index= new CopyOnWriteArrayList<String>();
		
		List<Map<String, String>> vmap= tree.getVmap();
		m_other_data.put("GetTreeData", vmap);
		int size= vmap.size();
		for (int key = 0; key < size; ++key)
		{
			Map<String, String> ndata = vmap.get(key);
			if (ndata.containsKey("sv_id"))
			{
				String id = ndata.get("sv_id");
				temp_index.add(id);
				setPlusInfo(id, ndata, plusInfo);
				Map<String, String> newdata = new ConcurrentHashMap<String, String>(ndata);
				m_tree_map.put(id, newdata);
				todel.remove(id);
			}
			Thread.yield();		
		}
		m_tree_index = temp_index;
		setTreeDelete( todel );
		setSonId();
		setUsedPointAndNetWork();
	}
	
	private void setPlusInfo(String id, Map<String, String> ndata, Map<String, Map<String, String>> plusInfo)
	{
		Map<String, String> pdata= plusInfo.get(id);
		if(pdata==null)
			return;
		for(String key: pdata.keySet())
		{
			if(key.equals("needtype"))
				continue;
			String value= pdata.get(key);
			if(value==null || value.isEmpty())
				continue;
			ndata.put(key, value);
		}
//		set_sv_dependson_svname(ndata);
	}
	
	private void set_sv_dependson_svname(Map<String, String> ndata)
	{
		String sv_dependson = ndata.get("sv_dependson");
		if (sv_dependson == null || sv_dependson.isEmpty())
			return;
		
		String depend = null;
		try
		{
			String pid = new String(sv_dependson);
			while (true)
			{
				Map<String, String> pdata = m_tree_map.get(pid);
				if ( !m_tree_map.containsKey(pid) || pdata == null)
					return;

				String sv_name = pdata.get("sv_name");
				if (sv_name == null)
					sv_name = "";
				if (depend==null)
					depend = sv_name;
				else
					depend = sv_name + ":" + depend;
				
				if (pid.contains("."))
					pid = pid.substring(0, pid.lastIndexOf("."));
				else
					break;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (depend != null && !depend.isEmpty())
			ndata.put("sv_dependson_svname", depend);
	}
	
	private void setTreeDelete(TreeSet<String> index)
	{
		for (String id : index)
		{
			m_tree_map.remove(id);
			m_son_id.remove(id);
		}
	}
	
	public boolean setCRUDofNode(String LoginName, INode node) throws Exception
	{		
		IniFile useini= getUserIni(LoginName);
		String index= useini.getSections();
		String groupright= useini.getValue(index, "groupright");
		String ungroupright= useini.getValue(index, "ungroupright");
		String check_groupright = "," + groupright + ",";
		String check_ungroupright = "," + ungroupright + ",";
		
		String newg=null;
		String pid = new String(node.getSvId());
		while (pid.contains("."))
		{
			String checkid= "," + pid + ",";
			if (check_ungroupright.contains(checkid))
			{
				if (groupright.endsWith(","))
					newg = groupright + node.getSvId() + ",";
				else
					newg = groupright + "," + node.getSvId() + ",";
				return setUserIniValue(index, "groupright", newg, node);
			}
			pid = pid.substring(0, pid.lastIndexOf("."));
		}
		pid = new String(node.getSvId());
		while (pid.contains("."))
		{
			String checkid= "," + pid + ",";
			if (check_groupright.contains(checkid))
				return setUserIniValue(index, null, null, node);
			pid = pid.substring(0, pid.lastIndexOf("."));
		}
		
		if (groupright.endsWith(","))
			newg = groupright + node.getSvId() + ",";
		else
			newg = groupright + "," + node.getSvId() + ",";
		return setUserIniValue(index, "groupright", newg, node);
	}
	
	private boolean setUserIniValue(String section, String key, String value, INode node) throws Exception
	{
		if(section==null || section.isEmpty())
			return false;
		
		if(key!=null && value!=null)
		{
			IniFileKeyValue kv= new IniFileKeyValue("user.ini", section, key);
			kv.setValue(value);
		}
		String right= "editmonitor=1,delmonitor=1,monitorrefresh=1,";
		if(node.getType().equals(INode.ENTITY) || node.getType().equals(INode.GROUP))
			right= "addmonitor=1,editdevice=1,testdevice=1,deldevice=1,devicerefresh=1," + right;
		if(node.getType().equals(INode.GROUP))
			right= "addsongroup=1,adddevice=1,editgroup=1,copydevice=1,delgroup=1,grouprefresh=1," + right;
		
		IniFileKeyValue ini= new IniFileKeyValue("user.ini", section, node.getSvId());
		ini.setValue(right);
		return true;
	}
	
	private int getValueInRMap(Map<String, String> rmap, String key)
	{
		if(rmap==null || key==null)
			return 0;
		String v= rmap.get(key);
		if(v==null)
			return 0;
		Integer i= new Integer(0);
		try
		{
			i= new Integer(v);
		} catch (Exception e)
		{
		}
		return i;
	}
	
	private void setUsedPointAndNetWork()
	{
		int pointUsed=0;
		int networkUsed=0;
		for (Iterator i = m_tree_index.iterator(); i.hasNext();)
		{
			String id = (String) i.next();
			if (!id.contains(".") && m_tree_map.containsKey(id))
			{
				Map<String, String> data = m_tree_map.get(id);
				if (data == null)
					continue;
				String type = data.get("type");
				if (type == null)
					continue;
				if (type.equals(INode.SE))
				{
					Map<String, String> rmap= getRawMapClone(id);
					int sub_monitor_sum= getValueInRMap(rmap, "sub_monitor_sum");
					int sub_point_reduce_sum= getValueInRMap(rmap, "sub_point_reduce_sum");
					int sub_network_sum= getValueInRMap(rmap, "sub_network_sum");
					pointUsed+= (sub_monitor_sum-sub_point_reduce_sum);
					networkUsed+= sub_network_sum;
				}
			}
		}
		m_other_data.put("pointUsed", new Integer(pointUsed));
		m_other_data.put("networkUsed", new Integer(networkUsed));
	}
	
	public Integer getPointUsed()
	{
		Integer i= -1;
		try
		{
			i= (Integer)m_other_data.get("pointUsed");
			if (i == null) i = -1;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return i;
	}
	
	public Integer getNetWorkUsed()
	{
		Integer i= new Integer(-1);
		try
		{
			i= (Integer)m_other_data.get("networkUsed");
			if (i == null) i = -1;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return i;
	}
	
	public void getAdminNamePWD(StringBuilder name, StringBuilder pwd)
	{
		Object n= m_other_data.get("adminLoginName");
		Object p= m_other_data.get("adminPassword");
		if(n!=null)
			name.append(n);
		if(p!=null)
			pwd.append(p);
	}
	
}
