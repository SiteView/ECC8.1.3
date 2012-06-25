package com.siteview.ecc.monitorbrower;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.siteview.ecc.report.common.ChartUtil;

public class CreateMonitorFilter extends GenericForwardComposer {
	
	private final String url = "/main/monitorbrower/filterMonitor.zul";
	private Listbox showMonitorFilter;
	
	public void onClick$filter(Event event) throws Exception{
		try {
			final Window win = (Window) Executions.createComponents(
					url, null, null);
			win.setMaximizable(false);
			win.setClosable(true);
			win.doModal();
		} catch (SuspendNotAllowedException e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"´íÎó", Messagebox.OK, Messagebox.ERROR);
		}
	}
	public void onCreate$monitorBrower(Event event) throws Exception{
		try{
			MonitorFilterModel model = new MonitorFilterModel();
			ChartUtil.makelistData(showMonitorFilter,model,model);
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"´íÎó", Messagebox.OK, Messagebox.ERROR);
		}
	}
}
