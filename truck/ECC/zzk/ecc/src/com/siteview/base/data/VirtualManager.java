package com.siteview.base.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;

public class VirtualManager
{
	
	/**
	 * 获取某用户的所有虚拟视图
	 */
	static public List<VirtualView> getAllVirtualView(IniFile userini, View view)
	{
		List<VirtualView> ret= new FastList<VirtualView>();
		VirtualView v= new VirtualView(VirtualView.DefaultView, null, userini, view);
		ret.add(v);
		
		IniFile allv = null;
		try {
			allv = getAllVirtualViewInIFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(allv==null || userini==null)
			return ret;
		
		boolean isAdmin= false;
		String nAdmin = userini.getValue(userini.getSections(),"nAdmin");
		if (nAdmin != null && nAdmin.compareTo("1") == 0)
			isAdmin= true;
		
		String index= userini.getSections();
		List<String> sec= allv.getSectionList();
		for(String viewName: sec)
		{
			String fileName= allv.getValue(viewName, "fileName");
			String user = allv.getValue(viewName, "user");
			if(user==null)
				user="";
			user = "," + user + ",";
			if(index==null || viewName==null || fileName==null || view==null)
				continue;
			if(index.isEmpty() || viewName.isEmpty() || fileName.isEmpty())
				continue;
			
			if (isAdmin || user.contains("," + index + ","))
			{
				VirtualView v2= new VirtualView(viewName, fileName, userini, view);
				ret.add(v2);	
			}
		}	
		return ret;
	}
	
	/**
	 * 获取某用户的所有虚拟视图
	 * @throws Exception 
	 */
	static public List<VirtualView> getAllVirtualViewThrowException(IniFile userini, View view) throws Exception
	{
		List<VirtualView> ret= new FastList<VirtualView>();
		VirtualView v= new VirtualView(VirtualView.DefaultView, null, userini, view);
		ret.add(v);
		
		IniFile allv= getAllVirtualViewInIFile();
		if(allv==null || userini==null)
			return ret;
		
		boolean isAdmin= false;
		String nAdmin = userini.getValue(userini.getSections(),"nAdmin");
		if (nAdmin != null && nAdmin.compareTo("1") == 0)
			isAdmin= true;
		
		String index= userini.getSections();
		List<String> sec= allv.getSectionList();
		for(String viewName: sec)
		{
			String fileName= allv.getValue(viewName, "fileName");
			String user = allv.getValue(viewName, "user");
			if(user==null)
				user="";
			user = "," + user + ",";
			if(index==null || viewName==null || fileName==null || view==null)
				continue;
			if(index.isEmpty() || viewName.isEmpty() || fileName.isEmpty())
				continue;
			
			if (isAdmin || user.contains("," + index + ","))
			{
				VirtualView v2= new VirtualView(viewName, fileName, userini, view);
				ret.add(v2);	
			}
		}	
		return ret;
	}
	
	/**
	 * 在服务器上新建一个虚拟视图
	 * @return VirtualView 为 null 时失败
	 */
	static public VirtualView createVirtualView(String viewName, IniFile userini, View view) throws Exception
	{
		if(viewName.isEmpty())
			throw new Exception(" viewName is empty ! ");
		if(viewName.equals(VirtualView.DefaultView))
			throw new Exception(" viewName is equal to DefaultView ! ");
		
		TreeSet<String> fnames= new TreeSet<String>(); 
		IniFile allv= getAllVirtualViewInIFile();
		List<String> sec= allv.getSectionList();
		for(String Name: sec)
		{
			String fileName= allv.getValue(Name, "fileName");
			if(fileName!=null && !fileName.isEmpty())
				fnames.add(fileName);
		}	
		Integer count = new Integer(sec.size());
		String newfileName = "view" + count + ".ini";
		while (fnames.contains(newfileName))
			newfileName = "view" + ++count + ".ini";
		while(true)
		{
			IniFile tryini= new IniFile(newfileName);
			try
			{
				tryini.load();
				newfileName = "view" + ++count + ".ini";
			} catch (Exception e)
			{
				break;
			}
		}

		Map<String, String> ndata = new FastMap<String, String>();
		ndata.put("dowhat", "AddView");
		ndata.put("viewName", viewName);
		ndata.put("fileName", newfileName);
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception(" Failed to createVirtualView, since:"+ ret.getEstr());
		
		IniFileKeyValue user= new IniFileKeyValue("allVirtualViewName.ini", viewName, "user"); 
		user.setValue(userini.getSections());
		VirtualView v= new VirtualView(viewName, newfileName, userini, view);
		return v;
	}
	
//	if(newViewName.contains(".") || newViewName.contains(" "))
//		throw new Exception(" newViewName can't contains \".\"(dot) or \" \"(blank) ! ");
//	if (isNumber)
//		throw new Exception(" newViewName can't be a number ! ");
	
	/**
	 * 修改某虚拟视图的显示名字
	 * @param view 要改名的视图
	 * @param newViewName 该视图新的名字
	 */
	static public boolean changeNameOfVirtualView(VirtualView view, String newViewName, IniFile userini) throws Exception
	{
		String vname= view.getViewName();
		if(vname.equals(VirtualView.DefaultView))
			throw new Exception(" DefaultView can't be changed name ! ");
		String fname= view.getFileName();
		if(fname.isEmpty())
			throw new Exception(" File name of this VirtualView is empty ! ");			
		if(newViewName.isEmpty())
			throw new Exception(" newViewName is empty ! ");
		
		Map<String, String> ndata = new FastMap<String, String>();
		ndata.put("dowhat", "AddView");
		ndata.put("viewName", vname);
		ndata.put("fileName", fname);
		ndata.put("newViewName", newViewName);
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception(" Failed to changeNameOfVirtualView, since:"+ ret.getEstr());
		return true;
	}
	
	/**
	 * 删除一个虚拟视图
	 */
	static public boolean deleteVirtualView(VirtualView view, IniFile userini) throws Exception
	{
		String vname= view.getViewName();
		if(vname.equals(VirtualView.DefaultView))
			throw new Exception(" DefaultView can't be deleted! ");
		
		Map<String, String> ndata = new FastMap<String, String>();
		ndata.put("dowhat", "DeleteView");
		ndata.put("viewName", vname);
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception(" Failed to deleteVirtualView, since:"+ ret.getEstr());
		return true;
	}
	
	/**
	 * 管理员，修改某虚拟视图的用户授权
	 * @param view 某虚拟视图
	 * @param aUser 某用户
	 * @param hasRight 为 true 则该用户可见该虚拟视图，false 则不可见
	 */
	static public boolean changeUserOfVirtualView(VirtualView view, String uindex, boolean hasRight,  IniFile userini)throws Exception
	{
		boolean isAdmin= false;
		String nAdmin = userini.getValue(userini.getSections(),"nAdmin");
		if (nAdmin != null && nAdmin.compareTo("1") == 0)
			isAdmin= true;
		if( !isAdmin )
			throw new Exception(" Only admin can changeUser Of VirtualView! ");
		
		String vname= view.getViewName();
		if(uindex==null || uindex.isEmpty())
			throw new Exception(" Invalid user ! ");
		
		IniFileKeyValue allv= new IniFileKeyValue("allVirtualViewName.ini", vname, "user");
		String value= allv.load();
		if(hasRight && value.contains(uindex))
			return true;
		if(!hasRight && !value.contains(uindex))
			return true;
		
		if(hasRight)
		{
			if( value.endsWith(",") )
				value= value + uindex + ",";
			else
				value= value + "," + uindex + ",";

			allv.setValue(value);
			return true;
		}
		else
		{
			if( !value.startsWith(",") )
				value= "," + value;
			if( !value.endsWith(",") )
				value= value + ",";
			value= value.replace("," + uindex + ",", ",");
			
			allv.setValue(value);
			return true;
		}
	}
	
	static private IniFile getAllVirtualViewInIFile() throws Exception
	{
		IniFile ret= new IniFile("allVirtualViewName.ini");
		try
		{
			ret.load();
		} catch (Exception e)
		{
			String errorMessage = e.getMessage();
			if(errorMessage.contains("is not exis")){
				throw new Exception("文件不存在");
			}else if(errorMessage.contains("is empty")){
				throw new Exception("文件存在，但为空");
			}
//			if(errorMessage.contains("allVirtualViewName.ini")){
//				throw new Exception("The file is allVirtualViewName.ini");
//			}
//			e.printStackTrace();
//			return ret;
		}
		return ret;
	}
}
