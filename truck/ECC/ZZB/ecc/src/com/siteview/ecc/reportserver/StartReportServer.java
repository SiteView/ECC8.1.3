package com.siteview.ecc.reportserver;

import java.io.File;

import org.apache.log4j.Logger;

import com.siteview.ecc.start.EccStarter;

public class StartReportServer {
	private final static Logger logger = Logger.getLogger(StartReportServer.class);

	public static void main(String[] args) {
		String webDir = args[0];
		if (webDir == null || !new File(webDir).isDirectory()) {
			System.out
					.println("Report start with command:java com.siteview.ecc.reportserver.StartReportServer webDir");
			return;
		}
		logger.info("web dir is:" + webDir);
		EccStarter eccStarter = new EccStarter(webDir);
		ReportServer reportServer = new ReportServer();
		reportServer.startInit(eccStarter);
		Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
		while (true) 
			{
				try 
				{
					Thread.currentThread().sleep(50000);
				} catch (Exception e) {
				}

			}

	}

}
