package com.siteview.ecc.report.statisticalreport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.siteview.ecc.report.common.ReportFileToolkit;
import com.siteview.ecc.reportserver.Constand;
import com.siteview.ecc.reportserver.DirectoryZip;
import com.siteview.ecc.reportserver.StatsReport;
import com.siteview.ecc.util.Toolkit;

public class ShowHtml extends HttpServlet{
	private String currentPage = "1";
	private String reportGenID = "";
	private String createTimeInMillis = "";
	private int pageCount = 0;
	private String download = null;

	public String getDownload() {
		return download;
	}

	public void setDownload(String download) {
		this.download = download;
	}

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getReportGenID() {
		return reportGenID;
	}

	public void setReportGenID(String reportGenID) {
		this.reportGenID = reportGenID;
	}

	
	public String getCreateTimeInMillis() {
		return createTimeInMillis;
	}

	public void setCreateTimeInMillis(String createTimeInMillis) {
		this.createTimeInMillis = createTimeInMillis;
	}

	public String getContent() {
//		String fileName = Toolkit.getToolkit().formatDate(new Date(Long.parseLong(reportGenID)), "yyyyMMdd_")+ reportGenID;
		String fileName = createTimeInMillis + "_" + reportGenID;
		String htmlurl = StatsReport.getHtmlFolderName(createTimeInMillis,reportGenID)
				+ fileName + currentPage + ".html";
		String path=StatsReport.getCreateFile(createTimeInMillis,reportGenID,"html");
		File file = new File(htmlurl);
		StringBuffer content = new StringBuffer();
		if (!file.exists())
			return "文件不存在!";
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		try {
			pageCount = ReportFileToolkit.listHtmlFiles(new File(StatsReport.getHtmlFolderName(createTimeInMillis,reportGenID)),"html").length;
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "utf-8");
			reader = new BufferedReader(isr);
			String strReadLine = reader.readLine();
			while (strReadLine != null) {
				content.append(strReadLine).append("\n");
				strReadLine = reader.readLine();
			}
		} catch (Exception e) {
			return "读取" + file.getName() + "出错！";
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				isr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String begin_string = String.format("<a name=\"JR_PAGE_ANCHOR_0_%s\"/>", currentPage);
		int beginflag = content.indexOf(begin_string);
		int trindex = content.indexOf("<tr>",beginflag);
		content.insert(trindex,getScroolBar());
		if(download!=null){
		}
		return content.toString();
	}
	
	  private String getScroolBar()
	  {
		  StringBuffer scroll=new StringBuffer();

		  if(Integer.parseInt(currentPage)>1)
		  {
		  scroll.append("<a style=\"font-size:12px;text-decoration: underline\" href=\"/ecc/main/report/showStatisticReport.jsp");
		  scroll.append("?reportGenID=").append(reportGenID);
		  scroll.append("&currentPage=").append(1);
		  scroll.append("&createTimeInMillis=").append((createTimeInMillis));
		  scroll.append("\">第一页</a>&nbsp;");
		  }
		  else
		  {
			  scroll.append("<span style=\"font-size:12px\">第一页</span>&nbsp;");
		  }
		  if(Integer.parseInt(currentPage)>1)
		  {
			  scroll.append("<a style=\"font-size:12px;text-decoration: underline\" href=\"/ecc/main/report/showStatisticReport.jsp");
			  scroll.append("?reportGenID=").append(reportGenID);
			  scroll.append("&currentPage=").append((Integer.parseInt(currentPage)-1));
			  scroll.append("&createTimeInMillis=").append((createTimeInMillis));
			  scroll.append("\">前一页</a>&nbsp;");
		  }else
		  {
			  scroll.append("<span style=\"font-size:12px\">前一页</span>&nbsp;");
		  }
		  if(Integer.parseInt(currentPage)<pageCount)
		  {
			  scroll.append("<a style=\"font-size:12px;text-decoration: underline\" href=\"/ecc/main/report/showStatisticReport.jsp");
			  scroll.append("?reportGenID=").append(reportGenID);
			  scroll.append("&currentPage=").append((Integer.parseInt(currentPage)+1));
			  scroll.append("&createTimeInMillis=").append((createTimeInMillis));
			  scroll.append("\">后一页</a>&nbsp;");
		  }
		  else
		  {
			  scroll.append("<span style=\"font-size:12px\">后一页</span>&nbsp;");
		  }
		  if(Integer.parseInt(currentPage)<pageCount)
		  { 
		  scroll.append("<a style=\"font-size:12px;text-decoration: underline\" href=\"/ecc/main/report/showStatisticReport.jsp");
		  scroll.append("?reportGenID=").append(reportGenID);
		  scroll.append("&currentPage=").append(pageCount);
		  scroll.append("&createTimeInMillis=").append((createTimeInMillis));
		  scroll.append("\">末一页</a>&nbsp;");
		  }
		  else
		  {
			  scroll.append("<span style=\"font-size:12px\">末一页</span>&nbsp;");
		  }
		  scroll.append("<span style=\"font-size:12px\">共 :");
		  scroll.append(pageCount);
		  scroll.append("页</span>&nbsp;");
		  
		  
		  scroll.append("<span style=\"font-size:12px\">当前是第:");
		  scroll.append(currentPage);
		  scroll.append("页</span>&nbsp;");
		  
		  
		  scroll.append("<a style=\"font-size:12px;text-decoration: underline\" href=\"/ecc/main/report/saveStatisticReport");
		  scroll.append("?reportGenID=").append(reportGenID);
		  scroll.append("&currentPage=").append((currentPage));
		  scroll.append("&createTimeInMillis=").append((createTimeInMillis));
		  scroll.append("\">保存</a>&nbsp;");
//		  if(isDownload) downloadFile();
		  return scroll.toString();
	  }
	  
	  public void doGet(HttpServletRequest req , HttpServletResponse rsp){
		  FileInputStream fis = null;
		  OutputStream os  = null;
		  try {
			  reportGenID = req.getParameter("reportGenID");
			  currentPage = req.getParameter("currentPage");
			  download = req.getParameter("download");
			  createTimeInMillis = req.getParameter("createTimeInMillis"); 
//			  String fileName = Toolkit.getToolkit().formatDate(new Date(Long.parseLong(reportGenID)), "yyyyMMdd_")	+ reportGenID;
			  String fileName = createTimeInMillis + "_" + reportGenID;
			  String srcFolder = Constand.statreportsavepath+fileName;
			  File f2 = new File(srcFolder + ".zip");
			  if(!f2.exists()){
				  DirectoryZip zip = new DirectoryZip();
				  zip.zip(srcFolder, srcFolder + ".zip");
			  }
			  fis = new FileInputStream(f2);
			  ((HttpServletResponse)rsp).setContentType("APPLICATION/OCTET-STREAM"); 
			  ((HttpServletResponse)rsp).setHeader("Content-Disposition", "attachment; filename=\"" + "staticReport.zip" + "\"");
			  int size = 0;
			  byte[] buff = new byte[1024];
			  os = rsp.getOutputStream();
			  while((size=fis.read(buff))!=-1){
				  os.write(buff, 0, size);
				  os.flush();
			  }
		  } catch (IOException e) {
			  e.printStackTrace();
		  } catch (Exception e) {
			  e.printStackTrace();
		  }finally{
			  try {
				if( os != null ) os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			  try {
				if ( fis != null ) fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		  }
	  }
	  public void doPost(HttpServletRequest req , HttpServletResponse rsp){
		  doGet(req,rsp);
	  }
	  public static void main(String[] args) throws Exception{
		  String src = "E:/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/ecc/main/report/statreport/20091111_1257921824875";
		  DirectoryZip zip = new DirectoryZip();
		  zip.zip(src, src + "20091111_1257921824875.zip");
	  }
}
