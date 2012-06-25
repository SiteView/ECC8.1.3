package com.siteview.ecc.monitorbrower;

import java.util.ArrayList;
import java.util.List;

import com.siteview.ecc.alert.util.LocalIniFile;

public class CustomViewSetting {

	static{
		init();
	}
	
	public static LocalIniFile getCustomViewSetIniFile() throws Exception{
		LocalIniFile iniFile = new LocalIniFile("MonitorBrowseSetting.ini");
		try{
			iniFile.load();
		}catch(Exception e){}
		
		return iniFile;
	}
	private static void init(){
		try {
			LocalIniFile iniFile = getCustomViewSetIniFile();
			
			//浏览次数最多的监测器
			if(!iniFile.getSectionList().contains("CV111")){
				CVBean bean = new CVBean("CV111","","","","","ShowAll","99999","所有类型","CV111","Manual","显示所有","Status","状态","浏览次数最多的监测器");
				saveCustomView("CV111",bean);
			}
			//出错最多的监测器
			if(!iniFile.getSectionList().contains("CV222")){
				CVBean bean = new CVBean("CV222","","","","","ShowAll","99999","所有类型","CV222","Manual","显示所有","Status","状态","出错最多的监测器");
				saveCustomView("CV222",bean);
			}
			//最近浏览的监测器
//			if(!iniFile.getSectionList().contains("CV333")){
//				CVBean bean = new CVBean("CV333","","","","","ShowAll","99999","所有类型","CV333","Manual","显示所有","Status","状态","最近浏览了的监测器");
//				saveCustomView("CV333",bean);
//			}
			//最新添加的监测器
//			if(!iniFile.getSectionList().contains("CV444")){
//			CVBean bean = new CVBean("CV444","","","","","ShowAll","99999","所有类型","CV444","Manual","显示所有","Status","状态","最新添加的监测器");
//			saveCustomView("CV444",bean);
//		}
			
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}
	
	public synchronized static boolean saveCustomView(String section,CVBean cvInfo) throws Exception{
		try{
			LocalIniFile iniFile = getCustomViewSetIniFile();
			iniFile.setKeyValue(section, "NodeId", cvInfo.getNodeId());
			iniFile.setKeyValue(section, "Titile", cvInfo.getTitile());
			
			String MonitorName = cvInfo.getMonitorName();
			MonitorName = MonitorName.replace(";", "|");
			iniFile.setKeyValue(section, "MonitorName", MonitorName);
			
			String GroupName = cvInfo.getGroupName();
			GroupName = GroupName.replace(";", "|");
			iniFile.setKeyValue(section, "GroupName", GroupName);
			
			String EntityName = cvInfo.getEntityName();
			EntityName = EntityName.replace(";", "|");
			iniFile.setKeyValue(section, "EntityName", EntityName);
			
			iniFile.setKeyValue(section, "MonitorDescripe", cvInfo.getMonitorDescripe());
			iniFile.setKeyValue(section, "MonitorState", cvInfo.getMonitorState());
			iniFile.setKeyValue(section, "MonitorType", cvInfo.getMonitorType());
			iniFile.setKeyValue(section, "Sort", cvInfo.getSort());
			iniFile.setKeyValue(section, "RefreshFre", cvInfo.getRefreshFre());
			iniFile.setKeyValue(section, "MonitorTypeName",  cvInfo.getMonitorTypeName());
			iniFile.setKeyValue(section, "ShowHideName", cvInfo.getShowHideName());
			iniFile.setKeyValue(section, "SortName", cvInfo.getSortName());
			iniFile.saveChange();
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public synchronized static boolean deleteCustomView(String cvId){
		boolean rtbool = true;
		try{
			LocalIniFile iniFile = getCustomViewSetIniFile();
			if(iniFile.getSectionList().contains(cvId))
				iniFile.deleteSection(cvId);
			iniFile.saveChange();
		}catch(Exception e){
			rtbool = false;
		}
		return rtbool;
	}
	
	public static CVBean getCustomView(String cvId){
		CVBean bean = null;
		try {
			LocalIniFile iniFile = getCustomViewSetIniFile();
			String NodeId = iniFile.getValue(cvId, "NodeId");
			String Title = iniFile.getValue(cvId, "Titile");
			String MonitorName = iniFile.getValue(cvId, "MonitorName");
			MonitorName = MonitorName.replace("|", ";");
			String GroupName = iniFile.getValue(cvId, "GroupName");
			GroupName = GroupName.replace("|", ";");
			String EntityName = iniFile.getValue(cvId, "EntityName");
			EntityName = EntityName.replace("|", ";");
			String MonitorDescripe = iniFile.getValue(cvId, "MonitorDescripe");
			String MonitorState = iniFile.getValue(cvId, "MonitorState");
			String MonitorType = iniFile.getValue(cvId, "MonitorType");
			String Sort = iniFile.getValue(cvId, "Sort");
			String RefreshFre = iniFile.getValue(cvId, "RefreshFre");
			String MonitorTypeName = iniFile.getValue(cvId, "MonitorTypeName");
			String ShowHideName = iniFile.getValue(cvId, "ShowHideName");
			String SortName = iniFile.getValue(cvId, "SortName");
			
			bean = new CVBean(NodeId,EntityName,GroupName
					,MonitorDescripe,MonitorName,MonitorState,MonitorType
					,MonitorTypeName,NodeId,RefreshFre,ShowHideName,Sort,SortName,Title);
			return bean;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	public static List<CVBean> getCustomView(){
		List<CVBean> rtnBean = new ArrayList<CVBean>();
		try {
			LocalIniFile iniFile = getCustomViewSetIniFile();

			for(String cvId : iniFile.getSectionList()){
				String NodeId = iniFile.getValue(cvId, "NodeId");
				String Title = iniFile.getValue(cvId, "Titile");
				
				String MonitorName = iniFile.getValue(cvId, "MonitorName");
				MonitorName = MonitorName.replace("|", ";");
				String GroupName = iniFile.getValue(cvId, "GroupName");
				GroupName = GroupName.replace("|", ";");
				String EntityName = iniFile.getValue(cvId, "EntityName");
				EntityName = EntityName.replace("|", ";");				
				
				String MonitorDescripe = iniFile.getValue(cvId, "MonitorDescripe");
				String MonitorState = iniFile.getValue(cvId, "MonitorState");
				String MonitorType = iniFile.getValue(cvId, "MonitorType");
				String Sort = iniFile.getValue(cvId, "Sort");
				String RefreshFre = iniFile.getValue(cvId, "RefreshFre");
				String MonitorTypeName = iniFile.getValue(cvId, "MonitorTypeName");
				String ShowHideName = iniFile.getValue(cvId, "ShowHideName");
				String SortName = iniFile.getValue(cvId, "SortName");
				CVBean bean = new CVBean(NodeId,EntityName,GroupName
						,MonitorDescripe,MonitorName,MonitorState,MonitorType
						,MonitorTypeName,NodeId,RefreshFre,ShowHideName,Sort,SortName,Title);
				rtnBean.add(bean);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtnBean;
	}
}
