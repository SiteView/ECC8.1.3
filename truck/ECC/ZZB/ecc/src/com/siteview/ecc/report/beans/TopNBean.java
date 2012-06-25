package com.siteview.ecc.report.beans;

import org.zkoss.zul.Image;

public class TopNBean {

	public TopNBean(){}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getPeriod() {
		if(period.equals("Day"))
		{
			return "日报";
		}else if(period.equals("Week"))
		{
			return "周报";
		}else if(period.equals("Month")){
			return "月报";
		}
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getGenerate() {
		return generate;
	}

	public void setGenerate(String generate) {
		this.generate = generate;
	}

	public String getEmailSend() {
		return emailSend;
	}

	public void setEmailSend(String emailSend) {
		this.emailSend = emailSend;
	}

	public String getDeny() {
		return deny;
	}

	public void setDeny(String deny) {
		this.deny = deny;
	}

	public String getGetValue() {
		return getValue;
	}

	public void setGetValue(String getValue) {
		this.getValue = getValue;
	}

	public String getWeekEndTime() {
		return weekEndTime;
	}

	public void setWeekEndTime(String weekEndTime) {
		this.weekEndTime = weekEndTime;
	}

	public String getGroupRight() {
		return groupRight;
	}

	public void setGroupRight(String groupRight) {
		this.groupRight = groupRight;
	}

	public TopNBean(String title, String descript, String type, String sort,
			String count, String period, String generate, String emailSend,
			String deny, String getValue, String weekEndTime, String groupRight,String section,String filetype) {
		super();
		this.title = title;
		this.descript = descript;
		this.type = type;
		this.sort = sort;
		this.count = count;
		this.period = period;
		this.generate = generate;
		this.emailSend = emailSend;
		this.deny = deny;
		this.getValue = getValue;
		this.weekEndTime = weekEndTime;
		this.groupRight = groupRight;
		this.section = section;
		this.filetype=filetype;
	}

	private String section;
	
	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	private String title;
	
	private String descript;
	
	private String type;
	
	private String sort;
	
	private String count;
	
	private String period;
	
	private String generate;
	
	private String emailSend;
	
	private String deny;
	
	private String getValue;
	
	private String weekEndTime;
	
	private String groupRight;
	private Image editImage;
    /**
     * 
     */
    private String filetype;
	public String getFiletype()
	{
		return filetype;
	}

	public void setFiletype(String filetype)
	{
		this.filetype = filetype;
	}

	public Image getEditImage() {
		return editImage;
	}

	public void setEditImage(Image editImage) {
		this.editImage = editImage;
	}
}
