package com.siteview.ecc.monitorbrower;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.report.common.ErrorMessage;
import com.siteview.ecc.treeview.EccTreeModel;
import com.siteview.ecc.util.Toolkit;

public class MonitorBrowseComposer extends GenericForwardComposer {
	private View view = Toolkit.getToolkit().getSvdbView(
			Executions.getCurrent().getDesktop());
	private Listbox showMonitorFilter;
	private Listbox monitorInfo;
	private Panel showMonitorData;
	private Label bad;
	private Label nullspecial;
	private Label ok;
	private Label warn;
	private Label error;
	private Label forbid;
	private Button editFilter;
	private Button delete;
	private Button count;
	private int showMonitorCount = 50;

	/**
	 * 创建筛选条件
	 */
	public void onClick$filter(Event event) throws InterruptedException {
		try {
			final Window win = (Window) Executions.createComponents(
					"/main/monitorbrower/filterMonitor.zul", null, null);
			win.setAttribute("listbox", showMonitorFilter);// 筛选条件
			win.setAttribute("monitorImfo", this);
			win.setMaximizable(false);
			win.setClosable(true);
			win.doModal();
			onCreate$showMonitorFilter(event);
			refreshMonitorListbox();
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
					Messagebox.ERROR);
			e.printStackTrace();
		}
	}

	public void onCreate$monitorBrower(Event e) throws Exception {
		onCreate$showMonitorFilter(e);
	}

	// 窗口加载
	public void onCreate$showMonitorFilter(Event event)
			throws InterruptedException {
		try {
			CVBean cvb = (CVBean) Executions.getCurrent().getDesktop()
					.getSession().getAttribute("monitor_browse_item");

			ChartUtil.clearListbox(showMonitorFilter);
			Listitem selectedItem = null;
			for (CVBean bk : CustomViewSetting.getCustomView()) {
				Listitem item = ChartUtil.addRow(showMonitorFilter, bk, bk
						.getTitile(), bk.getGroupName(), bk.getEntityName(), bk
						.getMonitorName(), bk.getMonitorTypeName(), bk
						.getShowHideName(), bk.getMonitorDescripe(), bk
						.getSortName());
				if (cvb != null && cvb.getCvName().equals(bk.getCvName())) {
					item.setSelected(true);
					selectedItem = item;
				}
			}
			showMonitorFilter.getPagingChild().setMold("os");

			if (selectedItem == null)
				return;
			int index = showMonitorFilter.getIndexOfItem(selectedItem);
			showMonitorFilter.setActivePage(index
					/ showMonitorFilter.getPageSize());
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "提示", Messagebox.OK,
					Messagebox.ERROR);
			e.printStackTrace();
		}
	}

	// 选择筛选条件触发事件
	public void onSelect$showMonitorFilter(Event event)
			throws InterruptedException {
		try {
			Listitem item = showMonitorFilter.getSelectedItem();
			if (item == null)
				return;
			CVBean bean = (CVBean) item.getValue();
			if (bean.getNodeId().equals("CV111")
					|| bean.getNodeId().equals("CV222")) {
				editFilter.setDisabled(true);
				delete.setDisabled(true);
				count.setVisible(true);
			} else {
				editFilter.setDisabled(false);
				count.setVisible(false);
				delete.setDisabled(false);
			}
			showMonitorData.setTitle(bean.getTitile());
			refreshMonitorListbox();
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "提示", Messagebox.OK,
					Messagebox.ERROR);
			e.printStackTrace();
		}
	}

	public void setButtonNormal() {
		editFilter.setDisabled(false);
		count.setVisible(false);
		delete.setDisabled(false);
	}

	public void onClick$refresh(Event e) throws Exception {
		onCreate$showMonitorFilter(e);
	}

	private void refreshMonitorListbox() throws InterruptedException {
		try {
			final Window win = (Window) Executions.createComponents(
					"/main/monitorbrower/progress.zul", null, null);
			win.setAttribute("monitorImfo", this);
			win.setMaximizable(false);
			win.setClosable(true);
			win.doModal();
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
					Messagebox.ERROR);
		}
	}

	public void onClick$delete(Event event) throws InterruptedException {
		try {
			Listitem item = showMonitorFilter.getSelectedItem();
			if (item == null) {
				Messagebox.show("请选择要删除的选项！", "提示", Messagebox.YES,
						Messagebox.INFORMATION);
				return;
			}
			int ret = Messagebox.show("你确认要删除选中的记录吗？", "询问", Messagebox.OK
					| Messagebox.CANCEL, Messagebox.QUESTION);
			if (ret == Messagebox.CANCEL)
				return;
			CVBean bean = (CVBean) item.getValue();
			if (!bean.getNodeId().equals("CV111")
					&& !bean.getNodeId().equals("CV222")
					&& !bean.getNodeId().equals("CV333")
					&& !bean.getNodeId().equals("CV444")) {
				CustomViewSetting.deleteCustomView(bean.getNodeId());

				onCreate$showMonitorFilter(event);
				// addlog
				String loginname = view.getLoginName();
				String minfo = loginname + " " + "在"
						+ OpObjectId.monitor_browser.name + "中进行了  "
						+ OpTypeId.del.name + "操作，删除筛选条件: " + item.getLabel();
				AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del,
						OpObjectId.monitor_browser);
			}
			Listitem lastItem = ChartUtil.getLastListitem(showMonitorFilter);
			if (lastItem != null) {
				lastItem.setSelected(true);
				CVBean tmpBean = (CVBean) lastItem.getValue();
				this.showMonitorData.setTitle(tmpBean.getTitile());
				refreshMonitorListbox();

				if (tmpBean.getNodeId().equals("CV111")
						|| tmpBean.getNodeId().equals("CV222")) {
					editFilter.setDisabled(true);
					delete.setDisabled(true);
					count.setVisible(true);
				} else {
					editFilter.setDisabled(false);
					count.setVisible(false);
					delete.setDisabled(false);
				}
			}
		} catch (InterruptedException e) {
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
					Messagebox.ERROR);
			e.printStackTrace();
		}

	}

	/**
	 * 编辑筛选条件
	 */
	public void onClick$editFilter(Event event) throws Exception {
		try {
			Listitem item = showMonitorFilter.getSelectedItem();
			if (item == null)
				return;
			CVBean bean = (CVBean) item.getValue();
			final Window win = (Window) Executions.createComponents(
					"/main/monitorbrower/editFilterMonitor.zul", null, null);
			win.setAttribute("listbox", showMonitorFilter);
			win.setAttribute("filterCondition", bean);
			win.setAttribute("monitorImfo", this);
			win.setMaximizable(false);
			win.setClosable(true);
			win.doModal();
			onCreate$showMonitorFilter(event);
			refreshMonitorListbox();
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
					Messagebox.ERROR);
			e.printStackTrace();
		}
	}

	public void onClick$count(Event event) throws InterruptedException {
		try {
			int showMonitorCount_old = this.showMonitorCount;
			Listitem item = showMonitorFilter.getSelectedItem();
			if (item == null)
				return;
			CVBean bean = (CVBean) item.getValue();
			StringBuffer sb = new StringBuffer("0");
			final Window win = (Window) Executions.createComponents(
					"/main/monitorbrower/showMonitorCount.zul", null, null);
			win.setAttribute("count", this);
			win.setMaximizable(false);
			win.setClosable(true);
			win.doModal();
			int showMonitorCount_new = this.showMonitorCount;
			if(showMonitorCount_old != showMonitorCount_new){
				refreshMonitorListbox();
			}
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
					Messagebox.ERROR);
			e.printStackTrace();
		}
	}

	public void onClick$excel(Event event) throws Exception {
		try {
			Listitem item = showMonitorFilter.getSelectedItem();
			if (item == null) {
				Messagebox.show("请选择要生成Excle的选项！", "提示", Messagebox.YES,
						Messagebox.INFORMATION);
				return;
			}
			InputStream inputstream = null;
			ByteArrayOutputStream baos = null;
			WritableWorkbook book = null;
			baos = new ByteArrayOutputStream();
			book = Workbook.createWorkbook(baos);
			WritableSheet sheet = book.createSheet("Sheet_1", 0);
			String[] headers = { "状态", "组", "设备", "名称", "更新时间", "描述" };
			jxl.write.WritableFont wfc = new jxl.write.WritableFont(
					WritableFont.ARIAL, 12, WritableFont.BOLD, false,
					UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.RED);
			jxl.write.WritableCellFormat wcfFC = new jxl.write.WritableCellFormat(
					wfc);
			jxl.write.WritableFont wfc1 = new jxl.write.WritableFont(
					WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
					UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.GREEN);
			jxl.write.WritableCellFormat wcfFC1 = new jxl.write.WritableCellFormat(
					wfc1);
			jxl.write.WritableFont wfc2 = new jxl.write.WritableFont(
					WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
					UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);
			jxl.write.WritableCellFormat wcfFC2 = new jxl.write.WritableCellFormat(
					wfc2);
			sheet.setColumnView(0, 5);
			sheet.setColumnView(1, 20);
			sheet.setColumnView(2, 30);
			sheet.setColumnView(3, 60);
			sheet.setColumnView(4, 20);
			sheet.setColumnView(5, 80);
			for (int i = 0; i < headers.length; i++) {
				jxl.write.Label label = new jxl.write.Label(i, 0, headers[i],
						wcfFC);
				sheet.addCell(label);
			}
			Tree tree = (org.zkoss.zul.Tree) Executions.getCurrent()
					.getDesktop().getPage("eccmain").getFellow("tree");
			EccTreeModel model = (EccTreeModel) tree.getModel();
			MonitorDaomImpl info = new MonitorDaomImpl(model, view);
			List<?> list = info.getMonitorBean();
			System.out.println(list.size());
			Iterator<?> iterator = list.iterator();
			int row = 0;
			while (iterator.hasNext()) {
				MonitorBean listValue = (MonitorBean) iterator.next();
				row++;
				if ((row + 1) % 2 == 0) {
					jxl.write.Label label0 = new jxl.write.Label(0, row,
							listValue.getStatus(), wcfFC2);
					jxl.write.Label label1 = new jxl.write.Label(1, row,
							listValue.getGroup(), wcfFC2);
					jxl.write.Label label2 = new jxl.write.Label(2, row,
							listValue.getEntity(), wcfFC2);
					jxl.write.Label label3 = new jxl.write.Label(3, row,
							listValue.getMonitorName(), wcfFC2);
					jxl.write.Label label4 = new jxl.write.Label(4, row,
							listValue.getUpdateTime(), wcfFC2);
					jxl.write.Label label5 = new jxl.write.Label(5, row,
							listValue.getDescript(), wcfFC2);
					sheet.addCell(label0);
					sheet.addCell(label1);
					sheet.addCell(label2);
					sheet.addCell(label3);
					sheet.addCell(label4);
					sheet.addCell(label5);
				} else {
					jxl.write.Label label0 = new jxl.write.Label(0, row,
							listValue.getStatus(), wcfFC2);
					jxl.write.Label label1 = new jxl.write.Label(1, row,
							listValue.getGroup(), wcfFC2);
					jxl.write.Label label2 = new jxl.write.Label(2, row,
							listValue.getEntity(), wcfFC2);
					jxl.write.Label label3 = new jxl.write.Label(3, row,
							listValue.getMonitorName(), wcfFC2);
					jxl.write.Label label4 = new jxl.write.Label(4, row,
							listValue.getUpdateTime(), wcfFC2);
					jxl.write.Label label5 = new jxl.write.Label(5, row,
							listValue.getDescript(), wcfFC2);
					sheet.addCell(label0);
					sheet.addCell(label1);
					sheet.addCell(label2);
					sheet.addCell(label3);
					sheet.addCell(label4);
					sheet.addCell(label5);
				}
			}
			book.write();
			baos.close();
			book.close();
			inputstream = new ByteArrayInputStream(baos.toByteArray());
			Filedownload.save(inputstream, "application/vnd.ms-excel", item
					.getLabel()
					+ ".xls");
			inputstream.close();
		} catch (Exception e) {
			Messagebox.show(ErrorMessage.EXPORT_ERROR
					+ this.getClass().getName() + e.getMessage(), "错误",
					Messagebox.OK, Messagebox.ERROR);
		}
	}

	public int getShowMonitorCount() {
		return showMonitorCount;
	}

	public void setShowMonitorCount(int showMonitorCount) {
		this.showMonitorCount = showMonitorCount;
	}

	public Listbox getShowMonitorFilter() {
		return showMonitorFilter;
	}

	public void setShowMonitorFilter(Listbox showMonitorFilter) {
		this.showMonitorFilter = showMonitorFilter;
	}

	public Listbox getMonitorInfo() {
		return monitorInfo;
	}

	public void setMonitorInfo(Listbox monitorInfo) {
		this.monitorInfo = monitorInfo;
	}

	
	
	public Label getOk() {
		return ok;
	}

	public void setOk(Label ok) {
		this.ok = ok;
	}

	public Label getWarn() {
		return warn;
	}

	public void setWarn(Label warn) {
		this.warn = warn;
	}

	public Label getError() {
		return error;
	}

	public void setError(Label error) {
		this.error = error;
	}

	public Label getForbid() {
		return forbid;
	}

	public void setForbid(Label forbid) {
		this.forbid = forbid;
	}

	public Label getBad() {
		return bad;
	}

	public void setBad(Label bad) {
		this.bad = bad;
	}

	public Label getNull() {
		return nullspecial;
	}

	public void setNull(Label nullspecial) {
		this.nullspecial = nullspecial;
	}
	
	
	public Button getCount() {
		return count;
	}

	public void setCount(Button count) {
		this.count = count;
	}



	
	public Panel getShowMonitorData() {
		return showMonitorData;
	}

	public void setShowMonitorData(Panel showMonitorData) {
		this.showMonitorData = showMonitorData;
	}
}
