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
        #region IAlertLogDao 成员
        /// <summary>
        /// 错误信息
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
            keytext.Add("AccessControlError", "数据访问控制信息取得有误");
            keytext.Add("RuleQueryConditionError", "报警规则查询条件解析有误");
            keytext.Add("LogQueryConditionError", "报警日志查询条件解析有误");
            keytext.Add("AlertIDError", "Index不能为空，或格式错误");
            keytext.Add("AlertTargetError", "没有选择任何监测器");
            keytext.Add("AlertNameError", "报警规则没有命名，或者重名");
            keytext.Add("AlertTypeError", "报警规则类型不对");
            keytext.Add("AlertCategoryError", "报警条件分类不对");
            keytext.Add("AlertTimesError", "报警发送模式不对");
            keytext.Add("AlertAlwaysError", "报警没有指定发送在第几次之后(Always)");
            keytext.Add("AlertOnlyError", "报警没有指定发送在第几次之后(Only)");
            keytext.Add("AlertSelect1Error", "报警没有指定发送在第几次之后(Select1)");
            keytext.Add("AlertSelect2Error", "报警没有指定重复发送的次数(Select2)");
            keytext.Add("AlertStateError", "当前报警的有效状态不对 ");
            keytext.Add("EmailAddresssError", "Email地址指定不对");
            keytext.Add("UpgradeTimesError", "升级次数格式不对");
            keytext.Add("StopTimesError", "停止次数格式不对");
            keytext.Add("SmsNumberError", "短信发送目的手机格式不对");
            keytext.Add("SendModeError", "短信发送模式不对");
            keytext.Add("SMSTemplateError", "短信发送模板指定不对");
            keytext.Add("ScriptServerError", "脚本报警的服务器没有指定");
            keytext.Add("ServerIDError", "脚本报器的服务器ID设定有误");
            keytext.Add("ServerNameError", "声音报警的服务器名没有指定");
            keytext.Add("LoginNameError", "登录名设定有误");
            keytext.Add("PushMessageError", "报警规则改变信息发送到消息队列中出错");
        }
        /// <summary>
        /// //访问控制信息的取得
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
            ////alertName、alertReceive、alertType 是 and 关系，如果为空则为全部。

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
