package com.siteview.base.treeEdit;

import java.util.HashMap;
import java.util.Map;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.GroupInfo;
import com.siteview.ecc.alert.Sync2AlertRule;

public class GroupEdit extends GroupInfo  implements IEdit
{
	private Map<String, String>	m_property;
	
	private boolean					m_just_create			= false;
	private String					m_just_create_parent_id	= "";
	
	public GroupEdit(INode node)
	{
		super(node);
	}
	
	/**
	 * 清空数据，仅用于新建一个组
	 */	
	public void initWholeData(String pid)
	{
		m_just_create_parent_id= pid;
		m_just_create = true;
		m_property = new HashMap<String, String>();
	
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
			
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetGroup");
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
		ndata.put("dowhat", "SubmitGroup");
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
		Sync2AlertRule.syncEntity2AleryStrategy(newid);
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
	 * 校验输入项目正确性、完整性等 ，必须有编辑权限
	 *  <br/>目前讨论结果，由UI层校验，本函数只要有编辑权限就返回 true
	 */	
	public boolean check()
	{
		if( !canEdit() )
			return false;
		

		return true;
	}
	
//	   -- Display UtilMapInMap begin (4 node) -- 
//	     ---- subentity (5 key) ----
//	        1.61.46= ""
//	        1.61.44= ""
//	        1.61.45= ""
//	        1.61.42= ""
//	        1.61.38= ""
//	     ---- return (2 key) ----
//	        id= "1.61"
//	        return= "true"
//	     ---- property (6 key) ----
//	        sv_dependson= ""
//	        sv_description= "三层交换机"
//	        sv_name= "三层交换机"
//	        sv_disable= "false"
//	        CombinedEntityTypeMarker= "三层交换机"
//	        sv_index= "4"
//	     ---- subgroup (2 key) ----
//	        1.61.40= ""
//	        1.61.41= ""
//	   -- Display UtilMapInMap end (4 node) -- 
}
