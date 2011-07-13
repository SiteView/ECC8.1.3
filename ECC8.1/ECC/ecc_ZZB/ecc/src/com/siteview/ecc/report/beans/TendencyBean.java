package com.siteview.ecc.report.beans;

public class TendencyBean {
	private String name;
	
	private String ok;
	
	private String warn;
	
	private String error;
	
	private String newDate;
	
	private String value;

	public TendencyBean(){}
	
	public TendencyBean(String name, String ok, String warn, String error,
			String newDate, String value) {
		super();
		this.name = name;
		this.ok = ok;
		this.warn = warn;
		this.error = error;
		this.newDate = newDate;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOk() {
		return ok;
	}

	public void setOk(String ok) {
		this.ok = ok;
	}

	public String getWarn() {
		return warn;
	}

	public void setWarn(String warn) {
		this.warn = warn;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getNewDate() {
		return newDate;
	}

	public void setNewDate(String newDate) {
		this.newDate = newDate;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
