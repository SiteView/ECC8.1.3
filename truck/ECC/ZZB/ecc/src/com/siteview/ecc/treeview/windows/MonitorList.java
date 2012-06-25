package com.siteview.ecc.treeview.windows;

import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.base.template.EntityTemplate;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.template.TemplateManager;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.EntityInfo;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svecc.zk.test.SVDBViewFactory;

public class MonitorList extends GenericForwardComposer
{
	// 绑定变量
	Window			monitorList;
	Rows			baserows;
	Button          btnclose;
	// 其他
	View			view;
	String			entityid;
	Row				row;
	Include			eccbody;
	
	INode			node;
	String			error_message;
	static String	monitorList_TargetUrl	= "/main/TreeView/WAddMonitor.zul";
	Tree tree ;
	
	public MonitorList()
	{
		String Id = Executions.getCurrent().getParameter("id");
		String sessionId = Executions.getCurrent().getParameter("sid");
		
		try
		{
			view = SVDBViewFactory.getView(sessionId);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		if (view == null)
		{
			error_message = "未登录或无效的会话！";
			return;
		}
		node = view.getNode(Id);
		if (node == null)
		{
			error_message = "节点不存在或无权访问！";
			return;
		}
		if (!node.getType().equals(INode.ENTITY))
		{
			error_message = "节点类型非法！";
			return;
		}
		
	}
	
	// 窗体创建
	public void onCreate$monitorList()
	{
		if (error_message!=null)
		{
			return;
		}
		CreateUI();
	 tree = (org.zkoss.zul.Tree) monitorList.getDesktop().getPage("eccmain").getFellow("tree");
	}
	
	public void onClose$monitorList()
	{
		Toolkit.getToolkit().expandTreeAndShowList(tree.getDesktop(), node);
	}
	
	
	private void CreateUI()
	{
		EntityInfo entity = view.getEntityInfo(node);
		
		EntityTemplate tpl = entity.getDeviceTemplate();
		Map<String, String> monitorTemplate = tpl.getSubMonitorTemplateLabel();
		for (String tid : monitorTemplate.keySet())
		{
			row = new Row();
			row.setParent(baserows);
			Toolbarbutton tbb = new Toolbarbutton(monitorTemplate.get(tid));
			tbb.setId(tid);
			tbb.addEventListener("onClick", new itemOnClick());
			//对添加监测器列表中，memory监测器名字显示为了中文描述进行修改
			if(tbb!=null && tbb.getLabel().equals("监测系统内存的使用率及剩余可用内存的大小")){
				tbb.setLabel("Memory");
			}
			row.appendChild(tbb);
			
			MonitorTemplate m = TemplateManager.getMonitorTemplate(tid);
			String description = m.get_sv_description().isEmpty() ? m.get_sv_name() : m.get_sv_description();
			
			Label lbdescription = new Label(description);
			tbb.setTooltiptext(description);
			row.appendChild(lbdescription);
			
		}
		
	}
	
	public class itemOnClick implements EventListener
	{
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			// TODO Auto-generated method stub
			String monitorTemplateId = ((Toolbarbutton) arg0.getTarget()).getId();
			// Collection eccbody= ((Label)arg0.getTarget()).getFellows();
			// eccbody.setSrc("/main/TreeView/WAddMonitor.zul?templateId="+entityTemplateId);
			final Window win = (Window) Executions.createComponents(monitorList_TargetUrl, null, null);
			EccTimer time=(EccTimer) monitorList.getDesktop().getPage("eccmain").getFellow("header_timer");
			win.setAttribute("inode", node);
			win.setAttribute("view", view);
			win.setAttribute("isedit", false);
			win.setAttribute("templateId", monitorTemplateId);
			win.setAttribute("tree", tree);
			win.setAttribute("eccTimer", time);
			win.doModal();
		}
		
	}
	
	public void onClick$btnclose()
	{
		Toolkit.getToolkit().expandTreeAndShowList(tree.getDesktop(), node);
		Session session = Executions.getCurrent().getDesktop().getSession();
		session.removeAttribute("doMap");
	}
}
