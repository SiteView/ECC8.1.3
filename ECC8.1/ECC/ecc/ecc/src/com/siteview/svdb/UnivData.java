package com.siteview.svdb;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javolution.util.FastList;
import javolution.util.FastMap;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.jsvapi.Jsvapi;

public class UnivData extends BaseSvdb 
{
	public static boolean setSvdbAddrByFile(String filename) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "SetSvdbAddrByFile");
		if(filename!=null)
			ndata.put("filename", filename);
		
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return ret;
	}
	public static String getSvdbAddr() throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetSvdbAddr");
		
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap.get("return").get("return");
	}	
	public static boolean delChildren(String parentid,boolean autoDelTable) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "DelChildren");
		if(parentid!=null)
			ndata.put("parentid", parentid);
		ndata.put("autoDelTable", String.valueOf(autoDelTable));
		
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return ret;
	}
	public static boolean deleteSVSE(String id) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "DeleteSVSE");
		if(id!=null)
			ndata.put("id", id);
		
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return ret;
	}	
	public static boolean deleteRecords(String id,String year,String month,String day,String hour,String minute,String second) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "DeleteRecords");
		if(id!=null)
			ndata.put("id", id);
		if(year!=null)
			ndata.put("year", year);
		if(month!=null)
			ndata.put("month", month);
		if(day!=null)
			ndata.put("day", day);
		if(hour!=null)
			ndata.put("hour", hour);
		if(minute!=null)
			ndata.put("minute", minute);
		if(second!=null)
			ndata.put("second", second);
		
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return ret;
	}	
	/**
	 * 
	 * @param type 
	 * svse/group/entity/monitor
	 */
	public static Map<String, Map<String, String>>  getCommonData(String type,String id) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetCommonData");
		if(type!=null)
			ndata.put("type", type);
		if(id!=null)
			ndata.put("id", id);

		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}
	public static Map<String, Map<String, String>>  getSVSE(String id) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetSVSE");
		if(id!=null)
			ndata.put("id", id);

		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}	
	/**
	 * @param id
	 * @param sv_depends
	 * �� sv_depends== true ʱ,������ sv_dependson_svname = ??:??:??:??
	 * @return
	 * ���������� fmap �������½��� map : return, property, subgroup, subentiey(id������key�У���valueΪ��)
	 * @throws Exception
	 */
	public static Map<String, Map<String, String>>  getGroup(String id,String sv_depends) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetGroup");
		if(id!=null)
			ndata.put("id", id);
		if(sv_depends!=null)
			ndata.put("sv_depends", sv_depends);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}
	/**
	 * @param id
	 * @param sv_depends
	 * �� sv_depends== true ʱ,������ sv_dependson_svname = ??:??:??:??
	 * @return
	 * ������ fmap �������½��� map : return, property, submonitor
	 * @throws Exception
	 */
	public static Map<String, Map<String, String>>  getEntity(String id,String sv_depends) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetEntity");
		if(id!=null)
			ndata.put("id", id);
		if(sv_depends!=null)
			ndata.put("sv_depends", sv_depends);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}
	/**
	 * 
	 * @param id
	 * @return
	 * ������ fmap �������½��� map : return, property, parameter, advance_parameter, error, warning, good
	 * @throws Exception
	 */
	public static Map<String, Map<String, String>>  getMonitor(String id) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetMonitor");
		if(id!=null)
			ndata.put("id", id);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}	
	/**
	 * 
	 * @param id
	 * @param needkeys
	 * ������ʹ��XXX,XXX�ĸ�ʽ
	 * @param language
	 * (ͨ��Ϊ default,���������˵���������,���߲���)
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Map<String, String>>  loadResourceByKeys(String id,String needkeys,String language) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "LoadResourceByKeys");
		putMapIgnorNull(ndata,"needkeys", needkeys);
		putMapIgnorNull(ndata,"language", language);
		
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}	
	/**
	 * 
	 * @param id
	 * @param language
	 * (ͨ��Ϊ default,���������˵���������,���߲���)
	 * @return
	 * @throws Exception
	 */
	private final static Map<String, Map<String,String>>	resouceMap		= new ConcurrentHashMap<String, Map<String,String>>();

	public static String  getResource(String id) throws Exception{
		return getResource(id,"default");
	}

	public static String  getResource(String id,String language) throws Exception
	{
		Map<String, String> map = resouceMap.get(language);
		if (map!=null){
			return map.get(id);
		}
		
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "LoadResource");
		putMapIgnorNull(ndata,"language", language);
		
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		
		Map<String, String> pdata= fmap.get("property");
		if (pdata==null || pdata.isEmpty())  return null;
		resouceMap.put(language, pdata);
		return pdata.get(id);
	}	
	public static Map<String, Map<String, String>>  getAllMonitorTempletInfo() throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetAllMonitorTempletInfo");
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}
	/**
	 * 
	 * @return
	 * ��ȡ���е�	EntityTemplet��	�� property ����sub_monitor ����
	 * @throws Exception
	 */
	public static Map<String, Map<String, String>>  getAllEntityTempletInfo() throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetAllEntityTempletInfo");
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}
	/**
	 * 
	 * @return
	 * ��ȡĳ��	MonitorTemplet������ϸ����
	 * @throws Exception
	 */
	public static Map<String, Map<String, String>>  getMonitorTemplet(String id) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetMonitorTemplet");
		putMapIgnorNull(ndata,"id", id);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}
	/**
	 * 
	 * @param id
	 * @return
	 * ��ȡĳ����	EntityTemplet��	����ϸ����
	 * @throws Exception
	 */
	public static Map<String, Map<String, String>>  getEntityTemplet(String id) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetEntityTemplet");
		putMapIgnorNull(ndata,"id", id);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}
	/**
	 * 
	 * @param id
	 * @return
	 * ��ȡĳ����	EntityGroup����	����ϸ����
	 * @throws Exception
	 */
	public  static Map<String, Map<String, String>>  getEntityGroup(String id) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetEntityGroup");
		putMapIgnorNull(ndata,"id", id);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}
	/**
	 * 
	 * @return
	 * ��ȡ���еġ�	EntityGroup��	�ġ�property������
	 * @throws Exception
	 */
	public static Map<String, Map<String, String>>  getAllEntityGroups() throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetAllEntityGroups");
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}
	/**
	 * 
	 * @param filename
	 * @param user
	 * (user ͨ��Ϊ default ,��Ϊ idc �û�ʱ�봫�� useid)
	 * @param sections
	 * sections= XXX (ͨ��Ϊ default,��ȫ����
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Map<String, String>>  getSvIniFileBySections(String filename,String user,String sections) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetSvIniFileBySections");
		putMapIgnorNull(ndata,"filename", filename);
		ndata.put("sections", (sections==null)?"default":sections);
		ndata.put("user", (user==null)?"default":user);
		
			ndata.put("user", user);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}
	/**
	 * 
	 * @param filename
	 * @param user
	 * @param sections
	 * sections=XXX,XXX,XXX... 
	 * @return
	 * @throws Exception
	 */
	public static boolean  deleteIniFileSection(String filename,String user,String sections) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "DeleteIniFileSection");
		putMapIgnorNull(ndata,"filename", filename);
		putMapIgnorNull(ndata,"sections", sections);
		ndata.put("user", (user==null)?"default":user);
		
			ndata.put("user", user);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return ret;
	}
	/**
	 *filename= XXX,  user= XXX,  key= XXX,   value= XXX,   value_type= string/int,	sections= XXX,XXX,XXX... 
	 */
	public static boolean  writeIniFileData(String filename,String user,String key,String value,String value_type,String sections) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "WriteIniFileData");
		putMapIgnorNull(ndata,"filename", filename);
		putMapIgnorNull(ndata,"sections", sections);
		putMapIgnorNull(ndata,"key", key);
		putMapIgnorNull(ndata,"value", value);
		putMapIgnorNull(ndata,"value_type", value_type);
		
		ndata.put("user", (user==null)?"default":user);
		
			ndata.put("user", user);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return ret;
	}
	/**
	 *filename= XXX,  user= XXX,  key= XXX,   sections= XXX,XXX,XXX... 
	 */
	public static boolean  deleteIniFileData(String filename,String user,String key,String sections) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "DeleteIniFileData");
		putMapIgnorNull(ndata,"filename", filename);
		putMapIgnorNull(ndata,"sections", sections);
		putMapIgnorNull(ndata,"key", key);
		ndata.put("user", (user==null)?"default":user);
		
			ndata.put("user", user);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return ret;
	}
	public static boolean  deleteIniFileKeys(String filename,String user,String section,String keys) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "DeleteIniFileKeys");
		putMapIgnorNull(ndata,"filename", filename);
		putMapIgnorNull(ndata,"section", section);
		putMapIgnorNull(ndata,"keys", keys);
		ndata.put("user", (user==null)?"default":user);
		
			ndata.put("user", user);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return ret;
	}	
	public static boolean  writeIniFileString(String filename,String user,String section,String key,String value) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "WriteIniFileString");
		putMapIgnorNull(ndata,"filename", filename);
		putMapIgnorNull(ndata,"section", section);
		putMapIgnorNull(ndata,"key", key);
		putMapIgnorNull(ndata,"value", value);
		ndata.put("user", (user==null)?"default":user);
		
			ndata.put("user", user);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return ret;
	}
	public static boolean  writeIniFileInt(String filename,String user,String section,String key,String value) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "WriteIniFileInt");
		putMapIgnorNull(ndata,"filename", filename);
		putMapIgnorNull(ndata,"section", section);
		putMapIgnorNull(ndata,"key", key);
		putMapIgnorNull(ndata,"value", value);
		ndata.put("user", (user==null)?"default":user);
		
			ndata.put("user", user);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return ret;
	}	
	public static boolean  deleteIniFileKey(String filename,String user,String section,String key) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "DeleteIniFileKey");
		putMapIgnorNull(ndata,"filename", filename);
		putMapIgnorNull(ndata,"section", section);
		putMapIgnorNull(ndata,"key", key);
		
		ndata.put("user", (user==null)?"default":user);
		
			ndata.put("user", user);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return ret;
	}
	public static boolean  editIniFileSection(String filename,String user,String section,String new_section) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "EditIniFileSection");
		putMapIgnorNull(ndata,"filename", filename);
		putMapIgnorNull(ndata,"section", section);
		putMapIgnorNull(ndata,"new_section", new_section);
		
		
		ndata.put("user", (user==null)?"default":user);
		
			ndata.put("user", user);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return ret;
	}	
	public static boolean  editIniFileKey(String filename,String user,String section,String key,String new_key) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "EditIniFileKey");
		putMapIgnorNull(ndata,"filename", filename);
		putMapIgnorNull(ndata,"section", section);
		putMapIgnorNull(ndata,"key", key);
		putMapIgnorNull(ndata,"new_key", new_key);
		
		ndata.put("user", (user==null)?"default":user);
		
			ndata.put("user", user);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return ret;
	}	
	public static Map<String, Map<String, String>>   getIniFileSections(String filename,String user) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetIniFileSections");
		putMapIgnorNull(ndata,"filename", filename);
		
		ndata.put("user", (user==null)?"default":user);
		
			ndata.put("user", user);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}	
	public static Map<String, Map<String, String>>   getIniFileKeys(String filename,String user,String section) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetIniFileKeys");
		putMapIgnorNull(ndata,"filename", filename);
		putMapIgnorNull(ndata,"section", section);
		ndata.put("user", (user==null)?"default":user);
		
			ndata.put("user", user);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}	
	public static Map<String, Map<String, String>>   getIniFileString(String filename,String user,String section,String key) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetIniFileString");
		putMapIgnorNull(ndata,"filename", filename);
		putMapIgnorNull(ndata,"section", section);
		putMapIgnorNull(ndata,"key", key);
		ndata.put("user", (user==null)?"default":user);
		
			ndata.put("user", user);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}	
	public static boolean  createQueue(String queuename) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "CreateQueue");
		putMapIgnorNull(ndata,"queuename", queuename);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return ret;
	}	
	public static boolean  pushStringMessage(String queuename,String label,String content) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "PushStringMessage");
		putMapIgnorNull(ndata,"queuename", queuename);
		putMapIgnorNull(ndata,"label", label);
		putMapIgnorNull(ndata,"content", content);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return ret;
	}
	public static Map<String, Map<String, String>>   getMQRecordCount(String queuename) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetMQRecordCount");
		putMapIgnorNull(ndata,"queuename", queuename);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}
	public static Map<String, Map<String, String>>  getAllQueueNames() throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetAllQueueNames");
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}
	public static boolean  deleteQueue(String queuename) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "DeleteQueue");
		putMapIgnorNull(ndata,"queuename", queuename);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return ret;
	}
	public static boolean  clearQueueMessage(String queuename) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "ClearQueueMessage");
		putMapIgnorNull(ndata,"queuename", queuename);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return ret;
	}
	public static Map<String, Map<String, String>>  GetTask(String id) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetTask");
		putMapIgnorNull(ndata,"id", id);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}
	public static Map<String, Map<String, String>>  getAllTaskName() throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "GetAllTaskName");
		
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}
	public static boolean  deleteTask(String id) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "DeleteTask");
		putMapIgnorNull(ndata,"id", id);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return ret;
	}
	public static Map<String, Map<String, String>>   getAllTask(String id) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "getAllTask");
		putMapIgnorNull(ndata,"id", id);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}
	/**
	 * 
			//����	(�ɷ��¼)Available: true/false		(�Ƿ����ð�)isTrial: true/false	
			//	(�Ƿ����Ա)isAdmin: true/false		(�û���)UserName=XXX
			//	(��ɼ�����)monitorNum=XXX		(��������豸��)networkNum=XXX
			//	(�Ƿ���ʾ������)showpoint= 1/0	(�Ƿ���ʾ�����豸��)shownw= 1/0
			//  (ģ����Ȩ)subMenu= XXX (���磺 101110000000000 , ���й�ʱ���д�ֵ)
			//  (���û���ini�е�section)section= XXX 		
	 */
	public static Map<String, Map<String, String>>   tryLogin(String LoginName,String PassWord) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "TryLogin");
		putMapIgnorNull(ndata,"LoginName", LoginName);
		putMapIgnorNull(ndata,"PassWord", PassWord);
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}
	public static String encrypt(String x) throws Exception {
		Map<String, String> map = encrypt(new String[] { x });
		return map.get(x);
	}

	public static Map<String, String> encrypt(String[] x) throws Exception {
		/*
		 * dowhat= encrypt , X1= , X2= , X3= , ... //���������󷵻� dowhat= decrypt ,
		 * X1= , X2= , X3= , ... //���������󷵻�
		 */
		Map<String, String> ndata = new FastMap<String, String>();
		ndata.put("dowhat", "encrypt");
		for (String key : x)
			ndata.put(key, "");

		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception("Failed to load :" + ret.getEstr());

		Map<String, Map<String, String>> pwdValue = ret.getFmap();
		return pwdValue.get("return");
	}

	public static String decrypt(String x) throws Exception {
		Map<String, String> map = decrypt(new String[] { x });
		return map.get(x);
	}

	public static Map<String, String> decrypt(String[] x) throws Exception {
		Map<String, String> ndata = new FastMap<String, String>();
		ndata.put("dowhat", "decrypt");
		for (String key : x)
			ndata.put(key, "");

		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception("Failed to load :" + ret.getEstr());

		/*
		 * if (m_fmap.containsKey("return")) m_fmap.remove("return");
		 * Jsvapi.DisplayUtilMapInMap(m_fmap);
		 */
		// logger.info("!!!!!!!!!!����map");
		Map<String, Map<String, String>> pwdValue = ret.getFmap();
		return pwdValue.get("return");
	}
	public static Map<String, Map<String, String>>   monitorCopy(String sourceId,String targetParentId,boolean autoCreateTable) throws Exception
	{
		throw new Exception("No implements now!!");
	}
	public static Map<String, Map<String, String>>   entityCopy(String sourceId,String targetParentId,boolean autoCreateTable) throws Exception
	{
		throw new Exception("No implements now!!");
	}
	public static Map<String, Map<String, String>>   disableForever(String... monitor) throws Exception
	{
		throw new Exception("No implements now!!");
	}
	public static Map<String, Map<String, String>>   disableTemp(String... monitorid) throws Exception
	{
		throw new Exception("No implements now!!");
	}
	public static Map<String, Map<String, String>>   enable(String... monitorid) throws Exception
	{
		throw new Exception("No implements now!!");
	}
	public static Map<String, Map<String, String>>   getDynamicData(String entityId,String monitorTplId) throws Exception
	{
		throw new Exception("No implements now!!");
	}
	public static Map<String, Map<String, String>>   getEntityDynamicData(String parentid,String entityTplId) throws Exception
	{
		throw new Exception("No implements now!!");
	}
	/**������������ˢ�¼����
	 parentid ������ id �Ĺ�ͬ����   // ��ѡ���������أ���ú�����ȴ�,ֱ����Ϣ���� ��֪ȫ��ˢ�½���ʱ����ͳһ�������µļ������
                                                        // ��ѡ���������أ��������һ������������ģ��ԭ cgi ��������ȡ�ü������Ч��
                                                        // �����ص� return ���� queueName= XXX ,������һ������.    
	 */
	public static Map<String, Map<String, String>>   refreshMonitors(String[] x,String parentid,boolean instantReturn) throws Exception
	{
		throw new Exception("No implements now!!");
	}
	/*
	 * ���ص� return ���� isDone= true/false  ���������øú���ֱ�� isDone==true (������ bool ״̬Ϊ false ) ��ÿ�η���ֵ�л��� dstr �ȼ����Ϣ
	 * */
	public static Map<String, Map<String, String>>   getLatestrefresh(String queueName,String parentid,boolean instantReturn) throws Exception
	{
		throw new Exception("No implements now!!");
	}
	/*
	 * ���ص� return ���� isDone= true/false  ���������øú���ֱ�� isDone==true (������ bool ״̬Ϊ false ) ��ÿ�η���ֵ�л��� dstr �ȼ����Ϣ
											         // ���ص�ˢ�������ڡ�refreshData_1, refreshData_2... ��
											         // parentid Խ��������Խ�ͣ��� "1" �ͱ� "1.2.2" ���ܵ�
	 * */
	public static Map<String, Map<String, String>>   getrefreshed(String queueName,String parentid,boolean instantReturn) throws Exception
	{
		throw new Exception("No implements now!!");
	}	
	/*
	 * ���������ڡ�DynamicData
	 */
	public static Map<String, Map<String, String>>  testEntity(String entityId) throws Exception
	{
		throw new Exception("No implements now!!");
	}	
	public static Map<String, Map<String, String>> queryReportData(List<String> nodeids,boolean compress,boolean dstrNeed,String dstrStatusNoNeed,String return_value_filter,Integer byCount,Date begin,Date end)throws Exception{
		StringBuffer sb = new StringBuffer();
		for (String id : nodeids){
			if (sb.length()>0) sb.append(",");
			sb.append(id);
		}
		return UnivData.queryReportData(sb.toString(), compress, dstrNeed, dstrStatusNoNeed, return_value_filter, byCount, begin, end);
	}

	public static List<Map<String, String>>  queryRecordsByTime(String monitorid,Date begin,Date end) throws Exception
	{
		List<Map<String, String>> vmap = new FastList<Map<String, String>>();
		StringBuilder estr = new StringBuilder();
		Map<String, String> ndata = new FastMap<String, String>();
		ndata.put("dowhat", "QueryRecordsByTime");
		ndata.put("id", monitorid);
		if (begin == null)
			throw new Exception(" Date of begin is null ! ");
		if (end == null)
			throw new Exception(" Date of end is null ! ");
		if (begin.compareTo(end) >= 0)
			throw new Exception(" Date of begin >= date of end! ");

		Calendar   cal   =   Calendar.getInstance();   
		cal.setTime(begin);

		ndata.put("begin_year","" + cal.get(Calendar.YEAR));
        ndata.put("begin_month","" + (cal.get(Calendar.MONTH) + 1));
        ndata.put("begin_day","" + cal.get(Calendar.DAY_OF_MONTH));
        ndata.put("begin_hour","" + cal.get(Calendar.HOUR_OF_DAY));
        ndata.put("begin_minute","" + cal.get(Calendar.MINUTE));
        ndata.put("begin_second","" + cal.get(Calendar.SECOND));
		
        
		cal.setTime(end);
        ndata.put("end_year","" + cal.get(Calendar.YEAR));
        ndata.put("end_month","" + (cal.get(Calendar.MONTH) + 1));
        ndata.put("end_day","" + cal.get(Calendar.DAY_OF_MONTH));
        ndata.put("end_hour","" + cal.get(Calendar.HOUR_OF_DAY));
        ndata.put("end_minute","" + cal.get(Calendar.MINUTE));
        ndata.put("end_second","" + cal.get(Calendar.SECOND));
		boolean ret= Jsvapi.getInstance().GetForestData(vmap, ndata, estr); 
		if(!ret)
			throw new Exception(estr.toString());
		return vmap;
	}
	
	//��ϸ�����������£����µ�������׷�� 2007-03-15 12:38:02=20.5747,2007-02-27 14:20:21=(status)bad,2007-02-26 19:10:21=(status)null,2007-02-24 19:16:21=(status)disable,2007-01-15 09:26:01=27.8916,
	//��ϸ���������ǽ�ԭʼ���ݸ���ѹ����ƽ�����������ݣ������ֵ����Сֵ�� dstr ��������ԭʼ����
	public static Map<String, Map<String, String>>  queryReportData(String nodeids,boolean compress,boolean dstrNeed,String dstrStatusNoNeed,String return_value_filter,Integer byCount,Date begin,Date end) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "QueryReportData");
		ndata.put("id", nodeids);
		ndata.put("compress", compress ? "true" : "false");
		if (dstrNeed) ndata.put("dstrNeed", "true");
		if (dstrStatusNoNeed!=null) ndata.put("dstrStatusNoNeed", dstrStatusNoNeed);
		if (return_value_filter!=null) ndata.put("return_value_filter",return_value_filter);

		if (byCount!=null) {
			ndata.put("byCount","" + byCount);
		}else{

			if (begin == null)
				throw new Exception(" Date of begin is null ! ");
			if (end == null)
				throw new Exception(" Date of end is null ! ");
			if (begin.compareTo(end) >= 0)
				throw new Exception(" Date of begin >= date of end! ");
	
			Calendar   cal   =   Calendar.getInstance();   
			cal.setTime(begin);
	
			ndata.put("begin_year","" + cal.get(Calendar.YEAR));
	        ndata.put("begin_month","" + (cal.get(Calendar.MONTH) + 1));
	        ndata.put("begin_day","" + cal.get(Calendar.DAY_OF_MONTH));
	        ndata.put("begin_hour","" + cal.get(Calendar.HOUR_OF_DAY));
	        ndata.put("begin_minute","" + cal.get(Calendar.MINUTE));
	        ndata.put("begin_second","" + cal.get(Calendar.SECOND));
			
	        
			cal.setTime(end);
	        ndata.put("end_year","" + cal.get(Calendar.YEAR));
	        ndata.put("end_month","" + (cal.get(Calendar.MONTH) + 1));
	        ndata.put("end_day","" + cal.get(Calendar.DAY_OF_MONTH));
	        ndata.put("end_hour","" + cal.get(Calendar.HOUR_OF_DAY));
	        ndata.put("end_minute","" + cal.get(Calendar.MINUTE));
	        ndata.put("end_second","" + cal.get(Calendar.SECOND));
		}
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr); 
		
		if(!ret)
			throw new Exception(estr.toString());

		if (fmap.containsKey("return"))
			fmap.remove("return");
		return fmap;
	}
	/**
	 * ������Լ��������
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Map<String, String>>  queryAllMonitorInfo() throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "QueryAllMonitorInfo");
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if (fmap.containsKey("return"))
			fmap.remove("return");
		if(!ret)
			throw new Exception(estr.toString());
		return fmap;
	}
	
	public static Map<String, Map<String, String>> queryReportRunData(String strIdList, Date beginTime, Date endTime) throws Exception
	{
		throw new Exception("No implements now!!");
	}
	/*/
	 * //��ѯ group��entity��monitor ��Ҫ����ֵ, ע��monitor �޷���ȡ��parameter��advance parameter��error��warn��ok �е�ֵ
	dowhat= QueryInfo��    needkey= XXX,XXX,XXX...    (ѡ��)needtype= group(��entity��monitor,������3��),    (ѡ��)parentid= XXX ; //ָ������ parentid �ٶȿ��Ը���,������򷵻����нڵ������

	 */
	public static Map<String, Map<String, String>>  queryInfo(String needkey,String needtype,String parentid) throws Exception
	{
		Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
		Map<String, String> ndata = new FastMap<String, String>();
		StringBuilder estr = new StringBuilder();
		
		ndata.put("dowhat", "QueryInfo");
		ndata.put("parentid",parentid);
		ndata.put("needkey",needkey);
		ndata.put("needtype",needtype);
		
		boolean ret= Jsvapi.getInstance().GetUnivData(fmap, ndata, estr);
		
		if(!ret)
			throw new Exception(estr.toString());
		
		if (fmap.containsKey("return"))
			fmap.remove("return");

		return fmap;
	}
	public static Map<String, Map<String, String>>  smsTest(String phoneNumber,boolean ByWebSms) throws Exception
	{
		throw new Exception("No implements now!!");
	}
	public static Map<String, Map<String, String>>  getSmsDllName() throws Exception
	{
		throw new Exception("No implements now!!");
	}
	public static Map<String, Map<String, String>>  smsTestByDll(String phoneNumber,String parameter,String dllName) throws Exception
	{
		throw new Exception("No implements now!!");
	}
	public static Map<String, Map<String, String>>  emailTest(String mailServer,String mailTo,String mailFrom,String user,String password,String subject,String content) throws Exception
	{
		throw new Exception("No implements now!!");
	}	
/*// absoluteFileName �����ֵ������ FileName ;
//�����ļ������ӣ�  absoluteFileName= "c:\\windows\\regedit.exe";   
//����ļ������ӣ�  FileName= "..\\webapps\\ROOT\\Report\\ReportTemplate\\ReportServerCfg.gif";
//����ļ����ĵ�ǰĿ¼��: tomcat-6.0.14\bin
 * */
	public static Map<String, Map<String, String>>  getFileWithBase64(String absoluteFileName,String FileName,boolean fileMark) throws Exception
	{
		throw new Exception("No implements now!!");
	}
	public static Map<String, Map<String, String>>  getFileNameList(String absoluteDirName,String DirName) throws Exception
	{
		throw new Exception("No implements now!!");
	}
	public static boolean deleteFile(String absoluteDirName,String FileName) throws Exception
	{
		throw new Exception("No implements now!!");
	}		
	public static boolean uploadFileWithBase64(String absoluteDirName,String FileName,String ValueBase64) throws Exception
	{
		throw new Exception("No implements now!!");
	}	
	/*
	 *  ��ȡ����������ͼ�����֣�����ÿ�� view �� viewName= XXX,  fileName= XXX,
	 */
	public static Map<String, Map<String, String>> getAllView() throws Exception
	{
		throw new Exception("No implements now!!");
	}
	public static Map<String, Map<String, String>> addView(String viewName,String fileName,String newViewName) throws Exception
	{
		throw new Exception("No implements now!!");
	}
	public static Map<String, Map<String, String>> deleteView(String viewName) throws Exception
	{
		throw new Exception("No implements now!!");
	}	
	public static Map<String, Map<String, String>> deleteViewItem(String fileName,String item_id) throws Exception
	{
		throw new Exception("No implements now!!");
	}	

}
