package com.siteview.ecc.util;

import java.util.ArrayList;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Image;
import org.zkoss.zul.Panel;

public class TitleChangedPanel extends Panel 
{
	Image img=null;
	ArrayList<EventListener> collapsibleListener=new ArrayList<EventListener>();;
	@Override
	public void setTitle(String title) {
		getCaption().setLabel(title);
	}

	public Caption getCaption()
	{
		if(super.getCaption()==null)
		{
			super.appendChild(new Caption());
		}
		return super.getCaption();
	}
	public void onCreate()
	{
		final TitleChangedPanel panel=this;

		img=new Image();
		img.setParent(getCaption());
		img.setSrc("/main/images/panel-exp.gif");
		img.setStyle("cursor:pointer");
		img.addEventListener("onClick",new EventListener(){
			@Override
			public void onEvent(Event event) throws Exception 
			{
				Event collapseEvent=null;
				if(img.getSrc().equals("/main/images/panel-exp.gif"))
				{
					img.setSrc("/main/images/panel-exp-colpsd.gif");
					collapseEvent=new Event("onCollapsible", panel, true);
				}
				else
				{
					img.setSrc("/main/images/panel-exp.gif");
					collapseEvent=new Event("onCollapsible", panel, false);
				}

				for(EventListener l:collapsibleListener)
					l.onEvent(collapseEvent);

			}});

	}
	
	public void addCollapsibleListener(EventListener l)
	{
		if(!collapsibleListener.contains(l))
			collapsibleListener.add(l);
	}
	

}
