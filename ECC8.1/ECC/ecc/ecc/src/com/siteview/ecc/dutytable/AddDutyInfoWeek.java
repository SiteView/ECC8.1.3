package com.siteview.ecc.dutytable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Combobox;

import com.siteview.base.manage.View;
import com.siteview.ecc.email.IniFilePack;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.util.Toolkit;

public class AddDutyInfoWeek extends GenericAutowireComposer {
	
	private static final long 						serialVersionUID = 1L;

	private Include 								eccBody;
	private Textbox  								alarmEmailbox;
	private Timebox 								beginduty; 
	private Timebox 								endduty;
	private Textbox 								mobilePhoneNum;
	private String									sectionGlobal = "";
	private Combobox								weekValue;

	public void onInit()throws Exception {
		try{		
			SimpleDateFormat df3 = new SimpleDateFormat("HH:mm");
			beginduty.setValue(df3.parse("9:00"));
			endduty.setValue(df3.parse("18:00"));

			Session session = this.session;
			Object sectionObject = session.getAttribute(DutyConstant.Add_DutyFather_Section);
			if(sectionObject != null){
				sectionGlobal = (String)sectionObject;
			}
			
			weekValue.setSelectedIndex(0);
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void onAddDutyInfo(Event event)throws Exception{
		try{

			String alarmEmailboxValue     	= 	alarmEmailbox.getValue().trim();
			Date begindutyValue				=   beginduty.getValue();
			Date enddutyValue				=   endduty.getValue();
			String mobileValue 				= 	mobilePhoneNum.getValue();			
			
			if ("".endsWith(mobileValue.trim())) {
				try{
					Messagebox.show("手机号码不能为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				mobilePhoneNum.setValue(null);
				mobilePhoneNum.setFocus(true);
				return;
			}
			long mobileLong = 0;
			try{
				mobileLong = Long.parseLong(mobileValue);
				if(mobileLong > Long.parseLong("19999999999") ||
						mobileLong < Long.parseLong("10000000000")){//11位
					throw new Exception("");
				}
			}catch(Exception e){
				e.printStackTrace();
				Messagebox.show("手机号码不正确！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				mobilePhoneNum.setFocus(true);
				return;
			}
			if ("".equals(alarmEmailboxValue)) {
				Messagebox.show("详细信息中接收报警邮箱不能够为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				alarmEmailbox.setFocus(true);
				return;
			}
			String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(alarmEmailboxValue);
			if(!matcher.matches()){
				Messagebox.show("邮件格式不正确！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				alarmEmailbox.setFocus(true);
				return;
			}

			if (begindutyValue == null) {
				Messagebox.show("您还没有选择开始时间", "提示", Messagebox.OK, Messagebox.INFORMATION);
				beginduty.setFocus(true);
				return;
			}
			if (enddutyValue == null) {
				Messagebox.show("您还没有选择结束时间", "提示", Messagebox.OK, Messagebox.INFORMATION);
				endduty.setFocus(true);
				return;
			}	

			if(begindutyValue.compareTo(enddutyValue)>=0){
				Messagebox.show("开始时间大于或等于结束时间", "提示", Messagebox.OK, Messagebox.INFORMATION);
				endduty.setFocus(true);
				return;
			}
			
			String item1Value = "";
			String weekString = weekValue.getSelectedItem().getValue()+",";
			item1Value = item1Value +weekString;
			String[] time1 = new String[2];
			SimpleDateFormat df3 = new SimpleDateFormat("HH:mm");
			time1 = df3.format(beginduty.getValue()).split(":");
			for (int i = 0; i < time1.length; i++) {
				item1Value = item1Value + time1[i] + ",";
			}
		
			String[] time2 = new String[2];
			time2 = df3.format(endduty.getValue()).split(":");
			for (int i = 0; i < time2.length; i++) {
				item1Value = item1Value + time2[i] + ",";
			}
			
			item1Value = item1Value + mobilePhoneNum.getValue() + ","+ alarmEmailbox.getValue();
			
			IniFilePack ini = new IniFilePack("watchsheetcfg.ini");
			try{
				ini.load();
			}catch(Exception e){}
			
			String count = ini.getM_fmap().get(sectionGlobal).get("count");	
			if(count == null){count = "0";}
			if("".equals(count)){count = "0";}
			String itemX=""+(Integer.parseInt(count)+1);
			String sonSection = "item"+System.currentTimeMillis();
			ini.setKeyValue(sectionGlobal, "count",itemX );				
			ini.setKeyValue(sectionGlobal,sonSection, item1Value); 
			ini.setKeyValue(sectionGlobal, "return", "true");
			ini.saveChange();
		
			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			String minfo=loginname+" "+"在"+OpObjectId.duty_set.name+"中进行了  "+OpTypeId.add.name+"详细信息操作，添加对象为 "+sectionGlobal;
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.duty_set);
			
			session.setAttribute(DutyConstant.Add_DutyFather_Section,sectionGlobal);
			session.setAttribute(DutyConstant.Add_DutySon_Section, sonSection);
			session.setAttribute(DutyConstant.State, "3");//绑定 state 值
			
			String targetUrl = "/main/setting/setmaintain.zul";
			eccBody = (Include) (this.desktop.getPage("eccmain").getFellow("eccBody"));
			eccBody.setSrc(null);
			eccBody.setSrc(targetUrl);
			
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	public void onRefresh()throws Exception{
		try{
			session.setAttribute(DutyConstant.Add_DutyFather_Section,sectionGlobal);
			session.setAttribute(DutyConstant.State, "3");//绑定 state 值
			
			String targetUrl = "/main/setting/setmaintain.zul";
			eccBody = (Include) (this.desktop.getPage("eccmain").getFellow("eccBody"));
			eccBody.setSrc(null);
			eccBody.setSrc(targetUrl);
		}catch(Exception e){
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
}
