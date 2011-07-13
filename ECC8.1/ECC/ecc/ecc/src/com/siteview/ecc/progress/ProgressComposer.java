package com.siteview.ecc.progress;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.ComposerExt;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.treeview.report.PredigestTreeEventListener;

public class ProgressComposer extends GenericForwardComposer implements	ComposerExt {
	private final static Logger logger = Logger.getLogger(ProgressComposer.class);
	Timer  progressTimer;
	Window progressWindow;
	Progressmeter progressmeter;
	Button btnCancel;
	EventListener cancelListener;
	Label excutingInfoLabel;
	private ArrayList<IEccProgressmeter> tasks=new ArrayList<IEccProgressmeter>();
	
	public void doAfterCompose(Component comp) throws Exception 
	{
		super.doAfterCompose(comp);
		self.setAttribute("Composer",this);
		progressWindow.setAttribute("Composer",this);
		btnCancel.addEventListener("onClick",getCancelListener() );
		
		progressTimer.addEventListener("onTimer", new EventListener(){

			@Override
			public void onEvent(Event event) throws Exception {

					IEccProgressmeter eccProgressmeter=(IEccProgressmeter)progressmeter.getAttribute("eccProgressmeter");
					int percent=eccProgressmeter.getPercent();
					if(percent>=100)
						percent=100;
					progressmeter.setValue(percent);
					
					excutingInfoLabel.setValue(eccProgressmeter.getExcutingInfo());
					if(percent==100)
					{
						long time=System.currentTimeMillis()-Long.parseLong(progressmeter.getAttribute("startTimer").toString());
//						Thread.sleep(10000);
						btnCancel.setLabel("完成");
						if(time/(1000*60)<1)
							excutingInfoLabel.setValue("花费时间:"+(time/(1000))+"秒");
						else	
							excutingInfoLabel.setValue("花费时间:"+(time/(1000*60))+"分");
						progressTimer.setRunning(false);
					}

			}});
		
	}
	public void onClose$progressWindow()
	{
		IEccProgressmeter eccProgressmeter=(IEccProgressmeter)progressmeter.getAttribute("eccProgressmeter");
		if(eccProgressmeter!=null)
			eccProgressmeter.cancel();
	}
	public EventListener getCancelListener()
	{
		if(cancelListener==null)
		{
			cancelListener=new EventListener(){
			@Override
			public void onEvent(Event event) throws Exception 
			{
					IEccProgressmeter eccProgressmeter=(IEccProgressmeter)progressmeter.getAttribute("eccProgressmeter");
					eccProgressmeter.cancel();
					progressWindow.setVisible(false);
					if(((Button)event.getTarget()).getLabel().equals("完成")){
						//						Executions.getCurrent().sendRedirect(eccProgressmeter.getFinishUrl(), "_blank");
						showfile(eccProgressmeter.getFinishUrl());
					}
					
			}};
		}
		return cancelListener;
	}
	void showfile(String url) throws Exception{
		File file = new File(EccWebAppInit.getWebDir()+url);
		logger.info(file.getAbsolutePath());
		if(url.endsWith(".pdf")){
			long afterTime = 0;
			while (file.exists() == false && afterTime < 1000 * 30) {
				Thread.sleep(100);
				afterTime += 100;
			}
			Filedownload.save(file,"application/pdf");
		}else if(url.endsWith(".xls")){
			long afterTime = 0;
			while (file.exists() == false && afterTime < 1000 * 30) {
				Thread.sleep(100);
				afterTime += 100;
			}
			Filedownload.save(file,"application/vnd.ms-excel");
		}else{
			Executions.getCurrent().sendRedirect(url, "_blank");
		}
	}
}
