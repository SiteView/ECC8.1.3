package com.siteview.ecc.report;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkex.zul.North;
import org.zkoss.zkex.zul.West;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.siteview.base.data.Report;
import com.siteview.base.data.ReportManager;
import com.siteview.base.data.UserRightId;
import com.siteview.base.tree.INode;
import com.siteview.ecc.alert.SelectTree;
import com.siteview.ecc.report.beans.TendencyCheckDataBean;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.report.common.ErrorMessage;
import com.siteview.ecc.report.common.GroupLinkListener;
import com.siteview.ecc.report.models.TendencyDataReportModel;
import com.siteview.ecc.report.models.TendencyModel;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.util.LinkCheck;

public class TendencyReport extends GenericForwardComposer {
	private final static Logger logger = Logger.getLogger(TendencyReport.class);

	private Report report;
	private INode n;
	private Combobox viewNamecombobox;
	private Tree tree;
	private Datebox start;
	private Timebox begin_Time;
	private Datebox end;
	private Timebox end_Time;
	private Image chart;
	private Listbox runtimeReport;
	private Listbox checkDataReport;
	private Div reportDate;
	private Radiogroup dataToShow;
	private North north;
	private Panel tt;
	private TendencyDataReportModel model;
	private West   					treeview;
	private Label 	 groupLink;
	
	public void onCreate$groupLink(Event event){
		boolean isLink = LinkCheck.getLinkCheck().CanSeeLink(UserRightId.WholeView);
		if(isLink){
			String style = "color:#18599C;cursor:pointer;text-decoration: underline;";
			groupLink.setStyle(style);
		}
	}
	public void onClick$groupLink(Event e){
		Comboitem item = viewNamecombobox.getSelectedItem();
		if(item!=null){
			groupLink.addEventListener(Events.ON_CLICK,new GroupLinkListener(item.getLabel()));
		}
	}

	public void onCreate$trendreport(Event event) {
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(new Date());
		calStart.add(Calendar.HOUR_OF_DAY, -24);
		start.setValue(calStart.getTime());
		begin_Time.setValue(calStart.getTime());
		end.setValue(new Date());
		end_Time.setValue(new Date());
	}

	public INode getSelectedNode(){
		Treeitem item = tree.getSelectedItem();                                      
		if(item==null) return null;
		EccTreeItem itemNode = (EccTreeItem) item.getValue();
		INode node = itemNode.getValue();
		if(node.getType().equals(INode.MONITOR)) return node;
		else return null;
	}

	public void onSelect$tree(Event event) throws Exception {
		try{
			build();
		}catch(Exception e){
			//点击非监测器时将内部抛出的异常屏蔽
		}	
	}

	public void onChange$viewNamecombobox(Event event) {
		SelectTree treeView = (SelectTree) tree;
		String viewName = viewNamecombobox.getValue();
		if (viewName != null) treeView.setViewName(viewName);
	}


	public void onClick$seachBtn(Event event) throws Exception {
		build();
	}

	public void onClick$exportReport(Event event) throws InterruptedException {
		if(getTime(start, begin_Time).after(getTime(end, end_Time))){
			Messagebox.show(ErrorMessage.TIME_ERROR, "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		if (getSelectedNode() == null) {
			Messagebox.show(ErrorMessage.UNSELECT_MONITOR, "提示", Messagebox.OK,
					Messagebox.INFORMATION);
			return;
		}
		final Window win = (Window) Executions.createComponents(
				"/main/report/tendencyreport/exporttendencyreport.zul", null,
				null);
		win.setAttribute("report", report);
		win.setAttribute("begin_Time", begin_Time.getValue());
		win.setAttribute("end_Time", end_Time.getValue());
		win.setSizable(false);
		win.setClosable(true);
		win.doModal();
	}


	public void onSelect$checkDataReport(Event event) throws Exception, IOException {
		Listitem item = checkDataReport.getSelectedItem();
		if (item == null)
			return;
		String index = item.getValue() == null ? "0" : item.getValue()
				.toString().trim();
			chart.setSrc(buildChart(Integer.parseInt(index)));
	}
	
	public void onCheck$dataToShow(Event event) throws Exception {
		if (report == null)
			return;
		String select = dataToShow.getSelectedItem().getValue();
		reportDate.setVisible(true);
		if (getSelectedNode() == null) {
			Messagebox.show(ErrorMessage.UNSELECT_MONITOR, "提示", Messagebox.OK,
					Messagebox.INFORMATION);
			return;
		}
		TendencyDataReportModel tdrm = new TendencyDataReportModel(this.getSelectedNode(),report,
				select);
		Component c = reportDate.getFirstChild();
		if(c!=null)
			reportDate.removeChild(c);
		reportDate.appendChild(tdrm.generateListbox());
	}

	public void onClick$h2(Event event) throws Exception {
		setStartDate(2);
		setEndDate();
		build();

	}

	public void onClick$h4(Event event) throws Exception {
		setStartDate(4);
		setEndDate();
		build();
	}

	public void onClick$h8(Event event) throws Exception {
		setStartDate(8);
		setEndDate();
		build();
	}

	public void onClick$d1(Event event) throws Exception {
		setStartDate(24);
		setEndDate();
		build();
	}

	public void onClick$d2(Event event) throws Exception {
		setStartDate(2 * 24);
		setEndDate();
		build();
	}

	public void onClick$d3(Event event) throws Exception {
		setStartDate(3 * 24);
		setEndDate();
		build();
	}

	public void onClick$d5(Event event) throws Exception {
		setStartDate(5 * 24);
		setEndDate();
		build();
	}

	public void onClick$week(Event event) throws Exception {
		// 重新计算开始时间
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		start.setValue(c.getTime());
		begin_Time.setValue(c.getTime());
		setEndDate();
		build();
	}

	// ?????????????????????????
	public void onClick$week1(Event event) throws Exception {
		setStartDate(1 * 7 * 24);
		setEndDate();
		build();
	}

	public void onClick$month1(Event event) throws Exception {
		setStartByMonth(1);
		setEndDate();
		build();
	}

	public void onClick$month2(Event event) throws Exception {
		setStartByMonth(2);
		setEndDate();
		build();
	}

	public void onClick$month3(Event event) throws Exception {
		setStartByMonth(6);
		setEndDate();
		build();
	}

	// 开始时间为当天00:00:00
	public void onClick$nowDay(Event event) throws Exception {
		// 重新计算开始时间
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		start.setValue(c.getTime());
		begin_Time.setValue(c.getTime());
		setEndDate();
		build();
	}

	public void onClick$createExcelReport(Event event)
			throws Exception,
			IOException {
		if (getSelectedNode() == null) {
			Messagebox.show(ErrorMessage.UNSELECT_MONITOR, "提示", Messagebox.OK,
					Messagebox.INFORMATION);
			return;
		}
		TendencyDataReportModel tdrm = new TendencyDataReportModel(getSelectedNode(),report,
				dataToShow.getSelectedItem().getValue());
		Filedownload.save(tdrm.writeDataToXls(), "application/vnd.ms-excel",
				"download.xls");
	}
	
	public int getTreeSize(){
		return Integer.parseInt(treeview.getSize().split("px")[0]);
	}
	private int getScreenWidth() {
		int screenWidth=-1;
		try {
			screenWidth = Integer.parseInt(desktop.getPage("eccmain")
				.getFellow("tree").getAttribute("screenWidth")
				.toString());
			West west=(West)this.desktop.getPage("eccmain").getFellow("westTree");
			int treeSize=Integer.parseInt(west.getWidth().split("px")[0]);
			screenWidth = screenWidth - treeSize;
		} catch (Exception e) {
			screenWidth = 1024;
		}
		return screenWidth;
	}
	
	private void clear() throws InterruptedException {
		ChartUtil.clearListbox(runtimeReport);
		ChartUtil.clearListbox(checkDataReport);
		chart.setSrc("");
		ChartUtil.clearComponent(reportDate);
	}
	private void build() throws Exception {
		
		logger.info("screen width is :"+getScreenWidth()+"  and west size is : "+getTreeSize());
		if(getTime(start, begin_Time).compareTo(getTime(end, end_Time))>=0){
			Messagebox.show(ErrorMessage.TIME_ERROR, "提示", Messagebox.OK,Messagebox.INFORMATION);
			clear();
			return;
		}
		if (getSelectedNode() == null) {
			Messagebox.show(ErrorMessage.UNSELECT_MONITOR, "提示", Messagebox.OK,
					Messagebox.INFORMATION);
			clear();
			return;
		}
		
		if (getTime(start, begin_Time).after(new Date())) {
			clear();
			return;
		}
		
		report = ReportManager.getReport(getSelectedNode(), getTime(start, begin_Time), getTime(end, end_Time));
		TendencyModel model = new TendencyModel(report, "");
		ChartUtil.makelistData(runtimeReport, model, model);
		TendencyModel model2 = new TendencyModel(report,
				"TendencyCheckDataBean");
		ChartUtil.makelistData(checkDataReport, model2, model2);
		
		if(model2.getSize()>0)
		{
			TendencyCheckDataBean tcdb = (TendencyCheckDataBean)model2.get(0);
			chart.setSrc(buildChart(tcdb.getId()));
		}
		reportDate.setVisible(true);
		TendencyDataReportModel tdrm = new TendencyDataReportModel(this.getSelectedNode(),report,
				"all");
		Component c = reportDate.getFirstChild();
		if(c!=null)
			reportDate.removeChild(c);
		reportDate.appendChild(tdrm.generateListbox());
	}

	public String buildChart(int index) throws Exception {
		String svdrawimage = report.getReturnValue("sv_drawimage", index);
		String title = report.getReturnValue("MonitorName", index);
		String min = report.getReturnValue("min", index);
		String max = report.getReturnValue("max", index);
		String average = report.getReturnValue("average", index);
		String subtitle = "最小值：" + min + "最大值：" + max + "平均值：" + average;
		String name = report.getReturnValue("ReturnName", index) == null ? ""
				: report.getReturnValue("ReturnName", index);
		XYDataset data = buildDataset(report,index, name);
		double tm = 0;
		if (max == null || max.equals(""))
			tm = 0;
		else
			tm = Double.parseDouble(max);
		double tmi = 0;
		if (min == null || min.equals(""))
			tmi = 0;
		else
			tmi = Double.parseDouble(min);
		Image img = ChartUtil.createBufferedImage(title, subtitle,
				title, data, 1, tm, getTime(start, begin_Time), getTime(end, end_Time), tmi, true, getScreenWidth()-getTreeSize(), 300,
				ChartUtil.REPORTTYPE_DAYREPORT);
		return img.getSrc();
	}

	public static XYDataset buildDataset(Report report, int index, String name) {
		Map<Date, String> imgdata = report
				.getReturnValueDetail(index);

		TimeSeries timeseries = new TimeSeries(name,
				org.jfree.data.time.Second.class);
		Date datestart=null;
		int i=0;
		for (Date date1 : imgdata.keySet()) {
			int ss = date1.getSeconds();
			int mm = date1.getMinutes();
			int hh = date1.getHours();
			int d = date1.getDate();
			int m = date1.getMonth() + 1;
			int y = date1.getYear() + 1900;
             if(i==0)
             {
            	 datestart=date1;
            	 i=1;
             }
			org.jfree.data.time.Second ttime = new Second(ss, mm, hh, d, m, y);
			String value = imgdata.get(date1);
			// equals("(status)bad")||value.trim().equals("(status)disable")
			// (status)null
			if (value.trim().startsWith("(status)")) {
				timeseries.add(ttime, null);
			} else {
				if (value.isEmpty()) {
					timeseries.add(ttime, null);
				} else {
					timeseries.add(ttime, Double.parseDouble(value));
				}
			}
		}
		int av=6*60*60;
		if (datestart!=null)
		{
			long day=GetDateMargin(datestart,new Date() );
			if (day>10)
			{
				av=24*60*60;
			}
			if(day>30)
			{
				av=6*24*60*60;
			}
			if(day>60)
			{
				av=24*24*60*60;
			}
			if(day>120)
			{
				av=10*24*24*60*60;
			}
			if(day>180)
			{
				av=60*24*24*60*60;
			}
		}
		TimeSeries average1 = MovingAverage.createMovingAverage(timeseries,"趋势线",av, 5);      // 绘制1小时移动平均线
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		timeseriescollection.addSeries(average1);
		timeseriescollection.addSeries(timeseries);
		return timeseriescollection;
	}
	public static long GetDateMargin(Date beginDate,Date endDate){
	    long margin = 0;
	    margin = endDate.getTime() - beginDate.getTime();
	    margin = margin/(1000*60*60*24);
	    return margin;
	}

	public Date getTime(Datebox arg1, Timebox arg2)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(arg1.getValue());
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		c.setTime(arg2.getValue());
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int second = c.get(Calendar.SECOND);
		c.set(year, month, day, hour, minute, second);
		return c.getTime();
	}

	private void setStartDate(int hours) {
		Calendar c = Calendar.getInstance();
		c.setTime(getTime(end, end_Time));
		c.add(Calendar.HOUR_OF_DAY, -hours);
		start.setValue(c.getTime());
		begin_Time.setValue(c.getTime());
	}

	private void setStartByMonth(int month) {
		Calendar c = Calendar.getInstance();
		c.setTime(getTime(end, end_Time));
		c.add(Calendar.MONTH, -month);
		start.setValue(c.getTime());
		begin_Time.setValue(c.getTime());
	}
	
	private void setEndDate(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		end.setValue(c.getTime());
		end_Time.setValue(c.getTime());
	}
}
