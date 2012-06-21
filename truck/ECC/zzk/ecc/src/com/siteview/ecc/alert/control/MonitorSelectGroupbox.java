package com.siteview.ecc.alert.control;

import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Label;

import com.siteview.base.data.UserRightId;
import com.siteview.ecc.alert.SelectTree;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.report.common.GroupLinkListener;
import com.siteview.ecc.util.LinkCheck;

public class MonitorSelectGroupbox extends Groupbox {
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
	
	public Label getGroupLabelLink(){
		return (Label)BaseTools.getComponentById(this, "groupLabelLink");
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
	
	public void groupLabelLink(Event event){
		boolean isLink = LinkCheck.getLinkCheck().CanSeeLink(UserRightId.WholeView);
		if(isLink){
			String style = "color:#18599C;cursor:pointer;text-decoration: underline;";
			Label label = (Label)event.getTarget();
			label.setStyle(style);
			label.addEventListener(Events.ON_CLICK,new GroupLinkListener(getViewNameCombobox().getValue()));
		}
	}
	
}
