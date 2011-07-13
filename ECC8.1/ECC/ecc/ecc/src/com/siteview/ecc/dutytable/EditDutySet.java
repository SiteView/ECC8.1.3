package com.siteview.ecc.dutytable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Longbox;

import com.siteview.base.manage.View;
import com.siteview.ecc.email.IniFilePack;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.util.Toolkit;

public class EditDutySet extends GenericAutowireComposer {
	
	private Textbox 							oldTableName;
	private Textbox 							newTableName;
	private Textbox 							oldDescription;
	private Textbox 							newDescription;
	private Combobox 							dutyType;
	private Textbox 							alarmEmailbox;
	private Longbox 							mobilePhoneNum;
	private Datebox 							begindata;
	private Datebox 							enddata;
	private Timebox 							beginduty;
	private Timebox 							endduty;	
	private Include 							eccBody;
	private Window 								editDutySetting;
	private String                              edit_dutyset_section = "";
	
	public void onInit()throws Exception{
		try{
			HashMap<String, Object> dutyOldvalue = new HashMap<String, Object>();
			Object editObject = editDutySetting.getAttribute(DutyConstant.Edit_DutySet_Section);
			if(editObject != null){
				this.edit_dutyset_section = (String)editObject;
			}
			IniFilePack ini = new IniFilePack("watchsheetcfg.ini");
			try{
				ini.load();
			}catch(Exception e){}
			oldTableName.setValue(edit_dutyset_section);
			
			String description_str = "";
			try{
				description_str = ini.getM_fmap().get(edit_dutyset_section).get("Description");
			}catch(Exception e){
			}
			oldDescription.setValue(description_str);
			
			String type_str = "";
			try{
				type_str = ini.getM_fmap().get(edit_dutyset_section).get("type");
			}catch(Exception e){
			}
			dutyType.setValue(type_str);
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}	
	
	public void onSaveDutySet(Event event) throws Exception {
		String newTableNameValue 		= newTableName.getValue().trim();
		String newDescriptionValue    	= newDescription.getValue().trim();
		String dutyTypeValue			= dutyType.getValue().trim();
		
		IniFilePack ini=new IniFilePack("watchsheetcfg.ini");

		if ("".equals(newTableNameValue)) {
			Messagebox.show("值班表名称不能够为空串！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			newTableName.setFocus(true);
			return;
		}
		if ("".equals(newDescriptionValue)) {
			Messagebox.show("描述不能够为空串！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			newDescription.setFocus(true);
			return;
		}
		try{
			ini.load();
		}catch(Exception e){}
		
		Map<String, Map<String, String>> dutyList = new HashMap<String, Map<String, String>>();
		dutyList=ini.getM_fmap();
		Iterator<String> keyIterator = dutyList.keySet().iterator();
		String keyValue="";
		while (keyIterator.hasNext()) {
			keyValue=(String) keyIterator.next();
			if(keyValue.equals(newTableNameValue)){
				try{
					Messagebox.show("此值班表名称已经存在，请换一个值班表名称！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				newTableName.setValue(null);
				newTableName.setFocus(true);
				return;
			}
		}	
		String newSection=newTableNameValue;
		try
		{
			ini.createSection(newSection);
			Map<String, String> map=ini.getM_fmap().get(this.edit_dutyset_section);
			Iterator<String> iterator = map.keySet().iterator(); 
			while(iterator.hasNext()){
				String key=iterator.next().toString();
				ini.setKeyValue(newSection, key, ini.getM_fmap().get(this.edit_dutyset_section).get(key));
			}
			ini.setKeyValue(newSection, "Description", newDescription.getValue());
			ini.setKeyValue(newSection, "type",dutyType.getValue() == null ? "" : dutyType.getValue());
			ini.deleteSection(this.edit_dutyset_section);
			ini.saveChange();
			
			Session session=this.session;
			session.setAttribute(DutyConstant.Edit_DutySet_Section, newSection);
			session.setAttribute(DutyConstant.State, "2");//要绑定 state 的值
			
			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			String minfo=loginname+" "+"在"+OpObjectId.duty_set.name+"中进行了  "+OpTypeId.edit.name+"操作，编辑后值班表名为： "+newSection;
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.duty_set);
			
			onRefresh();
		}catch(Exception e){
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	public void onRefresh() throws Exception{
		try{
			String targetUrl = "/main/setting/setmaintain.zul";
			eccBody = (Include) (this.desktop.getPage("eccmain").getFellow("eccBody"));
			eccBody.setSrc(null);
			eccBody.setSrc(targetUrl);
		}catch(Exception e){
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
}
