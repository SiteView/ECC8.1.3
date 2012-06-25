package com.siteview.ecc.report;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;
import com.siteview.ecc.report.common.ErrorMessage;
import com.siteview.ecc.report.common.SelectableListheader;
import com.siteview.ecc.report.syslogreport.QueryCondition;
import com.siteview.ecc.report.syslogreport.SyslogQureyRecords;

/**
 *syslog 处理类
 * 
 * @company: siteview
 * @author: jian.li changed by: kai.zhang
 * @date:2009-4-21
 */

public class SysLogQuery extends GenericForwardComposer {

	private Calendar calStart = null;
	private Calendar calEnd = null;
	private Listbox syslogListbox;
	private Textbox biaodashi, sourceIP;
	private Datebox startdate, enddate;
	private Timebox starttime, endtime;
	private Checkbox f0, f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12,
			f13, f14, f15, f16, f17, f18, f19, f20, f21, f22, f23;
	private Checkbox l0, l1, l2, l3, l4, l5, l6, l7;
	private List reportlist = new ArrayList();
	private IniFile ini = new IniFile("syslog.ini");
	private Map<String, Checkbox> cmap = new HashMap<String, Checkbox>();
	private Map<String, Checkbox> dmap = new HashMap<String, Checkbox>();

	public SysLogQuery() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 需要初始化的一些参数
	 * 
	 * @param event
	 */
	public void onCreate$sysLogWin(Event event) {
		calStart = Calendar.getInstance();
		calStart.setTime(new Date());
		calStart.add(Calendar.HOUR, -24);
		startdate.setValue(calStart.getTime());
		calEnd = Calendar.getInstance();
		enddate.setValue(new Date());
		starttime.setValue(new Date());
		endtime.setValue(new Date());
		// 初始化checkbox 状态
		cmap.put("0", f0);
		cmap.put("1", f1);
		cmap.put("2", f2);
		cmap.put("3", f3);
		cmap.put("4", f4);
		cmap.put("5", f5);
		cmap.put("6", f6);
		cmap.put("7", f7);
		cmap.put("8", f8);
		cmap.put("9", f9);
		cmap.put("10", f10);
		cmap.put("11", f11);
		cmap.put("12", f12);
		cmap.put("13", f13);
		cmap.put("14", f14);
		cmap.put("15", f15);
		cmap.put("16", f16);
		cmap.put("17", f17);
		cmap.put("18", f18);
		cmap.put("19", f19);
		cmap.put("20", f20);
		cmap.put("21", f21);
		cmap.put("22", f22);
		cmap.put("23", f23);
		dmap.put("0", l0);
		dmap.put("1", l1);
		dmap.put("2", l2);
		dmap.put("3", l3);
		dmap.put("4", l4);
		dmap.put("5", l5);
		dmap.put("6", l6);
		dmap.put("7", l7);
		try {
			onRefresh(null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SelectableListheader.addPopupmenu(syslogListbox);
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String[] splString(String s, String regex) {

		return s.split(regex);
	}

	private void MakelistData(Listbox listb, ListModelList model,
			ListitemRenderer rend) {
		listb.setModel(model);
		listb.setItemRenderer(rend);
	}

	/**
	 * 点击查询按钮事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onClick$seachButton(Event event) throws Exception {
		// 先判断开始时间 和结束时间
		Calendar start_calendar = Calendar.getInstance();
		start_calendar.setTime(startdate.getValue());// Date -- Date
		Calendar starttemp = Calendar.getInstance();
		starttemp.setTime(starttime.getValue());
		start_calendar.set(Calendar.HOUR_OF_DAY, starttemp
				.get(Calendar.HOUR_OF_DAY));
		start_calendar.set(Calendar.MINUTE, starttemp.get(Calendar.MINUTE));

		Calendar end_calendar = Calendar.getInstance();
		end_calendar.setTime(enddate.getValue());
		Calendar endtemp = Calendar.getInstance();
		endtemp.setTime(endtime.getValue());
		end_calendar.set(Calendar.HOUR_OF_DAY, endtemp
				.get(Calendar.HOUR_OF_DAY));
		end_calendar.set(Calendar.MINUTE, endtemp.get(Calendar.MINUTE));

		java.util.List table = new java.util.ArrayList();
		if ((start_calendar.compareTo(end_calendar)) > 0) {
			table = new java.util.ArrayList(); // table.size() <1
			SyslogItemRender model = new SyslogItemRender(table);
			MakelistData(syslogListbox, model, model);
			Messagebox.show(ErrorMessage.TIME_ERROR, "提示", Messagebox.OK,
					Messagebox.INFORMATION);
			return;
		}

		List<Map<String, String>> v_fmap = new ArrayList<Map<String, String>>();

		QueryCondition byTime = new QueryCondition();
		byTime.setLimitType(false);
		byTime.setId("syslog");

		byTime
				.setBeginYear(String
						.valueOf((start_calendar.get(Calendar.YEAR))));
		byTime.setBeginMonth(String
				.valueOf((start_calendar.get(Calendar.MONTH) + 1)));// +1 -1?
		byTime.setBeginDay(String.valueOf((start_calendar
				.get(Calendar.DAY_OF_MONTH))));
		byTime.setBeginHour(String.valueOf((start_calendar
				.get(Calendar.HOUR_OF_DAY))));
		byTime.setBeginMinute(String.valueOf((start_calendar
				.get(Calendar.MINUTE))));
		byTime.setBeginSecond("00");

		byTime.setEndYear(String.valueOf((end_calendar.get(Calendar.YEAR))));
		byTime.setEndMonth(String
				.valueOf((end_calendar.get(Calendar.MONTH) + 1)));// +1 -1?
		byTime.setEndDay(String.valueOf((end_calendar
				.get(Calendar.DAY_OF_MONTH))));
		byTime.setEndHour(String.valueOf((end_calendar
				.get(Calendar.HOUR_OF_DAY))));
		byTime
				.setEndMinute(String
						.valueOf((end_calendar.get(Calendar.MINUTE))));
		byTime.setEndSecond("00");

		SyslogQureyRecords qr = null;
		try {
			qr = new SyslogQureyRecords("QueryRecordsByTime", byTime);
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		v_fmap = qr.getV_map();

		for (int i = 0; i < v_fmap.size(); i++)
			table.add(new SysLogValueList(v_fmap, i));

		if (biaodashi.getValue().isEmpty() == false) {
			List temList = new ArrayList();
			for (int i = 0; i < table.size(); i++) {
				String syslogmsg = ((SysLogValueList) table.get(i))
						.getSysLogmsg();
				boolean flag = Pattern.compile(biaodashi.getValue()).matcher(
						syslogmsg).find();
				if (flag == true) {
					temList.add(table.get(i));
				}
			}
			table = temList;
		}
		if (sourceIP.getValue().isEmpty() == false) {
			List temList = new ArrayList();
			for (int i = 0; i < table.size(); i++) {
				if (sourceIP.getValue().equals(
						((SysLogValueList) table.get(i)).getSourceIP())) {
					temList.add(table.get(i));
				}
			}
			table = temList;
		}
		// 条件设置的时候 是 必须要有一个选中 才能够 运行
		if (f0.isChecked() || f1.isChecked() || f2.isChecked()
				|| f3.isChecked() || f4.isChecked() || f5.isChecked()
				|| f6.isChecked() || f7.isChecked() || f8.isChecked()
				|| f9.isChecked() || f10.isChecked() || f11.isChecked()
				|| f12.isChecked() || f13.isChecked() || f14.isChecked()
				|| f15.isChecked() || f16.isChecked() || f17.isChecked()
				|| f18.isChecked() || f19.isChecked() || f20.isChecked()
				|| f21.isChecked() || f22.isChecked() || f23.isChecked()) {
			int[] flag = new int[24];
			if (f0.isChecked()) {
				flag[0] = 0;
			} else {
				flag[0] = -1;
			}

			if (f1.isChecked()) {
				flag[1] = 1;
			} else {
				flag[1] = -1;
			}

			if (f2.isChecked()) {
				flag[2] = 2;
			} else {
				flag[2] = -1;
			}

			if (f3.isChecked()) {
				flag[3] = 3;
			} else {
				flag[3] = -1;
			}

			if (f4.isChecked()) {
				flag[4] = 4;
			} else {
				flag[4] = -1;
			}

			if (f5.isChecked()) {
				flag[5] = 5;
			} else {
				flag[5] = -1;
			}

			if (f6.isChecked()) {
				flag[6] = 6;
			} else {
				flag[6] = -1;
			}

			if (f7.isChecked()) {
				flag[7] = 7;
			} else {
				flag[7] = -1;
			}

			if (f8.isChecked()) {
				flag[8] = 8;
			} else {
				flag[8] = -1;
			}

			if (f9.isChecked()) {
				flag[9] = 9;
			} else {
				flag[9] = -1;
			}

			if (f10.isChecked()) {
				flag[10] = 10;
			} else {
				flag[10] = -1;
			}

			if (f11.isChecked()) {
				flag[11] = 11;
			} else {
				flag[11] = -1;
			}

			if (f12.isChecked()) {
				flag[12] = 12;
			} else {
				flag[12] = -1;
			}

			if (f13.isChecked()) {
				flag[13] = 13;
			} else {
				flag[13] = -1;
			}

			if (f14.isChecked()) {
				flag[14] = 14;
			} else {
				flag[14] = -1;
			}

			if (f15.isChecked()) {
				flag[15] = 15;
			} else {
				flag[15] = -1;
			}

			if (f16.isChecked()) {
				flag[16] = 16;
			} else {
				flag[16] = -1;
			}

			if (f17.isChecked()) {
				flag[17] = 17;
			} else {
				flag[17] = -1;
			}

			if (f18.isChecked()) {
				flag[18] = 18;
			} else {
				flag[18] = -1;
			}

			if (f19.isChecked()) {
				flag[19] = 19;
			} else {
				flag[19] = -1;
			}

			if (f20.isChecked()) {
				flag[20] = 20;
			} else {
				flag[20] = -1;
			}

			if (f21.isChecked()) {
				flag[21] = 21;
			} else {
				flag[21] = -1;
			}

			if (f22.isChecked()) {
				flag[22] = 22;
			} else {
				flag[22] = -1;
			}

			if (f23.isChecked()) {
				flag[23] = 23;
			} else {
				flag[23] = -1;
			}

			List temList1 = new ArrayList();
			for (int i = 0; i < table.size(); i++) {
				for (int j = 0; j < flag.length; j++) {
					if (((SysLogValueList) table.get(i)).getFacility().equals(
							String.valueOf(flag[j]))) {

						if(((SysLogValueList) table.get(i)).getFacility().equals("0")){
							((SysLogValueList) table.get(i)).setFacility("User");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("1")){
							((SysLogValueList) table.get(i)).setFacility("Mail");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("2")){
							((SysLogValueList) table.get(i)).setFacility("Demon");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("3")){
							((SysLogValueList) table.get(i)).setFacility("Auth");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("4")){
							((SysLogValueList) table.get(i)).setFacility("Syslog");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("5")){
							((SysLogValueList) table.get(i)).setFacility("Lpr");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("6")){
							((SysLogValueList) table.get(i)).setFacility("News");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("7")){
							((SysLogValueList) table.get(i)).setFacility("UUCP");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("8")){
							((SysLogValueList) table.get(i)).setFacility("Cron");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("9")){
							((SysLogValueList) table.get(i)).setFacility("Security");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("10")){
							((SysLogValueList) table.get(i)).setFacility("FTP Demo");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("11")){
							((SysLogValueList) table.get(i)).setFacility("NTP");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("12")){
							((SysLogValueList) table.get(i)).setFacility("Log Audit");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("14")){
							((SysLogValueList) table.get(i)).setFacility("Log Alert");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("15")){
							((SysLogValueList) table.get(i)).setFacility("Clock Demo");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("16")){
							((SysLogValueList) table.get(i)).setFacility("Local0");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("17")){
							((SysLogValueList) table.get(i)).setFacility("Local1");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("18")){
							((SysLogValueList) table.get(i)).setFacility("Local2");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("19")){
							((SysLogValueList) table.get(i)).setFacility("Local3");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("20")){
							((SysLogValueList) table.get(i)).setFacility("Local4");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("21")){
							((SysLogValueList) table.get(i)).setFacility("Local5");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("22")){
							((SysLogValueList) table.get(i)).setFacility("Local6");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("23")){
							((SysLogValueList) table.get(i)).setFacility("Local7");
						}
						temList1.add(table.get(i));
					}
				}
			}
			table = temList1;
		} else {
			table = new ArrayList(); // table.size()<1
			SyslogItemRender model = new SyslogItemRender(table);
			MakelistData(syslogListbox, model, model);
		}
		if (l0.isChecked() || l1.isChecked() || l2.isChecked()
				|| l3.isChecked() || l4.isChecked() || l5.isChecked()
				|| l6.isChecked() || l7.isChecked()) {
			int[] flag1 = new int[8];
			if (l0.isChecked()) {
				flag1[0] = 0;
			} else {
				flag1[0] = -1;
			}

			if (l1.isChecked()) {
				flag1[1] = 1;
			} else {
				flag1[1] = -1;
			}

			if (l2.isChecked()) {
				flag1[2] = 2;
			} else {
				flag1[2] = -1;
			}

			if (l3.isChecked()) {
				flag1[3] = 3;
			} else {
				flag1[3] = -1;
			}

			if (l4.isChecked()) {
				flag1[4] = 4;
			} else {
				flag1[4] = -1;
			}

			if (l5.isChecked()) {
				flag1[5] = 5;
			} else {
				flag1[5] = -1;
			}

			if (l6.isChecked()) {
				flag1[6] = 6;
			} else {
				flag1[6] = -1;
			}

			if (l7.isChecked()) {
				flag1[7] = 7;
			} else {
				flag1[7] = -1;
			}
			List temList2 = new ArrayList();
			for (int i = 0; i < table.size(); i++) {
				for (int j = 0; j < flag1.length; j++) {
					if (((SysLogValueList) table.get(i)).getLeave().equals(
							String.valueOf(flag1[j]))) {
						if (((SysLogValueList) table.get(i)).getLeave().equals("0")){
							((SysLogValueList) table.get(i)).setLeave("Emergency");
						}
						if (((SysLogValueList) table.get(i)).getLeave().equals("1")){
							((SysLogValueList) table.get(i)).setLeave("Alert");
						}
						if (((SysLogValueList) table.get(i)).getLeave().equals("2")){
							((SysLogValueList) table.get(i)).setLeave("Critical");
						}
						if (((SysLogValueList) table.get(i)).getLeave().equals("3")){
							((SysLogValueList) table.get(i)).setLeave("Error");
						}
						if (((SysLogValueList) table.get(i)).getLeave().equals("4")){
							((SysLogValueList) table.get(i)).setLeave("Warring");
						}
						if (((SysLogValueList) table.get(i)).getLeave().equals("5")){
							((SysLogValueList) table.get(i)).setLeave("Notice");
						}

						temList2.add(table.get(i));
					}
				}
			}
			table = temList2;
		} else {
			table = new ArrayList(); // table.size()<1
			SyslogItemRender model = new SyslogItemRender(table);
			MakelistData(syslogListbox, model, model);
		}
		if(table==null || table.size()<=0)
			Messagebox.show(ErrorMessage.NODATA, "提示", Messagebox.OK, Messagebox.INFORMATION);
		SyslogItemRender model = new SyslogItemRender(table);
		MakelistData(syslogListbox, model, model);
	}

	/**
	 * 点击导出报表按钮事件
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void onClick$exportButton(Event event) throws Exception {
		// 先判断开始时间 和结束时间
		Calendar start_calendar = Calendar.getInstance();
		start_calendar.setTime(startdate.getValue());// Date -- Date
		Calendar starttemp = Calendar.getInstance();
		starttemp.setTime(starttime.getValue());
		start_calendar.set(Calendar.HOUR_OF_DAY, starttemp
				.get(Calendar.HOUR_OF_DAY));
		start_calendar.set(Calendar.MINUTE, starttemp.get(Calendar.MINUTE));

		Calendar end_calendar = Calendar.getInstance();
		end_calendar.setTime(enddate.getValue());
		Calendar endtemp = Calendar.getInstance();
		endtemp.setTime(endtime.getValue());
		end_calendar.set(Calendar.HOUR_OF_DAY, endtemp
				.get(Calendar.HOUR_OF_DAY));
		end_calendar.set(Calendar.MINUTE, endtemp.get(Calendar.MINUTE));

		java.util.List table = new java.util.ArrayList();
		if ((start_calendar.compareTo(end_calendar)) > 0) {
			table = new java.util.ArrayList(); // table.size() <1
			SyslogItemRender model = new SyslogItemRender(table);
			MakelistData(syslogListbox, model, model);
			Messagebox.show(ErrorMessage.TIME_ERROR, "提示", Messagebox.OK,
					Messagebox.INFORMATION);
			return;
		}

		List<Map<String, String>> v_fmap = new ArrayList<Map<String, String>>();

		QueryCondition byTime = new QueryCondition();
		byTime.setLimitType(false);
		byTime.setId("syslog");

		byTime
				.setBeginYear(String
						.valueOf((start_calendar.get(Calendar.YEAR))));
		byTime.setBeginMonth(String
				.valueOf((start_calendar.get(Calendar.MONTH) + 1)));// +1 -1?
		byTime.setBeginDay(String.valueOf((start_calendar
				.get(Calendar.DAY_OF_MONTH))));
		byTime.setBeginHour(String.valueOf((start_calendar
				.get(Calendar.HOUR_OF_DAY))));
		byTime.setBeginMinute(String.valueOf((start_calendar
				.get(Calendar.MINUTE))));
		byTime.setBeginSecond("00");

		byTime.setEndYear(String.valueOf((end_calendar.get(Calendar.YEAR))));
		byTime.setEndMonth(String
				.valueOf((end_calendar.get(Calendar.MONTH) + 1)));// +1 -1?
		byTime.setEndDay(String.valueOf((end_calendar
				.get(Calendar.DAY_OF_MONTH))));
		byTime.setEndHour(String.valueOf((end_calendar
				.get(Calendar.HOUR_OF_DAY))));
		byTime
				.setEndMinute(String
						.valueOf((end_calendar.get(Calendar.MINUTE))));
		byTime.setEndSecond("00");

		SyslogQureyRecords qr = null;
		try {
			qr = new SyslogQureyRecords("QueryRecordsByTime", byTime);
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK, Messagebox.ERROR);
			return;
		}
		v_fmap = qr.getV_map();

		for (int i = 0; i < v_fmap.size(); i++)
			table.add(new SysLogValueList(v_fmap, i));
		// 从查出来的数据中进行筛选
		if (biaodashi.getValue().isEmpty() == false) {
			List temList = new ArrayList();
			for (int i = 0; i < table.size(); i++) {
				String syslogmsg = ((SysLogValueList) table.get(i))
						.getSysLogmsg();
				boolean flag = Pattern.compile(biaodashi.getValue()).matcher(
						syslogmsg).find();
				if (flag == true) {
					temList.add(table.get(i));
				}
			}
			table = temList;
		}
		if (sourceIP.getValue().isEmpty() == false) {
			List temList = new ArrayList();
			for (int i = 0; i < table.size(); i++) {
				if (sourceIP.getValue().equals(
						((SysLogValueList) table.get(i)).getSourceIP())) {
					temList.add(table.get(i));
				}
			}
			table = temList;
		}
		// 必须有一个被选中
		if (f0.isChecked() || f1.isChecked() || f2.isChecked()
				|| f3.isChecked() || f4.isChecked() || f5.isChecked()
				|| f6.isChecked() || f7.isChecked() || f8.isChecked()
				|| f9.isChecked() || f10.isChecked() || f11.isChecked()
				|| f12.isChecked() || f13.isChecked() || f14.isChecked()
				|| f15.isChecked() || f16.isChecked() || f17.isChecked()
				|| f18.isChecked() || f19.isChecked() || f20.isChecked()
				|| f21.isChecked() || f22.isChecked() || f23.isChecked()) {
			int[] flag = new int[24]; // 数组
			if (f0.isChecked()) {
				flag[0] = 0;
			} else {
				flag[0] = -1;
			}

			if (f1.isChecked()) {
				flag[1] = 1;
			} else {
				flag[1] = -1;
			}

			if (f2.isChecked()) {
				flag[2] = 2;
			} else {
				flag[2] = -1;
			}

			if (f3.isChecked()) {
				flag[3] = 3;
			} else {
				flag[3] = -1;
			}

			if (f4.isChecked()) {
				flag[4] = 4;
			} else {
				flag[4] = -1;
			}

			if (f5.isChecked()) {
				flag[5] = 5;
			} else {
				flag[5] = -1;
			}

			if (f6.isChecked()) {
				flag[6] = 6;
			} else {
				flag[6] = -1;
			}

			if (f7.isChecked()) {
				flag[7] = 7;
			} else {
				flag[7] = -1;
			}

			if (f8.isChecked()) {
				flag[8] = 8;
			} else {
				flag[8] = -1;
			}

			if (f9.isChecked()) {
				flag[9] = 9;
			} else {
				flag[9] = -1;
			}

			if (f10.isChecked()) {
				flag[10] = 10;
			} else {
				flag[10] = -1;
			}

			if (f11.isChecked()) {
				flag[11] = 11;
			} else {
				flag[11] = -1;
			}

			if (f12.isChecked()) {
				flag[12] = 12;
			} else {
				flag[12] = -1;
			}

			if (f13.isChecked()) {
				flag[13] = 13;
			} else {
				flag[13] = -1;
			}

			if (f14.isChecked()) {
				flag[14] = 14;
			} else {
				flag[14] = -1;
			}

			if (f15.isChecked()) {
				flag[15] = 15;
			} else {
				flag[15] = -1;
			}

			if (f16.isChecked()) {
				flag[16] = 16;
			} else {
				flag[16] = -1;
			}

			if (f17.isChecked()) {
				flag[17] = 17;
			} else {
				flag[17] = -1;
			}

			if (f18.isChecked()) {
				flag[18] = 18;
			} else {
				flag[18] = -1;
			}

			if (f19.isChecked()) {
				flag[19] = 19;
			} else {
				flag[19] = -1;
			}

			if (f20.isChecked()) {
				flag[20] = 20;
			} else {
				flag[20] = -1;
			}

			if (f21.isChecked()) {
				flag[21] = 21;
			} else {
				flag[21] = -1;
			}

			if (f22.isChecked()) {
				flag[22] = 22;
			} else {
				flag[22] = -1;
			}

			if (f23.isChecked()) {
				flag[23] = 23;
			} else {
				flag[23] = -1;
			}

			List temList1 = new ArrayList();
			for (int i = 0; i < table.size(); i++) {
				for (int j = 0; j < flag.length; j++) {
					if (((SysLogValueList) table.get(i)).getFacility().equals(
							String.valueOf(flag[j]))) {
						
						if(((SysLogValueList) table.get(i)).getFacility().equals("0")){
							((SysLogValueList) table.get(i)).setFacility("User");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("1")){
							((SysLogValueList) table.get(i)).setFacility("Mail");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("2")){
							((SysLogValueList) table.get(i)).setFacility("Demon");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("3")){
							((SysLogValueList) table.get(i)).setFacility("Auth");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("4")){
							((SysLogValueList) table.get(i)).setFacility("Syslog");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("5")){
							((SysLogValueList) table.get(i)).setFacility("Lpr");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("6")){
							((SysLogValueList) table.get(i)).setFacility("News");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("7")){
							((SysLogValueList) table.get(i)).setFacility("UUCP");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("8")){
							((SysLogValueList) table.get(i)).setFacility("Cron");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("9")){
							((SysLogValueList) table.get(i)).setFacility("Security");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("10")){
							((SysLogValueList) table.get(i)).setFacility("FTP Demo");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("11")){
							((SysLogValueList) table.get(i)).setFacility("NTP");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("12")){
							((SysLogValueList) table.get(i)).setFacility("Log Audit");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("14")){
							((SysLogValueList) table.get(i)).setFacility("Log Alert");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("15")){
							((SysLogValueList) table.get(i)).setFacility("Clock Demo");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("16")){
							((SysLogValueList) table.get(i)).setFacility("Local0");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("17")){
							((SysLogValueList) table.get(i)).setFacility("Local1");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("18")){
							((SysLogValueList) table.get(i)).setFacility("Local2");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("19")){
							((SysLogValueList) table.get(i)).setFacility("Local3");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("20")){
							((SysLogValueList) table.get(i)).setFacility("Local4");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("21")){
							((SysLogValueList) table.get(i)).setFacility("Local5");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("22")){
							((SysLogValueList) table.get(i)).setFacility("Local6");
						}
						if(((SysLogValueList) table.get(i)).getFacility().equals("23")){
							((SysLogValueList) table.get(i)).setFacility("Local7");
						}
						
						temList1.add(table.get(i));
					}
				}
			}
			table = temList1;
		} else {//都没选中
			table = new java.util.ArrayList(); // table.size() <1
			SyslogItemRender model = new SyslogItemRender(table);
			MakelistData(syslogListbox, model, model);// 跟新页面的数据
		}
		// 至少有一个被选中才能 让程序运行
		if (l0.isChecked() || l1.isChecked() || l2.isChecked()
				|| l3.isChecked() || l4.isChecked() || l5.isChecked()
				|| l6.isChecked() || l7.isChecked()) {
			int[] flag1 = new int[8];
			if (l0.isChecked()) {
				flag1[0] = 0;
			} else {
				flag1[0] = -1;
			}

			if (l1.isChecked()) {
				flag1[1] = 1;
			} else {
				flag1[1] = -1;
			}

			if (l2.isChecked()) {
				flag1[2] = 2;
			} else {
				flag1[2] = -1;
			}

			if (l3.isChecked()) {
				flag1[3] = 3;
			} else {
				flag1[3] = -1;
			}

			if (l4.isChecked()) {
				flag1[4] = 4;
			} else {
				flag1[4] = -1;
			}

			if (l5.isChecked()) {
				flag1[5] = 5;
			} else {
				flag1[5] = -1;
			}

			if (l6.isChecked()) {
				flag1[6] = 6;
			} else {
				flag1[6] = -1;
			}

			if (l7.isChecked()) {
				flag1[7] = 7;
			} else {
				flag1[7] = -1;
			}
			List temList2 = new ArrayList();
			for (int i = 0; i < table.size(); i++) {
				for (int j = 0; j < flag1.length; j++) {
					if (((SysLogValueList) table.get(i)).getLeave().equals(
							String.valueOf(flag1[j]))) {
						
						if (((SysLogValueList) table.get(i)).getLeave().equals("0")){
							((SysLogValueList) table.get(i)).setLeave("Emergency");
						}
						if (((SysLogValueList) table.get(i)).getLeave().equals("1")){
							((SysLogValueList) table.get(i)).setLeave("Alert");
						}
						if (((SysLogValueList) table.get(i)).getLeave().equals("2")){
							((SysLogValueList) table.get(i)).setLeave("Critical");
						}
						if (((SysLogValueList) table.get(i)).getLeave().equals("3")){
							((SysLogValueList) table.get(i)).setLeave("Error");
						}
						if (((SysLogValueList) table.get(i)).getLeave().equals("4")){
							((SysLogValueList) table.get(i)).setLeave("Warring");
						}
						if (((SysLogValueList) table.get(i)).getLeave().equals("5")){
							((SysLogValueList) table.get(i)).setLeave("Notice");
						}

						
						temList2.add(table.get(i));
					}
				}
			}
			table = temList2;
		} else {
			table = new java.util.ArrayList(); // table.size() <1
			SyslogItemRender model = new SyslogItemRender(table);
			MakelistData(syslogListbox, model, model);// 跟新页面的数据
		}
		if(table==null || table.size()<=0){
			Messagebox.show(ErrorMessage.NODATA, "提示", Messagebox.OK, Messagebox.INFORMATION);
			SyslogItemRender model = new SyslogItemRender(table);
			MakelistData(syslogListbox, model, model);// 跟新页面的数据			
			reportlist.addAll(table);
			return;
		}

		Map pmap = new HashMap();
		pmap.put("reportlist", table);
		final Window win = (Window) Executions.createComponents(
				"/main/report/syslogreport/exportsyslogreport.zul", null, null);
		win.setClosable(true);
		win.setAttribute("datasource", table);
		win.doModal();
	}
}
