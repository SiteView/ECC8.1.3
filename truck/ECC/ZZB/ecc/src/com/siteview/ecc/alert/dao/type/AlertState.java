package com.siteview.ecc.alert.dao.type;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

/**
 * �澯������ֹ
 * @author hailong.yi
 *
 */
public enum AlertState implements TypeInterface{
    Disable ,        //��ֹ
    Enable ;          //������
	public String toString(){
		return getDisplayString();
	}
	public static AlertState getType(String id){
		if ("Enable".equalsIgnoreCase(id)){
			return Enable;
		}
		return Disable;
	}
	public static AlertState getTypeByDisplayString(String displayString) {
		return getType(displayString);
	}
	public String getImage(){
		if (this == Disable){
			return "/main/images/alert/disable.gif";
		}else if (this == Enable){
			return "/main/images/alert/enable.gif";
		}
		return "/main/images/alert/none.gif";
	}
	
	public Component getComponent(){
		HboxWithSortValue hbox = new HboxWithSortValue();
		Image alertimage =  new Image(this.getImage());
		alertimage.setAlign("middle");
		Label label = new Label("   " + this.toString());
		alertimage.setParent(hbox);
		label.setParent(hbox);
		hbox.setSortValue(getDisplayString());
		return hbox;
	}
	@Override
	public String getDisplayString() {
		if (this == Disable){
			return "��ֹ";
		}else if (this == Enable){
			return "������";
		}
		return "��֪��";
	}
	@Override
	public String getStringVaule() {
		if (this == Enable){
			return "Enable";
		}
		return "��ֹ";
	}
}