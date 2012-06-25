package com.siteview.ecc.alert.control;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.siteview.base.data.VirtualView;
import com.siteview.base.manage.View;
import com.siteview.ecc.util.Toolkit;

public class ViewNameCombobox extends AbstractCombobox {
	private static final long serialVersionUID = -33323247080716778L;
	@Override
	public Map<String,String> getSelectArray() {
		Map<String,String> map = new LinkedHashMap<String,String>();
		try {
			View view = Toolkit.getToolkit().getSvdbView(getDesktop().getSession());
			List<VirtualView> viewAll = view.getAllVirtualView();
			for (VirtualView virtualview : viewAll){
				map.put(virtualview.getViewName(),virtualview.getViewName());
			}
		} catch (Exception e) {
		}
		return map;
	}

}
