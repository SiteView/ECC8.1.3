/**
 * 
 */
package com.siteview.ecc.setmonitor;

import java.util.UUID;

import org.zkoss.web.servlet.dsp.action.Page;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Window;

/**
 * @author yuandong浏览器设置主页面，该页面包含setbatch页面。theNorth是按钮和listbox，
 * 初始不现实。theSouth是setbatch页面，初始时显示，点击确定后不显示
 * 
 */
public class SetMonitor extends GenericAutowireComposer {
	public SetMonitor()
	{}
	private Listbox monitorlistbox;
	private Window setMonitorWin;
	private static String	batchMonitors_TargetUrl			= "/main/setmonitor/setbatch.zul";
	public void onSetBatchButton(Event event) {
		final Window win = (Window) Executions.createComponents(batchMonitors_TargetUrl, null, null);
		win.setAttribute("monitorlistbox", monitorlistbox);
		Label pall = (Label) setMonitorWin.getDesktop().getPage("setMonitorPage")
		.getFellow("all");
		win.setAttribute("all", pall);
		Label pok = (Label) setMonitorWin.getDesktop().getPage("setMonitorPage")
		.getFellow("ok");
		win.setAttribute("ok", pok);
		Label pwarn = (Label) setMonitorWin.getDesktop().getPage("setMonitorPage")
		.getFellow("warn");
		win.setAttribute("warn", pwarn);
		Label perror = (Label) setMonitorWin.getDesktop().getPage("setMonitorPage")
		.getFellow("error");
		win.setAttribute("error", perror);
		Label pforbid = (Label) setMonitorWin.getDesktop().getPage("setMonitorPage")
		.getFellow("forbid");
		win.setAttribute("forbid", pforbid);
		Label pbad = (Label) setMonitorWin.getDesktop().getPage("setMonitorPage")
		.getFellow("bad");
		win.setAttribute("bad", pbad);
		
		Label pnullspecial = (Label) setMonitorWin.getDesktop().getPage("setMonitorPage")
		.getFellow("nullspecial");
		win.setAttribute("null", pnullspecial);
		
		try
		{
			win.doModal();
		} catch (SuspendNotAllowedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

}
