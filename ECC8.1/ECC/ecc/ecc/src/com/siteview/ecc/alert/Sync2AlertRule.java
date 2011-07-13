package com.siteview.ecc.alert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.siteview.base.data.IniFile;
import com.siteview.ecc.alert.util.DictionaryFactory;

public class Sync2AlertRule {

	/**
	 * ���豸����Ӽ����ʱ������澯����Ӧ���˸澯���ԣ���ִ�и澯���Թ���
	 * @param monitorId
	 * @param monitorTemplateId
	 * @throws Exception 
	 * @throws Exception
	 */
	public static void sync2AlertRule(String monitorId, String monitorTemplateId) throws Exception {
		IniFile alertploy = null;
		IniFile alert = null;
		try {alertploy = DictionaryFactory.getAlertPloy();} catch (Exception e) {}
		try {alert = DictionaryFactory.getAlert();} catch (Exception e) {}
		Map<String, Map<String, String>> alertployMap = alertploy.getFmap();
		Map<String, Map<String, String>> alertMap = alert.getFmap();
		for (String key : alertMap.keySet()) {
			Map<String, String> value = alertMap.get(key);
			String strategy = value.get("Strategy");
			if (strategy == null || "".equals(strategy)) continue;
			if (alertployMap.containsKey(strategy) == false)  continue;
			String parentId = monitorId.substring(0,monitorId.lastIndexOf("."));
			Map<String,String> alertployValue = alertployMap.get(strategy);
			if (alertployValue.containsKey(parentId) == false) continue;
			List<String> typeList = Arrays.asList(alertployValue.get(parentId).split(","));
			if (typeList.contains(monitorTemplateId) == false) continue;
			String alerttarget = alertMap.get(key).get("AlertTarget");
			alert.setKeyValue(key, "AlertTarget", alerttarget + "," + monitorId);
		}
		try {alert.saveChange();} catch (Exception e) {e.printStackTrace();}
	}
	
	/**
	 * ����һ���豸�������ʱ������丸�ڵ������˸澯���ԣ���ô���Լ�Ҳ�����õ��澯������
	 * @param objectId
	 * @throws Exception 
	 * @throws Exception
	 */
	public static void syncEntity2AleryStrategy(String objectId) throws Exception {
		IniFile alertploy = null;
		try {alertploy = DictionaryFactory.getAlertPloy();} catch (Exception e) {}
		Map<String, Map<String, String>> alertployMap = alertploy.getFmap();
		int index = objectId.lastIndexOf(".");
		if (index < 0) return;
		String parent = objectId.substring(0,index);
		for (String key : alertployMap.keySet()) {
			Map<String, String> value = alertployMap.get(key);
			if (value.containsKey(objectId) == false) continue;
			alertploy.setKeyValue(key, objectId, value.get(parent));
		}
		try {alertploy.saveChange();} catch (Exception e) {}
	}
}
