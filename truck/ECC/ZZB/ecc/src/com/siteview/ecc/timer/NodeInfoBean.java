package com.siteview.ecc.timer;

public class NodeInfoBean {
	private int all=0;
	private int warning=0;
	private int error=0;
	private int ok=0;
	private int disabled=0;
	private int device=0;
	
	public int getAll() {
		return all;
	}
	public void setAll(int all) {
		this.all = all;
	}
	public int getWarning() {
		return warning;
	}
	public void setWarning(int warning) {
		this.warning = warning;
	}
	public int getError() {
		return error;
	}
	public void setError(int error) {
		this.error = error;
	}
	public int getOk() {
		return ok;
	}
	public void setOk(int ok) {
		this.ok = ok;
	}
	public int getDisabled() {
		return disabled;
	}
	public void setDisabled(int disabled) {
		this.disabled = disabled;
	}
	public int getDevice() {
		return device;
	}
	public void setDevice(int device) {
		this.device = device;
	}
}
