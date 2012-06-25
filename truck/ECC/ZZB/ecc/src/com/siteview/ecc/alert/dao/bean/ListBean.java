package com.siteview.ecc.alert.dao.bean;

import java.util.List;
import java.util.Map;

public class ListBean {
	private List<Map<String, String>> list = null;
	private String message = null;
	private boolean success = true;
	private int totalNumber = 0;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isSuccess() {
		return success;
	}
	public int getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public List<Map<String, String>> getList() {
		return list;
	}
	public void setList(List<Map<String, String>> list) {
		this.list = list;
	}
}
