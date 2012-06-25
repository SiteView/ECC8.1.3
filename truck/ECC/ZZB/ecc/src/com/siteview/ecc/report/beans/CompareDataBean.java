package com.siteview.ecc.report.beans;

import java.util.Date;
import java.util.Map;

public class CompareDataBean {
	private Map<Date, String> data = null;
	private String id = null;
	private String name = null;
	public CompareDataBean(String id ,String name ,Map<Date, String> data){
		this.setData(data);
		this.setId(id);
		this.setName(name);
	}
	public Map<Date, String> getData() {
		return data;
	}
	public void setData(Map<Date, String> data) {
		this.data = data;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
