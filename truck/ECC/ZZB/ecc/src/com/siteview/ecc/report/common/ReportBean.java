package com.siteview.ecc.report.common;

/**
 * 产生 报告数据报表的BEAN
 * 
 * @company siteview
 * @author di.tang
 * @date 2009-3-29
 */
public class ReportBean {
	private String name = "name";
	private String max = "max";
	private String min = "min";
	private String average = "average";
	private String when_max = "";
	private String monitorname = "monitorname ";
	private String title = "";
	private String ReturnName = "";
	private String latest = "";

	/**
	 * @return the latest
	 */
	public String getLatest() {
		return latest;
	}

	/**
	 * @param latest
	 *            the latest to set
	 */
	public void setLatest(String latest) {
		this.latest = latest;
	}

	/**
	 * @return the returnName
	 */
	public String getReturnName() {
		return ReturnName;
	}

	/**
	 * @param returnName
	 *            the returnName to set
	 */
	public void setReturnName(String returnName) {
		ReturnName = returnName;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the monitorname
	 */
	public String getMonitorname() {
		return monitorname;
	}

	/**
	 * @param monitorname
	 *            the monitorname to set
	 */
	public void setMonitorname(String monitorname) {
		this.monitorname = monitorname;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the max
	 */
	public String getMax() {
		return max;
	}

	/**
	 * @param max
	 *            the max to set
	 */
	public void setMax(String max) {
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public String getMin() {
		return min;
	}

	/**
	 * @param min
	 *            the min to set
	 */
	public void setMin(String min) {
		this.min = min;
	}

	/**
	 * @return the average
	 */
	public String getAverage() {
		return average;
	}

	/**
	 * @param average
	 *            the average to set
	 */
	public void setAverage(String average) {
		this.average = average;
	}

	/**
	 * @return the when_max
	 */
	public String getWhen_max() {
		return when_max;
	}

	/**
	 * @param when_max
	 *            the when_max to set
	 */
	public void setWhen_max(String when_max) {
		this.when_max = when_max;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
