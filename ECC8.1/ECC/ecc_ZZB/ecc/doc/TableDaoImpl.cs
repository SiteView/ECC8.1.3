using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Text;

using SiteView.Ecc.Core;
using SiteView.Ecc.Core.Dao;
using SiteView.Ecc.Core.Models;
using SiteView.Ecc.WSClient.EccWebService;
using SiteView.Ecc.WSClient.XFireWebService;

namespace SiteView.Ecc.WSClient
{
    /// <summary>
    /// �û����ݷ���ʵ��
    /// </summary>
    public class TableDaoImpl
    {
        /// <summary>
        /// 
        /// </summary>
        private static XFireWebService.InterfaceEccService service = Helper.service;

        /// <summary>
        /// 
        /// </summary>
        static IDictionary<string, NameValueCollection> data = new Dictionary<string, NameValueCollection>();

        #region TableDaoImpl ��Ա

        /// <summary>
        /// ��Ӳ�����־
        /// </summary>
        /// <param name="strUserID"></param>
        /// <param name="strOperateTime"></param>
        /// <param name="strOperateType"></param>
        /// <param name="strOperateObjName"></param>
        /// <param name="strOperateObjInfo"></param>
        public static void AddUserOperateLogRecord(string strUserID, string strOperateTime, string strOperateObjName, string strOperateType, string strOperateObjInfo)
        {
            int nCount = data.Count + 1;
            
            NameValueCollection values = new NameValueCollection();
            values.Add("_UserID", strUserID);
            values.Add("_OperateTime", strOperateTime);
            values.Add("_OperateType", strOperateType);
            values.Add("_OperateObjName", strOperateObjName);
            values.Add("_OperateObjInfo", strOperateObjInfo);
            
            data.Add("OperateLog_" + nCount.ToString(), values);
            
            if(nCount < 10)
            { 
                return;
            }

            anyType2anyTypeMapEntry[][] logData = Helper.XfireConventDictionaryToMapEntry(data);

            service.Url = UserPermissionContext.Instance.Url;
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","AppendOperateLog")
            };

            RetMapInVector result = ServiceClient.SubmitUnivData2(logData, requestData);

            data.Clear();

            if (!result.retbool)
            {
                throw new System.Net.WebException("����WebServiceʧ�ܣ����������");
            }
        }

        /// <summary>
        /// 
        /// </summary>
        public static void SubmitUserOperateLogRecord()
        {
            if (data.Count <= 0)
                return;

            anyType2anyTypeMapEntry[][] logData = Helper.XfireConventDictionaryToMapEntry(data);

            service.Url = UserPermissionContext.Instance.Url;
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","AppendOperateLog")
            };

            RetMapInVector result = ServiceClient.SubmitUnivData2(logData, requestData);

            data.Clear();

            if (!result.retbool)
            {
                throw new System.Net.WebException("����WebServiceʧ�ܣ����������");
            }
        }

        /// <summary>
        /// ɾ��ָ������ָ��ʱ����ǰ�ļ�¼
        /// </summary>
        public static IDictionary<string, NameValueCollection> DeleteTableRecords(string strTableId, DateTime tmFrom)
        {
            //��֯�������
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","DeleteRecords"),
                Helper.XfireCreateKeyValue("id",strTableId),
                Helper.XfireCreateKeyValue("year",tmFrom.Year.ToString()),
                Helper.XfireCreateKeyValue("month",tmFrom.Month.ToString()),
                Helper.XfireCreateKeyValue("day",tmFrom.Day.ToString()),
                Helper.XfireCreateKeyValue("hour",tmFrom.Hour.ToString()),
                Helper.XfireCreateKeyValue("minute",tmFrom.Minute.ToString()),
                Helper.XfireCreateKeyValue("second",tmFrom.Second.ToString())
            };

            //ͨ��WebService������
            service.Url = UserPermissionContext.Instance.Url;

            RetMapInVector result = ServiceClient.GetUnivData2(requestData);
            if (!result.retbool)
            {
                throw new Exception(result.estr);
            }

            IDictionary<string, NameValueCollection> resultData = Helper.XfireConventMapEntryToDictionary(result.vmap);

            return resultData;            
        }

        #endregion
    }
}
