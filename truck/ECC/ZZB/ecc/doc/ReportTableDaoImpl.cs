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
    /// �������ݻ�ȡ�ӿ�
    /// </summary>
    public class ReportTableDaoImpl
    {
        
        #region �ⲿ����

        /// <summary>
        /// Ϊ���򷵻����з���ֵ, 
        /// ������,�ָ��Ĵ� ��sv_primary,sv_drawimage ��ʾ ֻ��ʾsv_primary=1��sv_drawimage�ķ���ֵ
        /// </summary>
        public string ReturnFilter
        {
            get { return strFilter; }
            set { strFilter = value; }
        }

        /// <summary>
        /// ����
        /// </summary>
        public string Name
        {
            get { return name; }
            set { name = value; }
        }

        /// <summary>
        /// ͼƬ����Hash�б� KeyΪ����ֵ���ݽڵ��Key �����hashKey��Keyһ��
        /// </summary>
        public IDictionary<string, DataTable> ImgTableHash
        {
            get { return imgTables; }
            set { imgTables = value; }
        }
        /// <summary>
        /// �������ͳ�Ʊ�
        /// </summary>
        public DataTable RunTable
        {
            get { return this.primaryTable; }
            set { this.primaryTable = value; }
        }
        /// <summary>
        /// ������ͳ�Ʊ�
        /// </summary>
        public DataTable StatsTable
        {
            get { return this.measureTable; }
            set { this.measureTable = value; }
        }
        /// <summary>
        /// ���������
        /// </summary>
        public DataTable DstrTable
        {
            get { return this.dstrTable; }
            set { this.dstrTable = value; }
        }

        #endregion

        #region �ڲ���Ա
        
        /// <summary>
        /// 
        /// </summary>
        private IDictionary<string, DataTable> imgTables;
        /// <summary>
        /// 
        /// </summary>
        private DataTable primaryTable;
        /// <summary>
        /// 
        /// </summary>
        private DataTable measureTable;
        /// <summary>
        /// 
        /// </summary>
        private DataTable dstrTable;
        /// <summary>
        /// 
        /// </summary>
        private string name;

        private string strFilter = "";
        /// <summary>
        /// WebService�ӿ�
        /// </summary>
        //private ECCWebService service = new ECCWebService();
        private XFireWebService.InterfaceEccService service = Helper.service;        
        /// <summary>
        /// ������־
        /// </summary>
        private log4net.ILog logger = log4net.LogManager.GetLogger(typeof(ReportTableDaoImpl));

        #endregion

        #region �ڲ�����

        /// <summary>
        /// ͨ��WebService������ �������� ����֯��DataTable
        /// </summary>
        /// <param name="requestData"></param>
        /// <returns></returns>
        private DataTable CreateDataTable(anyType2anyTypeMapEntry[] requestData)
        {
            //ͨ��WebService������
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
            
            //�������ݲ���֯��DataTable            
            DataTable runTable = new DataTable();
            runTable.Columns.Add("����");
            runTable.Columns.Add("����(%)");
            runTable.Columns.Add("Σ��(%)");
            runTable.Columns.Add("����(%)");
            runTable.Columns.Add("��ֵ");
            runTable.Columns.Add("����״̬");
            runTable.Columns.Add("���һ������");

            //��ô��ʾhashKey�����? ...
            DataTable statTable = new DataTable();
            statTable.Columns.Add("hashKey");
            statTable.Columns.Add("����");
            statTable.Columns.Add("����ֵ����");
            statTable.Columns.Add("���ֵ");
            statTable.Columns.Add("��Сֵ");
            statTable.Columns.Add("ƽ��ֵ");
            statTable.Columns.Add("���һ��");
            statTable.Columns.Add("���ֵʱ��");


            DataTable m_dstrTable = new DataTable();
            m_dstrTable.Columns.Add("����");
            m_dstrTable.Columns.Add("ʱ��");
            m_dstrTable.Columns.Add("״̬");
            m_dstrTable.Columns.Add("����ֵ");
            
            string[] strFilters = strFilter.Split(',');

            DataRow dr = null;
            IDictionary<string, DataTable> hashImgTable = new Dictionary<string, DataTable>();           

            IDictionary<string, NameValueCollection> resultData = Helper.XfireConventMapEntryToDictionary(result.vmap);
            foreach (string key in resultData.Keys)
            {   
                if (key.StartsWith("(Return"))
                {
                    //��ô��֯ͳ�Ʊ��� �� dstr��� �ı���ʽ��? ... --> StatReportר��

                    //��ô���ݷ���ֵ��sv_primary����������? ...--> ��̨����Щ���Է���������ʱֻ��ֵΪ 1 + and ��ϵ
                    bool bFilter = false;
                    if (!strFilter.Equals(""))
                    {
                        foreach (string strTmp in strFilters)
                        {
                            if (!resultData[key][strTmp].Equals("1"))
                            {
                                bFilter = true;
                                break;
                            }                            
                        }
                    }
                    else
                    {
                        //strFilter Ϊ�������з���ֵ������
                    }

                    if (bFilter)
                        continue;

                    //���ͳ�Ʊ�

                    string strImageData = resultData[key]["detail"].ToString();
                    if (strImageData == "")
                        continue;

                    
                    dr = statTable.NewRow();
                    //����  ����ֵ����  ���  ƽ��  ���һ�� 
                    //latest max min MonitorName ReturnName average
                    //dr["monitorid"] = key;
                    dr["hashKey"] = key; //hashKey ��ǰ̨�ÿؼ����β���ʾ����
                    dr["����"] = resultData[key]["MonitorName"].ToString();
                    dr["����ֵ����"] = resultData[key]["ReturnName"].ToString();
                    dr["���ֵ"] = resultData[key]["max"].ToString();
                    dr["��Сֵ"] = resultData[key]["min"].ToString();
                    dr["ƽ��ֵ"] = resultData[key]["average"].ToString();
                    dr["���һ��"] = resultData[key]["latest"].ToString();
                    dr["���ֵʱ��"] = resultData[key]["when_max"].ToString();
                    
                    statTable.Rows.Add(dr);

                    //ͼƬ���ݱ�
                    //monitorid -> value = ReturnValue -> key detail time=value
                    string []imgDatas = strImageData.Split(',');

                    hashImgTable.Add(key, new DataTable());
                    //hashImgTable[key] = new DataTable();
                    DataColumn column = null;
                    column = new DataColumn();
                    column.DataType = System.Type.GetType("System.DateTime");
                    column.ColumnName = "ʱ��";
                    hashImgTable[key].Columns.Add(column);

                    column = new DataColumn();
                    column.DataType = System.Type.GetType("System.Single");
                    column.ColumnName = resultData[key]["ReturnName"].ToString();
                    hashImgTable[key].Columns.Add(column);
                    
                    foreach (string strImg in imgDatas)
                    {
                        string[] strTmps = strImg.Split('=');
                        
                        if(strTmps.Length == 2)
                        {
                            //����ֵ���ַ�����ô����? ...--> ��̨�Ѿ�������(�ַ����ͷ���ֵû�з�������)

                            dr = hashImgTable[key].NewRow();
                            dr["ʱ��"] = strTmps[0];

                            if (strTmps[1].StartsWith("(status)"))
                                dr[resultData[key]["ReturnName"].ToString()] = "0";
                            else
                                dr[resultData[key]["ReturnName"].ToString()] = float.Parse(strTmps[1]);

                            hashImgTable[key].Rows.Add(dr);
                        }
                    }
                }
                else if (key.StartsWith("(dstr"))
                {
                    //dstr ��
                    //monitor���� + ʱ�� + dstr
                    string strName = resultData[key]["MonitorName"];
                    foreach (string strKey in resultData[key].AllKeys)
                    {
                        if (strKey != "MonitorName")
                        {
                            dr = m_dstrTable.NewRow();
                            dr["����"] = strName;
                            dr["ʱ��"] = strKey;
                            dr["״̬"] = resultData[key][strKey].ToString().Substring(0, 8).Trim();
                            dr["����ֵ"] = resultData[key][strKey].ToString().Substring(7).Trim();
                            m_dstrTable.Rows.Add(dr);
                        }
                    }
                }
                else
                {
                    //
                    if (key.Equals("return"))
                        continue;

                    //���������
                    //����  ����(%)  Σ��(%)  ����(%)  ��ֵ 
                    //monitorid -> MonitorName errorCondition errorPercent latestStatus okPercent warnPercent 
                    dr = runTable.NewRow();
                    //dr["monitorid"] = key;
                    dr["����"] = resultData[key]["MonitorName"].ToString();
                    dr["����(%)"] = resultData[key]["okPercent"].ToString();
                    dr["Σ��(%)"] = resultData[key]["warnPercent"].ToString();
                    dr["����(%)"] = resultData[key]["errorPercent"].ToString();
                    dr["��ֵ"] = resultData[key]["errorCondition"].ToString();                    
                    dr["����״̬"] = resultData[key]["latestStatus"].ToString();
                    dr["���һ������"] = resultData[key]["latestDstr"].ToString();                                        
                    
                    runTable.Rows.Add(dr);
                }
            }

            //
            this.imgTables = hashImgTable;
            this.measureTable = statTable;
            this.primaryTable = runTable;
            this.dstrTable = m_dstrTable;

            return runTable;
        }

        #endregion

        #region �ⲿ�ӿ�

        /// <summary>
        /// QueryReportData
        /// </summary>
        /// <param name="strIdList"></param>
        /// <param name="bCompress"></param>
        /// <param name="beginTime"></param>
        /// <param name="endTime"></param>
        /// <returns></returns>
        public DataTable QueryReportData(string strIdList, bool bCompress, DateTime beginTime, DateTime endTime)
        {
            //��֯�������
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","QueryReportData"),
                Helper.XfireCreateKeyValue("id",strIdList),
                Helper.XfireCreateKeyValue("compress",bCompress?"true":"false"), 
                Helper.XfireCreateKeyValue("begin_year",beginTime.Year.ToString()),
                Helper.XfireCreateKeyValue("begin_month",beginTime.Month.ToString()),
                Helper.XfireCreateKeyValue("begin_day",beginTime.Day.ToString()),
                Helper.XfireCreateKeyValue("begin_hour",beginTime.Hour.ToString()),
                Helper.XfireCreateKeyValue("begin_minute",beginTime.Minute.ToString()),
                Helper.XfireCreateKeyValue("begin_second",beginTime.Second.ToString()),
                Helper.XfireCreateKeyValue("end_year",endTime.Year.ToString()),
                Helper.XfireCreateKeyValue("end_month",endTime.Month.ToString()),
                Helper.XfireCreateKeyValue("end_day",endTime.Day.ToString()),
                Helper.XfireCreateKeyValue("end_hour",endTime.Hour.ToString()),
                Helper.XfireCreateKeyValue("end_minute",endTime.Minute.ToString()),
                Helper.XfireCreateKeyValue("end_second",endTime.Second.ToString())
            };

            return CreateDataTable(requestData);
        }
        
        /// <summary>
        /// QueryReportData
        /// </summary>
        /// <param name="strIdList"></param>
        /// <param name="bCompress"></param>
        /// <param name="beginTime"></param>
        /// <param name="endTime"></param>
        /// <returns></returns>
        public DataTable QueryReportData(string strIdList, bool bCompress, bool bDstrNeed, DateTime beginTime, DateTime endTime)
        {
            //��֯�������
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","QueryReportData"),
                Helper.XfireCreateKeyValue("id",strIdList),
                Helper.XfireCreateKeyValue("compress",bCompress?"true":"false"), 
                Helper.XfireCreateKeyValue("dstrNeed",bDstrNeed?"true":"false"), 
                Helper.XfireCreateKeyValue("begin_year",beginTime.Year.ToString()),
                Helper.XfireCreateKeyValue("begin_month",beginTime.Month.ToString()),
                Helper.XfireCreateKeyValue("begin_day",beginTime.Day.ToString()),
                Helper.XfireCreateKeyValue("begin_hour",beginTime.Hour.ToString()),
                Helper.XfireCreateKeyValue("begin_minute",beginTime.Minute.ToString()),
                Helper.XfireCreateKeyValue("begin_second",beginTime.Second.ToString()),
                Helper.XfireCreateKeyValue("end_year",endTime.Year.ToString()),
                Helper.XfireCreateKeyValue("end_month",endTime.Month.ToString()),
                Helper.XfireCreateKeyValue("end_day",endTime.Day.ToString()),
                Helper.XfireCreateKeyValue("end_hour",endTime.Hour.ToString()),
                Helper.XfireCreateKeyValue("end_minute",endTime.Minute.ToString()),
                Helper.XfireCreateKeyValue("end_second",endTime.Second.ToString())
            };

            return CreateDataTable(requestData);
        }

        /// <summary>
        /// QueryReportData
        /// </summary>
        /// <param name="strIdList"></param>
        /// <param name="bCompress"></param>
        /// <param name="beginTime"></param>
        /// <param name="endTime"></param>
        /// <returns></returns>
        public IDictionary<string, NameValueCollection> QueryTopnMonitorInfo()
        {
            //��֯�������
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","QueryInfo"),
                Helper.XfireCreateKeyValue("needkey","sv_monitortype,sv_name"),
                Helper.XfireCreateKeyValue("needtype","monitor")
            };

            //ͨ��WebService������
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
           
            
            //ͳ���������õļ�������� �� ѡ��������� + ѡ�����ָ��ļ���ʾ ...
            IMonitorTempletDao templetDao = new MonitorTempletDaoImpl();

            IDictionary<string, MonitorTemplet> topnMontiorTemplet = new Dictionary<string, MonitorTemplet>();
            try
            {
                foreach (string key in resultData.Keys)
                {
                    if (resultData[key]["sv_monitortype"] == null)
                        continue;

                    if (!topnMontiorTemplet.ContainsKey(resultData[key]["sv_monitortype"]))
                    {
                        string strTemplateId = resultData[key]["sv_monitortype"];

                        //MonitorTemplet templet = templetDao.FindById(resultData[key]["sv_monitortype"]);
                        //MonitorTemplet[] templet = templetDao.FindById(new string[] { resultData[key]["sv_monitortype"] }, true);
                        MonitorTemplet[] templet = templetDao.FindById(new string[] { resultData[key]["sv_monitortype"] }, false);
                        topnMontiorTemplet.Add(resultData[key]["sv_monitortype"], templet[0]);

                        ////���������(��FindById true ����ʵ��ѡָ��) 
                        //string strTmp = topnMontiorTemplet[resultData[key]["sv_monitortype"]].Name;

                        //foreach (string key1 in topnMontiorTemplet[resultData[key]["sv_monitortype"]].Returns.Keys)
                        //{
                        //    //���ָ��
                        //    strTmp = topnMontiorTemplet[resultData[key]["sv_monitortype"].ToString()].Returns[key1]["sv_label"];
                        //}
                    }
                }
            }
            catch (Exception e)
            {
                ;
            }

            return resultData;
        }

        /// <summary>
        /// QueryTopnDevInfo
        /// </summary>
        /// <param name="strIdList"></param>
        /// <param name="bCompress"></param>
        /// <param name="beginTime"></param>
        /// <param name="endTime"></param>
        /// <returns></returns>
        public IDictionary<string, NameValueCollection> QueryTopnDevInfo()
        {
            //��֯�������
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","QueryInfo"),
                Helper.XfireCreateKeyValue("needkey","sv_name"),
                Helper.XfireCreateKeyValue("needtype","entity")
            };

            //ͨ��WebService������
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
        
        /// <summary>
        /// �������������¼����ת��Excel�ļ�������·�����ͻ��ˡ�
        /// </summary>
        /// <param name="strIdList"></param>
        /// <param name="strDataType"></param>
        /// <param name="beginTime"></param>
        /// <param name="endTime"></param>
        /// <returns></returns>
        public string QueryDataAndMakeExcel(string strIdList, string strDataType, DateTime beginTime, DateTime endTime)
        {
            //��֯�������
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","QueryDataAndMakeExcel"),
                Helper.XfireCreateKeyValue("id",strIdList),
                Helper.XfireCreateKeyValue("datatype",strDataType),
                Helper.XfireCreateKeyValue("begin_year",beginTime.Year.ToString()),
                Helper.XfireCreateKeyValue("begin_month",beginTime.Month.ToString()),
                Helper.XfireCreateKeyValue("begin_day",beginTime.Day.ToString()),
                Helper.XfireCreateKeyValue("begin_hour",beginTime.Hour.ToString()),
                Helper.XfireCreateKeyValue("begin_minute",beginTime.Minute.ToString()),
                Helper.XfireCreateKeyValue("begin_second",beginTime.Second.ToString()),
                Helper.XfireCreateKeyValue("end_year",endTime.Year.ToString()),
                Helper.XfireCreateKeyValue("end_month",endTime.Month.ToString()),
                Helper.XfireCreateKeyValue("end_day",endTime.Day.ToString()),
                Helper.XfireCreateKeyValue("end_hour",endTime.Hour.ToString()),
                Helper.XfireCreateKeyValue("end_minute",endTime.Minute.ToString()),
                Helper.XfireCreateKeyValue("end_second",endTime.Second.ToString())
            };

            return CreateDataTable(requestData).ToString();
        }

        /// <summary>
        /// �������������¼����ת��Excel�ļ�������·�����ͻ��ˡ�
        /// </summary>
        /// <param name="strIdList"></param>
        /// <param name="strDataType"></param>
        /// <param name="beginTime"></param>
        /// <param name="endTime"></param>
        /// <returns></returns>
        public IDictionary<string, NameValueCollection> QueryAllMonitorInfo()
        {
            //��֯�������
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","QueryAllMonitorInfo"),
            };

            //ͨ��WebService������
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

        ///// <summary>
        ///// �������ͳ�Ʊ�
        ///// </summary>
        ///// <param name="strIdList"></param>
        ///// <param name="beginTime"></param>
        ///// <param name="endTime"></param>
        ///// <returns></returns>
        //public DataTable QueryReportRunData(string strIdList, DateTime beginTime, DateTime endTime)
        //{
        //    //��֯�������
        //    anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
        //    {
        //        Helper.XfireCreateKeyValue("dowhat","QueryReportRunData"),
        //        Helper.XfireCreateKeyValue("id",strIdList),
        //        Helper.XfireCreateKeyValue("begin_year",beginTime.Year.ToString()),
        //        Helper.XfireCreateKeyValue("begin_month",beginTime.Month.ToString()),
        //        Helper.XfireCreateKeyValue("begin_day",beginTime.Day.ToString()),
        //        Helper.XfireCreateKeyValue("begin_hour",beginTime.Hour.ToString()),
        //        Helper.XfireCreateKeyValue("begin_minute",beginTime.Minute.ToString()),
        //        Helper.XfireCreateKeyValue("begin_second",beginTime.Second.ToString()),
        //        Helper.XfireCreateKeyValue("end_year",endTime.Year.ToString()),
        //        Helper.XfireCreateKeyValue("end_month",endTime.Month.ToString()),
        //        Helper.XfireCreateKeyValue("end_day",endTime.Day.ToString()),
        //        Helper.XfireCreateKeyValue("end_hour",endTime.Hour.ToString()),
        //        Helper.XfireCreateKeyValue("end_minute",endTime.Minute.ToString()),
        //        Helper.XfireCreateKeyValue("end_second",endTime.Second.ToString())
        //    };
            
        //    return CreateDataTable(requestData);
        //}

        ///// <summary>
        ///// �������ͳ�Ʊ�
        ///// </summary>
        ///// <param name="strIdList"></param>
        ///// <param name="beginTime"></param>
        ///// <param name="endTime"></param>
        ///// <returns></returns>
        //public DataTable QueryReportStatData(string strIdList, string strDataType, DateTime beginTime, DateTime endTime)
        //{
        //    //��֯�������
        //    anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
        //    {
        //        Helper.XfireCreateKeyValue("dowhat","QueryReportStatData"),
        //        Helper.XfireCreateKeyValue("id",strIdList),
        //        Helper.XfireCreateKeyValue("begin_year",beginTime.Year.ToString()),
        //        Helper.XfireCreateKeyValue("begin_month",beginTime.Month.ToString()),
        //        Helper.XfireCreateKeyValue("begin_day",beginTime.Day.ToString()),
        //        Helper.XfireCreateKeyValue("begin_hour",beginTime.Hour.ToString()),
        //        Helper.XfireCreateKeyValue("begin_minute",beginTime.Minute.ToString()),
        //        Helper.XfireCreateKeyValue("begin_second",beginTime.Second.ToString()),
        //        Helper.XfireCreateKeyValue("end_year",endTime.Year.ToString()),
        //        Helper.XfireCreateKeyValue("end_month",endTime.Month.ToString()),
        //        Helper.XfireCreateKeyValue("end_day",endTime.Day.ToString()),
        //        Helper.XfireCreateKeyValue("end_hour",endTime.Hour.ToString()),
        //        Helper.XfireCreateKeyValue("end_minute",endTime.Minute.ToString()),
        //        Helper.XfireCreateKeyValue("end_second",endTime.Second.ToString())
        //    };

        //    return CreateDataTable(requestData);        
        //}

        ///// <summary>
        ///// �����ͼƬ���ݱ�
        ///// </summary>
        ///// <param name="strIdList"></param>
        ///// <param name="strImgType"></param>
        ///// <param name="nCount"></param>
        ///// <param name="beginTime"></param>
        ///// <param name="endTime"></param>
        ///// <returns></returns>
        //public DataTable QueryReportImageData(string strIdList, string strImgType, int nCount, DateTime beginTime, DateTime endTime)
        //{
        //    //��֯�������
        //    anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
        //    {
        //        Helper.XfireCreateKeyValue("dowhat","QueryReportImageData"),
        //        Helper.XfireCreateKeyValue("id",strIdList),
        //        Helper.XfireCreateKeyValue("imgtype",strImgType),
        //        Helper.XfireCreateKeyValue("nReturnCount",nCount.ToString()),
        //        Helper.XfireCreateKeyValue("begin_year",beginTime.Year.ToString()),
        //        Helper.XfireCreateKeyValue("begin_month",beginTime.Month.ToString()),
        //        Helper.XfireCreateKeyValue("begin_day",beginTime.Day.ToString()),
        //        Helper.XfireCreateKeyValue("begin_hour",beginTime.Hour.ToString()),
        //        Helper.XfireCreateKeyValue("begin_minute",beginTime.Minute.ToString()),
        //        Helper.XfireCreateKeyValue("begin_second",beginTime.Second.ToString()),
        //        Helper.XfireCreateKeyValue("end_year",endTime.Year.ToString()),
        //        Helper.XfireCreateKeyValue("end_month",endTime.Month.ToString()),
        //        Helper.XfireCreateKeyValue("end_day",endTime.Day.ToString()),
        //        Helper.XfireCreateKeyValue("end_hour",endTime.Hour.ToString()),
        //        Helper.XfireCreateKeyValue("end_minute",endTime.Minute.ToString()),
        //        Helper.XfireCreateKeyValue("end_second",endTime.Second.ToString())
        //    };

        //    return CreateDataTable(requestData);                
        //}

        ///// <summary>
        ///// ��������ݱ���ҳ��Ϣ��
        ///// </summary>
        ///// <param name="strIdList"></param>
        ///// <param name="strDataType"></param>
        ///// <param name="nCurPage"></param>
        ///// <param name="nTotalPage"></param>
        ///// <param name="beginTime"></param>
        ///// <param name="endTime"></param>
        ///// <returns></returns>
        //public DataTable QueryReportRecordData(string strIdList, string strDataType, string strFieldList, int nCurPage, int nTotalPage, DateTime beginTime, DateTime endTime)
        //{
        //    //�Ӳ�ѯ���� ....

        //    //��֯�������
        //    anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
        //    {
        //        Helper.XfireCreateKeyValue("dowhat","QueryReportRecordData"),
        //        Helper.XfireCreateKeyValue("id",strIdList),
        //        Helper.XfireCreateKeyValue("datatype",strDataType),
        //        Helper.XfireCreateKeyValue("fieldlist",strFieldList), 
        //        Helper.XfireCreateKeyValue("nCurPage",nCurPage.ToString()),
        //        Helper.XfireCreateKeyValue("nTotalPage",nTotalPage.ToString()),
        //        Helper.XfireCreateKeyValue("begin_year",beginTime.Year.ToString()),
        //        Helper.XfireCreateKeyValue("begin_month",beginTime.Month.ToString()),
        //        Helper.XfireCreateKeyValue("begin_day",beginTime.Day.ToString()),
        //        Helper.XfireCreateKeyValue("begin_hour",beginTime.Hour.ToString()),
        //        Helper.XfireCreateKeyValue("begin_minute",beginTime.Minute.ToString()),
        //        Helper.XfireCreateKeyValue("begin_second",beginTime.Second.ToString()),
        //        Helper.XfireCreateKeyValue("end_year",endTime.Year.ToString()),
        //        Helper.XfireCreateKeyValue("end_month",endTime.Month.ToString()),
        //        Helper.XfireCreateKeyValue("end_day",endTime.Day.ToString()),
        //        Helper.XfireCreateKeyValue("end_hour",endTime.Hour.ToString()),
        //        Helper.XfireCreateKeyValue("end_minute",endTime.Minute.ToString()),
        //        Helper.XfireCreateKeyValue("end_second",endTime.Second.ToString())
        //    };

        //    return CreateDataTable(requestData);
        //}

        ///// <summary>
        ///// TopNͼƬ���ݱ�
        ///// </summary>
        ///// <param name="strIdList"></param>
        ///// <param name="strImgType"></param>
        ///// <param name="nCount"></param>
        ///// <param name="beginTime"></param>
        ///// <param name="endTime"></param>
        ///// <returns></returns>
        //public DataTable QueryTopNReportImageData(string strIdList, string strImgType, int nCount, DateTime beginTime, DateTime endTime)
        //{
        //    //��֯�������
        //    anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
        //    {
        //        Helper.XfireCreateKeyValue("dowhat","QueryReportStatData"),
        //        Helper.XfireCreateKeyValue("id",strIdList),
        //        Helper.XfireCreateKeyValue("imgtype",strImgType),
        //        Helper.XfireCreateKeyValue("nReturnCount",nCount.ToString()),
        //        Helper.XfireCreateKeyValue("begin_year",beginTime.Year.ToString()),
        //        Helper.XfireCreateKeyValue("begin_month",beginTime.Month.ToString()),
        //        Helper.XfireCreateKeyValue("begin_day",beginTime.Day.ToString()),
        //        Helper.XfireCreateKeyValue("begin_hour",beginTime.Hour.ToString()),
        //        Helper.XfireCreateKeyValue("begin_minute",beginTime.Minute.ToString()),
        //        Helper.XfireCreateKeyValue("begin_second",beginTime.Second.ToString()),
        //        Helper.XfireCreateKeyValue("end_year",endTime.Year.ToString()),
        //        Helper.XfireCreateKeyValue("end_month",endTime.Month.ToString()),
        //        Helper.XfireCreateKeyValue("end_day",endTime.Day.ToString()),
        //        Helper.XfireCreateKeyValue("end_hour",endTime.Hour.ToString()),
        //        Helper.XfireCreateKeyValue("end_minute",endTime.Minute.ToString()),
        //        Helper.XfireCreateKeyValue("end_second",endTime.Second.ToString())
        //    };

        //    return CreateDataTable(requestData);               
        //}

        #endregion
    }
}
