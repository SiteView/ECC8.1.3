package com.siteview.ecc.report.topnreport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.siteview.ecc.reportserver.Constand;
import com.siteview.ecc.reportserver.DirectoryZip;

public class HtmlTopNReportShow extends HttpServlet {
	private final static Logger logger = Logger.getLogger(HtmlTopNReportShow.class);
	private int currentPage = 1;
	private int pageCount = -1;
	private String topNReportPath;
	private String fileType = "html";

	public HtmlTopNReportShow() {
	}

	public HtmlTopNReportShow(String topNReportPath, int currentPage) {
		this.topNReportPath = topNReportPath;
		this.currentPage = currentPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public String getTopNReportPath() {
		return topNReportPath;
	}

	public void setTopNReportPath(String topNReportPath) {
		this.topNReportPath = topNReportPath;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * 显示TopN报告中html格式的报告
	 * 
	 * @return String
	 */
	public String getTopNContent() {
		File file = new File(topNReportPath);
		if (!file.exists())
			return "文件不存在!";

		StringBuffer content = new StringBuffer();
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "utf-8");
			reader = new BufferedReader(isr);
			String strReadLine = reader.readLine();
			int readPage = 0;
			while (strReadLine != null) {
				if (strReadLine.startsWith("<a name=\"JR_PAGE_ANCHOR"))
					readPage++;

				if (readPage == 0 || readPage == currentPage)
					content.append(strReadLine).append("\n");

				strReadLine = reader.readLine();
			}

			this.pageCount = readPage;

			return makeContent(content);

		} catch (Exception e) {
			return e.getMessage();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
			try {
				isr.close();
			} catch (Exception e) {
			}
			try {
				fis.close();
			} catch (Exception e) {
			}

		}

	}

	private String makeContent(StringBuffer content) {
		/*
		 * int idx=content.indexOf("第"+currentPage+"页"); if(idx>0)
		 * content.insert(idx, getScroolBar()); else
		 * content.append(getScroolBar());
		 */
		content.insert(0, getScroolBar());

		content.append("</td><td width=\"50%\">&nbsp;</td></tr>\n");
		content.append("</table>\n");
		content.append("</body>\n");
		content.append("</html>\n");

		return content.toString();
	}

	private String getScroolBar() {
		StringBuffer scroll = new StringBuffer();

		if (currentPage > 1) {
			scroll
					.append("<a style=\"font-size:12px;text-decoration: underline\" href=\"/ecc/main/report/showTopNReport.jsp");
			scroll.append("?fileType=").append(fileType);
			scroll.append("&topNReportPath=").append(topNReportPath);
			scroll.append("&currentPage=").append(1);
			scroll.append("\">第一页</a>&nbsp;");
		} else {
			scroll.append("<span style=\"font-size:12px\">第一页</span>&nbsp;");
		}
		if (currentPage > 1) {
			scroll
					.append("<a style=\"font-size:12px;text-decoration: underline\" href=\"/ecc/main/report/showTopNReport.jsp");
			scroll.append("?fileType=").append(fileType);
			scroll.append("&topNReportPath=").append(topNReportPath);
			scroll.append("&currentPage=").append((currentPage - 1));
			scroll.append("\">前一页</a>&nbsp;");
		} else {
			scroll.append("<span style=\"font-size:12px\">前一页</span>&nbsp;");
		}
		if (currentPage < pageCount) {
			scroll
					.append("<a style=\"font-size:12px;text-decoration: underline\" href=\"/ecc/main/report/showTopNReport.jsp");
			scroll.append("?fileType=").append(fileType);
			scroll.append("&topNReportPath=").append(topNReportPath);
			scroll.append("&currentPage=").append((currentPage + 1));
			scroll.append("\">后一页</a>&nbsp;");
		} else {
			scroll.append("<span style=\"font-size:12px\">后一页</span>&nbsp;");
		}
		if (currentPage < pageCount) {
			scroll
					.append("<a style=\"font-size:12px;text-decoration: underline\" href=\"/ecc/main/report/showTopNReport.jsp");
			scroll.append("?fileType=").append(fileType);
			scroll.append("&topNReportPath=").append(topNReportPath);
			scroll.append("&currentPage=").append(pageCount);
			scroll.append("\">末一页</a>&nbsp;");
		} else {
			scroll.append("<span style=\"font-size:12px\">末一页</span>&nbsp;");
		}
		scroll
				.append("<a style=\"font-size:12px;text-decoration: underline\" href=\"/ecc/main/report/saveTopReport");
		scroll.append("?fileType=").append(fileType);
		scroll.append("&topNReportPath=").append(topNReportPath);
		scroll.append("&currentPage=").append(currentPage);
		scroll.append("\">保存</a>&nbsp;");
		return scroll.toString();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse rsp) {
		FileInputStream fis = null;
		String topNReportPath = req.getParameter("topNReportPath");
		try {
			if (topNReportPath!=null && !topNReportPath.equals("") && !topNReportPath.equals("null")) {
				File f = new File(topNReportPath);
				String name = f.getName().substring(0,f.getName().lastIndexOf("."));
				String strZipDirPath = Constand.topnreportsavepath+ "report";
				// 清除目录等
				com.siteview.ecc.tuopu.MakeTuopuData.delFolder(strZipDirPath);
				com.siteview.ecc.tuopu.MakeTuopuData.createFolder(strZipDirPath);
				com.siteview.ecc.tuopu.MakeTuopuData.delFile(strZipDirPath+ ".zip");
				// 组建压缩包目录
				String strSrcDirPath = Constand.topnreportsavepath + name+ ".html_files";
				String strSrcHtmlPath = Constand.topnreportsavepath + name+ ".html";
				
				String strDestDirPath = Constand.topnreportsavepath	+ "report\\" + name + ".html_files";
				String strDestHtmlPath = Constand.topnreportsavepath+ "report\\" + name + ".html";
				com.siteview.ecc.tuopu.MakeTuopuData.copyFile(strSrcHtmlPath,	strDestHtmlPath);
				com.siteview.ecc.tuopu.MakeTuopuData.copyFolder(strSrcDirPath,	strDestDirPath);
				DirectoryZip zip = new DirectoryZip();
				zip.zip(strZipDirPath, strZipDirPath + ".zip");
				File f2 = new File(strZipDirPath + ".zip");
				if (f2 != null && f2.exists()) {
					logger.info(f2.getName());
					fis = new FileInputStream(f2);
					rsp.setContentType("APPLICATION/OCTET-STREAM");
					rsp.setHeader("Content-Disposition",
							"attachment; filename=\"" + "topNReport.zip"
									+ "\"");
					int size = 0;
					byte[] buff = new byte[1024];
					OutputStream os = rsp.getOutputStream();
					while ((size = fis.read(buff)) != -1) {
						os.write(buff, 0, size);
						os.flush();
					}
					os.close();
					fis.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void doPost(HttpServletRequest req, HttpServletResponse rsp){
		doGet(req,rsp);
	}
}
