package com.siteview.ecc.monitorbrower.edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.tree.EntityNode;
import com.siteview.base.tree.GroupNode;
import com.siteview.base.tree.INode;
import com.siteview.base.tree.SeNode;
import com.siteview.base.treeInfo.IInfo;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.monitorbrower.EntityLinkFuntion;
import com.siteview.ecc.monitorbrower.MonitorBean;
import com.siteview.ecc.monitorbrower.MonitorDetailLinkFuntion;
import com.siteview.ecc.monitorbrower.ProcessMonitor;
import com.siteview.ecc.monitorbrower.MonitorInfoListbox.onEditButtonListener;
import com.siteview.ecc.monitorbrower.MonitorInfoListbox.onRefreshButtonListener;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.treeview.windows.RefreshDataBean;
import com.siteview.ecc.treeview.windows.RefreshMonitorModel;

public class RefreshMonitors extends GenericForwardComposer
{
	Listbox						lbmonitor;
	Button						btnclose;
	Label						lbfinish;
	Label                       lbrefresh;
	Window						wrefresh;
	Listitem					monitorItem;
	
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
		monitorItem = (Listitem)wrefresh.getAttribute("monitorItem");
		
		//修正里面的逻辑  
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
					List c = monitorItem.getChildren();
					c.clear();
					int index = node.getSvId().lastIndexOf(".");

					MonitorBean bean = new MonitorBean(refreshData.get(key).get("sv_id"),
							refreshData.get(key).get("status"), findSeNameById(node.getSvId().substring(0,index))+findGroupNameById(node.getSvId().substring(0,index)),
							findEntityNameById(node.getSvId()),
							refreshData.get(key).get("sv_name"), refreshData.get(key).get("creat_time"),
							refreshData.get(key).get("dstr"), refreshData.get(key).get("sv_monitortype"));
					
					
					String monitorId 	= bean.getMonitorId();
					if(monitorId == null ||"".equals(monitorId)){
						return;
					}
					String entityId = monitorId.substring(0, monitorId.lastIndexOf("."));
					if(entityId == null ||"".equals(entityId)){
						return;
					}
					if(entityId.contains(".") == false){
						return;
					}
					

					monitorItem.setValue(bean);
					String status = bean.getStatus();
					Listcell cell = new Listcell();
					setImage(cell, status);
					cell.setTooltiptext(status);
					cell.setParent(monitorItem);

					Listcell c2 = new Listcell(bean.getGroup());
					c2.setTooltiptext(bean.getGroup());
					c2.setParent(monitorItem);
					
					Listcell c3 = new Listcell();
					c3.setTooltiptext(bean.getEntity());
					Component component3 = BaseTools.getWithEntityLink(bean.getEntity(),new EntityLinkFuntion(entityId,monitorId));
					c3.appendChild(component3);
					c3.setParent(monitorItem);

					Listcell c4 = new Listcell();
					c4.setTooltiptext(bean.getMonitorName());
					Component component2 = BaseTools.getWithMonitorLink(bean.getMonitorName(),new MonitorDetailLinkFuntion(entityId,monitorId,"btndetail"));						
					c4.appendChild(component2);
					c4.setParent(monitorItem);

					Listcell edit = new Listcell();
					BaseTools.getWithLink(
							"",
							"编辑",
							"/main/images/alert/edit.gif",
							new ProcessMonitor().new onEditButtonListener(ChartUtil.getView().getNode(
									bean.getMonitorId()), bean.getMonitorName(), ChartUtil
									.getView(), eccTimer)).setParent(edit);
					edit.setParent(monitorItem);
					Listcell refresh = new Listcell();
					BaseTools.getWithLink(
							"",
							"刷新",
							"/main/images/button/ico/ref_bt.gif",
							new ProcessMonitor().new onRefreshButtonListener(bean.getMonitorId(), bean
									.getMonitorId().substring(0,
											bean.getMonitorId().lastIndexOf(".")), monitorItem))
							.setParent(refresh);
					refresh.setParent(monitorItem);
					Listcell c5 = new Listcell(bean.getUpdateTime());
					c5.setTooltiptext(bean.getUpdateTime());
					c5.setParent(monitorItem);
					
					Listcell cell6 = new Listcell(bean.getDescript());
					cell6.setTooltiptext(bean.getDescript());
					cell6.setParent(monitorItem);
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
	private void setImage(Listcell cell, String status) {
		if (status == null || status.equals("null")) {
			cell.setImage("/images/state_grey.gif");
			return;
		}
		if (status.equals("bad") || status.equals("error"))
			cell.setImage("/images/state_red.gif");
		if (status.equals("ok"))
			cell.setImage("/images/state_green.gif");
		if (status.equals("warning"))
			cell.setImage("/images/state_yellow.gif");
		if (status.equals("disable"))
			cell.setImage("/images/state_stop.gif");
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
	public  String findEntityNameById(String entityId) {
		EntityNode node = view.getEntityNode(entityId);
		return node!=null ? node.getName() : "";
	}
	public  String findGroupNameById(String groupId) {
		GroupNode gn = view.getGroupNode(groupId);
		if (gn == null)
			return "";
		else {
			String gName = gn.getName();
			int index = groupId.lastIndexOf(".");
			return findGroupNameById(groupId.substring(0, index)) + "/" + gName;
		}
	}
	
	public String findSeNameById(String groupId){
		GroupNode gn = view.getGroupNode(groupId);
		if (gn == null){
			SeNode sn = view.getSeNode(groupId);
			return sn.getName();
		}else {
			int index = groupId.indexOf(".");
			return findSeNameById(groupId.substring(0, index));
		}
	}

	public  String findGroupNameById2(String entityId) {
		INode node = view.getNode(entityId);
		if (node.getType().equals(INode.GROUP))
			return node.getName();
		else if (node.getType().equals(INode.ENTITY)) {
			int index = entityId.lastIndexOf(".");
			String groupId = entityId.substring(0, index);
			return findGroupNameById2(groupId);
		} else
			return "";
	}
}