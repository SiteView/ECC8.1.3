package com.siteview.cwmp.bean;

import java.io.Serializable;
import java.util.Map;

import org.ofbiz.entity.GenericValue;

import com.siteview.utils.AlertInforTools;

import cwmp_1_1.dslforum_org.DeviceIdStruct;

/**
 * 
 * @author Administrator
 *
 */
public class ACSAlertInformation implements Serializable{
	private final static long serialVersionUID = 5170082597823698759L;
	AlertStatus alertStatus = AlertStatus.NoProcess;
	String createDateTime = "";
	String revertDateTime = "";

	long count = 1;

	public void setCount(long count) {
		this.count = count;
	}
	public long getCount() {
		return count;
	}
	public void incCount() {
		this.count++;
	}
	public String getRevertDateTime() {
		return revertDateTime;
	}
	public void setRevertDateTime(String revertDateTime) {
		this.revertDateTime = revertDateTime;
	}
	DeviceIdStruct deviceId = null ;
	String event = "";
	String id = "";
	String note = "";
	String oui = "";
	String realDeviceId = "";
	public ACSAlertInformation()throws Exception{
	}
	public ACSAlertInformation(DeviceIdStruct deviceId, String event, String oui,String realDeviceId) throws Exception
	{
		this.setDeviceId(deviceId);
		this.setRealDeviceId(realDeviceId);
		this.setOui(oui);
		this.setEvent(event);
	}
	public ACSAlertInformation(GenericValue val) throws Exception{
		this(AlertInforTools.convert(val));
	}
	public ACSAlertInformation(Map<String,String> map) throws Exception{
		AlertInforTools.initData(this,map);
	}
	
	public String getCreateDateTime() {
		return createDateTime;
	}
	public DeviceIdStruct getDeviceId() {
		return deviceId;
	}
	public String getEvent() {
		return event;
	}
	public String getId() {
		return id;
	}
	public String getNote() {
		return note;
	}
	public String getOui() {
		return oui;
	}
	public String getRealDeviceId() {
		return realDeviceId;
	}
	public AlertStatus getAlertStatus() {
		return alertStatus;
	}
	public void setAlertStatus(AlertStatus alertStatus) {
		this.alertStatus = alertStatus;
	}
	public void setCreateDateTime(String createDateTime) {
		this.createDateTime = createDateTime;
	}
	public void setDeviceId(DeviceIdStruct deviceId) {
		this.deviceId = deviceId;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public void setOui(String oui) {
		this.oui = oui;
	}
	public void setRealDeviceId(String realDeviceId) {
		this.realDeviceId = realDeviceId;
	}
	
}


