package com.siteview.ecc.report.common;

import java.awt.Image;
import java.io.InputStream;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 * 报告数据源
 * 
 * @company siteview
 * @author di.tang
 * @date 2009-3-30
 */
public class ReportDataSource implements JRDataSource {
	List listbean;

	public ReportDataSource(List list) {
		this.listbean = list;

	}

	private int index = -1;

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		if (listbean.get(index) instanceof String)
		{
			if ("image".equals(fieldName))
			{
				value=(String)listbean.get(index);
			}
		}
		if (listbean.get(index) instanceof InputStream) {
			if (fieldName.startsWith("image")) {
				value = (InputStream) listbean.get(index);
			}
		} else if (listbean.get(index) instanceof ReportBean) {
			if ("MonitorName".equals(fieldName)) {
				value = ((ReportBean) listbean.get(index)).getMonitorname();
			} else if ("max".equals(fieldName)) {
				value = ((ReportBean) listbean.get(index)).getMax();
			} else if ("min".equals(fieldName)) {
				value = ((ReportBean) listbean.get(index)).getMin();
			} else if ("when_max".equals(fieldName)) {
				value = ((ReportBean) listbean.get(index)).getWhen_max();
			} else if ("average".equals(fieldName)) {
				value = ((ReportBean) listbean.get(index)).getAverage();
			} else if ("title".equals(fieldName)) {
				value = ((ReportBean) listbean.get(index)).getTitle();
			} else if ("ReturnName".equals(fieldName)) {
				value = ((ReportBean) listbean.get(index)).getReturnName();
			} else if ("latest".equals(fieldName)) {
				value = ((ReportBean) listbean.get(index)).getLatest();
			} else if ("name".equals(fieldName)) {
				value = ((ReportBean) listbean.get(index)).getName();
			}
		}
		if (!"image".equals(fieldName) && value == null) {
			return "";
		}
		return value;
	}

	public boolean next() throws JRException {
		// TODO Auto-generated method stub
		index++;
		return (index < listbean.size());
	}

}
