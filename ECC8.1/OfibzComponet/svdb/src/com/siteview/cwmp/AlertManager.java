package com.siteview.cwmp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.ofbiz.base.util.Debug;

import com.ibm.icu.util.Calendar;
import com.siteview.cwmp.bean.ACSAlertInformation;
import com.siteview.cwmp.bean.ACSAlertList;
import com.siteview.cwmp.bean.AlertStatus;
import com.siteview.mq.MQManager;
import com.siteview.utils.AlertInforTools;
import com.siteview.utils.FactoryTools;

import cwmp_1_1.dslforum_org.DeviceIdStruct;

/**
 * 
 * ACS - 报警队列管理
 * @author hailong.yi
 *
 */
public class AlertManager {
	private static int interval = 24;
	private static String intervalType = null;
	public static int getInterval() {
		return 0 - interval;
	}
	public static void setInterval(String interval) {
		try{
			AlertManager.interval = Integer.parseInt(interval);
		}catch(Exception e){
			AlertManager.interval = 24;
		}
	}


	public static int getIntervalType() {
		if ("HOUR".equals(intervalType)) return Calendar.HOUR;
		if ("MINUTE".equals(intervalType)) return Calendar.MINUTE;
		return Calendar.HOUR;
	}


	public static void setIntervalType(String intervalType) {
		AlertManager.intervalType = intervalType;
	}

	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	private static final String module = AlertManager.class.getName();
	private static final AlertInformationDAO dao = FactoryTools.getAlertInformationDAOImpl();
	private static final QueueManager<ACSAlertInformation> queue = FactoryTools.getAlertQueueManagerImpl();

	private static final Thread queueManagerThread = new QueueManagerThread();
	static{
		queueManagerThread.start();
	}
	
	public static void clearAlertQueue() throws Exception {
		queue.clear();
		Map<String, String> map = new HashMap<String, String>();
		map.put("action", "clearAlertQueue");
		MQManager.getProxy().send(AlertInforTools.getCompanyIdByDeviceId(null), map);
	}

	
	public static ACSAlertInformation getAlertQueueInfor(int index) throws Exception {
		return queue.get(index);
	}

	public static ACSAlertInformation getAlertQueueInforById(String id) throws Exception {
		if (id==null) throw new Exception("0007:ID 是空");
		List<ACSAlertInformation> all = getAllAlertQueueInfors();
		for (ACSAlertInformation infor : all){
			if (infor==null) continue;
			if (id.equals(infor.getId())){
				return infor;
			}
		}
		return null;
	}
	
	public static List<ACSAlertInformation> getAllAlertQueueInfors() throws Exception {
		return queue.getAll();
	}

	
	public static int getAlertQueueSize() throws Exception {
		return queue.getSize();
	}

	
	public static ACSAlertInformation popupAlertQueueInfor() throws Exception {
		ACSAlertInformation infor = queue.popup();
		Map<String, String> map = AlertInforTools.toMap(infor);
		infor.setId(null);
		dao.save(infor);
		map.put("action", "popupAlertQueueInfor");
		MQManager.getProxy().send(AlertInforTools.getCompanyIdByDeviceId(infor.getRealDeviceId()), map);
		return infor;
	}

	
	public static List<ACSAlertInformation> popupAlertQueueInfors(int num) throws Exception {
		List<ACSAlertInformation> retlist = new ArrayList<ACSAlertInformation>();
		for (int i = 0 ; i< num ; i++){
			retlist.add(popupAlertQueueInfor());
		}
		return retlist;
	}

	public static List<ACSAlertInformation> getNewAlertQueueInfors(String beginId,String[] deviceIds,DeviceIdStruct[] sdeviceIds) throws Exception {
		if (beginId==null) throw new Exception("0005:beginId 是空");
		List<ACSAlertInformation> all = getAllAlertQueueInfors();
		List<ACSAlertInformation> retlist = new ArrayList<ACSAlertInformation>();
		for (ACSAlertInformation infor : all){
			if (infor==null) continue;
			if (beginId.equals(infor.getId())){
				int beginindex = all.indexOf(infor);
				List<ACSAlertInformation> records = queue.getNewRecords(beginindex);
				for (ACSAlertInformation record : records){
					if ( isOK(deviceIds,sdeviceIds,null,null,null,record) == false ) continue;
					retlist.add(record);
				}
				break;
			}
		}
		return retlist;
	}
	public static void updateAlertQueueInfor(ACSAlertInformation information) throws Exception {
		if (information==null) throw new Exception("0006:告警信息是空");
		String id = information.getId();
		if (id==null) throw new Exception("0007:ID 是空");
		List<ACSAlertInformation> all = getAllAlertQueueInfors();
		for (ACSAlertInformation infor : all){
			if (infor==null) continue;
			if (id.equals(infor.getId())){
				AlertInforTools.initData(infor,AlertInforTools.toMap(information));
				if (AlertStatus.Processed.equals(infor.getAlertStatus())){
					Map<String, String> map = AlertInforTools.toMap(infor);
					infor.setId(null);
					dao.save(infor);
					queue.remove(all.indexOf(infor));
					map.put("action", "updateAlertQueueInfor.toHistory");
					MQManager.getProxy().send(AlertInforTools.getCompanyIdByDeviceId(infor.getRealDeviceId()), map);
				}else{
					Map<String, String> map = AlertInforTools.toMap(infor);
					map.put("action", "updateAlertQueueInfor");
					MQManager.getProxy().send(AlertInforTools.getCompanyIdByDeviceId(infor.getRealDeviceId()), map);
					//all.remove(infor);
				}
				return;
			}
		}
	}
	
	public static void deleteAlertQueueInfor(String id)throws Exception{
		if (id==null) throw new Exception("0007:ID 是空");
		List<ACSAlertInformation> all = getAllAlertQueueInfors();
		for (ACSAlertInformation infor : all){
			if (infor==null) continue;
			if (id.equals(infor.getId())){
				all.remove(infor);
				Map<String, String> map = AlertInforTools.toMap(infor);
				map.put("action", "deleteAlertQueueInfor");
				MQManager.getProxy().send(AlertInforTools.getCompanyIdByDeviceId(infor.getRealDeviceId()), map);
				return;
			}
		}
	}
	public static void deleteAlertQueueInfors(String[] ids)throws Exception{
		for (String id : ids){
			if (id == null) continue;
			deleteAlertQueueInfor(id);
		}
	}
	public static ACSAlertList queryAlertQueueInfors(String[] deviceids,DeviceIdStruct[] sdeviceIds,AlertStatus alertStatus,String begintime,String endtime,int pageNo,int length)throws Exception{
		
		if ( begintime != null && endtime == null ) throw new Exception("0008:endtime 是空");
		if ( begintime == null && endtime != null ) throw new Exception("0009:begintime 是空");
		if (deviceids == null && sdeviceIds == null && alertStatus == null && begintime == null && endtime == null)
			throw new Exception("0010:请输入查询条件");
		
		List<ACSAlertInformation> all = getAllAlertQueueInfors();
		List<ACSAlertInformation> retlist = new ArrayList<ACSAlertInformation>();
		for (ACSAlertInformation infor : all){
			if (infor==null) continue;
			if ( isOK(deviceids,sdeviceIds,alertStatus,begintime,endtime,infor) == false ) continue;
			retlist.add(infor);
		}
		ACSAlertList ret = new ACSAlertList();
		ret.setList(getAlertInforsByCondition(retlist,pageNo,length));
		ret.setAllSize(retlist.size());
		return ret;
	}
	public static long queryAlertQueueInforsSize(String[] deviceids,DeviceIdStruct[] sdeviceIds,AlertStatus alertStatus,String begintime,String endtime)throws Exception{
		
		if ( begintime != null && endtime == null ) throw new Exception("0008:endtime 是空");
		if ( begintime == null && endtime != null ) throw new Exception("0009:begintime 是空");
		if (deviceids == null && sdeviceIds == null && alertStatus == null && begintime == null && endtime == null)
			throw new Exception("0010:请输入查询条件");
		List<ACSAlertInformation> all = getAllAlertQueueInfors();
		List<ACSAlertInformation> retlist = new ArrayList<ACSAlertInformation>();
		for (ACSAlertInformation infor : all){
			if (infor==null) continue;
			if ( isOK(deviceids,sdeviceIds,alertStatus,begintime,endtime,infor) == false ) continue;
			retlist.add(infor);
		}
		return retlist.size();
	}
	public static Map<String,Integer> stateAlertQueueInfors(String name)throws Exception{
		
		if ( name != null && name == null ) throw new Exception("0010:name 是空");
		
		List<ACSAlertInformation> all = getAllAlertQueueInfors();
		Map<String,Integer> map = new HashMap<String,Integer>();
		for (ACSAlertInformation infor : all){
			if (infor==null) continue;
			if ("RealDeviceId".equals(name)){
				Integer num = map.get(infor.getRealDeviceId());
				if (num == null) {
					num = 1;
				}else{
					num ++;
				}
				map.put(infor.getRealDeviceId(), num);
			}else if ("AlertStatus".equals(name)){
				Integer num = map.get(infor.getAlertStatus());
				if (num == null) {
					num = 1;
				}else{
					num ++;
				}
				map.put(infor.getAlertStatus().toString(), num);
			}else if ("ACSdeviceId".equals(name)){
				Integer num = map.get(infor.getDeviceId().toString());
				if (num == null) {
					num = 1;
				}else{
					num ++;
				}
				map.put(infor.getDeviceId().toString(), num);
			}
		}
		return map;
	}
	
	private static boolean isOK(String[] deviceids,DeviceIdStruct[] sdeviceIds,AlertStatus alertStatus,String begintime,String endtime,ACSAlertInformation infor)
	{
			
		if (deviceids!=null){
			boolean isExist = false;
			for (String deviceid : deviceids)
				if (deviceid.equals(infor.getRealDeviceId()) == true ){
					isExist = true;
					break;
				}
			if (!isExist) return false;
		}
		if (sdeviceIds!=null){
			boolean isExist = false;
			for (DeviceIdStruct deviceid : sdeviceIds)
				if (deviceid.toString().equals(infor.getDeviceId().toString()) == true ){
					isExist = true;
					break;
				}
			if (!isExist) return false;
		}
		if (alertStatus!=null){
			if (alertStatus.equals(infor.getAlertStatus()) == false ) return false;
		}
		if (begintime!=null && endtime!=null){
			if (begintime.compareTo(infor.getCreateDateTime()) > 0 ) return false;
			if (endtime.compareTo(infor.getCreateDateTime()) < 0 ) return false;
		}
		return true;
	}
	
	private  static int id = 0;
	public static void pushAlertQueueInfor(ACSAlertInformation information) throws Exception {
		proccessAlertQueueInfor(information);
		try{
			queue.push(information);
		}catch(Exception e){
	        Debug.logError(e.getMessage(), module);
	        popupAlertQueueInfor();
			queue.push(information);
		}finally{
			information.setId("" + id);
			id ++;
			information.setCreateDateTime(SDF.format(new Date()));
		}
		Map<String, String> map = AlertInforTools.toMap(information);
		map.put("action", "pushAlertQueueInfor");
		MQManager.getProxy().send(AlertInforTools.getCompanyIdByDeviceId(information.getRealDeviceId()), map);
	}
	private static final String APP_EXIT = "AppExit";
	private static final String APP_RESTART = "AppRestart";
	private static final String DS1_DOWN = "DS1Down";
	private static final String DS1_UP = "DS1Up";
	private static final String ISDN_DOWN = "ISDNDown";
	private static final String ISDN_UP = "ISDNUp";
	private static final String REG_FAIL = "RegFail";
	private static final String REG_OK = "RegOk";
	
	private static void proccessAlertQueueInfor(ACSAlertInformation information)throws Exception{
		if (information.getEvent() == null) throw new Exception("no event field");
		if (information.getEvent().contains(APP_EXIT) ||
				information.getEvent().contains(DS1_DOWN) ||
				information.getEvent().contains(ISDN_DOWN) ||
				information.getEvent().contains(REG_FAIL) 
				){
			if (information.getEvent().contains(REG_FAIL)){
				List<ACSAlertInformation> list = getAlertQueueInforsByCondition(information.getDeviceId(),AlertStatus.NoProcess,information.getEvent());
				for (ACSAlertInformation info : list){
					info.incCount();
					Map<String, String> map = AlertInforTools.toMap(info);
					map.put("action", "proccessAlertQueueInfor.REG_FAIL");
					MQManager.getProxy().send(AlertInforTools.getCompanyIdByDeviceId(info.getRealDeviceId()), map);
					throw new Exception("111:丢弃处理！" + information.getEvent());
				}
			}
			information.setAlertStatus(AlertStatus.NoProcess);
			return;
		}
		if (information.getEvent().contains(APP_RESTART) ||
				information.getEvent().contains(DS1_UP) ||
				information.getEvent().contains(ISDN_UP) ||
				information.getEvent().contains(REG_OK) 
				){
			String queryEvent = information.getEvent();
			queryEvent = queryEvent.replaceAll(APP_RESTART, APP_EXIT);
			queryEvent = queryEvent.replaceAll(DS1_UP, DS1_DOWN);
			queryEvent = queryEvent.replaceAll(ISDN_UP, ISDN_DOWN);
			queryEvent = queryEvent.replaceAll(REG_OK, REG_FAIL);
			List<ACSAlertInformation> list = getAlertQueueInforsByCondition(information.getDeviceId(),AlertStatus.NoProcess,queryEvent);
			for (ACSAlertInformation infor : list){
				infor.setAlertStatus(AlertStatus.Revert);
				infor.setRevertDateTime(SDF.format(new Date()));
				Map<String, String> map = AlertInforTools.toMap(infor);
				map.put("action", "proccessAlertQueueInfor.Revert");
				MQManager.getProxy().send(AlertInforTools.getCompanyIdByDeviceId(infor.getRealDeviceId()), map);
			}
			if (list.isEmpty()){
				if (information.getEvent().contains(REG_OK)){
					throw new Exception("111:丢弃处理！" + information.getEvent());
				}
				information.setAlertStatus(AlertStatus.Warning);
			}else{
				throw new Exception("111:丢弃处理！" + information.getEvent());
			}
		}
	}
	private static List<ACSAlertInformation> getAlertQueueInforsByCondition(DeviceIdStruct deviceId,AlertStatus alertStatus,String event)throws Exception
	{
		List<ACSAlertInformation> all = getAllAlertQueueInfors();
		List<ACSAlertInformation> retlist = new ArrayList<ACSAlertInformation>();
		for (ACSAlertInformation infor : all){
			if (infor==null) continue;
			if (deviceIdEquals(deviceId,infor.getDeviceId()) && alertStatus.equals(infor.getAlertStatus()) && event.equals(infor.getEvent())){
				retlist.add(infor);
			}
		}
		return retlist;
	}

	private static boolean deviceIdEquals(DeviceIdStruct deviceId1,DeviceIdStruct deviceId2){
		if (deviceId1.getManufacturer().equals(deviceId2.getManufacturer())
				&&		deviceId1.getOUI().equals(deviceId2.getOUI())
				&&		deviceId1.getProductClass().equals(deviceId2.getProductClass())
				&&		deviceId1.getSerialNumber().equals(deviceId2.getSerialNumber())
		) return true;
		return false;
	}
	public static List<ACSAlertInformation> getAlertQueueInforsByCondition(int pageNo,int length)throws Exception
	{
		return getAlertInforsByCondition(getAllAlertQueueInfors(),pageNo,length);
	}

	private static List<ACSAlertInformation> getAlertInforsByCondition(List<ACSAlertInformation> list ,int pageNo,int length)throws Exception
	{
		//总的记录数：
		int totalsize = list.size();

		int start = totalsize - (length * pageNo) - 1; 

		List<ACSAlertInformation> retlist = new ArrayList<ACSAlertInformation>();
		
		if (start<0) return retlist;
		
		for (int index = start ; index > (start - length)  ; index--){
			if (index<0)break;
			retlist.add(list.get(index));
		}
		return retlist;
	}
	
	public static ACSAlertInformation getOldest()throws Exception
	{
		return queue.getOldest();
	}

}

class QueueManagerThread extends Thread{
	private static final String module = QueueManagerThread.class.getName();
	public QueueManagerThread(){
		this.setName("Queue 24 hours proccess thread");
	}
	public void run(){
		while(true){
			try {
				sleep(proccess());  //休息
			} catch (Exception e) {
				Debug.logError(e, module);
				try {
					sleep(1000 * 10);
				} catch (InterruptedException e1) {
				}
			}
			
		}
	}
	/*
	//间隔一定时间，处理告警日志
	private long proccess() throws Exception{
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(AlertManager.getIntervalType(), AlertManager.getInterval());
		while(true){
			ACSAlertInformation infor = AlertManager.getOldest();
			if (infor == null) return 1000 * 10;
			Date createDate = AlertManager.SDF.parse(infor.getCreateDateTime());
			if (cal.getTime().compareTo(createDate) > 0){
				AlertManager.popupAlertQueueInfor();
			}else{
				return createDate.getTime() - cal.getTimeInMillis();
			}
		}
	}
	*/
	private Timer timer = new Timer();
	//每天0时处理告警日志
	private long proccess() throws Exception{
		Calendar cal = Calendar.getInstance(new Locale("zh","CN"));
		//cal.setTime(new Date());
		cal.add(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Debug.logError("process:" + cal.getTime(), module);
		timer.schedule(new MyTimerTask(), cal.getTime());
		return (1000 * 60 * 60 * 24);
	}
}

class MyTimerTask extends TimerTask{
	public static final String module = MyTimerTask.class.getName();
	@Override
	public void run() {
		Date now = new Date();
			try{
				while(true){
					Debug.logError(now.toString(), module);
					ACSAlertInformation infor = AlertManager.getOldest();
					if (infor == null) return;
					Date createDate = AlertManager.SDF.parse(infor.getCreateDateTime());
					if (now.compareTo(createDate) > 0){
						Debug.logError("popupAlertQueueInfor", module);
						AlertManager.popupAlertQueueInfor();
					}else{
						return;
					}
				}
			}catch(Exception e){
				Debug.logError(e, module);
			}
	}
	
}



