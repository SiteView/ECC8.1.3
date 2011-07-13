package com.siteview.ecc.alert.control;

import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

/**
 * @author hailong.yi
 * <br/>定义的共用Combobox控件抽象类。
 * <br/>继承出来的新类只要带有一个返回String[]的方法，就能在Combobox中显示该数据
 */
public abstract class AbstractCombobox extends Combobox{
	private static final long serialVersionUID = 7922646494554714903L;
	/**
	 * @return 需要在Combobox中显示的数据
	 * <br/>需要实现该方法
	 */
	public abstract Map<String,String> getSelectArray();
	/**
	 * 控件初始化
	 */
	public void onCreate(){
		Map<String,String> map = getSelectArray();
		for (String key : map.keySet()){
			try{
				Comboitem comboitem = new Comboitem();
				comboitem.setLabel(map.get(key));
				comboitem.setValue(key);
//				comboitem.setId(key);
				comboitem.setParent(this);
			}catch(Exception e){
				
			}
		}
		if (this.getItems().size()>0){
			Session session = Executions.getCurrent().getDesktop().getSession();
			String selectedViewName = (String)session.getAttribute("selectedViewName");
			if(selectedViewName != null && !selectedViewName.isEmpty()){
				for (int index = 0 ; index<this.getItemCount() ; index++){
					Comboitem comboitem = this.getItemAtIndex(index);
					if (selectedViewName.equals(comboitem.getValue())){
						this.setSelectedIndex(index);
						return;
					}
				}
			}
			if(null == this.getSelectedItem()){
				this.setSelectedIndex(0);
			}
			
		}
	}
}
