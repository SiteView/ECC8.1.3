package com.siteview.ecc.css;

import java.util.Map;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;

public class CssSelect extends Listbox 
{
	public void onCreate()
	{
		Map<String,String> themesMap=LoadCssFile.getCssNameMap();
		
		for(String css:themesMap.keySet())
		{
			String name=themesMap.get(css);
			Listitem item=new Listitem(css,css);
			item.setAction("onClick:loadjscssfile('"+css+"')");
			item.setParent(this);
		}
	}
}
