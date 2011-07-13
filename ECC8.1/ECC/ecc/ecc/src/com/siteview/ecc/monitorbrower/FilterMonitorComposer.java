package com.siteview.ecc.monitorbrower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.siteview.base.data.QueryInfo;
import com.siteview.base.manage.View;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.util.Toolkit;

public class FilterMonitorComposer extends GenericForwardComposer {
	private Window createFilter;
	private Textbox filterName;
	private Textbox monitorName;
	private Textbox groupName;
	private Textbox entityBe;
	private Textbox descript;
	private Listbox showAndHidden;
	private Listbox monitorType;
	private Listbox sort;
	private Listbox reflesh;
	private CVBean bean;
	private MonitorBrowseComposer composer;

	public FilterMonitorComposer() {
	}

	public void onClick$monitorNameBtn(Event event) throws InterruptedException {
		final Window win = (Window) Executions.createComponents(
				"/main/monitorbrower/monitorselecttree.zul", null, null);
		win.setAttribute("monitorLabel", monitorName);
		win.setMaximizable(false);
		win.setClosable(true);
		win.doModal();
	}

	public void onClick$monitorGroupBtn(Event event)
			throws InterruptedException {
		final Window win = (Window) Executions.createComponents(
				"/main/monitorbrower/goupselect.zul", null, null);
		win.setAttribute("groupLabel", groupName);
		win.setMaximizable(false);
		win.setClosable(true);
		win.doModal();
	}

	public void onClick$monitorEntiryBtn(Event event)
			throws InterruptedException {
		final Window win = (Window) Executions.createComponents(
				"/main/monitorbrower/entityselecttree.zul", null, null);
		win.setAttribute("entityLabel", entityBe);
		win.setMaximizable(false);
		win.setClosable(true);
		win.doModal();
	}

	public void onCreate$createFilter(Event event) throws Exception {
		try {
			composer = (MonitorBrowseComposer) createFilter
					.getAttribute("monitorImfo");
			Object obj = createFilter.getAttribute("filterCondition");
			if (obj != null) {
				CVBean bean = (CVBean) obj;
				this.bean = bean;
				if (!bean.getNodeId().equals("CV111")
						&& !bean.getNodeId().equals("CV222")
						&& !bean.getNodeId().equals("CV333")
						&& !bean.getNodeId().equals("CV444")
						&& !bean.getNodeId().equals("CV555")) {
					groupName.setValue(bean.getGroupName());
					filterName.setValue(bean.getTitile());
					monitorName.setValue(bean.getMonitorName());
					entityBe.setValue(bean.getEntityName());
					descript.setValue(bean.getMonitorDescripe());
					Map<String, String> typeInfo = queryMonitorTypeInfo();
					Listitem itm = new Listitem();
					itm.setId("99999");
					Listcell cell = new Listcell("所有类型");
					cell.setParent(itm);
					itm.setParent(this.monitorType);

					// 排序
					ArrayList<String> namelist = new ArrayList<String>();
					for (String key : typeInfo.keySet()) {
						namelist.add(typeInfo.get(key));
					}
					Object[] object = namelist.toArray();
					Arrays.sort(object);
					for (Object name : object) {
						String tempName = (String) name;
						for (String key : typeInfo.keySet()) {
							String keyValue = typeInfo.get(key);
							if (keyValue.equals(tempName)) {
								Listitem item = new Listitem();
								item.setId(key);
								Listcell c = new Listcell(typeInfo.get(key));
								item.appendChild(c);
								if (bean.getMonitorType().equals(key))
									item.setSelected(true);
								monitorType.appendChild(item);
							}
						}
					}

					/*
					 * for (String key : typeInfo.keySet()) { Listitem item =
					 * new Listitem(); item.setId(key); Listcell c = new
					 * Listcell(typeInfo.get(key)); item.appendChild(c);
					 * if(bean.getMonitorType().equals(key))
					 * item.setSelected(true); monitorType.appendChild(item); }
					 * <<<<<<< .mine Listheader h; =======
					 */
					for (Object item : showAndHidden.getChildren()) {
						if (item instanceof Listitem) {
							Listitem itmm = (Listitem) item;
							if (itmm.getId().equals(bean.getMonitorState())) {
								itmm.setSelected(true);
							}
						}
					}

					for (Object item : sort.getChildren()) {
						if (item instanceof Listitem) {
							Listitem itmm = (Listitem) item;
							if (itmm.getId().equals(bean.getSort())) {
								itmm.setSelected(true);
							}
						}
					}
				} else {

				}
			}
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "提示", Messagebox.OK,
					Messagebox.ERROR);
			e.printStackTrace();
		}
	}

	public void onClick$OK(Event event) throws Exception {
		try {
			String title = this.filterName.getValue().trim();
			if (title.isEmpty()) {
				Messagebox.show("报告标题不能为空,请重新输入!", "提示", Messagebox.OK,
						Messagebox.INFORMATION);
				filterName.focus();
				return;
			}
			String monitorName = this.monitorName.getValue().trim();
			String goupName = this.groupName.getValue().trim();
			String entityName = this.entityBe.getValue().trim();
			String descript = this.descript.getValue().trim();

			String showAndHidden = this.showAndHidden.getSelectedItem()
					.getLabel();
			String monitorState = this.showAndHidden.getSelectedItem().getId();

			Listitem typeitem = monitorType.getSelectedItem();
			String monitorTypeName = "所有类型";
			String monitorType = "99999";
			if (typeitem != null) {
				monitorTypeName = typeitem.getLabel();
				monitorType = typeitem.getId();
			}

			Listitem sortitem = sort.getSelectedItem();
			String sort = "";
			String sortName = "";
			if (sortitem != null) {
				sort = sortitem.getId();
				sortName = sortitem.getLabel();
			}
			Listitem refreshitem = reflesh.getSelectedItem();
			String refresh = "";
			String refreshName = "";
			if (refreshitem != null) {
				refresh = refreshitem.getId();
				refreshName = refreshitem.getLabel();
			}
			this.bean.setTitile(title);
			this.bean.setEntityName(entityName);
			this.bean.setGroupName(goupName);
			this.bean.setMonitorDescripe(descript);
			this.bean.setMonitorName(monitorName);
			this.bean.setMonitorState(monitorState);
			this.bean.setMonitorType(monitorType);
			this.bean.setMonitorTypeName(monitorTypeName);
			this.bean.setRefreshFre(refresh);
			this.bean.setShowHideName(showAndHidden);
			this.bean.setSort(sort);
			this.bean.setSortName(sortName);
			CustomViewSetting.saveCustomView(bean.getNodeId(), bean);

			Executions.getCurrent().getDesktop().getSession().setAttribute(
					"monitor_browse_item", bean);

			createFilter.detach();

			// addlog
			View view = Toolkit.getToolkit().getSvdbView(
					event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			String minfo = loginname + " " + "在"
					+ OpObjectId.monitor_browser.name + "中进行了  "
					+ OpTypeId.edit.name + "操作，编辑筛选条件: " + title;
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit,
					OpObjectId.monitor_browser);

			composer.getShowMonitorData().setTitle(title);
		} catch (Exception e) {
			Messagebox.show("报告标题不能为空,请重新输入!", "提示", Messagebox.OK,
					Messagebox.INFORMATION);
			e.printStackTrace();
		}
	}

	public Map<String, String> queryMonitorTypeInfo() throws Exception {
		View v = ChartUtil.getView();
		// 静态变量导致的空指针异常
		if (v == null) {
			v = Toolkit.getToolkit().getSvdbView(
					Executions.getCurrent().getDesktop().getSession());
		}
		HashMap<String, String> map = new HashMap<String, String>();
		QueryInfo q = new QueryInfo();
		q.needkey = "sv_monitortype";
		q.setNeedType_monitor();
		Map<String, Map<String, String>> fmap = q.load();
		for (String key : fmap.keySet()) {
			MonitorInfo info = v.getMonitorInfo(v.getNode(key));
			if (info == null)
				continue;
			MonitorTemplate template = info.getMonitorTemplate();
			if (template == null)
				continue;
			String name = template.get_sv_name();
			String id = template.get_sv_id();
			map.put(id, name);
		}
		return map;
	}
}
