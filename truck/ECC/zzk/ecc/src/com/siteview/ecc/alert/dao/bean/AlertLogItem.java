package com.siteview.ecc.alert.dao.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.siteview.ecc.alert.dao.type.AlertStatus;
import com.siteview.ecc.alert.dao.type.AlertType;

public class AlertLogItem {
	public final static SimpleDateFormat STRING_TO_DATE = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
    private String alertIndex;
    private String alertName;
    private AlertType alertType;
    private Date alertTime;
    private AlertStatus alertStatus;
    private String alertReceiver;
    private String entityName;
    private String monitorName;
    public void init(Map<String,String> map){
        this.setAlertIndex(map.get("_AlertIndex"));
        this.setAlertName(map.get("_AlertRuleName"));
        this.setEntityName(map.get("_DeviceName"));
        this.setMonitorName(map.get("_MonitorName"));
        this.setAlertReceiver(map.get("_AlertReceive"));

        String status = (String) map.get("_AlertStatus");
        this.setAlertStatus(AlertStatus.getType(status));
        
        try{
        	
            this.setAlertTime(STRING_TO_DATE.parse((String)map.get("_AlertTime")));
        }catch(Exception e){}
        
        
        String stye = (String) map.get("_AlertType");
        this.setAlertType(AlertType.getTypeByValue(stye));
    	
    }
	public String getAlertIndex() {
		return alertIndex;
	}
	public void setAlertIndex(String alertIndex) {
		this.alertIndex = alertIndex;
	}
	public String getAlertName() {
		return alertName;
	}
	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}
	public AlertType getAlertType() {
		return alertType;
	}
	public void setAlertType(AlertType alertType) {
		this.alertType = alertType;
	}
	public Date getAlertTime() {
		return alertTime;
	}
	public void setAlertTime(Date alertTime) {
		this.alertTime = alertTime;
	}
	public AlertStatus getAlertStatus() {
		return alertStatus;
	}
	public void setAlertStatus(AlertStatus alertStatus) {
		this.alertStatus = alertStatus;
	}
	public String getAlertReceiver() {
		return alertReceiver;
	}
	public void setAlertReceiver(String alertReceiver) {
		this.alertReceiver = alertReceiver;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getMonitorName() {
		return monitorName;
	}
	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}

}
