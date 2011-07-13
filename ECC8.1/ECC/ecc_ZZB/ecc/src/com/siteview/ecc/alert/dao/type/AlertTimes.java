package com.siteview.ecc.alert.dao.type;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;

/**
 * �澯��������
 * @author hailong.yi
 *
 */
public enum AlertTimes implements TypeInterface{
    Always,         //һֱ
    Only,           //����
    Select;          //ѡ��
	public String toString(){
		return getDisplayString();
	}
	public static AlertTimes getType(String id){
		if ("1".equals(id)){
			return Always;
		}else if ("2".equals(id)){
			return Only;
		}else if ("3".equals(id)){
			return Select;
		}
		return Select;
	}
	@Override
	public Component getComponent() {
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
		if (this == Always){
			return "�������ϱ���";
		}else if (this == Only){
			return "ֻ����һ��";
		}else if (this == Select){
			return "ѡ���Է��ͱ���";
		}
		return "";
	}
	@Override
	public String getImage() {
		if (this == Always){
			return "";
		}else if (this == Only){
			return "";
		}else if (this == Select){
			return "";
		}
		return "";
	}
	@Override
	public String getStringVaule() {
		if (this == Always){
			return "1";
		}else if (this == Only){
			return "2";
		}else if (this == Select){
			return "3";
		}
		return "3";
	}
	
}
