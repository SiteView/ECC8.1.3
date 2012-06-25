package com.siteview.actions;

import java.util.ArrayList;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Space;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.siteview.base.tree.INode;
import com.siteview.ecc.treeview.EccWebAppInit;
import com.siteview.ecc.treeview.controls.BaseTreeitem;

public class CheckAbleTreeitem extends BaseTreeitem {
	
	Checkbox checkbox=new Checkbox();
	private Label label=new Label();
	private Image image=new Image();
	private ArrayList<EventListener> checkboxListener=new ArrayList<EventListener>();
	
	public String getSrc() {
		return image.getSrc();
	}
	public void setSrc(String src) {
		this.image.setSrc(src);
	}
	public String getLabel() {
		return label.getValue();
	}
	public void setLabel(String value) {
		this.label.setValue(value);
	}
	public CheckAbleTreeitem()
	{
		super.setLabel("");
		super.setWidth("0");
		super.setCheckable(true);
		init();
	}
	public boolean isChecked()
	{
		return checkbox.isChecked();
	}
	public void setChecked(boolean checked)
	{
		checkbox.setChecked(checked);
	}
	public void init()
	{
		final CheckAbleTreeitem instance=this;
		Treerow row=getTreerow();
		Treecell cell=new Treecell();
		cell.appendChild(checkbox);
		row.insertBefore(cell, row.getFirstChild());
		cell.appendChild(image);
		image.setAlign("center");
		cell.appendChild(new Space());
		cell.appendChild(label);
		row.insertBefore(cell, row.getFirstChild());
		checkbox.addEventListener("onCheck", new EventListener(){
			@Override
			public void onEvent(Event event) throws Exception 
			{
				Event e=new Event("onCheck", instance,instance.getValue());
				for(EventListener l:checkboxListener)
				{
					l.onEvent(e);
				}
			}});
	}
	public boolean addEventListener(String evtnm,EventListener listener)
	{
		if(evtnm.equals("onCheck"))
		{
			if(!checkboxListener.contains(listener))
				checkboxListener.add(listener);
			return true;
		}
		else
			return super.addEventListener(evtnm, listener);
	}
}
