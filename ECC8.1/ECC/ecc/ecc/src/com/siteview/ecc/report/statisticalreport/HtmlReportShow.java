package com.siteview.ecc.report.statisticalreport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.siteview.ecc.reportserver.Constand;
import com.siteview.ecc.reportserver.DirectoryZip;
import com.siteview.ecc.reportserver.StatsReport;
import com.siteview.ecc.util.Toolkit;

public class HtmlReportShow extends HttpServlet{
	private final static Logger logger = Logger.getLogger(HtmlReportShow.class);
  private int currentPage=1;
  private int pageCount=-1;
  private String reportGenID;
  private String topNReportPath;
  private String fileType;
  private String download="false";
  private String createTimeInMillis = "";
  
  public String getDownload() {
	return download;
}
public void setDownload(String download) {
	this.download = download;
}
public HtmlReportShow()
  {
  }
  public HtmlReportShow(String reportGenID,String fileType,int currentPage)
  {
	 this.reportGenID	=	reportGenID;
	 this.fileType		= 	fileType;
	 this.currentPage	=	currentPage;
  }
  
  public int getCurrentPage() {
	return currentPage;
}

public void setCurrentPage(int currentPage) {
	this.currentPage = currentPage;com.siteview.ecc.report.comparereport.CompareExportWindow c;
}

public String getFileType() {
	return fileType;
}

public void setFileType(String fileType) {
	this.fileType = fileType;
}

public String getReportGenID() {
	return reportGenID;
}

public void setReportGenID(String reportGenID) {
	this.reportGenID = reportGenID;
}

public String getTopNReportPath() {
	return topNReportPath;
}

public void setTopNReportPath(String topNReportPath) {
	this.topNReportPath = topNReportPath;
}



public String getCreateTimeInMillis() {
	return createTimeInMillis;
}
public void setCreateTimeInMillis(String createTimeInMillis) {
	this.createTimeInMillis = createTimeInMillis;
}
public String getContent()
  {   
	  String filePath=StatsReport.getCreateFile(createTimeInMillis, reportGenID, fileType);
	  File file=new File(filePath);
	  if(!file.exists())
		  return "文件不存在!";
	  
	  StringBuffer content=new StringBuffer();
	  FileInputStream fis=null;
	  InputStreamReader isr=null;
	  BufferedReader reader=null;
	  try
	  {
	  	fis=new FileInputStream(file);
	  	isr=new InputStreamReader(fis,"utf-8");
	  	reader=new BufferedReader(isr);
		  String strReadLine = reader.readLine();
		  int readPage=0;
		  while(strReadLine!=null)
		  {
			  if(strReadLine.startsWith("<a name=\"JR_PAGE_ANCHOR"))
				  readPage++;
			  
			  if(readPage==0||readPage==currentPage)
				  content.append(strReadLine).append("\n");

			  strReadLine = reader.readLine();
		  }
		  
		  this.pageCount=readPage;
		  
		  return makeContent(content);
		  
	  }catch(Exception e)
	  {
		  return e.getMessage();
	  }
	  finally{
	  	try{reader.close();}catch(Exception e){}
	  	try{isr.close();}catch(Exception e){}
	  	try{fis.close();}catch(Exception e){}
	  	
	  }

  }

  private String makeContent(StringBuffer content)
  {
	  /*int idx=content.indexOf("第"+currentPage+"页");
	  if(idx>0)
		  content.insert(idx, getScroolBar());
	  else
		  content.append(getScroolBar());
	   */
	  content.insert(0, getScroolBar());
	  
	  content.append("</td><td width=\"50%\">&nbsp;</td></tr>\n");
	  content.append("</table>\n");
	  content.append("</body>\n");
	  content.append("</html>\n");
	  
	  return content.toString();
  }
  private String getScroolBar()
  {
	  StringBuffer scroll=new StringBuffer();

	  if(currentPage>1)
	  {
	  scroll.append("<a style=\"font-size:12px;text-decoration: underline\" href=\"/ecc/main/report/showReport.jsp");
	  scroll.append("?fileType=").append(fileType);
	  scroll.append("&download=").append("false");
	  scroll.append("&reportGenID=").append(reportGenID);
	  scroll.append("&currentPage=").append(1);
	  scroll.append("&createTimeInMillis=").append((createTimeInMillis));
	  scroll.append("\">第一页</a>&nbsp;");
	  }
	  else
	  {
		  scroll.append("<span style=\"font-size:12px\">第一页</span>&nbsp;");
	  }
	  if(currentPage>1)
	  {
		  scroll.append("<a style=\"font-size:12px;text-decoration: underline\" href=\"/ecc/main/report/showReport.jsp");
		  scroll.append("?fileType=").append(fileType);
		  scroll.append("&download=").append("false");
		  scroll.append("&reportGenID=").append(reportGenID);
		  scroll.append("&currentPage=").append((currentPage-1));
		  scroll.append("&createTimeInMillis=").append((createTimeInMillis));
		  scroll.append("\">前一页</a>&nbsp;");
	  }else
	  {
		  scroll.append("<span style=\"font-size:12px\">前一页</span>&nbsp;");
	  }
	  if(currentPage<pageCount)
	  {
		  scroll.append("<a style=\"font-size:12px;text-decoration: underline\" href=\"/ecc/main/report/showReport.jsp");
		  scroll.append("?fileType=").append(fileType);
		  scroll.append("&download=").append("false");
		  scroll.append("&reportGenID=").append(reportGenID);
		  scroll.append("&currentPage=").append((currentPage+1));
		  scroll.append("&createTimeInMillis=").append((createTimeInMillis));
		  scroll.append("\">后一页</a>&nbsp;");
	  }
	  else
	  {
		  scroll.append("<span style=\"font-size:12px\">后一页</span>&nbsp;");
	  }
	  if(currentPage<pageCount)
	  { 
	  scroll.append("<a style=\"font-size:12px;text-decoration: underline\" href=\"/ecc/main/report/showReport.jsp");
	  scroll.append("?fileType=").append(fileType);
	  scroll.append("&download=").append("false");
	  scroll.append("&reportGenID=").append(reportGenID);
	  scroll.append("&currentPage=").append(pageCount);
	  scroll.append("&createTimeInMillis=").append((createTimeInMillis));
	  scroll.append("\">末一页</a>&nbsp;");
	  }
	  else
	  {
		  scroll.append("<span style=\"font-size:12px\">末一页</span>&nbsp;");
	  }
	  String filePath=StatsReport.getCreateFile(createTimeInMillis, reportGenID, fileType);
	  scroll.append("<a style=\"font-size:12px;text-decoration: underline\" href=\"/ecc/main/report/saveReport");
	  scroll.append("?fileType=").append(fileType);
	  scroll.append("&download=").append("true");
	  scroll.append("&reportGenID=").append(reportGenID);
	  scroll.append("&currentPage=").append((currentPage));
	  scroll.append("&createTimeInMillis=").append((createTimeInMillis));
	  scroll.append("\">保存</a>&nbsp;");
	  boolean isDownload=Boolean.parseBoolean(this.download);
//	  if(isDownload) downloadFile();
	  return scroll.toString();
  }
  
  public void doGet(HttpServletRequest req,HttpServletResponse rsp){
	  FileInputStream fis = null;
	  String reportGenID = req.getParameter("reportGenID");
	  String createTimeInMillis = req.getParameter("createTimeInMillis"); 
	  try {
	  if(reportGenID!=null && !reportGenID.equals("") && !reportGenID.equals("null")){
		String strZipDirPath = Constand.statreportsavepath + "statreport";
			// 清除目录等
		com.siteview.ecc.tuopu.MakeTuopuData.delFolder(strZipDirPath);
		com.siteview.ecc.tuopu.MakeTuopuData.createFolder(strZipDirPath);
		com.siteview.ecc.tuopu.MakeTuopuData.delFile(strZipDirPath + ".zip");
//		String name=Toolkit.getToolkit().formatDate(new Date(Long.parseLong(reportGenID)),"yyyyMMdd_")+reportGenID;
		String name=createTimeInMillis + "_" + reportGenID;
		// 组建压缩包目录
		String strSrcDirPath = Constand.statreportsavepath + name + ".html_files";
		String strSrcHtmlPath = Constand.statreportsavepath + name + ".html";
		String strSrcImgPath = Constand.statreportsavepath + reportGenID + ".html_files";
			
		String strDestDirPath = Constand.statreportsavepath + "statreport\\" + name + ".html_files";
		String strDestHtmlPath = Constand.statreportsavepath + "statreport\\" + name + ".html";
		String strDestImgPath = Constand.statreportsavepath + "statreport\\"+ reportGenID + ".html_files";
		com.siteview.ecc.tuopu.MakeTuopuData.copyFile(strSrcHtmlPath, strDestHtmlPath);
		com.siteview.ecc.tuopu.MakeTuopuData.copyFolder(strSrcDirPath, strDestDirPath);
		com.siteview.ecc.tuopu.MakeTuopuData.copyFolder(strSrcImgPath, strDestImgPath);
		DirectoryZip zip = new DirectoryZip();
		zip.zip(strZipDirPath, strZipDirPath + ".zip");
		File f2 = new File(strZipDirPath + ".zip");
		if(f2!=null && f2.exists()){
			logger.info(f2.getName());
			fis = new FileInputStream(f2);
			rsp.setContentType("APPLICATION/OCTET-STREAM"); 
			rsp.setHeader("Content-Disposition", "attachment; filename=\"" + "staticReport.zip" + "\"");
			int size = 0;
			byte[] buff = new byte[1024];
			OutputStream os = rsp.getOutputStream();
			while((size=fis.read(buff))!=-1){
				os.write(buff, 0, size);
				os.flush();
			}
			os.close();
			fis.close();
		}
	  }
	 //req.getRequestDispatcher("/main/report/showReport.jsp").forward(req, rsp);
	 return;
	 } catch (Exception e) {
		 e.printStackTrace();
	 }
  }
}
