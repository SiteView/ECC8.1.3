package com.siteview.ecc.alert.dao.type;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

/**
 * 告警分类
 * @author hailong.yi
 *
 */
public enum AlertCategory implements TypeInterface{
    Danger,         //危险
    Error,         //错误
    Normal;           //正常

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
			return "危险";
		}else if (this == Error){
			return "错误";
		}else if (this == Normal){
			return "正常";
		}
		return "正常";
	}
	public static AlertCategory getType(String stringValue) {
		if ("错误".equals(stringValue)){
			return AlertCategory.Error;
		}else if ("危险".equals(stringValue)){
			return AlertCategory.Danger;
		}else if ("正常".equals(stringValue)){
			return AlertCategory.Normal;
		}
		return AlertCategory.Normal;
	}
	public static AlertCategory getTypeByDisplayString(String displayString) {
		return getType(displayString);
	}
}