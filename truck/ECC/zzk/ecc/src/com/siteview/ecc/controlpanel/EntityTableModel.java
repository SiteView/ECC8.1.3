package com.siteview.ecc.controlpanel;

import java.util.ArrayList;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;

import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.EntityInfo;
import com.siteview.ecc.timer.NodeInfoBean;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.util.Toolkit;

public class EntityTableModel extends EccListModel
{
	@Override
	public int getColCount() {
		return 7;
	}
	@Override
	public String getTitle(int idxCol) {
		switch(idxCol)
		{
			case 0:
				return "名称";
			case 1:
				return "监测器";
			case 2:
				return "禁止";
			case 3:
				return "错误";
			case 4:
				return "危险";
			case 5:
				return "ip地址";
			case 6:
				return "设备类型";
		}
		return "";
	}
	@Override
	public int forceColWidth(int idxCol) {
		
		switch(idxCol)
		{
		case 1:
			return 60;
		case 2:
			return 50;
		case 3:
			return 50;
		case 4:
			return 50;
		case 5:
			return 100;
		case 6:
			return 100;			
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
		}
		return false;
	}

	@Override
	public ListDataBean getValue(Object rowValue) 
	{
		ListDataBean bean = new ListDataBean();
		EntityInfo item=(EntityInfo)Toolkit.getToolkit().getInfoObject(view, ((EccTreeItem)rowValue).getValue());
		if(item==null)
			return null;
		//使用虚拟视图时刷新，刷新节点监测器信息
		Toolkits tool = new Toolkits();
		NodeInfoBean nodeBean = tool.refreshNodeInfoInList((EccTreeItem)rowValue);
		
		bean.setLineNum(7);
		bean.setName(item.getName());
		bean.setMonitorSum(nodeBean.getAll() + "");
		bean.setMonitorWarningSum(nodeBean.getWarning() + "");
		bean.setMonitorErrorSum(nodeBean.getError() + "");
		bean.setMonitorDisableSum(nodeBean.getDisabled() + "");
		bean.setIpAdress(item.getIpAdress());
		bean.setDeviceTemplate(item.getDeviceTemplate()!=null?item.getDeviceTemplate().get_sv_name():"");
		
//		switch(idxCol)
//		{
//			case 0:
//				return item.getName();
//			case 1:
//				return String.valueOf(item.get_sub_monitor_sum(session));
//			case 2:
//				return String.valueOf(item.get_sub_monitor_disable_sum(session));
//			case 3:
//				return String.valueOf(item.get_sub_monitor_error_sum(session));
//			case 4:
//				return String.valueOf(item.get_sub_monitor_warning_sum(session));
//			case 5:
//				return item.getIpAdress();
//			case 6:
//				if(item.getDeviceTemplate()!=null)
//					return item.getDeviceTemplate().get_sv_name();
//		}
		return bean;
	}
	private static final long serialVersionUID = 6378144899568629008L;
	public EntityTableModel(View view, EccTreeItem selectedNode) {
		super(view, selectedNode);
	}
	public EntityTableModel(View view, EccTreeItem selectedNode,boolean displayInherit) {
		super(view, selectedNode,displayInherit);
	}
	public EntityTableModel(View view, EccTreeItem selectedNode,boolean displayInherit,int filter) {
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
			if(child.getType().equals(INode.ENTITY))
				super.addByFilter(list,child);
			else if(isInherit())
		  		addInherit(child,list);
		}
	}


}
