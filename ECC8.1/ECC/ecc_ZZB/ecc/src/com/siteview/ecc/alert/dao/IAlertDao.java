package com.siteview.ecc.alert.dao;

import com.siteview.ecc.alert.dao.bean.AccessControl;
import com.siteview.ecc.alert.dao.bean.AlertRuleQueryCondition;
import com.siteview.ecc.alert.dao.bean.BaseAlert;
import com.siteview.ecc.alert.dao.bean.EmailAlert;
import com.siteview.ecc.alert.dao.bean.SMSAlert;
import com.siteview.ecc.alert.dao.bean.ScriptAlert;
import com.siteview.ecc.alert.dao.bean.SoundAlert;
import com.siteview.ecc.alert.dao.type.AlertType;

public interface IAlertDao
{   
    /// <summary>
    /// 
    /// </summary>
    /// <param name="AccessInformation"></param>
    /// <param name="AlertInformation"></param>
    /// <returns></returns>
    String addEmailAlert(AccessControl AccessInformation, EmailAlert AlertInformation)throws Exception;
    /// <summary>
    /// 
    /// </summary>
    /// <param name="AccessInformation"></param>
    /// <param name="AlertInformation"></param>
    /// <returns></returns>
    String addScriptAlert(AccessControl AccessInformation, ScriptAlert AlertInformation)throws Exception;
    /// <summary>
    /// 
    /// </summary>
    /// <param name="AccessInformation"></param>
    /// <param name="AlertInformation"></param>
    /// <returns></returns>
    String addSMSAlert(AccessControl AccessInformation, SMSAlert AlertInformation)throws Exception;
    /// <summary>
    /// 
    /// </summary>
    /// <param name="AccessInformation"></param>
    /// <param name="AlertIndex"></param>
    /// <returns></returns>
    String addSoundAlert(AccessControl accessInformation, SoundAlert AlertInformation)throws Exception;
    /// <summary>
    /// 
    /// </summary>
    /// <param name="AccessInformation"></param>
    /// <param name="AlertInformation"></param>
    /// <returns></returns>
    void deleteAlert(AccessControl accessInformation, BaseAlert alertInformation)throws Exception;
    /// <summary>
    /// 
    /// </summary>
    /// <param name="AccessInformation"></param>
    /// <param name="AlertIndex"></param>
    /// <returns></returns>
    BaseAlert getEmailAlert(AccessControl accessInformation, String alertIndex)throws Exception;
    /// <summary>
    /// 
    /// </summary>
    /// <param name="AccessInformation"></param>
    /// <param name="AlertIndex"></param>
    /// <returns></returns>
    BaseAlert getScriptAlert(AccessControl accessInformation, String alertIndex)throws Exception;
    /// <summary>
    /// 
    /// </summary>
    /// <param name="AccessInformation"></param>
    /// <param name="AlertIndex"></param>
    /// <returns></returns>
    BaseAlert getSMSAlert(AccessControl accessInformation, String alertIndex)throws Exception;
    /// <summary>
    /// 
    /// </summary>
    /// <param name="AccessInformation"></param>
    /// <param name="AlertIndex"></param>
    /// <returns></returns>
    BaseAlert getSoundAlert(AccessControl accessInformation, String alertIndex)throws Exception;

    BaseAlert getAlertInformation(AccessControl accessInformation,AlertType alerttype, String alertIndex)throws Exception;
    /// <summary>
    /// 
    /// </summary>
    /// <param name="AccessInformation"></param>
    /// <param name="QueryCondition"></param>
    /// <returns></returns>
    BaseAlert[] queryAlertRule(AccessControl accessInformation, AlertRuleQueryCondition queryCondition)throws Exception;
    /// <summary>
    /// 
    /// </summary>
    /// <param name="AccessInformation"></param>
    /// <param name="AlertInformation"></param>
    /// <returns></returns>
    void updateEmailAlert(AccessControl accessInformation, EmailAlert alertInformation,Boolean checkname)throws Exception;

    /// <summary>
    /// 
    /// </summary>
    /// <param name="AccessInformation"></param>
    /// <param name="AlertInformation"></param>
    /// <returns></returns>
    void updateScriptAlert(AccessControl accessInformation, ScriptAlert alertInformation,Boolean checkname)throws Exception;
    /// <summary>
    /// 
    /// </summary>
    /// <param name="AccessInformation"></param>
    /// <param name="AlertInformation"></param>
    /// <returns></returns>
    void updateSMSAlert(AccessControl accessInformation, SMSAlert alertInformation,Boolean checkname)throws Exception;
    /// <summary>
    /// 
    /// </summary>
    /// <param name="AccessInformation"></param>
    /// <param name="AlertInformation"></param>
    /// <returns></returns>
    void updateSoundAlert(AccessControl accessInformation, SoundAlert alertInformation,Boolean checkname)throws Exception;

    void updateAlert(AccessControl accessInformation, BaseAlert alertInformation,Boolean checkname)throws Exception;
    void addAlert(AccessControl accessInformation, BaseAlert alertInformation)throws Exception;
    public String getAlertReceiver(BaseAlert basealert) throws Exception;
    
    public String[] getEmailAdresss() throws Exception;
    public String[] getTelphoneNo() throws Exception;
}

