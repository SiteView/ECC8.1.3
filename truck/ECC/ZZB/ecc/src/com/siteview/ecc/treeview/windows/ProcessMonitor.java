package com.siteview.ecc.treeview.windows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;
import org.zkoss.zul.Button;

import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.treeview.EccTreeModel;
import com.siteview.ecc.util.Toolkit;

public class ProcessMonitor extends Window{
	
	private static final long serialVersionUID = 6275995731975382475L;
	private TeleBackup 				 			 teleBackupWin;
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
		teleBackupWin = (TeleBackup)getAttribute("teleBackupWin");

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
				flagSaveData = teleBackupWin.savedata();
			} catch (Exception e) {
				running = false;
				e.printStackTrace();
			}finally{
				running = false;
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
