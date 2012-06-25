package com.siteview.ecc.controlpanel;

import org.zkoss.zk.ui.Page;
import org.zkoss.zul.Column;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Space;

public class EccListhead extends Listhead {
	public EccListhead() {
		super();
		super.setSizable(true);
		super.setHeight("18px");
	}

	public void initHeader(EccListModel model)
	{
		Listheader selHeader=new Listheader("״̬");
		selHeader.setWidth("55px");
		selHeader.setAlign("left");
		selHeader.setStyle("width:55px;overflow:visible");
		selHeader.setParent(this);
		selHeader.setHeight("18px");
		selHeader.setValign("center");
		for(int i=0;i<model.getColCount();i++)
		{
			Listheader col=new Listheader(model.getTitle(i));
			col.setStyle("padding-top:8px");
			col.setHeight("18px");
			col.setValign("middle");
			col.setSortAscending(new EccRowComparator(model,i,model.isNumber(i),true));
			col.setSortDescending(new EccRowComparator(model,i,model.isNumber(i),false));
			if(model.forceColWidth(i)!=-1)
			{
				col.setMaxlength(model.forceColWidth(i));
				col.setWidth(model.forceColWidth(i)+"px");
			}
			if(model.isNumber(i))
				col.setAlign("center");
			else
				col.setAlign("left");
			
			col.setParent(this);
		}
		
	}
}
