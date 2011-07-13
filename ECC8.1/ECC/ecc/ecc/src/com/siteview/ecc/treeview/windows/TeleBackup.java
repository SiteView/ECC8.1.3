package com.siteview.ecc.treeview.windows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class TeleBackup extends GenericForwardComposer
{
	Window monitorselect;
	Tree monitorTree;
	Button	OK;
	View  view;
	INode node;

	public void onClick$OK()
	{
		try{
			showProgressWindow();
			monitorselect.detach();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	// 修改 监测器 的值
	protected boolean savedata(){
		try{
			List<String> selectedIds = ((MonitorSelectTree)monitorTree).getSelectedIds();//选中的
			Map<String, Map<String, String>> allmonitors = null;
			List<String> allIds = new ArrayList<String>(); //所有的
			List<String> un_selectedIds = new ArrayList<String>();//未能被选中的
			try {
				allmonitors = UnivData.queryAllMonitorInfo();//查找所有的监视器
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}		
			for(String key_1:allmonitors.keySet()){
				if(key_1 == null || "".equals(key_1))continue;
				allIds.add(key_1);
			}
			String selectedIds_Str = "";
			if(selectedIds.size()>0){
				selectedIds_Str = changeListToString(selectedIds);
			}
			
			for(String id:allIds){//查找未被选中的 id
				if(selectedIds_Str.contains(id)){continue;}
				un_selectedIds.add(id);
			}
			
			String unSelectedIds_Str = "";
			if(un_selectedIds.size()>0){
				unSelectedIds_Str = changeListToString(un_selectedIds);
			}
			
			Map<String, Map<String, String>> dataTrue	=	buildBaseData_true();
			Map<String, Map<String, String>> dataFalse	=	buildBaseData_false();

			//分多种情况
			boolean rt1 = true;
			if(!"".equals(selectedIds_Str)){
				try{
					rt1 = SetValueInManyMonitor_OnlyProperty(selectedIds_Str,dataTrue);
				}catch(Exception e){
					e.printStackTrace();
					rt1 = false;
				}
			}
			boolean rt2 = true;
			if(!"".equals(unSelectedIds_Str)){
				try{
					rt2 = SetValueInManyMonitor_OnlyProperty(unSelectedIds_Str,dataFalse);
				}catch(Exception e){
					e.printStackTrace();
					rt2 = false;
				}
			}
			if(rt1 && rt2){
				return true;
			}else{
				return false;
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	//新增加的进度条功能
	private void showProgressWindow() throws InterruptedException {
		try {
			final Window win = (Window) Executions.createComponents(
					"/main/TreeView/progress.zul", null, null);
			win.setAttribute("teleBackupWin", this);
			win.setMaximizable(false);
			win.setClosable(true);
			win.doModal();
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK,Messagebox.ERROR);
		}
	}
	
/*	public void onOk() throws Exception
	{
		if(before_savedata())
		{
			this.savedataFlag = false;
			refreshMonitorListbox();//运行 savedata()
			after_savedata();
			if(this.savedataFlag){
				this.refreshListbox();
				returnToParent();
			}else{
				return;
			}
			try{
				setBatchWin.detach();
			} catch (Exception e)
			{
			}
		}
	}*/
	
	
	/**
	 * 传递要批量修改的监测器属性的键值字典，将数据批量修改到多个监测器
	 * 
	 * @param ids
	 *            多个监测器的id,以逗号隔开
	 * @param data要修改的监测器属性的键值字典
	 * @return
	 */
	private boolean SetValueInManyMonitor_OnlyProperty(String ids, Map<String, Map<String, String>> data)
	{
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "SetValueInManyMonitor");//SetValueInManyMonitor//SetValueInManyMonitor_OnlyProperty
		ndata.put("id", ids);
		Map<String, Map<String, String>> fdata = data;
		try
		{
			RetMapInMap rmap = ManageSvapi.SubmitUnivData(fdata, ndata);
			if (!rmap.getRetbool())
			{
				return false;
			}
		} catch (Exception e)
		{
			return false;
		}
		Manager.instantUpdate();
		return true;
	}
	/**
	 * 构建基础数据
	 * @return
	 */
	private  Map<String, Map<String, String>>  buildBaseData_true()
	{
		Map<String, Map<String, String>> data=new HashMap<String, Map<String,String>>();
		Map<String, String> mointorparm=new HashMap<String, String>();
		mointorparm.put("sv_telebackup", "true");
		data.put("monitor_property", mointorparm);
		return data;
	}
	/**
	 * 构建基础数据
	 * @return
	 */
	private  Map<String, Map<String, String>>  buildBaseData_false()
	{
		Map<String, Map<String, String>> data=new HashMap<String, Map<String,String>>();
		Map<String, String> mointorparm=new HashMap<String, String>();
		mointorparm.put("sv_telebackup", "false");
		data.put("monitor_property", mointorparm);
		return data;
	}
	
	public String changeListToString(List<String> list){
		StringBuffer sb = new StringBuffer();

		for (String obj : list){
			if(obj == null || "".equals(obj)) continue;
			if (sb.length()>0) sb.append(",");
			sb.append(obj);
		}
		if (sb.length() == 0){
			return "";
		}
		return sb.toString();
	}

}
