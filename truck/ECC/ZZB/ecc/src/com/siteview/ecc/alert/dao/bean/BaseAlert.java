package com.siteview.ecc.alert.dao.bean;

import com.siteview.ecc.alert.dao.type.AlertCategory;
import com.siteview.ecc.alert.dao.type.AlertState;
import com.siteview.ecc.alert.dao.type.AlertTimes;
import com.siteview.ecc.alert.dao.type.AlertType;

public abstract class BaseAlert {
	private String always;
	private String strategy;
	
	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	private AlertCategory category;
	private String id;
	private String name;
	private String only;
	private String select1;
	private String select2;
	private AlertState state;
	private String target;
	private AlertTimes times;

	public String getAlways() {
		return always;
	}

	public AlertCategory getCategory() {
		return category;
	}

	/**
	 * 标识
	 * @return 标识
	 */
	public String getId() {
		return id;
	}

	/**
	 * 报警名称
	 * 
	 * @return 报警名称
	 */
	public String getName() {
		return name;
	}

	public String getOnly() {
		return only;
	}

	public String getSelect1() {
		return select1;
	}

	public String getSelect2() {
		return select2;
	}

	public AlertState getState() {
		return state;
	}

	public String getTarget() {
		return target;
	}

	public AlertTimes getTimes() {
		return times;
	}

	public AlertType getType() {
		if (this instanceof EmailAlert) {
			return AlertType.EmailAlert;
		} else if (this instanceof SMSAlert) {
			return AlertType.SmsAlert;
		} else if (this instanceof ScriptAlert) {
			return AlertType.ScriptAlert;
		} else if (this instanceof SoundAlert) {
			return AlertType.SoundAlert;
		}
		return null;
	}

	public void setAlways(String always) {
		this.always = always;
	}

	public void setCategory(AlertCategory category) {
		this.category = category;
	}

	/**
	 * 设置标识
	 * @param id 标识
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 设置报警名称
	 * @param name 报警名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	public void setOnly(String only) {
		this.only = only;
	}

	public void setSelect1(String select1) {
		this.select1 = select1;
	}

	public void setSelect2(String select2) {
		this.select2 = select2;
	}

	public void setState(AlertState state) {
		this.state = state;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public void setTimes(AlertTimes times) {
		this.times = times;
	}

	public void setValues(BaseAlert baseAlert) {
		if (baseAlert == null) return;
		this.always = baseAlert.getAlways();
		this.strategy = baseAlert.getStrategy();
		this.category = baseAlert.getCategory();
		this.id = baseAlert.getId();
		this.name = baseAlert.getName();
		this.only = baseAlert.getOnly();
		this.select1 = baseAlert.getSelect1();
		this.select2 = baseAlert.getSelect2();
		this.state = baseAlert.getState();
		this.target = baseAlert.getTarget();
		this.times = baseAlert.getTimes();

	}
}
