package com.siteview.base.tree;

import java.io.Serializable;


public class NameId implements Serializable
{
	private String m_id;
	private String m_name;
	private String m_status;
	private String m_type;

//  if this function is active, can delete all set_ function .
//	public NameId(String id,String name,String type,String status)
//	{
//		
//	}

	public void setType(String type)
	{
		m_type= type;
	}
	
	public String getType()
	{
		return m_type;
	}
	
	public void setName(String name)
	{
		m_name= name;
	}
	public String getName()
	{
		return m_name;
	}
	
	public void setId(String id)
	{
		m_id= id;
	}	
	public String getSvId()
	{
		return m_id;
	}
	
	public String getParentSvId()
	{
		String id = new String(m_id);
		try
		{
			if (id.contains("."))
				return id.substring(0,id.lastIndexOf("."));
			else
				return "";
		} catch (Exception e)
		{
			return id;
		}
	}
	
	public void setStatus(String status)
	{
		m_status= status;
	}	
	public String getStatus()
	{
		return m_status;
	}	
}
