package com.siteview.ecc.report;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Window;

import com.siteview.ecc.report.beans.MonitorBean;
import com.siteview.ecc.report.common.ChartUtil;

public class MonitorImfoComposer extends GenericForwardComposer{
	private static final long serialVersionUID 			= -3553797168790817179L;
	private Panel 										moreInfo;
	private Label 										groupName;
	private Label										entityName;
	private Label 										type;
	private Label 										frequency;
	private Label 										okCondition;
	private Label 										warnCondition;
	private Label 										errorCondition;
	private Listbox 									listbox;
	
	public void onCreate$monitorInfo(Event event) throws InterruptedException{
		try{
			Listitem item = ChartUtil.getFirstListitem(listbox);
			item.setSelected(true);
			onSelect$listbox(event);
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误",Messagebox.OK,Messagebox.ERROR);
		}
	}
	public void onPaging$listbox(Event event) throws InterruptedException{
		try{
			int pageSize = listbox.getPageSize();
			int currentPage = listbox.getActivePage();
			int index = pageSize*currentPage;
			Listitem item = listbox.getItemAtIndex(index);
			item.setSelected(true);
			onSelect$listbox(event);
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误",Messagebox.OK,Messagebox.ERROR);
		}
	}

	public void onClick$createExcel(Event event) throws InterruptedException{
		try {
			List<MonitorBean> beans = new ArrayList<MonitorBean>();
			String msg = "是否导出全部检测器信息？点击'是'保存全部信息;点击'否'保存当前页信息";
			int option = Messagebox.show(msg,"提示",Messagebox.YES|Messagebox.NO,Messagebox.INFORMATION);
			if(option == Messagebox.YES){
				int i = 0;
				while(i<listbox.getItemCount()){
					Listitem item = listbox.getItemAtIndex(i);
					if(item==null) continue;
					i++;
					MonitorBean bean = (MonitorBean)item.getValue();
					bean.setColor((i%2 == 0));
					beans.add(bean);
				}
			}else{
				int pageSize = listbox.getPageSize();
				int currentPage = listbox.getActivePage();
				int start = pageSize*currentPage;
				int i = start;
				while(i<(start+pageSize)&& i<listbox.getItemCount()){
					Listitem item = listbox.getItemAtIndex(i);
					if(item==null) continue;
					i++;
					MonitorBean bean = (MonitorBean)item.getValue();
					bean.setColor((i%2 == 0));
					beans.add(bean);
				}
			}
			final Window win = (Window) Executions.createComponents(
					"/main/report/monitorreport/exportmonitorreport.zul", null, null);
			win.setTitle("导出报表");
			win.setAttribute("dataSource", beans);
			win.setMaximizable(false);
			win.setClosable(true);
			win.doModal();
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误",Messagebox.OK,Messagebox.ERROR);
		} 
	}
	
	public void onClick$filterMonitor(Event event) throws Exception{
		try{
			final Window win = (Window) Executions.createComponents(
					"/main/report/MonitorFilter.zul", null, null);
			win.setTitle("监测器筛选");
			win.setAttribute("listbox", listbox);
			win.setMaximizable(false);
			win.setClosable(true);
			win.doModal();
			Listitem item = ChartUtil.getFirstListitem(listbox);
			item.setSelected(true);
			onSelect$listbox(event);
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误",Messagebox.OK,Messagebox.ERROR);
		}
	}
	
	public void onClick$reflesh(Event event) throws Exception{
		try{
			MonitorImfoListbox l = (MonitorImfoListbox)listbox;
			l.setCondition(null);
			l.onCreate();
			onCreate$monitorInfo(event);
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误",Messagebox.OK,Messagebox.ERROR);
		}
	}
	
	public void onSelect$listbox(Event event) throws InterruptedException{
		try{
			Listitem item = listbox.getSelectedItem();
			setDetailMonitorInfo(item);
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误",Messagebox.OK,Messagebox.ERROR);
		}
	}

	private void setDetailMonitorInfo(Listitem item){
		if(item==null) {
			clear();
			return;
		}
		MonitorBean mbean = (MonitorBean) item.getValue();
		moreInfo.setTitle("监测器详细信息: " + mbean.getMonitorName());
		groupName.setValue(mbean.getGroupName());
		entityName.setValue(mbean.getEntityName());
		type.setValue(mbean.getMonitorType());
		frequency.setValue(mbean.getFrequency());
		String[] keyValue = split(mbean.getKeyValue());
		okCondition.setValue(keyValue[0]);
		warnCondition.setValue(keyValue[1]);
		errorCondition.setValue(keyValue[2]);
	}
	
	private void clear(){
		moreInfo.setTitle("监测器详细信息");
		groupName.setValue("");
		entityName.setValue("");
		type.setValue("");
		frequency.setValue("");
		okCondition.setValue("");
		warnCondition.setValue("");
		errorCondition.setValue("");
	}
	
	private static String[] split(String str) {
		return str.replace("]", "]\n").split("\n");
	}
}
