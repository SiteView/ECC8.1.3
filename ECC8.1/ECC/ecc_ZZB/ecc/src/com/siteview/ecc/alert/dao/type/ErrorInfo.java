package com.siteview.ecc.alert.dao.type;

import java.util.HashMap;
import java.util.Map;

/**
 * 错误信息
 * @author hailong.yi
 *
 */
public enum ErrorInfo {
    AccessControlError,
    RuleQueryConditionError,
    LogQueryConditionError,
    AlertIDError,
    AlertTargetError,
    AlertNameError,
    AlertNameNoExistError,
    AlertTypeError,
    AlertCategoryError,
    AlertTimesError,
    AlertAlwaysError,
    AlertOnlyError,
    AlertSelect1Error,
    AlertSelect2Error,
    AlertStateError,
    AlertDateTimeOrderError,
    EmailAddresssError,
    UpgradeTimesError,
    StopTimesError,
    SmsNumberError,
    SendModeError,
    SMSTemplateError,
    ScriptServerError,
    ServerIDError,
    ServerNameError,
    LoginNameError,
    PushMessageError,
    NoData;
    
    public final static Map<String,String> keytext = new HashMap<String,String>();
	private static void initData(){
	    keytext.put(AccessControlError.toString(), "数据访问控制信息取得有误");
	    keytext.put(RuleQueryConditionError.toString(), "报警规则查询条件解析有误");
	    keytext.put(LogQueryConditionError.toString(), "报警日志查询条件解析有误");
	    keytext.put(AlertIDError.toString(), "Index不能为空，或格式错误");
	    keytext.put(AlertTargetError.toString(), "没有选择任何监测器");
	    keytext.put(AlertNameError.toString(), "报警规则没有命名，或者重名");
	    keytext.put(AlertNameNoExistError.toString(), "报警规则名不存在");
	    keytext.put(AlertTypeError.toString(), "报警规则类型不对");
	    keytext.put(AlertCategoryError.toString(), "报警条件分类不对");
	    keytext.put(AlertTimesError.toString(), "报警发送模式不对");
	    keytext.put(AlertAlwaysError.toString(), "报警没有指定发送在第几次之后(Always)");
	    keytext.put(AlertOnlyError.toString(), "报警没有指定发送在第几次之后(Only)");
	    keytext.put(AlertSelect1Error.toString(), "报警没有指定发送在第几次之后(Select1)");
	    keytext.put(AlertSelect2Error.toString(), "报警没有指定重复发送的次数(Select2)");
	    keytext.put(AlertStateError.toString(), "当前报警的有效状态不对 ");
	    keytext.put(AlertDateTimeOrderError.toString(), "报警开始时间晚于报警结束时间 ");
	    keytext.put(EmailAddresssError.toString(), "Email地址指定不对");
	    keytext.put(UpgradeTimesError.toString(), "升级次数格式不对");
	    keytext.put(StopTimesError.toString(), "停止次数格式不对");
	    keytext.put(SmsNumberError.toString(), "短信发送目的手机格式不对");
	    keytext.put(SendModeError.toString(), "短信发送模式不对");
	    keytext.put(SMSTemplateError.toString(), "短信发送模板指定不对");
	    keytext.put(ScriptServerError.toString(), "脚本报警的服务器没有指定");
	    keytext.put(ServerIDError.toString(), "脚本报器的服务器ID设定有误");
	    keytext.put(ServerNameError.toString(), "声音报警的服务器名没有指定");
	    keytext.put(LoginNameError.toString(), "登录名设定有误");
	    keytext.put(PushMessageError.toString(), "报警规则改变信息发送到消息队列中出错");
	    keytext.put(NoData.toString(), "没有查询到数据");
		
	}
    
	public static String getErrorMessage(ErrorInfo errorinfo)
	{
		if ( keytext.get(errorinfo.toString()) == null ){
			initData();
		}
		return keytext.get(errorinfo.toString());
	}
}
