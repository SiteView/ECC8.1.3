package com.siteview.ecc.report.common;

import java.util.List;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;

import com.siteview.ecc.alert.SelectTree;
import com.siteview.ecc.alert.util.BaseTools;

public class MonitorSelectDiv extends Div{
	private static final long serialVersionUID = -5151555478143581562L;
	/**
	 * 监测器选择树
	 * @return SelectTree
	 */
	public SelectTree getMonitorTree(){
		return (SelectTree)BaseTools.getComponentById(this,"monitortree");
	}

	/**
	 * 分组信息
	 * @return Combobox
	 */
	public Combobox getViewNameCombobox(){
		return (Combobox)BaseTools.getComponentById(this,"viewNamecombobox");
	}

	public String getValue(){
		return getMonitorTree().getAllSelectedIds();
	}
	public List<String> getArrayValue(){
		return getMonitorTree().getSelectedIds();
	}
	
	public void setValue(String value){
		this.getMonitorTree().setVariable("all_selected_ids", value, true);
	}
	
	public void setViewName(String viewname){
		getMonitorTree().setViewName(viewname);
	}
	
	public void doViewNameChange(){
		String viewName = this.getViewNameCombobox().getValue();
		if (viewName!=null){
			this.getMonitorTree().setViewName(viewName);
		}
	}
	
}
