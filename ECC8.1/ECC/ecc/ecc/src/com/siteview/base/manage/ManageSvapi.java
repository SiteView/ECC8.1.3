package com.siteview.base.manage;

import java.util.List;
import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.apache.log4j.Logger;

import com.siteview.jsvapi.Jsvapi;

public class ManageSvapi
{
	private final static Logger logger = Logger.getLogger(ManageSvapi.class);
//	static private Jsvapi	svapi	= null;
	static private boolean	m_show	= false;
/*	static
	{
		if (svapi == null)
		{
			svapi = new Jsvapi();
			SetSvdbAddr(true);
			TemplateManager tpl= new TemplateManager();
			m_show	= true;
		}
	}
*/	
	synchronized static public void setShow(boolean show)
	{
		m_show= false;
	}
	
	/**
	 * 此函数对应于 Jsvapi.GetUnivData
	 * 
	 * @return 从 ecc api(业务逻辑层)返回的数据 RetMapInMap
	 * @inwhat 传入的请求
	 * 
	 */
	public static RetMapInMap GetUnivData(Map<String, String> inwhat)
	{
		long start = System.currentTimeMillis();
		
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		StringBuilder estr = new StringBuilder();
		boolean ret = true;
			
		String dowhat = inwhat.get("dowhat");
		try
		{
			ret = Jsvapi.getInstance().GetUnivData(fmap, inwhat, estr);
		} catch (Exception e)
		{
			estr.append(e + " ;  ");
			logger.info(e);
			ret = false;
		}
		if(m_show)
			logger.info(" ManageSvapi GetUnivData dowhat: " + dowhat + "\n                         /ret: " + ret + "   run:" + (float) (System.currentTimeMillis() - start) / 1000 + " s");
		String ev = estr.toString();
		if (ret==false && !ev.isEmpty())
			logger.info("                         /estr:" + ev);	
		return new RetMapInMap(ret, ev, fmap);
	}
	
	/**
	 * 此函数对应于 Jsvapi.SubmitUnivData
	 * 
	 * @return 从 ecc api(业务逻辑层)返回的数据 RetMapInMap
	 * @fmap 要提交给 ecc api 的数据
	 * @inwhat 传入的请求
	 * 
	 */
	public static RetMapInMap SubmitUnivData(Map<String, Map<String, String>> fmap, Map<String, String> inwhat)
	{
		long start = System.currentTimeMillis();
		String dowhat = inwhat.get("dowhat");
		StringBuilder estr = new StringBuilder();
		boolean ret = true;
		try
		{
			ret = Jsvapi.getInstance().SubmitUnivData(fmap, inwhat, estr);
		} catch (Exception e)
		{
			estr.append(e + " ;  ");
			logger.info(e);
			ret = false;
		}
		if(m_show)
			logger.info(" ManageSvapi SubmitUnivData dowhat: " + dowhat + "\n                         /ret: " + ret + "   run:" + (float) (System.currentTimeMillis() - start) / 1000 + " s");
		String ev = estr.toString();
		if (ret==false && !ev.isEmpty())
			logger.info("                         /estr:" + ev);	
		return new RetMapInMap(ret, ev, fmap);
	}
	
	/**
	 * 此函数对应于 Jsvapi.GetForestData
	 * 
	 * @return 从 ecc api(业务逻辑层)请求获得的树数据 RetMapInVector
	 * @inwhat 传入的请求
	 * 
	 */
	public static RetMapInVector GetForestData(Map<String, String> inwhat)
	{
		long start = System.currentTimeMillis();
		
		List<Map<String, String>> vmap = new FastList<Map<String, String>>();
		StringBuilder estr = new StringBuilder();
		boolean ret = true;		
		String dowhat = inwhat.get("dowhat");
		
		try
		{
			ret = Jsvapi.getInstance().GetForestData(vmap, inwhat, estr);
		} catch (Exception e)
		{
			estr.append(e + " ;  ");
			logger.info(e);
			ret = false;
		}
		if(m_show)
			logger.info(" ManageSvapi GetForestData dowhat: " + dowhat + "\n                         /ret: " + ret + "   run:" + (float) (System.currentTimeMillis() - start) / 1000 + " s");
		String ev = estr.toString();
		if (ret==false && !ev.isEmpty())
			logger.info("                         /estr:" + ev);		
		return new RetMapInVector(ret, ev, vmap);
	}

}
