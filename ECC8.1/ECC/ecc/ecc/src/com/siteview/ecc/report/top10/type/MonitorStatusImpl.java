package com.siteview.ecc.report.top10.type;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

import com.siteview.base.tree.INode;
import com.siteview.ecc.alert.dao.type.HboxWithSortValue;
import com.siteview.ecc.treeview.EccWebAppInit;

public class MonitorStatusImpl implements IComponent{
	private String status = null;
	public MonitorStatusImpl(String status)
	{
		this.status = status;
	}
	private String getDisplayString() {
		return status;
	}
	
	private String getImage(){
		return EccWebAppInit.getInstance().getImage(INode.MONITOR,status);
	}
	@Override
	public Component getComponent() {
		HboxWithSortValue hbox = new HboxWithSortValue();
		Image alertimage =  new Image(this.getImage());
		alertimage.setAlign("middle");
		Label label = new Label(this.getDisplayString());
		alertimage.setParent(hbox);
		label.setParent(hbox);
		hbox.setSortValue(getDisplayString());
		return hbox;
	}

}
