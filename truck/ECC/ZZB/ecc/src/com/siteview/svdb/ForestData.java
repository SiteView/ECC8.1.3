package com.siteview.svdb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.siteview.jsvapi.ForestVector;
import com.siteview.jsvapi.Jsvapi;
import com.siteview.jsvapi.MyString;
import com.siteview.jsvapi.StringMap;
import com.siteview.jsvapi.SwigSvapi;


public class ForestData extends BaseSvdb {
	
	public static ForestData getInstance()
	{
		return new ForestData();
	}
 /*	
  * LoginName= XXX (即登录对话框中输入的名字, 如果不填则不做权限过滤、速度更快，填上后将作权限过滤); 
  *//*
	public  List<Map<String, String>> getTreeData(String parentid,boolean onlySon) throws Exception
	{
		return getTreeData(parentid,onlySon,null);
	}

	public  List<Map<String, String>> getTreeData(String parentid,boolean onlySon,String loginName) throws Exception
 {
	 List<Map<String, String>> vmap=new ArrayList<Map<String, String>>();
	 Map<String,String> param=new HashMap<String,String>();
	 StringBuilder errorStr = new StringBuilder();
	 param.put("dowhat","GetTreeData");
	 if(parentid!=null)
		 param.put("parentid",parentid);
	 else
		 param.put("parentid","default");
	 param.put("onlySon",String.valueOf(onlySon));
	 param.put("onlySon",String.valueOf(onlySon));
	 if(loginName!=null)
		 param.put("LoginName",loginName);
	 
	 boolean ret = Jsvapi.getInstance().GetForestData(vmap, param, errorStr);
	 if(ret==false)
		 throw new Exception(errorStr.toString());
	 
	 return vmap;
 }*/
 /*
  * dowhat= GetTreeData2 , parentid= XXX,	onlySon= true/false，　LoginName= XXX,   needKeys= XXX,XXX,XXX... ; //根据需要的键 needKeys ,过滤 GetTreeData 返回的数据
  *//*
	public  List<Map<String, String>> getTreeData2(String parentid,boolean onlySon,String needKeys) throws Exception
	{
		return getTreeData2(parentid,onlySon,"admin",needKeys);
	}

	public  List<Map<String, String>> getTreeData2(String parentid,boolean onlySon,String loginName,String needKeys) throws Exception
 {
	 List<Map<String, String>> vmap=new ArrayList<Map<String, String>>();
	 Map<String,String> param=new HashMap<String,String>();
	 StringBuilder errorStr = new StringBuilder();
	 param.put("dowhat","GetTreeData2");
	 if(parentid!=null)
		 param.put("parentid",parentid);
	 else
		 param.put("parentid","default");
	 param.put("onlySon",String.valueOf(onlySon));
	 if(loginName!=null)
		 param.put("LoginName",loginName);
	 if(needKeys!=null)
		 param.put("needKeys",needKeys);
	 boolean ret = Jsvapi.getInstance().GetForestData(vmap, param, errorStr);
	 if(ret==false)
		 throw new Exception(errorStr.toString());
	 
	 return vmap;
 }*/
	/*
	  * 对应 svapi 函数是：QueryRecords, 查询数据库监测记录  ; 记录个数极限为50000
	  *//*
	 public  List<Map<String, String>> QueryReportData(String id,String begin_year,String begin_month,String begin_day,String begin_hour,String begin_minute,String begin_second,String end_year,String end_month,String end_day,String end_hour,String end_minute,String end_second) throws Exception
	 {
		 List<Map<String, String>> vmap=new ArrayList<Map<String, String>>();
		 Map<String,String> param=new HashMap<String,String>();
		 StringBuilder errorStr = new StringBuilder();
		 param.put("dowhat","QueryReportData");
		 param.put("compress", "false");
		 param.put("dstrNeed", "false");
		 //param.put("dstrStatusNoNeed", dstrStatusNoNeed);
		 //param.put("return_value_filter",return_value_filter);
		 
		 if(id!=null)
			 param.put("id",id);
		 if(begin_year!=null)
			 param.put("begin_year",begin_year);
		 if(begin_month!=null)
			 param.put("begin_month",begin_month);
		 if(begin_day!=null)
			 param.put("begin_day",begin_day);
		 if(begin_hour!=null)
			 param.put("begin_hour",begin_hour);
		 if(begin_minute!=null)
			 param.put("begin_minute",begin_minute);
		 if(begin_second!=null)
			 param.put("begin_second",begin_second);
		 if(end_year!=null)
			 param.put("end_year",end_year);
		 if(end_month!=null)
			 param.put("end_month",end_month);
		 if(end_day!=null)
			 param.put("end_day",end_day);
		 if(end_hour!=null)
			 param.put("end_hour",end_hour);
		 if(end_minute!=null)
			 param.put("end_minute",end_minute);
		 if(end_second!=null)
			 param.put("end_second",end_second);
		 
		 
		 boolean ret = Jsvapi.getInstance().GetForestData(vmap, param, errorStr);
		 if(ret==false)
			 throw new Exception(errorStr.toString());
		 
		 return vmap;
	 }*/
 /*
  * 对应 svapi 函数是：QueryRecords, 查询数据库监测记录  ; 记录个数极限为50000
  */
 public   List<Map<String,String>> queryRecordsByTime(Map<String,String> param,String id,String begin_year,String begin_month,String begin_day,String begin_hour,String begin_minute,String begin_second,String end_year,String end_month,String end_day,String end_hour,String end_minute,String end_second) throws Exception
 {
	 StringBuilder errorStr = new StringBuilder();
	 
	 if(id!=null)
		 param.put("id",id);
	 if(begin_year!=null)
		 param.put("begin_year",begin_year);
	 if(begin_month!=null)
		 param.put("begin_month",begin_month);
	 if(begin_day!=null)
		 param.put("begin_day",begin_day);
	 if(begin_hour!=null)
		 param.put("begin_hour",begin_hour);
	 if(begin_minute!=null)
		 param.put("begin_minute",begin_minute);
	 if(begin_second!=null)
		 param.put("begin_second",begin_second);
	 if(end_year!=null)
		 param.put("end_year",end_year);
	 if(end_month!=null)
		 param.put("end_month",end_month);
	 if(end_day!=null)
		 param.put("end_day",end_day);
	 if(end_hour!=null)
		 param.put("end_hour",end_hour);
	 if(end_minute!=null)
		 param.put("end_minute",end_minute);
	 if(end_second!=null)
		 param.put("end_second",end_second);
	 
	 
	 List<Map<String,String>> fvec=new ArrayList<Map<String,String>> (); 
	 boolean ret = Jsvapi.getInstance().GetForestData(fvec, param, errorStr);
	 if(ret==false)
		 throw new Exception(errorStr.toString());
	 
	 return fvec;
 }
 /*
  * 不装配,省内存,稍微麻烦
  */
 public   ForestVector queryRecordsByTimeFast(StringMap param,String id,String begin_year,String begin_month,String begin_day,String begin_hour,String begin_minute,String begin_second,String end_year,String end_month,String end_day,String end_hour,String end_minute,String end_second) throws Exception
 {
	 StringBuilder errorStr = new StringBuilder();
	 
	 if(id!=null)
		 param.set("id",id);
	 if(begin_year!=null)
		 param.set("begin_year",begin_year);
	 if(begin_month!=null)
		 param.set("begin_month",begin_month);
	 if(begin_day!=null)
		 param.set("begin_day",begin_day);
	 if(begin_hour!=null)
		 param.set("begin_hour",begin_hour);
	 if(begin_minute!=null)
		 param.set("begin_minute",begin_minute);
	 if(begin_second!=null)
		 param.set("begin_second",begin_second);
	 if(end_year!=null)
		 param.set("end_year",end_year);
	 if(end_month!=null)
		 param.set("end_month",end_month);
	 if(end_day!=null)
		 param.set("end_day",end_day);
	 if(end_hour!=null)
		 param.set("end_hour",end_hour);
	 if(end_minute!=null)
		 param.set("end_minute",end_minute);
	 if(end_second!=null)
		 param.set("end_second",end_second);
	 
	 
	ForestVector fvec = new ForestVector();
	MyString mestr = new MyString();
		
	boolean ret = SwigSvapi.swig_GetForestData(fvec, param, mestr); 

	 if(ret==false)
		 throw new Exception(errorStr.toString());
	 
	 return fvec;
 }
 /*
  * 对应 svapi 函数是：QueryRecords, 查询数据库监测记录  ; 记录个数极限为50000
  */
 /*
 public  List<Map<String, String>> queryRecordsByCount(String id,int count) throws Exception
 {
	 List<Map<String, String>> vmap=new ArrayList<Map<String, String>>();
	 Map<String,String> param=new HashMap<String,String>();
	 StringBuilder errorStr = new StringBuilder();
	 param.put("dowhat","QueryRecordsByCount");
	 param.put("id",id);
	 param.put("count",String.valueOf(count));
	 
	 boolean ret = Jsvapi.getInstance().GetForestData(vmap, param, errorStr);
	 if(ret==false)
		 throw new Exception(errorStr.toString());
	 
	 return vmap;
 }
 */
 /*
  * 查询报警日志 ; 记录个数极限为50000,alertIndex、alertName、alertReceive、alertType 是 and 关系，如果为空则为全部。
  */
 /*
 public  List<Map<String, String>> queryAlertLog(String id,String begin_year,String begin_month,String begin_day,String begin_hour,String begin_minute,String begin_second,String end_year,String end_month,String end_day,String end_hour,String end_minute,String end_second,String alertIndex,String alertName,String alertReceive,String alertType) throws Exception
 {
	 List<Map<String, String>> vmap=new ArrayList<Map<String, String>>();
	 Map<String,String> param=new HashMap<String,String>();
	 StringBuilder errorStr = new StringBuilder();
	 param.put("dowhat","QueryAlertLog");
	 if(id!=null)
		 param.put("id",id);
	 if(begin_year!=null)
		 param.put("begin_year",begin_year);
	 if(begin_month!=null)
		 param.put("begin_month",begin_month);
	 if(begin_day!=null)
		 param.put("begin_day",begin_day);
	 if(begin_hour!=null)
		 param.put("begin_hour",begin_hour);
	 if(begin_minute!=null)
		 param.put("begin_minute",begin_minute);
	 if(begin_second!=null)
		 param.put("begin_second",begin_second);
	 if(end_year!=null)
		 param.put("end_year",end_year);
	 if(end_month!=null)
		 param.put("end_month",end_month);
	 if(end_day!=null)
		 param.put("end_day",end_day);
	 if(end_hour!=null)
		 param.put("end_hour",end_hour);
	 if(end_minute!=null)
		 param.put("end_minute",end_minute);
	 if(end_second!=null)
		 param.put("end_second",end_second);
	 if(alertIndex!=null)
		 param.put("alertIndex",alertIndex);
	 if(alertName!=null)
		 param.put("alertName",alertName);
	 if(alertReceive!=null)
		 param.put("alertReceive",alertReceive); 
	 if(alertType!=null)
		 param.put("alertType",alertType); 
	 
	 boolean ret = Jsvapi.getInstance().GetForestData(vmap, param, errorStr);
	 if(ret==false)
		 throw new Exception(errorStr.toString());
	 
	 return vmap;
 }*/
 /*
  * 获取某个虚拟视图的数据， 获取虚拟项目前检验对应真实项目的存在(如果某 item 有值 CheckNothing= true，则不校验)
  * 每个虚拟项目返回： item_id, sv_id, type 
	needDynamicData= true (默认为 false, getTreeData 的动态值),    needKeys= XXX,XXX,XXX...
  * 
  */
 /*
 public  List<Map<String, String>> getViewData(String fileName,boolean needDynamicData,String needKeys) throws Exception
 {
	 List<Map<String, String>> vmap=new ArrayList<Map<String, String>>();
	 Map<String,String> param=new HashMap<String,String>();
	 StringBuilder errorStr = new StringBuilder();
	 param.put("dowhat","GetViewData");
	 if(fileName!=null)
		 param.put("fileName",fileName);
	 param.put("needDynamicData",String.valueOf(needDynamicData));
	 if(needKeys!=null)
		 param.put("needKeys",needKeys);
	 boolean ret = Jsvapi.getInstance().GetForestData(vmap, param, errorStr);
	 if(ret==false)
		 throw new Exception(errorStr.toString());
	 
	 return vmap;
 }
 */
}
