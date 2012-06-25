package com.siteview.ecc.alert.control;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.siteview.base.data.IniFile;
import com.siteview.ecc.alert.util.DictionaryFactory;

public class AlertPloyCombobox extends AbstractCombobox {
	private static final long serialVersionUID = 42922401224847842L;

	@Override
	public Map<String,String> getSelectArray() {
		Map<String,String> map = new LinkedHashMap<String,String>();
		try {
			
			IniFile alertPloyIniFile = DictionaryFactory.getAlertPloy();
			List<String> retlist = alertPloyIniFile.getSectionList();
			
			for (String key : retlist){
				map.put(key, key);
			}
			
		} catch (Exception e) {
		}
		return map;
	}

}
