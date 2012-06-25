package com.siteview.ecc.dutytable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Window;

import com.siteview.ecc.alert.control.AbstractListbox;

public class DutySetListbox extends AbstractListbox {

	private static final long serialVersionUID = -5899307580559577904L;

	private ArrayList<DutyFatherBean> DutyFatherBeans;

	private Object stateObject ;
	
	@Override
	public List<String> getListheader() {
		return new ArrayList<String>(Arrays.asList(new String[] {"值班表"
																,"类型"
																,"描述"
																,"编辑"}));
	}

 	@Override
	public void renderer() {
 		if(DutyFatherBeans == null) return;
		try {
			for(DutyFatherBean fatherBean : DutyFatherBeans){
				Listitem item = new Listitem();
				item.setHeight("28px");
				item.setId(fatherBean.getSection());
				item.setValue(fatherBean);
				for(String head : listhead){
					if(head.equals("值班表")){
						Listcell cell = new Listcell(fatherBean.getSection());
						cell.setImage("/main/images/calendar.gif");
						cell.setParent(item);
					}
					if(head.equals("类型")){
						Listcell cell = new Listcell(fatherBean.getType());
						cell.setParent(item);
					}
					if(head.equals("描述")){
						Listcell cell = new Listcell(fatherBean.getDescription());
						cell.setTooltiptext(fatherBean.getDescription());
						cell.setParent(item);
					}
					if(head.equals("编辑")){
						Listcell cell = new Listcell();
						cell.setImage("/main/images/alert/edit.gif");
						cell.setParent(item);
						final String section = fatherBean.getSection();
						cell.addEventListener(Events.ON_CLICK, new EventListener()
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
				
				item.setParent(this);
				if(stateObject != null){
					String stateString = (String)stateObject;
					Session session = Executions.getCurrent().getDesktop().getSession();
					Object sectionObj = null;
					if("1".equals(stateString)){
						sectionObj= session.getAttribute(DutyConstant.Add_DutySet_Section);
					}else if("3".equals(stateString)){
						sectionObj= session.getAttribute(DutyConstant.Add_DutyFather_Section);
					}else if("2".equals(stateString)){
						sectionObj = session.getAttribute(DutyConstant.Edit_DutySet_Section);
					}else if("4".equals(stateString)){
						sectionObj = session.getAttribute(DutyConstant.Edit_DutyFather_Section);
					}
					if(sectionObj != null && fatherBean.getSection().equals((String)sectionObj)){
						this.setSelectedItem(item);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Object getStateObject() {
		return stateObject;
	}
	public void setStateObject(Object stateObject) {
		this.stateObject = stateObject;
	}

	public ArrayList<DutyFatherBean> getDutyFatherBeans() {
		return DutyFatherBeans;
	}
	public void setDutyFatherBeans(ArrayList<DutyFatherBean> dutyFatherBeans) {
		DutyFatherBeans = dutyFatherBeans;
	}
}
