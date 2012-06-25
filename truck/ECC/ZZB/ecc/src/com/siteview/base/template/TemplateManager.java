package com.siteview.base.template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.log4j.Logger;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.svdb.UnivData;

public class TemplateManager
{
	private final static Logger logger = Logger.getLogger(TemplateManager.class);
	private final static Map<String, MonitorTemplate>	m_monitor_template	= new ConcurrentHashMap<String, MonitorTemplate>();
	private final static Map<String, EntityTemplate>	m_entity_template	= new ConcurrentHashMap<String, EntityTemplate>();
	private final static Map<String, EntityGroup>		m_entity_group		= new ConcurrentHashMap<String, EntityGroup>();
	private final static List<String>				m_egroup_id			= new CopyOnWriteArrayList<String>();

	private final static UnivData univData = new UnivData();

	static 
	{
		try {
			logger.info("Test : read from resource : IDS_Monitor_Can_not_Disable : " + UnivData.getResource("IDS_Monitor_Can_not_Disable"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		teleLoadAll();
	}
	
	public TemplateManager(){
	}
	

	public static MonitorTemplate getMonitorTemplate(String id)
	{
		if(id==null)
			return null;
		if (m_monitor_template.get(id) == null)
			teleLoadAll();
		return m_monitor_template.get(id);
	}
	
	public static EntityTemplate getEntityTemplate(String id)
	{
		if(id==null)
			return null;
		if (m_entity_template.get(id) == null)
			teleLoadAll();
		return m_entity_template.get(id);
	}
	
	public static EntityGroup getEntityGroup(String id)
	{
		if(id==null)
			return null;
		if (m_entity_group.get(id) == null)
			teleLoadAll();
		return m_entity_group.get(id);
	}
	
	/**
	 * 获取获取设备组和设备模板的描述，通常用于添加监测器时右键菜单的显示
	 */	
	public static Map<String, Map<String, String>> getEntityGroupTemplateLabel()
	{
		Map<String, Map<String, String>> ret= new LinkedHashMap<String, Map<String, String>>();
		
		List<String> gid= getAllEntityGroupId();
		for (String id : gid)
		{
			EntityGroup eg= getEntityGroup(id);
			if( eg.get_sv_hidden() )
				continue;
			String label= eg.get_sv_label();
			
			Map<String, String> a= new LinkedHashMap<String, String>();
			List<String> eid= eg.getSubEntityTemplateId();
			for (String key : eid)
			{
				EntityTemplate et= getEntityTemplate(key);
				if(et==null)
					continue;
				if( et.get_sv_hidden() )
					continue;
				String label2= et.get_sv_name();
				a.put(key, label2);
			}
			ret.put(label, a);
		}
		return ret;
	}
	
	/**
	 * 获取设备组id
	 */	
	public static List<String> getAllEntityGroupId()
	{
		if(m_egroup_id == null)
			return null;
		if (!(m_egroup_id.size() >0) )
			teleLoadAll();
		return new ArrayList<String>(m_egroup_id);
	} 
	
	private static void sortEntityGroup(String id, String index, Map<Integer,String> data)
	{
		int realindex= 0;
		if(index!=null && !index.isEmpty())
			realindex= new Integer(index)*100;
		while(data.containsKey(new Integer(realindex)))
			++realindex;
		
		data.put(new Integer(realindex), id);
	}
	
	private static void teleLoadAll()
	{
		try
		{
			m_monitor_template.clear();
			m_entity_template.clear();
			m_entity_group.clear();
			m_egroup_id.clear();
			loadAllMonitorTemplate();
			loadAllEntityGroup();
			Date d2= new Date();
			logger.info("base.template.TemplateManager preloading all template data is done! ("+d2.toLocaleString()+")\n");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static void loadAllEntityGroup()
	{
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetAllEntityGroups");
		RetMapInMap mtpl= ManageSvapi.GetUnivData(ndata);
		if( !mtpl.getRetbool() )
			return ;
		Map<String, Map<String, String>> fmap= mtpl.getFmap();
		if(fmap==null || fmap.isEmpty())
			return;	
		
//		Jsvapi.DisplayUtilMapInMap(fmap);
		for (String id : fmap.keySet())
		{
			Map<String, String> group = fmap.get(id);
			if(group==null || group.isEmpty())
				continue;
			String sv_id= group.get("sv_id");
			if(sv_id==null || sv_id.isEmpty())
				continue;
			
			EntityGroup e= new EntityGroup(group);
			m_entity_group.put(id, e);
			
			for(String eid:group.keySet())
			{
				String sub_entity= group.get(eid);
				if (sub_entity.compareTo("sub_entity") == 0)
					loadEntityTemplate(eid);
			}
		}
		
		Map<Integer,String> data= new HashMap<Integer,String>(); 
		for (String id : m_entity_group.keySet())
		{
			EntityGroup t = m_entity_group.get(id);
			String index= t.get_sv_index();
			sortEntityGroup(id, index, data);
		}
		
		ArrayList<Integer> list = new ArrayList<Integer>(data.keySet());
		Collections.sort(list, new Comparator<Integer>()
		{
			public int compare(Integer o1, Integer o2)
			{
				return o1 - o2;
			}
		});  
		
		m_egroup_id.clear();
		int size = list.size();
		for (int i = 0; i < size; ++i)
		{
			String id =data.get(list.get(i));
			if (id != null)
			{
				m_egroup_id.add(id);
//				logger.info("   id: "+ " " + id + " index: "+list.get(i));	
			}
		}
		logger.info("                              preloaded "+m_entity_group.size()+"  entity group tpl !");
		logger.info("                              preloaded "+m_entity_template.size()+"  entity template  !");
	}
	
	private static void loadEntityTemplate(String id)
	{
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetEntityTemplet");
		ndata.put("id", id);
		RetMapInMap mtpl= ManageSvapi.GetUnivData(ndata);
		if( !mtpl.getRetbool() )
			return ;	
		
		Map<String, Map<String, String>> fmap= mtpl.getFmap();
//		if(id.compareTo("_win")==0)
//			Jsvapi.DisplayUtilMapInMap(fmap);
		if(fmap==null || fmap.isEmpty())
			return;
		EntityTemplate m= new EntityTemplate(fmap);
		m_entity_template.put(id, m);	
	}
	
	private static void loadAllMonitorTemplate()
	{
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetAllMonitorTempletInfo");
		RetMapInMap mtpl= ManageSvapi.GetUnivData(ndata);
		if( !mtpl.getRetbool() )
			return ;
		Map<String, Map<String, String>> fmap= mtpl.getFmap();
		if(fmap==null || fmap.isEmpty())
			return;
		
		Date d= new Date();
		logger.info("base.template.TemplateManager preloading all template data("+d.toLocaleString()+")...");
		
		Map<String, String> names = fmap.get("monitors");
		if(names==null || names.isEmpty())
			return;
		for( String id:names.keySet())
			loadMonitorTemplate(id);
		
		logger.info("                              preloaded "+m_monitor_template.size()+" monitor template !");
	}
	
	private static void loadMonitorTemplate(String id)
	{
		Integer i= new Integer(id);
		if(i<1 || i>10000)
			return;
		
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetMonitorTemplet");
		ndata.put("id", id);
		RetMapInMap mtpl= ManageSvapi.GetUnivData(ndata);
		if( !mtpl.getRetbool() )
			return ;	
		
		Map<String, Map<String, String>> fmap= mtpl.getFmap();
//		if(id.compareTo("5")==0)
//			Jsvapi.DisplayUtilMapInMap(fmap);
		if(fmap==null || fmap.isEmpty())
			return;
		MonitorTemplate m= new MonitorTemplate(fmap);
		m_monitor_template.put(id, m);		
	}

}
