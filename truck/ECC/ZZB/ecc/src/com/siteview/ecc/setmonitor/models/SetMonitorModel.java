package com.siteview.ecc.setmonitor.models;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.siteview.ecc.setmonitor.beans.SetMonitorBean;

public class SetMonitorModel extends ListModelList implements ListitemRenderer  {

	public SetMonitorModel(List<Map<String, String>> list){
		super();
		try {
			createSetMonitorInfo(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<SetMonitorBean> createSetMonitorInfo(List<Map<String, String>> list) throws Exception{
		List<SetMonitorBean> beans = new ArrayList<SetMonitorBean>();
		
/*		for(int i =0;i<list.size();i++ )
		{
			HashMap<String,String> mapValue = list.get(i);
			String[] MonitorName = mapValue.get("MonitorName").toString().split(":", 2);
			SetMonitorBean bean = new SetMonitorBean(
					MonitorName[1],
					mapValue.get("MonitorFrequency"),
					mapValue.get("OkConditon")+mapValue.get("WarnConditon")+mapValue.get("ErrorConditon"),
					mapValue.get("Status"));
			beans.add(bean);
		}
*/		
		for(Map<String,String> mapValue:list )
		{
			String[] MonitorName = mapValue.get("MonitorName").toString().split(":", 2);
			SetMonitorBean bean = new SetMonitorBean(
					MonitorName[1],
					mapValue.get("MonitorFrequency"),
					mapValue.get("OkConditon")+mapValue.get("WarnConditon")+mapValue.get("ErrorConditon"),
					mapValue.get("Status"));
			beans.add(bean);
		}
		
		
		addAll(beans);
		return beans;
	}
	
	@Override
	public void render(Listitem arg0, Object arg1) throws Exception{
		Listitem item = arg0;
		item.setSelected(true);
		SetMonitorBean m = (SetMonitorBean) arg1;

		item.setValue(m);
		Listcell c1 = new Listcell(m.getMonitorName());
		c1.setParent(item);
		Listcell c2 = new Listcell(m.getFrequency());
		c2.setParent(item);
		Listcell c3 = new Listcell(m.getKeyValue());
		c3.setParent(item);
		Listcell c4 = new Listcell(m.getStatus());
		c4.setParent(item);
	}
}
