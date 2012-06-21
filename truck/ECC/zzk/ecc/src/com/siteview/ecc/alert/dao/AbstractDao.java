package com.siteview.ecc.alert.dao;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import com.siteview.base.data.IniFile;
import com.siteview.ecc.alert.dao.bean.AccessControl;
import com.siteview.ecc.alert.dao.bean.AlertRuleQueryCondition;
import com.siteview.ecc.alert.dao.type.ErrorInfo;
import com.siteview.ecc.alert.util.DictionaryFactory;

public abstract class AbstractDao {
	
    

	
    public void checkAccessControlInformation(AccessControl accessInformation) throws Exception
    {

        if (accessInformation.isLimitServer())
        {

            if (accessInformation.getSVDBServer() == null || "".equals(accessInformation.getSVDBServer()))
            {
            	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AccessControlError));
            }
        }

        if (accessInformation.isLimitUser())
        {

        	if (accessInformation.getUserID() == null || "".equals(accessInformation.getUserID()))
            {
        		throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AccessControlError));
            }
        }
    }    
    public void checkAlertRuleQueryCondition(AlertRuleQueryCondition structQueryCondition) throws Exception
    {
        if (structQueryCondition.getLimitIndex())
        {
        	if (structQueryCondition.getAlertIndex() == null || "".equals(structQueryCondition.getAlertIndex()))
            {
        		throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.RuleQueryConditionError));
            }
        }
        if (structQueryCondition.getLimitTarget())
        {
        	if (structQueryCondition.getAlertTarget() == null || "".equals(structQueryCondition.getAlertTarget()))
            {
        		throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.RuleQueryConditionError));
            }
        }
        if (structQueryCondition.getLimitName())
        {
        	if (structQueryCondition.getAlertName() == null || "".equals(structQueryCondition.getAlertName()))
            {
        		throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.RuleQueryConditionError));
            }
        }

        if (structQueryCondition.getLimitType())
        {
        	if (structQueryCondition.getAlertType() == null)
            {
        		throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.RuleQueryConditionError));
            }
        }
        if (structQueryCondition.getLimitCategory())
        {
        	if (structQueryCondition.getAlertCategory() == null )
            {
        		throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.RuleQueryConditionError));
            }
        }

        if (structQueryCondition.getLimitTimes())
        {
        	if (structQueryCondition.getAlertTimes() == null)
            {
        		throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.RuleQueryConditionError));
            }
        }

        if (structQueryCondition.getLimitState())
        {
        	if (structQueryCondition.getAlertState() == null)
            {
        		throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.RuleQueryConditionError));
            }
        }
    }   
    /// <summary>
    /// email 格式是否正确
    /// </summary>
    /// <param name="szMailList"></param>
    /// <returns></returns>
    public boolean checkEmail(String szMailList)
    {
    	return szMailList.matches("[\\w\\.\\_]+[@]{1}[\\w]+[.]{1}[\\w]+");   
    }
    /// <summary>
    /// 数字判断
    /// </summary>
    /// <param name="strValue"></param>
    /// <returns></returns>
    public boolean isAlphaNumber(String strNumber)
    {
    	try{
        	new Double(strNumber);
        	return true;
    	}catch(Exception e){
    		return false;
    	}
    }
    /// <summary>
    /// 名称是否已存在
    /// </summary>
    /// <param name="strAlertName"></param>
    /// <returns></returns>
    public void checkAlertName(String strAlertName) throws Exception
    {
    	if (strAlertName == null || "".equals(strAlertName))throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertNameError));
        IniFile iniFile = DictionaryFactory.getAlert();
        
        for(String section : iniFile.getSectionList())
        {
        	Map<String,String> map = iniFile.getSectionData(section);
            String alertName = map.get("AlertName");
            if (alertName==null) continue;
            if (alertName.equals(strAlertName)) throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertNameError));
        }
    }    
    
    public int randIndex()
    {
        int nPort = 0;
        int nMin = 0x4000;
        int nMax = 0x7FFF;
        Random rdm1 = new Random(new Date().getTime());
        nPort = rdm1.nextInt(Integer.MAX_VALUE);
        nPort = nPort | nMin;
        nPort = nPort & nMax;
        return nPort;
    }    
    
}
