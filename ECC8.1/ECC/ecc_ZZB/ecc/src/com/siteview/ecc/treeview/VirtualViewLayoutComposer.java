package com.siteview.ecc.treeview;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.OpenEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.metainfo.ComponentInfo;
import org.zkoss.zk.ui.util.ComposerExt;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Window;

import com.siteview.base.data.VirtualItem;
import com.siteview.base.data.VirtualView;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.ecc.timer.EccTimer;

public class VirtualViewLayoutComposer extends GenericForwardComposer implements ComposerExt,TreeitemRenderer
{

	private Combobox viewSelectEditting;
	private Tree treeOriginal;
	private Tree treeEditting;
	
	@Override
	public void render(Treeitem item, Object data) throws Exception
	{
		EccTreeItem node = (EccTreeItem) data;
		item.setLabel(node.toString());
		item.setValue(data);
		if(node.getType().equals(INode.GROUP)||node.getType().equals(INode.ENTITY)||node.getType().equals(INode.MONITOR))
			item.setImage(EccWebAppInit.getInstance().getImage(node.getType(),node.getStatus()));
		else
			item.setImage(EccWebAppInit.getInstance().getImage(node.getType()));
		String treeid= item.getTree().getId();
		if(treeid!=null && treeid.equals("treeOriginal"))
		{
			item.getTreerowApi().setDraggable("true");//可以拖动
			String type= node.getType(); 
			if( type!=null && type.equals(INode.SE) )
				item.getTreerowApi().setDraggable("false");//可以拖动
		}
		if (treeid != null && treeid.equals("treeEditting"))
		{
			item.getTreerowApi().setDroppable("true");//可以释放
			item.getTreerowApi().setDraggable("true");//可以拖动
			
			String type= node.getType(); 
			if( type!=null && type.equals(VirtualView.GarbageType) ){
				item.getTreerowApi().setDraggable("false");//可以拖动
			}
			if( type!=null && type.equals(VirtualView.NewVirGroupType) ){
				item.getTreerowApi().setDroppable("false");//可以释放
			}
			item.getTreerow().addEventListener("onDrop", new DropOnTreeEditting());
			String id= node.getId();
			if( type!=null && type.equals(INode.GROUP) && id!=null && id.startsWith("i") )
				item.getTreerow().addEventListener("onDoubleClick", new DropOnTreeEditting());
			if( type!=null && type.equals(VirtualView.ViewEdittingType) )
				item.getTreerow().addEventListener("onDoubleClick", new DropOnTreeEditting());
		}
	}

	@Override
	public ComponentInfo doBeforeCompose(Page page, Component parent,
			ComponentInfo compInfo) {
		// TODO Auto-generated method stub
		return compInfo;
	}

	@Override
	public void doBeforeComposeChildren(Component comp) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean doCatch(Throwable ex) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void doFinally() throws Exception {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
		Components.addForwards(comp, this);
		treeOriginal.setTreeitemRenderer(this);
		treeEditting.setTreeitemRenderer(this);
		
		onCreate$treeOriginal();
		selectItem(null);
		onSelect$viewSelectEditting(null);	
		
		EccTimer timer=(EccTimer)this.desktop.getPage("eccmain").getFellow("header_timer");
		if(timer!=null)
			timer.addTimerListener("treeOriginalForVirtualViewEdit", (DefaultViewTreeModel)treeOriginal.getModel());
		
		treeOriginal.addEventListener("onSelect",new EventListener(){
			@Override
			public void onEvent(Event event) throws Exception{
					try{
						SelectEvent e=(SelectEvent)event;
						if (!e.getSelectedItems().isEmpty()){
							Treeitem treeitem=(Treeitem) e.getSelectedItems().iterator().next();
							treeitem.setOpen(true);				
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		);
		treeEditting.addEventListener("onSelect",new EventListener(){
			@Override
			public void onEvent(Event event) throws Exception{
					try{
						SelectEvent e=(SelectEvent)event;
						if (!e.getSelectedItems().isEmpty()){
							Treeitem treeitem=(Treeitem) e.getSelectedItems().iterator().next();
							treeitem.setOpen(true);				
						}
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		);

	}
	
	public void onCreate$treeOriginal()
	{
		DefaultViewTreeModel model = (DefaultViewTreeModel) treeOriginal.getModel();
		if(model==null)
		{
			model= DefaultViewTreeModel.getInstance(session);
			treeOriginal.setModel(model);
		}
		model.setDisplayMonitor(true);
		
		try
		{
			Treechildren children= treeOriginal.getTreechildren();
			if(children==null)
				return;
			Iterator iterator = children.getVisibleChildrenIterator();
			
			if (iterator.hasNext())
			{
				Treeitem topNode = (Treeitem) iterator.next();
				topNode.setOpen(true);
				children= topNode.getTreechildren();
				if(children==null)
				{
					treeOriginal.selectItem(topNode);
					return;
				}
				iterator = children.getVisibleChildrenIterator();
				if (iterator.hasNext())
				{
					Treeitem seNode = (Treeitem) iterator.next();
					treeOriginal.selectItem(seNode);
					seNode.setOpen(true);
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void onOpen$viewSelectEditting(OpenEvent event)
	{
		String vname=null;
		try
		{
			vname= (String)viewSelectEditting.getAttribute("viewNameToSelect");
			viewSelectEditting.removeAttribute("viewNameToSelect");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		if(vname!=null)
		{
			selectItem(vname);
			onSelect$viewSelectEditting(null);
			return;
		}
		
		Comboitem sel = viewSelectEditting.getSelectedItem();
		if (sel == null)
		{
			selectItem(null);
			treeEditting.focus();
		}
	}
	
	public void selectItem(String vname)
	{
		Object usersessionid = this.desktop.getSession().getAttribute(
		"usersessionid");
		if (usersessionid == null) {
			Executions.getCurrent().sendRedirect("/index.jsp", "_top");
			return;
		}
		View w = Manager.getView(usersessionid.toString());
		if (w == null)
			return;
		int count = viewSelectEditting.getItemCount();
		if (count > 0)
		{
			for (int index = count-1; index>=0; --index)
				viewSelectEditting.removeItemAt(index);
		}
		
		// 暂时将默认视图设为基础树
		int nSelIndex = 0;
		int tempIndex = 0;
		List<VirtualView> av = null;
		try {
			av = w.getAllVirtualViewThrowException();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(av != null){
			Collections.sort(av, new Comparator<VirtualView>(){
				@Override
				public int compare(VirtualView o1, VirtualView o2) {
					return o1.getViewName().compareToIgnoreCase(o2.getViewName());
				}
			});

			// 选择一个虚拟视图，进行编辑
			for (VirtualView v : av)
			{
				if (v.getViewName().equals(VirtualView.DefaultView))
					continue;
				viewSelectEditting.appendItem(v.getViewName());
				if(vname==null)
					nSelIndex = 0;
				else if(v.getViewName().equals(vname))
					nSelIndex= tempIndex; 	
				tempIndex++;
			}
			viewSelectEditting.appendItem(VirtualView.CreateNewView);
		}else{
			viewSelectEditting.appendItem(VirtualView.CreateNewView);
		}
				
		if (nSelIndex >= 0)
			viewSelectEditting.setSelectedIndex(nSelIndex);
	}
	
	public void onSelect$viewSelectEditting(SelectEvent event)
	{
		Comboitem item = viewSelectEditting.getSelectedItem();
		if (item == null)
			treeEditting.clear();
		else
		{
			VirtualViewTreeModel.removeInstance(session, item.getLabel());
			if (item.getLabel().equals(VirtualView.CreateNewView))
				treeEditting.clear();
			else
			{
				VirtualViewTreeModel model = com.siteview.ecc.treeview.VirtualViewTreeModel.getInstance(session, item.getLabel());
				model.getRoot();
				model.setTreeEdittingNode();
				model.setDisplayMonitor(true);
				treeEditting.setModel(model);
				trySelectVirtualViewNode();
			}
			
			if(event!=null && item.getLabel().equals(VirtualView.CreateNewView))
				createNewVirtualView();
		}
		EccTimer timer=(EccTimer)this.desktop.getPage("eccmain").getFellow("header_timer");
		if(timer!=null)
			timer.addTimerListener("treeEdittingForVirtualViewEdit", (VirtualViewTreeModel)treeEditting.getModel());

		treeEditting.focus();
	}

	public void trySelectVirtualViewNode()
	{
		try
		{
			Treechildren children= treeEditting.getTreechildren();
			if(children==null)
				return ;
			Iterator iterator= children.getVisibleChildrenIterator();
			Treeitem item = null;
			while(iterator.hasNext())
			{
				Object obj = iterator.next();
				if(obj==null)
					continue;
				item = (Treeitem) obj;
			}
			if (item != null)
			{
				treeEditting.selectItem(item);
				item.setOpen(true);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void createNewVirtualView()
	{
		try
		{
			Object usersessionid = this.desktop.getSession().getAttribute(
			"usersessionid");
			if (usersessionid == null) {
				Executions.getCurrent().sendRedirect("/index.jsp", "_top");
				return;
			}
			View w = Manager.getView(usersessionid.toString());
			if (w == null)
				return;
			
			final Window win = (Window) Executions.createComponents(
					"editVirtualGroupName.zul", null, null);
			win.setTitle("新建虚拟视图，名称不能为空");
			win.setClosable(true);
			((Label)win.getFellow("label")).setValue("视图名称*:");
			Button btnAdd = (Button)win.getFellow("btnAddName");
			Button btnCancel = (Button)win.getFellow("btnCancelAdd");
			btnAdd.setLabel("保存");
			btnCancel.setLabel("取消");
			btnAdd.setImage("/main/images/button/ico/save_bt.gif");
			btnCancel.setImage("/main/images/button/ico/close_bt.gif");
			btnAdd.setSclass("btnDefault");
			btnCancel.setSclass("btnDefault");
			String width = "74px";
			String height = "23px";
			btnAdd.setWidth(width);
			btnAdd.setHeight(height);
			btnCancel.setWidth(width);
			btnCancel.setHeight(height);
//			((Button)win.getFellow("btnAddName")).setLabel("保存");
//			((Button)win.getFellow("btnCancelAdd")).setLabel("取消");
			win.doModal();
			String value= ((Textbox)win.getFellow("name")).getValue();
//			logger.info(" Virtual Group Name is: " + value);
		
			if(value==null || value.isEmpty())
			{
				selectItem(null);
				onSelect$viewSelectEditting(null);
				
				return;
			}
			try
			{
				VirtualView vir= w.createVirtualView(value);
				try
				{
					vir.addItem(VirtualView.ViewEdittingType, VirtualItem.WholeView, null);
					vir.saveChange();
				} catch (Exception e)
				{
				}
			} catch (Exception ee)
			{
				selectItem(null);
				onSelect$viewSelectEditting(null);
				ee.printStackTrace();
				return;
			}
			selectItem(value);
			onSelect$viewSelectEditting(null);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
