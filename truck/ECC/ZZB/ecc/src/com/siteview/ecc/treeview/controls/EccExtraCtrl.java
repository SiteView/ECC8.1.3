package com.siteview.ecc.treeview.controls;

import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.ext.client.Movable;
import org.zkoss.zk.ui.ext.client.Sizable;
import org.zkoss.zk.ui.ext.client.ZIndexed;
import org.zkoss.zk.ui.ext.render.PrologAllowed;

public class EccExtraCtrl implements Movable, Sizable,ZIndexed, PrologAllowed{

	HtmlBasedComponent htmlBasedComponent;
	public EccExtraCtrl(HtmlBasedComponent htmlBasedComponent)
	{
		this.htmlBasedComponent=htmlBasedComponent;
	}
	//-- Movable --//
	public void setLeftByClient(String left) {
		htmlBasedComponent.setLeft(left);
	}
	public void setTopByClient(String top) {
		htmlBasedComponent.setTop(top);
	}
	//-- Sizable --//
	public void setWidthByClient(String width) {
		htmlBasedComponent.setWidth(width);
	}
	public void setHeightByClient(String height) {
		htmlBasedComponent.setHeight(height);
	}
	//-- ZIndexed --//
	public void setZIndexByClient(int zIndex) {
		htmlBasedComponent.setZIndex(zIndex);
	}
	//-- PrologAware --//
	public void setPrologContent(String prolog) {
		
	}

}
