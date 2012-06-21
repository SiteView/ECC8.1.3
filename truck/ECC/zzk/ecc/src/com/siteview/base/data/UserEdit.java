package com.siteview.base.data;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.siteview.base.manage.Manager;
import com.siteview.base.tree.INode;
import com.siteview.svdb.UnivData;

public class UserEdit
{
	private final static Logger logger = Logger.getLogger(UserEdit.class);
	private IniFile	m_ini;
	private boolean	m_just_create	= false;
	
	public UserEdit(IniFile ini)
	{
		if (ini == null)
			m_just_create = true;
		m_ini= ini;
	}
	
	public String getIndexInUserini()
	{
		if(m_ini==null)
			return "";
		return m_ini.getSections();
	}
	
	/**
	 * 该用户是否刚创建，尚未保存到服务器 
	 */
	public boolean isJustCreate()
	{
		return m_just_create;
	}
	
	public void display()
	{
		if(m_ini==null)
			return;
		
		m_ini.display();
	}
	
	public String getLoginName()
	{
		if(m_ini==null)		
			return "";
		return m_ini.getValue(m_ini.getSections(), "LoginName");
	}
	
	public String getUserName()
	{
		if(m_ini==null)		
			return "";
		return m_ini.getValue(m_ini.getSections(), "UserName");
	}

	/**
	 * 该用户是否管理员 
	 */
	public boolean isAdmin()
	{
		if(m_ini==null)		
			return false;
		String nAdmin = m_ini.getValue(m_ini.getSections(),"nAdmin");
		if (nAdmin != null && nAdmin.compareTo("1") == 0)
			return true;
		return false;
	}
	
	/**
	 * 该用户是否启用着
	 */
	public boolean isEnable()
	{
		if(m_ini==null)		
			return false;
		String n = m_ini.getValue(m_ini.getSections(),"nIsUse");
		if (n != null && n.compareTo("1") == 0)
			return true;
		return false;
	}

	public void setLoginName(String LoginName) throws Exception
	{
		m_ini.setKeyValue(m_ini.getSections(), "LoginName", LoginName);
	}
	
	public void setUserName(String UserName) throws Exception
	{
		m_ini.setKeyValue(m_ini.getSections(), "UserName", UserName);
	}
	
	public void setEnable(boolean enable) throws Exception
	{
		if(enable)
			m_ini.setKeyValue(m_ini.getSections(), "nIsUse", 1);
		else
			m_ini.setKeyValue(m_ini.getSections(), "nIsUse", -1);
	}
		
	public void setPassWord(String password) throws Exception
	{
		if(password==null)
			throw new Exception(" Password is null! ");
		String retPassword = UnivData.encrypt(password);
		m_ini.setKeyValue(m_ini.getSections(), "Password", retPassword == null ? "" : retPassword);
	}
	

	
	/**
	 * LDAPProviderUrl设置
	 * @param providerUrl
	 * @throws Exception
	 */
	public void setLDAPProviderUrl(String providerUrl) throws Exception
	{
		if(providerUrl == null || providerUrl.isEmpty()){
			throw new Exception("providerUrl is null!");
		}
		m_ini.setKeyValue(m_ini.getSections(), "LDAPProviderUrl", providerUrl);
	}
	/**
	 * LDAPSecurityPrincipal设置
	 * @param securityPrincipal
	 * @throws Exception
	 */
	public void setLDAPSecurityPrincipal(String securityPrincipal) throws Exception
	{
		if(securityPrincipal == null || securityPrincipal.isEmpty()){
			throw new Exception("securityPrincipal is null!");
		}
		m_ini.setKeyValue(m_ini.getSections(), "LDAPSecurityPrincipal", securityPrincipal);
	}
	
	
	
	/**
	 * 把修改保存到服务器上
	 */
	public boolean saveChange() throws Exception
	{
		List<String> ks= m_ini.getKeyList(m_ini.getSections());
		for(String key:ks)
		{
			if (key.startsWith("m_") || key.equals("nIsUse") || key.equals("nAdmin") )
			{
				String v= m_ini.getValue(m_ini.getSections(), key);
				m_ini.setKeyValue(m_ini.getSections(), key, new Integer(v).intValue());
			}
		}
		m_ini.saveChange();
		m_just_create= false;
		Manager.instantUpdate();
		return true;
	}
	
	
	
	/**
	 * 设置拓扑视图等模块的权限。 管理员总是拥有所有模块的权限
	 */
	public void setModuleRight(String key, boolean hasRight) throws Exception
	{
		if(!key.startsWith("m_"))
			throw new Exception(" Can't set this key: " + key + " . Can only start With m_ !");
		if(isAdmin())
			return;
		if (hasRight)
			m_ini.setKeyValue(m_ini.getSections(), key, 1);
		else
			m_ini.setKeyValue(m_ini.getSections(), key, -1);
	}
	
	/**
	 * 获取 拓扑视图等模块的权限。 管理员总是拥有所有模块的权限
	 */
	public boolean getModuleRight(String key)
	{
		if(!key.startsWith("m_")){
			return false;
		}
		if(isAdmin()){
			return true;
		}
		String value = m_ini.getValue(m_ini.getSections(), key);

		if(value == null){
			return false;
		}
		if("1".equals(value.trim())){
			return true;
		}else
		{
			return false;
		}
	}
	/**
	 * 检查某节点是否可见
	 */
	public boolean checkNodeVisible(INode node)
	{
		if(isAdmin())
			return true;
		String index= m_ini.getSections();
		String groupright= m_ini.getValue(index, "groupright");
		String ungroupright= m_ini.getValue(index, "ungroupright");
		groupright = "," + groupright + ",";
		ungroupright = "," + ungroupright + ",";
		
		String sv_id_c = "," + node.getSvId() + ",";	
		if (ungroupright.contains(sv_id_c))
			return false;
		if (groupright.contains(sv_id_c))
			return true;
		
		try
		{
			String pid = new String(node.getSvId());
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
	
	/**
	 * 设置某节点是否可见。管理员总是可见所有节点
	 */
	public void setNodeVisible(INode node, boolean visible) throws Exception
	{
		if(isAdmin())
			return;
		try
		{
			if (!visible)
				setNodeEditRight(node, null);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		String index= m_ini.getSections();
		String groupright= m_ini.getValue(index, "groupright");
		String ungroupright= m_ini.getValue(index, "ungroupright");
		if(groupright==null)
			groupright="";
		if(ungroupright==null)
			ungroupright="";
		String check_groupright = "," + groupright + ",";
		String check_ungroupright = "," + ungroupright + ",";
		
		String newung=null;
		String checkid = "," + new String(node.getSvId()) + ",";
		if (check_ungroupright.contains(checkid))
		{
			if(!visible)
				return;
			else
			{
				String s1= "," + new String(node.getSvId());
				if(ungroupright.endsWith(s1))
					ungroupright+= ",";

				String s2= new String(node.getSvId()) + "," ;
				if(ungroupright.startsWith(s2))
					ungroupright= "," + ungroupright;
				
				if(ungroupright.contains(checkid))
					newung= ungroupright.replace(checkid, ",");
				else
					throw new Exception(" Something wrong in setNodeVisible_1 , sv_id: " + node.getSvId() + "  visible:"+ visible);
			}
		}
		if(newung!=null)
			m_ini.setKeyValue(m_ini.getSections(), "ungroupright", newung);

		String newg=null;
		String pid = new String(node.getSvId());
		while (pid.contains("."))
		{
			checkid= "," + pid + ",";
			if (check_groupright.contains(checkid))
			{
				if(visible)
					return;
				else
				{
					if(pid.equals(node.getSvId()))
					{
						String s1= "," + new String(node.getSvId());
						if(groupright.endsWith(s1))
							groupright+= ",";

						String s2= new String(node.getSvId()) + "," ;
						if(groupright.startsWith(s2))
							groupright= "," + groupright;
						
						if (groupright.contains(checkid))
						{
							newg = groupright.replace(checkid, ",");
							m_ini.setKeyValue(m_ini.getSections(), "groupright", newg);
						}
						else
							throw new Exception(" Something wrong in setNodeVisible_2 , sv_id: " + node.getSvId() + "  visible:"+ visible);
					}
					else
					{
						if (ungroupright.endsWith(","))
							newung = ungroupright + node.getSvId() + ",";
						else
							newung = ungroupright + "," + node.getSvId() + ",";
						
						m_ini.setKeyValue(m_ini.getSections(), "ungroupright", newung);
						return;
					}
				}
			}
			pid = pid.substring(0, pid.lastIndexOf("."));
		}
		if(visible)
		{
			if (groupright.endsWith(","))
				newg = groupright + node.getSvId() + ",";
			else
				newg = groupright + "," + node.getSvId() + ",";
			
			m_ini.setKeyValue(m_ini.getSections(), "groupright", newg);
		}			
	}
	
	/**
	 * 设置某节点的编辑、添加、删除、测试、刷新监测等的权限，管理员总是拥有所有节点的各种权限； 构造好如下的授权串，直接写入
	 * @param right 如果为 null, 则删除该节点的授权串
	 * <br/><br/> 如：1.61= "addsongroup=1,delgroup=1,editgroup=1,grouprefresh=1,copydevice=1,adddevice=1,
	 * addmonitor=1,deldevice=1,editdevice=1,devicerefresh=1,testdevice=1,
	 * editmonitor=1,monitorrefresh=1,delmonitor=1,"
	 */
	public void setNodeEditRight(INode node, String right) throws Exception
	{
		if(isAdmin())
			return;
		if(right==null)
		{
			try
			{
				m_ini.deleteKey(m_ini.getSections(), node.getSvId());
			} catch (Exception e)
			{
			}
		}
		else
		{
			if(!checkNodeVisible(node))
				throw new Exception(" Can't visit this node: " + node.getSvId() + " setNodeEditRight need to set this node visible at first! " );
			
			String pid = new String(node.getSvId());
			if (pid.contains("."))
			{
				pid = pid.substring(0, pid.lastIndexOf("."));
				while (true)
				{
					String pright = m_ini.getValue(m_ini.getSections(), pid);
					if (pright != null)
					{
						if ( containChildRight(pright,right) )
							return;
						else
							break;
					}
					if(!pid.contains("."))
						break;
					pid = pid.substring(0, pid.lastIndexOf("."));
				}
			}
			m_ini.setKeyValue(m_ini.getSections(), node.getSvId(), right);
		}
	}

	private boolean containChildRight(String pright, String right)
	{
		if (pright.equals(right))
			return true;
	
		try
		{
			String[] s = right.split(",");
			if (s == null)
				return false;
			
			int size = s.length;
			for (int i = 0; i < size; ++i)
			{
				String str = s[i];
				if(str==null || str.isEmpty())
					continue;
				
				if( !pright.contains(s[i]) )
					return false;
			}
			return true;
		} catch (Exception e)
		{
			logger.info(e);
			return false;
		}		
	}
	
	public void initNewUserEdit(String index, String loginName) throws Exception
	{
		m_ini = new IniFile("user.ini", index, new HashMap<String, String>());
		m_ini.load();
		m_ini.setKeyValue(m_ini.getSections(), "nIndex", index);
		m_ini.setKeyValue(m_ini.getSections(), "LoginName", loginName);
	}
	
	
//	  -- Display UtilMapInMap begin (1 node) -- 
//	     ---- 19379 (34 key) ----
//	        m_tuop= "1"
//	        m_mailsetting= "1"
//	        1.173.4= "addsongroup=1,delgroup=0,editgroup=0,grouprefresh=0,deldevice=0,editdevice=0,devicerefresh=0,copydevice=0,adddevice=0,testdevice=0,editmonitor=0,monitorrefresh=0,delmonitor=0,addmonitor=0,"
//	        LoginName= "cxy"
//	        m_logshower= "1"
//	        Password= "PLLOJPGPJ7O7IPLe"
//	        1.61.41.10.7= "editmonitor=1,delmonitor=1,monitorrefresh=1,"
//	        1.61= "editmonitor=1,monitorrefresh=1,delmonitor=1,addmonitor=1,addsongroup=1,delgroup=1,editgroup=1,grouprefresh=1,deldevice=1,editdevice=1,devicerefresh=1,copydevice=1,adddevice=1,testdevice=1,"
//	        UserName= "cxy"
//	        m_reportlistEdit= "-1"
//	        1.61.52= "addmonitor=1,editdevice=1,testdevice=1,deldevice=1,devicerefresh=1,editmonitor=1,delmonitor=1,monitorrefresh=1,"
//	        1.61.41.10.8= "editmonitor=1,delmonitor=1,monitorrefresh=1,"
//	        m_AlertRuleEdit= "-1"
//	        groupright= "1.61.41,1.61.41.12,1.173.4,1.61,1.173,"
//	        ungroupright= ""
//	        1.63.5= "adddevice=1,addmonitor=0,copydevice=0,deldevice=0,delmonitor=0,devicerefresh=0,editdevice=0,editmonitor=0,monitorrefresh=0,testdevice=1,"
//	        1.63.6= "adddevice=0,addmonitor=0,copydevice=0,deldevice=1,delmonitor=0,devicerefresh=0,editdevice=1,editmonitor=0,monitorrefresh=0,testdevice=0,"
//	        1.173= "editmonitor=0,monitorrefresh=0,delmonitor=0,addmonitor=0,addsongroup=0,delgroup=0,editgroup=0,grouprefresh=0,deldevice=0,editdevice=0,devicerefresh=0,copydevice=0,adddevice=0,testdevice=0,"
//	        m_reportlistAdd= "1"
//	        TieTongJF.htm= "-1"
//	        nIndex= "19379"
//	        m_AlertRuleDel= "-1"
//	        m_allview= "1"
//	        m_reportlistDel= "-1"
//	        m_AlertRuleAdd= "1"
//	        nIsUse= "1"
//	        m_SetshowSystemReport= "1"
//	        m_smssetting= "1"
//	        1.61.52.1= "editmonitor=1,delmonitor=1,monitorrefresh=1,"
//	        1.61.41= "editmonitor=0,monitorrefresh=0,delmonitor=0,addmonitor=0,addsongroup=0,delgroup=0,editgroup=0,grouprefresh=0,deldevice=0,editdevice=0,devicerefresh=0,copydevice=0,adddevice=0,testdevice=0,"
//	        m_topnadd= "1"
//	        m_tree= "1"
//	        m_alertLogs= "1"
//	        m_general= "1"
//	   -- Display UtilMapInMap end (1 node) -- 
	
	
}
