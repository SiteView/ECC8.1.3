package com.siteview.ecc.reportserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.log4j.Logger;
import org.apache.tools.zip.ZipOutputStream;

/**
 * @author tyrone
 * 
 */

public class DirectoryZip {
	private final static Logger logger = Logger.getLogger(DirectoryZip.class);
	/**
	 *@param inputFileName
	 *            , file or directory waiting for zipping ,outputFileName output
	 *            file name
	 * 
	 */
	public void zip(String inputFileName, String outputFileName)
			throws Exception {
		FileOutputStream fos = null;
		ZipOutputStream out = null;
		try {
			fos = new FileOutputStream(outputFileName);
			out = new ZipOutputStream(fos);
			zip(out, new File(inputFileName), "");
			logger.info("zip done");
		} finally {
			try {
				out.close();
			} catch (Exception e) {
			}
			try {
				fos.close();
			} catch (Exception e) {
			}
		}
	}

	private void zip(ZipOutputStream out, File f, String base) throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			if (System.getProperty("os.name").startsWith("Windows")) {
				out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "\\"));
				base = base.length() == 0 ? "" : base + "\\";
			} else {
				out.putNextEntry(new org.apache.tools.zip.ZipEntry(base + "/"));
				base = base.length() == 0 ? "" : base + "/";
			}
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else {
			out.putNextEntry(new org.apache.tools.zip.ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			try {
				logger.info(base);
				int b;
				while ((b = in.read()) != -1) {
					out.write(b);
				}
			} finally {
				in.close();
			}
		}
	}

	public static void main(String[] args) {
		DirectoryZip m_zip = new DirectoryZip();
		try {
			String name = "C:/Program Files/Apache Software Foundation/Tomcat 6.0/webapps/ecc/main/report/statreport/statreport";
			m_zip.zip(name, "d:\\2005.zip");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
