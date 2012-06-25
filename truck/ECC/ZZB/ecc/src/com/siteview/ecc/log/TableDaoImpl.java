/**
 * 
 */
package com.siteview.ecc.log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Timebox;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.UserEdit;
import com.siteview.base.data.UserRightId;
import com.siteview.base.manage.View;
import com.siteview.ecc.log.beans.LogValueBean;
import com.siteview.ecc.report.common.AddLinkFuntion;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.report.common.UrlPropertiesType;
import com.siteview.ecc.util.LinkCheck;
import com.siteview.ecc.util.Toolkit;


/**
 * @author yuandong用户操作日志主页面主要进行用户交互和输入验证
 */
public class TableDaoImpl extends GenericAutowireComposer {
	private final static Logger logger = Logger.getLogger(TableDaoImpl.class);

	private Listbox 								logListbox;
	private Datebox 								logBeginDate;
	private Datebox 								logEndDate;
	private Calendar 								calStart = null;
	private Calendar 								calEnd = null;
	private Listbox 								logUserName;
	private Listbox 								logOperateObj;
	private Listbox 								logOperateType;
	private Listitem 								cbiUser0;
	private Listitem 								cbiObj25;
	private Listitem 								cbiType10;
	private Timebox 								logBeginTime;
	private Timebox 								logEndTime;
	private Label   								userLink;


	public void onInit() {
		calStart = Calendar.getInstance();
		calStart.setTime(new Date());
		calStart.add(Calendar.HOUR, -24);
		logBeginDate.setValue(calStart.getTime());
		logBeginTime.setValue(calStart.getTime());
		calEnd = Calendar.getInstance();
		logEndDate.setValue(calEnd.getTime());
		logEndTime.setValue(calEnd.getTime());
		// TableDaoImpl页面初始化
		createLogOperateObj();
		logUserName.setSelectedItem(cbiUser0);
		logOperateObj.setSelectedItem(cbiObj25);
		logOperateType.setSelectedItem(cbiType10);
		//增加链接
		boolean flag = LinkCheck.getLinkCheck().CanSeeLink(UserRightId.UserManager);
		userLink.setStyle("margin-left:10%");
		if(flag){
			userLink.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;margin-left:10%;" );
			userLink.addEventListener(Events.ON_CLICK, new AddLinkFuntion(UrlPropertiesType.UserManager));
		}
	}
	public void createLogOperateObj(){
		View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop().getSession());
		
		Listitem listitem = null;
		setValue(listitem,"登录",			"0",	"cbiObj0",	false);
		setValue(listitem,"组",				"2",	"cbiObj1",	false);
		setValue(listitem,"设备",			"3",	"cbiObj2",	false);
		setValue(listitem,"监测器",			"4",	"cbiObj3",	false);
		setValue(listitem,"客户名称",		"6",	"cbiObj4",	false);
		setValue(listitem,"监测服务器名称",	"7",	"cbiObj5",	false);
		if(view.isAdmin()){
			setValue(listitem,"监测器浏览",		"22",	"cbiObj6",	false);
			setValue(listitem,"监测器设置",		"23",	"cbiObj7",	false);
			setValue(listitem,"拓扑视图",		"9",	"cbiObj8",	false);
			setValue(listitem,"报警规则",		"8",	"cbiObj9",	false);
			setValue(listitem,"统计报告",		"17",	"cbiObj10",	false);
			setValue(listitem,"TopN报告",		"16",	"cbiObj11",	false);
			setValue(listitem,"基本设置",		"5",	"cbiObj12",	false);
			setValue(listitem,"邮件设置",		"12",	"cbiObj13",	false);
			setValue(listitem,"邮件模板",		"24",	"cbiObj14",	false);
			setValue(listitem,"短信设置",		"15",	"cbiObj15",	false);
			setValue(listitem,"短信模板",		"25",	"cbiObj16",	false);
			setValue(listitem,"值班表配置",		"18",	"cbiObj17",	false);
			setValue(listitem,"用户管理",		"11",	"cbiObj18",	false);
			setValue(listitem,"绝对时间任务计划","14",	"cbiObj19",	false);
			setValue(listitem,"时间段任务计划",	"13",	"cbiObj20",	false);
			setValue(listitem,"相对时间任务计划","20",	"cbiObj21",	false);
			setValue(listitem,"SysLog设置",		"10",	"cbiObj22",	false);
			setValue(listitem,"IP地址",			"28",	"cbiObj28",	false);
		}else
		{
			UserEdit userEdit = new UserEdit(view.getUserIni());
			if(userEdit.getModuleRight(UserRightId.MonitorBrower)){//监测器浏览
				setValue(listitem,"监测器浏览",		"22",	"cbiObj6",	false);
			}
			if(userEdit.getModuleRight(UserRightId.SetMonitor)){//监测器设置
				setValue(listitem,"监测器设置",		"23",	"cbiObj7",	false);
			}
			if(userEdit.getModuleRight(UserRightId.TopoView)){//拓扑视图
				setValue(listitem,"拓扑视图",		"9",	"cbiObj8",	false);
			}
			if(userEdit.getModuleRight(UserRightId.AlertRule)){//报警规则
				setValue(listitem,"报警规则",		"8",	"cbiObj9",	false);
			}
			if(userEdit.getModuleRight(UserRightId.ReportStatistic)){//统计报告
				setValue(listitem,"统计报告",		"17",	"cbiObj10",	false);
			}
			if(userEdit.getModuleRight(UserRightId.ReportTopN)){//TopN报告
				setValue(listitem,"TopN报告",		"16",	"cbiObj11",	false);
			}
			if(userEdit.getModuleRight(UserRightId.SetGeneral)){//基本设置
				setValue(listitem,"基本设置",		"5",	"cbiObj12",	false);
			}
			if(userEdit.getModuleRight(UserRightId.SetMail)){//邮件设置
				setValue(listitem,"邮件设置",		"12",	"cbiObj13",	false);
				setValue(listitem,"邮件模板",		"24",	"cbiObj14",	false);
			}
			if(userEdit.getModuleRight(UserRightId.SetSms)){//短信设置
				setValue(listitem,"短信设置",		"15",	"cbiObj15",	false);
				setValue(listitem,"短信模板",		"25",	"cbiObj16",	false);
			}
			if(userEdit.getModuleRight(UserRightId.SetMaintain)){//值班表设置
				setValue(listitem,"值班表配置",		"18",	"cbiObj17",	false);
			}
			if(userEdit.getModuleRight(UserRightId.UserManager)){//用户管理
				setValue(listitem,"用户管理",		"11",	"cbiObj18",	false);
			}
			if(userEdit.getModuleRight(UserRightId.TaskAbsolute)){//绝对时间任务计划
				setValue(listitem,"绝对时间任务计划","14",	"cbiObj19",	false);
			}
			if(userEdit.getModuleRight(UserRightId.TaskPeriod)){//时间段任务计划
				setValue(listitem,"时间段任务计划",	"13",	"cbiObj20",	false);
			}
			if(userEdit.getModuleRight(UserRightId.TaskRelative)){//相对时间任务计划
				setValue(listitem,"相对时间任务计划","20",	"cbiObj21",	false);
			}
			if(userEdit.getModuleRight(UserRightId.SysLogSet)){//SysLog设置
				setValue(listitem,"SysLog设置",		"10",	"cbiObj22",	false);
			}
		}
	}
	public void setValue(Listitem listitem,String label,String value,String id,boolean isSelected){
		listitem = new Listitem(label);
		listitem.setId(id);
		listitem.setValue(value);
		listitem.setParent(logOperateObj);
		if(isSelected){
			logOperateObj.setSelectedItem(listitem);
		}
	}
		
	public static List<String> getUserNameList() {
		IniFile ini = new IniFile("user.ini");
		List<String> namelist = new ArrayList<String>();
		try {
			ini.load();
			List<String> section = ini.getSectionList();
			for (int i = 0; i < section.size(); i++) {
				String LoginName = ini.getValue(section.get(i), "LoginName");
				if(LoginName == null){
					continue;
				}
				if("".equals(LoginName.trim())){
					continue;
				}
				namelist.add(LoginName.trim());
			}
			Collections.sort(namelist);
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return namelist;
	}

//	private void MakelistData(Listbox listb, ListModelList model,
//			ListitemRenderer rend) {
//		listb.getPagingChild().setMold("os");
//		listb.setModel(model);
//		listb.setItemRenderer(rend);
//	}

	public void onShowAllLog(Event event) throws Exception {
		try{
			logUserName.setSelectedItem(cbiUser0);
			logOperateObj.setSelectedItem(cbiObj25);
			logOperateType.setSelectedItem(cbiType10);
			
			List<Map<String, String>> v_fmap = new ArrayList<Map<String, String>>();
			QueryCondition byCount = new QueryCondition();
			byCount.setLimitType(true);
			byCount.setId("UserOperateLog");
			byCount.setCount("50000");
			QueryRecords qr = new QueryRecords("QueryRecordsByCount", byCount);
			v_fmap = qr.getV_map();
			ArrayList<LogValueBean> logValueBeans = new ArrayList<LogValueBean>();
			for (int i = 0; i < v_fmap.size(); i++)
				logValueBeans.add(new LogValueBean(v_fmap, i));
			
//			LogItemRenderer model = new LogItemRenderer(logValueBeans);
//			MakelistData(logListbox, model, model);

			UserOperateLogListbox listbox = (UserOperateLogListbox)logListbox;
		  	ChartUtil.clearListbox(listbox);
		  	listbox.setLogValueBeans(logValueBeans);
		  	listbox.onCreate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void onQueryByCondition(Event event) throws Exception {
		try{
			List<Map<String, String>> v_fmap = new ArrayList<Map<String, String>>();

			QueryCondition byCondition = new QueryCondition();

			calStart.setTime(logBeginDate.getValue());
			Calendar t = Calendar.getInstance();
			t.setTime(logBeginTime.getValue());
			calStart.set(Calendar.HOUR_OF_DAY, t.get(Calendar.HOUR_OF_DAY));
			calStart.set(Calendar.MINUTE, t.get(Calendar.MINUTE));
			calEnd.setTime(logEndDate.getValue());
			Calendar t1 = Calendar.getInstance();
			t1.setTime(logEndTime.getValue());
			calEnd.set(Calendar.HOUR_OF_DAY, t1.get(Calendar.HOUR_OF_DAY));
			calEnd.set(Calendar.MINUTE, t1.get(Calendar.MINUTE));

			if(calStart.compareTo(calEnd)>=0){
				Messagebox.show("开始时间必须小于结束时间！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}
			byCondition.setLimitType(false);
			byCondition.setId("UserOperateLog");
			byCondition.setBeginYear(String.valueOf(calStart.get(Calendar.YEAR)));
			byCondition.setBeginMonth(String.valueOf(calStart.get(Calendar.MONTH) + 1));
			byCondition.setBeginDay(String.valueOf(calStart.get(Calendar.DAY_OF_MONTH)));
			byCondition.setBeginHour(String.valueOf(calStart.get(Calendar.HOUR_OF_DAY)));
			byCondition.setBeginMinute(String.valueOf(calStart.get(Calendar.MINUTE)));
			byCondition.setBeginSecond(String.valueOf(calStart.get(Calendar.SECOND)));

			byCondition.setEndYear(String.valueOf(calEnd.get(Calendar.YEAR)));
			byCondition.setEndMonth(String.valueOf(calEnd.get(Calendar.MONTH) + 1));
			byCondition.setEndDay(String.valueOf(calEnd.get(Calendar.DAY_OF_MONTH)));
			byCondition.setEndHour(String.valueOf(calEnd.get(Calendar.HOUR_OF_DAY)));
			byCondition.setEndMinute(String.valueOf(calEnd.get(Calendar.MINUTE)));
			byCondition.setEndSecond(String.valueOf(calEnd.get(Calendar.SECOND)));
			byCondition.setUserId((String) logUserName.getSelectedItem().getValue());
			
			logger.info("@"+logOperateObj.getSelectedItem().getValue());
			byCondition.setOperateObjName((String) logOperateObj.getSelectedItem().getValue());
			byCondition.setOperateType((String) logOperateType.getSelectedItem().getValue());
			QueryRecords qr = new QueryRecords("QueryRecordsByTime", byCondition);
			v_fmap = qr.getV_map();
			java.util.ArrayList<LogValueBean> logValueBeans = new java.util.ArrayList<LogValueBean>();
			for (int i = 0; i < v_fmap.size(); i++)
				logValueBeans.add(new LogValueBean(v_fmap, i));
//			LogItemRenderer model = new LogItemRenderer(table);
//			MakelistData(logListbox, model, model);
			
			UserOperateLogListbox listbox = (UserOperateLogListbox)logListbox;
		  	ChartUtil.clearListbox(listbox);
		  	listbox.setLogValueBeans(logValueBeans);
		  	listbox.onCreate();	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
