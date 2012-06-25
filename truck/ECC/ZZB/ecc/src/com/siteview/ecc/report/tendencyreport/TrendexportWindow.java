package com.siteview.ecc.report.tendencyreport;

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

import com.siteview.base.data.Report;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.report.common.ReportServices;
import com.siteview.ecc.treeview.EccWebAppInit;

public class TrendexportWindow extends Window {
	private final static Logger logger = Logger.getLogger(TrendexportWindow.class);
	private static final long serialVersionUID = -2581931285241798683L;
	private Report report;
	private Date begin_Time;
	private Date end_Time;
	
	public Button getOkBtn(){
		return (Button)BaseTools.getComponentById(this, "saveReport");
	}
	
	public Listbox getFormatListbox(){
		return (Listbox)BaseTools.getComponentById(this, "format");
	}
	public void onCreate(){
		report = (Report)this.getAttribute("report");
		begin_Time = (Date)this.getAttribute("begin_Time");
		end_Time = (Date)this.getAttribute("end_Time");
		getOkBtn().addEventListener(Events.ON_CLICK, new onOkBtnClickedListener(this));
	}
	
	class onOkBtnClickedListener implements EventListener{
		Window window;
		public onOkBtnClickedListener(Window window){
			this.window = window;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void onEvent(Event arg0) throws Exception {
			String fileType = getFormatListbox().getSelectedItem().getValue().toString();
			String subDir = EccWebAppInit.getWebDir()+"main/report/exportreport/";
			List l1 = ReportServices.getRuntimeData(report);
			List l2 = ReportServices.buildstreamimage(report);
			Map parameter = new HashMap();
			String title = report.getPropertyValue("MonitorName")+"的趋势报告";
			parameter.put("title", title);
			parameter.put("subtitle", begin_Time.toLocaleString()+"~"+end_Time.toLocaleString());
			parameter.put("SUBREPORT_DIR", subDir);
			parameter.put("ds1", new TendencyDataSource(l1));
			parameter.put("ds2", new TendencyDataSource(l2));
			List l = new ArrayList();
			l.addAll(l1);
			l.addAll(l2);
			
			if(fileType.equals("html")){
				boolean flag = ChartUtil.saveAsHtml(subDir+"report.jasper",subDir,report.getPropertyValue("MonitorName")+"_趋势报告", parameter, new TendencyDataSource(l));
				logger.info(flag);
			}else if(fileType.equals("pdf")){
				AMedia media = ChartUtil.saveAsPdf(subDir+"report.jasper", report.getPropertyValue("MonitorName")+"_趋势报告",parameter, new TendencyDataSource(l));
				Filedownload.save(media);			
			}else{
				AMedia media = ChartUtil.saveAsXls(subDir+"report.jasper", report.getPropertyValue("MonitorName")+"_趋势报告",parameter, new TendencyDataSource(l));
				Filedownload.save(media);				
			}
			window.detach();
		}
	}
}
