package com.siteview.eccservice;

import java.util.HashMap;
import java.util.Map;

public class RetMapInMap
{
	private String										estr;
	private boolean										retbool;
	private Map<String, Map<String, String>>	fmap;
	
	public RetMapInMap()
	{
		estr = null;
		retbool = false;
		fmap = null;
	}
	
	public RetMapInMap(boolean isok, String inestr, Map<String, Map<String, String>> infmap)
	{
		retbool = isok;
		estr = inestr;
		fmap = infmap;
	}
	
	public String getEstr()
	{
		return estr;
	}
	
	public boolean getRetbool()
	{
		return retbool;
	}
	
	public Map<String, Map<String, String>> getFmap()
	{
		return fmap;
	}
	
	public void setEstr(String inestr)
	{
		estr = inestr;
	}
	
	public void setRetbool(boolean isok)
	{
		retbool = isok;
	}
	
	public void setFmap(Map<String, Map<String, String>> infmap)
	{
		fmap = infmap;
	}
	
}
