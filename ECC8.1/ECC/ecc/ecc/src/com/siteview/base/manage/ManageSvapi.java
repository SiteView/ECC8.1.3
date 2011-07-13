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
	 * �˺�����Ӧ�� Jsvapi.GetUnivData
	 * 
	 * @return �� ecc api(ҵ���߼���)���ص����� RetMapInMap
	 * @inwhat ���������
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
	 * �˺�����Ӧ�� Jsvapi.SubmitUnivData
	 * 
	 * @return �� ecc api(ҵ���߼���)���ص����� RetMapInMap
	 * @fmap Ҫ�ύ�� ecc api ������
	 * @inwhat ���������
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
	 * �˺�����Ӧ�� Jsvapi.GetForestData
	 * 
	 * @return �� ecc api(ҵ���߼���)�����õ������� RetMapInVector
	 * @inwhat ���������
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
