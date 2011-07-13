package com.siteview.base.manage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import javolution.util.FastMap;

import org.apache.log4j.Logger;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.QueryInfo;
import com.siteview.base.template.TemplateManager;
import com.siteview.ecc.start.EccStarter;
import com.siteview.ecc.start.StarterListener;
import com.siteview.ecc.tuopu.MakeTuopuData;
import com.siteview.ecc.tuopu.TuopulistModel;
import com.siteview.ecc.util.LdapAuth;
import com.siteview.svdb.UnivData;

public class Manager implements StarterListener 
{
	private final static Logger logger = Logger.getLogger(Manager.class);
	
	private static final long serialVersionUID = 739812160724825209L;
	//private final static ServerData m_server = new ServerData();
	// <session,>
	private final static Map<String, View> m_views = new ConcurrentHashMap<String, View>();
	// <session,>
	private final static Map<String, ProfileData> m_profiles = new ConcurrentHashMap<String, ProfileData>();

	private static boolean m_inited;

	private final static Thread m_m_thread;
	private final static Thread m_i_thread;
	private final static Thread m_t_thread;
	
	private final static TemplateManager tpl= new TemplateManager(); //容器启动时（而不是用户操作时），预加载所有模板数据，费时大约几秒。
	
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
		
		
		m_t_thread= new TuopuThread();
		m_t_thread.setName("TuopuThread -- Manager.java");
	//	m_t_thread.start();
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
	 * 在 log out 时调用
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
	 * 强行以管理员身份登录
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
		ServerData.getAdminNamePWD(name, pwd);
		String password = UnivData.decrypt(pwd.toString());
		return createView(name.toString(), password);
	}
	
	/**
	 * 每个浏览器在 login 时创建一个 View （也许多个浏览器 用的是同一个 LoginName ）
	 * @return 返回 session 串
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
		if(tryLdapPwd!=null){//ladp验证通过
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
		ProfileData p = new ProfileData(loginName, session, retm.getFmap().get("return"));
		p.setVisit();
		p.updateTree();
		p.updateTreeInfo();
		View w = new View(p);
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
					//考虑到有多个ip地址的情况
					boolean setflag = false;
					String [] iplist = ipaddress.split(",");//bs 版本的是以 ,为分隔符的
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
	 * 每个浏览器在 login 时创建一个 View （也许多个浏览器 用的是同一个 LoginName ）
	 * @return 返回 session 串
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
			throw new Exception("LDAP 登录认证失败");
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
		ProfileData p = new ProfileData(loginName, session, retm.getFmap().get("return"));
		p.setVisit();
		p.updateTree();
		p.updateTreeInfo();
		View w = new View( p);
		w.getChangeTree();
		
		m_profiles.put(session, p);
		m_views.put(session, w);	
		return session;
	}
	
	/**
	 * 中组部 不需要密码登录
	 * 每个浏览器在 login 时创建一个 View （也许多个浏览器 用的是同一个 LoginName ）
	 * @return 返回 session 串 
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
		ProfileData p = new ProfileData(loginName, session, retm.getFmap().get("return"));
		p.setVisit();
		p.updateTree();
		p.updateTreeInfo();
		View w = new View( p);
		w.getChangeTree();
		
		m_profiles.put(session, p);
		m_views.put(session, w);	
		return session;
	}

	/**
	 * 中组部 不需要密码登录
	 * 每个浏览器在 login 时创建一个 View （也许多个浏览器 用的是同一个 LoginName ）
	 * @return 返回 session 串 
	 */
	public static String createView_zhongZuBu(String loginName,String specialLoginName) throws Exception
	{
		System.out.println(loginName);
		System.out.println(specialLoginName);
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
		ProfileData p = new ProfileData( loginName,specialLoginName, session, retm.getFmap().get("return"));
		p.setVisit();
		p.updateTree();
		p.updateTreeInfo();
		View w = new View(p);
		w.getChangeTree();
		
		m_profiles.put(session, p);
		m_views.put(session, w);	
		return session;
	}
	
	public static String tryLoginByLdap(String LoginName, String PassWord) throws Exception
	{
		if(LoginName==null || "".equals(LoginName))
			throw new Exception("登录名称没有输入！ ");
		
		IniFile ini= ServerData.getUserIni(LoginName);
		if(ini==null)
			throw new Exception("该用户在系统中的配置信息没有找到！");
		String section= ini.getSections();
		if(section==null)
			throw new Exception("该用户在系统中的配置信息没有找到！");

		if(PassWord==null || "".equals(PassWord))
			throw new Exception("密码没有输入！");

		String LDAPProviderUrl= ini.getValue(section, "LDAPProviderUrl");
		if(LDAPProviderUrl==null || "".equals(LDAPProviderUrl)){
			return null;
//				throw new Exception("该用户的 LDAPProviderUrl 没有找到！");
		}
		String LDAPSecurityPrincipal= ini.getValue(section, "LDAPSecurityPrincipal");
		if(LDAPSecurityPrincipal==null || "".equals(LDAPSecurityPrincipal)){
			return null;
//				throw new Exception("该用户的 LDAPSecurityPrincipal 没有找到！");
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
		
		Integer pointUsed= ServerData.getPointUsed();
		Integer networkUsed= ServerData.getNetWorkUsed();
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
			
//			for(String name : TuopulistModel.nameSet){
//				new com.siteview.ecc.tuopu.MakeTuopuData(name);
//			}

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return true;
	}
	
	synchronized private static void updateServerData(RetMapInVector tree, RetMapInMap userini, Map<String, Map<String, String>> plusInfo)
	{
		ServerData.updateTreeData(tree, plusInfo);
		ServerData.updateUserIni(userini);	
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
					Thread.sleep(5*60 * 1000);
				} catch (InterruptedException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	static class TuopuThread extends Thread{
		
		Map<String, String> mapOtherTupuStatu = null;
		public TuopuThread(){
			mapOtherTupuStatu = new HashMap();
			this.setPriority(Thread.MIN_PRIORITY);
		}

		public void run()
		{
			while (true)
			{
				try
				{
					if(TuopulistModel.nameSet.size()<1){
						ArrayList<String> tuopuNameList = new ArrayList<String>();
						tuopuNameList = GetTuopuList();
						for(String name : tuopuNameList){
							try{
								System.out.println("TuopuThread 1111111111111111......................." + name);
								if(name.equals("test_zhuan"))
								{
									System.out.println("TuopuThread ..test_zhuan.......continue..............");
									continue;
								}
								MakeTuopuData tmp = new com.siteview.ecc.tuopu.MakeTuopuData(name, mapOtherTupuStatu);							
								mapOtherTupuStatu.put(name,tmp.getStrMyState());
								System.out.println("TuopuThread getStrMyState......................." + tmp.getStrMyState());
							}catch(Exception e){
								e.printStackTrace();
							}finally{
								Thread.yield();
							}
						}
					}else{
						for(String name : TuopulistModel.nameSet){
							try{
								System.out.println("TuopuThread 222222222222222222222......................." + name);
								if(name.equals("test_zhuan"))
								{
									System.out.println("TuopuThread ..test_zhuan.......continue..............");
									continue;
								}
								MakeTuopuData tmp = new com.siteview.ecc.tuopu.MakeTuopuData(name, mapOtherTupuStatu);							
								mapOtherTupuStatu.put(name,tmp.getStrMyState());
								System.out.println("TuopuThread getStrMyState......................." + tmp.getStrMyState());
							}catch(Exception e){
								e.printStackTrace();
							}finally{
								Thread.yield();
							}
						}
					}
					System.out.println("TuopuThread ....................... test_zhuan");
					MakeTuopuData tmp = new com.siteview.ecc.tuopu.MakeTuopuData("test_zhuan", mapOtherTupuStatu);
					mapOtherTupuStatu.put("test_zhuan",tmp.getStrMyState());
					System.out.println("TuopuThread getStrMyState......................." + tmp.getStrMyState());					
					mapOtherTupuStatu.clear();
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					try {
						Thread.sleep(10*60 * 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		public ArrayList<String> GetTuopuList()
		{
			//2、根据列表增加相应项到tuopu.ini
			IniFile ini = new IniFile("tuopfile.ini");
			ArrayList<String> tuopuNameList = new ArrayList<String>();
			Map<String,String> tuopuMap = new HashMap<String,String>();
				try {
					ini.load();
				} catch (Exception e) 
				{
					e.printStackTrace();
				}
				tuopuMap = ini.getSectionData("filename");
				//要注意中文的部分
				for(String key : tuopuMap.keySet()){					
					int index = key.indexOf(".");
					String temp = key.substring(0, index);
					if(tuopuNameList.contains(temp)==false)
					{
						tuopuNameList.add(temp);
					}
				}
				return tuopuNameList;
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
