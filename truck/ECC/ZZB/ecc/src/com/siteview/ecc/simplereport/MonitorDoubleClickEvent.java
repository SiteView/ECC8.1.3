package com.siteview.ecc.simplereport;


import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Listitem;

import com.siteview.base.treeInfo.MonitorInfo;

public class MonitorDoubleClickEvent implements org.zkoss.zk.ui.event.EventListener
{
	public MonitorDoubleClickEvent()
	{}

	@Override
	public void onEvent(Event event) throws Exception
	{
		// TODO Auto-generated method stub
		Listitem item=(Listitem)event.getTarget();
		MonitorInfo minfo=(MonitorInfo)item.getValue();
		Object usersessionid =item.getDesktop().getSession().getAttribute("usersessionid");
		item.getDesktop().getExecution().sendRedirect("/main/TreeView/WSimpleReport.zul?monitorId="+minfo.getSvId()+"&sid="+usersessionid.toString(),"_blank");
	}
	
}
