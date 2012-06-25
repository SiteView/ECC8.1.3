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
	 * 下部发送条件组件
	 * @return Component
	 */
	public Component getSelectradio() {
		return Path.getComponent("//alertcodition/selectradio");
	}

	/**
	 * 左侧树型选择组件
	 * @return Component
	 */
	public Component getLeftselect() {
		return this;//Path.getComponent("//lefttree/leftselect");
	}
	/**
	 * 确定按钮
	 * @return Button
	 */
	public Button getOkButton(){
		return (Button)BaseTools.getComponentById(this,"button_ok");
	}
	/**
	 * 取消按钮
	 * @return Button
	 */
	public Button getCancelButton(){
		return (Button)BaseTools.getComponentById(this,"button_cancel");
	}
	/**
	 * 连续报警
	 * @return Radio
	 */
	public Radio getSelectRadio1(){
		return (Radio)BaseTools.getComponentById(getSelectradio(),"selectradio1");
	}
	/**
	 * 报警只发送一次
	 * @return Radio
	 */
	public Radio getSelectRadio2(){
		return (Radio)BaseTools.getComponentById(getSelectradio(),"selectradio2");
	}
	/**
	 * 选择性发送报警
	 * @return Radio
	 */
	public Radio getSelectRadio3(){
		return (Radio)BaseTools.getComponentById(getSelectradio(),"selectradio3");
	}
	/**
	 * 连续报警 - 第几次开始
	 * @return Intbox
	 */
	public Intbox getAwayCount(){
		return (Intbox)BaseTools.getComponentById(getSelectradio(),"awayCount");
	}
	/**
	 * 报警只发送一次 - 第几次开始
	 * @return Intbox
	 */
	public Intbox getOnlyOne(){
		return (Intbox)BaseTools.getComponentById(getSelectradio(),"onlyOne");
	}
	/**
	 * 选择性发送报警 - 第几次开始
	 * @return Intbox
	 */
	public Intbox getSelect1(){
		return (Intbox)BaseTools.getComponentById(getSelectradio(),"select1");
	}
	/**
	 * 选择性发送报警 - 重复次数
	 * @return Intbox
	 */
	public Intbox getSelect2(){
		return (Intbox)BaseTools.getComponentById(getSelectradio(),"select2");
	}
	/**
	 * 报警时间
	 * @return Combobox
	 */
	public Combobox getAlertEvent(){
		return (Combobox)BaseTools.getComponentById(getSelectradio(),"alertEvent");
	}

	/**
	 * 监测器选择树
	 * @return SelectTree
	 */
	public SelectTree getMonitorTree(){
		return (SelectTree)BaseTools.getComponentById(getLeftselect(),"monitortree");
	}


	/**
	 * 报警条件
	 * @return Include
	 */
	public Include getConditionInculde(){
		return (Include)BaseTools.getComponentById(this,"ConditionInculde");
	}
	
	
	/**
	 * 报警名称
	 * @return Textbox
	 */
	public Textbox getAlertName(){
		return (Textbox)BaseTools.getComponentById(this,"alertName");
	}
	/**
	 * 报警策略
	 * @return Combobox
	 */
	public Combobox getAlertPloy(){
		return (Combobox)BaseTools.getComponentById(this,"alertPloy");
	}
	public void doCancel(){
		this.detach();
	}
	
	
	/**
	 * 将基本告警信息设置到界面上
	 * @param basealert ： 基本告警信息
	 */
	public void setBaseAlertComponentInformation(BaseAlert basealert)
	{
		getAlertName().setValue(basealert.getName());
		//getAlertPloy().setValue((basealert.getStrategy() == null || "".equals(basealert.getStrategy().trim())) ? "空" :basealert.getStrategy());
		
		this.getDesktop().getExecution().setAttribute("alertinformation", basealert);
		this.getDesktop().getExecution().setAttribute("all_selected_ids", basealert.getTarget());
	}
	
	/**
	 * 从界面上取得基本告警信息
	 * @param basealert ：需要设置基本告警信息的对象
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
		//basealert.setStrategy("空".equals(getAlertPloy().getValue().trim())?"":getAlertPloy().getValue());
		basealert.setCategory(AlertCategory.getTypeByDisplayString(getAlertEvent().getValue()));
		basealert.setState(AlertState.Enable);
		basealert.setTarget(getMonitorTree().getAllSelectedIds());
	}
}
