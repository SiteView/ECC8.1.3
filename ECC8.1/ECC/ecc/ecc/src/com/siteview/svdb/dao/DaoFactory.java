package com.siteview.svdb.dao;

public class DaoFactory {
	private static ReportDataDao reportDataDao = null;
	public static ReportDataDao getReportDataDao(){
		if (reportDataDao == null) reportDataDao = new ReportDataDaoImpl();
		return reportDataDao;
	}
}
