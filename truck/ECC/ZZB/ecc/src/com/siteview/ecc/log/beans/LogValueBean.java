/**
 * 
 */
package com.siteview.ecc.log.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuandong s[0] = m.get(i).get("_UserID"); s[1] =
 *         m.get(i).get("_OperateObjName"); s[2] = m.get(i).get("_OperateType");
 *         s[3] = m.get(i).get("_OperateTime"); s[4] =
 *         m.get(i).get("_OperateObjInfo");
 */
public class LogValueBean  {

	
	private String userId;

	private String operateObjName;
	private String operateType;
	private String operateTime;
	private String operateObjInfo;

	private Map<String, String> m_obj = new HashMap<String, String>();
	private Map<String, String> m_type = new HashMap<String, String>();

	public LogValueBean(List<Map<String, String>> a, int i) {
		
		//以下代号定义来自 mmc 版本   UserLogMmcListView.cs  60行  至 90行  //修改部分和 mmc 保持一致
		
		m_obj.put("0", "登录");            
		m_obj.put("1", "SE");              
		m_obj.put("2", "组");              
		m_obj.put("3", "设备");            
		m_obj.put("4", "监测器");          
		m_obj.put("5", "基本设置");        
		m_obj.put("6", "客户名称");        
		m_obj.put("7", "监测服务器名称");  
		m_obj.put("8", "报警规则");        
		m_obj.put("9", "拓扑视图");        
		m_obj.put("10", "SysLog设置");     
		m_obj.put("11", "用户管理");       
		m_obj.put("12", "邮件设置");       
		m_obj.put("13", "时间段任务计划"); 
		m_obj.put("14", "绝对时间任务计划");
		m_obj.put("15", "短信设置");       
		m_obj.put("16", "TopN报告");       
		m_obj.put("17", "统计报告");       
		m_obj.put("18", "值班配置");       
		m_obj.put("20", "相对时间任务计划"); 
		m_obj.put("22", "监测器浏览"); 		
		m_obj.put("23", "监测器设置"); 
		m_obj.put("24", "邮件模板"); 
		m_obj.put("25", "短信模板"); 
		m_obj.put("28", "IP地址"); 
		
		m_type.put("0", "登录");
		m_type.put("1", "添加");
		m_type.put("2", "编辑");
		m_type.put("3", "删除");
		m_type.put("4", "允许");
		m_type.put("5", "禁止");
		m_type.put("6", "批量添加");
		m_type.put("7", "快速添加");
		m_type.put("8", "粘贴");
		m_type.put("9", "事件确认");

		
		userId = a.get(i).get("_UserID");
		operateObjName = m_obj.get(a.get(i).get("_OperateObjName"));
		operateType = m_type.get(a.get(i).get("_OperateType"));
		operateTime = a.get(i).get("_OperateTime");
		operateObjInfo = a.get(i).get("_OperateObjInfo");

	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOperateObjName() {
		return operateObjName;
	}

	public void setOperateObjName(String operateObjName) {
		this.operateObjName = operateObjName;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}

	public String getOperateObjInfo() {
		return operateObjInfo;
	}

	public void setOperateObjInfo(String operateObjInfo) {
		this.operateObjInfo = operateObjInfo;
	}

	public Map<String, String> getM_obj() {
		return m_obj;
	}

	public void setM_obj(Map<String, String> m_obj) {
		this.m_obj = m_obj;
	}

	public Map<String, String> getM_type() {
		return m_type;
	}

	public void setM_type(Map<String, String> m_type) {
		this.m_type = m_type;
	}


	/*public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		LogValueList temp = (LogValueList) arg0;
		return this.operateTime.compareTo(temp.operateTime);
	}*/

}
