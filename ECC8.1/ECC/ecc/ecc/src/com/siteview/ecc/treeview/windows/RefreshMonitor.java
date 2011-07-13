package com.siteview.ecc.treeview.windows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import com.siteview.base.manage.Manager;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.EntityInfo;
import com.siteview.base.treeInfo.IInfo;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.treeview.EccTreeItem;

public class RefreshMonitor extends GenericForwardComposer
{
	
	Listbox						lbmonitor;
	Button						btnclose;
	Label						lbfinish;
	Label                       lbrefresh;
	Window						wrefresh;
	
	private String				error_message	= null;
	String						qname			= "";
	IInfo						info;
	Timer						time, timstop;
	private RefreshMonitorModel	model;
	private Boolean				timetag			= false;
	List						list;
	INode						node;
	View						view;
	
	EccTimer					eccTimer;
	ArrayList<EccTreeItem>		children		= null;
	
	public RefreshMonitor()
	{
		
	}
	
	public void onCreate$wrefresh()
	{
		node = (INode) wrefresh.getAttribute("inode");
		view = (View) wrefresh.getAttribute("view");
		//node=view.getNode(node.getSvId());
		eccTimer = (EccTimer) wrefresh.getAttribute("eccTimer");
		try
		{
			children = (ArrayList<EccTreeItem>) wrefresh.getAttribute("children");
		} catch (Exception ex)
		{
		}
		String nodetype = node.getType();
		String id = node.getSvId();
		if (node.getType().equals(INode.ENTITY))
		{
			info = view.getEntityInfo(node);
			StringBuilder sons = new StringBuilder();
			if (children != null)
			{
				int i = 1;
				for (EccTreeItem item : children)
				{
					INode n = item.getValue();
					if (i == 1)
					{
						sons.append(n.getSvId());
						i = 2;
					} else
					{
						sons.append("," + n.getSvId());
					}
				}
			}
			if (((EntityInfo) info).getSonId() == null || !sons.toString().equals(((EntityInfo) info).getSonId()))
			{
				((EntityInfo) info).setSonId(sons.toString());
			}
			String title="刷新设备("+node.getName()+")，以获取最新监测状态";
			wrefresh.setTitle(title);
		}
		if (node.getType().equals(INode.MONITOR))
		{
			info = view.getMonitorInfo(node);
			session.setAttribute(ConstantValues.RefreshMonitorId, id);
			String title="刷新监测器("+node.getName()+")，以获取最新监测状态";
			wrefresh.setTitle(title);
		}
		if (info == null)
		{
			error_message = "节点不存在或无权访问！";
			return;
		}
		try
		{
			
			qname = info.refresh();
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			error_message = e.getMessage();
		}
		if (error_message != null)
		{
			lbfinish.setValue(error_message);
			return;
		}
		if (qname == null)
		{
			return;
		}
		model = new RefreshMonitorModel();
		lbmonitor.setModel(model);
		lbmonitor.setItemRenderer(model);
		time = new Timer();
		time.setParent(wrefresh);
		time.setDelay(2000);			//刷新频率设为2秒
		time.setRepeats(true);
		time.setRunning(false);
		time.addEventListener("onTimer", new ontimer());
		try {
			Thread.sleep(3000);			//在定时器开始之前等待3秒
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		time.start();
		
		timstop = new Timer();
		timstop.setParent(wrefresh);
		timstop.setDelay(6000);
		timstop.setRepeats(false);
		timstop.setRunning(false);
		timstop.addEventListener("onTimer", new ontimerstope());
		
	}
	
	private class ontimerstope implements org.zkoss.zk.ui.event.EventListener
	{
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			try
			{
				time.stop();
				time.detach();
				time.setRunning(false);
				wrefresh.removeChild(time);
				wrefresh.detach();
			} catch (Exception ex)
			{
			}
		}
	}
	
	private class ontimer implements org.zkoss.zk.ui.event.EventListener
	{
		
		String	error_message	= null;
		private HashMap<String, String> statuslist=new LinkedHashMap<String, String>();
		private int ncount=0;
		public ontimer()
		{
			
		}
		
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			if (timetag)
			{
				if (error_message == null)
				{
					lbfinish.setValue("刷新完成。");
					System.out.println("刷新完成 OK");
					lbrefresh.setValue("刷新完成...");
					
					INode[] ids = null;
					if (node.getType().equals(INode.ENTITY))
					{
						if (children != null)
						{
							ids = new INode[children.size()];
							int i = 0;
							for (EccTreeItem item : children)
							{
								INode n=item.getValue();
								MonitorInfo minfo=new MonitorInfo(n);
								if (statuslist.containsKey(n.getSvId()))
								{
									String sta=statuslist.get(n.getSvId());
									minfo.setStatus(sta);
								}
								ids[i] = minfo;
								i++;
							}
						}
						
					} else
					{
						if (statuslist.containsKey(node.getSvId()))
						{
							String sta=statuslist.get(node.getSvId());
							((MonitorInfo)info).setStatus(sta);
						}
						ids = new INode[] { ((MonitorInfo)info) };
					}
					if (ids != null)
					{
						eccTimer.refresh(ids, ChangeDetailEvent.TYPE_MODIFY);
					}
					
				} else
				{
					lbfinish.setValue(error_message);
				}
				try
				{
					time.stop();
					time.detach();
					time.setRunning(false);
					timstop.start();
				} catch (Exception e)
				{
				}
				return;
			}
			Map<String, Map<String, String>> refreshData = null;
			RetMapInMap retData = null;
			try
			{
				// qname
				retData = info.getRefreshedData(qname);
				refreshData = retData.getFmap();
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				error_message = "获取刷新数据失败！";
				// error_message = e.getMessage();
				timetag = true;
				return;
			}
			Thread.sleep(1000);
			while(!Manager.isInstanceUpdated()){
				Thread.sleep(200);
			}
			String monitorname = "";
			String state = "";
			String dstr = "";
			String svid="";
			// model.add(new RefreshDataBean(monitorname, state, dstr));
			// model.add(new RefreshDataBean(monitorname, state, dstr));
			for (String key : refreshData.keySet())
			{
				if (key.startsWith("return"))
					continue;
				if (key.startsWith("RefreshData"))
				{
					monitorname = refreshData.get(key).get("sv_name");
					state = refreshData.get(key).get("status");
					svid=refreshData.get(key).get("sv_id");
					dstr = refreshData.get(key).get("dstr");
					statuslist.put(svid, state);
					model.add(new RefreshDataBean(monitorname, state, dstr));
				}
				
			}
			if (!refreshData.containsKey("RefreshData"))
			{
				ncount++;
			}else
			{
				ncount=0;
			}
			
			if(ncount==15)//if(ncount==30)时间太长，用15s取代
			{
				int r = Messagebox.show("暂时无法获取刷新数据，是否继续等待刷新？", "询问", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
			   if(r==Messagebox.CANCEL)
			   {
				   try
					{
						time.stop();
						time.detach();
						time.setRunning(false);
						wrefresh.removeChild(time);
						wrefresh.detach();
					} catch (Exception ex)
					{
					}
			   }else
			   {
				   ncount=0;
			   }
			}
			if (!retData.getRetbool())
				timetag = true;
			if (!retData.getEstr().isEmpty())
				error_message = retData.getEstr();
		}
	}
	
	public void onClick$btnclose()
	{
		try
		{
			time.stop();
			time.detach();
			time.setRunning(false);
			wrefresh.removeChild(time);
		} catch (Exception ex)
		{
		}
		wrefresh.detach();
	}
	
}
