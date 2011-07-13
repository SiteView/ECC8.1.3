package com.siteview.ecc.alert.control;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Textbox;

import com.siteview.ecc.util.Message;

public class ConstraintTextbox extends Textbox implements Constraint {
	
	private static final Pattern pattern = Pattern.compile("\\s");
	
	private String message=null;

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public void validate(Component comp, Object value)
			throws WrongValueException {
		Matcher matcher = pattern.matcher(value.toString());
		if(value==null || value.toString().trim().equals("")){
			Message.showInfo(getMessage()==null?"您的输入":getMessage()+"为空，请重新输入！");
		}else if(matcher.find()){
			Message.showInfo(getMessage()==null?"您的输入":getMessage()+"中存在空白字符，请重新输入！");
		}
	}

	public ConstraintTextbox(){
		super();
		super.setConstraint(this);
	}
}
