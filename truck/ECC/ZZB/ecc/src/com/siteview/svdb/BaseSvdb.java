package com.siteview.svdb;
import java.io.File;
import java.util.*;

import org.apache.log4j.Logger;

import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.tuopu.Base64;
import com.siteview.ecc.util.FileCopy;
import com.siteview.jsvapi.Jsvapi;
public class BaseSvdb 
{
	private final static Logger logger = Logger.getLogger(BaseSvdb.class);
	public static void putMapIgnorNull(Map map,String key,String value)
	{
		if(key!=null)
			map.put(key, value);
	}
	public static void trace(HashMap<String,HashMap<String,String>> map)
	{
		Iterator iterator=map.keySet().iterator();
		while(iterator.hasNext())
		{
			String key=iterator.next().toString();
			logger.info("----"+key+"----");
			HashMap<String,String> vmap=map.get(key);
			Iterator v_iterator=vmap.keySet().iterator();
			while(v_iterator.hasNext())
			{
			  String vkey=	v_iterator.next().toString();
			  String value=vmap.get(vkey);
			  logger.info("    "+vkey+"="+value);
			}
		}
	}
	public static void trace(ArrayList<HashMap<String,String>> vmap)
	{
		for(int i=0;i<vmap.size();i++)
		{
			HashMap<String,String> map=vmap.get(i);
			Iterator iterator=map.keySet().iterator();
		while(iterator.hasNext())
		{
			String key=iterator.next().toString();
			String value=map.get(key);
			  logger.info(i+": "+key+"="+value);
		}
		}
	}

}
