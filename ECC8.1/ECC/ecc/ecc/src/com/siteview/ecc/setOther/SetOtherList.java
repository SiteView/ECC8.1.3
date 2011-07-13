package com.siteview.ecc.setOther;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;


import com.siteview.ecc.alert.AbstractWindow;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.message.MessageConstant;
import com.siteview.ecc.setOther.bean.SetOtherBean;

public class SetOtherList extends AbstractWindow {

	private static final long serialVersionUID = -3171662399749204547L;
	private static String ADDOTHER = "/main/setOther/addother.zul";
	private boolean	editFlag = false;
	
	public Listbox getSysPassSetListbox() {
		return (Listbox) BaseTools.getComponentById(this, "syspassset_list");
	}

	public Button getAddButton() {
		return (Button) BaseTools.getComponentById(this, "add");
	}

	public Button getDeleteButton() {
		return (Button) BaseTools.getComponentById(this, "delete");
	}
	
	public Combobox getPageCombobox() {
		return (Combobox) BaseTools.getComponentById(this, "boxPageSize");
	}
	
	public void refresh() {
		try {
//			init();
			Events.sendEvent(new Event(Events.ON_SELECT, this.getSysPassSetListbox()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onCreate() throws Exception {
		try {
			this.getAddButton().addEventListener(Events.ON_CLICK, new AddClickListener());
			this.getDeleteButton().addEventListener(Events.ON_CLICK, new DeleteClickListener(null));
			this.getPageCombobox().addEventListener(Events.ON_CHANGE, new onFreshListener());
//			init();
		} catch (Exception e) {
			e.getMessage();
		}
	}	

//	public void init() throws Exception {					
//		View view = Manager.getView(Executions.getCurrent().getDesktop().getSession());
//		if (!view.isAdmin()){
//			Session session = Executions.getCurrent().getDesktop().getSession();			
//			String rightsDetailSet = session.getAttribute("rightsDetailSet").toString();
//				
//			String[] showRightSet = rightsDetailSet.split(",");
//			getAddButton().setDisabled(true);
//			getDeleteButton().setDisabled(true);				
//			for (int i=0; i<showRightSet.length; i++) {
//				if (UserRightId.preferenceEdit.equals(showRightSet[i])) {
//					getAddButton().setDisabled(false);
//					getDeleteButton().setDisabled(false);						
//					editFlag = true;
//				}					
//			}
//		}else {
//			editFlag = true;
//		}				
//		String pgsz = getPageCombobox().getValue();
//		int pageSize=Math.abs(Integer.parseInt(pgsz));
//		
//		List<SysPasswordSet> result = QuerySysPsssword();
//		Listitem firstItem = null;
//		getSysPassSetListbox().setPageSize(pageSize);
//		getSysPassSetListbox().getItems().clear();
//		Object indexObject = getEditOrAddListitem();
//		for (SysPasswordSet syspasswordset : result) {
//			SysPasswordSet syspasbean = (SysPasswordSet) syspasswordset;
//			Listitem item = BaseTools.setRow(getSysPassSetListbox(), 
//					syspasbean, syspasbean.getSysName(),
//					BaseTools.getWithLink("", Labels.getLabel("edit"), "/main/images/alert/edit.gif",
//					new EditSysClickListener(syspasbean,editFlag)));
//			item.setStyle("white-space:nowrap;overflow:hidden;text-overflow:ellipsis;height:23px;");
//			if (firstItem == null)
//				firstItem = item;
//			if(indexObject != null && syspasbean.getId().equals((String)indexObject)){
//				getSysPassSetListbox().setSelectedItem(item);
//			}
//		}
//	}

	private List<SetOtherBean> QuerySetOther() throws Exception {
		List<SetOtherBean> setotherbean = new ArrayList<SetOtherBean>();
		String id = null;
		String name = null;
		String password = null;
		String other = null;
		return setotherbean;
	}

	class EditSysClickListener implements org.zkoss.zk.ui.event.EventListener {
		private SetOtherBean syspasswordset;
		private boolean isEdit;

		public EditSysClickListener(SetOtherBean syspasswordset,boolean isEdit) {
			this.syspasswordset = syspasswordset;
			this.isEdit = isEdit;
		}

		public void onEvent(Event event) throws Exception {
			if (isEdit) {
				final Window win = (Window) Executions.createComponents(ADDOTHER, null, null);
				win.setTitle(Labels.getLabel("EditServerPassword"));
				win.setVariable("syspasswordvalue", syspasswordset, true);
				win.doModal();
			}
		}
	}
	
	private void checkSelectItems() throws Exception {
		Listbox listbox = this.getSysPassSetListbox();
		for (Object obj : listbox.getItems()) {
			if (obj instanceof Listitem) {
				Listitem item = (Listitem) obj;
				if (item.isSelected()) {
					return;
				}
			}
		}
		throw new Error(Labels.getLabel("SelectServerPassword"));
	}
	
	private class AddClickListener implements org.zkoss.zk.ui.event.EventListener {
		public void onEvent(Event arg0) throws Exception {
			final Window win = (Window) Executions.createComponents(ADDOTHER,
					null, null);
			win.setTitle("ÃÌº”URLµÿ÷∑");
			win.doModal();
		}
	}
	
	private class onFreshListener implements org.zkoss.zk.ui.event.EventListener {
		public void onEvent(Event arg0) throws Exception {
			refresh();
		}
	}

	private class DeleteClickListener implements org.zkoss.zk.ui.event.EventListener {
		private SetOtherBean setotherbean;

		public DeleteClickListener(SetOtherBean setotherbean) {
			this.setotherbean = setotherbean;
		}

		public void onEvent(Event arg0) throws Exception {
			checkSelectItems();
			int ret = Messagebox.show(Labels.getLabel("DeleteRunningOR"), Labels.getLabel("request"), Messagebox.OK| Messagebox.CANCEL, Messagebox.QUESTION);
			if (ret == 2)
				return;
			refresh();
		}
	}
	
	public Object getEditOrAddListitem(){
		Session session = Executions.getCurrent().getDesktop().getSession();
		Object editSectionObj = session.getAttribute(MessageConstant.MessageEditSection);
		Object addSectionObj = session.getAttribute(MessageConstant.MessageAddSection);
		session.removeAttribute(MessageConstant.MessageEditSection);
		session.removeAttribute(MessageConstant.MessageAddSection);
		if(editSectionObj != null){
			return editSectionObj;
		}else if(addSectionObj!= null){
			return addSectionObj;
		}else{
			return null;
		}
	}
	
}
