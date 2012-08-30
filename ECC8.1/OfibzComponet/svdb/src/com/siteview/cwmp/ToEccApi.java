package com.siteview.cwmp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javolution.util.FastMap;

import com.rabbitmq.client.Connection;
import com.siteview.cwmp.bean.ACSAlertInformation;
import com.siteview.cwmp.bean.ACSAlertList;
import com.siteview.cwmp.bean.AlertStatus;
import com.siteview.eccservice.RetMapInMap;
import com.siteview.mq.MQManager;
import com.siteview.mq.MQProxy;
import com.siteview.mq.RabbitProxy;
import com.siteview.utils.AlertInforTools;
import com.siteview.utils.FactoryTools;

import cwmp_1_1.dslforum_org.DeviceIdStruct;

public class ToEccApi {
	private static IMXSwitch imx = FactoryTools.getIMXSwitchImpl();
	/**
	 * 根据EccService的接口,呼叫响应的方法
	 * @param fmap 
	 * @param inwhat
	 * @param retValue
	 * @return
	 */
	public static boolean call(Map<String, Map<String, String>> fmap,Map<String, String> inwhat,Map<String,RetMapInMap> retValue){
		try{
			String dowhat = getStringValue(inwhat, "dowhat");
			if (dowhat == null) return false;
			fmap = new FastMap<String, Map<String, String>>();
			Map<String, String> retmap = null;
			if (dowhat.startsWith("AlertManager.")){
				dowhat = dowhat.replaceAll("AlertManager.", "");
				inwhat.remove("dowhat");
				if (dowhat.equals("getServerCurrentDate")){
					retmap = new FastMap<String, String>();
					retmap.put("return", AlertManager.SDF.format(new Date()));
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("clearAlertQueue")){
					imx.clearAlertQueue();
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("getAlertInfor")){
					ACSAlertInformation infor = imx.getAlertQueueInfor(getIntegerValue(inwhat, "index"));
					retmap = AlertInforTools.toMap(infor);
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("getAlertInforById")){
					ACSAlertInformation infor = imx.getAlertQueueInforById(getStringValue(inwhat, "id"));
					retmap = AlertInforTools.toMap(infor);
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("getNewAlertInfors")){
					String deviceids = inwhat.get("deviceids");
					if ("".equals(deviceids)) deviceids = null;
					String aCSdeviceIds = inwhat.get("ACSdeviceIds");
					if ("".equals(aCSdeviceIds)) aCSdeviceIds = null;
					List<ACSAlertInformation> infors = imx.getAlertQueueNewInfors(getStringValue(inwhat, "beginId"),getArray(deviceids),getDeviceIdStructs(aCSdeviceIds));
					for (ACSAlertInformation infor : infors){
						fmap.put(infor.getId(), AlertInforTools.toMap(infor));
					}
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("getAlertQueueSize")){
					int retInt = imx.getAlertQueueSize();
					retmap = new FastMap<String, String>();
					retmap.put("return", "" + retInt);
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("getAllAlertInfor")){
					List<ACSAlertInformation> infors = imx.getAlertQueueAllInfors();
					for (ACSAlertInformation infor : infors){
						fmap.put(infor.getId(), AlertInforTools.toMap(infor));
					}
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("popupAlertInfor")){
					ACSAlertInformation infor = imx.popupAlertQueueInfor();
					retmap = AlertInforTools.toMap(infor);
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("popupAlertInfors")){
					List<ACSAlertInformation> infors = imx.popupAlertInfors(getIntegerValue(inwhat, "num"));
					for (ACSAlertInformation infor : infors){
						fmap.put(infor.getId(), AlertInforTools.toMap(infor));
					}
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("queryAlertQueueInfors")){
					String strAlertStatus = inwhat.get("alertStatus");
					if ("".equals(strAlertStatus)) strAlertStatus = null;
					AlertStatus alertStatus = strAlertStatus==null ? null : AlertStatus.getAlertStatus(strAlertStatus);
					String deviceids = inwhat.get("deviceids");
					if ("".equals(deviceids)) deviceids = null;
					String aCSdeviceIds = inwhat.get("ACSdeviceIds");
					if ("".equals(aCSdeviceIds)) aCSdeviceIds = null;
					String begintime = inwhat.get("begintime");
					if ("".equals(begintime)) begintime = null;
					String endtime = inwhat.get("endtime");
					if ("".equals(endtime)) endtime = null;
					ACSAlertList infors = imx.queryAlertQueueInfors(getArray(deviceids),getDeviceIdStructs(aCSdeviceIds), alertStatus, begintime, endtime, getIntegerValue(inwhat, "pageNo"), getIntegerValue(inwhat, "length"));
					for (ACSAlertInformation infor : infors.getList()){
						fmap.put(infor.getId(), AlertInforTools.toMap(infor));
					}
					retmap = new FastMap<String, String>();
					retmap.put("AllSize", "" + infors.getAllSize());
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("queryAlertQueueInforsSize")){
					String strAlertStatus = inwhat.get("alertStatus");
					if ("".equals(strAlertStatus)) strAlertStatus = null;
					AlertStatus alertStatus = strAlertStatus==null ? null : AlertStatus.getAlertStatus(strAlertStatus);
					String deviceids = inwhat.get("deviceids");
					if ("".equals(deviceids)) deviceids = null;
					String aCSdeviceIds = inwhat.get("ACSdeviceIds");
					if ("".equals(aCSdeviceIds)) aCSdeviceIds = null;
					String begintime = inwhat.get("begintime");
					if ("".equals(begintime)) begintime = null;
					String endtime = inwhat.get("endtime");
					if ("".equals(endtime)) endtime = null;
					long retlong = imx.queryAlertQueueInforsSize(getArray(deviceids),getDeviceIdStructs(aCSdeviceIds), alertStatus, begintime, endtime);
					retmap = new FastMap<String, String>();
					retmap.put("return", "" + retlong);
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("queryHistoryAlertInfors")){
					String strAlertStatus = inwhat.get("alertStatus");
					if ("".equals(strAlertStatus)) strAlertStatus = null;
					AlertStatus alertStatus = strAlertStatus==null ? null : AlertStatus.getAlertStatus(strAlertStatus);
					String deviceids = inwhat.get("deviceids");
					if ("".equals(deviceids)) deviceids = null;
					String aCSdeviceIds = inwhat.get("ACSdeviceIds");
					if ("".equals(aCSdeviceIds)) aCSdeviceIds = null;
					String begintime = inwhat.get("begintime");
					if ("".equals(begintime)) begintime = null;
					String endtime = inwhat.get("endtime");
					if ("".equals(endtime)) endtime = null;
					ACSAlertList infors = imx.queryHistoryAlertInfors(getArray(deviceids),getDeviceIdStructs(aCSdeviceIds), alertStatus, begintime, endtime, getIntegerValue(inwhat, "pageNo"), getIntegerValue(inwhat, "length"));
					for (ACSAlertInformation infor : infors.getList()){
						fmap.put(infor.getId(), AlertInforTools.toMap(infor));
					}
					retmap = new FastMap<String, String>();
					retmap.put("AllSize", "" + infors.getAllSize());
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("deleteHistoryAlertInfors")){
					String strAlertStatus = inwhat.get("alertStatus");
					if ("".equals(strAlertStatus)) strAlertStatus = null;
					AlertStatus alertStatus = strAlertStatus==null ? null : AlertStatus.getAlertStatus(strAlertStatus);
					String deviceids = inwhat.get("deviceids");
					if ("".equals(deviceids)) deviceids = null;
					String aCSdeviceIds = inwhat.get("ACSdeviceIds");
					if ("".equals(aCSdeviceIds)) aCSdeviceIds = null;
					int retInt = imx.deleteHistoryAlertInfors(getArray(deviceids),getDeviceIdStructs(aCSdeviceIds), alertStatus, getStringValue(inwhat,"begintime"), getStringValue(inwhat,"endtime"));
					retmap = new FastMap<String, String>();
					retmap.put("return", "" + retInt);
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("updateAlertQueueInfor")){
					ACSAlertInformation information = new ACSAlertInformation(inwhat);
					imx.updateAlertQueueInfor(information);
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("stateAlertQueueInfors")){
					fmap.put("return", imx.stateAlertQueueInfors(getStringValue(inwhat,"name")));
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("deleteAlertQueueInfor")){
					imx.deleteAlertQueueInfor(getStringValue(inwhat, "id"));
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("deleteAlertQueueInfors")){
					imx.deleteAlertQueueInfors(getArray(getStringValue(inwhat, "ids")));
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("saveAlertInforToDB")){
					ACSAlertInformation information = new ACSAlertInformation(inwhat);
					imx.saveAlertLogHistoryInfor(information);
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("getHistoryAlertInfor")){
					ACSAlertInformation infor =  imx.readAlertLogHistoryInfor(getStringValue(inwhat, "id"));
					retmap = AlertInforTools.toMap(infor);
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("getHistoryAlertInforIds")){
					List<String> ids = imx.getAlertLogHistroyIds(getStringValue(inwhat, "begin"), getStringValue(inwhat, "end"));
					retmap = new FastMap<String, String>();
					for (String id : ids){
						retmap.put(id, id);
					}
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("deleleHistoryAlertInfor")){
					imx.deleteAlertLogHistoryInfor(getStringValue(inwhat, "begin"), getStringValue(inwhat, "end"));
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("deleleHistoryAlertInforById")){
					imx.deleteAlertHistoryInfor(getStringValue(inwhat, "Id"));
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("deleleHistoryAlertsInforByIds")){
					imx.deleteAlertLogHistoryInfors(getArray(getStringValue(inwhat, "ids")));
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("getAlertLogHistrorySize")){
					long retLong = imx.getAlertLogHistrorySize();
					retmap = new FastMap<String, String>();
					retmap.put("return", "" + retLong);
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("getAlertLogHistroryByCondition")){
					List<ACSAlertInformation> infors = imx.getAlertLogHistroryByCondition(getIntegerValue(inwhat, "pageNo"), getIntegerValue(inwhat, "length"));
					for (ACSAlertInformation infor : infors){
						fmap.put(infor.getId(), AlertInforTools.toMap(infor));
					}
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("getAlertLogHistrorySizeByDeviceId")){
					String deviceid = inwhat.get("realDeviceId");
					if ("".equals(deviceid)) deviceid = null;
					String aCSdeviceId = inwhat.get("ACSdeviceId");
					if ("".equals(aCSdeviceId)) aCSdeviceId = null;
					long retLong = imx.getAlertLogHistrorySizeByDeviceId(deviceid,getDeviceIdStruct(aCSdeviceId));
					retmap = new FastMap<String, String>();
					retmap.put("return", "" + retLong);
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("getAlertLogHistroryByDeviceId")){
					String deviceid = inwhat.get("realDeviceId");
					if ("".equals(deviceid)) deviceid = null;
					String aCSdeviceId = inwhat.get("ACSdeviceId");
					if ("".equals(aCSdeviceId)) aCSdeviceId = null;
					List<ACSAlertInformation> infors = imx.getAlertLogHistroryByConditionByDeviceId(getIntegerValue(inwhat, "pageNo"), getIntegerValue(inwhat, "length"),deviceid,getDeviceIdStruct(aCSdeviceId));
					for (ACSAlertInformation infor : infors){
						fmap.put(infor.getId(), AlertInforTools.toMap(infor));
					}
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("getAlertQueueInforsByCondition")){
					List<ACSAlertInformation> infors = imx.getAlertQueueInforsByCondition(getIntegerValue(inwhat, "pageNo"), getIntegerValue(inwhat, "length"));
					for (ACSAlertInformation infor : infors){
						fmap.put(infor.getId(), AlertInforTools.toMap(infor));
					}
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("getConfigInformation")){
					MQProxy proxy = MQManager.getProxy();
					if (proxy != null && proxy instanceof RabbitProxy){
						Connection connection = ((RabbitProxy)proxy).getConnection();
						retmap = new FastMap<String, String>();
						retmap.put("RabbitServerHostName", connection.getHost());
						retmap.put("RabbitServerPort", "" + connection.getPort());
						retmap.put("RabbitExchangeName", ((RabbitProxy)proxy).getExchangeName());
						retmap.put("RabbitQueueName", ((RabbitProxy)proxy).getQueueName());
					}
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}else if (dowhat.equals("setConfigInformation")){
					MQManager.getProxy(getStringValue(inwhat, "RabbitServerHostName"), getStringValue(inwhat, "RabbitServerPort"), getStringValue(inwhat, "RabbitExchangeName"), getStringValue(inwhat, "RabbitQueueName"), true);
					retValue.put("return", getReturnMap(retmap,fmap));
					return true;
				}
			}
			return false;
		}catch(Exception e){
			e.printStackTrace();
			retValue.put("return", new RetMapInMap(false,e.getMessage() + e.getCause() != null ? e.getCause().getMessage() : "",fmap));
			return true;
		}
	}
	
	/**
	 * 检查参数是否完备
	 * @param map 参数集合
	 * @param key 需要的参数名称
	 * @return 参数的值
	 * @throws Exception
	 */
	private static String getStringValue(Map<String,String> map,String key) throws Exception{
		String value = map.get(key);
		if (value == null) throw new Exception("0001:参数:" + key + "没有被赋值！现在传的值key有：" + map.keySet());
		return value;
	}
	
	private static Integer getIntegerValue(Map<String,String> map,String key) throws Exception{
		try{
			return new Integer(getStringValue(map,key));
		}catch(Exception e){
			throw new Exception("0002:参数:" + key + "不为Integer类型！");
		}
	}
	
	private static RetMapInMap getReturnMap(Map<String, String> retmap,Map<String, Map<String, String>> fmap)
	{
		if (retmap!=null) fmap.put("return", retmap);
		return new RetMapInMap(true,"",fmap);
	}
	private static String[] getArray(String values)
	{
		if(values==null) return null;
		return values.split(",");
	}
	private static DeviceIdStruct getDeviceIdStruct(String values){
		if(values==null) return null;
		String[] array = values.split("#");
		if (array.length >= 4) {
			DeviceIdStruct retDeviceIdStruct = new DeviceIdStruct();
			retDeviceIdStruct.setManufacturer(array[0]);
			retDeviceIdStruct.setOUI(array[1]);
			retDeviceIdStruct.setProductClass(array[2]);
			retDeviceIdStruct.setSerialNumber(array[3]);
			return retDeviceIdStruct;
		}
		return null;
	}
	private static DeviceIdStruct[] getDeviceIdStructs(String values){
		List<DeviceIdStruct> retlist = new ArrayList<DeviceIdStruct>();
		if(values==null) return retlist.toArray(new DeviceIdStruct[retlist.size()]);
		String[] list = getArray(values);
		for (String value : list){
			if ("".equals(value)) continue;
			DeviceIdStruct deviceIdStruct = getDeviceIdStruct(value);
			if (deviceIdStruct!=null)retlist.add(deviceIdStruct);
		}
		return retlist.toArray(new DeviceIdStruct[retlist.size()]);
	}
}
