package com.siteview.actions;

import com.siteview.base.data.IniFile;

/*��svdb����û�Ȩ�޵Ĵ󼯺�*/
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
