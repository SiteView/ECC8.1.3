package com.siteview.ecc.report;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.siteview.base.data.ReportDate;
import com.siteview.base.tree.INode;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zhtml.Fileupload;
import org.zkoss.zk.ui.Executions;
import com.siteview.base.manage.View;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.report.beans.ErrorLogsBean;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.report.common.ReportServices;
import com.siteview.ecc.report.errorcomparereport.ComparereportDatasource;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.util.Toolkit;
import com.siteview.base.cache.ReportDataDaoImpl;

public class SelectErrorExl {
	
	private List<ReportDate> 			reports;
	private List<INode>						nodes;
	private Date 						beginDate;
	private Date 						endDate;

	
	public List<ReportDate> getReports() {
		return reports;
	}
	public void setReports(List<ReportDate> reports) {
		this.reports = reports;
	}
	public List<INode> getNodes() {
		return nodes;
	}
	public void setNodes(List<INode> nodes) {
		this.nodes = nodes;
	}
	public Date getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public final static SimpleDateFormat SDF = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	
	public SelectErrorExl(List<ReportDate> reports,List<INode> nodes,Date beginDate,Date endDate) throws Exception{
		this.beginDate=beginDate;
		this.endDate=endDate;
		this.nodes=nodes;
		this.reports=reports;
		
		InsertError();
	}
	private void InsertError() throws Exception{
		System.out.println("分析错误报表导出数据.reports............................."+reports);
		View v = ChartUtil.getView();
		String fileType = "xls";
		String subDir = EccWebAppInit.getWebDir()+"main/report/errorexportreport/";
		List l1 = ReportServices.getRuntimeData(reports);
//		List l2 = ReportServices.buildImageMaps(reports);
		for(ReportDate r:reports){
			beginDate = r.getM_begin_date();
			endDate = r.getM_end_date();
			break;
		}
		StringBuffer sb = new StringBuffer();
		if(reports.size()==1){
			INode node = v.getNode(reports.get(0).getNodeidsArray()[0]);
			if(node!=null) {
				MonitorInfo info = v.getMonitorInfo(node);
				MonitorTemplate tmplate = null;
				if(info!=null) tmplate = info.getMonitorTemplate();
				if(tmplate!=null)
				sb.append(Executions.getCurrent().getDesktop().getSession().getAttribute("stringbufferParent")+"组:").append(tmplate.get_sv_name()).append("类型的监测器对比报告");
			}
		}else if(reports.size()>1){
			INode node = v.getNode(reports.get(0).getNodeidsArray()[0]);
			INode node2 = v.getNode(reports.get(1).getNodeidsArray()[0]);
			sb.append(Executions.getCurrent().getDesktop().getSession().getAttribute("stringbufferParent")+"组:").append(node.getName()).append("与").append(node2.getName()).append("等监测器的错误对比报告");
		}
		String title=Executions.getCurrent().getDesktop().getSession().getAttribute("stringbufferParent")+"组~"+Toolkit.getToolkit().ErrorformatDate(beginDate)+"~"+Toolkit.getToolkit().ErrorformatDate(endDate)+"~"+Toolkit.getToolkit().ErrorformatDate(new Date());
		Map parameter = new HashMap();
		parameter.put("title", sb.toString());
		parameter.put("subtitle", Toolkit.getToolkit().formatDate(beginDate)+"~"+Toolkit.getToolkit().formatDate(endDate));
		parameter.put("SUBREPORT_DIR", subDir);
		parameter.put("ds1", new ComparereportDatasource(l1));
		List l = new ArrayList();
		l.addAll(l1);
		if(fileType.equals("html")){
			boolean flag = ChartUtil.saveAsHtml(subDir+"report.jasper",subDir,sb.toString(), parameter, new ComparereportDatasource(l));
//			logger.info(flag);
			InsertInto(fileType,title,"不存在");
		}else if(fileType.equals("pdf")){
			AMedia media = ChartUtil.saveAsPdf(subDir+"report.jasper",sb.toString(), parameter, new ComparereportDatasource(l));
			Filedownload.save(media);	
			InsertInto(fileType,title,"不存在");
		}else{
			AMedia media = ChartUtil.saveAsXls(subDir+"report.jasper", title,parameter, new ComparereportDatasource(l));
			//下载到本地 Filedownload.save(media);
			
//			下载到服务器
			if(media!=null){
				System.out.println("分析错误报表导出数据下载到服务器..............................");
				String fileName =  media.getName();
//				Reader r = media.getReaderData();
				String contextPath=Executions.getCurrent().getDesktop().getWebApp().getRealPath("/"); 
				File f = new File(contextPath+"ErrorReport/"+fileName); 
				InputStream ins = new ByteArrayInputStream(media.getByteData());
				OutputStream out = new FileOutputStream(f);
                byte[] buf=new byte[1024];
                 int len;
                while((len=ins.read(buf))>0){
                out.write(buf,0,len);
                }
                out.close();
                ins.close();
                InsertInto(fileType,title,"ErrorReport/"+fileName);
                System.out.println("分析错误报表导出数据下载到服务器............OVER..................");
			}
		}
	}
		
	private void InsertInto(String fileType,String title,String path) throws Exception{
			ReportDataDaoImpl dao=new ReportDataDaoImpl();
			ErrorLogsBean data=new ErrorLogsBean ();
			data.setType(fileType);
			data.setTarget(path);
			data.setTime((SDF.format(new Date())).toString());
			data.setName(title);
			data.setTitle(title);
			data.setData(Toolkit.getToolkit().formatDate(beginDate)+"~"+Toolkit.getToolkit().formatDate(endDate));
			data.setResult("存在");
			data.setUsername("暂时空缺");
			dao.inserts(data);
		}
}
