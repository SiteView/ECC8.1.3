/**
 * 
 */
package com.siteview.ecc.email;

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
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.util.Toolkit;

/**
 * @author yuandong
 * 
 */
public class EmailTemplate extends GenericAutowireComposer {
	private static final long 						serialVersionUID = 1L;
	private Listbox 								templateList;
	private Textbox 								nameTextbox;
	private Textbox 								contentTextbox; 
	private Textbox 								templateTextbox;
	private Button 									addButton;
	private Button 									delButton; 
	private Button 									refreshButton;
	private IniFilePack 							ini = new IniFilePack("TXTTemplate.ini");
	
	private ArrayList<String> emailTemplateList = new ArrayList<String>();

	public void onInit()throws Exception{
		try{
			try{
				ini.load();
			}catch(Exception e){}
		    Map<String, String> m = new HashMap<String, String>();
			m = ini.getM_fmap().get("Email");
			ArrayList<String> table = new ArrayList<String>();
			ArrayList<String> SysList =  new ArrayList<String>();
			ArrayList<String> NonSysList =  new ArrayList<String>();
			
			Set<String> set = m.keySet();
			for (Iterator<String> it = set.iterator(); it.hasNext();) {
				String key = it.next();
				String contentValue = ini.getM_fmap().get("Email").get(key);
				if(contentValue == null ){ 
					continue;
				}
				if("".equals(contentValue.trim())) {
					continue;
				}
				if(contentValue.contains("&")){
					NonSysList.add(key);
				}else
				{
					SysList.add(key);
				}
			}
			Object [] SysObject = SysList.toArray();//1
			Arrays.sort(SysObject);
			
			Object [] NonSysObject = NonSysList.toArray();//2
			Arrays.sort(NonSysObject);
			
			for(Object name:SysObject){
				String value = ((String)name).trim();
				table.add(value);
				emailTemplateList.add(value);
			}
			
			for(Object name:NonSysObject){
				String value = ((String)name).trim();
				table.add(value);
				emailTemplateList.add(value);
			}
			
			ItemRenderer model = new ItemRenderer(table);
			MakelistData(templateList, model, model);

			String keyValue = "";
			String contentValue="";
			if(table.size()>0){
				keyValue = (String)table.get(0);
				contentValue = ini.getM_fmap().get("Email").get(keyValue);
			}
			
			if (contentValue.contains("&")) {
				String[] a = contentValue.split("&");
				nameTextbox.setValue(a[0]);
				contentTextbox.setValue(a[1]);
			} else{
				nameTextbox.setValue(keyValue);
				contentTextbox.setValue(contentValue);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	public void refresh()throws Exception{
		try{
			ItemRenderer model = new ItemRenderer(emailTemplateList);
			MakelistData(templateList, model, model);
			String keyValue = "";
			String contentValue="";
			if(emailTemplateList.size()>0){
				keyValue = emailTemplateList.get(0);
				try{
					ini.load();
				}catch(Exception e){}
				contentValue = ini.getM_fmap().get("Email").get(keyValue);
				if (contentValue.contains("&")) {
					String[] a = contentValue.split("&");
					nameTextbox.setValue(a[0]);
					contentTextbox.setValue(a[1]);
				} else{
					nameTextbox.setValue(keyValue);
					contentTextbox.setValue(contentValue);
				}
			}
		}catch(Exception e){e.printStackTrace();}

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
		try{
			String templateTextboxValue = 	templateTextbox.getValue().trim();
			String nameTextboxValue     = 	nameTextbox.getValue().trim();
			String contentTextboxValue  =   contentTextbox.getValue().trim();
			
			if(!validateTextbox(templateTextbox)){
				try{
					Messagebox.show("ģ�����Ʋ��ܹ�Ϊ�գ����������룡", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				templateTextbox.setFocus(true);
				return;
			}
			for(String templateName:emailTemplateList){
				if(templateName.equals(templateTextboxValue)){
					try{
						Messagebox.show("��ģ�����Ѿ����ڣ��뻻һ��ģ������", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
					}catch(Exception e){}
					templateTextbox.setValue(null);
					templateTextbox.setFocus(true);
					return;
				}
			}
			try{
				ini.load();
			}catch(Exception e){}
			Map<String, Map<String, String>> map = ini.getM_fmap();
			boolean createFlag = true;
			if(map != null){
				if(map.containsKey("Email")){
					createFlag = false;
				}
			}
			if(createFlag){
				ini.createSection("Email");
			}
			
			ini.setKeyValue("Email", templateTextboxValue, nameTextboxValue+ "&" + contentTextboxValue);
			ini.saveChange();
			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			String minfo=loginname+" "+"��"+OpObjectId.email_template.name+"�н�����  "+OpTypeId.add.name+"�ʼ�ģ���������ӵ���Ϣ��Ϊ�� "+templateTextbox.getValue();
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.email_template);
			
			emailTemplateList.add(templateTextboxValue);
			refresh();
			templateTextbox.setValue(null);
		}catch(Exception e){
			Messagebox.show(e.getMessage(),"����", Messagebox.OK, Messagebox.ERROR);
		}
	}

	public void onDel(Event event) throws Exception {
		try{
			Listitem selectedItem = templateList.getSelectedItem();
			if(selectedItem == null){
				try{
					Messagebox.show("��ѡ��Ҫɾ�����ʼ�ģ�壡", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				return;
			}
			String templateName = selectedItem.getId().trim();
			try{
				ini.load();
			}catch(Exception e){}
			String flag = ini.getM_fmap().get("Email").get(templateName);
			if(flag.contains("&")){
				EmailUsingCheck emailUsingCheck = new EmailUsingCheck();
				ArrayList<String> usingEmailTemplateAlarm = emailUsingCheck.getUsingEmailTemplateAlarmList();
				ArrayList<String> usingEmailTemplateSet   = emailUsingCheck.getUsingEmailTemplateSetList();
				
				for(String s : usingEmailTemplateAlarm){
					if( s != null  || !"".equals(s.trim())){
						if( templateName.equals(s)){
							Messagebox.show(" ����  ����ʹ���ʼ�ģ�� "+templateName+" ,���ܲ���������ѡ", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
							return;
						}
					}
				}
				for(String s : usingEmailTemplateSet){
					if( s != null  || !"".equals(s.trim())){
						if( templateName.equals(s)){
							Messagebox.show(" �ʼ�����  ����ʹ���ʼ�ģ�� "+templateName+" ,���ܲ���������ѡ", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
							return;
						}
					}
				}
				ini.deleteKey("Email", templateList.getSelectedItem().getId());
				ini.saveChange();
				
				View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
				String loginname = view.getLoginName();
				String minfo=loginname+" "+"��"+OpObjectId.email_template.name+"�н�����  "+OpTypeId.del.name+"�ʼ�ģ�������ɾ������Ϣ��Ϊ�� "+templateName;
				AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del, OpObjectId.email_template);				
				
				emailTemplateList.remove(templateName);
				refresh();	
			}else{
				try{
					Messagebox.show("ϵͳ�Զ���ģ�壬���ܹ���ɾ����", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				return;
			}
		}catch(Exception e){
			Messagebox.show(e.getMessage(),"����", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	public void onEdit(Event event)throws Exception{
		try{
			Listitem selectedItem = templateList.getSelectedItem();
			if(selectedItem == null){
				try{
					Messagebox.show("��ѡ��Ҫ���µ��ʼ�ģ�壡", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				return;
			}
			String nameTextboxValue 	= 	nameTextbox.getValue().trim();
			String contentTextboxValue  =	contentTextbox.getValue().trim();
			
			String keyValue = templateList.getSelectedItem().getId().trim();
			IniFilePack ini = new IniFilePack("TXTTemplate.ini");
			try{
				ini.load();
			}catch(Exception e){}
			
			if(ini.getM_fmap().get("Email").get(keyValue).contains("&")){
		
				ini.setKeyValue("Email",keyValue, nameTextboxValue+ "&" + contentTextboxValue);
				ini.saveChange();

				View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
				String loginname = view.getLoginName();
				String minfo=loginname+" "+"��"+OpObjectId.email_template.name+"�н�����  "+OpTypeId.edit.name+"�ʼ�ģ��������༭����Ϣ��Ϊ�� "+templateTextbox.getValue();
				AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.email_template);		
				try{
					Messagebox.show("���³ɹ���", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
			}else{
				try{
					Messagebox.show("ϵͳ�Զ���ģ�壬���ܹ������£�", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
			}
			templateTextbox.setValue(null);
		}catch(Exception e){}
	}

	private void MakelistData(Listbox listb, ListModelList model,
		ListitemRenderer rend) {
		listb.setModel(model);
		listb.setItemRenderer(rend);

	}

private	class ItemRenderer extends ListModelList implements ListitemRenderer {

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
			String keyValue = ((Listitem) event.getTarget()).getId();
			try{
				ini.load();
			}catch(Exception e){}
			try{
	            String s = ini.getM_fmap().get("Email").get(keyValue);
	            
				if (s.contains("&")) {
					String[] a = s.split("&");
					nameTextbox.setValue(a[0]);
					contentTextbox.setValue(a[1]);
					
				} else{
					nameTextbox.setValue(keyValue);
					contentTextbox.setValue(s);
				}
			}catch(Exception e){e.printStackTrace();}
		}
	}
}