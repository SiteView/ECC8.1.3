package com.siteview.ecc.alert.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import com.siteview.ecc.alert.dao.ITextTemplateDao;
import com.siteview.ecc.alert.util.DictionaryFactory;

public class EmailTemplateCombobox extends AbstractCombobox {
	private static final long serialVersionUID = 4965615794687662728L;

	@Override
	public Map<String,String> getSelectArray() {
		Map<String,String> map = new LinkedHashMap<String,String>();
		try {
			ITextTemplateDao textTemplateDao = DictionaryFactory.getITextTemplateDao();
			Map<String,String> mapTemplet = textTemplateDao.getMailTemplet();
			ArrayList keylist = new ArrayList();
			for (String key : mapTemplet.keySet()){
				keylist.add(key);
			}
			Object[] strKeylist = keylist.toArray();
			Arrays.sort(strKeylist);
			for(Object key : strKeylist){
				map.put(key.toString(), key.toString());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
