package com.siteview.ecc.report.syslogreport;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;


import com.siteview.ecc.report.SyslogBean;
/**
 *系统日志报表数据源 用于填充ireport模板
 * 
 * @company: siteview
 * @author:kai.zhang
 * @date:2009-4-22
 */
public class SyslogDataSource implements JRDataSource {
	List listbean;

	public SyslogDataSource(List list) {
		this.listbean = list;

	}
	private int index = -1;

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		if (listbean.get(index) instanceof SyslogBean) {
			if ("inTime".equals(fieldName)) {
				value = ((SyslogBean) listbean.get(index)).getInTime();
			} else if ("sourceIP".equals(fieldName)) {
				value = ((SyslogBean) listbean.get(index)).getSourceIP();
			} else if ("facility".equals(fieldName)) {
				value = ((SyslogBean) listbean.get(index)).getFacility();
			} else if ("leave".equals(fieldName)) {
				value = ((SyslogBean) listbean.get(index)).getLeave();
			} else if ("sysLogmsg".equals(fieldName)) {
				value = ((SyslogBean) listbean.get(index)).getSysLogmsg();
			} else if ( "color".equals(fieldName)) {
				value = ( (SyslogBean) listbean.get(index)).isColor();
			}
		}
		return value;
	}

	public boolean next() throws JRException {
		// TODO Auto-generated method stub
		index++;
		return (index < listbean.size());
	}

}
