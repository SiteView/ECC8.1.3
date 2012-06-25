package com.siteview.ecc.report.syslogreport;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.siteview.ecc.report.SysLogValueList;
import com.siteview.ecc.report.SyslogBean;
import com.siteview.ecc.treeview.EccWebAppInit;

/**
 *系统日志报告/导出报告界面处理类
 * 
 * @company: siteview
 * @author:qimin.xiong
 * @date:2009-4-22
 */
public class SyslogReportGenericForwardComposer  extends
		GenericForwardComposer {
	private Window exportsyslogreport;
	private Listbox format;// 界面元素
	private List reportlist = null;


	public void onClick$saveReport(Event event) throws Exception{
		try{
			reportlist = (List)exportsyslogreport.getAttribute("datasource");
			String reportType = format.getSelectedItem()!=null?format.getSelectedItem().getValue().toString():"pdf";
			String jasperpath = EccWebAppInit.getWebDir()+ "main\\report\\syslogreport\\syslogreport.jasper";
			Map parameters = new HashMap();
			SyslogDataSource dataSource = generateDatasource();
			AMedia media = null;
			if(reportType.equals("html")){
				media = this.generateHtml(jasperpath, parameters,
						dataSource);
			}else if(reportType.equals("pdf")){
				media = this.generatePDF(jasperpath, parameters,
						dataSource);
			}else{
				media = generateXLS(jasperpath, parameters,
						dataSource);
			}
			Filedownload.save(media);
			exportsyslogreport.detach();
		}catch(Exception e){
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
	/**
	 * 生成系统日志报表 syslog
	 * 
	 * @param parameters
	 */
	private SyslogDataSource generateDatasource() {
		List<SyslogBean> rdlist = new ArrayList<SyslogBean>();
		//这里必须要有一个循环的显示
		for(int i=0 ;i<this.reportlist.size();i++)
		{
			SyslogBean bean = new SyslogBean();
			bean.setInTime(((SysLogValueList)this.reportlist.get(i)).getInTime());
			bean.setSourceIP(((SysLogValueList)this.reportlist.get(i)).getSourceIP());
			bean.setFacility(((SysLogValueList)this.reportlist.get(i)).getFacility());
			bean.setLeave(((SysLogValueList)this.reportlist.get(i)).getLeave());
			bean.setSysLogmsg(((SysLogValueList)this.reportlist.get(i)).getSysLogmsg());
			bean.setColor( ( i % 2 == 0) );
			rdlist.add(bean);
		}
		SyslogDataSource rd = new SyslogDataSource(rdlist);
		return rd;
	}
	public AMedia generatePDF(String reportFilePath, Map paramter,
			JRDataSource ds) throws Exception {
		ByteArrayOutputStream arrayOutputStream =null;
		try{		
				arrayOutputStream = new ByteArrayOutputStream();
				JasperPrint jasperPrint = JasperFillManager.fillReport(reportFilePath,
						paramter, ds);
				JRExporter exporter = new JRPdfExporter();
				if (paramter != null)
					exporter.setParameters(paramter);
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
						arrayOutputStream);
				exporter.exportReport();
				return new AMedia("report.pdf", "pdf", "application/pdf",
				arrayOutputStream.toByteArray());
		}finally
		{
			try{arrayOutputStream.close();}catch(Exception e){}
	  }
		
	}

	/**
	 * 
	 * @param begCustNo
	 * @param endCustNo
	 * @param reportTitle
	 * @param reportFilePath
	 * @return
	 * @throws JRException
	 * @throws DemoException
	 */
	public AMedia generateHtml(String reportFilePath, Map paramter,
			JRDataSource ds) throws Exception {

		JRHtmlExporter exporter = new JRHtmlExporter();
		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		JasperPrint jasperPrint = JasperFillManager.fillReport(reportFilePath,
				paramter, ds);
		exporter.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,
				Boolean.FALSE);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
		exporter.exportReport();
		AMedia media = new AMedia("report.html", "html", "text/html", oStream.toByteArray());
		return media;
	}

	private AMedia generateXLS(String reportFilePath, Map paramter,
			JRDataSource ds) throws Exception {
		JRXlsExporter exporter = new JRXlsExporter();
		ByteArrayOutputStream oStream = new ByteArrayOutputStream();
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					reportFilePath, paramter, ds);
			exporter
					.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		} catch (JRException e1) {
			e1.printStackTrace();
		}
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
		exporter.setParameter(
				JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				Boolean.TRUE);
		exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
				Boolean.FALSE);
		exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
				Boolean.FALSE);
		try {
			exporter.exportReport();
		} catch (JRException e) {
			e.printStackTrace();
		}
		byte[] bytes = oStream.toByteArray();
		return new AMedia("report.xls", "xls", "application/vnd.ms-excel",
				bytes);

	}
}
