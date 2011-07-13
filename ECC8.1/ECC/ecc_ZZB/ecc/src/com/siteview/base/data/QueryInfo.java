package com.siteview.base.data;

import java.util.HashMap;
import java.util.Map;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.tree.INode;
import com.siteview.jsvapi.Jsvapi;
import com.siteview.svdb.UnivData;

public class QueryInfo
{
	public String needkey="";
	public String needtype="";
	public String parentid="";
	public Map<String, Map<String, String>> fmap;
	
	public void setNeedType_entity()
	{
		needtype= INode.ENTITY;
	}
	public void setNeedType_group()
	{
		needtype= INode.GROUP;
	}
	public void setNeedType_monitor()
	{
		needtype= INode.MONITOR;
	}
	public void setNeedType_all()
	{
		needtype= "";
	}
	
	public Map<String, Map<String, String>> load()  throws Exception
	{
		if(needkey.isEmpty())
			throw new Exception(" needkey is empty! ");
		return UnivData.queryInfo(needkey, needtype, parentid);
	}
	
	public void display()
	{
		if(fmap==null)
			return;
		
		Jsvapi.getInstance().DisplayUtilMapInMap(fmap);

//		   -- Display UtilMapInMap begin (8 node) -- 
//		     ---- 1.61.41.10 (2 key) ----
//		        _MachineName= "127.0.0.1"
//		        needtype= "entity"
//		     ---- 1.61.49 (2 key) ----
//		        _MachineName= "192.168.6.2"
//		        needtype= "entity"
//		     ---- 1.61.41.14 (2 key) ----
//		        _MachineName= "127.0.0.1"
//		        needtype= "entity"
//		     ---- 1.61.44 (2 key) ----
//		        _MachineName= "192.168.6.2"
//		        needtype= "entity"
//		     ---- 1.61.45 (2 key) ----
//		        _MachineName= "192.168.6.134"
//		        needtype= "entity"
//		     ---- 1.61.50 (2 key) ----
//		        _MachineName= "192.168.6.134"
//		        needtype= "entity"
//		     ---- 1.61.41.9 (2 key) ----
//		        _MachineName= "2322"
//		        needtype= "entity"
//		     ---- 1.61.51 (2 key) ----
//		        _MachineName= "192.168.6.2"
//		        needtype= "entity"
//		   -- Display UtilMapInMap end (8 node) -- 

	}
	
	public static void main(String[] args)
	{
		QueryInfo q= new QueryInfo();
		q.needkey= "_MachineName";
		q.setNeedType_entity();
		q.parentid= "1.61";
		try
		{
			q.load();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		q.display();
	}
	

}
