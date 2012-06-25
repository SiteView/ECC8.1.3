package com.siteview.base.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;

public class UserManager
{
	private final static Logger logger = Logger.getLogger(UserManager.class);
	/**
	 * 管理员，查看所有用户的信息
	 */
	static public List<UserEdit> getAllUserEdit(IniFile userini, View view, Map<String, IniFile> all)
	{
		List<UserEdit> ret= new ArrayList<UserEdit>();
		if( !isAdmin(userini) )
			return ret;
		if(all==null)
			return ret;
		
		for(String key:all.keySet())
		{
			IniFile ini= all.get(key);
			if(ini!=null)
			{
				UserEdit u= new UserEdit(ini);
				ret.add(u);
			}
		}
		return ret;
	}
	
	
	/**
	 * 管理员，新建一个用户
	 * @return UserEdit 为 null 时失败
	 */
	static public UserEdit createUserEdit(String loginName, IniFile userini, View view,Map<String, IniFile> all) throws Exception
	{
		if(loginName==null || loginName.isEmpty() || userini==null)
			return null;
		if(!isAdmin(userini))
			throw new Exception(" Only admin can create a user! ");
		
		for(String key:all.keySet())
		{
			IniFile ini= all.get(key);
			String name= ini.getValue(ini.getSections(), "LoginName");
			if(name!=null && name.compareTo(loginName)==0)
				throw new Exception(" This loginName("+loginName+") exists already! ");
		}

		long seed= System.currentTimeMillis() + loginName.hashCode() ;
		java.util.Random r=new java.util.Random(seed);
		Integer newindex= 500000+ Math.abs((loginName.hashCode()%100000)) + Math.abs((r.nextInt()%100000));
		while(all.containsKey(newindex.toString()))
			++newindex;
		UserEdit u= new UserEdit(null);	
		u.initNewUserEdit(newindex.toString(), loginName);
		return u;
	}
	
	/**
	 * 管理员，删除一个用户
	 */
	static public boolean deleteUserEdit(UserEdit aUser, IniFile userini) throws Exception
	{
		if(aUser==null || userini==null)
			return false;
		if(!isAdmin(userini))
			throw new Exception(" Only admin can delete a user! ");
		
		IniFile allini= new IniFile("user.ini");
		allini.load();
		String index= aUser.getIndexInUserini();
		if(!index.isEmpty())
		{
			if(index.compareTo("1")==0)
				throw new Exception(" It's a base user, that can't be deleted! ");
			allini.deleteSection(index);
			allini.saveChange();
			Manager.instantUpdate();
			return true;
		}
		return false;
	}
	
	static public boolean isAdmin(IniFile userini)
	{
		if(userini==null)
			return false;
		
		String nAdmin = userini.getValue(userini.getSections(),"nAdmin");
		if (nAdmin != null && nAdmin.compareTo("1") == 0)
			return true;
		return false;
	}
	
	public static void main(String[] args)
	{
		View w=null;
		try
		{
			String session = Manager.createView("admin", "siteview");
			w= Manager.getView(session);
			//w.getChangeTree();
		} catch (Exception e)
		{
			e.printStackTrace();
		}	
		if(w==null)
			return;
		
		testCreate(w);
		
//		 ArrayList<UserEdit> allu= w.getAllUserEdit();
//		 for(UserEdit u: allu)
//		 {
////			 u.display();
////			 logger.info(u.getLoginName() +"(LoginName)  userName:" + u.getUserName() +"  is use:" + u.isEnable() +"  is admin:" + u.isAdmin());
//			 testdelete(u,w);
//		 }
	}
	
	static void testCreate(View w)
	{
		try
		{
			UserEdit u= w.createUserEdit("563");
			u.saveChange();
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	static void testdelete(UserEdit u, View w)
	{
		try
		{
			if (u.getLoginName().equals("555"))
				w.deleteUserEdit(u);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		int index= 100;
//		try
//		{
//			index = new Integer(u.getIndexInUserini()).intValue();
//		} catch (NumberFormatException e1)
//		{
//			index= 0;
//		}
//		if (index == 0)
//		{
//			try
//			{
//				deleteUserEdit(u, w.getUserIni());
//			} catch (Exception e)
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}

	}
	
	static void testcxy(UserEdit u, View w)
	{
		 if(u.getLoginName().equals("cxy"))
		 {
//			 logger.info(" encode: "+ u.encodeStr("cxy") );
			 logger.info(u.getLoginName() +"(LoginName) is admin:" + u.isAdmin());
			 INode node= w.getNode("1.356.1");
			 try
			{
				u.setNodeVisible(node, false);
				
				u.saveChange();
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 }
	}
	
}
