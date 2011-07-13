/**
 * 
 */
package com.siteview.ecc.email;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author yuandong
 * 
 */
public class EmailUsingCheck  {

	
	public ArrayList<String> getUsingEmailTemplateAlarmList()
	{
		//在报警中的模板
		ArrayList<String> usingEmailTemplatelist = new ArrayList<String>();
		try{
			IniFilePack ini = new IniFilePack("alert.ini");
			try{
				ini.load();
			}catch(Exception e){}
			
			ArrayList<String> sectionlist = ini.getSectionList();
			for(String s:sectionlist)
			{
				String StringAlertType = ini.getValue(s, "AlertType");
				String StringEmailTemplate = ini.getValue(s, "EmailTemplate");
				if(StringAlertType != null){
					if("EmailAlert".equals(StringAlertType)){//邮件
						if(StringEmailTemplate != null){
							usingEmailTemplatelist.add(StringEmailTemplate);
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return usingEmailTemplatelist;
	}
	public ArrayList<String> getUsingEmailTemplateSetList()
	{
		//在邮件设置中...
		ArrayList<String> usingEmailTemplatelist = new ArrayList<String>();
		try{
			IniFilePack ini2 = new IniFilePack("emailAdress.ini");
			try{
				ini2.load();
			}catch(Exception e){}
			ArrayList<String> sectionlist2 = ini2.getSectionList();
			for(String s:sectionlist2)
			{
				String StringTemplateName =  ini2.getValue(s, "Template");
				HashMap<String,String> map = new HashMap<String,String>();
				if(StringTemplateName != null && StringTemplateName != null)
				{
					usingEmailTemplatelist.add(StringTemplateName);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return usingEmailTemplatelist;
	}
}