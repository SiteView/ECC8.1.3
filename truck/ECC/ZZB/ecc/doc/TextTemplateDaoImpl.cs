using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Text;

using SiteView.Ecc.Core.Dao;

namespace SiteView.Ecc.WSClient
{
    public class TextTemplateDaoImpl : ITextTemplateDao
    {
        private IList<KeyValuePair<string, string>> mailTempletList;
        private IList<KeyValuePair<string, string>> scriptionTemletList;
        private IList<KeyValuePair<string, string>> smsTempletList;

        private IniFileDaoImpl iniFile;

        private static readonly object lockObj = new object();

        private IList<KeyValuePair<string, string>> GetTemplet(string section)
        {
            if (this.iniFile == null)
            {
                lock (lockObj)
                {
                    if (this.iniFile == null)
                    {
                        this.iniFile = new IniFileDaoImpl();
                        IDictionary<string,NameValueCollection> templets = this.iniFile.GetIniFile("TXTTemplate.ini");

                        this.mailTempletList = BuildTempletList(templets, "Email");
                        this.scriptionTemletList = BuildTempletList(templets, "Scripts");
                        this.smsTempletList = BuildTempletList(templets, "SMS");
                    }
                }
            }

            IList<KeyValuePair<string, string>> templet = null;
            switch (section)
            {
                case "Email":
                    templet = this.mailTempletList;
                    break;
                case "Scripts":
                    templet = this.scriptionTemletList;
                    break;
                case "SMS":
                    templet = this.smsTempletList;
                    break;
            }
            return templet;
        }

        private static List<KeyValuePair<string, string>> BuildTempletList(IDictionary<string, NameValueCollection> templets,string scetion)
        {
            List<KeyValuePair<string, string>> templetList = new List<KeyValuePair<string, string>>();
            NameValueCollection nameValues = templets[scetion];
            if (nameValues == null)
            {
                return null;
            }
            foreach (string key in nameValues.Keys)
            {
                templetList.Add(new KeyValuePair<string, string>(key, nameValues[key]));
            }
            return templetList;
        }

        #region ITextTemplateDao ≥…‘±

        public IList<KeyValuePair<string, string>> GetMailTemplet()
        {
            return this.GetTemplet("Email");
        }

        public IList<KeyValuePair<string, string>> GetScriptTemplet()
        {
            return this.GetTemplet("Scripts");
        }

        public IList<KeyValuePair<string, string>> GetSmsTemplet()
        {
            return this.GetTemplet("SMS");
        }

        #endregion
    }
}
