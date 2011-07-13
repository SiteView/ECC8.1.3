package com.siteview.ecc.alert.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
 
import org.zkoss.zk.ui.Executions;

import com.siteview.base.data.IniFile;
import com.siteview.base.manage.View;
import com.siteview.ecc.alert.dao.bean.AccessControl;
import com.siteview.ecc.alert.dao.bean.AlertRuleQueryCondition;
import com.siteview.ecc.alert.dao.bean.BaseAlert;
import com.siteview.ecc.alert.dao.bean.EmailAlert;
import com.siteview.ecc.alert.dao.bean.SMSAlert;
import com.siteview.ecc.alert.dao.bean.ScriptAlert;
import com.siteview.ecc.alert.dao.bean.SoundAlert;
import com.siteview.ecc.alert.dao.type.AlertCategory;
import com.siteview.ecc.alert.dao.type.AlertState;
import com.siteview.ecc.alert.dao.type.AlertTimes;
import com.siteview.ecc.alert.dao.type.AlertType;
import com.siteview.ecc.alert.dao.type.ErrorInfo;
import com.siteview.ecc.alert.util.DictionaryFactory;
import com.siteview.ecc.log.AppendOperateLog;
import com.siteview.ecc.log.OpObjectId;
import com.siteview.ecc.log.OpTypeId;
import com.siteview.ecc.util.Toolkit;
import com.sun.org.apache.xalan.internal.lib.Extensions;

public class AlertDaoImpl extends AbstractDao implements IAlertDao {
	
    private IQueueDao queue = null;
	private static String OTHERS_TEXT = "其他";
	private static String OTHERS_TEXT2 = "其它";
	

    
 	public AlertDaoImpl() throws Exception{
 		queue = DictionaryFactory.getIQueueDao();
    }
 
 	private AlertType getAlertType(Map<String, String> map)
 	{
        String stye = (String) map.get("AlertType");
		return AlertType.getType(stye);
 	}
 	
    /// <summary>
    /// 设置基本信息
    /// </summary>
    /// <param name="AlertIndex"></param>
    /// <param name="sectionlists"></param>
    /// <returns></returns>
    private BaseAlert getIniBaseAlertInfo(String alertIndex, Map<String, String> map)
    {
    	if (map==null) return null;
		BaseAlert baseAlert = null;//new BaseAlert();
		
		AlertType alerttype = this.getAlertType(map);
	    if (alerttype == AlertType.EmailAlert){
	    	baseAlert = new EmailAlert();
	    }else  if (alerttype == AlertType.SmsAlert){
	    	baseAlert = new SMSAlert();
	    }else  if (alerttype == AlertType.ScriptAlert){
	    	baseAlert = new ScriptAlert();
	    }else  if (alerttype == AlertType.SoundAlert){
	    	baseAlert = new SoundAlert();
	    }else{
	    	return null;
	    }

        baseAlert.setId(alertIndex);
        baseAlert.setName(map == null ? "" : (String) map.get("AlertName"));
        baseAlert.setOnly(map == null ? "" : (String) map.get("OnlyTimes"));
        baseAlert.setSelect1(map == null ? "" : (String) map.get("SelTimes1"));
        baseAlert.setSelect2(map == null ? "" : (String) map.get("SelTimes2"));
        //baseAlert.setStrategy(map == null ? "" : (String) map.get("Strategy"));
        baseAlert.setState(AlertState.getType(map.get("AlertState")));
        baseAlert.setTarget(map == null ? "" : (String) map.get("AlertTarget"));
        String times = (String)map.get("AlertCond");
        baseAlert.setTimes(AlertTimes.getType(times));
        baseAlert.setAlways(map == null ? "" : (String) map.get("AlwaysTimes"));
        String category = (String) map.get("AlertCategory");
        baseAlert.setCategory((AlertCategory) AlertCategory.getType(category));
        return baseAlert;

    }   
    
    /// <summary>
    /// 设置BaseAlert信息
    /// </summary>
    /// <param name="alertInfo"></param>
    /// <param name="estr"></param>
    /// <returns></returns>
    IniFile setIniBaseAlertInfo(AccessControl accessInformation, BaseAlert alertInfo) throws Exception
    {

        

        String strAlertIndex = alertInfo.getId();

        if (strAlertIndex == null)
        {
            throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertIDError));
        }

        IniFile iniFile = DictionaryFactory.getAlert();
        if (iniFile.getSectionList().contains(strAlertIndex) == false){
        	iniFile.createSection(strAlertIndex);
        }
        iniFile.setKeyValue(strAlertIndex, "nIndex", alertInfo.getId()==null?"":alertInfo.getId());
        iniFile.setKeyValue(strAlertIndex, "AlertTarget", alertInfo.getTarget()==null?"":alertInfo.getTarget());
        iniFile.setKeyValue(strAlertIndex, "AlertName", alertInfo.getName()==null?"":alertInfo.getName());

        iniFile.setKeyValue(strAlertIndex, "AlertType", alertInfo.getType()==null?"":alertInfo.getType().toString());
       // iniFile.setKeyValue(strAlertIndex, "Strategy", alertInfo.getStrategy()==null?"":alertInfo.getStrategy());

        AlertCategory alertcategory = alertInfo.getCategory();
        if (alertcategory == null)
        {
            throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertCategoryError));
        }
        iniFile.setKeyValue(strAlertIndex, "AlertCategory", alertcategory.toString());
        iniFile.setKeyValue(strAlertIndex, "AlertCond", alertInfo.getTimes()==null?"3":alertInfo.getTimes().getStringVaule());

        iniFile.setKeyValue(strAlertIndex, "AlwaysTimes", "1");
        iniFile.setKeyValue(strAlertIndex, "OnlyTimes", "1");
        iniFile.setKeyValue(strAlertIndex, "SelTimes1", "2");
        iniFile.setKeyValue(strAlertIndex, "SelTimes2", "3");


        if (alertInfo.getTimes() == AlertTimes.Always)
        {

            if (!isAlphaNumber(alertInfo.getAlways()))
            {//AlertAlwaysError AlertOnlyError
            	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertAlwaysError));
            }
            iniFile.setKeyValue(strAlertIndex, "AlwaysTimes", alertInfo.getAlways()==null?"":alertInfo.getAlways());

        }
        else if (alertInfo.getTimes() == AlertTimes.Only)
        {

            if (!isAlphaNumber(alertInfo.getOnly()))
            {
            	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertOnlyError));
            }
            iniFile.setKeyValue(strAlertIndex, "AlwaysTimes", alertInfo.getOnly()==null?"":alertInfo.getOnly());
        }
        else
        {

            if (!isAlphaNumber(alertInfo.getSelect1()))
            {//
            	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertSelect1Error));
            }
            iniFile.setKeyValue(strAlertIndex, "SelTimes1", alertInfo.getSelect1()==null?"":alertInfo.getSelect1());

            if (!isAlphaNumber(alertInfo.getSelect2()))
            {
            	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertSelect2Error));
            }
            iniFile.setKeyValue(strAlertIndex, "SelTimes2", alertInfo.getSelect2()==null?"":alertInfo.getSelect2());
        }

        iniFile.setKeyValue(strAlertIndex, "AlertState", alertInfo.getState().getStringVaule());

        return iniFile;

    }    
    
    private String generateAlertIndex(AccessControl access) throws Exception
    {
        String strAlertIndex = "";
        Integer index = 0;
        IniFile iniFile = DictionaryFactory.getAlert();
        while (true)
        {
            index = randIndex();
            Map<String, String> rs = iniFile.getSectionData(index.toString());

            if (rs!=null && rs.size()>0)
            {
                continue;
            }
            else
            {
                strAlertIndex = index.toString();
                break;
            }
        }

        return strAlertIndex;
    }    
	@Override
	public String addEmailAlert(AccessControl accessInformation,
			EmailAlert alertInformation) throws Exception {

        checkAccessControlInformation(accessInformation);

        checkAlertName(alertInformation.getBaseInfo().getName());

		if (alertInformation.getEmailAddresss() ==null || "".equals(alertInformation.getEmailAddresss())){
			throw new Exception("请输入报警邮件接受地址");
		}
		if (alertInformation.getEmailTemplate() ==null || "".equals(alertInformation.getEmailTemplate())){
			throw new Exception("请输入Email模板");
		}

		if (alertInformation.getUpgradeTimes()!=null)
        {

            if (!isAlphaNumber(alertInformation.getUpgradeTimes()))
            {
            	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertTimesError));
            }
        }

        if (alertInformation.getStopTimes()!=null)
        {
            if (!isAlphaNumber(alertInformation.getStopTimes()))
            {
            	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertTimesError));
            }
        }
        

        String strAlertIndex = generateAlertIndex(accessInformation) ;
        alertInformation.getBaseInfo().setId(strAlertIndex) ;
        //数据存储。。
        IniFile iniFile = setIniBaseAlertInfo(accessInformation, alertInformation.getBaseInfo());
        iniFile.setKeyValue(strAlertIndex, "EmailAdress", alertInformation.getEmailAddresss());
        iniFile.setKeyValue(strAlertIndex, "OtherAdress", alertInformation.getOtherAddress());
        iniFile.setKeyValue(strAlertIndex, "EmailTemplate", alertInformation.getEmailTemplate());
        iniFile.setKeyValue(strAlertIndex, "WatchSheet", alertInformation.getWatchSheet());
        iniFile.setKeyValue(strAlertIndex, "Strategy", alertInformation.getAlertPloy());
        
        iniFile.setKeyValue(strAlertIndex, "Upgrade", alertInformation.getUpgradeTimes());
        iniFile.setKeyValue(strAlertIndex, "UpgradeTo", alertInformation.getReceiverAddress());
        iniFile.setKeyValue(strAlertIndex, "Stop", alertInformation.getStopTimes());
        iniFile.saveChange();
        //send message to service
        boolean meseagebool = this.queue.pushStringMessage("SiteView70-Alert", "IniChange", iniFile.getFileName() + "," + strAlertIndex + ",ADD");

        if (!meseagebool)
        {
        	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.PushMessageError));
        }
        
        View view =Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
		String loginname = view.getLoginName();
		String minfo=loginname+" "+"在"+OpObjectId.alert_rule.name+"中进行了  "+OpTypeId.add.name+"Email报警操作。";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.alert_rule);
        
        return strAlertIndex;
	}

	@Override
	public String addSMSAlert(AccessControl accessInformation,
			SMSAlert alertInformation) throws Exception {
        checkAccessControlInformation(accessInformation);

        checkAlertName(alertInformation.getBaseInfo().getName());
        //sms报警
		if (alertInformation.getSmsNumber() ==null || "".equals(alertInformation.getSmsNumber())){
			throw new Exception("请输入报警接收手机号");
		}
		if (alertInformation.getSendMode() ==null || "".equals(alertInformation.getSendMode())){
			throw new Exception("请输入发送方式");
		}
		if (alertInformation.getSMSTemplate() ==null || "".equals(alertInformation.getSMSTemplate())){
			throw new Exception("请输入短信模板");
		}


        if (alertInformation.getSmsNumber() == null)
        {
        	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.SmsNumberError));
        }

        if (alertInformation.getUpgradeTimes()!=null)
        {
            if (!isAlphaNumber(alertInformation.getUpgradeTimes()))
            {
            	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.UpgradeTimesError));
            }
        }

        if (alertInformation.getStopTimes()!=null)
        {
            if (!isAlphaNumber(alertInformation.getStopTimes()))
            {
            	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.StopTimesError));
            }
        }

        String strAlertIndex = generateAlertIndex(accessInformation);
        alertInformation.getBaseInfo().setId(strAlertIndex);
        //数据存储。。
        IniFile iniFile = setIniBaseAlertInfo(accessInformation, alertInformation.getBaseInfo());
        iniFile.setKeyValue(strAlertIndex, "SmsNumber", alertInformation.getSmsNumber());
        iniFile.setKeyValue(strAlertIndex, "OtherNumber", alertInformation.getOtherNumber());
        iniFile.setKeyValue(strAlertIndex, "SmsSendMode", alertInformation.getSendMode());
        iniFile.setKeyValue(strAlertIndex, "SmsTemplate", alertInformation.getSMSTemplate());
        iniFile.setKeyValue(strAlertIndex, "WatchSheet", alertInformation.getWatchSheet());
        iniFile.setKeyValue(strAlertIndex, "Strategy", alertInformation.getAlertPloy());

        iniFile.setKeyValue(strAlertIndex, "Upgrade", alertInformation.getUpgradeTimes());
        iniFile.setKeyValue(strAlertIndex, "UpgradeTo", alertInformation.getReceiverAddress());
        iniFile.setKeyValue(strAlertIndex, "Stop", alertInformation.getStopTimes());
        iniFile.saveChange();
        //send message to service
        boolean meseagebool = this.queue.pushStringMessage("SiteView70-Alert", "IniChange", iniFile.getFileName() + "," + strAlertIndex + ",ADD");

        if (!meseagebool)
        {
        	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.PushMessageError));
        }
        
        View view =Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
		String loginname = view.getLoginName();
		String minfo=loginname+" "+"在"+OpObjectId.alert_rule.name+"中进行了  "+OpTypeId.add.name+"短信息报警操作。";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.alert_rule);
        
        return strAlertIndex;
	}

	@Override
	public String addScriptAlert(AccessControl accessInformation,
			ScriptAlert alertInformation) throws Exception {
        checkAccessControlInformation(accessInformation);

        checkAlertName(alertInformation.getBaseInfo().getName());

        if (alertInformation.getScriptServer() ==null || "".equals(alertInformation.getScriptServer())){
			throw new Exception("请选择服务器");
		}
		if (alertInformation.getServerID() ==null || "".equals(alertInformation.getServerID())){
			throw new Exception("请选择脚本");
		}

        //script报警
        String strAlertIndex = generateAlertIndex(accessInformation);
        alertInformation.getBaseInfo().setId(strAlertIndex);
        //数据存储。。
        IniFile iniFile = setIniBaseAlertInfo(accessInformation, alertInformation.getBaseInfo());
        iniFile.setKeyValue(strAlertIndex, "ScriptServer", alertInformation.getScriptServer());
        iniFile.setKeyValue(strAlertIndex, "ScriptServerID", alertInformation.getServerID());
        iniFile.setKeyValue(strAlertIndex, "ScriptFile", alertInformation.getScriptFile());
        iniFile.setKeyValue(strAlertIndex, "ScriptParam", alertInformation.getScriptParam());
        iniFile.setKeyValue(strAlertIndex, "Strategy", alertInformation.getAlertPloy());
        iniFile.saveChange();

        //send message to service
        boolean meseagebool = this.queue.pushStringMessage("SiteView70-Alert", "IniChange", iniFile.getFileName() + "," + strAlertIndex + ",ADD");

        if (!meseagebool)
        {
        	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.PushMessageError));
        }
        
        View view =Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
		String loginname = view.getLoginName();
		String minfo=loginname+" "+"在"+OpObjectId.alert_rule.name+"中进行了  "+OpTypeId.add.name+"脚本报警操作。";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.alert_rule);  
        
        return strAlertIndex;
	}

	@Override
	public String addSoundAlert(AccessControl accessInformation,
			SoundAlert alertInformation) throws Exception {
        checkAccessControlInformation(accessInformation);

        checkAlertName(alertInformation.getBaseInfo().getName());
		if (alertInformation.getServerName() ==null || "".equals(alertInformation.getServerName())){
			throw new Exception("请输入服务器名");
		}
		if (alertInformation.getLoginName() ==null || "".equals(alertInformation.getLoginName())){
			throw new Exception("请输入登录名");
		}
        //script报警

        String strAlertIndex = generateAlertIndex(accessInformation);
        alertInformation.getBaseInfo().setId(strAlertIndex);
        //数据存储。。
        IniFile iniFile = setIniBaseAlertInfo(accessInformation, alertInformation.getBaseInfo());
        iniFile.setKeyValue(strAlertIndex, "Server", alertInformation.getServerName());
        iniFile.setKeyValue(strAlertIndex, "LoginName", alertInformation.getLoginName());
        iniFile.setKeyValue(strAlertIndex, "LoginPwd", alertInformation.getLoginPassword());
        iniFile.setKeyValue(strAlertIndex, "Strategy", alertInformation.getAlertPloy());

        iniFile.saveChange();
        //send message to service
        boolean meseagebool = this.queue.pushStringMessage("SiteView70-Alert", "IniChange", iniFile.getFileName() + "," + strAlertIndex + ",ADD");

        if (!meseagebool)
        {
        	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.PushMessageError));
        }
        

        View view =Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
		String loginname = view.getLoginName();
		String minfo=loginname+" "+"在"+OpObjectId.alert_rule.name+"中进行了  "+OpTypeId.add.name+"声音报警操作。";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.add, OpObjectId.alert_rule);   
		
        return strAlertIndex;
	}


	@Override
	public void deleteAlert(AccessControl accessInformation,
			BaseAlert alertInformation) throws Exception {
        checkAccessControlInformation(accessInformation);

        IniFile iniFile = DictionaryFactory.getAlert();
        iniFile.deleteSection(alertInformation.getId());
        iniFile.saveChange();
        //send message to service
        boolean meseagebool = this.queue.pushStringMessage("SiteView70-Alert", "IniChange", iniFile.getFileName() + "," + alertInformation.getId() + ",DELETE");

        if (!meseagebool)
        {
        	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.PushMessageError));
        }
        
        View view =Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
		String loginname = view.getLoginName();
		String minfo=loginname+" "+"在"+OpObjectId.alert_rule.name+"中进行了  "+OpTypeId.del.name+"报警操作。";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.del, OpObjectId.alert_rule);
	}

	@Override
	public BaseAlert getEmailAlert(AccessControl accessInformation,
			String alertIndex) throws Exception {
        EmailAlert email = new EmailAlert();
        checkAccessControlInformation(accessInformation);

        IniFile iniFile = DictionaryFactory.getAlert();
        Map<String, String> map = iniFile.getSectionData(alertIndex);
        email.setBaseInfo(getIniBaseAlertInfo(alertIndex, map));
        email.setEmailAddresss(map == null ? null : (String) map.get("EmailAdress"));
        email.setOtherAddress(map == null ? null : (String) map.get("OtherAdress"));
        email.setEmailTemplate(map == null ? null : (String) map.get("EmailTemplate"));
        email.setUpgradeTimes(map == null ? null : (String) map.get("Upgrade"));
        email.setReceiverAddress(map == null ? null : (String) map.get("UpgradeTo"));
        email.setStopTimes(map == null ? null : (String) map.get("Stop"));
        email.setWatchSheet(map == null ? null : (String) map.get("WatchSheet"));
        email.setAlertPloy(map == null ? null : (String) map.get("Strategy"));
        return email;
    }

	@Override
	public BaseAlert getSMSAlert(AccessControl accessInformation,
			String alertIndex) throws Exception {
        SMSAlert sms = new SMSAlert();
        checkAccessControlInformation(accessInformation);

        IniFile iniFile = DictionaryFactory.getAlert();
        Map<String, String> map = iniFile.getSectionData(alertIndex);
        sms.setBaseInfo(getIniBaseAlertInfo(alertIndex, map));
        sms.setSmsNumber(map == null ? null : (String) map.get("SmsNumber"));
        sms.setOtherNumber(map == null ? null : (String) map.get("OtherNumber"));
        sms.setSendMode(map == null ? null : (String) map.get("SmsSendMode"));
        sms.setSMSTemplate(map == null ? null : (String) map.get("SmsTemplate"));
        sms.setUpgradeTimes(map == null ? null : (String) map.get("Upgrade"));
        sms.setReceiverAddress(map == null ? null : (String) map.get("UpgradeTo"));
        sms.setStopTimes(map == null ? null : (String) map.get("Stop"));
        sms.setWatchSheet(map == null ? null : (String) map.get("WatchSheet"));
        sms.setAlertPloy(map == null ? null : (String) map.get("Strategy"));
        return sms;
    }

	@Override
	public BaseAlert getScriptAlert(AccessControl accessInformation,
			String alertIndex) throws Exception {
		ScriptAlert script = new ScriptAlert();
        checkAccessControlInformation(accessInformation);
        IniFile iniFile = DictionaryFactory.getAlert();
        Map<String, String> map = iniFile.getSectionData(alertIndex);
        script.setBaseInfo(getIniBaseAlertInfo(alertIndex, map));
        script.setScriptServer(map == null ? null : (String) map.get("ScriptServer"));
//        script.setServerID(map == null ? null : (String) map.get("ScriptServerID"));
        script.setServerID(map == null ? null : ((String) map.get("ScriptServerID")==null
        		?(String) map.get("ScriptServerId"):(String) map.get("ScriptServerID")));//兼容以前版本中ScriptServerId
        script.setScriptFile(map == null ? null : (String) map.get("ScriptFile"));
        script.setScriptParam(map == null ? null : (String) map.get("ScriptParam"));
        script.setAlertPloy(map == null ? null : (String) map.get("Strategy"));
        return script;
     }

	@Override
	public BaseAlert getSoundAlert(AccessControl accessInformation,
			String alertIndex) throws Exception {
		SoundAlert sound = new SoundAlert();
        checkAccessControlInformation(accessInformation);
        IniFile iniFile = DictionaryFactory.getAlert();
        Map<String, String> map = iniFile.getSectionData(alertIndex);
        sound.setBaseInfo(getIniBaseAlertInfo(alertIndex, map));
        sound.setServerName(map == null ? null : (String) map.get("Server"));
        sound.setLoginName(map == null ? null : (String) map.get("LoginName"));
        sound.setLoginPassword(map == null ? null : (String) map.get("LoginPwd"));
        sound.setAlertPloy(map == null ? null : (String) map.get("Strategy"));
        return sound;
	}
	@Override
	public BaseAlert getAlertInformation(AccessControl accessInformation,AlertType alerttype, String alertIndex)throws Exception
	{
    	if (alerttype == AlertType.EmailAlert){
			return this.getEmailAlert(accessInformation, alertIndex);
    	} else if (alerttype == AlertType.SmsAlert){
   			return this.getSMSAlert(accessInformation, alertIndex);
    	} else if (alerttype == AlertType.ScriptAlert){
   			return this.getScriptAlert(accessInformation, alertIndex);
    	} else if (alerttype == AlertType.SoundAlert){
   			return this.getSoundAlert(accessInformation, alertIndex);
    	} else{
    		throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertTypeError));
    	}
	}
	@Override
	public BaseAlert[] queryAlertRule(AccessControl accessInformation,
			AlertRuleQueryCondition queryCondition) throws Exception {
        checkAccessControlInformation(accessInformation);

        checkAlertRuleQueryCondition(queryCondition);
        
        IniFile iniFile = DictionaryFactory.getAlert();
        
        List<String> sectionlists = iniFile.getSectionList();
        List<BaseAlert> baseAlerts = new ArrayList<BaseAlert>();
        for (String section : sectionlists)
        {
        	AlertType alerttype = this.getAlertType(iniFile.getSectionData(section));
   			baseAlerts.add(this.getAlertInformation(accessInformation,alerttype, section));
           
        }
        return (baseAlerts.toArray(new BaseAlert[baseAlerts.size()]));

	}

	@Override
	public void updateEmailAlert(AccessControl accessInformation,
			EmailAlert alertInformation, Boolean checkname) throws Exception {

        checkAccessControlInformation(accessInformation);

        if (checkname)
        {
        	try{
        		checkAlertName(alertInformation.getBaseInfo().getName());
        		throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertNameNoExistError));
        	}catch(Exception e){
        		
        	}
        }
        //email报警
        //if (string.IsNullOrEmpty(AlertInformation.EmailAddresss))
        //{
        //    ret = false;
        //    estr = keytext[ErrorInfo.EmailAddresssError];
        //    break;
        //}


        //if (!string.IsNullOrEmpty(AlertInformation.EmailAddresss))
        //{
        //    if (!CheckEmail(AlertInformation.OtherAddress))
        //    {
        //        ret = false;
        //        estr = keytext[ErrorInfo.EmailAddresssError];
        //        break;
        //    }
        //}

        
		if (alertInformation.getEmailAddresss() ==null || "".equals(alertInformation.getEmailAddresss())){
			throw new Exception("请输入报警邮件接受地址");
		}
		if (alertInformation.getEmailTemplate() ==null || "".equals(alertInformation.getEmailTemplate())){
			throw new Exception("请输入Email模板");
		}
        
        if (alertInformation.getUpgradeTimes()!=null)
        {

            if (!isAlphaNumber(alertInformation.getUpgradeTimes()))
            {
                throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertTimesError));
            }
        }

        if (alertInformation.getStopTimes()!=null)
        {
            if (!isAlphaNumber(alertInformation.getStopTimes()))
            {
                throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertTimesError));
            }
        }


        String strAlertIndex = alertInformation.getBaseInfo().getId();
        //数据存储。。
        IniFile iniFile = setIniBaseAlertInfo(accessInformation, alertInformation.getBaseInfo());
        iniFile.setKeyValue(strAlertIndex, "EmailAdress", alertInformation.getEmailAddresss());
        iniFile.setKeyValue(strAlertIndex, "OtherAdress", alertInformation.getOtherAddress());
        iniFile.setKeyValue(strAlertIndex, "EmailTemplate", alertInformation.getEmailTemplate());
        iniFile.setKeyValue(strAlertIndex, "WatchSheet", alertInformation.getWatchSheet());
        iniFile.setKeyValue(strAlertIndex, "Strategy", alertInformation.getAlertPloy());

        iniFile.setKeyValue(strAlertIndex, "Upgrade", alertInformation.getUpgradeTimes());
        iniFile.setKeyValue(strAlertIndex, "UpgradeTo", alertInformation.getReceiverAddress());
        iniFile.setKeyValue(strAlertIndex, "Stop", alertInformation.getStopTimes());
        iniFile.saveChange();
         //send message to service
        boolean meseagebool = this.queue.pushStringMessage("SiteView70-Alert", "IniChange", iniFile.getFileName() + "," + strAlertIndex + ",EDIT");

        if (!meseagebool)
        {
            throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.PushMessageError));
        }
	}

	@Override
	public void updateSMSAlert(AccessControl accessInformation,
			SMSAlert alertInformation, Boolean checkname) throws Exception {
        checkAccessControlInformation(accessInformation);

        if (checkname)
        {
        	try{
        		checkAlertName(alertInformation.getBaseInfo().getName());
        		throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertNameNoExistError));
        	}catch(Exception e){
        		
        	}
        }
		if (alertInformation.getSmsNumber() ==null || "".equals(alertInformation.getSmsNumber())){
			throw new Exception("请输入报警接收手机号");
		}
		if (alertInformation.getSendMode() ==null || "".equals(alertInformation.getSendMode())){
			throw new Exception("请输入发送方式");
		}
		if (alertInformation.getSMSTemplate() ==null || "".equals(alertInformation.getSMSTemplate())){
			throw new Exception("请输入短信模板");
		}

        if (alertInformation.getSmsNumber()==null)
        {
        	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.SmsNumberError));
        }

        if (alertInformation.getUpgradeTimes()!=null)
        {
            if (!isAlphaNumber(alertInformation.getUpgradeTimes()))
            {
            	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.UpgradeTimesError));
            }
        }

        if (alertInformation.getStopTimes()!=null)
        {
            if (!isAlphaNumber(alertInformation.getStopTimes()))
            {
            	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.StopTimesError));
            }
        }

        //数据存储。。
        String strAlertIndex = alertInformation.getBaseInfo().getId();
        IniFile iniFile = setIniBaseAlertInfo(accessInformation, alertInformation.getBaseInfo());

        iniFile.setKeyValue(strAlertIndex, "SmsNumber", alertInformation.getSmsNumber());
        iniFile.setKeyValue(strAlertIndex, "OtherNumber", alertInformation.getOtherNumber());
        iniFile.setKeyValue(strAlertIndex, "SmsSendMode", alertInformation.getSendMode());
        iniFile.setKeyValue(strAlertIndex, "SmsTemplate", alertInformation.getSMSTemplate());
        iniFile.setKeyValue(strAlertIndex, "WatchSheet", alertInformation.getWatchSheet());
        iniFile.setKeyValue(strAlertIndex, "Strategy", alertInformation.getAlertPloy());
        
        iniFile.setKeyValue(strAlertIndex, "Upgrade", alertInformation.getUpgradeTimes());
        iniFile.setKeyValue(strAlertIndex, "UpgradeTo", alertInformation.getReceiverAddress());
        iniFile.setKeyValue(strAlertIndex, "Stop", alertInformation.getStopTimes());
        iniFile.saveChange();
        //send message to service
        boolean meseagebool = this.queue.pushStringMessage("SiteView70-Alert", "IniChange", iniFile.getFileName() + "," + strAlertIndex + ",EDIT");

        if (!meseagebool)
        {
        	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.PushMessageError));
        }

	}

	@Override
	public void updateScriptAlert(AccessControl accessInformation,
			ScriptAlert alertInformation, Boolean checkname) throws Exception {
        checkAccessControlInformation(accessInformation);

        if (checkname)
        {
        	try{
        		checkAlertName(alertInformation.getBaseInfo().getName());
        		throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertNameNoExistError));
        	}catch(Exception e){
        		
        	}
        }
		if (alertInformation.getScriptServer() ==null || "".equals(alertInformation.getScriptServer())){
			throw new Exception("请选择服务器");
		}
		if (alertInformation.getServerID() ==null || "".equals(alertInformation.getServerID())){
			throw new Exception("请选择脚本");
		}

        String strAlertIndex = alertInformation.getBaseInfo().getId();
        //数据存储。。
        IniFile iniFile = setIniBaseAlertInfo(accessInformation, alertInformation.getBaseInfo());

        iniFile.setKeyValue(strAlertIndex, "ScriptServer", alertInformation.getScriptServer());
        iniFile.setKeyValue(strAlertIndex, "ScriptServerID", alertInformation.getServerID());
        iniFile.setKeyValue(strAlertIndex, "ScriptFile", alertInformation.getScriptFile());
        iniFile.setKeyValue(strAlertIndex, "ScriptParam", alertInformation.getScriptParam());
        iniFile.setKeyValue(strAlertIndex, "Strategy", alertInformation.getAlertPloy());
        iniFile.saveChange();

        //send message to service
        boolean meseagebool = this.queue.pushStringMessage("SiteView70-Alert", "IniChange", iniFile.getFileName() + "," + strAlertIndex + ",EDIT");

        if (!meseagebool)
        {
        	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.PushMessageError));
        }
	}

	@Override
	public void updateSoundAlert(AccessControl accessInformation,
			SoundAlert alertInformation, Boolean checkname) throws Exception {
        checkAccessControlInformation(accessInformation);

        if (checkname)
        {
        	try{
        		checkAlertName(alertInformation.getBaseInfo().getName());
        		throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertNameNoExistError));
        	}catch(Exception e){
        		
        	}
        	
        }
		if (alertInformation.getServerName() ==null || "".equals(alertInformation.getServerName())){
			throw new Exception("请输入服务器名");
		}
		if (alertInformation.getLoginName() ==null || "".equals(alertInformation.getLoginName())){
			throw new Exception("请输入登录名");
		}
        //script报警
        String strAlertIndex = alertInformation.getBaseInfo().getId();
        //数据存储。。
        IniFile iniFile = setIniBaseAlertInfo(accessInformation, alertInformation.getBaseInfo());
        iniFile.setKeyValue(strAlertIndex, "Server", alertInformation.getServerName());
        iniFile.setKeyValue(strAlertIndex, "LoginName", alertInformation.getLoginName());
        iniFile.setKeyValue(strAlertIndex, "LoginPwd", alertInformation.getLoginPassword());
        iniFile.setKeyValue(strAlertIndex, "Strategy", alertInformation.getAlertPloy());
        iniFile.saveChange();
        
        //send message to service
        boolean meseagebool = this.queue.pushStringMessage("SiteView70-Alert", "IniChange", iniFile.getFileName() + "," + strAlertIndex + ",EDIT");

        if (!meseagebool)
        {
        	throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.PushMessageError));
        }

	}

	@Override
	public void updateAlert(AccessControl accessInformation,
			BaseAlert alertInformation, Boolean checkname) throws Exception {
		this.checkBaseAlerft(alertInformation);
		if (alertInformation.getType() == AlertType.EmailAlert){
			updateEmailAlert(accessInformation, (EmailAlert)alertInformation, checkname);
		}else if (alertInformation.getType() == AlertType.SmsAlert){
			updateSMSAlert(accessInformation, (SMSAlert)alertInformation, checkname);
		}else if (alertInformation.getType() == AlertType.ScriptAlert){
			updateScriptAlert(accessInformation, (ScriptAlert)alertInformation, checkname);
		}else if (alertInformation.getType() == AlertType.SoundAlert){
			updateSoundAlert(accessInformation, (SoundAlert)alertInformation, checkname);
		}else{
			throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertTypeError));
		}
		
        View view =Toolkit.getToolkit().getSvdbView(Executions.getCurrent().getDesktop());
		String loginname = view.getLoginName();
		String minfo=loginname+" "+"在"+OpObjectId.alert_rule.name+"中进行了  "+OpTypeId.edit.name+"报警操作。";
		AppendOperateLog.addOneLog(loginname, minfo, OpTypeId.edit, OpObjectId.alert_rule);
	}

	public String getAlertReceiver(BaseAlert basealert) throws Exception{
		IniFile emailAdressIniFile = DictionaryFactory.getEmailAdress();
		IniFile smsphonesetIniFile = DictionaryFactory.getSmsPhoneSet();

		String receiverAddress = null;
		
		if (basealert instanceof EmailAlert ) {
			EmailAlert emailalert = ((EmailAlert)basealert);
			if (OTHERS_TEXT.equals(emailalert.getEmailAddresss().trim())||OTHERS_TEXT2.equals(emailalert.getEmailAddresss().trim())){
				receiverAddress = emailalert.getOtherAddress();
			}else{
				receiverAddress = "";
				for (String emailaddress : emailalert.getEmailAddresss().split(",")){
					for (String section : emailAdressIniFile.getSectionList()){
						if (section == null) continue;
						String value = emailAdressIniFile.getValue(section, "Name");
						if (value == null) continue;
						if (value.equals(emailaddress)){
							if (receiverAddress.length() != 0){
								receiverAddress = receiverAddress + ",";
							}
							receiverAddress = receiverAddress + emailAdressIniFile.getValue(section, "MailList");
						}
					}
					
				}
			}
			
		}else if (basealert instanceof SMSAlert){
			SMSAlert smsalert = ((SMSAlert)basealert);
			if (OTHERS_TEXT.equals(smsalert.getSmsNumber())||OTHERS_TEXT2.equals(smsalert.getSmsNumber())){
				receiverAddress = smsalert.getOtherNumber();
			}else{
				receiverAddress = "";
				for (String emailaddress : smsalert.getSmsNumber().split(",")){
					for (String section : smsphonesetIniFile.getSectionList()){
						if (section == null) continue;
						String value = smsphonesetIniFile.getValue(section, "Name");
						if (value == null) continue;
						if (value.equals(emailaddress)){
							if (receiverAddress.length() != 0){
								receiverAddress = receiverAddress + ",";
							}
							receiverAddress = receiverAddress + smsphonesetIniFile.getValue(section, "Phone");
						}
					}
					
				}
			}
		}else if (basealert instanceof ScriptAlert){
			ScriptAlert scriptalert = ((ScriptAlert)basealert);
			if (scriptalert.getScriptServer().contains("_win")){
				receiverAddress = scriptalert.getScriptServer();//.substring(0,scriptalert.getScriptServer().indexOf("(_win)"));
				
			}else{
				if (scriptalert.getScriptServer().contains("_unix")){
					receiverAddress = scriptalert.getScriptServer();
				}else{
					receiverAddress = scriptalert.getScriptServer();//.substring(0,scriptalert.getScriptServer().indexOf("."));
				}
			}
		}else if (basealert instanceof SoundAlert){
			SoundAlert soundalert = ((SoundAlert)basealert);
			receiverAddress = soundalert.getServerName();
		}
		return receiverAddress;
	}

	@Override
	public String[] getEmailAdresss() throws Exception {
		IniFile emailAdressIniFile = DictionaryFactory.getEmailAdress();
		List<String> retlist = new ArrayList<String>();
		for (String section : emailAdressIniFile.getSectionList()){
			String bCheck = emailAdressIniFile.getValue(section, "bCheck");
			String name = emailAdressIniFile.getValue(section, "Name");
			if (name == null) continue;
			if ("1".equals(bCheck)){
				retlist.add(name + "(禁止)");
			}else{
				retlist.add(name);
			}
			
		}
		
		return retlist.toArray(new String[retlist.size()]);
	}

	@Override
	public String[] getTelphoneNo() throws Exception {
		IniFile smsphonesetIniFile = DictionaryFactory.getSmsPhoneSet();
		List<String> retlist = new ArrayList<String>();
		for (String section : smsphonesetIniFile.getSectionList()){
			String status = smsphonesetIniFile.getValue(section, "Status");
			String name = smsphonesetIniFile.getValue(section, "Name");
			if (name == null) continue;
			if ("No".equals(status)){
				retlist.add(name + "(禁止)");
			}else{
				retlist.add(name);
			}
			
		}
		
		return retlist.toArray(new String[retlist.size()]);
	}

	@Override
	public void addAlert(AccessControl accessInformation,
			BaseAlert alertInformation) throws Exception {
		this.checkBaseAlerft(alertInformation);
		if (alertInformation.getType() == AlertType.EmailAlert){
			addEmailAlert(accessInformation, (EmailAlert)alertInformation);
		}else if (alertInformation.getType() == AlertType.SmsAlert){
			addSMSAlert(accessInformation, (SMSAlert)alertInformation);
		}else if (alertInformation.getType() == AlertType.ScriptAlert){
			addScriptAlert(accessInformation, (ScriptAlert)alertInformation);
		}else if (alertInformation.getType() == AlertType.SoundAlert){
			addSoundAlert(accessInformation, (SoundAlert)alertInformation);
		}else{
			throw new Exception(ErrorInfo.getErrorMessage(ErrorInfo.AlertTypeError));
		}
	}
	
	private void checkBaseAlerft(BaseAlert alertInformation) throws Exception{
		if (alertInformation.getTarget() == null || "".equals(alertInformation.getTarget())){
			throw new Exception("监测范围不能为空");
		}

		if (alertInformation.getName() == null || "".equals(alertInformation.getName().trim())){
			throw new Exception("请输入报警名称");
		}
		
		if (alertInformation.getCategory() == null ){
			throw new Exception("请选择报警事件");
		}
		
		if (alertInformation.getTimes() == AlertTimes.Always){
			if (alertInformation.getAlways() == null || "".equals(alertInformation.getAlways().trim())){
				throw new Exception("报警次数不合法");
			}else if (new Integer(alertInformation.getAlways()) <= 0){
				throw new Exception("报警次数必须大于0");
			}
		}else if (alertInformation.getTimes() == AlertTimes.Only){
			if (alertInformation.getOnly() == null || "".equals(alertInformation.getOnly().trim())){
				throw new Exception("报警次数不合法");
			}else if (new Integer(alertInformation.getOnly()) <= 0){
				throw new Exception("报警次数必须大于0");
			}
		}else if (alertInformation.getTimes() == AlertTimes.Select){
			if (alertInformation.getSelect1() == null || "".equals(alertInformation.getSelect1().trim())){
				throw new Exception("报警次数不合法");
			}else if (new Integer(alertInformation.getSelect1()) <= 0){
				throw new Exception("报警次数必须大于0");
			}
			if (alertInformation.getSelect2() == null || "".equals(alertInformation.getSelect2().trim())){
				throw new Exception("重复次数不合法");
			}else if (new Integer(alertInformation.getSelect2()) <= 0){
				throw new Exception("重复次数必须大于0");
			}
		}		
	}

}
