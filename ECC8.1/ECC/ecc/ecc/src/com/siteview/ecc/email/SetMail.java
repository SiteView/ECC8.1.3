package com.siteview.ecc.email;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Include;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.log.UserOperateLogListbox;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svdb.UnivData;

public class SetMail extends GenericAutowireComposer {

	private static final long 				serialVersionUID = 1L;
	private Button 							applyButton;
	private Textbox 						strMailServer;
	private Textbox 						strMailFrom;
	private Textbox 						strBackupServer;
	private Textbox 						strUser;
	private Textbox 						strPassword;

	private String 							mailServer;
	private String 							mailFrom;
	private String 							backupServer;
	private String 							user;
	private String 							password;
	private Listbox 						listbox_data;
	private Include 						eccBody;
	private Window							setMail;
	
	
	public void onInit()throws Exception{
		try{
			//初始化listbox_data
			ArrayList<EmailBean> emailList = new ArrayList<EmailBean>();
			emailList = getAllEmailList();
//			EmailModel model = new EmailModel(emailList);
//			MakelistData(listbox_data, model, model);
			
			EmailModelListbox listbox = (EmailModelListbox)listbox_data;
		  	ChartUtil.clearListbox(listbox);
			Object indexObject = getEditOrAddListitem();
			if(indexObject != null){
				listbox.setIndexObject(indexObject);
			}
		  	listbox.setEmailBeans(emailList);
		  	listbox.onCreate();	
			
			listbox_data.getPagingChild().setMold("os");
			//初始化表格中的数据
			getEmailIniValue();
			strMailServer.setValue(mailServer);
			strMailFrom.setValue(mailFrom);
			strBackupServer.setValue(backupServer);
			strUser.setValue(user);
			String s[] = new String[1];
			s[0] = password;
			try {
				Map<String, Map<String, String>> map = dodecrypt(s);
				Map<String, String> returnMap = map.get("return");
				if(returnMap == null){
					password = "";
				}else{
					password = returnMap.get(s[0]);// 解密
				}
			} catch (Exception e) {}
			strPassword.setValue(password);
			
			//初始时打开设置窗口//超级连接功能增加
			String subMenuId = this.execution.getParameter("subMenuId");
			if(subMenuId!=null && !subMenuId.equals("")){
				Events.sendEvent(new Event(Events.ON_CLICK,BaseTools.getComponentById(setMail, subMenuId)));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 选中编辑 或 增加的 Listitem
	 */
	public Object getEditOrAddListitem(){
		Session session = Executions.getCurrent().getDesktop().getSession();
		//edit
		Object editSectionObj = session.getAttribute(EmailConstant.EmailEditSection);
		Object addSectionObj = session.getAttribute(EmailConstant.EmailAddSection);
		session.removeAttribute(EmailConstant.EmailEditSection);
		session.removeAttribute(EmailConstant.EmailAddSection);
		if(editSectionObj != null){
			return editSectionObj;
		}else if(addSectionObj!= null){
			return addSectionObj;
		}else{
			return null;
		}
	}
	public void selectingListbox(Object sectionObj){
		if(sectionObj != null){
			String section = (String)sectionObj;
			if (listbox_data != null){
				EmailModelListbox listbox = (EmailModelListbox)listbox_data;
				for (int i = 0; i < listbox.getGroupCount(); i++) {
//					EmailBean emailBean = (EmailBean)listbox.getModel().getElementAt(i);
//					if(section.equals(emailBean.getSection())){
//						listbox.setSelectedIndex(i);
//						break;
//					}	
				}
			}
		}
	}
	
/**
 * 	获取所有的 emailList信息
 * @return
 */
	public ArrayList<EmailBean> getAllEmailList(){
		IniFilePack ini = new IniFilePack("emailAdress.ini");
		Map<String, Map<String, String>> emailMap = new HashMap<String, Map<String, String>>();
		ArrayList<EmailBean> emailList = new ArrayList<EmailBean>();
		try {
			ini.load();
		} catch (Exception e) {}
		emailMap = ini.getM_fmap();
		for(String section:ini.getSectionList()){
			EmailBean emailBean = new EmailBean();//Des MailList Name Schedule TaskType Template bCheck nIndex
			if(section == null){continue;}
			emailBean.setSection(section);
			if(emailMap.get(section).get("nIndex") == null){continue;}
			String des 		= emailMap.get(section).get("Des");
			String mailList = emailMap.get(section).get("MailList");
			String name		= emailMap.get(section).get("Name");
			String schedule = emailMap.get(section).get("Schedule");
			String taskType = emailMap.get(section).get("TaskType");
			String template = emailMap.get(section).get("Template");
		    String bcheck   = emailMap.get(section).get("bCheck");
			String nIndex   = emailMap.get(section).get("nIndex");
			if(des == null){des = "";}
			emailBean.setDes(des);
			if(mailList == null){mailList = "";}
			emailBean.setMailList(mailList);
			if(name == null){name = "";}
			emailBean.setName(name);
			if(schedule == null){schedule = "";}
			emailBean.setSchedule(schedule);
			if(taskType == null){taskType = "";}
			emailBean.setTaskType(taskType);
			if(template == null){template = "";}
			emailBean.setTemplate(template);
			if(bcheck == null){bcheck = "";}
			emailBean.setBcheck(bcheck);
			if(nIndex == null){nIndex = "";}
			emailBean.setNIndex(nIndex);
			emailList.add(emailBean);
		}
		Collections.sort(emailList, new Comparator<EmailBean>(){
			@Override
			public int compare(EmailBean o1, EmailBean o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return emailList;
	}
	
	private void MakelistData(Listbox listb, ListModelList model,
			ListitemRenderer rend) {
		listb.getPagingChild().setMold("os");
		listb.setModel(model);
		listb.setItemRenderer(rend);
	}
	
	public void onApply(Event event) throws Exception{
		try{
			mailServer = strMailServer.getValue().trim();
			mailFrom = strMailFrom.getValue().trim();
			backupServer = strBackupServer.getValue().trim();
			user = strUser.getValue().trim();
			password = strPassword.getValue().trim();
			
			if("".equals(mailServer)){
				Messagebox.show("发送服务器SMTP不能为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				strMailServer.setFocus(true);
				return;
			}
			if("".equals(mailFrom)){
				Messagebox.show("发送方Email地址不能为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				strMailFrom.setFocus(true);
				return;
			}
			if("".equals(backupServer)){
				Messagebox.show("备份发送服务器不能为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				strBackupServer.setFocus(true);
				return;
			}
			if("".equals(user)){
				Messagebox.show("身份验证用户名不能为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				strUser.setFocus(true);
				return;
			}
			if("".equals(password)){
				Messagebox.show("身份验证密码不能为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				strPassword.setFocus(true);
				return;
			}
			try {
				password = UnivData.encrypt(password);
			} catch (Exception e) {e.printStackTrace();}
			Pattern regex = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
			Matcher matcher = regex.matcher(mailFrom);
			if (!matcher.matches()) {
				Messagebox.show("请输入正确的Email地址！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				strMailFrom.setFocus(true);
				return;
			}
			IniFilePack ini = new IniFilePack("email.ini");
			try{
				ini.load();
			}catch(Exception e){};
			Map<String, Map<String, String>> map = ini.getM_fmap();
			boolean createFlag = true;
			if(map != null){
				if(map.containsKey("email_config")){
					createFlag = false;
				}
			}
			if(createFlag){
				ini.createSection("email_config");
			}
			ini.setKeyValue("email_config", "server", mailServer);
			ini.setKeyValue("email_config", "from", mailFrom);
			ini.setKeyValue("email_config", "backupserver", backupServer);
			ini.setKeyValue("email_config", "user", user);
			ini.setKeyValue("email_config", "password", password);
			ini.saveChange();
			applyButton.setDisabled(true);
		}catch(Exception e){
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}

	public void getEmailIniValue(){
		try{
			IniFilePack ini = new IniFilePack("email.ini");
			try {
				ini.load();
			}catch(Exception e){}
			mailServer = (ini.getValue("email_config", "server"));
			mailFrom = (ini.getValue("email_config", "from"));
			backupServer = (ini.getValue("email_config", "backupserver"));
			user = (ini.getValue("email_config", "user"));
			password = (ini.getValue("email_config", "password"));

			if(mailServer == null){mailServer = "";}
			if(mailFrom == null){mailFrom = "";}
			if(backupServer == null){backupServer = "";}
			if(user == null){user = "";}
			if(password == null){password = "";}
		}catch(Exception e){}
	}

	public void onMessageRefrash(Event event)throws Exception {
		try{
			UUID uuid = UUID.randomUUID();
			String targetUrl = "/main/setting/setmail.zul" + "?uuid=" + uuid;
			eccBody = (Include) (this.desktop.getPage("eccmain").getFellow("eccBody"));
			eccBody.setSrc(null);
			eccBody.setSrc(targetUrl);
		}catch(Exception e){
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}

	public void reFresh()throws Exception {
		try{
			String targetUrl = "/main/setting/setmail.zul";
			eccBody = (Include) (this.desktop.getPage("eccmain").getFellow("eccBody"));
			eccBody.setSrc(null);
			eccBody.setSrc(targetUrl);
		}catch(Exception e){
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}

	}

	public void onPermiter(Event event)throws Exception {
		try{
			if (listbox_data.getSelectedItems().size() <= 0) {
				try {
					Messagebox.show("您还没有选定列表中的邮件，操作不能够完成！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				} catch (InterruptedException x) {}
				return;
			}
			
			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			
			IniFilePack ini = new IniFilePack("emailAdress.ini");
			
			for (Object it : listbox_data.getSelectedItems()) {
				Listitem la = (Listitem) it;
				String section = la.getId();
				try{
					ini.load();
				}catch(Exception e){}
				String flag = ini.getM_fmap().get(section).get("bCheck");
				String name = ini.getM_fmap().get(section).get("Name");
				if ("1".equals(flag)) {
					ini.setKeyValue(section, "bCheck", "0");
					ini.saveChange();
					String minfo=loginname+" "+"在"+OpObjectId.mail_set.name+"中进行了  "+OpTypeId.enable.name+"操作，允许的信息项为： "+name;
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.enable, OpObjectId.mail_set);	
				}
			}
			reFresh();
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}

	public void onHold(Event event) throws Exception{
		try{
			if (listbox_data.getSelectedItems().size() <= 0) {
				try {
					Messagebox.show("您还没有选定列表中的邮件，操作不能够完成！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				} catch (Exception x) {}
				return;
			}

			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			IniFilePack ini = new IniFilePack("emailAdress.ini");

			for (Object it : listbox_data.getSelectedItems()) {
				Listitem la = (Listitem) it;
				String section = la.getId();
				try{
					ini.load();
				}catch(Exception e){}
				String flag = ini.getM_fmap().get(section).get("bCheck");
				String name = ini.getM_fmap().get(section).get("Name");
				if ("0".equals(flag)) {
					ini.setKeyValue(section, "bCheck", "1");
					ini.saveChange();
					String minfo=loginname+" "+"在"+OpObjectId.mail_set.name+"中进行了  "+OpTypeId.diable.name+"操作，禁止的信息项为： "+name;
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.diable, OpObjectId.mail_set);	
				}
			}
			reFresh();
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
	/**
	 * 被引用的邮件list
	 * @return
	 */
	public ArrayList<String> getUsingEmailList()
	{
		ArrayList<String> emaillist = new ArrayList<String>();
		try{
			IniFilePack ini = new IniFilePack("alert.ini");
			try{
				ini.load();
			}catch(Exception e){}
			ArrayList<String> sectionlist = ini.getSectionList();
			for(String s:sectionlist)
			{
				String StringAlertType = ini.getValue(s, "AlertType");
				if(StringAlertType != null){
					if("EmailAlert".equals(StringAlertType)){
						String StringEmailAddressS =  ini.getValue(s, "EmailAdress");//可能是其他，多个email，或者为空情况
						if(StringEmailAddressS != null)
						{
							if(!"".equals(StringEmailAddressS.trim())){
								if("其他".equals(StringEmailAddressS.trim())){}
								if(StringEmailAddressS.contains(",")){//多个的情况
									String [] temp = StringEmailAddressS.trim().split(",");
									for(String a:temp){
										if(a.contains("(禁止)")) //名称中有 禁止的情况
										{
											String a1 = a.replace("(禁止)", "");
											emaillist.add(a1);
										}else
										{
											emaillist.add(a);
										}
									}
								}else//单个情况
								{
									String a = StringEmailAddressS.trim();
									if(a.contains("(禁止)")) //名称中有 禁止的情况
									{
										String a1 = a.replace("(禁止)", "");
										emaillist.add(a1);
									}else
									{
										emaillist.add(a);
									}
								}
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return emaillist;
	}
	
	
	public void onDel(Event event) throws Exception{
		try{
			if (listbox_data.getSelectedItems().size() <= 0) {
				try {
					Messagebox.show("您还没有选定列表中的邮件，操作不能够完成！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				} catch (Exception x) {}
				return;
			}
			String section = "";
			section = listbox_data.getSelectedItem().getId();
			
			ArrayList<String> usingEmaillist = getUsingEmailList();
			
			int i = 0;
			try{
				i = Messagebox.show("删除将会进行，是否继续?", "询问", Messagebox.OK| Messagebox.CANCEL, Messagebox.QUESTION);
			}catch(Exception e){}
			if (i == Messagebox.CANCEL) {
				return;
			}
			
			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			IniFilePack ini = new IniFilePack("emailAdress.ini");

			
			for (Object it : listbox_data.getSelectedItems()) {
				Listitem la = (Listitem) it;
				section = la.getId();
				try{
					ini.load();
				}catch(Exception e){}
				String name = ini.getM_fmap().get(section).get("Name");
				if(name == null){
					name = "";
				}
				boolean flag = false;
				for(String a:usingEmaillist){
					if(name.equals(a)){
						Messagebox.show("报警正在使用 "+name+"，不能操作，请重选！", "提示", Messagebox.OK, Messagebox.INFORMATION);
						flag = true;
						break;
					}
				}
				if(!flag){
					ini.deleteSection(section);
					ini.saveChange();
					String minfo=loginname+" "+"在"+OpObjectId.mail_set.name+"中进行了  "+OpTypeId.del.name+"操作，删除的信息项为： "+name;
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del, OpObjectId.mail_set);
				}
			}
			reFresh();
		}catch(Exception e){
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}

	public void onEdit(Event event) throws Exception{
		try {
			String section = (String) event.getData();
			this.session.removeAttribute(EmailConstant.EmailEditSection);
			this.session.setAttribute(EmailConstant.EmailEditSection, section);
			final Window win2 = (Window) Executions.createComponents(
					"/main/setting/editEmailSet.zul", null, null);
			win2.doModal();
		} catch (Exception e) {
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}

	public void onRefresh(Event event)throws Exception {
		reFresh();
	}

	public void onTestButton(Event event) throws Exception{
		try{
			final Window win = (Window) Executions.createComponents(
					"/main/setting/testmail.zul", null, null);
			win.setAttribute("mailServer", strMailServer.getValue().trim());
			win.setAttribute("mailFrom", strMailFrom.getValue().trim());
			win.setAttribute("backupServer", strBackupServer.getValue().trim());
			win.setAttribute("user", strUser.getValue().trim());
			win.setAttribute("password", strPassword.getValue().trim());
			win.doModal();
		}catch(Exception e){
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}

	}


	
	public Map<String, Map<String, String>> dodecrypt(String[] x1)
			throws Exception {
		HashMap<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "decrypt");
		for (int i = 0; i <= x1.length - 1; i++)
			ndata.put(x1[i], "");

		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception("Failed to load :" + ret.getEstr());

		return ret.getFmap();
	}
}