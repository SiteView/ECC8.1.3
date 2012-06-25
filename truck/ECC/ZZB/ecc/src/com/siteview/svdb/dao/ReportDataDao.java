package com.siteview.svdb.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.siteview.base.data.ReportDateError;
import com.siteview.svdb.dao.bean.ReportData;

public interface ReportDataDao {
	public void insert(ReportData data) throws Exception;
	public void delete(String id,Date createTime) throws Exception;
	public void update(ReportData data) throws Exception;
	public ReportData getReportData(String id,Date createTime) throws Exception;
	public List<ReportData> getReportData(String id,Date begin,Date end) throws Exception;
	public void update(List<ReportData> datalist) throws Exception;
	public Map<String, List<ReportData>> queryReportdataByTime(String[] idArr,Date begin, Date end) throws Exception;
	public Map<String, List<ReportData>> queryReportdataByCount(String[] idArr,int count) throws Exception;
	public Map<String, List<ReportDateError>> queryReportErrordataByCount(String[] idArr,int count) throws Exception;
	public Map<String, List<ReportDateError>> queryReportErrordataByTime(String[] idArr, Date begin, Date end) throws Exception;
}
