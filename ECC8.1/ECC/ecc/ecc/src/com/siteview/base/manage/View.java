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
	 * ȡ��һ�����Ķ���ڵ㣬�� se �ڵ� 
	 */
	public INode[] getSe()
	{
		return m_profile_data.getSe();
	}
	
	/***
	 * ȡ��һ����������ڵ� 
	 */
	public INode getNode(String id)
	{
		return m_profile_data==null ? null : m_profile_data.getNode(id);
	}
	
	/**
	 * ��ȡse������Ϣ���Ա㹹����
	 */
	public SeNode getSeNode(String id)
	{
		return m_profile_data.getSeNode(id);	
	}
	
	/**
	 * ��ȡ�������Ϣ���Ա㹹����
	 */
	public GroupNode getGroupNode(String id)
	{
		return m_profile_data.getGroupNode(id);	
	}
	
	/**
	 * ��ȡ�豸������Ϣ���Ա㹹����
	 */
	public EntityNode getEntityNode(String id)
	{
		return m_profile_data.getEntityNode(id);	
	}
	
	/**
	 * ��ȡ�����������Ϣ���Ա㹹����
	 */
	public MonitorNode getMonitorNode(String id)
	{
		return m_profile_data.getMonitorNode(id);	
	}	
	
	/**
	 * ��ȡ���û�������������ͼ
	 */
	public List<VirtualView> getAllVirtualView()
	{
		return VirtualManager.getAllVirtualView(getUserIni(), this);
	}
	
	/**
	 * ��ȡ���û�������������ͼ
	 * @throws Exception 
	 */
	public List<VirtualView> getAllVirtualViewThrowException() throws Exception
	{
		return VirtualManager.getAllVirtualViewThrowException(getUserIni(), this);
	}
	
	/**
	 * �ڷ��������½�һ��������ͼ
	 * @return VirtualView Ϊ null ʱʧ��
	 */
	public VirtualView createVirtualView(String viewName)throws Exception
	{
		return VirtualManager.createVirtualView(viewName, getUserIni(), this);
	}
	
	/**
	 * �޸�ĳ������ͼ����ʾ����
	 * @param view Ҫ��������ͼ
	 * @param newViewName ����ͼ�µ�����
	 */
	public boolean changeNameOfVirtualView(VirtualView view, String newViewName)throws Exception
	{
		return VirtualManager.changeNameOfVirtualView(view, newViewName, getUserIni());
	}
	
	/**
	 * ����Ա���޸�ĳ������ͼ���û���Ȩ
	 * @param view ĳ������ͼ
	 * @param aUser ĳ�û�
	 * @param hasRight Ϊ true ����û��ɼ���������ͼ��false �򲻿ɼ�
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
	 * ɾ��һ��������ͼ
	 */
	public boolean deleteVirtualView(VirtualView view)throws Exception
	{
		return VirtualManager.deleteVirtualView(view, getUserIni());
	}
	
	/**
	 * ����Ա���鿴�����û�����Ϣ
	 */
	public List<UserEdit> getAllUserEdit()
	{
		if(!isAdmin())
			return null;
		return UserManager.getAllUserEdit(getUserIni(), this, m_server_data.getAllUserIni());
	}
	
	/**
	 * ����Ա���½�һ���û����û��������ʱ����� UserEdit.saveChange()
	 * @return UserEdit Ϊ null ʱʧ��
	 */
	public UserEdit createUserEdit(String loginName) throws Exception
	{
		if(!isAdmin())
			return null;
		Thread.sleep(1000);
		return UserManager.createUserEdit(loginName, getUserIni(), this, m_server_data.getAllUserIni());
	}
	/**
	 * ����Ա��ɾ��һ���û�
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
	 * ��ȡ���û��ĵ�¼��
	 */
	public String getLoginName()
	{
		return m_profile_data.getLoginName();
	}
	
	/**
	 * ��ȡ���û��� user.ini �е�����
	 */
	public IniFile getUserIni()
	{
		String LoginName= m_profile_data.getLoginName();
		return m_server_data.getUserIni(LoginName);
	}
	
	/**
	 * ���û��Ƿ����Ա������ԱĬ�Ͽ��Թ������нڵ㣬���Թ������й���ģ��
	 */
	public boolean isAdmin()
	{
		String LoginName= m_profile_data.getLoginName();
		return m_server_data.isAdmin(LoginName);
	}
	
	/**
	 * �ǹ���Ա�����Ӷ�ĳ�ڵ��crudȨ��
	 */
	public boolean setCRUDofNode(INode node) throws Exception
	{
		if(isAdmin())
			return true;
		String LoginName= m_profile_data.getLoginName();
		return m_server_data.setCRUDofNode(LoginName, node);
	}
	
	/**
	 * ��ȡse���µ���ʾ��ϢSeInfo���Ա����м�������ʾ
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
	 * ��ȡ�����µ���ʾ��ϢGroupInfo���Ա����м�������ʾ
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
	 * ��ȡ�豸���µ���ʾ��ϢEntityInfo���Ա����м�������ʾ
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
	 * ��ȡ��������µ���ʾ��ϢMonitorInfo���Ա����м�������ʾ
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
	 * ��ȡSeEdit���Ա༭��se
	 * @throws Exception ��û�б༭Ȩ��ʱ���׳��쳣
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
		
		//����һ����������
		if( !e.teleLoad() )
			return null;
		return e;
	}
	
	/**
	 * ��ȡGroupEdit���Ա༭����
	 * @throws Exception ��û�б༭Ȩ��ʱ���׳��쳣
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
		
		//����һ����������
		if( !e.teleLoad() )
			return null;
		return e;
	}
	
	/**
	 * ��ȡEntityEdit���Ա༭���豸
	 * @throws Exception ��û�б༭Ȩ��ʱ���׳��쳣
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
		
		//����һ����������
		if( !e.teleLoad() )
			return null;
		return e;
	}
	
	/**
	 * ��ȡMonitorEdit���Ա༭�ü����
	 * @throws Exception ��û�б༭Ȩ��ʱ���׳��쳣
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
		
		//����һ����������
		if( !e.teleLoad() )
			return null;
		return e;
	}
	
	/**
	 * ��ȡ���ϴε��ñ����������� tree ������Ϣ��name,status���䶯�˵Ľڵ㣬�����˵Ľڵ㣬ɾ���˵Ľڵ�
	 * ��  ���ٷ�Χ�������� ��
	 * @return ���û�б仯���ݣ����� null
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
	 * ���ý��㣨ѡ�У��ڵ㣬�� getTreeInfoChange ������ṩ treeInfo �ı䶯��Ϣ
	 *  �� ÿ�ε��ñ��������Ḳ��֮ǰ������
	 */	
	public void setFocusNode(String[] ids)
	{
		m_profile_data.setFocusNode(ids);
	}
	
	/**
	 * ��ȡ���ϴε��ñ��������� treeInfo �䶯�˵Ľڵ� Id
	 *  �� ���ٷ�Χ��  setFocusNode ����
	 * @return ���û�б仯���ݣ����� null  
	 */	
	public List<String> getChangeTreeInfo()
	{
		return m_profile_data.getTreeInfoChange();	
	}
	
	/**
	 * �÷���Ӧ�ö�ʱѭ�����ã��Ա��� view �ļ���״̬
	 */
	public void setVisit()
	{
		m_profile_data.setVisit();
	}
	
	/**
	 * ��ȡ��¼ʱ�����ݣ��������ɵ�ҳ����Ҫ
	 */
	public Map<String, String> getLoginData()
	{
		return m_profile_data.getLoginData();
	}
	
	/**
	 * ����Ա��ȡ���ߵĵ�¼�û������б�
	 */
	public List<String> getOnlineLoginName() throws Exception
	{
		if(!isAdmin())
			throw new Exception(" Only admin can getOnlineLoginName ! ");
		return Manager.getOnlineLoginName();
	}
}
