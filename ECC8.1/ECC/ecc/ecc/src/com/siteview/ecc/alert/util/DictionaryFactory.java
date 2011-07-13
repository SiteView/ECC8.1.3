package com.siteview.ecc.alert.util;

import com.siteview.base.data.IniFile;
import com.siteview.ecc.alert.dao.AlertDaoImpl;
import com.siteview.ecc.alert.dao.AlertLogDaoImpl;
import com.siteview.ecc.alert.dao.IAlertDao;
import com.siteview.ecc.alert.dao.IAlertLogDao;
import com.siteview.ecc.alert.dao.IQueueDao;
import com.siteview.ecc.alert.dao.ITextTemplateDao;
import com.siteview.ecc.alert.dao.QueueDaoImpl;
import com.siteview.ecc.alert.dao.TextTemplateDaoImpl;

public class DictionaryFactory {
	/**
	 * 取得告警策略配置ini文件
	 * @return IniFile 
	 * @throws Exception
	 */
	public static IniFile getAlertPloy() throws Exception{
		return getIniFile("alertStrategy.ini");
	}
	/**
	 * 取得模板配置ini文件
	 * @return IniFile 
	 * @throws Exception
	 */
	public static IniFile getTemplate() throws Exception{
		return getIniFile("TXTTemplate.ini");
	}
	
	/**
	 * 取得值班配置ini文件
	 * @return IniFile 
	 * @throws Exception
	 */
	public static IniFile getWatchSheets()throws Exception{
		return getIniFile("watchsheetcfg.ini");
	}
	/**
	 * 取得email地址配置ini文件
	 * @return IniFile 
	 * @throws Exception
	 */
	public static IniFile getEmailAdress()throws Exception{
		return getIniFile("emailAdress.ini");
	}
	/**
	 * 取得短信号码配置ini文件
	 * @return IniFile 
	 * @throws Exception
	 */
	public static IniFile getSmsPhoneSet()throws Exception{
		return getIniFile("smsphoneset.ini");
	}
	/**
	 * 取得告警规则配置ini文件
	 * @return IniFile 
	 * @throws Exception
	 */
	public static IniFile getAlert()throws Exception{
		return getIniFile("alert.ini");
	}

	public static IniFile getIniFile(String fileName)throws Exception{
		IniFile iniFile = new IniFile(fileName);
		try{
			iniFile.load();
		}catch(Exception e){
			e.printStackTrace();
		}
		return iniFile;
	}

	
	/**
	 * 取得发送模板的Dao接口
	 * @return ITextTemplateDao
	 * @throws Exception
	 */
	public static ITextTemplateDao getITextTemplateDao() throws Exception{
		return new TextTemplateDaoImpl();
	}
	/**
	 * 取得告警规则的Dao接口
	 * @return IAlertDao
	 * @throws Exception
	 */
	public static IAlertDao getIAlertDao() throws Exception{
		return new AlertDaoImpl();
	}
	/**
	 * 取得告警日志的Dao接口
	 * @return IAlertLogDao
	 * @throws Exception
	 */
	public static IAlertLogDao getIAlertLogDao() throws Exception{
		return new AlertLogDaoImpl();
	}
	/**
	 * 取得消息队列的Dao接口
	 * @return IQueueDao
	 * @throws Exception
	 */
	public static IQueueDao getIQueueDao() throws Exception{
		return new QueueDaoImpl();
	}
}
