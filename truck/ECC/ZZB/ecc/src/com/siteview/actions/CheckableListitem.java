package com.siteview.actions;

import org.zkoss.zul.Listitem;

public class CheckableListitem extends Listitem {
	public CheckableListitem() {
		super();
	}
	public CheckableListitem(String label, Object value) {
		super(label, value);
	}
	public CheckableListitem(String label) {
		super(label);
	}
	public void setChecked(boolean checked)
	{
		super.setSelected(checked);
	}
	public boolean isChecked()
	{
		return super.isSelected();
	}
}
