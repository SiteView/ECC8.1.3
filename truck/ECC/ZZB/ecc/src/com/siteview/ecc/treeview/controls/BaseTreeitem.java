package com.siteview.ecc.treeview.controls;

import org.zkoss.zul.Treeitem;

public class BaseTreeitem extends Treeitem {
	private boolean checked=false;

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public BaseTreeitem getParentItem()
	{
		return (BaseTreeitem)super.getParentItem();
	}
}
