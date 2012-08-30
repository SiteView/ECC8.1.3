package com.siteview.jsvapi;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javolution.util.FastMap;

import org.ofbiz.base.util.UtilProperties;

import com.opensymphony.oscache.base.*;
import com.opensymphony.oscache.general.*;
import com.siteview.eccservice.SystemOut;

public class jcache
{
	private GeneralCacheAdministrator admin;
	
	public jcache(){
		admin	= new GeneralCacheAdministrator(UtilProperties.getProperties("oscache"));
	}
	public static void main(String[] args)
	{
		jcache jc = new jcache();
		
		String temp = new String("temp");
		while (temp.compareToIgnoreCase("q") != 0)
		{
			long start = System.currentTimeMillis();
			try
			{
				for (Integer i = 1; i <= 1; ++i)
					jc.test1(i.toString(), false);
				
			} catch (Exception e)
			{
				System.err.println("\n\n");
				e.printStackTrace();
			}
			SystemOut.println("\n run: " + (float) (System.currentTimeMillis() - start) / 1000 + " s");
			SystemOut.println("press enter key to continue, q + enter to quit.");
			Scanner in = new Scanner(System.in);
			try
			{
				temp = in.nextLine();
			} catch (Exception e)
			{
				System.exit(1);
			}
		}
	}
	
	public void test1(String temp, boolean show)
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		boolean ret = getUnivDataCache(fmap, ndata, estr);

		SystemOut.println("\n\n");
		if (show)
			Jsvapi.displayUtilMapInMap(fmap);
		
		SystemOut.println("GetUnivData:" + ret);
		SystemOut.println("estr:" + estr);
		SystemOut.println("get " + fmap.size() + " node");
		SystemOut.println("get " + fmap.get("property").size() + " key");
	}
	
	public static Map<String, Map<String, String>> test51(String temp, boolean show)
	{
		Jsvapi svapi = new Jsvapi();
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "LoadResource");
		boolean ret = svapi.getUnivData(fmap, ndata, estr);
		if (!ret)
			return null;
		return fmap;
	}
	
	public void test52(String temp, boolean show)
	{
		String myKey = new String("LoadResource");
		int myRefreshPeriod = 10;
		Map<String, Map<String, String>> fmap = null;
		
		try
		{
			fmap = (Map<String, Map<String, String>>) admin.getFromCache(myKey, myRefreshPeriod);
		} catch (NeedsRefreshException nre)
		{
			try
			{
				fmap = test51(temp, show);
				admin.putInCache(myKey, fmap);
			} catch (Exception ex)
			{
				fmap = (Map<String, Map<String, String>>) nre.getCacheContent();
				admin.cancelUpdate(myKey);
			}
		}
		
		SystemOut.println("\n\n");
		if (show)
			Jsvapi.displayUtilMapInMap(fmap);
		
		SystemOut.println("get " + fmap.size() + " node");
		SystemOut.println("get " + fmap.get("property").size() + " key");
	}
	
	public boolean getUnivDataCache(Map<String, Map<String, String>> fmap, Map<String, String> inwhat, StringBuilder estr)
	{
		String myKey = new String("LoadResource");
		Map<String, Map<String, String>> tfmap = new FastMap<String, Map<String, String>>();
		
		try
		{
			tfmap = (Map<String, Map<String, String>>) admin.getFromCache(myKey, 10);
		} catch (NeedsRefreshException nre)
		{
			try
			{
				tfmap = test51(" ", false);
				admin.putInCache(myKey, tfmap);
			} catch (Exception ex)
			{
				tfmap = (Map<String, Map<String, String>>) nre.getCacheContent();
				admin.cancelUpdate(myKey);
			}
		}
	
		fmap.clear();
		fmap.putAll(tfmap);
		return true;
	}
	
}
