package com.siteview.ecc.report;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zkex.zul.West;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import com.siteview.base.cache.ReportDataDaoImpl;
import com.siteview.base.data.ReportDate;
import com.siteview.base.data.ReportDateError;
import com.siteview.base.data.UserRightId;
import com.siteview.base.tree.INode;
import com.siteview.ecc.alert.SelectTree;
import com.siteview.ecc.alert.dao.bean.AccessControl;
import com.siteview.ecc.alert.dao.bean.AlertRuleQueryCondition;
import com.siteview.ecc.alert.dao.bean.BaseAlert;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.alert.util.DictionaryFactory;
import com.siteview.ecc.report.beans.CompareDataBean;
import com.siteview.ecc.report.beans.ErrorLogsBean;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.report.common.ErrorMessage;
import com.siteview.ecc.report.common.GroupLinkListener;
import com.siteview.ecc.report.common.ReportServices;
import com.siteview.ecc.util.LinkCheck;
import com.siteview.ecc.util.Toolkit;

/**
 *错误对比报告处理类
 * 
 * @company: siteview
 * @author:di.tang
 * @date:2009-4-2
 */

public class ErrorCompareReport extends GenericForwardComposer {
	Datebox 						starttime;
	Datebox 						endtime;
	Div 							runtimeDiv;
	private Div 					maptable;
	private West 					treeview;
	private Combobox 				viewNamecombobox;
	private Listbox					runtimeState;
	
	private Tree 					monitortree;
	private boolean					export=false;
	private Label 	 groupLink;
	public final static SimpleDateFormat SDF = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	public void onCreate$groupLink(Event event){
		boolean isLink = LinkCheck.getLinkCheck().CanSeeLink(UserRightId.WholeView);
		if(isLink){
			String style = "color:#18599C;cursor:pointer;text-decoration: underline;";
			groupLink.setStyle(style);
		}
	}
	public void onClick$groupLink(Event e){
		boolean isLink = LinkCheck.getLinkCheck().CanSeeLink(UserRightId.WholeView);
		if(isLink){
			Comboitem item = viewNamecombobox.getSelectedItem();
			if(item!=null){
				groupLink.addEventListener(Events.ON_CLICK,new GroupLinkListener(item.getLabel()));
			}
		}
	}
	public SelectTree getMonitorTree() {
		return (SelectTree) BaseTools.getComponentById(treeview, "monitortree");
	}

	public void onChange$viewNamecombobox(Event event) {
		SelectTree treeView = (SelectTree) monitortree;
		String viewName = viewNamecombobox.getValue();
		if (viewName != null) {
			treeView.setViewName(viewName);
		}
	}

	/**
	 * 获取选择的监测器节点
	 * 
	 * @return
	 */
	public List<INode> getNodeids() {
		List<INode> nodes = new ArrayList<INode>();
		boolean b = true;
		Set<String> retset = new LinkedHashSet<String>();
		String groupNmae = null;
		SelectTree sTree = (SelectTree) getMonitorTree();
		for (String id : sTree.getSelectedIds()) {
			INode node = ChartUtil.getView().getNode(id);
			if (node != null) {
				INode node1 = ChartUtil.getView().getNode(node.getParentSvId());
				if (node1 != null) {
					INode node2 = ChartUtil.getView().getNode(
							node1.getParentSvId());
					if (node2 != null) {
						INode node3 = ChartUtil.getView().getNode(
								node2.getParentSvId());
						if (node3 != null) {
							INode node4 = ChartUtil.getView().getNode(
									node3.getParentSvId());
							if (node4 != null) {
								INode node5 = ChartUtil.getView().getNode(
										node4.getParentSvId());
								if (node5 != null) {
									INode node6 = ChartUtil.getView().getNode(
											node5.getParentSvId());
									if (node6 != null) {
										INode node7 = ChartUtil.getView()
												.getNode(node6.getParentSvId());
										if (node7 != null) {
											INode node8 = ChartUtil
													.getView()
													.getNode(
															node7
																	.getParentSvId());
											if (node8 != null) {
												INode node9 = ChartUtil
														.getView()
														.getNode(
																node8
																		.getParentSvId());
												if (node9 != null) {
													INode node10 = ChartUtil
															.getView()
															.getNode(
																	node9
																			.getParentSvId());
													if (node10 != null) {
														INode node11 = ChartUtil
																.getView()
																.getNode(
																		node10
																				.getParentSvId());
														if (node11 != null) {
															INode node12 = ChartUtil
																	.getView()
																	.getNode(
																			node11
																					.getParentSvId());
															if (node12 != null) {
																INode node13 = ChartUtil
																		.getView()
																		.getNode(
																				node12
																						.getParentSvId());
																if (node13 != null) {
																	INode node14 = ChartUtil
																			.getView()
																			.getNode(
																					node13
																							.getParentSvId());
																	if (node14 != null) {
																		INode node15 = ChartUtil
																				.getView()
																				.getNode(
																						node14
																								.getParentSvId());
																		if (node15 != null) {
																			INode node16 = ChartUtil
																					.getView()
																					.getNode(
																							node15
																									.getParentSvId());
																			if (node16 != null) {
																				INode node17 = ChartUtil
																						.getView()
																						.getNode(
																								node16
																										.getParentSvId());
																			} else {
																				groupNmae = node15
																						.getName();
																			}
																		} else {
																			groupNmae = node14
																					.getName();
																		}
																	} else {
																		groupNmae = node13
																				.getName();
																	}
																} else {
																	groupNmae = node12
																			.getName();
																}
															} else {
																groupNmae = node11
																		.getName();
															}
														} else {
															groupNmae = node10
																	.getName();
														}
													} else {
														groupNmae = node9
																.getName();
													}
												} else {
													groupNmae = node8.getName();
												}
											} else {
												groupNmae = node7.getName();
											}
										} else {
											groupNmae = node6.getName();
										}
									} else {
										groupNmae = node5.getName();
									}
								} else {
									groupNmae = node4.getName();
								}
							} else {
								groupNmae = node3.getName();
							}
						} else {
							groupNmae = node1.getName();
						}
					} else {
						groupNmae = node.getName();
					}
				} else {
					groupNmae = node.getName();
				}
			} else {
				groupNmae = node.getName();
			}
			retset.add(groupNmae);
			if (node.getType().equals(INode.MONITOR))
				nodes.add(node);
		}
		StringBuffer buffer= new StringBuffer();
		for(String title : retset){
			if (buffer.length()>0)buffer.append(";");
			buffer.append(title);
		}
		
		Executions.getCurrent().getDesktop().getSession().setAttribute(
				"stringbufferParent", buffer.toString());
		return nodes;
	}

	/**
	 * 找出所有的监测器类型
	 * 
	 * @return
	 */
	private Set<String> findAllMonitorType() {
		Set<String> groupid = new HashSet<String>();
		List<INode> nodes = getNodeids();
		for (INode node : nodes) {
			groupid.add(ChartUtil.getView().getMonitorInfo(node).getMonitorType());
		}
		return groupid;
	}

	/**
	 * 根据id找出所对应的监测器类型
	 * 
	 * @return
	 */
	private String findMonitorTypeById(INode node) {
		return ChartUtil.getView().getMonitorInfo(node).getMonitorType();
	}

	/**
	 * 生成分好类别的Report
	 * 
	 * @return
	 * @throws Exception
	 */
	private List<ReportDate> getReportList() throws Exception {
		List<ReportDate> reports = new ArrayList<ReportDate>();
		Set<String> types = findAllMonitorType();
		List<INode> nodes = getNodeids();
		for (String fmt : types) {
			StringBuilder sb = new StringBuilder();
			for (INode node : nodes) {
				if (fmt.equals(findMonitorTypeById(node))) {
					sb.append(node.getSvId()).append(",");
				}
			}
			ReportDate r = new ReportDate(starttime.getValue(), endtime.getValue());
			r.getReportDate(sb.toString());
			
			reports.add(r);
		}
		return reports;
	}
	
	public void onClick$seachButton(Event event) throws InterruptedException {
		setExport(true);
		System.out.println("错误报表导出开始..............................");
		if (getNodeids() == null || getNodeids().size() == 0) {
			Messagebox.show(ErrorMessage.UNSELECT_MONITOR, "提示", Messagebox.OK,
					Messagebox.INFORMATION);
			ChartUtil.clearComponent(maptable);
			ChartUtil.clearListbox(runtimeState);
			return;
		}
		if(starttime.getValue().after(endtime.getValue())){
			Messagebox.show(ErrorMessage.TIME_ERROR, "提示", Messagebox.OK,
					Messagebox.INFORMATION);
			ChartUtil.clearComponent(maptable);
			ChartUtil.clearListbox(runtimeState);
			return;
		}
		if(ChartUtil.isFutureTime(starttime.getValue()) && ChartUtil.isFutureTime(endtime.getValue())){
			ChartUtil.clearComponent(maptable);
			ChartUtil.clearListbox(runtimeState);
			return;
		}
		try {
			//导出报表的修改2011-6-9 xiaoqingfeng
			List<INode> nodes = getNodeids();
			System.out.println("错误报表导出开始获取nodes.............................."+nodes);
			List<ReportDate> rdList = this.getReportList();
			System.out.println("错误报表导出开始获取ReportDate.............................."+rdList);
			SelectErrorExl SelectErrorExl=new SelectErrorExl(rdList, nodes, starttime.getValue(), endtime.getValue());
			System.out.println("错误报表导出结束..............................");
			Messagebox.show("查询且导出数据结束", "提示", Messagebox.OK,
					Messagebox.INFORMATION);
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//将获取数据的过程放到线程中去
	
	}

	public void onClick$exportButton(Event event) throws Exception {
		try{
			if(!isExport()){
				Messagebox.show("没有要显示的数据！", "提示", Messagebox.OK,
						Messagebox.INFORMATION);
				return;
			}
			if (getNodeids() == null || getNodeids().size() == 0) {
				Messagebox.show(ErrorMessage.UNSELECT_MONITOR, "提示", Messagebox.OK,
						Messagebox.INFORMATION);
				return;
			}
			if(starttime.getValue().after(endtime.getValue())){
				Messagebox.show(ErrorMessage.TIME_ERROR, "提示", Messagebox.OK,
						Messagebox.INFORMATION);
				ChartUtil.clearComponent(maptable);
				return;
			}
			final Window win = (Window) Executions.createComponents(
					"/main/report/errorcomparereport/exportcomparereport.zul", null,
					null);
			win.setAttribute("report", getReportList());
			win.setAttribute("nodes", getNodeids());
			win.setClosable(true);
			Executions.getCurrent().getDesktop().getSession().setAttribute(
					"THEWINDOW", win);
			win.doModal();
			this.init();
		}catch(Exception ee){
			ee.printStackTrace();
		}

	}
	private void checkSelectItems() throws Exception {
		Listbox listbox = this.runtimeState;
		for (Object obj : listbox.getItems()) {
			if (obj instanceof Listitem) {
				Listitem item = (Listitem) obj;
				if (item.isSelected()) {
					return;
				}
			}
		}
		Messagebox.show("请选择好你要操作的对象", "错误", Messagebox.OK, Messagebox.ERROR);

	}
	//批量删除
	public void onClick$deleStatisticButton(Event event) throws Exception {
		try{
			checkSelectItems();
			Listbox listbox = this.runtimeState;
			for (Object obj : listbox.getItems()) {
				if (obj instanceof Listitem) {
					Listitem item = (Listitem) obj;
					if (item.isSelected()) {
						ErrorLogsBean errorLogsBean = (ErrorLogsBean) item.getValue();
						if (errorLogsBean != null) {
							ReportDataDaoImpl dao=new ReportDataDaoImpl();
							dao.deleteValue(errorLogsBean);
							Zip.deleteFile("ErrorReport/" + errorLogsBean.getName());
							System.out.println("批量删除");
						}
						
					}
				}
			}
			init();
		}catch(Exception ee){
			ee.printStackTrace();
		}

	}
	//批量下载batchdownButton
	public void onClick$batchdownButton(Event event) throws Exception {
		try{
			checkSelectItems();
			Listbox listbox = this.runtimeState;
			List<File> listUrl=new ArrayList<File>();
			String contextPath=Executions.getCurrent().getDesktop().getWebApp().getRealPath("/"); 
			for (Object obj : listbox.getItems()) {
				if (obj instanceof Listitem) {
					Listitem item = (Listitem) obj;
					if (item.isSelected()) {
						ErrorLogsBean errorLogsBean = (ErrorLogsBean) item.getValue();
						if (errorLogsBean != null) {
							ReportDataDaoImpl dao=new ReportDataDaoImpl();
							String url=dao.selectValue(errorLogsBean);
							File file = new File(contextPath+url); 
							listUrl.add(file);
						}
						
					}
				}
			}
			File[] file1 = new File[listUrl.size()];
			if(listUrl.size()>0){
				for(int i=0;i<listUrl.size();i++){
					listUrl.toArray(file1);
				}
			}
			String filename="ErrorDate.rar";
			Zip.PagFile(filename, file1);
			File file=new File(contextPath+filename);
			if(file.exists()){
				Filedownload.save("/" + filename, null);
			}
//			删除ZIP
//			else {
//				Zip.deleteFile(contextPath+filename);
//			}
			init();
		}catch(Exception ee){
			ee.printStackTrace();
		}

	}
	
	/**
	 * 
	 * @param event
	 */
	public void onCreate$compareReport(Event event) throws Exception{
		Calendar calStart = Calendar.getInstance();
		calStart.setTime(new Date());
		calStart.add(Calendar.HOUR, -24);
		starttime.setValue(calStart.getTime());
		endtime.setValue(new Date());
		init();
	}


	/**
	 * add by di.tang 20090526 获得同类型Report中的Detail信息，以便构建XYDataset
	 * 
	 * @param reports
	 * @param index
	 * @return
	 */
	private List<CompareDataBean> xydatasetCreate(
			ReportDate rd, int index) {
		List<CompareDataBean> retlist = new ArrayList<CompareDataBean>();
		
		for (String id : rd.getNodeidsArray()) {
			Map<Date, String> imgdata = rd.getReturnValueDetail(id,
					index);
			String name = rd.getReturnValue(id, "MonitorName", index);
			if (name == null)
				name = "";
			CompareDataBean data = new CompareDataBean(id,name,imgdata);
			retlist.add(data);
		}
		return retlist;
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
	/**
	 * add 20090526 by di.tang 构建chart集合
	 * 
	 * @throws Exception
	 */
	private Div buildImageMaps(List<ReportDate> rdList) throws Exception {
		int width = getScreenWidth()-getTreeSize();
		Div tmpDiv = new Div();
		for (ReportDate rd : rdList) {
			Map<Integer, Map<String, String>> imageList = ReportServices.getImagelist(rd);
			for (int key : imageList.keySet()) {
				List<CompareDataBean> imgdatas = this
						.xydatasetCreate(rd, key);
				XYDataset data = buildDataset(imgdatas);
				Map<String, String> keyvalue = imageList.get(key);
				Image temmap = null;
				Panel panel = null;
				if (keyvalue.get("title").contains("%")) {
					temmap = ChartUtil.createBufferedImage(keyvalue	.get("title"), 
							keyvalue.get("subtitle"), keyvalue.get("title"), data, 10
							,100,starttime.getValue(), endtime.getValue(),0, true, width, 300,ChartUtil.REPORTTYPE_DAYREPORT);
					panel = createListbox(rd, key, keyvalue.get("title"));
				} else {
					double maxvalue = Double.parseDouble(keyvalue.get("maxvalue"));
					double minvalue = Double.parseDouble(keyvalue.get("minvalue"));
					maxvalue = maxvalue * 1.1;
					if (maxvalue < 1) {
						maxvalue = 1;
					}
					if (keyvalue.get("title").contains("ms")) {
						temmap = ChartUtil.createBufferedImage(keyvalue.get("title"), keyvalue.get("subtitle"),
								keyvalue.get("title"), data, 20, maxvalue,starttime.getValue(), endtime.getValue(),minvalue, true, width,
								300, ChartUtil.REPORTTYPE_DAYREPORT);
						panel = createListbox(rd, key, keyvalue.get("title"));
					} else {
						temmap = ChartUtil.createBufferedImage(keyvalue.get("title"), keyvalue.get("subtitle"),
								keyvalue.get("title"), data, 20, maxvalue,starttime.getValue(), endtime.getValue(), minvalue, true, width,
								300, ChartUtil.REPORTTYPE_DAYREPORT);
						panel = createListbox(rd, key, keyvalue.get("title"));
					}
				}
				tmpDiv.appendChild(panel);
				temmap.setWidth("98%");
//				temmap.setHeight("100%");
				tmpDiv.appendChild(temmap);
			}
		}
		return tmpDiv;
	}

	/**
	 * 构建数据
	 * 
	 * @param imgdata
	 * @return
	 */
	public XYDataset buildDataset(List<CompareDataBean> data) {
		TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
		int i = 1;
		for (CompareDataBean bean : data) {
			TimeSeries timeseries = new TimeSeries(bean.getName(),
					org.jfree.data.time.Second.class);
			i++;
			Map<Date, String> imgdata = bean.getData();
			for (Date date1 : imgdata.keySet()) {
				int ss = date1.getSeconds();
				int mm = date1.getMinutes();
				int hh = date1.getHours();
				int d = date1.getDate();
				int m = date1.getMonth() + 1;
				int y = date1.getYear() + 1900;

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
			timeseriescollection.addSeries(timeseries);
		}
		return timeseriescollection;
	}
	
	public Panel createListbox(ReportDate rd, int index, String title) {
		Panel panel = new Panel();
		panel.setCollapsible(true);
		panel.setTitle(title);
		panel.setWidth("98%");
		Panelchildren children = new Panelchildren();
		children.setParent(panel);
		Listbox box = new Listbox();
		box.setFixedLayout(true);
		box.setParent(children);
		box.setWidth("100%");
		ChartUtil.addListhead(box, "名称","最大值","平均值","最小值","最大值时间");
		for (String id : rd.getNodeidsArray()) {
			ChartUtil.addRow(box, "", rd.getReturnValue(id,"MonitorName", index),
					rd.getReturnValue(id,"max", index),rd.getReturnValue(id,"average", index),
					rd.getReturnValue(id,"min", index),rd.getReturnValue(id,"when_max", index));
		}
		return panel;
	}
	
	public boolean isExport() {
		return export;
	}

	public void setExport(boolean export) {
		this.export = export;
	}
	
	
	public void init() throws Exception {
		ErrorLogsBean[] result = Result();
		runtimeState.getItems().clear();
		for (ErrorLogsBean errorLogsBean : result) {
			Listitem item = BaseTools.setRow(runtimeState, errorLogsBean, errorLogsBean.getType(),errorLogsBean.getTitle(),errorLogsBean.getData(),errorLogsBean.getTime());
		}
	}
	private ErrorLogsBean[] Result() throws Exception{
		List<ErrorLogsBean> errorLogsbeans = new ArrayList<ErrorLogsBean>();
		ReportDataDaoImpl dao=new ReportDataDaoImpl();
		errorLogsbeans = dao.getValue();
		return (errorLogsbeans.toArray(new ErrorLogsBean[errorLogsbeans.size()]));
	}
}
