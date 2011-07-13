package com.siteview.ecc.alert.control;

import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;

/**
 * @author hailong.yi
 * <br/>����Ĺ���Combobox�ؼ������ࡣ
 * <br/>�̳г���������ֻҪ����һ������String[]�ķ�����������Combobox����ʾ������
 */
public abstract class AbstractCombobox extends Combobox{
	private static final long serialVersionUID = 7922646494554714903L;
	/**
	 * @return ��Ҫ��Combobox����ʾ������
	 * <br/>��Ҫʵ�ָ÷���
	 */
	public abstract Map<String,String> getSelectArray();
	/**
	 * �ؼ���ʼ��
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
