package com.siteview.ecc.monitorbrower;

public class MonitorFilter {

	private String descript;
	
	private String entityName; //以；分开的检测器名称
	
	private String groupName;
	
	private String hiddenAndShow;
	
	private String monitorName;
	
	private String monitorType;
	
	private String refresh;

	private String sort;
	
	public MonitorFilter(){}
	
	public MonitorFilter(CVBean bean){
		this.entityName = bean.getEntityName();
		this.groupName = bean.getGroupName();
		this.hiddenAndShow = bean.getMonitorState();
		this.sort = bean.getSort();
		this.refresh = bean.getRefreshFre();
		this.monitorName = bean.getMonitorName();
		this.monitorType = bean.getMonitorType(); 
		this.descript = bean.getMonitorDescripe();
	}
	public boolean containsMonitorName(String monitorName){
		boolean flag = false;
		if(this.monitorName==null || "".equals(this.monitorName.trim())) return true;
		for(String mn : this.monitorName.split(";")){
			if(monitorName.equals(mn)){ 
				flag=true;
				break;
			}
		}
		return flag;
	}
	
	public boolean containsEntityName(String entityName){
		boolean flag = false;
		if(this.entityName == null || "".equals(this.entityName.trim())) return true;
		for(String mn : this.entityName.split(";")){
			if(entityName.equals(mn)){ 
				flag=true;
				break;
			}
		}
		return flag;		
	}
	public boolean containsGroupName(String groupName){
		boolean flag = false;
		if(this.groupName==null || "".equals(this.groupName.trim())) return true;
		for(String mn : this.groupName.split(";")){
			StringBuffer mm=new StringBuffer();
			String mmb=mm.append("SiteView ECC 8.1/").append(mn).toString();
			if(groupName.equals(mmb)){ 
				flag=true;
				break;
			}
		}
		return flag;		
	}
	
	public boolean containsDescript(String descript){
		if(this.descript==null || this.descript.trim().equals("")) return true;
		boolean flag = false;
		if(descript==null || descript.equals("")) return false;
		if(descript.contains(this.descript))
			flag = true;
		return flag;
	}
	
	public boolean containsMonitorType(String monitorType){
		if(this.monitorType==null || this.monitorType.trim().equals("99999")) return true;
		boolean flag = false;
		
		if(monitorType!=null && monitorType.equals(this.monitorType))
			flag = true;
		return flag;
	}
	public boolean containsShowAndHidden(String status){
		String sta = showAndHidden();
		if("".equals(sta)) return true;
		boolean flag = false;
		for(String tt:sta.split(",")){
			if(tt.equals(status)){
				flag = true;
				break;
			}
		}
		return flag;
	}
	public String showAndHidden(){//ok,warning,error,bad,null,disable,time_disable
		if(this.hiddenAndShow==null || this.hiddenAndShow.trim().equals("ShowAll")) return "";
		if(this.hiddenAndShow.equals("showErrorWarnning")){
			return "bad,warning,error";
		}else if(this.hiddenAndShow.equals("showError")){
			return "error";
		}else if(this.hiddenAndShow.equals("showWarnning")){
			return "warning";
		}else if(this.hiddenAndShow.equals("showNormal")){
			return "ok";
		}else if(this.hiddenAndShow.equals("showNone")){
			return "null";
		}else if(this.hiddenAndShow.equals("showBad")){//add
			return "bad";
		}else if(this.hiddenAndShow.equals("showForbid")){
			return "disable";
		}else if(this.hiddenAndShow.equals("showForeverForbid")){
			return "disable";
		}else if(this.hiddenAndShow.equals("showTemporaryForbid")){
			return "time_disable";
		}else if(this.hiddenAndShow.equals("hideErrorWarnning")){
			return "time_disable,ok,disable,null";
		}else if(this.hiddenAndShow.equals("hideError")){
			return "ok,warning,null,disable,time_disable";
		}else if(this.hiddenAndShow.equals("hideWarnning")){
			return "ok,error,bad,null,disable,time_disable";
		}else if(this.hiddenAndShow.equals("hideNormal")){
			return "warning,error,bad,null,disable,time_disable";
		}else if(this.hiddenAndShow.equals("hideNone")){
			return "ok,warning,error,bad,null,time_disable";
		}else if(this.hiddenAndShow.equals("hideBad")){//add
			return "bad,time_disable";
		}else if(this.hiddenAndShow.equals("hideForbid")){
			return "ok,warning,error,bad,null,time_disable";
		}else if(this.hiddenAndShow.equals("hideForeverForbid")){
			return "ok,warning,error,bad,null,disable";
		}else if(this.hiddenAndShow.equals("hideTemporaryForbid")){
			return "ok,warning,error,bad,null,disable";
		}
		return "";
	}
	public String getDescript() {
		return descript;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getHiddenAndShow() {
		return hiddenAndShow;
	}

	public String getMonitorName() {
		return monitorName;
	}

	public String getMonitorType() {
		return monitorType;
	}

	public String getRefresh() {
		return refresh;
	}

	public String getSort() {
		return sort;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setHiddenAndShow(String hiddenAndShow) {
		this.hiddenAndShow = hiddenAndShow;
	}

	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}

	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}

	public void setRefresh(String refresh) {
		this.refresh = refresh;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
}
