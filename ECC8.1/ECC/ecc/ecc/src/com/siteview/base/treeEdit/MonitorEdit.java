package com.siteview.base.treeEdit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.alert.Sync2AlertRule;
import com.siteview.svdb.UnivData;

public class MonitorEdit extends MonitorInfo implements IEdit
{
	private Map<String, String> m_property;
	private Map<String, String> m_parameter; 
	private Map<String, String> m_advance_parameter; 
	private Map<String, String> m_error;
	private Map<String, String> m_warning;
	private Map<String, String> m_good; 
	
	private boolean					m_just_create			= false;
	private String					m_just_create_parent_id	= "";
	
	public MonitorEdit(INode node)
	{
		super(node);
	}
	
	/**
	 * 清空数据，仅用于新建一个监测器
	 */	
	public void initWholeData(String pid, String templateId)
	{
		m_just_create_parent_id= pid;
		m_just_create = true;
		
		m_property= new HashMap<String, String>();
		m_parameter= new HashMap<String, String>();
		m_advance_parameter= new HashMap<String, String>();
		m_error= new HashMap<String, String>();
		m_warning= new HashMap<String, String>();
		m_good= new HashMap<String, String>();
		
		m_property.put("sv_monitortype", templateId);
		Map<String, String> info = new HashMap<String, String>();
		super.setRawMap(info);
	}
	
	/**
	 * 加载数据，以便 UI 编辑，必须有编辑权限
	 */	
	public boolean teleLoad() throws Exception
	{
		if( !canEdit() )
			throw new Exception(" Refuse to edit node, id: "+super.getSvId()+" ("+super.getType()+")");
		
		Map<String, Map<String, String>> fmap= UnivData.getMonitor(super.getSvId());
		if(fmap==null || fmap.isEmpty())
			return false;
		
		m_property= fmap.get("property");
		m_parameter= fmap.get("parameter");
		m_advance_parameter= fmap.get("advance_parameter");
		m_error= fmap.get("error");
		m_warning= fmap.get("warning");
		m_good= fmap.get("good"); 	
		return true;
	}
	
	/**
	 * 必须有编辑权限，成功后最好从 View 重新获取
	 */
	public boolean teleSave(View view) throws Exception
	{
		if( !canEdit() )
			throw new Exception(" Refuse to edit node, id: "+super.getSvId()+" ("+super.getType()+")");
		
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "SubmitMonitor");
		ndata.put("del_supplement", "true");
		ndata.put("autoCreateTable", "true");

		Map<String, Map<String, String>> fdata= new HashMap<String, Map<String, String>>();
		fdata.put("property", m_property);
		fdata.put("parameter", m_parameter);
		fdata.put("advance_parameter", m_advance_parameter);
		fdata.put("error", m_error);
		fdata.put("warning", m_warning);
		fdata.put("good", m_good);
		
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
		
		
		//synchronize to alertrule add by qimin.xiong
		Sync2AlertRule.sync2AlertRule(newid, m_property.get("sv_monitortype"));
		return true;
	}
	
	/**
	 * 必须有编辑权限
	 */
	public List<Map<String, String>> getWholeData()
	{
		if( !canEdit() )
			return null;
		
		List<Map<String, String>> a= new ArrayList<Map<String, String>>();
		a.add(m_property);
		a.add(m_parameter); 
		a.add(m_advance_parameter); 
		a.add(m_error);
		a.add(m_warning);
		a.add(m_good); 		
		return a;
	}
	
	/**
	 * 校验输入项目正确性、完整性等 ，必须有编辑权限
	 * <br/>目前讨论结果，由UI层校验，本函数只要有编辑权限就返回 true
	 */	
	public boolean check()
	{
		if( !canEdit() )
			return false;
		

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
	 * 必须有编辑权限
	 */
	public Map<String, String> getParameter()
	{
		if( !canEdit() )
			return null;
		return m_parameter;
	}
	
	/**
	 * 必须有编辑权限
	 */
	public Map<String, String> getAdvanceParameter()
	{
		if( !canEdit() )
			return null;
		return m_advance_parameter;
	}
	
	/**
	 * 必须有编辑权限
	 */
	public Map<String, String> getErrorConditon()
	{
		if( !canEdit() )
			return null;
		return m_error;
	}
	
	/**
	 * 必须有编辑权限
	 */
	public Map<String, String> getWarningConditon()
	{
		if( !canEdit() )
			return null;
		return m_warning;
	}
	
	/**
	 * 必须有编辑权限
	 */
	public Map<String, String> getGoodConditon()
	{
		if( !canEdit() )
			return null;
		return m_good;
	}

	/**
	 * 获取该监测器的 监测器模板id
	 */	
	public String getMonitorType()
	{
		if(m_property==null)
			return "";
		String v= m_property.get("sv_monitortype");
		if(v==null)
			return "";
		return v;
	}
	

	/**
	 * 有些监测器，获取动态数据填充下拉列表。 必须有编辑权限
	 * @return 返回多个 key-name 值对
	 */
	public void startMonitorDynamicData(View view) throws Exception
	{
		if( !canEdit() )
			throw new Exception(" Refuse to edit node, id: "+super.getSvId()+" ("+super.getType()+")");

		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetDynamicData");
		ndata.put("monitorTplId", getMonitorType());
		ndata.put("entityId", super.getParentSvId());
		
		view.putDynamicData((INode)this, null);
		Timer timer = new Timer(true);
		timer.schedule(new StartDynamicData(timer, ndata, (INode)this, view),0);
	}
	
	public Map<String, String> getMonitorDynamicData(View view)throws Exception
	{
		RetMapInMap rmap= view.getDynamicData((INode)this);
		if(rmap==null)
			return null;
		
		if(!rmap.getRetbool())
			throw new Exception(" Failed to GetMonitorDynamicData, since:"+ rmap.getEstr());
		Map<String,Map<String, String>> fmap= rmap.getFmap();
		if(fmap.containsKey("DynamicData"))
			return fmap.get("DynamicData");
		else
			throw new Exception(" DynamicData is null! ");
	}
	
	static Object	DynamicData_lock	= new Object();
	class StartDynamicData extends java.util.TimerTask
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
					synchronized (DynamicData_lock)
					{
						RetMapInMap rmap = ManageSvapi.GetUnivData(m_ndata);
						m_view.putDynamicData(m_node, rmap);
					}
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			super.cancel();
			m_timer.cancel();
		}
	}
	
	
//
//	   -- Display UtilMapInMap begin (7 node) -- 
//	     ---- parameter (10 key) ----
//	        _frequency1= "5"
//	        sv_errfrequint= "1"
//	        sv_description= ""
//	        sv_plan= "7*24"
//	        sv_reportdesc= ""
//	        sv_checkerr= "false"
//	        _frequency= "5"
//	        sv_errfreq= "0"
//	        sv_errfreqsave= ""
//	        _frequencyUnit= "1"
//	     ---- advance_parameter (3 key) ----
//	        _SendNums= "6"
//	        _Timeout= "20000"
//	        _SendBytes= "64"
//	     ---- error (13 key) ----
//	        sv_expression= "1#and#2#and#3"
//	        sv_paramvalue2= "444"
//	        sv_paramvalue3= "444"
//	        sv_paramvalue1= "0"
//	        sv_paramname1= "packetsGoodPercent"
//	        sv_conditioncount= "3"
//	        sv_relation2= "and"
//	        sv_paramname3= "packetsGoodPercent"
//	        sv_relation1= "and"
//	        sv_paramname2= "packetsGoodPercent"
//	        sv_operate1= "=="
//	        sv_operate2= "=="
//	        sv_operate3= "=="
//	     ---- return (2 key) ----
//	        id= "1.61.45.1"
//	        return= "true"
//	     ---- property (7 key) ----
//	        sv_dependson= ""
//	        sv_intpos= "1"
//	        sv_name= "Ping"
//	        sv_disable= ""
//	        sv_monitortype= "5"
//	        sv_starttime= ""
//	        sv_endtime= ""
//	     ---- good (5 key) ----
//	        sv_expression= "1"
//	        sv_operate1= ">"
//	        sv_paramvalue1= "75"
//	        sv_paramname1= "packetsGoodPercent"
//	        sv_conditioncount= "1"
//	     ---- warning (5 key) ----
//	        sv_expression= "1"
//	        sv_operate1= "<="
//	        sv_paramvalue1= "75"
//	        sv_paramname1= "packetsGoodPercent"
//	        sv_conditioncount= "1"
//	   -- Display UtilMapInMap end (7 node) -- 
}
