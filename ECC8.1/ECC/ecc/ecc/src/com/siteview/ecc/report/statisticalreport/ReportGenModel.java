package com.siteview.ecc.report.statisticalreport;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Executions;
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
import com.siteview.ecc.reportserver.Constand;
import com.siteview.ecc.reportserver.StatsReport;
import com.siteview.ecc.util.Toolkit;

public class ReportGenModel extends ListModelList implements ListitemRenderer{
	
	ReportComposer reportComposer;
	private ReportItem reportItem;
	public ReportGenModel(ReportComposer reportComposer,ReportItem reportItem)
	{
//		try 
//		{
			this.reportComposer=reportComposer;
			this.reportItem = reportItem;
			IniFile reportGen = reportComposer.getReportGenIni(reportItem.getReportID());
			Map<String, Map<String, String>> map = reportGen.getFmap();
			ArrayList<ReportGenItem> list = new ArrayList<ReportGenItem>();
			if(map!=null){
				Iterator iterator = map.keySet().iterator();
				while (iterator.hasNext()) {
					String genID = iterator.next().toString();
					if(!genID.equals("TempSection(Please_modify_it)")) {
						ReportGenItem item = new ReportGenItem(reportItem,genID,map.get(genID));
						list.add(item);
					}
					
				}
			}
			Collections.sort(list, new Comparator<ReportGenItem>(){
				@Override
				public int compare(ReportGenItem o1, ReportGenItem o2) {
					Date date1 = new Date(),date2 = new Date();
					try{
						date1 = Toolkit.getToolkit().parseDate(o1.getCreatTime());
					}catch(Exception e){}
					try{
						date2 = Toolkit.getToolkit().parseDate(o2.getCreatTime());
					}catch(Exception e){}
					return date2.after(date1)?1:0;
				}
			});
			addAll(list);
//		} catch (Exception e) {
//			Toolkit.getToolkit().showError(e.getMessage());
//		}

		
	}
	
	/**
	 * 点击自动生成的报告列表事件,在simplemonitorreportsinclude中显示详细的报告内容
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onClickSimplemonitorreportslistbox(Event event)
			throws Exception {
		ReportGenItem reportGenItem = (ReportGenItem) event.getTarget().getAttribute("reportGenItem");
		String fileType=reportGenItem.getFileType();
		String title=reportGenItem.getReportItem().getTitle();
		
//		//防止因为定义的报告生成时间相同，而覆盖文件导致数据丢失
		String createTime = reportItem.getCreatTime();
		Date createDate = Toolkit.getToolkit().parseDate(createTime);
		String createTimeInMillis = createDate.getTime() + "";
		
		String downLoadUrl=StatsReport.getDownLoadUrl(createTimeInMillis, reportGenItem.getGenID(),fileType);
		if(downLoadUrl.equals(""))
		{
			Toolkit.getToolkit().showError("无效的报表联结!");
			return;
		}
		
		//AMedia(String name, String format, String ctype, URL url,	String charset)
		if(fileType.equals("html"))
		{
//			File file = new File(StatsReport.getCreateFile(reportGenItem.getGenID(),fileType));
//			String fileName = Toolkit.getToolkit().formatDate(
//					new Date(Long.parseLong(reportGenItem.getGenID())), "yyyyMMdd_")
//					+ reportGenItem.getGenID();
//			String htmlurl = Constand.statreportsavepath + fileName	+ File.separator ;
			String htmlurl = StatsReport.getHtmlFolderName(createTimeInMillis, reportGenItem.getGenID());
			File file = new File(htmlurl);
			if(file.exists()){
				final BlankWindow win = (BlankWindow) Executions.createComponents(
						"/main/progress/blankwin.zul", null, null);
				
				win.setTitle(title);
				win.setVisible(true);
				//win.showUrl(downLoadUrl);
				win.setAttribute("reportGenID", reportGenItem.getGenID());
//				win.showUrl("D:\\20090925_1253868441109\\20090925_12538684411091.html");
				win.showUrl("/main/report/showStatisticReport.jsp?reportGenID="+reportGenItem.getGenID()+"&currentPage=1&createTimeInMillis="+createTimeInMillis);
				win.doModal();
			}else{
				return;
			}
		}
		else if(fileType.equals("pdf"))
		{
		  File file = new File(StatsReport.getCreateFile(createTimeInMillis, reportGenItem.getGenID(),fileType));
		  if(file.exists()){
			  Filedownload.save(new AMedia(title+".pdf","pdf", "application/pdf",file,true));
		  }
		  //fis=new FileInputStream(StatsReport.getCreateFile(reportGenItem.getGenID(),fileType));
		  //Executions.getCurrent().sendRedirect(downLoadUrl,"_blank");
		}
		else if(fileType.equals("xls"))
		{
		 //fis=new FileInputStream(StatsReport.getCreateFile(reportGenItem.getGenID(),fileType));
		 //Filedownload.save(new AMedia(title+".xls","xls", "application/vnd.ms-excel",fis));
		 File file = new File(StatsReport.getCreateFile(createTimeInMillis, reportGenItem.getGenID(),fileType));
		 if(file.exists()){
			 Filedownload.save(new AMedia(title+".xls","xls", "application/vnd.ms-excel",file,true));
		 }
		}
		

	}
	@Override
	public void render(Listitem parent, Object data) throws Exception 
	{
		parent.setHeight("23px");
		ReportGenItem item=(ReportGenItem)data;
		parent.setValue(item);
		parent.setCheckable(true);
		parent.setAttribute("reportGenItem",item);

		Listcell cell=new Listcell();
		Image img=new Image();
		img.setSrc("/main/images/filetype/"+item.getFileType()+".gif");
		img.setTooltip(item.getFileType());
		img.setAlign("absmiddle");
		cell.appendChild(img);
		cell.setParent(parent);

		cell=new Listcell();
		Label label=new Label(item.getGeneratePeriod());
		label.setStyle("border-left-width: 1px; border-right-width: 1px; border-top-width: 1px; border-bottom: 1px dashed #C0C0C0");
		label.setParent(cell);
		cell.setParent(parent);
		cell.setAttribute("reportGenItem", item);
		cell.addEventListener("onClick", new EventListener(){

			@Override
			public void onEvent(Event event) throws Exception {
				onClickSimplemonitorreportslistbox(event);
				
			}});
		
		cell=new Listcell();
		cell.setLabel(item.getReportItem().getPeriod());
		cell.setParent(parent);


		cell=new Listcell();
		cell.setLabel(item.getCreatTime());
		cell.setParent(parent);
		
		cell=new Listcell();
		cell.setLabel(item.getCreator());
		cell.setParent(parent);
		
		cell=new Listcell();
		cell.setParent(parent);
		if(item.isValid())
		{
				cell.setLabel("存在");
		}
		else
		{
			cell.setLabel("不存在");
			parent.setStyle("color:gray");
		}
		cell=new Listcell();
		img=new Image("/main/images/action/delete.gif");
		img.setParent(cell);
		cell.setParent(parent);
		img.setAttribute("reportGenItem",item);
		img.addEventListener("onClick", new EventListener(){

			@Override
			public void onEvent(Event event) throws Exception {
				try
				{
					int ret = Messagebox.show("你确认要删除选中的记录吗？", "提示", Messagebox.OK
							| Messagebox.CANCEL, Messagebox.QUESTION);
					
					if (ret == Messagebox.CANCEL)
						return;
				}catch(Exception e)
				{
					Toolkit.getToolkit().showError(e.getMessage());
				}
				
//				//防止因为定义的报告生成时间相同，而覆盖文件导致数据丢失
				String createTime = reportItem.getCreatTime();
				Date createDate = Toolkit.getToolkit().parseDate(createTime);
				String createTimeInMillis = createDate.getTime() + "";
				
				ReportGenItem reportGenItem=(ReportGenItem)event.getTarget().getAttribute("reportGenItem");
				IniFile reportGenIni=reportComposer.getReportGenIni(reportGenItem.getReportItem().getReportID());
				reportComposer.deleteReportFile(createTimeInMillis, reportGenItem.getGenID(), reportGenItem.getFileType());
				reportGenIni.deleteSection(reportGenItem.getGenID());
				reportGenIni.saveChange();
				reportComposer.refreshGenlistbox(reportGenItem.getReportItem());
				
			}});
		

	}
}
