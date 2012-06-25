package com.siteview.ecc.setmonitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import com.siteview.base.manage.View;
import com.siteview.base.tree.INode;
import com.siteview.ecc.alert.control.AbstractListbox;
import com.siteview.ecc.alert.util.BaseTools;
import com.siteview.ecc.monitorbrower.EntityLinkFuntion;
import com.siteview.ecc.monitorbrower.MonitorDetailLinkFuntion;
import com.siteview.ecc.monitorbrower.MonitorBean;
import com.siteview.ecc.util.Toolkit;


public class MonitorInfoListbox extends AbstractListbox {

	private static final long serialVersionUID = 3825957038353315311L;
	private View v = Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
	private static String	RefreshMonitors_TargetUrl	= "/main/monitorbrower/refreshmonitor.zul";

	private List<MonitorBean> mbs = null;
	


	List<Map<String, String>> resultMap = null;

	private List<String>	changedMonitors	= null;
	
	@Override
	public List<String> getListheader() {
		return new ArrayList<String>(Arrays.asList(new String[] { "状态",
				"名称","组名","设备名","监测频率","阀值", "错误校检值"}));
	}
	
	@Override
	public void renderer() {
		try {
			if(changedMonitors == null) return;
			if(resultMap == null) return;

			for (int i = 0; i < changedMonitors.size(); i++)
			{
				INode node = v.getNode(changedMonitors.get(i));
				Listitem li = new Listitem();
				li.setHeight("20px");
				String monitorName  = node.getName();
				String monitorId 	= node.getSvId();
				String entityId 	= node.getParentSvId();
				String entityName   = v.getNode(node.getParentSvId()).getName();
				String groupName    = resultMap.get(i).get("GroupName");
				String status       = resultMap.get(i).get("Status");
				String monitorFrequency = resultMap.get(i).get("MonitorFrequency");
				String allConditionValue = resultMap.get(i).get("OkConditon")+resultMap.get(i).get("WarnConditon")+resultMap.get(i).get("ErrorConditon");
				String checkerr		= resultMap.get(i).get("Checkerr");
				
				if(monitorName == null||"".equals(monitorName)){continue;}
				if(monitorId == null||"".equals(monitorId)){continue;}
				if(entityId == null||"".equals(entityId)){continue;}
				if(entityName == null||"".equals(entityName)){continue;}
				if(groupName == null||"".equals(groupName)){continue;}
//				if(status == null||"".equals(status)){continue;}//
				if(monitorFrequency == null||"".equals(monitorFrequency)){continue;}
				if(allConditionValue == null ||"".equals(allConditionValue)){continue;}
				if(entityId.contains(".") == false){continue;}

				Listitem item = new Listitem();
				for(String head : listhead){
					if(head.equals("状态")){
						Listcell cell = new Listcell();
						cell.setImage(getImage(status));
						cell.setParent(item);
					}
					if(head.equals("名称")){
						Listcell cell = new Listcell();
						cell.setTooltiptext(monitorName);
						Component c = BaseTools.getWithMonitorLink(monitorName,new MonitorDetailLinkFuntion(entityId,monitorId,"btndetail"));
						cell.appendChild(c);
						cell.setParent(item);
					}
					if(head.equals("组名")){
						Listcell cell = new Listcell(groupName);
						cell.setTooltiptext(groupName);
						cell.setParent(item);
					}
					if(head.equals("设备名")){
						Listcell cell = new Listcell();
						cell.setTooltiptext(entityName);
						Component c2 = BaseTools.getWithEntityLink(entityName,new EntityLinkFuntion(entityId,monitorId));
						cell.appendChild(c2);
						cell.setParent(item);
					}
					if(head.equals("监测频率")){
						Listcell cell = new Listcell(monitorFrequency);
						cell.setTooltiptext(monitorFrequency);
						cell.setParent(item);
					}
					if(head.equals("阀值")){						
						Listcell cell = new Listcell(allConditionValue);
						cell.setTooltiptext(allConditionValue);
						cell.setParent(item);
					}
					if(head.equals("错误校检值")){
						Listcell cell = new Listcell(checkerr);
						cell.setTooltiptext(checkerr);
						cell.setParent(item);
					}
				}
				item.setParent(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getImage(String status){
		if (status == null || "null".equals(status)|| "".equals(status)) 
			return "/images/state_dark.gif";
		else if (status.equals("bad"))
			return "/images/state_grey.gif";
		else if (status.equals("error"))
			return "/images/state_red.gif";
		else if (status.equals("ok"))
			return "/images/state_green.gif";
		else if (status.equals("warning"))
			return "/images/state_yellow.gif";
		else if(status.equals("disable"))
			return "/images/state_stop.gif";
		else
			return "/images/state_grey.gif";		
	}

	public List<String> getChangedMonitors() {
		return changedMonitors;
	}

	public void setChangedMonitors(List<String> changedMonitors) {
		this.changedMonitors = changedMonitors;
	}
	
	public List<Map<String, String>> getResultMap() {
		return resultMap;
	}

	public void setResultMap(List<Map<String, String>> resultMap) {
		this.resultMap = resultMap;
	}
}
