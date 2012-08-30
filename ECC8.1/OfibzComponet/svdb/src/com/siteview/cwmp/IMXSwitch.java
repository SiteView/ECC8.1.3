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
 * iMX数字中继网关参数查询，修改接口
 * @author hailong.yi
 *
 */
@WebService(serviceName = "imxswitch", targetNamespace = "http://imxswitch.siteview.com")
public interface IMXSwitch {
	/**
	 * 取得参数的值 -- 单值
	 * @param id  标识符(设备ID)
	 * @param name 需要设置的值的名称
	 * @return	取得的值
	 * @throws Exception 异常(Code:MessageString)
	 */
	public String getValue(String id,String name) throws Exception;
	/**
	 * 取得参数的值 -- 全部
	 * @param id  标识符(设备ID)
	 * @return 取得的值的键值对列表
	 * @throws Exception 异常(Code:MessageString)
	 */
	public Map<String,String> getValuesById(String id) throws Exception;
	/**
	 * 取得参数的值 -- 指定名称列表的值
	 * @param id  标识符(设备ID)
	 * @param names  名称列表
	 * @return 取得的值的键值对列表
	 * @throws Exception 异常(Code:MessageString)
	 */
	public Map<String,String> getValuesByIdAndKeys(String id,String names[]) throws Exception;

	/**
	 * 取得参数的值 -- 指定id列表和名称列表的值
	 * @param ids	标识符(设备ID)列表
	 * @param names	名称列表
	 * @return	值的列表的键值对列表
	 * @throws Exception
	 */
	public Map<String, Map<String,String>> getValuesByIdsAndKeys(String[] ids,String names[]) throws Exception;
	
	public void setValue(String id,String name,String value) throws Exception;
	public void setValues(String id,Map<String,String> values) throws Exception;
	
	public void clearAlertQueue() throws Exception;
	

	/**
	 * 取得队列中index对应的告警信息
	 * @param index
	 * @return
	 * @throws Exception
	 */
	public ACSAlertInformation getAlertQueueInfor(int index) throws Exception;
	/**
	 * 通过id取得队列中的告警信息
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ACSAlertInformation getAlertQueueInforById(String id) throws Exception;

	/**
	 * 取得队列中所有的告警信息
	 * @return 告警信息列表
	 * @throws Exception
	 */
	public List<ACSAlertInformation> getAlertQueueAllInfors() throws Exception ;
	public List<ACSAlertInformation> getAlertQueueNewInfors(String beginId,String[] deviceIds,DeviceIdStruct[] sdeviceIds) throws Exception ;

	/**
	 * 得到当前的队列的大小。
	 * @return 当前的队列的大小。
	 * @throws Exception
	 */
	public int getAlertQueueSize() throws Exception ;

	/**
	 * 弹出队列中最老的一个告警信息
	 * @return 最老的告警信息
	 * @throws Exception
	 */
	public ACSAlertInformation popupAlertQueueInfor() throws Exception;
	
	/**
	 * 根据组合条件查询队列中的告警信息。条件deviceids,beNoted,begintime,endtime之间是and的关系。
	 * 如果没有输入其中一项，则省略该项。
	 * begintime,endtime必须同时输入，或者同时不输入。查询的为CreateDateTime在其间的数据，包含begintime,endtime本身
	 * @param deviceids 告警信息ID列表
	 * @param beNoted 是否给以备注
	 * @param begintime 开始时间
	 * @param endtime 结束时间
	 * @param pageNo 页号
	 * @param length 每页长度
	 * @return 列表结构
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
	 * 弹出队列中指定数量的最老的告警信息
	 * @param num 指定数量
	 * @return 告警信息列表
	 * @throws Exception
	 */
	public List<ACSAlertInformation> popupAlertInfors(int num) throws Exception ;
	public void deleteAlertQueueInfor(String id)throws Exception;
	public long queryAlertQueueInforsSize(String[] deviceids,DeviceIdStruct[] sdeviceIds,
			AlertStatus alertStatus, String begintime, String endtime) throws Exception;
	
	/**
	 * 批量删除队列中的相应告警信息
	 * @param ids 告警信息ID列表
	 * @throws Exception
	 */
	public void deleteAlertQueueInfors(String[] ids)throws Exception;
	
	/**
	 * 删除队列中的相应告警信息，并保存到历史记录中
	 * @param information 告警信息
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
	 * 删除告警信息
	 * @param begin 开始时间
	 * @param end 结束时间
	 * @throws Exception
	 */
	public void deleteAlertLogHistoryInfor(String begin,String end) throws Exception;
	/**
	 * 取所有的告警信息的ID
	 * @param begin
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public List<String> getAlertLogHistroyIds(String begin,String end) throws Exception;
	/**
	 * 取所有的告警信息的ID
	 * @throws Exception
	 */
	public List<String> getAlertLogHistoryAllIds() throws Exception;
	/**
	 * 保存修改过的告警信息到历史记录中
	 * 如果id不设置，则创建新的记录，否则，覆盖id的记录
	 * @throws Exception
	 */
	public void saveAlertLogHistoryInfor(ACSAlertInformation infor)throws Exception;
	public ACSAlertInformation readAlertLogHistoryInfor(String id) throws Exception;
}
