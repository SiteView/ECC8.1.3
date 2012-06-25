package com.siteview.ecc.system;

import java.util.List;

import org.zkoss.zul.Column;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Timer;

import com.siteview.ecc.alert.control.TooltipPopup;

public class DiagnosisTimer extends Timer {
	private static final long serialVersionUID = 4772678863767721998L;
	private Listbox listbox = null;
	public DiagnosisTimer(Listbox listbox){
		super(500);
		this.listbox = listbox;
		this.setRepeats(true);
	}
	public void onTimer() {
		try{
			if (this.listbox == null) return;
			for (Object obj : listbox.getItems()) {
				if (( obj instanceof Listitem) == false)continue;
				Listitem item = (Listitem) obj;
				if (!item.isSelected()) continue;
				Object value = item.getValue();
				if ((value instanceof Diagnosis) == false)continue;
				Diagnosis diagnosis = (Diagnosis) value;
				Listcell cell = getListcell(item,2);
				cell.setLabel(diagnosis.getLastResult());
				item.setTooltip(getPopup(diagnosis));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	private Listcell getListcell(Listitem item,int index){
		if (item==null) return null;
		List children = item.getChildren();
		if (children==null) return null;
		return (Listcell) children.get(index);
	}
	public TooltipPopup getPopup(Diagnosis diagnosis) {
		TooltipPopup tooltippopup = new TooltipPopup();
		tooltippopup.onCreate();
		tooltippopup.setStyle("border:none;color:#FFFFFF;background-color:#717171");
		this.getTooltiptext(tooltippopup, diagnosis);
		tooltippopup.setParent(listbox.getParent());
		
		List cols = tooltippopup.getCols().getChildren();
		for (Object obj : cols){
			if (obj instanceof Column){
				Column col = (Column) obj;
				col.setWidth("90px");
				break;
			}
		}
		
		return tooltippopup;
	}
	public void getTooltiptext(TooltipPopup tooltippopup, Diagnosis diagnosis) {
		tooltippopup.setTitle("诊断信息");
		tooltippopup.setImage("/main/images/diagnosis.gif");
		tooltippopup.addDescription("名称", diagnosis.getName());
		tooltippopup.addDescription("描述", diagnosis.getDescription());
		StringBuffer sb = new StringBuffer();
		for (String message : diagnosis.getResultList()){
			sb.append(message);
			sb.append("\n");
		}
		tooltippopup.addDescription("诊断结果", sb.toString());
	}		
}
