package com.siteview.ecc.report.topnreport;

import java.awt.Image;
import java.io.InputStream;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import com.siteview.ecc.simplereport.StatisticsBean;

/**
 * TOPN报告报表数据源
 * 
 * @company siteview
 * @author di.tang
 * @date 2009-4-28
 */
public class TopNReportDatasource implements JRDataSource {
	List	listbean;

	public TopNReportDatasource(List list) {
		this.listbean = list;
	}

	private int	index	= -1;

	@Override
	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		if (listbean.get(index) instanceof TopNReportListBean) {
			if ("name".equals(fieldName)) {
				value = ((TopNReportListBean) listbean.get(index)).getName();
			} else if ("max".equals(fieldName)) {
				value = ((TopNReportListBean) listbean.get(index)).getMax();
			} else if ("min".equals(fieldName)) {
				value = ((TopNReportListBean) listbean.get(index)).getMin();
			} else if ("average".equals(fieldName)) {
				value = ((TopNReportListBean) listbean.get(index)).getAverage();
			} else if ("latest".equals(fieldName)) {
				value = ((TopNReportListBean) listbean.get(index)).getLatest();
			} else if ("MonitorName".equals(fieldName)) {
				value = ((TopNReportListBean) listbean.get(index)).getMonitorName();
			}
		}
		if (listbean.get(index) instanceof String) {
			if ("image".equals(fieldName)) {
				value = (String) listbean.get(index);
			}
		}
		if (listbean.get(0) instanceof InputStream)
		{
			if ("image".equals(fieldName))
			{
				value=(InputStream)listbean.get(index);
			}
		}
		return value;
	}

	@Override
	public boolean next() throws JRException {
		index++;
		return (index < listbean.size());
	}

}
