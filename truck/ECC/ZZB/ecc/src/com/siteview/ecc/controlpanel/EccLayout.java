package com.siteview.ecc.controlpanel;

import java.util.ArrayList;
import java.util.HashMap;

import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.West;

public class EccLayout extends Borderlayout 
{
	private int treeSize=-1;
	private int actionSize=-1;
	private int controlPanelSize=0;
	
	HashMap<String,EccLayoutListener> sizeListener=new  HashMap<String,EccLayoutListener>();
	public int getTreeSize() 
	{
		if(treeSize==-1)
		{
			West west=(West)getDesktop().getPage("eccmain").getFellow("westTree");
			treeSize=Integer.parseInt(west.getWidth().split("px")[0]);
		}
		return treeSize;
	}
	public void setTreeSize(int treeSize) {
		this.treeSize = treeSize;
	}
	public int getActionSize() {
		return actionSize;
	}
	public void setActionSize(int actionSize) {
		this.actionSize = actionSize;
	}
	public int getControlPanelSize() {
		return controlPanelSize;
	}
	public void setControlPanelSize(int controlPanelSize) {
		this.controlPanelSize = controlPanelSize;
	}
	public void addEccLayoutListener(EccLayoutListener l)
	{
		sizeListener.put(l.getClass().getName(), l);
	}
	public void postEccLayoutEvent()
	{
		for(EccLayoutListener l:sizeListener.values())
		{
			l.eccLayout(this);
		}
	}
	
}
