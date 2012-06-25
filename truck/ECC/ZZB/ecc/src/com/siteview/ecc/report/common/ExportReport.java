package com.siteview.ecc.report.common;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Textbox;

import com.siteview.base.data.Report;
import com.siteview.base.data.ReportDate;
import com.siteview.base.data.Report.DstrItem;
import com.siteview.ecc.reportserver.Constand;
import com.siteview.ecc.reportserver.DirectoryZip;
import com.siteview.ecc.simplereport.HistoryBean;
import com.siteview.ecc.simplereport.Reportdatasource;
import com.siteview.ecc.simplereport.SimpleReport;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.util.Toolkit;

/**
 * 导出报表
 * 
 * @author di.tang 2009-05-19
 */
public class ExportReport {
	private Report report = null;
	Textbox savepath;
	String reporttype = null;// 报告格式(HTML,XLS,PDF);
	String jasperpath = null;
	String filename = null;
	String monitorId = (String) Executions.getCurrent().getDesktop().getSession().getAttribute("myREPORTNODEID");
	String sessionId = (String) Executions.getCurrent().getDesktop().getSession().getAttribute("usersessionid");
	String strZipDirPath = null;
	ReportDate rd;

	public ExportReport(String filename) {
		strZipDirPath = Constand.downloadreportpath;
		this.filename = filename;
	}

	public ExportReport(Report report, String reporttype, String jasperpath) {
		try {
		this.report = report;
		this.reporttype = reporttype;
		this.jasperpath = jasperpath;
			if (report != null && reporttype != null && jasperpath != null) {
				this.filename = report.getPropertyValue("MonitorName").replace("$", "").replace(":", "_").replace(" ", "_").replace("|", "_")
						.replace("(", "_").replace(")", "_");
				strZipDirPath = Constand.downloadreportpath;
				String file = null;
				// 生成报告文件;
				if ("html".equals(reporttype)) {
					this.getHtml();
					file = "/main/report/downloadreport/" + this.filename + ".zip";
					this.copyFiles(reporttype, filename);
					this.downloadReport(file);
				} else if ("xls".equals(reporttype)) {
					Reportdatasource ds = new Reportdatasource(buildBean("HistoryBean"));
					Filedownload.save(ChartUtil.saveAsXls(jasperpath, this.filename,getParaMap(), ds));
	//				this.getXls();
	//				file = "/main/report/downloadreport/" + this.filename + ".xls";
				} else if ("pdf".equals(reporttype)) {
					Reportdatasource ds = new Reportdatasource(buildBean("HistoryBean"));
					Filedownload.save(ChartUtil.saveAsPdf(jasperpath, this.filename,getParaMap(), ds));
//					this.getPdf();
//					file = "/main/report/downloadreport/" + this.filename + ".pdf";
				}
				// 拷贝文件并压缩打包ZIP
			}
		} catch (Exception e) {
			try {
				Messagebox.show(e.getMessage(), "错误", Messagebox.OK, Messagebox.ERROR);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		// 最后下载
	}

	public void copyFiles(String strReportType, String filename) {
		String strSrcHtmlPath = Constand.downloadreportpath + filename;
		String strDestHtmlPath = Constand.downloadreportpath + "zip\\" + filename;

		String strSrcDirPath = null;
		String strDestDirPath = null;
		// com.siteview.ecc.tuopu.MakeTuopuData.delFile(strDestHtmlPath);
		try {
			com.siteview.ecc.tuopu.MakeTuopuData.delFolder(strZipDirPath + "zip\\");
			com.siteview.ecc.tuopu.MakeTuopuData.createFolder(strZipDirPath + "zip\\");
			com.siteview.ecc.tuopu.MakeTuopuData.delFile(strZipDirPath + filename + ".zip");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("html".equals(strReportType)) {
			// 组建压缩包目录
			strSrcDirPath = strSrcHtmlPath + ".html_files";
			strSrcHtmlPath = strSrcHtmlPath + ".html";
			strDestDirPath = strDestHtmlPath + ".html_files";
			strDestHtmlPath = strDestHtmlPath + ".html";
			com.siteview.ecc.tuopu.MakeTuopuData.copyFolder(strSrcDirPath, strDestDirPath);
			try {
				com.siteview.ecc.tuopu.MakeTuopuData.copyFile(strSrcHtmlPath, strDestHtmlPath);
				// 压缩文件
				this.createZip();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} else if ("pdf".equals(strReportType)) {
			strSrcHtmlPath = strSrcHtmlPath + ".pdf";
			strDestHtmlPath = strDestHtmlPath + ".pdf";
		} else if ("xls".equals(strReportType)) {
			strSrcHtmlPath = strSrcHtmlPath + ".xls";
			strDestHtmlPath = strDestHtmlPath + ".xls";
		}

	}

	public void createZip() {
		DirectoryZip zip = new DirectoryZip();
		try {
			zip.zip(strZipDirPath + "zip\\", strZipDirPath + this.filename + ".zip");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Map getParaMap() {
		Map<Date, DstrItem> dstrs = this.report.getDstr();
		if (dstrs.isEmpty())
			return null;
	
		Iterator itm = dstrs.keySet().iterator();
		String mindate = Toolkit.getToolkit().formatDate((Date) itm.next());
		String maxdate = this.report.getPropertyValue("latestCreateTime");
		String subtitle = "时段：" + mindate + "~" + maxdate;

		// Reportdatasource ds = new Reportdatasource(buildBean("HistoryBean"));
		Reportdatasource subds1 = new Reportdatasource(buildBean("MonitorBean"));
		Reportdatasource subds2 = new Reportdatasource(buildBean("StatisticsBean"));
		Reportdatasource subds3 = null;
		subds3 = new Reportdatasource(buildBean("ImageBean"));
		Map parameters = new HashMap();
		parameters.put("ReportTitle", filename);
		parameters.put("subReportTitle", subtitle);
		String path = EccWebAppInit.getWebDir();
		parameters.put("SUBREPORT_DIR", path + "/main/report/");
		parameters.put("IS_IGNORE_PAGINATION", true);
		parameters.put("SUBREPORT_DIRfilename", path + "/main/report/report_subreport2.jasper");
		parameters.put("subDS1", subds1);
		parameters.put("subDS2", subds2);
		parameters.put("subDS3", subds3);
		return parameters;
	}

	private void getHtml() {
		Reportdatasource ds = new Reportdatasource(buildBean("HistoryBean"));
		try {
			JasperRunManager.runReportToHtmlFile(this.jasperpath, Constand.downloadreportpath + filename + ".html", this.getParaMap(), ds);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	private void getXls() {
		JRXlsExporter exporter = new JRXlsExporter();
		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		Reportdatasource ds = new Reportdatasource(buildBean("HistoryBean"));
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(this.jasperpath, this.getParaMap(), ds);
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		} catch (JRException e1) {
			e1.printStackTrace();
		}
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		try {
			exporter.exportReport();
		} catch (JRException e) {
			e.printStackTrace();
		}
		//
		byte[] bytes = oStream.toByteArray();
		OutputStream out=null;
		try 
		{
			out = new FileOutputStream(Constand.downloadreportpath + filename + ".xls");
			out.write(bytes);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally
		{
			try{out.close();}catch(Exception e){}
		}

	}

	private void getPdf() {
		Reportdatasource ds = new Reportdatasource(buildBean("HistoryBean"));
		try {
			JasperRunManager.runReportToPdfFile(this.jasperpath, Constand.downloadreportpath + filename + ".pdf", this.getParaMap(), ds);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public void downloadReport(String file) {

		try {
			Filedownload.save(file, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private List buildBean(String tag) {

		List list = new ArrayList();
		if (tag.equals("MonitorBean")) {
			String okPercent = this.report.getPropertyValue("okPercent");
			String warnPercent = this.report.getPropertyValue("warnPercent");
			String errorPercent = this.report.getPropertyValue("errorPercent");
			Map<Date, DstrItem> dstrs = this.report.getDstr();

			float dis = this.report.getDisablePercentOfSimpleReport();
			String disablePercent = Float.toString(dis);
			String errorCondition = this.report.getPropertyValue("errorCondition");

			list.add(new com.siteview.ecc.simplereport.MonitorBean(filename, okPercent, warnPercent, errorPercent, disablePercent,
					errorCondition));

		}
		if (tag.equals("StatisticsBean")) {

			for (int i = 0; i < this.report.getReturnSize(); i++) {
				String drawmeasure = this.report.getReturnValue("sv_drawmeasure", i);
				drawmeasure = drawmeasure.isEmpty() ? "0" : drawmeasure;
				if (!drawmeasure.equals("1")) {
					continue;
				}
				String returnName = this.report.getReturnValue("ReturnName", i);
				String max = this.report.getReturnValue("max", i);
				String latest = this.report.getReturnValue("latest", i);
				String average = this.report.getReturnValue("average", i);
				list.add(new com.siteview.ecc.simplereport.StatisticsBean(filename, returnName, max, average, latest));
			}

		}
		if (tag.equals("HistoryBean")) {

			Map<Date, DstrItem> dstrs = this.report.getDstr();
			List errorlist = new ArrayList();
			List dangerlist = new ArrayList();
			List oklist = new ArrayList();
			List disablelist = new ArrayList();
			List elselist = new ArrayList();
			for (Date D : dstrs.keySet()) {
				String state = dstrs.get(D).status;
				String dstr = dstrs.get(D).value;
				if (state.equals("ok")) {
					state = "正常";
					oklist.add(new com.siteview.ecc.simplereport.HistoryBean(filename,Toolkit.getToolkit().formatDate(D), dstr, state));
				} else if (state.equals("error")) {
					state = "错误";
					errorlist.add(new com.siteview.ecc.simplereport.HistoryBean(filename, Toolkit.getToolkit().formatDate(D), dstr, state));
				} else if (state.equals("disable")) {
					state = "禁止";
					disablelist.add(new com.siteview.ecc.simplereport.HistoryBean(filename, Toolkit.getToolkit().formatDate(D), dstr, state));
				} else if (state.equals("warning")) {
					state = "危险";
					dangerlist.add(new com.siteview.ecc.simplereport.HistoryBean(filename, Toolkit.getToolkit().formatDate(D), dstr, state));
				} else {
					elselist.add(new com.siteview.ecc.simplereport.HistoryBean(filename, Toolkit.getToolkit().formatDate(D), dstr, state));
				}

				// list.add(new
				// com.siteview.ecc.simplereport.HistoryBean(monitorName,
				// D.toLocaleString(), dstr, state));
			}
			if (errorlist.size() > 0) {
				Iterator item = errorlist.iterator();
				while (item.hasNext()) {
					list.add((HistoryBean) item.next());
				}
			}
			if (dangerlist.size() > 0) {
				Iterator item = dangerlist.iterator();
				while (item.hasNext()) {
					list.add((HistoryBean) item.next());
				}
			}
			if (oklist.size() > 0) {
				Iterator item = oklist.iterator();
				while (item.hasNext()) {
					list.add((HistoryBean) item.next());
				}
			}
			if (disablelist.size() > 0) {
				Iterator item = disablelist.iterator();
				while (item.hasNext()) {
					list.add((HistoryBean) item.next());
				}
			}
			if (elselist.size() > 0) {
				Iterator item = elselist.iterator();
				while (item.hasNext()) {
					list.add((HistoryBean) item.next());
				}
			}
		}
		if (tag.equals("ImageBean")) {
			list = buildimage();
		}
		if (tag.equals("ImageBeanpdf")) {
			list = buildstreamimage();
		}
		return list;
	}

	private List<InputStream> buildstreamimage() {
		Map<Integer, Map<String, String>> listimage = SimpleReport.getImagelist(this.report);
		if (listimage.size() == 0) {
			return null;
		}
		List<InputStream> list = new ArrayList<InputStream>();
		for (int key : listimage.keySet()) {
			String id = monitorId + sessionId + key;
			Map<Date, String> imgdata = this.report.getReturnValueDetail(key);
			Map<String, String> keyvalue = listimage.get(key);
			XYDataset data = SimpleReport.buildDataset(imgdata);
			BufferedImage temmap = null;
			String maxdate = keyvalue.get("maxdate");
			Date maxd = null;
			if (!maxdate.isEmpty()) {
				try {
					maxd = Toolkit.getToolkit().parseDate(maxdate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (keyvalue.get("title").contains("%")) {
				temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data, 10,
						100, maxd, 0, true, 650, 200);
			} else {
				double maxvalue = Double.parseDouble(keyvalue.get("maxvalue"));
				double minvalue = Double.parseDouble(keyvalue.get("minvalue"));
				maxvalue = maxvalue * 1.1;
				if (maxvalue < 1) {
					maxvalue = 1;
				}
				if (keyvalue.get("minvalue").contains("-")) {
					temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data,
							20, maxvalue, maxd, minvalue, true, 650, 200);
				} else {
					temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data,
							20, maxvalue, maxd, 0, true, 650, 200);
				}
			}

			ByteArrayOutputStream bs = new ByteArrayOutputStream();
			ImageOutputStream imOut = null;
			try {
				imOut = ImageIO.createImageOutputStream(bs);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				ImageIO.write(temmap, "GIF", imOut);
			} catch (IOException e) {
				e.printStackTrace();
			} // scaledImage1为BufferedImage，jpg为图像的类型
			InputStream istream = new ByteArrayInputStream(bs.toByteArray());
			list.add(istream);
		}
		return list;
	}

	private List<String> buildimage() {
		Map<Integer, Map<String, String>> listimage = SimpleReport.getImagelist(this.report);
		if (listimage.size() == 0) {
			return null;
		}

		List<String> list = new ArrayList<String>();

		for (int key : listimage.keySet()) {
			String id = monitorId + sessionId + key;
			Map<Date, String> imgdata = this.report.getReturnValueDetail(key);
			Map<String, String> keyvalue = listimage.get(key);
			XYDataset data = SimpleReport.buildDataset(imgdata);
			BufferedImage temmap = null;
			String maxdate = keyvalue.get("maxdate");
			Date maxd = null;
			if (!maxdate.isEmpty()) {
				try {
					maxd = Toolkit.getToolkit().parseDate(maxdate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (keyvalue.get("title").contains("%")) {
				temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data, 10,
						100, maxd, 0, true, 650, 200);
			} else {
				double maxvalue = Double.parseDouble(keyvalue.get("maxvalue"));
				double minvalue = Double.parseDouble(keyvalue.get("minvalue"));
				maxvalue = maxvalue * 1.1;
				if (maxvalue < 1) {
					maxvalue = 1;
				}
				if (keyvalue.get("minvalue").contains("-")) {
					temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data,
							20, maxvalue, maxd, minvalue, true, 650, 200);
				} else {
					temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data,
							20, maxvalue, maxd, 0, true, 650, 200);
				}
			}

			String strImagePath = Constand.downloadreportpath + filename + ".html_files\\" + id + ".png";
			File f = new File(Constand.downloadreportpath + filename + ".html_files\\");
			if (!f.exists()) {
				try {
					f.mkdir();
				} catch (Exception e) {
				}

			}
			new com.siteview.ecc.simplereport.CreateImage().create(temmap, strImagePath);
			if (this.reporttype.equals("html")) {
				list.add(filename + ".html_files" + "/" + id + ".png");
			} else {
				list.add(strImagePath);
			}
		}
		return list;
	}

	// *************************************************************************
	// *************************导出统计报表)************************************
	public ExportReport(ReportDate rd, String reporttype, String reporttime, String title, String jasperpath, Date start, Date end) {
		this.rd = rd;
		this.reporttype = reporttype;
		this.jasperpath = jasperpath;
		this.startd = start;
		this.endd = end;
		this.reporttime = reporttime;
		this.title = title;
		if (rd != null && reporttype != null && jasperpath != null) {
			this.filename = "exportreport" + System.currentTimeMillis();
			strZipDirPath = Constand.downloadreportpath;
			String file = null;
			// 生成报告文件;
			if ("html".equals(reporttype)) {
				this.getHtml(rd);
				file = "/main/report/downloadreport/" + this.filename + ".zip";
			} else if ("xls".equals(reporttype)) {
				this.getXls(rd);
				file = "/main/report/downloadreport/" + this.filename + ".xls";
			} else if ("pdf".equals(reporttype)) {
				this.getPdf(rd);
				file = "/main/report/downloadreport/" + this.filename + ".pdf";
			}
			// 拷贝文件并压缩打包ZIP
			this.copyFiles(reporttype, filename);
			this.downloadReport(file);
		}
		// 最后下载
	}

	Date startd;
	Date endd;
	String reporttime;
	String title;

	private void getHtml(ReportDate rd) {
		Reportdatasource ds = new Reportdatasource(buildBean(rd, "HistoryBean"));
		try {
			JasperRunManager
					.runReportToHtmlFile(this.jasperpath, Constand.downloadreportpath + filename + ".html", this.getParaMap(rd), ds);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	private void getXls(ReportDate rd) {
		JRXlsExporter exporter = new JRXlsExporter();
		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		Reportdatasource ds = new Reportdatasource(buildBean(rd, "HistoryBean"));
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(this.jasperpath, this.getParaMap(rd), ds);
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		} catch (JRException e1) {
			e1.printStackTrace();
		}
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
		exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
		try {
			exporter.exportReport();
		} catch (JRException e) {
			e.printStackTrace();
		}
		//
		byte[] bytes = oStream.toByteArray();
		OutputStream out =null;
		try {
			out = new FileOutputStream(Constand.downloadreportpath + filename + ".xls");
			out.write(bytes);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally
		{
			try{out.close();}catch(Exception e){}
		}

	}

	private void getPdf(ReportDate rd) {
		Reportdatasource ds = new Reportdatasource(buildBean(rd, "HistoryBean"));
		try {
			JasperRunManager.runReportToPdfFile(this.jasperpath, Constand.downloadreportpath + filename + ".pdf", this.getParaMap(rd), ds);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	private Map getParaMap(ReportDate rd) {
		String subtitle = "";// 不要副标题
		Reportdatasource subds1 = new Reportdatasource(buildBean(rd, "MonitorBean"));
		Reportdatasource subds2 = new Reportdatasource(buildBean(rd, "StatisticsBean"));
		Reportdatasource subds3 = null;
		subds3 = new Reportdatasource(buildBean(rd, "ImageBean"));
		Map parameters = new HashMap();
		parameters.put("ReportTitle", this.title);
		parameters.put("subReportTitle", subtitle);
		String path = EccWebAppInit.getWebDir();
		parameters.put("SUBREPORT_DIR", path + "/main/report/");
		parameters.put("IS_IGNORE_PAGINATION", true);
		parameters.put("SUBREPORT_DIRfilename", path + "/main/report/report_subreport2.jasper");
		parameters.put("subDS1", subds1);
		parameters.put("subDS2", subds2);
		parameters.put("subDS3", subds3);
		return parameters;
	}

	private List buildBean(ReportDate rd, String tag) {
		List list = new ArrayList();
		for (String id : rd.getNodeidsArray()) {
			if (tag.equals("MonitorBean")) {
				String okPercent = rd.getPropertyValue(id, "okPercent");
				String warnPercent = rd.getPropertyValue(id, "warnPercent");
				String errorPercent = rd.getPropertyValue(id, "errorPercent");
				Map<Date, DstrItem> dstrs = rd.getDstr(id);

				float dis = rd.getDisablePercentOfSimpleReport();
				String disablePercent = Float.toString(dis);
				String errorCondition = rd.getPropertyValue(id, "errorCondition");

				list.add(new com.siteview.ecc.simplereport.MonitorBean(rd.getPropertyValue(id, "MonitorName"), okPercent, warnPercent,
						errorPercent, disablePercent, errorCondition));

			}
			if (tag.equals("StatisticsBean")) {
				for (int i = 0; i < rd.getReturnSize(id); i++) {
					String drawmeasure = rd.getReturnValue(id, "sv_drawmeasure", i);
					drawmeasure = drawmeasure.isEmpty() ? "0" : drawmeasure;
					if (!drawmeasure.equals("1")) {
						continue;
					}
					String returnName = rd.getReturnValue(id, "ReturnName", i);
					String max = rd.getReturnValue(id, "max", i);
					String latest = rd.getReturnValue(id, "latest", i);
					String average = rd.getReturnValue(id, "average", i);
					list.add(new com.siteview.ecc.simplereport.StatisticsBean(rd.getPropertyValue(id, "MonitorName"), returnName, max,
							average, latest));
				}
			}
			if (tag.equals("HistoryBean")) {
				Map<Date, DstrItem> dstrs = rd.getDstr(id);
				List errorlist = new ArrayList();
				List dangerlist = new ArrayList();
				List oklist = new ArrayList();
				List disablelist = new ArrayList();
				List elselist = new ArrayList();
				for (Date D : dstrs.keySet()) {
					String state = dstrs.get(D).status;
					String dstr = dstrs.get(D).value;
					if (state.equals("ok")) {
						state = "正常";
						oklist.add(new com.siteview.ecc.simplereport.HistoryBean(rd.getPropertyValue(id, "MonitorName"),
								Toolkit.getToolkit().formatDate(D), dstr, state));
					} else if (state.equals("error")) {
						state = "错误";
						errorlist.add(new com.siteview.ecc.simplereport.HistoryBean(rd.getPropertyValue(id, "MonitorName"), Toolkit.getToolkit().formatDate(D), dstr, state));
					} else if (state.equals("disable")) {
						state = "禁止";
						disablelist.add(new com.siteview.ecc.simplereport.HistoryBean(rd.getPropertyValue(id, "MonitorName"),Toolkit.getToolkit().formatDate(D), dstr, state));
					} else if (state.equals("warning")) {
						state = "危险";
						dangerlist.add(new com.siteview.ecc.simplereport.HistoryBean(rd.getPropertyValue(id, "MonitorName"), Toolkit.getToolkit().formatDate(D), dstr, state));
					} else {
						elselist.add(new com.siteview.ecc.simplereport.HistoryBean(rd.getPropertyValue(id, "MonitorName"), Toolkit.getToolkit().formatDate(D), dstr, state));
					}
				}
				if (errorlist.size() > 0) {
					Iterator item = errorlist.iterator();
					while (item.hasNext()) {
						list.add((HistoryBean) item.next());
					}
				}
				if (dangerlist.size() > 0) {
					Iterator item = dangerlist.iterator();
					while (item.hasNext()) {
						list.add((HistoryBean) item.next());
					}
				}
				if (oklist.size() > 0) {
					Iterator item = oklist.iterator();
					while (item.hasNext()) {
						list.add((HistoryBean) item.next());
					}
				}
				if (disablelist.size() > 0) {
					Iterator item = disablelist.iterator();
					while (item.hasNext()) {
						list.add((HistoryBean) item.next());
					}
				}
				if (elselist.size() > 0) {
					Iterator item = elselist.iterator();
					while (item.hasNext()) {
						list.add((HistoryBean) item.next());
					}
				}
			}
			if (tag.equals("ImageBean")) {
				list = buildimage(rd, this.startd, this.endd);
			}
			if (tag.equals("ImageBeanpdf")) {
				list = buildstreamimage(rd, this.startd, this.endd);
			}
		}
		return list;

	}

	CreateReport cr = new CreateReportImpl();

	private List<InputStream> buildstreamimage(ReportDate rd, Date start, Date end) {
		List<InputStream> list = new ArrayList<InputStream>();
		for (String nd : rd.getNodeidsArray()) {
			Map<Integer, Map<String, String>> listimage = cr.getImagelist(nd, rd);
			for (int key : listimage.keySet()) {
				Map<Date, String> imgdata = rd.getReturnValueDetail(nd, key);
				Map<String, String> keyvalue = listimage.get(key);
				XYDataset xd = SimpleReport.buildDataset(imgdata);
				BufferedImage temmap = null;
				String maxdate = keyvalue.get("maxdate");
				if (keyvalue.get("title").contains("%")) {
					temmap = cr.getBufferedImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), xd, 10, 100,
							start, end, 0, true, 740, 350, reporttime);// 建各图
				} else {
					double maxvalue = Double.parseDouble(keyvalue.get("maxvalue"));
					double minvalue = Double.parseDouble(keyvalue.get("minvalue"));
					maxvalue = maxvalue * 1.1;
					if (maxvalue < 1) {
						maxvalue = 1;
					}
					temmap = cr.getBufferedImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), xd, 10, maxvalue,
							start, end, minvalue, true, 740, 350, reporttime);
				}

				ByteArrayOutputStream bs = new ByteArrayOutputStream();
				ImageOutputStream imOut = null;
				try {
					imOut = ImageIO.createImageOutputStream(bs);
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					ImageIO.write(temmap, "GIF", imOut);
				} catch (IOException e) {
					e.printStackTrace();
				} // scaledImage1为BufferedImage，jpg为图像的类型
				InputStream istream = new ByteArrayInputStream(bs.toByteArray());
				list.add(istream);
			}
		}
		return list;
	}

	private List<String> buildimage(ReportDate rd, Date start, Date end) {
		List<String> list = new ArrayList<String>();
		Map<String, Map<Date, String>> imgdatas = new HashMap<String, Map<Date, String>>();
		int i = 0;
		String nd = "";
		File f = new File(Constand.downloadreportpath + filename + ".html_files\\");
		if (!f.exists()) {
			try {
				f.mkdir();
			} catch (Exception e) {
			}
		}
		for (String id : rd.getNodeidsArray()) {
			BufferedImage temmap = null;
			Map<Integer, Map<String, String>> imgeLinkedHashMap = cr.getImagelist(id, rd);
			for (int key : imgeLinkedHashMap.keySet()) {
				nd = id + key;
				Map<Date, String> imgdata = rd.getReturnValueDetail(id, key);
				imgdatas.put(rd.getReturnValue(id, "MonitorName", key), imgdata);
				XYDataset xd = cr.buildDataset(imgdatas);
				Map<String, String> keyvalue = imgeLinkedHashMap.get(key);
				try {
					temmap = cr.getBufferedImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), xd, 10, 100,
							start, end, 0, true, 740, 350, reporttime);
					i++;
				} catch (Exception e) {
					e.printStackTrace();
				}
				String strImagePath = Constand.downloadreportpath + filename + ".html_files\\" + nd + ".png";
				new com.siteview.ecc.simplereport.CreateImage().create(temmap, strImagePath);
				if (this.reporttype.equals("html")) {
					list.add(filename + ".html_files" + "/" + nd + ".png");
				} else {
					list.add(strImagePath);
				}
			}
			if (i > 100)
				break;
		}
		return list;
	}

	// ***********************************************************
	// *********8888***导出TOPN报表*******************************
	// *************************************************************************
	// *************************导出统计报表)************************************
	public ExportReport(ReportDate rd, Map map, Date start, Date end) {
		this.rd = rd;
		this.reporttype = (String) map.get("reporttype");
		this.jasperpath = (String) map.get("jasperpath");
		this.startd = start;
		this.endd = end;
		this.title = (String) map.get("reporttitle");
		String strReportMark = (String) map.get("Mark");
		String strReportCount = (String) map.get("Count");

		if (rd != null && reporttype != null && jasperpath != null) {
			this.filename = "exporttopnreport" + System.currentTimeMillis();
			strZipDirPath = Constand.downloadreportpath;
			String file = null;
			// 生成报告文件;
			if ("html".equals(reporttype)) {
				file = "/main/report/downloadreport/" + this.filename + ".zip";
			} else if ("xls".equals(reporttype)) {
				file = "/main/report/downloadreport/" + this.filename + ".xls";
			} else if ("pdf".equals(reporttype)) {
				file = "/main/report/downloadreport/" + this.filename + ".pdf";
			}
			List imagelist = this.build(rd, "image", strReportMark, strReportCount, title, "", 740, 350);
			List datalist = this.build(rd, "bean", strReportMark, strReportCount, "", "", 0, 0);
			ReportDataSource subds1 = new ReportDataSource(imagelist);
			ReportDataSource subds2 = new ReportDataSource(datalist);
			ReportDataSource subds3 = new ReportDataSource(imagelist);
			String subtitle = "时间段:" + Toolkit.getToolkit().formatDate(start) + "~" + Toolkit.getToolkit().formatDate(end);
			Map parameters = new HashMap();
			parameters.put("ReportTitle", this.title);
			parameters.put("subReportTitle", subtitle);
			String path = EccWebAppInit.getWebDir();
			parameters.put("SUBREPORT_DIR", path + "/main/report/topnreport/");
			// parameters.put("IS_IGNORE_PAGINATION", true);
			parameters.put("subDS1", subds1);
			parameters.put("subDS2", subds2);
			getOutputFile(reporttype, subds3, parameters);
			// 拷贝文件并压缩打包ZIP
			this.copyFiles(reporttype, filename);
			this.downloadReport(file);
		}
		// 最后下载
	}

	private void getOutputFile(String type, ReportDataSource ds, Map parameters) {
		if (type.equals("html")) {
			try {
				JasperRunManager.runReportToHtmlFile(this.jasperpath, Constand.downloadreportpath + filename + ".html", parameters);
			} catch (JRException e) {
				e.printStackTrace();
			}
		} else if (type.equals("pdf")) {
			try {
				JasperRunManager.runReportToPdfFile(this.jasperpath, Constand.downloadreportpath + filename + ".pdf", parameters, ds);
			} catch (JRException e) {
				e.printStackTrace();
			}
		} else if (type.equals("xls")) {
			JRXlsExporter exporter = new JRXlsExporter();
			ByteArrayOutputStream oStream = new ByteArrayOutputStream();
			try {
				JasperPrint jasperPrint = JasperFillManager.fillReport(this.jasperpath, parameters, ds);
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			} catch (JRException e1) {
				e1.printStackTrace();
			}
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
			try {
				exporter.exportReport();
			} catch (JRException e) {
				e.printStackTrace();
			}
			//
			byte[] bytes = oStream.toByteArray();
			OutputStream out=null;
			try {
				out = new FileOutputStream(Constand.downloadreportpath + filename + ".xls");
				out.write(bytes);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally
			{
				try{out.close();}catch(Exception e){}
			}

		}
	}

	private List build(ReportDate rd, String type, String strReportMark, String strReportCount, String xtitle, String ytitle, int width,
			int height) {
		List list = new ArrayList();
		if (type.equals("image")) {
			HashMap<String, LinkedHashMap<Date, String>> imgdatas = new HashMap<String, LinkedHashMap<Date, String>>();
			int i = 0;
			String pre = filename + ".html_files\\" + this.sessionId + ".png";
			File f = new File(Constand.downloadreportpath + filename + ".html_files\\");
			if (!f.exists()) {
				try {
					f.mkdir();
				} catch (Exception e) {
				}
			}
			BufferedImage temmap = this.doimge(rd, strReportMark, strReportCount, xtitle, ytitle, width, height);
			String strImagePath = Constand.downloadreportpath + pre;
			new com.siteview.ecc.simplereport.CreateImage().create(temmap, strImagePath);
			if (this.reporttype.equals("html")) {
				list.add(pre);
			} else {
				list.add(strImagePath);
			}
			return list;
		} else if (type.equals("bean")) {
			int datacounts = 0;
			for (String nd : rd.getNodeidsArray()) {
				datacounts++;
				if (Integer.parseInt(strReportCount) < datacounts) {// 超过最多显示数量了
					break;
				}
				int xx = rd.getReturnSize(nd);
				for (int d = 0; d < xx; d++) {
					String returnName = rd.getReturnValue(nd, "ReturnName", d);
					// 不是选择的返回值则不用显示了
					if (!returnName.equals(strReportMark)) {
						continue;
					}
					ReportBean rb = new ReportBean();
					rb.setName(rd.getReturnValue(nd, "MonitorName", d).substring(0, rd.getReturnValue(nd, "MonitorName", d).indexOf(":")));
					rb.setMonitorname(rd.getReturnValue(nd, "MonitorName", d));
					rb.setAverage(rd.getReturnValue(nd, "average", d));
					rb.setMax(rd.getReturnValue(nd, "max", d));
					rb.setMin(rd.getReturnValue(nd, "min", d));
					rb.setReturnName(rd.getReturnValue(nd, "ReturnName", d));
					rb.setWhen_max(rd.getReturnValue(nd, "when_max", d));
					rb.setLatest(rd.getReturnValue(nd, "latest", d));
					rb.setTitle("运行情况表");
					list.add(rb);
				}
			}
		}
		return list;
	}

	private BufferedImage doimge(ReportDate rd, String strReportMark, String strReportCount, String xtitle, String ytitle, int width,
			int height) {
		// 构造Topn报告图片数据
		DefaultCategoryDataset dataset = buildDataset(rd, strReportMark, strReportCount);
		// 主标题 + X标题 + Y标题 + 数据
		JFreeChart chart = ChartFactory.createBarChart3D(title, xtitle, ytitle, dataset, PlotOrientation.VERTICAL, false, false, false);
		chart.setBackgroundPaint(new Color(0xE1E1E1));
		CategoryPlot plot = chart.getCategoryPlot();
		// 设置Y轴显示整数
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		// 横坐标竖显示
		rangeAxis.setVerticalTickLabels(true);
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		CategoryAxis domainAxis = plot.getDomainAxis();
		// 设置距离图片左端距离
		domainAxis.setLowerMargin(0.05);
		BarRenderer3D renderer = new BarRenderer3D();
		// 设置柱的颜色
		renderer.setSeriesPaint(0, new Color(0xff00));
		plot.setRenderer(renderer);
		// 存储
		// chart.createBufferedImage(width, height);
		// ChartUtilities.getImageMap(name, info)
		ChartRenderingInfo info = new ChartRenderingInfo();
		BufferedImage bi = chart.createBufferedImage(width, height, BufferedImage.SCALE_FAST, info);
		return bi;
	}

	// 构造Topn报告图片数据
	private DefaultCategoryDataset buildDataset(ReportDate rd, String strReportMark, String strReportCount) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		double dMax;
		String strMax = "", strName = "", returnName = "";
		int datacounts = 0;
		for (String id : rd.getNodeidsArray()) {
			datacounts++;
			if (Integer.parseInt(strReportCount) < datacounts) {// 超过最多显示数量了
				break;
			}
			for (int i = 0; i < rd.getReturnSize(id); i++) {
				returnName = rd.getReturnValue(id, "ReturnName", i);
				// 不是选择的返回值则不用显示了
				if (!returnName.equals(strReportMark)) {
					continue;
				}
				// 最大值或平均值等(Y轴)
				strMax = rd.getReturnValue(id, "average", i);
				dMax = Double.parseDouble(strMax);
				// 监测器名称(X轴)
				strName = rd.getPropertyValue(id, "MonitorName");

				dataset.addValue(dMax, Integer.toString(datacounts), rd.getReturnValue(id, "MonitorName", i));
			}
		}

		return dataset;
	}

	private boolean displaycountlimited = true;
}
