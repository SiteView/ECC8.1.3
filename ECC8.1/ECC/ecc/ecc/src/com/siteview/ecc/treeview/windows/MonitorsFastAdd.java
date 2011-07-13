package com.siteview.ecc.treeview.windows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import com.siteview.base.data.ReportManager;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.template.EntityTemplate;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.template.TemplateManager;
import com.siteview.base.tree.INode;
import com.siteview.base.treeEdit.EntityEdit;
import com.siteview.base.treeEdit.MonitorEdit;
import com.siteview.ecc.general.License;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.treeview.EccTreeItem;

public class MonitorsFastAdd extends GenericForwardComposer
{
	private final static Logger logger = Logger.getLogger(MonitorsFastAdd.class);
	Vbox					parentcontainers;
	Window					WFastAdd;
	Button					btnadd;
	Button					btnclose;
	Panelchildren pc;
	
	EntityTemplate			entityTemplate;
	private String			error_message				= null;
	List<MonitorTemplate>	monitorTemplates			= new ArrayList<MonitorTemplate>();
	private List<String>	idlist						= new ArrayList<String>();				// 快速添加id
	private List<String>	idsellist					= new ArrayList<String>();				// 快速添加选择 id
																								
	private List<Checkbox>	SelectedCheckboxs			= new ArrayList<Checkbox>();			// 已经选择的框
																								
	View					view;
	EntityEdit				entityedit;
	
	List<INode>				nodes						= new ArrayList<INode>();
	ArrayList<EccTreeItem>	children					= new ArrayList<EccTreeItem>();
	EccTimer				eccTimer;
	private static String	EntityRefreshi_TargetUrl	= "/main/TreeView/WRefreshMonitor.zul";
	Boolean					issave						= false;
	
	public MonitorsFastAdd()
	{
		
	}
	
	public void onCreate$WFastAdd()
	{
		entityedit = (EntityEdit) WFastAdd.getAttribute("entityEdit");
		view = (View) WFastAdd.getAttribute("view");
		eccTimer = (EccTimer) WFastAdd.getAttribute("eccTimer");
		Tree tree=(Tree)WFastAdd.getDesktop().getPage("eccmain").getFellow("tree");
		int maxhint = 0;
		try
		{
			int maxh = (Integer) tree.getAttribute("desktopHeight");
			maxhint = maxh - 200;
			if (maxhint>400)
			{
				maxhint=400;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if (maxhint == 0)
		{
			pc.setStyle("margin:5px 5px 5px 5px;overflow-y:auto;max-height:400px;");
		} else
		{
			pc.setStyle("margin:5px 5px 5px 5px;overflow-y:auto;max-height:" + maxhint + "px;");
		}
		try
		{
			entityTemplate = entityedit.getDeviceTemplate();
		} catch (Exception ex)
		{
			error_message = "获得设备模板失败！";
			
		}
		if (entityTemplate == null)
		{
			return;
		}
		String quickadd = entityTemplate.get_sv_quickadd();
		String quickaddsel = entityTemplate.get_sv_quickaddsel();
		if (quickadd != null && !quickadd.isEmpty())
		{
			if(quickadd.split(",").length<2)
			{
				parentcontainers.setHeight("150px");
			}else
			{
				parentcontainers.setHeight("400px");
			}
			idlist = buildlist(quickadd);
		}
		if (quickaddsel != null && !quickaddsel.isEmpty())
		{
			idsellist = buildlist(quickaddsel);
		}
		monitorTemplates.clear();
		MonitorTemplate mt;
		for (String monitortemplateid : idlist)
		{
			mt = TemplateManager.getMonitorTemplate(monitortemplateid);
			monitorTemplates.add(mt);
		}
		if (error_message != null)
		{
			return;
		}
		CreateUI();
		WFastAdd.setPosition("center");
	}
	
	/**
	 * 分解字符串
	 * 
	 * @param ids
	 * @return
	 */
	private List<String> buildlist(String ids)
	{
		List<String> lit = new ArrayList<String>();
		for (String key : ids.split(","))
		{
			lit.add(key);
		}
		return lit;
		
	}
	
	// / <summary>
	// / 建立界面
	// / </summary>
	private void CreateUI()
	{
		List<Checkbox> cbs = new ArrayList<Checkbox>();
		List<Label> lbs = new ArrayList<Label>();
		List<Vbox> vboxs = new ArrayList<Vbox>();
		List<MonitorEdit> mes = new ArrayList<MonitorEdit>();
		for (MonitorTemplate mt : monitorTemplates)
		{
			if (mt == null)
			{
				continue;
			}
			String Extrafunc = mt.get_Property().get("sv_extrafunc");
			String tempid = mt.get_sv_id();
			String name = mt.get_sv_name();
			String description = mt.get_sv_description() == null ? name : mt.get_sv_description();
			
			if (Extrafunc == null || Extrafunc.isEmpty())
			{
				Label lb = new Label();
				lb.setId(tempid);
				lb.setValue(description);
				
				Checkbox cb = new Checkbox();
				cb.addEventListener("onCheck", new mCheckboxOnCheck());
				cb.setName(tempid);
				cb.setLabel(name);
				cb.setAttribute("template", mt);
				cb.setStyle("margin-left:5px;");
				cb.setAttribute("more", "false");
				if (idsellist.contains(tempid))
				{
					cb.setChecked(true);
					SelectedCheckboxs.add(cb);
				}
				
				parentcontainers.appendChild(lb);
				parentcontainers.appendChild(cb);
			} else
			{
				
				Panel panel = new Panel();
				Panelchildren child = new Panelchildren();
				child.setParent(panel);
				Vbox vbox = new Vbox();
				vbox.setParent(child);
				Checkbox cb = new Checkbox();
				cb.setName(tempid);
				cb.setLabel(description);
				cb.setAttribute("more", "true");
				cb.setAttribute("template", mt);
				cb.addEventListener("onCheck", new CheckboxOnCheck());
				if (idsellist.contains(tempid))
				{
					cb.setChecked(true);
				}
				SelectedCheckboxs.add(cb);
				Label lb = new Label();
				lb.setStyle("margin-left:10px;");
				lb.setValue("load...");
				vbox.appendChild(cb);
				vbox.appendChild(lb);
				lb.setFocus(true);
				// lb.addEventListener("onFocus", new CheckboxOnCreate(cb, lb, vbox));
				// Timer time = new Timer();
				// time.setParent(WFastAdd);
				// time.setDelay(500);
				// time.setRepeats(false);
				// time.setRunning(false);
				// time.addEventListener("onTimer", new CheckboxOnCreate(cb, lb, vbox));
				// time.start();
				String tmpid = cb.getName();
				MonitorEdit medit = null;
				try
				{
					medit = entityedit.AddMonitor(tmpid);
				} catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try
				{
					if (medit!=null) medit.startMonitorDynamicData(view);
					cbs.add(cb);
					lbs.add(lb);
					mes.add(medit);
					vboxs.add(vbox);
				} catch (Exception ex)
				{
					lb.setVisible(false);
				}
				
				panel.setParent(parentcontainers);
				
			}
		}
		if (cbs.size() > 0)
		{
			Timer time = new Timer();
			time.setParent(WFastAdd);
			time.setDelay(1000);
			time.setRepeats(true);
			time.setRunning(false);
			time.addEventListener("onTimer", new ontimes(cbs, lbs, vboxs, mes));
			time.start();
		}
	}
	
	private class ontimes implements EventListener
	{
		List<Checkbox>		cbs;
		List<Label>			lbs;
		List<Vbox>			vboxs;
		List<Checkbox>		Subcbs	= null ;
		List<MonitorEdit>	medits;
		
		public ontimes(List<Checkbox> cbs, List<Label> lbs, List<Vbox> vboxs, List<MonitorEdit> medits)
		{
			this.cbs = cbs;
			this.lbs = lbs;
			this.vboxs = vboxs;
			this.medits = medits;
		}
		
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			Boolean stopgetdy = false;
			for (int i = 0; i < lbs.size(); i++)
			{
				if (lbs.get(i).isVisible())
				{
					stopgetdy = true;
				}
			}
			if (!stopgetdy)
			{
				((Timer) arg0.getTarget()).setRepeats(false);
				((Timer) arg0.getTarget()).stop();
				return;
				
			}
			// TODO Auto-generated method stub
			for (int i = 0; i < cbs.size(); i++)
			{
				if (!lbs.get(i).isVisible())
				{
					continue;
				}
//				logger.info(cbs.get(i).getLabel());
				Map<String, String> dydata = null;
				String tmid=medits.get(i).getMonitorType();
				try
				{
					dydata = medits.get(i).getMonitorDynamicData(view);
				} catch (Exception ex)
				{
//					logger.info(ex.getStackTrace());
					//lbs.get(i).setVisible(false);
					lbs.get(i).setValue("load data fail ");
				}
				if (dydata == null)
				{
					logger.info("null");
					continue;
					
				}
				TreeMap<String, String> sortdydata = new TreeMap<String, String>();
				
				Checkbox cb = null;
				for (String key : dydata.keySet())
				{
					String Key = dydata.get(key);
					String Value = key;
					if (!sortdydata.containsKey(Key))
					{
						sortdydata.put(Key, Value);
						
					}
				}
				
				lbs.get(i).setVisible(false);
				Subcbs=new ArrayList<Checkbox>();
				for (String key : sortdydata.keySet())
				{
					cb = new Checkbox();
					cb.setStyle("margin-left:10px;");
					cb.setName(sortdydata.get(key));
					cb.setChecked(cbs.get(i).isChecked());
					Subcbs.add(cb);
					cb.setLabel(key);
					vboxs.get(i).appendChild(cb);
				}
				List<Checkbox> findSubcbs = null;
				String name=((List<Checkbox>) cbs).get(i).getName();
				String atrrname="subcbs"+name;
				try
				{
					findSubcbs = (List<Checkbox>) cbs.get(i).getAttribute(atrrname);
				} catch (Exception e)
				{
					
				}
				if (findSubcbs == null)
				{
					
					cbs.get(i).setAttribute(atrrname, Subcbs);
//					logger.info(cbs.get(i).getName());
//					logger.info(atrrname);
//					logger.info(Subcbs.size());
				}
				
			}
			
		}
	}
	
	private class mCheckboxOnCheck implements EventListener
	{
		
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			// TODO Auto-generated method stub
			Checkbox cb = (Checkbox) arg0.getTarget();
			if (cb.isChecked())
			{
				SelectedCheckboxs.add(cb);
			} else
			{
				SelectedCheckboxs.remove(cb);
				
			}
		}
	}
	
	private class CheckboxOnCheck implements EventListener
	{
		
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			// TODO Auto-generated method stub
			Checkbox cb = (Checkbox) arg0.getTarget();
			String atrriname="subcbs"+cb.getName();
			List<Checkbox> Subcbs = (List<Checkbox>) cb.getAttribute(atrriname);
			logger.info(atrriname);
			if (Subcbs == null)
			{
				return;
			}
			
			for (Checkbox checkbox : Subcbs)
			{
				checkbox.setChecked(cb.isChecked());
			}
		}
	}
	
	private class CheckboxOnCreate implements EventListener
	{
		Checkbox		cb;
		Label			lb;
		Vbox			vbox;
		Timer			dytime;
		List<Checkbox>	Subcbs	= new ArrayList<Checkbox>();
		
		public CheckboxOnCreate(Checkbox cb, Label lb, Vbox vbox)
		{
			this.cb = cb;
			this.lb = lb;
			this.vbox = vbox;
		}
		
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			// String tmpid = cb.getName();
			// MonitorEdit medit = entityedit.AddMonitor(tmpid);
			// try
			// {
			// medit.startMonitorDynamicData(view);
			// } catch (Exception ex)
			// {
			// lb.setVisible(false);
			// return;
			// }
			// dytime = new Timer();
			// dytime.setParent(WFastAdd);
			// dytime.setDelay(100);
			// // dytime.setRepeats(true);
			// dytime.setRunning(false);
			// dytime.addEventListener("onTimer", new ontime(cb, lb, vbox, medit));
			// dytime.start();
			
			// dytime.
			// TODO Auto-generated method stub
			String tmpid = cb.getName();
			MonitorEdit medit = entityedit.AddMonitor(tmpid);
			Map<String, String> dydata = null;
			try
			{
				medit.startMonitorDynamicData(view);
				dytime = new Timer();
				
				while (dydata == null)
				{
					logger.info(" get monitor Dynamic Data ");
					Thread.sleep(1000);
					dydata = medit.getMonitorDynamicData(view);
				}
			} catch (Exception ex)
			{
				lb.setVisible(false);
			}
			TreeMap<String, String> sortdydata = new TreeMap<String, String>();
			if (dydata == null)
			{
				lb.setVisible(false);
				return;
			}
			Checkbox cb = null;
			for (String key : dydata.keySet())
			{
				String Key = dydata.get(key);
				String Value = key;
				if (!sortdydata.containsKey(Key))
				{
					sortdydata.put(Key, Value);
					
				}
			}
			
			lb.setVisible(false);
			for (String key : sortdydata.keySet())
			{
				cb = new Checkbox();
				cb.setStyle("margin-left:10px;");
				cb.setName(sortdydata.get(key));
				cb.setChecked(this.cb.isChecked());
				Subcbs.add(cb);
				cb.setLabel(key);
				vbox.appendChild(cb);
			}
			
			this.cb.setAttribute("subcbs", Subcbs);
			
		}
		
	}
	
	private class ontime implements EventListener
	{
		Checkbox		cb;
		Label			lb;
		Vbox			vbox;
		List<Checkbox>	Subcbs		= new ArrayList<Checkbox>();
		MonitorEdit		medit;
		private Boolean	stopgetdy	= false;
		
		public ontime(Checkbox cb, Label lb, Vbox vbox, MonitorEdit medit)
		{
			this.cb = cb;
			this.lb = lb;
			this.vbox = vbox;
			this.medit = medit;
		}
		
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			// TODO Auto-generated method stub
			if (stopgetdy)
			{
				((Timer) arg0.getTarget()).stop();
				return;
				
			}
			Map<String, String> dydata = null;
			try
			{
				dydata = medit.getMonitorDynamicData(view);
			} catch (Exception ex)
			{
				lb.setVisible(false);
				stopgetdy = true;
				return;
			}
			TreeMap<String, String> sortdydata = new TreeMap<String, String>();
			if (dydata == null)
			{
				return;
			} else
			{
				stopgetdy = true;
			}
			Checkbox cb1 = null;
			for (String key : dydata.keySet())
			{
				String Key = dydata.get(key);
				String Value = key;
				if (!sortdydata.containsKey(Key))
				{
					sortdydata.put(Key, Value);
					
				}
			}
			
			lb.setVisible(false);
			for (String key : sortdydata.keySet())
			{
				cb1 = new Checkbox();
				cb1.setStyle("margin-left:10px;");
				cb1.setName(sortdydata.get(key));
				cb1.setChecked(this.cb.isChecked());
				Subcbs.add(cb1);
				cb1.setLabel(key);
				// cb.setParent(vbox);
				vbox.appendChild(cb1);
			}
			
			this.cb.setAttribute("subcbs", Subcbs);
		}
	}
	
	/*
	 * 构建监测器
	 */
	private void buildMonitor(MonitorEdit monitoredit, MonitorTemplate template)
	{
		// 基本数据
		String network = null;
		try
		{
			network = entityedit.getProperty().get("sv_network");
		} catch (Exception ex)
		{
		}
		if (network == null)
		{
			monitoredit.getProperty().put("sv_intpos", "1");
		} else
		{
			if (network.toLowerCase().equals("true"))
			{
				monitoredit.getProperty().put("sv_intpos", "0");
			} else
			{
				monitoredit.getProperty().put("sv_intpos", "1");
			}
		}
		String name = template.get_sv_name();
		monitoredit.getProperty().put("sv_name", name);
		monitoredit.setName(name);
		monitoredit.getProperty().put("sv_disable", "false");
		monitoredit.getProperty().put("sv_endtime", "");
		monitoredit.getProperty().put("sv_starttime", "");
		monitoredit.getProperty().put("sv_dependson", "");
		// 参数
		monitoredit.getParameter().put("sv_checkerr", "true");
		monitoredit.getParameter().put("sv_description", "");
		monitoredit.getParameter().put("sv_reportdesc", "");
		monitoredit.getParameter().put("sv_plan", "7x24");
		monitoredit.getParameter().put("sv_errfrequint", "1");
		monitoredit.getParameter().put("sv_errfreqsave", "");
		monitoredit.getParameter().put("sv_errfreq", "0");
		// 动态参数
		buildDydata(monitoredit, template);
		// good error
		buildcondition(monitoredit.getGoodConditon(), template.get_good_conditon());
		buildcondition(monitoredit.getWarningConditon(), template.get_warning_conditon());
		buildcondition(monitoredit.getErrorConditon(), template.get_error_conditon());
	}
	
	public void buildcondition(Map<String, String> monitoreditstate, Map<String, String> expr)
	{
		if (expr == null)
		{
			return;
		}
		
		for (String key : expr.keySet())
		{
			String valu = expr.get(key);
			if (key.startsWith("sv_operate"))
			{
				monitoreditstate.put(key, valu);
			}
			if (key.startsWith("sv_conditioncount"))
			{
				monitoreditstate.put(key, valu);
			}
			if (key.startsWith("sv_expression"))
			{
				monitoreditstate.put(key, valu);
			}
			if (key.startsWith("sv_paramname"))
			{
				monitoreditstate.put(key, valu);
			}
			if (key.startsWith("sv_paramvalue"))
			{
				monitoreditstate.put(key, valu);
			}
			if (key.startsWith("sv_relation"))
			{
				monitoreditstate.put(key, valu);
			}
		}
		
	}
	
	private void buildDydata(MonitorEdit monitoredit, MonitorTemplate template)
	{
		for (Map<String, String> item : template.get_Parameter_Items())
		{
			String name = item.get("sv_name");
			String val = item.get("sv_value");
			monitoredit.getParameter().put(name, val);
		}
		for (Map<String, String> item : template.get_Advance_Parameter_Items())
		{
			String name = item.get("sv_name");
			String val = item.get("sv_value");
			monitoredit.getAdvanceParameter().put(name, val);
		}
	}
	
	/**
	 * ; 构建多检测器数据
	 */
	private void buildMonitorsData()
	{
		for (Checkbox cb : SelectedCheckboxs)
		{
			MonitorTemplate mt = null;
			MonitorEdit monitoredit = null;
			List<Checkbox> Subcbs = null;
			String ismore = (String) cb.getAttribute("more");
			try
			{
				mt = (MonitorTemplate) cb.getAttribute("template");
			} catch (Exception ex)
			{
				
			}
			if (mt == null)
			{
				continue;
			}
			try
			{
				monitoredit = entityedit.AddMonitor(mt.get_sv_id());
			} catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (monitoredit == null)
			{
				continue;
			}
			if (ismore.equals("false"))
			{
				buildMonitor(monitoredit, mt);
				try
				{
					monitoredit.teleSave(view);
					nodes.add(monitoredit);
				} catch (Exception ex)
				{
					String smessage = ex.getMessage();
					if (smessage == null)
					{
						smessage = "";
					}
					if (smessage.contains("Less permission"))
					{
						smessage = "您的监测器点数不够!";
					}
					try
					{
						Messagebox.show("保存监测器失败!", "提示", Messagebox.OK, Messagebox.EXCLAMATION);
						System.err.print(smessage);
						return;
					} catch (InterruptedException e2)
					{
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					return;
				}
			} else
			{
				MonitorEdit monitoredit1 = null;
				String templateid = mt.get_sv_id();
				try
				{
					Subcbs = (List<Checkbox>) cb.getAttribute("subcbs"+templateid);
					logger.info(cb.getId());
				} catch (Exception e)
				{
					
				}
				if (Subcbs == null)
				{
					continue;
				}
				
				
				for (Checkbox subcb : Subcbs)
				{
					
					if (subcb.isChecked())
					{
						try
						{
							monitoredit1 = entityedit.AddMonitor(templateid);
						} catch (Exception e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (monitoredit1 == null)
						{
							continue;
						}
						buildMonitor(monitoredit1, mt);
						String dyname = mt.get_Property().get("sv_extrasave");
//						String entityname = dyname.replace("_", "");
						String entityname=mt.get_sv_name();
						String mname = entityname + ":" + subcb.getLabel();
						monitoredit1.getProperty().put("sv_name", mname);
						monitoredit1.setName(mname);
						monitoredit1.getParameter().put(dyname, subcb.getName());
						
						//
						try
						{
							monitoredit1.teleSave(view);
							nodes.add(monitoredit1);
						} catch (Exception e1)
						{
							e1.printStackTrace();
							String smessage = e1.getMessage();
							if (smessage == null)
							{
								smessage = "";
							}
							if (smessage.contains("Less permission"))
							{
								smessage = "您的监测器点数不够!";
							}
							try
							{
								Messagebox.show("保存监测器失败！", "提示", Messagebox.OK, Messagebox.EXCLAMATION);
								System.err.print(smessage);
								return;
							} catch (InterruptedException e2)
							{
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}
							return;
						}
					}
				}
			}
			
		}
	}
	
	public void onClick$btnclose()
	{
		WFastAdd.detach();
	}
	
	public void onClick$btnadd()
	{
		if (issave)
		{
			return;
		} else
		{
			issave = true;
			btnadd.setDisabled(true);
		}
		
		String templateId = (String)WFastAdd.getAttribute("templateId");
		int point = 0;
		boolean isNetDevice = false;
		Map<String, String> netDeviceTemplate = TemplateManager.getEntityGroupTemplateLabel().get("网络设备");
		for(String deviceId : netDeviceTemplate.keySet()){
			if(deviceId!=null && deviceId.equals(templateId)){
				isNetDevice = true;
				break;
			}
		}
		int availablePoint = new License().getAvalibelPoint();
		int availableDevicePoint = new License().getAvalibelDevicePoint();
		if(isNetDevice){
			point += SelectedCheckboxs.size()/30 + 1;
			if(availableDevicePoint == 0 || availableDevicePoint < point){
				try {
					Messagebox.show("点数不足，无法进行操作", "提示", Messagebox.OK,	Messagebox.INFORMATION);
					return;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}else{
			point += SelectedCheckboxs.size();
			if(availablePoint == 0 || availablePoint < point){
				try {
					Messagebox.show("点数不足，无法进行操作", "提示", Messagebox.OK,	Messagebox.INFORMATION);
					return;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		buildMonitorsData();
		if (nodes.size() > 0)
		{
			INode[] nodess = new INode[nodes.size()];
			int i = 0;
			StringBuilder ids=new StringBuilder();
			String[] idss=new String[nodes.size()];
			for (INode node : nodes)
			{
				EccTreeItem item = new EccTreeItem(null, "", "", "");
				item.setValue(node);
				children.add(item);
				item.setTitle(node.getName());
				nodess[i] = node;
				idss[i]=node.getSvId();
				ids.append(node.getSvId()+",");
				i++;
			}
			
				String loginname = view.getLoginName();
                String name=entityedit.getName();
				String id=entityedit.getSvId();
				String	minfo = "快速添加监测器：" +"(" +ids.toString() + ") parent is " + name + "(" + id + ")";
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.fast_add, OpObjectId.monitor);
				
			
			
			eccTimer.refresh(nodess, ChangeDetailEvent.TYPE_ADD);
			String qname="";
			try
			{
				qname = entityedit.refresh(idss);
			} catch (Exception e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			RetMapInMap retData = null;
			try
			{
				// qname
				if (qname!=null && qname!="")
				retData = entityedit.getRefreshedData(qname);
			}
			catch(Exception e)
			{}
			
			final Window win = (Window) Executions.createComponents(EntityRefreshi_TargetUrl, null, null);
			win.setAttribute("inode", entityedit);
			win.setAttribute("view", view);
			win.setAttribute("eccTimer", eccTimer);
			win.setAttribute("children", children);
			try
			{
				win.doModal();
			} catch (SuspendNotAllowedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		WFastAdd.detach();
	}
}
