package com.siteview.ecc.setmonitor;


import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;
import org.zkoss.zul.Button;

import com.siteview.base.manage.View;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.treeview.EccTreeModel;

public class ProcessMonitor extends Window{
	
	private static final long serialVersionUID = 6275995731975382475L;
	private SetBatch 				 			 setBatchWin;
	private Boolean								 running = true;
	
	public Button getCancelProcessButton(){
		return (Button)BaseTools.getComponentById(this, "cancel");
	}
	
	public Progressmeter getProgressmeter(){
		return (Progressmeter)BaseTools.getComponentById(this, "pm");
	}
	
	
	public Label getInfomationLabel(){
		return (Label)BaseTools.getComponentById(this, "infomation");
	}
	
	public EccTimer getEccTimer(){
		return (com.siteview.ecc.timer.EccTimer)Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("header_timer");
	}
	
	public EccTreeModel getEccTreeModel(){
		return (EccTreeModel) ((org.zkoss.zul.Tree) Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("tree")).getModel();
	}
	
	public View getView(){
		return getEccTreeModel().getView();
	}
	public void onCreate(){
	    setBatchWin = (SetBatch)getAttribute("setBatchWin");

		Thread thread = new runClass();

		thread.start();
		
		getCancelProcessButton().addEventListener(Events.ON_CLICK, new cancelProcess(thread));
		
		Timer timer = new Timer();

		timer.setDelay(100);
		
		timer.setRepeats(true);
	    
		timer.addEventListener(Events.ON_TIMER, new ontime(this));
		
		this.addEventListener(Events.ON_CLOSE, new onclose(thread));
		this.appendChild(timer);
		
	}
	
	private class onclose implements EventListener{
		Thread thread;
		public onclose(Thread thread){
			this.thread = thread;
		}
		@Override
		public void onEvent(Event arg0) throws Exception {
			if(!thread.isInterrupted()){
				thread.interrupt();
			}
		}
		
	}
	
	private boolean			flagSaveData = false;
	
	
	private void closeProcess(){
		this.detach();
	}
	
	class cancelProcess implements EventListener{
		Thread thread;
		public cancelProcess(Thread thread){
			this.thread = thread;
		}
		@Override
		public void onEvent(Event arg0) throws Exception {
			closeProcess();
			if(!thread.isInterrupted()){
				thread.interrupt();
			}
		}
		
	}
	
	
	class runClass extends Thread{
		public void run(){
			try {
				flagSaveData = setBatchWin.savedata();
			} catch (Exception e) {
				running = false;
				e.printStackTrace();
			}finally{
			}
			running = false;
		}
	}

	class ontime implements EventListener {
		ProcessMonitor view;
		ontime(ProcessMonitor view){

			this.view = view;
		}
		@Override
		public void onEvent(Event arg0){
			getProgressmeter().setValue(getProgressmeter().getValue()==100?0:getProgressmeter().getValue()+1);
			try{
				if(!running){
					detach();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
