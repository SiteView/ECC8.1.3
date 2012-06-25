package com.siteview.ecc.report;

import java.awt.Color;
import java.util.ArrayList;
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

import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.report.beans.StateBean;
import com.siteview.ecc.report.beans.StateItem;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.report.statereport.ImageDatasource;
import com.siteview.ecc.report.statereport.StateDatasource;
import com.siteview.ecc.report.statereport.StateDscr;
import com.siteview.ecc.treeview.EccWebAppInit;

public class StateReportWindow extends Window {

	private static final long serialVersionUID = 2496783518154363186L;
	private static final Logger logger = Logger.getLogger(StateReportWindow.class);
	private List<Color> colorlist = new ArrayList<Color>();//用了画光谱图的数据
	private StateBean stateBean = null;//用来画统计图的数据
	private List<StateItem> stateItems = new ArrayList<StateItem>();//状态持续列表数据
	
	@SuppressWarnings("unchecked")
	public void onCreate(){
		colorlist = ( List<Color>)getAttribute("colorlist");
		stateBean = (StateBean)getAttribute("stateBeanData");
		stateItems = (List<StateItem>)getAttribute("persistTimeData");
		getSaveReportBtn().addEventListener(Events.ON_CLICK, new ExportReportListener(this));
	}

	public Button getSaveReportBtn(){
		return (Button)BaseTools.getComponentById(this, "saveReport");
	}
	public Listbox getFormatListbox(){
		return (Listbox)BaseTools.getComponentById(this, "format");
	}
	public class ExportReportListener implements EventListener{
		private Window win = null;
		public ExportReportListener(Window win){
			this.win = win;
		}
		@SuppressWarnings("unchecked")
		@Override
		public void onEvent(Event event) throws Exception {
			String fileType = getFormatListbox().getSelectedItem().getValue().toString();
			String subDir = EccWebAppInit.getWebDir()+"main/report/statereport/";
			Map parameter = new HashMap();
			String title = stateBean.getMonitorName()+"的状态统计报告";
			parameter.put("title", title);
			parameter.put("sub_dir", subDir);
			parameter.put("dataSource1", new StateDatasource(stateBean.cloneThis()));
			parameter.put("dataSource2", new ImageDatasource(stateBean.cloneThis()));
			parameter.put("dataSource3", new StateDscr(stateItems));
			parameter.put("dataSource4", new ImageDatasource(colorlist));
			
			if(fileType.equals("html")){
				boolean flag = ChartUtil.saveAsHtml(subDir+"stateImfoReport.jasper",subDir,stateBean.getMonitorName()+"_状态统计报告", parameter, new StateDatasource(stateBean.cloneThis()));
				logger.info(flag);
			}else if(fileType.equals("pdf")){
				AMedia media = ChartUtil.saveAsPdf(subDir+"stateImfoReport.jasper", stateBean.getMonitorName()+"_状态统计报告",parameter, new StateDatasource(stateBean.cloneThis()));
				Filedownload.save(media);			
			}else{
				AMedia media = ChartUtil.saveAsXls(subDir+"stateImfoReport.jasper", stateBean.getMonitorName()+"_状态统计报告",parameter, new StateDatasource(stateBean.cloneThis()));
				Filedownload.save(media);				
			}
			win.detach();
		}
	}
}
