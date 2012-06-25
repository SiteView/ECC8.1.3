package com.siteview.ecc.usermanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.ecc.alert.control.AbstractListbox;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.email.IniFilePack;
import com.siteview.ecc.monitorbrower.EntityLinkFuntion;
import com.siteview.ecc.monitorbrower.MonitorDetailLinkFuntion;
import com.siteview.ecc.monitorbrower.MonitorBean;
import com.siteview.ecc.usermanager.UserItemRenderer.EditClickListener;
import com.siteview.ecc.usermanager.UserItemRenderer.EmpowerClickListener;
import com.siteview.ecc.usermanager.UserItemRenderer.ForAdminClickListener;
import com.siteview.ecc.usermanager.UserItemRenderer.ForNotAdminClickListener;
import com.siteview.ecc.util.Toolkit;


public class UserListbox extends AbstractListbox {

	private static final long serialVersionUID = 8381958435764985821L;
	
	private List<User> userList = null;
	private Object indexObject ;
	
	public List<User> getUserList() {
		return userList;
	}
	public void setUserList(List<User> userList) {
		this.userList = userList;
	}
	public Object getIndexObject() {
		return indexObject;
	}
	public void setIndexObject(Object indexObject) {
		this.indexObject = indexObject;
	}
	
	@Override
	public List<String> getListheader() {
		return new ArrayList<String>(Arrays.asList(new String[] { "�û���",
				"��¼��","״̬","�û�����","�༭","��Ȩ"}));
	}
		
	@Override
	public void renderer() {
		try {
			if(userList == null) return;
			for(User user:userList)
			{
				Listitem item = new Listitem();
				item.setHeight("28px");
				item.setId(user.getNIndex());
				for(String head : listhead){
					if(head.equals("�û���")){
						Listcell cell = new Listcell(user.getUserName());//�û���
						if ("1".equals(user.getNAdmin())){
							cell.setImage("/main/images/user_suit.gif");
						}else{
							cell.setImage("/main/images/user.gif");
						}
						cell.setParent(item);
					}
					if(head.equals("��¼��")){
						Listcell cell = new Listcell(user.getLoginName());//��¼��
						cell.setParent(item);
					}
					if(head.equals("״̬")){						
						Listcell cell = null;//״̬
						if("1".equals(user.getNIsUse())){
							cell = new Listcell("����");//״̬
							cell.setImage("/main/images/button/ico/enable_bt.gif");
						}else{
							cell = new Listcell("��ֹ");//״̬
							cell.setImage("/main/images/button/ico/disable_bt.gif");
						}
						cell.setParent(item);
					}
					if(head.equals("�û�����")){
						Listcell cell = null;//�û�����
						if("1".equals(user.getNAdmin())){
							cell = new Listcell("����Ա�û�");
						}else{
							cell = new Listcell("��ͨ�û�");
						}
						cell.setParent(item);
					}
					if(head.equals("�༭")){
						Listcell cell = new Listcell();
						cell.setImage("/main/images/alert/edit.gif");
						cell.addEventListener("onClick", new EditClickListener(user.getNIndex()));
						cell.setParent(item);
					}
					if(head.equals("��Ȩ")){						
						Listcell cell = new Listcell();//��Ȩ
						View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
						if(view.isAdmin()){
							if("1".equals(user.getNAdmin())){
								cell.setImage("/main/images/license_false.gif");
								cell.addEventListener("onClick", new ForAdminClickListener());
							}else{
								cell.setImage("/main/images/license.gif");	
								cell.addEventListener("onClick", new EmpowerClickListener(user.getNIndex()));
							}
						}else{
							cell.setImage("/main/images/license_false.gif");
							cell.addEventListener("onClick", new ForNotAdminClickListener());
						}
						cell.setParent(item);
					}
				}
				item.setParent(this);
				if(indexObject != null && user.getNIndex().equals((String)indexObject)){
					this.setSelectedItem(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
				((com.siteview.actions.GrantRightWindow)win).setUserlist(getUserListData());//����Ҫ��ʱ����
				((com.siteview.actions.GrantRightWindow)win).setUserid(userId);
				win.doModal();

			} catch (Exception e) {
				e.printStackTrace();
				Messagebox.show(e.getMessage(),"����", Messagebox.OK, Messagebox.ERROR);
			}
		}
	}
	
	class ForAdminClickListener implements org.zkoss.zk.ui.event.EventListener {

		@Override
		public void onEvent(Event arg0) throws Exception {
			// TODO Auto-generated method stub
			Messagebox.show("����Ա������Ȩ��", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
		}
	}
	
	class ForNotAdminClickListener implements org.zkoss.zk.ui.event.EventListener {

		@Override
		public void onEvent(Event arg0) throws Exception {
			// TODO Auto-generated method stub
			Messagebox.show("��ͨ�û��޷���Ȩ��", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
		}
	}
	public Map<String, Map<String, String>> getUserListData(){
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
