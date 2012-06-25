package com.siteview.ecc.alert.dao.type;

import java.util.HashMap;
import java.util.Map;

/**
 * ������Ϣ
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
	    keytext.put(AccessControlError.toString(), "���ݷ��ʿ�����Ϣȡ������");
	    keytext.put(RuleQueryConditionError.toString(), "���������ѯ������������");
	    keytext.put(LogQueryConditionError.toString(), "������־��ѯ������������");
	    keytext.put(AlertIDError.toString(), "Index����Ϊ�գ����ʽ����");
	    keytext.put(AlertTargetError.toString(), "û��ѡ���κμ����");
	    keytext.put(AlertNameError.toString(), "��������û����������������");
	    keytext.put(AlertNameNoExistError.toString(), "����������������");
	    keytext.put(AlertTypeError.toString(), "�����������Ͳ���");
	    keytext.put(AlertCategoryError.toString(), "�����������಻��");
	    keytext.put(AlertTimesError.toString(), "��������ģʽ����");
	    keytext.put(AlertAlwaysError.toString(), "����û��ָ�������ڵڼ���֮��(Always)");
	    keytext.put(AlertOnlyError.toString(), "����û��ָ�������ڵڼ���֮��(Only)");
	    keytext.put(AlertSelect1Error.toString(), "����û��ָ�������ڵڼ���֮��(Select1)");
	    keytext.put(AlertSelect2Error.toString(), "����û��ָ���ظ����͵Ĵ���(Select2)");
	    keytext.put(AlertStateError.toString(), "��ǰ��������Ч״̬���� ");
	    keytext.put(AlertDateTimeOrderError.toString(), "������ʼʱ�����ڱ�������ʱ�� ");
	    keytext.put(EmailAddresssError.toString(), "Email��ַָ������");
	    keytext.put(UpgradeTimesError.toString(), "����������ʽ����");
	    keytext.put(StopTimesError.toString(), "ֹͣ������ʽ����");
	    keytext.put(SmsNumberError.toString(), "���ŷ���Ŀ���ֻ���ʽ����");
	    keytext.put(SendModeError.toString(), "���ŷ���ģʽ����");
	    keytext.put(SMSTemplateError.toString(), "���ŷ���ģ��ָ������");
	    keytext.put(ScriptServerError.toString(), "�ű������ķ�����û��ָ��");
	    keytext.put(ServerIDError.toString(), "�ű������ķ�����ID�趨����");
	    keytext.put(ServerNameError.toString(), "���������ķ�������û��ָ��");
	    keytext.put(LoginNameError.toString(), "��¼���趨����");
	    keytext.put(PushMessageError.toString(), "��������ı���Ϣ���͵���Ϣ�����г���");
	    keytext.put(NoData.toString(), "û�в�ѯ������");
		
	}
    
	public static String getErrorMessage(ErrorInfo errorinfo)
	{
		if ( keytext.get(errorinfo.toString()) == null ){
			initData();
		}
		return keytext.get(errorinfo.toString());
	}
}
