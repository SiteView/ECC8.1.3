package com.siteview.actions;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Image;

import com.siteview.ecc.util.Toolkit;

public class HelpImageButton extends Image implements EventListener
{
	final private String	outsetBtnStyle	= "cursor:pointer;border-color:#c0c0c0;border-width:1px;border-style: outset;background-color:#FFFFFF";
	final private String	insetBtnStyle	= "cursor:pointer;border-color:#c0c0c0;border-width:1px;border-style: inset;background-color:#E7FBFF";
	private boolean			clicked			= false;
	
	public void setClicked(boolean clicked)
	{
		this.clicked = clicked;
		
		if (clicked)
		{
			setStyle(insetBtnStyle);
			
		} else
		{
			setStyle(outsetBtnStyle);
			
		}
		
	}
	
	public void onCreate()
	{
		setStyle(outsetBtnStyle);
		addEventListener("onClick", this);
	}
	
	@Override
	public void onEvent(Event event)
	{
		setClicked(!clicked);
	}
}
