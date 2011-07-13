package com.siteview.ecc.report;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.log4j.Logger;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.siteview.base.data.Report;
import com.siteview.base.data.UserRightId;
import com.siteview.base.data.Report.DstrItem;
import com.siteview.base.manage.View;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.alert.SelectTree;
import com.siteview.ecc.alert.dao.type.MonitorType;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.report.beans.StateBean;
import com.siteview.ecc.report.beans.StateItem;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.report.common.ErrorMessage;
import com.siteview.ecc.report.common.GroupLinkListener;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.util.LinkCheck;
import com.siteview.ecc.util.Toolkit;

public class MonitorImfoOfState extends Window {

	private static final long serialVersionUID = 7107345094728889982L;
	private static final Calendar calendar = Calendar.getInstance();
	private static final Logger logger = Logger
			.getLogger(MonitorImfoOfState.class);
	private static final int BEGIN = 0;
	private static final int END = 1;
	private String monitorId = "";

	public void onCreate() throws Exception {
		try {
			init();
			getQueryBtn().addEventListener(Events.ON_CLICK,
					new EventListener() {
						@Override
						public void onEvent(Event arg0) throws Exception {
							createStateGrid();
						}
					});
			getSelectTree().addEventListener(Events.ON_SELECT,
					new EventListener() {

						@Override
						public void onEvent(Event arg0) throws Exception {
							createStateGrid();
						}
					});
			getExportReportBtn().addEventListener(Events.ON_CLICK,
					new ExportListener(this));
			this.getViewNamecombobox().addEventListener(Events.ON_CHANGE, new EventListener(){
				@Override
				public void onEvent(Event event) throws Exception {
					getSelectTree().setViewName(getViewNamecombobox().getValue());
				}});
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
					Messagebox.ERROR);
		}
	}
	public void onCreate$groupLink(Event event){
		boolean isLink = LinkCheck.getLinkCheck().CanSeeLink(UserRightId.WholeView);
		if(isLink){
			String style = "color:#18599C;cursor:pointer;text-decoration: underline;";
			getGroupLink().setStyle(style);
		}
	}
	public void onClick$groupLink(Event e){
		Comboitem item = getViewNamecombobox().getSelectedItem();
		if(item!=null){
			getGroupLink().addEventListener(Events.ON_CLICK,new GroupLinkListener(item.getLabel()));
		}
	}
	/**
	 * 用了画光谱图的数据
	 */
	private List<Color> colorlist = new ArrayList<Color>();
	private StateBean stateBean = null;//用来画统计图的数据
	private List<StateItem> stateItems = new ArrayList<StateItem>();//状态持续列表数据
	
	private void createStateGrid() throws Exception {
		try{
			View view = Toolkit.getToolkit().getSvdbView(getDesktop());
			INode node = this.getSelectedNode();
			if (node == null) {
				Messagebox.show(ErrorMessage.UNSELECT_MONITOR, "提示", Messagebox.OK,
						Messagebox.INFORMATION);
				return;
			}
			if (getBeginDatabox().getValue().after(getEndDatabox().getValue())) {
				Messagebox.show(ErrorMessage.TIME_ERROR, "提示", Messagebox.OK,
						Messagebox.INFORMATION);
				return;
			}
			Date begin_date = getBeginDatabox().getValue();
			Date end_date = getEndDatabox().getValue();
			getBeginLabel().setValue(Toolkit.getToolkit().formatDate(begin_date));
			getEndLabel().setValue(Toolkit.getToolkit().formatDate(end_date));
			Report report = new Report(node, begin_date, end_date);
			report.load();
	
			Map<Date, DstrItem> dstrMap = report.getDstr();
			if (dstrMap == null || dstrMap.size()==0){
				this.getBeginLabel().setValue("");
				this.getEndLabel().setValue("");
				ChartUtil.clearComponent(getStatePercentRows());
				getStatusImage().setSrc("");
				getSpectrumImage().setSrc("");
				ChartUtil.clearComponent(getStatePeriodRow());
//				Messagebox.show("没有您要显示的数据！", "提示", Messagebox.OK,
//						Messagebox.INFORMATION);
				return;
			}
			int ok = 0, warn = 0, error = 0, disable = 0, bad = 0;
			double size = dstrMap.size();// 提高精度
			if(colorlist.size()>0) colorlist.clear();
			if(stateItems.size()>0) stateItems.clear();
			for (Date keyDate : dstrMap.keySet()) {
				DstrItem dstrItem = dstrMap.get(keyDate);
				if ("ok".equals(dstrItem.status)) {
					ok++;
					colorlist.add(MonitorType.getColor(MonitorType.OK));
				} else if ("warning".equals(dstrItem.status)) {
					warn++;
					colorlist.add(MonitorType.getColor(MonitorType.WARN));
				} else if ("error".equals(dstrItem.status)) {
					error++;
					colorlist.add(MonitorType.getColor(MonitorType.ERROR));
				} else if ("disable".equals(dstrItem.status)) {
					disable++;
					colorlist.add(MonitorType.getColor(MonitorType.DISABLE));
				} else {
					bad++;
					colorlist.add(MonitorType.getColor(MonitorType.BAD));
				}
			}
			stateBean = new StateBean(node.getSvId(),node.getName(),Toolkit.getToolkit().formatDate(begin_date)
					,Toolkit.getToolkit().formatDate(end_date),ok / size * 100,warn / size * 100, error / size * 100
					,disable / size * 100,bad / size * 100);
			Row row = addRow(new Row(), stateBean, stateBean.getPercentOk(),stateBean.getPercentWarn(),stateBean.getPercentError()
					,stateBean.getPercentDisable(),stateBean.getPercentBad());
			ChartUtil.clearComponent(getStatePercentRows());
			getStatePercentRows().appendChild(row);
			getDesktop().getSession().setAttribute("colorlist", colorlist);
			getSpectrumImage().setSrc("/main/report/SpectrumImage?Id" + new Random().nextDouble());
			PieDataset dataSet = createDataset(stateBean);
			Image image = ChartUtil.create3DPieChart(node.getName(), dataSet, 600,300);
			getStatusImage().setSrc(image.getSrc());
	
			int index = 0;
			List<Date> aliasKey = new ArrayList<Date>(dstrMap.keySet());
			ChartUtil.clearComponent(getStatePeriodRow());
			while (index < size) {
				Date startTime = null;
				Date endTime = null;
				String status = null;
				int count = 0;
				for (; index < size; index++) {
					DstrItem dstrItem = dstrMap.get(aliasKey.get(index));
//					if (!"ok".equals(dstrItem.status) && !"error".equals(dstrItem.status))	continue;
					if (status == null)	status = dstrItem.status;
					if (!status.equals(dstrItem.status)) break;
					if (startTime == null) startTime = aliasKey.get(index);
					count++;
					endTime = aliasKey.get(index);
				}
				if (startTime == null || endTime == null) {
					Row blankRow = new Row();
					blankRow.appendChild(new Label("无数据！"));
					getStatePeriodRow().appendChild(blankRow);
					continue;
				}
				String strTime = subtract2Date(startTime,endTime);
				StateItem sitem = new StateItem(Toolkit.getToolkit().formatDate(startTime),status,String.valueOf(count),strTime);
				stateItems.add(sitem);
				Row generatetime = addRow(new Row(), null, sitem.getBeginTime(), ChartUtil.getImage(sitem.getStatus()), sitem.getCount(),sitem.getPersistTime());
				getStatePeriodRow().appendChild(generatetime);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private Row addRow(Row row, Object value, Object... cellValue) {
		for (Object obj : cellValue) {
			if ((obj instanceof String) == true) {
				Label label = new Label();
				label.setValue((String) obj);
				label.setParent(row);
			} else if ((obj instanceof Component) == true) {
				((Component) obj).setParent(row);
			}
		}
		return row;
	}

	public INode getSelectedNode() {
		Treeitem item = getSelectTree().getSelectedItem();
		if (item == null) return null;
		EccTreeItem itemNode = (EccTreeItem) item.getValue();
		INode node = itemNode.getValue();
		if (!node.getType().equals(INode.MONITOR))	return null;
		return node;
	}

	public void init() {
		calendar.setTime(new Date());
		getEndDatabox().setValue(calendar.getTime());
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		getBeginDatabox().setValue(calendar.getTime());
	}

	private String getInitFrequecy(INode node){
		View view = Toolkit.getToolkit().getSvdbView(getDesktop());
		MonitorInfo info = view.getMonitorInfo(node);
		MonitorTemplate tmplate = info.getMonitorTemplate();
		String initFreq = "";
		for (Map<String, String> keyMap : tmplate.get_Parameter_Items()) {
			if ("监测频率".equals(keyMap.get("sv_label"))) {
				initFreq = keyMap.get("sv_value") + "分钟";
				break;
			}
		}
		return initFreq;
	}
	/**
	 * 结束时间减去开始时间得到的时间差的字符串表示形式
	 * 
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public String subtract2Date(Date beginDate, Date endDate) {
		long l = endDate.getTime() - beginDate.getTime();
		if (l == 0)
			return getInitFrequecy(getSelectedNode());
		StringBuilder sb = new StringBuilder();
		long day = l / (24 * 60 * 60 * 1000);
		if (day > 0)
			sb.append(day).append("天");
		long hour = (l / (60 * 60 * 1000) - day * 24);
		if (hour > 0)
			sb.append(hour).append("小时");
		long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
		if (min > 0)
			sb.append(min).append("分钟");
		long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		if (s > 0)
			sb.append(s).append("秒");
		if("".equals(sb.toString())){
			sb.append(this.getInitFrequecy(getSelectedNode()));
		}
		return sb.toString();

	}

	private static PieDataset createDataset(StateBean sb) {
		DefaultPieDataset localDefaultPieDataset = new DefaultPieDataset();
		localDefaultPieDataset.setValue("正常", sb.getOk());
		localDefaultPieDataset.setValue("危险", sb.getWarn());
		localDefaultPieDataset.setValue("错误", sb.getError());
		localDefaultPieDataset.setValue("禁止", sb.getDisable());
		localDefaultPieDataset.setValue("无监测数据", sb.getBad());
		return localDefaultPieDataset;
	}
	public Label getGroupLink(){
		return (Label)BaseTools.getComponentById(this, "groupLink");
	}
	public Combobox getViewNamecombobox(){
		return (Combobox)BaseTools.getComponentById(this, "viewNamecombobox");
	}
	public Button getExportReportBtn() {
		return (Button) BaseTools.getComponentById(this, "exportButton");
	}

	public Label getBeginLabel() {
		return (Label) BaseTools.getComponentById(this, "begin_date_label");
	}

	public Label getEndLabel() {
		return (Label) BaseTools.getComponentById(this, "end_date_label");
	}

	public Button getQueryBtn() {
		return (Button) BaseTools.getComponentById(this, "query");
	}

	public Rows getStatePercentRows() {
		return (Rows) BaseTools.getComponentById(this, "state_statistic_row");
	}

	public Grid getStatePercentGrid() {
		return (Grid) BaseTools.getComponentById(this, "state_statistic");
	}

	public Rows getStatePeriodRow() {
		return (Rows) BaseTools.getComponentById(this, "state_generate_time");
	}

	public Datebox getBeginDatabox() {
		return (Datebox) BaseTools.getComponentById(this, "begin_date");
	}

	public Datebox getEndDatabox() {
		return (Datebox) BaseTools.getComponentById(this, "end_date");
	}

	public Image getSpectrumImage() {
		return (Image) BaseTools.getComponentById(this, "spectrumImage");
	}

	// statureport
	public Image getStatusImage() {
		return (Image) BaseTools.getComponentById(this, "statureport");
	}

	public SelectTree getSelectTree() {
		return (SelectTree) BaseTools.getComponentById(this, "tree");
	}
	public List<Color> getColorlist() {
		return colorlist;
	}

	public void setColorlist(List<Color> colorlist) {
		this.colorlist = colorlist;
	}

	public StateBean getStateBean() {
		return stateBean;
	}

	public void setStateBean(StateBean stateBean) {
		this.stateBean = stateBean;
	}
	
	public List<StateItem> getStateItems() {
		return stateItems;
	}

	public void setStateItems(List<StateItem> stateItems) {
		this.stateItems = stateItems;
	}
	class ExportListener implements EventListener {
		private MonitorImfoOfState view;

		public ExportListener(MonitorImfoOfState view) {
			this.view = view;
		}

		@Override
		public void onEvent(Event event) throws Exception {
			try{
				Date begin_date = view.getBeginDatabox().getValue();
				Date end_date = view.getEndDatabox().getValue();
				if (view.getSelectedNode()==null) {
					Messagebox.show(ErrorMessage.UNSELECT_MONITOR, "提示",
							Messagebox.OK, Messagebox.INFORMATION);
					return;
				}
				if (begin_date.after(end_date)) {
					Messagebox.show(ErrorMessage.TIME_ERROR, "提示", Messagebox.OK,
							Messagebox.INFORMATION);
					return;
				}
				Events.sendEvent(new Event(Events.ON_CLICK,view.getQueryBtn()));
				Window win = (Window) Executions.createComponents(
						"/main/report/export.zul", null, null);
				win.setAttribute("colorlist", view.getColorlist());
				win.setAttribute("stateBeanData", view.getStateBean());
				win.setAttribute("persistTimeData", view.getStateItems());
				win.setSizable(false);
				win.setClosable(true);
				win.doModal();
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}
}
