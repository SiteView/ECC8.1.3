package com.siteview.ecc.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.siteview.ecc.alert.control.AbstractListbox;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.monitorbrower.EntityLinkFuntion;
import com.siteview.ecc.monitorbrower.MonitorDetailLinkFuntion;
import com.siteview.ecc.report.beans.MonitorBean;
import com.siteview.ecc.report.models.MonitorModel;

public class MonitorImfoListbox extends AbstractListbox {

	private static final long serialVersionUID = 3825957038353315311L;
	private MonitorFilterCondition condition;

	private List<MonitorBean> monitors = null;
	public List<MonitorBean> getMonitors() {
		return monitors;
	}

	@Override
	public List<String> getListheader() {
		return new ArrayList<String>(Arrays.asList(new String[] { "监测器名称",
				"组名称","IP地址","设备名", "类型", "监测频率", "阀值", "最近一次时间"/*,"状态统计"*/}));
	}

	@Override
	public void renderer() {
		MonitorModel monitorModel = new MonitorModel(condition);
		Matcher matcher = null;
		try {
			if(null!=this.condition){
				monitors = monitorModel.getMonitorInfoByCondition();
			}else{
				monitors = monitorModel.getAllMonitorInfo();
			}
			for(final MonitorBean tmpKey : monitors){
				final String monitorId 	= tmpKey.getId();
				if(monitorId == null ||"".equals(monitorId)){
					continue;
				}
				String entityId = monitorId.substring(0, monitorId.lastIndexOf("."));
				if(entityId == null ||"".equals(entityId)){
					continue;
				}
				if(entityId.contains(".") == false){
					continue;
				}
				String monitorName 	= tmpKey.getMonitorName();
				String entityName  	= tmpKey.getEntityName();

				Listitem item = new Listitem();
				item.setValue(tmpKey);
				for(String head : listhead){
					if(head.equals("监测器名称")){
//						Listcell cell = new Listcell(tmpKey.getMonitorName());
//						cell.setTooltiptext(tmpKey.getMonitorName());
//						cell.setParent(item);
						Listcell cell = new Listcell();
						cell.setTooltiptext(monitorName);
						Component c = BaseTools.getWithMonitorLink(monitorName,new MonitorDetailLinkFuntion(entityId,monitorId,"btndetail"));
						cell.appendChild(c);
						cell.setParent(item);
					}
					if(head.equals("IP地址")){
						String ip = "";
						int index = entityName.indexOf("(");
						if(index>0)
							ip = entityName.substring(0,index);
						Listcell cell = new Listcell(ip);
						cell.setTooltiptext(ip);
						cell.setParent(item);
					}
					if(head.equals("组名称")){
						Listcell cell = new Listcell(tmpKey.getGroupName());
						cell.setTooltiptext(tmpKey.getGroupName());
						cell.setParent(item);
					}
					if(head.equals("设备名")){
//						Listcell cell = new Listcell(tmpKey.getEntityName());
//						cell.setTooltiptext(tmpKey.getEntityName());
//						cell.setParent(item);
						Listcell cell = new Listcell();
						cell.setTooltiptext(entityName);
						Component c2 = BaseTools.getWithEntityLink(entityName,new EntityLinkFuntion(entityId,monitorId));
						cell.appendChild(c2);
						cell.setParent(item);
					}
					if(head.equals("类型")){
						Listcell cell = new Listcell(tmpKey.getMonitorName());
						cell.setTooltiptext(tmpKey.getMonitorName());
						cell.setParent(item);
					}
					if(head.equals("监测频率")){
						Listcell cell = new Listcell(tmpKey.getFrequency());
						cell.setTooltiptext(tmpKey.getFrequency());
						cell.setParent(item);
					}
					if(head.equals("阀值")){
						Listcell cell = new Listcell(tmpKey.getKeyValue());
						cell.setTooltiptext(tmpKey.getKeyValue());
						cell.setParent(item);
					}
					if(head.equals("最近一次时间")){
						Listcell cell = new Listcell(tmpKey.getLatestUpdate());
						cell.setTooltiptext(tmpKey.getLatestUpdate());
						cell.setParent(item);
					}
			/*		if(head.equals("状态统计")){
						Listcell cell = new Listcell();
						Image image = new Image("/main/images/topn.gif");
						image.addEventListener(Events.ON_CLICK, new EventListener(){
							@Override
							public void onEvent(Event arg0) throws Exception {
								Window win = (Window)Executions.createComponents("/main/report/monitorStateInfo.zul", null, null);
								win.setAttribute("monitorId", monitorId);
								win.setSizable(true);
								win.setClosable(true);
								win.setTitle(tmpKey.getMonitorName()+"的状态统计");
								win.doModal();
							}});
						cell.appendChild(image);
						cell.setParent(item);
					}*/
				}
				item.setParent(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MonitorFilterCondition getCondition() {
		return condition;
	}
	
	public void setCondition(MonitorFilterCondition condition) {
		this.condition = condition;
	}

}
