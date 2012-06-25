package com.siteview.ecc.util;

import org.zkoss.zk.ui.Executions;

import com.siteview.actions.UserRight;
import com.siteview.base.manage.View;


/**
 * 此类专用于判断 新增加的超级连接是否可见
 * @author Administrator
 *
 */
public class LinkCheck 
{
	public static LinkCheck getLinkCheck()
	{
		return new LinkCheck();
	}
	/**
	 * 判断 新增加的超级连接是否可见
	 * @param userRightId 该参数是 UserRightId 中的值
	 * @return
	 */
	public boolean CanSeeLink(String userRightId){
		View view = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
		if(view.isAdmin()){
			return true;
		}
		UserRight userRight =  Toolkit.getToolkit().getUserRight(Executions.getCurrent().getDesktop());
		String flag = view.getUserIni().getValue(userRight.getUserid(), userRightId);
		if("1".equals(flag)){
			return true;
		}else{
			return false;
		}
	}
}
