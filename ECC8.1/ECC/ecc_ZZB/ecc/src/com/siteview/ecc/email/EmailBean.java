package com.siteview.ecc.email;

public class EmailBean{

	public String getSchedule() {
		return schedule;
	}
	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getMailList() {
		return mailList;
	}
	public void setMailList(String mailList) {
		this.mailList = mailList;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getBcheck() {
		return bcheck;
	}
	public void setBcheck(String bcheck) {
		this.bcheck = bcheck;
	}
	public String getNIndex() {
		return nIndex;
	}

	public void setNIndex(String index) {
		nIndex = index;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	public EmailBean(String section, String des, String mailList, String name,
			String schedule, String taskType, String template, String bcheck,
			String index) {
		super();
		this.section = section;
		this.des = des;
		this.mailList = mailList;
		this.name = name;
		this.schedule = schedule;
		this.taskType = taskType;
		this.template = template;
		this.bcheck = bcheck;
		nIndex = index;
	}
	
	public EmailBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	private String section;
	private String des;
	private String mailList;
	private String name;
	private String schedule;
	private String taskType;
	private String template;
	private String bcheck;
	private String nIndex;
	

}
