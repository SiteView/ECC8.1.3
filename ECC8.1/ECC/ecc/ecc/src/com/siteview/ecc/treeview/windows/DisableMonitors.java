package com.siteview.ecc.treeview.windows;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.EntityInfo;
import com.siteview.base.treeInfo.GroupInfo;
import com.siteview.base.treeInfo.IInfo;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.treeview.EccTreeItem;

public class DisableMonitors extends GenericForwardComposer
{
	Window				wdisablemonitors;
	Radio				rdy;
	Radio				rdl;
	Datebox				dateform;
	Datebox				dateto;
	Timebox				timefrom;
	Timebox				timeto;
	Button				btndisable;
	Button				btnclose;
	
	View				view;
	INode				node;
	EccTimer			timer;
	ArrayList<INode>	children	= null;
	
	public DisableMonitors()
	{
		
	}
	public void onCheck$rdy()
	{
		if(rdy.isChecked())
		{
			dateform.setDisabled(true);
			dateto.setDisabled(true);
			timefrom.setDisabled(true);
			timeto.setDisabled(true);
			
		}
		
	}
	public void onCheck$rdl()
	{
		if(rdl.isChecked())
		{
			dateform.setDisabled(false);
			dateto.setDisabled(false);
			timefrom.setDisabled(false);
			timeto.setDisabled(false);
		}
	}
	public void onCreate$wdisablemonitors()
	{
		Date df = new Date();
		Date dt = new Date();
		// dt.setHours(df.getHours() + 2);
		dateform.setValue(df);
		dateto.setValue(dt);
		dateform.setDisabled(true);
		dateto.setDisabled(true);
		timefrom.setDisabled(true);
		timeto.setDisabled(true);
		
		timefrom.setValue(df);
		dt.setHours(df.getHours() + 2);
		timeto.setValue(dt);
		
		node = (INode) wdisablemonitors.getAttribute("inode");
		view = (View) wdisablemonitors.getAttribute("view");
		timer = (EccTimer) wdisablemonitors.getAttribute("eccTimer");
		try
		{
			children = (ArrayList<INode>) wdisablemonitors.getAttribute("children");
		} catch (Exception e)
		{
			
		}
	}
	
	private void dismonitors()
	{
		IInfo info = null;
		if (node.getType().equals(INode.ENTITY))
		{
			info = view.getEntityInfo(node);
		} else if (node.getType().equals(INode.GROUP))
		{
			info = view.getGroupInfo(node);
		} else if (node.getType().equals(INode.MONITOR))
		{
			info = view.getMonitorInfo(node);
		}
		Boolean success = false;
		String[] svids = null;
		INode[] ids = null;
		StringBuilder idss = new StringBuilder();
		Boolean isbatch = false;

		String hour="";
		String mint="";
		String senc="";
		if (children != null)
		{
			int l = children.size();
			svids = new String[l];
			ids = new INode[l];
			int i = 0;
			for (INode n : children)
			{
				String id = n.getSvId();
				svids[i] = id;
				idss.append(id + ",");
				ids[i] = n;
				i++;
			}
			isbatch = true;
		} else
		{
			ids = new INode[] { node };
		}
		if (rdy.isChecked())
		{
			
			try
			{
				if (isbatch)
				{
					success = info.disableMonitor(svids, null, null, view);
				} else
				{
					success = info.disableMonitor(null, null, view);
				}
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
			
		} else
		{
			Date dtfrom = dateform.getValue();
			dtfrom.setHours(timefrom.getValue().getHours());
			dtfrom.setMinutes(timefrom.getValue().getMinutes());
			Date dtto = dateto.getValue();
			dtto.setHours(timeto.getValue().getHours());
			dtto.setMinutes(timeto.getValue().getMinutes()); 
			
			
			
			if(dtfrom.compareTo(dtto)>=0  )
			{
				
				try
				{
					Messagebox.show("起始时间应该小于结束时间！", "提示", Messagebox.OK, Messagebox.EXCLAMATION);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
			Date dt=new Date();
			if(dt.after(dtfrom)&&dt.after(dtto))
			{
				try
				{
					Messagebox.show("禁止的时间段应该包含未来的时间！", "提示", Messagebox.OK, Messagebox.EXCLAMATION);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
			try
			{
				if (isbatch)
				{
					success = info.disableMonitor(svids, dateform.getValue(), dateto.getValue(), view);
				} else
				{
					success = info.disableMonitor(dateform.getValue(), dateto.getValue(), view);
				}
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
			
			if (success)
			{
				
				try
				{
					Long ls=GetDateMargin(new Date(),dtfrom);
					Long lhour=ls/(60*60);
					hour=lhour.toString();
					Long lmint=(ls-lhour*60*60)/60;
					mint=lmint.toString();
					Long lsenc=ls-lhour*60*60-lmint*60;
					senc=lsenc.toString();
					Messagebox.show("还有 "+hour+" 小时 "+mint+" 分钟  "+ senc+" 秒设备才会禁止监测！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (success)
		{
			if (node.getType().equals(INode.ENTITY)){//设备
				String loginname = view.getLoginName();
				String minfo = "";
				if (idss.indexOf(",") != -1)
				{
					minfo = "禁止设备：" + "(" + idss.toString() + ")";
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.diable, OpObjectId.entity);
				} else
				{
					minfo = "禁止设备：" + node.getName() + "(" + node.getSvId() + ")";
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.diable, OpObjectId.entity);
				}
			}else //监测器 部分
			{
				String loginname = view.getLoginName();
				String minfo = "";
				if (idss.indexOf(",") != -1)
				{
					minfo = "禁止监测：" + "(" + idss.toString() + ")";
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.diable, OpObjectId.monitor);
				} else
				{
					minfo = "禁止监测：" + node.getName() + "(" + node.getSvId() + ")";
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.diable, OpObjectId.monitor);
				}
			}
			Session session = Executions.getCurrent().getDesktop().getSession();
			HashMap<String, String> doMap = new HashMap<String, String>();
			doMap.put("dowhat", "disabled");
			doMap.put("type", node.getType());
			if(ids==null || ids.length==1){
				doMap.put("svId", node.getSvId());
			}else{
				doMap.put("svId", idss.toString());
			}
			if(session.getAttribute("doMap")!=null){
				session.removeAttribute("doMap");
			}
			session.setAttribute("doMap", doMap);
			timer.refresh(ids, ChangeDetailEvent.TYPE_MODIFY);
			wdisablemonitors.detach();
		} else
		{
			try
			{
				Messagebox.show("禁止监测失败：" + node.getName(), "提示", Messagebox.OK, Messagebox.EXCLAMATION);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	/**
	  *求两个日期差
	  *@param beginDate  开始日期
	  *@param endDate   结束日期
	  *@return 两个日期差的秒数
	  */
	public static long GetDateMargin(Date beginDate,Date endDate){
	    long margin = 0;

	    margin = endDate.getTime() - beginDate.getTime();

	    margin = margin/(1000);

	    return margin;
	}


	public void onClick$btndisable()
	{
		dismonitors();
	}
	
}
