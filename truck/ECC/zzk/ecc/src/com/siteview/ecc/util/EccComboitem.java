package com.siteview.ecc.util;

import org.zkoss.zul.Comboitem;

public class EccComboitem extends Comboitem {
	public EccComboitem(String label,Object value,String imgSrc)
	{
		super(label);
		setValue(value);
		super.setImage(imgSrc);
	}
	public EccComboitem(String label,Object value)
	{
		super(label);
		setValue(value);
	}
}
