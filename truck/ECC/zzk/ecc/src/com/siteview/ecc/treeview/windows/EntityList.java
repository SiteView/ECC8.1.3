package com.siteview.ecc.treeview.windows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.base.template.EntityTemplate;
import com.siteview.base.template.TemplateManager;
import com.siteview.base.tree.INode;
import com.siteview.ecc.general.License;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svecc.zk.test.SVDBViewFactory;

public class EntityList extends GenericForwardComposer
{
	private final static Logger logger = Logger.getLogger(EntityList.class);
	
	// 绑定组件
	Window					entityList;
	Panelchildren			p1;
	Panel					p2;
	// 其他
	private Panel			panel;
	private Grid			grid;
	private Panelchildren	panelChild;
	private Columns			columns;
	private Column			column;
	private Rows			rows;
	private Row				row;
	private Include			eccbody;
	private Button			btnclose;
	View					view;
	INode					node;
	static String			EntityList_TargetUrl	= "/main/TreeView/WAddEntity.zul";
	String					error_message;
	Tree					tree;
	
	public EntityList()
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
		if (!node.getType().equals(INode.GROUP) && !node.getType().equals(INode.SE))
		{
			error_message = "节点类型非法！";
			return;
		}
		
	}
	
	public void onCreate$entityList() throws InterruptedException
	{
		if (error_message != null)
		{
			return;
		}
		btnclose = new Button("取消");
		btnclose.setWidth("60px");
		btnclose.setHeight("23px");
		btnclose.setClass("btnDefault");
		btnclose.setParent(p1);
		btnclose.addEventListener("onClick", new onclick());
		createUI();
		btnclose = new Button("取消");
		btnclose.setWidth("60px");
		btnclose.setHeight("23px");
		btnclose.setClass("btnDefault");
		btnclose.setParent(p1);
		btnclose.addEventListener("onClick", new onclick());
		tree = (org.zkoss.zul.Tree) entityList.getDesktop().getPage("eccmain").getFellow("tree");
		// height="100px" contentStyle=
		p2.setHeight("100%");
	}
	
	public void onClose$entityList() throws InterruptedException
	{
		Toolkit.getToolkit().expandTreeAndShowList(tree.getDesktop(), node);
		
	}
	
	private class onclick implements EventListener
	{
		
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			// TODO Auto-generated method stub
			// eccbody.setSrc("/main/eccbody.zul?type=" + node.getType() + "&id=" + node.getSvId());
			
			Toolkit.getToolkit().expandTreeAndShowList(tree.getDesktop(), node);
		}
		
	}
	
	/**
	 * 生成设备菜单
	 * 
	 * @param itemMenu
	 */
	private void createUI()
	{
		Map<String, Map<String, String>> entitGroupTemplate = TemplateManager.getEntityGroupTemplateLabel();
		Map<String, Map<String, String>> temp = new LinkedHashMap<String, Map<String, String>>();
		temp.put("服务器", entitGroupTemplate.get("服务器"));
		temp.put("网络设备", entitGroupTemplate.get("网络设备"));
		temp.put("数据库", entitGroupTemplate.get("数据库"));
		temp.put("URL", entitGroupTemplate.get("URL"));
		temp.put("中间件", entitGroupTemplate.get("中间件"));
		for (String gid : entitGroupTemplate.keySet())
		{
			if (!temp.containsKey(gid))
			{
				temp.put(gid, entitGroupTemplate.get(gid));
			}
			logger.info(gid);
			
		}
		int i = 0;
		for (String gid : temp.keySet())
		{
			panel = new Panel();
			panel.setTitle(gid);
			panel.setCollapsible(true);
			
			if (i >= 4)
			{
				panel.setOpen(false);
				
			}
			i = i + 1;
			panelChild = new Panelchildren();
			panelChild.setParent(panel);
			
			grid = new Grid();
			columns = new Columns();
			columns.setParent(grid);
			column = new Column();
			column.setWidth("20%");
			column.setParent(columns);
			column = new Column();
			column.setParent(columns);
			grid.setParent(panelChild);
			rows = new Rows();
			rows.setParent(grid);
			
			Map<String, String> entityTemplate = entitGroupTemplate.get(gid);
			if(entityTemplate==null || entityTemplate.isEmpty())
			{
				continue;
			}
			panel.setParent(p1);
			
			LinkedHashMap<String, String> tempMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> resultMap = new LinkedHashMap<String, String>();
			ArrayList<String> tempList = new ArrayList<String>();
			//对map中的value进行排序
			for(String eid: entityTemplate.keySet()){
				tempMap.put(entityTemplate.get(eid), eid);
				tempList.add(entityTemplate.get(eid));
			}
			Collections.sort(tempList);
			for(String eid: tempList){
				resultMap.put(tempMap.get(eid), eid);
			}
			
			for (String eid : resultMap.keySet())
			{
				row = new Row();
				row.setParent(rows);
				Toolbarbutton tbb = new Toolbarbutton();
				String lbname = entityTemplate.get(eid);
				tbb.setLabel(lbname);
				tbb.setId(eid);
				// Label lbname=new Label();
				// lbname.setId(eid);
				tbb.addEventListener("onClick", new itemOnClick());
				row.appendChild(tbb);
				EntityTemplate entem = TemplateManager.getEntityTemplate(eid);
				String decription = entem.get_sv_description() == null ? entem.get_sv_name() : entem.get_sv_description();
				Label lbdescription = new Label(decription);
				if (decription.contains("IDS"))
				{
					lbdescription.setValue(lbname);
				}
				tbb.setTooltiptext(decription);
				row.appendChild(lbdescription);
				
			}
			
		}
		
	}
	
	public class itemOnClick implements EventListener
	{
		@Override
		public void onEvent(Event arg0) throws Exception
		{
			// TODO Auto-generated method stub
			String TemplateId = ((Toolbarbutton) arg0.getTarget()).getId();
			
			boolean isNetDevice = false;
			Map<String, String> netDeviceTemplate = TemplateManager.getEntityGroupTemplateLabel().get("网络设备");
			for(String deviceId : netDeviceTemplate.keySet()){
				if(deviceId!=null && deviceId.equals(TemplateId)){
					isNetDevice = true;
					break;
				}
			}
			int availableDevicePoint = new License().getAvalibelPoint();
			if(isNetDevice && availableDevicePoint < 2){
				try {
					int i = Messagebox.show("点数已低于2点，是否继续", "询问", Messagebox.OK | Messagebox.CANCEL,	Messagebox.QUESTION);
					if(i == Messagebox.CANCEL){
						return;
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else if(isNetDevice && availableDevicePoint < 1){
				try {
					Messagebox.show("点数不足，无法进行操作", "提示", Messagebox.OK,	Messagebox.INFORMATION);
					return;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// Collection eccbody= ((Label)arg0.getTarget()).getFellows();eccTimer
			final Window win = (Window) Executions.createComponents(EntityList_TargetUrl, null, null);
			EccTimer time = (EccTimer) entityList.getDesktop().getPage("eccmain").getFellow("header_timer");
			win.setAttribute("inode", node);
			win.setAttribute("view", view);
			win.setAttribute("isedit", false);
			win.setAttribute("templateId", TemplateId);
			win.setAttribute("tree", tree);
			win.setAttribute("eccTimer", time);
			win.doModal();
		}
		
	}
	
}
