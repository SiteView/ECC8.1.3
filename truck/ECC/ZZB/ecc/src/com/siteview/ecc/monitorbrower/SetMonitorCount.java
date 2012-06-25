package com.siteview.ecc.monitorbrower;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Window;

import com.siteview.ecc.report.common.ChartUtil;

public class SetMonitorCount extends GenericForwardComposer {

	private Spinner cc;
	
	private Window setcount;
	
	public void onClick$okBtn(Event event) throws WrongValueException, Exception{
		MonitorBrowseComposer obj = (MonitorBrowseComposer)setcount.getAttribute("count");
		obj.setShowMonitorCount(cc.getValue());
		setcount.detach();
	}
}
