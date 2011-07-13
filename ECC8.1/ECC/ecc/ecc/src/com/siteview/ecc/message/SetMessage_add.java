package com.siteview.ecc.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Label;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.UserRightId;
import com.siteview.base.manage.View;

import com.siteview.ecc.email.IniFilePack;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.report.common.AddLinkFuntion;
import com.siteview.ecc.report.common.UrlPropertiesType;
import com.siteview.ecc.tasks.Task;
import com.siteview.ecc.tasks.TaskPack;
import com.siteview.ecc.util.LinkCheck;
import com.siteview.ecc.util.Toolkit;

import com.siteview.ecc.email.Email_add;

public class SetMessage_add extends GenericAutowireComposer {
	/**
	 * 
	 */
	private static final long 					serialVersionUID = 1L;
	private Textbox 							name;
	private Checkbox 							hold;
	private Combobox 							templateType;
	private Combobox 							messageTemplater;
	private Combobox 							taskPlan;
	private Combobox 							taskType;
	private Include 							eccBody;
	private Window 								messageWin;
	private Textbox 							mobileNum;
	private Label 								messageTemplateLink;
	private Label 								taskLink;
	
	public void onInit()throws Exception{
		
		messageTemplater.getChildren().clear();
		taskPlan.getChildren().clear();
		
		taskType.setSelectedIndex(1);//时间段的任务计划
		int taskSize = createNewList("2");
		if(taskSize > 0){
			taskPlan.setSelectedIndex(0);
		}
		//添加链接
		boolean flag = LinkCheck.getLinkCheck().CanSeeLink(UserRightId.TaskPeriod);
		taskLink.setStyle("");
		if(flag){
			taskLink.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;" );
			taskLink.addEventListener(Events.ON_CLICK, new AddLinkFuntion(UrlPropertiesType.TaskPeriod));
		}
		
		
		templateType.setSelectedIndex(0);//com类型
		int templateSize = createTemplateList("com");
		if(templateSize > 0){
			messageTemplater.setSelectedIndex(0);
		}
		//添加链接
		messageTemplateLink.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;" );
		messageTemplateLink.addEventListener(Events.ON_CLICK, new AddLinkFuntion(UrlPropertiesType.SetSms,"msgTemplateSet"));
	}

	public void onSave(Event event)throws Exception{
		if (name.getValue() == null || name.getValue().trim().isEmpty()) {
			try{
				Messagebox.show("请填写名称", "提示", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			name.setValue(null);
			name.setFocus(true);
			return;
		}
		IniFilePack ini = new IniFilePack("smsphoneset.ini");
		try{
			ini.load();
		}catch(Exception e){
		}
		
		int num=ini.getM_fmap().size();
		Map<String, Map<String, String>> messageList = ini.getM_fmap();
		Set<String> keySet=messageList.keySet();
		Iterator<String> keyIterator = keySet.iterator();
		String key="";
		String[] emailNameList=new String[num];
		int i=0;
		while (keyIterator.hasNext()) {
			key=(String) keyIterator.next();
			Map<String, String> mapValue=messageList.get(key);
			emailNameList[i++]=(String)mapValue.get("Name");
		}
		for(int m=0;m<emailNameList.length;m++){
			if(emailNameList[m]!=null&&emailNameList[m].equals(name.getValue().trim())){
				try{
					Messagebox.show("此信息名已经存在！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				name.setFocus(true);
				return;
			}
		}

		String mobileValue = mobileNum.getValue().toString();			
		if ("".endsWith(mobileValue.trim())) {
			try{
				Messagebox.show("手机号码不能为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			mobileNum.setValue(null);
			mobileNum.setFocus(true);
			return;
		}
		long mobileLong = 0;
		try{
			mobileLong = Long.parseLong(mobileValue);
			if(mobileLong > Long.parseLong("19999999999") ||
					mobileLong < Long.parseLong("10000000000")){//11位
										     //  12345678901	
				throw new Exception("");
			}
		}catch(Exception e){
			Messagebox.show("手机号码不正确！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			mobileNum.setFocus(true);
			return;
		}
		
		
		if(taskPlan.getValue() == null||taskPlan.getValue().isEmpty()){
			try{
				Messagebox.show("任务计划没有设值！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			return;
		}
		
		UUID uuid = UUID.randomUUID();
		String section=uuid.toString();
		try{
			ini.createSection(uuid.toString());
			ini.setKeyValue(section, "Name", name.getValue()==null?"":name.getValue());
			ini.setKeyValue(section, "Phone",mobileValue == null ? "" : mobileValue);
			ini.setKeyValue(section, "Status", hold.isChecked()? "No" : "Yes");
			ini.setKeyValue(section, "TaskType", taskType.getValue()==null?"":taskType.getValue());
			ini.setKeyValue(section, "Plan", taskPlan.getValue()==null?"":taskPlan.getValue());
			ini.setKeyValue(section, "TemplateType", templateType.getValue()==null?"":templateType.getValue());
			ini.setKeyValue(section, "Template", messageTemplater.getValue()==null?"":messageTemplater.getValue());
			ini.saveChange();

			Session session = this.session;
			session.removeAttribute(MessageConstant.MessageAddSection);
			session.setAttribute(MessageConstant.MessageAddSection, section);
			
			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			String minfo=loginname+" "+"在"+OpObjectId.sms_set.name+"中进行了  "+OpTypeId.add.name+"操作，添加信息项为:"+name.getValue();
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.sms_set);				
			onReFresh();
			
		}catch(Exception e){
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	public void onReFresh() {
		try{
			messageWin.detach();
			String targetUrl = "/main/setting/setmessage.zul";
			eccBody = (Include) (this.desktop.getPage("eccmain").getFellow("eccBody"));
			eccBody.setSrc(null);
			eccBody.setSrc(targetUrl);
		}catch(Exception e){
			
		}

	}	
	
	public Vector<String> getMessageTemplate(String type) {
		Vector<String> keyList = new Vector<String>();

		IniFilePack ini = new IniFilePack("TXTTemplate.ini");
		try{
			ini.load();
		}catch(Exception e){
			e.printStackTrace();
		}
		Map<String, String> keyMap = new HashMap<String,String>();
		if("com".equals(type)){
			keyMap = ini.getM_fmap().get("SMS");
		}else 
		{
			keyMap = ini.getM_fmap().get("WebSmsConfige");
		}
		
		Set<String> keySet = keyMap.keySet();
		Iterator<String> keyIterator = keySet.iterator();
		while (keyIterator.hasNext()) {
			keyList.add(keyIterator.next().toString());
			}
		Object [] temp = keyList.toArray();
		java.util.Arrays.sort(temp);//排序
		keyList = new Vector<String>();
		for(Object s:temp){
			keyList.add(s.toString());
		}
		return keyList;
	}
	
	public void onGetTemplatevalue(Event event) {
		int size = 0;
		if (templateType.getSelectedItem().getValue().equals("com")) {
			size = createTemplateList("com");
			messageTemplateLink.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;" );
			messageTemplateLink.addEventListener(Events.ON_CLICK, new AddLinkFuntion(UrlPropertiesType.SetSms,"msgTemplateSet"));

		} else if (templateType.getSelectedItem().getValue().equals("web")) {
			size = createTemplateList("web");
			messageTemplateLink.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;" );
			messageTemplateLink.addEventListener(Events.ON_CLICK, new AddLinkFuntion(UrlPropertiesType.SetSms,"webMsgTemplateSet"));

		}
		if(size > 0){
			messageTemplater.setSelectedIndex(0);//设置默认的选项
		}
	}
	
	private int createTemplateList(String key) {
		messageTemplater.setText(null);
		messageTemplater.getChildren().clear();
		Vector<String> templateList = getMessageTemplate(key);
		boolean flag = false;
		if(templateList.size()>0){
			flag = true;
		}
		if(!flag){
			try{
				Messagebox.show("信息模板没有设值！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			return 0;
		}
		for(String name:templateList){
			Comboitem comboitem = new Comboitem();
			comboitem.setLabel(name);
			comboitem.setValue(name); 
			messageTemplater.appendChild(comboitem);
		}
		return templateList.size();	
	}
	
	
	public void onGetTaskvalue(Event event) {
		int size = 0;
		if (taskType.getSelectedItem().getValue().equals("1")) {
			size = createNewList("1");
			boolean flag = LinkCheck.getLinkCheck().CanSeeLink(UserRightId.TaskAbsolute);
			taskLink.setStyle("");
			if(flag){
				taskLink.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;" );
				taskLink.addEventListener(Events.ON_CLICK, new AddLinkFuntion(UrlPropertiesType.TaskAbsolute));
			}
		} else if (taskType.getSelectedItem().getValue().equals("2")) {
			size = createNewList("2");
			boolean flag = LinkCheck.getLinkCheck().CanSeeLink(UserRightId.TaskPeriod);
			taskLink.setStyle("");
			if(flag){
				taskLink.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;" );
				taskLink.addEventListener(Events.ON_CLICK, new AddLinkFuntion(UrlPropertiesType.TaskPeriod));
			}
		} else if (taskType.getSelectedItem().getValue().equals("3")) {
			size = createNewList("3");
			boolean flag = LinkCheck.getLinkCheck().CanSeeLink(UserRightId.TaskRelative);
			taskLink.setStyle("");
			if(flag){
				taskLink.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;" );
				taskLink.addEventListener(Events.ON_CLICK, new AddLinkFuntion(UrlPropertiesType.TaskRelative));
			}
		}
		if(size > 0){
			taskPlan.setSelectedIndex(0);//设置默认的选项
		}
	}

	private int createNewList(String taskindex) {
		taskPlan.setText(null);
		taskPlan.getChildren().clear();
		com.siteview.ecc.tasks.TaskPack taskPack = new TaskPack();
		Task[] task = taskPack.findAllByType(taskindex);

		boolean flag = false;
		if(task.length>0){
			flag = true;
		}
		if(!flag){
			try{
				Messagebox.show("任务计划没有设值！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			return 0;
		}
		ArrayList<String> namelist = new ArrayList<String>();
		for (int i = 0; i < task.length; i++) {
			namelist.add(task[i].getName());
		}
		Object [] object = namelist.toArray();
		Arrays.sort(object);
		for(Object name:object){
			Comboitem comboitem = new Comboitem();
			comboitem.setLabel((String)(name));
			comboitem.setValue((String)(name)); 
			taskPlan.appendChild(comboitem);
		}
		return task.length;	
	}
}
