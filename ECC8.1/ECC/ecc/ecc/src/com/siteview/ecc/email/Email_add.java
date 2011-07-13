package com.siteview.ecc.email;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.zkoss.zul.Label;

import com.siteview.base.data.UserRightId;
import com.siteview.base.manage.View;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.report.common.AddLinkFuntion;
import com.siteview.ecc.report.common.UrlPropertiesType;
import com.siteview.ecc.tasks.Task;
import com.siteview.ecc.tasks.TaskPack;
import com.siteview.ecc.util.LinkCheck;
import com.siteview.ecc.util.Toolkit;

public class Email_add extends GenericAutowireComposer {

	private static final long 					serialVersionUID = 1L;
	private Textbox 							emailName;
	private Textbox 							emailAddress;
	private Textbox 							emailDescription;
	private Checkbox 							emailHold;
	private Combobox  							taskPlain;
	private Combobox  							taskType;
	private Combobox  							emailTemplate;
	private Include 							eccBody;
	private Window 								addEmailSetting;
	private Label 								taskLink;
	private Label 								emailTemplateLink;

	public void onInit()throws Exception{
		try{
			emailTemplate.getChildren().clear();
			Vector <String>emailTemplateVector= getEmailTemplate();
				
			int length = 0;
			for (Iterator<String> it = emailTemplateVector.iterator(); it.hasNext();) {
				String key = it.next();
				Comboitem item = new Comboitem();
				item.setValue(key);
				item.setLabel(key);
				item.setParent(emailTemplate);
				length ++;
			}
			if(length >0){
				emailTemplate.setSelectedIndex(0);
			}
			taskType.setSelectedIndex(1);//时间段的任务计划
			int size = createNewList("2");//选中 “时间段任务计划 ” 的初始化
			if(size > 0){
				taskPlain.setSelectedIndex(0);
			}
			//邮件模板链接
			emailTemplateLink.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;");
			emailTemplateLink.addEventListener(Events.ON_CLICK, new AddLinkFuntion(UrlPropertiesType.SetMail,"templateSetBtn"));
			
			//任务计划链接
			boolean flag = LinkCheck.getLinkCheck().CanSeeLink(UserRightId.TaskPeriod);
			taskLink.setStyle("");
			if(flag){
				taskLink.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;" );
				taskLink.addEventListener(Events.ON_CLICK, new AddLinkFuntion(UrlPropertiesType.TaskPeriod));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	
	public Vector<String> getEmailTemplate() {
		Vector<String> keyList = new Vector<String>();
		try{
			IniFilePack ini = new IniFilePack("TXTTemplate.ini");
			try{
				ini.load();
			}catch(Exception e){}
			
			Map<String,String> keyMap = ini.getM_fmap().get("Email");
			if(keyMap == null){
				keyMap = new HashMap<String,String>();
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
				if("".equals(s.toString())){
					continue;
				}
				keyList.add(s.toString());
			}
		}catch(Exception e){}
		return keyList;
	}

	public void onEmailAdd(Event event)throws Exception {
		try{
			String emailNameValue 			= emailName.getValue().trim();
			String emailAddressValue 		= emailAddress.getValue().trim();
			String emailDescriptionValue 	= emailDescription.getValue().trim();
			String taskPlainValue           = taskPlain.getValue();
			String emailTemplateValue       = emailTemplate.getValue();
			if(taskPlainValue == null){
				taskPlainValue = "";
			}
			if(emailTemplateValue == null){
				emailTemplateValue = "";
			}
			if ("".equals(emailNameValue)) {
				Messagebox.show("请填写名称!", "提示", Messagebox.OK, Messagebox.INFORMATION);
				emailName.setFocus(true);
				return;
			}
			if ("".equals(emailAddressValue)) {
				Messagebox.show("请填写Email地址!", "提示", Messagebox.OK, Messagebox.INFORMATION);
				emailAddress.setFocus(true);
				return;
			}
			IniFilePack ini = new IniFilePack("emailAdress.ini");
			try{
				ini.load();
			}catch(Exception e){}
			
			int num=ini.getM_fmap().size();
			Map<String, Map<String, String>> emailList = ini.getM_fmap();
			Set<String> keySet=emailList.keySet();
			Iterator<String> keyIterator = keySet.iterator();
			String key="";
			String[] emailNameList=new String[num];
			int i=0;
			while (keyIterator.hasNext()) {
				key=(String) keyIterator.next();
				Map<String, String> mapValue=emailList.get(key);
				String tempValue = "";
				if(mapValue.get("Name") == null){
					tempValue = "";
				}else{
					tempValue = (String)mapValue.get("Name");
				}
				emailNameList[i++]=tempValue;
			}
		
			for(int m=0;m<emailNameList.length;m++){
				if(emailNameList[m].trim().equals(emailNameValue)){
					Messagebox.show("此名称已经存在！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					emailName.setFocus(true);
					return;
				}
			}
		
			String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
			Pattern regex = Pattern.compile(check);
			Matcher matcher = regex.matcher(emailAddressValue);
			boolean isMatched = matcher.matches();
			if (!isMatched) {
				Messagebox.show("邮件格式不正确！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				emailAddress.setFocus(true);
				return ;
			}
			
			if("".equals(taskPlainValue)){
				Messagebox.show("任务计划没有设值！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				return ;
			}
			
			UUID uuid = UUID.randomUUID();
			String temp = uuid.toString();
			String section = "Item" + temp;

			ini.createSection(section);
			ini.setKeyValue(section, "Name", emailNameValue);
			ini.setKeyValue(section, "MailList",emailAddressValue);
			ini.setKeyValue(section, "Des",emailDescriptionValue);
			ini.setKeyValue(section, "nIndex", temp);//符合 CS 版本的要求
			// 0 1 2
			int IntValue = Integer.parseInt((String)taskType.getSelectedItem().getValue())-1;
			ini.setKeyValue(section, "TaskType",  String.valueOf(IntValue));
			ini.setKeyValue(section, "bCheck", emailHold.isChecked() ? "1": "0");
			ini.setKeyValue(section, "Template",emailTemplateValue);
			ini.setKeyValue(section, "Schedule",taskPlainValue);
			ini.saveChange();
			
			Session session = this.session;
			session.setAttribute(EmailConstant.EmailAddSection, section);

			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			String minfo=loginname+" "+"在"+OpObjectId.mail_set.name+"中进行了  "+OpTypeId.add.name+"操作，添加的信息项为： "+emailName.getValue();
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.mail_set);	
			onFresh(event);
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}

	public void onFresh(Event event)throws Exception {
		try{
			addEmailSetting.detach();
			String targetUrl = "/main/setting/setmail.zul";
			eccBody = (Include) (this.desktop.getPage("eccmain")
					.getFellow("eccBody"));
			eccBody.setSrc(null);
			eccBody.setSrc(targetUrl);
		}catch(Exception e){}
	}
/*
 * 任务计划的联动
 * */
	public void onGetTaskvalue(Event event) throws Exception{
		try{
			int size =0;
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
			if(size >0 ){
				taskPlain.setSelectedIndex(0);//设置默认的选项
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
/*
 * 根据类型创建任务计划列表
 * */
	private int createNewList(String taskindex) {
		taskPlain.setText(null);
		taskPlain.getChildren().clear();
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
			taskPlain.appendChild(comboitem);
		}
		return task.length;	
	}
	
}
