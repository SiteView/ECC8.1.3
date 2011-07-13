package com.siteview.ecc.monitorbrower.edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.template.TemplateManager;
import com.siteview.base.tree.INode;
import com.siteview.base.treeEdit.EntityEdit;
import com.siteview.base.treeEdit.MonitorEdit;
import com.siteview.base.treeInfo.EntityInfo;
import com.siteview.ecc.tasks.Task;
import com.siteview.ecc.tasks.TaskPack;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.treeview.controls.ISvdbControl;
import com.siteview.ecc.treeview.controls.SvdbCheckBox;
import com.siteview.ecc.treeview.controls.SvdbComboBox;
import com.siteview.ecc.treeview.controls.SvdbTextBox;

public class AddMonitor extends GenericForwardComposer {
	// 绑定的界面的控件
	private Window WAddMonitor;

	private Label entityname;
	private Rows baserow;

	private Textbox tberror;
	private Textbox tbdanger;
	private Textbox tbnormal;
	Panel p1;

	private Rows advancerow;
	private Row insertp;
	private Checkbox chCheckError;
	private Textbox tbErrorFrequency;
	private Combobox cbErrorFrequencyUnit;
	private Combobox cbPlan;
	private Textbox tbDescription;
	private Textbox tbReqortDescription;

	private Button btnok;
	private Button btncancel;
	private Button btnadd;
	private Button btndefault;
	private Button btnplok;

	Button btnerror;
	Button btndanger;
	Button btnnormal;
	// 其他定义
	private Comboitem item;
	private MonitorTemplate monitorTemplate;
	private HashMap<String, String> parsvrun = new HashMap<String, String>(); // 标准运行参数
	private HashMap<String, String> advparsvrun = new HashMap<String, String>(); // 高级运行参数
	private SvdbTextBox tbtitle;
	private Row currentrow;
	private Include eccbody;

	private SvdbComboBox cbdydata; // 动态数据
	String cbValue;
	private String svid;
	private View view;
	private INode node;
	private MonitorEdit monitorEdit;
	private String templateId;
	EntityEdit entityedit;

	public final static String REG_DIGIT = "[0-9]+";
	private static String EntityRefreshi_TargetUrl = "/main/TreeView/WRefreshMonitor.zul";
	private static String PLMonitors_TargetUrl = "/main/TreeView/pladdmonitor.zul";
	private static String Alert_TargetUrl = "/main/TreeView/editalertexpression.zul";
	String monitorid;
	TreeMap<String, String> sortdydata;
	Boolean isedit;
	EccTimer eccTimer;

	int idcount = 9;
	Vbox vbox;
	Timer dytimer = null;
	Boolean issave = false;

	public AddMonitor() {

	}

	public void onCreate$WAddMonitor() {
		node = (INode) WAddMonitor.getAttribute("inode");
		view = (View) WAddMonitor.getAttribute("view");
		//
		node = view.getNode(node.getSvId());
		isedit = (Boolean) WAddMonitor.getAttribute("isedit");
		eccTimer = (EccTimer) WAddMonitor.getAttribute("eccTimer");
		String enname = (String) WAddMonitor.getAttribute("entityname");

		TaskPack t = new TaskPack();
		Task[] tasks = t.findAllTasks();
		for (Task task : tasks) {
			String name = task.getName();
			item = new Comboitem();
			item.setLabel(name);
			item.setValue(name);
			cbPlan.appendChild(item);
		}
		try {

			currentrow = insertp;
			item = new Comboitem("分钟");
			item.setValue("1");
			item.setParent(cbErrorFrequencyUnit);

			item = new Comboitem("小时");
			item.setValue("60");
			item.setParent(cbErrorFrequencyUnit);
			cbErrorFrequencyUnit.setSelectedIndex(0);
			if (isedit) {

				this.monitorEdit = view.getMonitorEdit(node);
				this.monitorTemplate = this.monitorEdit.getMonitorTemplate();
				String templatename = monitorTemplate.get_sv_name();
				WAddMonitor.setTitle("编辑(" + templatename + ")监测器");
				entityname.setValue(enname);
				btnok.setLabel("保存");
				btnadd.setVisible(false);
			} else {
				templateId = (String) WAddMonitor.getAttribute("templateId");
				this.monitorTemplate = TemplateManager
						.getMonitorTemplate(templateId);
				EntityInfo entityinfo = this.view.getEntityInfo(node);
				entityedit = this.view.getEntityEdit(node);
				this.monitorEdit = entityinfo.AddMonitor(templateId);
				String templatename = monitorTemplate.get_sv_name();
				WAddMonitor.setTitle("添加(" + templatename + ")监测器");
				String ename = node.getName();
				entityname.setValue(ename);
				btndefault.setVisible(false);
			}

			BuildParamterForm(baserow, this.monitorTemplate
					.get_Parameter_Items(), true);
			BuildParamterForm(advancerow, this.monitorTemplate
					.get_Advance_Parameter_Items(), false);
			if (isedit) {
				Label lb = new Label("监测器标题*:");
				String svname = this.monitorEdit.getName();
				tbtitle = new SvdbTextBox();
				tbtitle.setValue(svname);
				tbtitle.setWidth("335px");
				vbox = new Vbox();
				Label labelh = new Label();
				labelh.setId("lbp" + idcount);
				labelh.setValue("监测器的显示名称");
				labelh.setSclass("helplabel");
				labelh.setVisible(false);
				idcount++;
				vbox.appendChild(tbtitle);
				vbox.appendChild(labelh);

				Row row = new Row();
				row.appendChild(lb);
				row.appendChild(vbox);
				row.setParent(baserow);
				SetBaseValue();
				if (monitorEdit != null) {
					if (monitorEdit.getParameter() != null) {
						buildUIdata(monitorEdit.getParameter());
					}
					if (monitorEdit.getAdvanceParameter() != null) {
						buildUIdata(monitorEdit.getAdvanceParameter());
					}
				}
				if (monitorEdit != null) {
					if (monitorEdit.getErrorConditon() != null) {
						String errorexpr = buildExpression(monitorEdit
								.getErrorConditon(), monitorTemplate
								.get_Return_Items());
						tberror.setValue(errorexpr);
						tberror.setAttribute("expr", monitorEdit
								.getErrorConditon());
					}
					if (monitorEdit.getWarningConditon() != null) {
						String dangerexpr = buildExpression(monitorEdit
								.getWarningConditon(), monitorTemplate
								.get_Return_Items());
						tbdanger.setValue(dangerexpr);
						tbdanger.setAttribute("expr", monitorEdit
								.getWarningConditon());
					}
					if (monitorEdit.getErrorConditon() != null) {
						String goodexpr = buildExpression(monitorEdit
								.getGoodConditon(), monitorTemplate
								.get_Return_Items());
						tbnormal.setValue(goodexpr);
						tbnormal.setAttribute("expr", monitorEdit
								.getGoodConditon());
					}
				}
			} else {
				Label lb = new Label("监测器标题*:");
				String svname = this.monitorTemplate.get_sv_name();
				tbtitle = new SvdbTextBox();
				tbtitle.setValue(svname);

				vbox = new Vbox();
				Label labelh = new Label();
				labelh.setId("lbp" + idcount);
				labelh.setValue("监测器的显示名称");
				labelh.setSclass("helplabel");
				labelh.setVisible(false);
				idcount++;
				vbox.appendChild(tbtitle);
				vbox.appendChild(labelh);

				Row row = new Row();
				row.appendChild(lb);
				row.appendChild(vbox);
				row.setParent(baserow);

				if (monitorEdit != null) {
					if (monitorTemplate.get_error_conditon() != null) {
						String errorexpr = buildExpression(monitorTemplate
								.get_error_conditon(), monitorTemplate
								.get_Return_Items());
						tberror.setValue(errorexpr);
						HashMap<String, String> conditon = new HashMap<String, String>();
						for (String key : monitorTemplate.get_error_conditon()
								.keySet()) {
							Boolean contin = isgo(key);
							if (contin) {
								continue;
							}
							String value = monitorTemplate.get_error_conditon()
									.get(key);
							conditon.put(key, value);
						}
						tberror.setAttribute("expr", conditon);
					}
					if (monitorTemplate.get_warning_conditon() != null) {
						String dangerexpr = buildExpression(monitorTemplate
								.get_warning_conditon(), monitorTemplate
								.get_Return_Items());
						tbdanger.setValue(dangerexpr);
						HashMap<String, String> conditon = new HashMap<String, String>();
						for (String key : monitorTemplate
								.get_warning_conditon().keySet()) {
							Boolean contin = isgo(key);
							if (contin) {
								continue;
							}
							String value = monitorTemplate
									.get_warning_conditon().get(key);
							conditon.put(key, value);
						}
						tbdanger.setAttribute("expr", conditon);
					}
					if (monitorTemplate.get_good_conditon() != null) {
						String goodexpr = buildExpression(monitorTemplate
								.get_good_conditon(), monitorTemplate
								.get_Return_Items());
						tbnormal.setValue(goodexpr);
						HashMap<String, String> conditon = new HashMap<String, String>();
						for (String key : monitorTemplate.get_good_conditon()
								.keySet()) {
							Boolean contin = isgo(key);
							if (contin) {
								continue;
							}
							String value = monitorTemplate.get_good_conditon()
									.get(key);
							conditon.put(key, value);
						}
						tbnormal.setAttribute("expr", conditon);
					}
				}
			}
			tbtitle.setWidth("350px");
			tbtitle.setHeight("15px");
			if (this.cbdydata != null) {

				this.cbdydata.setFocus(true);
				this.cbdydata.addEventListener("onFocus",
						new GetDydataOnFocus());
				this.cbdydata.addEventListener("onChange",
						new comboboxOnchange());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		WAddMonitor.setPosition("center");
		// p1.setHeight("500px");

		// position= height="100px" contentStyle=
	}

	private Boolean isgo(String key) {
		Boolean isgo = true;
		if (key.startsWith("sv_operate")) {
			isgo = false;
		} else if (key.startsWith("sv_conditioncount")) {
			isgo = false;
		} else if (key.startsWith("sv_expression")) {
			isgo = false;
		} else if (key.startsWith("sv_paramname")) {
			isgo = false;
		} else if (key.startsWith("sv_paramvalue")) {
			isgo = false;
		} else if (key.startsWith("sv_relation")) {
			isgo = false;
		}

		return isgo;
	}

	/**
	 * 构建界面
	 * 
	 * @param rows
	 *            容器
	 * @param parameters
	 *            参数
	 */
	private void BuildParamterForm(Rows rows,
			List<Map<String, String>> parameters, boolean isbaserow) {
		if (parameters.isEmpty() || parameters.size() == 0) {
			return;
		}
		Label label = null;
		HtmlBasedComponent control = null;

		String svname = "";
		String svalue = "";

		Map<String, Map<String, String>> followpar = new HashMap<String, Map<String, String>>();
		// 附属组件
		ArrayList<String> followList = new ArrayList<String>();
		for (Map<String, String> parameter : parameters) {
			if (parameter.containsKey("sv_follow")) {
				if (!parameter.get("sv_follow").isEmpty()) {
					if (!followList.contains(parameter.get("sv_follow"))) {
						followList.add(parameter.get("sv_follow"));
					}
				}
			}
		}
		for (Map<String, String> parameter : parameters) {
			if (followList.contains(parameter.get("sv_name"))) {
				followpar.put(parameter.get("sv_name"), parameter);
			}
		}
		for (Map<String, String> parameter : parameters) {

			// 是否在界面显示
			if (parameter.containsKey("sv_hidden")) {
				if (parameter.get("sv_hidden").equals("true")) {
					if (parameter.containsKey("sv_run")) {
						if (parameter.get("sv_run").toLowerCase()
								.equals("true")) {
							svalue = "";
							if (parameter.containsKey("sv_value")) {
								svalue = parameter.get("sv_value");
							}
							svname = parameter.get("sv_name");
							if (isbaserow) {
								if (!parsvrun.containsKey(svname)) {
									parsvrun.put(svname, svalue);
								}
							} else {
								if (!advparsvrun.containsKey(svname)) {
									advparsvrun.put(svname, svalue);
								}

							}

						}
					}
					continue;
				}

			}
			// 是否是附属组件
			if (followList.contains(parameter.get("sv_name"))) {
				continue;
			}
			// 构建label
			label = buildLabel(parameter);
			// 构建组件
			// 双组件
			if (parameter.containsKey("sv_follow")
					&& !parameter.get("sv_follow").isEmpty()) {
				Hbox bparent = new Hbox();
				String follow = parameter.get("sv_follow");
				HtmlBasedComponent basecontrol = buildControl(parameter, false,
						true);
				HtmlBasedComponent followcontrol = buildControl(followpar
						.get(follow), true, false);
				basecontrol.setParent(bparent);
				followcontrol.setParent(bparent);
				control = bparent;
			} else {
				control = buildControl(parameter, false, false);
			}
			String heltext = parameter.get("sv_helptext");
			if (heltext == null) {
				heltext = "";
			}
			vbox = new Vbox();
			Label labelh = new Label();
			labelh.setId("lbp" + idcount);
			labelh.setValue(heltext);
			labelh.setSclass("helplabel");
			labelh.setVisible(false);
			idcount++;

			vbox.appendChild(control);
			vbox.appendChild(labelh);

			Row row = new Row();
			row.appendChild(label);
			row.appendChild(vbox);

			if (isbaserow) {
				rows.appendChild(row);
			} else {
				row.setId(parameter.get("sv_name"));
				rows.insertBefore(row, currentrow);
			}
		}

	}

	/**
	 * 构建label
	 * 
	 * @param par
	 * @return
	 */
	private Label buildLabel(Map<String, String> par) {
		Label label = new Label();
		if (par.containsKey("sv_allownull")) {
			if (par.get("sv_allownull").toLowerCase().equals("false")) {
				label.setValue(par.get("sv_label") + "*:");
			} else {
				label.setValue(par.get("sv_label") + ":");
			}
		} else {
			label.setValue(par.get("sv_label") + ":");
		}
		return label;
	}

	/**
	 * 构建组件
	 * 
	 * @param par
	 * @return
	 * @throws InterruptedException
	 */
	private HtmlBasedComponent buildControl(Map<String, String> par,
			Boolean f, Boolean z) {
		if (par == null) {
			return null;

		}
		HtmlBasedComponent control = null;
		Comboitem cbitem = null;
		String svtype = "";
		if (par.containsKey("sv_type")) {
			svtype = par.get("sv_type").toLowerCase();
			if (svtype.equals("textbox")) {
				control = new SvdbTextBox();
				((SvdbTextBox) control).setHelptext(par.get("sv_helptext"));
				if (par.containsKey("sv_run")) {
					boolean svrun;
					svrun = par.get("sv_run").toLowerCase().equals("true") ? true
							: false;
					((SvdbTextBox) control).setSvrun(svrun);
				}
			} else if (svtype.equals("password")) {

				Textbox tb = new SvdbTextBox();
				tb.setType("password");
				control = tb;
				((SvdbTextBox) control).setHelptext(par.get("sv_helptext"));
				if (par.containsKey("sv_run")) {
					boolean svrun;
					svrun = par.get("sv_run").toLowerCase().equals("true") ? true
							: false;
					((SvdbTextBox) control).setSvrun(svrun);
				}
			} else if (svtype.equals("combobox")) {
				SvdbComboBox cb = new SvdbComboBox();
				if (par.containsKey("sv_run")) {
					boolean svrun;
					svrun = par.get("sv_run").toLowerCase().equals("true") ? true
							: false;
					((SvdbComboBox) cb).setSvrun(svrun);
				}
				cb.setReadonly(true);
				for (String key : par.keySet()) {
					if (key.startsWith("sv_itemlabel")) {
						cbitem = new Comboitem();
						cbitem.setLabel(par.get(key));
						cbitem.setValue(par.get("sv_itemvalue"
								+ key.substring("sv_itemlabel".length(), key
										.length())));
						cbitem.setParent(cb);
					}
				}
				if (par.containsKey("sv_dll") && !par.get("sv_dll").isEmpty()) {
					if (!isedit) {
						btnplok.setVisible(true);
					}
					cbitem = new Comboitem();
					cbitem.setLabel("正在获取数据...");
					cbitem.setValue("1");
					cbitem.setParent(cb);
					cb.setSelectedIndex(0);
					this.cbdydata = cb;
				}
				control = cb;
				((SvdbComboBox) control).setHelptext(par.get("sv_helptext"));
			} else if (svtype.equals("checkbox")) {
				control = new SvdbCheckBox();
				((SvdbCheckBox) control).setHelptext(par.get("sv_helptext"));
			} else {
				// Messagebox.show("未知的控件类型:" + par.get("sv_type"));
			}

		}
		if (!f) {
			if (control instanceof SvdbComboBox) {
				control.setWidth("335px");
				control.setHeight("15px");
				if (z) {
					control.setWidth("285px");
				}
			} else {
				control.setWidth("350px");
				control.setHeight("15px");
				if (z) {
					control.setWidth("285px");
				}
			}
		} else {
			control.setWidth("40px");
			control.setHeight("15px");
			// if(control instanceof SvdbTextBox)
			// {
			// control.setWidth("55px");
			// }
		}
		control.setId("svctrl" + par.get("sv_name"));
		((ISvdbControl) control).setSvdbField(par.get("sv_name"));

		if (par.containsKey("sv_savename") && !par.get("sv_savename").isEmpty()) {
			((ISvdbControl) control).setSvdbField(par.get("sv_savename"));
		}
		if (par.containsKey("sv_value")) {
			((ISvdbControl) control).setSvdbValue(par.get("sv_value"));
		}
		control.setAttribute("tag", par);
		return control;

	}

	public class GetDydataOnFocus implements EventListener {
		private Comboitem item;
		private SvdbComboBox cb;

		public GetDydataOnFocus() {

		}

		@Override
		public void onEvent(Event arg0) throws Exception {
			try {
				cb = (SvdbComboBox) arg0.getTarget();
				if (cb.getItemCount() > 1) {

					return;
				}
				if (dytimer != null) {
					return;
				}
				monitorEdit.startMonitorDynamicData(view);
				dytimer = new Timer();
				dytimer.setParent(WAddMonitor);
				dytimer.setDelay(1000);
				dytimer.setRepeats(true);
				dytimer.setRunning(false);
				dytimer.addEventListener("onTimer", new ontime(cb));
				dytimer.start();
			} catch (Exception ex) {

			}
			// try
			// {
			// cb = (SvdbComboBox) arg0.getTarget();
			// if (cb.getItemCount() > 1)
			// {
			//					
			// return;
			// }
			//				
			// monitorEdit.startMonitorDynamicData(view);
			// HashMap<String, String> dydata = null;
			// while (dydata == null)
			// {
			// logger.info(" get monitor Dynamic Data ");
			// Thread.sleep(1000);
			// dydata = monitorEdit.getMonitorDynamicData(view);
			// }
			//				
			// sortdydata = new TreeMap<String, String>();
			//				
			// for (String key : dydata.keySet())
			// {
			// String Key = dydata.get(key);
			// String Value = key;
			// if (!sortdydata.containsKey(Key))
			// {
			// sortdydata.put(Key, Value);
			//						
			// }
			// }
			// cb.getItems().clear();
			// this.cb.setValue("");
			// for (String key : sortdydata.keySet())
			// {
			// item = new Comboitem();
			// item.setLabel(key);
			// item.setValue(sortdydata.get(key));
			// item.setParent(cb);
			// }
			// cb.setSelectedIndex(0);
			// if (isedit)
			// {
			// String cbvalue =
			// monitorEdit.getParameter().get(cb.getSvdbField());
			// if (cbvalue != null)
			// {
			// cb.setSvdbValue(cbvalue);
			// }
			// }
			//				
			// } catch (Exception e)
			// {
			//				
			// this.cb.getItems().clear();
			// this.cb.setValue("");
			//				
			// }
			//			
		}
	}

	public class ontime implements EventListener {
		SvdbComboBox cb;
		Boolean stoptag = false;

		public ontime(SvdbComboBox cb) {
			this.cb = cb;
		}

		@Override
		public void onEvent(Event arg0) throws Exception {
			// TODO Auto-generated method stub
			if (stoptag) {
				try {
					((Timer) arg0.getTarget()).stop();
					((Timer) arg0.getTarget()).setRepeats(false);
				} catch (Exception e) {

				}
				return;
			}
			try {

				Map<String, String> dydata = monitorEdit.getMonitorDynamicData(view);
				if (dydata == null) {
					return;
				} else {
					stoptag = true;
				}

				sortdydata = new TreeMap<String, String>();

				for (String key : dydata.keySet()) {
					String Key = dydata.get(key);
					String Value = key;
					if (!sortdydata.containsKey(Key)) {
						sortdydata.put(Key, Value);

					}
				}
				cb.getItems().clear();
				this.cb.setValue("");
				for (String key : sortdydata.keySet()) {
					item = new Comboitem();
					item.setLabel(key);
					item.setValue(sortdydata.get(key));
					item.setParent(cb);
				}
				cb.setSelectedIndex(0);
				if (isedit) {
					String cbvalue = monitorEdit.getParameter().get(
							cb.getSvdbField());
					if (cbvalue != null) {
						cb.setSvdbValue(cbvalue);
					}
				}

			} catch (Exception e) {

				this.cb.getItems().clear();
				this.cb.setValue("");
				stoptag = true;

			}

		}
	}

	public class comboboxOnchange implements EventListener {

		@Override
		public void onEvent(Event arg0) throws Exception {
			// TODO Auto-generated method stub
			String dyname = monitorTemplate.get_Property().get("sv_extrasave");
			String entityname = dyname.replace("_", "");
			String va = ((SvdbComboBox) arg0.getTarget()).getValue();
			tbtitle.setValue(entityname + ":" + va);
		}

	}

	// <summary>
	// 验证
	// </summary>
	// <param name="message"></param>
	// <returns></returns>
	public String Validate() {

		String message = "";
		double value;
		if (tbtitle.getValue() == null || tbtitle.getValue().isEmpty()) {
			tbtitle.focus();
			return "请输入标题";
		}
		if (tberror.getValue() == null || tberror.getValue().isEmpty()) {
			tberror.focus();
			return "请输入错误的阀值";
		}
		if (tbdanger.getValue() == null || tbdanger.getValue().isEmpty()) {
			tbdanger.focus();
			return "请输入警报的阀值";
		}
		if (tbnormal.getValue() == null || tbnormal.getValue().isEmpty()) {
			tbnormal.focus();
			return "请输入正常的阀值";
		}
		if (!tbErrorFrequency.getValue().isEmpty()) {
			if (!tbErrorFrequency.getValue().matches(REG_DIGIT)) {
				tbErrorFrequency.focus();
				return "请输入正确的错误监测频率(数字)";
			}
		}

		for (Map<String, String> item : monitorTemplate
				.get_Parameter_Items()) {
			if (item.get("sv_hidden") != null) {
				if (item.get("sv_hidden").trim().toLowerCase().equals("true")) {
					continue;
				}
			}
			HtmlBasedComponent control = null;
			try {
				control = (HtmlBasedComponent) WAddMonitor.getFellow("svctrl"
						+ item.get("sv_name"));
			} catch (Exception ex) {
			}
			if (control == null) {
				continue;
			}
			ISvdbControl svControl = (ISvdbControl) control;
			if ("false".equals(item.get("sv_allownull"))
					&& svControl.getSvdbValue().isEmpty()) {
				control.focus();
				message = item.get("sv_tip");
				if (message == null) {
					message = "请检查输入的" + item.get("sv_label") + "是否正确";
				}
				return message;
			}
			if ("false".equals(item.get("sv_allownull"))
					&& "true".equals(item.get("sv_isnumeric"))
					&& !svControl.getSvdbValue().matches(REG_DIGIT)) {

				control.focus();
				message = item.get("sv_tip") + "(数字)";
				;
				if (message == "null(数字)") {
					message = "请检查输入的" + item.get("sv_label") + "是否正确";
				}
				return message;
			}

		}
		for (Map<String, String> item : monitorTemplate
				.get_Advance_Parameter_Items()) {
			if (item.get("sv_hidden") != null) {
				if (item.get("sv_hidden").trim().toLowerCase().equals("true")) {
					continue;
				}
			}
			HtmlBasedComponent control = null;
			try {
				control = (HtmlBasedComponent) WAddMonitor.getFellow("svctrl"
						+ item.get("sv_name"));
			} catch (Exception ex) {

			}

			if (control == null) {
				continue;
			}
			ISvdbControl svControl = (ISvdbControl) control;
			if ("false".equals(item.get("sv_allownull"))
					&& svControl.getSvdbValue().isEmpty()) {
				control.focus();
				message = item.get("sv_tip");
				if (message == null) {
					message = "请检查输入的" + item.get("sv_label") + "是否正确";
				}
				return message;
			}
			if ("false".equals(item.get("sv_allownull"))
					&& "true".equals(item.get("sv_isnumeric"))
					&& !svControl.getSvdbValue().matches(REG_DIGIT)) {

				control.focus();
				message = item.get("sv_tip") + "(数字)";
				;
				if (message == "null(数字)") {
					message = "请检查输入的" + item.get("sv_label") + "是否正确";
				}
				return message;
			}

		}

		return "";
	}

	//
	private void SetBaseValue() {
		if (monitorEdit == null) {
			return;
		}
		tbtitle.setSvdbValue(monitorEdit.getName());
		String sv_errfreqsave = monitorEdit.getParameter()
				.get("sv_errfreqsave");
		tbErrorFrequency.setValue(sv_errfreqsave);
		if (monitorEdit.getParameter().get("sv_errfrequint") != null) {
			String sv_errfrequint = monitorEdit.getParameter().get(
					"sv_errfrequint").equals("1") ? "分钟" : "小时";
			cbErrorFrequencyUnit.setValue(sv_errfrequint);
		}
		String sv_description = monitorEdit.getParameter()
				.get("sv_description");
		tbDescription.setValue(sv_description);
		String sv_reportdesc = monitorEdit.getParameter().get("sv_reportdesc");
		tbReqortDescription.setValue(sv_reportdesc);
		if (monitorEdit.getParameter().get("sv_checkerr") != null) {
			Boolean sv_checkerr = monitorEdit.getParameter().get("sv_checkerr")
					.equals("true") ? true : false;
			chCheckError.setChecked(sv_checkerr);
		}
		String sv_plan = monitorEdit.getParameter().get("sv_plan");
		cbPlan.setValue(sv_plan);
	}

	private void buildUIdata(Map<String, String> par) {

		for (String key : par.keySet()) {
			HtmlBasedComponent control = null;
			try {
				control = (HtmlBasedComponent) WAddMonitor.getFellow("svctrl"
						+ key);
			} catch (Exception ex) {
			}
			if (control == null) {
				continue;
			}
			String value = par.get(key);
			ISvdbControl svControl = (ISvdbControl) control;
			if (key.equals("_frequency")) {
				String _frequency1 = par.get("_frequency1");
				if (_frequency1 == null || _frequency1.isEmpty()) {
					svControl.setSvdbValue(value);
				} else {
					svControl.setSvdbValue(_frequency1);
				}
			} else {
				Map<String, String> parameter = (Map<String, String>) control
						.getAttribute("tag");
				if (parameter.get("sv_savename") != null) {
					HtmlBasedComponent control1 = (HtmlBasedComponent) WAddMonitor
							.getFellow("svctrl" + parameter.get("sv_follow"));
					ISvdbControl svControl1 = (ISvdbControl) control1;
					Boolean ishave = false;

					SvdbComboBox cb = (SvdbComboBox) control;
					for (int j = 0; j < cb.getItemCount(); j++) {
						if (cb.getItemAtIndex(j).getValue().equals(value)) {
							ishave = true;
							break;
						}
					}
					if (ishave) {
						svControl.setSvdbValue(value);
					} else {
						svControl1.setSvdbValue(value);
					}
				} else {
					if (cbdydata != null) {
						if (("svctrl" + svControl.getSvdbField())
								.equals(cbdydata.getName())) {
							continue;
						}
					}

					svControl.setSvdbValue(value);

				}
			}
		}
	}

	public static String buildExpression(Map<String, String> expr,
			List<Map<String, String>> returnvalues) {
		if (expr == null) {
			return null;
		}
		if (returnvalues == null) {
			return null;
		}
		Map<String, String> itemMapping = new LinkedHashMap<String, String>();
		for (Map<String, String> hashMap : returnvalues) {
			String itemName = hashMap.get("sv_name");
			String itemLabel = hashMap.get("sv_label");
			if (itemName == null || itemName.isEmpty()) {
				continue;
			}
			itemMapping.put(itemName, itemLabel);
		}
		String label = "";
		StringBuilder AlertExpression = new StringBuilder();
		int condtionCount = Integer.parseInt(expr.get("sv_conditioncount"));
		for (int i = 1; i < condtionCount + 1; i++) {
			String item = expr.get("sv_paramname" + i);
			if (!itemMapping.containsKey(item)) {
				continue;
			}
			label = "[" + itemMapping.get(item);
			String paramvalue = expr.get("sv_paramvalue" + i) + "] ";
			String operate = " " + expr.get("sv_operate" + i);
			String sv_relation = expr.get("sv_relation" + i);
			if (sv_relation != null) {
				sv_relation = sv_relation.equals("or") ? "或 " : "与 ";
			}
			AlertExpression.append(label);
			AlertExpression.append(operate);
			AlertExpression.append(paramvalue);
			if (sv_relation != null) {
				AlertExpression.append(sv_relation);
			}
		}
		return AlertExpression.toString();
	}

	/**
	 * 构建基本数据
	 */
	private void buildBaseData() {

		this.monitorEdit.getProperty().put("sv_name", tbtitle.getValue());
		if (entityedit != null) {
			String network = entityedit.getProperty().get("sv_network");
			if (network != null) {
				if (network.toLowerCase().equals("true")) {
					this.monitorEdit.getProperty().put("sv_intpos", "0");
				} else {
					this.monitorEdit.getProperty().put("sv_intpos", "1");
				}
			}
			this.monitorEdit.getProperty().put("sv_disable", "");
			this.monitorEdit.getProperty().put("sv_starttime", "");
			this.monitorEdit.getProperty().put("sv_endtime", "");
		}

		// paramter
		String errfreqsave = tbErrorFrequency.getValue();
		this.monitorEdit.getParameter().put("sv_errfreqsave", errfreqsave);
		String errfrequint = "分钟".equals(this.cbErrorFrequencyUnit.getValue()) ? "1"
				: "60";
		this.monitorEdit.getParameter().put("sv_errfrequint", errfrequint);
		String desc = tbDescription.getValue();
		this.monitorEdit.getParameter().put("sv_description", desc);
		String reportdesc = tbReqortDescription.getValue();
		this.monitorEdit.getParameter().put("sv_reportdesc", reportdesc);
		String checkerr = chCheckError.isChecked() ? "true" : "false";
		this.monitorEdit.getParameter().put("sv_checkerr", checkerr);
		String plan = cbPlan.getValue();
		this.monitorEdit.getParameter().put("sv_plan", plan);

		String ErrorFrequency = tbErrorFrequency.getValue();
		if (ErrorFrequency == null || ErrorFrequency.isEmpty()) {
			ErrorFrequency = "0";
		}
		if (!cbErrorFrequencyUnit.getValue().equals("分钟")) {
			ErrorFrequency = Integer
					.toString((Integer.parseInt(ErrorFrequency) * 60));
		}
		this.monitorEdit.getParameter().put("sv_errfreq", ErrorFrequency);
		buildMonitorData(monitorTemplate.get_Parameter_Items(), false);

		if (parsvrun.keySet() != null) {
			for (String key : parsvrun.keySet()) {
				this.monitorEdit.getParameter().put(key, parsvrun.get(key));
			}
		}
		// advanceparamter
		buildMonitorData(monitorTemplate.get_Advance_Parameter_Items(), true);
		if (advparsvrun.keySet() != null) {
			for (String key : advparsvrun.keySet()) {
				this.monitorEdit.getAdvanceParameter().put(key,
						advparsvrun.get(key));
			}
		}
		//
		Map<String, String> ErrorConditon = (Map<String, String>) tberror
				.getAttribute("expr");
		for (String key : ErrorConditon.keySet()) {
			this.monitorEdit.getErrorConditon()
					.put(key, ErrorConditon.get(key));
		}
		Map<String, String> WarningConditon = (Map<String, String>) tbdanger
				.getAttribute("expr");
		for (String key : WarningConditon.keySet()) {
			this.monitorEdit.getWarningConditon().put(key,
					WarningConditon.get(key));
		}
		Map<String, String> GoodConditon = (Map<String, String>) tbnormal
				.getAttribute("expr");
		for (String key : GoodConditon.keySet()) {
			this.monitorEdit.getGoodConditon().put(key, GoodConditon.get(key));
		}
	}

	/**
	 * 构建监测器数据
	 */
	private void buildMonitorData(List<Map<String, String>> par,
			Boolean isadvance) {

		for (Map<String, String> item : par) {
			if (item.get("sv_hidden") != null) {
				if (item.get("sv_hidden").trim().toLowerCase().equals("true")) {
					continue;
				}
			}
			HtmlBasedComponent control = null;
			try {
				control = (HtmlBasedComponent) WAddMonitor.getFellow("svctrl"
						+ item.get("sv_name"));
			} catch (Exception ex) {
			}
			if (control == null) {
				continue;
			}
			ISvdbControl svControl = (ISvdbControl) control;
			if (!("svctrl" + svControl.getSvdbField()).equals(control.getId())) {
				if (svControl.getSvdbValue() != null
						&& !svControl.getSvdbValue().isEmpty()) {
					if (isadvance) {
						this.monitorEdit.getAdvanceParameter().put(
								svControl.getSvdbField(),
								svControl.getSvdbValue());
					} else {
						this.monitorEdit.getParameter().put(
								svControl.getSvdbField(),
								svControl.getSvdbValue());
					}
				}

				continue;
			}
			if (isadvance) {
				this.monitorEdit.getAdvanceParameter().put(
						svControl.getSvdbField(), svControl.getSvdbValue());
			} else {
				String value = svControl.getSvdbValue();
				if (svControl.getSvdbField().equals("_URL")) {
					if (!value.startsWith("http://")) {
						value = "http://" + value;
					}
				}
				this.monitorEdit.getParameter().put(svControl.getSvdbField(),
						value);

			}

			if (svControl.getSvdbField().equals("_frequency")) {
				if (isadvance) {
					this.monitorEdit.getAdvanceParameter().put("_frequency1",
							svControl.getSvdbValue());
				} else {
					this.monitorEdit.getParameter().put("_frequency1",
							svControl.getSvdbValue());
				}

			}
			if (svControl.getSvdbField().equals("_frequencyUnit")) {

				if (isadvance) {
					String frequency = this.monitorEdit.getAdvanceParameter()
							.get("_frequency");
					String frequencyUnit = this.monitorEdit
							.getAdvanceParameter().get("_frequencyUnit");
					String va = "1".equals(frequencyUnit) ? frequency : Integer
							.toString((Integer.parseInt(frequency) * 60));
					this.monitorEdit.getAdvanceParameter()
							.put("_frequency", va);
				} else {
					String frequency = this.monitorEdit.getParameter().get(
							"_frequency");
					String frequencyUnit = this.monitorEdit.getParameter().get(
							"_frequencyUnit");
					String va = "1".equals(frequencyUnit) ? frequency : Integer
							.toString((Integer.parseInt(frequency) * 60));
					this.monitorEdit.getParameter().put("_frequency", va);
				}
			}

		}

	}

	public void onClick$btndefault() {
		if (monitorTemplate.get_error_conditon() != null) {
			String errorexpr = buildExpression(monitorTemplate
					.get_error_conditon(), monitorTemplate.get_Return_Items());
			tberror.setValue(errorexpr);
			HashMap<String, String> conditon = new HashMap<String, String>();
			for (String key : monitorTemplate.get_error_conditon().keySet()) {
				Boolean contin = isgo(key);
				if (contin) {
					continue;
				}
				String value = monitorTemplate.get_error_conditon().get(key);
				conditon.put(key, value);
			}
			tberror.setAttribute("expr", conditon);
		}
		if (monitorTemplate.get_warning_conditon() != null) {
			String dangerexpr = buildExpression(monitorTemplate
					.get_warning_conditon(), monitorTemplate.get_Return_Items());
			tbdanger.setValue(dangerexpr);
			HashMap<String, String> conditon = new HashMap<String, String>();
			for (String key : monitorTemplate.get_warning_conditon().keySet()) {
				Boolean contin = isgo(key);
				if (contin) {
					continue;
				}
				String value = monitorTemplate.get_warning_conditon().get(key);
				conditon.put(key, value);
			}
			tbdanger.setAttribute("expr", conditon);
		}
		if (monitorTemplate.get_good_conditon() != null) {
			String goodexpr = buildExpression(monitorTemplate
					.get_good_conditon(), monitorTemplate.get_Return_Items());
			tbnormal.setValue(goodexpr);
			HashMap<String, String> conditon = new HashMap<String, String>();
			for (String key : monitorTemplate.get_good_conditon().keySet()) {
				Boolean contin = isgo(key);
				if (contin) {
					continue;
				}
				String value = monitorTemplate.get_good_conditon().get(key);
				conditon.put(key, value);
			}
			tbnormal.setAttribute("expr", conditon);
		}
	}

	/**
	 * 设置默认值
	 */
	private void SetDefaultValue() {

	}

	// Button btnerror;
	// Button btndanger;
	// Button btnnormal;
	public void onClick$btnerror() throws Exception {

		final Window win = (Window) Executions.createComponents(
				Alert_TargetUrl, null, null);
		win.setAttribute("tb", tberror);
		win.setAttribute("monitorTemplate", monitorTemplate);
		try {
			win.doModal();
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
					Messagebox.ERROR);
		}
	}

	public void onClick$btndanger() throws Exception {
		final Window win = (Window) Executions.createComponents(
				Alert_TargetUrl, null, null);
		win.setAttribute("tb", tbdanger);
		win.setAttribute("monitorTemplate", monitorTemplate);
		try {
			win.doModal();
		} catch (SuspendNotAllowedException e) {
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
					Messagebox.ERROR);
		}
	}

	public void onClick$btnnormal() throws Exception {
		final Window win = (Window) Executions.createComponents(
				Alert_TargetUrl, null, null);
		win.setAttribute("tb", tbnormal);
		win.setAttribute("monitorTemplate", monitorTemplate);
		try {
			win.doModal();
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
					Messagebox.ERROR);
		}
	}

	private void savedata(Boolean f) {
		if (issave) {
			return;
		} else {
			issave = true;
			btnok.setDisabled(true);
			btnadd.setDisabled(true);
		}
		if (cbdydata != null) {
			if (cbdydata.getItemCount() == 0
					|| cbdydata.getText().equals("正在获取数据...")) {

				try {
					Messagebox.show("没有可以添加的监测器！", "提示", Messagebox.OK,
							Messagebox.EXCLAMATION);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
		}
		try {
			String message = Validate();
			if (!message.equals("")) {
				Messagebox.show(message, "提示", Messagebox.OK,
						Messagebox.EXCLAMATION);
				return;
			}
			buildBaseData();
			this.monitorEdit.teleSave(view);
			monitorid = monitorEdit.getSvId();
			monitorEdit.setName(tbtitle.getValue());
			INode[] ids = new INode[] { monitorEdit };
			if (isedit) {
				eccTimer.refresh(ids, ChangeDetailEvent.TYPE_MODIFY);
			} else {
				eccTimer.refresh(ids, ChangeDetailEvent.TYPE_ADD);
			}

			// INode node1 = view.getNode(monitorid);
			WAddMonitor.detach();

			final Window win = (Window) Executions.createComponents(
					EntityRefreshi_TargetUrl, null, null);
			win.setAttribute("inode", monitorEdit);
			win.setAttribute("view", view);
			win.setAttribute("eccTimer", eccTimer);
			win.doModal();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			String smessage = e.getMessage();
			if (smessage == null) {
				smessage = "";
				return;
			}
			if (smessage.contains("Less permission")) {
				smessage = "您的监测器点数不够!";
			}
			try {
				Messagebox.show(smessage, "提示", Messagebox.OK,
						Messagebox.EXCLAMATION);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public void onClick$btnok() {
		savedata(true);
	}

	public void onOK$WAddMonitor() {
		savedata(true);
	}

	public void onClick$btnadd() {
		savedata(false);
	}

	public void onClick$btnhelp() {
		for (int i = 1; i < idcount; i++) {
			Label lb = null;
			try {
				lb = (Label) WAddMonitor.getFellow("lbp" + i);
			} catch (Exception ex) {

			}
			if (lb != null) {
				lb.setVisible(!lb.isVisible());
			}
		}
	}

	public void onClick$btncancel() {
		WAddMonitor.detach();
	}

	/**
	 * 批量添加
	 * @throws Exception 
	 */
	public void onClick$btnplok() throws Exception {
		if (cbdydata != null) {
			if (cbdydata.getItemCount() == 0
					|| cbdydata.getText().equals("正在获取数据...")) {

				try {
					Messagebox.show("没有可以添加的监测器！", "提示", Messagebox.OK,
							Messagebox.EXCLAMATION);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}
		}
		WAddMonitor.detach();
		final Window win = (Window) Executions.createComponents(
				PLMonitors_TargetUrl, null, null);
		win.setAttribute("inode", node);
		win.setAttribute("view", view);
		win.setAttribute("dydata", sortdydata);
		win.setAttribute("monitorTemplate", monitorTemplate);
		win.setAttribute("eccTimer", eccTimer);
		try {
			win.doModal();
		} catch (SuspendNotAllowedException e) {
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
}
