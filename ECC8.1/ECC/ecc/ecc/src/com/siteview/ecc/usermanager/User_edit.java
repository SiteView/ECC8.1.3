package com.siteview.ecc.usermanager;

import java.util.Map;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Include;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.siteview.base.manage.Manager;
import com.siteview.base.manage.View;
import com.siteview.ecc.email.IniFilePack;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.util.LdapAuth;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svdb.UnivData;

public class User_edit extends GenericAutowireComposer {
	private Textbox loginName;
	private Textbox userName;
	private Checkbox hold;
	private Textbox  pwd;
	private Textbox  confirmPwd;
	private Textbox  LDAPProviderUrl;
	private Textbox LDAPSecurityPrincipal;
	
	private Include  eccBody;
	private Window editUserSetting;
	private String edit_section = "";
	
	private Checkbox ldapCheck;
	private Div passwordDiv;
	private Div ldapDiv;
	
	public void onInit()throws Exception{
		try {
			edit_section = (String)editUserSetting.getAttribute(UserConstant.EDIT_USERID);
			if(edit_section == null) return;
			if("".equals(edit_section)) return;
			View view = Toolkit.getToolkit().getSvdbView(session);
			if(view.isAdmin()){
				hold.setVisible(true);
			}
			
			IniFilePack ini = new IniFilePack("user.ini");
			try{
				ini.load();
			}catch(Exception e){}
			
			Map<String, String> map = ini.getM_fmap().get(edit_section);
			
			String loginNameValue 			=  map.get("LoginName");
			String userNameValue 			=  map.get("UserName");
			String nIsUseValue 				=  map.get("nIsUse");
			String nIsLdapValue             =  map.get("nIsLdap");
			String LDAPProviderUrlValue 	=  map.get("LDAPProviderUrl");
			String LDAPSecurityPrincipalValue 	=  map.get("LDAPSecurityPrincipal");
			if(loginNameValue == null) loginNameValue = "";
			if(userNameValue == null) userNameValue = "";
			if(nIsUseValue == null) nIsUseValue = "0";
			if(nIsLdapValue == null) nIsLdapValue = "0";
			if(LDAPProviderUrlValue == null) LDAPProviderUrlValue = "";
			if(LDAPSecurityPrincipalValue == null) LDAPSecurityPrincipalValue = "";
			
			loginName.setValue(loginNameValue);
			userName.setValue(userNameValue);
			if("1".equals(nIsUseValue)){
				hold.setChecked(false);
			}else{
				hold.setChecked(true);
			}
			if("1".equals(nIsLdapValue)){
				ldapCheck.setChecked(true);
				ldapDiv.setVisible(true);
			}else{
				ldapCheck.setChecked(false);
				ldapDiv.setVisible(false);
			}
			LDAPProviderUrl.setValue(LDAPProviderUrlValue);
			LDAPSecurityPrincipal.setValue(LDAPSecurityPrincipalValue);
				
			if(pwd!=null) pwd.addEventListener(Events.ON_CTRL_KEY, new CtrlListener1());
			if(confirmPwd!=null) confirmPwd.addEventListener(Events.ON_CTRL_KEY, new CtrlListener2());
			
		}catch(Exception e)
		{
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}

	class CtrlListener1 implements EventListener{
		@Override
		public void onEvent(Event event) throws Exception {
			pwd.setValue("");
		}
	}	
	class CtrlListener2 implements EventListener{
		@Override
		public void onEvent(Event event) throws Exception {
			confirmPwd.setValue("");
		}
		
	}
	public void onSave(Event event)throws Exception{
		try {
			String loginName_str 				=  loginName.getValue().trim();
			String userName_str 				=  userName.getValue().trim();
			String pwd_str              	  	=  pwd.getValue().trim();
			String confirmPwd_str         		=  confirmPwd.getValue().trim();
			String LDAPProviderUrl_str			= LDAPProviderUrl.getValue().trim();
			String LDAPSecurityPrincipal_str 	= LDAPSecurityPrincipal.getValue().trim();
			if ("".equals(loginName_str)) {
				Messagebox.show("请填写登录名！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				loginName.setFocus(true);
				return;
			}
			if ("".equals(userName_str)) {
				Messagebox.show("请填写用户名！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				userName.setFocus(true);
				return;
			}
			if ("".equals(pwd_str)) {
				Messagebox.show("请填写密码！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				pwd.setFocus(true);
				return;
			}
			if ("".equals(confirmPwd_str)) {
				Messagebox.show("请填写确认密码！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				confirmPwd.setFocus(true);
				return;
			}
			if (confirmPwd_str.equals(pwd_str)==false) {
				Messagebox.show("确认密码与密码不符，请重新输入！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				confirmPwd.setFocus(true);
				return;
			}
			//分两种情况 处理
			if(ldapCheck.isChecked() ){
				if ("".equals(LDAPProviderUrl_str)) {
					Messagebox.show("请填写LDAP的认证地址！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					LDAPProviderUrl.setFocus(true);
					return;
				}
				if ("".equals(LDAPSecurityPrincipal_str)) {
					Messagebox.show("请填写LDAP的认证规则！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					LDAPSecurityPrincipal.setFocus(true);
					return;
				}
			}
			View view = Toolkit.getToolkit().getSvdbView(session);
			IniFilePack ini = new IniFilePack("user.ini");
			try{
				ini.load();
			}catch(Exception e){}
			if("".equals(edit_section)){
				throw new Exception("can get this user's nIndex!");
			}

			
			
			ini.setKeyValue(edit_section, "LoginName",loginName_str);
			ini.setKeyValue(edit_section, "UserName", userName_str);
			ini.setKeyValue(edit_section, "nIsUse", hold.isChecked() ? "0": "1");
			String password= UnivData.encrypt(pwd_str);
			ini.setKeyValue(edit_section, "Password", password);
			ini.setKeyValue(edit_section, "nIsLdap", ldapCheck.isChecked() ? "1":"0");	
			//1.ldap认证
			if(ldapCheck.isChecked()){
				try{
					LdapAuth.addLdapUser_onlyAdd(LDAPProviderUrl_str, LDAPSecurityPrincipal_str, ini, edit_section);
				} catch (Exception e){
					e.printStackTrace();
					Messagebox.show(e.toString(), "提示", Messagebox.OK, Messagebox.INFORMATION);
					throw e;
				}
			}else{
				ini.setKeyValue(edit_section, "LDAPProviderUrl", "");
				ini.setKeyValue(edit_section, "LDAPSecurityPrincipal", "");
			}
			
			ini.saveChange();
			Manager.instantUpdate();
			
			String loginname = view.getLoginName();
			String minfo=loginname+" "+"在"+OpObjectId.user_manage.name+"中进行了  "+OpTypeId.edit.name+"操作，"+OpTypeId.edit.name+"了 "+userName.getValue();
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.user_manage);	
			
			session.setAttribute(UserConstant.ADD_USERID,edit_section);
			
			String targetUrl = "/main/setting/usermanager.zul";
			eccBody = (Include) (this.desktop.getPage("eccmain").getFellow("eccBody"));
			eccBody.setSrc(null);
			eccBody.setSrc(targetUrl);

		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
			
		}
	}

}
