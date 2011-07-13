package com.siteview.ecc.report.statisticalreport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;
import com.siteview.ecc.alert.util.LocalIniFile;
import com.siteview.ecc.report.Constand;

/**
 *简单监测信息处理类
 * 
 * @company: siteview
 * @author:di.tang
 * @date:2009-4-17
 */
public class SimpleMonitorInfoComposer extends GenericForwardComposer {
	Listbox allmonitors;
	Listbox simplemonitorinfolistbox;
	Window simplemonitorinfowindow;
	private String createSimpleInfoSection = null;
	private LocalIniFile ini = null;
	private List alltimesList = null;// 时间段
	private Hashtable<String, HashMap<String, String>> allmonitortypeHashtable = null;// 已定制的监测器

	private void getIniFile() {
		ini = new LocalIniFile(Constand.simplemonitorinfoinifilename);
		try {
			ini.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示
	 * 
	 * @param e
	 */
	public void onCreate$simplemonitorinfowindow(CreateEvent e) {
		createSimpleInfoSection = (String) e.getArg().get("createSimpleInfoSection");
		if (createSimpleInfoSection != null) {
			getIniFile();
			alltimesList = new ArrayList();
			allmonitortypeHashtable = new Hashtable<String, HashMap<String, String>>();
			// for (String a : this.ini.m_fmap.keySet()) {
			for (String a : this.ini.getSectionList()) {
				if(a.equals("TempSection(Please_modify_it)")) continue;
				if (a.startsWith(createSimpleInfoSection)) {
					alltimesList.add(a);
					HashMap<String, String> ht = null;
					// for (String x : this.ini.m_fmap.get(a).keySet()) {
					for (String x : this.ini.getSectionData(a).keySet()) {
						ht = allmonitortypeHashtable.get(x);
						if (ht == null) {
							ht = new HashMap<String, String>();
						}
						// ht.put(a, this.ini.m_fmap.get(a).get(x));
						ht.put(a, this.ini.getSectionData(a).get(x));
						allmonitortypeHashtable.put(x, ht);

					}
				}
			}
			Listitem o = new Listitem();
			o.setLabel("(空)$");
			allmonitors.appendChild(o);
			allmonitors.setSelectedItem(o);
			for (Object t : allmonitortypeHashtable.keySet()) {
				Listitem li = new Listitem();
				li.setLabel((String) t);
				allmonitors.appendChild(li);
			}
			// 获取报表所有生成的报告,时间段
		}
	}

	public void onGetSimpleMonitorInfo(SelectEvent event) {
		String selectlabel = allmonitors.getSelectedItem().getLabel();
		HashMap<String, String> need = allmonitortypeHashtable.get(selectlabel);
		List<Listitem> t = new ArrayList<Listitem>();
		for (Object e : simplemonitorinfolistbox.getItems()) {
			t.add((Listitem) e);
		}
		if (t.size() > 0) {
			for (Listitem p : t) {
				try {
					simplemonitorinfolistbox.removeChild(p);
				} catch (Exception e1) {
				}
			}
		}
		if (need == null)
			return;
		String times = null;
		String max = null;
		String min = null;
		String avg = null;
		String tem = null;
		for (String n : need.keySet()) {
			try {
				Listitem li = new Listitem();
				li.setHeight("23px");
				Listcell lc1 = new Listcell();
				Listcell lc2 = new Listcell();
				Listcell lc3 = new Listcell();
				Listcell lc4 = new Listcell();
				times = n.substring(n.indexOf("$"));
				times = times.substring(1);
				times = times.substring(0, times.length() - 1);
				times = times.replace("$", "~");
				lc1.setLabel(times);
				tem = need.get(n);
				min = tem.substring(0, tem.indexOf("$"));
				max = tem.substring(tem.lastIndexOf("$") + 1);
				avg = tem.substring(tem.indexOf("$") + 1, tem.lastIndexOf("$"));
				lc2.setLabel(min);
				lc3.setLabel(avg);
				lc4.setLabel(max);
				lc1.setParent(li);
				lc2.setParent(li);
				lc3.setParent(li);
				lc4.setParent(li);
				simplemonitorinfolistbox.appendChild(li);
			} catch (Exception e) {
			}
		}

	}
}
