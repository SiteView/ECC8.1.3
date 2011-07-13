package com.siteview.svdb.dao;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.siteview.svdb.dao.bean.ReportData;

public interface TelebackupDataDao {
	public void insert(ArrayList<String> ids) throws Exception;	
	public boolean query(String id) throws Exception;	
}
