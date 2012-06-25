using System;
using System.Collections.Generic;
using System.Text;

using SiteView.Ecc.Core.Dao;
using SiteView.Ecc.WSClient.XFireWebService;
using SiteView.Ecc.Core;
using SiteView.Ecc.Core.Models;
using System.Collections.Specialized;

namespace SiteView.Ecc.WSClient
{
    public class QueueDaoImpl : IQueueDao
    {

        #region IQueueDao ≥…‘±
        private InterfaceEccService service = new InterfaceEccService();
        private static log4net.ILog logger = log4net.LogManager.GetLogger(typeof(QueueDaoImpl));
        
        public void CreateQueue(string queuename)
        {
            this.service.Url = UserPermissionContext.Instance.Url;
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","CreateQueue"),
                Helper.XfireCreateKeyValue("queuename",queuename),
            };
            RetMapInVector result = ServiceClient.GetUnivData2(requestData);
            if (!result.retbool)
            {
                if (logger.IsWarnEnabled)
                {
                    logger.Warn(result.estr);
                }
            }
            IDictionary<string, NameValueCollection> resultData = Helper.XfireConventMapEntryToDictionary(result.vmap);

        }

        public bool PushStringMessage(string queuename, string label, string content)
        {
            this.service.Url = UserPermissionContext.Instance.Url;
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","PushStringMessage"),
                Helper.XfireCreateKeyValue("queuename",queuename),
                Helper.XfireCreateKeyValue("label",label),
                Helper.XfireCreateKeyValue("content",content)
            };
            RetMapInVector result = ServiceClient.GetUnivData2(requestData);
            bool rs = false;
            if (result.retbool)
            {
                rs = true;
            }
            else
            {
                if (logger.IsWarnEnabled)
                {
                    logger.Warn(result.estr);
                }
            }
            IDictionary<string, NameValueCollection> resultData = Helper.XfireConventMapEntryToDictionary(result.vmap);
            return rs;

        }

        public void GetMQRecordCount(string queuename)
        {
            this.service.Url = UserPermissionContext.Instance.Url;
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","GetMQRecordCount"),
                Helper.XfireCreateKeyValue("queuename",queuename),
            };
            RetMapInVector result = ServiceClient.GetUnivData2(requestData);
            if (!result.retbool)
            {
                if (logger.IsWarnEnabled)
                {
                    logger.Warn(result.estr);
                }
            }
            IDictionary<string, NameValueCollection> resultData = Helper.XfireConventMapEntryToDictionary(result.vmap);

        }

        public void GetAllQueueNames()
        {
            this.service.Url = UserPermissionContext.Instance.Url;
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","GetAllQueueNames"),
            };
            RetMapInVector result = ServiceClient.GetUnivData2(requestData);
            if (!result.retbool)
            {
                if (logger.IsWarnEnabled)
                {
                    logger.Warn(result.estr);
                }
            }
            IDictionary<string, NameValueCollection> resultData = Helper.XfireConventMapEntryToDictionary(result.vmap);
        }

        public void DeleteQueue(string queuename)
        {
            this.service.Url = UserPermissionContext.Instance.Url;
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","DeleteQueue"),
                Helper.XfireCreateKeyValue("queuename",queuename),
            };
            RetMapInVector result = ServiceClient.GetUnivData2(requestData);
            if (!result.retbool)
            {
                if (logger.IsWarnEnabled)
                {
                    logger.Warn(result.estr);
                }
            }
            IDictionary<string, NameValueCollection> resultData = Helper.XfireConventMapEntryToDictionary(result.vmap);
        }

        public void ClearQueueMessage(string queuename)
        {
            this.service.Url = UserPermissionContext.Instance.Url;
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","ClearQueueMessage"),
                Helper.XfireCreateKeyValue("queuename",queuename),
            };
            RetMapInVector result = ServiceClient.GetUnivData2(requestData);
            if (!result.retbool)
            {
                if (logger.IsWarnEnabled)
                {
                    logger.Warn(result.estr);
                }
            }
            IDictionary<string, NameValueCollection> resultData = Helper.XfireConventMapEntryToDictionary(result.vmap);
        }
       
        #endregion


        
    }
        
}
