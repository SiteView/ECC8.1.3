package com.siteview.svecc.zk.test;

import java.util.HashMap;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Include;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.base.tree.IForkNode;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;

public class TreePush
{
	public final static HashMap	ThreadMap	= new HashMap();
	
	public static void start(Window win, String username, String password) throws Exception
	{
		final Desktop desktop = Executions.getCurrent().getDesktop();
		View view = SVDBViewFactory.getView(username, password);
		if (desktop.isServerPushEnabled())
		{
			Messagebox.show("Already started", "提示", Messagebox.OK, Messagebox.INFORMATION);
		} else
		{
			desktop.enableServerPush(true);
			new WorkingThread(win, view).start();
		}
	}
	
	public static void start(Window win) throws Exception
	{
		final Desktop desktop = Executions.getCurrent().getDesktop();
		
		View view = SVDBViewFactory.getView(desktop.getSession().getAttribute("usersessionid").toString());
		if (desktop.isServerPushEnabled())
		{
			Messagebox.show("Already started", "提示", Messagebox.OK, Messagebox.INFORMATION);
		} else
		{
			desktop.enableServerPush(true);
			WorkingThread wt = new WorkingThread(win, view);
			TreePush.ThreadMap.put(desktop.getSession().getAttribute("usersessionid").toString(), wt);
			
			wt.start();
		}
	}
	
	public static Tree getTree() throws Exception
	{
		Desktop desktop = Executions.getCurrent().getDesktop();
		Tree tree = null;
		
		int timmer = 0;
		while (timmer < 270)
		{
			tree = ((WorkingThread) ThreadMap.get(desktop.getSession().getAttribute("usersessionid").toString())).getTree();
			
			if (tree != null)
				break;
			timmer = timmer + 10;
			Thread.currentThread().sleep(10);
		}
		return tree;
	}
	
	public static void stop() throws InterruptedException
	{
		final Desktop desktop = Executions.getCurrent().getDesktop();
		if (desktop.isServerPushEnabled())
		{
			desktop.enableServerPush(false);
		} else
		{
			Messagebox.show("Already stopped", "提示", Messagebox.OK, Messagebox.INFORMATION);
		}
	}
	
}

class WorkingThread extends Thread
{
	private Tree	tree;
	
	public Tree getTree()
	{
		return tree;
	}
	
	private final Desktop	_desktop;
	private View			view;
	private Window			win;
	
	public WorkingThread(Window win, View view) throws Exception
	{
		this.win = win;
		Tree tree = new Tree();
		tree.addEventListener("onSelect", new treeOnSelect(win));
		tree.setParent(win);
		this.tree = tree;
		Treechildren treechildren = new Treechildren();
		treechildren.setParent(this.tree);
		this._desktop = this.tree.getDesktop();
		this.view = view;
		
		this.win.appendChild(getMenupopup(INode.SE));
		this.win.appendChild(getMenupopup(INode.GROUP));
		this.win.appendChild(getMenupopup(INode.ENTITY));
		this.win.appendChild(getMenupopup(INode.MONITOR));
		this.setName(WorkingThread.class.getName());
	}
	
	class treeOnSelect implements EventListener
	{
		Window	win;
		
		public treeOnSelect(Window win)
		{
			this.win = win;
		}
		
		public void onEvent(Event arg0) throws Exception
		{
			SelectEvent e = (SelectEvent) arg0;
			if (!e.getSelectedItems().isEmpty())
			{
				INode node = (INode) ((Treeitem) (e.getSelectedItems().iterator().next())).getAttribute("obj");
				Messagebox.show(node.getName(), "提示", Messagebox.OK, Messagebox.INFORMATION);
				String type = node.getType();
				String id = node.getSvId();
				
				((Include)win.getParent().getFellow("xcontents")).setSrc("/main/eccbody.zul?type=" + type + "&id=" + id);
				//Executions.getCurrent().sendRedirect("/main/eccbody.zul?type=" + type + "&id=" + id, "eccbody");
			}
			
		}
	}
	
	public void run()
	{
		try
		{
			while (true)
			{
				if (_desktop == null || !_desktop.isServerPushEnabled())
				{
					if (_desktop!=null) _desktop.enableServerPush(false);
					return;
				}
				try
				{
					Executions.activate(_desktop);
					setTree(this.tree, this.tree.getTreechildren());
				} catch (Exception e)
				{
					e.printStackTrace();
				} finally
				{
					Executions.deactivate(_desktop);
				}
				try
				{
					sleep(2000);
				} catch (InterruptedException e)
				{
				}
			}
		} finally
		{
			TreePush.ThreadMap.remove(_desktop.getSession().getAttribute("usersessionid").toString());
		}
	}
	
	private boolean	bfirst	= true;
	
	public void setTree(Tree tree, Treechildren treechildren)
	{
		if (bfirst)
		{
			this.tree.clear();
			this.initTree(treechildren);
			bfirst = false;
		} else
		{
			
			this.changeTree(treechildren);
		}
	}
	
	private void initTree(Treechildren treechildren)
	{
		view.getChangeTree();
		// view.getChangeTreeInfo();
		INode[] ses = view.getSe();
		if (ses == null)
			return;
		for (INode se : ses)
		{
			Treeitem ti = getTreeitem(se);
			ti.setParent(treechildren);
			this.constructNode(ti, se);
		}
	}
	
	private void constructNode(Treeitem ti, INode n)
	{
		if (INode.MONITOR.equals(n.getType()))
			return;
		
		IForkNode f = (IForkNode) n;
		List<String> ids = f.getSonList();
		if (ids.size() > 0)
		{
			Treechildren mytreechildren = new Treechildren();
			ti.appendChild(mytreechildren);
			for (String id : ids)
			{
				// 递归构造所有儿子
				INode node = view.getNode(id);
				if (node == null)
					continue;
				
				Treeitem tii = getTreeitem(node);
				tii.setParent(mytreechildren);
				constructNode(tii, node);
			}
		}
	}
	
	private void changeTree(Treechildren treechildren)
	{
		List<String> ids = null;//view.getChangeTree();
		if (ids == null)
			return;
		// List<String> idsInfo = view.getChangeTreeInfo();
		for (String id : ids)
		{
			if (id == null)
				continue;
			INode node = view.getNode(id);
			boolean bfind = false;
			for (Object obj : treechildren.getItems())
			{
				if (obj instanceof Treeitem)
				{
					Treeitem ti = (Treeitem) obj;
					if (id.equals(ti.getId()))
					{
						bfind = true;
						if (node == null)
						{
							Component parent = ti.getParent();
							if (parent == null)
								break;
							parent.removeChild(ti);
							if (parent instanceof Treechildren)
							{
								Treechildren mytreechildren = (Treechildren) parent;
								if (mytreechildren.getItemCount() == 0)
								{
									Component itemParent = parent.getParent();
									if (itemParent != null)
										itemParent.removeChild(mytreechildren);
								}
							}
						} else
						{
							this.setTreeitem(ti, node);
						}
						break;
					}
				}
			}
			if (!bfind && node != null)
			{
				String parentid = node.getParentSvId();
				if (parentid == null)
					return;
				for (Object obj : treechildren.getItems())
				{
					if (obj instanceof Treeitem)
					{
						Treeitem ti = (Treeitem) obj;
						if (parentid.equals(ti.getId()))
						{
							Treechildren mytreechildren = ti.getTreechildren();
							if (mytreechildren == null)
							{
								mytreechildren = new Treechildren();
								ti.appendChild(mytreechildren);
							}
							Treeitem tii = getTreeitem(node);
							tii.setParent(mytreechildren);
							break;
						}
					}
				}
			}
		}
	}
	
	private Treeitem getTreeitem(INode node)
	{
		Treeitem tii = new Treeitem();
		
		this.setTreeitem(tii, node);
		tii.setContext(getMenupopupidByType(node.getType()));
		tii.setAttribute("obj", node);
		return tii;
	}
	
	private void setTreeitem(Treeitem tii, INode node)
	{
		tii.setLabel(node.getName() == null || "".equals(node.getName()) ? "<" + INode.UNKNOWN + ">" : node.getName());
		tii.setId(node.getSvId());
		
		if (INode.MONITOR.equals(node.getType()))
		{
			MonitorInfo info = view.getMonitorInfo(node);
			tii.setTooltiptext(info.getDstr());
		} else
		{
			tii.setTooltiptext(node.getStatus());
		}
		tii.setImage(getImage(node));
		tii.setValue(node);
	}
	
	private String getMenupopupidByType(String type)
	{
		return "mymenu_" + type;
	}
	
	private Menupopup getMenupopup(String type)
	{
		Menupopup menupopup = new Menupopup();
		menupopup.setId(getMenupopupidByType(type));
		Menuitem menuitem = null;
		if (INode.SE.equals(type))
		{
			menuitem = new Menuitem();
			menuitem.setLabel("添加组");
			menuitem.setId("SE_AddGroup");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
			menuitem = new Menuitem();
			menuitem.setLabel("添加设备");
			menuitem.setId("SE_AddDevice");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
			menuitem = new Menuitem();
			menuitem.setLabel("编辑");
			menuitem.setId("SE_Modify");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
			menuitem = new Menuitem();
			menuitem.setLabel("粘贴");
			menuitem.setId("SE_Paste");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
		} else if (INode.MONITOR.equals(type))
		{
			menuitem = new Menuitem();
			menuitem.setLabel("允许");
			menuitem.setId("MONITOR_Enable");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
			menuitem = new Menuitem();
			menuitem.setLabel("禁止");
			menuitem.setId("MONITOR_Disable");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
			menuitem = new Menuitem();
			menuitem.setLabel("删除");
			menuitem.setId("MONITOR_Delete");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
			menuitem = new Menuitem();
			menuitem.setLabel("复制");
			menuitem.setId("MONITOR_Copy");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
			menuitem = new Menuitem();
			menuitem.setLabel("粘贴");
			menuitem.setId("MONITOR_Paste");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
		} else if (INode.ENTITY.equals(type))
		{
			menuitem = new Menuitem();
			menuitem.setLabel("添加检测");
			menuitem.setId("ENTITY_AddMonitor");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
			menuitem = new Menuitem();
			menuitem.setLabel("删除");
			menuitem.setId("ENTITY_Delete");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
			menuitem = new Menuitem();
			menuitem.setLabel("编辑");
			menuitem.setId("ENTITY_Modify");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
			menuitem = new Menuitem();
			menuitem.setLabel("复制");
			menuitem.setId("ENTITY_Copy");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
			menuitem = new Menuitem();
			menuitem.setLabel("启用检测");
			menuitem.setId("ENTITY_Enable");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
			menuitem = new Menuitem();
			menuitem.setLabel("禁用检测");
			menuitem.setId("ENTITY_Disable");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
		} else if (INode.GROUP.equals(type))
		{
			menuitem = new Menuitem();
			menuitem.setLabel("添加组");
			menuitem.setId("GROUP_AddGroup");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
			menuitem = new Menuitem();
			menuitem.setLabel("添加设备");
			menuitem.setId("GROUP_AddDevice");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
			menuitem = new Menuitem();
			menuitem.setLabel("删除");
			menuitem.setId("GROUP_Delete");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
			menuitem = new Menuitem();
			menuitem.setLabel("编辑");
			menuitem.setId("GROUP_Modify");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
			menuitem = new Menuitem();
			menuitem.setLabel("粘贴");
			menuitem.setId("GROUP_Paste");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
			menuitem = new Menuitem();
			menuitem.setLabel("启用检测");
			menuitem.setId("GROUP_Enable");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
			menuitem = new Menuitem();
			menuitem.setLabel("禁用检测");
			menuitem.setId("GROUP_Disable");
			menuitem.setImage("/main/images/none.gif");
			menuitem.setParent(menupopup);
		}
		return menupopup;
		
	}
	
	private String getImage(INode node)
	{
		return this.getImage(node.getType(), node.getStatus());
	}
	
	private String getImage(String type, String status)
	{
		StringBuffer sbf = new StringBuffer();
		sbf.append("/main/images/");
		if (status == null || status.indexOf(" ") >= 0 || INode.NULL.equals(status))
		{
			sbf.append("none");
		} else
		{
			sbf.append(type);
			sbf.append("_");
			sbf.append(status);
		}
		if (type.equals("monitor"))
			sbf.append(".gif");
		else
			sbf.append(".gif");
		return sbf.toString();
		
	}
}
