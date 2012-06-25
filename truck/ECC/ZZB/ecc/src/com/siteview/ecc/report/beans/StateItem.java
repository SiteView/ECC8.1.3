package com.siteview.ecc.report.beans;

public class StateItem extends BaseBean {

	private String beginTime;
	private String status;
	private String count;
	private String persistTime;
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getPersistTime() {
		return persistTime;
	}
	public void setPersistTime(String persistTime) {
		this.persistTime = persistTime;
	}
	public StateItem(String beginTime, String status, String count,
			String persistTime) {
		super();
		this.beginTime = beginTime;
		this.status = status;
		this.count = count;
		this.persistTime = persistTime;
	}
}
