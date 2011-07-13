package com.siteview.base.manage;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.UserEdit;
import com.siteview.base.data.UserManager;
import com.siteview.base.data.VirtualManager;
import com.siteview.base.data.VirtualView;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.tree.EntityNode;
import com.siteview.base.tree.GroupNode;
import com.siteview.base.tree.INode;
import com.siteview.base.tree.MonitorNode;
import com.siteview.base.tree.SeNode;
import com.siteview.base.treeEdit.EntityEdit;
import com.siteview.base.treeEdit.GroupEdit;
import com.siteview.base.treeEdit.MonitorEdit;
import com.siteview.base.treeEdit.SeEdit;
import com.siteview.base.treeInfo.EntityInfo;
import com.siteview.base.treeInfo.GroupInfo;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.base.treeInfo.SeInfo;
import com.siteview.ecc.usermanager.User;

public class View implements Serializable
{
	private ServerData	m_server_data;
	private ProfileData	m_profile_data;

	public View(ServerData sd, ProfileData pd)
	{
		m_server_data= sd;
		m_profile_data= pd;
	}
	
	public List<Map<String, String>> getRawTreeData()
	{
		return m_server_data.getRawTreeData();
	}

	/**
	 * 取得一棵数的顶层节点，即 se 节点 
	 */
	public INode[] getSe()
	{
		return m_profile_data.getSe();
	}
	
	/***
	 * 取得一棵数的任意节点 
	 */
	public INode getNode(String id)
	{
		return m_profile_data==null ? null : m_profile_data.getNode(id);
	}
	
	/**
	 * 获取se的树信息，以便构造树
	 */
	public SeNode getSeNode(String id)
	{
		return m_profile_data.getSeNode(id);	
	}
	
	/**
	 * 获取组的树信息，以便构造树
	 */
	public GroupNode getGroupNode(String id)
	{
		return m_profile_data.getGroupNode(id);	
	}
	
	/**
	 * 获取设备的树信息，以便构造树
	 */
	public EntityNode getEntityNode(String id)
	{
		return m_profile_data.getEntityNode(id);	
	}
	
	/**
	 * 获取监测器的树信息，以便构造树
	 */
	public MonitorNode getMonitorNode(String id)
	{
		return m_profile_data.getMonitorNode(id);	
	}	
	
	/**
	 * 获取该用户的所有虚拟视图
	 */
	public List<VirtualView> getAllVirtualView()
	{
		return VirtualManager.getAllVirtualView(getUserIni(), this);
	}
	
	/**
	 * 获取该用户的所有虚拟视图
	 * @throws Exception 
	 */
	public List<VirtualView> getAllVirtualViewThrowException() throws Exception
	{
		return VirtualManager.getAllVirtualViewThrowException(getUserIni(), this);
	}
	
	/**
	 * 在服务器上新建一个虚拟视图
	 * @return VirtualView 为 null 时失败
	 */
	public VirtualView createVirtualView(String viewName)throws Exception
	{
		return VirtualManager.createVirtualView(viewName, getUserIni(), this);
	}
	
	/**
	 * 修改某虚拟视图的显示名字
	 * @param view 要改名的视图
	 * @param newViewName 该视图新的名字
	 */
	public boolean changeNameOfVirtualView(VirtualView view, String newViewName)throws Exception
	{
		return VirtualManager.changeNameOfVirtualView(view, newViewName, getUserIni());
	}
	
	/**
	 * 管理员，修改某虚拟视图的用户授权
	 * @param view 某虚拟视图
	 * @param aUser 某用户
	 * @param hasRight 为 true 则该用户可见该虚拟视图，false 则不可见
	 */
	public boolean changeUserOfVirtualView(VirtualView view, String aUser, boolean hasRight)throws Exception
	{
		if(!isAdmin())
			throw new Exception(" Only admin can changeUser Of VirtualView! ");
		IniFile ini= m_server_data.getUserIni(aUser); 
		if(ini==null)
			throw new Exception(" User doesn't exist: " + aUser + "! ");
		String uindex= ini.getSections();
		return VirtualManager.changeUserOfVirtualView(view, uindex, hasRight, getUserIni());
	}
	
	/**
	 * 删除一个虚拟视图
	 */
	public boolean deleteVirtualView(VirtualView view)throws Exception
	{
		return VirtualManager.deleteVirtualView(view, getUserIni());
	}
	
	/**
	 * 管理员，查看所有用户的信息
	 */
	public List<UserEdit> getAllUserEdit()
	{
		if(!isAdmin())
			return null;
		return UserManager.getAllUserEdit(getUserIni(), this, m_server_data.getAllUserIni());
	}
	
	/**
	 * 管理员，新建一个用户，用户点击保存时请调用 UserEdit.saveChange()
	 * @return UserEdit 为 null 时失败
	 */
	public UserEdit createUserEdit(String loginName) throws Exception
	{
		if(!isAdmin())
			return null;
		Thread.sleep(1000);
		return UserManager.createUserEdit(loginName, getUserIni(), this, m_server_data.getAllUserIni());
	}
	/**
	 * 管理员，删除一个用户
	 */
	public boolean deleteUserEdit(UserEdit aUser) throws Exception
	{
		if(!isAdmin())
			return false;
		return UserManager.deleteUserEdit(aUser, getUserIni());
	}
	
	public Date getLatestDateOfDisOrEnable(INode node)
	{
		return m_profile_data.getLatestDateOfDisOrEnable(node);
	}
	
	public void putDateOfDisOrEnable(INode node)
	{
		m_profile_data.putDateOfDisOrEnable(node);
	}
	
	public RetMapInMap getTestDeviceData(INode node)
	{
		return m_profile_data.getTestDeviceData(node);
	}
	public void putTestDeviceData(INode node, RetMapInMap data)
	{
		m_profile_data.putTestDeviceData(node, data);
	}
	public RetMapInMap getDynamicData(INode node)
	{
		return m_profile_data.getDynamicData(node);
	}
	public  void putDynamicData(INode node, RetMapInMap data)
	{
		m_profile_data.putDynamicData(node, data);
	}
	
	/**
	 * 获取该用户的登录名
	 */
	public String getLoginName()
	{
		return m_profile_data.getLoginName();
	}
	
	/**
	 * 获取该用户在 user.ini 中的数据
	 */
	public IniFile getUserIni()
	{
		String LoginName= m_profile_data.getLoginName();
		return m_server_data.getUserIni(LoginName);
	}
	
	/**
	 * 该用户是否管理员；管理员默认可以管理所有节点，可以管理所有功能模块
	 */
	public boolean isAdmin()
	{
		String LoginName= m_profile_data.getLoginName();
		return m_server_data.isAdmin(LoginName);
	}
	
	/**
	 * 非管理员，增加对某节点的crud权限
	 */
	public boolean setCRUDofNode(INode node) throws Exception
	{
		if(isAdmin())
			return true;
		String LoginName= m_profile_data.getLoginName();
		return m_server_data.setCRUDofNode(LoginName, node);
	}
	
	/**
	 * 获取se最新的显示信息SeInfo，以便在中间区域显示
	 */
	public SeInfo getSeInfo(INode node)
	{
		if(node==null)
			return null;
		SeInfo e = m_profile_data.GetSeInfo(node);
		if(e!=null)
			return e;

		e= new SeInfo(node);
		String LoginName= m_profile_data.getLoginName();
		Map<String, String> inivalue= m_server_data.getIniValue(LoginName, e.getSvId()); 
		if(inivalue==null)
			return null;
		e.setIniValue(inivalue);
		e.setRawMap(m_server_data.getRawMapClone(e.getSvId()));
		return e;
	}
	
	/**
	 * 获取组最新的显示信息GroupInfo，以便在中间区域显示
	 */
	public GroupInfo getGroupInfo(INode node)
	{
		if(node==null)
			return null;
		GroupInfo e = m_profile_data.GetGroupInfo(node);
		if(e!=null)
			return e;

		e=  new GroupInfo(node);
		String LoginName= m_profile_data.getLoginName();
		Map<String, String> inivalue= m_server_data.getIniValue(LoginName, e.getSvId()); 
		if(inivalue==null)
			return null;
		e.setIniValue(inivalue);
		e.setRawMap(m_server_data.getRawMapClone(e.getSvId()));		
		return e;
	}
	
	/**
	 * 获取设备最新的显示信息EntityInfo，以便在中间区域显示
	 */
	public EntityInfo getEntityInfo(INode node)
	{
		if(node==null)
			return null;
		EntityInfo e = m_profile_data.GetEntityInfo(node);
		if(e!=null)
			return e;

		e=  new EntityInfo(node);
		String LoginName= m_profile_data.getLoginName();
		Map<String, String> inivalue= m_server_data.getIniValue(LoginName, e.getSvId()); 
		if(inivalue==null)
			return null;
		e.setIniValue(inivalue);
		e.setRawMap(m_server_data.getRawMapClone(e.getSvId()));		
		return e;
	}
	
	/**
	 * 获取监测器最新的显示信息MonitorInfo，以便在中间区域显示
	 */
	public MonitorInfo getMonitorInfo(INode node)
	{
		if(node==null)
			return null;
		MonitorInfo e = m_profile_data.GetMonitorInfo(node);
		if(e!=null)
			return e;

		e=  new MonitorInfo(node);
		String LoginName= m_profile_data.getLoginName();
		Map<String, String> inivalue= m_server_data.getIniValue(LoginName, e.getSvId()); 
		if(inivalue==null)
			return null;
		e.setIniValue(inivalue);
		e.setRawMap(m_server_data.getRawMapClone(e.getSvId()));		
		return e;
	}
	
	
	/**
	 * 获取SeEdit，以编辑该se
	 * @throws Exception 当没有编辑权限时，抛出异常
	 */
	public SeEdit getSeEdit(INode node) throws Exception
	{
		if(node==null)
			return null;
		SeEdit e = new SeEdit(node);
		
		String LoginName= m_profile_data.getLoginName();
		Map<String, String> inivalue= m_server_data.getIniValue(LoginName, e.getSvId()); 
		if(inivalue==null)
			return null;
		e.setIniValue(inivalue);
		e.setRawMap(m_server_data.getRawMapClone(e.getSvId()));		
		
		//加载一下最新数据
		if( !e.teleLoad() )
			return null;
		return e;
	}
	
	/**
	 * 获取GroupEdit，以编辑该组
	 * @throws Exception 当没有编辑权限时，抛出异常
	 */
	public GroupEdit getGroupEdit(INode node) throws Exception
	{
		if(node==null)
			return null;
		GroupEdit e = new GroupEdit(node);
		
		String LoginName= m_profile_data.getLoginName();
		Map<String, String> inivalue= m_server_data.getIniValue(LoginName, e.getSvId()); 
		if(inivalue==null)
			return null;		
		e.setIniValue(inivalue);
		e.setRawMap(m_server_data.getRawMapClone(e.getSvId()));		
		
		//加载一下最新数据
		if( !e.teleLoad() )
			return null;
		return e;
	}
	
	/**
	 * 获取EntityEdit，以编辑该设备
	 * @throws Exception 当没有编辑权限时，抛出异常
	 */
	public EntityEdit getEntityEdit(INode node) throws Exception
	{
		if(node==null)
			return null;
		EntityEdit e = new EntityEdit(node);
		
		String LoginName= m_profile_data.getLoginName();
		Map<String, String> inivalue= m_server_data.getIniValue(LoginName, e.getSvId()); 
		if(inivalue==null)
			return null;		
		e.setIniValue(inivalue);
		e.setRawMap(m_server_data.getRawMapClone(e.getSvId()));		
		
		//加载一下最新数据
		if( !e.teleLoad() )
			return null;
		return e;
	}
	
	/**
	 * 获取MonitorEdit，以编辑该监测器
	 * @throws Exception 当没有编辑权限时，抛出异常
	 */
	public MonitorEdit getMonitorEdit(INode node) throws Exception
	{
		if(node==null)
			return null;
		MonitorEdit e = new MonitorEdit(node);
		
		String LoginName= m_profile_data.getLoginName();
		Map<String, String> inivalue= m_server_data.getIniValue(LoginName, e.getSvId()); 
		if(inivalue==null)
			return null;		
		e.setIniValue(inivalue);
		e.setRawMap(m_server_data.getRawMapClone(e.getSvId()));		
		
		//加载一下最新数据
		if( !e.teleLoad() )
			return null;
		return e;
	}
	
	/**
	 * 获取自上次调用本方法以来： tree 基本信息（name,status）变动了的节点，增加了的节点，删除了的节点
	 * ；  跟踪范围是整棵树 ；
	 * @return 如果没有变化数据，返回 null
	 */		
	public List<ChangeDetailEvent> getChangeTree()
	{
		this.setVisit();
		List<ChangeDetailEvent> a= m_profile_data.getTreeChange();
//		try
//		{
//			if (a == null)
//				return null;
//			
//			logger.info("---------------------------");
//			for ( ChangeDetailEvent e: a)
//			{
//				String key= e.getSvid();
//				logger.info("  change: " + key);
//				INode n = e.getData();//getNode(key);
//				if (n != null)
//				{
//					logger.info("   " + n.getSvId() + " -- " + n.getType() + " -- " + n.getStatus() + " ");
//					logger.info("   " + " " + n.getName());
//				}
//				logger.info();
//			}
//			
//			logger.info("---------------------------");
//		} catch (Exception e)
//		{
//		}
		return a;
	}
	
	/**
	 * 设置焦点（选中）节点，跟 getTreeInfoChange 配合以提供 treeInfo 的变动信息
	 *  ； 每次调用本方法，会覆盖之前的设置
	 */	
	public void setFocusNode(String[] ids)
	{
		m_profile_data.setFocusNode(ids);
	}
	
	/**
	 * 获取自上次调用本方法以来 treeInfo 变动了的节点 Id
	 *  ； 跟踪范围由  setFocusNode 设置
	 * @return 如果没有变化数据，返回 null  
	 */	
	public List<String> getChangeTreeInfo()
	{
		return m_profile_data.getTreeInfoChange();	
	}
	
	/**
	 * 该方法应该定时循环调用，以保持 view 的激活状态
	 */
	public void setVisit()
	{
		m_profile_data.setVisit();
	}
	
	/**
	 * 获取登录时的数据，在软件许可等页面需要
	 */
	public Map<String, String> getLoginData()
	{
		return m_profile_data.getLoginData();
	}
	
	/**
	 * 管理员获取在线的登录用户名字列表
	 */
	public List<String> getOnlineLoginName() throws Exception
	{
		if(!isAdmin())
			throw new Exception(" Only admin can getOnlineLoginName ! ");
		return Manager.getOnlineLoginName();
	}
}
