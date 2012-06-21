package com.siteview.ecc.alert.dao.bean;

import com.siteview.ecc.alert.dao.type.AlertCategory;
import com.siteview.ecc.alert.dao.type.AlertState;
import com.siteview.ecc.alert.dao.type.AlertTimes;
import com.siteview.ecc.alert.dao.type.AlertType;

public class AlertRuleQueryCondition {
    private AlertCategory alertCategory;
    private String alertIndex;
    private String alertName;
    private AlertState alertState;
    private String alertTarget;
    private AlertTimes alertTimes;
    private AlertType alertType;
    private boolean limitCategory;
    private boolean limitIndex;
    private boolean limitName;
    private boolean limitState;
    private boolean limitTarget;
    private boolean limitTimes;
    private boolean limitType;
	public AlertCategory getAlertCategory() {
		return alertCategory;
	}
	public String getAlertIndex() {
		return alertIndex;
	}
	public String getAlertName() {
		return alertName;
	}
	public AlertState getAlertState() {
		return alertState;
	}
	public String getAlertTarget() {
		return alertTarget;
	}
	public AlertTimes getAlertTimes() {
		return alertTimes;
	}
	public AlertType getAlertType() {
		return alertType;
	}
	public boolean getLimitCategory() {
		return limitCategory;
	}
	public boolean getLimitIndex() {
		return limitIndex;
	}
	public boolean getLimitName() {
		return limitName;
	}
	public boolean getLimitState() {
		return limitState;
	}
	public boolean getLimitTarget() {
		return limitTarget;
	}
	public boolean getLimitTimes() {
		return limitTimes;
	}
	public boolean getLimitType() {
		return limitType;
	}
	public void setAlertCategory(AlertCategory alertCategory) {
		this.alertCategory = alertCategory;
	}
	public void setAlertIndex(String alertIndex) {
		this.alertIndex = alertIndex;
	}
	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}
	public void setAlertState(AlertState alertState) {
		this.alertState = alertState;
	}
	public void setAlertTarget(String alertTarget) {
		this.alertTarget = alertTarget;
	}
	public void setAlertTimes(AlertTimes alertTimes) {
		this.alertTimes = alertTimes;
	}
	public void setAlertType(AlertType alertType) {
		this.alertType = alertType;
	}
	public void setLimitCategory(boolean limitCategory) {
		this.limitCategory = limitCategory;
	}
	public void setLimitIndex(boolean limitIndex) {
		this.limitIndex = limitIndex;
	}
	public void setLimitName(boolean limitName) {
		this.limitName = limitName;
	}
	public void setLimitState(boolean limitState) {
		this.limitState = limitState;
	}
	public void setLimitTarget(boolean limitTarget) {
		this.limitTarget = limitTarget;
	}
	public void setLimitTimes(boolean limitTimes) {
		this.limitTimes = limitTimes;
	}
	public void setLimitType(boolean limitType) {
		this.limitType = limitType;
	}

}
