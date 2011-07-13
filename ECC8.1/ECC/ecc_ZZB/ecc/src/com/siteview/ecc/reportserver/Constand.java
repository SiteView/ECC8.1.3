package com.siteview.ecc.reportserver;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.util.Toolkit;

/**
 * reportserver用到的常量
 * 
 * @author di.tang
 * 
 */
public class Constand {
	public static String adminusername = "admin";
	public static String adminpassword = "siteview";
	public final static String statreportsavepath = EccWebAppInit.getWebDir() + "main\\report\\statreport\\"; // 自动生成的统计报告保存位置
	public final static String statreportwebpath = "/main/report/statreport/"; 								// 自动生成的统计报告引用位置
	public final static String topnreportsavepath=EccWebAppInit.getWebDir() + "main\\report\\topnreport\\"; // 自动生成的topn报告保存位置
	public final static String topnreportwebpath = "/main/report/topnreport/";// 自动生成的统计报告引用位置 	
	public final static String fisheyeimagesavepath = EccWebAppInit.getWebDir() + "\\main\\images\\fisheye\\img\\"; // 鱼眼图图片保存位置
	public final static String downloadreportpath=EccWebAppInit.getWebDir() + "main\\report\\downloadreport\\";//导出报表文件临时存放位置
	static {
		File f = new File(statreportsavepath);
		if (!f.exists()) {
			com.siteview.ecc.tuopu.MakeTuopuData.createFolder(statreportsavepath);
		}
		File f2 = new File(fisheyeimagesavepath);
		if (!f2.exists()) {
			com.siteview.ecc.tuopu.MakeTuopuData.createFolder(fisheyeimagesavepath);
		}
		f2 = new File(downloadreportpath);
		if (!f2.exists()) {
			com.siteview.ecc.tuopu.MakeTuopuData.createFolder(downloadreportpath);
		}
	}

	public static String getTopNReportBystrReportName(String strReportName) {
		return strReportName.replace(" ", "_").replace(":", "_").replace("*", "_").replace("/", "_").replace("\\", "_").replace("?", "_")
				.replace("|", "_").replace("<", "_").replace(">", "_").replace("\"", "_")
				+ "TopN";
	}

	public static String getTopNReportFileName(String t_t_section) {
		return statreportsavepath + getName(t_t_section) + "TopN.html";
	}

	private static String getName(String t_t_section) {
		String url = null;
		url = t_t_section.substring(t_t_section.indexOf("$") + 1);
		String stime = url.substring(0, url.indexOf("$"));
		String etime = url.substring(url.indexOf("$") + 1, url.lastIndexOf("$"));
		Calendar cal = Calendar.getInstance();
		Date a;
		Date b;
		try {
			a = Toolkit.getToolkit().parseDate(stime);
			b = Toolkit.getToolkit().parseDate(etime);

			stime = Toolkit.getToolkit().formatDate(a);
			etime = Toolkit.getToolkit().formatDate(b);
			stime = stime + etime + url.substring(url.lastIndexOf("$") + 1);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		url = stime.replace("$", "").replace(":", "_").replace(" ", "_").replace("|", "_");
		return url;
	}
}
