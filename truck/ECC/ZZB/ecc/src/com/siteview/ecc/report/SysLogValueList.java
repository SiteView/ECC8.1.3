package com.siteview.ecc.report;

import java.util.List;
import java.util.Map;

public class SysLogValueList {
	String inTime;
	String sourceIP;
	String facility;
	String leave;
	String sysLogmsg;

	public SysLogValueList(List<Map<String, String>> a, int i) {

		inTime = a.get(i).get("creat_time");
		sourceIP = a.get(i).get("_SourceIp");
		facility = a.get(i).get("_Facility");
		leave = a.get(i).get("_Level");
		sysLogmsg = a.get(i).get("_SysLogMsg");

	}
	
	public String getInTime() {
		return inTime;
	}
	public void setInTime(String inTime) {
		this.inTime = inTime;
	}
	public String getSourceIP() {
		return sourceIP;
	}
	public void setSourceIP(String sourceIP) {
		this.sourceIP = sourceIP;
	}
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	public String getLeave() {
		return leave;
	}
	public void setLeave(String leave) {
		this.leave = leave;
	}
	public String getSysLogmsg() {
		return sysLogmsg;
	}
	public void setSysLogmsg(String sysLogmsg) {
		this.sysLogmsg = sysLogmsg;
	}

}
