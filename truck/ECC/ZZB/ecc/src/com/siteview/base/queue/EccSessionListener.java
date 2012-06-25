package com.siteview.base.queue;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.http.WebManager;
import org.zkoss.zk.ui.sys.WebAppCtrl;

import com.siteview.base.manage.Manager;

public class EccSessionListener implements HttpSessionListener,ServletRequestListener {
	private final static Logger logger = Logger.getLogger(EccSessionListener.class);
	public static int online=0;
	private final static Map<HttpSession,Date> m_seesion_visit = new ConcurrentHashMap<HttpSession,Date>(); 
	
	/*static 
	{
		CheckThread chth= new CheckThread();
		Thread thread= new Thread(chth);
		thread.start();
	}*/
	
	@Override
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		online++;
//		m_seesion_visit.put(sessionEvent.getSession(), new Date());
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		if(online>0)
			online--;

		HttpSession session = sessionEvent.getSession();
		Object strSession=session.getAttribute("usersessionid");
		//removeZkSession(session);		
		if(strSession!=null)
		{
			Manager.invalidateView(strSession.toString());
			//Toolkit.getToolkit().cleanSession(session);
		}
//		m_seesion_visit.remove(session);
	}
	
	public void requestDestroyed(ServletRequestEvent sre)
	{

	}

	public void requestInitialized(ServletRequestEvent sre)
	{
//		HttpServletRequest sr= (HttpServletRequest)sre.getServletRequest();
//		if(sr==null)
//			return;
//		m_seesion_visit.put(sr.getSession(), new Date());
	}	
	
	public static org.zkoss.zk.ui.Session getZkSession (HttpSession httpSession)
	{
		try
		{
			if(httpSession==null)
				return null;
			
			WebManager wma= WebManager.getWebManager(httpSession.getServletContext());
			WebAppCtrl ctrl = (WebAppCtrl)wma.getWebApp();
			org.zkoss.zk.ui.Session zkSession= ctrl.getSessionCache().get(httpSession);
			return zkSession;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static void removeZkSession1(org.zkoss.zk.ui.Session session)
	{
		try
		{
			if (session == null)
				return;
			synchronized (session)
			{
				WebAppCtrl ctrl = (WebAppCtrl) session.getWebApp();
				ctrl.sessionDestroyed(session);
			}
			logger.info("Destroyed a zk session by EccSessionListener !");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void removeZkSession1(HttpSession httpSession)
	{
		if(httpSession==null)
			return;
		org.zkoss.zk.ui.Session ZkSession= getZkSession(httpSession);
		if(ZkSession==null)
			return;
		removeZkSession1(ZkSession);
	}
	
	public static void checkAllHttpSession()
	{
		Date nowdate= new Date();
		ArrayList<HttpSession> toRemove = new ArrayList<HttpSession>();
		for(HttpSession ses:m_seesion_visit.keySet())
		{
			if(ses==null)
				continue;
			Date visitDate= m_seesion_visit.get(ses);
			if(visitDate==null)
			{
				toRemove.add(ses);
				continue;
			}
			
			long howlong= nowdate.getTime()- visitDate.getTime();
			if(howlong > 1000 * 60 * 5)
			{
				//removeZkSession(ses);
				toRemove.add(ses);
			}
		}
		for(HttpSession ses:toRemove)
		{
			m_seesion_visit.remove(ses);
		}
	}

	/*public static class CheckThread implements Runnable不需要此线程
	{		
		public void run()
		{
			while (true)
			{
				try
				{
					Thread.sleep(20 * 1000);
					checkAllHttpSession();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}*/
	
}
