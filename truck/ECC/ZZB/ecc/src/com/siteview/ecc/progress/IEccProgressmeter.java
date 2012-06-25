package com.siteview.ecc.progress;

import java.util.ArrayList;

public interface IEccProgressmeter extends Runnable{
	public String getExcutingInfo();
	public int getPercent();
	public void cancel();
	public void run();
	public ArrayList<String> getErrors();
	public String getFinishUrl();
}
