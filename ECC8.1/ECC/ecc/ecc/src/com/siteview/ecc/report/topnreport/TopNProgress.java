package com.siteview.ecc.report.topnreport;

import java.io.File;
import java.io.FileInputStream;

import org.zkoss.util.media.AMedia;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Window;

import com.siteview.ecc.reportserver.Constand;
import com.siteview.ecc.reportserver.TopNReport;


public class TopNProgress extends GenericForwardComposer
{
	Timer progresstime=null;
	Window topnprogress;
	Label  getdata;
	public Boolean getFinishGerateFile()
	{
		return topnreport.getFinishGerateFile();
	}

	public void setFinishGerateFile(Boolean finishGerateFile)
	{
		this.finishGerateFile = finishGerateFile;
	}
	Progressmeter progressmeter;
	int progressvalue=0;
	TopNReport topnreport;
	String filetype="";
	String reportname="";
	Boolean finishGerateFile=false;
	public int getProgressvalue()
	{
		return topnreport.getProgressvalue() ;
	}

	public void setProgressvalue(int progressvalue)
	{
		this.progressvalue = progressvalue;
	}

	public void onCreate$topnprogress()
	{
		topnreport=(TopNReport)topnprogress.getAttribute("topnreport");
		filetype=(String)topnprogress.getAttribute("filetype");
		reportname=(String)topnprogress.getAttribute("reportname");
		progresstime=new Timer();
		progresstime.setParent(topnprogress);
		progresstime.setDelay(1000);
		progresstime.setRepeats(true);
		progresstime.setRunning(false);
		progresstime.addEventListener("onTimer", new ontime());
		progresstime.start();
		
	}
	public class ontime implements EventListener
	{
        public ontime()
        {
        	
        }
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			// TODO Auto-generated method stub
			int pvalue=getProgressvalue();
			if(pvalue>100)
			{
				pvalue=100;
			}
			progressmeter.setValue(pvalue);
			if(pvalue==100)
			{
				getdata.setValue("正在保存报告文件...");
			}
			if(getFinishGerateFile())
			{
			  showfile();
			  close();
			}
		}
		
	}
	
	private void showfile()
	{
		FileInputStream fis = null;
		String filename = TopNLogListmodel.getfilename(reportname, filetype);
		if (filename.equals(""))
		{
			return;
		}
		// AMedia(String name, String format, String ctype, URL url, String charset)
		
		try
		{
		if (filetype.equals("html"))
		{
			String webpath = Constand.topnreportwebpath + reportname + ".html";
			Executions.getCurrent().sendRedirect(webpath, "_blank");
		} else if (filetype.equals("pdf"))
		{
//			fis = new FileInputStream(filename);
			File file = new File(filename);
			//如果正常的话文件肯定会生成的，但是当文件很大的时候也许还没有写入操作系统
			long afterTime = 0;
			while (file.exists() == false && afterTime < 1000 * 30) {
				Thread.sleep(100);
				afterTime += 100;
			}
			Filedownload.save(new AMedia(reportname + ".pdf", "pdf", "application/pdf", file,true));
		} else if (filetype.equals("xls"))
		{
//			fis = new FileInputStream(filename);
			File file = new File(filename);
			long afterTime = 0;
			while (file.exists() == false && afterTime < 1000 * 30) {
				Thread.sleep(100);
				afterTime += 100;
			}
			Filedownload.save(new AMedia(reportname + ".xls", "xls", "application/vnd.ms-excel", file,true));
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			try
			{
				if (fis != null)
					fis.close();
			} catch (Exception e)
			{
			}
		}
	}
	private void close()
	{
		try
		{
			topnprogress.detach();
			progresstime.setRepeats(false);
			progresstime.stop();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void onClick$cancel()
	{
		topnreport.setFinish(true);
		close();
	}
	
}
