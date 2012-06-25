package com.siteview.ecc.timer;

import org.zkoss.zk.ui.util.Statistic;

public class EccStatistic extends Statistic {

	private static EccStatistic instance=null;
	public EccStatistic() {
		super();
		instance=this;
	}
	public static EccStatistic getInstance()
	{
		return instance;
	}
}
