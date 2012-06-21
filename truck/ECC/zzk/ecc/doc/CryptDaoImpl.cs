using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Text;

using SiteView.Ecc.Core.Dao;
//using SiteView.Ecc.WSClient.EccWebService;
using SiteView.Ecc.WSClient.XFireWebService;
using SiteView.Ecc.Core;

namespace SiteView.Ecc.WSClient
{
    /// <summary>
    /// 加密，解密访问实现
    /// </summary>
    public class CryptDaoImpl : ICryptDao
    {
        private InterfaceEccService service = new InterfaceEccService();
        private log4net.ILog logger = log4net.LogManager.GetLogger(typeof(CryptDaoImpl));

        public IDictionary<string, NameValueCollection> Encrypt(string[] values)
        {
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[values.Length + 1];
            requestData[0] = Helper.XfireCreateKeyValue("dowhat","encrypt");

            for (int i = 1; i <= values.Length; i++)
            {
                //requestData[i] = Helper.XfireCreateKeyValue("X" + i.ToString(), values[i - 1]);
                requestData[i] = Helper.XfireCreateKeyValue(values[i - 1],"");
            }

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

            return Helper.XfireConventMapEntryToDictionary(result.vmap);
        }

        public IDictionary<string, NameValueCollection> Decrypt(string[] values)
        {
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[values.Length + 1];
            requestData[0] = Helper.XfireCreateKeyValue("dowhat", "decrypt");

            for (int i = 1; i <= values.Length; i++)
            {
                //requestData[i] = Helper.XfireCreateKeyValue("X" + i.ToString(), values[i - 1]);
                requestData[i] = Helper.XfireCreateKeyValue(values[i - 1], "");
            }

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

            return Helper.XfireConventMapEntryToDictionary(result.vmap);
        }
    }
}
