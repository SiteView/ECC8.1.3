package com.siteview.ecc.report.top10;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.siteview.ecc.report.top10.type.IComponent;
import com.siteview.ecc.report.top10.type.TextImpl;


public class TopTenTestImpl implements TopTen{

	@Override
	public List<Map<String, IComponent>> getData() throws Exception {
		List<Map<String, IComponent>> retlist = new LinkedList<Map<String, IComponent>>();
		Map<String, IComponent> map = new HashMap<String, IComponent>();
		List<String> titles = getTitles();
		for (String title : titles){
			IComponent comp = new TextImpl("这是一个测试界面");
			map.put(title, comp);
		}
		retlist.add(map);
		return retlist;
	}

	@Override
	public List<String> getTitles() throws Exception {
		List<String> retlist = new LinkedList<String>();
		retlist.add("Node");
		retlist.add("messgae");
		return retlist;
	}

	@Override
	public String getCaption() throws Exception {
		return "this is a test for me";
	}
	
}