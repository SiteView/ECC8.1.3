package com.siteview.ecc.alert.control;

import java.util.LinkedHashMap;
import java.util.Map;

import com.siteview.ecc.alert.dao.type.AlertCategory;


public class AlertEventCombobox extends AbstractCombobox {
	private static final long serialVersionUID = 4965615794687662728L;

	@Override
	public Map<String,String> getSelectArray() {
		Map<String,String> map = new LinkedHashMap<String,String>();
		for (AlertCategory alertcategory : AlertCategory.getAll()){
			if (alertcategory == AlertCategory.Normal) continue;
			map.put(alertcategory.getStringVaule(),alertcategory.getDisplayString());
		}
		return map;
		
	}

}
