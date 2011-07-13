package com.siteview.ecc.controlpanel;

import java.util.HashMap;
import java.util.Map;

public class ListDataBean {
	private String name;
	//MonitorTableModel 3
	private String description;
	private String createTime;
	//EntityTableModel 7
	private String monitorSum;
	private String monitorDisableSum;
	private String monitorErrorSum;
	private String monitorWarningSum;
	private String ipAdress;
	private String deviceTemplate;
	//SeTableModel 6
	private String entitySum;
	//GroupTableModel 9
	private String dependsOn;
	
	private int lineNum;

	/**
	 * 根据描述信息返回监测器的状态信息
	 * @return int
	 */
	public int getStatus(){
		return 0;
	}
	
	/**
	 * 根据列数返回相应的信息
	 * @param int
	 * @return Map<Integer,String>
	 */
	public Map<Integer,String> getValueByIndex(int index){
		switch(index){
			case 3:
				return getValueFromMonitorTableModel();
			case 7:
				return getValueFromEntityTableModel();
			case 6:
				return getValueFromSeTableModel();
			case 8:
				return getValueFromGroupTableModel();
			default:
				return getNameOnly();
		}
	}
	
	/**
	 * 将MonitorTableModel类中getValue方法中得到的参数放入Map中，并返回
	 * @return Map<Integer,String>
	 */
	private Map<Integer,String> getValueFromMonitorTableModel(){
		Map<Integer,String> map = new HashMap<Integer,String>();
		map.put(0, getName());
		map.put(1, getDescription());
		map.put(2, getCreateTime());
		return map;
	}
	
	/**
	 * 将EntityTableModel类中getValue方法中得到的参数放入Map中，并返回
	 * @return
	 */
	private Map<Integer,String> getValueFromEntityTableModel(){
		Map<Integer,String> map = new HashMap<Integer,String>();
		map.put(0, getName());
		map.put(1, getMonitorSum());
		map.put(2, getMonitorDisableSum());
		map.put(3, getMonitorErrorSum());
		map.put(4, getMonitorWarningSum());
		map.put(5, getIpAdress());
		map.put(6, getDeviceTemplate());
		return map;
	}
	
	/**
	 * 将SeTableModel类中getValue方法中得到的参数放入Map中，并返回
	 * @return
	 */
	private Map<Integer,String> getValueFromSeTableModel(){
		Map<Integer,String> map = new HashMap<Integer,String>();
		map.put(0, getName());
		map.put(1, getEntitySum());
		map.put(2, getMonitorSum());
		map.put(3, getMonitorDisableSum());
		map.put(4, getMonitorErrorSum());
		map.put(5, getMonitorWarningSum());
		return map;
	}
	
	/**
	 * 将GroupTableModel类中getValue方法中得到的参数放入Map中，并返回
	 * @return
	 */
	private Map<Integer,String> getValueFromGroupTableModel(){
		Map<Integer,String> map = new HashMap<Integer,String>();
		map.put(0, getName());
		map.put(1, getEntitySum());
		map.put(2, getMonitorSum());
		map.put(3, getMonitorDisableSum());
		map.put(4, getMonitorErrorSum());
		map.put(5, getMonitorWarningSum());
		map.put(6, getDescription());
		map.put(7, getDependsOn());
		map.put(8, getName());
		return map;
	}
	
	/**
	 * 在不确定是那个类调用bean的情况下调用此方法
	 * @return Map<Integer, String>
	 */
	private Map<Integer, String> getNameOnly(){
		Map<Integer,String> map = new HashMap<Integer,String>();
		map.put(0, getName());
		map.put(1, "");
		map.put(2, "");
		map.put(3, "");
		map.put(4, "");
		map.put(5, "");
		map.put(6, "");
		map.put(7, "");
		map.put(8, "");
		map.put(9, "");
		map.put(10, "");
		map.put(11, "");
		return map;
	}
	
	public String getName() {
		if(name==null){
			return "";
		}else{
			return name;
		}
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		if(description==null){
			return "";
		}else{
			return description;
		}
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCreateTime() {
		if(createTime==null){
			return "";
		}else{
			return createTime;
		}
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getMonitorSum() {
		if(monitorSum==null){
			return "";
		}else{
			return monitorSum;
		}
	}
	public void setMonitorSum(String monitorSum) {
		this.monitorSum = monitorSum;
	}
	public String getMonitorDisableSum() {
		if(monitorDisableSum==null){
			return "";
		}else{
			return monitorDisableSum;
		}
	}
	public void setMonitorDisableSum(String monitorDisableSum) {
		this.monitorDisableSum = monitorDisableSum;
	}
	public String getMonitorErrorSum() {
		if(monitorErrorSum==null){
			return "";
		}else{
			return monitorErrorSum;
		}
	}
	public void setMonitorErrorSum(String monitorErrorSum) {
		this.monitorErrorSum = monitorErrorSum;
	}
	public String getMonitorWarningSum() {
		if(monitorWarningSum==null){
			return "";
		}else{
			return monitorWarningSum;
		}
	}
	public void setMonitorWarningSum(String monitorWarningSum) {
		this.monitorWarningSum = monitorWarningSum;
	}
	public String getIpAdress() {
		if(ipAdress==null){
			return "";
		}else{
			return ipAdress;
		}
	}
	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}
	public String getDeviceTemplate() {
		if(deviceTemplate==null){
			return "";
		}else{
			return deviceTemplate;
		}
	}
	public void setDeviceTemplate(String deviceTemplate) {
		this.deviceTemplate = deviceTemplate;
	}
	public String getEntitySum() {
		if(entitySum==null){
			return "";
		}else{
			return entitySum;
		}
	}
	public void setEntitySum(String entitySum) {
		this.entitySum = entitySum;
	}
	public String getDependsOn() {
		if(dependsOn==null){
			return "";
		}else{
			return dependsOn;
		}
	}
	public void setDependsOn(String dependsOn) {
		this.dependsOn = dependsOn;
	}
	public int getLineNum() {
		return lineNum;
	}
	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}
}
