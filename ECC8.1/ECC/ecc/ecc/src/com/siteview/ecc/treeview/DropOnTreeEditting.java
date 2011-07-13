package com.siteview.ecc.treeview;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Window;

import com.siteview.base.data.VirtualItem;
import com.siteview.base.data.VirtualView;
import com.siteview.base.data.ZulItem;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.util.Toolkit;

public class DropOnTreeEditting implements EventListener
{
	private final static Logger logger = Logger.getLogger(DropOnTreeEditting.class);
	public void onEvent(Event event) throws Exception
	{
		if (event instanceof DropEvent)
		{
			DropEvent dropevent = (DropEvent) event;
			Treeitem move_zk = (Treeitem) dropevent.getDragged().getParent();
			EccTreeItem move = (EccTreeItem) move_zk.getValue();
			
			Treeitem target_zk = (Treeitem) dropevent.getTarget().getParent();
			EccTreeItem target = (EccTreeItem) target_zk.getValue();
			VirtualView vir = ((VirtualViewTreeModel) target_zk.getTree().getModel()).getVirtualView();
			
			if (target.getType().equals(VirtualView.GarbageType))
				delete(move_zk, move, vir);
			else
				add(move_zk, move, target, target_zk.getTree(), vir);
		}
		else if(event instanceof MouseEvent)
		{
			MouseEvent me= (MouseEvent)event;
			Treeitem target_zk = (Treeitem)me.getTarget().getParent();
			EccTreeItem target = (EccTreeItem) target_zk.getValue();
			VirtualView vir = ((VirtualViewTreeModel) target_zk.getTree().getModel()).getVirtualView();
			rename(target_zk, target, vir);
		}
	}
	
	public boolean rename(Treeitem data_zk, EccTreeItem data, VirtualView vir)
	{
		String dtype= data.getType();
		if(dtype==null)
			return false;
		String treeid= data_zk.getTree().getId();
		if(treeid!=null && !treeid.equals("treeEditting"))
			return false;
	
		try
		{
			if (data.getType().equals(VirtualView.ViewEdittingType))
			{
				Object usersessionid = getDesktop().getSession().getAttribute(
				"usersessionid");
				if (usersessionid == null) {
					Executions.getCurrent().sendRedirect("/index.jsp", "_top");
					return false;
				}
				View w = Manager.getView(usersessionid.toString());
				if (w == null)
					return false;
				
				final Window win = (Window) Executions.createComponents(
						"editVirtualGroupName.zul", null, null);
				win.setTitle("编辑视图，名称不能为空");
				((Label)win.getFellow("label")).setValue("视图名称:");
				Button btnAddName = (Button)win.getFellow("btnAddName");
				Button btnCancelAdd = (Button)win.getFellow("btnCancelAdd");
				btnAddName.setLabel("保存");
				btnCancelAdd.setLabel("取消");
				btnAddName.setImage("/main/images/button/ico/save_bt.gif");
				btnCancelAdd.setImage("/main/images/button/ico/close_bt.gif");
				btnAddName.setSclass("btnDefault");
				btnCancelAdd.setSclass("btnDefault");
				String width = "74px";
				String height = "23px";
				btnAddName.setWidth(width);
				btnAddName.setHeight(height);
				btnCancelAdd.setWidth(width);
				btnCancelAdd.setHeight(height);
				((Textbox)win.getFellow("name")).setValue(data.getId());
				win.doModal();
				String value= ((Textbox)win.getFellow("name")).getValue();
//				logger.info(" Virtual Group Name is: " + value);
			
				if (value == null || value.isEmpty())
					return false;
				if (!w.changeNameOfVirtualView(vir, value))
					return false;
				logger.info("virtual view \"" + vir.getViewName() + "\" rename to \"" + value + "\"");

				Combobox viewSelectEditting = (Combobox) (getDesktop().getPage("eccmain").getFellow("viewSelectEditting"));
				viewSelectEditting.setAttribute("viewNameToSelect", value);
				viewSelectEditting.open();
				viewSelectEditting.close();
				return true;
			}
			
			if(!dtype.equals(INode.GROUP) || !data.getId().startsWith("i"))
				return false;
			String zultype = VirtualItem.VirtualGroup.zulType;
			String zulname = data.getTitle();			
			ZulItem zi = new ZulItem(zultype, zulname, "");
			String pitemid= data.getParent().getItemId();
			vir.changeItem(data.getItemId(), pitemid, zi, null);

			vir.cancelAllChange();
			String vname = getVirtualGroupName(zulname);
			if (vname == null || vname.isEmpty())
			{
				Messagebox.show("虚拟组名称为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				vir.cancelAllChange();
				return false;
			}
			zulname = vname;
			zi = new ZulItem(zultype, zulname, "");
			vir.changeItem(data.getItemId(), pitemid, zi, null);
			
			logger.info("virtual view: " + vir.getViewName() + " ,rename to: " + zulname + "(id:" + data.getId() + ")");
			reload(false, data.getItemId(), data_zk.getTree(), vir);
			return true;
		} catch (Exception e)
		{
			e.printStackTrace();
			vir.cancelAllChange();
		}	
		return false;
	}
	
	public boolean delete(Treeitem data_zk, EccTreeItem data, VirtualView vir)
	{
		String treeid= data_zk.getTree().getId();
		if(treeid!=null && !treeid.equals("treeEditting"))
			return false;
		if(data.getType().equals(VirtualView.NewVirGroupType))
			return false;
		
		try
		{
			if (data.getType().equals(VirtualView.ViewEdittingType))
			{
				String vstr="确定要删除虚拟视图：\"" + data.getTitle() + "\"吗？";
				if( Messagebox.OK != Messagebox.show(vstr, "询问", Messagebox.OK| Messagebox.CANCEL, Messagebox.QUESTION) )
					return false;
				View view = Toolkit.getToolkit().getSvdbView(getDesktop().getSession());
				view.deleteVirtualView(vir);
				
				Combobox viewSelectEditting = (Combobox) (getDesktop().getPage("eccmain").getFellow("viewSelectEditting"));
				viewSelectEditting.setSelectedItem(null);
				viewSelectEditting.open();
				viewSelectEditting.close();
				return true;
			}
			
			String ItemId= data.getItemId();
			vir.deleteItem(ItemId);
			String str="确定要删除\"" + data.getTitle() + "\"及其所有子孙吗？";
			if( Messagebox.OK == Messagebox.show(str, "询问", Messagebox.OK| Messagebox.CANCEL, Messagebox.QUESTION) )
			{
				logger.info("virtual view: "+ vir.getViewName() +" ,delete: " +data.getTitle()+"(id:"+ ItemId+")");
				EccTreeItem peccitem= (EccTreeItem)data_zk.getParentItem().getValue();
				reload(false, peccitem.getItemId(), data_zk.getTree(), vir);
				return true;
			}
			else
				vir.cancelAllChange();
		} catch (Exception e)
		{
			e.printStackTrace();
			vir.cancelAllChange();
		}	
		return false;
	}
	
	private Desktop getDesktop()
	{
		Desktop _deskTop= null;
		if(_deskTop==null)
			_deskTop=Executions.getCurrent().getDesktop();
		return _deskTop;
	}
	
	public String getVirtualGroupName(String oldname)
	{								
		try
		{
			final Window win = (Window) Executions.createComponents(
					"editVirtualGroupName.zul", null, null);
			if (oldname!=null)
			{
				win.setTitle("重命名虚拟组，名称不能为空");
				((Label) win.getFellow("label")).setValue("新的名称:");
				((Button) win.getFellow("btnAddName")).setLabel("重命名");

				try
				{
					if(oldname.startsWith("虚拟组："))
						oldname= oldname.substring(4);
				} catch (Exception e)
				{
				}

				((Textbox)win.getFellow("name")).setValue(oldname);
			}
			win.doModal();
			String value= ((Textbox)win.getFellow("name")).getValue();
//			logger.info(" Virtual Group Name is: " + value);
			return value;
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean add(Treeitem data_zk, EccTreeItem data, EccTreeItem parent, Tree tree, VirtualView vir)
	{
		String ptype= parent.getType();
		String dtype= data.getType();
		if(ptype==null || dtype==null)
			return false;
		if("monitor".equals(dtype) && !"entity".equals(ptype)){
			try {
				Messagebox.show("只能在设备下添加监测器！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				return false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if("monitor".equals(dtype) && !parent.getId().equals(data.getParent().getId())){
			try {
				Messagebox.show("监测器不属于该设备，不允许添加！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				return false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		List list = parent.getChildRen();
		Iterator it = list.iterator();
		boolean isSame = false;
		while(it.hasNext()){
			isSame = false;
			EccTreeItem item = (EccTreeItem)it.next();
			if("monitor".equals(data.getType()) || "entity".equals(data.getType()) || "group".equals(data.getType()) || "se".equals(data.getType())){
				if(item.getId().equals(data.getId())){
					isSame = true;
				}
			}else{
				if(item.getTitle().equals(data.getTitle())){
					isSame = true;
				}
			}
			
			if(isSame){
				try {
					Messagebox.show("已存在相同节点！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
		}
		
		if ( !dtype.equals(VirtualView.NewVirGroupType) )
		{
			String treeid = data_zk.getTree().getId();
			if (treeid != null && treeid.equals("treeEditting"))
				return move(data_zk, data, parent, vir);
		}
		if(ptype.equals(INode.MONITOR) || dtype.equals(VirtualView.ViewEdittingType))
			return false;
		
		try
		{		
			if( data.getId().startsWith("i") )
			{
				String zultype= data.getType();
				String zulname= data.getTitle();
				if(dtype.equals(INode.GROUP) || dtype.equals(VirtualView.NewVirGroupType))
					zultype= VirtualItem.VirtualGroup.zulType;
				
				ZulItem zi= new ZulItem(zultype, zulname, "");
				vir.addItem(parent.getItemId(), zi, null);
				vir.cancelAllChange();
				
				if( dtype.equals(VirtualView.NewVirGroupType) )
				{
					String vname= getVirtualGroupName(null);
					if( vname==null || vname.isEmpty() )
					{	
						vir.cancelAllChange();
						return false;
					}
					zulname= vname;
				}
				else
				{
					String str="确定要添加\"" + data.getTitle() + "\" 到\r\n \"" + parent.getTitle() + "\" 下吗？";
					int mret= Messagebox.show(str, "询问", Messagebox.OK| Messagebox.CANCEL, Messagebox.QUESTION);
					if( Messagebox.OK != mret)
					{
						vir.cancelAllChange();
						return false;
					}
				}
				zi= new ZulItem(zultype, zulname, "");
				vir.addItem(parent.getItemId(), zi, null);
					
				logger.info("virtual view: "+ vir.getViewName() +" ,add: " +zulname+"(id:"+ data.getId()+") to " + parent.getTitle() + "(id:" +parent.getId()+")");
				reload(true, parent.getItemId() , tree, vir);
				return true;
			}
			View view = Toolkit.getToolkit().getSvdbView(getDesktop().getSession());
			INode node = view.getNode(data.getId());
			vir.addINode(parent.getItemId(), node, false);
			vir.cancelAllChange();

			final Window win = (Window) Executions.createComponents(
					"addINodeSelection.zul", null, null);
			win.setTitle("节点 \""+data.getTitle()+"\" 添加至 \""+parent.getTitle() +"\" 的选项");
			Radio radio1=((Radio) win.getFellow("justAddSelf"));
			Radio radio2=((Radio) win.getFellow("AddWithAllSubMonitor"));
			Radio radio3=((Radio) win.getFellow("AddWithConstruction"));
			if(node.getType().equals(INode.MONITOR))
			{
				radio1.setSelected(true);
				radio2.setVisible(false);
				radio3.setVisible(false);
			}
			if(node.getType().equals(INode.ENTITY))
			{
				radio1.setSelected(true);
				radio2.setVisible(true);
				radio2.setLabel("添加自身，及监测器");
				radio3.setVisible(false);
			}
			if(node.getType().equals(INode.GROUP)){
				radio1.setSelected(true);
				radio2.setVisible(false);
				radio3.setVisible(true);
			}
			
			win.doModal();
			String selection= ((Textbox)win.getFellow("name")).getValue();
			if(selection!=null && !selection.isEmpty())
			{
				if(selection.equals("1"))
					vir.addINode(parent.getItemId(), node, false);
				else if (selection.equals("2"))
					vir.addINode(parent.getItemId(), node, true);
				else if (selection.equals("3"))
					vir.addINodeWithConstruction(parent.getItemId(), node);
				else
					throw new Exception(" 未知选择，节点 \""+data.getTitle()+"\" 添加至 \""+parent.getTitle() +"\" ");
				logger.info("virtual view: "+ vir.getViewName() +" ,add: " +data.getTitle()+"(id:"+ data.getId()+") to " + parent.getTitle() + "(id:" +parent.getId()+")");
				reload(true, parent.getItemId() , tree, vir);
				return true;
			}
		} catch (Exception e)
		{
			e.printStackTrace();
			vir.cancelAllChange();
		}	
		return false;
	}
	
	public boolean move(Treeitem data_zk, EccTreeItem data, EccTreeItem parent, VirtualView vir)
	{
		String ptype = parent.getType();
		String dtype = data.getType();
		if (ptype == null || dtype == null)
			return false;
		if (dtype.equals(VirtualView.NewVirGroupType) || dtype.equals(VirtualView.ViewEdittingType))
			return false;	
		String treeid = data_zk.getTree().getId();
		if (treeid == null || !treeid.equals("treeEditting"))
			return false;
		
		try
		{
			vir.moveItem(data.getItemId(), parent.getItemId());
			String str="确定要移动\"" + data.getTitle() + "\" 及其所有子孙到\r\n \"" + parent.getTitle() + "\" 下吗？";
			if( Messagebox.OK == Messagebox.show(str, "询问", Messagebox.OK| Messagebox.CANCEL, Messagebox.QUESTION) )
			{
				logger.info("virtual view: "+ vir.getViewName() +" ,move: " +data.getTitle()+"(id:"+ data.getId()+") to " + parent.getTitle() + "(id:" +parent.getId()+")");
				reload(true, parent.getItemId(), data_zk.getTree(), vir);
				return true;
			}
			else
				vir.cancelAllChange();
		} catch (Exception e)
		{
			e.printStackTrace();
			vir.cancelAllChange();
		}	
		return false;
	}

	public void reload(boolean newone, String selectId, Tree tree, VirtualView vir) throws Exception
	{
		vir.saveChange();
		
		VirtualViewTreeModel.removeInstance(tree.getDesktop().getSession(), vir.getViewName());
		VirtualViewTreeModel eccTreeModle= VirtualViewTreeModel.getInstance(tree.getDesktop().getSession(), vir.getViewName());
		eccTreeModle.getRoot();
		eccTreeModle.setTreeEdittingNode();
		eccTreeModle.setDisplayMonitor(true);
		tree.setModel(eccTreeModle);
		
		EccTimer timer=(EccTimer)getDesktop().getPage("eccmain").getFellow("header_timer");
		if(timer!=null)
			timer.addTimerListener("treeEdittingForVirtualViewEdit", eccTreeModle);

		
		Treeitem item = select(selectId, tree, null, vir);
		if (item != null && newone)
		{
			item.setOpen(true);
			Treeitem childitem = selectLastChild(tree, item);
			if(childitem!=null)
				item= childitem;
		}
		if (item != null)
		{
			tree.selectItem(item);
			item.setOpen(true);
		}
	}

	public Treeitem select(String selectId, Tree tree, Treeitem treeitem, VirtualView vir)
	{
		try
		{
			Treechildren children=null;
			if(treeitem==null)
				children= tree.getTreechildren();
			else
				children= treeitem.getTreechildren();
			if(children==null)
				return null;
			Iterator iterator= children.getVisibleChildrenIterator();
			while(iterator.hasNext())
			{
				Object obj = iterator.next();
				if(obj==null)
					continue;
				Treeitem tempitem = (Treeitem) obj;
				EccTreeItem eccTreeitem = (EccTreeItem) tempitem.getValue();
				String tempItemId= eccTreeitem.getItemId();
//			logger.info(" select ItemId: " + selectId + "  ItemId: " + tempItemId);
				if (tempItemId!=null && tempItemId.equals(selectId))
						return tempitem;

				if (tempItemId!=null && selectId.contains(".") && !selectId.startsWith(tempItemId) && !tempItemId.equals(VirtualView.ViewEdittingType) )
					continue;
				if (tempItemId!=null && !selectId.contains(".") && !tempItemId.equals(VirtualView.ViewEdittingType))
					continue;
				if(eccTreeitem.getChildRen().isEmpty())
					continue;
				
				tempitem.setOpen(true);			
				Treeitem subitem= select(selectId, tree, tempitem, vir); 
				if( subitem==null )
					tempitem.setOpen(false);
				else
					return subitem;
			}
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
//		 select ItemId: 1.6.1.1  ItemId: null
//		 select ItemId: 1.6.1.1  ItemId: null
//		 select ItemId: 1.6.1.1  ItemId: 虚拟视图3
//		 select ItemId: 1.6.1.1  ItemId: 1
//		 select ItemId: 1.6.1.1  ItemId: 1.1
//		 select ItemId: 1.6.1.1  ItemId: 1.2
//		 select ItemId: 1.6.1.1  ItemId: 1.3
//		 select ItemId: 1.6.1.1  ItemId: 1.4
//		 select ItemId: 1.6.1.1  ItemId: 1.5
//		 select ItemId: 1.6.1.1  ItemId: 1.6
//		 select ItemId: 1.6.1.1  ItemId: 1.6.1
//		 select ItemId: 1.6.1.1  ItemId: 1.6.1.1
	}

	public Treeitem selectLastChild(Tree tree, Treeitem treeitem)
	{
		try
		{
			Treeitem childitem = null;
			Treechildren children= treeitem.getTreechildren();
			if(children==null)
				return null;
			Iterator iterator= children.getVisibleChildrenIterator();
			while(iterator.hasNext())
			{
				Object obj = iterator.next();
				if(obj==null)
					continue;
				Treeitem tempitem = (Treeitem) obj;
				childitem= tempitem;
			}
			return childitem;
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
