package com.siteview.ecc.alert.control;

import java.util.LinkedHashMap;
import java.util.Map;

import com.siteview.ecc.alert.dao.ITextTemplateDao;
import com.siteview.ecc.alert.util.DictionaryFactory;

public class ScriptTemplateCombobox extends AbstractCombobox {
	private static final long serialVersionUID = 4965615794687662728L;

	@Override
	public Map<String,String> getSelectArray() {
		Map<String,String> map = new LinkedHashMap<String,String>();
		try {
			ITextTemplateDao textTemplateDao = DictionaryFactory.getITextTemplateDao();
			Map<String,String> mapTemplet = textTemplateDao.getScriptTemplet();
			for (String key : mapTemplet.keySet()){
				map.put(key, key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
