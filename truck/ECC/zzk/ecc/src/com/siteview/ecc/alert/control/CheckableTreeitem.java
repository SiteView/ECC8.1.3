package com.siteview.ecc.alert.control;

import java.util.ArrayList;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Space;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import com.siteview.ecc.treeview.controls.BaseTreeitem;

public class CheckableTreeitem extends BaseTreeitem {
	private static final long serialVersionUID = -7403404484534552924L;
	private Checkbox checkbox = new Checkbox();
	private Label label = new Label();
	private Image image = new Image();
	private ArrayList<EventListener> checkboxListener = new ArrayList<EventListener>();

	public String getImage() {
		return image.getSrc();
	}

	public void setImage(String src) {
		this.image.setSrc(src);
	}

	public String getLabel() {
		return label.getValue();
	}

	public void setLabel(String value) {
		this.label.setValue(value);
	}

	public CheckableTreeitem() {
		super.setLabel("");
		super.setWidth("0");
		super.setCheckable(true);
		init();
	}

	public boolean isChecked() {
		return checkbox.isChecked();
	}

	public void setChecked(boolean checked) {
		checkbox.setChecked(checked);
	}

	
	public void init() {
		Treecell cell=new Treecell();
		
		cell.appendChild(checkbox);
		cell.appendChild(image);
		Space space = new Space();
		space.setWidth("4px");
		cell.appendChild(space);
		cell.appendChild(label);
		label.setMultiline(false);
		Treerow row=getTreerow();
		row.insertBefore(cell, row.getFirstChild());
		
		final CheckableTreeitem instance = this;
		checkbox.addEventListener(Events.ON_CHECK, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				Event e = new Event(Events.ON_CHECK, instance, instance
						.getValue());
				for (EventListener l : checkboxListener) {
					l.onEvent(e);
				}
			}
		});
	}

	public boolean addEventListener(String evtnm, EventListener listener) {
		if (evtnm.equals(Events.ON_CHECK)) {
			if (!checkboxListener.contains(listener))
				checkboxListener.add(listener);
			return true;
		} else
			return super.addEventListener(evtnm, listener);
	}
}
