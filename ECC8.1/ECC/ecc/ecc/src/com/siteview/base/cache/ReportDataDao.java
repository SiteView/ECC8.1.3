package com.siteview.base.cache;

import java.util.Date;
import java.util.List;

import com.siteview.base.cache.bean.ReportData;

public interface ReportDataDao {
	public void insert(ReportData data) throws Exception;
	public void delete(String id,Date createTime) throws Exception;
	public void update(ReportData data) throws Exception;
	public ReportData getReportData(String id,Date createTime) throws Exception;
	public List<ReportData> getReportData(String id,Date begin,Date end) throws Exception;
	public void update(List<ReportData> datalist) throws Exception;
}
