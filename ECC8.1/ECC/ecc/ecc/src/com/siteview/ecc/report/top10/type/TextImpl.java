package com.siteview.ecc.report.top10.type;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;

public class TextImpl implements IComponent{
	private String text = null;
	public TextImpl(String text)
	{
		this.text = text;
	}
	private String getDisplayString() {
		return text;
	}
	
	@Override
	public Component getComponent() {
		return new Label(getDisplayString());
	}

}
