package com.siteview.ecc.userRole;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import com.siteview.ecc.email.IniFilePack;

public class AddRole extends GenericAutowireComposer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Textbox areaAddress;
	Textbox entityNumber;
	Textbox monitorNumber;
	Window addRoles;
	
	public void onAdd(Event event) throws Exception{
		String areaAddress_str 				= areaAddress.getValue().trim();
		String entityNumber_str  			= entityNumber.getValue().trim();
		String monitorNumber_str       			= monitorNumber.getValue().trim();
		Window addRole;
	    IniFilePack ini = new IniFilePack("role.ini");
		try{
			ini.load();
		}catch(Exception e){}
		long seed= System.currentTimeMillis() + areaAddress_str.hashCode() ;
		java.util.Random r=new java.util.Random(seed);
		Integer newindex= 500000+ Math.abs((areaAddress_str.hashCode()%100000)) + Math.abs((r.nextInt()%100000));
		while(ini.getSectionList().contains(newindex.toString())){
			++newindex;
		}
		String section =newindex.toString();
		ini = new IniFilePack("role.ini",section);
		ini.createSection(section);
		ini.setKeyValue(section, "areaAddress", areaAddress_str);
		ini.setKeyValue(section, "entityNumber",entityNumber_str);
		ini.setKeyValue(section, "nIndex",section);
		ini.setKeyValue(section, "monitorNumber", monitorNumber_str);	
		ini.saveChange();	
	}
}
