package com.siteview.ecc.usermanager;

import java.util.ArrayList;

import org.zkoss.zk.ui.Session;
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

import com.siteview.base.manage.View;
import com.siteview.ecc.email.IniFilePack;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.util.LdapAuth;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svdb.UnivData;

public class User_add extends GenericAutowireComposer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4947234240681330921L;
	Textbox loginName;
	Textbox userName;
	Checkbox hold;
	Textbox  pwd;
	Textbox  confirmPwd;
	Textbox  LDAPProviderUrl;
	Textbox LDAPSecurityPrincipal;
	
	Include  eccBody;
	Window addUserSetting;
	
	Checkbox ldapCheck;
	Div passwordDiv;
	Div ldapDiv;
	
	// ��Ҫ�Լ����� user.ini, ������������� ���û�����
	// ʹ�� view.getAllUserEdit(), createUserEdit,deleteUserEdit �Լ�
	// com.siteview.base.data.UserEdit �еķ���-->
	//˵���� view.getAllUserEdit() ����Ĵ����߼��ܶ� ���������ݲ��ܼ�ʱ�ĸ���
	
	public void onCreate(){
		if(pwd!=null) pwd.addEventListener(Events.ON_CTRL_KEY, new CtrlListener1());
		if(confirmPwd!=null) confirmPwd.addEventListener(Events.ON_CTRL_KEY, new CtrlListener2());
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
	
	
	public void onAdd(Event event) throws Exception{
		try {
			String loginName_str 				= loginName.getValue().trim();
			String userName_str  			= userName.getValue().trim();
			String pwd_str       			= pwd.getValue().trim();
			String confirmPwd_str 			= confirmPwd.getValue().trim();
			String LDAPProviderUrl_str			= LDAPProviderUrl.getValue().trim();
			String LDAPSecurityPrincipal_str 	= LDAPSecurityPrincipal.getValue().trim();
			
			if ("".equals(loginName_str)) {
				Messagebox.show("����д��¼����", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				loginName.setFocus(true);
				return;
			}
			if ("".equals(userName_str)) {
				Messagebox.show("����д�û�����", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				userName.setFocus(true);
				return;
			}
			if ("".equals(pwd_str)) {
				Messagebox.show("����д���룡", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				pwd.setFocus(true);
				return;
			}
			if ("".equals(confirmPwd_str)) {
				Messagebox.show("����дȷ�����룡", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				confirmPwd.setFocus(true);
				return;
			}
			if (pwd_str.equals(confirmPwd_str)==false) {
				Messagebox.show("����û�еõ�ȷ�ϣ����������룡", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				confirmPwd.setFocus(true);
				return;
			}
			//��������� ����
			if(ldapCheck.isChecked() ){
				if ("".equals(LDAPProviderUrl_str)) {
					Messagebox.show("����дLDAP����֤��ַ��", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
					LDAPProviderUrl.setFocus(true);
					return;
				}
				if ("".equals(LDAPSecurityPrincipal_str)) {
					Messagebox.show("����дLDAP����֤����", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
					LDAPSecurityPrincipal.setFocus(true);
					return;
				}
			}
			
			View view = Toolkit.getToolkit().getSvdbView(this.desktop);
		    ArrayList <String> loginNamelist 	= new ArrayList<String>();
		    IniFilePack ini = new IniFilePack("user.ini");
			try{
				ini.load();
			}catch(Exception e){}
			for(String section:ini.getSectionList()){
				String loginName1 = ini.getValue(section, "LoginName");
				if(loginName1 != null && !"".equals(loginName1)){
					loginNamelist.add(loginName1);
				}
			}
			for(String loginNameString:loginNamelist){//ֻ�Ǽ���¼���Ƿ��Ѿ�����
				if(loginName_str.equals(loginNameString)){
					Messagebox.show("�˵�¼���Ѿ����ڣ��뻻һ����¼����", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
					loginName.setFocus(true);
					return;
				}
			}
			
			long seed= System.currentTimeMillis() + loginName.hashCode() ;
			java.util.Random r=new java.util.Random(seed);
			Integer newindex= 500000+ Math.abs((loginName.hashCode()%100000)) + Math.abs((r.nextInt()%100000));
			while(ini.getSectionList().contains(newindex.toString())){
				++newindex;
			}
			String section =newindex.toString();
			ini = new IniFilePack("user.ini",section);
			ini.createSection(section);
			ini.setKeyValue(section, "UserName", userName_str);
			ini.setKeyValue(section, "LoginName",loginName_str);
			ini.setKeyValue(section, "nIndex",section);
			ini.setKeyValue(section, "nIsUse", hold.isChecked() ? "0": "1");	
			String password= UnivData.encrypt(pwd_str);
			ini.setKeyValue(section, "Password", password);
			ini.setKeyValue(section, "nIsLdap", ldapCheck.isChecked() ? "1":"0");	
			//1.ldap��֤
			if(ldapCheck.isChecked()){
				try{
					LdapAuth.addLdapUser_onlyAdd(LDAPProviderUrl_str, LDAPSecurityPrincipal_str, ini, section);
				} catch (Exception e)
				{
					e.printStackTrace();
					Messagebox.show(e.toString(), "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
					throw e;
				}
			}

/*			if (LDAPProviderUrl.getValue() != null && !LDAPProviderUrl.getValue().isEmpty()
					&& !LDAPProviderUrl.getValue().trim().equals("")
					&& LDAPSecurityPrincipal.getValue() != null && !LDAPSecurityPrincipal.getValue().isEmpty()
					&& !LDAPSecurityPrincipal.getValue().trim().equals("")) 
			{
				try
				{
					//����������û���  LDAP ��֤��ַ, LDAP ��֤���� ��Ϊ��
					//����û���½ʱ���� ldap ������֤�������� ecc ���е�½��֤��
					LdapAuth.addLdapUser(LDAPProviderUrl.getValue().trim(), LDAPSecurityPrincipal.getValue().trim(), pwd.getValue() == null ? "" : pwd.getValue().trim(), ini, section);
				} catch (Exception e)
				{
					e.printStackTrace();
					Messagebox.show(e.toString(), "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
					throw e;
				}
			}*/
			ini.saveChange();				
			
			Session session = this.session;
			session.setAttribute(UserConstant.ADD_USERID, section);
			String loginname = view.getLoginName();
			String minfo=loginname+" "+"��"+OpObjectId.user_manage.name+"�н�����  "+OpTypeId.add.name+"������"+OpTypeId.add.name+"�� "+userName.getValue();
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.user_manage);	
			
			String targetUrl = "/main/setting/usermanager.zul";
			eccBody = (Include) (this.desktop.getPage("eccmain").getFellow("eccBody"));
			eccBody.setSrc(null);
			eccBody.setSrc(targetUrl);
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"����", Messagebox.OK, Messagebox.ERROR);
		}
	}	
/*	
	public void onAdd(Event event){
		try {
			String loginNameValue = loginName.getValue().trim();
			String userNameValue  = userName.getValue().trim();
			String pwdValue       = pwd.getValue().trim();
			String confirmPwdValue= confirmPwd.getValue().trim();
			
			if ("".equals(loginNameValue)) {
				Messagebox.show("����д��¼����", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				loginName.setFocus(true);
				return;
			}
			if ("".equals(userNameValue)) {
				Messagebox.show("����д�û�����", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				userName.setFocus(true);
				return;
			}
			if ("".equals(pwdValue)) {
				Messagebox.show("����д���룡", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				pwd.setFocus(true);
				return;
			}
			if ("".equals(confirmPwdValue)) {
				Messagebox.show("����дȷ�����룡", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				confirmPwd.setFocus(true);
				return;
			}
			if (pwdValue.equals(confirmPwdValue)==false) {
				Messagebox.show("����û�еõ�ȷ�ϣ����������룡", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
				confirmPwd.setFocus(true);
				return;
			}
			HashMap<String, HashMap<String, String>> userList = new HashMap<String, HashMap<String, String>>();
			View view = Toolkit.getToolkit().getSvdbView(this.desktop);
		    ArrayList <String> userNamelist 	= new ArrayList<String>();
		    ArrayList <String> loginNamelist 	= new ArrayList<String>();
		    IniFilePack ini = new IniFilePack("user.ini");
			try{
				ini.load();
			}catch(Exception e){}
			for(String section:ini.getSectionList()){
				String loginName1 = ini.getValue(section, "LoginName");
				if(loginName1 != null && !"".equals(loginName1)){
					loginNamelist.add(loginName1);
				}
				String userName1 = ini.getValue(section, "UserName");
				if(userName1 != null && !"".equals(userName1)){
					userNamelist.add(userName1);
				}
			}
			for(String loginNameString:loginNamelist){
				if(loginNameValue.equals(loginNameString)){
					Messagebox.show("�˵�¼���Ѿ����ڣ��뻻һ����¼����", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
					loginName.setFocus(true);
					return;
				}
			}
			for(String userNameString:userNamelist){
				if(userNameValue.equals(userNameString)){
					Messagebox.show("���û����Ѿ����ڣ��뻻һ���û�����", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
					userName.setFocus(true);
					return;
				}
			}	
			UserEdit userEdit = view.createUserEdit(loginNameValue);
			
			userEdit.setUserName(userNameValue);
			String password = pwd.getValue().trim();
			userEdit.setPassWord(UnivData.encrypt(password));

			if( hold.isChecked()){
				userEdit.setEnable(false);
			}else{
				userEdit.setEnable(true);
			}

			if (LDAPProviderUrl.getValue() != null && !LDAPProviderUrl.getValue().isEmpty()
					&& !LDAPProviderUrl.getValue().trim().equals("")
					&& LDAPSecurityPrincipal.getValue() != null && !LDAPSecurityPrincipal.getValue().isEmpty()
					&& !LDAPSecurityPrincipal.getValue().trim().equals("")) 
			{
				
				try
				{
					//����������û���  LDAP ��֤��ַ, LDAP ��֤���� ��Ϊ��
					//����û���½ʱ���� ldap ������֤�������� ecc ���е�½��֤��
					LdapAuth.addLdapUser(LDAPProviderUrl.getValue().trim(), 
							LDAPSecurityPrincipal.getValue().trim(), 
							pwd.getValue() == null ? "" : pwd.getValue().trim(), userEdit);
				} catch (Exception e)
				{
					throw e;
				}
			}
			userEdit.saveChange();
			String section = userEdit.getIndexInUserini();
			Session session = this.session;
			session.setAttribute(UserConstant.ADD_USERID, section);
			String loginname = view.getLoginName();
			String minfo=loginname+" "+"��"+OpObjectId.user_manage.name+"�н�����  "+OpTypeId.add.name+"������"+OpTypeId.add.name+"�� "+userName.getValue();
			AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.user_manage);	
			
			
			String targetUrl = "/main/setting/usermanager.zul";
			eccBody = (Include) (this.desktop.getPage("eccmain").getFellow("eccBody"));
			eccBody.setSrc(null);
			eccBody.setSrc(targetUrl);

		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"����", Messagebox.OK, Messagebox.ERROR);
		}
	}
*/
}
