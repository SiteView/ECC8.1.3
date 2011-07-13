package com.siteview.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.EntityInfo;
import com.siteview.base.treeInfo.GroupInfo;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.base.treeInfo.SeInfo;
import com.siteview.ecc.controlpanel.ControlLayoutComposer;
import com.siteview.ecc.controlpanel.MonitorReport;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.util.Message;
import com.siteview.ecc.util.Toolkit;

public class ActionClickListener implements EventListener
{
	private ArrayList<EccTreeItem>	checkedItem	= null;
	
	public ArrayList<EccTreeItem> getCheckedItem()
	{
		return checkedItem;
	}
	
	public void setCheckedItem(ArrayList<EccTreeItem> checkedItem)
	{
		this.checkedItem = checkedItem;
	}
	
	ActionPopup				popup						= null;
	String					action						= null;
	private final static String	TeleBackup_TargetUrl		= "/main/TreeView/monitorselecttree.zul";
	private final static String	LoadSmallEcc_TargetUrl		= "/main/TreeView/smallecc.zul";
	private final static String	AddGroup_TargetUrl			= "/main/TreeView/WAddGroup.zul";
	private final static String	EntityTest_TargetUrl		= "/main/TreeView/entitytest.zul";
	private final static String	AddEntity_TargetUrl			= "/main/TreeView/WAddEntity.zul";
	private final static String	AddMonitor_TargetUrl		= "/main/TreeView/WAddMonitor.zul";
	private final static String	EntityList_TargetUrl		= "/main/TreeView/EntityList.zul";
	private final static String	EntityRefreshi_TargetUrl	= "/main/TreeView/WRefreshMonitor.zul";
	private final static String	MonitorList_TargetUrl		= "/main/TreeView/MonitorList.zul";
	private final static String	DisableMonitors_TargetUrl	= "/main/TreeView/disablemonitors.zul";
	private final static String	EditSe_TargetUrl			= "/main/TreeView/editse.zul";
	private final static String	MonitorsRefresh_TargetUrl	= "/main/TreeView/refreshmonitors.zul";
	EccTimer				timer						= null;
	
	public ActionClickListener(ActionPopup popup, String action)
	{
		this.popup = popup;
		this.action = action;
	}
	
	@Override
	public void onEvent(Event event) throws Exception
	{
		try
		{
			ControlLayoutComposer composer = (ControlLayoutComposer) event.getTarget().getDesktop().getPage("controlPage").getFellow("controlLayout").getAttribute("Composer");
			
			setCheckedItem(composer.getCheckedEccTreeItem());
		} catch (Exception e)
		{
		}
		
		popup.close();
		EccTreeItem eccTreeItem = (EccTreeItem) event.getTarget().getAttribute("eccTreeItem");
		View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
		String sid = (String) event.getTarget().getDesktop().getSession().getAttribute("usersessionid");
		Include eccbody = (Include) event.getTarget().getDesktop().getPage("eccmain").getFellow("eccBody");
		Tree tree = (org.zkoss.zul.Tree) event.getTarget().getDesktop().getPage("eccmain").getFellow("tree");
		Treeitem item = tree.getSelectedItem();
		String entityname="";
		try
		{
		entityname= item.getLabel();
		}catch(Exception ex)
		{}
		INode node = eccTreeItem.getValue();
		INode parentnode = null;
		try
		{
			parentnode = eccTreeItem.getParent().getValue();
		} catch (Exception ex)
		{
			
		}
		timer = (EccTimer) event.getTarget().getDesktop().getPage("eccmain").getFellow("header_timer");
		Session pasteSession = event.getTarget().getDesktop().getSession();
		/* 以下为具体处理action的代码 */
		if (action.equals("addsongroup") || action.equals("addgroup"))
		{
			Session session = Executions.getCurrent().getDesktop().getSession();
			HashMap<String, String> doMap = new HashMap<String, String>();
			doMap.put("dowhat", "addGroup");
			if(session.getAttribute("doMap")!=null){
				session.removeAttribute("doMap");
			}
			session.setAttribute("doMap", doMap);
			final Window win = (Window) Executions.createComponents(AddGroup_TargetUrl, null, null);
			win.setAttribute("inode", eccTreeItem.getValue());
			win.setAttribute("view", view);
			win.setAttribute("isedit", false);
			win.setAttribute("eccTimer", timer);
			win.doModal();
		}else if(action.equals("telebackup")){
			final Window win = (Window) Executions.createComponents(TeleBackup_TargetUrl, null, null);			
			win.setMaximizable(false);
			win.setClosable(true);
			win.doModal();
	    }else if(action.equals("loadsmallecc")){
	    	Row row = (Row)event.getTarget();
	    	EccTreeItem eccTreeitem = (EccTreeItem)row.getAttribute("eccTreeItem");
	    	String id = eccTreeitem.getId();
	    	String title = eccTreeitem.getTitle();
	    	String flag = id+"%"+title;
			Session session = Executions.getCurrent().getDesktop().getSession();
			String flag_str = "";
			try{
				flag_str = (String)session.getAttribute(flag);
			}catch(Exception e){flag_str = "";}
			try{
				final Window win = (Window) Executions.createComponents(LoadSmallEcc_TargetUrl, null, null);	
				win.setAttribute("flag", flag);
				win.setMaximizable(false);
				win.setClosable(true);
				win.doModal();
//				if(flag_str == null || "".equals(flag_str)){
//					final Window win = (Window) Executions.createComponents(LoadSmallEcc_TargetUrl, null, null);	
//					win.setAttribute("flag", flag);
//					win.setMaximizable(false);
//					win.setClosable(true);
//					win.doModal();
//				}
//				else{
//					Executions.getCurrent().sendRedirect(flag_str, "_blank");
//				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else if (action.equals("editgroup"))
		{
			final Window win = (Window) Executions.createComponents(AddGroup_TargetUrl, null, null);
			win.setAttribute("inode", eccTreeItem.getValue());
			win.setAttribute("view", view);
			win.setAttribute("isedit", true);
			win.setAttribute("eccTimer", timer);
			win.doModal();
		} else if (action.equals("adddevice"))
		{
			Session session = Executions.getCurrent().getDesktop().getSession();
			HashMap<String, String> doMap = new HashMap<String, String>();
			doMap.put("dowhat", "addDevice");
			doMap.put("svId", node.getSvId());
			if(session.getAttribute("doMap")!=null){
				session.removeAttribute("doMap");
			}
			session.setAttribute("doMap", doMap);
			// final Window win = (Window) Executions.createComponents(EntityList_TargetUrl, null, null);
			// win.setAttribute("inode", eccTreeItem.getValue());
			// win.setAttribute("view", view);
			// win.setAttribute("isedit", false);
			// win.doModal();
			eccbody.setSrc(EntityList_TargetUrl + "?id=" + eccTreeItem.getId() + "&sid=" + sid);
		} else if (action.equals("editdevice"))
		{
			Session session = Executions.getCurrent().getDesktop().getSession();
			HashMap<String, String> doMap = new HashMap<String, String>();
			doMap.put("dowhat", "editDevice");
			doMap.put("svId", node.getSvId());
			if(session.getAttribute("doMap")!=null){
				session.removeAttribute("doMap");
			}
			session.setAttribute("doMap", doMap);
			final Window win = (Window) Executions.createComponents(AddEntity_TargetUrl, null, null);
			win.setAttribute("inode", eccTreeItem.getValue());
			win.setAttribute("view", view);
			win.setAttribute("isedit", true);
			win.setAttribute("eccTimer", timer);
			win.setAttribute("tree", tree);
			win.doModal();
		} else if (action.equals("testdevice"))
		{
			if(eccTreeItem.getChildRen().size()<1)
			{
				Message.showInfo("没有可以测试的监测器！");
			}else{
				final Window win = (Window) Executions.createComponents(EntityTest_TargetUrl, null, null);
				win.setAttribute("inode", eccTreeItem.getValue());
				win.setAttribute("view", view);
				win.doModal();
			}

		} else if (action.equals("devicerefresh") || action.equals("monitorrefresh"))
		{
			if (node.getType().equals(INode.ENTITY))
			{
				List<EccTreeItem> children = eccTreeItem.getChildRen();
				int mcount = children.size();
				if (mcount == 0)
				{
					Message.showInfo("没有可以刷新的监测器！");
					return;
				}
				final Window win = (Window) Executions.createComponents(EntityRefreshi_TargetUrl, null, null);
				win.setAttribute("inode", eccTreeItem.getValue());
				win.setAttribute("view", view);
				win.setAttribute("eccTimer", timer);
				win.setAttribute("children", children);
				win.doModal();
			}
			if (node.getType().equals(INode.MONITOR))
			{
				final Window win = (Window) Executions.createComponents(EntityRefreshi_TargetUrl, null, null);
				win.setAttribute("inode", eccTreeItem.getValue());
				win.setAttribute("view", view);
				win.setAttribute("eccTimer", timer);
				win.doModal();
			}
		} else if (action.equals("addmonitor"))
		{
			Session session = Executions.getCurrent().getDesktop().getSession();
			HashMap<String, String> doMap = new HashMap<String, String>();
			doMap.put("dowhat", "addMonitor");
			doMap.put("svId", node.getSvId());
			if(session.getAttribute("doMap")!=null){
				session.removeAttribute("doMap");
			}
			session.setAttribute("doMap", doMap);
			eccbody.setSrc(MonitorList_TargetUrl + "?id=" + eccTreeItem.getId() + "&sid=" + sid);
		} else if (action.equals("editmonitor"))
		{
			final Window win = (Window) Executions.createComponents(AddMonitor_TargetUrl, null, null);
			win.setAttribute("inode", eccTreeItem.getValue());
			win.setAttribute("view", view);
			win.setAttribute("isedit", true);
			win.setAttribute("entityname", entityname);
			win.setAttribute("eccTimer", timer);
			win.setAttribute("tree", tree);
			win.doModal();
		} else if (action.equals("delgroup"))
		{
			try
			{
				if("二层交换机,三层交换机,路由器,防火墙,服务器,PC设备,其他设备".contains(node.getName())){
					Message.showInfo("该设备组与NNM关联，不允许删除");
					return;
				}
				int r = Message.showQuestion("确认删除组：" + node.getName());
				Boolean delsuccess = false;
				if (r == Messagebox.OK)
				{
					Session session = Executions.getCurrent().getDesktop().getSession();
					HashMap<String, String> doMap = new HashMap<String, String>();
					doMap.put("dowhat", "delGroup");
					if(session.getAttribute("doMap")!=null){
						session.removeAttribute("doMap");
					}
					session.setAttribute("doMap", doMap);
					GroupInfo groupinfo = view.getGroupInfo(node);
					try
					{
						delsuccess = groupinfo.deleteNode(view);
					} catch (Exception ex)
					{
						ex.printStackTrace();
					}
					if (delsuccess)
					{
						String loginname = view.getLoginName();
						String minfo = ""; 
						if (parentnode == null)
						{
							minfo = "删除组：" + node.getName() + "(" + node.getSvId() + ")  ";
						} else
						{
							minfo = "删除组：" + node.getName() + "(" + node.getSvId() + ") parent is " + parentnode.getName() + "(" + parentnode.getSvId() + ")";
						}
						AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del, OpObjectId.group);
					}
					INode[] ids = new INode[] { node };
					timer.refresh(ids, ChangeDetailEvent.TYPE_DELETE);
				}
			} catch (Exception e)
			{
				
			}
		} else if (action.equals("deldevice"))
		{
			try
			{
				int r = Message.showQuestion("确认删除设备：" + node.getName());
				Boolean delsuccess = false;
				if (r == Messagebox.OK)
				{
					Session session = Executions.getCurrent().getDesktop().getSession();
					HashMap<String, String> doMap = new HashMap<String, String>();
					doMap.put("dowhat", "delDevice");
					if(session.getAttribute("doMap")!=null){
						session.removeAttribute("doMap");
					}
					session.setAttribute("doMap", doMap);
					EntityInfo entityinfo = view.getEntityInfo(node);
					try
					{
						delsuccess = entityinfo.deleteNode(view);
						
					} catch (Exception ex)
					{
						ex.printStackTrace();
					}
					if (delsuccess)
					{
						String loginname = view.getLoginName();
						String minfo = "";
						if (parentnode == null)
						{
							minfo = "删除设备：" + node.getName() + "(" + node.getSvId() + ")";
						} else
						{
							minfo = "删除设备：" + node.getName() + "(" + node.getSvId() + ") parent is " + parentnode.getName() + "(" + parentnode.getSvId() + ")";
						}
						AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del, OpObjectId.entity);
					}
					INode[] ids = new INode[] { node };
					timer.refresh(ids, ChangeDetailEvent.TYPE_DELETE);
				}
			} catch (Exception e)
			{
				
			}
		} else if (action.equals("delmonitor"))
		{
			try
			{
				int r = Message.showQuestion("确认删除监测器：" + node.getName());
				Boolean delsuccess = false;
				if (r == Messagebox.OK)
				{
					Session session = Executions.getCurrent().getDesktop().getSession();
					HashMap<String, String> doMap = new HashMap<String, String>();
					doMap.put("dowhat", "delMonitor");
					if(session.getAttribute("doMap")!=null){
						session.removeAttribute("doMap");
					}
					session.setAttribute("doMap", doMap);
					MonitorInfo monitorinfo = view.getMonitorInfo(node);
					try
					{
						delsuccess = monitorinfo.deleteNode(view);
					} catch (Exception ex)
					{
						ex.printStackTrace();
					}
					if (delsuccess)
					{
						String loginname = view.getLoginName();
						String minfo = "";
						if (parentnode == null)
						{
							minfo = "删除监测器：" + node.getName() + "(" + node.getSvId() + ")";
						} else
						{
							minfo = "删除监测器：" + node.getName() + "(" + node.getSvId() + ") parent is " + parentnode.getName() + "(" + parentnode.getSvId() + ")";
						}
						AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del, OpObjectId.monitor);
					}
					INode[] ids = new INode[] { node };
					timer.refresh(ids, ChangeDetailEvent.TYPE_DELETE);
				}
			} catch (Exception e)
			{
				
			}
			
		} else if (action.equals("startup"))
		{
			try
			{
				int r = 0;
				if("group".equals(node.getType())){
					r = Message.showQuestion("是否启动组：" + node.getName());
				}else if("entity".equals(node.getType())){
					r = Message.showQuestion("是否启动设备：" + node.getName());
				}else if("monitor".equals(node.getType())){
					r = Message.showQuestion("是否启动监测器：" + node.getName());
				}
				
				if (r == Messagebox.OK)
				{
					if (node.getType().equals(INode.GROUP))
					{
						GroupInfo groupinfo = view.getGroupInfo(node);
						try
						{
							if(parentnode.getStatus().equals(INode.DISABLE)){
								Message.showInfo("父节点被禁用");
								return;
							}
							Boolean startsuccess = groupinfo.enableMonitor(view);
							if (startsuccess)
							{
								String loginname = view.getLoginName();
								String minfo = "";
								if (parentnode == null)
								{
									minfo = "启动组：" + node.getName() + "(" + node.getSvId() + ")";
								} else
								{
									minfo = "启动组：" + node.getName() + "(" + node.getSvId() + ") parent is " + parentnode.getName() + "(" + parentnode.getSvId() + ")";
								}
								AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.enable, OpObjectId.group);
							}
						} catch (Exception ex)
						{
							ex.printStackTrace();
						}
					} else if (node.getType().equals(INode.ENTITY))
					{
						EntityInfo entityinfo = view.getEntityInfo(node);
						try
						{
							if(parentnode.getStatus().equals(INode.DISABLE)){
								Message.showInfo("父节点被禁用");
								return;
							}
							Boolean startsuccess = entityinfo.enableMonitor(view);
							if (startsuccess)
							{
								String loginname = view.getLoginName();
								String minfo = "";
								if (parentnode == null)
								{
									minfo = "启动设备：" + node.getName() + "(" + node.getSvId() + ")";
								} else
								{
									minfo = "启动设备：" + node.getName() + "(" + node.getSvId() + ") parent is " + parentnode.getName() + "(" + parentnode.getSvId() + ")";
								}
								AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.enable, OpObjectId.entity);
							}
						} catch (Exception ex)
						{
							ex.printStackTrace();
						}
					} else if (node.getType().equals(INode.MONITOR))
					{
						MonitorInfo monitorinfo = view.getMonitorInfo(node);
						try
						{
							if(parentnode.getStatus().equals(INode.DISABLE)){
								Message.showInfo("父节点被禁用");
								return;
							}
							Boolean startsuccess = monitorinfo.enableMonitor(view);
							if (startsuccess)
							{
								String loginname = view.getLoginName();
								String minfo = "";
								if (parentnode == null)
								{
									minfo = "启动监测器：" + node.getName() + "(" + node.getSvId() + ")";
								} else
								{
									minfo = "启动监测器：" + node.getName() + "(" + node.getSvId() + ") parent is " + parentnode.getName() + "(" + parentnode.getSvId() + ")";
								}
								AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.enable, OpObjectId.monitor);
							}
						} catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
					INode[] ids = new INode[] { node };
					
					Session session = Executions.getCurrent().getDesktop().getSession();
					HashMap<String, String> doMap = new HashMap<String, String>();
					doMap.put("dowhat", "startup");
					doMap.put("type", node.getType());
					doMap.put("svId", node.getSvId());
					if(session.getAttribute("doMap")!=null){
						session.removeAttribute("doMap");
					}
					session.setAttribute("doMap", doMap);
					
					timer.refresh(ids, ChangeDetailEvent.TYPE_MODIFY);
				}
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		} else if (action.startsWith("disabled"))
		{
			//若对空组或空设备实行禁，不进行任何操作
			if(!"monitor".equals(eccTreeItem.getType()) && (eccTreeItem.getChildRen() == null || eccTreeItem.getChildRen().size() == 0)){
				return;
			}
			final Window win = (Window) Executions.createComponents(DisableMonitors_TargetUrl, null, null);
			win.setAttribute("inode", eccTreeItem.getValue());
			win.setAttribute("view", view);
			win.setAttribute("eccTimer", timer);
			win.setAttribute("action", action);
			win.doModal();
		} else if (action.equals("se_edit"))
		{
			final Window win = (Window) Executions.createComponents(EditSe_TargetUrl, null, null);
			win.setAttribute("inode", eccTreeItem.getValue());
			win.setAttribute("view", view);
			win.setAttribute("eccTimer", timer);
			win.doModal();
		} else if (action.equals("copydevice"))
		{
			pasteSession.setAttribute("pastenode", eccTreeItem.getValue());
		} else if (action.equals("copymonitor"))
		{
			pasteSession.setAttribute("pastenode", eccTreeItem.getValue());
		} else if (action.equals("pastedevice"))
		{
			INode pasternode = null;
			String entityid = null;
			try
			{
				pasternode = (INode) pasteSession.getAttribute("pastenode");
				
				if (pasternode == null)
				{
					Message.showInfo("请先复制好设备！");
					return;
				}
				if (!pasternode.getType().equals(INode.ENTITY))
				{
					Message.showInfo("请先复制好设备！");
					return;
				}
				int r = Message.showQuestion("是否粘贴设备：" + pasternode.getName());
				if (r == Messagebox.CANCEL)
				{
					return;
				}
				String sourceEntityId = pasternode.getSvId();
				GroupInfo groupinfo = view.getGroupInfo(node);
				entityid = groupinfo.PasteDevice(sourceEntityId);
				
				INode inode = view.getNode(sourceEntityId);
				if (entityid != null && !entityid.isEmpty())
				{
					String loginname = view.getLoginName();
					String minfo = "";
					
					minfo = "粘贴设备：" + inode.getName() + "(" + entityid + ") to " + node.getName() + "(" + node.getSvId() + ")";
					
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.paste, OpObjectId.entity);
				}
				Message.showInfo("粘贴设备成功！");
				
				EntityInfo entityinfo = view.getEntityInfo(inode);
				List<EccTreeItem> children = new ArrayList<EccTreeItem>();
				List<String> childids = entityinfo.getSonList();
				if (childids != null)
				{
					int i = 1;
					for (String key : childids)
					{
						INode n = view.getNode(key);
						MonitorInfo minfo = new MonitorInfo(n);
						String id1 = entityid + "." + i;
						minfo.setId(id1);
						EccTreeItem it = new EccTreeItem(null, "", "", "");
						i++;
						it.setValue(minfo);
						children.add(it);
					}
					
				}
				entityinfo.setId(entityid);
				// 等待一秒
				Thread.sleep(1000);
				if (childids.size() > 0)
				{
					final Window win = (Window) Executions.createComponents(EntityRefreshi_TargetUrl, null, null);
					win.setAttribute("inode", entityinfo);
					win.setAttribute("view", view);
					win.setAttribute("eccTimer", timer);
					win.setAttribute("children", children);
					win.doModal();
				}
				INode[] ids = new INode[] { entityinfo };
				timer.refresh(ids, ChangeDetailEvent.TYPE_ADD);
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
				Message.showInfo(smessage);
				return;
			}
			
		} else if (action.equals("pastemonitor"))
		{
			INode pasternode = null;
			String monitorid = null;
			try
			{
				pasternode = (INode) pasteSession.getAttribute("pastenode");
				if (pasternode == null)
				{
					Message.showInfo("请先复制好监测器！");
					return;
				}
				if (!pasternode.getType().equals(INode.MONITOR))
				{
					Message.showInfo("请先复制好监测器！");
					return;
				}
				int r = Message.showQuestion("是否粘贴监测器：" + pasternode.getName());
				if (r == Messagebox.CANCEL)
				{
					return;
				}
				String sourceMonitorId = pasternode.getSvId();
				EntityInfo entityinfo = view.getEntityInfo(node);
				monitorid = entityinfo.PasteMonitor(sourceMonitorId);
				INode inode = view.getNode(sourceMonitorId);
				if (monitorid != null && !monitorid.isEmpty())
				{
					String loginname = view.getLoginName();
					String minfo = "";
					
					minfo = "粘贴监测器：" + inode.getName() + "(" + monitorid + ") to " + node.getName() + "(" + node.getSvId() + ")";
					
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.paste, OpObjectId.monitor);
				}
				Message.showInfo("粘贴监测器成功！");
				
				MonitorInfo monitorinfo = view.getMonitorInfo(inode);
				monitorinfo.setId(monitorid);
				final Window win = (Window) Executions.createComponents(EntityRefreshi_TargetUrl, null, null);
				win.setAttribute("inode", monitorinfo);
				win.setAttribute("view", view);
				win.setAttribute("eccTimer", timer);
				win.doModal();
				
				INode[] ids = new INode[] { monitorinfo };
				timer.refresh(ids, ChangeDetailEvent.TYPE_ADD);
			} catch (Exception ex)
			{
				String smessage = ex.getMessage();
				if (smessage == null)
				{
					smessage = "";
				}
				if (smessage.contains("doesn't belong"))
				{
					smessage = "监测器不属于当前设备类型!";
				}
				if (smessage.contains("Less permission"))
				{
					smessage = "您的监测器点数不够!";
				}
				Message.showInfo(smessage);
				return;
			}
		} else if (action.equals("batchDel"))
		{
			try
			{
				ArrayList<EccTreeItem> items = getCheckedItem();
				if (items == null || items.isEmpty())
				{
					Message.showInfo("没有选中的项目");
					return;
				}
				int r = Message.showQuestion("确认删除选中的项目吗？");
				if (r == Messagebox.OK)
				{
					
					String[] svids = new String[items.size()];
					INode[] ids = new INode[items.size()];
					String vids = "";
					int i = 0;
					for (EccTreeItem ite : items)
					{
						String id = ite.getId();
						svids[i] = id;
						ids[i] = ite.getValue();
						vids = id + ",";
						i++;
					}
					Boolean success = false;
					if (node.getType().equals(INode.GROUP))
					{
						GroupInfo groupinfo = view.getGroupInfo(node);
						try
						{
							success = groupinfo.deleteNode(svids, view);
						} catch (Exception ex)
						{
							ex.printStackTrace();
						}
					} else if (node.getType().equals(INode.ENTITY))
					{
						EntityInfo entityinfo = view.getEntityInfo(node);
						try
						{
							success = entityinfo.deleteNode(svids, view);
							
						} catch (Exception ex)
						{
							ex.printStackTrace();
						}
					} else if (node.getType().equals(INode.SE))
					{
						SeInfo seinfo = view.getSeInfo(node);
						try
						{
							success = seinfo.deleteNode(svids, view);
						} catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
					if (success)
					{
						String loginname = view.getLoginName();
						String minfo = "";
						for (INode node2 : ids)
						{
							minfo = "删除：" + node2.getName() + "(" + node2.getSvId() + ") parent is " + node.getName() + "(" + node.getSvId() + ")";
							if (node2.getType().equals(INode.GROUP))
							{
								AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del, OpObjectId.group);
							} else if (node2.getType().equals(INode.ENTITY))
							{
								AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del, OpObjectId.entity);
							} else if (node2.getType().equals(INode.MONITOR))
							{
								AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del, OpObjectId.monitor);
							}
							
						}
						
						timer.refresh(ids, ChangeDetailEvent.TYPE_DELETE);
					}
				}
			} catch (Exception e)
			{
				
			}
		} else if (action.equals("batchStartup"))
		{
			try
			{
				ArrayList<EccTreeItem> items = getCheckedItem();
				if (items == null || items.isEmpty())
				{
					Message.showInfo("没有选中的项目");
					return;
				}
				String[] svids = new String[items.size()];
				INode[] ids = new INode[items.size()];
				int i = 0;
				int starts = 0;
				for (EccTreeItem ite : items)
				{
					
					svids[i] = ite.getId();
					INode n = ite.getValue();
					if (n.getStatus().equals(INode.DISABLE))
					{
						starts = 1;
					}
					ids[i] = n;
					
					i++;
				}
				// if (starts==0)
				// {
				// Message.showInfo("所有选中的项目均已启动!");
				// return;
				// }
				int r = Message.showQuestion("是否启动选中的项目的监测?");
				if (r == Messagebox.OK)
				{
					
					Boolean success = false;
					if (node.getType().equals(INode.GROUP))
					{
						GroupInfo groupinfo = view.getGroupInfo(node);
						try
						{
							success = groupinfo.enableMonitor(svids, view);
						} catch (Exception ex)
						{
							ex.printStackTrace();
						}
					} else if (node.getType().equals(INode.ENTITY))
					{
						EntityInfo entityinfo = view.getEntityInfo(node);
						try
						{
							success = entityinfo.enableMonitor(svids, view);
						} catch (Exception ex)
						{
							ex.printStackTrace();
						}
					}
					if (success)
					{
						String loginname = view.getLoginName();
						String minfo = "";
						for (INode node2 : ids)
						{
							minfo = "启动：" + node2.getName() + "(" + node2.getSvId() + ") parent is " + node.getName() + "(" + node.getSvId() + ")";
							if (node2.getType().equals(INode.GROUP))
							{
								AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.enable, OpObjectId.group);
							} else if (node2.getType().equals(INode.ENTITY))
							{
								AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.enable, OpObjectId.entity);
							} else if (node2.getType().equals(INode.MONITOR))
							{
								AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.enable, OpObjectId.monitor);
							}
							
						}
						
						timer.refresh(ids, ChangeDetailEvent.TYPE_MODIFY);
					}
				}
			} catch (Exception e)
			{
			}
		} else if (action.equals("batchRefresh"))
		{
			ArrayList<EccTreeItem> items = getCheckedItem();
			if (items == null || items.isEmpty())
			{
				Message.showInfo("没有选中的项目");
				return;
			}
			ArrayList<INode> children = new ArrayList<INode>();
			
			for (EccTreeItem ite : items)
			{
				INode n = ite.getValue();
				children.add(n);
				
			}
			final Window win = (Window) Executions.createComponents(MonitorsRefresh_TargetUrl, null, null);
			win.setAttribute("inode", eccTreeItem.getValue());
			win.setAttribute("view", view);
			win.setAttribute("eccTimer", timer);
			win.setAttribute("children", children);
			win.doModal();
		} else if (action.equals("batchDisable"))
		{
			ArrayList<EccTreeItem> items = getCheckedItem();
			if (items == null || items.isEmpty())
			{
				Message.showInfo("没有选中的项目");
				return;
			}
			ArrayList<INode> children = new ArrayList<INode>();
			int starts = 0;
			for (EccTreeItem ite : items)
			{
				//若对空组或空设备实行禁，不进行任何操作
				if((!"monitor".equals(ite.getType())) && (ite.getChildRen() == null || ite.getChildRen().size() == 0)){
					continue;
				}
				INode n = ite.getValue();
				if (!n.getStatus().equals(INode.DISABLE))
				{
					starts = 1;
				}
				children.add(n);
				
			}
			// if (starts==0)
			// {
			// Message.showInfo("所有选中的项目均已禁止!");
			// return;
			// }
			final Window win = (Window) Executions.createComponents(DisableMonitors_TargetUrl, null, null);
			win.setAttribute("inode", eccTreeItem.getValue());
			win.setAttribute("view", view);
			win.setAttribute("eccTimer", timer);
			win.setAttribute("children", children);
			win.doModal();
		} else if (action.equals("monitorDetail"))
		{
			Borderlayout wMonitorInfo = (Borderlayout) event.getTarget().getDesktop().getPage("controlPage").getFellow("WMonitorInfo");
			MonitorReport mr = (MonitorReport) wMonitorInfo.getAttribute("Composer");
			mr.showDetail();
		}
		
	}
	
	/*
	 * 刷新界面
	 */
	private void refreshdestop(INode[] inodes, int actionType)
	{
		
		// timer.refresh(inodes, actionType);
		// timer.refresh();
	}
	// group =editgroup,delgroup,addsongroup,grouprefresh,disablegroup,startupgroup
	// entity =adddevice,editdevice,deldevice,copydevice,devicerefresh,testdevice,disabledevice,startupdevice
	// monitor =addmonitor,editmonitor,delmonitor,monitorrefresh,disablemonitor,startupmonitor
	
}
