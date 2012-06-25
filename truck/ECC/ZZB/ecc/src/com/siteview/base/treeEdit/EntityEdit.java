package com.siteview.base.treeEdit;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.EntityInfo;

public class EntityEdit extends EntityInfo  implements IEdit
{
	private Map<String, String>	m_property;
	
	private boolean					m_just_create			= false;
	private String					m_just_create_parent_id	= "";
	
	public EntityEdit(INode node)
	{
		super(node);
	}
	
	/**
	 * 清空数据，仅用于新建一个设备
	 */	
	public void initWholeData(String pid, String templateId)
	{
		m_just_create_parent_id= pid;
		m_just_create = true;
		m_property= new HashMap<String, String>();
		m_property.put("sv_devicetype", templateId);
	
		Map<String, String> info = new HashMap<String, String>();
		info.put("sv_devicetype", templateId);
		super.setRawMap(info);
	}
	
	/**
	 * 加载数据，以便 UI 编辑，必须有编辑权限
	 */		
	public boolean teleLoad() throws Exception
	{
		if( !canEdit() )
			throw new Exception(" Refuse to edit node, id: "+super.getSvId()+" ("+super.getType()+")");
		
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetEntity");
		ndata.put("id", super.getSvId());
		RetMapInMap rmap= ManageSvapi.GetUnivData(ndata);
		if(!rmap.getRetbool())
			throw new Exception(" Failed to load:"+super.getSvId()+" since:"+ rmap.getEstr());
		Map<String, Map<String, String>> fmap= rmap.getFmap();
		if(fmap==null || fmap.isEmpty())
			return false;
		
		m_property= fmap.get("property");
		return true;
	}
	
	/**
	 * 必须有编辑权限，成功后要用最好从 View 重新获取
	 */	
	public boolean teleSave(View view) throws Exception
	{
		if( !canEdit() )
			throw new Exception(" Refuse to edit node, id: "+super.getSvId()+" ("+super.getType()+")");
		
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "SubmitEntity");
		ndata.put("del_supplement", "true");
		
		Map<String, Map<String, String>> fdata= new HashMap<String, Map<String, String>>();
		if(m_just_create)
		{
			if(m_just_create_parent_id.isEmpty())
				throw new Exception(" Parent id of this new created node is empty! ");
			ndata.put("parentid", m_just_create_parent_id);
		}
		else
		{
			Map<String, String> idData = new HashMap<String, String>();
			idData.put("id", super.getSvId());
			fdata.put("return", idData);
		}
		
		fdata.put("property", m_property);
		RetMapInMap rmap= ManageSvapi.SubmitUnivData(fdata, ndata);
		if(!rmap.getRetbool())
			throw new Exception(" Failed to save:"+super.getSvId()+" since:"+ rmap.getEstr());
		
		String newid= null;
		Map<String, Map<String, String>> fmap= rmap.getFmap();
		if (fmap != null && !fmap.isEmpty())
		{
			Map<String, String> ret= fmap.get("return");
			if (ret != null && !ret.isEmpty())
				newid= ret.get("id");
		}
		if(newid==null || newid.isEmpty())
			throw new Exception(" newid is emtpty, but saving is succeeded! ");
		super.setId(newid);
		
//		if(m_just_create && view!=null)
//			view.setCRUDofNode(this);
		m_just_create= false;
		Manager.instantUpdate();
		return true;
	}
	
	/**
	 * 必须有编辑权限
	 */
	public Map<String, String> getProperty()
	{
		if( !canEdit() )
			return null;
		return m_property;
	}
	
	/**
	 * 校验输入项目正确性、完整性等 ， 必须有编辑权限
	 *  <br/>目前讨论结果，由UI层校验，本函数只要有编辑权限就返回 true
	 */	
	public boolean check()
	{
		if( !canEdit() )
			return false;
		
		return true;
	}
	
	/**
	 * 有几种设备，获取动态数据填充下拉列表。 必须有编辑权限
	 * @return 返回多个 key-name 值对
	 */
	public void startEntityDynamicData(View view) throws Exception
	{
		if( !canEdit() )
			throw new Exception(" Refuse to edit node, id: "+super.getSvId()+" ("+super.getType()+")");

		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetEntityDynamicData");
		ndata.put("entityTplId", super.getDeviceType());
		ndata.put("parentid", super.getParentSvId());
		
		view.putDynamicData((INode)this, null);
		Timer timer = new Timer(true);
		timer.schedule(new StartDynamicData(timer, ndata, (INode)this, view),0);
	}
	
	public Map<String, String> getEntityDynamicData(View view)throws Exception
	{
		RetMapInMap rmap= view.getDynamicData((INode)this);
		if(rmap==null)
			return null;
		
		if(!rmap.getRetbool())
			throw new Exception(" Failed to GetEntityDynamicData, since:"+ rmap.getEstr());
		Map<String,Map<String, String>> fmap= rmap.getFmap();
		if(fmap.containsKey("DynamicData"))
			return fmap.get("DynamicData");
		else
			throw new Exception(" DynamicData is null! ");
	}
	
	static class StartDynamicData extends java.util.TimerTask
	{
		private Timer					m_timer	= null;
		private Map<String, String>	m_ndata;
		private View					m_view;
		private INode					m_node;
		
		public StartDynamicData(Timer t, Map<String, String> data, INode node, View view)
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
					m_view.putDynamicData(m_node, rmap);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			super.cancel();
			m_timer.cancel();
		}
	}
	
	
//	   -- Display UtilMapInMap begin (3 node) -- 
//	     ---- submonitor (4 key) ----
//	        1.61.45.1= ""
//	        1.61.45.2= ""
//	        1.61.45.3= ""
//	        1.61.45.4= ""
//	     ---- return (2 key) ----
//	        id= "1.61.45"
//	        return= "true"
//	     ---- property (18 key) ----
//	        _MachineName= "192.168.6.134"
//	        _Port= "23"
//	        sv_devicetype= "_unix"
//	        sv_name= "192.168.6.134"
//	        _UserAccount= "kenny"
//	        _PriKeyPath= ""
//	        _KeyType= "false"
//	        _ProtocolType= "2"
//	        _OsType= "Linux"
//	        _PassWord= "kenny"
//	        sv_dependson= ""
//	        sv_description= ""
//	        sv_disable= ""
//	        sv_network= "false"
//	        _LoginPrompt= "ogin:"
//	        _Prompt= "# $ >"
//	        _PWPrompt= "assword:"
//	        sv_dependscondition= "3"
//	   -- Display UtilMapInMap end (3 node) -- 
}
