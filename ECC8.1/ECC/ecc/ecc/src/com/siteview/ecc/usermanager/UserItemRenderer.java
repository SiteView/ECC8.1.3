package com.siteview.ecc.usermanager;

/**
 * @author yuandong
 *
 */

import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.ecc.email.IniFilePack;
import com.siteview.ecc.util.Toolkit;

public class UserItemRenderer extends ListModelList implements ListitemRenderer {

	EventListener checkEditListener = null;
	HashMap<String, String> m = new HashMap<String, String>();
	public UserItemRenderer(List table) {
		addAll(table);
	}

	@Override
	public void render(Listitem arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		Listitem item = arg0;
		User user = (User) arg1;
		item.setId(user.getNIndex());
		item.setHeight("28px");
		Listcell l = new Listcell(user.getUserName());//用户名
		if ("1".equals(user.getNAdmin())){
			l.setImage("/main/images/user_suit.gif");
		}else{
			l.setImage("/main/images/user.gif");
		}	
		l.setParent(item);
		
		Listcell l2 = new Listcell(user.getLoginName());//登录名
		l2.setParent(item);
		
		Listcell l3 = null;//状态
		if("1".equals(user.getNIsUse())){
			l3 = new Listcell("允许");//状态
			l3.setImage("/main/images/button/ico/enable_bt.gif");
		}else
		{
			l3 = new Listcell("禁止");//状态
			l3.setImage("/main/images/button/ico/disable_bt.gif");
		}
		l3.setParent(item);
		
		Listcell l4 = null;//用户类型
		if("1".equals(user.getNAdmin())){
			l4 = new Listcell("管理员用户");
		}else
		{
			l4 = new Listcell("普通用户");
		}
		l4.setParent(item);
		
		Listcell l5 = new Listcell();//编辑
		l5.setImage("/main/images/alert/edit.gif");
		l5.addEventListener("onClick", new EditClickListener(user.getNIndex()));
		l5.setParent(item);
		
		Listcell l6 = new Listcell();//授权
		View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
		if(view.isAdmin()){
			if("1".equals(user.getNAdmin())){
				l6.setImage("/main/images/license_false.gif");
				l6.addEventListener("onClick", new ForAdminClickListener());
			}else{
				l6.setImage("/main/images/license.gif");	
				l6.addEventListener("onClick", new EmpowerClickListener(user.getNIndex()));
			}
		}else{
			l6.setImage("/main/images/license_false.gif");
			l6.addEventListener("onClick", new ForNotAdminClickListener());
		}
		l6.setParent(item);
		
	}	

	class EditClickListener implements org.zkoss.zk.ui.event.EventListener {


		private static final long serialVersionUID = 1L;
		private String userId;

		public EditClickListener(String id) {
			this.userId = id;
		}

		@Override
		public void onEvent(Event arg0) throws Exception {
			// TODO Auto-generated method stub
				
				final Window win = (Window) Executions.createComponents("/main/setting/editUser.zul", null, null);
				win.setAttribute(UserConstant.EDIT_USERID, userId);
			//	win.setMaximizable(true);
				win.doModal();			
		}
	}
	
	class EmpowerClickListener implements org.zkoss.zk.ui.event.EventListener {

		private static final long serialVersionUID = 1L;
		private String userId;

		public EmpowerClickListener(String id) {
			this.userId = id;
		}
		
		@Override
		public void onEvent(Event arg0) throws Exception {
			try {
				Window win = (Window)arg0.getPage().getAttribute("grantWin");
				if(win==null)
				{
					win = (Window)Executions.createComponents("/main/setting/user_grant.zul", null, null);
					arg0.getPage().setAttribute("grantWin",win);
				}
				((com.siteview.actions.GrantRightWindow)win).setUserlist(getUserList());//数据要及时更新
				((com.siteview.actions.GrantRightWindow)win).setUserid(userId);
				win.doModal();

			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show(e.getMessage(),"错误", Messagebox.OK, Messagebox.ERROR);
			}
		}
	}
	
	class ForAdminClickListener implements org.zkoss.zk.ui.event.EventListener {

		@Override
		public void onEvent(Event arg0) throws Exception {
			// TODO Auto-generated method stub
			Messagebox.show("管理员无需授权！", "提示", Messagebox.OK, Messagebox.INFORMATION);
		}
	}
	
	class ForNotAdminClickListener implements org.zkoss.zk.ui.event.EventListener {

		@Override
		public void onEvent(Event arg0) throws Exception {
			// TODO Auto-generated method stub
			Messagebox.show("普通用户无法授权！", "提示", Messagebox.OK, Messagebox.INFORMATION);
		}
	}
	
	public Map<String, Map<String, String>> getUserList(){
		Map<String, Map<String, String>> userList = new HashMap<String, Map<String, String>>();
		try{
			IniFilePack ini = new IniFilePack("user.ini");
			ini.load();
			userList = ini.getM_fmap();
			Iterator <String>iterator= ini.getM_fmap().keySet().iterator();
			for(;iterator.hasNext();){
				String key = iterator.next();
				if(userList.get(key).get("nIndex") == null){
					userList.remove(key);
				}
			}
		}catch(Exception e){}
		return userList;
	}
}
