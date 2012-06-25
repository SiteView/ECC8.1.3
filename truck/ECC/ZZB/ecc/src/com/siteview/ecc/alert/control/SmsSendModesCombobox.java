package com.siteview.ecc.alert.control;

import java.util.LinkedHashMap;
import java.util.Map;


public class SmsSendModesCombobox extends AbstractCombobox {
	private static final long serialVersionUID = 4965615794687662728L;
	@Override
	public Map<String,String> getSelectArray() {
		Map<String,String> map = new LinkedHashMap<String,String>();
		map.put("Web", "Web");
		map.put("Com", "Com");
		map.put("SelfDefine", "SelfDefine");
		map.put("Smsdll.dll", "Smsdll.dll");
		map.put("DataBase", "DataBase");
		return map;
	}

}
