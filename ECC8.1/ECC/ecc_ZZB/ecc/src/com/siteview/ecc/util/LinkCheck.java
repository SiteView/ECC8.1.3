package com.siteview.ecc.util;

import org.zkoss.zk.ui.Executions;

import com.siteview.actions.UserRight;
import com.siteview.base.manage.View;


/**
 * ����ר�����ж� �����ӵĳ��������Ƿ�ɼ�
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
	 * �ж� �����ӵĳ��������Ƿ�ɼ�
	 * @param userRightId �ò����� UserRightId �е�ֵ
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
