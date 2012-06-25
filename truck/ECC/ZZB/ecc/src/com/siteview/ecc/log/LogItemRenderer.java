package com.siteview.ecc.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

import com.siteview.ecc.alert.control.TooltipPopup;
import com.siteview.ecc.log.beans.LogValueBean;

public class LogItemRenderer extends ListModelList implements ListitemRenderer {

	public LogItemRenderer(List table) {

		addAll(table);
	}

	@Override
	public void render(Listitem arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		Listitem item = arg0;
		LogValueBean m = (LogValueBean) arg1;
		Listcell l = new Listcell(m.getUserId());
		TooltipPopup popup = new TooltipPopup();
		popup.onCreate();
		popup.setStyle("border:none;color:#FFFFFF;background-color:#717171");
		popup.setTitle(m.getUserId());
		if (m.getUserId().equals("admin"))
			popup.setImage("/main/images/user_suit.gif");
		else
			popup.setImage("/main/images/user.gif");
		popup.addDescription("用户名称", m.getUserId());
		popup.addDescription("操作对象", m.getOperateObjName());
		popup.addDescription("操作类型", m.getOperateType());
		popup.addDescription("操作时间", m.getOperateTime());
		popup.addDescription("描述", m.getOperateObjInfo());
		if (m.getUserId().equals("admin"))
			l.setImage("/main/images/user_suit.gif");
		else
			l.setImage("/main/images/user.gif");
		l.setTooltip(popup);
		popup.setParent(l);
		l.setParent(item);
		Listcell c2 = new Listcell(m.getOperateObjName());
		c2.setTooltip(popup);
		popup.setParent(c2);
		c2.setParent(item);
		Listcell c3 = new Listcell(m.getOperateType());
		c3.setTooltip(popup);
		popup.setParent(c3);
		c3.setParent(item);
		Listcell c4 = new Listcell(m.getOperateTime());
		c4.setTooltip(popup);
		popup.setParent(c4);
		c4.setParent(item);
		Listcell c5 = new Listcell(m.getOperateObjInfo());
		c5.setTooltip(popup);
		popup.setParent(c5);
		c5.setParent(item);

	}

}
