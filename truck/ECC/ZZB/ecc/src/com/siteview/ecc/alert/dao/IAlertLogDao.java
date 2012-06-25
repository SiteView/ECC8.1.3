package com.siteview.ecc.alert.dao;

import com.siteview.ecc.alert.dao.bean.AccessControl;
import com.siteview.ecc.alert.dao.bean.AlertLogItem;
import com.siteview.ecc.alert.dao.bean.AlertLogQueryCondition;
import com.siteview.ecc.alert.dao.bean.ListBean;

public interface IAlertLogDao {
	ListBean queryAlertLog(AccessControl AccessInformation, AlertLogQueryCondition QueryCondition)throws Exception;

	ListBean queryAlertLog(AccessControl AccessInformation, AlertLogQueryCondition QueryCondition,int noFrom,int onTo)throws Exception;
}
