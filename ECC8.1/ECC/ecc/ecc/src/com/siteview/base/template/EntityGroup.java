package com.siteview.base.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.siteview.jsvapi.Jsvapi;

public class EntityGroup implements Serializable
{
	private static final long serialVersionUID = 780596669725172407L;
	private Map<String, String> m_fmap;
	
	public EntityGroup(Map<String, String> fmap)
	{
		m_fmap= fmap;
	}
	
	public List<String> getSubEntityTemplateId()
	{
		ArrayList<String> a= new ArrayList<String>();
		for(String eid:m_fmap.keySet())
		{
			String sub_entity= m_fmap.get(eid);
			if (sub_entity.compareTo("sub_entity") == 0)
				a.add(eid);
		}
		return a;
	}
	
    public String get_sv_id()
    {
		return m_fmap.get("sv_id");
    }
	
	public String get_sv_name()
	{
		return m_fmap.get("sv_name");
	}
	
	public String get_sv_label()
	{
		return m_fmap.get("sv_label");
	}
	
	public String get_sv_description()
	{
		return m_fmap.get("sv_description");
	}
	
	public String get_sv_index()
	{
		return m_fmap.get("sv_index");
	}
	
	public boolean get_sv_hidden()
	{
		String s= m_fmap.get("sv_hidden");
		if(s==null || s.compareToIgnoreCase("true")!=0)
			return false;
		return true;
	}
	
	public void display()
	{
		if(m_fmap==null)
			return;
		Jsvapi.getInstance().DisplayUtilMap(m_fmap);
	}
	
//    ---- _Server (12 key) ----
//    sv_label= "服务器"
//    _unix= "sub_entity"
//    sv_description= "包括Windwos服务器和UNIX服务器"
//    entityTemplateId= "_SnmpWin,_unix,_unixagent,_AS400,_win,"
//    sv_name= "服务器"
//    _unixagent= "sub_entity"
//    sv_id= "_Server"
//    sv_func= "GetAllCPURate"
//    sv_index= "0"
//    _win= "sub_entity"
//    _SnmpWin= "sub_entity"
//    _AS400= "sub_entity"
	
	
}
