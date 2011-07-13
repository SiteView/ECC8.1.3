package com.siteview.ecc.treeview.windows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.tree.INode;
import com.siteview.base.treeEdit.EntityEdit;
import com.siteview.base.treeEdit.MonitorEdit;
import com.siteview.ecc.general.License;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.util.Toolkit;

public class PLAddMonitor extends GenericForwardComposer
{
	private static String	RefreshMonitors_TargetUrl	= "/main/TreeView/refreshmonitors.zul";
	Listbox					lbmonitor;
	Button					btnok;
	Button					btnclose;
	Window					Wpladdmonitor;
	Panelchildren pc;
	View					view;
	INode					node;
	TreeMap<String, String>	sortdydata;
	EntityEdit				entityedit;
	MonitorTemplate			monitorTemplate;
	String					templateid					= null;
	EccTimer				eccTimer;
	Boolean                 issave=false;
	
	public PLAddMonitor()
	{
		
	}
	
	public void onCreate$Wpladdmonitor()
	{
		view = (View) Wpladdmonitor.getAttribute("view");
		node = (INode) Wpladdmonitor.getAttribute("inode");
		eccTimer = (EccTimer) Wpladdmonitor.getAttribute("eccTimer");
		Tree tree=(Tree)Wpladdmonitor.getDesktop().getPage("eccmain").getFellow("tree");
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
			entityedit = view.getEntityEdit(node);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sortdydata = (TreeMap<String, String>) Wpladdmonitor.getAttribute("dydata");
		monitorTemplate = (MonitorTemplate) Wpladdmonitor.getAttribute("monitorTemplate");
		templateid = monitorTemplate.get_sv_id();
		Listitem listitem = null;
		Listcell listcell = null;
		
		HashMap<String,String> FirstMap = new HashMap<String,String>();
		ArrayList<String> arrayList = new ArrayList<String>();
		for (String key : sortdydata.keySet())
		{
			int index = key.indexOf("_");
			if(index>0){
				String tempKey = key.substring(0, index);
				arrayList.add(tempKey);
				FirstMap.put(String.valueOf(tempKey), key);
			}else
			{	//处理普通情况逻辑
				arrayList.add(key);
				FirstMap.put(key, key);
			}
		}
		Collections.sort(arrayList, new Comparator<String>()
		{
			@Override
			public int compare(String o1, String o2) {
				try{
					int a = Integer.parseInt(o1);
					int b = Integer.parseInt(o2);
					if(a>b) return 1;
					else return 0;
				}catch(Exception e){
					return 0;
				}
			}
		});
		for(String key : arrayList)
		{
			key = FirstMap.get(key);
			listitem = new Listitem();
			listitem.setParent(lbmonitor);
			listitem.setWidth("100%");
			listitem.setLabel(key);
			listitem.setValue(sortdydata.get(key));
		}
//		for (String key : sortdydata.keySet())
//		{
//			listitem = new Listitem();
//			listitem.setParent(lbmonitor);
//			listitem.setWidth("100%");
//			listitem.setLabel(key);
//			listitem.setValue(sortdydata.get(key));
//			// listcell=new Listcell(key);
//			// listcell.setValue();
//			// listcell.setParent(listitem);
//		}
		
		Wpladdmonitor.setPosition("center");
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
		monitoredit.getProperty().put("sv_disable", "false");
		monitoredit.getProperty().put("sv_endtime", "");
		monitoredit.getProperty().put("sv_starttime", "");
		monitoredit.getProperty().put("sv_dependson", "");
		// 参数
		monitoredit.getParameter().put("sv_checkerr", "true");	//默认选中错误校验 
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
	
	public void onClick$btnclose()
	{
		Wpladdmonitor.detach();
	}
	
	public void onClick$btnok()
	{
		if(issave)
		{
			return;
		}else
		{
			issave=true;
			btnok.setDisabled(true);
		}
		MonitorEdit monitoredit1 = null;
		ArrayList<INode> children = new ArrayList<INode>();
		
		if (lbmonitor.getSelectedCount()==0)
		{
			try
			{
				Messagebox.show("没有可以添加的监测器！", "提示", Messagebox.OK, Messagebox.EXCLAMATION);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		
		//预判断所需点数，若点数不足则停止批量添加并返回
		String network = null;
		try
		{
			network = entityedit.getProperty().get("sv_network");
		} catch (Exception ex)
		{
		}
		int point = 0;
		int availablePoint = new License().getAvalibelPoint();
		int availableDevicePoint = new License().getAvalibelDevicePoint();
		if(network != null && "true".equals(network)){
			point += lbmonitor.getSelectedCount()/30 + 1;
			if(availableDevicePoint == 0 || availableDevicePoint < point){
				try {
					Messagebox.show("点数不足，无法进行操作", "提示", Messagebox.OK,	Messagebox.INFORMATION);
					return;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}else{
			point += lbmonitor.getSelectedCount();
			if(availablePoint == 0 || availablePoint < point){
				try {
					Messagebox.show("点数不足，无法进行操作", "提示", Messagebox.OK,	Messagebox.INFORMATION);
					return;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		for (int i = 0; i < lbmonitor.getItemCount(); i++)
		{
			Listitem item = lbmonitor.getItemAtIndex(i);
			if (item.isSelected())
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
				buildMonitor(monitoredit1, monitorTemplate);
				String dyname = monitorTemplate.get_Property().get("sv_extrasave");
				//String entityname = dyname.replace("_", "");
				String entityname=monitorTemplate.get_sv_name();
				String mname = entityname + ":" + item.getLabel();
				monitoredit1.getProperty().put("sv_name", mname);
				monitoredit1.setName(mname);
				monitoredit1.getParameter().put(dyname, (String) item.getValue());
				//
				try
				{
					monitoredit1.teleSave(view);
					children.add(monitoredit1);
				} catch (Exception e1)
				{
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
						Messagebox.show(smessage, "提示", Messagebox.OK, Messagebox.EXCLAMATION);
					} catch (InterruptedException e2)
					{
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					e1.printStackTrace();
					return;
				}
			}
		}
		if (children.size() > 0)
		{
			INode[] nodess = new INode[children.size()];
			String[] idss=new String[children.size()];
			int i = 0;
			StringBuilder ids=new StringBuilder();
			for (INode node : children)
			{
				nodess[i] = node;
				String nodeid=node.getSvId();
				idss[i]=nodeid;
				ids.append(nodeid+",");
				i++;
			}
			String loginname = view.getLoginName();
            String name=entityedit.getName();
			String id=entityedit.getSvId();
			String	minfo = "批量添加监测器：" +"(" +ids.toString() + ") parent is " + name + "(" + id + ")";
				AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.many_add, OpObjectId.monitor);
			
		
			eccTimer.refresh(nodess, ChangeDetailEvent.TYPE_ADD);
			Wpladdmonitor.detach();
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
			final Window win = (Window) Executions.createComponents(RefreshMonitors_TargetUrl, null, null);
			win.setAttribute("inode", node);
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
		Toolkit.getToolkit().expandTreeAndShowList(eccTimer.getDesktop(), node);
	}
	
	public void onSelect$lbmonitor(){
		this.issave = false;
		btnok.setDisabled(false);
	}
}
