package com.siteview.utils;

import java.util.Properties;

import org.ofbiz.base.util.UtilProperties;

public class ConfigInformation {
	private static final Properties properties = UtilProperties.getProperties("config_imx.properties");
	public static int getAlertQueueMaxSize(){
		try{
			return new Integer(properties.getProperty("alertQueueMaxSize"));
		}catch(Exception e){
			return 500;
		}
	}
}
