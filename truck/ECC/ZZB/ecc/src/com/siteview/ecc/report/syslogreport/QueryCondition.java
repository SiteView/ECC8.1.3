package com.siteview.ecc.report.syslogreport;

import java.util.Date;
//kai.zhang
/*Helper.XfireCreateKeyValue("begin_year",beginTime.Year.ToString()),
*         Helper.XfireCreateKeyValue("begin_month",beginTime.Month.ToString()),
*         Helper.XfireCreateKeyValue("begin_day",beginTime.Day.ToString()),
*         Helper.XfireCreateKeyValue("begin_hour",beginTime.Hour.ToString()),
*         Helper
*         .XfireCreateKeyValue("begin_minute",beginTime.Minute.ToString()),
*         Helper
*         .XfireCreateKeyValue("begin_second",beginTime.Second.ToString()),
*         Helper.XfireCreateKeyValue("end_year",endTime.Year.ToString()),
*         Helper.XfireCreateKeyValue("end_month",endTime.Month.ToString()),
*         Helper.XfireCreateKeyValue("end_day",endTime.Day.ToString()),
*         Helper.XfireCreateKeyValue("end_hour",endTime.Hour.ToString()),
*         Helper.XfireCreateKeyValue("end_minute",endTime.Minute.ToString()),
*         Helper.XfireCreateKeyValue("end_second",endTime.Second.ToString())*/
public class QueryCondition {
	
	 private String id;
	 private String count; 
	 private String beginYear;
	 private String beginMonth;
	 private String beginDay; 
	 private String beginHour;
	 private String beginMinute; 
	 private String beginSecond;
	 private String endYear; 
	 private String endMonth; 
	 private String endDay; 
	 private String endHour;
	 private String endMinute; 
	 private String endSecond;
	 

   private boolean limitType;//标识是按时间搜索还是按数量搜索

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getBeginYear() {
		return beginYear;
	}
	public void setBeginYear(String beginYear) {
		this.beginYear = beginYear;
	}
	public String getBeginMonth() {
		return beginMonth;
	}
	public void setBeginMonth(String beginMonth) {
		this.beginMonth = beginMonth;
	}
	public String getBeginDay() {
		return beginDay;
	}
	public void setBeginDay(String beginDay) {
		this.beginDay = beginDay;
	}
	public String getBeginHour() {
		return beginHour;
	}
	public void setBeginHour(String beginHour) {
		this.beginHour = beginHour;
	}
	public String getBeginMinute() {
		return beginMinute;
	}
	public void setBeginMinute(String beginMinute) {
		this.beginMinute = beginMinute;
	}
	public String getBeginSecond() {
		return beginSecond;
	}
	public void setBeginSecond(String beginSecond) {
		this.beginSecond = beginSecond;
	}
	public String getEndYear() {
		return endYear;
	}
	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}
	public String getEndMonth() {
		return endMonth;
	}
	public void setEndMonth(String endMonth) {
		this.endMonth = endMonth;
	}
	public String getEndDay() {
		return endDay;
	}
	public void setEndDay(String endDay) {
		this.endDay = endDay;
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
	public String getEndSecond() {
		return endSecond;
	}
	public void setEndSecond(String endSecond) {
		this.endSecond = endSecond;
	}

	public boolean isLimitType() {
		return limitType;
	}
	public void setLimitType(boolean limitType) {
		this.limitType = limitType;
	}

}

