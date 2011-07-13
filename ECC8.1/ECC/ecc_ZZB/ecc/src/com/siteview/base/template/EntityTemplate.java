package com.siteview.base.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javolution.util.FastList;

import com.siteview.jsvapi.Jsvapi;

public class EntityTemplate implements Serializable
{
	private static final long serialVersionUID = -4798504644169724556L;
	private Map<String, Map<String, String>>	m_fmap;
	private List<Map<String, String>>			m_items= new FastList<Map<String, String>>	();
	
	public EntityTemplate(Map<String, Map<String, String>> fmap)
	{
		m_fmap= fmap;
		
		m_items.clear();
		for(int i=1; i<100; ++i)
		{
			String index= "EntityItem_"+i;
			Map<String, String> data= m_fmap.get(index);
			if(data==null || data.isEmpty())
				return;
			
			m_items.add(data);
		}
	}
	
	/**
	 * 获取下属监测器模板的描述，通常用于添加监测器时右键菜单的显示
	 */	
	public Map<String, String> getSubMonitorTemplateLabel()
	{
		List<String> mid= getSubMonitorTemplateId();
		Map<String, String> a= new LinkedHashMap<String, String>();
		for (String id : mid)
		{
			MonitorTemplate tpl = TemplateManager.getMonitorTemplate(id);
			if(tpl==null)
				continue;
			String label = tpl.get_sv_label();
			if (label != null && !label.isEmpty())
				a.put(id,label);
		}
		return a;
	}
	
	/**
	 * 获取下属的 监测器模板id
	 */	
	public List<String> getSubMonitorTemplateId()
	{
		Map<String, String> n= m_fmap.get("submonitor"); 
		if(n==null || n.isEmpty())
			return null;
		
		List<String> a= new ArrayList<String>();
		for(String id:n.keySet())
			a.add(id);

		Collections.sort(a, new Comparator<String>()
		{
			public int compare(String s1, String s2)
			{
				Integer o1= new Integer(s1);
				Integer o2= new Integer(s2);
				return o1 - o2;
			}
		});
		return a;
	}
	
    public String get_sv_id()
    {
    	Map<String, String> n= m_fmap.get("property"); 
		if(n==null || n.isEmpty())
			return null;
		return n.get("sv_id");
    }
    
	public String get_sv_name()
	{
		Map<String, String> n= m_fmap.get("property"); 
		if(n==null || n.isEmpty())
			return null;
		
		return n.get("sv_name");
	}
	
	public String get_sv_label()
	{
		Map<String, String> n= m_fmap.get("property"); 
		if(n==null || n.isEmpty())
			return null;
		
		return n.get("sv_label");
	}
	
	public String get_sv_description()
	{
		Map<String, String> n= m_fmap.get("property"); 
		if(n==null || n.isEmpty())
			return null;
		
		return n.get("sv_description");
	}
	
	public boolean get_sv_hidden()
	{
		Map<String, String> n= m_fmap.get("property"); 
		if(n==null || n.isEmpty())
			return false;
		
		String s= n.get("sv_hidden");
		if(s==null || s.compareToIgnoreCase("true")!=0)
			return false;
		return true;
	}
	
	public boolean get_sv_network()
	{
		Map<String, String> n= m_fmap.get("property"); 
		if(n==null || n.isEmpty())
			return false;
		
		String s= n.get("sv_network");
		if(s==null || s.compareToIgnoreCase("true")!=0)
			return false;
		return true;
	}
	
	public String get_sv_dll()
	{
		Map<String, String> n= m_fmap.get("property"); 
		if(n==null || n.isEmpty())
			return null;
		
		return n.get("sv_dll");
	}
	
	public String get_sv_func()
	{
		Map<String, String> n= m_fmap.get("property"); 
		if(n==null || n.isEmpty())
			return null;
		
		return n.get("sv_func");
	}
	
	public String get_sv_quickadd()
	{
		Map<String, String> n= m_fmap.get("property"); 
		if(n==null || n.isEmpty())
			return null;
		
		return n.get("sv_quickadd");
	}
	
	public String get_sv_quickaddsel()
	{
		Map<String, String> n= m_fmap.get("property"); 
		if(n==null || n.isEmpty())
			return null;
		
		return n.get("sv_quickaddsel");
	}
	
	public List<Map<String, String>> getItems()
	{
		List<Map<String, String>> a= new FastList<Map<String, String>>(m_items);
		return a;
	}
	
	public void display()
	{
		if(m_fmap==null)
			return;
		Jsvapi.getInstance().DisplayUtilMapInMap(m_fmap);
	}
	
//	
//	  -- Display UtilMapInMap begin (6 node) -- 
//	     ---- EntityItem_3 (9 key) ----
//	        sv_label= "登录密码"
//	        sv_run= "true"
//	        sv_tip= "请检查输入的密码是否正确"
//	        sv_name= "_PassWord"
//	        sv_type= "password"
//	        sv_isreadonly= "false"
//	        sv_allownull= "true"
//	        sv_style= "cell_40"
//	        sv_helptext= "请输入远程Windows服务器的登录密码"
//	     ---- EntityItem_2 (10 key) ----
//	        sv_label= "登录名"
//	        sv_run= "true"
//	        sv_tip= "请检查输入的登录名是否正确"
//	        sv_name= "_UserAccount"
//	        sv_type= "textbox"
//	        sv_isreadonly= "false"
//	        sv_allownull= "true"
//	        sv_style= "cell_40"
//	        sv_value= "administrator"
//	        sv_helptext= "请输入远程Windows服务器的登录名（如果管理员是域管理员输入格式如: DOMAIN\Administrator）"
//	     ---- submonitor (14 key) ----
//	        112= ""
//	        35= ""
//	        13= ""
//	        14= ""
//	        11= ""
//	        321= ""
//	        12= ""
//	        352= ""
//	        10= ""
//	        434= ""
//	        6= ""
//	        41= ""
//	        5= ""
//	        8= ""
//	     ---- EntityItem_1 (8 key) ----
//	        sv_label= "对应服务器名称"
//	        sv_run= "true"
//	        sv_tip= "请检查输入的服务器名或IP地址是否正确"
//	        sv_name= "_MachineName"
//	        sv_type= "textbox"
//	        sv_allownull= "false"
//	        sv_style= "cell_40"
//	        sv_helptext= "对应服务器主机名或IP地址"
//	     ---- return (2 key) ----
//	        id= "_win"
//	        return= "true"
//	     ---- property (8 key) ----
//	        sv_description= "Windows服务器"
//	        sv_name= "Windows"
//	        sv_id= "_win"
//	        sv_func= "GetMemoryInfo"
//	        sv_hidden= "false"
//	        sv_quickaddsel= "5,10,11,12,"
//	        sv_quickadd= "5,10,11,12,14,41,"
//	        sv_dll= "ntperftest.dll"
//	   -- Display UtilMapInMap end (6 node) -- 


}
