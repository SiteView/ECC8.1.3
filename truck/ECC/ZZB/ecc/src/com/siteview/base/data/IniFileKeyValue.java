package com.siteview.base.data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInMap;

public class IniFileKeyValue implements Serializable
{
	private String										m_key;
	private String										m_section;
	private String										m_fileName;
	private Map<String, Map<String, String>>	m_fmap;
	
	public IniFileKeyValue(String fileName, String section, String key)
	{
		m_fileName = fileName;
		m_section = section;
		m_key= key;
	}
	
	public String load() throws Exception
	{
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetIniFileString");
		ndata.put("filename", m_fileName);
		ndata.put("section", m_section);
		ndata.put("key", m_key);
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception("Failed to load :" + ret.getEstr());
		
		m_fmap = ret.getFmap();
		
		if( !m_fmap.containsKey("return") )
			return null;
		Map<String, String> data= m_fmap.get("return");
		if(data==null)
			return null;
		if(!data.containsKey("value"))
			return null;
		return data.get("value");
	}
	
	/**
	 * 修改 value ，并保存到服务器上
	 */
	public boolean setValue(String newValue) throws Exception
	{
		HashMap<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "WriteIniFileString");
		ndata.put("filename", m_fileName);
		ndata.put("section", m_section);
		ndata.put("key", m_key);
		ndata.put("value", newValue);
		ndata.put("user", "default");
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception("Failed to setValue :" + ret.getEstr() +" ini file:"+ m_fileName+" section:"+ m_section+" key:"+ m_key);
		
		return true;
	}
	
	public String getFileName()
	{
		return m_fileName;
	}
	
	public String getSection()
	{
		return m_section;
	}
	
	public String getKey()
	{
		return m_key;
	}
}
