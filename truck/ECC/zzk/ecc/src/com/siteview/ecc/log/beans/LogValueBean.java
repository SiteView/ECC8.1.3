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
		
		//���´��Ŷ������� mmc �汾   UserLogMmcListView.cs  60��  �� 90��  //�޸Ĳ��ֺ� mmc ����һ��
		
		m_obj.put("0", "��¼");            
		m_obj.put("1", "SE");              
		m_obj.put("2", "��");              
		m_obj.put("3", "�豸");            
		m_obj.put("4", "�����");          
		m_obj.put("5", "��������");        
		m_obj.put("6", "�ͻ�����");        
		m_obj.put("7", "������������");  
		m_obj.put("8", "��������");        
		m_obj.put("9", "������ͼ");        
		m_obj.put("10", "SysLog����");     
		m_obj.put("11", "�û�����");       
		m_obj.put("12", "�ʼ�����");       
		m_obj.put("13", "ʱ�������ƻ�"); 
		m_obj.put("14", "����ʱ������ƻ�");
		m_obj.put("15", "��������");       
		m_obj.put("16", "TopN����");       
		m_obj.put("17", "ͳ�Ʊ���");       
		m_obj.put("18", "ֵ������");       
		m_obj.put("20", "���ʱ������ƻ�"); 
		m_obj.put("22", "��������"); 		
		m_obj.put("23", "���������"); 
		m_obj.put("24", "�ʼ�ģ��"); 
		m_obj.put("25", "����ģ��"); 
		m_obj.put("28", "IP��ַ"); 
		
		m_type.put("0", "��¼");
		m_type.put("1", "���");
		m_type.put("2", "�༭");
		m_type.put("3", "ɾ��");
		m_type.put("4", "����");
		m_type.put("5", "��ֹ");
		m_type.put("6", "�������");
		m_type.put("7", "�������");
		m_type.put("8", "ճ��");
		m_type.put("9", "�¼�ȷ��");

		
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
