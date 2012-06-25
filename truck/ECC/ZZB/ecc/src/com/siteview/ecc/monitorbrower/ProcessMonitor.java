package com.siteview.ecc.monitorbrower;

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
import org.zkoss.zul.Button;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.treeview.EccTreeModel;
import com.siteview.ecc.util.Toolkit;

public class ProcessMonitor extends Window{
	
	private static final long serialVersionUID = 6275995731975382475L;
	private MonitorBrowseComposer 				 mbc;
	private Boolean								 running = true;
	private View 							     eccView = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
	private static String	RefreshMonitors_TargetUrl	= "/main/monitorbrower/refreshmonitor.zul";
	private Listbox filterListbox = null;
	private Listitem filterItem = null;
	private CVBean bean = null;
	
	public Button getCancelProcessButton(){
		return (Button)BaseTools.getComponentById(this, "cancel");
	}
	public Progressmeter getProgressmeter(){
		return (Progressmeter)BaseTools.getComponentById(this, "pm");
	}
	
	public Label getFinishedLabel(){
		return (Label)BaseTools.getComponentById(this, "finish");
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
		mbc = (MonitorBrowseComposer)getAttribute("monitorImfo");
		filterListbox = mbc.getShowMonitorFilter();
		filterItem = filterListbox.getSelectedItem();
		if(filterItem==null)
		{
			this.detach();
			return;
		}
		bean = (CVBean)filterItem.getValue();
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
				thread.interrupt();/*interrupt()只改变线程的中断状态，sleep,wait,join的内部会不停的检查线程中断状态，如果它们检查到线程处于中断状态，就抛出异常中断线程。如果你的线程不处于这3个状态中，调用interrupt不会中断线程,或者说不会马上中断线程，如果后面的代码有让线程进入上面3个状态的其中一个，线程会马上抛出InterruptException而中断，因为你之前的interrupt()调用改变了线程的内部状态。*/
			}
		}
		
	}
	private MonitorDaomImpl info = new MonitorDaomImpl(getEccTreeModel(),getView());
	
	private List<MonitorBean> mbs = null;
	
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
				if(bean.getNodeId().equals("CV111")){
					mbs = info.getBrowseMost(mbc.getShowMonitorCount());
				}else if(bean.getNodeId().equals("CV222")){
					mbs = info.getErrorMost(mbc.getShowMonitorCount());
				}else{
					mbs = info.queryMonitorInfo(new MonitorFilter(bean));
				}
			} catch (Exception e) {
				running = false;
				e.printStackTrace();
			}finally{
			}
			running = false;
		}
	}
	private Image getImage(String status) {
		if (status == null || "null".equals(status)|| "".equals(status)) 
			return new Image("/images/state_dark.gif");
		else if (status.equals("bad"))
			return new Image("/images/state_grey.gif");
		else if (status.equals("error"))
			return new Image("/images/state_red.gif");
		else if (status.equals("ok"))
			return new Image("/images/state_green.gif");
		else if (status.equals("warning"))
			return new Image("/images/state_yellow.gif");
		else if(status.equals("disable"))
			return new Image("/images/state_stop.gif");
		return null;
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
					mbc.getBad().setValue(""+info.getBadCount());
					mbc.getError().setValue(""+info.getErrorCount());
					mbc.getWarn().setValue(""+info.getWarnCount());
					mbc.getOk().setValue(""+info.getOkCount());
					mbc.getForbid().setValue(""+info.getForbidCount());
					mbc.getNull().setValue(""+info.getNullCount());
					
					mbc.getMonitorInfo().getItems().clear();
					if(mbs==null) return;
					Listitem it = mbc.getShowMonitorFilter().getSelectedItem();
					CVBean bean = (CVBean)it.getValue();
					String sort = bean.getSort();
					sort(mbs,sort);
					MonitorInfoListbox monitorInfoListbox = (MonitorInfoListbox)mbc.getMonitorInfo();
				  	ChartUtil.clearListbox(monitorInfoListbox);
				  	monitorInfoListbox.setMbs(mbs);
				  	monitorInfoListbox.onCreate();	
	/*				for(MonitorBean value : mbs){
						Listitem item = new Listitem();
						String monitorTemp = value.getMonitorId();
						String entityId = "";
						if(monitorTemp == null || "".equals(monitorTemp)){
							continue;
						}
						
						entityId = monitorTemp.substring(0, monitorTemp.lastIndexOf("."));
						if(entityId.contains(".") == false){
							continue;
						}
						item = BaseTools.addRow(mbc.getMonitorInfo()
								, value
								, getImage(value.getStatus())
								,value.getGroup()
//								,value.getEntity()
								,BaseTools.getWithEntityLink(value,new EntityLinkFuntion(entityId,value.getMonitorId()))
//								,value.getMonitorName()
								,BaseTools.getWithMonitorLink(value,new MonitorDetailLinkFuntion(entityId,value.getMonitorId(),"btndetail"))							
								,BaseTools.getWithLink(
										"","编辑","/main/images/alert/edit.gif"
										,new onEditButtonListener(eccView.getNode(value.getMonitorId())
												,value.getMonitorName(),eccView,getEccTimer()))
								,BaseTools.getWithLink(
										"","刷新","/main/images/button/ico/ref_bt.gif"
										,new onRefreshButtonListener(value.getMonitorId()
												,value.getMonitorId().substring(0,value.getMonitorId().lastIndexOf(".")),item))
								,value.getUpdateTime(),value.getDescript());
					}*/
					getProgressmeter().setValue(getProgressmeter().getValue()<100?100:getProgressmeter().getValue());
					getFinishedLabel().setValue("完成！");
					getInfomationLabel().setValue("");
					detach();
				}
			}catch(Exception e){
				e.printStackTrace();
			}

		}
	}
	public void sort(List<MonitorBean> list,String field){
		if(field.equals("entity")){
			Collections.sort(list, new Comparator<MonitorBean>(){
				@Override
				public int compare(MonitorBean o1, MonitorBean o2) {
					return o1.getEntity().compareTo(o2.getEntity());
				}
			});
		}else if(field.equals("statusText")){
			Collections.sort(list, new Comparator<MonitorBean>(){
				@Override
				public int compare(MonitorBean o1, MonitorBean o2) {
					return o1.getDescript().compareTo(o2.getDescript());
				}
			});
		}else if(field.equals("name")){
			Collections.sort(list, new Comparator<MonitorBean>(){
				@Override
				public int compare(MonitorBean o1, MonitorBean o2) {
					return o1.getMonitorName().compareTo(o2.getMonitorName());
				}
			});
		}else if(field.equals("group")){
			Collections.sort(list, new Comparator<MonitorBean>(){
				@Override
				public int compare(MonitorBean o1, MonitorBean o2) {
					return o1.getGroup().compareTo(o2.getGroup());
				}
			});
		}else{//status
			Collections.sort(list, new Comparator<MonitorBean>(){
				@Override
				public int compare(MonitorBean o1, MonitorBean o2) {
					if(o1 == null || o2 == null){
						return 0;
					}
					return o1.getStatus().compareTo(o2.getStatus());
				}
			});
		}
			
	}
	public class onEditButtonListener implements EventListener {
		private EccTimer	 timer;
		private INode 		 node;
		private View         view;
		private String       name;
		public onEditButtonListener(INode node, String name, View view,
				EccTimer timer) {
			this.name = name;
			this.node = node;
			this.timer = timer;
			this.view = view;
		}

		public void onEvent(Event event) throws Exception {
			try {
				final Window win = (Window) Executions.createComponents(
						"/main/monitorbrower/WAddMonitor.zul", null, null);
				win.setAttribute("inode", node);
				win.setAttribute("view", view);
				win.setAttribute("isedit", true);
				win.setAttribute("entityname", name);
				win.setAttribute("eccTimer", timer);
				win.doModal();
			} catch (Exception e) {
				Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
						Messagebox.ERROR);
			}
		}
	}
	
	public class onRefreshButtonListener implements EventListener {
		private String id;
		private String parentId;
		private Listitem item;

		public onRefreshButtonListener(String id, String parentId,
				Listitem item) {
			this.id = id;
			this.parentId = parentId;
			this.item = item;
		}

		public void onEvent(Event event) {
			try
			{
				final Window win = (Window) Executions.createComponents(RefreshMonitors_TargetUrl, null, null);
				win.setAttribute("inode", eccView.getNode(parentId));
				win.setAttribute("view", ChartUtil.getView());
				win.setAttribute("eccTimer", getEccTimer());
				INode node = eccView.getNode(id);
				List<INode> list = new ArrayList<INode>();
				list.add(node);
				win.setAttribute("children", list);
				win.setAttribute("monitorItem", item);
				win.doModal();
			} catch (SuspendNotAllowedException e)
			{
				e.printStackTrace();
			} catch (Exception e)
			{
				e.printStackTrace();
			}	
		}
	}

}
