package com.siteview.ecc.userRole;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;
import com.siteview.ecc.email.IniFilePack;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.usermanager.UserConstant;
import com.siteview.ecc.util.LdapAuth;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svdb.UnivData;

public class GrantEdit extends GenericAutowireComposer{
	private Label ruleAddres;
	Listbox listbox_data;
	private Combobox ruleaddressOption;
	Window setgrant;
	Textbox areaAddress;
	Textbox entityNumber;
	Textbox monitorNumber;
	Checkbox manage;
	Checkbox manager;
	public void onInit()throws Exception{
		System.out.println("实现授权");
		listbox_data=(Listbox) setgrant.getAttribute("listbox_data");
		System.out.println(listbox_data.getSelectedItems().size());
	}
	public void onSave(Event event)throws Exception{
		String ruleAddressValue=ruleaddressOption.getValue();
		String sectionrule=saveRule();
		if(listbox_data instanceof Listbox){
			for(Object data:((Listbox) listbox_data).getSelectedItems()){
				Listitem la=(Listitem)data;
				String section=la.getId();
				IniFilePack ini=new IniFilePack("user.ini");
				ini.load();
				saveUpdate(sectionrule,section,ini,ruleAddressValue);
			}
		}
		
		System.out.println("保存");
	}
	/*
	 * 保存角色信息
	 */
	
	private String saveRule() throws Exception {
		String areaAddress_str 				= ruleaddressOption.getValue().trim();
		String entityNumber_str  			= entityNumber.getValue().trim();
		String monitorNumber_str       			= monitorNumber.getValue().trim();
		String mannager_str;
		if(manage.isChecked()){
			mannager_str="manage=1";
		}else {
			mannager_str="manage=0";
		}
		if(manager.isChecked()){
			mannager_str=mannager_str+"mannager=1";
		}else {
			mannager_str=mannager_str+"mannager=0";
		}
		Window addRole;
	    IniFilePack ini = new IniFilePack("role.ini");
		try{
			ini.load();
		}catch(Exception e){}
		long seed= System.currentTimeMillis() + areaAddress_str.hashCode() ;
		java.util.Random r=new java.util.Random(seed);
		Integer newindex= 500000+ Math.abs((areaAddress_str.hashCode()%100000)) + Math.abs((r.nextInt()%100000));
		while(ini.getSectionList().contains(newindex.toString())){
			++newindex;
		}
		String section =newindex.toString();
		ini = new IniFilePack("role.ini",section);
		ini.createSection(section);
		ini.setKeyValue(section, "areaAddress", ruleaddressOption.getValue());
		ini.setKeyValue(section, "entityNumber",entityNumber_str);
		ini.setKeyValue(section, "nIndex",section);
		ini.setKeyValue(section, "monitorNumber", monitorNumber_str);
		ini.setKeyValue(section, "mannager", mannager_str);
		ini.saveChange();
		return section;
	}
	private void saveUpdate(String sectionrule,String section,IniFilePack ini2, String ruleAddressValue) {

		try {
			String loginName_str 				=  ini2.getValue(section, "LoginName");
			String userName_str 				=  ini2.getValue(section, "UserName");
			String pwd_str              	  	=  ini2.getValue(section, "Password");
			
			IniFilePack ini = new IniFilePack("user.ini");
			try{
				ini.load();
			}catch(Exception e){}
			if("".equals("EDIT_USERID")){
				throw new Exception("can get this user's nIndex!");
			}

			
			if(loginName_str!=null)
				ini.setKeyValue(section, "LoginName",loginName_str);
			if(userName_str!=null)
				ini.setKeyValue(section, "UserName", userName_str);
			if(userName_str!=null)
				ini.setKeyValue(section, "nIsUse", ini2.getValue(section, "nIsUse"));
			if(userName_str!=null){
				String password= UnivData.encrypt(pwd_str);
				ini.setKeyValue(section, "Password", password);
			}
			String nIsLdap=ini2.getValue(section, "nIsLdap");
			if(userName_str!=null)
				ini.setKeyValue(section, "nIsLdap", nIsLdap);	
			String LDAPProviderUrl=ini2.getValue(section, "LDAPProviderUrl");
			if(LDAPProviderUrl!=null)
				ini.setKeyValue(section, "LDAPProviderUrl",ini2.getValue(section, "LDAPProviderUrl"));
			String LDAPSecurityPrincipal=ini2.getValue(section, "LDAPSecurityPrincipal");
			if(LDAPSecurityPrincipal!=null)
				ini.setKeyValue(section, "LDAPSecurityPrincipal", LDAPSecurityPrincipal);
			ini.setKeyValue(section, "rulesection", sectionrule);
			ini.setKeyValue(section, "1", "value=1,editgroup=1,delgroup=1,addsongroup=1,grouprefresh=1,sort=1,value=1,adddevice=1,editdevice=1,deldevice=1,copydevice=1,devicerefresh=1,sort=1,");
			ini.setKeyValue(section, "1.32", "value=1,editgroup=1,delgroup=1,addsongroup=1,grouprefresh=1,sort=1,value=1,adddevice=1,editdevice=1,deldevice=1,copydevice=1,devicerefresh=1,sort=1,");
			ini.saveChange();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
}
