package com.siteview.ecc.dutytable;

public class DutyFatherBean  {

	public DutyFatherBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public DutyFatherBean(String description, String count, String returnValue,
			String type, String section) {
		super();
		this.description = description;
		this.count = count;
		this.returnValue = returnValue;
		this.type = type;
		this.section = section;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(String returnValue) {
		this.returnValue = returnValue;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}
	private String description;
	private String count;
	private String returnValue;
	private String type;
	private String section;
	
}