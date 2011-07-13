package com.siteview.ecc.simplereport;

import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import com.siteview.base.data.ReportDate;
import com.siteview.ecc.reportserver.StatsReport;

public class StatisticDataSource extends EccDataSource{

	int statisticSize=0;
	int currentPos=-1;
	
	public StatisticDataSource(String[] monitorIDArray, ReportDate reportDate,StatsReport statsReport,Map<String,String> monitorIdNameMap) 
	{
		super(monitorIDArray, reportDate, statsReport,monitorIdNameMap);
		this.monitorIdNameMap=monitorIdNameMap;
	}
	@Override
	public boolean next() throws JRException 
	{
		
		if(cancel)
			return false;

		
		currentPos++;
		
		while(currentPos==statisticSize)
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
			
			statisticSize=reportDate.getReturnSize(monitorIDArray[monitorPos]);
			currentPos=0;
			
			String drawmeasure = reportDate.getReturnValue(monitorIDArray[monitorPos], "sv_drawmeasure", currentPos);
			if (!"1".equals(drawmeasure))
				 continue;
			
		}
		
		return true;
	}
	@Override
	public Object getFieldValue(JRField jrField) throws JRException 
	{
		String fieldName = jrField.getName();
		
		if ("name".equals(fieldName))
		{
			return monitorIdNameMap.get(monitorIDArray[monitorPos]);
		} else if ("returnvalue".equals(fieldName))
		{
			return reportDate.getReturnValue(monitorIDArray[monitorPos], "ReturnName", currentPos);
			
		} else if ("maxvalue".equals(fieldName))
		{
			return reportDate.getReturnValue(monitorIDArray[monitorPos], "max", currentPos);
		}else if ("averagevalue".equals(fieldName))
		{
			return reportDate.getReturnValue(monitorIDArray[monitorPos], "average", currentPos);
		}else if ("lastestvalue".equals(fieldName))
		{
			return reportDate.getReturnValue(monitorIDArray[monitorPos], "latest", currentPos);
		}
		return null;
	}
	@Override
	public void getExcutingInfo(StringBuffer sb)
	{
		if(monitorPos==-1||monitorPos>=monitorIDArray.length)
			return;
		sb.append("统计数据,").append(getCurrentMonitorName()).append("(").append(currentPos+1).append("/").append(statisticSize).append(")");
		
	}
	@Override
	public int getTaskProgress(){
		if(monitorPos==-1)
			return 0;
		return monitorPos;
	}
}
