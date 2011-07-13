package com.siteview.ecc.treeview.windows;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.tree.INode;
import com.siteview.base.treeEdit.SeEdit;
import com.siteview.base.treeInfo.SeInfo;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.timer.EccTimer;

public class EditSe extends GenericForwardComposer
{
	Button		btnsave;
	Button		btnclose;
	Window		wse;
	Textbox		sename;
	
	View		view;
	INode		node;
	SeEdit		seedit;
	EccTimer	eccTimer;
	
	public EditSe()
	{
		
	}
	
	public void onCreate$wse()
	{
		node = (INode) wse.getAttribute("inode");
		view = (View) wse.getAttribute("view");
		eccTimer = (EccTimer) wse.getAttribute("eccTimer");
		try
		{
			this.seedit = view.getSeEdit(node);
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String name = seedit.getProperty().get("svse_label");
		sename.setValue(name);
	}
	
	private void savedata()
	{
//		if (BaseTools.validateBackspace(sename.getValue().trim()))
//		{
//			try {
//				Messagebox.show("Se名称不能存在空白字符，请重新输入！", "提示", Messagebox.OK, Messagebox.INFORMATION);
//				sename.focus();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			return;
//		}
		this.seedit.setLabel(sename.getValue());
		try
		{
			Boolean savesuccess = this.seedit.teleSave(view);
			INode[] ids = new INode[] { seedit };
			eccTimer.refresh(ids, ChangeDetailEvent.TYPE_MODIFY);
			String name = sename.getValue();
			String id = node.getSvId();
			if (savesuccess)
			{
				String loginname = view.getLoginName();
				String minfo = "";
				
				minfo = "编辑se：" + name + "(" + id+ ") ";
				AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.se);
				
			}
			
		} catch (Exception e)
		{
		}
		wse.detach();
		
	}
	
	public void onClick$btnsave()
	{
		savedata();
	}
	
	public void onOK$wse()
	{
		savedata();
	}
}
