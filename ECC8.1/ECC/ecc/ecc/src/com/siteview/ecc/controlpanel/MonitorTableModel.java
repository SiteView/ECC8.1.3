package com.siteview.ecc.controlpanel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.base.treeInfo.MonitorInfo;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.util.Toolkit;

public class MonitorTableModel extends EccListModel {

	@Override
	public int getColCount() {
		return 3;
	}
	@Override
	public String getTitle(int idxCol) {
		switch(idxCol)
		{
			case 0:
				return "名称";
			case 1:
				return "描述";
			case 2:
				return "最后更新";
		}
		return "";
	}
	@Override
	public int forceColWidth(int idxCol) {
		
		switch(idxCol)
		{
		case 0:
			return 120;
		case 2:
			return 126;
		}
		return -1;
	}
	@Override
	public boolean isNumber(int idxCol) {
		return false;
	}

	@Override
	public ListDataBean getValue(Object rowValue) 
	{
		ListDataBean bean = new ListDataBean();
		MonitorInfo item=(MonitorInfo)Toolkit.getToolkit().getInfoObject(view, ((EccTreeItem)rowValue).getValue());
		if(item==null)
			return null;
		
		bean.setLineNum(3);
		bean.setName(item.getName());
		bean.setDescription(item.getDstr());
		bean.setCreateTime(item.getCreateTime());
		
//		switch(idxCol)
//		{
//			case 0:
//				return item.getName();
//			case 1:
//				return item.getDstr();
//			case 2:
//				return item.getCreateTime();
//		}
		return bean;
	}
	private static final long serialVersionUID = 1008373496713630637L;
	public static SimpleDateFormat dateFormat=new SimpleDateFormat("hh:mm:ss");
	//Date refreshDate=null;
	public MonitorTableModel(View view, EccTreeItem selectedNode) {
		super(view, selectedNode);
	}
	public MonitorTableModel(View view, EccTreeItem selectedNode,boolean displayInherit) {
		super(view, selectedNode,displayInherit);
	}
	public MonitorTableModel(View view, EccTreeItem selectedNode,boolean displayInherit,int filter) {
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
			if(child.getType().equals(INode.MONITOR))
				super.addByFilter(list,child);
			else if(isInherit())
		  		addInherit(child,list);
		}
	}

}
