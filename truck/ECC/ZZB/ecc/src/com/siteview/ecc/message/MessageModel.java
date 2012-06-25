/**
 * @author yuandong
 */
package com.siteview.ecc.message;

import java.util.ArrayList;
import java.util.HashMap;
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

import com.siteview.ecc.message.MessageBean;
import com.siteview.ecc.message.MessageConstant;


//Name Phone Plan Status TaskType Template TemplateType
public class MessageModel extends ListModelList implements ListitemRenderer {
	
	private static final long serialVersionUID = -4424650582884585969L;

	public MessageModel(List<MessageBean> table) {
		addAll(table);
	}

	@Override
	public void render(Listitem arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		Listitem item = arg0;
		item.setHeight("28px");
		MessageBean messageBean = (MessageBean) arg1;
		item.setId(messageBean.getSection());
		Listcell c1 = new Listcell(messageBean.getName());
		c1.setImage("/main/images/sms.gif");
		c1.setParent(item);
		Listcell c2 = null;
		if("No".equals(messageBean.getStatus())){
			c2 = new Listcell("½ûÖ¹");
			c2.setImage("/main/images/button/ico/disable_bt.gif");
		}else{
			c2 = new Listcell("ÔÊÐí");
			c2.setImage("/main/images/button/ico/enable_bt.gif");
		}
		c2.setParent(item);
		Listcell c3 = new Listcell(messageBean.getPhone());
		c3.setImage("/images/sms2.gif");
		c3.setParent(item);
		Listcell c4 = new Listcell();
		c4.setImage("/main/images/alert/edit.gif");
		c4.setParent(item);
		final String section = messageBean.getSection();
		c4.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(Event event) throws Exception
			{
				final Window win2 = (Window) Executions.createComponents("/main/setting/editMessageSet.zul", null, null);
				win2.setAttribute(MessageConstant.MessageEditSection, section);
				win2.doModal();
			}
		});
	}
}
