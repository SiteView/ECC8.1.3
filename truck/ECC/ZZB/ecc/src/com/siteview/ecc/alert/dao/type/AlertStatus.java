package com.siteview.ecc.alert.dao.type;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;


/**
 * 告警状态
 * @author hailong.yi
 *
 */
public enum AlertStatus implements TypeInterface{
    Fail,
    Success;
    public static AlertStatus getType(String id)
    {
    	if ("1".equals(id)) return Success;
    	return Fail;
    }
	public static AlertStatus getTypeByDisplayString(String displayString) {
    	if ("成功".equals(displayString)) return Success;
    	return Fail;
	}
	public String getImage(){
		if (this == Fail){
			return "/main/images/alert/fail.gif";
		}else if (this == Success){
			return "/main/images/alert/success.gif";
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
    	if (this == Fail){
    		return "失败";
    	}else if (this == Success){
    		return "成功";
    	}
    	return "不知道";
	}
	@Override
	public String getStringVaule() {
    	if (this == Success){
    		return "1";
    	}
    	return "0";
	}
}
