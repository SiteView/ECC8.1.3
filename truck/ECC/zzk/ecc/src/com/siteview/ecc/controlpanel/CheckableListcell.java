package com.siteview.ecc.controlpanel;

import java.util.ArrayList;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;

public class CheckableListcell extends Listcell {

	private Checkbox checkbox=new Checkbox();
	private ArrayList<EventListener> checkListener=new ArrayList<EventListener>();
	private boolean checked;
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public CheckableListcell() {
		super();
		init();
	}

	public CheckableListcell(String label, String src) {
		super(label, src);
		init();
	}

	public CheckableListcell(String label) {
		super(label);
		init();
	}
	private void init()
	{
		insertBefore(checkbox, getFirstChild());
		final CheckableListcell checkableListcell=this;
		checkbox.addEventListener("onCheck", new EventListener(){
			@Override
			public void onEvent(Event event) throws Exception {
				checked=checkbox.isChecked();
				for(EventListener l:checkListener)
				{
					CheckEvent e=new CheckEvent("onCheck", checkableListcell,checked);
					l.onEvent(e);
				}
			}});
	}
	public void addCheckListener(EventListener l)
	{
		if(checkListener.indexOf(l)==-1)
			checkListener.add(l);
	}
}
