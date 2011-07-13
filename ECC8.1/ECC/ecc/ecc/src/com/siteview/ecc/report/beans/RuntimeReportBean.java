package com.siteview.ecc.report.beans;

public class RuntimeReportBean {

	private String name;
	
	private String returnName;
	
	private String min;
	
	private String max;
	
	private String average;
	
	private String lastest;
	
	private String lasttime;

	public RuntimeReportBean(String name, String returnName, String min,
			String max, String average, String lastdate, String lasttime) {
		this.name = name;
		this.returnName = returnName;
		this.min = min;
		this.max = max;
		this.average = average;
		this.lastest = lastdate;
		this.lasttime = lasttime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReturnName() {
		return returnName;
	}

	public void setReturnName(String returnName) {
		this.returnName = returnName;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getAverage() {
		return average;
	}

	public void setAverage(String average) {
		this.average = average;
	}

	public String getLastest() {
		return lastest;
	}

	public void setLastest(String lastdate) {
		this.lastest = lastdate;
	}

	public String getLasttime() {
		return lasttime;
	}

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}
	
}
