package com.siteview.ecc.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;

import com.siteview.ecc.alert.control.AbstractListbox;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.log.beans.LogValueBean;
import com.siteview.ecc.monitorbrower.EntityLinkFuntion;
import com.siteview.ecc.monitorbrower.MonitorDetailLinkFuntion;
import com.siteview.ecc.monitorbrower.ProcessMonitor.onEditButtonListener;
import com.siteview.ecc.monitorbrower.ProcessMonitor.onRefreshButtonListener;
import com.siteview.ecc.report.MonitorFilterCondition;
import com.siteview.ecc.report.beans.MonitorBean;
import com.siteview.ecc.report.models.MonitorModel;

public class UserOperateLogListbox extends AbstractListbox {

	private static final long serialVersionUID = 451964017915286149L;
	private ArrayList<LogValueBean> logValueBeans;
	
	@Override
	public List<String> getListheader() {
		return new ArrayList<String>(Arrays.asList(new String[] {"用户名称"
																,"操作对象"
																,"操作类型"
																,"操作时间"
																,"描述信息"}));
	}

 	@Override
	public void renderer() {
 		if(logValueBeans == null) return;
		try {
			for(LogValueBean tmpKey : logValueBeans){

				Listitem item = new Listitem();
				item.setValue(tmpKey);
				for(String head : listhead){
					if(head.equals("用户名称")){
						Listcell cell = new Listcell(tmpKey.getUserId());
						if (tmpKey.getUserId().equals("admin"))
							cell.setImage("/main/images/user_suit.gif");
						else
							cell.setImage("/main/images/user.gif");
						cell.setTooltiptext(tmpKey.getUserId());
						cell.setParent(item);
					}else
					if(head.equals("操作对象")){
						Listcell cell = new Listcell(tmpKey.getOperateObjName());
						cell.setTooltiptext(tmpKey.getOperateObjName());
						cell.setParent(item);
					}else
					if(head.equals("操作类型")){
						Listcell cell = new Listcell(tmpKey.getOperateType());
						cell.setTooltiptext(tmpKey.getOperateType());
						cell.setParent(item);
					}else
					if(head.equals("操作时间")){
						Listcell cell = new Listcell(tmpKey.getOperateTime());
						cell.setTooltiptext(tmpKey.getOperateTime());
						cell.setParent(item);
					}else
					if(head.equals("描述信息")){
						Listcell cell = new Listcell(tmpKey.getOperateObjInfo());
						cell.setTooltiptext(tmpKey.getOperateObjInfo());
						cell.setParent(item);
					}
				}
				item.setParent(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 	
	public ArrayList<LogValueBean> getLogValueBeans() {
		return logValueBeans;
	}

	public void setLogValueBeans(ArrayList<LogValueBean> logValueBeans) {
		this.logValueBeans = logValueBeans;
	}

}
