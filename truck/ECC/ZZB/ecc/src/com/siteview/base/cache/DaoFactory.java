package com.siteview.base.cache;

public class DaoFactory {
	private static ReportDataDao reportDataDao = null;
	public static ReportDataDao getReportDataDao(){
		if (reportDataDao == null) reportDataDao = new ReportDataDaoImpl();
		return reportDataDao;
	}
}
