package com.siteview.ecc.alert.dao.type;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

/**
 * �澯����
 * @author hailong.yi
 *
 */
public enum AlertCategory implements TypeInterface{
    Danger,         //Σ��
    Error,         //����
    Normal;           //����

	public String toString(){
		return getStringVaule();
	}
	public static AlertCategory[] getAll(){
        return new AlertCategory[]{
        		Danger,
        		Error,
        		Normal
        		};
	}
	
	public String getImage(){
		if (this == Danger){
			return "/main/images/alert/danger.gif";
		}else if (this == Error){
			return "/main/images/alert/error.gif";
		}else if (this == Normal){
			return "/main/images/alert/normal.gif";
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
		return getStringVaule();
	}
	@Override
	public String getStringVaule() {
		if (this == Danger){
			return "Σ��";
		}else if (this == Error){
			return "����";
		}else if (this == Normal){
			return "����";
		}
		return "����";
	}
	public static AlertCategory getType(String stringValue) {
		if ("����".equals(stringValue)){
			return AlertCategory.Error;
		}else if ("Σ��".equals(stringValue)){
			return AlertCategory.Danger;
		}else if ("����".equals(stringValue)){
			return AlertCategory.Normal;
		}
		return AlertCategory.Normal;
	}
	public static AlertCategory getTypeByDisplayString(String displayString) {
		return getType(displayString);
	}
}