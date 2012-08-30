package com.siteview.utils;

import java.util.HashMap;
import java.util.Map;

import javolution.util.FastMap;

import org.ofbiz.base.util.Debug;
import org.ofbiz.entity.GenericValue;

import com.siteview.cwmp.bean.ACSAlertInformation;
import com.siteview.cwmp.bean.AlertStatus;
import com.siteview.jsvapi.Jsvapi;

import cwmp_1_1.dslforum_org.DeviceIdStruct;

public class AlertInforTools {
	private final static String module = AlertInforTools.class.getName();
	private final static Jsvapi svapi = new Jsvapi();
	/**
	 * 将该bean的信息转化成map
	 * @return 转化后的map
	 */
	public static Map<String,String> toMap(ACSAlertInformation infor){
		Map<String,String> retMap = new FastMap<String,String>();
		retMap.put("Event", infor.getEvent());
		retMap.put("Oui", infor.getOui());
		if (infor.getDeviceId() != null){
			retMap.put("deviceId.Manufacturer", infor.getDeviceId().getManufacturer());
			retMap.put("deviceId.OUI", infor.getDeviceId().getOUI());
			retMap.put("deviceId.ProductClass", infor.getDeviceId().getProductClass());
			retMap.put("deviceId.SerialNumber", infor.getDeviceId().getSerialNumber());
		}
		retMap.put("AlertStatus", infor.getAlertStatus().toString());
		retMap.put("Note", infor.getNote());
		retMap.put("Id", infor.getId());
		retMap.put("RealDeviceId", infor.getRealDeviceId());
		retMap.put("CreateDateTime", infor.getCreateDateTime());
		retMap.put("RevertDateTime", infor.getRevertDateTime());
		retMap.put("Count", "" + infor.getCount());

		return retMap;
	}
	/**
	 * 根据map内容初始化bean
	 * @param map
	 * @throws Exception 
	 */
	public static void initData(ACSAlertInformation infor,Map<String,String> map) throws Exception{
		infor.setEvent(map.get("Event"));
		infor.setOui(map.get("Oui"));
		if (infor.getDeviceId() == null){
			infor.setDeviceId(new DeviceIdStruct());
		}
		infor.getDeviceId().setManufacturer(map.get("deviceId.Manufacturer"));
		infor.getDeviceId().setOUI(map.get("deviceId.OUI"));
		infor.getDeviceId().setProductClass(map.get("deviceId.ProductClass"));
		infor.getDeviceId().setSerialNumber(map.get("deviceId.SerialNumber"));
		infor.setRealDeviceId(map.get("RealDeviceId"));
		infor.setAlertStatus(AlertStatus.getAlertStatus(map.get("AlertStatus")));
		infor.setNote(map.get("Note"));
		infor.setId(map.get("Id"));
		infor.setCreateDateTime(map.get("CreateDateTime"));
		infor.setRevertDateTime(map.get("RevertDateTime"));
		try{
			infor.setCount(Long.parseLong(map.get("Count")));
		}catch(Exception e){
			
		}
	}
	public static Map<String,String> convert(GenericValue genericvalue){
		Map<String,String> map = new FastMap<String,String>();
		map.put("deviceId.Manufacturer", genericvalue.getString("deviceIdManufacturer"));
		map.put("deviceId.OUI", genericvalue.getString("deviceIdOUI"));
		map.put("deviceId.ProductClass", genericvalue.getString("deviceIdProductClass"));
		map.put("deviceId.SerialNumber", genericvalue.getString("deviceIdSerialNumber"));
		
		map.put("Note", genericvalue.getString("Note"));
		map.put("AlertStatus", genericvalue.getString("AlertStatus"));
		map.put("Oui", genericvalue.getString("Oui"));
		map.put("Event", genericvalue.getString("Event"));
		map.put("RealDeviceId", genericvalue.getString("RealDeviceId"));
		map.put("Id", genericvalue.getString("Id"));
		map.put("CreateDateTime", genericvalue.getString("CreateDateTime"));
		map.put("RevertDateTime", genericvalue.getString("RevertDateTime"));

		map.put("Count", "" + genericvalue.getLong("Count"));
		return map;
	}
	
	public static void initGenericValue(GenericValue genericvalue , Map<String,String> map){
		genericvalue.set("deviceIdManufacturer", map.get("deviceId.Manufacturer"));
		genericvalue.set("deviceIdOUI", map.get("deviceId.OUI"));
		genericvalue.set("deviceIdProductClass", map.get("deviceId.ProductClass"));
		genericvalue.set("deviceIdSerialNumber", map.get("deviceId.SerialNumber"));
		
		genericvalue.set("Id", map.get("Id"));
		genericvalue.set("Note", map.get("Note"));
		genericvalue.set("AlertStatus", map.get("AlertStatus"));
		genericvalue.set("Oui", map.get("Oui"));
		genericvalue.set("Event", map.get("Event"));
		genericvalue.set("RealDeviceId", map.get("RealDeviceId"));
		genericvalue.set("CreateDateTime", map.get("CreateDateTime"));
		genericvalue.set("RevertDateTime", map.get("RevertDateTime"));

		try{
			genericvalue.set("Count", Long.parseLong(map.get("Count")));
		}catch(Exception e){
			
		}
		
	}
	
	
	
	/**
	 * 通过 DeviceIdStructs 取真实的设备 ID
	 * @param deviceId 设备DeviceIdStructs信息
	 * @return 真实的设备 ID
	 */
	public static String getRealDeviceIdByDeviceId(DeviceIdStruct deviceId) throws Exception{
        StringBuilder estr = new StringBuilder();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("dowhat", "getAlertID");
        parameters.put("manufacturer", deviceId.getManufacturer());
        parameters.put("oui", deviceId.getOUI());
        parameters.put("productClass", deviceId.getProductClass());
        parameters.put("serialNumber", deviceId.getSerialNumber());
        Map<String, Map<String, String>> fmap = new FastMap<String, Map<String, String>>();
        boolean ret = svapi.getUnivData(fmap,parameters,  estr);
        if (ret == false) {
            Debug.logError(estr.toString(), module);
        	throw new Exception("真实设备ID取不到:" + estr.toString());
        }
        Map<String,String> map = fmap.get("return");
		if (map == null){
			throw new Exception("真实设备ID取不到");
		}
		return map.get("alertID");
	}
	
	public static String getCompanyIdByDeviceId(String deviceid){
		return "eccqueues";
	}
}
