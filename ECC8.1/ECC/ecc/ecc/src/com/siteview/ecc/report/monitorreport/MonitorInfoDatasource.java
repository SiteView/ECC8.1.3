package com.siteview.ecc.report.monitorreport;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import com.siteview.ecc.report.beans.MonitorBean;

public class MonitorInfoDatasource implements JRDataSource {
	
	private List<MonitorBean> beans;
	private int index = -1;
	public MonitorInfoDatasource(List<MonitorBean> beans){
		this.beans = beans;
	}

	@Override
	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		if (beans.get(index) instanceof MonitorBean) {
			if ("monitorName".equals(fieldName)) {
				String monitorName = beans.get(index).getMonitorName();
				int index = monitorName.indexOf(":");
				if(index>0)
					monitorName = monitorName.substring(index+1);
				if(monitorName.length()>200){
					monitorName = monitorName.substring(0, 200);
				}
				value = monitorName;
			} else if ("groupName".equals(fieldName)) {
				value = beans.get(index).getGroupName();
			}else if ("entityName".equals(fieldName)) {
				value = beans.get(index).getEntityName();
			} else if ("typeName".equals(fieldName)) {
				value = beans.get(index).getMonitorType();
			} else if ("freq".equals(fieldName)) {
				value = beans.get(index).getFrequency();
			} else if ("keyValue".equals(fieldName)) {
				value =beans.get(index).getKeyValue();
			} else if ("latestTime".equals(fieldName)) {
				value = beans.get(index).getLatestUpdate();
			} else if("color".endsWith(fieldName)){
				value = beans.get(index).isColor();
			}
		}
		return value;
	}

	@Override
	public boolean next() throws JRException {
		index++;
		return (index < beans.size());
	}

}
