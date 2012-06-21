package com.siteview.ecc.report.mysql;

import java.util.Date;

public class InitInfoBean 
{
	public final static int DIRCTION_FORWARD=1;
	public final static int DIRCTION_BACK=0;
	int direction=1;
	long lastUpdateCount=0;
	
	private String tableName;
	private String monitorId;
	private Date startTime;
	private Date endTime;

	long allUpdateCount=0;
	
	public InitInfoBean(String tableName,String monitorid,Date startTime,Date endTime,int direction)
	{
		this.tableName=tableName;
		this.monitorId=monitorid;
		this.startTime=startTime;
		this.endTime=endTime;
		this.direction=direction;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getDirection() {
		return direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	
	public long getLastUpdateCount() {
		return lastUpdateCount;
	}
	public void setLastUpdateCount(long lastUpdateCount) 
	{
		this.allUpdateCount+= lastUpdateCount;
		this.lastUpdateCount = lastUpdateCount;
	}

	public String getMonitorId() {
		return monitorId;
	}
	public void setMonitorId(String monitorId) {
		this.monitorId = monitorId;
	}
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public long getAllUpdateCount() {
		return allUpdateCount;
	}
}
