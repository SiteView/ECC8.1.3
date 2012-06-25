package com.siteview.ecc.report.errorcomparereport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.siteview.base.cache.DaoFactory;
import com.siteview.base.cache.ReportDataDaoImpl;
import com.siteview.base.data.ReportDate;
import com.siteview.base.data.ReportDateError;
import com.siteview.base.manage.View;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.report.beans.ErrorLogsBean;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.report.common.ReportServices;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.util.Toolkit;

public class CompareExportWindow extends Window {
	private final static Logger logger = Logger.getLogger(CompareExportWindow.class);
	private static final long serialVersionUID = 5917163640208351810L;
	private List<ReportDate> 			reports;
	private List<INode>						nodes;
	private Date 						beginDate;
	private Date 						endDate;
	public final static SimpleDateFormat SDF = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	public Button getOkBtn(){
		return (Button)BaseTools.getComponentById(this, "saveReport");
	}
	
	public Listbox getFormatListbox(){
		return (Listbox)BaseTools.getComponentById(this, "format");
	}
	public void onCreate(){
		reports = (List<ReportDate>)this.getAttribute("report");
		nodes = (List<INode>)this.getAttribute("nodes");
		getOkBtn().addEventListener(Events.ON_CLICK, new onOkBtnClickedListener(this));
	}
	
	class onOkBtnClickedListener implements EventListener{
		Window window;
		View v = ChartUtil.getView();
		public onOkBtnClickedListener(Window window){
			this.window = window;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void onEvent(Event arg0) throws Exception {
			try{

				String fileType = getFormatListbox().getSelectedItem().getValue().toString();
				String subDir = EccWebAppInit.getWebDir()+"main/report/errorexportreport/";
				List l1 = ReportServices.getRuntimeData(reports);
//				List l2 = ReportServices.buildImageMaps(reports);
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
						sb.append(Executions.getCurrent().getDesktop().getSession().getAttribute("stringbufferParent")+"��:").append(tmplate.get_sv_name()).append("���͵ļ�����Աȱ���");
					}
				}else if(reports.size()>1){
					INode node = v.getNode(reports.get(0).getNodeidsArray()[0]);
					INode node2 = v.getNode(reports.get(1).getNodeidsArray()[0]);
					sb.append(Executions.getCurrent().getDesktop().getSession().getAttribute("stringbufferParent")+"��:").append(node.getName()).append("��").append(node2.getName()).append("�ȼ�����Ĵ���Աȱ���");
				}
				Map parameter = new HashMap();
				parameter.put("title", sb.toString());
				parameter.put("subtitle", Toolkit.getToolkit().formatDate(beginDate)+"~"+Toolkit.getToolkit().formatDate(endDate));
				parameter.put("SUBREPORT_DIR", subDir);
				parameter.put("ds1", new ComparereportDatasource(l1));
				List l = new ArrayList();
				l.addAll(l1);
				if(fileType.equals("html")){
					boolean flag = ChartUtil.saveAsHtml(subDir+"report.jasper",subDir,sb.toString(), parameter, new ComparereportDatasource(l));
					logger.info(flag);
//					InsertInto(fileType,sb);
				}else if(fileType.equals("pdf")){
					AMedia media = ChartUtil.saveAsPdf(subDir+"report.jasper",sb.toString(), parameter, new ComparereportDatasource(l));
					Filedownload.save(media);	
//					InsertInto(fileType,sb);
				}else{
					AMedia media = ChartUtil.saveAsXls(subDir+"report.jasper", sb.toString(),parameter, new ComparereportDatasource(l));
					Filedownload.save(media);	
//					InsertInto(fileType,sb);
				}
				window.detach();
			}catch(Exception e){
				e.printStackTrace();
			}

		}

		private void InsertInto(String fileType,Object sb) throws Exception{
			ReportDataDaoImpl dao=new ReportDataDaoImpl();
			ErrorLogsBean data=new ErrorLogsBean ();
			data.setType(fileType);
			data.setTarget("��ʶ");
			data.setTime((SDF.format(new Date())).toString());
			data.setName(sb.toString());
			data.setTitle(sb.toString());
			data.setData(Toolkit.getToolkit().formatDate(beginDate)+"~"+Toolkit.getToolkit().formatDate(endDate));
			data.setResult("����");
			data.setUsername("��ʱ��ȱ");
			dao.inserts(data);
		}
	}
}
