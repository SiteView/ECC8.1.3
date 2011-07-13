package com.siteview.ecc.report.statisticalreport;

import java.io.File;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import com.siteview.ecc.reportserver.Constand;
import com.siteview.ecc.reportserver.StatsReport;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svdb.SvdbItem;

public class ReportGenItem extends SvdbItem{
	ReportItem reportItem;
	String genID;
	public String getGenID() {
		return genID;
	}
	public ReportItem getReportItem() {
		return reportItem;
	}
	public String getFileType() {
		String fileType= getPropMap().get("fileType");
		if(fileType==null)
			fileType="html";
		
		return fileType;
	}
	public void setFileType(String fileType) 
	{
		getPropMap().put("fileType",fileType==null?"html":fileType);
	}
	public boolean isValid() {
		
		return new Boolean(super.getPropMap().get("valid"));
	}

	public String getCreatTime() {
		return super.getPropMap().get("creatTime");
	}

	public String getCreator() {
		return getPropMap().get("creator");
	}
	public void setCreator(String creator) {
		getPropMap().put("creator", creator);
	}
	public String getGeneratePeriod(){
		String beginTime = getPropMap().get("beginTime");
		String endTime	 = getPropMap().get("endTime");
		return beginTime+"~"+endTime;
	}

	public ReportGenItem(ReportItem reportItem,String genID,Map<String,String> propMap)
	{
		super(propMap);
//		//防止因为定义的报告生成时间相同，而覆盖文件导致数据丢失
		String createTime = reportItem.getCreatTime();
		Date createDate = new Date();
		try {
			createDate = Toolkit.getToolkit().parseDate(createTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String createTimeInMillis = createDate.getTime() + "";
		
		
		this.reportItem=reportItem;
		this.genID=genID;
		super.getPropMap().put("genID",genID);
		super.getPropMap().put("creatTime",Toolkit.getToolkit().formatDate(Long.parseLong(genID)));
		if ("html".equals(getFileType()))
			super.getPropMap().put("valid",String.valueOf(new File(StatsReport.getHtmlFolderName(createTimeInMillis,genID)).exists()));
		else
			super.getPropMap().put("valid",String.valueOf(new File(StatsReport.getCreateFile(createTimeInMillis,genID, getFileType())).exists()));
		super.getPropMap().put("Period",reportItem.getPeriod());
	}
}
