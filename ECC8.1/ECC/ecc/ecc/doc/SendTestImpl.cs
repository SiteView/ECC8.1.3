using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Text;
using System.Data;

using SiteView.Ecc.Core.Dao;
using SiteView.Ecc.Core.Models;
using SiteView.Ecc.WSClient.EccWebService;
using SiteView.Ecc.Core;
using SiteView.Ecc.WSClient.XFireWebService;
namespace SiteView.Ecc.WSClient
{
    /// <summary>
    /// 报告数据获取接口
    /// </summary>
    public class SendTestImpl
    {
        #region 内部成员

        /// <summary>
        /// WebService接口
        /// </summary>
        //private ECCWebService service = new ECCWebService();
        private XFireWebService.InterfaceEccService service = Helper.service;
        /// <summary>
        /// 错误日志
        /// </summary>
        private log4net.ILog logger = log4net.LogManager.GetLogger(typeof(ReportTableDaoImpl));

        #endregion
        
        #region 外部接口

        /// <summary>
        /// SmsTest
        /// </summary>
        /// <param name="strPhoneNumber"></param>
        /// <param name="bByWebSms"></param>
        /// <returns></returns>
        public bool SmsTest(string strPhoneNumber, bool bByWebSms)
        {
            //组织输入参数
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","SmsTest"),
                Helper.XfireCreateKeyValue("phoneNumber",strPhoneNumber),
                Helper.XfireCreateKeyValue("ByWebSms",bByWebSms?"true":"false")
            };

            //通过WebService查数据
            this.service.Url = UserPermissionContext.Instance.Url;

            RetMapInVector result = ServiceClient.GetUnivData2(requestData);
            if (!result.retbool)
            {
                if (logger.IsErrorEnabled)
                {
                    logger.Error(result.estr);
                }
            }

            return result.retbool;
        }

        /// <summary>
        /// EmailTest
        /// </summary>
        /// <param name="strMailServer"></param>
        /// <param name="strMailTo"></param>
        /// <param name="strMailFrom"></param>
        /// <param name="strUser"></param>
        /// <param name="strPassword"></param>
        /// <param name="strSubject"></param>
        /// <param name="strContent"></param>
        /// <returns></returns>
        public bool EmailTest(string strMailServer, string strMailTo, string strMailFrom, string strUser, 
                  string strPassword, string strSubject, string strContent)
        {
            //组织输入参数
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","EmailTest"),
                Helper.XfireCreateKeyValue("mailServer",strMailServer),
                Helper.XfireCreateKeyValue("mailTo",strMailTo),
                Helper.XfireCreateKeyValue("mailFrom",strMailFrom),
                Helper.XfireCreateKeyValue("user",strUser),
                Helper.XfireCreateKeyValue("password",strPassword),
                Helper.XfireCreateKeyValue("subject",strSubject),
                Helper.XfireCreateKeyValue("content",strContent)
            };

            //通过WebService查数据
            this.service.Url = UserPermissionContext.Instance.Url;

            RetMapInVector result = ServiceClient.GetUnivData2(requestData);
            if (!result.retbool)
            {
                if (logger.IsErrorEnabled)
                {
                    logger.Error(result.estr);
                }
            }

            return result.retbool;
        }

        /// <summary>
        /// SmsTestByDll
        /// </summary>
        /// <param name="strPhoneNumber"></param>
        /// <param name="strParameter"></param>
        /// <param name="strDllName"></param>
        /// <returns></returns>
        public bool SmsTestByDll(string strPhoneNumber, string strDllName, string strParameter)
        {
            //组织输入参数
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","SmsTestByDll"),
                Helper.XfireCreateKeyValue("phoneNumber",strPhoneNumber),
                Helper.XfireCreateKeyValue("parameter",strParameter),
                Helper.XfireCreateKeyValue("dllName",strDllName)
            };

            //通过WebService查数据
            this.service.Url = UserPermissionContext.Instance.Url;

            RetMapInVector result = ServiceClient.GetUnivData2(requestData);
            if (!result.retbool)
            {
                if (logger.IsErrorEnabled)
                {
                    logger.Error(result.estr);
                }
            }

            return result.retbool;
        }

        /// <summary>
        /// GetSmsDllName
        /// </summary>
        /// <returns></returns>
        public IDictionary<string, NameValueCollection> GetSmsDllName()
        {
            //组织输入参数
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","GetSmsDllName")
            };

            //通过WebService查数据
            this.service.Url = UserPermissionContext.Instance.Url;

            RetMapInVector result = ServiceClient.GetUnivData2(requestData);
            if (!result.retbool)
            {
                if (logger.IsErrorEnabled)
                {
                    logger.Error(result.estr);
                }
                throw new Exception(result.estr);
            }

            IDictionary<string, NameValueCollection> resultData = Helper.XfireConventMapEntryToDictionary(result.vmap);

            return resultData;
        }

        #endregion
    }
}
