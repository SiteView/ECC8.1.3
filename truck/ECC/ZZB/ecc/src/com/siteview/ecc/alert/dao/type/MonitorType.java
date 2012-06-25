package com.siteview.ecc.alert.dao.type;

import java.awt.Color;

public enum MonitorType {
	OK,
	WARN,
	ERROR,
	DISABLE,
	BAD;
	
	public static Color getColor(MonitorType type){
		switch(type){
			case OK : return new Color(152,251,152);
			case WARN : return new Color(238,221,130);
			case ERROR : return new Color(255, 62, 150);
			case DISABLE : return new Color(139,0,0);
			default : return new Color(181,181,181);
		}
	}
}
