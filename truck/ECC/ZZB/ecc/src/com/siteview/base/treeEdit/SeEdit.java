package com.siteview.base.treeEdit;

import java.util.HashMap;
import java.util.Map;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.SeInfo;

public class SeEdit extends SeInfo implements IEdit
{
	private Map<String, String> m_property;
	
	public SeEdit(INode node)
	{
		super(node);
	}
	
	/**
	 * �������ݣ��Ա� UI �༭�������б༭Ȩ��
	 */		
	public boolean teleLoad() throws Exception
	{
		if( !canEdit() )
			throw new Exception(" Refuse to edit node, id: "+super.getSvId()+" ("+super.getType()+")");
		
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetSVSE");
		ndata.put("id", super.getSvId());
		RetMapInMap rmap= ManageSvapi.GetUnivData(ndata);
		if(!rmap.getRetbool())
			throw new Exception(" Failed to load:"+super.getSvId()+" since:"+ rmap.getEstr());
		Map<String, Map<String, String>> fmap= rmap.getFmap();
		if(fmap==null || fmap.isEmpty())
			return false;
		
		m_property= fmap.get("return");
		return true;
	}
	
	/**
	 * �����б༭Ȩ��
	 */
	public boolean teleSave(View view) throws Exception
	{
		if( !canEdit() )
			throw new Exception(" Refuse to edit node, id: "+super.getSvId()+" ("+super.getType()+")");

		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "SubmitSVSE");

		Map<String, Map<String, String>> fdata= new HashMap<String, Map<String, String>>();
		Map<String, String> idData = new HashMap<String, String>();
		idData.put("id", super.getSvId());
		fdata.put("return", idData);
		fdata.put("return", m_property);
		RetMapInMap rmap= ManageSvapi.SubmitUnivData(fdata, ndata);
		if(!rmap.getRetbool())
			throw new Exception(" Failed to save:"+super.getSvId()+" since:"+ rmap.getEstr());

		Manager.instantUpdate();
		return true;
	}
	
	/**
	 * �޸� se �� Label �� �����б༭Ȩ�ޣ� ��Ҫ������ teleSave()
	 */	
	public boolean setLabel(String label)
	{
		if( !canEdit() )
			return false;
		
		m_property.put("svse_label", label);
		return true;
	}
	
	/**
	 * �����б༭Ȩ��
	 */
	public Map<String, String> getProperty()
	{
		if( !canEdit() )
			return null;
		return m_property;
	}
	
	/**
	 * У��������Ŀ��ȷ�ԡ������Ե� �� �����б༭Ȩ��
	 * <br/>Ŀǰ���۽������UI��У�飬������ֻҪ�б༭Ȩ�޾ͷ��� true
	 */	
	public boolean check()
	{
		if( !canEdit() )
			return false;
		

		return true;
	}
	
//	   -- Display UtilMapInMap begin (3 node) -- 
//	     ---- subentity (1 key) ----
//	        1.356= ""
//	     ---- return (3 key) ----
//	        id= "1"
//	        return= "true"
//	        svse_label= "�ۺϼ�ع���ϵͳ"
//	     ---- subgroup (23 key) ----
//	        1.93= ""
//	        1.106= ""
//	        1.128= ""
//	        1.182= ""
//	        1.60= ""
//	        1.61= ""
//	        1.62= ""
//	        1.63= ""
//	        1.123= ""
//	        1.64= ""
//	        1.94= ""
//	        1.209= ""
//	        1.157= ""
//	        1.167= ""
//	        1.168= ""
//	        1.207= ""
//	        1.130= ""
//	        1.211= ""
//	        1.357= ""
//	        1.133= ""
//	        1.164= ""
//	        1.174= ""
//	        1.173= ""
//	   -- Display UtilMapInMap end (3 node) -- 
}
