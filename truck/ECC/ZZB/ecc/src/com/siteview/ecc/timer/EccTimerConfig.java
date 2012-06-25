package com.siteview.ecc.timer;

import com.siteview.ecc.start.EccStarter;
import com.siteview.ecc.start.StarterListener;

public class EccTimerConfig implements StarterListener {

	private static EccTimerConfig eccTimerConfig;
	private String[] eccTimerOrder;
	public String[] getEccTimerOrder() {
		return eccTimerOrder;
	}

	@Override
	public void destroyed(EccStarter starter) {

	}

	@Override
	public void startInit(EccStarter starter) {
		eccTimerOrder=starter.getInitParameter("eccTimerOrder").split(",");
	}
	public static EccTimerConfig getInstance()
	{
		if(eccTimerConfig==null)
			eccTimerConfig=(EccTimerConfig)EccStarter.getInstance().getStarterListener("eccTimerConfig");
		return eccTimerConfig;
	}

}
