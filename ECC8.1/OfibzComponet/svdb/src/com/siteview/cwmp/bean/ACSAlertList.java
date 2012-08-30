package com.siteview.cwmp.bean;

import java.util.List;

public class ACSAlertList {
	private List<ACSAlertInformation> list = null;
	private long size = 0;
	public List<ACSAlertInformation> getList() {
		return list;
	}
	public void setList(List<ACSAlertInformation> list) {
		this.list = list;
	}
	public long getAllSize() {
		return size;
	}
	public void setAllSize(long size) {
		this.size = size;
	}
}
