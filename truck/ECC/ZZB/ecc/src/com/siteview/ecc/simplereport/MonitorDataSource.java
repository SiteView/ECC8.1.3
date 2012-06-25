package com.siteview.ecc.simplereport;


import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import com.focus.db.DBCon;
import com.focus.db.QueryResult;
import com.focus.util.Util;
import com.siteview.base.data.ReportDate;
import com.siteview.ecc.report.mysql.ReadDataToMysql;
import com.siteview.ecc.reportserver.StatsReport;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.util.Toolkit;

public class MonitorDataSource extends EccDataSource{

	public static final String sql="select status,count(status) as num from ecc_:tableName where datatime>=:startTime and datatime<=:endTime and monitorid=:monitorid group by status";
	Map<String,Object> values=new HashMap<String,Object>(); 
	
	public MonitorDataSource(String[] monitorID,ReportDate reportDate,StatsReport statsReport,Map<String,String> monitorIdNameMap) {
		super(monitorID,reportDate,statsReport,monitorIdNameMap);
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException 
	{
		String fieldName = jrField.getName();

		if(ReadDataToMysql.getInstance().importToSQLDB)
			return getFieldValueByDataBase(fieldName);

		String currentMonitorId=monitorIDArray[monitorPos];
		if(reportDate.getM_fmap()!=null && reportDate.getM_fmap().size()>0){
			if ("name".equals(fieldName))
			{
				return getCurrentMonitorName();
				
			} else if ("nomal".equals(fieldName))
			{
				return reportDate.getPropertyValue(currentMonitorId, "okPercent");
				
			} else if ("danger".equals(fieldName))
			{
				return reportDate.getPropertyValue(currentMonitorId, "warnPercent");
				
			}else if ("error".equals(fieldName))
			{
				return reportDate.getPropertyValue(currentMonitorId, "errorPercent");
				
			}else if ("disable".equals(fieldName))
			{
				return String.valueOf(reportDate.getDisablePercentOfSimpleReport());
				
			}else if ("errorvalue".equals(fieldName))
			{
				return reportDate.getPropertyValue(currentMonitorId, "errorCondition");
			}
		}
		return null;
	}

	@Override
	public boolean next() throws JRException 
	{
		
		if(cancel)
			return false;

		monitorPos++;
		if(monitorPos>=monitorIDArray.length)
		{
			finish=true;
			return false;
		}

		try
		{
			if(ReadDataToMysql.getInstance().importToSQLDB)
				readDataFromDataBase(monitorIDArray[monitorPos]);
			else	
				reportDate.getReportDate(monitorIDArray[monitorPos],statsReport.dstrstatusnoneed,statsReport.showdstr,statsReport.return_value_filter);
			monitorIdNameMap.put(monitorIDArray[monitorPos], reportDate.getPropertyValue(monitorIDArray[monitorPos], "MonitorName"));
			
		}catch(Exception e)
		{
			finish=true;
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	@Override
	public void getExcutingInfo(StringBuffer sb)
	{
		if(monitorPos==-1||monitorPos>=monitorIDArray.length)
			return;
		sb.append("¼à²âÆ÷Êý¾Ý,").append(getCurrentMonitorName()).append("(").append(monitorPos+1).append("/").append(monitorIDArray.length).append(")");
	}
	@Override
	public int getTaskProgress(){
		if(monitorPos==-1)
			return 0;

		return monitorPos;
	}
	private void readDataFromDataBase(String monitorid) throws Exception
	{
		ReadDataToMysql readDataToMysql=ReadDataToMysql.getInstance();
		String tableName=readDataToMysql.getTableName(monitorid);
		String monitorName=readDataToMysql.getMonitorName(monitorid);
		DBCon con=null;
		try{
			
			String mysql=Util.getInstance().replace(sql, ":tableName",tableName);
			con=DBCon.getConnection("siteviewDS");
			con.setSQL(mysql);
			con.setParameter("monitorid", monitorid);
			con.setParameter("startTime", Toolkit.getToolkit().formatDate(reportDate.getM_begin_date()));
			con.setParameter("endTime", Toolkit.getToolkit().formatDate(reportDate.getM_end_date()));
			QueryResult rs=con.query();
			
			float allStatus=0;
			
			float okPercent=0;
			float warnPercent=0;
			float errorPercent=0;
			float disablePercent=0;
			
			for(int i=0;i<rs.size();i++)
			{
				Object status= rs.getObject(i, "status");
				int num= Integer.parseInt(rs.getObject(i, "num").toString());
				allStatus+=num;
				if(status==null||status.toString().toLowerCase().equals("null"))
					errorPercent+=num;
				else
				{
					int s=Integer.parseInt(status.toString());
					switch(s)
					{
						case EccTreeItem.STATUS_OK:okPercent+=num;break;
						case EccTreeItem.STATUS_WARNING:warnPercent+=num;break;
						case EccTreeItem.STATUS_DISABLED:disablePercent+=num;break;
						default:
							errorPercent+=num;
					}
				}
			}
			DecimalFormat df = new DecimalFormat("####.000");
			
			values.put("name", monitorName);
			values.put("nomal", df.format(100.00*okPercent/allStatus));
			values.put("danger", df.format(100.00*warnPercent/allStatus));
			values.put("error", df.format(100.00*errorPercent/allStatus));
			values.put("disable", df.format(100.00*disablePercent/allStatus));
			values.put("errorCondition", readDataToMysql.getErrorCondition(monitorid));
			
			//Map<String, Map<String, String>>
		}
		finally
		{
			if(con!=null)
				con.close();
		}
		
	}
	public Object getFieldValueByDataBase(String fieldName)
	{
		if ("name".equals(fieldName))
		{
			return getCurrentMonitorName();
			
		} else if ("nomal".equals(fieldName))
		{
			return values.get("okPercent");
			
		} else if ("danger".equals(fieldName))
		{
			return values.get("warnPercent");
			
		}else if ("error".equals(fieldName))
		{
			return values.get("errorPercent");
			
		}else if ("disable".equals(fieldName))
		{
			return values.get("disablePercent");
			
		}else if ("errorvalue".equals(fieldName))
		{
			return values.get("errorCondition");
		}
		return null;
	}
}
