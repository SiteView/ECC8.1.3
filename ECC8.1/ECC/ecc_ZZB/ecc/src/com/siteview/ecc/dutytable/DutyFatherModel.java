package com.siteview.ecc.dutytable;

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

import com.siteview.ecc.dutytable.DutyFatherBean;

//Description count return type
public class DutyFatherModel extends ListModelList implements ListitemRenderer {

	public DutyFatherModel(List<DutyFatherBean> table) {
		addAll(table);
	}

	@Override
	public void render(Listitem arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		Listitem item = arg0;
		item.setHeight("28px");
		DutyFatherBean dutyFatherBean = (DutyFatherBean) arg1;
		item.setId(dutyFatherBean.getSection());
		Listcell c1 = new Listcell(dutyFatherBean.getSection());
		c1.setImage("/main/images/calendar.gif");
		c1.setParent(item);
		Listcell c2 = new Listcell(dutyFatherBean.getType());
		c2.setParent(item);
		
		Listcell c3 = new Listcell(dutyFatherBean.getDescription());
		c3.setParent(item);
		
		Listcell c4 = new Listcell();
		c4.setImage("/main/images/alert/edit.gif");
		c4.setParent(item);
		final String section = dutyFatherBean.getSection();
		c4.addEventListener(Events.ON_CLICK, new EventListener()
		{
			@Override
			public void onEvent(Event event) throws Exception
			{
				final Window win2 = (Window) Executions.createComponents("/main/setting/editDutySet.zul", null, null);
				win2.setAttribute(DutyConstant.Edit_DutySet_Section, section);
				win2.doModal();
			}
		});
	}
}