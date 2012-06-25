package com.siteview.ecc.report.statereport;

import java.util.List;

import com.siteview.ecc.report.beans.StateItem;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class StateDscr implements JRDataSource {
	private List<StateItem> stateItems;
	int index = -1;
	
	public StateDscr(List<StateItem> stateItems){
		this.stateItems = stateItems;
	}

	@Override
	public Object getFieldValue(JRField arg0) throws JRException {
		String name = arg0.getName();
		StateItem item = stateItems.get(index);
		Object obj = null;
		if("begin_time".equals(name)){
			obj = item.getBeginTime();
		}else if("state".equals(name)){
			obj =  item.getStatus();
		}else if("count".equals(name)){
			obj =  item.getCount();
		}else if("persisttime".equals(name)){
			obj =  item.getPersistTime();
		}
		return obj;
	}

	@Override
	public boolean next() throws JRException {
		if(stateItems == null ) return false;
		
		if(++index < stateItems.size())	return true;
		
		return false;
	}

}
