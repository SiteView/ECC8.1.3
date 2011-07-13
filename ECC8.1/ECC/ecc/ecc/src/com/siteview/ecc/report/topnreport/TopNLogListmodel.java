package com.siteview.ecc.report.topnreport;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;

import com.siteview.base.data.IniFile;
import com.siteview.ecc.progress.BlankWindow;
import com.siteview.ecc.report.TopNReportComposer;
import com.siteview.ecc.reportserver.Constand;
import com.siteview.ecc.util.Toolkit;

public class TopNLogListmodel extends ListModelList implements ListitemRenderer
{
	private IniFile infile;
	private TopNReportComposer topNReportComposer;
	public TopNLogListmodel(IniFile infile,TopNReportComposer topNReportComposer)
	{
		super();
		clear();
		this.infile = infile;
		this.topNReportComposer=topNReportComposer;
		List list = new ArrayList<TopNLogBean>();
		for (String section : infile.getSectionList())
		{
			if (section.equalsIgnoreCase("TempSection(Please_modify_it)"))
				continue;
			Map<String, String> dmap = infile.getSectionData(section);
			if (dmap == null)
			{
				continue;
			}
			TopNLogBean logbean = new TopNLogBean();
			String filetype = dmap.get("fileType");
			if (filetype == null)
			{
				filetype = "html";
			}
			logbean.setFiletype(filetype);
			
			String filename = section;
			logbean.setTitle(filename);//1252304338546-1252390738546TopN
			
			String sgdate = filename.substring(filename.indexOf("-") + 1);//1252390738546TopN
			String gdate = sgdate.substring(0, 13);///1252390738546
			String geratedate = Toolkit.getToolkit().formatDate(Long.parseLong(gdate));//1252390738546
			logbean.setGenerateDate(geratedate);
			
			String author = dmap.get("creator");
			if (author == null)
			{
				author = "";
			}
			logbean.setAuthor(author);
			String name = getfilename(filename, filetype);
			String enable = "不存在";
			if (new File(name).exists())
			{
				enable = "存在";
			}
			logbean.setEnabled(enable);
			list.add(logbean);
			
		}
		Collections.sort(list, new ChartObjectData());
		addAll(list);
	}
	/**
	 * 
	 * 实现排序用的
	 * 
	 */
	class ChartObjectData implements Comparator
	{
		String	id;
		
		ChartObjectData()
		{
		}
		
		/*
		 * 比较的算法
		 */
		public int compare(Object obj1, Object obj2)
		{ 
			String filename1=((TopNLogBean)obj1).getTitle();
			String filename2=((TopNLogBean)obj2).getTitle();
			String sgdate1 = filename1.substring(filename1.indexOf("-") + 1);
			String gdate1 = sgdate1.substring(0, 13);;
			Long a =Long.parseLong(gdate1); 	
			String sgdate2 = filename2.substring(filename2.indexOf("-") + 1);
			String gdate2 = sgdate2.substring(0, 13);;
			Long b =Long.parseLong(gdate2); 	
				if (a > b)
				{
					return 0;
				} else
				{
					return 1;
				}
			
		}
		
	}
	
	public void onClickShowReport(Event event)
	{
		TopNLogBean logbean = (TopNLogBean) event.getTarget().getAttribute("logbean");
		String fileType = logbean.getFiletype();
		String filename = "";
		String filenametitle = logbean.getTitle();
		filename = getfilename(filenametitle, fileType);
		int index = filenametitle.indexOf("-");
		String start = filenametitle.substring(0,index);
		String end = filenametitle.substring(index+1,index+1+13);
		
		String title = Toolkit.getToolkit().formatDate(Long.parseLong(start))+"~"+Toolkit.getToolkit().formatDate(Long.parseLong(end));
		if (filename.equals(""))
		{
			return;
		}
		// AMedia(String name, String format, String ctype, URL url, String charset)
		
		try{
				if (fileType.equals("html"))
				{
					File file = new File(filename);
					if(file.exists()){
						final BlankWindow topWin = (BlankWindow) Executions.createComponents(
								"/main/progress/blankwin.zul", null, null);
						
						topWin.setTitle(title);
						topWin.setVisible(true);
						//String filePath = filename.getBytes("ISO-")
						
						try {
							topWin.showUrl("/main/report/showTopNReport.jsp?topNReportPath="+URLEncoder.encode(filename,"UTF-8")+"&fileType="+fileType+"&currentPage="+1);
							topWin.doModal();
						} catch (SuspendNotAllowedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						return;
					}
					
				} else if (fileType.equals("pdf"))
				{
					try {
						//fis=new FileInputStream(filename);
						File file = new File(filename);
						if(file.exists()){
							Filedownload.save(new AMedia(URLDecoder.decode(filenametitle,"UTF-8") +".pdf","pdf", "application/pdf",file,true));
							Filedownload.save(new AMedia(filenametitle +".pdf","pdf", "application/pdf",file,true));
						}
					} catch (Exception e) {return;}finally
					{
					}
					//Filedownload.save(new AMedia(filenametitle + ".pdf", "pdf", "application/pdf", fis));
				}
				else if (fileType.equals("xls"))
				{
					try {
						//fis=new FileInputStream(filename);
						File file = new File(filename);
						if(file.exists()){
							Filedownload.save(new AMedia(filenametitle + ".xls", "xls", "application/vnd.ms-excel", file, true));
						}
					} catch (Exception e) {return;}finally
					{
					}
					//Filedownload.save(new AMedia(filenametitle + ".xls", "xls", "application/vnd.ms-excel", fis));
				}
		}finally
		{
		}		
	}
	
	public static String getfilename(String filename, String filetype)
	{
		String pathname = "";
		if (filetype.equals("html"))
		{
			pathname = Constand.topnreportsavepath + filename + ".html";
		} else if (filetype.equals("xls"))
		{
			pathname = Constand.topnreportsavepath + filename + ".xls";
		} else if (filetype.equals("pdf"))
		{
			pathname = Constand.topnreportsavepath + filename + ".pdf";
		}
		return pathname;
	}
	
	@Override
	public void render(Listitem arg0, Object arg1) throws Exception
	{
		// TODO Auto-generated method stub
		
		Listitem item = arg0;
		item.setHeight("23px");
		TopNLogBean logBean = (TopNLogBean) arg1;//1252308449437-1252394849437TopN.html
		item.setValue(logBean);
		Listcell tmpcell = null;
		tmpcell = new Listcell();
		tmpcell.setParent(item);
		String fileType = logBean.getFiletype();
		Image img = new Image("/main/images/filetype/" + fileType + ".gif");
		img.setTooltiptext(fileType);
		img.setParent(tmpcell);
		
		String title = logBean.getTitle();
		
		int index = title.indexOf("-");
		String start = title.substring(0,index);
		
		String end = title.substring(index+1,index+1+13);
		String title2 = Toolkit.getToolkit().formatDate(Long.parseLong(start))+"~"+Toolkit.getToolkit().formatDate(Long.parseLong(end));
		
		Label label = new Label(title2);
		label.setStyle("border-left-width: 1px; border-right-width: 1px; border-top-width: 1px; border-bottom: 1px dashed #C0C0C0");
		tmpcell = new Listcell();
		label.setParent(tmpcell);
		tmpcell.setParent(item);
		tmpcell.setAttribute("logbean", logBean);
		tmpcell.addEventListener("onClick", new EventListener()
		{
			
			@Override
			public void onEvent(Event event) throws Exception
			{
				onClickShowReport(event);
				
			}
		});
		tmpcell.setTooltiptext(title2);
		
		tmpcell = new Listcell(logBean.getGenerateDate());
		tmpcell.setParent(item);
		
		tmpcell = new Listcell(logBean.getAuthor());
		tmpcell.setParent(item);
		String isvalid=logBean.getEnabled();
		tmpcell = new Listcell(isvalid);
		if(isvalid.equals("不存在"))
		{
			item.setStyle("color:gray");
		}
		tmpcell.setParent(item);
		tmpcell = new Listcell();
		img = new Image("/main/images/action/delete.gif");
		img.setParent(tmpcell);
		img.addEventListener("onClick", new deleteItem(title,getfilename(title, fileType)) );
		tmpcell.setParent(item);
		
	}
	private class deleteItem implements EventListener
	{
         private String section;
         private String finame;
		public deleteItem(String section,String filename)
		{
			this.section=section;
			this.finame=filename;
			
		}
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			// TODO Auto-generated method stub
			try
			{
				int ret = Messagebox.show("你确认要删除选中的记录吗？", "提示", Messagebox.OK
						| Messagebox.CANCEL, Messagebox.QUESTION);
				
				if (ret == Messagebox.CANCEL)
					return;
			}catch(Exception e)
			{
				e.printStackTrace();
			}
//			String reportid=this.section.substring(0, this.section.indexOf("-"));
//			IniFile inifile=new IniFile("reportTopN."+reportid+".ini");
			try
			{
				infile.load();
				String fileType = infile.getFmap().get(section).get("fileType");
				String filePath = TopNLogListmodel.getfilename(section, fileType);
				File f = new File(filePath);
				f.delete();
				if(fileType.equals("html"))
				{
					f.delete();
					Toolkit.getToolkit().deleteFolder(filePath+"_files");
				}
				infile.deleteSection(section);
				infile.saveChange();
				topNReportComposer.onSelecttopNList(arg0);
			}catch(Exception e)
			{
				return;
			}
			File file=new File(finame);
			if(file.exists())
			{
				file.delete();
			}
			
		} 
	}
}
