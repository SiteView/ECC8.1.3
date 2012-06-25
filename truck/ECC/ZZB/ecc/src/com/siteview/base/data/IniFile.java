package com.siteview.base.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.jsvapi.Jsvapi;

/**
 * public IniFile(String fileName) </br>
 * public IniFile(String fileName, String section) </br>
 * public IniFile(String fileName, String section, FastMap<String, String> data)
 * 
 */
public class IniFile implements Serializable
{
	private String										m_sections	= "default";
	private String										m_section_todel;
	private String										m_fileName;
	public Map<String, Map<String, String>>	m_fmap		= new FastMap<String, Map<String, String>>();
	
	public IniFile(String fileName)
	{
		m_fileName = fileName;
	}
	
	public IniFile(String fileName, String section)
	{
		m_fileName = fileName;
		m_sections = section;
	}
	
	public IniFile(String fileName, String section, Map<String, String> data)
	{
		m_fileName = fileName;
		m_sections = section;
		m_fmap.put(section, data);
	}

	public void setFmap(Map<String, Map<String, String>> fmap)
	{
		m_fmap = fmap; 
	}
	
	public Map<String, Map<String, String>> getFmap()
	{
		return m_fmap; 
	}
	
	public void load() throws Exception
	{
		Map<String, String> ndata = new FastMap<String, String>();
		ndata.put("dowhat", "GetSvIniFileBySections");
		ndata.put("filename", m_fileName);
		ndata.put("user", "default");
		ndata.put("sections", m_sections);
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool()){
			throw new Exception("Failed to load :" + ret.getEstr());
		}
		
		m_fmap = ret.getFmap();
		if (m_fmap.containsKey("return"))
			m_fmap.remove("return");
	}

	/**
	 * 新建一个section，需要最后调用 saveChange()
	 */
	public void createSection(String section) throws Exception
	{
		if(section==null || section.isEmpty())
			return;
		if (m_fmap.containsKey(section))
			throw new Exception(" There is already a section naming of:" + section +" in ini: " + m_fileName);

		m_fmap.put(section, new FastMap<String,String>() );
	}
	
	/**
	 * 删除一个section，需要最后调用 saveChange()
	 */
	public void deleteSection(String section) throws Exception
	{
		if (section == null || section.isEmpty())
			return;
		if (m_fmap.containsKey(section))
			m_fmap.remove(section);
		
		if (m_section_todel == null)
			m_section_todel = section;
		else
			m_section_todel += ","+section;
	}
	
	/**
	 * 删除一个 key ，需要最后调用 saveChange()
	 */
	public void deleteKey(String section, String key) throws Exception
	{
		if(section==null || section.isEmpty())
			throw new Exception(" section==null || section.isEmpty() ");
		if(key==null || key.isEmpty())
			throw new Exception(" key==null || key.isEmpty() ");
			
		if (!m_fmap.containsKey(section) || m_fmap.get(section)==null)
			throw new Exception(" There is no section:" + section +" of ini: " + m_fileName);
		Map<String, String> sec= m_fmap.get(section);
		if(sec!=null && sec.containsKey(key))
			sec.remove(key);
	}
	
	/**
	 * 修改(或新建)一个 key value，需要最后调用 saveChange()
	 */
	public void setKeyValue(String section, String key, String value) throws Exception
	{
		if(section==null || section.isEmpty())
			throw new Exception(" section==null || section.isEmpty() ");
		if(key==null || key.isEmpty())
			throw new Exception(" key==null || key.isEmpty() ");
		if(value==null)
			throw new Exception(" value==null ");
		
		if (!m_fmap.containsKey(section) || m_fmap.get(section)==null)
			throw new Exception(" There is no section:" + section +" in ini: " + m_fileName);
		Map<String, String> sec= m_fmap.get(section);
		sec.put(key,value);
	}
	
	/**
	 * 修改(或新建)一个 key value，需要最后调用 saveChange()
	 */
	public void setKeyValue(String section, String key, int value) throws Exception
	{
		if(section==null || section.isEmpty())
			throw new Exception(" section==null || section.isEmpty() ");
		if(key==null || key.isEmpty())
			throw new Exception(" key==null || key.isEmpty() ");
		
		Map<String, String> strsec= m_fmap.get(section);
		if(strsec!=null)
			strsec.remove(key);
		else
			throw new Exception(" There is no section:" + section +" in ini: " + m_fileName);
		
		Map<String, String> sec= m_fmap.get("(INT_VALUE)"+section);
		if (sec == null)
		{
			sec= new FastMap<String, String>();
			sec.put(key,new Integer(value).toString());
			m_fmap.put("(INT_VALUE)" + section, sec);
		}
		else
		{
			sec.put(key,new Integer(value).toString());
		}	
	}
	
	/**
	 * 把修改保存到服务器上
	 * <br/> 保存后请重新获取（类实例中的数据会被置 null）
	 */
	public boolean saveChange() throws Exception
	{
		if(m_section_todel!=null && !m_section_todel.isEmpty())
		{
			Map<String, String> ndata = new FastMap<String, String>();
			ndata.put("dowhat", "DeleteIniFileSection");
			ndata.put("filename", m_fileName);
			ndata.put("sections", m_section_todel);
			RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
			if (!ret.getRetbool())
				throw new Exception("Failed to DeleteIniFileSection :" + ret.getEstr());
			m_section_todel= null;
		}

		for(String section:m_fmap.keySet())
		{
			if(section.startsWith("(INT_VALUE)"))
				continue;
			
			Map<String,Map<String,String>> fdata= new FastMap<String,Map<String,String>>();
			fdata.put(section, m_fmap.get(section));
			if(m_fmap.containsKey("(INT_VALUE)"+ section))
				fdata.put("(INT_VALUE)"+ section, m_fmap.get("(INT_VALUE)"+ section));
				
			Map<String, String> ndata = new FastMap<String, String>();
			ndata.put("dowhat", "WriteIniFileSection");
			ndata.put("filename", m_fileName);
			ndata.put("section", section);
			RetMapInMap ret = ManageSvapi.SubmitUnivData(fdata, ndata);
			if (!ret.getRetbool())
				throw new Exception("Failed to WriteIniFileSection :" + ret.getEstr());
		}
		m_fmap=null;
		return true;
	}
	
	public String getValue(String section, String key)
	{
		if( !m_fmap.containsKey(section) )
			return null;

		Map<String, String> ndata = m_fmap.get("(INT_VALUE)" + section);
		if (ndata != null)
		{
			if(ndata.containsKey(key))
				return ndata.get(key);
		}
		
		ndata= m_fmap.get(section);
		if(ndata==null)
			return null;
		if(!ndata.containsKey(key))
			return null;
		return ndata.get(key);
	}
	
	public Map<String, String> getSectionData(String section)
	{
		if( !m_fmap.containsKey(section) )
			return null;
		return m_fmap.get(section);
	}
	
	public List<String> getSectionList()
	{
		List<String> ret= new FastList<String>();
		for(String key:m_fmap.keySet())
		{
			if(key.equals("return"))
				continue;
			ret.add(key);
		}
		return ret;
	}
	
	public List<String> getKeyList(String section)
	{
		List<String> ret= new FastList<String>();
		Map<String, String> ndata= m_fmap.get(section);
		if(ndata==null)
			return null;
		for(String key:ndata.keySet())
			ret.add(key);
		
		ndata = m_fmap.get("(INT_VALUE)" + section);
		if (ndata != null)
		{
			for (String key : ndata.keySet())
				ret.add(key);
		}
		return ret;
	}
	
	public String getFileName()
	{
		return m_fileName;
	}
	
	public String getSections()
	{
		return m_sections;
	}
	
	public void display()
	{
		Jsvapi.getInstance().DisplayUtilMapInMap(m_fmap);
	}
	
	public static void main(String[] args)
	{
//		IniFile ini= new IniFile("general.ini","IPCheck");
		IniFile ini= new IniFile("test.ini.ini");
		ArrayList<String> l;
		try
		{
			ini.load();
//			logger.info(" IniFile value is: " + ini.getValue("IPCheck", "name"));
//			
//			l= ini.getSectionList();
//			logger.info(" IniFile sections: " + l);
//			
//			l= ini.getKeyList("IPCheck");
//			logger.info(" IniFile keys: " + l);
			
			ini.display();
			ini.createSection("sec4");
//			ini.setKeyValue("sec3", "key2", "123");
//			ini.setKeyValue("sec3", "keys", "ss");
			ini.setKeyValue("sec4", "key4", 123);
			ini.setKeyValue("sec4", "key", 123);
			ini.saveChange();
			
			ini.load();
			ini.display();
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	
	}
	

//		以下这些值是 int 型 	
//	   -- Display UtilMapInMap begin (1 node) -- 
//	     ---- 19379 (34 key) ----
//			nAdmin= "-1";	
//	        m_tuop= "1"
//	        m_mailsetting= "1"
//	        m_logshower= "1"
//	        m_reportlistEdit= "-1"
//	        m_AlertRuleEdit= "-1"
//	        m_reportlistAdd= "1"
//	        m_AlertRuleDel= "-1"
//	        m_allview= "1"
//	        m_reportlistDel= "-1"
//	        m_AlertRuleAdd= "1"
//	        nIsUse= "1"
//	        m_SetshowSystemReport= "1"
//	        m_smssetting= "1"
//	        m_topnadd= "1"
//	        m_tree= "1"
//	        m_alertLogs= "1"
//	        m_general= "1"
//	   -- Display UtilMapInMap end (1 node) -- 
//

 
}

