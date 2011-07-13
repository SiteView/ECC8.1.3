package com.siteview.svdb.utils;

import java.io.InputStream;
import java.util.Properties;

public class PropertyConfig {
	public static Properties loadFile(String path) {
		Properties prop = null;
		try {
			InputStream in = PropertyConfig.class.getClassLoader()
					.getResourceAsStream(path);
			prop = new Properties();
			if (path.endsWith(".xml"))
				prop.loadFromXML(in);
			else
				prop.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prop;
	}
}
