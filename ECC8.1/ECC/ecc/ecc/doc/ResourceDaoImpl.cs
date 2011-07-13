using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Text;
using SiteView.Ecc.Core.Dao;
using SiteView.Ecc.WSClient.EccWebService;
using SiteView.Ecc.Core;
using SiteView.Ecc.WSClient.XFireWebService;
namespace SiteView.Ecc.WSClient
{
    public class ResourceDaoImpl : IResourceDao
    {        
        //private ECCWebService service = new ECCWebService();
        private XFireWebService.InterfaceEccService service = Helper.service;
        private NameValueCollection resource;
        private string language = "chinese";

        private static readonly object lockObj = new object();
        private static log4net.ILog logger = log4net.LogManager.GetLogger(typeof(ResourceDaoImpl));

        #region IResourceDao ≥…‘±

        private void Load()
        {
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","LoadResource"),
                Helper.XfireCreateKeyValue("language",this.language)
            };
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
            IDictionary<string,NameValueCollection> data = Helper.XfireConventMapEntryToDictionary(result.vmap);
            this.resource = data["property"];
        }

        /// <summary>
        /// 
        /// </summary>
        private  NameValueCollection Resource
        {
            get
            {
                if (this.resource == null)
                {
                    lock (lockObj)
                    {
                        if (this.resource == null)
                        {
                            this.Load();
                        }
                    }
                }

                return this.resource;
            }
        }

        

        public void SetLanguage(string language)
        {
            if (!this.language.Equals(language))
            {
                this.resource = null;
                this.language = language;
            }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="key"></param>
        /// <returns></returns>
        public string this[string key]
        {
            get { return this.Resource[key]; }
            set { this.Resource[key] = value; }
        }

        #endregion
    }
}
