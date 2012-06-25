using System;
using System.Collections.Generic;
using System.Text;

using SiteView.Ecc.Core.Dao;
using System.Collections.Specialized;

using SiteView.Ecc.WSClient.XFireWebService;
using SiteView.Ecc.Core;
using System.Text.RegularExpressions;

namespace SiteView.Ecc.WSClient
{
    public class AlertDaoImpl : IAlertDao
    {
        #region IAlertDao 成员
        private IniFileDaoImpl iniFile;
        private QueueDaoImpl queue;
        private InterfaceEccService service = new InterfaceEccService();
        private static NameValueCollection keytext = new NameValueCollection();
        private static log4net.ILog logger = log4net.LogManager.GetLogger(typeof(AlertDaoImpl));
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
        /// <summary>
        /// 
        /// </summary>
        public AlertDaoImpl()
        {
            keytext.Clear();
            keytext.Add("AccessControlError", "数据访问控制信息取得有误");
            keytext.Add("RuleQueryConditionError", "报警规则查询条件解析有误");
            keytext.Add("LogQueryConditionError", "报警日志查询条件解析有误");
            keytext.Add("AlertIDError", "Index不能为空，或格式错误");
            keytext.Add("AlertTargetError", "没有选择任何监测器");
            //keytext.Add("AlertNameError", "报警规则没有命名，或者重名");
            keytext.Add("AlertNameError", "报警规则名称重复");
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

        #region 基本方法
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
        /// <summary>
        /// 条件
        /// </summary>
        /// <param name="structQueryCondition"></param>
        /// <returns></returns>
        private bool GetAlertRuleQueryCondition(AlertRuleQueryCondition structQueryCondition)
        {
            if (structQueryCondition.LimitIndex)
            {

                if (string.IsNullOrEmpty(structQueryCondition.AlertIndex))
                {
                    return false;
                }
            }
            if (structQueryCondition.LimitTarget)
            {
                if (string.IsNullOrEmpty(structQueryCondition.AlertTarget))
                {
                    return false;
                }
            }
            if (structQueryCondition.LimitName)
            {

                if (string.IsNullOrEmpty(structQueryCondition.AlertName))
                {
                    return false;
                }
            }

            if (structQueryCondition.LimitType)
            {

                if (string.IsNullOrEmpty(structQueryCondition.AlertType.ToString()))
                {
                    return false;
                }
            }
            if (structQueryCondition.LimitCategory)
            {
                if (string.IsNullOrEmpty(structQueryCondition.AlertCategory.ToString()))
                {
                    return false;
                }
            }

            if (structQueryCondition.LimitTimes)
            {
                if (string.IsNullOrEmpty(structQueryCondition.AlertTimes.ToString()))
                {
                    return false;
                }
            }

            if (structQueryCondition.LimitState)
            {
                if (string.IsNullOrEmpty(structQueryCondition.AlertState.ToString()))
                {
                    return false;
                }
            }
            return true;
        }
        /// <summary>
        /// 设置基本信息
        /// </summary>
        /// <param name="AlertIndex"></param>
        /// <param name="sectionlists"></param>
        /// <returns></returns>
        private BaseAlert GetIniBaseAlertInfo(string AlertIndex, IDictionary<string, NameValueCollection> sectionlists)
        {
            BaseAlert baseAlert = new BaseAlert();
            baseAlert.ID = AlertIndex;
            baseAlert.Name = string.IsNullOrEmpty(sectionlists[AlertIndex]["AlertName"]) ? "" : sectionlists[AlertIndex]["AlertName"];
            baseAlert.Only = string.IsNullOrEmpty(sectionlists[AlertIndex]["OnlyTimes"]) ? "" : sectionlists[AlertIndex]["OnlyTimes"];
            baseAlert.Select1 = string.IsNullOrEmpty(sectionlists[AlertIndex]["SelTimes1"]) ? "" : sectionlists[AlertIndex]["SelTimes1"];
            baseAlert.Select2 = string.IsNullOrEmpty(sectionlists[AlertIndex]["SelTimes2"]) ? "" : sectionlists[AlertIndex]["SelTimes2"];
            if (string.IsNullOrEmpty(sectionlists[AlertIndex]["AlertState"]))
            {
                baseAlert.State = AlertState.Enable;
            }
            else
            {
                baseAlert.State = sectionlists[AlertIndex]["AlertState"].ToLower() == "enable" ? AlertState.Enable : AlertState.Disable;
            }
            baseAlert.Target = string.IsNullOrEmpty(sectionlists[AlertIndex]["AlertTarget"]) ? "" : sectionlists[AlertIndex]["AlertTarget"];
            string times = "";
            if (string.IsNullOrEmpty(sectionlists[AlertIndex]["AlertCond"]))
            {
                times = "1";
            }
            else
            {
                times = sectionlists[AlertIndex]["AlertCond"];
            }
            switch (times)
            {
                case "1":
                    baseAlert.Times = AlertTimes.Always;
                    break;
                case "2":
                    baseAlert.Times = AlertTimes.Only;
                    break;
                case "3":
                    baseAlert.Times = AlertTimes.Select;
                    break;
            }

            string stye = "EmailAlert";
            if (string.IsNullOrEmpty(sectionlists[AlertIndex]["AlertType"]))
            {
                stye = "EmailAlert";
            }
            else
            {
                stye = sectionlists[AlertIndex]["AlertType"];
            }
            switch (stye)
            {
                case "EmailAlert":
                    baseAlert.Type = AlertType.EmailAlert;
                    break;
                case "SmsAlert":
                    baseAlert.Type = AlertType.SmsAlert;
                    break;
                case "ScriptAlert":
                    baseAlert.Type = AlertType.ScriptAlert;
                    break;
                case "SoundAlert":
                    baseAlert.Type = AlertType.SoundAlert;
                    break;

            }




            baseAlert.Always = string.IsNullOrEmpty(sectionlists[AlertIndex]["AlwaysTimes"]) ? "" : sectionlists[AlertIndex]["AlwaysTimes"];
            string category = "";
            if (string.IsNullOrEmpty(sectionlists[AlertIndex]["AlertCategory"]))
            {
                category = "正常";
            }
            else
            {
                category = sectionlists[AlertIndex]["AlertCategory"];
            }
            switch (category)
            {
                case "正常":
                    baseAlert.Category = AlertCategory.Normal;
                    break;
                case "危险":
                    baseAlert.Category = AlertCategory.Danger;
                    break;
                case "错误":
                    baseAlert.Category = AlertCategory.Error;
                    break;
            }
            return baseAlert;

        }
        /// <summary>
        /// 名称是否已存在
        /// </summary>
        /// <param name="strAlertName"></param>
        /// <returns></returns>
        bool IsAlertNameExist(AccessControl AccessInformation, string strAlertName)
        {
            string strTemp = "";

            IList<string> keylist = new List<string>();
            string strServer = "localhost";
            string strUser = "default";

            if (AccessInformation.LimitServer)
            {
                strServer = AccessInformation.SVDBServer;
            }

            if (AccessInformation.LimitUser)
            {
                strUser = AccessInformation.UserID;
            }

            bool bExist = false;
            //从ini获取用户列表
            if (this.iniFile == null)
            {
                this.iniFile = new IniFileDaoImpl();
            }
            IDictionary<string, NameValueCollection> sectionlists = this.iniFile.GetIniFile("alert.ini");
            keylist.Clear();
            foreach (string key in sectionlists.Keys)
            {
                string alertName = string.IsNullOrEmpty(sectionlists[key]["AlertName"]) ? "" : sectionlists[key]["AlertName"];
                keylist.Add(alertName);
            }
            if (keylist.Contains(strAlertName))
            {
                bExist = true;
            }

            return bExist;
        }
        /// <summary>
        /// email 格式是否正确
        /// </summary>
        /// <param name="szMailList"></param>
        /// <returns></returns>
        bool CheckEmail(string szMailList)
        {
            return Regex.IsMatch(szMailList, @"^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\]?)$");
        }
        /// <summary>
        /// 数字判断
        /// </summary>
        /// <param name="strValue"></param>
        /// <returns></returns>
        bool IsAlphaNumber(string strNumber)
        {
            Regex objNotNumberPattern = new Regex("[^0-9.-]");
            Regex objTwoDotPattern = new Regex("[0-9]*[.][0-9]*[.][0-9]*");
            Regex objTwoMinusPattern = new Regex("[0-9]*[-][0-9]*[-][0-9]*");
            String strValidRealPattern = "^([-]|[.]|[-.]|[0-9])[0-9]*[.]*[0-9]+$";
            String strValidIntegerPattern = "^([-]|[0-9])[0-9]*$";
            Regex objNumberPattern = new Regex("(" + strValidRealPattern + ")|(" + strValidIntegerPattern + ")");

            return !objNotNumberPattern.IsMatch(strNumber) &&
            !objTwoDotPattern.IsMatch(strNumber) &&
            !objTwoMinusPattern.IsMatch(strNumber) &&
            objNumberPattern.IsMatch(strNumber);
        }
        /// <summary>
        /// 设置BaseAlert信息
        /// </summary>
        /// <param name="alertInfo"></param>
        /// <param name="estr"></param>
        /// <returns></returns>
        bool SetIniBaseAlertInfo(AccessControl AccessInformation, BaseAlert alertInfo, string estr)
        {
            bool ret = true;

            while (ret)
            {

                string strTemp;

                string strAlertIndex = alertInfo.ID;

                string strServer = "localhost";
                string strUser = "default";

                if (AccessInformation.LimitServer)
                {
                    strServer = AccessInformation.SVDBServer;
                }

                if (AccessInformation.LimitUser)
                {
                    strUser = AccessInformation.UserID;
                }

                if (string.IsNullOrEmpty(strAlertIndex))
                {
                    ret = false;
                    estr = keytext[ErrorInfo.AlertIDError.ToString()];
                    break;
                }

                if (this.iniFile == null)
                {
                    this.iniFile = new IniFileDaoImpl();
                }

                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "nIndex", alertInfo.ID);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "AlertTarget", alertInfo.Target);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "AlertName", alertInfo.Name);

                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "AlertType", alertInfo.Type.ToString());

                if (alertInfo.Category == AlertCategory.Normal)
                {
                    strTemp = "正常";
                }
                else if (alertInfo.Category == AlertCategory.Danger)
                {
                    strTemp = "危险";
                }
                else if (alertInfo.Category == AlertCategory.Error)
                {
                    strTemp = "错误";
                }
                else
                {
                    //程序分支走不到此路径AlertCategoryError
                    ret = false;
                    estr = keytext[ErrorInfo.AlertCategoryError.ToString()];
                    break;
                }
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "AlertCategory", strTemp);
                this.iniFile.WriteIniFileInt("alert.ini", strAlertIndex, "AlertCond", alertInfo.Times.GetHashCode());

                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "AlwaysTimes", "1");
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "OnlyTimes", "1");
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "SelTimes1", "2");
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "SelTimes2", "3");


                if (alertInfo.Times == AlertTimes.Always)
                {

                    if (!IsAlphaNumber(alertInfo.Always))
                    {//AlertAlwaysError AlertOnlyError
                        ret = false;
                        estr = keytext[ErrorInfo.AlertAlwaysError.ToString()];
                        break;
                    }
                    this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "AlwaysTimes", alertInfo.Always);

                }
                else if (alertInfo.Times == AlertTimes.Only)
                {

                    if (!IsAlphaNumber(alertInfo.Only))
                    {
                        ret = false;
                        estr = keytext[ErrorInfo.AlertOnlyError.ToString()];
                        break;
                    }
                    this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "AlwaysTimes", alertInfo.Only);
                }
                else
                {

                    if (!IsAlphaNumber(alertInfo.Select1))
                    {//
                        ret = false;
                        estr = keytext[ErrorInfo.AlertSelect1Error.ToString()];
                        break;
                    }
                    this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "SelTimes1", alertInfo.Select1);

                    if (!IsAlphaNumber(alertInfo.Select2))
                    {
                        ret = false;
                        estr = keytext[ErrorInfo.AlertSelect2Error.ToString()];
                        break;
                    }
                    this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "SelTimes2", alertInfo.Select2);
                }

                if (alertInfo.State == AlertState.Enable)
                {
                    strTemp = "Enable";
                }
                else
                {
                    strTemp = "禁止";
                }
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "AlertState", strTemp);


                break;
            }

            return ret;

        }

        uint RandIndex()
        {
            uint nPort = 0;
            uint nMin = 0x4000;
            uint nMax = 0x7FFF;
            Random rdm1 = new Random(unchecked((int)DateTime.Now.Ticks));
            nPort = (uint)rdm1.Next(int.MaxValue);
            nPort = nPort | nMin;
            nPort = nPort & nMax;
            return nPort;
        }

        private string GenerateAlertIndex(AccessControl access)
        {
            string strServer = "localhost";
            string strUser = "default";

            if (access.LimitServer)
            {
                strServer = access.SVDBServer;
            }

            if (access.LimitUser)
            {
                strUser = access.UserID;
            }
            string strAlertIndex = "";
            uint index = 0;
            while (true)
            {
                index = RandIndex();
                if (this.iniFile == null)
                {
                    this.iniFile = new IniFileDaoImpl();
                }
                IDictionary<string, NameValueCollection> rs = this.iniFile.GetIniFileBySection("alert.ini", index.ToString());

                if (rs.ContainsKey(index.ToString()))
                {
                    continue;
                }
                else
                {
                    strAlertIndex = index.ToString();
                    break;
                }
            }

            return strAlertIndex;
        }
        #endregion

        #region 报警信息的取得
        /// <summary>
        /// Email报警规则信息的取得
        /// </summary>
        /// <param name="AccessInformation"></param>
        /// <param name="AlertIndex"></param>
        /// <returns></returns>
        public GetEmailAlertResult GetEmailAlert(AccessControl AccessInformation, string AlertIndex)
        {
            GetEmailAlertResult Result = new GetEmailAlertResult();
            EmailAlert email = new EmailAlert();
            string estr = "";
            bool ret = true;

            ret = GetAccessControlInformation(AccessInformation);
            if (!ret)
            {
                estr = keytext[ErrorInfo.AccessControlError.ToString()];
            }

            string strServer = "localhost";
            string strUser = "default";

            if (AccessInformation.LimitServer)
            {
                strServer = AccessInformation.SVDBServer;
            }

            if (AccessInformation.LimitUser)
            {
                strUser = AccessInformation.UserID;
            }

            while (ret)
            {
                if (this.iniFile == null)
                {
                    this.iniFile = new IniFileDaoImpl();
                }
                IDictionary<string, NameValueCollection> sectionlists = this.iniFile.GetIniFileBySection("alert.ini", AlertIndex);
                email.BaseInfo = GetIniBaseAlertInfo(AlertIndex, sectionlists);
                email.EmailAddresss = sectionlists[AlertIndex]["EmailAdress"];
                email.OtherAddress = sectionlists[AlertIndex]["OtherAdress"];
                email.EmailTemplate = sectionlists[AlertIndex]["EmailTemplate"];
                email.UpgradeTimes = sectionlists[AlertIndex]["Upgrade"];
                email.ReceiverAddress = sectionlists[AlertIndex]["UpgradeTo"];
                email.StopTimes = sectionlists[AlertIndex]["Stop"];
                email.WatchSheet = sectionlists[AlertIndex]["WatchSheet"];
                break;
            }
            Result.ErrorInformation = estr;
            Result.GetEmailAlertOK = ret;
            Result.ResponseData = email;
            return Result;

        }
        /// <summary>
        ///  SMS报警规则信息的取得
        /// </summary>
        /// <param name="AccessInformation"></param>
        /// <param name="AlertIndex"></param>
        /// <returns></returns>
        public GetSMSAlertResult GetSMSAlert(AccessControl AccessInformation, string AlertIndex)
        {
            GetSMSAlertResult Result = new GetSMSAlertResult();
            SMSAlert sms = new SMSAlert();
            string estr = "";
            bool ret = true;
            ret = GetAccessControlInformation(AccessInformation);
            if (!ret)
            {
                estr = keytext[ErrorInfo.AccessControlError.ToString()];
            }
            string strServer = "localhost";
            string strUser = "default";

            if (AccessInformation.LimitServer)
            {
                strServer = AccessInformation.SVDBServer;
            }

            if (AccessInformation.LimitUser)
            {
                strUser = AccessInformation.UserID;
            }

            while (ret)
            {
                if (this.iniFile == null)
                {
                    this.iniFile = new IniFileDaoImpl();
                }
                IDictionary<string, NameValueCollection> sectionlists = this.iniFile.GetIniFileBySection("alert.ini", AlertIndex);
                sms.BaseInfo = GetIniBaseAlertInfo(AlertIndex, sectionlists);

                sms.SmsNumber = sectionlists[AlertIndex]["SmsNumber"];
                sms.OtherNumber = sectionlists[AlertIndex]["OtherNumber"];
                sms.SendMode = sectionlists[AlertIndex]["SmsSendMode"];
                sms.SMSTemplate = sectionlists[AlertIndex]["SmsTemplate"];
                sms.UpgradeTimes = sectionlists[AlertIndex]["Upgrade"];
                sms.ReceiverAddress = sectionlists[AlertIndex]["UpgradeTo"];
                sms.StopTimes = sectionlists[AlertIndex]["Stop"];
                sms.WatchSheet = sectionlists[AlertIndex]["WatchSheet"];
                break;
            }
            Result.ErrorInformation = estr;
            Result.GetSMSAlertOK = ret;
            Result.ResponseData = sms;
            return Result;
        }
        /// <summary>
        /// Script报警规则信息的取得
        /// </summary>
        /// <param name="AccessInformation"></param>
        /// <param name="AlertIndex"></param>
        /// <returns></returns>
        public GetScriptAlertResult GetScriptAlert(AccessControl AccessInformation, string AlertIndex)
        {
            GetScriptAlertResult Result = new GetScriptAlertResult();
            ScriptAlert script = new ScriptAlert();
            string estr = "";
            bool ret = true;
            ret = GetAccessControlInformation(AccessInformation);
            if (!ret)
            {
                estr = keytext[ErrorInfo.AccessControlError.ToString()];
            }

            string strServer = "localhost";
            string strUser = "default";

            if (AccessInformation.LimitServer)
            {
                strServer = AccessInformation.SVDBServer;
            }

            if (AccessInformation.LimitUser)
            {
                strUser = AccessInformation.UserID;
            }

            while (ret)
            {
                if (this.iniFile == null)
                {
                    this.iniFile = new IniFileDaoImpl();
                }
                IDictionary<string, NameValueCollection> sectionlists = this.iniFile.GetIniFileBySection("alert.ini", AlertIndex);
                script.BaseInfo = GetIniBaseAlertInfo(AlertIndex, sectionlists);

                script.ScriptServer = sectionlists[AlertIndex]["ScriptServer"];
                script.ServerID = sectionlists[AlertIndex]["ScriptServerID"];
                script.ScriptFile = sectionlists[AlertIndex]["ScriptFile"];
                script.ScriptParam = sectionlists[AlertIndex]["ScriptParam"];
                break;
            }
            Result.ErrorInformation = estr;
            Result.GetScriptAlertOK = ret;
            Result.ResponseData = script;
            return Result;
        }
        /// <summary>
        /// Sound报警规则信息的取得
        /// </summary>
        /// <param name="AccessInformation"></param>
        /// <param name="AlertIndex"></param>
        /// <returns></returns>
        public GetSoundAlertResult GetSoundAlert(AccessControl AccessInformation, string AlertIndex)
        {
            GetSoundAlertResult Result = new GetSoundAlertResult();
            SoundAlert sound = new SoundAlert();
            string estr = "";
            bool ret = true;
            ret = GetAccessControlInformation(AccessInformation);
            if (!ret)
            {
                estr = keytext[ErrorInfo.AccessControlError.ToString()];
            }
            string strServer = "localhost";
            string strUser = "default";

            if (AccessInformation.LimitServer)
            {
                strServer = AccessInformation.SVDBServer;
            }

            if (AccessInformation.LimitUser)
            {
                strUser = AccessInformation.UserID;
            }

            while (ret)
            {
                if (this.iniFile == null)
                {
                    this.iniFile = new IniFileDaoImpl();
                }
                IDictionary<string, NameValueCollection> sectionlists = this.iniFile.GetIniFileBySection("alert.ini", AlertIndex);
                sound.BaseInfo = GetIniBaseAlertInfo(AlertIndex, sectionlists);

                sound.ServerName = sectionlists[AlertIndex]["Server"];
                sound.LoginName = sectionlists[AlertIndex]["LoginName"];
                sound.LoginPassword = sectionlists[AlertIndex]["LoginPwd"];
                break;
            }
            Result.ErrorInformation = estr;
            Result.GetSoundAlertOK = ret;
            Result.ResponseData = sound;
            return Result;
        }
        #endregion

        #region 添加报警
        /// <summary>
        /// email 报警
        /// </summary>
        /// <param name="AccessInformation"></param>
        /// <param name="AlertInformation"></param>
        /// <returns></returns>
        public AddAlertResult AddEmailAlert(AccessControl AccessInformation, EmailAlert AlertInformation)
        {
            AddAlertResult Result = new AddAlertResult();

            string estr = "";
            bool ret = true;
            string strServer = "";
            string strUser = "";

            while (ret)
            {

                ret = GetAccessControlInformation(AccessInformation);
                if (!ret)
                {
                    estr = keytext[ErrorInfo.AccessControlError.ToString()];
                }

                strServer = "localhost";
                strUser = "default";

                if (AccessInformation.LimitServer)
                {
                    strServer = AccessInformation.SVDBServer;
                }

                if (AccessInformation.LimitUser)
                {
                    strUser = AccessInformation.UserID;
                }
                if (IsAlertNameExist(AccessInformation, AlertInformation.BaseInfo.Name))
                {
                    ret = false;
                    estr = keytext[ErrorInfo.AlertNameError.ToString()];
                    break;
                }
                //email报警
                //if (string.IsNullOrEmpty(AlertInformation.EmailAddresss))
                //{
                //    ret = false;
                //    estr = keytext[ErrorInfo.EmailAddresssError.ToString()];
                //    break;
                //}


                //if (!string.IsNullOrEmpty(AlertInformation.EmailAddresss))
                //{
                //    if (!CheckEmail(AlertInformation.OtherAddress))
                //    {
                //        ret = false;
                //        estr = keytext[ErrorInfo.EmailAddresssError.ToString()];
                //        break;
                //    }
                //}

                if (!string.IsNullOrEmpty(AlertInformation.UpgradeTimes))
                {

                    if (!IsAlphaNumber(AlertInformation.UpgradeTimes))
                    {
                        ret = false;
                        estr = keytext[ErrorInfo.AlertTimesError.ToString()];
                        break;
                    }
                }

                if (!string.IsNullOrEmpty(AlertInformation.StopTimes))
                {
                    if (!IsAlphaNumber(AlertInformation.StopTimes))
                    {
                        ret = false;
                        estr = keytext[ErrorInfo.AlertTimesError.ToString()];
                        break;
                    }
                }

                break;
            }
            string strAlertIndex = "";

            while (ret)
            {
                strAlertIndex = GenerateAlertIndex(AccessInformation) ;
                AlertInformation.BaseInfo.ID = strAlertIndex;
                //数据存储。。
                ret = SetIniBaseAlertInfo(AccessInformation, AlertInformation.BaseInfo, estr);

                if (!ret)
                {
                    break;
                }

                //email报警
                if (this.iniFile == null)
                {
                    this.iniFile = new IniFileDaoImpl();
                }
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "EmailAdress", AlertInformation.EmailAddresss);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "OtherAdress", AlertInformation.OtherAddress);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "EmailTemplate", AlertInformation.EmailTemplate);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "WatchSheet", AlertInformation.WatchSheet);
                
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "Upgrade", AlertInformation.UpgradeTimes);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "UpgradeTo", AlertInformation.ReceiverAddress);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "Stop", AlertInformation.StopTimes);

                if (queue == null)
                {
                    this.queue = new QueueDaoImpl();
                }
                //send message to service
                bool meseagebool = this.queue.PushStringMessage("SiteView70-Alert", "IniChange", "alert.ini," + strAlertIndex + ",ADD");

                if (!meseagebool)
                {
                    estr = keytext[ErrorInfo.PushMessageError.ToString()];
                }

                break;
            }
            Result.ErrorInformation = estr;
            Result.AddAlertOK = ret;
            Result.AlertIndex = strAlertIndex;
            return Result;
        }
        /// <summary>
        /// SMS 报警
        /// </summary>
        /// <param name="AccessInformation"></param>
        /// <param name="AlertInformation"></param>
        /// <returns></returns>
        public AddAlertResult AddSMSAlert(AccessControl AccessInformation, SMSAlert AlertInformation)
        {
            AddAlertResult Result = new AddAlertResult();
            string estr = "";
            bool ret = true;
            string strServer = "";
            string strUser = "";

            while (ret)
            {

                ret = GetAccessControlInformation(AccessInformation);
                if (!ret)
                {
                    estr = keytext[ErrorInfo.AccessControlError.ToString()];
                }

                strServer = "localhost";
                strUser = "default";

                if (AccessInformation.LimitServer)
                {
                    strServer = AccessInformation.SVDBServer;
                }

                if (AccessInformation.LimitUser)
                {
                    strUser = AccessInformation.UserID;
                }
                if (IsAlertNameExist(AccessInformation, AlertInformation.BaseInfo.Name))
                {
                    ret = false;
                    estr = keytext[ErrorInfo.AlertNameError.ToString()];
                    break;
                }
                //sms报警


                if (string.IsNullOrEmpty(AlertInformation.SmsNumber))
                {
                    ret = false;

                    break;
                }

                if (!string.IsNullOrEmpty(AlertInformation.UpgradeTimes))
                {
                    if (!IsAlphaNumber(AlertInformation.UpgradeTimes))
                    {
                        ret = false;
                        estr = keytext[ErrorInfo.UpgradeTimesError.ToString()];
                        break;
                    }
                }

                if (!string.IsNullOrEmpty(AlertInformation.StopTimes))
                {
                    if (!IsAlphaNumber(AlertInformation.StopTimes))
                    {
                        ret = false;
                        estr = keytext[ErrorInfo.StopTimesError.ToString()];
                        break;
                    }
                }

                break;
            }
            string strAlertIndex = "";

            while (ret)
            {
                strAlertIndex = GenerateAlertIndex(AccessInformation);
                AlertInformation.BaseInfo.ID = strAlertIndex;
                //数据存储。。
                ret = SetIniBaseAlertInfo(AccessInformation, AlertInformation.BaseInfo, estr);

                if (!ret)
                {
                    break;
                }

                //email报警
                if (this.iniFile == null)
                {
                    this.iniFile = new IniFileDaoImpl();
                }


                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "SmsNumber", AlertInformation.SmsNumber);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "OtherNumber", AlertInformation.OtherNumber);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "SmsSendMode", AlertInformation.SendMode);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "SmsTemplate", AlertInformation.SMSTemplate);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "WatchSheet", AlertInformation.WatchSheet);

                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "Upgrade", AlertInformation.UpgradeTimes);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "UpgradeTo", AlertInformation.ReceiverAddress);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "Stop", AlertInformation.StopTimes);

                if (queue == null)
                {
                    this.queue = new QueueDaoImpl();
                }
                //send message to service
                bool meseagebool = this.queue.PushStringMessage("SiteView70-Alert", "IniChange", "alert.ini," + strAlertIndex + ",ADD");

                if (!meseagebool)
                {
                    estr = keytext[ErrorInfo.PushMessageError.ToString()];
                }

                break;
            }
            Result.ErrorInformation = estr;
            Result.AddAlertOK = ret;
            Result.AlertIndex = strAlertIndex;

            return Result;
        }
        /// <summary>
        /// Script 报警
        /// </summary>
        /// <param name="AccessInformation"></param>
        /// <param name="AlertInformation"></param>
        /// <returns></returns>
        public AddAlertResult AddScriptAlert(AccessControl AccessInformation, ScriptAlert AlertInformation)
        {
            AddAlertResult Result = new AddAlertResult();
            string estr = "";
            bool ret = true;
            string strServer = "";
            string strUser = "";

            while (ret)
            {

                ret = GetAccessControlInformation(AccessInformation);
                if (!ret)
                {
                    estr = keytext[ErrorInfo.AccessControlError.ToString()];
                }

                strServer = "localhost";
                strUser = "default";

                if (AccessInformation.LimitServer)
                {
                    strServer = AccessInformation.SVDBServer;
                }

                if (AccessInformation.LimitUser)
                {
                    strUser = AccessInformation.UserID;
                }
                if (IsAlertNameExist(AccessInformation, AlertInformation.BaseInfo.Name))
                {
                    ret = false;
                    estr = keytext[ErrorInfo.AlertNameError.ToString()];
                    break;
                }
                //script报警

                break;
            }
            string strAlertIndex = "";

            while (ret)
            {
                strAlertIndex = GenerateAlertIndex(AccessInformation);
                AlertInformation.BaseInfo.ID = strAlertIndex;
                //数据存储。。
                ret = SetIniBaseAlertInfo(AccessInformation, AlertInformation.BaseInfo, estr);

                if (!ret)
                {
                    break;
                }

                //email报警
                if (this.iniFile == null)
                {
                    this.iniFile = new IniFileDaoImpl();
                }


                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "ScriptServer", AlertInformation.ScriptServer);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "ScriptServerID", AlertInformation.ServerID);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "ScriptFile", AlertInformation.ScriptFile);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "ScriptParam", AlertInformation.ScriptParam);


                if (queue == null)
                {
                    this.queue = new QueueDaoImpl();
                }
                //send message to service
                bool meseagebool = this.queue.PushStringMessage("SiteView70-Alert", "IniChange", "alert.ini," + strAlertIndex + ",ADD");

                if (!meseagebool)
                {
                    estr = keytext[ErrorInfo.PushMessageError.ToString()];
                }

                break;
            }
            Result.ErrorInformation = estr;
            Result.AddAlertOK = ret;
            Result.AlertIndex = strAlertIndex;

            return Result;
        }

        public AddAlertResult AddSoundAlert(AccessControl AccessInformation, SoundAlert AlertInformation)
        {
            AddAlertResult Result = new AddAlertResult();
            string estr = "";
            bool ret = true;
            string strServer = "";
            string strUser = "";

            while (ret)
            {

                ret = GetAccessControlInformation(AccessInformation);
                if (!ret)
                {
                    estr = keytext[ErrorInfo.AccessControlError.ToString()];
                }

                strServer = "localhost";
                strUser = "default";

                if (AccessInformation.LimitServer)
                {
                    strServer = AccessInformation.SVDBServer;
                }

                if (AccessInformation.LimitUser)
                {
                    strUser = AccessInformation.UserID;
                }
                if (IsAlertNameExist(AccessInformation, AlertInformation.BaseInfo.Name))
                {
                    ret = false;
                    estr = keytext[ErrorInfo.AlertNameError.ToString()];
                    break;
                }
                //script报警

                break;
            }
            string strAlertIndex = "";

            while (ret)
            {
                strAlertIndex = GenerateAlertIndex(AccessInformation);
                AlertInformation.BaseInfo.ID = strAlertIndex;
                //数据存储。。
                ret = SetIniBaseAlertInfo(AccessInformation, AlertInformation.BaseInfo, estr);

                if (!ret)
                {
                    break;
                }

                //email报警
                if (this.iniFile == null)
                {
                    this.iniFile = new IniFileDaoImpl();
                }

                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "Server", AlertInformation.ServerName);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "LoginName", AlertInformation.LoginName);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "LoginPwd", AlertInformation.LoginPassword);


                if (queue == null)
                {
                    this.queue = new QueueDaoImpl();
                }
                //send message to service
                bool meseagebool = this.queue.PushStringMessage("SiteView70-Alert", "IniChange", "alert.ini," + strAlertIndex + ",ADD");

                if (!meseagebool)
                {
                    estr = keytext[ErrorInfo.PushMessageError.ToString()];
                }

                break;
            }
            Result.ErrorInformation = estr;
            Result.AddAlertOK = ret;
            Result.AlertIndex = strAlertIndex;

            return Result;
        }
        #endregion

        public DeleteAlertResult DeleteAlert(AccessControl AccessInformation, BaseAlert AlertInformation)
        {
            DeleteAlertResult Result = new DeleteAlertResult();
            string estr = "";
            bool ret = true;
            string strServer = "";
            string strUser = "";

            while (ret)
            {

                ret = GetAccessControlInformation(AccessInformation);
                if (!ret)
                {
                    estr = keytext[ErrorInfo.AccessControlError.ToString()];
                }

                strServer = "localhost";
                strUser = "default";

                if (AccessInformation.LimitServer)
                {
                    strServer = AccessInformation.SVDBServer;
                }

                if (AccessInformation.LimitUser)
                {
                    strUser = AccessInformation.UserID;
                }
                if (this.iniFile == null)
                {
                    this.iniFile = new IniFileDaoImpl();
                }
                this.iniFile.DeleteIniFileBySection("alert.ini", AlertInformation.ID);

                if (queue == null)
                {
                    this.queue = new QueueDaoImpl();
                }
                //send message to service
                bool meseagebool = this.queue.PushStringMessage("SiteView70-Alert", "IniChange", "alert.ini," + AlertInformation.ID + ",DELETE");

                if (!meseagebool)
                {
                    estr = keytext[ErrorInfo.PushMessageError.ToString()];
                }

                break;
            }
            Result.ErrorInformation = estr;
            Result.DeleteAlertOK = ret;
            return Result;
        }

        #region 修改报警
        /// <summary>
        /// 
        /// </summary>
        /// <param name="AccessInformation"></param>
        /// <param name="AlertInformation"></param>
        /// <returns></returns>
        public UpdateAlertResult UpdateEmailAlert(AccessControl AccessInformation, EmailAlert AlertInformation,bool checkname)
        {
            UpdateAlertResult Result = new UpdateAlertResult();
            string estr = "";
            bool ret = true;
            string strServer = "";
            string strUser = "";

            while (ret)
            {

                ret = GetAccessControlInformation(AccessInformation);
                if (!ret)
                {
                    estr = keytext[ErrorInfo.AccessControlError.ToString()];
                }

                strServer = "localhost";
                strUser = "default";

                if (AccessInformation.LimitServer)
                {
                    strServer = AccessInformation.SVDBServer;
                }

                if (AccessInformation.LimitUser)
                {
                    strUser = AccessInformation.UserID;
                }
                if (checkname)
                {
                    if (IsAlertNameExist(AccessInformation, AlertInformation.BaseInfo.Name))
                    {
                        ret = false;
                        estr = keytext[ErrorInfo.AlertNameError.ToString()];
                        break;
                    }
                }
                //email报警
                //if (string.IsNullOrEmpty(AlertInformation.EmailAddresss))
                //{
                //    ret = false;
                //    estr = keytext[ErrorInfo.EmailAddresssError.ToString()];
                //    break;
                //}


                //if (!string.IsNullOrEmpty(AlertInformation.EmailAddresss))
                //{
                //    if (!CheckEmail(AlertInformation.OtherAddress))
                //    {
                //        ret = false;
                //        estr = keytext[ErrorInfo.EmailAddresssError.ToString()];
                //        break;
                //    }
                //}

                if (!string.IsNullOrEmpty(AlertInformation.UpgradeTimes))
                {

                    if (!IsAlphaNumber(AlertInformation.UpgradeTimes))
                    {
                        ret = false;
                        estr = keytext[ErrorInfo.AlertTimesError.ToString()];
                        break;
                    }
                }

                if (!string.IsNullOrEmpty(AlertInformation.StopTimes))
                {
                    if (!IsAlphaNumber(AlertInformation.StopTimes))
                    {
                        ret = false;
                        estr = keytext[ErrorInfo.AlertTimesError.ToString()];
                        break;
                    }
                }


                break;
            }
            string strAlertIndex = "";

            while (ret)
            {
                strAlertIndex = AlertInformation.BaseInfo.ID;
                //数据存储。。
                ret = SetIniBaseAlertInfo(AccessInformation, AlertInformation.BaseInfo, estr);

                if (!ret)
                {
                    break;
                }

                //email报警
                if (this.iniFile == null)
                {
                    this.iniFile = new IniFileDaoImpl();
                }
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "EmailAdress", AlertInformation.EmailAddresss);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "OtherAdress", AlertInformation.OtherAddress);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "EmailTemplate", AlertInformation.EmailTemplate);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "WatchSheet", AlertInformation.WatchSheet);

                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "Upgrade", AlertInformation.UpgradeTimes);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "UpgradeTo", AlertInformation.ReceiverAddress);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "Stop", AlertInformation.StopTimes);

                if (queue == null)
                {
                    this.queue = new QueueDaoImpl();
                }
                //send message to service
                bool meseagebool = this.queue.PushStringMessage("SiteView70-Alert", "IniChange", "alert.ini," + strAlertIndex + ",EDIT");

                if (!meseagebool)
                {
                    estr = keytext[ErrorInfo.PushMessageError.ToString()];
                }

                break;
            }

            Result.ErrorInformation = estr;
            Result.UpdateAlertOK = ret;
            return Result;
        }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="AccessInformation"></param>
        /// <param name="AlertInformation"></param>
        /// <returns></returns>
        public UpdateAlertResult UpdateSMSAlert(AccessControl AccessInformation, SMSAlert AlertInformation,bool checkname)
        {
            UpdateAlertResult Result = new UpdateAlertResult();
            string estr = "";
            bool ret = true;
            string strServer = "";
            string strUser = "";

          while (ret)
            {

                ret = GetAccessControlInformation(AccessInformation);
                if (!ret)
                {
                    estr = keytext[ErrorInfo.AccessControlError.ToString()];
                }

                strServer = "localhost";
                strUser = "default";

                if (AccessInformation.LimitServer)
                {
                    strServer = AccessInformation.SVDBServer;
                }

                if (AccessInformation.LimitUser)
                {
                    strUser = AccessInformation.UserID;
                }
                if (checkname)
                {
                    if (IsAlertNameExist(AccessInformation, AlertInformation.BaseInfo.Name))
                    {
                        ret = false;
                        estr = keytext[ErrorInfo.AlertNameError.ToString()];
                        break;
                    }
                }
                //sms报警


                if (string.IsNullOrEmpty(AlertInformation.SmsNumber))
                {
                    ret = false;

                    break;
                }

                if (!string.IsNullOrEmpty(AlertInformation.UpgradeTimes))
                {
                    if (!IsAlphaNumber(AlertInformation.UpgradeTimes))
                    {
                        ret = false;
                        estr = keytext[ErrorInfo.UpgradeTimesError.ToString()];
                        break;
                    }
                }

                if (!string.IsNullOrEmpty(AlertInformation.StopTimes))
                {
                    if (!IsAlphaNumber(AlertInformation.StopTimes))
                    {
                        ret = false;
                        estr = keytext[ErrorInfo.StopTimesError.ToString()];
                        break;
                    }
                }

                break;
            }
            string strAlertIndex = "";

            while (ret)
            {
                strAlertIndex = AlertInformation.BaseInfo.ID;
                //数据存储。。
                ret = SetIniBaseAlertInfo(AccessInformation, AlertInformation.BaseInfo, estr);

                if (!ret)
                {
                    break;
                }

                //email报警
                if (this.iniFile == null)
                {
                    this.iniFile = new IniFileDaoImpl();
                }


                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "SmsNumber", AlertInformation.SmsNumber);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "OtherNumber", AlertInformation.OtherNumber);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "SmsSendMode", AlertInformation.SendMode);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "SmsTemplate", AlertInformation.SMSTemplate);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "WatchSheet", AlertInformation.WatchSheet);
                
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "Upgrade", AlertInformation.UpgradeTimes);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "UpgradeTo", AlertInformation.ReceiverAddress);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "Stop", AlertInformation.StopTimes);

                if (queue == null)
                {
                    this.queue = new QueueDaoImpl();
                }
                //send message to service
                bool meseagebool = this.queue.PushStringMessage("SiteView70-Alert", "IniChange", "alert.ini," + strAlertIndex + ",EDIT");

                if (!meseagebool)
                {
                    estr = keytext[ErrorInfo.PushMessageError.ToString()];
                }

                break;
            }
            Result.ErrorInformation = estr;
            Result.UpdateAlertOK = ret;
            return Result;
        }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="AccessInformation"></param>
        /// <param name="AlertInformation"></param>
        /// <returns></returns>
        public UpdateAlertResult UpdateScriptAlert(AccessControl AccessInformation, ScriptAlert AlertInformation,bool checkname)
        {
            UpdateAlertResult Result = new UpdateAlertResult();
            string estr = "";
            bool ret = true;
            string strServer = "";
            string strUser = "";

            while (ret)
            {

                ret = GetAccessControlInformation(AccessInformation);
                if (!ret)
                {
                    estr = keytext[ErrorInfo.AccessControlError.ToString()];
                }

                strServer = "localhost";
                strUser = "default";

                if (AccessInformation.LimitServer)
                {
                    strServer = AccessInformation.SVDBServer;
                }

                if (AccessInformation.LimitUser)
                {
                    strUser = AccessInformation.UserID;
                }
                if (checkname)
                {
                    if (IsAlertNameExist(AccessInformation, AlertInformation.BaseInfo.Name))
                    {
                        ret = false;
                        estr = keytext[ErrorInfo.AlertNameError.ToString()];
                        break;
                    }
                }
                //script报警

                break;
            }
            string strAlertIndex = "";

            while (ret)
            {
                strAlertIndex = AlertInformation.BaseInfo.ID;
                //数据存储。。
                ret = SetIniBaseAlertInfo(AccessInformation, AlertInformation.BaseInfo, estr);

                if (!ret)
                {
                    break;
                }

                //email报警
                if (this.iniFile == null)
                {
                    this.iniFile = new IniFileDaoImpl();
                }


                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "ScriptServer", AlertInformation.ScriptServer);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "ScriptServerID", AlertInformation.ServerID);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "ScriptFile", AlertInformation.ScriptFile);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "ScriptParam", AlertInformation.ScriptParam);


                if (queue == null)
                {
                    this.queue = new QueueDaoImpl();
                }
                //send message to service
                bool meseagebool = this.queue.PushStringMessage("SiteView70-Alert", "IniChange", "alert.ini," + strAlertIndex + ",EDIT");

                if (!meseagebool)
                {
                    estr = keytext[ErrorInfo.PushMessageError.ToString()];
                }

                break;
            }
            Result.ErrorInformation = estr;
            Result.UpdateAlertOK = ret;
            return Result;
        }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="AccessInformation"></param>
        /// <param name="AlertInformation"></param>
        /// <returns></returns>
        public UpdateAlertResult UpdateSoundAlert(AccessControl AccessInformation, SoundAlert AlertInformation,bool checkname)
        {
            UpdateAlertResult Result = new UpdateAlertResult();
            string estr = "";
            bool ret = true;
            string strServer = "";
            string strUser = "";

            while (ret)
            {

                ret = GetAccessControlInformation(AccessInformation);
                if (!ret)
                {
                    estr = keytext[ErrorInfo.AccessControlError.ToString()];
                }

                strServer = "localhost";
                strUser = "default";

                if (AccessInformation.LimitServer)
                {
                    strServer = AccessInformation.SVDBServer;
                }

                if (AccessInformation.LimitUser)
                {
                    strUser = AccessInformation.UserID;
                }
                if (checkname)
                {
                    if (IsAlertNameExist(AccessInformation, AlertInformation.BaseInfo.Name))
                    {
                        ret = false;
                        estr = keytext[ErrorInfo.AlertNameError.ToString()];
                        break;
                    }
                }
                //script报警

                break;
            }
            string strAlertIndex = "";

            while (ret)
            {
                strAlertIndex = AlertInformation.BaseInfo.ID;
                //数据存储。。
                ret = SetIniBaseAlertInfo(AccessInformation, AlertInformation.BaseInfo, estr);

                if (!ret)
                {
                    break;
                }

                //email报警
                if (this.iniFile == null)
                {
                    this.iniFile = new IniFileDaoImpl();
                }

                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "Server", AlertInformation.ServerName);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "LoginName", AlertInformation.LoginName);
                this.iniFile.WriteIniFileString("alert.ini", strAlertIndex, "LoginPwd", AlertInformation.LoginPassword);


                if (queue == null)
                {
                    this.queue = new QueueDaoImpl();
                }
                //send message to service
                bool meseagebool = this.queue.PushStringMessage("SiteView70-Alert", "IniChange", "alert.ini," + strAlertIndex + ",EDIT");

                if (!meseagebool)
                {
                    estr = keytext[ErrorInfo.PushMessageError.ToString()];
                }

                break;
            }
            Result.ErrorInformation = estr;
            Result.UpdateAlertOK = ret;
            return Result;
        }
        #endregion

        #region 查询报警rule
        public QueryAlertRuleResult QueryAlertRule(AccessControl AccessInformation, AlertRuleQueryCondition QueryCondition)
        {
            QueryAlertRuleResult Result = new QueryAlertRuleResult();
            string estr = "";
            bool ret = true;
            while (ret)
            {
                ret = GetAccessControlInformation(AccessInformation);
                if (!ret)
                {
                    estr = "";
                    break;
                }

                ret = GetAlertRuleQueryCondition(QueryCondition);
                if (!ret)
                {
                    estr = "";
                    break;
                }
                break;
            }

            if (ret)
            {
                string strServer = "localhost";
                string strUser = "default";

                if (AccessInformation.LimitServer)
                {
                    strServer = AccessInformation.SVDBServer;
                }

                if (AccessInformation.LimitUser)
                {
                    strUser = AccessInformation.UserID;
                }
                if (this.iniFile == null)
                {
                    this.iniFile = new IniFileDaoImpl();
                }
                IDictionary<string, NameValueCollection> sectionlists = this.iniFile.GetIniFile("alert.ini");
                BaseAlert[] baseAlerts = new BaseAlert[sectionlists.Count];
                int i = 0;
                foreach (string key in sectionlists.Keys)
                {
                    baseAlerts[i] = GetIniBaseAlertInfo(key, sectionlists);
                    i = i + 1;
                }
                Result.ResponseData = baseAlerts;
            }

            Result.ErrorInformation = estr;
            Result.QueryAlertRuleOK = ret;

            return Result;
        }
        #endregion

        #region 查询日志

        public QueryAlertLogResult QueryAlertLog(AccessControl AccessInformation, AlertLogQueryCondition QueryCondition)
        {
            QueryAlertLogResult Result = new QueryAlertLogResult();
            if (QueryCondition.LimitTime)
            {
                anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
               {
                Helper.XfireCreateKeyValue("dowhat","QueryRecordsByTime"),
                Helper.XfireCreateKeyValue("id","alertlogs"),
                Helper.XfireCreateKeyValue("begin_year",QueryCondition.StartTime.Year.ToString()),
                Helper.XfireCreateKeyValue("begin_month",QueryCondition.StartTime.Month.ToString()),
                Helper.XfireCreateKeyValue("begin_day",QueryCondition.StartTime.Day.ToString()),
                Helper.XfireCreateKeyValue("begin_hour",QueryCondition.StartTime.Hour.ToString()),
                Helper.XfireCreateKeyValue("begin_minute",QueryCondition.StartTime.Minute.ToString()),
                Helper.XfireCreateKeyValue("begin_second",QueryCondition.StartTime.Second.ToString()),
                Helper.XfireCreateKeyValue("end_year",QueryCondition.EndTime.Year.ToString()),
                Helper.XfireCreateKeyValue("end_month",QueryCondition.EndTime.Month.ToString()),
                Helper.XfireCreateKeyValue("end_day",QueryCondition.EndTime.Day.ToString()),
                Helper.XfireCreateKeyValue("end_hour",QueryCondition.EndTime.Hour.ToString()),
                Helper.XfireCreateKeyValue("end_minute",QueryCondition.EndTime.Minute.ToString()),
                Helper.XfireCreateKeyValue("end_second",QueryCondition.EndTime.Second.ToString())
              };
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
                IList<NameValueCollection> resultData = Helper.XfireConventMapEntryToList(result.vmap);
            }
            else
            {

            }

            return Result;
        }
        #endregion

        #endregion
    }
}
