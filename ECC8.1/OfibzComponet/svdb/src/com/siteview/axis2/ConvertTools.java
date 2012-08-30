package com.siteview.axis2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.siteview.eccservice.keyValue;

public class ConvertTools {
	public static KeyValue[] convert(Map<String,String> map){
		List<KeyValue> retKeyValues = new ArrayList<KeyValue>();
		if (map==null)return retKeyValues.toArray(new KeyValue[retKeyValues.size()]);
		for (String key : map.keySet()){
			retKeyValues.add(new KeyValue(key,map.get(key)));
		}
		return retKeyValues.toArray(new KeyValue[retKeyValues.size()]);
	}
	public static Map<String,String> convert(KeyValue[] keyValues){
		Map<String,String> retMap = new HashMap<String,String>();
		if (keyValues==null)return retMap;
		for (KeyValue keyValue : keyValues){
			retMap.put(keyValue.getKey(), keyValue.getValue());
		}
		return retMap;
	}
	public static keyValue[] convertTo(KeyValue[] keyValues){
		List<keyValue> retlist = new ArrayList<keyValue>();
		if (keyValues==null)return retlist.toArray(new keyValue[retlist.size()]);
		for (KeyValue keyValue : keyValues){
			retlist.add( new keyValue(keyValue.getKey(),keyValue.getValue()));
		}
		return retlist.toArray(new keyValue[retlist.size()]);
	}
	public static KeyValue[] convertTo(keyValue[] keyValues){
		List<KeyValue> retlist = new ArrayList<KeyValue>();
		if (keyValues==null) return retlist.toArray(new KeyValue[retlist.size()]);
		for (keyValue keyValue : keyValues){
			retlist.add( new KeyValue(keyValue.getKey(),keyValue.getValue()));
		}
		return retlist.toArray(new KeyValue[retlist.size()]);
	}
	public static keyValue[][] convertTo(KeyValueArray[] keyValues){
		List<keyValue[]> retlist = new ArrayList<keyValue[]>();
		if (keyValues==null) return retlist.toArray(new keyValue[retlist.size()][]);
		for (KeyValueArray keyValue : keyValues){
			retlist.add(convertTo(keyValue.getValues()));
		}
		return retlist.toArray(new keyValue[retlist.size()][]);
	}

	public static AnyType2AnyTypeMapEntry[] convert(Map<String,Map<String,String>> map){
		List<AnyType2AnyTypeMapEntry> retKeyValues = new ArrayList<AnyType2AnyTypeMapEntry>();
		if (map==null) return retKeyValues.toArray(new AnyType2AnyTypeMapEntry[retKeyValues.size()]);
		for (String key : map.keySet()){
			retKeyValues.add(new AnyType2AnyTypeMapEntry(key,convert(map.get(key))));
		}
		return retKeyValues.toArray(new AnyType2AnyTypeMapEntry[retKeyValues.size()]);
	}
	public static Map<String,Map<String,String>> convert(AnyType2AnyTypeMapEntry[] keyValues){
		Map<String,Map<String,String>> retMap = new HashMap<String,Map<String,String>>();
		if (keyValues==null) return retMap;
		for (AnyType2AnyTypeMapEntry keyValue : keyValues){
			retMap.put(keyValue.getKey(), convert(keyValue.getValue()));
		}
		return retMap;
	}
	public static Vector<Map<String, String>> convert(KeyValueArray[] value){
		Vector<Map<String, String>> retList = new Vector<Map<String, String>>();
		if (value==null) return retList;
		for (KeyValueArray keyvalues : value){
			retList.add(convert(keyvalues.getValues()));
		}
		return retList;
	}
	public static KeyValueArray[] convert(Vector<Map<String, String>> value){
		List<KeyValueArray> retList = new ArrayList<KeyValueArray>();
		if (value==null) return retList.toArray(new KeyValueArray[retList.size()]);
		for (Map<String, String> map : value){
			KeyValueArray valueArray = new KeyValueArray();
			valueArray.setValues(convert(map));
			retList.add(valueArray);
		}
		return retList.toArray(new KeyValueArray[retList.size()]);
	}
	
	public static RetMapInVector convert(com.siteview.eccservice.RetMapInVector value){
		RetMapInVector retValue = new RetMapInVector();
		retValue.setEstr(value.getEstr());
		retValue.setRetbool(value.getRetbool());
		retValue.setVmap(convert(value.getVmap()));
		return retValue;
	}
	public static com.siteview.eccservice.RetMapInVector convert(RetMapInVector value){
		com.siteview.eccservice.RetMapInVector retValue = new com.siteview.eccservice.RetMapInVector();
		retValue.setEstr(value.getEstr());
		retValue.setRetbool(value.getRetbool());
		retValue.setVmap(convert(value.getVmap()));
		return retValue;
	}
	public static RetMapInMap convert(com.siteview.eccservice.RetMapInMap value){
		RetMapInMap retValue = new RetMapInMap();
		retValue.setEstr(value.getEstr());
		retValue.setRetbool(value.getRetbool());
		retValue.setFmap(convert(value.getFmap()));
		return retValue;
	}
	public static com.siteview.eccservice.RetMapInMap convert(RetMapInMap value){
		com.siteview.eccservice.RetMapInMap retValue = new com.siteview.eccservice.RetMapInMap();
		retValue.setEstr(value.getEstr());
		retValue.setRetbool(value.getRetbool());
		retValue.setFmap(convert(value.getFmap()));
		return retValue;
	}
}
