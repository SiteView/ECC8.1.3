package com.siteview.ecc.alert.dao.type;

import org.zkoss.zul.Hbox;

public class HboxWithSortValue extends Hbox {
	private static final long serialVersionUID = 8000956769716781169L;
	private String sortvalue = null;
	public String getSortValue() {
		return sortvalue;
	}
	public void setSortValue(String sortvalue) {
		this.sortvalue = sortvalue;
	}

}
