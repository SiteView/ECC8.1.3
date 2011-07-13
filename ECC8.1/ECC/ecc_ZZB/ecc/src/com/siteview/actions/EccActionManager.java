package com.siteview.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletContext;

import com.siteview.base.data.IniFile;
import com.siteview.base.data.UserRightId;
import com.siteview.base.data.VirtualItem;
import com.siteview.base.data.ZulItem;
import com.siteview.ecc.start.EccStarter;
import com.siteview.ecc.start.StarterListener;
import com.siteview.ecc.treeview.EccTreeItem;
import com.siteview.ecc.treeview.EccWebAppInit;

public class EccActionManager implements StarterListener {

	private HashMap<String, String> actionNameMap = new HashMap<String, String>();
	private HashMap<String, String> actionTargetMap = new HashMap<String, String>();
	private HashMap<String, String> urlMap = new HashMap<String, String>();
	private HashMap<String, EccAction> eccActionMap = new HashMap<String, EccAction>();

	private String eccVersion = "0";//0.普通ecc 1.小猫 2.大猫
	private final String DEFAULT_URL = "/main/eccbody.zul";

	private static EccActionManager eccActionManager;

	public static EccActionManager getInstance() {
		if (eccActionManager == null)
			eccActionManager = (EccActionManager) EccStarter.getInstance()
					.getStarterListener("eccActionManager");
		return eccActionManager;
	}

	@Override
	public void startInit(EccStarter starter) {
		
		IniFile ini_general = new IniFile("general.ini");
		try{
			ini_general.load();
		}catch(Exception e){}
		String ismaster_str = ini_general.getValue("version","ismaster");
		
		if(ismaster_str != null && "1".equals(ismaster_str)){// 大猫
			this.eccVersion = "2";
		}else{
			IniFile ini_svdbbackupconfig = new IniFile("svdbbackupconfig.ini");
			try{
				ini_svdbbackupconfig.load();
			}catch(Exception e){}
			String centerAdress_str = ini_svdbbackupconfig.getValue("backupCenter","CenterAdress");
			String localSEId_str = ini_svdbbackupconfig.getValue("backupCenter","LocalSEId");
			if(centerAdress_str == null || "".equals(centerAdress_str) || localSEId_str == null || "".equals(localSEId_str)){
			    this.eccVersion="0";
			}else{
				this.eccVersion="1";
			}
		}
		loadActionNames(starter);
		loadUrlMap(starter);
		loadActionTarget(starter);
		loadEccAction(starter);
		loadVirtualAction(starter);
		//addSpeciaAction();//取消特殊的逻辑
	}

	@Override
	public void destroyed(EccStarter starter) {
	}

	private void loadEccAction(EccStarter starter) {
		for (String type : actionTargetMap.keySet()) 
		{
			for (String license : actionTargetMap.get(type).split(",")) {
				EccAction eccAction = new EccAction(EccAction.ACTION_FOR_POPUP);
				eccAction.setType(type);
				eccAction.setActionName(actionNameMap.get(license));
				eccAction.setLicense(license);
				eccAction.setNeedAuth(true);
				eccAction.setUrl(urlMap.get(type+"."+license));
				eccAction.setImage(EccWebAppInit.getInstance().getActionImage(license));
				eccActionMap.put(type, eccAction);
			}

		}

	}

	/* 来自VirtualItem的授权数据 */
	private void loadVirtualAction(EccStarter starter) {
		try {
			for (String key : VirtualItem.allZulItem.keySet()) {
				ZulItem item = VirtualItem.allZulItem.get(key);
				EccAction eccAction = new EccAction(EccAction.ACTION_FOR_TREENODE);
				eccAction.setType(item.zulType);
				eccAction.setActionName(item.zulName);
				if (item.license != null) {
					eccAction.setLicense(item.license);
					eccAction.setNeedAuth(true);
				}
				eccAction.setUrl(urlMap.get(item.zulType));
				eccAction.setImage(EccWebAppInit.getInstance().getImage(item.zulType));
				eccActionMap.put(eccAction.getType(), eccAction);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Runtime.getRuntime().exit(1);
		}
	}

	private void loadUrlMap(EccStarter starter) {
		BufferedReader bufReader=null;
		InputStreamReader isr=null;
		FileInputStream fis=null;
		try {


			fis=new FileInputStream(new File(starter.getRealPath("/main/eccUrl.properties")));
			isr=new InputStreamReader(fis);
			bufReader = new BufferedReader(isr);
			
			Properties urlProp = new Properties();
			urlProp.load(bufReader);
			for (Object key : urlProp.keySet())
				urlMap.put(key.toString(), urlProp.get(key).toString());
			
			
		} catch (IOException e) {
			System.err
					.println("Ingored: failed to load eccUrl.properties file, \nCause: "
							+ e.getMessage());
		}finally
		{
			try{bufReader.close();}catch(Exception e){}
			try{isr.close();}catch(Exception e){}
			try{fis.close();}catch(Exception e){}
		}

	}

	public EccAction getAction(String type) {
		return eccActionMap.get(type);
	}

	private void loadActionTarget(EccStarter starter) {
		BufferedReader bufReader = null;
		InputStreamReader isr =null;
		FileInputStream fis=null;
		try {
			String path = "/main/eccAtionsTarget_normal.properties";
			if("2".equals(this.eccVersion)){
				path = "/main/eccAtionsTarget_big.properties";
			}else if("1".equals(this.eccVersion)){
				path = "/main/eccAtionsTarget_small.properties";
			}

			fis=new FileInputStream(new File(starter.getContext()
							.getRealPath(path)));
							
			isr=new InputStreamReader(fis);
							
			bufReader = new BufferedReader(isr);

			Properties actionTargetProp = new Properties();
			actionTargetProp.load(bufReader);
			for (Object key : actionTargetProp.keySet())
				actionTargetMap.put(key.toString(), actionTargetProp.get(key)
						.toString());

		} catch (IOException e) {
			e.printStackTrace();
			System.err
					.println("Ingored: failed to load eccAtions.properties or eccAtionsTarget.properties file, \nCause: "
							+ e.getMessage());
		}finally
		{
			try{bufReader.close();}catch(Exception e){}
			try{isr.close();}catch(Exception e){}
			try{fis.close();}catch(Exception e){}
		}

	}

	private void loadActionNames(EccStarter starter) {
		BufferedReader bufReader = null;
		FileInputStream fis=null;
		InputStreamReader isr=null;
		try {
			String path = "/main/eccActions_normal.properties";
			if("2".equals(this.eccVersion)){
				path = "/main/eccActions_big.properties";
			}else if("1".equals(this.eccVersion)){
				path = "/main/eccActions_small.properties";
			}
			fis=new FileInputStream(new File(starter.getContext()
					.getRealPath(path)));
			isr=new InputStreamReader(fis);
			bufReader = new BufferedReader(isr);
			Properties actionProp = new Properties();
			actionProp.load(bufReader);
			for (Object key : actionProp.keySet())
				actionNameMap.put(key.toString(), actionProp.get(key)
						.toString());

			

		} catch (IOException e) {
			e.printStackTrace();
			System.err
					.println("Ingored: failed to load eccAtions.properties or eccAtionsTarget.properties file, \nCause: "
							+ e.getMessage());
		}
		finally
		{
			try{isr.close();}catch(Exception e){}
			try{fis.close();}catch(Exception e){}
			try{bufReader.close();}catch(Exception e){}
		}

	}

	public String[] getActionList(String actionTarget) {
		Object actionList = actionTargetMap.get(actionTarget);
		if (actionList != null)
			return actionList.toString().split(",");
		else
			return new String[] {};
	}
	public String getActionName(String action) {
		Object actionName=actionNameMap.get(action);
		if(actionName!=null)
			return actionName.toString();
		else
			return action;
	}
	public String getUrl(String type)
	{
		String url=urlMap.get(type);
		return (url==null)?DEFAULT_URL:url;
	}
	
	/*特殊逻辑,目前还没有机制完成此类授权定义*/
//	public void addSpeciaAction()
//	{
//		String type[]=new String[]{UserRightId.DoAlertRuleAdd,UserRightId.DoAlertRuleEdit,UserRightId.DoAlertRuleDel,UserRightId.DoReportlistAdd,UserRightId.DoReportlistEdit,UserRightId.DoReportlistDel};
//		String licence[]=new String[]{UserRightId.DoAlertRuleAdd,UserRightId.DoAlertRuleEdit,UserRightId.DoAlertRuleDel,UserRightId.DoReportlistAdd,UserRightId.DoReportlistEdit,UserRightId.DoReportlistDel};
//		String name[]=new String[]{"添加报警","编辑报警","删除报警","添加报告","编辑报告","删除报告"};
//
//		for(int i=0;i<type.length;i++)
//		{
//			EccAction eccAction = new EccAction(EccAction.ACTION_FOR_TREENODE);
//			eccAction.setType(type[i]);
//			eccAction.setActionName(name[i]);
//			eccAction.setLicense(licence[i]);
//			eccAction.setNeedAuth(true);
//			eccAction.setImage(EccWebAppInit.getInstance().getActionImage(type[i]));
//			eccActionMap.put(type[i], eccAction);
//		}
//		
//	}
}
