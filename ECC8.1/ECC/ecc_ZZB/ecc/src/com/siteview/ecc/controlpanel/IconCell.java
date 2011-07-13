package com.siteview.ecc.controlpanel;

import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Vbox;

public class IconCell extends Div 
{
	private Image dropDownImage;
	private Image statusImage;
	private Label nameLabel;
	private String name;
	private String imgSrc;
	private Object value;

	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public IconCell() {
		super();
		super.setStyle("width:85px;height:50px;float:left;padding:5px;margin:5px 5px 5px 5px");
		Vbox box=new Vbox();
		box.setAlign("center");
		box.setParent(this);

		Hbox hbox=new Hbox();
		hbox.setParent(box);
		
		getStatusImage().setParent(hbox);
		getDropDownImage().setParent(hbox);

		Separator space=new Separator();
		space.setHeight("5px");
		space.setParent(box);
		createLabelDiv().setParent(box);
		
	}
	public Image getStatusImage()
	{
		if(statusImage==null)
		{
			statusImage=new Image();
			statusImage.setAlign("center");
			statusImage.setClass("pointer");
			statusImage.setAlign("absmiddle");
			//statusImage.setAction("onmouseover:this.style.zoom=1.5;onmouseout:this.style.zoom=1");
		}
		return statusImage;
	}
	public Image getDropDownImage()
	{
		if(dropDownImage==null)
		{
			dropDownImage=new Image("/main/images/ic_menu.gif");
			dropDownImage.setHover("/main/images/ic_menu_hover.gif");
			dropDownImage.setAlign("absmiddle");
		}
		return dropDownImage;
	}
	public Label getNameLabel()
	{
		if(nameLabel==null)
		{
			nameLabel=new Label();
			nameLabel.setStyle("cursor:pointer;color:#18599C;text-decoration: underline");
		}
		
		return nameLabel;
	}
	private Div createLabelDiv()
	{
		Div div=new Div();
		div.setStyle("white-space:normal pre nowrap;height:18px;width:120px;overflow:hidden");
		getNameLabel().setParent(div);
		return div;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		getNameLabel().setValue(name);
		
	}
	
	public String getImgSrc() {
		return imgSrc;
	}
	public void setImgSrc(String imgSrc) {
		getStatusImage().setSrc(imgSrc);
		this.imgSrc = imgSrc;
	}
	
}
