package com.siteview.actions;

import java.util.ArrayList;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Image;

import com.siteview.ecc.util.Toolkit;

public class ImageButton extends Image implements EventListener {
	private boolean clicked = false;
	private boolean cookied = false;
	private boolean defaultPressed = false;
	private boolean isInit = false;
	private boolean useCookied = false;
	private ButtonGroup buttonGroup = null;

	public ButtonGroup getButtonGroup() {
		return buttonGroup;
	}

	public void setButtonGroup(ButtonGroup buttonGroup) {
		this.buttonGroup = buttonGroup;
	}


	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getName().equals("onClick")) {
			if (buttonGroup != null) {
				if (buttonGroup.getCurrentButton().equals(
						(ImageButton) event.getTarget()))
					return;
				buttonGroup.setCurrentButton((ImageButton) event.getTarget());
				if (useCookied) 
					Toolkit.getToolkit().setCookie(buttonGroup.getCookieId(),
						String.valueOf(buttonGroup.getCurrent()), getDesktop(),
						999999999);
			} else {
				setClicked(!clicked);
				if (useCookied) 
					Toolkit.getToolkit().setCookie(getSrc(),
						clicked ? "true" : "false", getDesktop(), 999999999);
			}

			clickListener.onEvent(event);
		}
	}


	public boolean isUseCookied() {
		return useCookied;
	}

	public void setUseCookied(boolean useCookied) {
		this.useCookied = useCookied;
	}


	private EventListener clickListener = null;

	public boolean isCookied() {
		return cookied;
	}

	public boolean isClicked() {
		if(!isInit)
			init();
		return clicked;
	}

	public void setClicked(boolean clicked) {
		this.clicked = clicked;
		this.cookied = true;

		setSclass(clicked ? "insetBtnStyle" : "outsetBtnStyle");

	}
	public void onCreate()
	{
		if(!isInit)
			init();
	}
	public void init() 
	{
		isInit = true;
		if (useCookied) 
		{
			String cookiedValue = Toolkit.getToolkit().getCookie((buttonGroup==null)?getSrc():buttonGroup.getCookieId(),getDesktop());
			if (cookiedValue != null) 
			{
				cookied = true;
				if (buttonGroup == null) {
					clicked = cookiedValue.equals("true")?true:false;
				} else {
					buttonGroup.setCurrent(Integer.parseInt(cookiedValue));
				}
			}
		}
		setSclass(clicked ? "insetBtnStyle" : "outsetBtnStyle");
		addEventListener("onClick", this);
	}

	public void setClickListener(EventListener listener) {
		clickListener = listener;
	}

	public void setDefaultPressed(boolean defaultPressed) {
		this.defaultPressed = defaultPressed;
		this.clicked = defaultPressed;
	}
}
