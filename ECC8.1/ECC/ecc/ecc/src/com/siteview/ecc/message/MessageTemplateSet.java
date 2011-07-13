package com.siteview.ecc.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import com.siteview.base.manage.View;
import com.siteview.ecc.email.IniFilePack;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.util.Toolkit;


public class MessageTemplateSet extends GenericAutowireComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Textbox messageTitle, contentTextbox, templateTextbox;
	private Listbox templateList;
	private Button addButton, delButton, refreshButton;
	IniFilePack ini = new IniFilePack("TXTTemplate.ini");
	private ArrayList<String> messageTemplateList = new ArrayList<String>();

	public void onInit()throws Exception{
		try{
			ini.load();
		}catch(Exception e){}
		Map<String, String> m = new HashMap<String, String>();
		m = ini.getM_fmap().get("SMS");
		java.util.List<String> table = new ArrayList<String>();
		ArrayList<String> SysList =  new ArrayList<String>();
		ArrayList<String> NonSysList =  new ArrayList<String>();
		
		Set<String> set = m.keySet();
		for (Iterator<String> it = set.iterator(); it.hasNext();) {
			String key = it.next();
			String contentValue = ini.getM_fmap().get("SMS").get(key);
			if(contentValue == null){
				continue;
			}
			if("".equals(contentValue)){
				continue;
			}
			if(contentValue.contains("&")){
				NonSysList.add(key);
			}else{
				SysList.add(key);
			}
		}
		Object [] SysObject = SysList.toArray();
		Arrays.sort(SysObject);
		
		Object [] NonSysObject = NonSysList.toArray();
		Arrays.sort(NonSysObject);
		
		for(Object name:SysObject){
			String value = ((String)name).trim();
			table.add(value);
			messageTemplateList.add(value);
		}
		
		for(Object name:NonSysObject){
			String value = ((String)name).trim();
			table.add(value);
			messageTemplateList.add(value);
		}
		
		ItemRenderer model = new ItemRenderer(table);
		MakelistData(templateList, model, model);

		String keyValue = "";
		String contentValue="";
		if(table.size()>0){
			keyValue = (String)table.get(0);
			contentValue = ini.getM_fmap().get("SMS").get(keyValue);
		}
		if (contentValue.contains("&")) {
			String[] a = contentValue.split("&");
			messageTitle.setValue(a[0]);
			contentTextbox.setValue(a[1]);
		} else{
			messageTitle.setValue(keyValue);
			contentTextbox.setValue(contentValue);
		}
	}
	
	public void refresh(){
		ItemRenderer model = new ItemRenderer(messageTemplateList);
		MakelistData(templateList, model, model);
		String keyValue = "";
		String contentValue="";
		if(messageTemplateList.size()>0){
			keyValue = messageTemplateList.get(0);
			try{
				ini.load();
			}catch(Exception e){}
			contentValue = ini.getM_fmap().get("Email").get(keyValue);
			if (contentValue.contains("&")) {
				String[] a = contentValue.split("&");
				messageTitle.setValue(a[0]);
				contentTextbox.setValue(a[1]);
			} else{
				messageTitle.setValue(keyValue);
				contentTextbox.setValue(contentValue);
			}
		}
	}
	
	public boolean validateTextbox(Textbox textbox){
		String textboxValue = textbox.getValue().trim();
		if("".equals(textboxValue)){
			return false;
		}else
		{
			return true;
		}
	}
	
	public void onAdd(Event event) throws Exception{
		
		String templateTextboxValue 	= 	templateTextbox.getValue().trim();
		String messageTitleValue 		= 	messageTitle.getValue().trim();
		String contentTextboxValue      =   contentTextbox.getValue().trim();
		
		if(!validateTextbox(templateTextbox))
		{
			Messagebox.show("模板名称不能够为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			templateTextbox.setValue(null);
			templateTextbox.setFocus(true);
			return;
		}
		try{
			ini.load();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			for(String templateName:messageTemplateList){
				if(templateName.equals(templateTextboxValue)){
					Messagebox.show("此模板名已经存在！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					templateTextbox.setFocus(true);
					return;
				}
			}

			Map<String, Map<String, String>> map = ini.getM_fmap();
			boolean createFlag = true;
			if(map != null){
				if(map.containsKey("SMS")){
					createFlag = false;
				}
			}
			if(createFlag){
				ini.createSection("SMS");
			}
			
			ini.setKeyValue("SMS", templateTextboxValue, messageTitleValue+ "&" + contentTextboxValue);
			ini.saveChange();
			
			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			String minfo=loginname+" "+"在"+OpObjectId.message_template.name+"中进行了  "+OpTypeId.add.name+"短信模板操作，添加的短信模板为： "+ templateTextbox.getValue();
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.message_template);	
			
			messageTemplateList.add(templateTextboxValue);
			refresh();
			templateTextbox.setValue(null);
			
		}catch(Exception e){
			try{
				Messagebox.show("添加短信模板错误:"+e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
			}catch(Exception e2){}
		}
	}
	
	public void onDel(Event event)  throws Exception{
		Listitem selectedItem = templateList.getSelectedItem();
		if(selectedItem == null){
			try{
				Messagebox.show("请选择要删除的短信模板！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			return;
		}
		String templateName = selectedItem.getId().trim();
		try{
			ini.load();
		}catch(Exception e){e.printStackTrace();}
		try{
			String flag = ini.getM_fmap().get("SMS").get(templateName);
			if(flag.contains("&")){	
				ArrayList<String> usingSmsTemplate = getUsingSmsTemplateList();
				//报警筛选
				for(String s : usingSmsTemplate){
					if( s != null  || !"".equals(s.trim())){
						if( s.equals(templateName)){
							Messagebox.show(" 报警正在使用短信模板 "+templateName+" ,不能操作，请重选", "提示", Messagebox.OK, Messagebox.INFORMATION);
							return;
						}
					}
				}
				ArrayList<String> usingSmsSet = getUsingSmsSetList();
				//短信设置筛选
				for(String s : usingSmsSet){
					if( s != null  || !"".equals(s.trim())){
						if( s.equals(templateName)){
							Messagebox.show(" 短信设置正在使用短信模板 "+templateName+" ,不能操作，请重选", "提示", Messagebox.OK, Messagebox.INFORMATION);
							return;
						}
					}
				}
				
				ini.deleteKey("SMS", templateList.getSelectedItem().getId());
				ini.saveChange();
				View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
				String loginname = view.getLoginName();
				String minfo=loginname+" "+"在"+OpObjectId.message_template.name+"中进行了  "+OpTypeId.del.name+"短信模板操作，删除的短信模为： "+ templateTextbox.getValue();
				AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del, OpObjectId.message_template);		
				
				messageTemplateList.remove(templateName);
				refresh();	

			}else{
				try{
					Messagebox.show("系统自定义模板，不能够被删除！" , "提示", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				return;
			}
		}catch(Exception e){
			try{
				Messagebox.show("删除短信模板错误:"+e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
			}catch(Exception e2){}
		}
	}
	
	public void onEdit(Event event)throws Exception{
		Listitem selectedItem = templateList.getSelectedItem();
		if(selectedItem == null){
			try{
				Messagebox.show("请选择要更新的短信模板！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			return;
		}
		
		String messageTitleValue 	= 	messageTitle.getValue().trim();
		String contentTextboxValue  =	contentTextbox.getValue().trim();
		String keyValue = templateList.getSelectedItem().getId().trim();
		
		try{
			ini.load();
		}catch(Exception e){e.printStackTrace();}
		
		try{
			if(ini.getM_fmap().get("SMS").get(keyValue).contains("&")){
	
				ini.setKeyValue("SMS",keyValue, messageTitleValue+ "&" + contentTextboxValue);
				ini.saveChange();
				
				View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
				String loginname = view.getLoginName();
				String minfo=loginname+" "+"在"+OpObjectId.message_template.name+"中进行了  "+OpTypeId.edit.name+"邮件模板操作，编辑的短信模板为："+templateTextbox.getValue();
				AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.message_template);
				try{
					Messagebox.show("更新成功！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
			}else{
				try{
					Messagebox.show("系统自定义模板，不能够被更新！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				return;
			}
		}catch(Exception e){
			try{
				Messagebox.show("更新短信模板错误:"+e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
			}catch(Exception e2){	
			}
		}
		templateTextbox.setValue(null);
		
	}
	
	public ArrayList<String> getUsingSmsTemplateList()
	{
		IniFilePack ini = new IniFilePack("alert.ini");
		try{
			ini.load();
		}catch(Exception e){}
		ArrayList<String> usingSmsTemplatelist = new ArrayList<String>();
		ArrayList<String> sectionlist = ini.getSectionList();
		for(String s:sectionlist)
		{
			String StringAlertType = ini.getValue(s, "AlertType");
			String StringSmsSendMode = ini.getValue(s, "SmsSendMode");
			if(StringAlertType != null){
				if("SmsAlert".equals(StringAlertType)){//短信
					if(StringSmsSendMode != null){
						if("Com".equals(StringSmsSendMode)){//Com方式
							String StringSmsTemplate =  ini.getValue(s, "SmsTemplate");
							if(StringSmsTemplate != null)
							{
								if(!"".equals(StringSmsTemplate.trim())){
										usingSmsTemplatelist.add(StringSmsTemplate);
								}
							}
						}
					}
				}
			}
		}
		return usingSmsTemplatelist;
	}
	
	public ArrayList<String> getUsingSmsSetList()
	{
		//从短信设置中...
		ArrayList<String> usingSmsSetlist = new ArrayList<String>();
		IniFilePack ini = new IniFilePack("smsphoneset.ini");
		try{
			ini.load();
		}catch(Exception e){}
		ArrayList<String> sectionlist = ini.getSectionList();
		for(String s:sectionlist)
		{
			String StringTaskName =  ini.getValue(s, "Template");//模板名称
			usingSmsSetlist.add(StringTaskName);
		}
		return usingSmsSetlist;
	}

	private void MakelistData(Listbox listb, ListModelList model,
		ListitemRenderer rend) {
		listb.setModel(model);
		listb.setItemRenderer(rend);

	}
	
	private	class ItemRenderer extends ListModelList implements ListitemRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		HashMap<String, String> m = new HashMap<String, String>();

		public ItemRenderer(List<String> table) {

			addAll(table);
		}

		@Override
		public void render(Listitem arg0, Object arg1) throws Exception {
			// TODO Auto-generated method stub
			Listitem item = arg0;
			item.setHeight("20px");
			String t = (String) arg1;
			item.setId(t);
			item.addEventListener("onClick", new SelectListener());
			Listcell l = new Listcell(t);
			l.setParent(item);
			/* l1.setParent(item); */

		}
	}
	
	private	class SelectListener implements org.zkoss.zk.ui.event.EventListener {

		private static final long serialVersionUID = 1L;

		public void onEvent(Event event) throws Exception {

			String keyValue = ((Listitem) event.getTarget()).getId();
			try{
				ini.load();
				String s = ini.getM_fmap().get("SMS").get(keyValue);
				if (s.contains("&")) {
					String[] a = s.split("&");
					messageTitle.setValue(a[0]);
					contentTextbox.setValue(a[1]);
				}else{
					messageTitle.setValue(((Listitem) event.getTarget()).getId());
					contentTextbox.setValue(s);
				}
			}catch(Exception e){e.printStackTrace();}

		}
	}
}
