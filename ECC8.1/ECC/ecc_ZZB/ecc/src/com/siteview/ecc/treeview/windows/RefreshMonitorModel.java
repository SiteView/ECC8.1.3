package com.siteview.ecc.treeview.windows;

import java.util.List;

import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class RefreshMonitorModel extends ListModelList implements ListitemRenderer
{
	
	public RefreshMonitorModel()
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void render(Listitem arg0, Object arg1) throws Exception
	{
		// TODO Auto-generated method stub
		Listitem item = arg0;
		item.setStyle("background:white;");
		RefreshDataBean m = (RefreshDataBean) arg1;
		String src = "/images/state_green.gif";
		new Listcell(m.getMonitorname()).setParent(item);
		if (m.getState().equals("error"))
		{
			src = "/images/state_red.gif";
		} else if (m.getState().equals("warnning"))
		{
			src = "/images/state_yellow.gif";
		} else if (m.getState().equals("disable"))
		{
			src = "/images/state_stop.gif";
		} else if (m.getState().equals("bad"))
		{
			src = "/images/state_grey.gif";
		} else if (m.getState().equals("null"))
		{
			src = "/images/state_grey.gif";
		}
		Listcell iconcell = new Listcell();
		iconcell.setImage(src);
		iconcell.setParent(item);
		Listcell cell=new Listcell(m.getDstr());
		cell.setParent(item);
		cell.setTooltiptext(m.getDstr());
	}
}
