package com.siteview.ecc.report.mysql;

import java.util.ArrayList;
import java.util.HashMap;

import com.siteview.jsvapi.ForestVector;

public class ImportTask {
	String tableName;
	ForestVector value;
	String monitorID;
	public ImportTask(String tableName,ForestVector value,String monitorID)
	{
		this.value=value;
		this.tableName=tableName;
		this.monitorID=monitorID;
	}
}
