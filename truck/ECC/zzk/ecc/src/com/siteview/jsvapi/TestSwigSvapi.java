package com.siteview.jsvapi;

import java.util.HashMap;

import org.apache.log4j.Logger;

public class TestSwigSvapi
{
	private final static Logger logger = Logger.getLogger(TestSwigSvapi.class);
	
	public static void main(String[] argv)
	{
		try
		{
			System.loadLibrary("swigsvapi");
		} catch (UnsatisfiedLinkError e)
		{
			System.err.println("Native code library failed to load. See the chapter on Dynamic Linking Problems in the SWIG Java documentation for help.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}
		logger.info("loadLibrary is done!");
		
		try
		{
			SwigSvapi s = new SwigSvapi();
			s.swig_test1();
			
			HashMap<String, HashMap<String, String>> fmap = new HashMap<String, HashMap<String, String>>();
			HashMap<String, String> ndata = new HashMap<String, String>();
			
			ForestMap fsmap = new ForestMap();
			StringMap smap = new StringMap();
			MyString mestr = new MyString();
			smap.set("dowhat", "GetSvdbAddr");
			boolean ret = s.swig_GetUnivData(fsmap, smap, mestr);
			
			StringMap smap1 = fsmap.get("return");
			DisplayForestMap(fsmap);
			
			logger.info("swig_GetUnivData: " + ret);
			logger.info("estr/length: " + mestr.getStr() + "/" + mestr.getStr().length());
		
			logger.info("\n\n\n\n");
			
		} catch (Exception e)
		{
			logger.info(e);
		}
	}
	
	public static void DisplayStringMap(StringMap smap)
	{
		if (smap == null)
			return;
		
		MyBool mb = new MyBool();
		MyString nextkey = new MyString();
		SwigSvapi s = new SwigSvapi();
		while (s.swig_SNextKey(smap, nextkey, mb))
		{
			String key = nextkey.getStr();
			logger.info("        " + key + "=" + smap.get(key));
			Thread.currentThread().yield();
		}
	}
	
	public static void DisplayForestMap(ForestMap fsmap)
	{
		if (fsmap == null)
			return;
		
		logger.info("\n   -- DisplayForestMap begin ("+fsmap.size()+" node)"+" -- ");
		
		MyBool mb = new MyBool();
		MyString nextkey = new MyString();
		SwigSvapi s = new SwigSvapi();
		while (s.swig_FNextKey(fsmap, nextkey, mb))
		{
			String key = nextkey.getStr();
			StringMap smap = null;
			try
			{
				smap = fsmap.get(key);
			} catch (Exception e)
			{
				logger.info("     DisplayForestMap: " + e + " \"" + key + "\"");
				continue;
			}
			logger.info("     ---- " + key + " ("+smap.size()+" key)"+" ----");			
			DisplayStringMap(smap);
		}
		logger.info("   -- DisplayForestMap end   -- \n");
	}
}
