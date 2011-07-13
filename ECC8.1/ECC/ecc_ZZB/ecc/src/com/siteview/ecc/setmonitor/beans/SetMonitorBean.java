package com.siteview.ecc.setmonitor.beans;

public class SetMonitorBean {
	

	public SetMonitorBean( String monitorName, String frequency,
			String keyValue, String status) {
		super();

		this.monitorName = monitorName;
		this.frequency = frequency;
		this.keyValue = keyValue;
		this.status = status;
	}

	private String monitorName;
	private String frequency;
	private String keyValue;
    private String status;
	

	public String getMonitorName() {
		return monitorName;
	}
	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
