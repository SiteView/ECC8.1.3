package com.siteview.ecc.treeview.windows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jruby.libraries.RbConfigLibrary;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Window;

import com.siteview.base.data.QueryInfo;
import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.Manager;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.base.queue.ChangeDetailEvent;
import com.siteview.base.tree.GroupNode;
import com.siteview.base.tree.INode;
import com.siteview.base.treeEdit.EntityEdit;
import com.siteview.base.treeEdit.GroupEdit;
import com.siteview.base.treeEdit.MonitorEdit;
import com.siteview.base.treeInfo.GroupInfo;
import com.siteview.base.treeInfo.SeInfo;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.tasks.Task;
import com.siteview.ecc.tasks.TaskPack;
import com.siteview.ecc.timer.EccTimer;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccTreeModel;
import com.siteview.ecc.treeview.telebackup.MonitorSelectTree;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svdb.UnivData;

public class LoadSmallEcc extends GenericForwardComposer
{
	Window 		smallEccUrl;
	Textbox   	smallUrlValue;
	Button      urlButton;
	String      flag = "";
	
	public void onCreate$smallEccUrl(){
		try{
			flag = (String)smallEccUrl.getAttribute("flag");
			
			Object flagObject = session.getAttribute(flag);
			if(flagObject!=null){
				String flag_str = (String)flagObject;
				if("".equals(flag_str))return;
				smallUrlValue.setValue(flag_str);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void onClick$urlButton()
	{
		
/*		final Window win = (Window) Executions.createComponents("/main/TreeView/newWindow.zul", null, null);
		win.setAttribute("smallEccUrl", smallUrlValue.getValue());
		try
		{
			win.doModal();
		} catch (Exception e)
		{
		}
		*/
//		Executions.sendRedirect(smallUrlValue.getValue());
		
		//要对输入的地址进行验证
				
		String temp = smallUrlValue.getValue().trim();
		if("".equals(temp)){
			try{
				Messagebox.show("输入的地址为空!", "提示", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			return;
		}
		Pattern regex = Pattern.compile("^http://([1-9]|[1-9]\\d|1\\d{2}|2[0-1]\\d|22[0-3])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}(:)([0-9]{1,8})(/ecc)$");//端口号在 1到8位之间
		Matcher matcher = regex.matcher(temp);
		if (!matcher.matches()) {
			try{
				Messagebox.show("输入的地址格式不正确!", "提示", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			return;
		}
		
		session.setAttribute(flag, smallUrlValue.getValue().trim());
		
		Executions.getCurrent().sendRedirect(smallUrlValue.getValue(), "_blank");

		smallEccUrl.detach();
	}


}
