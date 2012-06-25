package com.siteview.ecc.report;

public class SyslogBean {
	
	private String inTime;
	
	private String sourceIP;
	
	private String facility;
	
	private String leave;
	
	private String sysLogmsg;
	
	private boolean color;

	public boolean isColor() {
		return color;
	}

	public void setColor(boolean color) {
		this.color = color;
	}

	public SyslogBean()
	{
		
	}
	
	public SyslogBean(String inTime, String sourceIP, String facility,
			String leave, String sysLogmsg) {
		super();
		this.inTime = inTime;
		this.sourceIP = sourceIP;
		this.facility = facility;
		this.leave = leave;
		this.sysLogmsg = sysLogmsg;
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
