/**
 * 
 */
package com.siteview.ecc.log;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;

import com.siteview.base.data.IniFile;

/**
 * @author yuandong
 * @param <cb2_1>
 * 
 */
public class SysLogLevelSet<cb1_1> extends GenericAutowireComposer {
	private final static Logger logger = Logger.getLogger(SysLogLevelSet.class);
	private Checkbox cb2_1;
	private Checkbox cb2_2;
	private Checkbox cb2_3;
	private Checkbox cb2_4;
	private Checkbox cb2_5;
	private Checkbox cb2_6;
	private Checkbox cb2_7;
	private Checkbox cb2_0;
	private HashMap<String, Checkbox> cmap = new HashMap<String, Checkbox>();
	private Button applyButton2;

	public void onInit() {
		cmap.put("0", cb2_0);
		cmap.put("1", cb2_1);
		cmap.put("2", cb2_2);
		cmap.put("3", cb2_3);
		cmap.put("4", cb2_4);
		cmap.put("5", cb2_5);
		cmap.put("6", cb2_6);
		cmap.put("7", cb2_7);

	}

	public void onRefresh() throws Exception {
		IniFile ini = new IniFile("syslog.ini");
		try {
			ini.load();
//			ini.display();
			String value1 = ini.getValue("QueryCond", "Severities");
			String[] v = splString(value1, ",");
			for (int i = 0; i < 8; i++) {
				cmap.get(Integer.toString(i)).setChecked(false);
			}
			for (int i = 0; i < v.length; i++) {
				cmap.get(v[i]).setChecked(true);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String[] splString(String s, String regex) {

		return s.split(regex);
	}

	public void onApply(Event event) {
		IniFile ini = new IniFile("syslog.ini");
		String value = "";
		for (int i = 0; i < 8; i++) {
			if (cmap.get(Integer.toString(i)).isChecked())
				value = value + Integer.toString(i) + ",";

		}
		try {
			ini.load();
//			ini.display();
			logger.info(ini.getKeyList("QueryCond"));
			logger.info(ini.getSectionList());
			ini.setKeyValue("QueryCond", "Severities", value);
			ini.saveChange();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		applyButton2.setDisabled(true);

	}

}
