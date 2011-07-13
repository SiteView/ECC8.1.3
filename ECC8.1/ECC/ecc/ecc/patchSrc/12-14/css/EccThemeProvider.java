package com.siteview.ecc.css;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.util.ThemeProvider;

import com.siteview.ecc.util.Toolkit;

public class EccThemeProvider implements ThemeProvider {

	@Override
	public Collection getThemeURIs(Execution exec, List uris) 
	{
		for (Iterator it = uris.iterator(); it.hasNext();) 
		{
			   if (it.next().toString().startsWith("~./eccTheme"))
			   it.remove(); 			//remove the ecc theme
		}
		
		HttpServletRequest req = (HttpServletRequest)exec.getNativeRequest();
		Object uri=getMyThemeURI(req);
		if(uri!=null)
			uris.add(uri);
		return uris;
	}
	
	public Object getMyThemeURI(HttpServletRequest req)
	{
		String eccTheme=Toolkit.getToolkit().getCookie("eccTheme", req);
		
		
		if(eccTheme==null||eccTheme.length()==0)
			eccTheme= "eccThemeBlue";		
		return "~./"+eccTheme+"/img.css.dsp";
	}
	
}
