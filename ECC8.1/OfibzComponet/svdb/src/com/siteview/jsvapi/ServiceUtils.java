package com.siteview.jsvapi;

import org.ofbiz.base.component.ComponentConfig;

public class ServiceUtils {
	private static String ConfigFilePath = null;
	public static String getConfigFilePath() throws Exception {
		if (ConfigFilePath == null){
			ComponentConfig componentConfig = null;
			try{
				componentConfig = ComponentConfig.getComponentConfig("svdb");
				ConfigFilePath = componentConfig.getRootLocation() + "config/";
			}catch(Exception e){
				try{
					componentConfig = ComponentConfig.getComponentConfig("itsm");
					ConfigFilePath = componentConfig.getRootLocation() + "config/";
				}catch(Exception e1){
					ConfigFilePath = "";
				}
				 
			}
		}
		return ConfigFilePath;
	}

}
