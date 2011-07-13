/**
 * @author yuandong
 */
package com.siteview.ecc.message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Include;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInMap;
import com.siteview.base.manage.View;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.email.EmailConstant;
import com.siteview.ecc.email.EmailModelListbox;
import com.siteview.ecc.email.IniFilePack;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.report.common.ChartUtil;
import com.siteview.ecc.util.Toolkit;
import com.siteview.svdb.UnivData;

public class SetMessage extends GenericAutowireComposer
{
	
	private static final long 					serialVersionUID = 1L;
	private Textbox								userName;
	private Textbox								pwd;
	private Intbox								messageLength;
	private Intbox								webMessageLength;
	
	private Button								applyButton;
	private Button								applyButton2;
	private Button								applyButton3;
	private Button								recoverButton;
	private Button								recoverButton2;
	private Button								recoverButton3;
	private Button								dllTestButton;

	
	private Listbox								listbox_data;
	private Combobox							comNum;
	private Include								eccBody;
	private String								strMsg;
	private Combobox							libName;
	private Textbox								libPara;
	
	private Window								setMassage;
	
	public void onInit() throws Exception
	{
		try{
			//初始化listbox_data
			ArrayList<MessageBean> messageList = new ArrayList<MessageBean>();
			messageList = getAllMessageList();
//			MessageModel model = new MessageModel(messageList);
//			MakelistData(listbox_data, model, model);

			MessageModelListbox listbox = (MessageModelListbox)listbox_data;
		  	ChartUtil.clearListbox(listbox);
			Object indexObject = getEditOrAddListitem();
			if(indexObject != null){
				listbox.setIndexObject(indexObject);
			}
		  	listbox.setMessageBeans(messageList);
		  	listbox.onCreate();	
		  	listbox_data.getPagingChild().setMold("os");
			getDataInit();
			String subMenuId = this.execution.getParameter("subMenuId");
			if(subMenuId!=null && !subMenuId.equals("")){
				Events.sendEvent(new Event(Events.ON_CLICK,BaseTools.getComponentById(setMassage, subMenuId)));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 	获取所有的 messageList信息
	 * @return
	 */
	public ArrayList<MessageBean> getAllMessageList(){
		IniFilePack ini = new IniFilePack("smsphoneset.ini");
		Map<String, Map<String, String>> messageMap = new HashMap<String, Map<String, String>>();
		ArrayList<MessageBean> messageList = new ArrayList<MessageBean>();
		try {
			ini.load();
		} catch (Exception e) {}
		messageMap = ini.getM_fmap();
		for(String section:ini.getSectionList()){
			MessageBean messageBean = new MessageBean();//Name Phone Plan Status TaskType Template TemplateType
			if(section == null){continue;}
			messageBean.setSection(section);
			if(messageMap.get(section).get("Name") == null){continue;}
			String name 			= messageMap.get(section).get("Name");
			String phone   			= messageMap.get(section).get("Phone");
			String plan				= messageMap.get(section).get("Plan");
			String status 			= messageMap.get(section).get("Status");
			String taskType 		= messageMap.get(section).get("TaskType");
			String template 		= messageMap.get(section).get("Template");
		    String templateType   	= messageMap.get(section).get("TemplateType");
			if(name == null){name = "";}
			messageBean.setName(name);
			if(phone == null){phone = "";}
			messageBean.setPhone(phone);
			if(plan == null){plan = "";}
			messageBean.setPlan(plan);
			if(status == null){status = "";}
			messageBean.setStatus(status);
			if(taskType == null){taskType = "";}
			messageBean.setTaskType(taskType);
			if(template == null){template = "";}
			messageBean.setTemplate(template);
			if(templateType == null){templateType = "";}
			messageBean.setTemplateType(templateType);
			messageList.add(messageBean);
		}
		Collections.sort(messageList, new Comparator<MessageBean>(){
			@Override
			public int compare(MessageBean o1, MessageBean o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		return messageList;
	}
	public Object getEditOrAddListitem(){
		Session session = Executions.getCurrent().getDesktop().getSession();
		Object editSectionObj = session.getAttribute(MessageConstant.MessageEditSection);
		Object addSectionObj = session.getAttribute(MessageConstant.MessageAddSection);
		session.removeAttribute(MessageConstant.MessageEditSection);
		session.removeAttribute(MessageConstant.MessageAddSection);
		if(editSectionObj != null){
			return editSectionObj;
		}else if(addSectionObj!= null){
			return addSectionObj;
		}else{
			return null;
		}
	}
	
	
	
	private void MakelistData(Listbox listb, ListModelList model,
			ListitemRenderer rend) {
		listb.getPagingChild().setMold("os");
		listb.setModel(model);
		listb.setItemRenderer(rend);
	}
	public Map<String, Map<String, String>> getSmsNumIni(String inifilename)
	{
		Map<String, Map<String, String>> smsNum = new HashMap<String, Map<String, String>>();
		IniFilePack ini = new IniFilePack(inifilename);
		try{
			ini.load();
		} catch (Exception e){}
		smsNum = ini.getM_fmap();
		return smsNum;
	}
	
	public Map<String, Map<String, String>> getMessageLinkedValue(){
		
		IniFilePack ini = new IniFilePack("smsphoneset.ini");
		Map<String, Map<String, String>> messageSet = new HashMap<String, Map<String, String>> ();
		try {
			ini.load();
		}catch(Exception e){}
		try{
			messageSet = ini.getM_fmap();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, Map<String, String>> messageSetLinkedList = new LinkedHashMap<String, Map<String, String>>();
		Map<String,String> 	nameAndKey 		= new HashMap<String,String>();
		List<String> 		messageNameList 	= new ArrayList<String>();
		
		for(String section:ini.getSectionList()){
			String messageName = ini.getValue(section, "Name");
			if(messageName == null){
				continue;
			}
			if("".equals(messageName.trim())){
				continue;
			}
			nameAndKey.put(messageName, section);
			messageNameList.add(messageName);
		}
		Collections.sort(messageNameList);
		
	    for(String messageName:messageNameList){
	    	String sectionValue = nameAndKey.get(messageName);
	    	Map<String,String> map = messageSet.get(sectionValue);
	    	messageSetLinkedList.put(sectionValue, map);
	    }
	    return messageSetLinkedList;
	}	
	
	public void onApply(Event event)throws Exception
	{
		try{
			String strUser = userName.getValue().trim();
			String strPwd = pwd.getValue().trim();
			if ("".equals(strUser)){
				Messagebox.show("带 * 号的是必填项！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				userName.setFocus(true);
				return;
			}
			if ("".equals(strPwd)){
				Messagebox.show("带 * 号的是必填项！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				pwd.setFocus(true);
				return;
			}
			if (webMessageLength.getValue() != null){
				strMsg = webMessageLength.getValue().toString();
				if ("".equals(strMsg) || Integer.parseInt(strMsg) > 70 || Integer.parseInt(strMsg) < 1)
				{
					Messagebox.show("请输入长度大于0并且小于等于70的信息！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					webMessageLength.setFocus(true);
					return;
				}
			} else{
				Messagebox.show("带 * 号的是必填项！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				webMessageLength.setFocus(true);
				return;
			}
			try {
				strPwd = UnivData.encrypt(strPwd);// 加密存储
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			IniFilePack ini = new IniFilePack("smsconfig.ini");
			try{
				ini.load();
			}catch(Exception e){}
			Map<String, Map<String, String>> map = ini.getM_fmap();
			boolean createFlag = true;
			if(map != null){
				if(map.containsKey("SMSWebConfig")){
					createFlag = false;
				}
			}
			if(createFlag){
				ini.createSection("SMSWebConfig");
			}
			ini.setKeyValue("SMSWebConfig", "User", strUser);
			ini.setKeyValue("SMSWebConfig", "Pwd", strPwd);
			ini.setKeyValue("SMSWebConfig", "Length", strMsg);
			ini.saveChange();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//程序初始化的时候很多的 ini 为 null key不存在
	private void getDataInit() throws Exception
	{		
		try{
			IniFilePack smsconfigIni = new IniFilePack("smsconfig.ini");
			IniFilePack interfacedllIni = new IniFilePack("interfacedll.ini");
			try{
				smsconfigIni.load();
			} catch (Exception e){}
			try{
				interfacedllIni.load();
			} catch (Exception e){}

			String UserNameValue = smsconfigIni.getValue("SMSWebConfig", "User");
			String pwdValue 	 = smsconfigIni.getValue("SMSWebConfig", "Pwd");
			String webLengthValue = smsconfigIni.getValue("SMSWebConfig", "Length");
			String commLengthValue = smsconfigIni.getValue("SMSCommConfig", "length");
			String portValue     = smsconfigIni.getValue("SMSCommConfig", "Port");
			
			String libNameValue = interfacedllIni.getValue("DLL", "DllName");
			String libParaValue = interfacedllIni.getValue("DLL", "DllFunParam");
			
			if(UserNameValue == null){UserNameValue = "";}
			if(pwdValue == null){pwdValue = "";}
			if(webLengthValue == null){webLengthValue = "";}
			if(commLengthValue == null){commLengthValue = "";}
			if(portValue == null){portValue = "";}
			
			if(libNameValue == null){libNameValue = "";}
			if(libParaValue == null){libParaValue = "";}

			userName.setValue(UserNameValue);
			String s[] = new String[1];
			s[0] = pwdValue;
			try {
				pwdValue = dodecrypt(s).get("return").get(s[0]);
			} catch (Exception e1) {
			}
			pwd.setValue(pwdValue);
			int a = 0;
			try{
				a = Integer.parseInt(webLengthValue);
			}catch(Exception e){}
			webMessageLength.setValue(a);

			int b = 0;
			try{
				b = Integer.parseInt(commLengthValue);
			}catch(Exception e){}
			messageLength.setValue(b);
			
			int c = 0;
			try{
				c = Integer.parseInt(portValue);
			}catch(Exception e){}
			if(c>0){
				comNum.setSelectedIndex(c- 1);
			}else{
				comNum.setSelectedIndex(0);
			}

			Map<String, String> m = new HashMap<String, String>();
			
			try{
				m = GetSmsDllName();// dll发送短信,读取dll文件名，该dll需要在本机上，并且有正确的注册表设置
				boolean flag = true;
				Set<String> set = m.keySet();
				Object [] object = set.toArray();
				Arrays.sort(object);
				for(Object key:object){
					Comboitem item = new Comboitem();
					String value = m.get(key);
					item.setValue(value);
					item.setLabel((String)key);
					item.setParent(libName);
					if(value.equals(libNameValue)){
						libName.setSelectedItem(item);
						flag = false;
					}
				}
				if(flag){libName.setSelectedIndex(0);}
			} catch (Exception e){
				dllTestButton.setDisabled(true);
			}

			libPara.setValue(libParaValue);	
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void onRecover()
	{
		try{
			IniFilePack smsconfigIni = new IniFilePack("smsconfig.ini");
			try{
				smsconfigIni.load();
			} catch (Exception e){}
			String UserNameValue = smsconfigIni.getValue("SMSWebConfig", "User");
			String pwdValue 	 = smsconfigIni.getValue("SMSWebConfig", "Pwd");
			String webLengthValue = smsconfigIni.getValue("SMSWebConfig", "Length");
			if(UserNameValue == null){UserNameValue = "";}
			if(pwdValue == null){pwdValue = "";}
			if(webLengthValue == null){webLengthValue = "";}
			userName.setValue(UserNameValue);
			String s[] = new String[1];
			s[0] = pwdValue;
			try {
				pwdValue = dodecrypt(s).get("return").get(s[0]);
			} catch (Exception e1) {
			}
			pwd.setValue(pwdValue);
			
			Integer a = null;
			try{
				a = Integer.parseInt(webLengthValue);
			}catch(Exception e){}
			webMessageLength.setValue(a);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void onMessageRefrash(Event event)
	{
		
		UUID uuid = UUID.randomUUID();
		String targetUrl = "/main/setting/setmessage.zul" + "?uuid=" + uuid;
		eccBody = (Include) (this.desktop.getPage("eccmain").getFellow("eccBody"));
		eccBody.setSrc(null);
		eccBody.setSrc(targetUrl);
		
	}
	
	public void onPermiter(Event event) throws Exception
	{
		if (listbox_data.getSelectedItems().size() <= 0)
		{
			try{
				Messagebox.show("您还没有选定列表中的短信设置项，操作不能够完成！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			} catch (Exception e){}
			return;
		}
		View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		
		IniFilePack ini = new IniFilePack("smsphoneset.ini");
		for (Object it : listbox_data.getSelectedItems())
		{
			Listitem la = (Listitem) it;
			String section = la.getId();
			try{
				ini.load();
			} catch (Exception e){
				e.printStackTrace();
			}
			String flag = ini.getM_fmap().get(section).get("Status");
			String name = ini.getM_fmap().get(section).get("Name");
			if ("No".equals(flag))
			{
				try{
					ini.setKeyValue(section, "Status", "Yes");
					ini.saveChange();
					String minfo = loginname + " " + "在" + OpObjectId.sms_set.name + "中进行了  " + OpTypeId.enable.name + "操作，修改的项为:" + name;
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.enable, OpObjectId.sms_set);
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		reFresh();
	}
	
	public void onHold(Event event) throws Exception
	{
		if (listbox_data.getSelectedItems().size() <= 0)
		{
			try{
				Messagebox.show("您还没有选定列表中的短信设置项，操作不能够完成！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			} catch (Exception e){}
			return;
		}
		
		View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
		String loginname = view.getLoginName();
		
		IniFilePack ini = new IniFilePack("smsphoneset.ini");
		for (Object it : listbox_data.getSelectedItems())
		{
			Listitem la = (Listitem) it;
			String section = la.getId();
			try{
				ini.load();
			} catch (Exception e){
			}
			String flag = ini.getM_fmap().get(section).get("Status");
			String name = ini.getM_fmap().get(section).get("Name");
			if ("Yes".equals(flag))
			{
				try{
					ini.setKeyValue(section, "Status", "No");
					ini.saveChange();
					String minfo = loginname + " " + "在" + OpObjectId.sms_set.name + "中进行了  " + OpTypeId.diable.name + "操作，修改的项为:" + name;
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.diable, OpObjectId.sms_set);
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		reFresh();
	}
	
	public void onDel(Event event) throws Exception
	{
		if (listbox_data.getSelectedItems().size() <= 0)
		{
			try{
				Messagebox.show("您还没有选定列表中的短信设置项，操作不能够完成！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			} catch (Exception e){}
			return;
		}
		
		String section = listbox_data.getSelectedItem().getId();
		int i = 0;
		try{
			i = Messagebox.show("删除将会进行，是否继续?", "询问", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
		} catch (Exception e){}
		
		if (i == 1)
		{
			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			
			IniFilePack ini = new IniFilePack("smsphoneset.ini");
			for (Object it : listbox_data.getSelectedItems())
			{
				Listitem la = (Listitem) it;
				section = la.getId();
				try{
					ini.load();
				} catch (Exception e){
					e.printStackTrace();
				}
				
				String name = ini.getM_fmap().get(section).get("Name");
				String minfo = loginname + " " + "在" + OpObjectId.sms_set.name + "中进行了  " + OpTypeId.del.name + "操作，删除的项为:" + name;
				try{
					ini.deleteSection(section);
					ini.saveChange();
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del, OpObjectId.sms_set);
				} catch (Exception e){
					e.printStackTrace();
				}
			}
			reFresh();
		}
	}
	
	public void onEdit(Event event) throws Exception
	{
		String section = null;
		try{
			section = (String) event.getData();
			this.session.setAttribute("section", section);
			final Window win2 = (Window) Executions.createComponents("/main/setting/editMessageSet.zul", null, null);
			win2.doModal();
		} catch (Exception e)
		{
			e.printStackTrace();
			try{
				Messagebox.show("您还没有选定列表中的信息设置项，操作不能够完成！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			} catch (InterruptedException e1){
				e1.printStackTrace();
			}
		}
	}
	
	public void reFresh() throws Exception
	{
		String targetUrl = "/main/setting/setmessage.zul";
		eccBody = (Include) (this.desktop.getPage("eccmain").getFellow("eccBody"));
		eccBody.setSrc(null);
		eccBody.setSrc(targetUrl);
	}
	// 串口发送短信
	public void onApply2() throws Exception
	{
		Object objectPort = comNum.getSelectedItem().getValue();
		if(objectPort == null){
			Messagebox.show("请输选择端口号！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		String strPort = (String)objectPort;
		String strMessageLength = "";
		
		if ((messageLength.getValue() != null))
		{
			strMessageLength = messageLength.getValue().toString();
			if ("".equals(strMessageLength)|| Integer.parseInt(strMessageLength) > 70 || Integer.parseInt(strMessageLength) < 1)
			{
				Messagebox.show("请输入长度大于0并且小于等于70的信息！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				messageLength.setFocus(true);
				return;
			}
		}else{
			Messagebox.show("带 * 号的是必填项！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			messageLength.setFocus(true);
			return;
		}
		try
		{
			IniFilePack ini = new IniFilePack("smsconfig.ini");
			try{
				ini.load();
			}catch(Exception e){}
			Map<String, Map<String, String>> map = ini.getM_fmap();
			boolean createFlag = true;
			if(map != null){
				if(map.containsKey("SMSCommConfig")){
					createFlag = false;
				}
			}
			if(createFlag){
				ini.createSection("SMSCommConfig");
			}
			ini.setKeyValue("SMSCommConfig", "Port", strPort);
			ini.setKeyValue("SMSCommConfig", "length", strMessageLength);
			ini.saveChange();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void onRecover2()
	{
		try{
			IniFilePack smsconfigIni = new IniFilePack("smsconfig.ini");
			try{
				smsconfigIni.load();
			} catch (Exception e){}
			String commLengthValue = smsconfigIni.getValue("SMSCommConfig", "length");
			String portValue     = smsconfigIni.getValue("SMSCommConfig", "Port");	
			int b = 0;
			try{
				b = Integer.parseInt(commLengthValue);
			}catch(Exception e){}
			messageLength.setValue(b);
			
			int c = 0;
			try{
				c = Integer.parseInt(portValue);
			}catch(Exception e){}
			if(c>0){
				comNum.setSelectedIndex(c- 1);
			}else{
				//comNum.setSelectedIndex(0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	// dll发送短信
	private Map<String, String> GetSmsDllName() throws Exception
	{
		// Helper.XfireCreateKeyValue("dowhat","GetSmsDllName")
		Map<String, Map<String, String>> m_fmap = new HashMap<String, Map<String, String>>();
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetSmsDllName");
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception("Failed to load :" + ret.getEstr());
		m_fmap = ret.getFmap();
		if (m_fmap.containsKey("return"))
			m_fmap.remove("return");

		return m_fmap.get("DllName");
	}
	
	public void onChangeItem() throws Exception
	{
		// 更改comboxitem时，更新其参数信息
		// key 为DllName,DllInfo,DllFunParam
		IniFilePack ini = new IniFilePack("interfacedll.ini");
		if (libName.getSelectedItem().getLabel().equals(ini.getValue("DLL", "DllName")))
			libPara.setValue(ini.getValue("DLL", "DllFunParam"));
		else
			libPara.setValue(ini.getValue("DLL", ""));
	}
	
	// dll发送短信
	public void onDllTest() throws Exception
	{
		try{
			final Window win = (Window) Executions.createComponents("/main/setting/testmessage.zul", null, null);
			win.setAttribute("flag", "dll");
			win.setTitle("通过" + libName.getSelectedItem().getValue() + "发送短信");
			win.setAttribute("parameter", libPara.getValue());
			win.setAttribute("dllName", libName.getSelectedItem().getLabel());
			win.doModal();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void onApply3()throws Exception
	{
		// dll发送短信,写入interfacedll.ini
		// key 为DllName,DllInfo,DllFunParam
		if(libName.getSelectedItem() == null){
			Messagebox.show("动态库名称为空！", "提示", Messagebox.OK, Messagebox.INFORMATION);
			return;
		}
		String DllNameValue = libName.getSelectedItem().getLabel();
		String DllInfoValue = (String)(libName.getSelectedItem().getValue());		
		
		IniFilePack ini = new IniFilePack("interfacedll.ini");
		try{
			ini.load();
		}catch(Exception e){}
		Map<String, Map<String, String>> map = ini.getM_fmap();
		boolean createFlag = true;
		if(map != null){
			if(map.containsKey("DLL")){
				createFlag = false;
			}
		}
		try{
			if(createFlag){
				ini.createSection("DLL");
			}
			ini.setKeyValue("DLL", "DllName", DllNameValue);
			ini.setKeyValue("DLL", "DllInfo", DllInfoValue);
			ini.setKeyValue("DLL", "DllFunParam", libPara.getValue());
			ini.saveChange();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public void onRecover3()
	{
		try{
			IniFilePack interfacedllIni = new IniFilePack("interfacedll.ini");
			try{
				interfacedllIni.load();
			} catch (Exception e){}
			String libNameValue = interfacedllIni.getValue("DLL", "DllName");
			String libParaValue = interfacedllIni.getValue("DLL", "DllFunParam");
			if(libNameValue == null){libNameValue = "";}
			if(libParaValue == null){libParaValue = "";}

			Map<String, String> m = new HashMap<String, String>();
			try{
				m = GetSmsDllName();// dll发送短信,读取dll文件名，该dll需要在本机上，并且有正确的注册表设置
				boolean flag = true;
				Set<String> set = m.keySet();
				Object [] object = set.toArray();
				Arrays.sort(object);
				for(Object key:object){
					Comboitem item = new Comboitem();
					String value = m.get(key);
					item.setValue(value);
					item.setLabel((String)key);
					item.setParent(libName);
					if(value.equals(libNameValue)){
						libName.setSelectedItem(item);
						flag = false;
					}
				}
				if(flag){libName.setSelectedIndex(0);}
				dllTestButton.setDisabled(false);
			} catch (Exception e){
				dllTestButton.setDisabled(true);
			}

			libPara.setValue(libParaValue);	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public Map<String, Map<String, String>> dodecrypt(String[] x1) throws Exception
	{
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "decrypt");
		for (int i = 0; i <= x1.length - 1; i++)
			ndata.put(x1[i], "");

		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception("Failed to load :" + ret.getEstr());
		
		return ret.getFmap();
	}
	
}
