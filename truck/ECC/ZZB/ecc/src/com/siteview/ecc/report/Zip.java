package com.siteview.ecc.report;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.io.FileOutputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.zkoss.zk.ui.Executions;

public class Zip {

	public static void PagFile(String dir, File[] file1)throws Exception{
		byte[] buffer = new byte[1024];

		// ���ɵ�ZIP�ļ���Ϊ

		String strZipName = dir;

		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				Executions.getCurrent().getDesktop().getWebApp().getRealPath("/")+strZipName));
		out.setEncoding("GBK");
		// ��Ҫͬʱ���ص������ļ�

		for (int i = 0; i < file1.length; i++) {

			FileInputStream fis = new FileInputStream(file1[i]);
			String filename=new String(file1[i].getName());
			out.putNextEntry(new ZipEntry(filename));

			int len;

			// ������Ҫ���ص��ļ������ݣ������zip�ļ�

			while ((len = fis.read(buffer)) > 0) {

				out.write(buffer, 0, len);

			}

			out.closeEntry();

			fis.close();

		}

		out.close();

		System.out.println("����"+strZipName+"�ɹ�");
	}
	public static void deleteFile(String targetPath) throws IOException {
		File targetFile = new File(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/")+targetPath+".xls");
		if (targetFile.isDirectory()) {
			FileUtils.deleteDirectory(targetFile);
		} else if (targetFile.isFile()) {
			targetFile.delete();
		}else {
			targetFile.delete();
		}
	}
}
