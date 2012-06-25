package com.siteview.base.tree;

import java.util.ArrayList;
import java.util.List;


public class Fork extends NameId implements IForkNode
{
	private static final long serialVersionUID = -5911616747913833333L;
	private String m_son_id; 

	public void setSonId(String sonid)
	{
		m_son_id= sonid;
	}
	
	public String getSonId()
	{
		return m_son_id;
	}
	
	public List<String> getSonList()
	{
		List<String> retlist= new ArrayList<String>();	
		if(m_son_id==null || m_son_id.isEmpty())
			return retlist;
		
		String [] s= m_son_id.split(",");
		for(String value : s){
			if (value==null || "".equals(value)) continue;
			retlist.add(value);
		}
		return retlist;
	}
	
}
