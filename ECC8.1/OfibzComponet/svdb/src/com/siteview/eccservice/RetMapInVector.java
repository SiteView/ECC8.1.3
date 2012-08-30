package com.siteview.eccservice;

import java.util.Map;
import java.util.Vector;

public class RetMapInVector
{
	private String							estr;
	private boolean							retbool;
	private Vector<Map<String, String>>	vmap;
	
	public RetMapInVector()
	{
		estr = null;
		retbool = false;
		vmap = null;
	}
	
	public RetMapInVector(boolean isok, String inestr, Vector<Map<String, String>> invmap)
	{
		retbool = isok;
		estr = inestr;
		vmap = invmap;
	}
	
	public String getEstr()
	{
		return estr;
	}
	
	public boolean getRetbool()
	{
		return retbool;
	}
	
	public Vector<Map<String, String>> getVmap()
	{
		return vmap;
	}
	
	public void setEstr(String inestr)
	{
		estr = inestr;
	}
	
	public void setRetbool(boolean isok)
	{
		retbool = isok;
	}
	
	public void setVmap(Vector<Map<String, String>> invmap)
	{
		vmap = invmap;
	}
	
}
