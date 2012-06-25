package com.siteview.actions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.zkoss.zul.Window;

public class GrantRightWindow extends Window 
{
	public void setSe(String seid)
	{
		
	}
	public void setUserid(String userid)
	{
		GrantRightTree tree=(GrantRightTree)getFellow("grantTree");
		tree.setUserid(userid);
		UserSelector userSelector=(UserSelector)getFellow("userSelect");
		userSelector.setUserid(userid);
		super.getCaption().setLabel(userSelector.getSelectedItem().getLabel());
	}
	public void setUserlist(Map userlist)
	{
		UserSelector userSelector=(UserSelector)getFellow("userSelect");
		Iterator iterator=userlist.keySet().iterator();
		StringBuffer userid=new StringBuffer();
		StringBuffer userName=new StringBuffer();
		while(iterator.hasNext())
		{
			String key=iterator.next().toString();
			userid.append(key).append(",");
			Map usrMap=((Map)userlist.get(key));
			userName.append(usrMap.get("UserName")).append(",");
		}
		
		String [] useridlist 	= userid.toString().split(",");
		String [] userNamelist  = userName.toString().split(",");
		
		Map<String,String> userChangedlist = new HashMap<String,String>();
		for(int i=0;i<useridlist.length;i++){
			String key   = userNamelist[i]; //将名称作为key
			String value = useridlist[i];   //将section 作为 value
			userChangedlist.put(key, value);
		}
		java.util.Arrays.sort(userNamelist);//用户名排序
		
        //查找相应 的 userid
		for(int i=0;i<userNamelist.length;i++){
			String userIdValue = userChangedlist.get(userNamelist[i]);
			useridlist[i] = userIdValue;
		}
		userSelector.setUserList(useridlist,userNamelist);
		
	}
}
