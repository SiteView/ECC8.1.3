package com.siteview.ecc.alert.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.siteview.base.data.IniFile;
import com.siteview.ecc.alert.util.DictionaryFactory;

public class WatchSheetsCombobox extends AbstractCombobox {
	private static final long serialVersionUID = 42922401224847842L;

	@Override
	public Map<String,String> getSelectArray() {
		Map<String,String> map = new LinkedHashMap<String,String>();
		try {
			IniFile watchsheetcfgIniFile = DictionaryFactory.getWatchSheets();
			List<String> retlist = watchsheetcfgIniFile.getSectionList();
			
			ArrayList keylist = new ArrayList();
			for (String key : retlist){
				keylist.add(key);
			}
			Object[] strKeylist = keylist.toArray();
			Arrays.sort(strKeylist);
			for(Object key : strKeylist){
				map.put(key.toString(), key.toString());
			}
			
		} catch (Exception e) {
		}
		return map;
	}

}
