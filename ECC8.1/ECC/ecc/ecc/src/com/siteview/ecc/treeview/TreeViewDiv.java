package com.siteview.ecc.treeview;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Div;
import org.zkoss.zul.Menu;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;

import com.siteview.base.data.VirtualItem;
import com.siteview.base.data.VirtualView;
import com.siteview.base.manage.View;
import com.siteview.base.template.EntityTemplate;
import com.siteview.base.template.TemplateManager;
import com.siteview.base.tree.IForkNode;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.EntityInfo;
import com.siteview.base.treeInfo.GroupInfo;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.base.treeInfo.SeInfo;
import com.siteview.svecc.zk.test.SVDBViewFactory;

public class TreeViewDiv extends Div implements Runnable {
	private final static Logger logger = Logger.getLogger(TreeViewDiv.class);

	private String strTreeType = VirtualView.DefaultView;
	private Tree tree;
	Thread pushThread = null;
	private View view;
	private String m_session = "";
	
	public synchronized Tree getTree() {
		if (tree == null) 
		{
			Object usersessionid = getDesktop().getSession().getAttribute(
					"usersessionid");
			if (usersessionid == null) {
				Executions.getCurrent().sendRedirect("/index.jsp", "_top");
				return null;
			}

			try {
				m_session = usersessionid.toString();
				view = SVDBViewFactory.getView(usersessionid.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

			tree = new Tree();
			tree.setParent(this);
			Treechildren treechildren = new Treechildren();
			treechildren.setParent(this.tree);
			tree.clear();
			initTree(treechildren);

		}
		return tree;
	}

	public void onCreate() 
	{

		tree=getTree();
		
		if(getTree()==null)
			return;
		try
		{
			getDesktop().enableServerPush(true);
		}
		catch(Exception e)
		{
//			Executions.activate(getDesktop());
			e.printStackTrace();
		}
			
		
		pushThread = new Thread(this);
		//pushThread.setDaemon(false);
		
		pushThread.start();
	}
	
	public String getStrTreeType() {
		return strTreeType;
	}

	public void setStrTreeType(String strTreeType) {
		this.strTreeType = strTreeType;
	}
	
	public void refresh()
	{
		pushThread.suspend();
		
		//清除原来的树
		Treechildren oldchildren = tree.getTreechildren();
		tree.removeChild(oldchildren);		
		
		Treechildren newchildren = new Treechildren();
		newchildren.setParent(this.tree);
		tree.clear();
		
		//构建新的树
		initTree(newchildren);
		
		pushThread.resume();
	}
	
	private void initTree(Treechildren treechildren) 
	{
		//view.getChangeTree();
		// view.getChangeTreeInfo();
				
		//1、构造监测数据树视图
		if(strTreeType.equals(VirtualView.DefaultView))
		{
			//默认树情况
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
		
		//2、构造虚拟视图监测数据树  + 其他报警等树节点
		List<VirtualView> av= view.getAllVirtualView();
		for(VirtualView v: av)
		{
			if(v.getViewName().equals(strTreeType))
			{
				ArrayList<VirtualItem> vis= v.getTopItems();
				for(VirtualItem vi:vis)
				{
					if (vi.getType().equals(VirtualView.Item))
					{
//							logger.info(" item: "+  vi.getItemDataZulName() + " / " + vi.getItemDataZulType()+ " / " + vi.getItemId() );
						
						//默认树情况暂不显示整体视图节点
						if(vi.getItemDataZulType().equals(VirtualItem.WholeView.zulType)
						   && strTreeType.equals(VirtualView.DefaultView))
							continue;
						
						Treeitem ti = getVirtualTreeitem(vi);
						ti.setParent(treechildren);
						
						buildSonVirtualItem(ti, v, vi, "     ");
					}					
					
					if (vi.getType().equals(VirtualView.INode))
					{
						INode node= view.getNode(vi.getSvId());
						//logger.info(" inode: " + vi.getSvId() + " / " + node.getName());
						
						Treeitem ti = getTreeitem(node);
						ti.setParent(treechildren);
						
						buildSonVirtualItem(ti, v, vi, "     ");
					}
				}
			}
		}
	}
	
	public void buildSonVirtualItem(Treeitem ti, VirtualView vv, VirtualItem v, String head)
	{
		ArrayList<VirtualItem> vis= vv.getSonItems(v);
		if(vis.size() > 0)
		{
			Treechildren mytreechildren = new Treechildren();
			ti.appendChild(mytreechildren);
			
			for(VirtualItem vi:vis)
			{
				if (vi.getType().equals(VirtualView.Item))
				{
//					logger.info(head+ " item: "+  vi.getItemDataZulName() + " / " + vi.getItemDataZulType()+ " / " + vi.getItemId() );
					
					Treeitem child = getVirtualTreeitem(vi);
					child.setParent(mytreechildren);
					
//					ti.appendChild(child);
					
					buildSonVirtualItem(child, vv, vi, "     ");
				}
				
				if (vi.getType().equals(VirtualView.INode))
				{
//					View w = Manager.getView(m_session);
//					if (w != null)
//					{
						INode node = view.getNode(vi.getSvId());
						
						Treeitem child = getTreeitem(node);
						child.setParent(mytreechildren);
						
//						ti.appendChild(child);
						
						buildSonVirtualItem(child, vv, vi, "     ");
						
//						logger.info(head + " inode: " + vi.getSvId() + " / " + node.getName()+ " /itemid: " + vi.getItemId() );
//					}
				}				
			}
		}
	}	

	public void onOK() {
		logger.info("tree ok!!");
	}

	public void onCancel() {

		pushThread.stop();
		pushThread.destroy();
	}

	public void run() {
		while (true) 
		{
			try {
				Thread.currentThread().sleep(2000);
			} catch (InterruptedException e) {
			}

			if (getDesktop() == null || !getDesktop().isServerPushEnabled()) 
				continue;

			try {
				Executions.activate(getDesktop());
				this.changeTree(getTree().getTreechildren());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				Executions.deactivate(getDesktop());
			}
		}
	}

	private void changeTree(Treechildren treechildren) {
		List<String> ids = null;//view.getChangeTree();
		if (ids == null)
			return;
		// List<String> idsInfo = view.getChangeTreeInfo();
		for (String id : ids) {
			if (id == null)
				continue;
			INode node = view.getNode(id);
			boolean bfind = false;
			for (Object obj : treechildren.getItems()) {
				if (obj instanceof Treeitem) {
					Treeitem ti = (Treeitem) obj;
					if (id.equals(ti.getId())) {
						bfind = true;
						if (node == null) {
							Component parent = ti.getParent();
							if (parent == null)
								break;
							parent.removeChild(ti);
							if (parent instanceof Treechildren) {
								Treechildren mytreechildren = (Treechildren) parent;
								if (mytreechildren.getItemCount() == 0) {
									Component itemParent = parent.getParent();
									if (itemParent != null)
										itemParent.removeChild(mytreechildren);
								}
							}
						} else {
							this.setTreeitem(ti, node);
						}
						break;
					}
				}
			}
			if (!bfind && node != null) {
				String parentid = node.getParentSvId();
				if (parentid == null)
					return;
				for (Object obj : treechildren.getItems()) {
					if (obj instanceof Treeitem) {
						Treeitem ti = (Treeitem) obj;
						if (parentid.equals(ti.getId())) {
							Treechildren mytreechildren = ti.getTreechildren();
							if (mytreechildren == null) {
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

	private Treeitem getTreeitem(INode node) {
		Treeitem tii = new Treeitem();

		if(node.getType().equals("se"))
			tii.setOpen(true);
		else
			tii.setOpen(false);
		this.setTreeitem(tii, node);

//		tii.setContext(getMenupopupidByType(node.getType()));


		//tii.setContext(getMenupopupidByType(node.getType()));
		tii.setContext(setItemMenu(node));

		return tii;
	}

	private void setTreeitem(Treeitem tii, INode node) {
		tii.setLabel(node.getName() == null || "".equals(node.getName()) ? "<"
				+ INode.UNKNOWN + ">" : node.getName());
		tii.setId(node.getSvId());

		if (INode.MONITOR.equals(node.getType())) {
			MonitorInfo info = view.getMonitorInfo(node);
			tii.setTooltiptext(info.getDstr());
		} else {
			tii.setTooltiptext(node.getStatus());
		}
		tii.setImage(getImage(node));
		tii.setValue(node);
	}

	private String getMenupopupidByType(String type) {
		return "mymenu_" + type;
	}
	
	/**
	 * 设置菜单
	 * @param node INode
	 * @return Menupopup
	 */
	private Menupopup setItemMenu(INode node)
	{
		Menupopup itemMenu=new Menupopup();
		//itemMenu.setId(node.getId());
		itemMenu.setPage(getDesktop().getPage("eccmain"));
		
		 Menu menu;
		 Menuitem item;
		 Menupopup tmp;
		if(node.getType().equals(INode.SE))
		{
		  SeInfo se=view.getSeInfo(node);
		  if(se.canAddGroup())
		  {
			  item=new Menuitem();
			  item.setLabel("添加组");
			  item.setParent(itemMenu);
		  }
		  else
		  {
			  item=new Menuitem();
			  item.setLabel("添加组");
			  item.setParent(itemMenu);
			  item.setDisabled(true);
		  }
		  if (se.canAddDevice())
		  {
			  createEntityMenu(itemMenu);
		  }
		  else
		  {
			  item=new Menuitem();
			  item.setLabel("添加设备");
			  item.setParent(itemMenu);
			  item.setDisabled(true);
		  }
		  if (se.canEdit())
		  {
			  item=new Menuitem();
			  item.setLabel("编辑");
			  item.setParent(itemMenu);
		  }
		  else
		  {
			  item=new Menuitem();
			  item.setLabel("编辑");
			  item.setParent(itemMenu);
			  item.setDisabled(true);
		  }
		}
		else if(node.getType().equals(INode.GROUP))
		{
			 GroupInfo group=view.getGroupInfo(node);
			  if(group.canAddGroup())
			  {
				  item=new Menuitem();
				  item.setLabel("添加组");
				  item.setParent(itemMenu);
			  }
			  else
			  {
				  item=new Menuitem();
				  item.setLabel("添加组");
				  item.setParent(itemMenu);
				  item.setDisabled(true);
			  }
			  if (group.canAddDevice())
			  {
				  createEntityMenu(itemMenu);
			  }
			  else
			  {
				  item=new Menuitem();
				  item.setLabel("添加设备");
				  item.setParent(itemMenu);
				  item.setDisabled(true);
			  }
			  if (group.canEdit())
			  {
				  item=new Menuitem();
				  item.setLabel("编辑");
				  item.setParent(itemMenu);
			  }
			  else
			  {
				  item=new Menuitem();
				  item.setLabel("编辑");
				  item.setParent(itemMenu);
				  item.setDisabled(true);
			  }
			  if (group.canEdit())
			  {
				  item=new Menuitem();
				  item.setLabel("禁止");
				  item.setParent(itemMenu);
			  }
			  else
			  {
				  item=new Menuitem();
				  item.setLabel("禁止");
				  item.setParent(itemMenu);
				  item.setDisabled(true);
			  }
			  
			  if (group.canPasteDevice())
			  {
				  item=new Menuitem();
				  item.setLabel("粘帖");
				  item.setParent(itemMenu);
			  }
			  else
			  {
				  item=new Menuitem();
				  item.setLabel("粘帖");
				  item.setParent(itemMenu);
				  item.setDisabled(true); 
			  }
			  if (group.canDeleteNode())
			  {
				  item=new Menuitem();
				  item.setLabel("删除");
				  item.setParent(itemMenu);
			  }
			  else
			  {
				  item=new Menuitem();
				  item.setLabel("删除");
				  item.setParent(itemMenu);
				  item.setDisabled(true); 
			  }
			
		}
		else if(node.getType().equals(INode.ENTITY))
		{
			 EntityInfo entity=view.getEntityInfo(node);
			  if(entity.canAddMonitor())
			  {
				  menu=new Menu();
				  menu.setLabel("添加监测器");
				  menu.setParent(itemMenu);
				  EntityTemplate tpl= entity.getDeviceTemplate();
				  Map<String, String> a= tpl.getSubMonitorTemplateLabel();
				  Menupopup mp=new Menupopup();
				  mp.setParent(menu);
					for(String tid:a.keySet())
					{
						 item=new Menuitem();
						 item.setLabel(a.get(tid));
						 item.setParent(mp);
					}
			  }
			  else
			  {
				  item=new Menuitem();
				  item.setLabel("添加监测器");
				  item.setParent(itemMenu);
				  item.setDisabled(true); 
			  }
			 
			  if (entity.canEdit())
			  {
				  item=new Menuitem();
				  item.setLabel("编辑");
				  item.setParent(itemMenu);
			  }
			  else
			  {
				  item=new Menuitem();
				  item.setLabel("编辑");
				  item.setParent(itemMenu);
				  item.setDisabled(true); 
			  }
			  if (entity.canTestDevice())
			  {
				  item=new Menuitem();
				  item.setLabel("测试");
				  item.setParent(itemMenu);
			  }
			  else
			  {
				  item=new Menuitem();
				  item.setLabel("测试");
				  item.setParent(itemMenu);
				  item.setDisabled(true); 
			  }
			  if (entity.canEdit())
			  {
				  item=new Menuitem();
				  item.setLabel("禁止");
				  item.setParent(itemMenu);
			  }
			  else
			  {
				  item=new Menuitem();
				  item.setLabel("禁止");
				  item.setParent(itemMenu);
				  item.setDisabled(true); 
			  }
			  if (entity.canDeleteNode())
			  {
				  item=new Menuitem();
				  item.setLabel("删除");
				  item.setParent(itemMenu);
			  }
			  else
			  {
				  item=new Menuitem();
				  item.setLabel("删除");
				  item.setParent(itemMenu);
				  item.setDisabled(true); 
			  }
			  if (entity.canRefresh())
			  {
				  item=new Menuitem();
				  item.setLabel("刷新");
				  item.setParent(itemMenu);
			  }
			  else
			  {
				  item=new Menuitem();
				  item.setLabel("刷新");
				  item.setParent(itemMenu);
				  item.setDisabled(true); 
			  }
		}
		else if(node.getType().equals(INode.MONITOR))
		{
			
		}
		else
		{}
		
		if (itemMenu.getChildren().isEmpty())
		{
			return null;
		}
		return itemMenu;
		
	}
	/**
	 * 生成设备菜单
	 * @param itemMenu
	 */
    private void createEntityMenu(Menupopup itemMenu)
    {
    	  Menupopup tmp;
    	  Menuitem item;
    	  Menu menu=new Menu();
		  menu.setLabel("添加设备");
		  menu.setParent(itemMenu);
		  Map<String, Map<String, String>> a = TemplateManager.getEntityGroupTemplateLabel();
		  Menupopup mp=new Menupopup();
		  mp.setParent(menu);
		  
		 
		  for (String gid : a.keySet())
			{
				//logger.info(" 设备组 ：" + gid);
				menu=new Menu();
				//menu.setId(gid);
				menu.setParent(mp);
		        menu.setLabel(gid);
				
		        tmp=new Menupopup();
		        tmp.setParent(menu);
				Map<String, String> e = a.get(gid);
				for (String eid : e.keySet())
				{
				    item=new Menuitem();
				    //item.setId(eid);
				    EntityTemplate entem=TemplateManager.getEntityTemplate(eid);
				    if(entem==null) continue;
				    String decription=entem.get_sv_description()==null?entem.get_sv_name():entem.get_sv_description();
				    item.setTooltiptext(decription);
				    item.setLabel(e.get(eid));
				    item.setParent(tmp);
					//logger.info("    label/id ：" + e.get(eid) + "/" + eid);
				}
			}
    }
	
	private String getImage(INode node) {
		return this.getImage(node.getType(), node.getStatus());
	}

	private void setVirtualTreeitem(Treeitem tii, VirtualItem node) {
		tii.setLabel(node.getItemDataZulName() == null || "".equals(node.getItemDataZulName()) ? "<"
				+ INode.UNKNOWN + ">" : node.getItemDataZulName());
		tii.setId(node.getItemDataZulType());

		tii.setTooltiptext(node.getItemDataZulName());

		tii.setImage(getVirtualImage(node));
		tii.setValue(node);
	}

	private Treeitem getVirtualTreeitem(VirtualItem node) 
	{
		Treeitem tii = new Treeitem();

		this.setVirtualTreeitem(tii, node);
		tii.setContext(getMenupopupidByType(node.getType()));

		return tii;
	}

	private String getVirtualImage(VirtualItem node)
	{
		return getImage(node.getItemDataZulType(), "");
	}
	
	
	private String getImage(String type, String status) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("/main/images/");
		if (status == null || status.indexOf(" ") >= 0
				|| INode.NULL.equals(status)) {
			sbf.append("none");
		} else {
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

	private void constructNode(Treeitem ti, INode n) {
		if (INode.MONITOR.equals(n.getType()))
			return;

		IForkNode f = (IForkNode) n;
		List<String> ids = f.getSonList();
		if (ids.size() > 0) {
			Treechildren mytreechildren = new Treechildren();
			ti.appendChild(mytreechildren);
			for (String id : ids) {
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
}
