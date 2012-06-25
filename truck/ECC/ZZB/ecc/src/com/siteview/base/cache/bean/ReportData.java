package com.siteview.base.cache.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ReportData implements Serializable {
	private static final long serialVersionUID = -661815104677761591L;
	private String id;
	private String name;
	private Date createTime;
	private Map<String,String> values = new HashMap<String,String>();
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getValue(String key) {
		return values.get(key);
	}
	public void setValue(String key,String value) {
		values.put(key, value);
	}
	public void setValue(Map<String,String> values) {
		this.values = values;
	}
	public Set<String> getValueKeys(){
		return values.keySet();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
