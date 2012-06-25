package com.siteview.ecc.report.models;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.siteview.base.data.Report;
import com.siteview.base.data.ReportDate;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.ecc.report.beans.RuntimeReportBean;
import com.siteview.ecc.report.common.ChartUtil;

public class RuntimeReportListModel extends ListModelList implements ListitemRenderer {

	private List<List<Report>>        			simpleReports;

	public RuntimeReportListModel(ReportDate rd) {
		super();
		View v = ChartUtil.getView();
		for (String id : rd.getNodeidsArray()) {
			INode node = v.getNode(id);
			List<RuntimeReportBean> list = new ArrayList<RuntimeReportBean>();
			for (int i = 0; i < rd.getReturnSize(id); i++) {
				String name = rd.getReturnValue(id, "MonitorName", i);
				String returnName = rd.getReturnValue(id, "ReturnName", i);
				String max = rd.getReturnValue(id, "max", i);
				String min = rd.getReturnValue(id, "min", i);
				String latest = rd.getReturnValue(id, "latest", i);
				String average = rd.getReturnValue(id, "average", i);
				String when_max = rd.getReturnValue(id, "when_max", i);
				if(ChartUtil.isShowReport(node, i)){
					list.add(new RuntimeReportBean(name, returnName, min, average, max, latest, when_max));
				}
			}
			addAll(list);
		}
	}
	public RuntimeReportListModel(List<ReportDate> rdList) {
		View v = ChartUtil.getView();
		for(ReportDate rd : rdList){
			List<RuntimeReportBean> list = new ArrayList<RuntimeReportBean>();
			for (String id : rd.getNodeidsArray()) {
				INode node = v.getNode(id);
				for (int i = 0; i < rd.getReturnSize(id); i++) {
					String name = rd.getReturnValue(id, "MonitorName", i);
					String returnName = rd.getReturnValue(id, "ReturnName", i);
					String max = rd.getReturnValue(id, "max", i);
					String min = rd.getReturnValue(id, "min", i);
					String latest = rd.getReturnValue(id, "latest", i);
					String average = rd.getReturnValue(id, "average", i);
					String when_max = rd.getReturnValue(id, "when_max", i);
					if(ChartUtil.isShowReport(node, i)){
						list.add(new RuntimeReportBean(name, returnName, min,  max, average,latest, when_max));
					}
				}
			}
			addAll(list);
		}
	}
	/**
	 * 生成运行情况报告数据
	 * 
	 * @return
	 */
	public void runtimeState() {
		if (simpleReports == null) {
			return;
		}
		for (List<Report> reports : simpleReports) {
			List<RuntimeReportBean> list = new ArrayList<RuntimeReportBean>();
			for (Report simpleReport : reports) {
				for (int i = 0; i < simpleReport.getReturnSize(); i++) {
					String name = simpleReport.getReturnValue("MonitorName", i);
					String returnName = simpleReport.getReturnValue("ReturnName", i);
					String max = simpleReport.getReturnValue("max", i);
					String min = simpleReport.getReturnValue("min", i);
					String latest = simpleReport.getReturnValue("latest", i);
					String average = simpleReport.getReturnValue("average", i);
					String when_max = simpleReport.getReturnValue("when_max", i);
					list.add(new RuntimeReportBean(name, returnName, min, average, max, latest, when_max));
				}
				addAll(list);
			}
		}
	}

	@Override
	public void render(Listitem arg0, Object arg1) throws Exception {
		Listitem item = arg0;
		RuntimeReportBean m = (RuntimeReportBean) arg1;
		String name = m.getName();
		Listcell l1 = new Listcell(name);
		l1.setTooltiptext(name);
		l1.setParent(item);
		
		String rn = m.getReturnName();
		Listcell l2 = new Listcell(rn);
		l2.setTooltiptext(rn);
		l2.setParent(item);
		
		String max = m.getMax();
		Listcell l3 = new Listcell(max);
		l3.setTooltiptext(max);
		l3.setParent(item);
		
		String avg= m.getAverage();
		Listcell l4 = new Listcell(avg);
		l4.setTooltiptext(avg);
		l4.setParent(item);
		
		String min = m.getMin();
		Listcell l5 = new Listcell(min);
		l5.setTooltiptext(min);
		l5.setParent(item);
		
		String latest = m.getLastest();
		Listcell l6 = new Listcell(latest);
		l6.setTooltiptext(latest);
		l6.setParent(item);
		
		String lt = m.getLasttime();
		Listcell l7 = new Listcell(lt);
		l7.setTooltiptext(lt);
		l7.setParent(item);
	}
}
