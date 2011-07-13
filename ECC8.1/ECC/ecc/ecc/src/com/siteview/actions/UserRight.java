package com.siteview.actions;


import java.util.HashMap;

import com.siteview.base.data.IniFile;
import com.siteview.base.manage.Manager;
import com.siteview.base.queue.GrantChangeEvent;
import com.siteview.base.queue.QueueManager;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccTreeModel;
import com.siteview.ecc.util.Toolkit;

/*缓存到登录用户的session中*/
public class UserRight {
	private String userid;
	private IniFile userIni;
	private boolean isAdmin = false;
	private HashMap<String, Integer> groupright = new HashMap<String, Integer>();
	private HashMap<String, Integer> ungroupright = new HashMap<String, Integer>();

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	protected IniFile getUserIni() {
		if (userIni == null) {
			userIni = new IniFile("user.ini", userid);
			try {

				userIni.load();
				String nAdmin =userIni.getValue(userid, "nAdmin");
				if(nAdmin!=null)
					if(nAdmin.equals("1"))
						this.isAdmin=true;
				
				arrayToMap(groupright, userIni.getValue(userid, "groupright"));
				arrayToMap(ungroupright, userIni.getValue(userid,
						"ungroupright"));

			} catch (Exception e) {
				e.printStackTrace();
				userIni = null;
			}
		}
		try{
			if (userIni!=null)
				if(userIni.m_fmap==null)
					userIni.load();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
		return userIni;
	}

	private void arrayToMap(HashMap<String, Integer> map, String list) {
		if (list != null)
			for (String str : list.split(","))
				map.put(str, 1);
	}

	public UserRight(String userid) {
		this.userid = userid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userIni=null;
		this.userid = userid;
	}

	/* 功能菜单,树节点是否可见 */
	public boolean haveTreeMenu(String type) {
		if (isAdmin)
			return true;
		if (getUserIni() != null) 
		{
			EccAction eccAction=EccActionManager.getInstance().getAction(type);
			if(eccAction==null)
				return false;
			String value = userIni.getValue(userid, eccAction.getLicense());
			if (value != null)
				return true;
		}
		return false;
	}

	
	/**/
	/* 右键菜单,是否可见 */
	public boolean havePopupMenuRight(String svid, String action) {
		if (isAdmin)
			return true;
		if (getUserIni() != null) 
		{
			//新增加的特殊逻辑 用于判断 报警 报表的权限的设置
				if(svid.startsWith("i")){
					String value = userIni.getValue(userid,action);//action:m_AlertRuleAdd
					if("1".equals(value)) {return true;}
					else {return false;}
				}else{
					String value;
					for(;;svid=svid.substring(0,svid.lastIndexOf(".")))/*从子往父找*/
					{
						value = userIni.getValue(userid,svid);
						if(value!=null)
							return value.indexOf(action + "=1") > -1;
						if(!svid.contains("."))
							break;
					}
				}
		}
		return false;
	}
	public boolean canSeeTreeNode(String svid)
	{
		if (isAdmin)
			return true;
		return canSeeMonitorTreeItem(svid);
	}
	public boolean canSeeTreeNode(EccTreeItem eccTreeItem)
	{
		if (isAdmin)
			return true;
		if(eccTreeItem.isMonitorTreeNode())
			return canSeeMonitorTreeItem(eccTreeItem.getId());
		else
			return haveTreeMenu(eccTreeItem.getType());
	}
	public boolean canSeeMonitorTreeItem(String svid) {
		if (isAdmin)
			return true;
		if (getUserIni() != null) 
		{
			for(;;svid=svid.substring(0,svid.lastIndexOf(".")))/*从子往父找*/
			{
				if(ungroupright.get(svid) != null)
				  return false;
				else if (groupright.get(svid) != null)
				  return true;
				if(!svid.contains("."))
					break;
			}
		}
		return false;
	}

	public void writePopupMenuRight(String svid, String actionsStr) throws Exception{
		if (isAdmin)
			return;
			String actions[]=actionsStr.split(",");
			HashMap<String,String> map=new HashMap<String,String>();
			for(String kv:actions)
			{
				String array[]=kv.split("=");
				if(map.get(array[0])!=null)
				{
					if(!map.get(array[0]).equals(array[1]))
					{
						throw new Exception("必须同时选择或者取消权限:"+EccActionManager.getInstance().getActionName(array[0]));
					}
				}
				map.put(array[0],array[1]);
			}
			getUserIni().setKeyValue(userid, svid, actionsStr);
	}

	public void writeTreeMenuVisible(String licence, boolean visible) throws Exception{
		if (isAdmin)
			return;

		if (visible)
				getUserIni().setKeyValue(userid, licence, "1");
			else
				getUserIni().deleteKey(userid, licence);
	}

	public void writeTreeNodeVisible(String groupRight, String unGroupRight) throws Exception{
		if (isAdmin)
			return;

			getUserIni().setKeyValue(userid, "groupright", groupRight);
			getUserIni().setKeyValue(userid, "ungroupright", unGroupRight);
	}
	public void saveChange()throws Exception
	{
		if (isAdmin)
			return;
			getUserIni().saveChange();
			Manager.instantUpdate();			
			getUserIni().load();
			QueueManager queueManager=QueueManager.getInstance();
			queueManager.refreshUserRight(this);
			queueManager.addEvent(new GrantChangeEvent(this));	
	}
	public String getUserName()
	{
		return getUserIni().getValue(userid, "UserName"); 
	}
}
