/**
 * 
 */
package com.siteview.ecc.log;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Timebox;

import com.siteview.base.data.IniFile;
import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.ecc.util.Toolkit;

/**
 * @author yuandong
 * @param <cb1_1>
 * 
 */
public class SysLogSet extends GenericAutowireComposer {
	private final static Logger logger = Logger.getLogger(SysLogSet.class);
	private Checkbox cb1_1;
	private Checkbox cb1_2;
	private Checkbox cb1_3;
	private Checkbox cb1_4;
	private Checkbox cb1_5;
	private Checkbox cb1_6;
	private Checkbox cb1_7;
	private Checkbox cb1_8;
	private Checkbox cb1_9;
	private Checkbox cb1_10;
	private Checkbox cb1_11;
	private Checkbox cb1_12;
	private Checkbox cb1_13;
	private Checkbox cb1_14;
	private Checkbox cb1_15;
	private Checkbox cb1_16;
	private Checkbox cb1_17;
	private Checkbox cb1_18;
	private Checkbox cb1_19;
	private Checkbox cb1_20;
	private Checkbox cb1_21;
	private Checkbox cb1_22;
	private Checkbox cb1_23;
	private Checkbox cb1_0;
	private Checkbox cb2_1;
	private Checkbox cb2_2;
	private Checkbox cb2_3;
	private Checkbox cb2_4;
	private Checkbox cb2_5;
	private Checkbox cb2_6;
	private Checkbox cb2_7;
	private Checkbox cb2_0;
	private HashMap<String, Checkbox> cmap = new HashMap<String, Checkbox>();
	private HashMap<String, Checkbox> dmap = new HashMap<String, Checkbox>();
	private Button applyButton;
	private Button applyButton2;
	private IniFile ini = new IniFile("syslog.ini");
	private Datebox delDatebox;
	private Timebox delTimebox;
//	private Button applyDelButton;
	private Button applyDateButton;
	private Intbox dateTextbox;
	private Calendar calDel = null;

	public void onInit() throws Exception {
		cmap.put("0", cb1_0);
		cmap.put("1", cb1_1);
		cmap.put("2", cb1_2);
		cmap.put("3", cb1_3);
		cmap.put("4", cb1_4);
		cmap.put("5", cb1_5);
		cmap.put("6", cb1_6);
		cmap.put("7", cb1_7);
		cmap.put("8", cb1_8);
		cmap.put("9", cb1_9);
		cmap.put("10", cb1_10);
		cmap.put("11", cb1_11);
		cmap.put("12", cb1_12);
		cmap.put("13", cb1_13);
		cmap.put("14", cb1_14);
		cmap.put("15", cb1_15);
		cmap.put("16", cb1_16);
		cmap.put("17", cb1_17);
		cmap.put("18", cb1_18);
		cmap.put("19", cb1_19);
		cmap.put("20", cb1_20);
		cmap.put("21", cb1_21);
		cmap.put("22", cb1_22);
		cmap.put("23", cb1_23);
		dmap.put("0", cb2_0);
		dmap.put("1", cb2_1);
		dmap.put("2", cb2_2);
		dmap.put("3", cb2_3);
		dmap.put("4", cb2_4);
		dmap.put("5", cb2_5);
		dmap.put("6", cb2_6);
		dmap.put("7", cb2_7);
		try {
			onRefresh(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void onRefresh(Event event) throws Exception {

		try {

			ini.load();
			String value1 = ini.getValue("QueryCond", "Facility");
			String value2 = ini.getValue("QueryCond", "Severities");
			String[] v1 = splString(value1, ",");
			String[] v2 = splString(value2, ",");
			for (int i = 0; i < 24; i++) {
				cmap.get(Integer.toString(i)).setChecked(false);
			}

			for (int i = 0; i < v1.length; i++) {
				cmap.get(v1[i]).setChecked(true);
			}

			for (int i = 0; i < 8; i++) {
				dmap.get(Integer.toString(i)).setChecked(false);
			}
			for (int i = 0; i < v2.length; i++) {
				dmap.get(v2[i]).setChecked(true);
			}
			calDel = Calendar.getInstance();
			calDel.setTime(new Date());
			delDatebox.setValue(calDel.getTime());	
			delTimebox.setValue(calDel.getTime());
			if (ini.getValue("DelCond", "KeepDay")!=null) dateTextbox.setValue(Integer.parseInt(ini.getValue("DelCond", "KeepDay")));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String[] splString(String s, String regex) {

		return s.split(regex);
	}

	public void onApply(Event event) {
		String value = "";

		for (int i = 0; i < 24; i++) {
			if (cmap.get(Integer.toString(i)).isChecked()){
				value = value + Integer.toString(i) + ",";
			}
		}
		try {
			ini.load();
			ini.setKeyValue("QueryCond", "Facility", value);
			ini.saveChange();
			
			View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
			String loginname = view.getLoginName();
			String minfo = loginname + "在" + OpObjectId.syslog_set.name + "中进行了  "+OpTypeId.edit.name+"  设备参数设置  操作";
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.syslog_set);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		applyButton.setDisabled(true);
	}

	public void onApply2(Event event) {
		String value = "";

		for (int i = 0; i < 8; i++) {
			if (dmap.get(Integer.toString(i)).isChecked()){
				value = value + Integer.toString(i) + ",";
			}
		}
		try {
			ini.load();
			logger.info(ini.getKeyList("QueryCond"));
			logger.info(ini.getSectionList());
			ini.setKeyValue("QueryCond", "Severities", value);
			ini.saveChange();
			
			View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
			String loginname = view.getLoginName();
			String minfo = loginname + "在" + OpObjectId.syslog_set.name + "中进行了  "+OpTypeId.edit.name+"  级别参数设置  操作";
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.syslog_set);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		applyButton2.setDisabled(true);

	}

	// SVAPI_API
	// bool DeleteRecords(string monitorid,svutil::TTime before,string
	// user="default",string addr="localhost");
	/*
	 * Helper.XfireCreateKeyValue("dowhat","DeleteRecords"),
	 * Helper.XfireCreateKeyValue("id",strTableId),
	 * Helper.XfireCreateKeyValue("year",tmFrom.Year.ToString()),
	 * Helper.XfireCreateKeyValue("month",tmFrom.Month.ToString()),
	 * Helper.XfireCreateKeyValue("day",tmFrom.Day.ToString()),
	 * Helper.XfireCreateKeyValue("hour",tmFrom.Hour.ToString()),
	 * Helper.XfireCreateKeyValue("minute",tmFrom.Minute.ToString()),
	 * Helper.XfireCreateKeyValue("second",tmFrom.Second.ToString())
	 //dowhat= DeleteRecords  ,    id= XXX ,  year= XXX,  month= XXX, day= XXX,  hour= XXX,  minute= XXX,  second= XXX,
	// 删除某个表中的指定时间以前的记录，因为删除是按 4KB 一页的页式删除, 因此没法精确删除所有指定时间之前的记录,可能会少删*/
	public void onDelLog(Event event) throws InterruptedException, Exception{
	
		calDel.setTime(delDatebox.getValue());
		Calendar t = Calendar.getInstance();
		t.setTime(delTimebox.getValue());
		
		calDel.set(Calendar.HOUR_OF_DAY, t.get(Calendar.HOUR_OF_DAY));
		calDel.set(Calendar.MINUTE, t.get(Calendar.MINUTE));
		if(Messagebox.show("确定要删除"+calDel.get(Calendar.YEAR)+"年"+(calDel.get(Calendar.MONTH)+1)+"月"+calDel.get(Calendar.DAY_OF_MONTH)+"日"+calDel.get(Calendar.HOUR_OF_DAY)+"点"+calDel.get(Calendar.MINUTE)+"分"+"之前的记录吗？", "询问", Messagebox.OK | Messagebox.CANCEL,
				Messagebox.QUESTION)==Messagebox.OK){
			HashMap<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "DeleteRecords");
		ndata.put("id", "syslog");
		ndata.put("year", String.valueOf(calDel.get(Calendar.YEAR)));
		ndata.put("month", String.valueOf(calDel.get(Calendar.MONTH)+1));
		ndata.put("day", String.valueOf(calDel.get(Calendar.DAY_OF_MONTH)));
		ndata.put("hour", String.valueOf(calDel.get(Calendar.HOUR_OF_DAY)));
		ndata.put("minute", String.valueOf(calDel.get(Calendar.MINUTE)));
		ndata.put("second", String.valueOf(calDel.get(Calendar.SECOND)));
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		
		View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
		String loginname = view.getLoginName();
		String minfo = loginname + "在" + OpObjectId.syslog_set.name + "中进行了  "+OpTypeId.del.name+"  系统日志   操作";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del, OpObjectId.syslog_set);

		Messagebox.show(String.valueOf(ret.getFmap().size()-1)+"条记录被删除。", "提示", Messagebox.OK, Messagebox.INFORMATION);
		
		}
		
		
	}

	
	public void onApplyDate(Event event) throws Exception {
		try{
			if((dateTextbox.getValue())==null){
				Messagebox.show("输入天数不能为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}
			if(dateTextbox.getValue().intValue()<1){
				Messagebox.show("输入天数必须为大于0的整数！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}
			String text=dateTextbox.getValue().toString();
			try{
				ini.load();
			}catch(Exception e){}
			
			Pattern regex = Pattern.compile("^[0-9]*$");
			Matcher matcher = regex.matcher(text);
			if(ini.getKeyList("DelCond") == null){
				ini.createSection("DelCond");
			}
			ini.setKeyValue("DelCond", "KeepDay",text);
			ini.saveChange();
			
			View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
			String loginname = view.getLoginName();
			String minfo = loginname + "在" + OpObjectId.syslog_set.name + "中进行了  "+OpTypeId.edit.name+"  系统日志保持天数   操作";
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.syslog_set);

			applyDateButton.setDisabled(true);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
}
