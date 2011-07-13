package com.siteview.ecc.report.top10;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Executions;

import com.siteview.ecc.monitorbrower.MonitorBean;
import com.siteview.ecc.monitorbrower.MonitorDaomImpl;
import com.siteview.ecc.report.top10.type.IComponent;
import com.siteview.ecc.report.top10.type.MonitorLinkImpl;
import com.siteview.ecc.report.top10.type.MonitorStatusImpl;
import com.siteview.ecc.report.top10.type.TextImpl;
import com.siteview.ecc.treeview.EccTreeModel;


public class TopTenBrowseImpl implements TopTen{

	
	@Override
	public List<Map<String, IComponent>> getData() throws Exception {
		List<Map<String, IComponent>> retlist = new LinkedList<Map<String, IComponent>>();
		
		EccTreeModel model = EccTreeModel.getInstance(Executions.getCurrent().getDesktop().getSession());
		MonitorDaomImpl info = new MonitorDaomImpl(model,model.getView());

		List<MonitorBean> list = info.getBrowseMost(10);
		
		for (MonitorBean bean : list){
			Map<String, IComponent> map = new HashMap<String, IComponent>();
			map.put("id", new MonitorLinkImpl(bean,bean.getMonitorId()));
			map.put("状态", new MonitorStatusImpl(bean.getStatus()));
			map.put("名称", new TextImpl(bean.getMonitorName()));
			retlist.add(map);
		}
		return retlist;
	}

	@Override
	public List<String> getTitles() throws Exception {
		List<String> retlist = new LinkedList<String>();
		retlist.add("id");
		retlist.add("状态");
		retlist.add("名称");
		return retlist;
	}

	@Override
	public String getCaption() throws Exception {
		return "Top 10 查看最多的检测器";
	}
	
}