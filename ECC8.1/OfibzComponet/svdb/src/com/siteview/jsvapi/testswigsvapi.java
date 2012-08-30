package com.siteview.jsvapi;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import com.siteview.eccservice.SystemOut;

public class testswigsvapi
{
	
	public static void main(String[] argv)
	{
		try
		{
			System.loadLibrary("swigsvapi");
		} catch (UnsatisfiedLinkError e)
		{
			System.err.println("Native code library failed to load. See the chapter on Dynamic Linking Problems in the SWIG Java documentation for help.\n" + e);
			System.exit(1);
		}
		SystemOut.println("loadLibrary is done!");
		
		try
		{
			swigsvapi s = new swigsvapi();
			s.swig_test1();
			
			HashMap<String, HashMap<String, String>> fmap = new HashMap<String, HashMap<String, String>>();
			HashMap<String, String> ndata = new HashMap<String, String>();
			
			ForestMap fsmap = new ForestMap();
			StringMap smap = new StringMap();
			myString mestr = new myString();
			smap.set("dowhat", "GetSvdbAddr");
			boolean ret = s.swig_GetUnivData(fsmap, smap, mestr);
			
			StringMap smap1 = fsmap.get("return");
			DisplayForestMap(fsmap);
			
			SystemOut.println("swig_GetUnivData: " + ret);
			SystemOut.println("estr/length: " + mestr.getStr() + "/" + mestr.getStr().length());
		
			SystemOut.println("\n\n\n\n");
			
		} catch (Exception e)
		{
			SystemOut.println(e);
		}
	}
	
	public static void DisplayStringMap(StringMap smap)
	{
		if (smap == null)
			return;
		
		myBool mb = new myBool();
		myString nextkey = new myString();
		swigsvapi s = new swigsvapi();
		while (s.swig_SNextKey(smap, nextkey, mb))
		{
			String key = nextkey.getStr();
			SystemOut.println("        " + key + "=" + smap.get(key));
		}
	}
	
	public static void DisplayForestMap(ForestMap fsmap)
	{
		if (fsmap == null)
			return;
		
		SystemOut.println("\n   -- DisplayForestMap begin ("+fsmap.size()+" node)"+" -- ");
		
		myBool mb = new myBool();
		myString nextkey = new myString();
		swigsvapi s = new swigsvapi();
		while (s.swig_FNextKey(fsmap, nextkey, mb))
		{
			String key = nextkey.getStr();
			StringMap smap = null;
			try
			{
				smap = fsmap.get(key);
			} catch (Exception e)
			{
				SystemOut.println("     DisplayForestMap: " + e + " \"" + key + "\"");
				continue;
			}
			SystemOut.println("     ---- " + key + " ("+smap.size()+" key)"+" ----");			
			DisplayStringMap(smap);
		}
		SystemOut.println("   -- DisplayForestMap end   -- \n");
	}
}
