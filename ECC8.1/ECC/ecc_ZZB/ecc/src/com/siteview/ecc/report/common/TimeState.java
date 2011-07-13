package com.siteview.ecc.report.common;

import java.io.Serializable;

public class TimeState implements Serializable {

	
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4065148796838725188L;
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	private String time;
	private String state;
}
