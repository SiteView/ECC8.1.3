package com.siteview.ecc.report.topnreport;


/**
 * 自动生成TOPN报表,用于IREPORT模板绑定数据
 * @company siteview
 * @author di.tang
 * @date 2009-4-28 
 */
public class TopNReportListBean {
	private String	MonitorName	= "";
	private String	max			= "";
	private String	average		= "";
	private String	lastest		= ""; 
	private String	min			= "";
	private String	name		= "";

	public TopNReportListBean(String name, String MonitorName, String max, String average, String min, String latest) {
		this.name = name;
		this.MonitorName = MonitorName;
		this.max = max;
		this.average = average;
		this.lastest = latest;
		this.min = min;
	}

	public String getMonitorName() {
		return MonitorName;
	}

	public void setMonitorName(String monitorName) {
		MonitorName = monitorName;
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

	public String getLatest() {
		return lastest;
	}

	public void setLatest(String latest) {
		this.lastest = latest;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
