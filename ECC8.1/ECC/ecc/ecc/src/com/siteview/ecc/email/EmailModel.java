package com.siteview.ecc.email;

import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

public class EmailModel extends ListModelList implements ListitemRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmailModel(List<EmailBean> table) {
		addAll(table);
	}

	@Override
	public void render(Listitem arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		Listitem item = arg0;
		item.setHeight("28px");
		EmailBean emailBean = (EmailBean) arg1;
		item.setId(emailBean.getSection());
		Listcell c1 = new Listcell(emailBean.getName());
		c1.setImage("/main/images/email.gif");
		c1.setParent(item);
		Listcell c2 = null;
		if("1".equals(emailBean.getBcheck())){
			c2 = new Listcell("禁止");
			c2.setImage("/main/images/button/ico/disable_bt.gif");
		}else{
			c2 = new Listcell("允许");
			c2.setImage("/main/images/button/ico/enable_bt.gif");
		}
		c2.setParent(item);
		Listcell c3 = new Listcell(emailBean.getMailList());
		c3.setImage("/images/email2.gif");
		c3.setParent(item);
		Listcell c4 = new Listcell();
		c4.setImage("/main/images/alert/edit.gif");
		c4.setParent(item);
		final String section = emailBean.getSection();
		c4.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(Event event) throws Exception
			{
				final Window win2 = (Window) Executions.createComponents("/main/setting/editEmailSet.zul", null, null);
				win2.setAttribute(EmailConstant.EmailEditSection, section);
				win2.doModal();
			}
		});
	}
	
	
	
	/*		
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
	popup.addDescription("描述", m.getOperateObjInfo());*/
}
