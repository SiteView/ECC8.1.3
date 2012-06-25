package com.siteview.ecc.reportserver;

import java.util.Date;

public class ReturnBean {
	private Date beginTime = null;
	private Date endTime = null;
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
