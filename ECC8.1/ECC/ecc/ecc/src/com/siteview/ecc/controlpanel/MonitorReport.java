package com.siteview.ecc.controlpanel;

import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.jfree.data.xy.XYDataset;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Image;
import org.zkoss.zul.Imagemap;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.siteview.base.data.Report;
import com.siteview.base.data.Report.DstrItem;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.email.EmailBean;
import com.siteview.ecc.email.EmailModel;
import com.siteview.ecc.simplereport.ReportListmodel;
import com.siteview.ecc.simplereport.SimpleReport;
import com.siteview.ecc.util.Toolkit;

public class MonitorReport extends GenericForwardComposer implements
		EccLayoutListener {	
	
	public void onCreate$WMonitorInfo(Event event) {
		//初始时打开设置窗口//超级连接功能增加
		try{
			String subMenuId = this.execution.getParameter("subMenuId");
			if(subMenuId!=null && !subMenuId.equals("")){
				if(WMonitorInfo == null){throw new Exception("#WMonitorInfo is null! in onCreate$WMonitorInfo(Event event)");}
				if(BaseTools.getComponentById(WMonitorInfo, subMenuId) == null){
					throw new Exception("#BaseTools.getComponentById(WMonitorInfo, subMenuId)) is null! in onCreate$WMonitorInfo(Event event)");
				}
				Events.sendEvent(new Event(Events.ON_CLICK,BaseTools.getComponentById(WMonitorInfo, subMenuId)));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void eccLayout(EccLayout eccLayout) {
		if (this.monitorId == null)
			return;
		int left = eccLayout.getTreeSize();
		int screenwidth = getScreenWidth();
		int right = eccLayout.getActionSize();
		int chartWidth = screenwidth - left - right - 350 - 60;

		buildMap(true, chartWidth);
	}

	// 绑定
	Borderlayout WMonitorInfo;
	Button btndetail;//这个 Button 在别处有 引用 不能 修改
	Label lbok;
	Label lberror;
	Label lbdanger;
	Label lbdisable;
	Label lbfz;
	Label datefrom;
	Label dateto;
	Listbox listtj;

	Image imagetable;
	// Imagemap image;
	int screenWidth = -1;
	//
	private String monitorId;
	private String monitorname;
	private Report simpleReport;
	static String error_message;

	public MonitorReport() {
		
	}

	/**
	 * 
	 * @param include
	 * @param monitorid
	 */
	public void RefreshData(String monitorid) {
		error_message = null;
		this.monitorId = monitorid;
		View w = null;
		try {
			w = Toolkit.getToolkit().getSvdbView(desktop);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (w == null) {
			error_message = "未登录或无效的会话！";
			return;
		}
		INode n = w.getNode(monitorId);
		if (n == null) {
			error_message = "节点不存在或无权访问！";
			return;
		}
		if (!n.getType().equals(INode.MONITOR)) {
			error_message = "节点类型非法！";
			return;
		}
//		MonitorInfo info = w.getMonitorInfo(n);
//		if (info == null) {
//			error_message = "节点不存在或无权访问！";
//			return;
//		}
		try {
//			simpleReport = ReportManager.getSimpleReport(info);
			simpleReport=new Report(n);
			simpleReport.load();
		} catch (Exception ex) {
			error_message = ex.getMessage();
			ex.printStackTrace();
		}
		
		//读取当前时间前24小时内的数据
//		Date now = new Date();
//		Calendar c = Calendar.getInstance();
//		c.setTime(now);
//		c.add(Calendar.DAY_OF_MONTH, -1);
//		simpleReport = new Report(simpleReport.getM_node(), c.getTime(), now);
//		try {
//			simpleReport.load();
//		} catch (Exception e) {}
//		
		if (error_message != null) {
			System.err.println("详细信息错误"+error_message);
			return;
		}
		Listbox listtj1 = (Listbox) WMonitorInfo.getFellow("listtj");
		buidInfo(simpleReport);
		ReportListmodel reportListmodeltj = new com.siteview.ecc.simplereport.ReportListmodel(
				"StatisticsBean1", simpleReport);
		MakelistData(listtj1, reportListmodeltj, reportListmodeltj);
		// imgfilldata(simpleReport);
		buildMap(false, 0);
		//记录监测器浏览次数
		
		if (INode.MONITOR.equals(n.getType())){
			View view =Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
			MonitorInfo mInfo = view.getMonitorInfo(simpleReport.getM_node());
			try {
				mInfo.incBrowserCount();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void imgfilldata(Report simpleReport) {
		Map<Integer, Map<String, String>> listimage = SimpleReport
				.getImagelist(simpleReport);
		if (listimage.size() == 0) {
			return;
		}
		GraphicsEnvironment ge = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		DisplayMode dm = gs[0].getDisplayMode();
		int w = 600;
		int h = 200;
		if (dm.getWidth() == 1024) {
			w = 300;
			h = 110;

		}
		int i = 0;
		int index = 0;
		for (int key : listimage.keySet()) {
			if (i == 0) {
				index = key;
			}
			i = 1;
		}
		Map<Date, String> imgdata = simpleReport
				.getReturnValueDetail(index);
		Map<String, String> keyvalue = listimage.get(index);
		XYDataset data = SimpleReport.buildDataset(imgdata);
		Imagemap temmap = (Imagemap) WMonitorInfo.getFellow("image");
		String maxdate = keyvalue.get("maxdate");
		Date maxd = null;
		if (!maxdate.isEmpty()) {
			try {
				maxd = Toolkit.getToolkit().parseDate(maxdate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (keyvalue.get("title").contains("%")) {
			temmap = SimpleReport.buildImageMapDEL(keyvalue.get("title"), keyvalue
					.get("subtitle"), keyvalue.get("title"), data, 10, 100,
					maxd, 0, false, w, h);
		} else {
			double maxvalue = Double.parseDouble(keyvalue.get("maxvalue"));
			double minvalue = Double.parseDouble(keyvalue.get("minvalue"));
			maxvalue = maxvalue * 1.1;
			if (maxvalue < 1) {
				maxvalue = 1;
			}
			if (keyvalue.get("minvalue").contains("-")) {
				temmap = SimpleReport.buildImageMapDEL(keyvalue.get("title"),
						keyvalue.get("subtitle"), keyvalue.get("title"), data,
						20, maxvalue, maxd, minvalue, false, w, h);
			} else {
				temmap = SimpleReport.buildImageMapDEL(keyvalue.get("title"),
						keyvalue.get("subtitle"), keyvalue.get("title"), data,
						20, maxvalue, maxd, 0, false, w, h);
			}
		}

	}

	/**
	 * 
	 * @param include
	 * @param simpleReport
	 */
	public void buidInfo(Report simpleReport) {
		// String okPercent = simpleReport.getPropertyValue("okPercent");
		// String warnPercent = simpleReport.getPropertyValue("warnPercent");
		// String errorPercent = simpleReport.getPropertyValue("errorPercent");
		Map<Date, DstrItem> dstrs = simpleReport.getDstr();
		int okcount = 0, errorcount = 0, disablecount = 0, warningcount = 0;
		String mindate = null;
		String maxdate = null;
		if (!dstrs.isEmpty()){
			Object[] objs = dstrs.keySet().toArray();
			if(objs.length >= 40){
				for(int i=objs.length-40;i<objs.length;i++){
					String state = dstrs.get(objs[i]).status;
					
					if (state.equals("ok")) {
						okcount++;

					} else if (state.equals("error") || state.equals("bad")) {		//将error和 bad统一显示到错误里面
						errorcount++;
					} else if (state.equals("disable")) {
						disablecount++;
					} else if (state.equals("warning")) {
						warningcount++;
					}
				}
			}else{
				for (Date D : dstrs.keySet()) {
					String state = dstrs.get(D).status;

					if (state.equals("ok")) {
						okcount++;

					} else if (state.equals("error") || state.equals("bad")) {		//将error和 bad统一显示到错误里面
						errorcount++;
					} else if (state.equals("disable")) {
						disablecount++;
					} else if (state.equals("warning")) {
						warningcount++;
					}
					
					// list.add(new
					// com.siteview.ecc.simplereport.HistoryBean(monitorName,
					// D.toLocaleString(), dstr, state));
				}
			}
			Iterator itm = dstrs.keySet().iterator();
			mindate = Toolkit.getToolkit().formatDate((Date) itm.next());
			maxdate = simpleReport.getPropertyValue("latestCreateTime");
		}
		float dis = simpleReport.getDisablePercentOfSimpleReport();
		// String disablePercent = Float.toString(dis);
		String errorCondition = simpleReport.getPropertyValue("errorCondition");
//		Iterator itm = dstrs.keySet().iterator();
//		String mindate = Toolkit.getToolkit().formatDate((Date) itm.next());
//		String maxdate = simpleReport.getPropertyValue("latestCreateTime");
		Label lbok1 = (Label) WMonitorInfo.getFellow("lbok");
		Label lbdanger1 = (Label) WMonitorInfo.getFellow("lbdanger");
		Label lberror1 = (Label) WMonitorInfo.getFellow("lberror");
		Label lbdisable1 = (Label) WMonitorInfo.getFellow("lbdisable");
		Label lbfz1 = (Label) WMonitorInfo.getFellow("lbfz");
		Label datefrom1 = (Label) WMonitorInfo.getFellow("datefrom");
		Label dateto1 = (Label) WMonitorInfo.getFellow("dateto");

		lbok1.setValue(okcount + " 条");
		lbdanger1.setValue(warningcount + " 条");

		lberror1.setValue(errorcount + " 条");

		lbdisable1.setValue(disablecount + " 条");

		if (errorCondition != null) {
			lbfz1.setValue(errorCondition);
			lbfz1.setTooltiptext(errorCondition);
		}
		if (mindate != null) {
			datefrom1.setValue(mindate);
		}else{
			datefrom1.setValue("");
		}
		if (maxdate != null) {
			dateto1.setValue(maxdate);
		}else{
			dateto1.setValue("");
		}
	}

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		WMonitorInfo.setAttribute("Composer", this);

		if (desktop.hasPage("controlPage")) {
			if (desktop.getPage("controlPage").hasFellow("controlLayout")) {
				EccLayout eccLayout = (EccLayout) desktop
						.getPage("controlPage").getFellow("controlLayout");
				eccLayout.addEccLayoutListener(this);
			}
		}
		
		/*
		 * if (error_message != null && !error_message.isEmpty()) { return; }
		 * buildinfo(); // 统计 ReportListmodel reportListmodeltj = new
		 * com.siteview.ecc.simplereport.ReportListmodel("StatisticsBean",
		 * simpleReport); MakelistData(listtj, reportListmodeltj,
		 * reportListmodeltj);
		 * 
		 * buildMap();
		 */

	}

	/*
	 * 
	 */

	/**
	 * 
	 * @param listb
	 * @param model
	 * @param rend
	 */
	public void MakelistData(Listbox listb, ReportListmodel model,
			ListitemRenderer rend) {
		listb.setModel(model);
		listb.setItemRenderer(rend);
	}

	/**
	 * 构建百分比等信息
	 */
	private void buildinfo() {
		String okPercent = simpleReport.getPropertyValue("okPercent");
		String warnPercent = simpleReport.getPropertyValue("warnPercent");
		String errorPercent = simpleReport.getPropertyValue("errorPercent");
		Map<Date, DstrItem> dstrs = simpleReport.getDstr();
		if (dstrs.isEmpty())
			return;
		float dis = simpleReport.getDisablePercentOfSimpleReport();
		String disablePercent = Float.toString(dis);
		String errorCondition = simpleReport.getPropertyValue("errorCondition");
		Iterator itm = dstrs.keySet().iterator();
		String mindate = Toolkit.getToolkit().formatDate((Date) itm.next());
		String maxdate = simpleReport.getPropertyValue("latestCreateTime");
		if (okPercent != null) {
			lbok.setValue(okPercent);
		}
		if (warnPercent != null) {
			lbdanger.setValue(warnPercent);
		}
		if (errorPercent != null) {
			lberror.setValue(errorPercent);
		}
		if (disablePercent != null) {
			lbdisable.setValue(disablePercent);
		}
		if (errorCondition != null) {
			lbfz.setValue(errorCondition);
		}
		if (mindate != null) {
			datefrom.setValue(mindate);
		}
		if (maxdate != null) {
			dateto.setValue(maxdate);
		}

	}

	private int getScreenWidth() {
		if (screenWidth == -1) {
			try {
				screenWidth = Integer.parseInt(desktop.getPage("eccmain")
						.getFellow("tree").getAttribute("screenWidth")
						.toString());
			} catch (Exception e) {
				screenWidth = 1024;
			}
		}
		return screenWidth;
	}

	private void buildMap(Boolean refresh, int chartWidth) {
		buildMapOld(refresh, chartWidth);
		imagetable.setSrc("/main/report/createImage.jsp?id=" + this.monitorId);
	}
	
	private void buildNullMap(int w,int h)
	{
		Map<Date, String> imgdata =new LinkedHashMap<Date, String>();
		Date d1=new Date();
		imgdata.put(d1 , "0");
		XYDataset data = SimpleReport.buildDataset(imgdata);	
		BufferedImage temmap = null;
		temmap = SimpleReport.buildBufferImage("",
				"","", data, 10,
				102, d1, 0, false, w, h);
		session.setAttribute(this.monitorId, temmap);
	}

	private void buildMapOld(Boolean refresh, int chartWidth) {

		Map<Integer, Map<String, String>> listimage = SimpleReport
				.getImagelist(simpleReport);
		int h = 157;

		int w = getScreenWidth() - 1024 + 278;
		if (w < 278)
			w = 278;
		if (refresh) {
			w = chartWidth;
		}
		// imagetable.setWidth(w + 5 + "px");
		if (listimage.size() == 0) {
			buildNullMap(w,h);
			return;
		}
		int i = 0;
		int index = 0;
		for (int key : listimage.keySet()) {
			if (i == 0) {
				http: // 127.0.0.1:8080/ecc/
				index = key;
			}
			i = 1;
		}
		Map<Date, String> imgdata = this.simpleReport
				.getReturnValueDetail(index);
		Map<String, String> keyvalue = listimage.get(index);
		if (keyvalue == null) {
			return;
		}
		XYDataset data = SimpleReport.buildDataset(imgdata);
		BufferedImage temmap = null;
		String maxdate = keyvalue.get("maxdate");
		Date maxd = null;
		if (maxdate != null && !maxdate.isEmpty()) {
			try {
				maxd = Toolkit.getToolkit().parseDate(maxdate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (keyvalue.get("title").contains("%")) {
			temmap = SimpleReport.buildBufferImage(keyvalue.get("title"),
					keyvalue.get("subtitle"), keyvalue.get("title"), data, 10,
					102, maxd, 0, false, w, h);
		} else {
			double maxvalue = Double.parseDouble(keyvalue.get("maxvalue"));
			double minvalue = Double.parseDouble(keyvalue.get("minvalue"));
			maxvalue = maxvalue * 1.1;
			if (maxvalue < 1) {
				maxvalue = 1;
			}
			if (keyvalue.get("minvalue").contains("-")) {
				temmap = SimpleReport.buildBufferImage(keyvalue.get("title"),
						keyvalue.get("subtitle"), keyvalue.get("title"), data,
						20, maxvalue, maxd, minvalue, true, w, h);
			} else {
				temmap = SimpleReport.buildBufferImage(keyvalue.get("title"),
						keyvalue.get("subtitle"), keyvalue.get("title"), data,
						20, maxvalue, maxd, 0, false, w, h);
			}
		}
		session.setAttribute(this.monitorId, temmap);
	}

	// /
	public void onClick$btndetail(Event event) {
		showDetail();
	}

	public void showDetail() {
		if (monitorId != null && simpleReport != null) {
			HashMap pmap = new HashMap();
			pmap.put("monitorId", monitorId);
			pmap.put("simpleReport", simpleReport);
			final Window win = (Window) Executions.createComponents(
					"/main/control/monitordetailinfo.zul", null, pmap);
			// win.setMaximizable(true);// 不允许最大化
			// win.setMaximized(true);
			win.setAttribute("monitorId", monitorId);
			win.setClosable(true);
			win.setTitle("详细信息");
			try {
				win.doModal();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	
	public void doCookies(String sessionid,String nodeid){
		setCookie("sessionid",sessionid);
		setCookie("nodeid",nodeid);
		//doRequest("/ecc/cookie.jsp?name=sessionid&op=set&item=" + sessionid);
		//doRequest("/ecc/cookie.jsp?name=nodeid&op=set&item=" + nodeid);
	}

	private void doRequest(String urlstring) {
		try {
			// Create a URL for the desired page
			URL url = new URL("http://localhost:8080/" + urlstring);

			// Read all the text returned by the server
			BufferedReader in = new BufferedReader(new InputStreamReader(url
					.openStream()));
			String str;
			while ((str = in.readLine()) != null) {
				// str is one line of text; readLine() strips the newline
				// character(s)
			}
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public String getSessionId(){
		Session session = this.desktop.getSession();
		Object usersessionid = session.getAttribute("usersessionid");
		return usersessionid == null ? null : usersessionid.toString();
	}
	
	public void setCookie(String name, String Content) {
		HttpServletResponse response = (HttpServletResponse)Executions.getCurrent()
				.getNativeResponse();
		Cookie userCookie = new Cookie(name, Content);
		userCookie.setMaxAge(128000);
		response.addCookie(userCookie);
	} 

}
