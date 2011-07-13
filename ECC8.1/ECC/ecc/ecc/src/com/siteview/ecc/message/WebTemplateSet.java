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


public class WebTemplateSet extends GenericAutowireComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Textbox messageTitle, contentTextbox, templateTextbox;
	private Listbox templateList;
	private Button addButton, delButton, refreshButton;
	
	IniFilePack ini = new IniFilePack("TXTTemplate.ini");
	private ArrayList<String> webMessageTemplateList = new ArrayList<String>();

	
	public void onInit() throws Exception {
		try{
			ini.load();
		}catch(Exception e){}
		Map<String, String> m = new HashMap<String, String>();
		m = ini.getM_fmap().get("WebSmsConfige");

		ArrayList<String> table = new ArrayList<String>();
		Set<String> set = m.keySet();
		for (Iterator<String> it = set.iterator(); it.hasNext();) {
			String key = it.next();
			if("WebDefine".equals(key)){
				continue;
			}
			table.add(key);
		}
		Object [] object = table.toArray();
		Arrays.sort(object);

		table = new ArrayList<String>();
		table.add("WebDefine");
		webMessageTemplateList.add("WebDefine");
		for(Object name:object){
			String value = ((String)name).trim();
			table.add(value);
			webMessageTemplateList.add(value);
		}
		ItemRenderer model = new ItemRenderer(table);
		MakelistData(templateList, model, model);
		
		String s="";
		String myKey="";
		for (Iterator<String> it = set.iterator(); it.hasNext();) {
			 myKey = it.next();
			s=ini.getM_fmap().get("WebSmsConfige").get(myKey);
			break;
		}
		String[] a = s.split("\\\\;");
		messageTitle.setValue(a[0]);
		contentTextbox.setValue(a[1]);
	}
	
	public void refresh(){
		ItemRenderer model = new ItemRenderer(webMessageTemplateList);
		MakelistData(templateList, model, model);
		String keyValue = "";
		String contentValue="";
		if(webMessageTemplateList.size()>0){
			keyValue = webMessageTemplateList.get(0);
			try{
				ini.load();
			}catch(Exception e){}
			contentValue = ini.getM_fmap().get("WebSmsConfige").get(keyValue);
			String[] a = contentValue.split("\\\\;");
			messageTitle.setValue(a[0]);
			contentTextbox.setValue(a[1]);
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
	
	public void onAdd(Event event)throws Exception{
		String messageTitleValue 	= 	messageTitle.getValue().trim();
		String contentTextboxValue 	= 	contentTextbox.getValue().trim();
		String templateTextboxValue = 	templateTextbox.getValue().trim();
		
		if(!validateTextbox(messageTitle)){
			Messagebox.show("Web发送模板不能够为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			messageTitle.focus();
			return;
		}
		if(!validateTextbox(contentTextbox)){
			Messagebox.show("短信内容不能够为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			contentTextbox.focus();
			return;
		}
		if(!validateTextbox(templateTextbox)){
			Messagebox.show("模板名称不能够为空，请重新输入！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			templateTextbox.focus();
			return;
		}
		try{
			ini.load();
		}catch(Exception e){}
		try{
			for(String webTemplateName:webMessageTemplateList){
				if(webTemplateName.equals(templateTextboxValue)){
					try{
						Messagebox.show("此模板名已经存在！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					}catch(Exception e){}
					templateTextbox.setFocus(true);
					return;
				}
			}
			
			Map<String, Map<String, String>> map = ini.getM_fmap();
			boolean createFlag = true;
			if(map != null){
				if(map.containsKey("WebSmsConfige")){
					createFlag = false;
				}
			}
			if(createFlag){
				ini.createSection("WebSmsConfige");
			}
			ini.setKeyValue("WebSmsConfige", templateTextboxValue, messageTitleValue+"\\;"+contentTextboxValue);
			ini.saveChange();
				
			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			String minfo=loginname+" "+"在"+OpObjectId.message_template.name+"中进行了  "+OpTypeId.add.name+"Web短信模板操作，添加的Web短信模板为： "+ templateTextbox.getValue();
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.message_template);	
			
			webMessageTemplateList.add(templateTextboxValue);
			refresh();
			templateTextbox.setValue(null);
			}catch(Exception e){
				try{
					Messagebox.show("添加WEB短信模板错误:"+e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
				}catch(Exception e2){}
			}
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
						if("Web".equals(StringSmsSendMode)){//Web方式
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
	
	public void onDel(Event event)  throws Exception{
		Listitem selectedItem = templateList.getSelectedItem();
		if(selectedItem == null){
			try{
				Messagebox.show("请选择要删除的WEB短信模板！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			return;
		}
		String webTemplateName = selectedItem.getId().trim();
		if(templateList.getSelectedItem().getId().equals("WebDefine")){
			try{
				Messagebox.show("系统自定义模板，不能够被删除！" , "提示", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			return;
		}
		try{
			ini.load();
		}catch(Exception e){e.printStackTrace();}
		try{
			ArrayList<String> usingSmsTemplate = getUsingSmsTemplateList();

			for(String s : usingSmsTemplate){
				if( s != null  || !"".equals(s.trim())){
					if( webTemplateName.equals(s)){
						Messagebox.show(" 报警正在使用web短信模板 "+webTemplateName+" ,不能操作，请重选", "提示", Messagebox.OK, Messagebox.INFORMATION);
						return;
					}
				}
			}
			ini.deleteKey("WebSmsConfige", templateList.getSelectedItem().getId());
			ini.saveChange();
			
			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			String minfo=loginname+" "+"在"+OpObjectId.message_template.name+"中进行了  "+OpTypeId.del.name+"Web短信模板操作，删除的Web短信模板为： "+ templateTextbox.getValue();
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del, OpObjectId.message_template);
			
			webMessageTemplateList.remove(webTemplateName);
			refresh();	
			
		}catch(Exception e){
			try{
				Messagebox.show("删除WEB短信模板错误:"+e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
			}catch(Exception e2){}
		}
	}

	public void onEdit(Event event) throws Exception{
		Listitem selectedItem = templateList.getSelectedItem();
		if(selectedItem == null){
			try{
				Messagebox.show("请选择要删除的WEB短信模板！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			return;
		}
		
		if(templateList.getSelectedItem().getId().equals("WebDefine")){
			try{
				Messagebox.show("系统自定义模板，不能够被更新！" , "提示", Messagebox.OK, Messagebox.INFORMATION);
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
			if(ini.getM_fmap().get("WebSmsConfige").get(templateList.getSelectedItem().getId()).contains("\\")){	
			ini.setKeyValue("WebSmsConfige", keyValue, messageTitleValue+"\\;"+contentTextboxValue);
			ini.saveChange();

			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			String minfo=loginname+" "+"在"+OpObjectId.message_template.name+"中进行了  "+OpTypeId.edit.name+"Web短信模板操作，编辑的Web短信模板为： "+templateTextbox.getValue();
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.message_template);	
			try{
				Messagebox.show("更新成功！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			}catch(Exception e){}
			}
		}catch(Exception e){
			try{
				Messagebox.show("更新WEB短信模板错误:"+e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
			}catch(Exception e2){	
			}
		}

		templateTextbox.setValue(null);
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

		public ItemRenderer(List table) {

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
			try{
				ini.load();
				String s = ini.getM_fmap().get("WebSmsConfige").get(((Listitem) event.getTarget()).getId());
				String[] a = s.split("\\\\;");
				messageTitle.setValue(a[0]);
				contentTextbox.setValue(a[1]);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
}
