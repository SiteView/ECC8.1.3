package com.siteview.ecc.controlpanel;

import java.util.ArrayList;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;

import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.GroupInfo;
import com.siteview.ecc.timer.NodeInfoBean;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.util.Toolkit;

public class GroupTableModel extends EccListModel {
	@Override
	public int getColCount() {
		return 8;
	}
	@Override
	public String getTitle(int idxCol) {
		switch(idxCol)
		{
			case 0:
				return "名称";
			case 1:
				return "设备数";
			case 2:
				return "监测器数";
			case 3:
				return "禁止数";
			case 4:
				return "错误";
			case 5:
				return "危险";
			case 6:
				return "描述信息";
			case 7:
				return "依靠";
		}
		return "";
	}
	@Override
	public int forceColWidth(int idxCol) {
		
		switch(idxCol)
		{
		case 0:
			return 120;
		case 1:
			return 60;
		case 2:
			return 70;
		case 3:
			return 60;
		case 4:
			return 50;
		case 5:
			return 50;
			
		}
		return -1;
	}
	@Override
	public boolean isNumber(int idxCol) {
		
		switch(idxCol)
		{
			case 1:
				return true;
			case 2:
				return true;
			case 3:
				return true;
			case 4:
				return true;
			case 5:
				return true;
				
		}
		return false;
	}


	@Override
	public ListDataBean getValue(Object rowValue) 
	{
		ListDataBean bean = new ListDataBean();
		GroupInfo item=(GroupInfo)Toolkit.getToolkit().getInfoObject(view, ((EccTreeItem)rowValue).getValue());
		if(item==null)
			return null;
		//使用虚拟视图时刷新，刷新节点监测器信息
		Toolkits tool = new Toolkits();
		NodeInfoBean nodeBean = tool.refreshNodeInfoInList((EccTreeItem)rowValue);

		bean.setLineNum(9);
		bean.setName(item.getName());
		bean.setEntitySum(nodeBean.getDevice() + "");
		bean.setMonitorSum(nodeBean.getAll() + "");
		bean.setMonitorDisableSum(nodeBean.getDisabled() + "");
		bean.setMonitorErrorSum(nodeBean.getError() + "");
		bean.setMonitorWarningSum(nodeBean.getWarning() + "");
		bean.setDescription(item.getSvDescription());
		bean.setDependsOn(item.getSvDependsOn());
		
//		switch(idxCol)
//		{
//			case 0:
//				return item.getName();
//			case 1:
//				return String.valueOf(item.get_sub_entity_sum(session));
//			case 2:
//				return String.valueOf(item.get_sub_monitor_sum(session));
//			case 3:
//				return String.valueOf(item.get_sub_monitor_disable_sum(session));
//			case 4:
//				return String.valueOf(item.get_sub_monitor_error_sum(session));
//			case 5:
//				return String.valueOf(item.get_sub_monitor_warning_sum(session));
//			case 6:
//				return item.getSvDescription();
//			case 7:
//				return item.getSvDependsOn();
//			case 8:
//				return item.getName();
//		}
		return bean;
	}
	private static final long serialVersionUID = 4213630517674623504L;
	public GroupTableModel(View view, EccTreeItem selectedNode) {
		super(view, selectedNode);
	}
	public GroupTableModel(View view, EccTreeItem selectedNode,boolean displayInherit) {
		super(view, selectedNode,displayInherit);
	}
	public GroupTableModel(View view, EccTreeItem selectedNode,boolean displayInherit,int filter) {
		super(view, selectedNode,displayInherit,filter);
	}

	public void refresh()
	{
		ArrayList<EccTreeItem> list=new ArrayList<EccTreeItem>(); 
		addInherit(parentNode,list);
		clear();
		addAll(list);
	}
	private void addInherit(EccTreeItem item,ArrayList<EccTreeItem> list)
	{
		if(item!=null)
		for(EccTreeItem child:item.getChildRen())
		{
			if(child.getType().equals(INode.GROUP))
				super.addByFilter(list,child);
			if(isInherit())
		  		addInherit(child,list);
		}
	}
}
