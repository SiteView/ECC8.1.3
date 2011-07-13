package com.siteview.ecc.treeview.windows;

import java.util.ArrayList;
import java.util.List;

import org.jruby.libraries.RbConfigLibrary;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.tree.GroupNode;
import com.siteview.base.tree.INode;
import com.siteview.base.treeEdit.EntityEdit;
import com.siteview.base.treeEdit.GroupEdit;
import com.siteview.base.treeInfo.GroupInfo;
import com.siteview.base.treeInfo.SeInfo;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccTreeModel;
import com.siteview.ecc.util.Toolkit;

public class AddGroup extends GenericForwardComposer
{
	
	private static String	Dependontree_TargetUrl	= "/main/TreeView/dependontree.zul";
	// 组件绑定
	
	Textbox					tbDescription;
	Textbox					tbDepends;
	Textbox					tbname;
	Button					btnDepends;
	Radio					rdGood;
	Radio					rdWarning;
	Radio					rdError;
	Window					WAddGroup;														// embeded object,
																							
	Button					btnok;
	Button					btncancel;
	Button					btnhelp;
	// 其他
	private View			view;
	private Tree			tree;
	private String			svid;
	private GroupEdit		groupEdit;
	private INode			node;
	
	private Include			eccbody;
	private EccTimer		eccTimer;
	Boolean					isedit;
	
	public AddGroup()
	{
		
	}
	
	public void onClick$btnDepends()
	{
		final Window win = (Window) Executions.createComponents(Dependontree_TargetUrl, null, null);
		win.setAttribute("tb", tbDepends);
		try
		{
			win.doModal();
		} catch (Exception e)
		{
		}
	}
	
	public void onCreate$WAddGroup()
	{
		node = (INode) WAddGroup.getAttribute("inode");
		view = (View) WAddGroup.getAttribute("view");
        //重新获得
		node=view.getNode(node.getSvId());
		isedit = (Boolean) WAddGroup.getAttribute("isedit");
		eccTimer = (EccTimer) WAddGroup.getAttribute("eccTimer");
		tree=(org.zkoss.zul.Tree) WAddGroup.getDesktop().getPage("eccmain").getFellow("tree");
		try
		{
			if (isedit)
			{
				WAddGroup.setTitle("编辑组");
				this.groupEdit = view.getGroupEdit(node);
				String name = groupEdit.getName();
				String depends = groupEdit.getSvDependsOn();
				String description = groupEdit.getSvDescription();
				String condtion = groupEdit.getProperty().get("sv_dependscondtion");
				String value = groupEdit.getProperty().get("sv_dependson");
				if (value == null)
				{
					value = "";
				}
				tbname.setValue(name);
				tbDepends.setValue(depends);
				tbDepends.setAttribute("value", value);
				tbDescription.setValue(description);
				if (condtion.equals("2"))
				{
					rdWarning.setChecked(true);
				} else if (condtion.equals("1"))
				{
					rdGood.setChecked(true);
				}
				
			} else
			{
				if (this.node.getType().equals(INode.SE))
				{
					SeInfo seinfo = this.view.getSeInfo(node);
					this.groupEdit = seinfo.AddGroup();
				}
				if (this.node.getType().equals(INode.GROUP))
				{
					GroupInfo groupinfo = this.view.getGroupInfo(node);
					this.groupEdit = groupinfo.AddGroup();
				}
				
				tbDepends.setAttribute("value", "");
			}
		} catch (Exception ex)
		{
		}
	}
	
	/**
	 * 保存
	 */
	public void onClick$btnok()
	{
		try{
			savedata();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	public void onOK$WAddGroup()
	{
		savedata();
	}
	
	private void savedata()
	{
		if (tbname.getValue().trim().equals(""))
		{
			try
			{
				Messagebox.show("组名称不能为空！", "提示", Messagebox.OK, Messagebox.EXCLAMATION);
				tbname.focus();
				return;
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// sv_dependscondtion sv_description sv_disable
		String name = tbname.getValue();
		String depend = (String) tbDepends.getAttribute("value");
		String description = tbDescription.getValue();
		String condtion = "3";
		if (rdWarning.isChecked())
		{
			condtion = "2";
		}
		if (rdGood.isChecked())
		{
			condtion = "1";
		}
		groupEdit.getProperty().put("sv_name", name);
		groupEdit.setName(name);
		this.groupEdit.getProperty().put("sv_dependson", depend);
		this.groupEdit.getProperty().put("sv_description", description);
		this.groupEdit.getProperty().put("sv_dependscondtion", condtion);
		try
		{
		  Boolean savesuccess=	this.groupEdit.teleSave(view);
			String id = this.groupEdit.getSvId();
			WAddGroup.detach();
			
			INode[] ids = new INode[] { groupEdit };
			if (isedit)
			{
				eccTimer.refresh(ids, ChangeDetailEvent.TYPE_MODIFY);
			} else
			{
				eccTimer.refresh(ids, ChangeDetailEvent.TYPE_ADD);
			}
			// eccTimer.refresh(ids, true,true);
			if (savesuccess)
			{
				String loginname = view.getLoginName();
				String minfo = "";

				if (isedit )
				{
					minfo = "编辑组：" + name + "(" + node.getSvId() + ") " ;
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.group);
				} else
				{
					minfo = "添加组：" + name+ "(" +id + ") parent is " + node.getName() + "(" + node.getSvId() + ")";
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.group);
				}
				Session session = Executions.getCurrent().getDesktop().getSession();
				session.setAttribute(ConstantValues.RefreshGroupId, id);
			}
			
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Toolkit.getToolkit().expandTreeAndShowList(tree.getDesktop(), node);
		
	}
	public void onClick$btnhelp()
	{
		for (int i = 1; i < 7; i++)
		{
			Label lb = (Label) WAddGroup.getFellow("lbp" + i);
			lb.setVisible(!lb.isVisible());
		}
		WAddGroup.setPosition("center");
	}
	
	/**
	 * 关闭
	 */
	public void onClick$btncancel()
	{
		WAddGroup.detach();
	}
	
}
