package com.siteview.ecc.controlpanel;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SizeEvent;
import org.zkoss.zkex.zul.West;

public class EccLayoutWest extends West {
	private final static Logger logger = Logger.getLogger(EccLayoutWest.class);
	public void onCreate()
	{
		final EccLayoutWest eccLayoutWest=this;
		super.addEventListener("onSize",new EventListener(){
			@Override
			public void onEvent(Event event) throws Exception 
			{
				SizeEvent e=(SizeEvent)event;
				eccLayoutWest.setWestWidth(e.getTarget().getDesktop(),eccLayoutWest.getWidth());
			}});
	}
	public void setSize(String size)
	{
		super.setSize(size);
		setWestWidth(getDesktop(),size);
	}
	private void setWestWidth(Desktop desktop,String width)
	{
		if(width.startsWith("-")){
			logger.info("1111111");
			return;
		}
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
