package com.siteview.ecc.report.top10;

import java.util.List;
import java.util.Map;

import com.siteview.ecc.report.top10.type.IComponent;

public interface TopTen{
	public String getCaption() throws Exception;
	public List<String> getTitles() throws Exception;
	public List<Map<String,IComponent>> getData() throws Exception;
}