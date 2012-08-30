package com.siteview.cwmp;

import java.util.List;

import com.siteview.cwmp.bean.ACSAlertInformation;
import com.siteview.cwmp.bean.ACSAlertList;
import com.siteview.cwmp.bean.AlertStatus;

import cwmp_1_1.dslforum_org.DeviceIdStruct;

public interface AlertInformationDAO {
	public List<ACSAlertInformation> getAlertInforsByDeviceId(int pageNo,int length,String realDeviceId,DeviceIdStruct sdeviceId)throws Exception;
	public List<ACSAlertInformation> getAlertInfors(int pageNo,int length)throws Exception;
	public long getSizeByDeviceId(String realDeviceId,DeviceIdStruct sdeviceId) throws Exception;
	public long getSize() throws Exception;
	public void deleteAlertInfors(String[] ids) throws Exception;
	public void deleteAlertInfor(String id) throws Exception;
	
	/**
	 * ɾ���澯��Ϣ
	 * @param begin ��ʼʱ��
	 * @param end ����ʱ��
	 * @throws Exception
	 */
	public void deleteAlertInfor(String begin,String end) throws Exception;
	/**
	 * ȡ���еĸ澯��Ϣ��ID
	 * @param begin
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public List<String> getIds(String begin,String end) throws Exception;
	/**
	 * ȡ���еĸ澯��Ϣ��ID
	 * @throws Exception
	 */
	public List<String> getAllIds() throws Exception;
	/**
	 * ����Ϣ���浽DAO��
	 * @throws Exception
	 */
	public void save(ACSAlertInformation infor)throws Exception;
	public ACSAlertInformation read(String id) throws Exception;
	
	public ACSAlertList queryHistoryAlertInfors(String[] deviceids,DeviceIdStruct[] sdeviceIds,AlertStatus alertStatus,String begintime,String endtime,int pageNo,int length)throws Exception;
	public int deleteHistoryAlertInfors(String[] deviceids,DeviceIdStruct[] sdeviceIds,AlertStatus alertStatus,String begintime,String endtime)throws Exception;
}
