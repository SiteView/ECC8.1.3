package com.siteview.actions;

import java.util.ArrayList;
import java.util.HashMap;

import org.zkoss.zul.Image;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;
import org.zkoss.zul.Separator;

import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccWebAppInit;

public class GrantActionPanel extends Panel 
{
	private ArrayList<CheckableListitem> list=new ArrayList<CheckableListitem>();
	private String actionTarget;
	public String getActionTarget() {
		return actionTarget;
	}
	public GrantActionPanel(String actionTarget,String title)
	{
		super();
		this.actionTarget=actionTarget;
		setTitle(title);
		setStyle("border:none;padding-right:16px;marging:0 20px 0 0");
		setFramable(true);
		setCollapsible(true);
		setBorder("none");
		setSclass("ecc-panel");
	}
	public void clear()
	{
		list.clear();
	}
	public void setCheckedAll(boolean checked)
	{
		for(CheckableListitem item:list)
			item.setChecked(checked);
	}
	public void addCheckbox(CheckableListitem item)
	{
		list.add(item);
	}
	/*
	 * actionTarget如SE,GROUP,ENTITY,MONITOR
	 * */
	
	public void refresh(UserRight userRight,EccTreeItem curEccTreeItem)
	{
		Listbox listbox = new Listbox();
		listbox.setWidth("100%");
		listbox.setStyle("border:none");
		listbox.setMultiple(true);
		listbox.setCheckmark(true);
		for(String action:EccActionManager.getInstance().getActionList(actionTarget))
		{
			if(action.equals("-"))
			{
				Separator se=new Separator();
				se.setBar(true);
				if(listbox.getLastChild()!=null)
					if(listbox.getLastChild().getLastChild()!=null)
						listbox.getLastChild().getLastChild().appendChild(se);
				continue;
			}
			CheckableListitem listitem=new CheckableListitem(EccActionManager.getInstance().getActionName(action),action);
			
			listitem.setImage(EccWebAppInit.getInstance().getActionImage(action));

			listitem.setParent(listbox);
			if(userRight.havePopupMenuRight(curEccTreeItem.getId(), action))
				listitem.setChecked(true);
			else
				listitem.setChecked(false);
			
			addCheckbox(listitem);
		}
		
		
		listbox.setFixedLayout(true);
		listbox.setVflex(false);/*出滚动条*/
		Panelchildren panelchildren = new Panelchildren();
		panelchildren.setParent(this);
		listbox.setParent(panelchildren);
	}
	public String getCheckedStr()
	{
		StringBuffer vb=new StringBuffer();
		for(CheckableListitem box:list)
		{
			if(vb.length()>0)
				vb.append(",");
			if(box.isChecked())
				vb.append(box.getValue()).append("=1");
			else
				vb.append(box.getValue()).append("=0");
		}
		return vb.toString();
	}
	
	//特殊逻辑 专门用来 处理 报警 和 报告 的授权
	public HashMap<String,String> getCheckedMap()
	{
		HashMap<String,String> checkedMap = new HashMap<String,String>();
		for(CheckableListitem box:list)
		{
			if(box.isChecked()){
				checkedMap.put(box.getValue().toString(), "1");
			}else{
				checkedMap.put(box.getValue().toString(), "0");
			}
		}
		return checkedMap;
	}
}
