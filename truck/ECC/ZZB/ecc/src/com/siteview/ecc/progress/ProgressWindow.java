package com.siteview.ecc.progress;

import java.util.ArrayList;

import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.siteview.ecc.util.Toolkit;

public class ProgressWindow extends Window 
{
	public void addProgress(IEccProgressmeter eccProgressmeter)
	{
		Progressmeter progressmeter=(Progressmeter)getFellow("progressmeter");
		Button btnCancel=(Button)getFellow("btnCancel");
		btnCancel.setLabel("È¡Ïû");
		btnCancel.setAction("");
		Timer progressTimer=(Timer)getFellow("progressTimer");
		progressTimer.setRunning(true);
		
		progressmeter.setValue(0);
		progressmeter.setAttribute("eccProgressmeter", eccProgressmeter);
		progressmeter.setAttribute("startTimer", System.currentTimeMillis());
		Thread thread = new Thread(eccProgressmeter);
		thread.setName("IEccProgressmeter -- ProgressWindow.java");
		thread.start();
	}
}

