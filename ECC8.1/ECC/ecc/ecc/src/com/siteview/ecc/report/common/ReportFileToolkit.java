package com.siteview.ecc.report.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.commons.io.IOUtils;

public class ReportFileToolkit {

	/**
	 * 删除目录
	 * 
	 * @param directory
	 * @throws IOException
	 */
	public static void deleteDirectory(File directory) throws IOException {
		if (!(directory.exists())) {
			return;
		}

		cleanDirectory(directory);
		if (!(directory.delete())) {
			String message = "Unable to delete directory " + directory + ".";

			throw new IOException(message);
		}
	}

	public static void cleanDirectory(File directory) throws IOException {
		String message;
		if (!(directory.exists())) {
			message = directory + " does not exist";
			throw new IllegalArgumentException(message);
		}

		if (!(directory.isDirectory())) {
			message = directory + " is not a directory";
			throw new IllegalArgumentException(message);
		}

		File[] files = directory.listFiles();
		if (files == null) {
			throw new IOException("Failed to list contents of " + directory);
		}

		IOException exception = null;
		for (int i = 0; i < files.length; ++i) {
			File file = files[i];
			try {
				forceDelete(file);
			} catch (IOException ioe) {
				exception = ioe;
			}
		}

		if (null != exception)
			throw exception;
	}

	public static void forceDelete(File file) throws IOException {
		if (file.isDirectory()) {
			deleteDirectory(file);
		} else {
			if (!(file.exists()))
				throw new FileNotFoundException("File does not exist: " + file);

			if (!(file.delete())) {
				String message = "Unable to delete file: " + file;

				throw new IOException(message);
			}
		}
	}

	/**
	 * 复制一个目录到另一个目录
	 * 
	 * @param srcDir
	 *            被复制的目录
	 * @param destDir
	 *            接受被复制目录的目录
	 * @throws IOException
	 */
	public static void copyDirectoryToDirectory(File srcDir, File destDir)
			throws IOException {
		if (srcDir == null)
			throw new NullPointerException("Source must not be null");

		if ((srcDir.exists()) && (!(srcDir.isDirectory())))
			throw new IllegalArgumentException("Source '" + destDir
					+ "' is not a directory");

		if (destDir == null)
			throw new NullPointerException("Destination must not be null");

		if ((destDir.exists()) && (!(destDir.isDirectory())))
			throw new IllegalArgumentException("Destination '" + destDir
					+ "' is not a directory");

		copyDirectory(srcDir, new File(destDir, srcDir.getName()), true);
	}

	public static void copyDirectory(File srcDir, File destDir,
			boolean preserveFileDate) throws IOException {
		if (srcDir == null)
			throw new NullPointerException("Source must not be null");

		if (destDir == null)
			throw new NullPointerException("Destination must not be null");

		if (!(srcDir.exists()))
			throw new FileNotFoundException("Source '" + srcDir
					+ "' does not exist");

		if (!(srcDir.isDirectory()))
			throw new IOException("Source '" + srcDir
					+ "' exists but is not a directory");

		if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath()))
			throw new IOException("Source '" + srcDir + "' and destination '"
					+ destDir + "' are the same");

		doCopyDirectory(srcDir, destDir, preserveFileDate);
	}

	private static void doCopyDirectory(File srcDir, File destDir,
			boolean preserveFileDate) throws IOException {
		if (destDir.exists()) {
			if (!destDir.isDirectory())
				throw new IOException("Destination '" + destDir
						+ "' exists but is not a directory");
		}

		if (!destDir.exists() && !(destDir.mkdirs()))
			throw new IOException("Destination '" + destDir
					+ "' directory cannot be created");

		if (preserveFileDate) {
			destDir.setLastModified(srcDir.lastModified());
		}

		if (!(destDir.canWrite())) {
			throw new IOException("Destination '" + destDir
					+ "' cannot be written to");
		}

		File[] files = srcDir.listFiles();
		if (files == null)
			throw new IOException("Failed to list contents of " + srcDir);

		for (int i = 0; i < files.length; ++i) {
			File copiedFile = new File(destDir, files[i].getName());
			if (files[i].isDirectory())
				doCopyDirectory(files[i], copiedFile, preserveFileDate);
			else
				doCopyFile(files[i], copiedFile, preserveFileDate);
		}
	}

	private static void doCopyFile(File srcFile, File destFile,
			boolean preserveFileDate) throws IOException {
		if ((destFile.exists()) && (destFile.isDirectory())) {
			throw new IOException("Destination '" + destFile
					+ "' exists but is a directory");
		}

		FileInputStream input = new FileInputStream(srcFile);
		try {
			FileOutputStream output = new FileOutputStream(destFile);
			try {
				IOUtils.copy(input, output);
			} finally {
				IOUtils.closeQuietly(output);
			}
		} finally {
			IOUtils.closeQuietly(input);
		}

		if (srcFile.length() != destFile.length()) {
			throw new IOException("Failed to copy full contents from '"
					+ srcFile + "' to '" + destFile + "'");
		}

		if (preserveFileDate)
			destFile.setLastModified(srcFile.lastModified());
	}

	public static boolean makeFile(File file, String content)
			throws IOException {
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			fos = new FileOutputStream(file);
			osw = new OutputStreamWriter(fos, "utf-8");
			bw = new BufferedWriter(osw);
			bw.write(content);
		} catch (Exception e) {
			throw new IOException("fail to make file :" + file.getName() + "!");
		} finally {
			if (bw != null)
				bw.close();
			if (osw != null)
				osw.close();
			if (fos != null)
				fos.close();
		}
		return true;
	}

	public static File[] listHtmlFiles(File destDir , final String suffix) throws IOException{
		if (destDir == null)
			throw new NullPointerException("Destination must not be null");

		if (!(destDir.exists()))
			throw new FileNotFoundException("Source '" + destDir
					+ "' does not exist");

		if (!(destDir.isDirectory()))
			throw new IOException("Source '" + destDir
					+ "' exists but is not a directory");
		return destDir.listFiles(new FilenameFilter(){
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(suffix);
			}
		});
	}
	public static int getReportPageCount(File file) {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		int totalPage = 0;
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "utf-8");
			reader = new BufferedReader(isr);
			String strReadLine = "";
			while ((strReadLine = reader.readLine())  != null) {
				if (strReadLine.startsWith("<a name=\"JR_PAGE_ANCHOR")) {
					totalPage++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		return totalPage;
	}

	/**
	 * 针对ireport报表生成的html文件过大浏览器不能打开而进行的分页处理
	 * 
	 * @param file
	 * @param pageSize
	 * @return
	 * @throws IOException
	 */
/*	public static boolean splitHtmlFile(File file,String fileName,String fileId) throws IOException {
		if (!file.exists())
			return false;
		String head = new String(
				"<html><head><title></title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"
						+ "<style type=\"text/css\">a {text-decoration: none}</style></head>"
						+ "<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\">"
						+ "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">"
						+ "<tr><td width=\"50%\">&nbsp;</td><td align=\"center\">");
		
		String tail = "</td><td width=\"50%\">&nbsp;</td></tr></table></body></html>";

		int pageCount = 0;
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		File htmlFolder = new File(file.getParentFile(),fileName);
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis, "utf-8");
			reader = new BufferedReader(isr);
			String strReadLine = reader.readLine();
			while (strReadLine != null) {
				if (strReadLine.startsWith("<a name=\"JR_PAGE_ANCHOR")) {
					pageCount++;
				}
				strReadLine = reader.readLine();
			}
			
			if (!htmlFolder.exists()) {
				if (!htmlFolder.mkdir())
					throw new IOException("File to create folder :"
							+ htmlFolder.getName());
			}
			
			StringBuffer sb = new StringBuffer();
			for (int i = 1; i <= pageCount; i++) {
				File htmlFile = new File(htmlFolder, fileName + i + ".html");
				if (!htmlFile.exists())
					htmlFile.createNewFile();
				StringBuilder tmp = new StringBuilder(head);
				String begin_string = String.format("<a name=\"JR_PAGE_ANCHOR_0_%d\"/>", i);
				String end_string = String.format("<a name=\"JR_PAGE_ANCHOR_0_%d\"/>", i + 1);
				int begin_index = sb.indexOf(begin_string);
				int end_index = sb.indexOf(end_string);
				String content = "";
				if (i == pageCount) {
					content = sb.substring(begin_index);
				} else {
					content = sb.substring(begin_index, end_index);
				}
				tmp.append(content);
				int rowindex = tmp.indexOf("<table>", begin_index);
				rowindex = tmp.indexOf("<tr>", rowindex);
				if (i != pageCount)
					tmp.append(tail);
				makeFile(htmlFile, tmp.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				throw new IOException(e);
			}
			try {
				isr.close();
			} catch (Exception e) {
				throw new IOException(e);
			}
			try {
				fis.close();
			} catch (Exception e) {
				throw new IOException(e);
			}
		}
		File pxFile = new File(file.getParentFile(), fileName + ".html_files");
		File imageFile = new File(file.getParentFile(),fileId+ ".html_files");
		copyDirectoryToDirectory(pxFile, htmlFolder);
		copyDirectoryToDirectory(imageFile, htmlFolder);
		forceDelete(file);
		deleteDirectory(pxFile);
		deleteDirectory(imageFile);
		return true;
	}*/
	/**
	 * 针对ireport报表生成的html文件过大浏览器不能打开而进行的分页处理
	 * 
	 * @param file
	 * @param pageSize
	 * @return
	 * @throws IOException
	 */
	public static boolean splitHtmlFile(File file,String fileName,String fileId) throws IOException {
		
		if ( !file.exists() )
			throw new IOException( file + " is not exists ! " );
		
		File htmlFolder = new File(file.getParentFile(),fileName);
		if (!htmlFolder.exists()) {
			if (!htmlFolder.mkdir())
				throw new IOException("Faile to create folder :"
						+ htmlFolder.getName());
		}
		StringBuffer head = new StringBuffer();
		head.append("<html>\n");
		head.append("<head>\n");
		head.append("<title></title>\n");
		head.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n");
		head.append("<style type=\"text/css\">a {text-decoration: none}</style>\n");
		head.append("</head>\n");
		head.append("<body text=\"#000000\" link=\"#000000\" alink=\"#000000\" vlink=\"#000000\">\n");
		head.append("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n");
		head.append("<tr>\n");
		head.append("<td width=\"50%\">&nbsp;</td>\n");
		head.append("<td align=\"center\">\n");
		
		StringBuffer tail = new StringBuffer();
		tail.append("</td>\n");
		tail.append("<td width=\"50%\">&nbsp;</td>\n");
		tail.append("</tr>\n");
		tail.append("</table>\n");
		tail.append("</body>\n");
		tail.append("</html>\n");

		int pageCount = getReportPageCount(file);
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader reader = null;
		try {
			for(int i=1;i<=pageCount;i++){
				fis = new FileInputStream(file);
				isr = new InputStreamReader(fis, "utf-8");
				reader = new BufferedReader(isr);
				File htmlFile = new File(htmlFolder, fileName + i + ".html");
				StringBuffer sb = new StringBuffer();
				int page = 0;
				String str = "";
				while ( ( str = reader.readLine() ) != null ) {
					if ( str.startsWith("<a name=\"JR_PAGE_ANCHOR") ) {
						page++;
					}
					if ( i == page )
						sb.append(str).append("\n");
				}
				sb.insert(0, head);
				if( i != pageCount ) {
					sb.append(tail);
				}
				reader.close();
				isr.close();
				fis.close();
				makeFile(htmlFile, sb.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
				throw new IOException(e);
			}
			try {
				isr.close();
			} catch (Exception e) {
				throw new IOException(e);
			}
			try {
				fis.close();
			} catch (Exception e) {
				throw new IOException(e);
			}
		}
		File pxFile = new File(file.getParentFile(), fileName + ".html_files");
		File imageFile = new File(file.getParentFile(),fileId+ ".html_files");
		copyDirectoryToDirectory(pxFile, htmlFolder);
		if(imageFile.exists())
		{	
			copyDirectoryToDirectory(imageFile, htmlFolder);
			deleteDirectory(imageFile);
		}
		forceDelete(file);
		deleteDirectory(pxFile);
		return true;
	}	
	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
//		getReportPageCount(new File("d:\\20091111_1257911712234.html"));
		splitHtmlFile(new File("d:\\20091112_1258014681125.html"),"20091112_1258014681125","1258014681125");
		//File[] files = listHtmlFiles(new File("D:\\20090925_1253868441109"),"html");
//		System.out.println(getReportPageCount(new File("d:\\20091111_1257911712234.html")));
//		File file = new File("E:\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\ecc\\main\\report\\statreport\\test.html");
//		file.createNewFile();
		File file = new File("");
		
	}

}
