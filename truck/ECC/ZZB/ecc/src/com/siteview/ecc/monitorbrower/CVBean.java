package com.siteview.ecc.monitorbrower;

/**
 * @author qimin.xiong
 *
 */
public class CVBean {

	private String cvName="";

	private String entityName="";

	private String groupName="";

	private String monitorDescripe="";
	
	private String monitorName="";


	private String monitorState="";

	private String monitorType="";

	private String monitorTypeName="";

	private String nodeId="";

	private String refreshFre="";

	private String showHideName="";

	private String sort="";

	private String sortName="";

	private String titile="";

	public CVBean(){}
	
	public CVBean(String cvName, String entityName, String groupName,
			String monitorDescripe, String monitorName, String monitorState,
			String monitorType, String monitorTypeName, String nodeId,
			String refreshFre, String showHideName, String sort,
			String sortName, String titile) {
		super();
		this.cvName = cvName;
		this.entityName = entityName;
		this.groupName = groupName;
		this.monitorDescripe = monitorDescripe;
		this.monitorName = monitorName;
		this.monitorState = monitorState;
		this.monitorType = monitorType;
		this.monitorTypeName = monitorTypeName;
		this.nodeId = nodeId;
		this.refreshFre = refreshFre;
		this.showHideName = showHideName;
		this.sort = sort;
		this.sortName = sortName;
		this.titile = titile;
	}
	public String getCvName() {
		return cvName;
	}

	public String getEntityName() {
		return entityName;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getMonitorDescripe() {
		return monitorDescripe;
	}

	public String getMonitorName() {
		return monitorName;
	}

	public String getMonitorState() {
		return monitorState;
	}

	public String getMonitorType() {
		return monitorType;
	}

	public String getMonitorTypeName() {
		return monitorTypeName;
	}

	public String getNodeId() {
		return nodeId;
	}

	public String getRefreshFre() {
		return refreshFre;
	}

	public String getShowHideName() {
		return showHideName;
	}

	public String getSort() {
		return sort;
	}

	public String getSortName() {
		return sortName;
	}

	public String getTitile() {
		return titile;
	}

	public void setCvName(String cvName) {
		this.cvName = cvName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setMonitorDescripe(String monitorDescripe) {
		this.monitorDescripe = monitorDescripe;
	}

	public void setMonitorName(String monitorName) {
		this.monitorName = monitorName;
	}

	public void setMonitorState(String monitorState) {
		this.monitorState = monitorState;
	}

	public void setMonitorType(String monitorType) {
		this.monitorType = monitorType;
	}

	public void setMonitorTypeName(String monitorTypeName) {
		this.monitorTypeName = monitorTypeName;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public void setRefreshFre(String refreshFre) {
		this.refreshFre = refreshFre;
	}

	public void setShowHideName(String showHideName) {
		this.showHideName = showHideName;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public void setTitile(String titile) {
		this.titile = titile;
	}
	public String toString(){
		return this.titile;
	}

}
