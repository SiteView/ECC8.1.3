package com.siteview.ecc.report.comparereport;

import java.io.InputStream;
import java.util.List;

import com.siteview.ecc.report.beans.TendencyBean;
import com.siteview.ecc.report.beans.TendencyCheckDataBean;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class ComparereportDatasource implements JRDataSource {

	private List listbean;

	public ComparereportDatasource(List list) {
		this.listbean = list;

	}

	private int index = -1;

	public Object getFieldValue(JRField field) throws JRException {
		Object value = null;
		String fieldName = field.getName();
		if (listbean.get(index) instanceof InputStream) {
			if (fieldName.startsWith("image")) {
				value = (InputStream) listbean.get(index);
			}

		} else if (listbean.get(index) instanceof TendencyBean) {
			if ("MonitorName".equals(fieldName)) {
				value = ((TendencyBean) listbean.get(index)).getName();
			} else if ("latestStatus".equals(fieldName)) {
				value = ((TendencyBean) listbean.get(index)).getNewDate();
			} else if ("okPercent".equals(fieldName)) {
				value = ((TendencyBean) listbean.get(index)).getOk();
			} else if ("errorPercent".equals(fieldName)) {
				value = ((TendencyBean) listbean.get(index)).getError();
			} else if ("warnPercent".equals(fieldName)) {
				value = ((TendencyBean) listbean.get(index)).getWarn();
			} else if ("errorCondition".equals(fieldName)) {
				value = ((TendencyBean) listbean.get(index)).getValue();
			}
		}else if (listbean.get(index) instanceof TendencyCheckDataBean) {
			if ("MonitorName".equals(fieldName)) {
				value = ((TendencyCheckDataBean) listbean.get(index)).getName();
			} else if ("average".equals(fieldName)) {
				value = ((TendencyCheckDataBean) listbean.get(index)).getAverage();
			} else if ("max".equals(fieldName)) {
				value = ((TendencyCheckDataBean) listbean.get(index)).getMax();
			} else if ("min".equals(fieldName)) {
				value = ((TendencyCheckDataBean) listbean.get(index)).getMin();
			} else if ("when_max".equals(fieldName)) {
				value = ((TendencyCheckDataBean) listbean.get(index)).getMax_when();
			} else if ("latest".equals(fieldName)) {
				value = ((TendencyCheckDataBean) listbean.get(index)).getLatest();
			} else if ("ReturnName".equals(fieldName)) {
				value = ((TendencyCheckDataBean) listbean.get(index)).getType();
			} else if ( "color".equals(fieldName) ) {
				value = ((TendencyCheckDataBean) listbean.get(index)).isColor();
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
