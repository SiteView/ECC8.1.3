package com.siteview.ecc.report.statisticalreport;

import java.util.Map;

import com.siteview.ecc.util.Toolkit;
import com.siteview.svdb.SvdbItem;

public class ReportItem extends SvdbItem{

	private String reportID;
	
	public ReportItem(String reportID,Map<String,String> propMap)
	{
	  super(propMap);
	  this.reportID=reportID;	
//	  super.getPropMap().put("creatTime",Toolkit.getToolkit().formatDate(Long.parseLong(reportID)));
	}
	public String getCreatTime()
	{
		return super.getPropMap().get("creatTime"); 
	}
	public String getReportID() {
		return reportID;
	}
	public String getMonitotNumber(){
		return super.getPropMap().get("MonitorNumber"); 
	}
	public void setReportID(String reportID) {
		this.reportID = reportID;
	}
	public String getTitle() {
		String title=getPropMap().get("Title");
		if(title!=null)
		{
			int idx=title.indexOf("|");
			if(idx>0)
				title=title.substring(0,idx);
		}
		return title;
	}
	public void setTitle(String title) {
		getPropMap().put("Title",title);
	}
	public String getPeriod() {
		String period = getPropMap().get("Period");
		if(period.equals("Day")){
			return "日报";
		}else if(period.equals("Week")){
			return "周报";
		}else if(period.equals("Month")){
			return "月报";
		}else if(period.split("Other").length>0){
			return "其他固定时间";
		}
		return "";
	}
	public void setPeriod(String period) {
		getPropMap().put("Period",period);
	}
	public String getDeny() {
		return getPropMap().get("Deny");
	}
	public void setDeny(String deny) {
		getPropMap().put("Deny",deny);
	}
	public String getFileType() {
		return getPropMap().get("fileType");
	}
	public void setFileType(String fileType) {
		getPropMap().put("fileType",fileType);
	}
	
	public String getDescript() {
		return getPropMap().get("Descript");
	}
	public void setDescript(String descript) {
		getPropMap().put("Descript",descript);
	}	
	
	public int hashCode(){
		return this.reportID.hashCode();
	}
	
	public boolean equals(Object anObject){
		if (this == anObject) {
		    return true;
		}
		if (anObject instanceof ReportItem) {
			ReportItem another = (ReportItem)anObject;
			if(another.reportID.equals(this.reportID)){
				return true;
			}
		}
		return false;
	}
}
