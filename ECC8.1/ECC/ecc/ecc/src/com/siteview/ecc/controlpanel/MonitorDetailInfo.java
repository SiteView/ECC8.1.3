package com.siteview.ecc.controlpanel;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jfree.data.time.MovingAverage;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.zkoss.calendar.Calendars;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.CreateEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkex.zul.Fisheye;
import org.zkoss.zkex.zul.Fisheyebar;
import org.zkoss.zul.Applet;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Space;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.siteview.base.data.Report;
import com.siteview.base.data.ReportManager;
import com.siteview.base.data.Report.DstrItem;
import com.siteview.base.manage.View;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.alert.control.TooltipPopup;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.report.common.CreateReport;
import com.siteview.ecc.report.common.CreateReportImpl;
import com.siteview.ecc.report.common.ErrorMessage;
import com.siteview.ecc.simplereport.SimpleReport;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svecc.zk.test.SVDBViewFactory;

/**
 * 监测器详细信息处理类,用于鱼眼日历视图功能
 * 
 * @author di.tang 20090505
 */
public class MonitorDetailInfo extends GenericForwardComposer {
	private final static Logger logger = Logger.getLogger(MonitorDetailInfo.class);


	Window monitordetailwindow;
	Radiogroup theradiogroup;
	Hbox imagetable;
	Report simpleReport = null;
	private CreateReport cr = new CreateReportImpl();
	private boolean reflesh = false;// 是否刷新数据
	Date enddate;
	Date startdate;
	Date dataStartDate;
	Date dataEndDate;
	Tabbox box;

	Datebox createBegin_Date;
	Datebox createEnd_Date;
	Timebox createBegin_Time;
	Timebox createEnd_Time;

	// ****数据********************
	Radio r1;
	Radio r2;
	Radio r3;
	Radio r4;
	Radio r5;
	Listbox datalistbox;
	boolean isSerch = false;

	// ******趋势报告**************
	Datebox start;
	Datebox end;
	Timebox begin_Time;
	Timebox end_Time;
	Listbox checkDataReport;
	Listbox runtimeReport;
	Hbox imagehbox;
	int trendcyimageheight = 230;
	// *********鱼眼日历视图************
	Include fisheye;
	Listbox yylist;
	Listbox mmlist;
	Label nowtime;
	Date now;
	//Flash fishflash;
	Calendars fishflash;
	Hbox imagehboxC;
	
	Applet fisheyeapplet;
	
	Boolean dataclick=false;
	Boolean reportclick=false;
	Boolean fishclick=false;

	TooltipPopup tp=null;
	Button fisheyeclose ;
	
	boolean appendListHeader = true;


	/**
	 * WINDOW创建事件 生成图片,生成单选按钮
	 * 
	 * @param event
	 */
	public void onCreate$monitordetailwindow(CreateEvent event) {
		try{
			Map<String, Object> state = (Map<String, Object>)Executions.getCurrent().getDesktop().getSession().getAttribute("state");
			int selectedIndex=0;
		   	Boolean isSelected=false;
			if(state!=null){
//				String oldMonitorId = (String)state.get("monitorId");
//				String newMonitorId = (String)monitordetailwindow.getAttribute("monitorId");
//				selectedIndex = (Integer)state.get("selectedIndex");

//				if(selectedIndex == 3){
//					selectedIndex = 0;
//				}
//				if(oldMonitorId != null && newMonitorId!=null && oldMonitorId.equals(newMonitorId)){
					box.setSelectedIndex(selectedIndex);
					isSelected=true;
//				}
			}
			//设置分页
			datalistbox.setFixedLayout(true);
			//四十条数据不需要分页
//			datalistbox.setMold("paging");
//			datalistbox.setPageSize(20);
//			datalistbox.getPagingChild().setMold("os");
			// 设置提示
			 tp=new TooltipPopup();
			 tp.setParent(monitordetailwindow);
			 tp.onCreate();
			 tp.setStyle("border:none;color:#FFFFFF;background-color:#717171");
			 tp.addEventListener("onOpen", new PopupOpenListener());
			 
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DAY_OF_MONTH, -1);
			startdate = c.getTime();
			enddate = new Date();
			createBegin_Time.setValue(startdate);
			createEnd_Time.setValue(enddate);
			createBegin_Date.setValue(startdate);
			createEnd_Date.setValue(enddate);
			
			start.setValue(startdate);
			end.setValue(enddate);
			begin_Time.setValue(startdate);
			end_Time.setValue(enddate);	
			
			dataStartDate = startdate;
			dataEndDate = enddate;
			
			simpleReport = (Report) event.getArg().get("simpleReport");
			
			simpleReport = new Report(simpleReport.getM_node(), startdate,
					enddate);
			try {
				simpleReport.load();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Radio child = dotheradiogroup("显示全部数据");
			child.setChecked(true);
			doRADIO();
		
			imgfilldata(simpleReport, null, startdate, enddate);
			
			if (selectedIndex==1&&isSelected)
			{
				onClick$tbdata();
			}
			if(selectedIndex==2&&isSelected)
			{
				onClick$tbreport();
			}
			if(selectedIndex==3&&isSelected)
			{
				//onClick$tbfish();
				onClick$tbfisheye();
			}
			/*if(selectedIndex==4&&isSelected)
			{
			}*/
			
		
			// onClick$fishtabpanel();
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	public class PopupOpenListener implements EventListener {

		@Override
		public void onEvent(Event event) throws Exception 
		{
			TooltipPopup popup=(TooltipPopup)event.getTarget();
			Component obj=((OpenEvent)event).getReference();
			if(obj==null||!(obj instanceof Component))
				return;
			//清空数据
			popup.getRows().getChildren().clear();
			LinkedHashMap<String, String> dstrmap= (LinkedHashMap<String, String>)obj.getAttribute("dstrmap");
			popup.setTitle(dstrmap.get("title"));
			for (String kety :dstrmap.keySet())
			{
				if (!kety.equals("title"))
				popup.addDescription(kety,dstrmap.get(kety));
			}
			
		}

	}
	/**
	 * 设置 要筛选的数据
	 * @param event
	 * @throws InterruptedException
	 */
	public void onClick$filterdata(Event event) throws InterruptedException{
		try {
			startdate = getImageStartTime();
			enddate = getImageEndTime();
			if (startdate.after(enddate)) {
				throw new Exception("开始时间大于结束时间,无法筛选数据！");
			}else if(isSameDate(startdate, enddate)){
				throw new Exception("开始时间与结束时间相同,无法筛选数据！");
			}

			final Window win = (Window) Executions.createComponents(
					"/main/control/filterDataMaxMin.zul", null, null);
			win.setAttribute("count", this);
			win.setMaximizable(false);
			win.setClosable(true);
			win.doModal();
			onSelectRadio();
			
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK,Messagebox.ERROR);
		}
	}
	
	
	
	public void onClick$tbmap()
	{
		
	}
	public void onClick$tbdata()
	{
		if(dataclick)
			return;
		getDataTab();
		dataclick=true;
		// ***********************************
	}
	public void onClick$tbreport()
	{
		if(reportclick)
		{
			return;
		}
		reportclick=true;
		doTrendcyReport();
		// *******************************
	}
	public void onClick$tbfish()
	{
		if(fishclick)
		{
			return;
		}
		doFishEyeView();
		fishclick=true;
	}
	public void onClick$tbfisheye(){
		if ("com.siteview.ecc.FishEyeApplet.class".equals(fisheyeapplet.getCode())) return;
		fisheyeapplet.setCode("com.siteview.ecc.FishEyeApplet.class");
		fisheyeclose.setVisible(true);
	}
	public void onClose$monitordetailwindow(){
		closeWindow();
	}
	public static String getNodeId(){
		Report report = (Report)Executions.getCurrent().getArg().get("simpleReport");
		return report.getM_node().getSvId();
	}
	public static String getNow(){
		Calendar cal = Calendar.getInstance(Locale.CHINESE);
		return STRING_TO_DATE.format(cal.getTime());
	}

	public void closeWindow(){
		Map<String, Object> state = new HashMap<String, Object>();
		state.put("selectedIndex", box.getSelectedIndex());
		state.put("monitorId", monitordetailwindow.getAttribute("monitorId"));
		Session session = Executions.getCurrent().getDesktop().getSession();
		session.setAttribute("state", state);

	}
	
	public void onClick$regetclose(Event event){
		for(int i=0;i<monitordetailwindow.getChildren().size();i++)
		{
			Object ob=monitordetailwindow.getChildren().get(i);
			((HtmlBasedComponent)ob).detach();
		}
		monitordetailwindow.detach();
		closeWindow();
	}
	
	public void onClick$dataclose(Event event){
		for(int i=0;i<monitordetailwindow.getChildren().size();i++)
		{
			Object ob=monitordetailwindow.getChildren().get(i);
			((HtmlBasedComponent)ob).detach();
		}
		monitordetailwindow.detach();
		closeWindow();
	}
	
	public void onClick$trendcyclose(Event event){
		for(int i=0;i<monitordetailwindow.getChildren().size();i++)
		{
			Object ob=monitordetailwindow.getChildren().get(i);
			((HtmlBasedComponent)ob).detach();
		}
		monitordetailwindow.detach();
		closeWindow();
	}
	
	public void onClick$fisheyeclose(Event event){
		for(int i=0;i<monitordetailwindow.getChildren().size();i++)
		{
			Object ob=monitordetailwindow.getChildren().get(i);
			((HtmlBasedComponent)ob).detach();
		}
		monitordetailwindow.detach();
		closeWindow();
	}

	/**
	 * 生成单选按钮
	 * 
	 * @param title
	 */
	private Radio dotheradiogroup(String title) {
		Radio child = new Radio();
		child.setLabel(title);
		// Events.sendEvent(child, new Event("onCreate", child));
		/*
		 * child.add .addForward("onCheck", monitordetailwindow,
		 * "onSelectRadio", child);
		 */
		child.addEventListener(Events.ON_CHECK, new EventListener() {
			public void onEvent(Event arg0) throws Exception {
				onSelectRadio();
			}

		});
		theradiogroup.appendChild(child);
		Space space = new Space();
		space.setWidth("10px");
		theradiogroup.appendChild(space);
		return child;
	}

	/**
	 * 选中单选按钮事件
	 * 
	 * @param event
	 */
	private void onSelectRadio() {
		List<Object> lc = new ArrayList<Object>();
		for (Object o : theradiogroup.getChildren()) {
			if (o instanceof Radio) {
				lc.add(o);
			}
		}
		for (Object o : lc) {

			Radio ra = null;
			try {
				ra = (Radio) o;
			} catch (Exception e1) {
				continue;
			}
			if (ra != null && ra.isChecked()) {
				String radioTitle = ra.getLabel();
				try {
					List imagetablechild = imagetable.getChildren();
					imagetable
							.removeChild((Component) (imagetablechild.get(0)));
				} catch (Exception e) {
				}
				if (radioTitle.contains("全部")) {
					imgfilldata(simpleReport, null, getImageStartTime(), getImageEndTime());
				} else {
					imgfilldata(simpleReport, radioTitle, getImageStartTime(), getImageEndTime());
				}
			}
		}

	}

	/**
	 * 生成其他的RADIO
	 */
	private void doRADIO() {
		Map<Integer, Map<String, String>> listimage = SimpleReport
				.getImagelist(simpleReport);
		try {
			for (int key : listimage.keySet()) {
				Map<Date, String> imgdata = simpleReport
						.getReturnValueDetail(key);
				Map<String, String> keyvalue = null;
				keyvalue = listimage.get(key);
				try {
					dotheradiogroup(keyvalue.get("title"));
				} catch (Exception e1) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重新获取数据按钮事件
	 * 
	 * @param event
	 */
	public void onClick$regetdata(Event event) {
		try{
			startdate = getImageStartTime();
			enddate = getImageEndTime();
			dataStartDate = startdate;
			dataEndDate = enddate;
			reflesh = true;
			getDate(reflesh);

			if (startdate.after(enddate)) {
				try {
					Messagebox.show(ErrorMessage.TIME_ERROR, "提示", Messagebox.OK,
							Messagebox.INFORMATION);
				} catch (InterruptedException e) {
				}
				try{
					initTimeSet();
					onSelectRadio();
					clearDateListbox(datalistbox);
					setDataListbox();
					doTrendcyReport();
				}catch(Exception e){}
				return;
			}
			if (isSameDate(startdate, enddate)) {
				try {
					Messagebox.show("开始时间与结束时间相同", "提示", Messagebox.OK,
							Messagebox.INFORMATION);
				} catch (InterruptedException e) {
				}
				try{
					initTimeSet();
					onSelectRadio();
					clearDateListbox(datalistbox);
					setDataListbox();
					doTrendcyReport();
				}catch(Exception e){}
				return;
			}
			simpleReport = new Report(simpleReport.getM_node(), startdate, enddate);
			try {
				simpleReport.load();
			} catch (Exception e) {}
			initTimeSet();
			onSelectRadio();
			clearDateListbox(datalistbox);
			setDataListbox();
			doTrendcyReport();
			isSerch = true;
		}catch(Exception e){
			e.printStackTrace();
			try {
				Messagebox.show("请输入完整的查询条件", "提示", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e1) {}
		}
	}

	public void imgfilldata(Report simpleReport, String radiovalue, Date stime,
			Date etime) {

		// imagetable.setWidth(getWidth()[0] + 50 + "px");
		imagetable.setWidth("900px");
		imagetable.appendChild(getImage(simpleReport, radiovalue, stime, etime,
				350));
		/*
		 * Imagemap temmap = getImagemap(simpleReport, radiovalue, stime,
		 * etime,350); try { imagetable.appendChild(temmap); } catch (Exception
		 * e) { }
		 */
	}

	private int[] getWidth() {
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		DisplayMode dm = gs[0].getDisplayMode();
		int w = dm.getWidth();
		int h = 400;
		w = w / 10 * 8;// 设置图片宽度:分辨率80%
		int[] a = { w, h };
		return a;
	}

	/**
	 * 生成图片
	 * 
	 * @param simpleReport
	 * @param radiovalue
	 *            决定显示哪个曲线
	 */
	public Image getImage(Report simpleReport, String radiovalue, Date stime,
			Date etime, int height) {
		Map<Integer, Map<String, String>> listimage = SimpleReport
				.getImagelist(simpleReport);
		if (listimage.size() == 0) {
			return null;
		}
		double max = 0;
		double min = 0;
		for(int key : listimage.keySet()){
			String tmpMax = listimage.get(key).get("maxvalue");
			if(tmpMax!=null && !tmpMax.equals("")){
				double tt = Double.parseDouble(tmpMax);
				max = tt>max?tt:max;
				min = tt<min?tt:min;
			}
		}
		Map<String, Map<Date, String>> imgdatas = new HashMap<String, Map<Date, String>>();

		Map<String, String> keyvalue = null;
		double maxvalue = -1;
		double minvalue = -1;
		for (int key : listimage.keySet()) {
			Map<Date, String> imgdata = simpleReport
					.getReturnValueDetail(key);
			keyvalue = listimage.get(key);
			if (keyvalue == null)
				continue;
			if (radiovalue == null || "".equals(radiovalue)) {
				imgdatas.put(keyvalue.get("title"), imgdata);
				maxvalue = max;
				minvalue = min;
			} else if (radiovalue.equals(keyvalue.get("title"))) {// 只显指定的一条曲线
				imgdatas.put(radiovalue, imgdata);
				maxvalue = Double.parseDouble(keyvalue.get("maxvalue"));
				minvalue = Double.parseDouble(keyvalue.get("minvalue"));
			}
			String maxdate = keyvalue.get("maxdate");
			if (!maxdate.isEmpty() && !reflesh) {
				try {
					if(Toolkit.getToolkit().parseDate(maxdate).after(etime))
					etime = Toolkit.getToolkit().parseDate(maxdate);
				} catch (ParseException e) {
				}
			}
		}
		try {
			int	step = 10;
			int a = (int)((maxvalue - minvalue) / 10);
			if (step < a)
				step = a;

			boolean xlabel = true;
			String comparetype = "fishview";
//			maxvalue = Double.parseDouble(keyvalue.get("maxvalue")) * 1.1;
//			minvalue = Double.parseDouble(keyvalue.get("minvalue"));
			
			Session session = Executions.getCurrent().getDesktop().getSession();
			
			session.setAttribute(FilterMaxMinConstant.ExitImageMax, maxvalue);
			session.setAttribute(FilterMaxMinConstant.ExitImageMin, minvalue);
			
			Object maxObj = session.getAttribute(FilterMaxMinConstant.FilterMax);
			Object minObj = session.getAttribute(FilterMaxMinConstant.FilterMin);
			session.removeAttribute(FilterMaxMinConstant.FilterMax);
			session.removeAttribute(FilterMaxMinConstant.FilterMin);
			if(maxObj!=null && minObj!=null){
				maxvalue = Double.valueOf(String.valueOf(maxObj));
				minvalue = Double.valueOf(String.valueOf(minObj));
			}
			
			for(String keySet1:imgdatas.keySet()){
				List<Date> removeDateList = new ArrayList<Date>();
				for(Date date:imgdatas.get(keySet1).keySet()){
					Double value ;
					try{
						value = Double.parseDouble(imgdatas.get(keySet1).get(date));
					}catch(Exception e){
						value = 0.0;
					}
					if(value<minvalue ||value>maxvalue){
						removeDateList.add(date);
					}
				}
				for(Date date:removeDateList){
					imgdatas.get(keySet1).remove(date);
				}
			}
			XYDataset data1 = cr.buildDataset(imgdatas);
			
			return ChartUtil.createBufferedImage(simpleReport.getPropertyValue("MonitorName"), "", "", data1, step, maxvalue, stime, etime, minvalue, xlabel, 965, height, ChartUtil.REPORTTYPE_DAYREPORT);
//			return Toolkit.getToolkit().createImage(
//					cr.buildImageBuffer(simpleReport
//							.getPropertyValue("MonitorName"), "", "", data,
//							step, maxvalue, stime, etime, minvalue, xlabel,
//							965, height, comparetype));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	private void getDate(boolean sreflesh) {
		try {
			if (sreflesh) {
				Calendar c = Calendar.getInstance();
				c.setTime(createBegin_Date.getValue());
				Date da = createBegin_Time.getValue();
				Calendar b = Calendar.getInstance();
				b.setTime(da);
				c.set(Calendar.HOUR_OF_DAY, b.get(Calendar.HOUR_OF_DAY));
				c.set(Calendar.MINUTE, b.get(Calendar.MINUTE));
				c.set(Calendar.SECOND, 0);
				startdate = c.getTime();
				c = Calendar.getInstance();
				c.setTime(createEnd_Date.getValue());
				da = createEnd_Time.getValue();
				b = Calendar.getInstance();
				b.setTime(da);
				c.set(Calendar.HOUR_OF_DAY, b.get(Calendar.HOUR_OF_DAY));
				c.set(Calendar.MINUTE, b.get(Calendar.MINUTE));
				c.set(Calendar.SECOND, 0);
				enddate = c.getTime();
			}
		} catch (WrongValueException e) {
		}
	}

	// *******************数据********************************
	private void getDataTab() {
		setDataRadios();
		setDataListbox();
	}

	String style1 = "background:#0099ff;";// all
	String style2 = "background:#ff6666;";// error
	String style3 = "background:#ffff88;";// warning
	String style4 = "background:#00ff00;";// ok
	String style5 = "background:#ffaa66;";// disable
	String mi = "margin-left:10px;";

	private void setDataRadios() {
		r1.setStyle(style1);
		r2.setStyle(style2 + mi);
		r3.setStyle(style3 + mi);
		r4.setStyle(style4 + mi);
		r5.setStyle(style5 + mi);

		myEventListener my = new myEventListener();
		my.setr(r1);
		r1.addEventListener(Events.ON_CHECK, my);
		my = new myEventListener();
		my.setr(r2);
		r2.addEventListener(Events.ON_CHECK, my);
		my = new myEventListener();
		my.setr(r3);
		r3.addEventListener(Events.ON_CHECK, my);
		my = new myEventListener();
		my.setr(r4);
		r4.addEventListener(Events.ON_CHECK, my);
		my = new myEventListener();
		my.setr(r5);
		r5.addEventListener(Events.ON_CHECK, my);
	}

	class myEventListener implements EventListener {
		Radio r = null;

		public void setr(Radio r) {
			this.r = r;
		}

		public void onEvent(Event arg0) throws Exception {
			clearDateListbox(datalistbox);
			setdisplayanObject(this.r.getValue());
			setDataListbox();
		}

	}

	private void clearDateListbox(Listbox lbox) {
		
//		try
//		{
//			lbox.getChildren().clear();
//		}
//		catch(Exception e)
//		{}
		List lc = lbox.getChildren();
		List lh = new ArrayList();
		List li = new ArrayList();
		for (Object o : lc) {
			try {
				if (o instanceof Listhead) {
					lh.add(o);
				} else if (o instanceof Listitem) {
					li.add(o);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//点击不同的Radio时不清空Listhead
//		try {
//			for (Object c : lh) {
//				lbox.removeChild((Listhead) c);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		try {
			for (Object x : li) {
				lbox.removeChild((Listitem) x);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String[] allotherheader = null;

	/**
	 * 生成列表数据
	 */
	private void setDataListbox() {
		if(datalistbox.getListhead() == null){

			Listhead lh = new Listhead();
			lh.setSizable(true);
			lh.setParent(datalistbox);
			appendListHeader = false;
			getlh(" ", lh);
			getlh("时间", lh);
			getlh("名称", lh);
			allotherheader = new String[simpleReport.getReturnSize()];
			// int count= simpleReport.getReturnSize();
			// Integer width= 400/count;
			for (int a = 0; a < simpleReport.getReturnSize(); a++) {// 生成表头
				String header = simpleReport.getReturnValue("ReturnName", a);
				if (header.isEmpty()) {
					header = "";
				}
				allotherheader[a] = header;
				Listheader lhd = getlh(header, lh);
				// if(count>3)
				// lhd.setWidth(width+"px");
			}
			getlh("描述", lh);
		}
		
		datalistbox.setHeight("380px");
		// simpleReport.display();
		//用新的条件查询后重新load
		if(dataclick){
			simpleReport = new Report(simpleReport.getM_node(), dataStartDate,
					dataEndDate);
			try {
				simpleReport.load();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Map<Date, DstrItem> dstrs = simpleReport.getDstr();
		String name = simpleReport.getPropertyValue("MonitorName");
		
		Object[] objs = dstrs.keySet().toArray();
		int index = 0;
		if(objs.length >= 40 && !isSerch){
			for(int i=objs.length-40;i<objs.length;i++){
				index++;
				String state = dstrs.get(objs[i]).status;
				String value = dstrs.get(objs[i]).value;
				Date D = (Date)objs[i];
				if ("all".equals(displayanObject)) {
					getRow(index, state, name, D, value);
					continue;
				}
				if (state.equals(displayanObject) && !state.equals("bad")) {// 只显示一类数据
					getRow(index, state, name, D, value);
				}
				if("bad".equals(displayanObject) && (state.equals("bad") || state.equals("error"))){
					getRow(index, state, name, D, value);
				}
			}
		}else
		
		for (Date D : dstrs.keySet())// 生成各行数据
		{
			index++;
			String state = dstrs.get(D).status;
			String value = dstrs.get(D).value;
			if ("all".equals(displayanObject)) {
//				TooltipPopup tp = this.getTooltipPopup();
//				tp.setParent(monitordetailwindow);
				getRow(index, state, name, D, value);
				continue;
			}
			if (state.equals(displayanObject) && !state.equals("bad")) {// 只显示一类数据
//				TooltipPopup tp = this.getTooltipPopup();
//				tp.setParent(monitordetailwindow);
				getRow(index, state, name, D, value);
			}
			if("bad".equals(displayanObject) && (state.equals("bad") || state.equals("error"))){
				getRow(index, state, name, D, value);
			}
			
			//错误包含error和 bad
//			if(displayanObject.equals("bad"))
//		    {
//				getRow( "error", name, D, value);
//			}
		}
	}

	String displayanObject = "all";// 显示哪一类数据:正常,错误,禁止,危险,全部

	private void setdisplayanObject(String state) {
		displayanObject = state;
	}

	
	private void getRow(int id, String state, String name, Date D,
			String value) {
		Listitem li = new Listitem();
		Map<String, String> dstrmap=new LinkedHashMap<String, String>();
		//添加id信息
		getlc(id + " ", li);
		li.setTooltip(tp);
		// tp.setParent(li);
		li.setParent(datalistbox);
		setSt(state, li);// 设置当前行显示颜色
		getlc(Toolkit.getToolkit().formatDate(D), li);
		//tp.setTitle(Toolkit.getToolkit().formatDate(D));
		dstrmap.put("title",Toolkit.getToolkit().formatDate(D));
		getlc(name, li);
		//tp.addDescription("名称:", name);
		dstrmap.put("名称",name);
		// String[] v = value.split(",");
		// int i=0;
		// for (String x : v) {// 生成每格数据
		// int a = x.indexOf("=") + 1;
		// x = x.substring(a);
		// getlc(x, li);
		// tp.addDescription(allotherheader[i], x);
		// i++;
		// }
		// CPU综合使用率(%)=6,CPU详细使用率(%)=CPU0:0,CPU1:8,CPU2:18,CPU3:0.
		// 如果返回值如上，上面代码会出问题，故改成了下面的代码

		int size = allotherheader.length;
		boolean isError = false;
		for (int i = 0; i < size; ++i) {
			String key = allotherheader[i];
			try {
				int index = value.indexOf(key);
				if (index == -1) throw new Exception("key not found!");
				int index0 = index + key.length() + 1;
				int index1 = value.length() - 1;
				if (i != (size - 1))
					index1 = value.indexOf(allotherheader[i + 1]) - 1;
				String val = value.substring(index0, index1);
				//tp.addDescription(key, val);
				dstrmap.put(key, val);
				getlc(val, li);
			} catch (Exception e) {
				// 当某一条记录如下时，上面的代码不需运行，只要后面的代码显示一下 “描述” 即可
				// 2009-04-17 11:05:37 127.0.0.1(22):Cpu 监测器被禁止
				isError = true;
				//tp.addDescription(allotherheader[i], "");
				dstrmap.put(key, "");
				getlc(null, li);
			}
		}
		if(isError){
			Listcell cell = (Listcell)li.getChildren().get(li.getChildren().size() - 1);
			cell.setLabel(null);
			cell.setTooltip("");
			//tp.changeLastRow(allotherheader[size-1], null);
		}
		getlc(value, li, "40px");
		dstrmap.put("描述", value);
		li.setAttribute("dstrmap", dstrmap);
		//tp.addDescription("描述:", value);
	}

	private void setSt(String state, Listitem li) {
		if ("error".equals(state) || "bad".equals(state)) {
			li.setStyle(style2);
		} else if ("warning".equals(state)) {
			li.setStyle(style3);
		} else if ("ok".equals(state)) {
			li.setStyle(style4);
		} else if ("disable".equals(state)) {
			li.setStyle(style5);
		}
	}

	private Listcell getlc(String title, Listitem l) {
		// Style s = new Style();
		// s.setContent("border:1px;border-color: #NaNNaNNaN");
		// s.setDynamic(true);
		Listcell lx = new Listcell();
		// lx.appendChild(s);
		lx.setParent(l);
		lx.setLabel(title);
		return lx;
	}

	private Listcell getlc(String title, Listitem l, String width) {
		Listcell lx = new Listcell();
		lx.setParent(l);
		lx.setLabel(title);
		lx.setTooltip(title);
		// lx.setWidth(width);
		return lx;
	}

	/**
	 * 设置tooltip
	 * 
	 * @param row
	 * @return
	 */
	private TooltipPopup getTooltipPopup() {
		TooltipPopup tooltippopup = new TooltipPopup();
		tooltippopup.onCreate();
		tooltippopup
				.setStyle("border:none;color:#FFFFFF;background-color:#717171");
		tooltippopup.setParent(monitordetailwindow);
		return tooltippopup;
	}

	private Listheader getlh(String title, Listhead l) {
		Listheader le2 = new Listheader();
		// 根据列头中文字的长度决定列的宽度，对某些文字特别长的列宽度定位150px
		if (" ".equals(title)) {
			le2.setWidth("15px");
		}else if ("时间".equals(title) || "名称".equals(title) || "描述".equals(title)
				|| title.length() > 10) {
			le2.setWidth("140px");
		}else if("状态".equals(title)){
			le2.setWidth("40px");
		} else {
			String width = 14 * title.length() + "px";
			le2.setWidth(width);
		}
		le2.setParent(l);
		le2.setLabel(title);
		le2.setSort("auto");
		return le2;
	}

	// ********************趋势报告***********************************************
	private void doTrendcyReport() {
		//initTimeSet();
		doRuntimeReport();
		doCheckDataReport();
		
		onSelect$checkDataReport(null);
	}
	private void clearImage(){
		Component comp = imagehbox.getFirstChild();
		if (comp != null)
			imagehbox.removeChild(comp);
	}

	/**
	 * 初始化时间设置
	 */
	private void initTimeSet() {
		start.setValue(startdate);
		end.setValue(enddate);
		begin_Time.setValue(startdate);
		end_Time.setValue(enddate);

		createBegin_Date.setValue(startdate);
		createEnd_Date.setValue(enddate);
		createBegin_Time.setValue(startdate);
		createEnd_Time.setValue(enddate);
	}

	/**
	 * 生成运行情况表数据
	 */
	private void doRuntimeReport() {
		Listitem item = new Listitem();
		TooltipPopup tp = getTooltipPopup();
		tp.setTitle(simpleReport.getPropertyValue("MonitorName"));
		tp.addDescription("正常(%)", simpleReport
				.getPropertyValue("okPercent"));
		tp
				.addDescription("危险(%)", simpleReport
						.getPropertyValue("warnPercent"));
		tp.addDescription("错误(%)", simpleReport.getPropertyValue("errorPercent"));

		item.setTooltip(tp);
		getrLc(simpleReport.getPropertyValue("MonitorName"), item);
		getrLc(simpleReport.getPropertyValue("okPercent"), item);
		getrLc(simpleReport.getPropertyValue("warnPercent"), item);
		getrLc(simpleReport.getPropertyValue("errorPercent"), item);
		String latestStatus = simpleReport.getPropertyValue("latestStatus");
		if (latestStatus != null) {
			if (latestStatus.equals("ok")) {
				latestStatus = "正常";
			} else if (latestStatus.equals("error")) {
				latestStatus = "错误";
			} else
				latestStatus = "警告";
		}
		tp.addDescription("最新", latestStatus);
		getrLc(latestStatus, item);
		getrLc(simpleReport.getPropertyValue("errorCondition"), item);
		tp.addDescription("阀值", simpleReport.getPropertyValue("errorCondition"));
		clearDateListbox(runtimeReport);
		Listhead lh = new Listhead();
		Listheader lher = new Listheader();
		lher.setLabel("名称");
		lher.setWidth("100px");
		lher.setSort("auto");
		lher.setAlign("left");
		lh.appendChild(lher);
		lher = new Listheader();
		lher.setLabel("正常运行时间(%)");
		lher.setWidth("150px");
		lher.setSort("auto");
		lher.setAlign("left");
		lh.appendChild(lher);
		lher = new Listheader();
		lher.setLabel("危险(%)");
		lher.setWidth("70px");
		lher.setSort("auto");
		lher.setAlign("left");
		lh.appendChild(lher);
		lher = new Listheader();
		lher.setLabel("错误(%)");
		lher.setWidth("70px");
		lher.setSort("auto");
		lher.setAlign("left");
		lh.appendChild(lher);
		lher = new Listheader();
		lher.setLabel("最新");
		lher.setWidth("70px");
		lher.setSort("auto");
		lher.setAlign("left");
		lh.appendChild(lher);
		lher = new Listheader();
		lher.setLabel("阀值");
		lher.setWidth("200px");
		lher.setSort("auto");
		lher.setAlign("left");
		lh.appendChild(lher);
		List children = runtimeReport.getChildren();
		for (Object child : children) {
			if ((child instanceof Listhead) == true) {
				runtimeReport.removeChild((Listhead)child);
				break;
			}
		}
		runtimeReport.appendChild(lh);
		runtimeReport.appendChild(item);

	}
	public void onSelect$checkDataReport(Event event){
		try {
			View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
			int selectedIndex = getFirstValidIndex(view,simpleReport.getM_node());
			Listitem item = checkDataReport.getSelectedItem();
			if(item!=null){
				selectedIndex = Integer.parseInt(item.getValue().toString());
			}
			checkDataReportSelectListener listener = new checkDataReportSelectListener(imagehbox,simpleReport,selectedIndex,startdate,enddate);
			listener.onEvent(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static int getFirstValidIndex(View view,INode node){
		MonitorInfo info = view.getMonitorInfo(node);
		MonitorTemplate template = info.getMonitorTemplate();
		int index = 0;
		for(Map<String, String> key : template.get_Return_Items()){
			if(!key.get("sv_type").equals("String")){
				return index;
			}
			index++;
		}
		return index;
	}
	/**
	 * 生成监测数据统计表
	 */
	private void doCheckDataReport() {
		clearDateListbox(checkDataReport);
		for (int i = 0; i < simpleReport.getReturnSize(); i++) {
			Listitem item = new Listitem();
			TooltipPopup tp = getTooltipPopup();
			String svdrawimage = simpleReport.getReturnValue("sv_drawmeasure", i);
			svdrawimage = svdrawimage.isEmpty() ? "0" : svdrawimage;
			if (!ChartUtil.isShowReport(simpleReport.getM_node(), i))
			{
				continue;
			}
			item.setValue(""+i);
			getrLc(simpleReport.getReturnValue("MonitorName", i), item);
			getrLc(simpleReport.getReturnValue("ReturnName", i), item);
			getrLc(simpleReport.getReturnValue("max", i), item);
			getrLc(simpleReport.getReturnValue("min", i), item);
			getrLc(simpleReport.getReturnValue("average", i), item);
			
			getrLc(simpleReport.getReturnValue("latest", i), item);
			getrLc(simpleReport.getReturnValue("when_max", i), item);
			tp.setTitle(simpleReport.getReturnValue("MonitorName", i));
			checkDataReport.appendChild(item);
		}
		Listhead cLh = new Listhead();
		Listheader cLher = new Listheader();
		cLher.setLabel("名称");
		cLher.setSort("auto");
		cLher.setAlign("left");
		cLh.appendChild(cLher);
		cLher = new Listheader();
		cLher.setLabel("测量");
		cLher.setSort("auto");
		cLher.setAlign("left");
		cLh.appendChild(cLher);
		cLher = new Listheader();
		cLher.setLabel("最大值");
		cLher.setSort("auto");
		cLher.setAlign("left");
		cLh.appendChild(cLher);
		cLher = new Listheader();
		cLher.setLabel("最小值");
		cLher.setSort("auto");
		cLher.setAlign("left");
		cLh.appendChild(cLher);
		cLher = new Listheader();
		cLher.setLabel("平均值");
		cLher.setSort("auto");
		cLher.setAlign("left");
		cLh.appendChild(cLher);
		cLher = new Listheader();
		cLher.setLabel("最近一次");
		cLher.setSort("auto");
		cLher.setAlign("left");
		cLh.appendChild(cLher);
		cLher = new Listheader();
		cLher.setLabel("最大值时间");
		cLher.setSort("auto");
		cLher.setAlign("left");
		cLh.appendChild(cLher);
		List children = checkDataReport.getChildren();
		for (Object child : children) {
			if ((child instanceof Listhead) == true) {
				checkDataReport.removeChild((Listhead)child);
				break;
			}
		}
		checkDataReport.appendChild(cLh);
		checkDataReport.setRows(3);
	}

	/**
	 * 查询按钮事件
	 * 
	 * @param event
	 */
	public void onClick$searchBtn(Event event) {
		String title = "";
		try {
			startdate = start.getValue();
			enddate = end.getValue();
			dataStartDate = startdate;
			dataEndDate = enddate;
			if (startdate.after(enddate)) {
				Messagebox.show(ErrorMessage.TIME_ERROR, "提示", Messagebox.OK,
						Messagebox.INFORMATION);
//				ChartUtil.clearListbox(runtimeReport);
//				ChartUtil.clearListbox(checkDataReport);
//				clearImage();
				return;
			}
			logger.info(startdate.getTime());
			logger.info(enddate.getTime());
			if (isSameDate(startdate, enddate)) {
				Messagebox.show("开始时间与结束时间相同", "提示", Messagebox.OK,
						Messagebox.INFORMATION);
//				ChartUtil.clearListbox(runtimeReport);
//				ChartUtil.clearListbox(checkDataReport);
//				clearImage();
				return;
			}
			simpleReport = new Report(simpleReport.getM_node(), startdate,
					enddate);
			try {
				simpleReport.load();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			initTimeSet();
			onSelectRadio();
			clearDateListbox(datalistbox);
			setDataListbox();
			doTrendcyReport();
			isSerch = true;


			
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Messagebox.show("请输入完整的查询条件", "提示", Messagebox.OK,
						Messagebox.INFORMATION);
			} catch (InterruptedException e1) {}
		}

	}
	
	//判断2个时间除秒以外是否相同
	private boolean isSameDate(Date startdate, Date enddate){
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd hh:mm");
		return format.format(startdate).equals(format.format(enddate));
	}

	/**
	 * 返回当前趋势报告中所设定的开始时间
	 * @return Date
	 */
	private Date getTrendcyStartTime() {
		Calendar c = Calendar.getInstance();
		c.setTime(start.getValue());
		Date da = begin_Time.getValue();
		Calendar b = Calendar.getInstance();
		b.setTime(da);
		c.set(Calendar.HOUR_OF_DAY, b.get(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, b.get(Calendar.MINUTE));
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}

	/**
	 * 返回当前趋势报告中所设定的结束时间
	 * @return Date
	 */
	private Date getTrendcyEndTime() {
		Calendar c = Calendar.getInstance();
		c.setTime(end.getValue());
		Date da = end_Time.getValue();
		Calendar b = Calendar.getInstance();
		b.setTime(da);
		c.set(Calendar.HOUR_OF_DAY, b.get(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, b.get(Calendar.MINUTE));
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 返回当前图表中所设定的开始时间
	 * @return Date
	 */
	private Date getImageStartTime(){
		Calendar c = Calendar.getInstance();
		c.setTime(createBegin_Date.getValue());
		Date da = createBegin_Time.getValue();
		Calendar b = Calendar.getInstance();
		b.setTime(da);
		c.set(Calendar.HOUR_OF_DAY, b.get(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, b.get(Calendar.MINUTE));
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 返回当前图表中所设定的结束时间
	 * @return Date
	 */
	private Date getImageEndTime(){
		Calendar c = Calendar.getInstance();
		c.setTime(createEnd_Date.getValue());
		Date da = createEnd_Time.getValue();
		Calendar b = Calendar.getInstance();
		b.setTime(da);
		c.set(Calendar.HOUR_OF_DAY, b.get(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, b.get(Calendar.MINUTE));
		c.set(Calendar.SECOND, 0);
		return c.getTime();
	}
	
	/**
	 * 返回当前动态开始时间
	 * @return Date
	 */
	private Date getStartTime(){
		return this.startdate;
	}
	
	/**
	 * 返回当前动态结束时间
	 * @return Date
	 */
	private Date getEndTime(){
		return this.enddate;
	}

	private void getrLc(String label, Listitem parent) {
		Listcell lc = new Listcell();
		lc.setLabel(label);
		lc.setParent(parent);
		lc.setTooltiptext(label);
	}

	
	private void buildData() {
		try {
			simpleReport = ReportManager.getReport(simpleReport.getM_node(), startdate,enddate);
			createNewImage(startdate, "");
			doRuntimeReport();
			doCheckDataReport();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * toolbarbutton事件
	 * 
	 * @param event
	 * @throws Exception 
	 */
	public void onClick$tbbt1(Event event) {
		// 重新计算开始时间
		startdate = getStartDate(2);
		enddate = getEndDate();
		buildData();
	}

	/**
	 * toolbarbutton事件
	 * 
	 * @param event
	 */
	public void onClick$tbbt2(Event event) {
		// 重新计算开始时间
		startdate = getStartDate(4);
		enddate = getEndDate();
		buildData();
	}

	/**
	 * toolbarbutton事件
	 * 
	 * @param event
	 */
	public void onClick$tbbt3(Event event) {
		// 重新计算开始时间
		startdate = getStartDate(8);
		enddate = getEndDate();
		buildData();
	}

	/**
	 * toolbarbutton事件
	 * 
	 * @param event
	 */
	public void onClick$tbbt4(Event event) {
		// 重新计算开始时间
		startdate = getStartDate(24);
		enddate = getEndDate();
		buildData();
	}

	/**
	 * toolbarbutton事件
	 * 
	 * @param event
	 */
	public void onClick$tbbt5(Event event) {
		// 重新计算开始时间
		startdate = getStartDate(2 * 24);
		enddate = getEndDate();
		buildData();
	}

	/**
	 * toolbarbutton事件
	 * 
	 * @param event
	 */
	public void onClick$tbbt6(Event event) {
		// 重新计算开始时间
		startdate = getStartDate(3 * 24);
		enddate = getEndDate();
		buildData();
	}

	/**
	 * toolbarbutton事件
	 * 
	 * @param event
	 */
	public void onClick$tbbt7(Event event) {
		// 重新计算开始时间
		startdate = getStartDate(5 * 24);
		enddate = getEndDate();
		buildData();
	}

	/**
	 * toolbarbutton事件
	 * 
	 * @param event
	 */
	public void onClick$tbbt8(Event event) {
		// 重新计算开始时间
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		startdate = c.getTime();// 开始时间为当天00:00:00
		start.setValue(c.getTime());
		begin_Time.setValue(c.getTime());
		enddate = getEndDate();
		buildData();
	}

	/**
	 * toolbarbutton事件
	 * 
	 * @param event
	 */
	public void onClick$tbbt9(Event event) {
		// 重新计算开始时间
		startdate = getStartDate(7 * 24);
		enddate = getEndDate();
		buildData();
	}

	/**
	 * toolbarbutton事件
	 * 
	 * @param event
	 */
	public void onClick$tbbt10(Event event) {
		// 重新计算开始时间
		startdate = getStartByMonth(1);
		enddate = getEndDate();
		buildData();
	}

	/**
	 * toolbarbutton事件
	 * 
	 * @param event
	 */
	public void onClick$tbbt11(Event event) {
		// 重新计算开始时间
		startdate = getStartByMonth(2);
		enddate = getEndDate();
		buildData();
	}

	/**
	 * toolbarbutton事件
	 * 
	 * @param event
	 */
	public void onClick$tbbt12(Event event) {
		// 重新计算开始时间
		startdate = getStartByMonth(6);
		enddate = getEndDate();
		buildData();
	}

	/**
	 * toolbarbutton事件
	 * 
	 * @param event
	 */
	public void onClick$tbbt13(Event event) {
		// 重新计算开始时间
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		startdate = c.getTime();// 开始时间为当天00:00:00
		start.setValue(c.getTime());
		begin_Time.setValue(c.getTime());
		enddate = getEndDate();
		buildData();
	}

	
	private void createNewImage(Date st, String title) {
		Component comp = imagehbox.getFirstChild();
		if (comp != null)
			imagehbox.removeChild(comp);
		checkDataReportSelectListener listener = null;
		Listitem item = checkDataReport.getSelectedItem();
		int index = 0;
		if(item!=null){
			index = Integer.parseInt(item.getValue().toString());
			listener = new checkDataReportSelectListener(imagehbox,simpleReport,index,startdate,enddate);
		}else{
			View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
			index = getFirstValidIndex(view,simpleReport.getM_node());
			listener = new checkDataReportSelectListener(imagehbox,simpleReport,index,startdate,enddate);
		}
		try {
			listener.onEvent(null);
		} catch (Exception e) {
		}
	}

	/**
	 * 设置起始时间，当前的时间减去要回到以前的小时数
	 * 
	 * @param hours
	 */
	private Date getStartDate(int hours) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.HOUR_OF_DAY, -hours);
		start.setValue(c.getTime());
		begin_Time.setValue(c.getTime());
		return c.getTime();
	}

	private Date getStartByMonth(int month) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -month);
		start.setValue(c.getTime());
		begin_Time.setValue(c.getTime());
		return c.getTime();
	}
	
	/**
	 * 设置结束时间，获取当前时间
	 * @return Date
	 */
	private Date getEndDate(){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		end.setValue(c.getTime());
		end_Time.setValue(c.getTime());
		return c.getTime();
	}
	
	public void onChange$begin_Time(Event e){
		Calendar c = Calendar.getInstance();
		c.setTime(begin_Time.getValue());
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		c.setTime(start.getValue());
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		start.setValue(c.getTime());
	}
	
	public void onChange$end_Time(Event e){
		Calendar c = Calendar.getInstance();
		c.setTime(end_Time.getValue());
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		c.setTime(end.getValue());
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		end.setValue(c.getTime());
	}

	// ************************************************
	// **********鱼眼日历视图***************************
	
	private void setCalenderChar(Calendar ca) {
		Date etime = null;
		Calendar nowCa = Calendar.getInstance();
		nowCa.setTime(now);
		//对所选日期是否是当天进行判断，若是当天则结束时间为当前时间
		//若不是当天，则结束时间为23点59分59秒
		if(ca.get(Calendar.YEAR) == nowCa.get(Calendar.YEAR) && ca.get(Calendar.MONTH) == nowCa.get(Calendar.MONTH) && ca.get(Calendar.DAY_OF_MONTH) == nowCa.get(Calendar.DAY_OF_MONTH)){
			etime = now;
		}else{
			ca.set(Calendar.HOUR_OF_DAY, 23);
			ca.set(Calendar.MINUTE, 59);
			ca.set(Calendar.SECOND, 59);
			etime = ca.getTime();
		}
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		Date stime = ca.getTime();
		
//		simpleReport = new Report(simpleReport.getM_node(), simpleReport.getOldestValueTime()==null?ca.getTime():simpleReport.getOldestValueTime(), simpleReport.getLatestCreateTime()==null?now:simpleReport.getLatestCreateTime());
		simpleReport = new Report(simpleReport.getM_node(), stime, etime);
		try {
			simpleReport.load();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		Component comp = imagehboxC.getFirstChild();
		if (comp != null)
			imagehboxC.removeChild(comp);
		View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
		int firstValidIndex = getFirstValidIndex(view,simpleReport.getM_node());
		checkDataReportSelectListener listener= new checkDataReportSelectListener(imagehboxC,simpleReport,firstValidIndex,stime,etime);
		try {
			imagehboxC.appendChild(listener.buildChart(firstValidIndex));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doFishEyeView() {
		now = new Date();
		Calendar ca = Calendar.getInstance(org.zkoss.util.Locales.getCurrent());
		ca.setTime(now);
		int year = ca.get(Calendar.YEAR);
		int month = ca.get(Calendar.MONTH) + 1;
		for (int a = 2000; a < 2020; a++) {
			Listitem li = new Listitem();
			if (a == year)
				li.setSelected(true);
			li.setLabel("" + a);
			Listcell lc = new Listcell();
			lc.setLabel("" + a);
			li.appendChild(lc);
			yylist.appendChild(li);
		}
		for (int a = 1; a < 13; a++) {

			Listitem li = new Listitem();
			li.setLabel("" + a);
			if (a == month)
				li.setSelected(true);
			Listcell lc = new Listcell();
			lc.setLabel("" + a);
			li.appendChild(lc);
			mmlist.appendChild(li);
		}
		
		nowtime.setValue(Toolkit.getToolkit().formatDate(now));
		setCalenderChar(ca);		
	}

	/**
	 * 显示按钮事件,显示鱼眼视图
	 * 
	 * @param event
	 */
	public void onClick$displayfisheye(Event event) {
		/*try {
			String year = yylist.getSelectedItem().getLabel();dsf
			String month = mmlist.getSelectedItem().getLabel();
			Calendar ca = Calendar.getInstance();
			ca.setTime(new Date());
			int value = Integer.parseInt(year);
			ca.set(Calendar.YEAR, value);
			value = Integer.parseInt(month);
			ca.set(Calendar.MONTH, value - 1);
			createAllFishEyeImage(ca.getTime());
		} catch (Exception e) {
			e.printStackTrace();
			try {
				// Messagebox.show("请选择时间!");
				Messagebox.show("请选择时间!", "提示", Messagebox.OK,
						Messagebox.INFORMATION);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}*/

		String year = yylist.getSelectedItem().getLabel();
		String month = mmlist.getSelectedItem().getLabel();
		org.zkoss.zul.Calendar curCal = (org.zkoss.zul.Calendar)event.getTarget().getFellow("fishflash");
		
		Calendar ca = Calendar.getInstance(org.zkoss.util.Locales.getCurrent());
		ca.setTime(curCal.getValue());
		curCal.setValue(ca.getTime());
		
		int value = Integer.parseInt(year);
		ca.set(Calendar.YEAR, value);
		value = Integer.parseInt(month);
		ca.set(Calendar.MONTH, value - 1);
		
		setCalenderChar(ca);		
	}

	/**
	 * 显示选中日期的图形报表内容
	 * 
	 * @param event
	 */
	public void onChange$fishflash(Event event) {		
		org.zkoss.zul.Calendar curCal = (org.zkoss.zul.Calendar)event.getTarget().getFellow("fishflash");
		Calendar ca = Calendar.getInstance(org.zkoss.util.Locales.getCurrent());
		ca.setTime(curCal.getValue());
		int year = ca.get(Calendar.YEAR);
		int month = ca.get(Calendar.MONTH);
		Listitem item = null;
		List<String> years = new ArrayList<String>();
		for(Object obj : yylist.getItems()){
			if(obj instanceof Listitem){
				years.add(((Listitem)obj).getLabel());
			}
		}
		if(!years.contains(year+"")){
			years.add(year+"");
		}
		Collections.sort(years, (RuleBasedCollator)Collator.getInstance(Locale.CHINA));
		yylist.getItems().clear();
		for(String y : years){
			Listitem aitem = new Listitem();
			aitem.setLabel(y);
			if(y.equals(year+"")) aitem.setSelected(true);
			yylist.appendChild(aitem);
		}
		mmlist.selectItem(mmlist.getItemAtIndex(ca.get(Calendar.MONTH)));
		setCalenderChar(ca);
	}
	
	/**
	 * 日历显示选中的年份
	 * 
	 * @param event
	 */
	public void onSelect$yylist(Event event) {
		org.zkoss.zul.Calendar curCal = (org.zkoss.zul.Calendar)event.getTarget().getFellow("fishflash");
		String year = yylist.getSelectedItem().getLabel();
//		Date today = new Date();

		Calendar ca = Calendar.getInstance(org.zkoss.util.Locales.getCurrent());
		ca.setTime(curCal.getValue());
		ca.set(Calendar.YEAR, Integer.valueOf(year).intValue());
//		ca.set(Calendar.HOUR_OF_DAY, today.getHours());
//		ca.set(Calendar.MINUTE, today.getMinutes());
//		ca.set(Calendar.SECOND, today.getSeconds());
		curCal.setValue(ca.getTime());
		setCalenderChar(ca);
	}
	
	/**
	 * 日历显示选中的月份
	 * 
	 * @param event
	 */
	public void onSelect$mmlist(Event event) {
		org.zkoss.zul.Calendar curCal = (org.zkoss.zul.Calendar)event.getTarget().getFellow("fishflash");
		String month = mmlist.getSelectedItem().getLabel();
		Date today = new Date();
		
		Calendar ca = Calendar.getInstance(org.zkoss.util.Locales.getCurrent());
//		ca.setTime(curCal.getValue());
		ca.set(Calendar.MONTH, Integer.valueOf(month).intValue()-1);
//		ca.set(Calendar.HOUR_OF_DAY, today.getHours());
//		ca.set(Calendar.MINUTE, today.getMinutes());
//		ca.set(Calendar.SECOND, today.getSeconds());
		curCal.setValue(ca.getTime());
		setCalenderChar(ca);
	}
	
	private String pngpre = "fish";// PNG图片文件名前缀

	private void createAllFishEyeImage(Date date) {
		// 生成图片
		/*FishEyeImage fei = null;
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.set(ca.YEAR, ca.MONTH, 1);
		int max = ca.getMaximum(Calendar.DAY_OF_MONTH);// 一个月的最大天数
		String pngnameS = null;
		String pngnameL = null;		
		INode inode = simpleReport.getM_node();
		ca.setTime(date);
		int pngnameindex = getPngName(date);// PNG文件名后缀
		dopngnameindex(pngnameindex);
		for (int a = 1; a < max + 1; a++) {
			pngnameS = "icon" + a + "_small";			
			pngnameL = "icon" + a + "_large";
			ca.set(Calendar.DAY_OF_MONTH, a);
			ca.set(Calendar.HOUR_OF_DAY, 0);
			ca.set(Calendar.MINUTE, 0);
			ca.set(Calendar.SECOND, 0);
			Date stime = ca.getTime();
			// ca.set(Calendar.MONTH, 5);
			ca.set(Calendar.HOUR_OF_DAY, 23);
			ca.set(Calendar.MINUTE, 59);
			ca.set(Calendar.SECOND, 59);
			Date etime = ca.getTime();
			fei = new FishEyeImage(pngnameS, inode, stime, etime);
			fei.setFilename(pngnameS);
			fei.getJFreeChart(stime, etime, false);// 不显轴数据
			fei.createPng(fei.chart, 100, 100);// 生成小图片
			pngnameL = pngpre + pngnameindex;
			fei.setFilename(pngnameL);
			fei.getJFreeChart(stime, etime, true);// 显示轴数据
			fei.createPng(fei.chart, 600, 500);// 生成大图片
			pngnameindex++;
		}
		String src = "main/images/fisheye/tileexplorer.swf";*/
		//fishflash.setSrc(src);
		Calendar ca = Calendar.getInstance(org.zkoss.util.Locales.getCurrent());
		setCalenderChar(ca);
		// 改变fisheyejsp的src
		/*
		 * fisheye.setSrc(null); String pa = "?year=" + ca.get(Calendar.YEAR) +
		 * "&month=" + ca.get(Calendar.MONTH);
		 * fisheye.setSrc("/main/control/test.jsp" + pa);
		 */
	}

	/**
	 * 1-42按顺序生成文件名
	 * 
	 * @param date
	 * @return
	 */
	private int getPngName(Date date) {
		Calendar ca = Calendar.getInstance(org.zkoss.util.Locales.getCurrent());
		ca.setTime(date);
		GregorianCalendar calendar = new GregorianCalendar(ca
				.get(Calendar.YEAR), ca.get(Calendar.MONTH), 1);
		int lead = calendar.get(Calendar.DAY_OF_WEEK);
		return lead;
	}

	/**
	 * 处理大于1号小于月末的图片,以免在SWF中显示不正确的图片
	 * 
	 * @param pngnameindex
	 */
	private void dopngnameindex(int pngnameindex) {
		String png = null;
		for (int i = 0; i < pngnameindex; i++) {
			// 处理月初图片
			png = pngpre + i;

		}
		for (int x = pngnameindex + 1; x < 42; x++) {
			// 处理月末图片
			png = pngpre + x;
		}
	}

	public void ssssssssssssssonClick$fishtabpanel() {// 鱼眼日历视图
		Grid fishgrid = new Grid();
		fishgrid.setWidth("95%");

		// fishgrid.setStyle("border:normal");
		try {
			fishgrid.removeChild(fishgrid.getFirstChild());
			fishgrid.removeChild(fishgrid.getLastChild());
		} catch (Exception e) {
		}
		Columns columns = new Columns();
		for (int a = 0; a < 7; a++) {
			Column column = new Column();
			// column.setWidth("250px");
			columnHashMap.put("" + a, column);
			columns.appendChild(column);
		}
		fishgrid.appendChild(columns);
		Rows rs = getFisheyeRow();
		fishgrid.appendChild(rs);

	}

	Map<String, Row> rowHashMap = new HashMap<String, Row>();
	Map<String, Column> columnHashMap = new HashMap<String, Column>();
	Map fisheyeHashMap = new HashMap();

	Fisheye currentFisheye;

	private Rows getFisheyeRow() {
		Rows rs = new Rows();
		for (int x = 0; x < 6; x++) {
			Row r0 = new Row();
			r0.setHeight("5px");
			Row r1 = new Row();
			r1.setHeight("70px");
			// r1.setAction("onmouseover:addfisheyead(this);");
			for (int a = 0; a < 7; a++) {
				// 鱼眼
				Fisheyebar fisheyebar = new Fisheyebar();
				fisheyebar.setStyle("margin:-10px; ");
				fisheyebar.setAttachEdge("middle");
				fisheyebar.setItemHeight(80);
				fisheyebar.setItemWidth(100);
				fisheyebar.setItemMaxHeight(600);
				fisheyebar.setItemMaxWidth(600);

				// li.addForward("onClick", topNReport,
				// "onClickSimplemonitorreportslistbox", x + item.getId());

				Fisheye fisheye = new Fisheye();
				fisheye.setAttribute("xy", x + "" + a);
				rowHashMap.put(x + "" + a, r1);
				fisheye.setImage("/main/images/fisheye/icon1.gif");
				// fisheye.setAction("onmouseover:setdivindex(this);");
				fisheye.addEventListener(Events.ON_CLICK, new EventListener() {
					public void onEvent(Event arg0) throws Exception {
						onClickfisheye(arg0);
					}
				});
				fisheyebar.appendChild(fisheye);
				// 日期
				Label l = new Label();
				l.setValue("31日");
				Vbox v = new Vbox();
				Hbox h = new Hbox();
				v.appendChild(l);
				v.appendChild(fisheyebar);
				// r0.appendChild(l);
				r1.appendChild(v);
			}
			rs.appendChild(r0);
			rs.appendChild(r1);
		}
		return rs;
	}

	public void onClickfisheye(Event event) {
		Object da = event.getTarget();
		String key = (String) ((Fisheye) da).getAttribute("xy");
		if (currentFisheye != null && currentFisheye.equals(da)) {
			for (Row r : rowHashMap.values()) {
				r.setHeight("30px");
			}
			for (Column co : columnHashMap.values()) {
				co.setWidth("50px");
			}
		} else {
			for (Row r : rowHashMap.values()) {
				r.setHeight("30px");
			}
			for (Column co : columnHashMap.values()) {
				co.setWidth("50px");
			}
			String rn = key.substring(1);
			Column c = columnHashMap.get(rn);
			c.setWidth("300px");
			Row row = rowHashMap.get(key);
			row.setHeight("400px");
		}
		currentFisheye = (Fisheye) da;
		// ((Row)(((Fisheye)da).getParent().getParent())).setHeight("600px");		
	}
	public static final SimpleDateFormat STRING_TO_DATE = new SimpleDateFormat ("yyyy-MM-dd");
	public static BufferedImage getBufferedImage(String sessionid,
			String nodeid, String date) throws Exception {
		logger.info("--->" + date);
		View view = SVDBViewFactory.getView(sessionid);
		Report simpleReport = new Report(view.getNode(nodeid));
		Calendar ca = Calendar.getInstance(org.zkoss.util.Locales.getCurrent());
		ca.setTime(STRING_TO_DATE.parse(date));
		return getBufferedImage(view,simpleReport,ca);
	}
	public static BufferedImage getBufferedImage(View view,Report simpleReport,Calendar ca) throws Exception{
		Date etime = null;
		Calendar nowCa = Calendar.getInstance(org.zkoss.util.Locales.getCurrent());
		//对所选日期是否是当天进行判断，若是当天则结束时间为当前时间
		//若不是当天，则结束时间为23点59分59秒
		if(ca.get(Calendar.YEAR) == nowCa.get(Calendar.YEAR) && ca.get(Calendar.MONTH) == nowCa.get(Calendar.MONTH) && ca.get(Calendar.DAY_OF_MONTH) == nowCa.get(Calendar.DAY_OF_MONTH)){
			etime = nowCa.getTime();
		}else{
			ca.set(Calendar.HOUR_OF_DAY, 23);
			ca.set(Calendar.MINUTE, 59);
			ca.set(Calendar.SECOND, 59);
			etime = ca.getTime();
		}
		ca.set(Calendar.HOUR_OF_DAY, 0);
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		Date stime = ca.getTime();
		
		Report report = new Report(simpleReport.getM_node(), stime, etime);
		try {
			report.load();
		}catch (Exception e) {
			e.printStackTrace();
		}
		int firstIndex = getFirstValidIndex(view,report.getM_node());
		checkDataReportSelectListener listener= new checkDataReportSelectListener(null,report,firstIndex,stime,etime);
		return listener.buildFishEyeBufferedImage(firstIndex);
	}
}
class checkDataReportSelectListener implements EventListener {
	private int index = 0;
	private Report report;
	private Hbox imagehbox;
	private Date startTime, endTime;
	public checkDataReportSelectListener(Hbox imagehbox ,Report report,int index,Date startTime,Date endTime) {
		this.index = index;
		this.report = report;
		this.imagehbox = imagehbox;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		try {
			report.load();
		} catch (Exception e) {
			throw e;
		}
		Component cpt = imagehbox.getFirstChild();
		if (cpt != null)
			imagehbox.removeChild(cpt);
		try {
			imagehbox.appendChild(this.buildChart(index));
		} catch (IOException e) {
			throw e;
		}	
	}
    //过滤 要 生成图片的数据
	public XYDataset buildDataset(int index, String name) {
		Map<Date, String> imgdata = report
				.getReturnValueDetail(index);

		TimeSeries timeseries = new TimeSeries(name,
				org.jfree.data.time.Second.class);
		Date datestart = null;
		int i = 0;
		for (Date date1 : imgdata.keySet()) {
			int ss = date1.getSeconds();
			int mm = date1.getMinutes();
			int hh = date1.getHours();
			int d = date1.getDate();
			int m = date1.getMonth() + 1;
			int y = date1.getYear() + 1900;
			if (i == 0) {
				datestart = date1;
				i = 1;
			}
			org.jfree.data.time.Second ttime = new Second(ss, mm, hh, d, m,
					y);
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
		int av = 6 * 60 * 60;
		if (datestart != null) {
			long day = GetDateMargin(datestart, new Date());
			if (day > 10) {
				av = 24 * 60 * 60;
			}
			if (day > 30) {
				av = 6 * 24 * 60 * 60;
			}
			if (day > 60) {
				av = 24 * 24 * 60 * 60;
			}
			if (day > 120) {
				av = 10 * 24 * 24 * 60 * 60;
			}
			if (day > 180) {
				av = 60 * 24 * 24 * 60 * 60;
			}

		}
		TimeSeries average1 = MovingAverage.createMovingAverage(timeseries,
				"趋势线", av, 5); // 绘制1小时移动平均线

		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection(
				timeseries);
		timeseriescollection.addSeries(average1);
		return timeseriescollection;
	}
	
	public long GetDateMargin(Date beginDate,Date endDate){
	    long margin = 0;

	    margin = endDate.getTime() - beginDate.getTime();

	    margin = margin/(1000*60*60*24);

	    return margin;
	}
	
	public Div buildChart(int index) throws Exception {
		Div imagetable = new Div();
		imagetable.appendChild(buildImage(index));
		return imagetable;
	}
	private Image buildImage(int index) throws Exception{
		String title = report.getReturnValue("MonitorName", index);
		String min = report.getReturnValue("min", index);
		String max = report.getReturnValue("max", index);
		String average = report.getReturnValue("average", index);
		String subtitle = "最小值：" + min + "最大值：" + max + "平均值：" + average;
		String name = report.getReturnValue("ReturnName", index) == null ? ""
				: report.getReturnValue("ReturnName", index);
		XYDataset data = buildDataset(index, name);
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
		return ChartUtil.createBufferedImage(title, subtitle,
				title, data, 1, tm, startTime, endTime, tmi, true, 965, 300,
				ChartUtil.REPORTTYPE_DAYREPORT);
	}
	private BufferedImage buildBufferedImage(int index) throws Exception{
		String title = report.getReturnValue("MonitorName", index);
		String min = report.getReturnValue("min", index);
		String max = report.getReturnValue("max", index);
		String average = report.getReturnValue("average", index);
		String subtitle = "最小值：" + min + "最大值：" + max + "平均值：" + average;
		String name = report.getReturnValue("ReturnName", index) == null ? ""
				: report.getReturnValue("ReturnName", index);
		XYDataset data = buildDataset(index, name);
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
		return ChartUtil.getBufferedImage(title, subtitle,
				title, data, 1, tm, startTime, endTime, tmi, true, 790, 300,
				ChartUtil.REPORTTYPE_DAYREPORT);
	}
	public BufferedImage buildFishEyeBufferedImage(int index) throws Exception{
		String title = report.getReturnValue("MonitorName", index);
		String min = report.getReturnValue("min", index);
		String max = report.getReturnValue("max", index);
		String average = report.getReturnValue("average", index);
		String subtitle = "最小值：" + min + "最大值：" + max + "平均值：" + average;
		String name = report.getReturnValue("ReturnName", index) == null ? ""
				: report.getReturnValue("ReturnName", index);
		XYDataset data = buildDataset(index, name);
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
		return ChartUtil.getBufferedImage(title, subtitle,
				"", data, 1, tm, startTime, endTime, tmi, false, 790, 300,
				ChartUtil.REPORTTYPE_DAYREPORT);
	}
}