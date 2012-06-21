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
    public class ReportTableDaoImpl
    {
        
        #region 外部属性

        /// <summary>
        /// 为空则返回所有返回值, 
        /// 传入用,分隔的串 如sv_primary,sv_drawimage 表示 只显示sv_primary=1和sv_drawimage的返回值
        /// </summary>
        public string ReturnFilter
        {
            get { return strFilter; }
            set { strFilter = value; }
        }

        /// <summary>
        /// 名称
        /// </summary>
        public string Name
        {
            get { return name; }
            set { name = value; }
        }

        /// <summary>
        /// 图片数据Hash列表 Key为返回值数据节点的Key 表里的hashKey和Key一样
        /// </summary>
        public IDictionary<string, DataTable> ImgTableHash
        {
            get { return imgTables; }
            set { imgTables = value; }
        }
        /// <summary>
        /// 运行情况统计表
        /// </summary>
        public DataTable RunTable
        {
            get { return this.primaryTable; }
            set { this.primaryTable = value; }
        }
        /// <summary>
        /// 监测情况统计表
        /// </summary>
        public DataTable StatsTable
        {
            get { return this.measureTable; }
            set { this.measureTable = value; }
        }
        /// <summary>
        /// 监测描述表
        /// </summary>
        public DataTable DstrTable
        {
            get { return this.dstrTable; }
            set { this.dstrTable = value; }
        }

        #endregion

        #region 内部成员
        
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
        /// WebService接口
        /// </summary>
        //private ECCWebService service = new ECCWebService();
        private XFireWebService.InterfaceEccService service = Helper.service;        
        /// <summary>
        /// 错误日志
        /// </summary>
        private log4net.ILog logger = log4net.LogManager.GetLogger(typeof(ReportTableDaoImpl));

        #endregion

        #region 内部函数

        /// <summary>
        /// 通过WebService查数据 解密数据 并组织成DataTable
        /// </summary>
        /// <param name="requestData"></param>
        /// <returns></returns>
        private DataTable CreateDataTable(anyType2anyTypeMapEntry[] requestData)
        {
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
            
            //解密数据并组织成DataTable            
            DataTable runTable = new DataTable();
            runTable.Columns.Add("名称");
            runTable.Columns.Add("正常(%)");
            runTable.Columns.Add("危险(%)");
            runTable.Columns.Add("错误(%)");
            runTable.Columns.Add("阀值");
            runTable.Columns.Add("最新状态");
            runTable.Columns.Add("最近一次描述");

            //怎么表示hashKey最好呢? ...
            DataTable statTable = new DataTable();
            statTable.Columns.Add("hashKey");
            statTable.Columns.Add("名称");
            statTable.Columns.Add("返回值名称");
            statTable.Columns.Add("最大值");
            statTable.Columns.Add("最小值");
            statTable.Columns.Add("平均值");
            statTable.Columns.Add("最近一次");
            statTable.Columns.Add("最大值时间");


            DataTable m_dstrTable = new DataTable();
            m_dstrTable.Columns.Add("名称");
            m_dstrTable.Columns.Add("时间");
            m_dstrTable.Columns.Add("状态");
            m_dstrTable.Columns.Add("返回值");
            
            string[] strFilters = strFilter.Split(',');

            DataRow dr = null;
            IDictionary<string, DataTable> hashImgTable = new Dictionary<string, DataTable>();           

            IDictionary<string, NameValueCollection> resultData = Helper.XfireConventMapEntryToDictionary(result.vmap);
            foreach (string key in resultData.Keys)
            {   
                if (key.StartsWith("(Return"))
                {
                    //怎么组织统计报告 由 dstr组成 的表形式呢? ... --> StatReport专用

                    //怎么根据返回值的sv_primary等做过滤呢? ...--> 后台把这些属性返回来　暂时只做值为 1 + and 关系
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
                        //strFilter 为空则所有返回值都可以
                    }

                    if (bFilter)
                        continue;

                    //监测统计表

                    string strImageData = resultData[key]["detail"].ToString();
                    if (strImageData == "")
                        continue;

                    
                    dr = statTable.NewRow();
                    //名称  返回值名称  最大  平均  最近一次 
                    //latest max min MonitorName ReturnName average
                    //dr["monitorid"] = key;
                    dr["hashKey"] = key; //hashKey 在前台用控件屏蔽不显示即可
                    dr["名称"] = resultData[key]["MonitorName"].ToString();
                    dr["返回值名称"] = resultData[key]["ReturnName"].ToString();
                    dr["最大值"] = resultData[key]["max"].ToString();
                    dr["最小值"] = resultData[key]["min"].ToString();
                    dr["平均值"] = resultData[key]["average"].ToString();
                    dr["最近一次"] = resultData[key]["latest"].ToString();
                    dr["最大值时间"] = resultData[key]["when_max"].ToString();
                    
                    statTable.Rows.Add(dr);

                    //图片数据表
                    //monitorid -> value = ReturnValue -> key detail time=value
                    string []imgDatas = strImageData.Split(',');

                    hashImgTable.Add(key, new DataTable());
                    //hashImgTable[key] = new DataTable();
                    DataColumn column = null;
                    column = new DataColumn();
                    column.DataType = System.Type.GetType("System.DateTime");
                    column.ColumnName = "时间";
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
                            //返回值是字符串怎么办呢? ...--> 后台已经过滤了(字符串型返回值没有返回来的)

                            dr = hashImgTable[key].NewRow();
                            dr["时间"] = strTmps[0];

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
                    //dstr 表
                    //monitor名称 + 时间 + dstr
                    string strName = resultData[key]["MonitorName"];
                    foreach (string strKey in resultData[key].AllKeys)
                    {
                        if (strKey != "MonitorName")
                        {
                            dr = m_dstrTable.NewRow();
                            dr["名称"] = strName;
                            dr["时间"] = strKey;
                            dr["状态"] = resultData[key][strKey].ToString().Substring(0, 8).Trim();
                            dr["返回值"] = resultData[key][strKey].ToString().Substring(7).Trim();
                            m_dstrTable.Rows.Add(dr);
                        }
                    }
                }
                else
                {
                    //
                    if (key.Equals("return"))
                        continue;

                    //运行情况表
                    //名称  正常(%)  危险(%)  错误(%)  阀值 
                    //monitorid -> MonitorName errorCondition errorPercent latestStatus okPercent warnPercent 
                    dr = runTable.NewRow();
                    //dr["monitorid"] = key;
                    dr["名称"] = resultData[key]["MonitorName"].ToString();
                    dr["正常(%)"] = resultData[key]["okPercent"].ToString();
                    dr["危险(%)"] = resultData[key]["warnPercent"].ToString();
                    dr["错误(%)"] = resultData[key]["errorPercent"].ToString();
                    dr["阀值"] = resultData[key]["errorCondition"].ToString();                    
                    dr["最新状态"] = resultData[key]["latestStatus"].ToString();
                    dr["最近一次描述"] = resultData[key]["latestDstr"].ToString();                                        
                    
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

        #region 外部接口

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
            //组织输入参数
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
            //组织输入参数
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
            //组织输入参数
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","QueryInfo"),
                Helper.XfireCreateKeyValue("needkey","sv_monitortype,sv_name"),
                Helper.XfireCreateKeyValue("needtype","monitor")
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
           
            
            //统计所有已用的监测器类型 及 选监测器类型 + 选监测器指标的简单演示 ...
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

                        ////监测器类型(用FindById true 即可实现选指标) 
                        //string strTmp = topnMontiorTemplet[resultData[key]["sv_monitortype"]].Name;

                        //foreach (string key1 in topnMontiorTemplet[resultData[key]["sv_monitortype"]].Returns.Keys)
                        //{
                        //    //监测指标
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
            //组织输入参数
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","QueryInfo"),
                Helper.XfireCreateKeyValue("needkey","sv_name"),
                Helper.XfireCreateKeyValue("needtype","entity")
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
        
        /// <summary>
        /// 请求服务器将记录数据转成Excel文件并返回路径给客户端。
        /// </summary>
        /// <param name="strIdList"></param>
        /// <param name="strDataType"></param>
        /// <param name="beginTime"></param>
        /// <param name="endTime"></param>
        /// <returns></returns>
        public string QueryDataAndMakeExcel(string strIdList, string strDataType, DateTime beginTime, DateTime endTime)
        {
            //组织输入参数
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
        /// 请求服务器将记录数据转成Excel文件并返回路径给客户端。
        /// </summary>
        /// <param name="strIdList"></param>
        /// <param name="strDataType"></param>
        /// <param name="beginTime"></param>
        /// <param name="endTime"></param>
        /// <returns></returns>
        public IDictionary<string, NameValueCollection> QueryAllMonitorInfo()
        {
            //组织输入参数
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","QueryAllMonitorInfo"),
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

        ///// <summary>
        ///// 运行情况统计表
        ///// </summary>
        ///// <param name="strIdList"></param>
        ///// <param name="beginTime"></param>
        ///// <param name="endTime"></param>
        ///// <returns></returns>
        //public DataTable QueryReportRunData(string strIdList, DateTime beginTime, DateTime endTime)
        //{
        //    //组织输入参数
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
        ///// 监测数据统计表
        ///// </summary>
        ///// <param name="strIdList"></param>
        ///// <param name="beginTime"></param>
        ///// <param name="endTime"></param>
        ///// <returns></returns>
        //public DataTable QueryReportStatData(string strIdList, string strDataType, DateTime beginTime, DateTime endTime)
        //{
        //    //组织输入参数
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
        ///// 监测器图片数据表
        ///// </summary>
        ///// <param name="strIdList"></param>
        ///// <param name="strImgType"></param>
        ///// <param name="nCount"></param>
        ///// <param name="beginTime"></param>
        ///// <param name="endTime"></param>
        ///// <returns></returns>
        //public DataTable QueryReportImageData(string strIdList, string strImgType, int nCount, DateTime beginTime, DateTime endTime)
        //{
        //    //组织输入参数
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
        ///// 监测器数据表（带页信息）
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
        //    //加查询条件 ....

        //    //组织输入参数
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
        ///// TopN图片数据表
        ///// </summary>
        ///// <param name="strIdList"></param>
        ///// <param name="strImgType"></param>
        ///// <param name="nCount"></param>
        ///// <param name="beginTime"></param>
        ///// <param name="endTime"></param>
        ///// <returns></returns>
        //public DataTable QueryTopNReportImageData(string strIdList, string strImgType, int nCount, DateTime beginTime, DateTime endTime)
        //{
        //    //组织输入参数
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
