package com.siteview.svdb;


import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.siteview.svdb.utils.PropertyConfig;

public class ConfigReader{
	private static final Log log = LogFactory.getLog(ConfigReader.class);
	private static final long serialVersionUID = 2074635765033689916L;
	public static Properties properties = null;
	
	public static Object getConfig(String key){
		if (properties == null || properties.isEmpty()){
			properties = PropertyConfig.loadFile("config.properties");
		}
		return properties.get(key);
	}

}
