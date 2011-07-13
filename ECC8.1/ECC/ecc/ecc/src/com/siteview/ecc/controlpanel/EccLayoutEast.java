package com.siteview.ecc.controlpanel;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SizeEvent;
import org.zkoss.zkex.zul.East;

public class EccLayoutEast extends East 
{
	public void onCreate()
	{
		final EccLayoutEast eccLayoutEast=this;
		super.addEventListener("onSize",new EventListener(){
			@Override
			public void onEvent(Event event) throws Exception 
			{
				SizeEvent e=(SizeEvent)event;
				eccLayoutEast.setEastWidth(e.getTarget().getDesktop(),eccLayoutEast.getWidth());
			}});
	}
	public void setSize(String size)
	{
		super.setSize(size);
		setEastWidth(getDesktop(),size);
	}
	private void setEastWidth(Desktop desktop,String width)
	{
		if(desktop.hasPage("controlPage"))
		{
			if(desktop.getPage("controlPage").hasFellow("controlLayout"))
			{
				EccLayout eccLayout=(EccLayout)desktop.getPage("controlPage").getFellow("controlLayout");
				eccLayout.setActionSize(Integer.parseInt(width.split("px")[0]));
				eccLayout.postEccLayoutEvent();
			}
		}

	}
}
