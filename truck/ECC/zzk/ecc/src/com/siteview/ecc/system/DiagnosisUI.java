package com.siteview.ecc.system;

import java.util.List;

import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import com.siteview.ecc.alert.AbstractWindow;
import com.siteview.ecc.alert.util.BaseTools;

public class DiagnosisUI extends AbstractWindow {
	private static final long serialVersionUID = -4741616105434918866L;
	public Listbox getListbox() {
		return (Listbox) BaseTools.getComponentById(this, "listbox_data");
	}
	
	public void onCreate() throws Exception {
		try {
			init();
		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("初始化数据出错，有可能是连接服务器或者网络问题，请刷新页面后重试！", "错误", Messagebox.OK, Messagebox.ERROR);
		}
	}
	public void init() throws Exception {
		DiagnosisTimer timer = new DiagnosisTimer(getListbox());
		getListbox().getItems().clear();
		List<Diagnosis> diagnosises = DiagnosisUtils.getDiagnosises();
		for (Diagnosis diagnosis : diagnosises) {
			Listitem item = BaseTools.setRow(getListbox(), diagnosis,diagnosis.getName(),diagnosis.getDescription(),diagnosis.getResultList());
			item.setTooltip(timer.getPopup(diagnosis));
		}
		timer.setParent(this);
		timer.setRunning(true);
	}
	public void onExcute() throws Exception {
		try {
			checkSelectItems();
			Listbox listbox = this.getListbox();
			for (Object obj : listbox.getItems()) {
				if (( obj instanceof Listitem) == false)continue;
				Listitem item = (Listitem) obj;
				if (!item.isSelected()) continue;
				Object value = item.getValue();
				if ((value instanceof Diagnosis) == false)continue;
				Diagnosis diagnosis = (Diagnosis) value;
				new RunThread(diagnosis).start();
			}
		} catch (Exception e) {
			Messagebox.show(e.getMessage(), "错误", Messagebox.OK, Messagebox.ERROR);
		}
		
	}
	
	private void checkSelectItems() throws Exception {
		Listbox listbox = this.getListbox();
		for (Object obj : listbox.getItems()) {
			if (obj instanceof Listitem) {
				Listitem item = (Listitem) obj;
				if (item.isSelected()) {
					return;
				}
			}
		}
		throw new Exception("请选择好你要诊断的项目！");

	}

	
	
}
