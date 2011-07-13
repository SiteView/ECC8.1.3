package com.siteview.ecc.report.monitorreport;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zhtml.Messagebox;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.siteview.ecc.report.beans.MonitorBean;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.treeview.EccWebAppInit;

public class ExportMonitorReportComposer extends GenericForwardComposer {

	private Listbox format;

	private Window exportmonitorreport;

	@SuppressWarnings("unchecked")
	public void onClick$saveReport(Event event) throws Exception {
		try {
			String reporttype = (String) (format.getSelectedItem().getValue());
			String jasperpath = EccWebAppInit.getWebDir()
					+ "main\\report\\monitorreport\\monitorReport.jasper";
			Map parameters = new HashMap();
			List<MonitorBean> beans = (List<MonitorBean>) exportmonitorreport
					.getAttribute("dataSource");
			MonitorInfoDatasource dataSource = new MonitorInfoDatasource(beans);
			AMedia media = null;
			
			if (reporttype.equals("pdf")) {
				media = ChartUtil.saveAsPdf(jasperpath, "监测器信息报告",parameters,
						dataSource);
			} else if (reporttype.equals("xls")) {
				media = ChartUtil.saveAsXls(jasperpath,"监测器信息报告", parameters,
						dataSource);
			} else if (reporttype.equals("html")) {
				media = ChartUtil.saveAsHtml(jasperpath,"监测器信息报告", parameters,
						dataSource);
			}
			Filedownload.save(media);
			exportmonitorreport.detach();
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
}
