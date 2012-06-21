package com.siteview.ecc.monitorbrower;

import org.apache.log4j.Logger;

public class MonitorBean {
	private final static Logger logger = Logger.getLogger(MonitorBean.class);

	private String monitorId;

	private String status;
	
	private String group;
	
	private String entity;
	
	private String monitorName;
	
	private String updateTime;
	
	private double errorPersent=0;
	
	public double getErrorPersent() {
		return errorPersent;
	}

	public void setErrorPersent(double errorPersent) {
		this.errorPersent = errorPersent;
	}

	public String getMonitorType() {
		return monitorType;
	}

	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}

	private String monitorType;
	
	public String getMonitorId() {
		return monitorId;
	}

	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}

	private String descript;

	public MonitorBean(){}
	
	public MonitorBean(String monitorId, String status, String group, String entity,
			String monitorName, String updateTime, String descript,String monitorType) {
		super();
		this.monitorId = monitorId;
		this.status = status;
		this.group = group;
		this.entity = entity;
		this.monitorName = monitorName;
		this.updateTime = updateTime;
		this.descript = descript;
		this.monitorType = monitorType;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public String getMonitorName() {
		return monitorName;
	}

	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}
	
	public boolean equals(Object obj){
		if(!(obj instanceof MonitorBean)) return false;
		else if(((MonitorBean)obj).getMonitorId().equals(this.monitorId))
				return true;
		else	return false;
	}
	
	public int hashCode(){
		return this.monitorId.hashCode();
	}
	
	public String toString(){
		return this.monitorId+":"+this.monitorName;
	}
	public static void main(String[] args){
		String aa = "djflkajdsf;lkaj";
		logger.info(aa.matches(".*jfl.*"));
	}
}
