package com.siteview.svdb;

import java.util.Map;

public class SvdbItem {
	private Map<String, String> propMap = null;
	public SvdbItem(Map<String, String> propMap)
	{
		this.propMap=propMap;
	}
	public Map<String, String> getPropMap() {
		return propMap;
	}
}
