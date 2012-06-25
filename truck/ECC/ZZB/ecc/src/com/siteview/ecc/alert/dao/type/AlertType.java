package com.siteview.ecc.alert.dao.type;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

/**
 * ¸æ¾¯ÀàÐÍ
 * @author hailong.yi
 *
 */
public enum AlertType implements TypeInterface{
    EmailAlert, 
    ScriptAlert,
    SmsAlert,
    SoundAlert;
	public String toString(){
		return getDisplayString();
	}
	
	public String getImage(){
		if (this == EmailAlert){
			return "/main/images/alert/email.gif";
		}else if (this == ScriptAlert){
			return "/main/images/alert/script.gif";
		}else if (this == SmsAlert){
			return "/main/images/alert/sms.gif";
		}else if (this == SoundAlert){
			return "/main/images/alert/sound.gif";
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

	public static AlertType getType(String value){
		if ("EmailAlert".equals(value)){
			return EmailAlert;
		}else if ("ScriptAlert".equals(value)){
			return ScriptAlert;
		}else if ("SmsAlert".equals(value)){
			return SmsAlert;
		}else if ("SoundAlert".equals(value)){
			return SoundAlert;
		}
		return EmailAlert;
	}
	public static AlertType getTypeByDisplayString(String displayString) {
    	return getType(displayString);
	}
	public static AlertType getTypeByValue(String value){
		if ("1".equals(value)){
			return EmailAlert;
		}else if ("3".equals(value)){
			return ScriptAlert;
		}else if ("2".equals(value)){
			return SmsAlert;
		}else if ("4".equals(value)){
			return SoundAlert;
		}
		return EmailAlert;
	}
	public static AlertType[] getAll(){
        return new AlertType[]{
        		EmailAlert,
        		SmsAlert,
        		ScriptAlert,
        		SoundAlert
        		};
	}

	@Override
	public String getDisplayString() {
		if (this == EmailAlert){
			return "EmailAlert";
		}else if (this == ScriptAlert){
			return "ScriptAlert";
		}else if (this == SmsAlert){
			return "SmsAlert";
		}else if (this == SoundAlert){
			return "SoundAlert";
		}
		return "";
	}

	@Override
	public String getStringVaule() {
		if (this == EmailAlert){
			return "1";
		}else if (this == ScriptAlert){
			return "3";
		}else if (this == SmsAlert){
			return "2";
		}else if (this == SoundAlert){
			return "4";
		}
		return "";
	}
}
