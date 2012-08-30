package com.siteview.cwmp;

import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import com.siteview.cwmp.bean.ACSAlertInformation;
import com.siteview.cwmp.bean.ACSAlertList;
import com.siteview.cwmp.bean.AlertStatus;

import cwmp_1_1.dslforum_org.DeviceIdStruct;

/**
 * 
 * iMX�����м����ز�����ѯ���޸Ľӿ�
 * @author hailong.yi
 *
 */
@WebService(serviceName = "imxswitch", targetNamespace = "http://imxswitch.siteview.com")
public interface IMXSwitch {
	/**
	 * ȡ�ò�����ֵ -- ��ֵ
	 * @param id  ��ʶ��(�豸ID)
	 * @param name ��Ҫ���õ�ֵ������
	 * @return	ȡ�õ�ֵ
	 * @throws Exception �쳣(Code:MessageString)
	 */
	public String getValue(String id,String name) throws Exception;
	/**
	 * ȡ�ò�����ֵ -- ȫ��
	 * @param id  ��ʶ��(�豸ID)
	 * @return ȡ�õ�ֵ�ļ�ֵ���б�
	 * @throws Exception �쳣(Code:MessageString)
	 */
	public Map<String,String> getValuesById(String id) throws Exception;
	/**
	 * ȡ�ò�����ֵ -- ָ�������б��ֵ
	 * @param id  ��ʶ��(�豸ID)
	 * @param names  �����б�
	 * @return ȡ�õ�ֵ�ļ�ֵ���б�
	 * @throws Exception �쳣(Code:MessageString)
	 */
	public Map<String,String> getValuesByIdAndKeys(String id,String names[]) throws Exception;

	/**
	 * ȡ�ò�����ֵ -- ָ��id�б�������б��ֵ
	 * @param ids	��ʶ��(�豸ID)�б�
	 * @param names	�����б�
	 * @return	ֵ���б�ļ�ֵ���б�
	 * @throws Exception
	 */
	public Map<String, Map<String,String>> getValuesByIdsAndKeys(String[] ids,String names[]) throws Exception;
	
	public void setValue(String id,String name,String value) throws Exception;
	public void setValues(String id,Map<String,String> values) throws Exception;
	
	public void clearAlertQueue() throws Exception;
	

	/**
	 * ȡ�ö�����index��Ӧ�ĸ澯��Ϣ
	 * @param index
	 * @return
	 * @throws Exception
	 */
	public ACSAlertInformation getAlertQueueInfor(int index) throws Exception;
	/**
	 * ͨ��idȡ�ö����еĸ澯��Ϣ
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ACSAlertInformation getAlertQueueInforById(String id) throws Exception;

	/**
	 * ȡ�ö��������еĸ澯��Ϣ
	 * @return �澯��Ϣ�б�
	 * @throws Exception
	 */
	public List<ACSAlertInformation> getAlertQueueAllInfors() throws Exception ;
	public List<ACSAlertInformation> getAlertQueueNewInfors(String beginId,String[] deviceIds,DeviceIdStruct[] sdeviceIds) throws Exception ;

	/**
	 * �õ���ǰ�Ķ��еĴ�С��
	 * @return ��ǰ�Ķ��еĴ�С��
	 * @throws Exception
	 */
	public int getAlertQueueSize() throws Exception ;

	/**
	 * �������������ϵ�һ���澯��Ϣ
	 * @return ���ϵĸ澯��Ϣ
	 * @throws Exception
	 */
	public ACSAlertInformation popupAlertQueueInfor() throws Exception;
	
	/**
	 * �������������ѯ�����еĸ澯��Ϣ������deviceids,beNoted,begintime,endtime֮����and�Ĺ�ϵ��
	 * ���û����������һ���ʡ�Ը��
	 * begintime,endtime����ͬʱ���룬����ͬʱ�����롣��ѯ��ΪCreateDateTime���������ݣ�����begintime,endtime����
	 * @param deviceids �澯��ϢID�б�
	 * @param beNoted �Ƿ���Ա�ע
	 * @param begintime ��ʼʱ��
	 * @param endtime ����ʱ��
	 * @param pageNo ҳ��
	 * @param length ÿҳ����
	 * @return �б�ṹ
	 * @throws Exception
	 */
	public ACSAlertList queryAlertQueueInfors(String[] deviceids,DeviceIdStruct[] sdeviceIds,AlertStatus alertStatus,String begintime,String endtime,int pageNo,int length)throws Exception;
	
	/**
	 * 
	 * @param deviceids
	 * @param beNoted
	 * @param begintime
	 * @param endtime
	 * @param pageNo
	 * @param length
	 * @return
	 * @throws Exception
	 */
	public ACSAlertList queryHistoryAlertInfors(String[] deviceids,DeviceIdStruct[] sdeviceIds,AlertStatus alertStatus,String begintime,String endtime,int pageNo,int length)throws Exception;
	public int deleteHistoryAlertInfors(String[] deviceids,DeviceIdStruct[] sdeviceIds,AlertStatus alertStatus,String begintime,String endtime)throws Exception;
	
	/**
	 * ����������ָ�����������ϵĸ澯��Ϣ
	 * @param num ָ������
	 * @return �澯��Ϣ�б�
	 * @throws Exception
	 */
	public List<ACSAlertInformation> popupAlertInfors(int num) throws Exception ;
	public void deleteAlertQueueInfor(String id)throws Exception;
	public long queryAlertQueueInforsSize(String[] deviceids,DeviceIdStruct[] sdeviceIds,
			AlertStatus alertStatus, String begintime, String endtime) throws Exception;
	
	/**
	 * ����ɾ�������е���Ӧ�澯��Ϣ
	 * @param ids �澯��ϢID�б�
	 * @throws Exception
	 */
	public void deleteAlertQueueInfors(String[] ids)throws Exception;
	
	/**
	 * ɾ�������е���Ӧ�澯��Ϣ�������浽��ʷ��¼��
	 * @param information �澯��Ϣ
	 * @throws Exception
	 */
	public void updateAlertQueueInfor(ACSAlertInformation information) throws Exception;
	public void pushAlertQueueInfor(ACSAlertInformation information) throws Exception ;
	
	public Map<String,String> stateAlertQueueInfors(String name)throws Exception;
	public List<ACSAlertInformation> getAlertQueueInforsByCondition(int pageNo,int length)throws Exception;
	public List<ACSAlertInformation> getAlertLogHistroryByConditionByDeviceId(int pageNo,int length,String realDeviceId,DeviceIdStruct sdeviceId)throws Exception;
	public List<ACSAlertInformation> getAlertLogHistroryByCondition(int pageNo,int length)throws Exception;
	public long getAlertLogHistrorySizeByDeviceId(String realDeviceId,DeviceIdStruct sdeviceId) throws Exception;
	public long getAlertLogHistrorySize() throws Exception;
	public void deleteAlertLogHistoryInfors(String[] ids) throws Exception;
	public void deleteAlertHistoryInfor(String id) throws Exception;
	
	/**
	 * ɾ���澯��Ϣ
	 * @param begin ��ʼʱ��
	 * @param end ����ʱ��
	 * @throws Exception
	 */
	public void deleteAlertLogHistoryInfor(String begin,String end) throws Exception;
	/**
	 * ȡ���еĸ澯��Ϣ��ID
	 * @param begin
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public List<String> getAlertLogHistroyIds(String begin,String end) throws Exception;
	/**
	 * ȡ���еĸ澯��Ϣ��ID
	 * @throws Exception
	 */
	public List<String> getAlertLogHistoryAllIds() throws Exception;
	/**
	 * �����޸Ĺ��ĸ澯��Ϣ����ʷ��¼��
	 * ���id�����ã��򴴽��µļ�¼�����򣬸���id�ļ�¼
	 * @throws Exception
	 */
	public void saveAlertLogHistoryInfor(ACSAlertInformation infor)throws Exception;
	public ACSAlertInformation readAlertLogHistoryInfor(String id) throws Exception;
}
