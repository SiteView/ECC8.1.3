package com.siteview.ecc.usermanager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Include;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.siteview.base.data.UserEdit;
import com.siteview.base.manage.View;
import com.siteview.ecc.email.IniFilePack;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.util.Toolkit;
import com.siteview.ecc.report.common.ChartUtil;

public class SetUser extends GenericAutowireComposer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 35381716823859245L;
	
	private Listbox listbox_data;
	private Include eccBody;
	private Button addUserButton;
	private Button deleteUserButton;
	private Button permiteUserButton;
	private Button holdUserButton;	
	
	View view = Toolkit.getToolkit().getSvdbView(this.desktop);
	IniFilePack ini = new IniFilePack("user.ini");

	// 不要自己操作 user.ini, 基于下面的类做 “用户管理”
	// 使用 view.getAllUserEdit(), createUserEdit,deleteUserEdit 以及
	// com.siteview.base.data.UserEdit 中的方法-->

	public void onInit()throws Exception{
		try{
			ArrayList<User> userList = getUserList();
			View view = Toolkit.getToolkit().getSvdbView(this.session);
			if(!view.isAdmin()){
				addUserButton.setDisabled(true);
				deleteUserButton.setDisabled(true);
				permiteUserButton.setDisabled(true);
				holdUserButton.setDisabled(true);
			}
			listbox_data.getPagingChild().setMold("os");
		    UserListbox userListbox =(UserListbox)listbox_data;
		    userListbox.setUserList(userList);
			
			Session session =  Executions.getCurrent().getDesktop().getSession();
			Object addObj = session.getAttribute(UserConstant.ADD_USERID);
			session.removeAttribute(UserConstant.ADD_USERID);//remove;
			Object editObj = session.getAttribute(UserConstant.EDIT_USERID);
			session.removeAttribute(UserConstant.EDIT_USERID);//remove;
			if(addObj!= null){
				userListbox.setIndexObject(addObj);
			}else if(editObj!=null){
				userListbox.setIndexObject(editObj);
			}
			ChartUtil.clearListbox(listbox_data);
			userListbox.onCreate();
			
//			UserItemRenderer model = new UserItemRenderer(userList);
//			MakelistData(listbox_data, model, model);
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
//	private void MakelistData(Listbox listb, ListModelList model,
//			ListitemRenderer rend) {
//		listb.setModel(model);
//		listb.setItemRenderer(rend);
//	}

	public ArrayList<User> getUserList(){
		ArrayList<User> userListNormal = new ArrayList<User>();
		ArrayList<User> userListAdmin  = new ArrayList<User>();
		ArrayList<User> userList = new ArrayList<User>();
		if(view.isAdmin()){
			try{
				ini.load();
			}catch(Exception e){}
			User user = null;
			for(String section:ini.getSectionList()){
				String nIndex = ini.getValue(section, "nIndex");
				if(nIndex == null) continue;
				if("".equals(nIndex)) continue;
				user = new User();
				String loginName 	= ini.getValue(section, "LoginName");
				String userName 	= ini.getValue(section, "UserName");
				String nAdmin 		= ini.getValue(section, "nAdmin");
				String nIsUse 		= ini.getValue(section, "nIsUse");
				
				if(loginName == null) loginName = "";
				if(userName == null) userName = "";
				if(nIsUse == null) nIsUse = "0";
				
				user.setLoginName(loginName);
				user.setUserName(userName);
				user.setNIsUse(nIsUse);
				user.setNIndex(nIndex);
				if(nAdmin != null && "1".equals(nAdmin)){
					user.setNAdmin(nAdmin);
					userListAdmin.add(user);
				}else{
					userListNormal.add(user);
				}
			}
			java.util.Collections.sort(userListNormal, new Comparator<User>(){
				@Override
				public int compare(User o1, User o2) {
					return o1.getLoginName().compareTo(o2.getLoginName());
				}
			});
			java.util.Collections.sort(userListNormal, new Comparator<User>(){
				@Override
				public int compare(User o1, User o2) {
					return o1.getLoginName().compareTo(o2.getLoginName());
				}
			});
			userList.addAll(userListAdmin);
			userList.addAll(userListNormal);
			return userList;
		}else
		{
			Map<String, Map<String, String>> hashMap = view.getUserIni().m_fmap;
			String key = null;
			for (java.util.Iterator<String> it= hashMap.keySet().iterator(); it.hasNext();){
				key = it.next();
			}
			Map <String,String> values = hashMap.get(key);
			User user = new User();
			String userName = values.get("UserName");
			String nIsUse	= values.get("nIsUse");
			user.setLoginName(view.getLoginName());
			user.setUserName(userName);
			user.setNAdmin("0");
			user.setNIndex(key);
			user.setNIsUse(nIsUse);
			userListNormal.add(user);
			return userListNormal;
		}
	}
	
	public void onAdd(Event event)throws Exception{
		try{
			final Window win = (Window) Executions.createComponents("/main/setting/addUser.zul", null, null);								
			win.doModal();
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	public void refresh()throws Exception{
		try{
			String targetUrl = "/main/setting/usermanager.zul";
			eccBody = (Include) (this.desktop.getPage("eccmain")
					.getFellow("eccBody"));
			eccBody.setSrc(null);
			eccBody.setSrc(targetUrl);
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	public void onPermite(Event event) throws Exception{
		try {
			if(listbox_data.getSelectedItems().size()<=0){
				Messagebox.show("您还没有选定列表中的用户，操作不能够完成！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}
			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			
			for(Object it:listbox_data.getSelectedItems()){
				Listitem la=(Listitem)it;
				String section=la.getId();
				
				IniFilePack ini=new IniFilePack(" user.ini");
				ini.load();
				String isAdmin=ini.getValue(section, "nAdmin");
				if(isAdmin!=null&&isAdmin.equals("1")){
					continue;
				}
				String flag = ini.getM_fmap().get(section).get("nIsUse");
				String userName = ini.getM_fmap().get(section).get("UserName");
				String minfo=loginname+" "+"在"+OpObjectId.user_manage.name+"中进行了  "+OpTypeId.enable.name+"操作，允许的用户为： "+userName;
				if ("1".equals(flag) == false) {
					ini.setKeyValue(section, "nIsUse", "1");
					ini.saveChange();
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.enable, OpObjectId.user_manage);
				}
			}
			refresh();
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}

	public void onDel(Event event)throws Exception {
		try{
			if(listbox_data.getSelectedItems().size()<=0){
				try{
					Messagebox.show("您还没有选定列表中的用户，操作不能够完成！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				}catch(Exception e){}
				return;
			}
			int i=	Messagebox.show("删除将会进行，是否继续?", "询问", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
			if(i==1){
				View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
				List<UserEdit> userList = view.getAllUserEdit();
				IniFilePack ini = new IniFilePack("user.ini");	
				for(Object it:listbox_data.getSelectedItems()){
					Listitem la=(Listitem)it;
					String section=la.getId();
					try{
						ini.load();
					}catch(Exception e){}
					String isAdmin=ini.getValue(section, "nAdmin");
					if("1".equals(isAdmin))
					{
						Messagebox.show("管理员不能删除！", "提示", Messagebox.OK, Messagebox.INFORMATION);
						continue;
					}
					String userName = ini.getM_fmap().get(section).get("UserName");
					String minfo=view.getLoginName()+" "+"在"+OpObjectId.user_manage.name+"中进行了  "+OpTypeId.del.name+"操作，删除的用户名为： "+userName;			
					ini.deleteSection(section);
					ini.saveChange();
					AppendOperateLog.addOneLog(view.getLoginName(), minfo, OpTypeId.del, OpObjectId.user_manage);
				}
				refresh();
			}
		}catch(Exception e){
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}

	public void onHold(Event event) throws Exception{
		try {
			if(listbox_data.getSelectedItems().size()<=0){
				Messagebox.show("您还没有选定列表中的用户，操作不能够完成！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}

			View view = Toolkit.getToolkit().getSvdbView(event.getTarget().getDesktop());
			String loginname = view.getLoginName();
			for(Object it:listbox_data.getSelectedItems()){
				Listitem la=(Listitem)it;
				String section=la.getId();

				IniFilePack ini=new IniFilePack("user.ini");
				try{
					ini.load();
				}catch(Exception e){}
				String isAdmin=ini.getValue(section, "nAdmin");
				if("1".equals(isAdmin))
				{
					Messagebox.show("管理员不能禁用！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					continue;
				}
				String flag = ini.getM_fmap().get(section).get("nIsUse");
				String userName = ini.getM_fmap().get(section).get("UserName");
		
				if ("0".equals(flag) == false) {
					ini.setKeyValue(section, "nIsUse", "0");
					ini.saveChange();
					String minfo=loginname+" "+"在"+OpObjectId.user_manage.name+"中进行了  "+OpTypeId.diable.name+"操作，禁止的用户为： "+userName;
					AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.diable, OpObjectId.user_manage);
				}
			}
			refresh();
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
	
	public void onShowAdminMsg() throws Exception{
		Messagebox.show("管理员无需授权！", "提示", Messagebox.OK, Messagebox.INFORMATION);
	}
}