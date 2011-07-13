package com.siteview.ecc.alert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.event.PagingEvent;

import com.siteview.ecc.alert.control.AbstractListbox;
import com.siteview.ecc.alert.dao.bean.AccessControl;
import com.siteview.ecc.alert.dao.bean.AlertLogItem;
import com.siteview.ecc.alert.dao.bean.AlertLogQueryCondition;
import com.siteview.ecc.alert.dao.bean.ListBean;
import com.siteview.ecc.alert.util.DictionaryFactory;
import com.siteview.ecc.util.Message;
import com.siteview.ecc.util.Toolkit;

public class AlertRuleLogListbox extends AbstractListbox {
	
	private static final long serialVersionUID = -4159234754438311977L;
	
	private static final Logger logger = Logger.getLogger(AlertRuleLogListbox.class);
	
	private static final int PAGE_SIZE=15;
	
	private AlertLogQueryCondition aCondition = null;
	
	private int beginIndex = 0;
	
	private int endIndex = PAGE_SIZE;
	
	private int totalNumber = 0;
	
	private ListBean result = null;

	public AlertRuleLogListbox(AlertLogQueryCondition condition) throws Exception {
		super();
		try {
			aCondition = condition;
			result = DictionaryFactory.getIAlertLogDao().queryAlertLog(new AccessControl(), this.aCondition, beginIndex, endIndex);
			this.totalNumber = result.getTotalNumber();
		} catch (Exception e) {
			logger.error(e.getMessage());
			Message.showError(e.getMessage());
		}
	}
	
	public int getTotalNumber() {
		return totalNumber;
	}

	@Override
	public List<String> getListheader() {
		String[] listheader = {"报警时间","报警名称","设备名称","监测器名称","报警类型","报警接收人","报警状态"};
		return new ArrayList<String>(Arrays.asList(listheader));
	}

	@Override
	public void renderer() {
		refreshListbox(result);
	}
	
	public void onCreate(){
		setFixedLayout(true);
		setMold("paging");
		setPageSize(PAGE_SIZE);
		super.onCreate();
		getPagingChild().setTotalSize(totalNumber);
	}
	boolean ascending = false;
	
	protected Listhead buildListheader(){
		Listhead lh = new Listhead();
		lh.setSizable(true);
		if(listhead.contains("报警时间")){
			Listheader lr1 = getListheader("报警时间", "17%", "auto");
			lr1.setParent(lh);
		}
		if(listhead.contains("报警名称")){
			Listheader lr2 = getListheader("报警名称", "18%", "auto");
			lr2.setParent(lh);
		}
		if(listhead.contains("设备名称")){
			Listheader lr3 = getListheader("设备名称", "15%", "auto");
			lr3.setParent(lh);
		}
		if(listhead.contains("监测器名称")){
			Listheader lr4 = getListheader("监测器名称", "20%", "auto");
			lr4.setParent(lh);
		}
		if(listhead.contains("报警类型")){
			Listheader lr5 = getListheader("报警类型", "10%", "auto");
			lr5.setId("alertlogtype_header");
			lr5.setParent(lh);
		}
		if(listhead.contains("报警接收人")){
			Listheader lr6 = getListheader("报警接收人", "10%", "auto");
			lr6.setParent(lh);
		}
		if(listhead.contains("报警状态")){
			Listheader lr7 = getListheader("报警状态", "10%", "auto");
			lr7.setId("alertlogstatus_header");
			lr7.setParent(lh);
		}

		return lh;	
	}
	private Listheader getListheader(String label, String width, String sort) {
		Listheader lr1 = new Listheader(label);
		lr1.setWidth(width);
		lr1.setSort(sort);
		lr1.addEventListener("onSort", new SortingEventListener());
		return lr1;
	}
	
	private void refreshListbox(ListBean result){
		int index = getActivePage()*getPageSize();
		AlertLogItem alertLogItem = new AlertLogItem();
		for (Map<String, String> map : result.getList()) {
			if(index>=this.getTotalNumber()) continue;
			Listitem listitem = new Listitem();
			if(listitem.getChildren()!=null) listitem.getChildren().clear();
			alertLogItem.init(map);
			setListitem(listitem,alertLogItem);
			listitem.setParent(this);
			index++;
		}
	} 
	private void setListitem(Listitem listitem ,AlertLogItem alertLogItem){
		if(alertLogItem == null){
			return;
		}
		listitem.setValue(alertLogItem);
		for(String head :this.listhead){
			if(head.equals("报警时间")){
				Listcell cell = new Listcell(Toolkit.getToolkit().formatDate(alertLogItem.getAlertTime()));
				cell.setTooltiptext(Toolkit.getToolkit().formatDate(alertLogItem.getAlertTime()));
				cell.setParent(listitem);
			}
			if(head.equals("报警名称")){
				Listcell cell = new Listcell(alertLogItem.getAlertName());
				cell.setTooltiptext(alertLogItem.getAlertName());
				cell.setParent(listitem);
			}
			if(head.equals("设备名称")){
				Listcell cell = new Listcell(alertLogItem.getEntityName());
				cell.setTooltiptext(alertLogItem.getEntityName());
				cell.setParent(listitem);
			}
			if(head.equals("监测器名称")){
				Listcell cell = new Listcell(alertLogItem.getMonitorName());
				cell.setTooltiptext(alertLogItem.getMonitorName());
				cell.setParent(listitem);
			}
			if(head.equals("报警类型")){
				Listcell cell = new Listcell();
				cell.appendChild(alertLogItem.getAlertType().getComponent());
				cell.setParent(listitem);
			}
			if(head.equals("报警接收人")){
				Listcell cell = new Listcell(alertLogItem.getAlertReceiver());
				cell.setTooltiptext(alertLogItem.getAlertReceiver());
				cell.setParent(listitem);
			}
			if(head.equals("报警状态")){
				Listcell cell = new Listcell();
				cell.appendChild(alertLogItem.getAlertStatus().getComponent());
				cell.setParent(listitem);
			}
		}
	}
	public void onPaging(PagingEvent event) throws Exception{
		try {
			int currentPage =this.getActivePage();
			beginIndex = currentPage*getPageSize();
			endIndex = currentPage*getPageSize()+getPageSize();
			result = DictionaryFactory.getIAlertLogDao().queryAlertLog(new AccessControl(), this.aCondition, beginIndex, endIndex);
			refreshListbox(result);
			getPagingChildApi().setTotalSize(totalNumber);
		} catch (Exception e) {
			logger.error(e.getMessage());
			Message.showError(e.getMessage());
		}
	}
	
	private class SortingEventListener implements EventListener {
		@Override
		public void onEvent(Event event){
			getPagingChild().setTotalSize(totalNumber);
		}
	}
	
	
}
