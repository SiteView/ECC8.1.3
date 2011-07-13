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

import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.IInfo;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.timer.EccTimer;

public class RefreshMonitors extends GenericForwardComposer
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
	
	ArrayList<INode>			children		= null;
	
	public RefreshMonitors()
	{
		
	}
	
	public void onCreate$wrefresh()
	{
		node = (INode) wrefresh.getAttribute("inode");
		view = (View) wrefresh.getAttribute("view");
		eccTimer = (EccTimer) wrefresh.getAttribute("eccTimer");
		try
		{
			children = (ArrayList<INode>) wrefresh.getAttribute("children");
		} catch (Exception ex)
		{
			
		}
		String nodetype = node.getType();
		String id = node.getSvId();
		if (!node.getType().equals(INode.ENTITY))
		{
			error_message = "节点类型不对！";
			return;
		}
		info = view.getEntityInfo(node);
		String[] ids = null;
		if (children != null)
		{
			int i = 0;
			ids=new String[children.size()];
			for (INode n : children)
			{
				ids[i]=n.getSvId();
				i++;
			}
		}
		
		
		if (info == null)
		{
			error_message = "节点不存在或无权访问！";
			return;
		}
		if(ids==null)
		{
			error_message = "没有可以刷新的监测器！";
			return;
		}
		try
		{
			
			qname = info.refresh(ids);
			
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
		time.setDelay(1000);
		time.setRepeats(true);
		time.setRunning(false);
		time.addEventListener("onTimer", new ontimer());
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
					lbrefresh.setValue("刷新完成...");
					INode[] ids = null;
					if (node.getType().equals(INode.ENTITY))
					{
						if (children != null)
						{
							ids = new INode[children.size()];
							int i = 0;
							for (INode n : children)
							{
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
			
			if(ncount==30)
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
