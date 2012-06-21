package com.siteview.ecc.report.beans;

import java.io.Serializable;

public abstract class BaseBean implements Serializable {

	private static final long serialVersionUID = -5626973297482400743L;
	
	private String monitorId;
	private String monitorName;
	private String queryBegintime;
	public BaseBean(){};
	public BaseBean(String monitorId, String monitorName,
			String queryBegintime, String queryEndtime) {
		super();
		this.monitorId = monitorId;
		this.monitorName = monitorName;
		this.queryBegintime = queryBegintime;
		this.queryEndtime = queryEndtime;
	}
	public String getMonitorId() {
		return monitorId;
	}
	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}
	public String getMonitorName() {
		return monitorName;
	}
	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}
	public String getQueryBegintime() {
		return queryBegintime;
	}
	public void setQueryBegintime(String queryBegintime) {
		this.queryBegintime = queryBegintime;
	}
	public String getQueryEndtime() {
		return queryEndtime;
	}
	public void setQueryEndtime(String queryEndtime) {
		this.queryEndtime = queryEndtime;
	}
	private String queryEndtime;
}
