package com.siteview.ecc.report.beans;

public class ErrorLogsBean {

	private int id;
	private String name;
	private String time;
	private String title;
	private String data;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private String type;
	private String username;
	private String result;
	private String target;
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public ErrorLogsBean(int id,String name,String time,String title,String data,String type,String username,String result,String target){
		this.id=id;
		this.name=name;
		this.time=time;
		this.title=title;
		this.data=data;
		this.type=type;
		this.username=username;
		this.result=result;
		this.target=target;
	}
	public ErrorLogsBean(){
		
	}
}
