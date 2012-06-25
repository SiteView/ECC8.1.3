package com.siteview.ecc.alert;

import java.text.SimpleDateFormat;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Path;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Include;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.siteview.ecc.alert.dao.bean.BaseAlert;
import com.siteview.ecc.alert.dao.type.AlertCategory;
import com.siteview.ecc.alert.dao.type.AlertState;
import com.siteview.ecc.alert.dao.type.AlertTimes;
import com.siteview.ecc.alert.util.BaseTools;

public abstract class AbstractWindow extends Window {
	private static final long serialVersionUID = 8573985598900203229L;
	
	public final static String COMMAND_VAR = "command_string";
	public final static String CMD_ADD = "add";
	public final static String CMD_EDIT = "edit";
	public final static SimpleDateFormat DATE_TO_STRING = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
	
	
	/**
	 * �²������������
	 * @return Component
	 */
	public Component getSelectradio() {
		return Path.getComponent("//alertcodition/selectradio");
	}

	/**
	 * �������ѡ�����
	 * @return Component
	 */
	public Component getLeftselect() {
		return this;//Path.getComponent("//lefttree/leftselect");
	}
	/**
	 * ȷ����ť
	 * @return Button
	 */
	public Button getOkButton(){
		return (Button)BaseTools.getComponentById(this,"button_ok");
	}
	/**
	 * ȡ����ť
	 * @return Button
	 */
	public Button getCancelButton(){
		return (Button)BaseTools.getComponentById(this,"button_cancel");
	}
	/**
	 * ��������
	 * @return Radio
	 */
	public Radio getSelectRadio1(){
		return (Radio)BaseTools.getComponentById(getSelectradio(),"selectradio1");
	}
	/**
	 * ����ֻ����һ��
	 * @return Radio
	 */
	public Radio getSelectRadio2(){
		return (Radio)BaseTools.getComponentById(getSelectradio(),"selectradio2");
	}
	/**
	 * ѡ���Է��ͱ���
	 * @return Radio
	 */
	public Radio getSelectRadio3(){
		return (Radio)BaseTools.getComponentById(getSelectradio(),"selectradio3");
	}
	/**
	 * �������� - �ڼ��ο�ʼ
	 * @return Intbox
	 */
	public Intbox getAwayCount(){
		return (Intbox)BaseTools.getComponentById(getSelectradio(),"awayCount");
	}
	/**
	 * ����ֻ����һ�� - �ڼ��ο�ʼ
	 * @return Intbox
	 */
	public Intbox getOnlyOne(){
		return (Intbox)BaseTools.getComponentById(getSelectradio(),"onlyOne");
	}
	/**
	 * ѡ���Է��ͱ��� - �ڼ��ο�ʼ
	 * @return Intbox
	 */
	public Intbox getSelect1(){
		return (Intbox)BaseTools.getComponentById(getSelectradio(),"select1");
	}
	/**
	 * ѡ���Է��ͱ��� - �ظ�����
	 * @return Intbox
	 */
	public Intbox getSelect2(){
		return (Intbox)BaseTools.getComponentById(getSelectradio(),"select2");
	}
	/**
	 * ����ʱ��
	 * @return Combobox
	 */
	public Combobox getAlertEvent(){
		return (Combobox)BaseTools.getComponentById(getSelectradio(),"alertEvent");
	}

	/**
	 * �����ѡ����
	 * @return SelectTree
	 */
	public SelectTree getMonitorTree(){
		return (SelectTree)BaseTools.getComponentById(getLeftselect(),"monitortree");
	}


	/**
	 * ��������
	 * @return Include
	 */
	public Include getConditionInculde(){
		return (Include)BaseTools.getComponentById(this,"ConditionInculde");
	}
	
	
	/**
	 * ��������
	 * @return Textbox
	 */
	public Textbox getAlertName(){
		return (Textbox)BaseTools.getComponentById(this,"alertName");
	}
	/**
	 * ��������
	 * @return Combobox
	 */
	public Combobox getAlertPloy(){
		return (Combobox)BaseTools.getComponentById(this,"alertPloy");
	}
	public void doCancel(){
		this.detach();
	}
	
	
	/**
	 * �������澯��Ϣ���õ�������
	 * @param basealert �� �����澯��Ϣ
	 */
	public void setBaseAlertComponentInformation(BaseAlert basealert)
	{
		getAlertName().setValue(basealert.getName());
		//getAlertPloy().setValue((basealert.getStrategy() == null || "".equals(basealert.getStrategy().trim())) ? "��" :basealert.getStrategy());
		
		this.getDesktop().getExecution().setAttribute("alertinformation", basealert);
		this.getDesktop().getExecution().setAttribute("all_selected_ids", basealert.getTarget());
	}
	
	/**
	 * �ӽ�����ȡ�û����澯��Ϣ
	 * @param basealert ����Ҫ���û����澯��Ϣ�Ķ���
	 * @throws Exception
	 */
	public void setBaseAlertInformation(BaseAlert basealert)throws Exception
	{
		
		if (getSelectRadio1().isChecked()){
			basealert.setTimes(AlertTimes.Always);
			basealert.setAlways(getAwayCount().getValue().toString());
		}else if (getSelectRadio2().isChecked()){
			basealert.setTimes(AlertTimes.Only);
			basealert.setOnly(getOnlyOne().getValue().toString());
		}else if (getSelectRadio3().isChecked()){
			basealert.setTimes(AlertTimes.Select);
			basealert.setSelect1(getSelect1().getValue().toString());
			basealert.setSelect2(getSelect2().getValue().toString());
		}
		basealert.setName(getAlertName().getValue());
		//basealert.setStrategy("��".equals(getAlertPloy().getValue().trim())?"":getAlertPloy().getValue());
		basealert.setCategory(AlertCategory.getTypeByDisplayString(getAlertEvent().getValue()));
		basealert.setState(AlertState.Enable);
		basealert.setTarget(getMonitorTree().getAllSelectedIds());
	}
}
