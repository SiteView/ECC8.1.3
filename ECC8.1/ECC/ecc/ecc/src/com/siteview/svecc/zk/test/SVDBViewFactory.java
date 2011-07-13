package com.siteview.svecc.zk.test;

import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;

public class SVDBViewFactory {
	public static View getView(String loginName, String passWord) throws Exception{
		String session = Manager.createView(loginName, passWord);
		return Manager.getView(session);
	}
	
	public static View getView(String strSession) throws Exception{
		return Manager.getView(strSession);
	}
	
}
