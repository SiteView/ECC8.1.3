package com.siteview.base.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.siteview.jsvapi.Jsvapi;

public class MonitorTemplate implements Serializable
{
	private static final long serialVersionUID = 5870040839783079266L;
	private Map<String, Map<String, String>>	m_fmap;
	private List<Map<String, String>>			m_parameter_items		= new ArrayList<Map<String, String>>();
	private List<Map<String, String>>			m_ad_parameter_items	= new ArrayList<Map<String, String>>();
	private List<Map<String, String>>			m_return_items			= new ArrayList<Map<String, String>>();
	
	public MonitorTemplate(Map<String, Map<String, String>> fmap)
	{
		m_fmap= fmap;
		
		m_parameter_items.clear();
		for(int i=1; i<100; ++i)
		{
			String index= "ParameterItem_"+i;
			Map<String, String> data= m_fmap.get(index);
			if(data==null || data.isEmpty())
				break;
			
			m_parameter_items.add(data);
		}
		
		m_ad_parameter_items.clear();
		for(int i=1; i<100; ++i)
		{
			String index= "AdvanceParameterItem_"+i;
			Map<String, String> data= m_fmap.get(index);
			if(data==null || data.isEmpty())
				break;
			
			m_ad_parameter_items.add(data);
		}
		
		m_return_items.clear();
		for(int i=1; i<100; ++i)
		{
			String index= "ReturnItem_"+i;
			Map<String, String> data= m_fmap.get(index);
			if(data==null || data.isEmpty())
				break;
			
			m_return_items.add(data);
		}
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
	
	public List<Map<String, String>> get_Parameter_Items()
	{
		List<Map<String, String>> a= new ArrayList<Map<String, String>>(m_parameter_items);
		return a;
	}
	
	public List<Map<String, String>> get_Advance_Parameter_Items()
	{
		List<Map<String, String>> a= new ArrayList<Map<String, String>>(m_ad_parameter_items);
		return a;
	}
	
	public List<Map<String, String>> get_Return_Items()
	{
		List<Map<String, String>> a= new ArrayList<Map<String, String>>(m_return_items);
		return a;
	}

	public Map<String, String> get_Property()
	{
		Map<String, String> n= m_fmap.get("property");
		Map<String, String> data=null;
		if(n!=null)
			data= new HashMap<String, String>(n);
		return data;
	}
	
	public Map<String, String> get_error_conditon()
	{
		Map<String, String> n= m_fmap.get("error");
		Map<String, String> data=null;
		if(n!=null)
			data= new HashMap<String, String>(n);
		return data;
	}
	
	public Map<String, String> get_warning_conditon()
	{
		Map<String, String> n= m_fmap.get("warning");
		Map<String, String> data=null;
		if(n!=null)
			data= new HashMap<String, String>(n);
		return data;
	}
	
	public Map<String, String> get_good_conditon()
	{
		Map<String, String> n= m_fmap.get("good");
		Map<String, String> data=null;
		if(n!=null)
			data= new HashMap<String, String>(n);
		return data;
	}

	public void display()
	{
		if(m_fmap==null)
			return;
		Jsvapi.getInstance().DisplayUtilMapInMap(m_fmap);
	}
	
//	  -- Display UtilMapInMap begin (13 node) -- 
//	     ---- ParameterItem_2 (15 key) ----
//	        sv_run= "false"
//	        sv_itemlabel2= "小时"
//	        sv_itemlabel1= "分钟"
//	        sv_name= "_frequencyUnit"
//	        sv_tip= "请检查输入的监测频率是否正确"
//	        sv_isreadonly= "false"
//	        sv_allownull= "false"
//	        sv_itemvalue1= "1"
//	        sv_style= "cell_10"
//	        sv_value= "1"
//	        sv_label= "FrequencyUnit"
//	        sv_type= "combobox"
//	        sv_itemcount= "2"
//	        sv_itemvalue2= "60"
//	        sv_helptext= "监测器的监测频率"
//	     ---- ParameterItem_1 (15 key) ----
//	        sv_run= "false"
//	        sv_accountwith= "_frequencyUnit"
//	        sv_name= "_frequency"
//	        sv_isreadonly= "false"
//	        sv_allownull= "false"
//	        sv_style= "cell_10"
//	        sv_minivalue= "1"
//	        sv_expressions= "*"
//	        sv_value= "5"
//	        sv_isnumeric= "true"
//	        sv_label= "监测频率"
//	        sv_maxvalue= "4000"
//	        sv_type= "textbox"
//	        sv_follow= "_frequencyUnit"
//	        sv_helptext= "监测器的监测频率"
//	     ---- error (13 key) ----
//	        sv_expression= "1"
//	        sv_name= "_errorParameter"
//	        sv_tip= "请检查输入的错误阀值是否正确"
//	        sv_isreadonly= "false"
//	        sv_style= "cell_80"
//	        sv_paramvalue1= "0"
//	        sv_paramname1= "packetsGoodPercent"
//	        sv_conditioncount= "1"
//	        sv_value= "[包成功率(%) == 0]"
//	        sv_label= "错误"
//	        sv_operate1= "=="
//	        sv_type= "textarea"
//	        sv_helptext= "设置错误条件,满足此条件则该监测状态为错误，表现为红色"
//	     ---- return (2 key) ----
//	        id= "5"
//	        return= "true"
//	     ---- ReturnItem_2 (9 key) ----
//	        sv_label= "数据往返时间(ms)"
//	        sv_unit= "(ms)"
//	        sv_name= "roundTripTime"
//	        sv_type= "Float"
//	        sv_drawimage= "1"
//	        sv_drawtable= "1"
//	        sv_primary= "0"
//	        sv_drawmeasure= "1"
//	        sv_baseline= "0"
//	     ---- ReturnItem_3 (9 key) ----
//	        sv_label= "状态值(200表示成功 300表示出错)"
//	        sv_unit= ""
//	        sv_name= "status"
//	        sv_type= "Float"
//	        sv_drawimage= "0"
//	        sv_drawtable= "0"
//	        sv_primary= "0"
//	        sv_drawmeasure= "1"
//	        sv_baseline= "0"
//	     ---- property (8 key) ----
//	        sv_label= "Ping"
//	        sv_description= "监测Ping指定服务器状况"
//	        sv_name= "Ping"
//	        sv_id= "5"
//	        sv_helplink= "javascript:shelp2('monitor_ping.htm')"
//	        sv_func= "PING"
//	        sv_dll= "msping.dll"
//	        sv_class= "Ping"
//	     ---- good (13 key) ----
//	        sv_expression= "1"
//	        sv_name= "_goodParameter"
//	        sv_tip= "请检查输入的正常阀值是否正确"
//	        sv_isreadonly= "false"
//	        sv_style= "cell_80"
//	        sv_paramvalue1= "75"
//	        sv_paramname1= "packetsGoodPercent"
//	        sv_conditioncount= "1"
//	        sv_value= "[包成功率(%) >= 75]"
//	        sv_label= "正常"
//	        sv_operate1= ">"
//	        sv_type= "textarea"
//	        sv_helptext= "设置正常条件,满足此条件则该监测状态为正常，表现为绿色"
//	     ---- ReturnItem_1 (9 key) ----
//	        sv_label= "包成功率(%)"
//	        sv_unit= "(%)"
//	        sv_name= "packetsGoodPercent"
//	        sv_type= "Float"
//	        sv_drawimage= "1"
//	        sv_drawtable= "1"
//	        sv_primary= "1"
//	        sv_drawmeasure= "1"
//	        sv_baseline= "1"
//	     ---- AdvanceParameterItem_3 (12 key) ----
//	        sv_label= "超时"
//	        sv_run= "true"
//	        sv_maxvalue= "45000"
//	        sv_tip= "请检查输入的超时时间是否正确(最大设置为40000毫秒)"
//	        sv_name= "_Timeout"
//	        sv_type= "textbox"
//	        sv_isreadonly= "false"
//	        sv_style= "cell_10"
//	        sv_minivalue= "1"
//	        sv_value= "20000"
//	        sv_helptext= "PING监测的超时时间,（单位:毫秒）"
//	        sv_isnumeric= "true"
//	     ---- AdvanceParameterItem_1 (12 key) ----
//	        sv_label= "包大小"
//	        sv_run= "true"
//	        sv_maxvalue= "3000"
//	        sv_tip= "请检查输入的数据包大小是否正确"
//	        sv_name= "_SendBytes"
//	        sv_type= "textbox"
//	        sv_isreadonly= "false"
//	        sv_style= "cell_10"
//	        sv_minivalue= "1"
//	        sv_value= "64"
//	        sv_helptext= "PING数据包的大小,（单位：字节）"
//	        sv_isnumeric= "true"
//	     ---- AdvanceParameterItem_2 (12 key) ----
//	        sv_label= "Ping次数"
//	        sv_run= "true"
//	        sv_maxvalue= "12"
//	        sv_tip= "请检查输入的Ping次数是否正确"
//	        sv_name= "_SendNums"
//	        sv_type= "textbox"
//	        sv_isreadonly= "false"
//	        sv_style= "cell_10"
//	        sv_minivalue= "1"
//	        sv_value= "6"
//	        sv_helptext= "一次Ping监测所执行的ping操作次数（最大设置为10次）"
//	        sv_isnumeric= "true"
//	     ---- warning (13 key) ----
//	        sv_expression= "1"
//	        sv_name= "_warningParameter"
//	        sv_tip= "请检查输入的危险阀值是否正确"
//	        sv_isreadonly= "false"
//	        sv_style= "cell_80"
//	        sv_paramvalue1= "75"
//	        sv_paramname1= "packetsGoodPercent"
//	        sv_conditioncount= "1"
//	        sv_value= "[包成功率(%) <= 75]"
//	        sv_label= "危险"
//	        sv_operate1= "<="
//	        sv_type= "textarea"
//	        sv_helptext= "设置危险条件,满足此条件则该监测状态为危险，表现为黄色"
//	   -- Display UtilMapInMap end (13 node) -- 
	
	
	
}
