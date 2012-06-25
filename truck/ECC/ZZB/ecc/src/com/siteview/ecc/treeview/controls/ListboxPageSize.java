package com.siteview.ecc.treeview.controls;

import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;

import com.siteview.ecc.util.Toolkit;


public class ListboxPageSize extends Combobox {
	@Override
	public void onPageAttached(Page newpage, Page oldpage)
	{
		if(super.getItemCount()<=0)
		{
			appendChild(new EccComboitem("10","10"));
			appendChild(new EccComboitem("15","15"));
			appendChild(new EccComboitem("20","20"));
			appendChild(new EccComboitem("30","30"));
			appendChild(new EccComboitem("40","40"));
			appendChild(new EccComboitem("50","50"));
			appendChild(new EccComboitem("60","60"));
			appendChild(new EccComboitem("70","70"));
			appendChild(new EccComboitem("80","90"));
			appendChild(new EccComboitem("90","90"));
			appendChild(new EccComboitem("100","100"));
			appendChild(new EccComboitem("150","150"));
			appendChild(new EccComboitem("200","200"));
		}
	}
}
