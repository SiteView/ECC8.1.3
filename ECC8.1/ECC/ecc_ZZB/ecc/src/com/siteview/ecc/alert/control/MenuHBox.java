package com.siteview.ecc.alert.control;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

public class MenuHBox extends Hbox {
	private static final long serialVersionUID = -6314668704358935408L;

	private String uri = null;
	private String name = null;
	private String image = null;
	public String getImage() {
		return image == null || "".equals(image) ? "/main/images/application_view_detail.gif" : image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public void setName(String name){
		this.name = name;
	}
	public void setUri(String uri){
		this.uri = uri;
	}
	public String getUri() {
		return uri;
	}
	
	public String getName() {
		return name;
	}
	public void onCreate(){
		this.addEventListener(Events.ON_CLICK, getClickEventListener());

		Image image = new Image();
		image.setSrc(this.getImage());
		image.setTooltiptext(this.getName());
		image.addEventListener(Events.ON_CLICK, getClickEventListener());
		image.setParent(this);

		Label label = new Label();
		label.setValue(this.getName());
		label.addEventListener(Events.ON_CLICK, getClickEventListener());
		label.setParent(this);
		label.setLeft("3px");

		Label labelTemp = new Label();
		labelTemp.addEventListener(Events.ON_CLICK, getClickEventListener());
		labelTemp.setParent(this);
		labelTemp.setWidth("100%");

	}
	
	private EventListener getClickEventListener(){
		return new EventListener(){

			@Override
			public void onEvent(Event arg0) throws Exception {
				if (getUri() == null) return ;
				Include eccBody=(Include)getDesktop().getPage("eccmain").getFellow("eccBody");
				eccBody.setSrc(getUri());
			}
			
		};
	}
	
}
