package com.siteview.actions;

import com.siteview.base.data.IniFile;

/*读svdb获得用户权限的大集合*/
public class UserRightAll {
	private IniFile userrightIni;

	public IniFile getUserrightIni() 
	{
		if (userrightIni == null) {
			userrightIni = new IniFile("userright.ini");
			try 
			{
				userrightIni.load();
			} catch (Exception e) {
				userrightIni = null;
			}
		}
		return userrightIni;
	}
	
}
