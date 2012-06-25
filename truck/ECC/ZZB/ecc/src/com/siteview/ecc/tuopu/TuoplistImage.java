package com.siteview.ecc.tuopu;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vbox;

public class TuoplistImage extends Vbox {

	private static final long serialVersionUID = 4786641067261794647L;
	final private String	outsetBtnStyle	= "cursor:pointer;border-color:#c0c0c0;border-width:1px;border-style: outset;background-color:#FFFFFF";
	final private String	insetBtnStyle	= "cursor:pointer;border-color:#c0c0c0;border-width:1px;border-style: inset;background-color:#E7FBFF";
	private boolean			clicked			= false;
	private static final Logger logger = Logger.getLogger(TuoplistImage.class);

	private String src;
	
	private String title = null;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	private EventListener editListener;
	
	private EventListener deleteListener;
	
	private EventListener openListener;
	
	private EventListener downloadListener;
	
	public TuoplistImage(){
	}
	public void onCreate(){
		this.setAlign("center");
		if(getTitle()!=null){
			new Label(this.getTitle()).setParent(this);
		}
		final Image image = new Image();
		image.setWidth("150px");
		image.setHeight("150px");
		image.setStyle("cursor:pointer;border:1px");
		image.setSrc(src);
		image.setBorder("1px");
		if(this.getOpenListener()!=null)
			image.addEventListener(Events.ON_CLICK, getOpenListener());
		image.setParent(this);
		
		
		Hbox hbox = new Hbox();
		Toolbarbutton editbtn = new Toolbarbutton();
		editbtn.setLabel("±à¼­ ");
		if(this.getEditListener()!=null){
			editbtn.addEventListener(Events.ON_CLICK, getEditListener());
		}
		editbtn.setParent(hbox);
		
		Toolbarbutton deletebtn = new Toolbarbutton();
		deletebtn.setLabel("É¾³ý ");
		if(this.getDeleteListener()!=null){
			deletebtn.addEventListener(Events.ON_CLICK, getDeleteListener());
		}
		deletebtn.setParent(hbox);
		
		Toolbarbutton downloadbtn = new Toolbarbutton();
		downloadbtn.setLabel("ÏÂÔØÍØÆËÍ¼ ");
		if(this.getDownloadListener()!=null){
			downloadbtn.addEventListener(Events.ON_CLICK, getDownloadListener());
		}
		downloadbtn.setParent(hbox);
		hbox.setParent(this);
	}

	public void invalidate(){
		super.invalidate();
	}
	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public EventListener getEditListener() {
		return editListener;
	}

	public void setEditListener(EventListener editListener) {
		this.editListener = editListener;
	}

	public EventListener getDeleteListener() {
		return deleteListener;
	}

	public void setDeleteListener(EventListener deleteListener) {
		this.deleteListener = deleteListener;
	}

	public EventListener getOpenListener() {
//		setClicked();
		return openListener;
	}

	public void setOpenListener(EventListener openListener) {
		this.openListener = openListener;
	}

	public EventListener getDownloadListener() {
		return downloadListener;
	}

	public void setDownloadListener(EventListener downloadListener) {
		this.downloadListener = downloadListener;
	}
	
	public void setClicked()
	{
		clicked = clicked?false:true;
		
		if (clicked)
		{
			setStyle(insetBtnStyle);
			
		} else
		{
			setStyle(outsetBtnStyle);
			
		}
		
	}
}
