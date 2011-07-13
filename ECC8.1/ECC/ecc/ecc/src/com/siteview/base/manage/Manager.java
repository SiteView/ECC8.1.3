package com.siteview.base.manage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javolution.util.FastMap;

import org.apache.log4j.Logger;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.QueryInfo;
import com.siteview.base.template.TemplateManager;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.start.EccStarter;
import com.siteview.ecc.start.StarterListener;
import com.siteview.ecc.util.LdapAuth;
import com.siteview.svdb.UnivData;

public class Manager implements StarterListener 
{
	private final static Logger logger = Logger.getLogger(Manager.class);
	
	private static final long serialVersionUID = 739812160724825209L;
	private final static ServerData m_server = new ServerData();
	// <session,>
	private final static Map<String, View> m_views = new ConcurrentHashMap<String, View>();
	// <session,>
	private final static Map<String, ProfileData> m_profiles = new ConcurrentHashMap<String, ProfileData>();

	private static boolean m_inited;

	private final static Thread m_m_thread;
	private final static Thread m_i_thread;
	
	private final static TemplateManager tpl= new TemplateManager(); //��������ʱ���������û�����ʱ����Ԥ��������ģ�����ݣ���ʱ��Լ���롣

	public TemplateManager getTemplateManager(){
		return tpl;
	}
	static
	{
		
		ManageSvapi.setShow(true);
		
		m_m_thread= new ManagerThread();
		m_m_thread.setName("ManagerThread -- Manager.java");
		m_m_thread.start();
		
		m_i_thread= new InstantUpdateThread();
		m_i_thread.setName("InstantUpdateThread -- Manager.java");
		m_i_thread.start();
	}
	
	@Override
	public void startInit(EccStarter starter)
	{
	}
	@Override
	public void destroyed(EccStarter starter)
	{
	}
	
	public static View getView(String session)
	{
		if(session==null)
			return null;
		return m_views.get(session);
	}
	
	/**
	 * �� log out ʱ����
	 */
	public static void invalidateView(String session)
	{
		if(session==null)
			return;
		
		logger.info("---------------------------------------------------------");
		logger.info("base.manage.Manager Invalidate a session! :" + session);
		logger.info("---------------------------------------------------------");
		m_profiles.remove(session);
		m_views.remove(session);
	}
	
	
	/**
	 * ǿ���Թ���Ա��ݵ�¼
	 */
	public static String forceLoginAsAdmin() throws Exception
	{
		try
		{
			while(!m_inited)
				Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		StringBuilder name= new StringBuilder();
		StringBuilder pwd= new StringBuilder();
		m_server.getAdminNamePWD(name, pwd);
		String password = UnivData.decrypt(pwd.toString());
		return createView(name.toString(), password);
	}
	
	/**
	 * ÿ��������� login ʱ����һ�� View ��Ҳ��������� �õ���ͬһ�� LoginName ��
	 * @return ���� session ��
	 */
	public static String createView(String loginName, String passWord) throws Exception
	{
		if(loginName==null || "".equals(loginName))
			throw new Exception("Failed to login : LoginName is null! ");
		if(passWord==null || "".equals(passWord))
			throw new Exception("Failed to login : PassWord is null! ");

		try
		{
			while(!m_inited)
				Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		HashMap<String, String> ndata = new HashMap<String, String>();
/*		String tryLdapPwd= tryLoginByLdap(loginName,passWord);
		if(tryLdapPwd!=null){//ladp��֤ͨ��
			passWord= tryLdapPwd;
		}*/
		ndata.put("dowhat", "TryLogin");
		ndata.put("LoginName", loginName);
		ndata.put("PassWord", passWord);

		RetMapInMap retm= ManageSvapi.GetUnivData(ndata);
		if(retm.getRetbool()==false)
			throw new Exception("Failed to login :" + retm.getEstr() );
		
		long seed= System.currentTimeMillis() + loginName.hashCode() + passWord.hashCode();
		java.util.Random r=new java.util.Random(seed);
		Long i= new Long( r.nextLong() );
		if(i<0)
			i= -i;
		int times= 1;
		while (m_profiles.containsKey("" + i))
		{
			logger.info("base.manage.CreateView " + ++times +"th try to get random !");
			i = r.nextLong();
		}
		
		String session = i.toString();
		ProfileData p = new ProfileData(m_server, loginName, session, retm.getFmap().get("return"));
		p.setVisit();
		p.updateTree();
		p.updateTreeInfo();
		View w = new View(m_server, p);
		w.getChangeTree();
		
		m_profiles.put(session, p);
		m_views.put(session, w);	
		return session;
	}
	
	public static boolean  IPCheck(String ip) throws Exception {

		logger.info(ip);
		
		IniFile ini = new IniFile("general.ini");
		try{
			ini.load();
		}catch(Exception e){
			e.printStackTrace();
			return true;
		}
		try{
			if ("1".equals(ini.getValue("IPCheck", "isCheck")))
			{
				String ipaddress = ini.getValue("IPCheck", "IPAddress");
				if(ipaddress == null || "".equals(ipaddress.trim())){
					return true;
				}else
				{
					//���ǵ��ж��ip��ַ�����
					boolean setflag = false;
					String [] iplist = ipaddress.split(",");//bs �汾������ ,Ϊ�ָ�����
					for(String oneIP:iplist){
						if(oneIP.trim().equals(ip)){
							setflag = true;
							break;
						}
					}
					if(setflag){
						return true;
					}else
					{
						return false;
					}
				}
			}
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return true;
		}
	}
	/**
	 * ÿ��������� login ʱ����һ�� View ��Ҳ��������� �õ���ͬһ�� LoginName ��
	 * @return ���� session ��
	 */
	public static String createViewByLdap(String loginName, String passWord) throws Exception
	{
		try
		{
			while(!m_inited)
				Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		String tryLdapPwd= tryLoginByLdap(loginName,passWord);
/*		if(tryLdapPwd == null){
			return createView(loginName, passWord);
		}*/
		Map<String, String> ndata = new FastMap<String, String>();
		if(tryLdapPwd!=null){
			passWord= tryLdapPwd;
			ndata.put("dowhat", "TryLogin");
			ndata.put("LoginName", loginName);
			ndata.put("PassWord", passWord);
		}else{
			throw new Exception("LDAP ��¼��֤ʧ��");
		}
		
		RetMapInMap retm= ManageSvapi.GetUnivData(ndata);
		if(retm.getRetbool()==false)
			throw new Exception("Failed to login :" + retm.getEstr() );
		
		long seed= System.currentTimeMillis() + loginName.hashCode() + passWord.hashCode();
		java.util.Random r=new java.util.Random(seed);
		Long i= new Long( r.nextLong() );
		if(i<0)
			i= -i;
		int times= 1;
		while (m_profiles.containsKey("" + i))
		{
			logger.info("base.manage.CreateView " + ++times +"th try to get random !");
			i = r.nextLong();
		}
		
		String session = i.toString();
		ProfileData p = new ProfileData(m_server, loginName, session, retm.getFmap().get("return"));
		p.setVisit();
		p.updateTree();
		p.updateTreeInfo();
		View w = new View(m_server, p);
		w.getChangeTree();
		
		m_profiles.put(session, p);
		m_views.put(session, w);	
		return session;
	}
	
	/**
	 * ���鲿 ����Ҫ�����¼
	 * ÿ��������� login ʱ����һ�� View ��Ҳ��������� �õ���ͬһ�� LoginName ��
	 * @return ���� session �� 
	 */
	public static String createView_zhongZuBu(String loginName) throws Exception
	{
		if(loginName==null || "".equals(loginName))
			throw new Exception("Failed to login : LoginName is null! ");
		try
		{
			while(!m_inited)
				Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		HashMap<String, String> ndata = new HashMap<String, String>();

		ndata.put("dowhat", "TryToLogin_NoPassWord");
		ndata.put("LoginName", loginName);

		RetMapInMap retm= ManageSvapi.GetUnivData(ndata);
		if(retm.getRetbool()==false)
			throw new Exception("Failed to login :" + retm.getEstr() );
		
		long seed= System.currentTimeMillis() + loginName.hashCode() ;
		java.util.Random r=new java.util.Random(seed);
		Long i= new Long( r.nextLong() );
		if(i<0)
			i= -i;
		int times= 1;
		while (m_profiles.containsKey("" + i))
		{
			logger.info("base.manage.CreateView " + ++times +"th try to get random !");
			i = r.nextLong();
		}
		
		String session = i.toString();
		ProfileData p = new ProfileData(m_server, loginName, session, retm.getFmap().get("return"));
		p.setVisit();
		p.updateTree();
		p.updateTreeInfo();
		View w = new View(m_server, p);
		w.getChangeTree();
		
		m_profiles.put(session, p);
		m_views.put(session, w);	
		return session;
	}

	public static String tryLoginByLdap(String LoginName, String PassWord) throws Exception
	{
		if(LoginName==null || "".equals(LoginName))
			throw new Exception("��¼����û�����룡 ");
		
		IniFile ini= m_server.getUserIni(LoginName);
		if(ini==null)
			throw new Exception("���û���ϵͳ�е�������Ϣû���ҵ���");
		String section= ini.getSections();
		if(section==null)
			throw new Exception("���û���ϵͳ�е�������Ϣû���ҵ���");

		if(PassWord==null || "".equals(PassWord))
			throw new Exception("����û�����룡");

		String LDAPProviderUrl= ini.getValue(section, "LDAPProviderUrl");
		if(LDAPProviderUrl==null || "".equals(LDAPProviderUrl)){
			return null;
//				throw new Exception("���û��� LDAPProviderUrl û���ҵ���");
		}
		String LDAPSecurityPrincipal= ini.getValue(section, "LDAPSecurityPrincipal");
		if(LDAPSecurityPrincipal==null || "".equals(LDAPSecurityPrincipal)){
			return null;
//				throw new Exception("���û��� LDAPSecurityPrincipal û���ҵ���");
		}
		
		if( !LdapAuth.tryAuth(LDAPProviderUrl, LDAPSecurityPrincipal, PassWord) )
			return null;
		String pwd= ini.getValue(section, "Password");
		return UnivData.decrypt(pwd);
	}
	
	public static Map<String, String> getLicenseData()
	{
		Map<String, String> data=null;
		for (String key: m_profiles.keySet())
		{
			ProfileData p = m_profiles.get(key);
			if (p != null) 
			{
				data= p.getLoginData();
				break;
			}
		}
		if(data==null)
			data= new HashMap<String, String>();
		
		Integer pointUsed= m_server.getPointUsed();
		Integer networkUsed= m_server.getNetWorkUsed();
		data.put("pointUsed", pointUsed.toString());
		data.put("networkUsed", networkUsed.toString());	
		return data;
	}
	
	public boolean teleLoad()
	{
		try
		{
			
			//Date d= new Date();
//			logger.info("base.manage.Manager.teleLoad() beginning("+d.toLocaleString()+")...  " );
			
			HashMap<String, String> ndata = new HashMap<String, String>();
			ndata.put("dowhat", "GetTreeData");
			ndata.put("parentid", "default");
			ndata.put("onlySon", "false");	
			RetMapInVector tree= ManageSvapi.GetForestData(ndata);
			
			HashMap<String, String> ndata2 = new HashMap<String, String>();
			ndata2.put("dowhat", "GetSvIniFileBySections");
			ndata2.put("sections", "default");
			ndata2.put("filename", "user.ini");	
			RetMapInMap userini= ManageSvapi.GetUnivData(ndata2);
				
			QueryInfo q= new QueryInfo();
			q.needkey= "_MachineName,sv_description,sv_monitortype,creat_timeb";
			q.setNeedType_all();
			Map<String, Map<String, String>> plusInfo=null;
			try
			{
				plusInfo= q.load();
			} catch (Exception e)
			{
			}
			
			updateServerData(tree,userini,plusInfo);
			for (String key: m_profiles.keySet())
			{
				ProfileData p = m_profiles.get(key);
				if (p != null) 
				{
					p.updateTree();
					p.updateTreeInfo();
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	synchronized private static void updateServerData(RetMapInVector tree, RetMapInMap userini, Map<String, Map<String, String>> plusInfo)
	{
		m_server.updateTreeData(tree, plusInfo);
		m_server.updateUserIni(userini);	
		m_inited= true;
	}
	
	public void checkAbsentSession()
	{
		try
		{
			Set<String> todel= new TreeSet<String>();
			for (String key: m_profiles.keySet())
			{
				ProfileData p= m_profiles.get(key);
				View view = m_views.get(key);
				p.setOnceMoreAbsent();
				if (p.getHowManyTimesOfAbsent() > 4)
					todel.add(key);
			}
			for (String ses : todel)
			{
				if (ses == null) continue;
				m_profiles.remove(ses);
				m_views.remove(ses);
			}			
			if (!todel.isEmpty())
			{
				logger.info("---------------------------------------------------------");
				logger.info("base.manage.Manager delete " + todel.size() + " absent session! ");
				logger.info("---------------------------------------------------------");
			}
			Date d2= new Date();
			logger.info("-------- " + m_profiles.size() + " users online ! ("+d2.toLocaleString()+") --------" );
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static ArrayList<String> getOnlineLoginName()
	{
		ArrayList<String> a= new ArrayList<String>();
		for (String key: m_profiles.keySet())
		{
			ProfileData p= m_profiles.get(key);
			if(p!=null)
				a.add(p.getLoginName());
		}
		return a;
	}
	
	static class ManagerThread extends Thread
	{	
		private Manager manager = new Manager();
		public void run()
		{
			while (true)
			{
				manager.checkAbsentSession();
				manager.teleLoad();
				try
				{
					Thread.sleep(90 * 1000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	private volatile static boolean bInstantUpdate = false; 
	
	public static void instantUpdate()
	{
		bInstantUpdate = true;
	}
	
	public static boolean isInstanceUpdated(){
		return bInstantUpdate == false;
	}
	static boolean isExecuting = false; 
	public static void checkInstantUpdate()
	{
		if(bInstantUpdate && isExecuting == false)
		{
			isExecuting = true;
			Manager manager = new Manager();
			manager.teleLoad();
			bInstantUpdate = false;
			isExecuting = false;
		}
	}
	
	static class InstantUpdateThread extends Thread
	{		
		public void run()
		{
			while (true)
			{
				try
				{
					Thread.sleep(200);
					checkInstantUpdate();
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
