package com.siteview.ecc.alert.control;

import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Rows;

public class TooltipPopupBase extends Popup {
	private static final long serialVersionUID = -1826798358594630032L;
	private static final String titleStyle="border:none;";
	private Image img=new Image();
	private Label titleLbl=new Label();
	private Columns cols=new Columns();
	public Columns getCols() {
		return cols;
	}
	private Rows rows=new Rows();
	
	public Rows getRows() {
		return rows;
	}
	public TooltipPopupBase() {
		super();
		super.setWidth("450px");
	}
	public void onCreate()
	{
		Grid grid=new Grid();
		grid.setStyle("over-flow:hidden");
		grid.setParent(this);
		grid.setFixedLayout(true);
		
		
		cols.setParent(grid);
		rows.setParent(grid);
		
		
		
		Column col1=new Column();
//		Column col2=new Column();
		col1.setParent(cols);
		col1.setAlign("left");
//		col1.setWidth("120px");
//		col2.setParent(cols);
//		col2.setAlign("left");

		col1.setStyle(titleStyle);
//		col2.setStyle(titleStyle);
		//col1.setZclass("ecc-popup-header");
		//col2.setZclass("ecc-popup-header");
		img.setParent(col1);
		img.setStyle("padding-left:5px");
		titleLbl.setMultiline(false);
		titleLbl.setParent(col1);
		titleLbl.setStyle("font-size:12px;font-weight:bold;color:#083884");


	}
	public String getTitle() {
		return titleLbl.getValue();
	}
	public void setTitle(String title) {
		this.titleLbl.setValue(title);
	}
	public void setImage(String src)
	{
		img.setSrc(src);
	}
}
