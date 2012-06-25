package com.siteview.ecc.alert.control;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zul.Constraint;
import org.zkoss.zul.Textbox;

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
		try {
			Matcher matcher = pattern.matcher(value.toString());
			if(value==null || value.toString().trim().equals("")){
				Messagebox.show(getMessage()==null?"��������":getMessage()+"Ϊ�գ����������룡", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
			}else if(matcher.find()){
				Messagebox.show(getMessage()==null?"��������":getMessage()+"�д��ڿհ��ַ������������룡", "��ʾ", Messagebox.OK, Messagebox.INFORMATION);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public ConstraintTextbox(){
		super();
		super.setConstraint(this);
	}
}
