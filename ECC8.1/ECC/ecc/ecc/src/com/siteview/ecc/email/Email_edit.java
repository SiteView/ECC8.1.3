package com.siteview.ecc.email;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

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

public class Email_edit extends GenericAutowireComposer {

	private static final long 						serialVersionUID = 1L;
	private Textbox 								emailName;
	private Textbox 								emailAddress; 
	private Textbox 								emailDescription;
	private Checkbox 								emailHold;
	private Combobox  								taskPlain;
	private Combobox 								taskType;
	private Combobox 								emailTemplate;
	private Include 								eccBody;
	private Window 									editEmailSetting;
	private Label 									taskLink;
	private Label 									emailTemplateLink;
	private String									section = "";
	
	public void onInit()throws Exception {
		try{
			emailTemplate.getChildren().clear();
			Object sectionObj = editEmailSetting.getAttribute(EmailConstant.EmailEditSection);
			if(sectionObj != null){
				section = (String)sectionObj;
			}
			Map<String, String> map = new HashMap<String, String>();
			IniFilePack ini = new IniFilePack("emailAdress.ini");
			try{
				ini.load();
			}catch(Exception e){}
			
			map = ini.getM_fmap().get(section);
	        if(map == null){
	        	return;
	        }
	        String emailNameValue = (String)map.get("Name");
	        if(emailNameValue == null){
	        	emailNameValue = "";
	        }
			emailName.setValue(emailNameValue);
			
			String emailAddressValue = (String) map.get("MailList");
			if(emailAddressValue == null){
				emailAddressValue = "";
			}
			emailAddress.setValue(emailAddressValue);
			
			String bCheckValue = (String)map.get("bCheck");
			if(bCheckValue == null)
			{
				bCheckValue = "";
			}
			if("1".equals(bCheckValue)){
				emailHold.setChecked(true);
			}else
				emailHold.setChecked(false);
			
			String emailDescriptionValue = (String) map.get("Des");
			if(emailDescriptionValue == null){
				emailDescriptionValue = "";
			}
			emailDescription.setValue(emailDescriptionValue);
			
			String emailTemplateStr = (String) map.get("Template");
			if(emailTemplateStr == null){
				emailTemplateStr = "";
			}
			
			boolean flag = false;
			int size = 0;
			Vector<String> emailTemplateVector= getEmailTemplate();
			
			ArrayList<String> keylist = new ArrayList<String>();
			for (Iterator<String> it = emailTemplateVector.iterator(); it.hasNext();) {
				String key = it.next();
				keylist.add(key);
			}
			Object [] object = keylist.toArray();
			Arrays.sort(object);//排序
			for(Object key:object){
				Comboitem item = new Comboitem();
				item.setValue((String)key);
				item.setLabel((String)key);
				item.setParent(emailTemplate);
				size ++;
				if(key.equals(emailTemplateStr)){
					emailTemplate.setSelectedItem(item);
					flag = true;
				}
			}
			
			if(!flag){
				if(size>0){
					emailTemplate.setSelectedIndex(0);
				}
			}	
			emailTemplateLink.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;");
			emailTemplateLink.addEventListener(Events.ON_CLICK, new AddLinkFuntion(UrlPropertiesType.SetMail,"templateSetBtn"));
			
			String taskStr = ((String)map.get("Schedule"));//任务计划
			if(taskStr == null){
				taskStr = "";
			}
			String taskTypeStr = ((String)map.get("TaskType"));//任务计划类型
			if(taskTypeStr == null){
				taskTypeStr = "";
			}

			if("0".equals(taskTypeStr)){//绝对
				taskType.setSelectedIndex(0);
				createNewList("1",taskStr);
				boolean taskFlag = LinkCheck.getLinkCheck().CanSeeLink(UserRightId.TaskAbsolute);
				taskLink.setStyle("");
				if(taskFlag){
					taskLink.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;" );
					taskLink.addEventListener(Events.ON_CLICK, new AddLinkFuntion(UrlPropertiesType.TaskAbsolute));
				}
			}
			if("1".equals(taskTypeStr))//时间段
			{
				taskType.setSelectedIndex(1);
				createNewList("2",taskStr);
				boolean taskFlag = LinkCheck.getLinkCheck().CanSeeLink(UserRightId.TaskPeriod);
				taskLink.setStyle("");
				if(taskFlag){
					taskLink.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;" );
					taskLink.addEventListener(Events.ON_CLICK, new AddLinkFuntion(UrlPropertiesType.TaskPeriod));
				}
			}
			if("2".equals(taskTypeStr))//相对
			{
				taskType.setSelectedIndex(2);
				createNewList("3",taskStr);
				boolean taskFlag = LinkCheck.getLinkCheck().CanSeeLink(UserRightId.TaskRelative);
				taskLink.setStyle("");
				if(taskFlag){
					taskLink.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;" );
					taskLink.addEventListener(Events.ON_CLICK, new AddLinkFuntion(UrlPropertiesType.TaskRelative));
				}
			}			
		}catch(Exception e){
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
	/**
	 * 创建新的任务计划列表
	 * @param taskindex
	 * @param taskStr
	 */
	private void createNewList(String taskindex,String taskStr) {
		taskPlain.setText(null);
		taskPlain.getChildren().clear();
		com.siteview.ecc.tasks.TaskPack taskPack = new TaskPack();
		Task[] task = taskPack.findAllByType(taskindex);
		boolean flag = false;
		if(task.length>0){
			flag = true;
		}
		if(flag){
			ArrayList<String> namelist = new ArrayList<String>();
			for (int i = 0; i < task.length; i++) {
				namelist.add(task[i].getName());
			}
			Object [] object = namelist.toArray();
			Arrays.sort(object);
			for(Object name:object){
				Comboitem comboitem = new Comboitem();
				String nameValue = (String)name;
				comboitem.setLabel(nameValue);
				comboitem.setValue(nameValue); 
				taskPlain.appendChild(comboitem);
				if(taskStr.equals(nameValue))
				{
					taskPlain.setSelectedItem(comboitem);
				}
			}
		}
	}
	/**
	 * 获得邮件模板列表
	 * @return
	 */
	public Vector<String> getEmailTemplate() {
		Vector<String> keyList = new Vector<String>();
		try {
			IniFilePack ini = new IniFilePack("TXTTemplate.ini");
			try{
				ini.load();
			}catch(Exception e){}
			
			Map<String, String> keyMap = ini.getM_fmap().get("Email");
			if(keyMap == null){
				keyMap = new HashMap<String, String>();
			}
			Set<String> keySet = keyMap.keySet();
			Iterator<String> keyIterator = keySet.iterator();
			while (keyIterator.hasNext()) {
				keyList.add(keyIterator.next().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keyList;
	}

	public void onSave(Event event) throws Exception{
		
		String emailNameValue 			= emailName.getValue().trim();
		String emailAddressValue 		= emailAddress.getValue().trim();
		String emailDescriptionValue 	= emailDescription.getValue().trim();
		String taskPlainValue           = taskPlain.getValue();
		if(taskPlainValue == null){
			taskPlainValue = null;
		}else{
			taskPlainValue = taskPlainValue.trim();
		}

		IniFilePack ini = new IniFilePack("emailAdress.ini");
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

		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(emailAddressValue);
		boolean isMatched = matcher.matches();
		if (!isMatched) {
			Messagebox.show("邮件格式不正确!", "提示", Messagebox.OK, Messagebox.INFORMATION);
			emailAddress.setFocus(true);
			return;
		}
		if("".equals(taskPlainValue)){
			Messagebox.show("任务计划没有设值！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		try{
			ini.load();		
		}catch(Exception e){}
		try{
			ini.setKeyValue(section, "Name", emailName.getValue() == null ? "": emailNameValue);
			ini.setKeyValue(section, "MailList",emailAddress.getValue() == null ? "" : emailAddressValue);
			ini.setKeyValue(section, "Des",emailDescription.getValue() == null ? "" : emailDescriptionValue);
			
			String sectiontemp = section.substring(4);//去掉Item
			ini.setKeyValue(section, "nIndex", sectiontemp);//符合 CS 版本的要求
			// 0 1 2
			int IntValue = Integer.parseInt((String)taskType.getSelectedItem().getValue())-1;
			ini.setKeyValue(section, "TaskType",  String.valueOf(IntValue));
			ini.setKeyValue(section, "bCheck", emailHold.isChecked() ? "1": "0");
			ini.setKeyValue(section, "Template",emailTemplate.getValue() == null ? "" : emailTemplate.getValue());
			ini.setKeyValue(section, "Schedule",taskPlain.getValue() == null ? "" : taskPlain.getValue());
			ini.saveChange();
			
			Session session = Executions.getCurrent().getDesktop().getSession();
			session.removeAttribute(EmailConstant.EmailEditSection);
			session.setAttribute(EmailConstant.EmailEditSection, section);
			
			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			String minfo=loginname+" "+"在"+OpObjectId.mail_set.name+"中进行了  "+OpTypeId.edit.name+"操作，编辑的信息项为： "+emailName.getValue();
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.mail_set);	
			onFresh();
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
		
	}

	public void onFresh() {
		try{
			editEmailSetting.detach();
			String targetUrl = "/main/setting/setmail.zul";
			eccBody = (Include) (this.desktop.getPage("eccmain").getFellow("eccBody"));
			eccBody.setSrc(null);
			eccBody.setSrc(targetUrl);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
/**
 * 任务计划联动
 * @param event
 */
	public void onGetTaskvalue(Event event) {
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
			boolean flag = LinkCheck.getLinkCheck().CanSeeLink(UserRightId.TaskAbsolute);
			taskLink.setStyle("");
			if(flag){
				taskLink.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;" );
				taskLink.addEventListener(Events.ON_CLICK, new AddLinkFuntion(UrlPropertiesType.TaskAbsolute));
			}
		} else if (taskType.getSelectedItem().getValue().equals("3")) {
			size = createNewList("3");
			boolean flag = LinkCheck.getLinkCheck().CanSeeLink(UserRightId.TaskAbsolute);
			taskLink.setStyle("");
			if(flag){
				taskLink.setStyle("color:#18599C;cursor:pointer;text-decoration: underline;" );
				taskLink.addEventListener(Events.ON_CLICK, new AddLinkFuntion(UrlPropertiesType.TaskAbsolute));
			}
		}
		if(size >0 ){
			taskPlain.setSelectedIndex(0);//设置默认的选项
		}
	}
/**
 * 创建任务计划列表
 * @param taskindex
 * @return
 */
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
