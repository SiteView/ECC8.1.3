package com.siteview.ecc.util;

import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Space;

import com.siteview.actions.UserRight;

public class TooltipPopupBase extends Popup {
	private Image img=new Image();
	private Label titleLbl=new Label();
	private Columns cols=new Columns();
	private Grid grid=null;
	private boolean displayTitle=true;
	
	public void setDisplayTitle(boolean displayTitle) {
		this.displayTitle = displayTitle;
	}
	public Columns getCols() {
		return cols;
	}
	private Rows rows=new Rows();
	
	public Rows getRows() {
		return rows;
	}
	public TooltipPopupBase() {
		super();
		super.setWidth("300px");
	}
	public Grid getGrid()
	{
		if(grid==null)
		{
			grid=new Grid();
			grid.setStyle("over-flow:hidden");
			grid.setFixedLayout(true);
			
			if(displayTitle)
			{
				cols.setParent(grid);
				Column col1=new Column();
				col1.setParent(cols);
				col1.setAlign("left");
	
				col1.setStyle("border:none;");
				img.setParent(col1);
				img.setStyle("padding-left:5px");
				img.setAlign("absmiddle");
				new Space().setParent(col1);
				titleLbl.setMultiline(false);
				titleLbl.setParent(col1);
				titleLbl.setStyle("font-size:12px;font-weight:bold;color:#083884");
			}
			rows.setParent(grid);
		}
		return grid;
	}
	public UserRight getUserRight() {
		return  Toolkit.getToolkit().getUserRight(getGrid().getDesktop());
	}

	public void onCreate()
	{
		getGrid().setParent(this);
	}
	public String getTitle() {
		return titleLbl.getValue();
	}
	public void setTitle(String title) {
		this.titleLbl.setValue(title);
		this.titleLbl.setTooltiptext(title);
	}
	public void setImage(String src)
	{
		img.setSrc(src);
	}
}
