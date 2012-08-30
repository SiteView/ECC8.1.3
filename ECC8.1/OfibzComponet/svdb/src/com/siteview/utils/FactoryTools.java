package com.siteview.utils;

import com.siteview.cwmp.AlertInformationDAO;
import com.siteview.cwmp.AlertInformationDAOImpl;
import com.siteview.cwmp.QueueManager;
import com.siteview.cwmp.QueueManagerImpl;
import com.siteview.cwmp.IMXSwitch;
import com.siteview.cwmp.IMXSwitchImpl;
import com.siteview.cwmp.bean.ACSAlertInformation;

public class FactoryTools {
	public static AlertInformationDAO getAlertInformationDAOImpl(){
		return new AlertInformationDAOImpl();
	}
	
	public static IMXSwitch getIMXSwitchImpl(){
		return new IMXSwitchImpl();
	}
	
	public static QueueManager<ACSAlertInformation> getAlertQueueManagerImpl(){
		return new QueueManagerImpl<ACSAlertInformation>();
	}
}
