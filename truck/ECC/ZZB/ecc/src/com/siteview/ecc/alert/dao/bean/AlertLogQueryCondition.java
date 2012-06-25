package com.siteview.ecc.alert.dao.bean;

import java.util.Date;

import com.siteview.ecc.alert.dao.type.AlertStatus;
import com.siteview.ecc.alert.dao.type.AlertType;

public class AlertLogQueryCondition {
    private String alertIndex;
    private String alertName;
    private String alertReceiver;
    private AlertStatus alertStatus;
    private AlertType alertType;
    private Date endTime;
    private String entityName;
    private boolean limitEntity;
    private boolean limitIndex;
    private boolean limitMonitor;
    private boolean limitName;
    private boolean limitReceiver;
    private boolean limitStatus;
    private boolean limitTime = true;
    private boolean limitType;
    private String monitorName;
	private Date startTime;
	public String getAlertIndex() {
		return alertIndex;
	}
	public String getAlertName() {
		return alertName;
	}
	public String getAlertReceiver() {
		return alertReceiver;
	}
	public AlertStatus getAlertStatus() {
		return alertStatus;
	}
	public AlertType getAlertType() {
		return alertType;
	}
	public Date getEndTime() {
		return endTime;
	}
	public String getEntityName() {
		return entityName;
	}
	public boolean getLimitEntity() {
		return limitEntity;
	}
	public boolean getLimitIndex() {
		return limitIndex;
	}
	public boolean getLimitMonitor() {
		return limitMonitor;
	}
	public boolean getLimitName() {
		return limitName;
	}
	public boolean getLimitReceiver() {
		return limitReceiver;
	}
	public boolean getLimitStatus() {
		return limitStatus;
	}
	public boolean getLimitTime() {
		return limitTime;
	}
	public boolean getLimitType() {
		return limitType;
	}
	public String getMonitorName() {
		return monitorName;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setAlertIndex(String alertIndex) {
		this.alertIndex = alertIndex;
	}
	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}
	public void setAlertReceiver(String alertReceiver) {
		this.alertReceiver = alertReceiver;
	}
	public void setAlertStatus(AlertStatus alertStatus) {
		this.alertStatus = alertStatus;
	}
	public void setAlertType(AlertType alertType) {
		this.alertType = alertType;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public void setLimitEntity(boolean limitEntity) {
		this.limitEntity = limitEntity;
	}
	public void setLimitIndex(boolean limitIndex) {
		this.limitIndex = limitIndex;
	}
	public void setLimitMonitor(boolean limitMonitor) {
		this.limitMonitor = limitMonitor;
	}
	public void setLimitName(boolean limitName) {
		this.limitName = limitName;
	}
	public void setLimitReceiver(boolean limitReceiver) {
		this.limitReceiver = limitReceiver;
	}
	public void setLimitStatus(boolean limitStatus) {
		this.limitStatus = limitStatus;
	}
	public void setLimitTime(boolean limitTime) {
		this.limitTime = limitTime;
	}
	public void setLimitType(boolean limitType) {
		this.limitType = limitType;
	}
	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

}
