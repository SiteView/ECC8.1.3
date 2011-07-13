package com.siteview.ecc.alert;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Bandbox;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;
import com.siteview.ecc.alert.control.TooltipPopup;
import com.siteview.ecc.alert.dao.Constand;
import com.siteview.ecc.alert.dao.bean.AccessControl;
import com.siteview.ecc.alert.dao.bean.AlertLogItem;
import com.siteview.ecc.alert.dao.bean.AlertLogQueryCondition;
import com.siteview.ecc.alert.dao.bean.AlertRuleQueryCondition;
import com.siteview.ecc.alert.dao.bean.BaseAlert;
import com.siteview.ecc.alert.dao.bean.EmailAlert;
import com.siteview.ecc.alert.dao.bean.SMSAlert;
import com.siteview.ecc.alert.dao.type.AlertType;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.alert.util.DictionaryFactory;
import com.siteview.ecc.report.common.AddLinkFuntion;
import com.siteview.ecc.report.common.UrlPropertiesType;
import com.siteview.ecc.util.Message;

public class AlertRuleLogView extends AbstractWindow {
	private static final long serialVersionUID = 7836913249749751601L;
	private static String ALL_TEXT = "全部";

	public String getAllText() {
		return ALL_TEXT;
	}

	//
	public Window getAlertRuleTest(){
		return (Window)BaseTools.getComponentById(this, "alertRuleTest");
	}
	//alertNameLink alertRuleTest
	public Toolbarbutton getalertNameLink() {
		return (Toolbarbutton) BaseTools.getComponentById(this, "alertNameLink");
	}
	
	public Toolbarbutton getalertReceiverLink() {
		return (Toolbarbutton) BaseTools.getComponentById(this, "alertReceiverLink");
	}
	
	public Combobox getAlertTypeCombobox() {
		return (Combobox) BaseTools.getComponentById(this, "alertType_list");
	}

	public Bandbox getAlertReceiverBandbox() {
		return (Bandbox) BaseTools.getComponentById(this, "alertReceiver");
	}

	public Bandbox getAlertNameBandbox() {
		return (Bandbox) BaseTools.getComponentById(this, "alertName");
	}

	public Listbox getAlertReceiverListbox() {
		return (Listbox) BaseTools.getComponentById(this, "alertReceiver_list");
	}

	public Listbox getAlertNameListbox() {
		return (Listbox) BaseTools.getComponentById(this, "alertName_list");
	}

	public Listbox getAlertLogListbox() {
		return (Listbox) BaseTools.getComponentById(this, "alert_log_list");
	}

	public Datebox getStartDatebox() {
		return (Datebox) BaseTools.getComponentById(this, "alertBegin_Date");
	}

	public Timebox getStartTimebox() {
		return (Timebox) BaseTools.getComponentById(this, "alertBegin_Time");
	}

	public Datebox getEndDatebox() {
		return (Datebox) BaseTools.getComponentById(this, "alertEnd_Date");
	}

	public Timebox getEndTimebox() {
		return (Timebox) BaseTools.getComponentById(this, "alertEnd_Time");
	}

	public Button getQueryButton() {
		return (Button) BaseTools.getComponentById(this, "alertQuery_Button");
	}

	public void doQuery() throws Exception {
		try {
			AlertLogQueryCondition queryCondition = new AlertLogQueryCondition();
			if (this.getAlertTypeCombobox().getSelectedItem() != null) {
				Comboitem item = this.getAlertTypeCombobox().getSelectedItem();
				if (getAllText().equals(item.getLabel())) {

				} else {
					queryCondition.setLimitType(true);
					queryCondition.setAlertType(AlertType.getTypeByDisplayString(item.getLabel()));
				}
			}

			if (this.getEndDatebox().getValue() != null && this.getEndTimebox().getValue() != null) {
				queryCondition.setLimitTime(true);

				Calendar cal = Calendar.getInstance();
				Calendar caltemp = Calendar.getInstance();
				cal.setTime(this.getEndTimebox().getValue());
				caltemp.setTime(this.getEndDatebox().getValue());
				cal.set(Calendar.YEAR, caltemp.get(Calendar.YEAR));
				cal.set(Calendar.MONTH, caltemp.get(Calendar.MONTH));
				cal.set(Calendar.DAY_OF_MONTH, caltemp.get(Calendar.DAY_OF_MONTH));
				cal.set(Calendar.SECOND, 0);
				queryCondition.setEndTime(cal.getTime());
			}
			if (this.getStartDatebox().getValue() != null && this.getStartTimebox().getValue() != null) {
				queryCondition.setLimitTime(true);

				Calendar cal = Calendar.getInstance();
				Calendar caltemp = Calendar.getInstance();
				cal.setTime(this.getStartTimebox().getValue());
				caltemp.setTime(this.getStartDatebox().getValue());
				cal.set(Calendar.YEAR, caltemp.get(Calendar.YEAR));
				cal.set(Calendar.MONTH, caltemp.get(Calendar.MONTH));
				cal.set(Calendar.DAY_OF_MONTH, caltemp.get(Calendar.DAY_OF_MONTH));
				cal.set(Calendar.SECOND, 0);
				queryCondition.setStartTime(cal.getTime());
			}
			Bandbox alertNameBandbox = this.getAlertNameBandbox();
			if (alertNameBandbox.getValue() != null) {
				if (getAllText().equals(alertNameBandbox.getValue())) {

				} else {
					queryCondition.setLimitName(true);
					queryCondition.setAlertName(alertNameBandbox.getValue());
				}
			}
			Bandbox receiverBandbox = this.getAlertReceiverBandbox();
			if (receiverBandbox.getValue() != null) {
				if (getAllText().equals(receiverBandbox.getValue())) {

				} else {
					queryCondition.setLimitReceiver(true);
					queryCondition.setAlertReceiver(receiverBandbox.getValue());
				}
			}
			LargeListbox ll = new LargeListbox(this);
			ll.setTotalSize(288);
			ll.redraw(queryCondition, 0, Constand.recordecount);
			return;
			// ***************************************************
			/*
			 * ListBean result =
			 * DictionaryFactory.getIAlertLogDao().queryAlertLog(new
			 * AccessControl(), queryCondition);
			 * BaseTools.clear(this.getAlertLogListbox()); if
			 * (result.getList().size() == 0) throw new Exception("没有查询到数据");
			 * AlertLogItem alertLogItem = new AlertLogItem(); for
			 * (HashMap<String, String> map : result.getList()) {
			 * alertLogItem.init(map); Listitem listitem =
			 * BaseTools.setRow(this.getAlertLogListbox(), alertLogItem,
			 * DATE_TO_STRING.format(alertLogItem .getAlertTime()),
			 * alertLogItem.getAlertName(), alertLogItem.getEntityName(),
			 * alertLogItem.getMonitorName(),
			 * alertLogItem.getAlertType().getComponent(),
			 * alertLogItem.getAlertReceiver(), alertLogItem.getAlertStatus()
			 * .getComponent()); listitem.setTooltip(getLogPopup(alertLogItem));
			 * } if (!result.isSuccess()) throw new
			 * Exception(result.getMessage());
			 */
		} catch (Exception e) {
			e.printStackTrace();
			Message.showError(e.getMessage());
		}
	}

	/**
	 * 组件初始化事件
	 * 
	 * @throws Exception
	 */
	public void onCreate() throws Exception {
		try {
			getalertNameLink().addEventListener(Events.ON_CLICK, new AddLinkFuntion(UrlPropertiesType.AlertRule));
			getalertReceiverLink().addEventListener(Events.ON_CLICK, new AddLinkFuntion(UrlPropertiesType.AlertRule));
			/*
			 * ((Listheader) BaseTools.getComponentById(this,
			 * "alertlogtype_header"
			 * )).setSortAscending(BaseTools.getSortComparator(4, false));
			 * ((Listheader) BaseTools.getComponentById(this,
			 * "alertlogtype_header"
			 * )).setSortDescending(BaseTools.getSortComparator(4, true));
			 * ((Listheader) BaseTools.getComponentById(this,
			 * "alertlogstatus_header"))
			 * .setSortAscending(BaseTools.getSortComparator(6, false));
			 * ((Listheader) BaseTools.getComponentById(this,
			 * "alertlogstatus_header"))
			 * .setSortDescending(BaseTools.getSortComparator(6, true));
			 */

			for (AlertType alerttype : AlertType.getAll()) {
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(alerttype.toString());
				comboitem.setId(alerttype.getStringVaule());
				getAlertTypeCombobox().appendChild(comboitem);
			}

			Calendar calStart = Calendar.getInstance();
			calStart.setTime(new Date());
			calStart.add(Calendar.HOUR, -24);
			getStartDatebox().setValue(calStart.getTime());
			getStartTimebox().setValue(calStart.getTime());
			getEndDatebox().setValue(new Date());
			getEndTimebox().setValue(new Date());
			AlertRuleQueryCondition condition = new AlertRuleQueryCondition();
			BaseAlert[] result = DictionaryFactory.getIAlertDao().queryAlertRule(new AccessControl(), condition);
			Set<String> alertReciverList = new TreeSet<String>();
			Set<String> alertNameList = new TreeSet<String>();
			for (BaseAlert basealert : result) {
				String alertName= this.getAlertName(basealert);
				if(!"".equals(alertName) && alertName!=null) 
					alertNameList.add(alertName);
				String alertReciver = this.getAlertReceiver(basealert);
				if(!alertReciver.equals(""))
					alertReciverList.add(alertReciver);
				Set<String> watchSheet = getAlertReceiverFromWatchSheet(basealert);
				if(watchSheet!=null){
					alertReciverList.addAll(watchSheet);
				}
			}
			
			for(String value : alertReciverList){
				Listitem item = new Listitem();
				item.setLabel(value);
				item.setValue(value);
				getAlertReceiverListbox().appendChild(item);
			}
			for(String value : alertNameList){
				Listitem item = new Listitem();
				item.setLabel(value);
				item.setValue(value);
				getAlertNameListbox().appendChild(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Message.showError(e.getMessage());
		}
	}

	/**	private void getAlertReceiverFromWatchSheet(BaseAlert basealert) throws Exception{
		String watchSheetSection = "";
		if(basealert instanceof EmailAlert) watchSheetSection = ((EmailAlert)basealert).getWatchSheet();
		else if(basealert instanceof SMSAlert) watchSheetSection = ((SMSAlert)basealert).getWatchSheet();
		else return;
		if(watchSheetSection==null || watchSheetSection.equals("")) return;
		List<String> list = new ArrayList<String>();
		IniFile ini = DictionaryFactory.getWatchSheets();
		ArrayList<String> keys = ini.getKeyList(watchSheetSection);
		if(keys==null) return;
		for(String key : keys){
			if(key.startsWith("item")){
				String value = ini.getValue(watchSheetSection, key);
				int index = value.lastIndexOf(",");
				if(index<0) continue;
				value = value.substring(index+1);
				if(BaseTools.validateEmail(value)){
					Listitem item = new Listitem();
					item.setLabel(value);
					item.setId(value);
					getAlertReceiverListbox().appendChild(item);
				}
			}
		}
	}*/
	private void addAlertName(BaseAlert basealert) {
		try {
			Listitem item = new Listitem();
			item.setLabel(basealert.getName());
			item.setId(basealert.getName());
			getAlertNameListbox().appendChild(item);
		} catch (Exception e) {

		}
	}

/**	private void addAlertReceiver(BaseAlert basealert) throws Exception {
		try {
			String receiverAddress = DictionaryFactory.getIAlertDao().getAlertReceiver(basealert);
			if (receiverAddress == null || "".equals(receiverAddress))
				return;
			Listitem item = new Listitem();
			item.setLabel(receiverAddress);
			item.setId(receiverAddress);
			getAlertReceiverListbox().appendChild(item);
		} catch (Exception e) {

		}

	}*/
	private Set<String> getAlertReceiverFromWatchSheet(BaseAlert basealert) throws Exception{
		String watchSheetSection = "";
		if(basealert instanceof EmailAlert) watchSheetSection = ((EmailAlert)basealert).getWatchSheet();
		else if(basealert instanceof SMSAlert) watchSheetSection = ((SMSAlert)basealert).getWatchSheet();
		else return null;
		if(watchSheetSection==null || watchSheetSection.equals("")) return null;
		Set<String> list = new TreeSet<String>();
		IniFile ini = DictionaryFactory.getWatchSheets();
		List<String> keys = ini.getKeyList(watchSheetSection);
		if(keys==null) return null;
		for(String key : keys){
			if(key.startsWith("item")){
				String value = ini.getValue(watchSheetSection, key);
				int index = value.lastIndexOf(",");
				if(index<0) continue;
				value = value.substring(index+1);
				if(BaseTools.validateEmail(value)){
					list.add(value);
				}
			}
		}
		return list;
	}
	private String getAlertName(BaseAlert basealert) {
		return basealert.getName();
	}
	private String getAlertReceiver(BaseAlert basealert) throws Exception {
		try {
			String receiverAddress = DictionaryFactory.getIAlertDao().getAlertReceiver(basealert);
			if (receiverAddress == null || "".equals(receiverAddress))
				return "";
			return receiverAddress;
		} catch (Exception e) {
			return "";
		}
	}
	public TooltipPopup getLogPopup(AlertLogItem alertlogitem) {
		TooltipPopup tooltippopup = new TooltipPopup();
		tooltippopup.onCreate();
		tooltippopup.setStyle("border:none;color:#FFFFFF;background-color:#717171");
		this.getTooltiptext(tooltippopup, alertlogitem);
		tooltippopup.setParent(this);
		return tooltippopup;
	}

	private void getTooltiptext(TooltipPopup tooltippopup, AlertLogItem alertlogitem) {
		tooltippopup.setTitle(alertlogitem.getAlertName());
		tooltippopup.setImage(alertlogitem.getAlertType().getImage());
		synchronized(DATE_TO_STRING){
			tooltippopup.addDescription("报警时间", DATE_TO_STRING.format(alertlogitem.getAlertTime()));
		}
		tooltippopup.addDescription("设备名称", alertlogitem.getEntityName());
		tooltippopup.addDescription("监测器名称", alertlogitem.getMonitorName());
		tooltippopup.addDescription("报警接收人", alertlogitem.getAlertReceiver());
		tooltippopup.addDescription("报警状态", alertlogitem.getAlertStatus().toString());
	}

}
