package com.siteview.ecc.report.reportforms;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.apache.log4j.Logger;
import org.jfree.data.xy.XYDataset;
import org.zkoss.util.media.AMedia;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import com.siteview.base.data.Report;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.report.Constand;
import com.siteview.ecc.report.PeriodReport;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.report.common.ReportServices;
import com.siteview.ecc.simplereport.SimpleReport;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.util.Toolkit;

public class PeriodExportWindow extends Window {
	private final static Logger logger = Logger.getLogger(PeriodExportWindow.class);

	private static final long 								serialVersionUID = -6558336737595403270L;
	private Report 											report1;
	private Report											report2;
	private Date											begin_date;
	private Date											end_date;
	private String											compareType;
	
	public Button getOkBtn(){
		return (Button)BaseTools.getComponentById(this, "saveReport");
	}
	
	public Listbox getFormatListbox(){
		return (Listbox)BaseTools.getComponentById(this, "format");
	}
	public void onCreate(){
		report1 = (Report)this.getAttribute("report1");
		report2 = (Report)this.getAttribute("report2");
		begin_date = (Date)this.getAttribute("begin_date");
		end_date = (Date)this.getAttribute("end_date");
		compareType = (String)this.getAttribute("compareType");
		getOkBtn().addEventListener(Events.ON_CLICK, new onOkBtnClickedListener(this));
	}
	
	class onOkBtnClickedListener implements EventListener{
		Window window;
		public onOkBtnClickedListener(Window window){
			this.window = window;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void onEvent(Event arg0) throws Exception {
			String sub1 = "",sub2="";
			if(getComparetype().equals(Constand.reporttype_dayreport)){
				sub1="("+Toolkit.getToolkit().formatDate(PeriodReport.getDayBegintime(getStarttime()))+"~"+Toolkit.getToolkit().formatDate(PeriodReport.getDayEndtime(getStarttime()))+")";
				sub2="("+Toolkit.getToolkit().formatDate(PeriodReport.getDayBegintime(getEndtime()))+"~"+Toolkit.getToolkit().formatDate(PeriodReport.getDayEndtime(getEndtime()))+")";
			}else if(getComparetype().equals(Constand.reporttype_weekreport)){
				sub1="("+Toolkit.getToolkit().formatDate(PeriodReport.getWeekBegintime(getStarttime()))+"~"+Toolkit.getToolkit().formatDate(PeriodReport.getWeekEndtime(getStarttime()))+")";
				sub2="("+Toolkit.getToolkit().formatDate(PeriodReport.getWeekBegintime(getEndtime()))+"~"+Toolkit.getToolkit().formatDate(PeriodReport.getWeekEndtime(getEndtime()))+")";
			}else{
				sub1="("+Toolkit.getToolkit().formatDate(PeriodReport.getMonthBegintime(getStarttime()))+"~"+Toolkit.getToolkit().formatDate(PeriodReport.getMonthEndtime(getStarttime()))+")";
				sub2="("+Toolkit.getToolkit().formatDate(PeriodReport.getMonthBegintime(getEndtime()))+"~"+Toolkit.getToolkit().formatDate(PeriodReport.getMonthEndtime(getEndtime()))+")";
			}
			String fileType = getFormatListbox().getSelectedItem().getValue().toString();
			String subDir = EccWebAppInit.getWebDir()+"main/report/exportreport/";
			List l1 = ReportServices.getRuntimeData(report1,report2);
			List l2 = buildstreamimage();
			Map parameter = new HashMap();
			parameter.put("title", "时段对比报告:"+report1.getPropertyValue("MonitorName"));
			parameter.put("subtitle", "时段:"+sub1+"与"+"\n时段:"+sub2);
			parameter.put("SUBREPORT_DIR", subDir);
			parameter.put("ds1", new PerodreportDatasource(l1));
			parameter.put("ds2", new PerodreportDatasource(l2));
			List l = new ArrayList();
			l.addAll(l1);
			l.addAll(l2);
			if(fileType.equals("html")){
				boolean flag = ChartUtil.saveAsHtml(subDir+"report.jasper",subDir,report1.getPropertyValue("MonitorName")+"_时段对比报告", parameter, new PerodreportDatasource(l));
				logger.info(flag);
			}else if(fileType.equals("pdf")){
				AMedia media = ChartUtil.saveAsPdf(subDir+"report.jasper",report1.getPropertyValue("MonitorName")+"_时段对比报告", parameter, new PerodreportDatasource(l));
				Filedownload.save(media);			
			}else{
				AMedia media = ChartUtil.saveAsXls(subDir+"report.jasper",report1.getPropertyValue("MonitorName")+"_时段对比报告", parameter, new PerodreportDatasource(l));
				Filedownload.save(media);				
			}
			window.detach();
		}
	}
	private List<InputStream> buildstreamimage() throws ParseException, IOException{
		String sub1 = "",sub2="";
		Date date1,date2;
		SimpleDateFormat format;
		if(getComparetype().equals(Constand.reporttype_dayreport)){
			date1=PeriodReport.getDayBegintime(getStarttime());
			date2=PeriodReport.getDayEndtime(getStarttime());
			sub1="("+Toolkit.getToolkit().formatDate(PeriodReport.getDayBegintime(getStarttime()))+"~"+Toolkit.getToolkit().formatDate(PeriodReport.getDayEndtime(getStarttime()))+")";
			sub2="("+Toolkit.getToolkit().formatDate(PeriodReport.getDayBegintime(getEndtime()))+"~"+Toolkit.getToolkit().formatDate(PeriodReport.getDayEndtime(getEndtime()))+")";
		}else if(getComparetype().equals(Constand.reporttype_weekreport)){
			date1=PeriodReport.getWeekBegintime(getStarttime());
			date2=PeriodReport.getWeekEndtime(getStarttime());
			sub1="("+Toolkit.getToolkit().formatDate(PeriodReport.getWeekBegintime(getStarttime()))+"~"+Toolkit.getToolkit().formatDate(PeriodReport.getWeekEndtime(getStarttime()))+")";
			sub2="("+Toolkit.getToolkit().formatDate(PeriodReport.getWeekBegintime(getEndtime()))+"~"+Toolkit.getToolkit().formatDate(PeriodReport.getWeekEndtime(getEndtime()))+")";
		}else{
			date1=PeriodReport.getMonthBegintime(getStarttime());
			date2=PeriodReport.getMonthEndtime(getStarttime());
			sub1="("+Toolkit.getToolkit().formatDate(PeriodReport.getMonthBegintime(getStarttime()))+"~"+Toolkit.getToolkit().formatDate(PeriodReport.getMonthEndtime(getStarttime()))+")";
			sub2="("+Toolkit.getToolkit().formatDate(PeriodReport.getMonthBegintime(getEndtime()))+"~"+Toolkit.getToolkit().formatDate(PeriodReport.getMonthEndtime(getEndtime()))+")";
		}
		Map<Integer, Map<String, String>> listimage = PeriodReport.getImagelist(report1, report2);
		List<InputStream> list = new ArrayList<InputStream>();
			for (int key : listimage.keySet())
			{
				Map<Date, String> imgdata1 = report1.getReturnValueDetail(key);
				Map<Date, String> imgdata2 = report2.getReturnValueDetail(key);
				Map<String, String> keyvalue = listimage.get(key);
				XYDataset data = PeriodReport.buildDataset(imgdata2,keyvalue.get("title")+sub2,imgdata1,keyvalue.get("title")+sub1);
				BufferedImage temmap = null;
				if (keyvalue.get("title").contains("%"))
				{
					temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data, 10,
							100, this.getEndtime(), 0, true, 650, 200);
				} else {
					double maxvalue = Double.parseDouble(keyvalue.get("maxvalue"));
					double minvalue = Double.parseDouble(keyvalue.get("minvalue"));
					maxvalue = maxvalue * 1.1;
					if (maxvalue < 1) {
						maxvalue = 1;
					}
					if (keyvalue.get("minvalue").contains("-")) {
						temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data,
								20, maxvalue, this.getEndtime(), minvalue, true, 650, 200);
					} else {
						temmap = SimpleReport.buildBufferImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data,
								20, maxvalue, this.getEndtime(), 0, true, 650, 200);
					}
				}
				ByteArrayOutputStream bs = new ByteArrayOutputStream();
				ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
				ImageIO.write(temmap, "GIF", imOut);
				// scaledImage1为BufferedImage，jpg为图像的类型
				InputStream istream = new ByteArrayInputStream(bs.toByteArray());
				list.add(istream);
			}
		return list;
	}
	public String getComparetype() {
		return compareType;
	}

	public void setComparetype(String compareType) {
		this.compareType = compareType;
	}
	
	public Date getStarttime() {
		return begin_date;
	}

	public void setStarttime(Date begin_date) {
		this.begin_date = begin_date;
	}

	public Date getEndtime() {
		return end_date;
	}

	public void setEndtime(Date end_date) {
		this.end_date = end_date;
	}
}
