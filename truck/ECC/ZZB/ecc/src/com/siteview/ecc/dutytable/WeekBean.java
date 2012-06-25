package com.siteview.ecc.dutytable;

public class WeekBean {



	public WeekBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public WeekBean(String key, String week, String startHour,
			String startMinute, String endHour, String endMinute, String phone,
			String email) {
		super();
		this.key = key;
		this.week = week;
		this.startHour = startHour;
		this.startMinute = startMinute;
		this.endHour = endHour;
		this.endMinute = endMinute;
		this.phone = phone;
		this.email = email;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	public String getStartHour() {
		return startHour;
	}
	public void setStartHour(String startHour) {
		this.startHour = startHour;
	}
	public String getStartMinute() {
		return startMinute;
	}
	public void setStartMinute(String startMinute) {
		this.startMinute = startMinute;
	}
	public String getEndHour() {
		return endHour;
	}
	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}
	public String getEndMinute() {
		return endMinute;
	}
	public void setEndMinute(String endMinute) {
		this.endMinute = endMinute;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	private String key;
	private String week;
	private String startHour;
	private String startMinute;
	private String endHour;
	private String endMinute;
	private String phone;
	private String email;
	


	
}
