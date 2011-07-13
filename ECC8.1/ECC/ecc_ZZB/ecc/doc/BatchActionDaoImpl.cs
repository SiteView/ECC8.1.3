using System;
using System.Text;
using System.Collections;
using System.Collections.Generic;
using System.Collections.Specialized;

using SiteView.Ecc.Core;
using SiteView.Ecc.Core.Dao;
using SiteView.Ecc.Core.Models;
using SiteView.Ecc.WSClient.EccWebService;
//using SiteView.Ecc.WSClient.EccService;
using SiteView.Ecc.WSClient.XFireWebService;

namespace SiteView.Ecc.WSClient
{
    /// <summary>
    /// ��������豸���������
    /// </summary>
    public class BatchActionDaoImpl
    {
        private static log4net.ILog logger = log4net.LogManager.GetLogger(typeof(MonitorDaoImpl));
        //private ECCWebService service = new ECCWebService();
        private XFireWebService.InterfaceEccService service = Helper.service;

        #region BatchActionDaoImpl ��Ա

        /// <summary>
        /// 
        /// </summary>
        /// <param name="monitors"></param>
        /// <returns></returns>
        public string[] BatchAddMonitors(ArrayList monitors)
        {
            //������������
            IDictionary<string, NameValueCollection> data = new Dictionary<string, NameValueCollection>();

            int i = 0;
            foreach(NameValueCollection monitor in monitors)
            {
                string strTmp = "";
                foreach (string strKey in monitor.Keys)
                {
                    strTmp += ("monitor_" + strKey.Split(':')[0]);
                    strTmp += "_";
                    strTmp += i.ToString();

                    if (data.ContainsKey(strTmp))
                    {
                        data[strTmp][strKey.Split(':')[1]] = monitor[strKey];
                    }
                    else
                    {
                        data[strTmp] = new NameValueCollection();
                        data[strTmp][strKey.Split(':')[1]] = monitor[strKey];
                    }

                    strTmp = "";
                }

                i++;
            }

            anyType2anyTypeMapEntry[][] values = Helper.XfireConventDictionaryToMapEntry(data);

            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","AdvanceAddManyMonitor"),
                Helper.XfireCreateKeyValue("autoCreateTable","true")
            };
            
            this.service.Url = UserPermissionContext.Instance.Url;
            int oldTimeout = this.service.Timeout;
            this.service.Timeout = 600000;

            RetMapInVector result = ServiceClient.SubmitUnivData2(values, requestData);
            if (!result.retbool)
            {
                if (logger.IsErrorEnabled)
                {
                    logger.Error(result.estr);
                }
                throw new System.Net.WebException("����Web Serviceʧ�ܣ�" + result.estr);
            }            
            
            IDictionary<string, NameValueCollection> resultData = Helper.XfireConventMapEntryToDictionary(result.vmap);
            string[] monitorids = new string[resultData["return"].Count - 1];
            i = 0;
            foreach (string id in resultData["return"].AllKeys)
            {
                if (id.ToLower() == "return")
                {
                    continue;
                }
                monitorids[i] = id;
                i = i + 1;
            }

            this.service.Timeout = oldTimeout;

            return monitorids;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="entitys"></param>
        /// <returns></returns>
        public string[] BatchAddEntitys(ArrayList entitys)
        {
            //�����豸����
            IDictionary<string, NameValueCollection> data = new Dictionary<string, NameValueCollection>();

            int i = 0;
            foreach (NameValueCollection entity in entitys)
            {
                string strTmp = "";
                strTmp = "entity_" + i.ToString();
                data[strTmp] = entity;
                i++;
            }

            anyType2anyTypeMapEntry[][] values = Helper.XfireConventDictionaryToMapEntry(data);

            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","AddManyMonitor"),
                //Helper.XfireCreateKeyValue("parentid",parentid),
                Helper.XfireCreateKeyValue("autoCreateTable","true")
            };

            this.service.Url = UserPermissionContext.Instance.Url;
            int oldTimeout = this.service.Timeout;
            this.service.Timeout = 600000;

            RetMapInVector result = ServiceClient.SubmitUnivData2(values, requestData);
            if (!result.retbool)
            {
                if (logger.IsErrorEnabled)
                {
                    logger.Error(result.estr);
                }
                throw new System.Net.WebException("����Web Serviceʧ�ܣ�" + result.estr);
            }

            IDictionary<string, NameValueCollection> resultData = Helper.XfireConventMapEntryToDictionary(result.vmap);
            string[] entityids = new string[resultData["return"].Count - 1];
            i = 0;
            foreach (string id in resultData["return"].AllKeys)
            {
                if (id.ToLower() == "return")
                {
                    continue;
                }
                entityids[i] = id;
                i = i + 1;
            }

            this.service.Timeout = oldTimeout;

            return entityids;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="monitors"></param>
        /// <returns></returns>
        public string[] BatchUpdateMonitors(ArrayList monitors)
        {
            return null;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="entitys"></param>
        /// <returns></returns>
        public string[] BatchUpdateEntitys(ArrayList entitys)
        {
            return null;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="stIds"></param>
        /// <returns></returns>
        public bool BatchDeleteObjects(string stIds)
        {
            this.service.Url = UserPermissionContext.Instance.Url;
            return Helper.XfireDeletetNode(this.service, stIds);
        }               

        #endregion
        
    }
}
