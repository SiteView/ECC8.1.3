package com.siteview.ecc.report.beans;

public class StateBean extends BaseBean implements Cloneable{
	private static final long serialVersionUID = -1728125386189319021L;
	private double bad;
	private double disable;
	private double error;
	private double ok;

	private double warn;

	public StateBean(double ok, double warn, double error, double disable,
			double bad) {
		this.ok = ok;
		this.warn = warn;
		this.error = error;
		this.disable = disable;
		this.bad = bad;
	}
	public StateBean(String monitorId,String monitorName,String beginTime,String endTime,double ok, double warn, double error, double disable,
			double bad){
		super(monitorId,monitorName,beginTime,endTime);
		this.ok = ok;
		this.warn = warn;
		this.error = error;
		this.disable = disable;
		this.bad = bad;
	}
//String monitorId,String monitorName,String beginTime,String endTime,
	private String double2Percent(double num) {
		String tmp = String.valueOf(num);
		int index = tmp.indexOf(".");
		if (index <= 0)
			return tmp + "%";
		else
			return tmp.substring(0, index + 2) + "%";
	}

	public String getPercentBad() {
		return double2Percent(bad);
	}

	public String getPercentDisable() {
		return double2Percent(disable);
	}
	public String getPercentError() {
		return double2Percent(error);
	}
	public String getPercentOk() {
		return double2Percent(ok);
	}
	
	public String getPercentWarn() {
		return double2Percent(warn);
	}
	
	public double getBad() {
		return bad;
	}

	public double getDisable() {
		return disable;
	}

	public double getError() {
		return error;
	}

	public double getOk() {
		return ok;
	}

	public double getWarn() {
		return warn;
	}

	public void setBad(double bad) {
		this.bad = bad;
	}
	public void setDisable(double disable) {
		this.disable = disable;
	}
	public void setError(double error) {
		this.error = error;
	}

	public void setOk(double ok) {
		this.ok = ok;
	}

	public void setWarn(double warn) {
		this.warn = warn;
	}
	public StateBean cloneThis() throws CloneNotSupportedException{
		return (StateBean) this.clone();
	}
}
