package com.siteview.jsvapi;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javolution.util.FastMap;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;
import com.siteview.eccservice.SystemOut;

/**
 * swig 封装的 svapi , 部分模板数据缓存在 java 端
 */
public class Jsvapi
{
	public static final String module = Jsvapi.class.getName();
	private GeneralCacheAdministrator admin = new GeneralCacheAdministrator(UtilProperties.getProperties("oscache"));
	
	static{
		try {
			Debug.logInfo("SVDB Starting ... ", module);
			
			// 获取私有的方法 loadLibrary0
			Method llm = ClassLoader.class.getDeclaredMethod("loadLibrary0", new Class[] { Class.class, File.class });
			llm.setAccessible(true);// 破解权限
			String dllFilename = ServiceUtils.getConfigFilePath() + "swigsvapi.dll";
			llm.invoke(null, new Object[] { Jsvapi.class, new File(dllFilename) });
			init();
			Debug.logInfo("SVDB Started!", module);
		} catch (Exception e) {
			Debug.logError("SVDB Start Fail!", module);
			e.printStackTrace();
		} 		
		
	}

	/**
	 * 本函数的功能：读取 .dll 所在目录下的 svapi.ini ,设置与 svdb 通讯的强制地址。
	 * @throws Exception 
	 */
	public static void init() throws Exception
	{
		Jsvapi svapi = new Jsvapi();
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "SetSvdbAddrByFile");
		String filename = ServiceUtils.getConfigFilePath() + "svapi.ini";
		ndata.put("filename", filename.replace("/", "\\"));
		boolean ret = svapi.getUnivData(fmap, ndata, estr);
		
		//displayUtilMapInMap(fmap);
		Debug.logInfo("SetSvdbAddrByFile:" + ret + (ret ? "" : "errorMessage:" + estr),module);
	}
	
	/**
	 * 本函数的功能：获取与 svdb 通讯的强制地址。
	 */
	public static void test()
	{
		Jsvapi svapi = new Jsvapi();
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetSvdbAddr");
		//ndata.put("dowhat", "GetSvIniFileBySections");
		//ndata.put("filename", "user.ini");
		
		boolean ret = svapi.getUnivData(fmap, ndata, estr);
		
		//displayUtilMapInMap(fmap);
		Debug.logInfo("GetSvdbAddr:" + ret + (ret ? ",address=" + fmap.get("return").get("return") : "errorMessage:" + estr),module);
	}
	
	/**
	 * 此函数对应于 scasvapi.h(即scasvapi.dll) 中的 c++ 函数 GetUnivData
	 * 
	 * @fmap 从 svdb 服务器请求获得的数据
	 * @inwhat 传入的请求
	 * @estr 返回的错误信息 便于调试
	 * 
	 */
	public boolean getUnivData(Map<String, Map<String, String>> fmap, Map<String, String> inwhat, StringBuilder estr)
	{
		boolean gocache = false;
		try
		{
			String dowhat = inwhat.get("dowhat");
			if (dowhat.contains("LoadResource") || dowhat.contains("MonitorTemplet") || dowhat.contains("EntityTemplet") || dowhat.contains("EntityGroup"))
				gocache = true;
		} catch (Exception e)
		{
		}
		if (gocache)
			return getUnivDataCache(fmap, inwhat, estr);
		
		StringMap smap = utilMapToStringMap(inwhat);
		ForestMap fsmap = new ForestMap();
		
		myString mestr = new myString();
		
		boolean ret = swigsvapi.swig_GetUnivData(fsmap, smap, mestr);
		
		forestMapToUtilMapInMap(fmap, fsmap);
		estr.delete(0, estr.length());
		estr.append(mestr.getStr());
		return ret;
	}
	
	/**
	 * 此函数对应于 scasvapi.h(即scasvapi.dll) 中的 c++ 函数 SubmitUnivData
	 * 
	 * @fmap 要提交给 svdb 服务器的数据
	 * @inwhat 传入的请求
	 * @estr 返回的错误信息 便于调试
	 * 
	 */
	public boolean submitUnivData(Map<String, Map<String, String>> fmap, Map<String, String> inwhat, StringBuilder estr)
	{
		StringMap smap = utilMapToStringMap(inwhat);
		ForestMap fsmap = utilMapInMapToForestMap(fmap);
		myString mestr = new myString();
		
		boolean ret = swigsvapi.swig_SubmitUnivData(fsmap, smap, mestr);
		
		forestMapToUtilMapInMap(fmap, fsmap);
		estr.delete(0, estr.length());
		estr.append(mestr.getStr());
		return ret;
	}
	
	/**
	 * 此函数对应于 scasvapi.h(即scasvapi.dll) 中的 c++ 函数 GetForestData
	 * 
	 * @fmap 从 svdb 服务器请求获得的树数据
	 * @inwhat 传入的请求
	 * @estr 返回的错误信息 便于调试
	 * 
	 */
	public boolean getForestData(List<Map<String, String>> vmap, Map<String, String> inwhat, StringBuilder estr)
	{
		StringMap smap = utilMapToStringMap(inwhat);
		ForestVector fvec = new ForestVector();
		myString mestr = new myString();
		
		boolean ret = swigsvapi.swig_GetForestData(fvec, smap, mestr);
		
		forestVectorToUtilMapInVector(vmap, fvec);
		estr.delete(0, estr.length());
		estr.append(mestr.getStr());
		return ret;
	}
	
	/**
	 * 显示 java.util.FastMap 的内容
	 */
	public static void displayUtilMap(String id,Map<String, String> ndata)
	{
		if (ndata == null)
			return;
		
		for (String key : ndata.keySet()){
			SystemOut.println("        " + key + "= \"" + ndata.get(key) + "\"");
		}
	}
	
	/**
	 * 显示 java.util.FastMap in FastMap 的内容
	 */
	public static void displayUtilMapInMap(Map<String, Map<String, String>> fmap)
	{
		if (fmap == null)
			return;
		
		SystemOut.println("\n   -- Display UtilMapInMap begin (" + fmap.size() + " node)" + " -- ");
		for (String key : fmap.keySet())
		{
			Map<String, String> ndata = fmap.get(key);
			SystemOut.println("     ---- " + key + " (" + ndata.size() + " key)" + " ----");
			displayUtilMap(key,ndata);
		}
		SystemOut.println("   -- Display UtilMapInMap end (" + fmap.size() + " node)" + " -- \n");
	}
	
	/**
	 * 显示 java.util.FastMap in vector 的内容
	 */
	public static void displayUtilMapInVector(List<FastMap<String, String>> fmap)
	{
		if (fmap == null)
			return;
		
		SystemOut.println("\n   -- Display UtilMapInVector begin (" + fmap.size() + " node)" + " -- ");
		for (int key = 0; key < fmap.size(); ++key)
		{
			FastMap<String, String> ndata = fmap.get(key);
			SystemOut.println("     ---- No. " + (key + 1) + " (" + ndata.size() + " key)" + " ----");
			displayUtilMap("" + (key + 1),ndata);
		}
		SystemOut.println("   -- Display UtilMapInVector end (" + fmap.size() + " node)" + " -- \n");
	}
	
	private boolean getUnivDataCache(Map<String, Map<String, String>> fmap, Map<String, String> inwhat, StringBuilder estr)
	{
		String myKey = inwhat.toString();
		Map<String, Map<String, String>> tfmap = new FastMap<String, Map<String, String>>();
		
		boolean ret = true;
		try
		{
			tfmap = (Map<String, Map<String, String>>) admin.getFromCache(myKey, 10);
		} catch (NeedsRefreshException nre)
		{
			try
			{
				try
				{
					StringMap smap = utilMapToStringMap(inwhat);
					ForestMap fsmap = new ForestMap();
					myString mestr = new myString();
					
					ret = swigsvapi.swig_GetUnivData(fsmap, smap, mestr);
					
					forestMapToUtilMapInMap(tfmap, fsmap);
					estr.delete(0, estr.length());
					estr.append(mestr.getStr());
				} catch (Exception e)
				{
					ret = false;
					estr.append(e.toString() + ";  ");
				}
				
				if (ret)
					admin.putInCache(myKey, tfmap);
				else
					admin.cancelUpdate(myKey);
				
			} catch (Exception ex)
			{
				tfmap = (Map<String, Map<String, String>>) nre.getCacheContent();
				admin.cancelUpdate(myKey);
			}
		}
		
		fmap.clear();
		if (!tfmap.isEmpty())
			fmap.putAll(tfmap);
		return ret;
	}
	
	/**
	 * 将 java.util.FastMap ，转换为 swig 的 StringMap
	 */
	private static StringMap utilMapToStringMap(Map<String, String> ndata)
	{
		StringMap smap = new StringMap();
		if (ndata == null)
			return smap;
		
		for (String key : ndata.keySet())
			smap.set(key, ndata.get(key));
		return smap;
	}
	
	/**
	 * 将 java.util.FastMap in FastMap，转换为 swig 的 ForestMap
	 */
	private static ForestMap utilMapInMapToForestMap(Map<String, Map<String, String>> fmap)
	{
		ForestMap fsmap = new ForestMap();
		if (fmap == null)
			return fsmap;
		
		for (String key : fmap.keySet())
			fsmap.set(key, utilMapToStringMap(fmap.get(key)));
		return fsmap;
	}
	
	/**
	 * 将 swig 的 StringMap ，转换为 java.util.FastMap
	 */
	private static FastMap<String, String> stringMapToUtilMap(StringMap smap)
	{
		FastMap<String, String> ndata = new FastMap<String, String>();
		if (smap == null)
			return ndata;
		
		myBool mb = new myBool();
		myString nextkey = new myString();
		while (swigsvapi.swig_SNextKey(smap, nextkey, mb))
		{
			String key = nextkey.getStr();
			ndata.put(key, smap.get(key));
		}
		return ndata;
	}
	
	/**
	 * 将 swig 的 ForestMap ，转换为 java.util.FastMap in FastMap
	 */
	private static void forestMapToUtilMapInMap(Map<String, Map<String, String>> fmap, ForestMap fsmap)
	{
		fmap.clear();
		if (fsmap == null)
			return;
		
		myBool mb = new myBool();
		myString nextkey = new myString();
		while (swigsvapi.swig_FNextKey(fsmap, nextkey, mb))
		{
			String key = nextkey.getStr();
			fmap.put(key, stringMapToUtilMap(fsmap.get(key)));
		}
		return;
	}
	
	/**
	 * 将 swig 的 ForestVector ，转换为 java.util.FastMap in Vector
	 */
	private static void forestVectorToUtilMapInVector(List<Map<String, String>> fmap, ForestVector fvec)
	{
		fmap.clear();
		if (fvec == null)
			return;
		
		long size = fvec.size();
		for (int index = 0; index < size; ++index)
			fmap.add(stringMapToUtilMap(fvec.get(index)));
		
		return;
	}
	
	/**
	 * 显示 swig 封装的数据结构 StringMap 的内容
	 */
	private static void displayStringMap(String id,StringMap smap)
	{
		if (smap == null)
			return;
		
		myBool mb = new myBool();
		myString nextkey = new myString();
		while (swigsvapi.swig_SNextKey(smap, nextkey, mb))
		{
			String key = nextkey.getStr();
			SystemOut.println("        " + key + "= \"" + smap.get(key) + "\"");
			
		}
	}
	
	
	/**
	 * 显示 swig 封装的数据结构 ForestMap 的内容
	 */
	private static void displayForestMap(ForestMap fsmap)
	{
		if (fsmap == null)
			return;
		
		SystemOut.println("\n   -- Display ForestMap begin (" + fsmap.size() + " node)" + " -- ");
		
		myBool mb = new myBool();
		myString nextkey = new myString();
		while (swigsvapi.swig_FNextKey(fsmap, nextkey, mb))
		{
			String key = nextkey.getStr();
			StringMap smap = null;
			try
			{
				smap = fsmap.get(key);
			} catch (Exception e)
			{
				SystemOut.println("     Display ForestMap: " + e + " \"" + key + "\"");
				continue;
			}
			SystemOut.println("     ---- " + key + " (" + smap.size() + " key)" + " ----");
			displayStringMap(key,smap);
		}
		SystemOut.println("   -- Display ForestMap end   -- \n");
	}


}
