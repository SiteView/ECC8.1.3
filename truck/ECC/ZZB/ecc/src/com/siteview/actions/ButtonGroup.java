package com.siteview.actions;

import java.util.ArrayList;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class ButtonGroup extends ArrayList
{
	private int current=0;
	private EventListener clickListener=null;
	private boolean useCookied=false;
	private String cookieId;
	
	public String getCookieId() {
		return cookieId;
	}
	public boolean isUseCookied() {
		return useCookied;
	}
	public ButtonGroup(boolean useCookied)
	{
		this.useCookied=useCookied;
		this.useCookied=false;
	}
	public ButtonGroup(boolean useCookied,String cookieId)
	{
		this.useCookied=useCookied;
		this.cookieId=cookieId;
	}
	public int getCurrent() {
		return current;
	}

	public void setCurrent(int idx)
	{
		if(idx>=0&&idx<size())
			setCurrentButton((ImageButton)get(idx));
	}
	public void setCurrentButton(ImageButton btn) 
	{
		ImageButton currentButton=(ImageButton)get(current);
		if(currentButton.equals(btn))
			return;
		
		currentButton.setClicked(false);/*设置上一个current按钮被弹起*/
		
		btn.setClicked(true);/*设置新的current被按下*/
		this.current=indexOf(btn);

	}
	public ImageButton getCurrentButton() {
		return (ImageButton)get(current);
	}
	public ButtonGroup addButton(ImageButton btn)
	{
		add(btn);
		btn.setButtonGroup(this);
		btn.setUseCookied(useCookied);
		if(clickListener!=null)
			btn.setClickListener(clickListener);
		return this;
	}
	public void setClickListener(EventListener listener)
	{
		for(Object obj:this)
			((ImageButton)obj).setClickListener(listener);
	}
}
