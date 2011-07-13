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
	 * ȡ�ø澯��������ini�ļ�
	 * @return IniFile 
	 * @throws Exception
	 */
	public static IniFile getAlertPloy() throws Exception{
		return getIniFile("alertStrategy.ini");
	}
	/**
	 * ȡ��ģ������ini�ļ�
	 * @return IniFile 
	 * @throws Exception
	 */
	public static IniFile getTemplate() throws Exception{
		return getIniFile("TXTTemplate.ini");
	}
	
	/**
	 * ȡ��ֵ������ini�ļ�
	 * @return IniFile 
	 * @throws Exception
	 */
	public static IniFile getWatchSheets()throws Exception{
		return getIniFile("watchsheetcfg.ini");
	}
	/**
	 * ȡ��email��ַ����ini�ļ�
	 * @return IniFile 
	 * @throws Exception
	 */
	public static IniFile getEmailAdress()throws Exception{
		return getIniFile("emailAdress.ini");
	}
	/**
	 * ȡ�ö��ź�������ini�ļ�
	 * @return IniFile 
	 * @throws Exception
	 */
	public static IniFile getSmsPhoneSet()throws Exception{
		return getIniFile("smsphoneset.ini");
	}
	/**
	 * ȡ�ø澯��������ini�ļ�
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
	 * ȡ�÷���ģ���Dao�ӿ�
	 * @return ITextTemplateDao
	 * @throws Exception
	 */
	public static ITextTemplateDao getITextTemplateDao() throws Exception{
		return new TextTemplateDaoImpl();
	}
	/**
	 * ȡ�ø澯�����Dao�ӿ�
	 * @return IAlertDao
	 * @throws Exception
	 */
	public static IAlertDao getIAlertDao() throws Exception{
		return new AlertDaoImpl();
	}
	/**
	 * ȡ�ø澯��־��Dao�ӿ�
	 * @return IAlertLogDao
	 * @throws Exception
	 */
	public static IAlertLogDao getIAlertLogDao() throws Exception{
		return new AlertLogDaoImpl();
	}
	/**
	 * ȡ����Ϣ���е�Dao�ӿ�
	 * @return IQueueDao
	 * @throws Exception
	 */
	public static IQueueDao getIQueueDao() throws Exception{
		return new QueueDaoImpl();
	}
}
