package com.siteview.ecc.report.top10.type;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

import com.siteview.base.tree.INode;
import com.siteview.ecc.alert.dao.type.HboxWithSortValue;
import com.siteview.ecc.treeview.EccWebAppInit;

public class NumberImpl implements IComponent{
	private double value = 0;
	private String status = null;
	private String displayValue = null;
	public NumberImpl(double value,String displayValue,String status)
	{
		this.value = value;
		this.status = status;
		this.displayValue = displayValue;
	}
	private String getDisplayString() {
		return displayValue;
	}
	
	private String getImage(){
		if ("ok".equals(status)){
			return "/main/report/top10/images/Bar_Green.gif";
		}else if ("error".equals(status)){
			return "/main/report/top10/images/Bar_Red.gif";
		}else if ("warnning".equals(status)){
			return "/main/report/top10/images/Bar_Yellow.gif";
		}
		return "/main/report/top10/images/Bar_Red.gif";
	}
	@Override
	public Component getComponent() {
		HboxWithSortValue hbox = new HboxWithSortValue();
		Label label = new Label(this.getDisplayString());
		label.setWidth("20px");
		label.setParent(hbox);
		Image alertimage =  new Image(this.getImage());
		alertimage.setWidth(value + "px");
		alertimage.setAlign("left");
		alertimage.setParent(hbox);
		hbox.setSortValue(getDisplayString());
		return hbox;
	}

}
