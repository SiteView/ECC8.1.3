package com.siteview.ecc.monitorbrower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.ecc.alert.control.AbstractListbox;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.monitorbrower.EntityLinkFuntion;
import com.siteview.ecc.monitorbrower.MonitorDetailLinkFuntion;
import com.siteview.ecc.monitorbrower.MonitorBean;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.util.Toolkit;


public class MonitorInfoListbox extends AbstractListbox {

	private static final long serialVersionUID = 3825957038353315311L;
	private View eccView = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
	private static String	RefreshMonitors_TargetUrl	= "/main/monitorbrower/refreshmonitor.zul";

	private List<MonitorBean> mbs = null;
	@Override
	public List<String> getListheader() {
		return new ArrayList<String>(Arrays.asList(new String[] { "状态",
				"组","设备", "名称", "编辑", "刷新", "更新时间" ,"描述"}));
	}
	
	@Override
	public void renderer() {
		
		/**
		 * 				if(mbs==null) return;
				Listitem it = mbc.getShowMonitorFilter().getSelectedItem();
				CVBean bean = (CVBean)it.getValue();
				String sort = bean.getSort();
				sort(mbs,sort);
				for(MonitorBean value : mbs){
					Listitem item = new Listitem();
					String monitorTemp = value.getMonitorId();
					String entityId = "";
					if(monitorTemp == null || "".equals(monitorTemp)){
						continue;
					}
					
					entityId = monitorTemp.substring(0, monitorTemp.lastIndexOf("."));
					if(entityId.contains(".") == false){
						continue;
					}
					item = BaseTools.addRow(mbc.getMonitorInfo()
							, value
							, getImage(value.getStatus())
							,value.getGroup()
//							,value.getEntity()
							,BaseTools.getWithEntityLink(value,new EntityLinkFuntion(entityId,value.getMonitorId()))
//							,value.getMonitorName()
							,BaseTools.getWithMonitorLink(value,new MonitorDetailLinkFuntion(entityId,value.getMonitorId(),"btndetail"))							
							,BaseTools.getWithLink(
									"","编辑","/main/images/alert/edit.gif"
									,new onEditButtonListener(eccView.getNode(value.getMonitorId())
											,value.getMonitorName(),eccView,getEccTimer()))
							,BaseTools.getWithLink(
									"","刷新","/main/images/button/ico/ref_bt.gif"
									,new onRefreshButtonListener(value.getMonitorId()
											,value.getMonitorId().substring(0,value.getMonitorId().lastIndexOf(".")),item))
							,value.getUpdateTime(),value.getDescript());
				}
		 */
		try {
			if(mbs == null) return;
			for(MonitorBean tmpKey : mbs){
				if(tmpKey == null) continue;
				String monitorId 	= tmpKey.getMonitorId();
				if(monitorId == null ||"".equals(monitorId)){
					continue;
				}
				String entityId = monitorId.substring(0, monitorId.lastIndexOf("."));
				if(entityId == null ||"".equals(entityId)){
					continue;
				}
				if(entityId.contains(".") == false){
					continue;
				}
				String monitorName 	= tmpKey.getMonitorName();
				String entityName  	= tmpKey.getEntity();

				Listitem item = new Listitem();
				item.setValue(tmpKey);
				for(String head : listhead){
					if(head.equals("状态")){
						Listcell cell = new Listcell();
						cell.setImage(getImage(tmpKey.getStatus()));
						cell.setParent(item);
					}
					if(head.equals("组")){
						Listcell cell = new Listcell(tmpKey.getGroup());
						cell.setTooltiptext(tmpKey.getGroup());
						cell.setParent(item);
					}
					if(head.equals("设备")){
						Listcell cell = new Listcell();
						cell.setTooltiptext(entityName);
						Component c2 = BaseTools.getWithEntityLink(entityName,new EntityLinkFuntion(entityId,monitorId));
						cell.appendChild(c2);
						cell.setParent(item);
					}
					if(head.equals("名称")){
						Listcell cell = new Listcell();
						cell.setTooltiptext(monitorName);
						Component c2 = BaseTools.getWithMonitorLink(monitorName,new MonitorDetailLinkFuntion(entityId,monitorId,"btndetail"));						

						cell.appendChild(c2);
						cell.setParent(item);
					}
					if(head.equals("编辑")){
						Listcell cell = new Listcell();
						cell.setTooltiptext(monitorName);
						Component c2 = BaseTools.getWithLink(
								"","编辑","/main/images/alert/edit.gif"
								,new onEditButtonListener(eccView.getNode(tmpKey.getMonitorId())
										,tmpKey.getMonitorName(),eccView,getEccTimer()));

						cell.appendChild(c2);
						cell.setParent(item);
					}
					if(head.equals("刷新")){
						Listcell cell = new Listcell();
						cell.setTooltiptext(monitorName);
						Component c2 = BaseTools.getWithLink(
								"","刷新","/main/images/button/ico/ref_bt.gif"
								,new onRefreshButtonListener(tmpKey.getMonitorId()
										,tmpKey.getMonitorId().substring(0,tmpKey.getMonitorId().lastIndexOf(".")),item));

						cell.appendChild(c2);
						cell.setParent(item);
					}
					if(head.equals("更新时间")){
						Listcell cell = new Listcell(tmpKey.getUpdateTime());
						cell.setTooltiptext(tmpKey.getUpdateTime());
						cell.setParent(item);
					}
					if(head.equals("描述")){
						Listcell cell = new Listcell(tmpKey.getDescript());
						cell.setTooltiptext(tmpKey.getDescript());
						cell.setParent(item);
					}
				}
				item.setParent(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<MonitorBean> getMbs() {
		return mbs;
	}

	public void setMbs(List<MonitorBean> mbs) {
		this.mbs = mbs;
	}
	
	private String getImage(String status){
		if (status == null || status.equals("null")) 
			return "/images/state_dark.gif";
		else if (status.equals("bad"))
			return "/images/state_grey.gif";
		else if	(status.equals("error"))
			return "/images/state_red.gif";
		else if (status.equals("ok"))
			return "/images/state_green.gif";
		else if (status.equals("warning"))
			return "/images/state_yellow.gif";
		else if(status.equals("disable"))
			return "/images/state_stop.gif";
		else
			return "/images/state_grey.gif";
	}
	public EccTimer getEccTimer(){
		return (com.siteview.ecc.timer.EccTimer)Executions.getCurrent().getDesktop().getPage("eccmain").getFellow("header_timer");
	}
	public class onEditButtonListener implements EventListener {
		private EccTimer	 timer;
		private INode 		 node;
		private View         view;
		private String       name;
		public onEditButtonListener(INode node, String name, View view,
				EccTimer timer) {
			this.name = name;
			this.node = node;
			this.timer = timer;
			this.view = view;
		}

		public void onEvent(Event event) throws Exception {
			try {
				final Window win = (Window) Executions.createComponents(
						"/main/monitorbrower/WAddMonitor.zul", null, null);
				win.setAttribute("inode", node);
				win.setAttribute("view", view);
				win.setAttribute("isedit", true);
				win.setAttribute("entityname", name);
				win.setAttribute("eccTimer", timer);
				win.doModal();
			} catch (Exception e) {
				Messagebox.show(e.getMessage(), "错误", Messagebox.OK,
						Messagebox.ERROR);
			}
		}
	}
	public class onRefreshButtonListener implements EventListener {
		private String id;
		private String parentId;
		private Listitem item;

		public onRefreshButtonListener(String id, String parentId,
				Listitem item) {
			this.id = id;
			this.parentId = parentId;
			this.item = item;
		}

		public void onEvent(Event event) {
			final Window win = (Window) Executions.createComponents(RefreshMonitors_TargetUrl, null, null);
			win.setAttribute("inode", eccView.getNode(parentId));
			win.setAttribute("view", ChartUtil.getView());
			win.setAttribute("eccTimer", getEccTimer());
			INode node = eccView.getNode(id);
			List<INode> list = new ArrayList<INode>();
			list.add(node);
			win.setAttribute("children", list);
			win.setAttribute("monitorItem", item);
			try
			{
				win.doModal();
			} catch (SuspendNotAllowedException e)
			{
				e.printStackTrace();
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}	
		}
	}
}
