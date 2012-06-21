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

		// 生成的ZIP文件名为

		String strZipName = dir;

		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				Executions.getCurrent().getDesktop().getWebApp().getRealPath("/")+strZipName));
		out.setEncoding("GBK");
		// 需要同时下载的两个文件

		for (int i = 0; i < file1.length; i++) {

			FileInputStream fis = new FileInputStream(file1[i]);
			String filename=new String(file1[i].getName());
			out.putNextEntry(new ZipEntry(filename));

			int len;

			// 读入需要下载的文件的内容，打包到zip文件

			while ((len = fis.read(buffer)) > 0) {

				out.write(buffer, 0, len);

			}

			out.closeEntry();

			fis.close();

		}

		out.close();

		System.out.println("生成"+strZipName+"成功");
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
