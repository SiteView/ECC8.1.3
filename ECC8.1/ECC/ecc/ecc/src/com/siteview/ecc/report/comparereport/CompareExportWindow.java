package com.siteview.ecc.report.comparereport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.siteview.base.data.ReportDate;
import com.siteview.base.manage.View;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.report.common.ReportServices;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.util.Toolkit;

public class CompareExportWindow extends Window {
	private final static Logger logger = Logger.getLogger(CompareExportWindow.class);
	private static final long serialVersionUID = 5917163640208351810L;
	private List<ReportDate> 			reports;
	private List<INode>						nodes;
	private Date 						beginDate;
	private Date 						endDate;
	public Button getOkBtn(){
		return (Button)BaseTools.getComponentById(this, "saveReport");
	}
	
	public Listbox getFormatListbox(){
		return (Listbox)BaseTools.getComponentById(this, "format");
	}
	public void onCreate(){
		reports = (List<ReportDate>)this.getAttribute("report");
		nodes = (List<INode>)this.getAttribute("nodes");
		getOkBtn().addEventListener(Events.ON_CLICK, new onOkBtnClickedListener(this));
	}
	
	class onOkBtnClickedListener implements EventListener{
		Window window;
		View v = ChartUtil.getView();
		public onOkBtnClickedListener(Window window){
			this.window = window;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void onEvent(Event arg0) throws Exception {
			String fileType = getFormatListbox().getSelectedItem().getValue().toString();
			String subDir = EccWebAppInit.getWebDir()+"main/report/exportreport/";
			List l1 = ReportServices.getRuntimeData(reports);
			List l2 = ReportServices.buildImageMaps(reports);
			for(ReportDate r:reports){
				beginDate = r.getM_begin_date();
				endDate = r.getM_end_date();
				break;
			}
			StringBuffer sb = new StringBuffer();
			if(reports.size()==1){
				INode node = v.getNode(reports.get(0).getNodeidsArray()[0]);
				if(node!=null) {
					MonitorInfo info = v.getMonitorInfo(node);
					MonitorTemplate tmplate = null;
					if(info!=null) tmplate = info.getMonitorTemplate();
					if(tmplate!=null)
					sb.append(tmplate.get_sv_name()).append("类型的监测器对比报告");
				}
			}else if(reports.size()>1){
				INode node = v.getNode(reports.get(0).getNodeidsArray()[0]);
				INode node2 = v.getNode(reports.get(1).getNodeidsArray()[0]);
				sb.append(node.getName()).append("与").append(node2.getName()).append("等监测器的对比报告");
			}
			Map parameter = new HashMap();
			parameter.put("title", sb.toString());
			parameter.put("subtitle", Toolkit.getToolkit().formatDate(beginDate)+"~"+Toolkit.getToolkit().formatDate(endDate));
			parameter.put("SUBREPORT_DIR", subDir);
			parameter.put("ds1", new ComparereportDatasource(l1));
			parameter.put("ds2", new ComparereportDatasource(l2));
			List l = new ArrayList();
			l.addAll(l1);
			l.addAll(l2);
			if(fileType.equals("html")){
				boolean flag = ChartUtil.saveAsHtml(subDir+"report.jasper",subDir,sb.toString(), parameter, new ComparereportDatasource(l));
				logger.info(flag);
			}else if(fileType.equals("pdf")){
				AMedia media = ChartUtil.saveAsPdf(subDir+"report.jasper",sb.toString(), parameter, new ComparereportDatasource(l));
				Filedownload.save(media);			
			}else{
				AMedia media = ChartUtil.saveAsXls(subDir+"report.jasper", sb.toString(),parameter, new ComparereportDatasource(l));
				Filedownload.save(media);				
			}
			window.detach();
		}
	}
}
