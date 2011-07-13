package com.siteview.ecc.report.beans;

public class MonitorBean {

	private boolean color;

	private String entityName;

	private String frequency;

	private String groupName;

	private String Id;

	private String keyValue;

	private String latestUpdate;

	private String monitorName;

	private String monitorType;

	private String status;

	public MonitorBean() {
	}

	public MonitorBean(String id, String monitorName, String groupName,
			String monitorType, String frequency, String keyValue,
			String latestUpdate, String entityName, String status) {
		super();
		this.monitorName = monitorName;
		this.groupName = groupName;
		this.monitorType = monitorType;
		this.frequency = frequency;
		this.keyValue = keyValue;
		this.Id = id;
		this.latestUpdate = latestUpdate;
		this.entityName = entityName;
		this.status = status;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getFrequency() {
		return frequency;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getId() {
		return Id;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public String getLatestUpdate() {
		return latestUpdate;
	}

	public String getMonitorName() {
		return monitorName;
	}

	public String getMonitorType() {
		return monitorType;
	}

	public String getStatus() {
		return status;
	}

	public boolean isColor() {
		return color;
	}

	public void setColor(boolean color) {
		this.color = color;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setId(String id) {
		Id = id;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public void setLatestUpdate(String latestUpdate) {
		this.latestUpdate = latestUpdate;
	}

	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}

	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void trim() {
		int index = monitorName.indexOf(":");
		if (index > 0)
			this.setMonitorName(monitorName.substring(index + 1));
		else
			this.setMonitorName(monitorName);
	}

}
