using System;
using System.Collections.Generic;
using System.Text;
using SiteView.Ecc.Core.Dao;
using System.Collections.Specialized;
using SiteView.Ecc.WSClient.XFireWebService;
using SiteView.Ecc.Core;

namespace SiteView.Ecc.WSClient
{
    public class AlertLogDaoImpl : IAlertLogDao
    {
        private static log4net.ILog logger = log4net.LogManager.GetLogger(typeof(AlertLogDaoImpl));
        private InterfaceEccService service = new InterfaceEccService();
        private static NameValueCollection keytext = new NameValueCollection();
        #region IAlertLogDao ��Ա
        /// <summary>
        /// ������Ϣ
        /// </summary>
        enum ErrorInfo
        {
            AccessControlError,
            RuleQueryConditionError,
            LogQueryConditionError,
            AlertIDError,
            AlertTargetError,
            AlertNameError,
            AlertTypeError,
            AlertCategoryError,
            AlertTimesError,
            AlertAlwaysError,
            AlertOnlyError,
            AlertSelect1Error,
            AlertSelect2Error,
            AlertStateError,
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
            PushMessageError
        }
        public AlertLogDaoImpl()
        {
            keytext.Clear();
            keytext.Add("AccessControlError", "���ݷ��ʿ�����Ϣȡ������");
            keytext.Add("RuleQueryConditionError", "���������ѯ������������");
            keytext.Add("LogQueryConditionError", "������־��ѯ������������");
            keytext.Add("AlertIDError", "Index����Ϊ�գ����ʽ����");
            keytext.Add("AlertTargetError", "û��ѡ���κμ����");
            keytext.Add("AlertNameError", "��������û����������������");
            keytext.Add("AlertTypeError", "�����������Ͳ���");
            keytext.Add("AlertCategoryError", "�����������಻��");
            keytext.Add("AlertTimesError", "��������ģʽ����");
            keytext.Add("AlertAlwaysError", "����û��ָ�������ڵڼ���֮��(Always)");
            keytext.Add("AlertOnlyError", "����û��ָ�������ڵڼ���֮��(Only)");
            keytext.Add("AlertSelect1Error", "����û��ָ�������ڵڼ���֮��(Select1)");
            keytext.Add("AlertSelect2Error", "����û��ָ���ظ����͵Ĵ���(Select2)");
            keytext.Add("AlertStateError", "��ǰ��������Ч״̬���� ");
            keytext.Add("EmailAddresssError", "Email��ַָ������");
            keytext.Add("UpgradeTimesError", "����������ʽ����");
            keytext.Add("StopTimesError", "ֹͣ������ʽ����");
            keytext.Add("SmsNumberError", "���ŷ���Ŀ���ֻ���ʽ����");
            keytext.Add("SendModeError", "���ŷ���ģʽ����");
            keytext.Add("SMSTemplateError", "���ŷ���ģ��ָ������");
            keytext.Add("ScriptServerError", "�ű������ķ�����û��ָ��");
            keytext.Add("ServerIDError", "�ű������ķ�����ID�趨����");
            keytext.Add("ServerNameError", "���������ķ�������û��ָ��");
            keytext.Add("LoginNameError", "��¼���趨����");
            keytext.Add("PushMessageError", "��������ı���Ϣ���͵���Ϣ�����г���");
        }
        /// <summary>
        /// //���ʿ�����Ϣ��ȡ��
        /// </summary>
        /// <returns></returns>
        private bool GetAccessControlInformation(AccessControl AccessInformation)
        {

            if (AccessInformation.LimitServer)
            {

                if (string.IsNullOrEmpty(AccessInformation.SVDBServer))
                {
                    return false;
                }
            }

            if (AccessInformation.LimitUser)
            {

                if (string.IsNullOrEmpty(AccessInformation.UserID))
                {
                    return false;
                }
            }


            return true;

        }
        public QueryAlertLogResult QueryAlertLog(AccessControl AccessInformation, AlertLogQueryCondition QueryCondition)
        {
            QueryAlertLogResult queryAlertLogResult = new QueryAlertLogResult();
            //dowhat= QueryAlertLog,	
            //begin_year= XXX,  
            //begin_month= XXX,  
            //begin_day= XXX,  
            //begin_hour= XXX,  
            //begin_minute= XXX,  
            //begin_second= XXX,  
            //end_year= XXX,  
            //end_month= XXX,  
            //end_day= XXX,  
            //end_hour= XXX,  
            //end_minute= XXX,  
            //end_second= XXX,
            //alertName= XXX,   
            //alertReceive= XXX, 
            //alertType= XXX; 
            ////alertName��alertReceive��alertType �� and ��ϵ�����Ϊ����Ϊȫ����

            SortedDictionary<string, string> paras = new SortedDictionary<string,string>();

            paras.Add("dowhat","QueryAlertLog");
            paras.Add("begin_year",QueryCondition.StartTime.Year.ToString());
            paras.Add("begin_month",QueryCondition.StartTime.Month.ToString());
            paras.Add("begin_day",QueryCondition.StartTime.Day.ToString());
            paras.Add("begin_hour",QueryCondition.StartTime.Hour.ToString());
            paras.Add("begin_minute",QueryCondition.StartTime.Minute.ToString());
            paras.Add("begin_second",QueryCondition.StartTime.Second.ToString());
            paras.Add("end_year",QueryCondition.EndTime.Year.ToString());
            paras.Add("end_month",QueryCondition.EndTime.Month.ToString());
            paras.Add("end_day",QueryCondition.EndTime.Day.ToString());
            paras.Add("end_hour",QueryCondition.EndTime.Hour.ToString());
            paras.Add("end_minute",QueryCondition.EndTime.Minute.ToString());
            paras.Add("end_second",QueryCondition.EndTime.Second.ToString());

            if(QueryCondition.LimitName)
            {
                paras.Add("alertName", QueryCondition.AlertName);
            }

            if(QueryCondition.LimitReceiver)
            {
                paras.Add("alertReceive", QueryCondition.AlertReceiver);
            }

            if(QueryCondition.LimitType)
            {
                paras.Add("alertType", QueryCondition.AlertType.ToString("D"));
            }
            if (QueryCondition.LimitIndex)
            {
                paras.Add("alertIndex", QueryCondition.AlertIndex);
            }
            
            
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[paras.Count];
        
            int i = 0;
            foreach(string key in paras.Keys)
            {
                anyType2anyTypeMapEntry node = Helper.XfireCreateKeyValue(key, paras[key]);
                requestData[i++] = node;
            }
           
            this.service.Url = UserPermissionContext.Instance.Url;
            RetMapInVector result = ServiceClient.GetForestData(requestData);
            //RetMapInVector result = this.service.GetForestData(requestData);
            if (!result.retbool)
            {
                if (logger.IsWarnEnabled)
                {
                    logger.Warn(result.estr);
                }
                return null;
            }

            queryAlertLogResult.ErrorInformation = result.estr;
            queryAlertLogResult.QueryAlertLogOK = result.retbool;
   
            IList<NameValueCollection> resultData = Helper.XfireConventMapEntryToList(result.vmap);
            AlertLogItem[] responseData = new AlertLogItem[resultData.Count];
            for (int j = 0; j < resultData.Count; j++)
            {
                NameValueCollection record = resultData[j];
                AlertLogItem item = new AlertLogItem();
                item.AlertIndex = record["_AlertIndex"];
                item.AlertName = record["_AlertRuleName"];
                item.EntityName = record["_DeviceName"];
                item.MonitorName = record["_MonitorName"];
                item.AlertReceiver = record["_AlertReceive"];
                item.AlertStatus = (AlertStatus)Enum.Parse(typeof(AlertStatus), record["_AlertStatus"]);
                item.AlertTime = DateTime.Parse(record["_AlertTime"]);
                item.AlertType = (AlertType)Enum.Parse(typeof(AlertType), record["_AlertType"]);
                
                responseData[j] = item;

            }

            queryAlertLogResult.ResponseData = responseData;

            return queryAlertLogResult;

        }

        #endregion
    }
}
