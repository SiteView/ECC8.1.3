package com.siteview.ecc.alert;

import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.siteview.base.data.IniFile;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.alert.util.DictionaryFactory;
import com.siteview.ecc.util.Message;

public class AlertStrategyView extends Window {

	private static final long serialVersionUID = 6557577502115272897L;
	
	public void onCreate() {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void init() throws Exception {
		Map<String, Map<String, String>> map = DictionaryFactory.getAlertPloy().getFmap();
		for (String key : map.keySet()) {
//			Map<String, String> value = map.get(key);
			BaseTools.addRow(getAlertStrategyList(),key,key);
		}
	}
	public Listbox getAlertStrategyList() {
		return (Listbox) BaseTools.getComponentById(this, "alertStrategyList");
	}
	
	public Button getAddButton() {
		return (Button) BaseTools.getComponentById(this, "add");
	}
	
	public Button getEditButton() {
		return (Button) BaseTools.getComponentById(this, "edit");
	}
	
	public Button getDeleteButton() {
		return (Button) BaseTools.getComponentById(this, "delete");
	}
	
	public Button getRefreshButton() {
		return (Button) BaseTools.getComponentById(this, "refresh");
	}
	
	public void onAdd(Event event) {
		try {
			Listitem item = getAlertStrategyList().getSelectedItem();
			Component button =  event.getTarget();
			Window win = null;
			if (button.getId().startsWith("add")) {
				win = (Window) Executions.createComponents("/main/alert/strategy.zul", null, null);
				win.setTitle("新建策略");
			} else if (button.getId().startsWith("edit")) {
				if (item == null) {
					Messagebox.show("请选择要编辑的策略！", "提示", Messagebox.OK, Messagebox.INFORMATION);
					return;
				}
				win = (Window) Executions.createComponents("/main/alert/strategy.zul", null, null);
				win.setTitle("编辑策略");
				win.setAttribute("selectedItem", item.getValue());
			}
			win.setVisible(true);
			win.doModal();
			BaseTools.clear(getAlertStrategyList());
			init();
		} catch (Exception e) {
			Message.showError(e.getMessage());
		}
	}
	
	public void onRefresh() {
		try{
			BaseTools.clear(getAlertStrategyList());
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onDelete() {
		try{
			Listitem item = getAlertStrategyList().getSelectedItem();
			if (item == null) {
				Messagebox.show("请选择要删除的策略！", "提示", Messagebox.OK, Messagebox.INFORMATION);
				return;
			}
			int ret = Message.showQuestion("你确认要删除选中的记录吗？");
			if (ret == Messagebox.CANCEL)
				return;
			IniFile file = DictionaryFactory.getAlertPloy();
			file.deleteSection((String) item.getValue());
			try { file.saveChange();} catch (Exception e) {}
			BaseTools.clear(getAlertStrategyList());
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
