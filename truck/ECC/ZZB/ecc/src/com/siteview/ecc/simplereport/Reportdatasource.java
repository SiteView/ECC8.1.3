package com.siteview.ecc.simplereport;

import java.awt.Image;
import java.io.InputStream;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class Reportdatasource implements JRDataSource
{
	List	listbean;
	private boolean cancel=false;
	public void setCancel(boolean cancel) {
		this.cancel = cancel;
	}

	public Reportdatasource(List list)
	{
		this.listbean = list;
	}
	
	private int	index	= -1;
	
	@Override
	public Object getFieldValue(JRField field) throws JRException
	{
		// TODO Auto-generated method stub
		Object value = null;
		
		String fieldName = field.getName();
		
		if (listbean.get(0) instanceof MonitorBean)
		{
			if ("name".equals(fieldName))
			{
				value = ((MonitorBean) listbean.get(index)).getName();
			} else if ("nomal".equals(fieldName))
			{
				value = ((MonitorBean) listbean.get(index)).getNomal();
			} else if ("danger".equals(fieldName))
			{
				value = ((MonitorBean) listbean.get(index)).getDanger();
			}else if ("error".equals(fieldName))
			{
				value = ((MonitorBean) listbean.get(index)).getError();
			}else if ("disable".equals(fieldName))
			{
				value = ((MonitorBean) listbean.get(index)).getDisable();
			}else if ("errorvalue".equals(fieldName))
			{
				value = ((MonitorBean) listbean.get(index)).getErrorvalue();
			}
		}
		if (listbean.get(0) instanceof StatisticsBean)
		{
			if ("name".equals(fieldName))
			{
				value = ((StatisticsBean) listbean.get(index)).getName();
			} else if ("returnvalue".equals(fieldName))
			{
				value = ((StatisticsBean) listbean.get(index)).getReturnvalue();
			} else if ("maxvalue".equals(fieldName))
			{
				value = ((StatisticsBean) listbean.get(index)).getMaxvalue();
			}else if ("averagevalue".equals(fieldName))
			{
				value = ((StatisticsBean) listbean.get(index)).getAveragevalue();
			}else if ("lastestvalue".equals(fieldName))
			{
				value = ((StatisticsBean) listbean.get(index)).getLastestvalue();
			}
		}
		if (listbean.get(0) instanceof String)
		{
			if ("image".equals(fieldName))
			{
				value=(String)listbean.get(index);
			}
		}
		if (listbean.get(0) instanceof Image)
		{
			if ("image".equals(fieldName))
			{
				value=(Image)listbean.get(index);
			}
		}
		if (listbean.get(0) instanceof InputStream)
		{
			if ("image".equals(fieldName))
			{
				value=(InputStream)listbean.get(index);
			}
		}
		if (listbean.get(0) instanceof HistoryBean)
		{
			if ("name".equals(fieldName))
			{
				value = ((HistoryBean) listbean.get(index)).getName();
			} else if ("state".equals(fieldName))
			{
				value = ((HistoryBean) listbean.get(index)).getState();
			} else if ("datev".equals(fieldName))
			{
				value = ((HistoryBean) listbean.get(index)).getDate();
			}else if ("destr".equals(fieldName))
			{
				value = ((HistoryBean) listbean.get(index)).getDestr();
			}
		}
		
		return value;
	}
	
	@Override
	public boolean next() throws JRException
	{
		if(cancel)
			return false;
		// TODO Auto-generated method stub
		index++;
		return (index < listbean.size());
	}
	public int getSize()
	{
		if(listbean==null)
			return 0;
		return listbean.size();
	}
	public int getCurrentIndex()
	{
		if(index==-1)
			return 0;
		
		return index;
	}
}
