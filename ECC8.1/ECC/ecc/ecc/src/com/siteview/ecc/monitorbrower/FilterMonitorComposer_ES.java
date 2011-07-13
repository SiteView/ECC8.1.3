package com.siteview.ecc.monitorbrower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

/**
 * 创建筛选条件类
 * 
 * @author Administrator
 * 
 */
public class FilterMonitorComposer_ES extends GenericForwardComposer {
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
	private MonitorBrowseComposer composer;

	public String makeRandomCVName(long seed) {
		Random r = new Random(seed);
		return "CV" + r.nextLong();
	}

	public void onCreate$createFilter(Event event) throws InterruptedException {
		try {
			composer = (MonitorBrowseComposer) createFilter
					.getAttribute("monitorImfo");
			HashMap<String, String> typeInfo;
			typeInfo = queryMonitorTypeInfo();
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
						item.setId(typeInfo.get(key));
						Listcell c = new Listcell(typeInfo.get(key));
						item.appendChild(c);
						monitorType.appendChild(item);
					}
				}
			}
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
					Messagebox.ERROR);
			e.printStackTrace();
		}

	}

	public void onClick$monitorNameBtn(Event event) throws Exception {
		try {
			final Window win = (Window) Executions.createComponents(
					"/main/monitorbrower/monitorselecttree.zul", null, null);
			win.setAttribute("monitorLabel", monitorName);
			win.setMaximizable(false);
			win.setClosable(true);
			win.doModal();
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
					Messagebox.ERROR);
			e.printStackTrace();
		}
	}

	public void onClick$monitorGroupBtn(Event event)
			throws SuspendNotAllowedException, InterruptedException {
		final Window win = (Window) Executions.createComponents(
				"/main/monitorbrower/goupselect.zul", null, null);
		win.setAttribute("groupLabel", groupName);
		win.setMaximizable(false);
		win.setClosable(true);
		win.doModal();
	}

	public void onClick$monitorEntiryBtn(Event event) throws Exception {
		final Window win = (Window) Executions.createComponents(
				"/main/monitorbrower/entityselecttree.zul", null, null);
		win.setAttribute("entityLabel", entityBe);
		win.setMaximizable(false);
		win.setClosable(true);
		win.doModal();
	}

	public void onClick$editOK(Event event) throws Exception {
		try {
			String cvName = this.makeRandomCVName(System.currentTimeMillis());
			String title = this.filterName.getValue().trim();
			if (title == null || title.trim().equals("")) {
				Messagebox.show("请输入自定义筛选名称！", "提示", Messagebox.OK,
						Messagebox.INFORMATION);
				filterName.setFocus(true);
				return;
			}
			// 判断该名称是否存在

			Listbox box = (Listbox) createFilter.getAttribute("listbox");
			if (box == null)
				throw new Exception("listbox is null!");
			List<CVBean> bs = CustomViewSetting.getCustomView();
			String monitorName = this.monitorName.getValue().trim();
			for (CVBean bk : bs) {
				if (bk.getTitile().equals(title)) {
					Messagebox.show("筛选名称:  " + title + "  已经存在！", "提示",
							Messagebox.OK, Messagebox.INFORMATION);
					filterName.setFocus(true);
					return;
				}
			}

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

			CVBean bean = new CVBean(cvName, entityName, goupName, descript,
					monitorName, monitorState, monitorType, monitorTypeName,
					cvName, refresh, showAndHidden, sort, sortName, title);
			CustomViewSetting.saveCustomView(bean.getNodeId(), bean);
			createFilter.detach();

			Executions.getCurrent().getDesktop().getSession().setAttribute(
					"monitor_browse_item", bean);

			composer.getShowMonitorData().setTitle(bean.getTitile());
			// 改变一些按钮的状态
			composer.setButtonNormal();
			// addlog
			View view = Toolkit.getToolkit().getSvdbView(
					event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			String minfo = loginname + " " + "在"
					+ OpObjectId.monitor_browser.name + "中进行了  "
					+ OpTypeId.add.name + "操作，创建筛选条件: " + title;
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add,
					OpObjectId.monitor_browser);
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
					Messagebox.ERROR);
			e.printStackTrace();
		}
	}

	public HashMap<String, String> queryMonitorTypeInfo() throws Exception {
		View v = ChartUtil.getView();
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
