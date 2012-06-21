package com.siteview.ecc.reportserver;

import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import com.siteview.base.data.ReportDate;
import com.siteview.ecc.simplereport.EccDataSource;

public class BlankDataSource extends EccDataSource{

	@Override
	public void getExcutingInfo(StringBuffer sb) {
	}

	@Override
	public int getTaskProgress() {
		return 0;
	}

	@Override
	public int getTaskSize() {
		return 0;
	}

	public BlankDataSource(String[] monitorIDArray, ReportDate reportDate,
			StatsReport statsReport, Map<String, String> monitorIdNameMap) {
		super(monitorIDArray, reportDate, statsReport, monitorIdNameMap);
	}

	boolean blank=true;
	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		return "";
	}

	@Override
	public boolean next() throws JRException {
		if(blank)
		{
			blank=false;
			return true;
		}
		return false;
	}

}
