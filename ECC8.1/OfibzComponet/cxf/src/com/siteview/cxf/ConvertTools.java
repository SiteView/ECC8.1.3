package com.siteview.cxf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.siteview.eccservice.keyValue;

public class ConvertTools {
	public static RetMapInMap convert(com.siteview.eccservice.RetMapInMap value){
		RetMapInMap ret = new RetMapInMap();
		ret.setEstr(value.getEstr());
		ret.setFmap(value.getFmap());
		ret.setRetbool(value.getRetbool());
		return ret;
	}
	public static RetMapInVector convert(com.siteview.eccservice.RetMapInVector value){
		RetMapInVector ret = new RetMapInVector();
		ret.setEstr(value.getEstr());
		ret.setVmap(value.getVmap());
		ret.setRetbool(value.getRetbool());
		return ret;
	}
	public static Vector<Map<String, String>> convert(List<Map<String, String>> value){
		Vector<Map<String, String>> ret = new Vector<Map<String, String>>();
		ret.addAll(value);
		return ret;
	}
	public static keyValue[] convert(Map<String, String> value){
		List<keyValue> retlist = new ArrayList<keyValue>();
		if (value==null)return retlist.toArray(new keyValue[retlist.size()]);
		for (String key : value.keySet()){
			retlist.add( new keyValue(key,value.get(key)));
		}
		return retlist.toArray(new keyValue[retlist.size()]);
	}
	public static Map<String,String> convert(keyValue[] keyValues){
		Map<String,String> retMap = new HashMap<String,String>();
		if (keyValues==null)return retMap;
		for (keyValue keyValue : keyValues){
			retMap.put(keyValue.getKey(), keyValue.getValue());
		}
		return retMap;
	}
	
	public static keyValue[][] convertTo(List<Map<String, String>> keyValues){
		List<keyValue[]> retlist = new ArrayList<keyValue[]>();
		if (keyValues==null) return retlist.toArray(new keyValue[retlist.size()][]);
		for (Map<String, String> map : keyValues){
			retlist.add(convert(map));
		}
		return retlist.toArray(new keyValue[retlist.size()][]);
	}
}
