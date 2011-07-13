package com.siteview.ecc.treeview;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import com.siteview.actions.EccActionManager;
import com.siteview.base.manage.View;
import com.siteview.base.template.MonitorTemplate;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.controlpanel.ControlLayoutComposer;
import com.siteview.ecc.monitorbrower.MonitorBean;
import com.siteview.ecc.treeview.windows.ConstantValues;
import com.siteview.ecc.util.Toolkit;

/**
 * 最近浏览过的监测器Listbox的model
 * @author MaKun
 *
 */
public class RecentViewListModel extends ListModelList implements ListitemRenderer {

	private static final long serialVersionUID = 1L;
	
	public RecentViewListModel(LinkedList<EccTreeItem> recentlyViewMonitors){
		Session session = Executions.getCurrent().getDesktop().getSession();
		View view = Toolkit.getToolkit().getSvdbView(session);
		String loginName = view.getLoginName();
		LinkedList<EccTreeItem> list = (LinkedList<EccTreeItem>)session.getAttribute("recentlyViewMonitors");
		if(list == null){
			list = new LinkedList<EccTreeItem>();
			
			String monitorIdCookielist = Toolkit.getToolkit().getCookie("recentlyViewMonitorsCookie%"+loginName);
			if(monitorIdCookielist != null){
				String [] ArrayCookieId = monitorIdCookielist.split("%");
				for(int i=0;i<ArrayCookieId.length;i++){
					if(ArrayCookieId[i] == null ||"".equals(ArrayCookieId[i])) continue;
					RecentViewFindEccTreeItem rvfitem =new  RecentViewFindEccTreeItem(ArrayCookieId[i]);
					EccTreeItem eccTreeItem = rvfitem.getEccTreeItem();
					if(eccTreeItem != null){
						list.add(eccTreeItem);
					}
				}
				session.setAttribute("recentlyViewMonitors", list);
			}
		}
		
		while(list.size()>5){
			list.removeLast();
		}
		String cookieId ="";
		for(EccTreeItem itemId:list){
			String tempId = itemId.getId();
			if(tempId == null || "".equals(itemId)) continue;
			if("".equals(cookieId)){//第一次
				cookieId = tempId ;
			}else{
				cookieId =cookieId+"%"+tempId;
			}
		}
		if(!"".equals(cookieId)){
			Toolkit.getToolkit().setCookie("recentlyViewMonitorsCookie%"+loginName, cookieId, 36000);
		}

		addAll(list);
	}

	@Override
	public void render(Listitem parent, Object data) throws Exception {
		EccTreeItem item=(EccTreeItem)data;
		parent.setValue(item);
		parent.setCheckable(true);
		Listcell iconCell = new Listcell();
		iconCell.setParent(parent);
		iconCell.setImage(EccWebAppInit.getInstance().getStatusImage(Toolkit.getToolkit().changeStatusToString(item.getStatus())));
		
		Listcell cell=new Listcell();
		cell.setValue(item);
		cell.setParent(parent);
		Label lbl=new Label(item.getTitle());
		lbl.setAttribute("eccTreeItem", item);
		lbl.setParent(cell);
		cell.setTooltiptext(item.getTitle() + "  所属设备：" + item.getParent().getTitle());
		cell.setAttribute("eccTreeItem", item);
		String monitorId 	= item.getId();
		String entityId = monitorId.substring(0, monitorId.lastIndexOf("."));

		cell.addEventListener(Events.ON_CLICK, new RecentlyViewMonitorEvent2(entityId,monitorId));
	}
}
	
/*	class RecentlyViewMonitorEvent implements EventListener {
		private INode iinfo;
		private EccTreeItem item;

		public RecentlyViewMonitorEvent(){
			
		}

		@Override
		public void onEvent(Event event) throws Exception {
			try{
				item = (EccTreeItem)event.getTarget().getAttributes().get("eccTreeItem");
				iinfo = item.getValue();
				Page controlPage = null;
				try{
					controlPage = Executions.getCurrent().getDesktop().getPage("controlPage");
				}catch(Exception e){
					controlPage = null;
				}
				if(controlPage == null){
					System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ clc is null");
					String url = EccActionManager.getInstance().getUrl("entity");
					Include eccBody = (Include) (event.getTarget().getDesktop().getPage("eccmain").getFellow("eccBody"));
					eccBody.setSrc(url);
					Tree tree = (Tree) (event.getTarget().getDesktop().getPage("eccmain").getFellow("tree"));
					Toolkit.getToolkit().autoExpandTreeNode(tree, iinfo, item.getItemId());
					
					
				}else{
					ControlLayoutComposer clc = ((ControlLayoutComposer)Executions.getCurrent().getDesktop().getPage("controlPage").getFellow("controlLayout").getAttribute("Composer"));
					System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ clc is not null");
					Tree tree = (Tree) (event.getTarget().getDesktop().getPage("eccmain").getFellow("tree"));
					if (Toolkit.getToolkit().autoExpandTreeNode(tree, iinfo, item.getItemId())){
						Session session = Executions.getCurrent().getDesktop().getSession();
						session.setAttribute("selectedItem", item);
						clc.refreshByClickRecentlyViewMonitor(item);

						clc.getActionMenuDiv().refreshAll(item.getParent());
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}

			
			
			//两种 解决方案
			//1.能够获取 clc 即显示的是 eccBody.zul页面
			//2.不能获取 clc 即显示的是其他节点的页面
			if(clc == null){
				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ clc is null");
				String url = EccActionManager.getInstance().getUrl("entity");
				Include eccBody = (Include) (event.getTarget().getDesktop().getPage("eccmain").getFellow("eccBody"));
				eccBody.setSrc(url);
			}else{
				System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ clc is not null");
				Tree tree = (Tree) (event.getTarget().getDesktop().getPage("eccmain").getFellow("tree"));
				if (Toolkit.getToolkit().autoExpandTreeNode(tree, iinfo, item.getItemId())){
					Session session = Executions.getCurrent().getDesktop().getSession();
					session.setAttribute("selectedItem", item);
					clc.refreshByClickRecentlyViewMonitor(item);

					clc.getActionMenuDiv().refreshAll(item.getParent());
				}
			}

		}
	}
}*/

class RecentlyViewMonitorEvent2 implements EventListener
{
	private String 	entityId;
	private String  monitorId;
	private boolean globalFlag = false;//判断 entity 是否找到
	
	private INode iinfo;
	private EccTreeItem item;
	
	private ArrayList<Treeitem> treeItemList = new ArrayList<Treeitem>();
	
	public RecentlyViewMonitorEvent2(String entityId,String monitorId)
	{
		this.entityId = entityId;
		this.monitorId = monitorId;
	}
	
	@Override
	public void onEvent(Event event) throws Exception
	{
			item = (EccTreeItem)event.getTarget().getAttributes().get("eccTreeItem");
			iinfo = item.getValue();
			Page controlPage = null;
			try{
//				controlPage = Executions.getCurrent().getDesktop().getPage("controlPage");	
				controlPage = event.getTarget().getDesktop().getPage("controlPage");
			}catch(Exception e){
				controlPage = null;
			}
			if(controlPage != null){
				ControlLayoutComposer clc = ((ControlLayoutComposer)Executions.getCurrent().getDesktop().getPage("controlPage").getFellow("controlLayout").getAttribute("Composer"));
				Tree tree = (Tree) (event.getTarget().getDesktop().getPage("eccmain").getFellow("tree"));
				
				try{
					INode node = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop()).getNode(monitorId);
					if (node == null)
						throw new Exception("获取该监测器(编号为："+this.monitorId+")数据出现异常,该监测器可能已经被删除");
				}catch(Exception e){
					try{
						
						Messagebox.show(e.getMessage(), "提示", Messagebox.OK,Messagebox.INFORMATION);
						return;
					}catch(Exception ee){
					}
				}

				if (Toolkit.getToolkit().autoExpandTreeNode(tree, iinfo, item.getItemId())){
					Session session = Executions.getCurrent().getDesktop().getSession();
					session.setAttribute("doMap", null);
					session.setAttribute("selectedItem", item);
					session.setAttribute(ConstantValues.LatestBrowseMonitorId, monitorId);
					clc.refreshByClickRecentlyViewMonitor(item);
					clc.getActionMenuDiv().refreshAll(item.getParent());
				}else{
					//当前视图中不包含最近浏览的监测器，提示该检测器不存在于当前视图中
					Messagebox.show("编号为："+this.monitorId+"的监测器不属于当前视图!", "提示", Messagebox.OK,Messagebox.INFORMATION);
					return;
				}
			}else{
				try{
					Object tmpobj = event.getTarget().getDesktop().getSession().getAttribute("CurrentWindow");
					if(tmpobj!=null){
						event.getTarget().getDesktop().getSession().removeAttribute("CurrentWindow");
					}
					Include eccBody = (Include) (event.getTarget().getDesktop().getPage("eccmain").getFellow("eccBody"));
					Tree tree = (Tree) (event.getTarget().getDesktop().getPage("eccmain").getFellow("tree"));
					Collection children = tree.getTreechildren().getChildren();
					Treeitem root = null;
					for(Object obj : children){
						if(obj instanceof Treeitem){
							root = (Treeitem)obj;
							getTreeItem(root,entityId,item);
						}
					}
					if(this.globalFlag){
						for(Object obj : children){
							if(obj instanceof Treeitem){
								root = (Treeitem)obj;
								openTreeItem(root);
							}
						}
						String url = EccActionManager.getInstance().getUrl("entity");
						eccBody.setSrc(url);
					}else{
						try{
							Messagebox.show("获取该设备(编号为："+this.entityId+")数据出现异常,该设备可能已经被删除", "提示", Messagebox.OK,Messagebox.INFORMATION);
							return;
						}catch(Exception ee){}
					}
				}catch(Exception e){
					e.printStackTrace();
					try{
						Messagebox.show(e.getMessage(), "提示", Messagebox.OK,Messagebox.INFORMATION);
					}catch(Exception ee){}
				}
			}		
	}
	
	public Treeitem getTreeItem(Treeitem root,String entityId,EccTreeItem sessionitem)throws Exception{
		if(root==null) return null;
		boolean flag = root.isOpen();
		root.setOpen(true);
		
		Treechildren tChildren = root.getTreechildren();
		if(tChildren==null) return null;
		
		Collection children = tChildren.getItems();
		Object[] objArr = children.toArray();
		if(children==null || children.size()<=0) return null;
		
		
		for(Object obj : objArr)
		{
			if(obj instanceof Treeitem)
			{
				Treeitem item = (Treeitem)obj;
				EccTreeItem eccItem = (EccTreeItem)item.getValue();
				if(eccItem.getId().equals(entityId))
				{
					flag = true;
					globalFlag = true;
					item.setSelected(true);
					Session session = Executions.getCurrent().getDesktop().getSession();
					boolean monitorIdExit = false;
					for(EccTreeItem sonEccTreeItem:eccItem.getChildRen()){
						if(monitorId.equals(sonEccTreeItem.getId())){
							session.setAttribute(ConstantValues.LatestBrowseMonitorId, monitorId);
							session.setAttribute("selectedItem", sessionitem);
							monitorIdExit = true;
							root.setOpen(flag);
							return null;
						}
					}
					if(monitorIdExit == false){
						throw new Exception("获取该监测器(编号为："+this.monitorId+")数据出现异常,该监测器可能已经被删除");
					}
					treeItemList.add(item);
					Treeitem parentTreeItem = item.getParentItem();
					while(parentTreeItem!=null){
						treeItemList.add(parentTreeItem);
						parentTreeItem = parentTreeItem.getParentItem();
					}
					return item;
				}else
				{
					getTreeItem(item,entityId,sessionitem);
				}
			}
		}
		root.setOpen(flag);
		return null;
	}
	
	public void openTreeItem(Treeitem root){
		if(root==null) return;
		Treechildren tChildren = root.getTreechildren();
		if(tChildren==null) return;
		Collection children = tChildren.getItems();
		Object[] objArr = children.toArray();
		if(children==null || children.size()<=0) return ;
		for(Object obj : objArr)
		{
			if(obj instanceof Treeitem)
			{
				Treeitem item = (Treeitem)obj;
				openTreeItem(item);
			}
		}
		for(Treeitem treeitem:treeItemList){
			if(treeitem.equals(root)){
				treeitem.setOpen(true);
			}
		}
		return ;
	}
}
