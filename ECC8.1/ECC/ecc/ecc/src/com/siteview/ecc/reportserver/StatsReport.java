package com.siteview.ecc.reportserver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.ReportDate;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.ecc.progress.IEccProgressmeter;
import com.siteview.ecc.report.common.CreateReportImpl;
import com.siteview.ecc.report.common.ReportFileToolkit;
import com.siteview.ecc.simplereport.EccDataSource;
import com.siteview.ecc.simplereport.HistoryDataSource;
import com.siteview.ecc.simplereport.ImageDataSource;
import com.siteview.ecc.simplereport.MonitorDataSource;
import com.siteview.ecc.simplereport.StatisticDataSource;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.util.Toolkit;

public class StatsReport implements IEccProgressmeter
{
	ArrayList<String> errors;

	@Override
	public String getExcutingInfo()
	{
		StringBuffer sb=new StringBuffer();
		if(monitorDataSource!=null)
			monitorDataSource.getExcutingInfo(sb);
		if(historyDataSource!=null)
			historyDataSource.getExcutingInfo(sb);
		if(statisticDataSource!=null)
			statisticDataSource.getExcutingInfo(sb);
		if(imageDataSource!=null)
			imageDataSource.getExcutingInfo(sb);
		
		return sb.toString();
	}
	@Override
	public String getFinishUrl()
	{
		return downLoadUrl;
	}
	@Override
	public ArrayList<String> getErrors(){
		return errors;
	}
	@Override
	public void run(){
		try{
			doReport();
		}catch(Exception e)
		{
			errors.add(e.getMessage());
		}
	}
	@Override
	public void cancel() {
		if(historyDataSource!= null)
			historyDataSource.setCancel(true);
		if(monitorDataSource!= null)
			monitorDataSource.setCancel(true);
		if(statisticDataSource!= null)
			statisticDataSource.setCancel(true);
		if(imageDataSource!= null)
			imageDataSource.setCancel(true);
	}
	@Override
	public int getPercent() 
	{
		int progress=0;
		int size=0;
		if(monitorDataSource!=null)
		{
			progress+=monitorDataSource.isFinish()?monitorDataSource.getTaskSize():monitorDataSource.getTaskProgress();
			size+=monitorDataSource.getTaskSize();
		}
		if(statisticDataSource!=null)
		{
			progress+=statisticDataSource.isFinish()?statisticDataSource.getTaskSize():statisticDataSource.getTaskProgress();
			size+=statisticDataSource.getTaskSize();
		}
		if(imageDataSource!=null)
		{
			progress+=imageDataSource.isFinish()?imageDataSource.getTaskSize():imageDataSource.getTaskProgress();
			size+=imageDataSource.getTaskSize();
		}
		if(historyDataSource!=null)
		{
			progress+=historyDataSource.isFinish()?historyDataSource.getTaskSize():historyDataSource.getTaskProgress();
			size+=historyDataSource.getTaskSize();
		}
		if(size==0)
			return 0;
		return progress*100/size;
	}
	
	EccDataSource				historyDataSource					= null;
	MonitorDataSource			monitorDataSource					= null;
	StatisticDataSource			statisticDataSource					= null;
	ImageDataSource				imageDataSource						= null;
	

	// public ArrayList<Report> lstMonitorReport;
	public String getReportFileID() {
		return "" + this.now.getTimeInMillis();
	}
	String							error_message;
	
	// �г���ϸ����
	public boolean					IsStatusResultVisible	= false;
	// �Ƿ��г���ֵ
	public boolean					IsListClicketVisible	= false;
	// �Ƿ��г�����
	public boolean					IsListErrorVisible		= false;
	// �Ƿ��г�Σ��
	public boolean					IsListDangerVisible		= false;
	// �Ƿ��г�ͼƬ
	public boolean					IsGraphic				= false;
	
	public String 					dstrstatusnoneed		="null,ok,warning,error,disable,bad";
	public String 					return_value_filter		="";
	
	public Boolean 					showdstr 				= false;
	public CreateReportImpl				createReportImpl						= new CreateReportImpl();
	
	// ��������
	private String                  ComboGraphic="��״ͼ";  //��״ͼ ����ͼ
	private String[]				monitorIds;
	private String[] 				validMonitorIDArray;
	private Map<String,String>	monitorIdNameMap=new HashMap<String,String>();
	private View					view;
	private Date					tmStart;
	private Date					tmEnd;
	private Map<String,String>  reportDefineMap;
	private String reportID;
	private String downLoadUrl;
	
	String	DatePart			= "";

	public Map<String, String> getMonitorIdNameMap() {
		return monitorIdNameMap;
	}
	
	public String getReportID() {
		return reportID;
	}

	public String[] getValidMonitorIDArray() {
		return validMonitorIDArray;
	}

	private Calendar now = null;
	public StatsReport(String reportId,Map<String,String> reportDefineMap, Date tmStart,Date tmEnd,View view,Calendar now)
	{
		this.reportID=reportId;
		this.reportDefineMap=reportDefineMap;
		this.tmStart=tmStart;
		this.tmEnd=tmEnd;
		this.view=view;
		this.now = now;
	}
	
	public String DateToString(java.util.Date date)
	{
		String dateStr = "";
		try
		{
			// ���ȡ����ʱ�䣬���õ�ǰʱ��
			dateStr = Toolkit.getToolkit().formatDate(date == null ? new Date() : date);
		} catch (Exception e)
		{
			// e.printStackTrace();
			// logger.info(e);
		}
		return dateStr;
	}
	
	/*�����涨������*/
	private boolean readFromMap()
	{
		// ����ֹ ������ ִ����һ������
		String strGroupRight = this.reportDefineMap.get("GroupRight");
		if (strGroupRight == null || "".equals(strGroupRight))
			return false;

		this.monitorIds=strGroupRight.split(",");
		
		// �� ���ɴ˱��� ��
		//strReportName = DateToString(tmStart) + DateToString(tmEnd) + this.reportID;
		try
			{
				// �г���ϸ����
				this.IsStatusResultVisible = "Yes".equals(this.reportDefineMap.get("Parameter"));
				// �Ƿ��г���ֵ
				this.IsListClicketVisible = "Yes".equals(this.reportDefineMap.get("ListClicket"));
				// �Ƿ��г�����
				this.IsListErrorVisible = "Yes".equals(this.reportDefineMap.get("ListError"));
				// �Ƿ��г�Σ��
				this.IsListDangerVisible = "Yes".equals(this.reportDefineMap.get("ListDanger"));
				// �Ƿ��г���ֹ
				//tmpReport.IsDenyVisible = "Yes".equals(map.get("Deny"));
				// �Ƿ��г�ͼƬ
				this.IsGraphic = "Yes".equals(this.reportDefineMap.get("Graphic"));
				//ͼ������
				String GraphicType=this.reportDefineMap.get("ComboGraphic");
				if(GraphicType!=null)
				{
					GraphicType=GraphicType.equals("��״ͼ")?"��״ͼ":"��״ͼ";
					this.ComboGraphic=GraphicType;
				}
				
				// ���ɱ����ļ�
			
			} catch (Exception e1)
			{
				e1.printStackTrace();
				return false;
			}
		return true;
	
	}
	private boolean readData()
	{
		if(!readFromMap())
			return false;
		
		try
		{
			
			ArrayList<String> validMonitorID=new ArrayList<String>(); 
			for(String id:this.monitorIds)
			{
				INode node=view.getMonitorNode(id);
				if(node!=null){
					this.monitorIdNameMap.put(id,node.getName());
					validMonitorID.add(id);
				}
			}
			this.validMonitorIDArray=new String[validMonitorID.size()];
			validMonitorID.toArray(this.validMonitorIDArray);
			
			if(validMonitorIDArray.length==0)
				return false;
			
			// �Ƿ���ʾdstr
			if (IsListDangerVisible || IsListErrorVisible)
			{
				showdstr = true;
			}
			if (IsListDangerVisible && IsListErrorVisible)
			{
				dstrstatusnoneed = "null,ok,disable,bad";
			}
			if (IsListDangerVisible && !IsListErrorVisible)
			{
				dstrstatusnoneed = "null,ok,error,disable,bad";
			}
			if (!IsListDangerVisible && IsListErrorVisible)
			{
				dstrstatusnoneed = "null,ok,warning,disable,bad";
			}
			if(!IsStatusResultVisible)
			{
				this.return_value_filter= "sv_primary,sv_drawimage";
			}

		} catch (Exception ex)
		{
			ex.printStackTrace();
			error_message = ex.getMessage();
			return false;
		}
		
		return true;
	}
	

	public void doReport() throws IOException
	{
		String fileType=reportDefineMap.get("fileType");
		if(fileType==null)
			fileType="html";
		createReport(fileType);
	}
	private boolean createReport(String type) throws IOException
	{
		if(!readData())
			return false;
		
		try
		{
			/*LinkedHashMap<Date, DstrItem> dstrs;
			String mindate = "";
			String maxdate = "";
			String subtitle = "";
			for (String nd : reportDate.getNodeidsArray())
			{
				dstrs = reportDate.getDstr(nd);
				if (dstrs == null || "".equals(dstrs) || dstrs.size() == 0)
					continue;
				Iterator itm = dstrs.keySet().iterator();
				mindate = ((Date) itm.next()).toLocaleString();
				maxdate = reportDate.getPropertyValue(nd, "latestCreateTime");
				subtitle = "ʱ�Σ�" + mindate + "~" + maxdate;
			}*/


			
			monitorDataSource = new MonitorDataSource(validMonitorIDArray,new ReportDate(tmStart, tmEnd),this,monitorIdNameMap);
			statisticDataSource = new StatisticDataSource(validMonitorIDArray,new ReportDate(tmStart, tmEnd),this,monitorIdNameMap);
			if(this.showdstr)
				historyDataSource = new HistoryDataSource(validMonitorIDArray,new ReportDate(tmStart, tmEnd),this,monitorIdNameMap);
			
			if (IsGraphic)
			{
				imageDataSource = null;
				if ("html".equals(type))
				{
					imageDataSource = new ImageDataSource(validMonitorIDArray,new ReportDate(tmStart, tmEnd),this,false,monitorIdNameMap,ComboGraphic);
				} else
				{
					imageDataSource = new ImageDataSource(validMonitorIDArray,new ReportDate(tmStart, tmEnd),this,true,monitorIdNameMap,ComboGraphic);
				}
			}
			Map parameters = new HashMap();
			String title = this.reportDefineMap.get("Title");
			if(title!=null){
				int index = title.indexOf("|");
				if(index>0){
					title = title.substring(0,index); 
				}
			}
			parameters.put("ReportTitle", title);
			parameters.put("subReportTitle", "ʱ�Σ�" + Toolkit.getToolkit().formatDate(tmStart) + "~" + Toolkit.getToolkit().formatDate(tmEnd));
			String path = EccWebAppInit.getWebDir();
			parameters.put("SUBREPORT_DIR", path + "/main/report/");
			if ("html".equals(type))
			{
				parameters.put("IS_IGNORE_PAGINATION", Boolean.valueOf(false));
				parameters.put("SUBREPORT_DIRfilename", path + "/main/report/report_subreport2.jasper");
			} else
			{
				parameters.put("SUBREPORT_DIRfilename", path + "/main/report/report_subreport3.jasper");
			}
			if ("xls".equals(type))
			{
				parameters.put("IS_IGNORE_PAGINATION", Boolean.valueOf(false));
			}
			parameters.put("subDS1", monitorDataSource);
			parameters.put("subDS2", statisticDataSource);
			parameters.put("subDS3", imageDataSource);
			parameters.put("historyDS", historyDataSource);
			File f=new File("/report.tmp");
			if(!f.exists())
				f.mkdirs();
			JRFileVirtualizer  virtualizer = new JRFileVirtualizer(2, "/report.tmp");
			parameters.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);/*�ڴ治����Ӳ�̻�������*/
			
			
			// generate report pdf stream
			// is = Thread.currentThread().getContextClassLoader()
			// .getResourceAsStream("/main/report/report.jasper");
			// JasperFillManager.fillReport(sourceFileName, parameters)
			// final Map params = new HashMap();
			// params.put("ReportTitle", "The First Jasper Report Ever");
			// params.put("MaxOrderID", new Integer(10500));
			// Reportdatasource ds = new
			// Reportdatasource(buildBean("HistoryBean"));
			// final byte[] buf =
			// JasperRunManager.runReportToPdf(is, parameters, ds);
			// PrintWriter myFile1 = new PrintWriter(new OutputStreamWriter(new
			// FileOutputStream("e:\\test.pdf")));
			// myFile1.write(new String(buf));
			// myFile1.close(); //
			// ByteArrayOutputStream bout = new ByteArrayOutputStream();
			// bout.write(buf);
			// bout.flush();
			// bout.close();
			
			
			//��ֹ��Ϊ����ı�������ʱ����ͬ���������ļ��������ݶ�ʧ
			String createTime = reportDefineMap.get("creatTime");
			Date createDate = Toolkit.getToolkit().parseDate(createTime);
			String createTimeInMillis = createDate.getTime() + "";
			

			downLoadUrl=getDownLoadUrl(createTimeInMillis, getReportFileID(),type);
			String file=getCreateFile(createTimeInMillis, getReportFileID(),type);
			try{
				if ("html".equals(type))
				{
					JasperRunManager.runReportToHtmlFile(path + "/main/report/report.jasper",file, parameters, new BlankDataSource(validMonitorIDArray,new ReportDate(tmStart, tmEnd),this,monitorIdNameMap));
					/**modifyed by qimin.xioing*/
					File tmpFile = new File(file);
					while(f.canRead() == false){
						Thread.sleep(100);
					}
//					String fileName=Toolkit.getToolkit().formatDate(new Date(Long.parseLong(getReportFileID())),"yyyyMMdd_")+getReportFileID();
					String fileName=createTimeInMillis+"_"+getReportFileID();
					ReportFileToolkit.splitHtmlFile(tmpFile, fileName, getReportFileID());
				} else if ("pdf".equals(type))
				{
					JasperRunManager.runReportToPdfFile(path + "/main/report/report.jasper", file, parameters, new BlankDataSource(validMonitorIDArray,new ReportDate(tmStart, tmEnd),this,monitorIdNameMap));
				} else if ("xls".equals(type))
				{
					com.siteview.ecc.report.xls.JRXlsExporter exporter = new com.siteview.ecc.report.xls.JRXlsExporter();
					ByteArrayOutputStream oStream = null;
					OutputStream out=null;
					
					try{
						oStream = new ByteArrayOutputStream();
						JasperPrint jasperPrint = JasperFillManager.fillReport(path + "/main/report/report.jasper", parameters, new BlankDataSource(validMonitorIDArray,new ReportDate(tmStart, tmEnd),this,monitorIdNameMap));
						exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
						exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, oStream);
						exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
						exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
						exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
						exporter.exportReport();
						byte[] bytes = oStream.toByteArray();
						out= new FileOutputStream(file);
						out.write(bytes);
					}finally
					{
						try{out.close();}catch(Exception e){}
						try{oStream.close();}catch(Exception e){}
					}
				} 
			}catch(Exception e){
				e.printStackTrace();
			}
			
			writeGenIni(this.reportID,this.getReportFileID(),type);
			
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		} 
		return true;
	}
	// д��Ϣ�� reportgenerate.ini
	//strGenSection��¼�˴���ʱ����Ϣ,reportID��¼�˱�����Ϣ
	private void writeGenIni(String reportID,String reportFileID,String fileType)
	{
		IniFile iniGen = new IniFile("report."+reportID+".ini");
		try
		{iniGen.load();
		} catch (Exception e1){}
		try{
		iniGen.createSection(reportFileID);
		iniGen.setKeyValue(reportFileID, "creator",view.getLoginName());
		iniGen.setKeyValue(reportFileID, "fileType",fileType);
		iniGen.setKeyValue(reportFileID, "beginTime",tmStart.toLocaleString());
		iniGen.setKeyValue(reportFileID, "endTime",tmEnd.toLocaleString());
		iniGen.saveChange();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static String getCreateFile(String createTimeInMillis, String reportFileID,String type)
	{
//		String fileName=Toolkit.getToolkit().formatDate(new Date(Long.parseLong(reportFileID)),"yyyyMMdd_")+reportFileID;
		String fileName=createTimeInMillis + "_" + reportFileID;
		if ("html".equals(type))
			return Constand.statreportsavepath + fileName + ".html";
		else if ("pdf".equals(type))
			return Constand.statreportsavepath + fileName + ".pdf";
		else if ("xls".equals(type))
			return Constand.statreportsavepath + fileName + ".xls";
		else if ("excel".equals(type))
			return Constand.statreportsavepath + fileName + ".xls";
		else
			return "";
	}
	
	public static String getHtmlFolderName(String createTimeInMillis,String reportFileID){
//		String fileName=Toolkit.getToolkit().formatDate(new Date(Long.parseLong(reportFileID)),"yyyyMMdd_")+reportFileID;
		String fileName=createTimeInMillis + "_" + reportFileID;
		return Constand.statreportsavepath + fileName+File.separator;
	}
	public static String getDownLoadUrl(String createTimeInMillis,String reportFileID,String type)
	{
//		String fileName=Toolkit.getToolkit().formatDate(new Date(Long.parseLong(reportFileID)),"yyyyMMdd_")+reportFileID;
		String fileName=createTimeInMillis + "_" + reportFileID;
		
		if(type==null)
			type="html";
		
		type=type.toLowerCase();
		
		if ("html".equals(type))
		{
			return "/main/report/showStatisticReport.jsp?reportGenID="+reportFileID+"&fileType="+type+"&currentPage=1&createTimeInMillis="+createTimeInMillis;
			//return Constand.statreportwebpath+reportFileID + ".html";
		}
		else if ("pdf".equals(type))
			return Constand.statreportwebpath+fileName + ".pdf";
		else if ("xls".equals(type))
			return Constand.statreportwebpath+fileName + ".xls";
		else if ("excel".equals(type))
			return Constand.statreportwebpath+fileName + ".xls";
		else
			return "";
	}
}
