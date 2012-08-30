package com.siteview.cwmp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import com.siteview.cwmp.bean.ACSAlertInformation;
import com.siteview.cwmp.bean.ACSAlertList;
import com.siteview.cwmp.bean.AlertStatus;
import com.siteview.utils.FactoryTools;

import cwmp_1_1.dslforum_org.DeviceIdStruct;

@WebService(serviceName = "imxswitch", endpointInterface = "com.siteview.cwmp.IMXSwitch")
public class IMXSwitchImpl implements IMXSwitch {
	public static final AlertInformationDAO dao = FactoryTools.getAlertInformationDAOImpl();
	
	@Override
	public String getValue(String id, String name) throws Exception {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Map<String, String> getValuesById(String id) throws Exception {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Map<String, String> getValuesByIdAndKeys(String id, String[] names)
			throws Exception {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public Map<String, Map<String, String>> getValuesByIdsAndKeys(String[] ids,
			String[] names) throws Exception {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();
	}

	@Override
	public void setValue(String id, String name, String value) throws Exception {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();

	}

	@Override
	public void setValues(String id, Map<String, String> values)
			throws Exception {
		// TODO Auto-generated method stub
		throw new java.lang.UnsupportedOperationException();

	}

	@Override
	public void clearAlertQueue() throws Exception {
		AlertManager.clearAlertQueue();
	}

	@Override
	public ACSAlertInformation getAlertQueueInfor(int index) throws Exception {
		return AlertManager.getAlertQueueInfor(index);
	}

	@Override
	public int getAlertQueueSize() throws Exception {
		return AlertManager.getAlertQueueSize();
	}

	@Override
	public List<ACSAlertInformation> getAlertQueueAllInfors() throws Exception {
		return AlertManager.getAllAlertQueueInfors();
	}

	@Override
	public ACSAlertInformation popupAlertQueueInfor() throws Exception {
		return AlertManager.popupAlertQueueInfor();
	}

	@Override
	public List<ACSAlertInformation> popupAlertInfors(int num) throws Exception {
		return AlertManager.popupAlertQueueInfors(num);
	}

	@Override
	public void pushAlertQueueInfor(ACSAlertInformation information)
			throws Exception {
		AlertManager.pushAlertQueueInfor(information);
	}

	@Override
	public void updateAlertQueueInfor(ACSAlertInformation information)
			throws Exception {
		AlertManager.updateAlertQueueInfor(information);
	}

	@Override
	public List<ACSAlertInformation> getAlertQueueNewInfors(String beginId,String[] deviceIds,DeviceIdStruct[] sdeviceIds)
			throws Exception {
		return AlertManager.getNewAlertQueueInfors(beginId,deviceIds,sdeviceIds);
	}

	@Override
	public List<ACSAlertInformation> getAlertLogHistroryByCondition(
			int pageNo, int length) throws Exception {
		return dao.getAlertInfors(pageNo, length);
	}

	@Override
	public long getAlertLogHistrorySize() throws Exception {
		return dao.getSize();
	}

	@Override
	public List<ACSAlertInformation> getAlertQueueInforsByCondition(int pageNo,
			int length) throws Exception {
		return AlertManager.getAlertQueueInforsByCondition(pageNo, length);
	}

	@Override
	public List<ACSAlertInformation> getAlertLogHistroryByConditionByDeviceId(
			int pageNo, int length, String realDeviceId,DeviceIdStruct sdeviceId) throws Exception {
		return dao.getAlertInforsByDeviceId(pageNo, length, realDeviceId,sdeviceId);
	}

	@Override
	public long getAlertLogHistrorySizeByDeviceId(String realDeviceId,DeviceIdStruct sdeviceId)
			throws Exception {
		return dao.getSizeByDeviceId(realDeviceId,sdeviceId);
	}

	@Override
	public void deleteAlertQueueInfor(String id) throws Exception {
		AlertManager.deleteAlertQueueInfor(id);
	}

	@Override
	public ACSAlertList queryAlertQueueInfors(String[] deviceids,DeviceIdStruct[] sdeviceIds,
			AlertStatus alertStatus, String begintime, String endtime, int pageNo,
			int length) throws Exception {
		return AlertManager.queryAlertQueueInfors(deviceids,sdeviceIds, alertStatus, begintime, endtime, pageNo, length);
	}

	@Override
	public void deleteAlertQueueInfors(String[] ids) throws Exception {
		AlertManager.deleteAlertQueueInfors(ids);
	}

	@Override
	public void deleteAlertLogHistoryInfors(String[] ids) throws Exception {
		dao.deleteAlertInfors(ids);
		this.deleteAlertQueueInfors(ids);
		
	}

	@Override
	public void deleteAlertHistoryInfor(String id) throws Exception {
		dao.deleteAlertInfor(id);
		this.deleteAlertQueueInfors(new String[]{id});
		
	}

	@Override
	public void deleteAlertLogHistoryInfor(String begin, String end) throws Exception {
		dao.deleteAlertInfor(begin, end);
		
	}

	@Override
	public List<String> getAlertLogHistoryAllIds() throws Exception {
		return dao.getAllIds();
	}

	@Override
	public List<String> getAlertLogHistroyIds(String begin, String end) throws Exception {
		return dao.getIds(begin, end);
	}

	@Override
	public ACSAlertInformation readAlertLogHistoryInfor(String id) throws Exception {
		return dao.read(id);
	}

	@Override
	public void saveAlertLogHistoryInfor(ACSAlertInformation infor) throws Exception {
		dao.save(infor);
	}

	@Override
	public ACSAlertList queryHistoryAlertInfors(String[] deviceids,DeviceIdStruct[] sdeviceIds,
			AlertStatus alertStatus, String begintime, String endtime, int pageNo,
			int length) throws Exception {
		return dao.queryHistoryAlertInfors(deviceids,sdeviceIds, alertStatus, begintime, endtime, pageNo, length);
	}

	@Override
	public ACSAlertInformation getAlertQueueInforById(String id)
			throws Exception {
		return AlertManager.getAlertQueueInforById(id);
	}

	@Override
	public int deleteHistoryAlertInfors(String[] deviceids,DeviceIdStruct[] sdeviceIds, AlertStatus alertStatus,
			String begintime, String endtime) throws Exception {
		return dao.deleteHistoryAlertInfors(deviceids,sdeviceIds, alertStatus, begintime, endtime);
	}

	@Override
	public Map<String, String> stateAlertQueueInfors(String name)
			throws Exception {
		Map<String, Integer> map = AlertManager.stateAlertQueueInfors(name);
		Map<String, String> retMap = new HashMap<String, String>();
		for (String key : map.keySet()){
			retMap.put(key, "" + map.get(key));
		}
		return retMap;
		
	}

	@Override
	public long queryAlertQueueInforsSize(String[] deviceids,DeviceIdStruct[] sdeviceIds,
			AlertStatus alertStatus, String begintime, String endtime)
			throws Exception {
		return AlertManager.queryAlertQueueInforsSize(deviceids,sdeviceIds, alertStatus, begintime, endtime);
	}

	
}
