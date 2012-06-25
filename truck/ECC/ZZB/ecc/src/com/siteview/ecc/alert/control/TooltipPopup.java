package com.siteview.ecc.alert.control;

import java.util.Iterator;

import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

public class TooltipPopup extends TooltipPopupBase {
	private static final long serialVersionUID = -8543106648137068272L;
	
	public void addDescription(String title,String value){
//		if(value==null || value.trim().equals(""))return;//将提示信息中为空的项去掉
		Row row = new Row();
		row.setParent(getRows());

		Label valueLabel =new Label();
		Label titleLabel =new Label();
		titleLabel.setParent(row);
		valueLabel.setParent(row);

		valueLabel.setMultiline(true);
		valueLabel.setValue(value);
		titleLabel.setValue(title);
	}
	
	public void addDivRow(){
		Row row = new Row();
		row.setParent(getRows());
		row.setStyle("border:none;color:#000000;background-color:#FF5952");
	}
	
	public void changeLastRow(String title, String value){
		Row row = new Row();
		row = (Row)getRows().getChildren().get(getRows().getChildren().size() - 1);
		
		Label valueLabel = new Label();
		Label titleLabel = new Label();
		
		Iterator it = row.getChildren().iterator();
		while(it.hasNext()){
			Label label = (Label)it.next();
			if(title.equals(label.getValue())){
				titleLabel = label;
			}else{
				valueLabel = label;
			}
		}
		
		valueLabel.setValue(value);
		titleLabel.setValue(title);
	}
}
