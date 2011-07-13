package com.siteview.ecc.report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkex.zul.West;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;
import org.zkoss.zul.api.Datebox;

import com.siteview.base.data.Report;
import com.siteview.base.data.ReportManager;
import com.siteview.base.data.UserRightId;
import com.siteview.base.tree.INode;
import com.siteview.ecc.alert.SelectTree;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.report.common.ErrorMessage;
import com.siteview.ecc.report.common.GroupLinkListener;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.util.LinkCheck;
import com.siteview.ecc.util.Toolkit;

public class PeriodReport extends GenericForwardComposer {

	private static final long serialVersionUID = -2358783590572478386L;
	private INode							node;
	private Report							report1;
	private Report							report2;
	private Tree 							tree;	
	private Combobox 						viewNamecombobox;
	private Div 							maptable;
	private Datebox 						starttime;
	private Datebox 						endtime;	
	private Listbox 						comparetype;
	private West 							treeview;
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
	public void onClick$exportButton(Event event) throws InterruptedException{
		if(this.getNode()==null){
			Messagebox.show(ErrorMessage.UNSELECT_MONITOR, "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}	
		if(this.getStarttime().after(this.getEndtime())){
			Messagebox.show(ErrorMessage.TIME_ERROR, "提示", Messagebox.OK,
					Messagebox.INFORMATION);
			return;
		}
		
		if (this.getStarttime().after(new Date())) {
			Messagebox.show("没有您要显示的数据！", "提示", Messagebox.OK,
					Messagebox.INFORMATION);
			return;
		}
		final Window win = (Window) Executions.createComponents(
				"/main/report/exportreport/exportreport.zul", null, null);
		win.setAttribute("report1",getReport1());
		win.setAttribute("report2",getReport2());
		win.setAttribute("begin_date", getStarttime());
		win.setAttribute("end_date", getEndtime());
		win.setAttribute("compareType", getComparetype());
		win.setClosable(true);
		win.doModal();
	}
	
	public void onClick$seachButton(Event event) throws InterruptedException{
		try {
			if(this.getNode()==null){
				Messagebox.show(ErrorMessage.UNSELECT_MONITOR, "提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}
			
			if(this.getStarttime().after(this.getEndtime())){
				Messagebox.show(ErrorMessage.TIME_ERROR, "提示", Messagebox.OK,
						Messagebox.INFORMATION);
				return;
			}
			
			if (this.getStarttime().after(new Date())) {
				clear();
				return;
			}
			
			build();
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "提示", Messagebox.OK, Messagebox.INFORMATION);
		}
	}

	public void onSelect$tree(Event event) throws InterruptedException{
		try {
			Treeitem item = tree.getSelectedItem();
			if(item==null) return;
			EccTreeItem node = (EccTreeItem)item.getValue();
			INode tmpNode = node.getValue();
			if(tmpNode==null||!tmpNode.getType().equals(INode.MONITOR)) return;
			this.setNode(tmpNode);
			if(this.getNode()==null){
				Messagebox.show(ErrorMessage.UNSELECT_MONITOR, "提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}
			
			if(this.getStarttime().after(this.getEndtime())){
				Messagebox.show(ErrorMessage.TIME_ERROR, "提示", Messagebox.OK,
						Messagebox.INFORMATION);
				return;
			}
			
			if (this.getStarttime().after(new Date())) {
				clear();
				return;
			}
			
			build();
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "提示", Messagebox.OK, Messagebox.INFORMATION);
			e.printStackTrace();
		}
	}
	
	public void onChange$viewNamecombobox(Event event){
		SelectTree treeView = (SelectTree)tree;
		String viewName = viewNamecombobox.getValue();
		if (viewName==null) return;
		treeView.setViewName(viewName);	
	}
	
	public Report getReport(Date beginDate, Date endDate) throws Exception{
		return ReportManager.getReport(this.getNode(), beginDate,endDate);
	}
	
	private void init() throws Exception{
		if(getComparetype().equals(Constand.reporttype_dayreport)){
			report1 = getReport(getDayBegintime(getStarttime()),getDayEndtime(getStarttime()));
			report2 = getReport(getDayBegintime(getEndtime()),getDayEndtime(getEndtime()));
		}else if(getComparetype().equals(Constand.reporttype_weekreport)){
			report1 = getReport(getWeekBegintime(getStarttime()),getWeekEndtime(getStarttime()));
			report2 = getReport(getWeekBegintime(getEndtime()),getWeekEndtime(getEndtime()));
		}else{
			report1 = getReport(getMonthBegintime(getStarttime()),getMonthEndtime(getStarttime()));
			report2 = getReport(getMonthBegintime(getEndtime()),getMonthEndtime(getEndtime()));
		}
	}
	private void build() throws Exception{
		init();
		String type = comparetype.getSelectedItem()==null?Constand.reporttype_dayreport:comparetype.getSelectedItem().getValue().toString();
		Div tmp = buildImageMaps();
		Component c = maptable.getFirstChild();
		if(c!=null){
			maptable.removeChild(c);
		}
		maptable.appendChild(tmp);
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
	private Div buildImageMaps() throws Exception{
		int width = getScreenWidth()-getTreeSize();
		Div maptable = new Div();
		String sub1 = "",sub2="";
		Date date1=null,date2=null;
		SimpleDateFormat format;
		Map<Integer, Map<String, String>> listimage=getImagelist(report1,report2);
		if(getComparetype().equals(Constand.reporttype_dayreport)){
			sub1="("+Toolkit.getToolkit().formatDate(getDayBegintime(getStarttime()))+"~"+Toolkit.getToolkit().formatDate(getDayEndtime(getStarttime()))+")";
			sub2="("+Toolkit.getToolkit().formatDate(getDayBegintime(getEndtime()))+"~"+Toolkit.getToolkit().formatDate(getDayEndtime(getEndtime()))+")";
		}else if(getComparetype().equals(Constand.reporttype_weekreport)){
			sub1="("+Toolkit.getToolkit().formatDate(getWeekBegintime(getStarttime()))+"~"+Toolkit.getToolkit().formatDate(getWeekEndtime(getStarttime()))+")";
			sub2="("+Toolkit.getToolkit().formatDate(getWeekBegintime(getEndtime()))+"~"+Toolkit.getToolkit().formatDate(getWeekEndtime(getEndtime()))+")";
		}else{
			sub1="("+Toolkit.getToolkit().formatDate(getMonthBegintime(getStarttime()))+"~"+Toolkit.getToolkit().formatDate(getMonthEndtime(getStarttime()))+")";
			sub2="("+Toolkit.getToolkit().formatDate(getMonthBegintime(getEndtime()))+"~"+Toolkit.getToolkit().formatDate(getMonthEndtime(getEndtime()))+")";
		}
		for (int key : listimage.keySet())
		{
			Map<Date, String> imgdata1 = report1.getReturnValueDetail(key);
			Map<Date, String> imgdata2 = report2.getReturnValueDetail(key);
			Map<String, String> keyvalue = listimage.get(key);
			XYDataset data = buildDataset(imgdata2,keyvalue.get("title")+sub2,imgdata1,keyvalue.get("title")+sub1);
			Image temmap = null;
			Panel panel = null;
			long tm = Math.round(Double.parseDouble(keyvalue.get("maxvalue")))*3/2;
			long tmi = Math.round(Double.parseDouble(keyvalue.get("minvalue")))/2;
			if(imgdata1.size()>imgdata2.size()){
				if(getComparetype().equals(Constand.reporttype_dayreport)){
					date1=getDayBegintime(getStarttime());
					date2=getDayEndtime(getStarttime());
				}else if(getComparetype().equals(Constand.reporttype_weekreport)){
					date1=getWeekBegintime(getStarttime());
					date2=getWeekEndtime(getStarttime());
				}else{
					date1=getMonthBegintime(getStarttime());
					date2=getMonthEndtime(getStarttime());
				}
			}else{
				if(getComparetype().equals(Constand.reporttype_dayreport)){
					date1=getDayBegintime(getEndtime());
					date2=getDayEndtime(getEndtime());
				}else if(getComparetype().equals(Constand.reporttype_weekreport)){
					date1=getWeekBegintime(getEndtime());
					date2=getWeekEndtime(getEndtime());
				}else{
					date1=getMonthBegintime(getEndtime());
					date2=getMonthEndtime(getEndtime());
				}
			}
			if (keyvalue.get("title").contains("%"))
			{

				temmap = ChartUtil.createImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data
						, 10, tm, date1, date2, tmi,
						true, width, 300, getComparetype());
				panel = createListbox(report1,sub1,report2,sub2,key,keyvalue.get("title"));
			} else
			{
				double maxvalue = Double.parseDouble(keyvalue.get("maxvalue"));
				double minvalue = Double.parseDouble(keyvalue.get("minvalue"));
				maxvalue = maxvalue * 1.1;
				if (maxvalue < 1)
				{
					maxvalue = 1;
				}
				if (keyvalue.get("minvalue").contains("-"))
				{
					temmap = ChartUtil.createImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data
							, 10, tm, date1, date2, tmi,
							true, width, 300, getComparetype());
					panel = createListbox(report1,sub1,report2,sub2,key,keyvalue.get("title"));
				} else
				{
					temmap = ChartUtil.createImage(keyvalue.get("title"), keyvalue.get("subtitle"), keyvalue.get("title"), data
							, 10, tm, date1, date2, tmi,
							true, width, 300, getComparetype());
					panel = createListbox(report1,sub1,report2,sub2,key,keyvalue.get("title"));
				}
			}
			maptable.appendChild(panel);
			temmap.setWidth("98%");
			maptable.appendChild(temmap);
		}
		return maptable;
	}
	private static XYDataset buildDataset(Map<Date, String> imgdata,String name1,List data,String name2){
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		TimeSeries timeseries1 = new TimeSeries(name1, org.jfree.data.time.Second.class);
		TimeSeries timeseries2 = new TimeSeries(name2, org.jfree.data.time.Second.class);
		int i=0;
		for (Date date1 : imgdata.keySet())	{
			int ss = date1.getSeconds();
			int mm = date1.getMinutes();
			int hh = date1.getHours();
			int d = date1.getDate();
			int m = date1.getMonth() + 1;
			int y = date1.getYear() + 1900;
			org.jfree.data.time.Second ttime = new Second(ss, mm, hh, d, m, y);;
			String value = imgdata.get(date1);
			if (value.trim().startsWith("(status)")){
				timeseries1.add(ttime, 0);
				if(i<=data.size()-1&& data.get(i)!=null)timeseries2.add(ttime, Double.parseDouble(data.get(i).toString()));
				else timeseries2.add(ttime, null);
			} else{
				if (value.isEmpty()){
					timeseries1.add(ttime, null);
					if(i<=data.size()-1&& data.get(i)!=null)timeseries2.add(ttime, Double.parseDouble(data.get(i).toString()));
					else timeseries2.add(ttime, 0);
				} else{
					timeseries1.add(ttime, Double.parseDouble(value));
					if(i<=data.size()-1 && data.get(i)!=null)timeseries2.add(ttime, Double.parseDouble(data.get(i).toString()));
					else timeseries2.add(ttime, null);
				}
			}
			i++;
		}
		timeseriescollection.addSeries(timeseries1);
		timeseriescollection.addSeries(timeseries2);
		return timeseriescollection;
	}
	public static XYDataset buildDataset(Map<Date, String> imgdata1,String name1,Map<Date, String> imgdata2,String name2)
	{
		List<String> list = new ArrayList<String>();
		if(imgdata1.size()>imgdata2.size()){
			for(Date date : imgdata2.keySet()){
				String value = imgdata2.get(date);
				if (value.trim().startsWith("(status)")){
					list.add(null);
				} else{
					if (value.isEmpty()){
						list.add(null);
					} else{
						list.add(value);
					}
				}
			}
			return buildDataset(imgdata1,name1,list,name2);
		}else{
			for(Date date1 : imgdata1.keySet()){
				String value = imgdata1.get(date1);
				if (value.trim().startsWith("(status)")){
					list.add(null);
				} else{
					if (value.isEmpty()){
						list.add(null);
					} else{
						list.add(value);
					}
				}
			}
			return buildDataset(imgdata2,name2,list,name1);
		}
	}
	
	public static Map<Integer, Map<String, String>> getImagelist(Report simpleReport1,Report simpleReport2) {
		Map<Integer, Map<String, String>> listimage = new LinkedHashMap<Integer, Map<String, String>>();
		for (int i = 0; i < simpleReport1.getReturnSize(); i++) {
			Map<String, String> keyvalue = new HashMap<String, String>();
			String max1 = simpleReport1.getReturnValue("max", i);
			String max2 = simpleReport2.getReturnValue("max", i);
			String min1 = simpleReport1.getReturnValue("min", i);
			String min2 = simpleReport2.getReturnValue("min", i);
			String average1 = simpleReport1.getReturnValue("average", i);
			String average2 = simpleReport1.getReturnValue("average", i);
			if (simpleReport1.getReturnValue("ReturnName", i).equals("状态")) {
				keyvalue.put("subtitle", "最大值" + max1 + "/" + max2 + "平均值"
						+ average1 + "/" + average2 + "最小值" + min1 + "/" + min2
						+ "/" + "标注：状态图竖线表示告警状态");

			}

			else {
				keyvalue
						.put("subtitle", "最大值" + max1 + "/" + max2 + "平均值"
								+ average1 + "/" + average2 + "最小值" + min1
								+ "/" + min2);

			}
			keyvalue.put("title", simpleReport1.getReturnValue("ReturnName", i));
			double dmax1 =Double.parseDouble(max1);
			double dmax2 =Double.parseDouble(max2);
			keyvalue.put("maxvalue", dmax1>dmax2?max1:max2);
			double dmin1 =Double.parseDouble(min1);
			double dmin2 =Double.parseDouble(min2);
			keyvalue.put("minvalue", dmin1<dmin2?min1:min2);
			if(ChartUtil.isShowReport(simpleReport1.getM_node(), i)){
				listimage.put(i, keyvalue);
			}
		}
		return listimage;
	}
	private Panel createListbox(Report report1,String sub1,Report report2,String sub2,int index, String title) {
		Panel panel = new Panel();
		panel.setWidth("98%");
		panel.setTitle(title);
		Panelchildren children = new Panelchildren();
		children.setParent(panel);
		Listbox box = new Listbox();
		box.setFixedLayout(true);
		box.setParent(children);
		box.setWidth("100%");
		ChartUtil.addListhead(box, "名称","最大值","平均值","最小值","最大值时间");
		ChartUtil.addRow(box, "", report1.getReturnValue("MonitorName", index)+sub1,
				report1.getReturnValue("max", index),report1.getReturnValue("average", index),
				report1.getReturnValue("min", index),report1.getReturnValue("when_max", index));
		ChartUtil.addRow(box, "", report2.getReturnValue("MonitorName", index)+sub2,
				report2.getReturnValue("max", index),report2.getReturnValue("average", index),
				report2.getReturnValue("min", index),report2.getReturnValue("when_max", index));
		return panel;
	}
	public String getComparetype(){
		return comparetype.getSelectedItem()==null?Constand.reporttype_dayreport:comparetype.getSelectedItem().getValue().toString();
	}

	public Date getStarttime() {
		return starttime.getValue();
	}

	public void setStarttime(Date starttime) {
		this.starttime.setValue(starttime);
	}

	public Date getEndtime() {
		return endtime.getValue();
	}

	public void setEndtime(Date endtime) {
		this.endtime.setValue(endtime);
	}
	
	public INode getNode() {
		return node;
	}

	public void setNode(INode node) {
		this.node = node;
	}
	public Report getReport1() {
		return report1;
	}

	public void setReport1(Report report1) {
		this.report1 = report1;
	}

	public Report getReport2() {
		return report2;
	}

	public void setReport2(Report report2) {
		this.report2 = report2;
	}
	public static Date getDayBegintime(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	public static Date getDayEndtime(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		return c.getTime();
	}
	public static Date getWeekBegintime(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);		
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	public static Date getWeekEndtime(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);		
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	public static Date getMonthBegintime(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH,1);		
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	public static Date getMonthEndtime(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH,30);		
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	private void clear() {
		Component c = maptable.getFirstChild();
		if(c!=null){
			maptable.removeChild(c);
		}
	}
}
