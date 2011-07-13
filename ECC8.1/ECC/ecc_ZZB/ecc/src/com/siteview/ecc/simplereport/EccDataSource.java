package com.siteview.ecc.simplereport;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import com.siteview.base.data.ReportDate;
import com.siteview.ecc.reportserver.StatsReport;

public abstract class EccDataSource implements JRDataSource {
	
	boolean finish=false;
	public boolean isFinish() {
		return finish;
	}
	boolean cancel=false;
	ReportDate reportDate;
	StatsReport statsReport;
	
	Map<String,String> monitorIdNameMap;
	int monitorPos=-1;
	String[] monitorIDArray;
	
	public Map<String,ReportDate> reportDateMap =new HashMap<String,ReportDate>();
	
	public EccDataSource(String[] monitorIDArray,ReportDate reportDate,StatsReport statsReport,Map<String,String> monitorIdNameMap) {
		super();
		this.monitorIDArray=monitorIDArray;
		this.reportDate=reportDate;
		this.statsReport=statsReport;
		this.monitorIdNameMap=monitorIdNameMap;
	}
	public boolean isCancel() {
		return cancel;
	}

	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	@Override
	public abstract Object getFieldValue(JRField jrField) throws JRException;

	@Override
	public abstract boolean next() throws JRException ;
	
	

	public int getTaskSize()
	{
		return monitorIDArray.length;
	}
	public String[] getMonitorID() {
		return monitorIDArray;
	}
	public ReportDate getReportDate() {
		return reportDate;
	}


	public StatsReport getStatsReport() {
		return statsReport;
	}
	public abstract void getExcutingInfo(StringBuffer sb);
	
	public abstract  int getTaskProgress();
	
	public String getMonitorName(String monitorID)
	{
		return monitorIdNameMap.get(monitorID);
	}
	public String getCurrentMonitorName()
	{
		String name=monitorIdNameMap.get(monitorIDArray[monitorPos]);
		if(name==null&&monitorPos>0)
			name=monitorIdNameMap.get(monitorIDArray[monitorPos-1]);
		return name;
	}

	
}
