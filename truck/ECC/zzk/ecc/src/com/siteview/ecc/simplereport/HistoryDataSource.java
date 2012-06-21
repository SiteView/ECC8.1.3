package com.siteview.ecc.simplereport;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import com.siteview.base.data.ReportDate;
import com.siteview.base.data.Report.DstrItem;
import com.siteview.ecc.reportserver.StatsReport;
import com.siteview.ecc.util.Toolkit;

public class HistoryDataSource extends EccDataSource 
{
	int historySize=0;
	int currentHistoryPos=-1;

	private Map<Date, DstrItem> dstrs;
	private Iterator iterator;
	private Date currentDate;
	
	public HistoryDataSource(String[] monitorIDArray, ReportDate reportDate,StatsReport statsReport,Map<String,String> monitorIdNameMap) {
		super(monitorIDArray, reportDate, statsReport,monitorIdNameMap);
	}
	@Override
	public boolean next() throws JRException 
	{
		
		
		if(cancel)
			return false;

		
		currentHistoryPos++;
		
		
		while(currentHistoryPos==historySize)
		{
			++monitorPos;
			
			if(monitorPos>=monitorIDArray.length)
			{
				finish=true;
				return false;
			}

			try
			{
				reportDate.getReportDate(monitorIDArray[monitorPos],statsReport.dstrstatusnoneed,statsReport.showdstr,statsReport.return_value_filter);
			}catch(Exception e)
			{
				finish=true;
				e.printStackTrace(); 
				return false;
			}
			
			this.dstrs=reportDate.getDstr(monitorIDArray[monitorPos]);
			if (dstrs == null || dstrs.size() == 0 || dstrs.isEmpty())
				continue;
			
			historySize=this.dstrs.size();
			currentHistoryPos=0;
			
			if(historySize>0)
				this.iterator=dstrs.keySet().iterator();
		}	
		currentDate=(Date)iterator.next();


		return true;
	}
	@Override
	public Object getFieldValue(JRField jrField) throws JRException 
	{
		DstrItem dstrItem=dstrs.get(currentDate);
		String fieldName=jrField.getName();
		
		
		if ("name".equals(fieldName))
		{
			return getCurrentMonitorName();
			
		} else if ("state".equals(fieldName))
		{
			if (dstrItem.status.equals("error"))
			{
				return "错误";
			}
			if (dstrItem.status.equals("warning"))
			{
				return "危险";
			}
			return "正常";
			
		} else if ("datev".equals(fieldName))
		{
			return Toolkit.getToolkit().formatDate(currentDate);
		}else if ("destr".equals(fieldName))
		{
			return dstrItem.value;
		}
		return null;
	}
	@Override
	public void getExcutingInfo(StringBuffer sb)
	{
		if(monitorPos==-1||monitorPos>=monitorIDArray.length)
			return;
		sb.append("细节数据,").append(getCurrentMonitorName()).append("(").append(currentHistoryPos+1).append("/").append(historySize).append(")");
		
	}
	@Override
	public int getTaskProgress(){
		if(monitorPos==-1)
			return 0;

		return monitorPos;
	}
}
