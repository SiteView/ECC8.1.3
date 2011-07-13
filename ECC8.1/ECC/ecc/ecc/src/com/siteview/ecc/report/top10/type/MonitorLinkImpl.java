package com.siteview.ecc.report.top10.type;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.monitorbrower.EntityLinkFuntion;
import com.siteview.ecc.monitorbrower.MonitorBean;
import com.siteview.ecc.treeview.EccTreeModel;

public class MonitorLinkImpl implements IComponent{
	private String text = null;
	private MonitorBean bean = null;
	public MonitorLinkImpl(MonitorBean bean ,String text)
	{
		this.bean = bean;
		this.text = text;
	}
	private String getDisplayString() {
		return text;
	}
	
	@Override
	public Component getComponent() {
		EccTreeModel model = EccTreeModel.getInstance(Executions.getCurrent().getDesktop().getSession());
		
		return BaseTools.getWithMonitorLink(getDisplayString(),new EntityLinkFuntion(model.getView().getMonitorNode(bean.getMonitorId()).getParentSvId(),bean.getMonitorId()));
	}

}
