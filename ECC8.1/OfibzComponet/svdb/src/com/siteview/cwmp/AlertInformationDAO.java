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
	 * 删除告警信息
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @throws Exception
	 */
	public void deleteAlertInfor(String begin,String end) throws Exception;
	/**
	 * 取所有的告警信息的ID
	 * @param begin
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public List<String> getIds(String begin,String end) throws Exception;
	/**
	 * 取所有的告警信息的ID
	 * @throws Exception
	 */
	public List<String> getAllIds() throws Exception;
	/**
	 * 将信息保存到DAO中
	 * @throws Exception
	 */
	public void save(ACSAlertInformation infor)throws Exception;
	public ACSAlertInformation read(String id) throws Exception;
	
	public ACSAlertList queryHistoryAlertInfors(String[] deviceids,DeviceIdStruct[] sdeviceIds,AlertStatus alertStatus,String begintime,String endtime,int pageNo,int length)throws Exception;
	public int deleteHistoryAlertInfors(String[] deviceids,DeviceIdStruct[] sdeviceIds,AlertStatus alertStatus,String begintime,String endtime)throws Exception;
}
