#region usings

using System;
using System.Xml;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Text;
using System.Windows.Forms;

using Velocity = NVelocity.App.Velocity;
using VelocityContext = NVelocity.VelocityContext;
using Template = NVelocity.Template;
using ParseErrorException = NVelocity.Exception.ParseErrorException;
using ResourceNotFoundException = NVelocity.Exception.ResourceNotFoundException;
using System.Collections;
using System.IO;

using Infragistics.UltraChart.Shared.Styles;


using NVelocity.Runtime;
using System.Collections.Specialized;

using ICSharpCode.SharpZipLib.Checksums;
using ICSharpCode.SharpZipLib.Zip;
using ICSharpCode.SharpZipLib.GZip;
using System.Net.Mail;

using SiteView.Ecc.Core;
using SiteView.Ecc.Core.Dao;
using SiteView.Ecc.Core.Models;

using SiteView.Ecc.WSClient;
//using SiteView.PluginFramework;

#endregion

namespace ReportFormServer
{
    public partial class ReportToHTMLControl : UserControl
    {
        /// <summary>
        /// 
        /// </summary>
        public ArrayList lstDirectories = new ArrayList();

        /// <summary>
        /// 
        /// </summary>
        public string strSmtpServerInfo, strFromInfo, strFromUserInfo, strFromPwdInfo, strToInfo;

        /// <summary>
        /// 
        /// </summary>
        IIniFileDao iniFileDao = null;
        IDictionary<string, NameValueCollection> file = null;
        CryptDaoImpl crypt = null;
        ReportTableDaoImpl impl = null;                
        public string strContext = "";

        private string monitorId;
        private string EntityName;

        //private Monitor monitor;
        //private MonitorTemplet templet;
        //private DataTable source;
        //private DataTable imgsource;
        //private System.Threading.Thread autoReloadThread;

        //private IMonitorDao monitorDao;
        //private IMonitorTempletDao monitorTempletDao;
        //private ISvdbTableDao svdbDao;

        private IDictionary<string, DataTable> imgTables;

        public string strReportIndex = "";

        DataTable QueryReportData;
        DataTable StatsTable;
        DataTable Imgsource;
        DataTable dstrTable;
        private string BasePath = AppDomain.CurrentDomain.BaseDirectory;
        private string DatePart = "";//时间段（日、周、月）

        bool bSendMail = false;
        /// <summary>
        /// 
        /// </summary>
        /// <param name="monitorId"></param>
        /// <param name="entityName"></param>
        public ReportToHTMLControl(string monitorId, string entityName)
        {
            QueryReportData = new DataTable();
            QueryReportData.Columns.Add("名称");//RunName
            QueryReportData.Columns.Add("正常(%)");//RunRight
            QueryReportData.Columns.Add("危险(%)");//Rundanger
            QueryReportData.Columns.Add("错误(%)");//RunWrong
            QueryReportData.Columns.Add("阀值");//RunPass
            QueryReportData.Columns.Add("最新状态");//RunLatest
            QueryReportData.Columns.Add("最近一次描述");

            StatsTable = new DataTable();
            StatsTable.Columns.Add("hashKey");
            StatsTable.Columns.Add("名称");//StatName
            StatsTable.Columns.Add("返回值名称");//StatReturnName
            StatsTable.Columns.Add("最大值");//StatMax
            StatsTable.Columns.Add("最小值");//StatMin
            StatsTable.Columns.Add("平均值", typeof(double));//StatAverage
            StatsTable.Columns.Add("最近一次");//StatLatest
            StatsTable.Columns.Add("最大值时间");

            dstrTable = new DataTable();
            dstrTable.Columns.Add("名称");
            dstrTable.Columns.Add("时间");
            dstrTable.Columns.Add("状态");
            dstrTable.Columns.Add("返回值");


            this.monitorId = monitorId;

            //this.monitorDao = MicroKernelUtil.GetDefaultKernel()[typeof(IMonitorDao)] as IMonitorDao;
            //this.monitorTempletDao = MicroKernelUtil.GetDefaultKernel()[typeof(IMonitorTempletDao)] as IMonitorTempletDao;
            //this.svdbDao = MicroKernelUtil.GetDefaultKernel()[typeof(ISvdbTableDao)] as ISvdbTableDao;
            this.EntityName = entityName;
            
            //iniFileDao = MicroKernelUtil.GetDefaultKernel()["IniDao"] as IIniFileDao;
            iniFileDao = new IniFileDaoImpl();
            crypt = new CryptDaoImpl();
            impl = new ReportTableDaoImpl();

            //UserPermissionContext.Instance.Url = "http://218.249.196.87:18080/webservice/InterfaceEccService";
            bSendMail = false;

            //从配置文件读取Ws Url ...
            if (File.Exists(BasePath + "\\ReportTemplate\\" + "ReportServerCfg.Xml"))
            {
                XmlDocument xmldoc = new XmlDocument();
                xmldoc.Load(BasePath + "\\ReportTemplate\\" + "ReportServerCfg.Xml");
                UserPermissionContext.Instance.Url = xmldoc.DocumentElement.SelectSingleNode("/configurations/WsUrl").InnerText;
                
                //if (xmldoc.DocumentElement.SelectSingleNode("/configurations/IsSendMail").InnerText.Equals("yes"))
                {
                    bSendMail = true;
                }
            }

            InitializeComponent();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="templateFile"></param>
        /// <param name="IsTopN"></param>
        public void WriteAllReportHTML(string templateFile, bool IsTopN)
        {
            try
            {
                bool bGen = false;
                bool bReportGen = true;

                string name = "";
                string strTmp = "";
                string strGenSection = "";
                string strGenKey = "";
                string strGenValue = "";
                string strGroupRight = "";
                string strReportName = "";
                string strBaseDirectory = "";
                string strZipFileName = "";

                DateTime tmEnd = DateTime.Now;
                DateTime tmStart = DateTime.Now;

                if (IsTopN)
                {
                    file = iniFileDao.GetIniFile("topnreportset.ini");
                    StatsTable = new DataTable();
                    StatsTable.Columns.Add("hashKey");
                    StatsTable.Columns.Add("名称");//StatName
                    StatsTable.Columns.Add("返回值名称");//StatReturnName
                    StatsTable.Columns.Add("最大值");//StatMax
                    StatsTable.Columns.Add("最小值");//StatMin
                    StatsTable.Columns.Add("平均值", typeof(double));//StatAverage
                    StatsTable.Columns.Add("最近一次");//StatLatest
                    StatsTable.Columns.Add("最大值时间");
                }
                else
                {
                    file = iniFileDao.GetIniFile("reportset.ini");
                }

                //根据用户报告配置 生成所有报告
                foreach (string key in file.Keys)
                {
                    strGroupRight = "";
                    strReportName = "";
                    bGen = false;
                    bReportGen = false;

                    string strReportFile = "";
                    
                    //是否到生成时间                    
                    string strGenerate = file[key]["Generate"];
                    
                    if (strGenerate == "" || strGenerate == null)
                        continue;
                    
                    int nHours = int.Parse(strGenerate);
                    if (nHours == DateTime.Now.Hour)
                    {
                        bGen = true;
                    }

                    //被禁止 则不生成 执行下一个报告
                    if (file[key]["Deny"] == "No")
                    {
                        strGroupRight = file[key]["GroupRight"];

                        if (strGroupRight == "" || strGroupRight == null)
                            continue;
                        
                        //string[] monitorList = strGroupRight.Split(',');

                        //未到生成时间 则不生成 执行下一个报告
                        if (bGen)
                        {
                            //获取报表生成时间段
                            if (file[key]["Period"] == "日报")
                            {
                                string szEndTime = file[key]["EndTime"];
                                string szStartTime = file[key]["StartTime"];
                                //szEndTime = "12:30";

                                string szHour = "00";
                                string szMinute = "00";

                                if (IsTopN)
                                {
                                    szEndTime = "00";
                                    szStartTime = "00";
                                }
                                else
                                {
                                    if (szEndTime == "" || szEndTime == null)
                                        continue;
                                    if (szStartTime == "" || szStartTime == null)
                                        continue;

                                    int pos = szEndTime.IndexOf(":", 0);
                                    if (pos <= 0)
                                        continue;

                                    szHour = szEndTime.Substring(0, pos);
                                    szMinute = szEndTime.Substring(pos + 1, szEndTime.Length - pos - 1);
                                }

                                tmEnd = new DateTime(DateTime.Now.Year, DateTime.Now.Month, DateTime.Now.Day, int.Parse(szHour), int.Parse(szMinute), 0);
                                //tmEnd = new DateTime(DateTime.Now.Year, DateTime.Now.Month, DateTime.Now.Day, int.Parse(szStartTime), int.Parse(szEndTime), 0);

                                tmStart = tmEnd.Subtract(new TimeSpan(1, 0, 0, 0, 0));
                                bReportGen = true;
                                this.DatePart = "day";
                            }
                            else if (file[key]["Period"] == "周报")
                            {
                                if (file[key]["WeekEndTime"] == "" || file[key]["WeekEndTime"] == null)
                                    continue;

                                int nWeekDay;
                                nWeekDay = int.Parse(file[key]["WeekEndTime"]);
                                if ((int)(DateTime.Now.DayOfWeek) == nWeekDay)
                                {
                                    tmEnd = new DateTime(DateTime.Now.Year, DateTime.Now.Month, DateTime.Now.Day, 0, 0, 0);
                                    tmStart = tmEnd.Subtract(new TimeSpan(7, 0, 0, 0, 0));
                                    bReportGen = true;
                                }
                                this.DatePart = "week";
                            }
                            else if (file[key]["Period"] == "月报")
                            {
                                if ((int)DateTime.Now.Day == 1)
                                {
                                    if (DateTime.Now.Month == 1)
                                    {
                                        tmStart = new DateTime(DateTime.Now.Year - 1, 12, 1, 0, 0, 0);
                                        tmEnd = new DateTime(DateTime.Now.Year, DateTime.Now.Month, 1, 0, 0, 0);
                                    }
                                    else
                                    {
                                        tmStart = new DateTime(DateTime.Now.Year, DateTime.Now.Month - 1, 1, 0, 0, 0);
                                        tmEnd = new DateTime(DateTime.Now.Year, DateTime.Now.Month, 1, 0, 0, 0);
                                    }

                                    bReportGen = true;
                                }
                                this.DatePart = "month";
                            }
                            else
                            {
                                
                            }
                        }
                        else
                        {
                            continue;
                        }
                    }
                    else
                    {
                        continue;
                    }

                    //该 生成此报告 了
                    if (bReportGen)
                    {
                        if (IsTopN)
                        {
                            strReportName = tmStart.ToString() + tmEnd.ToString() + key + "TopN";
                        }
                        else
                        {
                            strReportName = tmStart.ToString() + tmEnd.ToString() + key;
                        }

                        //查询报告所需数据
                        int index = 200;//一次查询在200个点以内
                        if (IsTopN)
                        {
                            IDictionary<string, NameValueCollection> monitoidToType = impl.QueryTopnMonitorInfo();
                            IDictionary<string, MonitorTemplet> topnMontiorTemplet = new Dictionary<string, MonitorTemplet>();
                            IMonitorTempletDao templetDao = new MonitorTempletDaoImpl();
                            foreach (string key1 in monitoidToType.Keys)
                            {
                                if (monitoidToType[key1]["sv_monitortype"] == null)
                                    continue;
                                if (!topnMontiorTemplet.ContainsKey(monitoidToType[key1]["sv_monitortype"]))
                                {
                                    MonitorTemplet[] templet = templetDao.FindById(new string[] { monitoidToType[key1]["sv_monitortype"] }, true);
                                    topnMontiorTemplet.Add(monitoidToType[key1]["sv_monitortype"], templet[0]);
                                }
                            }

                            string strSelMonitorType = file[key]["Type"];//用户选择的监测器类型 
                            string strMark = file[key]["Mark"];
                            int strCount = int.Parse(file[key]["Count"]);
                            string strSort = file[key]["Sort"];
                            if (strSelMonitorType == "" || strSelMonitorType == null)
                                continue;

                            if (strMark == "" || strMark == null)
                                continue;

                            strTmp = "";
                            foreach (string strMonitorId in strGroupRight.Split(','))
                            {
                                if (strMonitorId != "" && monitoidToType.Keys.Contains(strMonitorId))
                                {
                                    if (topnMontiorTemplet[monitoidToType[strMonitorId]["sv_monitortype"]].Name == strSelMonitorType)
                                    {
                                        strTmp += strMonitorId;
                                        strTmp += ",";
                                    }
                                }
                            }


                            //拆分监测器ID,进行分批查询。
                            int splitLength = strTmp.Split(',').Length;
                            this.StatsTable.Rows.Clear();
                            this.QueryReportData.Rows.Clear();
                            if (splitLength > index)
                            {
                                int sect = splitLength / index;
                                DataTable StatsTableTimp = StatsTable.Clone();
                                string[] strArray = strTmp.Split(',');
                                for (int split = 0; split < sect; split++)
                                {
                                    DataTable QueryReportDataTemp = QueryReportData.Clone();
                                    string splitString = "";
                                    int _splitCount = split * index;
                                    for (int splitCount = _splitCount; splitCount < (split + 1) * index; splitCount++)
                                    {
                                        splitString += strArray[splitCount];
                                        splitString += ",";
                                    }
                                    QueryReportDataTemp = impl.QueryReportData(splitString, true, false, tmStart, tmEnd);
                                    foreach (DataRow QrdrTemp in QueryReportDataTemp.Rows)
                                    {
                                        QueryReportData.ImportRow(QrdrTemp);
                                    }
                                    foreach (DataRow StdrTemp in impl.StatsTable.Rows)
                                    {
                                        StatsTableTimp.ImportRow(StdrTemp);
                                    }
                                }

                                string splitStringLast = "";
                                int _splitCountLast = sect * index;
                                for (int splitCount = _splitCountLast; splitCount < splitLength; splitCount++)
                                {
                                    splitStringLast += strArray[splitCount];
                                    splitStringLast += ",";
                                }
                                DataTable QueryReportDataTempLast = impl.QueryReportData(splitStringLast, true, false, tmStart, tmEnd);
                                foreach (DataRow QrdrTemp in QueryReportDataTempLast.Rows)
                                {
                                    QueryReportData.ImportRow(QrdrTemp);
                                }
                                foreach (DataRow StdrTemp in impl.StatsTable.Rows)
                                {
                                    StatsTableTimp.ImportRow(StdrTemp);
                                }


                                ChangeColumnType(StatsTableTimp);
                            }
                            else
                            {
                                QueryReportData = impl.QueryReportData(strTmp, true, false, tmStart, tmEnd);
                                ChangeColumnType(impl.StatsTable);
                            }

                            //QueryReportData = impl.QueryReportData(strTmp, true, false, tmStart, tmEnd);
                            //this.StatsTable.Rows.Clear();
                            //for (int a = 0; a < impl.StatsTable.Rows.Count; a++)
                            //{
                            //    DataRow dr = this.StatsTable.NewRow();
                            //    for (int b = 0; b < impl.StatsTable.Columns.Count; b++)
                            //    {
                            //        if (b == 5)
                            //        {
                            //            dr[b] = double.Parse(impl.StatsTable.Rows[a][b].ToString());
                            //        }
                            //        else
                            //        {
                            //            dr[b] = impl.StatsTable.Rows[a][b].ToString();
                            //        }
                            //    }
                            //    this.StatsTable.Rows.Add(dr);
                            //}
                            //DataView dv = this.StatsTable.DefaultView;
                            DataView dv = new DataView(this.StatsTable);
                            dv.Sort = "平均值 desc";
                            //测试代码
                            //***********************************************
                            if (key == "AqcTestSNMP Cpu")
                            {
                                this.DataTableToExcel(dv.ToTable(), "c:\\" + key + DateTime.Now.ToShortDateString() + ".csv");
                                this.DataTableToExcel(this.StatsTable, "c:\\" + key + "stat" + DateTime.Now.ToShortDateString() + ".csv");
                            }
                            //*************************************************
                            selectDataTable(dv.ToTable(), strMark, strCount, strSort);
                        }
                        else
                        {
                            impl.ReturnFilter = "sv_primary,sv_drawimage";
                            int splitLength = strGroupRight.Split(',').Length;
                            this.StatsTable.Rows.Clear();
                            this.QueryReportData.Rows.Clear();
                            this.dstrTable.Rows.Clear();
                            if (file[key]["Parameter"].Equals("Yes"))
                            {
                                //拆分监测器ID,进行分批查询。
                                if (splitLength > index)
                                {
                                    int sect = splitLength / index;
                                    DataTable StatsTableTimp = StatsTable.Clone();
                                    string[] strArray = strGroupRight.Split(',');
                                    imgTables = new Dictionary<string, DataTable>();
                                    for (int split = 0; split < sect; split++)
                                    {
                                        DataTable QueryReportDataTemp = QueryReportData.Clone();
                                        string splitString = "";
                                        int _splitCount = split * index;
                                        for (int strCount = _splitCount; strCount < (split + 1) * index; strCount++)
                                        {
                                            splitString += strArray[strCount];
                                            splitString += ",";
                                        }
                                        QueryReportDataTemp = impl.QueryReportData(splitString, true, true, tmStart, tmEnd);
                                        foreach (DataRow QrdrTemp in QueryReportDataTemp.Rows)
                                        {
                                            QueryReportData.ImportRow(QrdrTemp);
                                        }
                                        foreach (DataRow StdrTemp in impl.StatsTable.Rows)
                                        {
                                            StatsTable.ImportRow(StdrTemp);
                                        }
                                        foreach (KeyValuePair<string, DataTable> Kv in impl.ImgTableHash)
                                        {
                                            if (!imgTables.ContainsKey(Kv.Key))
                                            {
                                                imgTables.Add(Kv);
                                            }
                                        }
                                        foreach (DataRow dr in impl.DstrTable.Rows)
                                        {
                                            this.dstrTable.ImportRow(dr);
                                        }
                                    }

                                    string splitStringLast = "";
                                    int _splitCountLast = sect * index;
                                    for (int strCount = _splitCountLast; strCount < splitLength; strCount++)
                                    {
                                        splitStringLast += strArray[strCount];
                                        splitStringLast += ",";
                                    }
                                    DataTable QueryReportDataTempLast = impl.QueryReportData(splitStringLast, true, true, tmStart, tmEnd);
                                    foreach (DataRow QrdrTemp in QueryReportDataTempLast.Rows)
                                    {
                                        QueryReportData.ImportRow(QrdrTemp);
                                    }
                                    foreach (DataRow StdrTemp in impl.StatsTable.Rows)
                                    {
                                        StatsTable.ImportRow(StdrTemp);
                                    }
                                    foreach (KeyValuePair<string, DataTable> Kv in impl.ImgTableHash)
                                    {
                                        if (!imgTables.ContainsKey(Kv.Key))
                                        {
                                            imgTables.Add(Kv);
                                        }
                                    }
                                    foreach (DataRow dr in impl.DstrTable.Rows)
                                    {
                                        this.dstrTable.ImportRow(dr);
                                    }
                                    //imgTables = impl.ImgTableHash;
                                }
                                else
                                {
                                    QueryReportData = impl.QueryReportData(strGroupRight, true, true, tmStart, tmEnd);
                                    StatsTable = impl.StatsTable;
                                    imgTables = impl.ImgTableHash;
                                }
                            }
                            else
                            {
                                if (splitLength > index)
                                {
                                    int sect = splitLength / index;
                                    DataTable StatsTableTimp = StatsTable.Clone();
                                    string[] strArray = strGroupRight.Split(',');
                                    imgTables = new Dictionary<string, DataTable>();
                                    for (int split = 0; split < sect; split++)
                                    {
                                        DataTable QueryReportDataTemp = QueryReportData.Clone();
                                        string splitString = "";
                                        int _splitCount = split * index;
                                        for (int strCount = _splitCount; strCount < (split + 1) * index; strCount++)
                                        {
                                            splitString += strArray[strCount];
                                            splitString += ",";
                                        }
                                        QueryReportDataTemp = impl.QueryReportData(splitString, true, false, tmStart, tmEnd);
                                        foreach (DataRow QrdrTemp in QueryReportDataTemp.Rows)
                                        {
                                            QueryReportData.ImportRow(QrdrTemp);
                                        }
                                        foreach (DataRow StdrTemp in impl.StatsTable.Rows)
                                        {
                                            StatsTable.ImportRow(StdrTemp);
                                        }
                                        foreach (KeyValuePair<string, DataTable> Kv in impl.ImgTableHash)
                                        {
                                            if (!imgTables.ContainsKey(Kv.Key))
                                            {
                                                imgTables.Add(Kv);
                                            }
                                        }
                                    }

                                    string splitStringLast = "";
                                    int _splitCountLast = sect * index;
                                    for (int strCount = _splitCountLast; strCount < splitLength; strCount++)
                                    {
                                        splitStringLast += strArray[strCount];
                                        splitStringLast += ",";
                                    }
                                    DataTable QueryReportDataTempLast = impl.QueryReportData(splitStringLast, true, false, tmStart, tmEnd);
                                    foreach (DataRow QrdrTemp in QueryReportDataTempLast.Rows)
                                    {
                                        QueryReportData.ImportRow(QrdrTemp);
                                    }
                                    foreach (DataRow StdrTemp in impl.StatsTable.Rows)
                                    {
                                        StatsTable.ImportRow(StdrTemp);
                                    }
                                    foreach (KeyValuePair<string, DataTable> Kv in impl.ImgTableHash)
                                    {
                                        if (!imgTables.ContainsKey(Kv.Key))
                                        {
                                            imgTables.Add(Kv);
                                        }
                                    }
                                    //imgTables = impl.ImgTableHash;
                                }
                                else
                                {
                                    QueryReportData = impl.QueryReportData(strGroupRight, true, false, tmStart, tmEnd);
                                    StatsTable = impl.StatsTable;
                                    imgTables = impl.ImgTableHash;
                                }
                            }


                            //if (file[key]["Parameter"].Equals("Yes"))
                            //{
                            //    QueryReportData = impl.QueryReportData(strGroupRight, true, true, tmStart, tmEnd);
                            //}
                            //else
                            //{
                            //    QueryReportData = impl.QueryReportData(strGroupRight, true, false, tmStart, tmEnd);
                            //}
                            //StatsTable = impl.StatsTable;
                            //imgTables = impl.ImgTableHash;
                        }
                        
                        //生成报告图片文件
                        Savepictures(key, tmStart.ToString(), tmEnd.ToString(), IsTopN);

                        //创建模板并绑定变量
                        WriteLogFile("nvelocity.properties");
                        CreateVM(templateFile, IsTopN,key);
                        Velocity.SetProperty(RuntimeConstants.INPUT_ENCODING, "GB2312");
                        Velocity.SetProperty(RuntimeConstants.OUTPUT_ENCODING, "GB2312");
                        Velocity.Init(BasePath + "\\nvelocity.properties");
                        
                        //...
                        
                        //是否列出阀值

                        //是否列出错误

                        //是否列出危险

                        //是否列出禁止

                        VelocityContext context = new VelocityContext();
                        //context.Put("RunName", RunName);
                        //context.Put("RunRight", RunRight);
                        //context.Put("Rundanger", Rundanger);
                        //context.Put("RunWrong", RunWrong);
                        //context.Put("RunPass", RunPass);
                        //context.Put("RunLatest", RunLatest);

                        //context.Put("StatName", StatName);
                        //context.Put("StatReturnName", StatReturnName);
                        //context.Put("StatMax", StatMax);
                        //context.Put("StatMin", StatMin);
                        //context.Put("StatAverage", StatAverage);
                        //context.Put("StatLatest", StatLatest);
                        if (IsTopN)
                        {
                            context.Put("MakeTitleTable", MakeTitle(file[key]["Title"].ToString().Split('|')[0], tmStart.ToString(), tmEnd.ToString()));                            
                            context.Put("MakeTopNhtmltable", MakeTopNhtmltable());
                            context.Put("MakeTopNhtmlPicture", MakeTopNhtmlPicture(key, tmStart.ToString(), tmEnd.ToString()));
                        }
                        else
                        {
                            //列出详细数据
                            bool IsStatusResultVisible = file[key]["Parameter"].Equals("Yes");
                            //是否列出阀值
                            bool IsListClicketVisible = file[key]["ListClicket"].Equals("Yes");
                            //是否列出错误
                            bool IsListErrorVisible = file[key]["ListError"].Equals("Yes");
                            //是否列出危险
                            bool IsListDangerVisible = file[key]["ListDanger"].Equals("Yes");
                            //是否列出禁止 
                            bool IsDenyVisible = file[key]["Deny"].Equals("Yes");
                            //是否列出图片
                            bool IsGraphic = file[key]["Graphic"].Equals("Yes");
                            DataTable tb = new DataTable();
                            DataColumn c1 = new DataColumn("名称");
                            DataColumn c2 = new DataColumn("时间");
                            DataColumn c3 = new DataColumn("状态");
                            DataColumn c4 = new DataColumn("返回值");
                            DataColumn[] c5 = { c1, c2, c3, c4 };
                            tb.Columns.AddRange(c5);
                            if (file[key]["ListError"].Equals("Yes"))
                            {
                                tb.Rows.Clear();
                                foreach (DataRow dr in impl.DstrTable.Select("状态='Error'"))
                                {
                                    tb.Rows.Add(dr.ItemArray);
                                }
                                context.Put("MakeErrorDataStrTable", MakeDataStrTable(tb));
                            }
                            if (file[key]["ListDanger"].Equals("Yes"))
                            {
                                tb.Rows.Clear();
                                foreach (DataRow dr in impl.DstrTable.Select("状态='warning'"))
                                {
                                    tb.Rows.Add(dr.ItemArray);
                                }
                                context.Put("MakeDangerDataStrTable", MakeDataStrTable(tb));
                            }

                            context.Put("MakeTitleTable", MakeTitle(file[key]["Title"].ToString().Split('|')[0], tmStart.ToString(), tmEnd.ToString()));
                            //context.Put("RunQueryReportData", RunQueryReportData);
                            context.Put("MakeRunHTMLTable", MakeRunHTMLTable());
                            context.Put("MakeStatehtmltable", MakeStatehtmltable(key, tmStart.ToString(), tmEnd.ToString()));
                            context.Put("MakeStatehtmlPicture", MakeStatehtmlPicture(key, tmStart.ToString(), tmEnd.ToString()));
                            if (file[key]["Parameter"].Equals("Yes"))
                            {
                                context.Put("MakeDataStrTable", MakeDataStrTable(this.dstrTable));
                            }
                            else
                            {
                                context.Put("MakeDataStrTable", MakeDataStrTable(impl.DstrTable));
                            }

                            context.Put("IsStatusResultVisible", IsStatusResultVisible);//列出状态总结
                            context.Put("IsListClicketVisible", IsListClicketVisible);//是否列出阀值
                            context.Put("IsListErrorVisible", IsListErrorVisible);//是否列出错误
                            context.Put("IsListDangerVisible", IsListDangerVisible);//是否列出危险
                            context.Put("IsDenyVisible", IsDenyVisible); //是否列出禁止 
                            context.Put("IsGraphic", IsGraphic);//是否列出图片
                        }

                        //生成报告 html
                        Template template = null;
                        try
                        {
                            //获取模板
                            //放在哪个目录下面？
                            template = Velocity.GetTemplate("\\ReportTemplate\\" + templateFile);
                            //输出HTML
                            if (template != null)
                            {
                                strTmp = strReportName.Replace(" ", "_").Replace(":", "_").Replace("*", "_").Replace("/", "_").
                                      Replace("\\", "_").Replace("?", "_").Replace("|", "_").Replace("<", "_").Replace(">", "_").Replace("\"", "_");
                                strReportName = strTmp;
                                StringWriter writer = new StringWriter();
                                template.Merge(context, writer);
                                string p =  BasePath + "ReportTemplate\\" + DatePart + "\\" + strReportName;


                                if (!Directory.Exists(p))
                                {
                                    Directory.CreateDirectory(p);
                                }

                                strReportFile = p + "\\" + strReportName + ".html";


                                StreamWriter s = new StreamWriter(strReportFile, false, System.Text.Encoding.UTF8);
                                s.WriteLine(writer.ToString());
                                s.Flush();
                                s.Close();
                            }
                        }
                        catch (ResourceNotFoundException xxx)
                        {
                            MessageBox.Show(xxx.Message + "请添加模板文件到提示的目录");
                        }
                        catch (ParseErrorException pee)
                        {
                            MessageBox.Show(pee.Message);
                        }

                        //写信息到 reportgenerate.ini                        
                        strGenKey = "";
                        strGenValue = "";
                        strTmp = "";

                        //section节格式：报告名$开始时间$截止时间$                         
                        strGenSection = file[key]["Title"].ToString().Split('|')[1] + "$" + tmStart.ToString() + "$" + tmEnd.ToString() + "$";
                        if (!IsTopN)
                        {
                            for (int i = 0; i < imgTables.Count; i++)
                            {
                                //键值格式：监测器名$返回值名称$
                                strTmp = StatsTable.Rows[i][1].ToString() + "$" + StatsTable.Rows[i][2].ToString() + "$";
                                strGenKey = strTmp.Replace(" ", "_").Replace(":", "_").Replace("*", "_").Replace("/", "_").
                                        Replace("\\", "_").Replace("?", "_").Replace("|", "_").Replace("<", "_").Replace(">", "_").Replace("\"", "_");

                                //值格式：最小值$平均值$最大值$
                                strTmp = StatsTable.Rows[i][4].ToString() + "$" + StatsTable.Rows[i][5].ToString() + "$" + StatsTable.Rows[i][3].ToString();
                                strGenValue = strTmp.Replace(" ", "_").Replace(":", "_").Replace("*", "_").Replace("/", "_").
                                        Replace("\\", "_").Replace("?", "_").Replace("|", "_").Replace("<", "_").Replace(">", "_").Replace("\"", "_");

                                //写Ini
                                //iniFileDao.WriteIniFileString("reportgenerate.ini", strGenSection, strGenKey, strGenValue);
                                iniFileDao.WriteIniFileString("mmcreportgenerate.ini", strGenSection, strGenKey, strGenValue);
                            }
                        }
                        else
                        {
                            //键值格式：$返回值名称$
                            strTmp = file[key]["Mark"];
                            if (strTmp == "")
                            {
                                strTmp = file[key]["Type"];
                            }

                            strGenKey = "$" + strTmp + "$";

                            //值格式：最小值$平均值$最大值$  --> 暂时没有用
                            //strTmp = StatsTable.Rows[i][4].ToString() + "$" + StatsTable.Rows[i][5].ToString() + "$" + StatsTable.Rows[i][3].ToString();
                            //strGenValue = strTmp.Replace(" ", "_").Replace(":", "_").Replace("*", "_").Replace("/", "_").
                            //        Replace("\\", "_").Replace("?", "_").Replace("|", "_").Replace("<", "_").Replace(">", "_").Replace("\"", "_");

                            //写Ini
                            //iniFileDao.WriteIniFileString("topnreportgenerate.ini", strGenSection, strGenKey, strGenValue);
                            iniFileDao.WriteIniFileString("mmctopnreportgenerate.ini", strGenSection, strGenKey, strGenValue);
                        }

                        //如果用户提供了 邮件地址 则压缩报告文件并发送
                        if (bSendMail && file[key]["EmailSend"] != "" && file[key]["EmailSend"] != null)
                        {
                            //报告发送地址
                            strToInfo = file[key]["EmailSend"];

                            //组建压缩包文件等
                            strBaseDirectory = "";
                            strZipFileName = "";
                            string p = BasePath + "ReportTemplate\\" + DatePart + "\\" + strReportName;
                            if (!IsTopN)
                            {
                                //组建压缩包目录
                                if (!Directory.Exists(BasePath + "ReportTemplate\\statreport"))
                                {
                                    Directory.CreateDirectory(BasePath + "ReportTemplate\\statreport");
                                    Directory.CreateDirectory(BasePath + "ReportTemplate\\statreport\\jpeg");
                                }
                                else
                                {
                                    CleanFiles(BasePath + "ReportTemplate\\statreport");
                                    CleanFiles(BasePath + "ReportTemplate\\statreport");

                                    Directory.CreateDirectory(BasePath + "ReportTemplate\\statreport");
                                    Directory.CreateDirectory(BasePath + "ReportTemplate\\statreport\\jpeg");
                                }

                                strBaseDirectory = BasePath + "ReportTemplate\\statreport\\";
                                strZipFileName = BasePath + "ReportTemplate\\statreport" + key.Split('|')[1] + ".zip";

                                //拷贝html文件 
                                File.Copy(strReportFile, strBaseDirectory + strReportName + ".html");

                                //拷贝jpeg图片文件
                                for (int i = 0; i < imgTables.Count; i++)
                                {
                                    strTmp = key.Split('|')[1] + tmStart.ToString() + tmEnd.ToString() + StatsTable.Rows[i][1].ToString();
                                    name = strTmp.Replace(" ", "_").Replace(":", "_").Replace("*", "_").Replace("/", "_").
                                            Replace("\\", "_").Replace("?", "_").Replace("|", "_").Replace("<", "_").Replace(">", "_").Replace("\"", "_");

                                    if (File.Exists(p + "\\jpeg\\" + i + name + ".Png"))
                                        File.Copy(p + "\\jpeg\\" + i + name + ".Png",
                                            strBaseDirectory + "\\jpeg\\" + i + name + ".Png");
                                }
                            }
                            else
                            {
                                //组建压缩包目录
                                if (!Directory.Exists(BasePath + "ReportTemplate\\topnreport"))
                                {
                                    Directory.CreateDirectory(BasePath + "ReportTemplate\\topnreport");
                                    Directory.CreateDirectory(BasePath + "ReportTemplate\\topnreport\\jpeg");
                                }
                                else
                                {
                                    CleanFiles(BasePath + "ReportTemplate\\topnreport");
                                    CleanFiles(BasePath + "ReportTemplate\\topnreport");

                                    Directory.CreateDirectory(BasePath + "ReportTemplate\\topnreport");
                                    Directory.CreateDirectory(BasePath + "ReportTemplate\\topnreport\\jpeg");
                                }

                                strBaseDirectory = BasePath + "ReportTemplate\\topnreport\\";
                                strZipFileName = BasePath + "ReportTemplate\\topnreport" + key.Split('|')[1] + ".zip";

                                //拷贝html文件
                                File.Copy(strReportFile, strBaseDirectory + strReportName + ".html");

                                //拷贝jpeg图片文件
                                strTmp = key + tmStart.ToString() + tmEnd.ToString() + "TopN";
                                name = strTmp.Replace(" ", "_").Replace(":", "_").Replace("*", "_").Replace("/", "_").
                                            Replace("\\", "_").Replace("?", "_").Replace("|", "_").Replace("<", "_").Replace(">", "_").Replace("\"", "_");
                                name += ".Png";

                                if (File.Exists(p + "\\jpeg\\" + name))
                                    File.Copy(p + "\\jpeg\\" + name, strBaseDirectory + "\\jpeg\\" + name);
                            }

                            //压缩目录
                            ZipFileMain(strBaseDirectory, strZipFileName);

                            //发送邮件
                            if (!IsTopN)
                            {
                                SendSMTPEMail(strSmtpServerInfo, strFromInfo, strFromUserInfo, strFromPwdInfo, strToInfo, "statreport", "statreport", strZipFileName);
                            }
                            else
                            {
                                SendSMTPEMail(strSmtpServerInfo, strFromInfo, strFromUserInfo, strFromPwdInfo, strToInfo, "topnreport", "topnreport", strZipFileName);
                            }
                        }
                    }

                    System.Threading.Thread.Sleep(50);                    
                }
            }
            catch (System.Exception ex)
            {
                MessageBox.Show(ex.StackTrace, ex.Message, MessageBoxButtons.OK, MessageBoxIcon.Question);                
            }
        }

        /// <summary>
        /// 筛选数据
        /// </summary>
        /// <param name="tb">要筛选的数据表</param>
        /// <param name="_mark">条件(仅此程序中使用)</param>
        /// <returns></returns>
        private void selectDataTable(DataTable tb, string _mark)
        {
            ArrayList DatarowList = new ArrayList();
            for (int a = 0; a < tb.Rows.Count; a++)
            {
                if (tb.Rows[a][2].ToString().Trim() == _mark.Trim())
                {
                    DatarowList.Add(tb.Rows[a]);
                }
            }
            StatsTable.Rows.Clear();
            for (int s = 0; s < DatarowList.Count; s++)
            {
                StatsTable.Rows.Add(((DataRow)DatarowList[s]).ItemArray);
            }
        }

        /// <summary>
        /// 筛选数据
        /// </summary>
        /// <param name="tb">要筛选的数据表</param>
        /// <param name="_mark">条件(仅此程序中使用)</param>
        /// <returns></returns>
        private void selectDataTable(DataTable tb, string _mark, int _strCount, string _strSort)
        {
            ArrayList DatarowList = new ArrayList();
            for (int a = 0; a < tb.Rows.Count; a++)
            {
                if (tb.Rows[a][2].ToString().Trim() == _mark.Trim())
                {
                    DatarowList.Add(tb.Rows[a]);
                }
            }
            StatsTable.Rows.Clear();
            if (_strCount > DatarowList.Count)
            {
                if (_strSort == "降序")
                {
                    for (int s = 0; s < DatarowList.Count; s++)
                    {
                        StatsTable.Rows.Add(((DataRow)DatarowList[s]).ItemArray);
                    }
                }
                else
                {
                    for (int s = DatarowList.Count - 1; s >= 0; s--)
                    {
                        StatsTable.Rows.Add(((DataRow)DatarowList[s]).ItemArray);
                    }
                }
            }
            else
            {
                if (_strSort == "降序")
                {
                    for (int s = 0; s < _strCount; s++)
                    {
                        StatsTable.Rows.Add(((DataRow)DatarowList[s]).ItemArray);
                    }
                }
                else
                {
                    for (int s = DatarowList.Count - 1; s >= DatarowList.Count - _strCount; s--)
                    {
                        StatsTable.Rows.Add(((DataRow)DatarowList[s]).ItemArray);
                    }
                }
            }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="strReportName"></param>
        /// <param name="strStartTime"></param>
        /// <param name="strEndTime"></param>
        /// <returns></returns>
        private string MakeTitle(string strReportName, string strStartTime, string strEndTime)
        {
            StringBuilder st = new StringBuilder(null);
            //st.Append("<table border=\"0\" style=\"width: 100%\">\r\n");
            st.Append("<tr>\r\n");
            st.Append("<td align=\"center\" bgcolor=\"#99ccff\" style=\"width:100%\">\r\n");
            st.Append(strReportName);
            st.Append("</td>\r\n");                
            st.Append("</tr>\r\n");
            st.Append("<tr>\r\n");
            st.Append("<td align=\"center\" bgcolor=\"#99ccff\" style=\"width:100%\">\r\n");
            st.Append("时间段:" + strStartTime + "~" + strEndTime);
            st.Append("</td>\r\n");
            st.Append("</tr>\r\n");
            //st.Append("</table>\r\n");

            return st.ToString();
        }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="strReportName"></param>
        /// <param name="strStartTime"></param>
        /// <param name="strEndTime"></param>
        /// <returns></returns>
        private string MakeTopNhtmlPicture(string strReportName, string strStartTime, string strEndTime)
        {
            string strTmp = strReportName + strStartTime + strEndTime + "TopN";
            string name = strTmp.Replace(" ", "_").Replace(":", "_").Replace("*", "_").Replace("/", "_").
                        Replace("\\", "_").Replace("?", "_").Replace("|", "_").Replace("<", "_").Replace(">", "_").Replace("\"", "_");

            //string name = "TopNPicrure";
            StringBuilder st = new StringBuilder(null);
            st.Append("<table border=\"1\" style=\"width: 100%\">\r\n");
            st.Append("<tr height=500px>\r\n");
            st.Append("<td align=\"center\" style=\"width: 100%;\">\r\n");
            //st.Append("<img src=\"" + AppDomain.CurrentDomain.BaseDirectory + "\\jpeg\\" + name + ".Png" + "\"></img></td>\r\n");
            st.Append("<img src=\"" + "./jpeg/" + name + ".Png" + "\"></img></td>\r\n");
            //st.Append("<img src=\"" + ".\\" + name + ".Png" + "\"></img></td>\r\n");
            st.Append("</tr>\r\n");
            st.Append("</table>\r\n");

            return st.ToString();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        private string MakeTopNhtmltable()
        {
            StringBuilder st = new StringBuilder(null);
            st.Append("<table border=\"1\" style=\"width: 100%\">\r\n");
            st.Append("<tr>\r\n");
            st.Append("<td bgcolor=\"#99ccff\" style=\"width: 100px; height: 21px\">\r\n");
            st.Append("<span id=\"o46\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">设备名称</span></td>\r\n");
            st.Append("<td bgcolor=\"#99ccff\" style=\"width: 100px; height: 21px\">\r\n");
            st.Append("<span id=\"o4f\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">监测器名称</span></td>\r\n");
            st.Append("<td bgcolor=\"#99ccff\" style=\"width: 100px; height: 21px\">\r\n");
            st.Append("<span id=\"o57\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">最大值</span></td>\r\n");
            st.Append("<td bgcolor=\"#99ccff\" style=\"width: 100px; height: 21px\">\r\n");
            st.Append("<span id=\"o5f\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">平均值</span></td>\r\n");
            st.Append("<td bgcolor=\"#99ccff\" style=\"width: 100px; height: 21px\">\r\n");
            st.Append("<span id=\"o67\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">最小值</span></td>\r\n");
            st.Append("<td bgcolor=\"#99ccff\" style=\"width: 100px; height: 21px;\">\r\n");
            st.Append("<span id=\"o6f\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">最近一次描述</span></td>\r\n");
            st.Append("</tr>\r\n");
            if (StatName.Count > 0)
            {
                for (int i = 0; i < StatName.Count; i++)
                {
                    st.Append("<tr>\r\n");
                    st.Append("<td style=\"width: 100px; height: 21px\">\r\n");
                    st.Append("<span id=\"o46\" style=\"font-size: smaller;word-break:break-all;\">" + StatName[i].ToString().Split(':')[0] + "</span></td>\r\n");
                    st.Append("<td style=\"width: 100px; height: 21px\">\r\n");
                    st.Append("<span id=\"o4f\" style=\"font-size: smaller;word-break:break-all;\">" + StatName[i].ToString() + "</span></td>\r\n");
                    st.Append("<td style=\"width: 100px; height: 21px\">\r\n");
                    st.Append("<span id=\"o57\" style=\"font-size: smaller;word-break:break-all;\">" + StatMax[i].ToString() + "</span></td>\r\n");
                    st.Append("<td style=\"width: 100px; height: 21px\">\r\n");
                    st.Append("<span id=\"o5f\" style=\"font-size: smaller;word-break:break-all;\">" + StatAverage[i].ToString() + "</span></td>\r\n");
                    st.Append("<td style=\"width: 100px; height: 21px\">\r\n");
                    st.Append("<span id=\"o67\" style=\"font-size: smaller;word-break:break-all;\">" + StatMin[i].ToString() + "</span></td>\r\n");
                    st.Append("<td style=\"width: 100px; height: 21px\">\r\n");
                    st.Append("<span id=\"o6f\" style=\"font-size: smaller;word-break:break-all;\">" + LastDescriptionTopN[i].ToString() + "</span></td>\r\n");
                    st.Append("</tr>\r\n");
                }
            }
            else
            {
                st.Append("<tr>\r\n");
                st.Append("<td style=\"width: 100px\">\r\n");
                st.Append("<span id=\"o46\" style=\"font-size: smaller;word-break:break-all;\">无数据......</span></td>\r\n");
                st.Append("</tr>\r\n");
            }
            st.Append("</table>\r\n");


            return st.ToString();
        }

        /**/
        /// <summary>
        /// 写入日志文件
        /// </summary>
        /// <param name="input"></param>
        private void WriteLogFile(string input)
        {
            string fname = AppDomain.CurrentDomain.BaseDirectory + "\\" + input;//nvelocity.properties";
            FileInfo finfo = new FileInfo(fname);
            if (finfo.Exists && finfo.Length > 0)
            {
                return;
            }
            using (FileStream fs = finfo.OpenWrite())
            {
                StreamWriter w = new StreamWriter(fs);
                w.BaseStream.Seek(0, SeekOrigin.End);
                w.WriteLine("runtime.log = nvelocity_example1.log");
                w.WriteLine("input.encoding=GB2312");
                w.WriteLine("output.encoding=GB2312");
                w.Flush();
                w.Close();
            }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        private string MakeDataStrTable(DataTable table)
        {
            //名称 时间 返回值
            StringBuilder DataStrTable = new StringBuilder(null);
            DataStrTable.Append("<table border=\"1\" style=\"width: 100%; height: 100%;\">\r\n");
            DataStrTable.Append("<tr>");
            DataStrTable.Append("<td bgcolor=\"#ccffcc\" style=\"width: 100px font-weight: bold;\">\r\n");
            DataStrTable.Append("<span id=\"o46\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">名称</span>\r\n");
            DataStrTable.Append("</td>\r\n");
            DataStrTable.Append("<td bgcolor=\"#ccffcc\" style=\"width: 100px; font-weight: bold;\">\r\n");
            DataStrTable.Append("<span id=\"o46\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">时间</span>\r\n");
            DataStrTable.Append("</td>\r\n");
            DataStrTable.Append("<td bgcolor=\"#ccffcc\" style=\"width: 200px; font-weight: bold;\">\r\n");
            DataStrTable.Append("<span id=\"o46\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">返回值</span>\r\n");
            DataStrTable.Append("</td>\r\n");
            DataStrTable.Append("</tr>\r\n");

            if (table.Rows.Count > 0)
            {
                for (int i = 0; i < table.Rows.Count; i++)
                {
                    DataStrTable.Append("<tr>\r\n");

                    DataStrTable.Append("<td style=\"width: 100px;\">\r\n");
                    DataStrTable.Append("<span id=\"o46\" style=\"font-size: smaller;word-break:break-all;\">" + table.Rows[i]["名称"].ToString() + "</span>\r\n");
                    DataStrTable.Append("</td>\r\n");

                    DataStrTable.Append("<td style=\"width: 100px;\">\r\n");
                    DataStrTable.Append("<span id=\"o46\" style=\"font-size: smaller;word-break:break-all;\">" + table.Rows[i]["时间"].ToString() + "</span>\r\n");
                    DataStrTable.Append("</td>\r\n");

                    DataStrTable.Append("<td style=\"width: 200px;\">\r\n");
                    DataStrTable.Append("<span id=\"o46\" style=\"font-size: smaller;word-break:break-all;\">" + table.Rows[i]["返回值"].ToString() + "</span>\r\n");
                    DataStrTable.Append("</td>\r\n");

                    DataStrTable.Append("</tr>\r\n");
                }
            }
            else
            {
                DataStrTable.Append("<tr>\r\n");
                DataStrTable.Append("<td style=\"width: 100px\">\r\n");
                DataStrTable.Append("<span id=\"o46\" style=\"font-size: smaller;word-break:break-all;\">无数据......</span>\r\n");
                DataStrTable.Append("</td>\r\n");
                DataStrTable.Append("</tr>\r\n");
            }

            DataStrTable.Append("</table>\r\n");

            return DataStrTable.ToString();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        private string MakeRunHTMLTable()
        {
            StringBuilder Runhtmltable = new StringBuilder(null);
            Runhtmltable.Append("<table border=\"1\" style=\"width: 100%; height: 100%;\">\r\n");
            Runhtmltable.Append("<tr>");
            Runhtmltable.Append("<td bgcolor=\"#ccffcc\" style=\"width: 100px font-weight: bold;\">\r\n");
            Runhtmltable.Append("<span id=\"o46\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">名称</span>\r\n");
            Runhtmltable.Append("</td>\r\n");
            Runhtmltable.Append("<td bgcolor=\"#ccffcc\" style=\"width: 100px; font-weight: bold;\">\r\n");
            Runhtmltable.Append("<span id=\"o46\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">正常(%)</span>\r\n");
            Runhtmltable.Append("</td>\r\n");
            Runhtmltable.Append("<td bgcolor=\"#ccffcc\" style=\"width: 100px; font-weight: bold;\">\r\n");
            Runhtmltable.Append("<span id=\"o46\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">危险(%)</span>\r\n");
            Runhtmltable.Append("</td>\r\n");
            Runhtmltable.Append("<td bgcolor=\"#ccffcc\" style=\"width: 100px; font-weight: bold;\">\r\n");
            Runhtmltable.Append("<span id=\"o46\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">错误(%)</span>\r\n");
            Runhtmltable.Append("</td>\r\n");
            Runhtmltable.Append("<td bgcolor=\"#ccffcc\" style=\"width: 100px; font-weight: bold;\">\r\n");
            Runhtmltable.Append("<span id=\"o46\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">阀值</span>\r\n");
            Runhtmltable.Append("</td>\r\n");
            Runhtmltable.Append("<td bgcolor=\"#ccffcc\" style=\"width: 100px; font-weight: bold;\">\r\n");
            Runhtmltable.Append("<span id=\"o46\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">最新状态<span>\r\n");
            Runhtmltable.Append("</td>\r\n");
            Runhtmltable.Append("</tr>\r\n");
            if (RunName.Count > 0)
            {
                for (int i = 0; i < RunName.Count; i++)
                {
                    Runhtmltable.Append("<tr>\r\n");

                    Runhtmltable.Append("<td style=\"width: 100px;\">\r\n");
                    Runhtmltable.Append("<span id=\"o46\" style=\"font-size: smaller;word-break:break-all;\">" + RunName[i].ToString() + "</span>\r\n");
                    Runhtmltable.Append("</td>\r\n");

                    Runhtmltable.Append("<td style=\"width: 100px;\">\r\n");
                    Runhtmltable.Append("<span id=\"o46\" style=\"font-size: smaller;word-break:break-all;\">" + RunRight[i].ToString() + "</span>\r\n");
                    Runhtmltable.Append("</td>\r\n");

                    Runhtmltable.Append("<td style=\"width: 100px;\">\r\n");
                    Runhtmltable.Append("<span id=\"o46\" style=\"font-size: smaller;word-break:break-all;\">" + Rundanger[i].ToString() + "</span>\r\n");
                    Runhtmltable.Append("</td>\r\n");

                    Runhtmltable.Append("<td style=\"width: 100px;\">\r\n");
                    Runhtmltable.Append("<span id=\"o46\" style=\"font-size: smaller;word-break:break-all;\">" + RunWrong[i].ToString() + "</span>\r\n");
                    Runhtmltable.Append("</td>\r\n");

                    Runhtmltable.Append("<td style=\"width: 100px;\">\r\n");
                    Runhtmltable.Append("<span id=\"o46\" style=\"font-size: smaller;word-break:break-all;\">" + RunPass[i].ToString() + "</span>\r\n");
                    Runhtmltable.Append("</td>\r\n");

                    Runhtmltable.Append("<td style=\"width: 100px;\">\r\n");
                    Runhtmltable.Append("<span id=\"o46\" style=\"font-size: smaller;word-break:break-all;\">" + RunLatest[i].ToString() + "</span>\r\n");
                    Runhtmltable.Append("</td>\r\n");

                    Runhtmltable.Append("</tr>\r\n");
                }
            }
            else
            {
                Runhtmltable.Append("<tr>\r\n");
                Runhtmltable.Append("<td style=\"width: 100px\">\r\n");
                Runhtmltable.Append("<span id=\"o46\" style=\"font-size: smaller\">无数据......</span>\r\n");
                Runhtmltable.Append("</td>\r\n");
                Runhtmltable.Append("</tr>\r\n");
            }
            Runhtmltable.Append("</table>\r\n");
            return Runhtmltable.ToString();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        private string MakeStatehtmltable(string strReportName, string strStartTime, string strEndTime)
        {
            StringBuilder Stathtmltable = new StringBuilder(null);
            Stathtmltable.Append("<table border=\"1\" style=\"width: 100%; height: 100%;\">\r\n");
            Stathtmltable.Append("<tr>\r\n");
            Stathtmltable.Append("<td bgcolor=\"#ccffcc\" style=\"width: 100px; font-weight: bold;\">\r\n");
            Stathtmltable.Append("<span id=\"o46\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">名称</span>\r\n");
            Stathtmltable.Append("</td>\r\n");
            Stathtmltable.Append("<td bgcolor=\"#ccffcc\" style=\"width: 100px; font-weight: bold;\">\r\n");
            Stathtmltable.Append("<span id=\"o46\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">返回值名称</span>\r\n");
            Stathtmltable.Append("</td>\r\n");
            Stathtmltable.Append("<td bgcolor=\"#ccffcc\" style=\"width: 100px; font-weight: bold;\">\r\n");
            Stathtmltable.Append("<span id=\"o46\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">最大值</span>\r\n");
            Stathtmltable.Append("</td>\r\n");
            Stathtmltable.Append("<td bgcolor=\"#ccffcc\" style=\"width: 100px; font-weight: bold;\">\r\n");
            Stathtmltable.Append("<span id=\"o46\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">最小值</span>\r\n");
            Stathtmltable.Append("</td>\r\n");
            Stathtmltable.Append("<td bgcolor=\"#ccffcc\" style=\"width: 100px; font-weight: bold;\">\r\n");
            Stathtmltable.Append("<span id=\"o46\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">平均值</span>\r\n");
            Stathtmltable.Append("</td>\r\n");
            Stathtmltable.Append("<td bgcolor=\"#ccffcc\" style=\"width: 100px; font-weight: bold;\">\r\n");
            Stathtmltable.Append("<span id=\"o46\" style=\"font-weight: bold; font-size: smaller;word-break:break-all;\">最近一次</span>\r\n");
            Stathtmltable.Append("</td>\r\n");
            Stathtmltable.Append("</tr>\r\n");
            if (StatName.Count > 0)
            {
                for (int i = 0; i < StatName.Count; i++)
                {
                    string strTmp = strReportName + strStartTime + strEndTime + StatsTable.Rows[i][1].ToString();

                    string name = strTmp.Replace(" ", "_").Replace(":", "_").Replace("*", "_").Replace("/", "_").
                          Replace("\\", "_").Replace("?", "_").Replace("|", "_").Replace("<", "_").Replace(">", "_").Replace("\"", "_");

                    Stathtmltable.Append("<tr>\r\n");
                    Stathtmltable.Append("<td style=\"width: 100px;\">\r\n");
                    Stathtmltable.Append("<a href=\"#" + i + name + "\">" + StatName[i].ToString() + "</a>\r\n");
                    Stathtmltable.Append("</td>\r\n");
                    Stathtmltable.Append("<td style=\"width: 100px;\">\r\n");
                    Stathtmltable.Append("<span id=\"o46\" style=\"font-size: smaller;word-break:break-all;\">" + StatReturnName[i].ToString() + "</span>\r\n");
                    Stathtmltable.Append("</td>\r\n");
                    Stathtmltable.Append("<td style=\"width: 100px;\">\r\n");
                    Stathtmltable.Append("<span id=\"o46\" style=\"font-size: smaller;word-break:break-all;\">" + StatMax[i].ToString() + "</span>\r\n");
                    Stathtmltable.Append("</td>\r\n");
                    Stathtmltable.Append("<td style=\"width: 100px;\">\r\n");
                    Stathtmltable.Append("<span id=\"o46\" style=\"font-size: smaller;word-break:break-all;\">" + StatMin[i].ToString() + "</span>\r\n");
                    Stathtmltable.Append("</td>\r\n");
                    Stathtmltable.Append("<td style=\"width: 100px;\">\r\n");
                    Stathtmltable.Append("<span id=\"o46\" style=\"font-size: smaller;word-break:break-all;\">" + StatAverage[i].ToString() + "</span>\r\n");
                    Stathtmltable.Append("</td>\r\n");
                    Stathtmltable.Append("<td style=\"width: 100px;\">\r\n");
                    Stathtmltable.Append("<span id=\"o46\" style=\"font-size: smaller;word-break:break-all;\">" + StatLatest[i].ToString() + "</span>\r\n");
                    Stathtmltable.Append("</td>\r\n");
                    Stathtmltable.Append("</tr>\r\n");
                }
            }
            else
            {
                Stathtmltable.Append("<tr>\r\n");
                Stathtmltable.Append("<td style=\"width: 100px\">\r\n");
                Stathtmltable.Append("<span id=\"o46\" style=\"font-size: smaller;word-break:break-all;\">无数据......</span>\r\n");
                Stathtmltable.Append("</td>\r\n");
                Stathtmltable.Append("</tr>\r\n");
            }
            Stathtmltable.Append("</table>\r\n");
            return Stathtmltable.ToString();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="strReportName"></param>
        /// <param name="strStartTime"></param>
        /// <param name="strEndTime"></param>
        /// <returns></returns>
        private string MakeStatehtmlPicture(string strReportName, string strStartTime, string strEndTime)
        {
            StringBuilder Stathtmltable = new StringBuilder(null);
            Stathtmltable.Append("<table border=\"1\" style=\"width: 100%; height: 100%;\">\r\n");
            for (int i = 0; i < imgTables.Count; i++)
            {
                Stathtmltable.Append("<tr align=center height=335px>\r\n");
                Stathtmltable.Append("<td align=\"center\" style=\"width: 100%;\">\r\n");
                
                string strTmp = strReportName + strStartTime + strEndTime + StatsTable.Rows[i][1].ToString();
                string name = strTmp.Replace(" ", "_").Replace(":", "_").Replace("*", "_").Replace("/", "_").
                        Replace("\\", "_").Replace("?", "_").Replace("|", "_").Replace("<", "_").Replace(">", "_").Replace("\"", "_");

                //string name = StatsTable.Rows[i][1].ToString().Replace(" ", "_").Replace(":", "_").Replace("*", "_").Replace("/", "_").
                //       Replace("\\", "_").Replace("?", "_").Replace("|", "_").Replace("<", "_").Replace(">", "_").Replace("\"", "_");
                //Stathtmltable.Append("<a name=\"" + i + name + "\"></a><img src=\"" + AppDomain.CurrentDomain.BaseDirectory + "\\jpeg\\" + i + name + ".Png" + "\"></img>\r\n");
                Stathtmltable.Append("<a name=\"" + i + name + "\"></a><img src=\"" + "./jpeg/" + i + name + ".Png" + "\"></img>\r\n");
                Stathtmltable.Append("</td>\r\n");
                Stathtmltable.Append("</tr>\r\n");
            }
            Stathtmltable.Append("</table>\r\n");
            return Stathtmltable.ToString();
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="templateFileN"></param>
        /// <param name="istopn"></param>
        private void CreateVM(string templateFileN, bool istopn)
        {
            if (!Directory.Exists(BasePath + "\\ReportTemplate"))
            {
                Directory.CreateDirectory(BasePath + "\\ReportTemplate");
            }
            string fname = BasePath + "\\ReportTemplate\\" + templateFileN;//nvelocity.properties";
            FileInfo finfo = new FileInfo(fname);
            if (finfo.Exists && finfo.Length > 0)
            {
                return;
            }
            StringBuilder Vm = new StringBuilder(null);
            if (istopn)
            {
                Vm.Append("<html>\r\n");
                Vm.Append("<body>\r\n");
                Vm.Append("<center>\r\n");
                Vm.Append("<table border=\"1\" style=\"width: 700px\">\r\n");
                Vm.Append("<tr>\r\n");
                Vm.Append("<td style=\"width: 100%\">\r\n");
                Vm.Append("$MakeTopNhtmlPicture \r\n");
                Vm.Append("</td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("<tr>\r\n");
                Vm.Append("<td align=\"left\" bgcolor=\"#99ccff\" style=\"width: 100%\">\r\n");
                Vm.Append("</td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("<tr>\r\n");
                Vm.Append("<td style=\"width: 100%\">\r\n");
                Vm.Append("$MakeTopNhtmltable \r\n");
                Vm.Append("</td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("</table>\r\n");
                Vm.Append("</center>\r\n");
                Vm.Append("</body>\r\n");
                Vm.Append("</html>\r\n");
            }
            else
            {
                Vm.Append("<html>\r\n");
                Vm.Append("<body>\r\n");
                Vm.Append("<center>\r\n");
                Vm.Append("<div style=\"text-align: center\">\r\n");
                Vm.Append("<table border=\"0\" style=\"width: 700px\">\r\n");
                Vm.Append("<tr>\r\n");
                Vm.Append("<td align=\"left\" bgcolor=\"#99ccff\" style=\"width: 100%\">\r\n");
                Vm.Append("监测信息</td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("<tr>\r\n");
                Vm.Append("<td style=\"width: 100%\">\r\n");
                Vm.Append("$MakeRunHTMLTable \r\n");
                Vm.Append("</td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("<tr>\r\n");
                Vm.Append("<td align=\"left\" bgcolor=\"#99ccff\" style=\"width: 100%\">\r\n");
                Vm.Append("运行情况表</td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("<tr>\r\n");
                Vm.Append("<td style=\"width: 100%\">\r\n");
                Vm.Append("$MakeStatehtmltable \r\n");
                Vm.Append("</td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("<tr>\r\n");
                Vm.Append(" <td align=\"left\" bgcolor=\"#99ccff\" style=\"width: 100%\">\r\n");
                Vm.Append("图表</td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("<tr>\r\n");
                Vm.Append("<td align=\"center\" style=\"width: 100%\">\r\n");
                Vm.Append("$MakeStatehtmlPicture \r\n");
                Vm.Append("</td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("</table>\r\n");
                Vm.Append("</div>\r\n");
                Vm.Append("</center>\r\n");
                Vm.Append("</body>\r\n");
                Vm.Append("</html>");
            }

            using (FileStream fs = finfo.OpenWrite())
            {
                StreamWriter w = new StreamWriter(fs, Encoding.Default);
                w.BaseStream.Seek(0, SeekOrigin.End);
                w.Write(Vm.ToString());
                w.Flush();
                w.Close();
            }
        }

        /// <summary>
        /// 建立临时VM文件
        /// </summary>
        /// <param name="templateFileN"></param>
        /// <param name="istopn"></param>
        private void CreateVM(string templateFileN, bool istopn, string strReportName)
        {
            if (!Directory.Exists(BasePath + "\\ReportTemplate"))
            {
                Directory.CreateDirectory(BasePath + "\\ReportTemplate");
            }
            string fname = BasePath + "\\ReportTemplate\\" + templateFileN;//nvelocity.properties";
            FileInfo finfo = new FileInfo(fname);
            if (finfo.Exists && finfo.Length > 0)
            {
                return;
            }
            StringBuilder Vm = new StringBuilder(null);
            if (istopn)
            {
                Vm.Append("<html>\r\n");
                Vm.Append("<head>");
                Vm.Append("<META HTTP-EQUIV=\"PRAGMA\"   CONTENT=\"NO-CACHE\">");
                Vm.Append("</head>\r\n");
                Vm.Append("<body style=\"top: 12px; right: 12px; left: 12px;position: absolute;\">\r\n");
                Vm.Append("<center>\r\n");
                Vm.Append("<table border=\"1\" style=\"width: 700px;margin-left:3px;margin-right:3px;\">\r\n");
                //Vm.Append("<tr>\r\n");
                //Vm.Append("<td align=\"left\" bgcolor=\"#99ccff\" style=\"width: 100%\">\r\n");
                Vm.Append("$MakeTitleTable");// </td>\r\n");
                //Vm.Append("</tr>\r\n");

                Vm.Append("<tr>\r\n");
                Vm.Append("<td style=\"width: 100%\">\r\n");
                Vm.Append("$MakeTopNhtmlPicture \r\n");
                Vm.Append("</td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("<tr>\r\n");
                Vm.Append("<td align=\"left\" bgcolor=\"#99ccff\" style=\"width: 100%\">\r\n");
                Vm.Append("</td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("<tr>\r\n");
                Vm.Append("<td style=\"width: 100%\">\r\n");
                Vm.Append("$MakeTopNhtmltable \r\n");
                Vm.Append("</td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("</table>\r\n");
                Vm.Append("</center>\r\n");
                Vm.Append("</body>\r\n");
                Vm.Append("</html>\r\n");
            }
            else
            {
                Vm.Append("<html>\r\n");
                Vm.Append("<head>");
                Vm.Append("<META HTTP-EQUIV=\"PRAGMA\"   CONTENT=\"NO-CACHE\">");
                Vm.Append("</head>\r\n");
                Vm.Append("<body style=\"top: 12px; right: 12px; left: 12px;position: absolute;\">\r\n");
                Vm.Append("<center>\r\n");
                Vm.Append("<div style=\"text-align: center;margin-left:3px;margin-right:3px;\">\r\n");
                Vm.Append("<table border=\"0\" style=\"width: 700px\">\r\n");
                //Vm.Append("<tr>\r\n");
                //Vm.Append("<td align=\"left\" bgcolor=\"#99ccff\" style=\"width: 100%\">\r\n");
                Vm.Append("$MakeTitleTable");// </td>\r\n");
                //Vm.Append("</tr>\r\n");

                Vm.Append("<tr>\r\n");
                Vm.Append("<td align=\"left\" bgcolor=\"#99ccff\" style=\"width: 100%\">\r\n");
                Vm.Append("监测信息</td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("<tr>\r\n");
                Vm.Append("<td style=\"width: 100%\">\r\n");
                Vm.Append("$MakeRunHTMLTable \r\n");
                Vm.Append("</td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("<tr>\r\n");
                Vm.Append("<td align=\"left\" bgcolor=\"#99ccff\" style=\"width: 100%\">\r\n");
                Vm.Append("运行情况表</td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("<tr>\r\n");
                Vm.Append("<td style=\"width: 100%\">\r\n");
                Vm.Append("$MakeStatehtmltable \r\n");
                Vm.Append("</td>\r\n");
                Vm.Append("</tr>\r\n");

                //是否列出图片
                Vm.Append("#if($IsGraphic) \r\n");
                Vm.Append("<tr>\r\n");
                Vm.Append("<td align=\"left\" bgcolor=\"#99ccff\" style=\"width: 100%\">\r\n");
                Vm.Append("图表</td>\r\n");
                Vm.Append("</tr>\r\n");

                Vm.Append("<tr>\r\n");
                Vm.Append("<td align=\"center\" style=\"width: 100%\">\r\n");
                Vm.Append("$MakeStatehtmlPicture \r\n");
                Vm.Append("</td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("#end \r\n");

                //是否列出状态总结
                Vm.Append("#if($IsStatusResultVisible) \r\n");
                Vm.Append("<tr>\r\n");
                Vm.Append("<td align=\"left\" bgcolor=\"#99ccff\" style=\"width: 100%\">\r\n");
                Vm.Append("数据</td>\r\n");
                Vm.Append("</tr>\r\n");

                Vm.Append("<tr>\r\n");
                Vm.Append("<td align=\"left\" bgcolor=\"#99ccff\" style=\"width: 100%\">\r\n");
                Vm.Append("$MakeDataStrTable </td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("#end \r\n");

                //是否列出错误
                Vm.Append("#if($IsListErrorVisible) \r\n");
                Vm.Append("<tr>\r\n");
                Vm.Append("<td align=\"left\" bgcolor=\"#99ccff\" style=\"width: 100%\">\r\n");
                Vm.Append("错误</td>\r\n");
                Vm.Append("</tr>\r\n");

                Vm.Append("<tr>\r\n");
                Vm.Append("<td align=\"left\" bgcolor=\"#99ccff\" style=\"width: 100%\">\r\n");
                Vm.Append("$MakeErrorDataStrTable </td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("#end \r\n");

                //是否列出危险
                Vm.Append("#if($IsListDangerVisible) \r\n");
                Vm.Append("<tr>\r\n");
                Vm.Append("<td align=\"left\" bgcolor=\"#99ccff\" style=\"width: 100%\">\r\n");
                Vm.Append("危险</td>\r\n");
                Vm.Append("</tr>\r\n");

                Vm.Append("<tr>\r\n");
                Vm.Append("<td align=\"left\" bgcolor=\"#99ccff\" style=\"width: 100%\">\r\n");
                Vm.Append("$MakeDangerDataStrTable </td>\r\n");
                Vm.Append("</tr>\r\n");
                Vm.Append("#end \r\n");

                Vm.Append("</table>\r\n");
                Vm.Append("</div>\r\n");
                Vm.Append("</center>\r\n");
                Vm.Append("</body>\r\n");
                Vm.Append("</html>");
            }

            using (FileStream fs = finfo.OpenWrite())
            {
                StreamWriter w = new StreamWriter(fs, Encoding.Default);
                w.BaseStream.Seek(0, SeekOrigin.End);
                w.Write(Vm.ToString());
                w.Flush();
                w.Close();
            }
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="strReportName"></param>
        /// <param name="strStartTime"></param>
        /// <param name="strEndTime"></param>
        /// <param name="istopn"></param>
        private void Savepictures(string strReportName, string strStartTime, string strEndTime, bool istopn)
        {
            try
            {
                string Folder = strStartTime + strEndTime + strReportName;
                Folder = Folder.Replace(" ", "_").Replace(":", "_").Replace("*", "_").Replace("/", "_").
                                        Replace("\\", "_").Replace("?", "_").Replace("|", "_").Replace("<", "_").Replace(">", "_").Replace("\"", "_");
                string p = BasePath + "ReportTemplate\\" + DatePart + "\\" + Folder;
                if (istopn)
                {
                    if (!Directory.Exists(p + "TopN\\jpeg"))
                    {
                        Directory.CreateDirectory(p + "TopN\\jpeg");
                    }

                    DataTable imgtable = new DataTable();
                    DataColumn cc1 = new DataColumn("监测器");
                    DataColumn cc2 = new DataColumn("监测指标", typeof(double));
                    imgtable.Columns.Add(cc1);
                    imgtable.Columns.Add(cc2);
                    string[,] imgarray = new string[StatsTable.Rows.Count, 2];
                    for (int bt = 0; bt < StatsTable.Rows.Count; bt++)
                    {
                        imgarray[bt, 0] = StatsTable.Rows[bt][1].ToString() + "\n" + StatsTable.Rows[bt][2].ToString();
                        imgarray[bt, 1] = StatsTable.Rows[bt][5].ToString();
                    }
                    DataRow imgrow;
                    for (int tb = 0; tb < StatsTable.Rows.Count; tb++)
                    {
                        imgrow = imgtable.NewRow();
                        for (int bbt = 0; bbt < 2; bbt++)
                        {
                            if (bbt == 1)
                            {
                                imgrow[bbt] = Convert.ToDouble(imgarray[tb, bbt].ToString());
                            }
                            else
                            {
                                imgrow[bbt] = imgarray[tb, bbt].ToString();
                            }
                        }
                        imgtable.Rows.Add(imgrow);
                    }
                    this.Size = new Size(this.Size.Width, 500);
                    this.ultraChart1.ColorModel.ModelStyle = ColorModels.CustomRandom;
                    this.ultraChart1.ChartType = ChartType.ColumnChart;
                    this.ultraChart1.ColumnChart.ColumnSpacing = 1;
                    this.ultraChart1.Axis.X.Extent = 200;
                    this.ultraChart1.Data.SwapRowsAndColumns = true;
                    this.ultraChart1.Axis.X.TickmarkStyle = AxisTickStyle.Smart;
                    this.ultraChart1.Legend.Visible = false;

                    if (imgtable.Rows.Count > 0)
                    {
                        this.ultraChart1.DataSource = imgtable;
                    }
                    else
                    {
                        this.ultraChart1.DataSource = ImgFilter();
                    }
                    this.ultraChart1.DataBind();
                    this.ultraChart1.Refresh();

                    string strTmp = strReportName + strStartTime + strEndTime + "TopN";
                    string name = strTmp.Replace(" ", "_").Replace(":", "_").Replace("*", "_").Replace("/", "_").
                                Replace("\\", "_").Replace("?", "_").Replace("|", "_").Replace("<", "_").Replace(">", "_").Replace("\"", "_");

                    this.ultraChart1.TitleTop.Text = file[strReportName]["Title"].ToString().Split('|')[0];
                    //this.ultraChart1.TitleLeft.Text = "";

                    this.ultraChart1.SaveTo(p + "TopN\\jpeg\\" + name + ".Png", System.Drawing.Imaging.ImageFormat.Png);

                    this.ultraChart1.TitleTop.Text = "";
                    //this.ultraChart1.TitleLeft.Text = "";
                }
                else
                {
                    if (file[strReportName]["Graphic"].Equals("Yes"))
                    {
                        if (!Directory.Exists(p + "\\jpeg"))
                        {
                            Directory.CreateDirectory(p + "\\jpeg");
                        }
                        switch (this.file[strReportName]["ComboGraphic"])
                        {
                            case "线状图":
                                this.ultraChart1.ChartType = ChartType.LineChart;
                                break;
                            case "柱状图":
                                this.ultraChart1.ChartType = ChartType.ColumnChart;
                                break;
                        }
                        for (int i = 0; i < imgTables.Count; i++)
                        {
                            //this.ultraChart1.ChartType = ChartType.LineChart;
                            //this.ultraChart1.Data.SwapRowsAndColumns = true;
                            //this.ultraChart1.Legend.Visible = true;

                            if (imgTables[StatsTable.Rows[i]["hashKey"].ToString()].Rows.Count > 0)
                            {
                                this.ultraChart1.DataSource = imgTables[StatsTable.Rows[i]["hashKey"].ToString()];
                            }
                            else
                            {
                                this.ultraChart1.DataSource = ImgFilter();
                            }

                            this.ultraChart1.DataBind();
                            this.ultraChart1.Refresh();

                            string strTmp = strReportName + strStartTime + strEndTime + StatsTable.Rows[i][1].ToString();
                            string name = strTmp.Replace(" ", "_").Replace(":", "_").Replace("*", "_").Replace("/", "_").
                                    Replace("\\", "_").Replace("?", "_").Replace("|", "_").Replace("<", "_").Replace(">", "_").Replace("\"", "_");

                            this.ultraChart1.TitleTop.Text = StatsTable.Rows[i][1].ToString() + "(" + StatsTable.Rows[i][2].ToString() + ")\n";

                            //this.ultraChart1.TitleTop.Text += "\n";

                            this.ultraChart1.TitleTop.Text += "最大值:";
                            this.ultraChart1.TitleTop.Text += StatsTable.Rows[i][3].ToString();

                            this.ultraChart1.TitleTop.Text += "最小值:";
                            this.ultraChart1.TitleTop.Text += StatsTable.Rows[i][4].ToString();

                            this.ultraChart1.TitleTop.Text += "平均值:";
                            this.ultraChart1.TitleTop.Text += StatsTable.Rows[i][5].ToString();

                            //this.ultraChart1.TitleLeft.Text = StatsTable.Rows[i][2].ToString();
                            //this.ultraChart1.TitleBottom.Text = "监测时间";
                            //this.ultraChart1.Legend.Visible = true;
                            //this.ultraChart1.Legend.SpanPercentage = 100; 

                            this.ultraChart1.SaveTo(p + "\\jpeg\\" + i + name + ".Png", System.Drawing.Imaging.ImageFormat.Png);

                            this.ultraChart1.TitleTop.Text = "";
                            //this.ultraChart1.TitleLeft.Text = "";
                        }
                    }
                }
            }
            catch(Exception ex)
            {
                MessageBox.Show(ex.StackTrace, ex.Message);
            }
            
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        private DataView ImgFilter()
        {
            if (this.Imgsource == null)
            {
                DataTable dt = new DataTable();
                DataColumn column = null;
                column = new DataColumn();
                column.DataType = System.Type.GetType("System.String");
                column.ColumnName = "时间";
                dt.Columns.Add(column);
                column = new DataColumn();
                column.DataType = System.Type.GetType("System.Int32");
                column.ColumnName = "无监测数据";
                dt.Columns.Add(column);

                DataRow dr = dt.NewRow();
                dr["无监测数据"] = 0;
                dr["时间"] = DateTime.Now;
                dt.Rows.Add(dr);
                return new DataView(dt);
            }

            DataView dv = new DataView(this.Imgsource);
            if (dv.Count == 0)
            {
                DataTable dt = new DataTable();
                DataColumn column = null;
                column = new DataColumn();
                column.DataType = System.Type.GetType("System.String");
                column.ColumnName = "时间";
                dt.Columns.Add(column);
                column = new DataColumn();
                column.DataType = System.Type.GetType("System.Int32");
                column.ColumnName = "无监测数据";
                dt.Columns.Add(column);

                DataRow dr = dt.NewRow();
                dr["无监测数据"] = 0;
                dr["时间"] = "";
                dt.Rows.Add(dr);
                return new DataView(dt);
            }
            return dv;
        }

        #region 统计报告Property

        public virtual ArrayList RunQueryReportData
        {
            get
            {
                ArrayList list = new ArrayList();
                ArrayList list1 = new ArrayList();

                for (int i = 0; i < QueryReportData.Rows.Count; i++)
                {
                    for (int j = 0; j < QueryReportData.Columns.Count; j++)
                    {
                        list1.Add(QueryReportData.Rows[i][j].ToString());
                    }
                    list.Add(list1);
                }
                return list;
            }
        }

        public virtual ArrayList RunName
        {
            get
            {
                ArrayList list = new ArrayList();

                for (int i = 0; i < QueryReportData.Rows.Count; i++)
                {
                    list.Add(QueryReportData.Rows[i][0].ToString());
                }
                return list;
            }
        }

        public virtual ArrayList RunRight
        {
            get
            {
                ArrayList list = new ArrayList();

                for (int i = 0; i < QueryReportData.Rows.Count; i++)
                {
                    list.Add(QueryReportData.Rows[i][1].ToString());
                }
                return list;
            }
        }

        public virtual ArrayList Rundanger
        {
            get
            {
                ArrayList list = new ArrayList();

                for (int i = 0; i < QueryReportData.Rows.Count; i++)
                {
                    list.Add(QueryReportData.Rows[i][2].ToString());
                }
                return list;
            }
        }

        public virtual ArrayList RunWrong
        {
            get
            {
                ArrayList list = new ArrayList();

                for (int i = 0; i < QueryReportData.Rows.Count; i++)
                {
                    list.Add(QueryReportData.Rows[i][3].ToString());
                }
                return list;
            }
        }

        public virtual ArrayList RunPass
        {
            get
            {
                ArrayList list = new ArrayList();

                for (int i = 0; i < QueryReportData.Rows.Count; i++)
                {
                    list.Add(QueryReportData.Rows[i][4].ToString());
                }
                return list;
            }
        }

        public virtual ArrayList RunLatest
        {
            get
            {
                ArrayList list = new ArrayList();

                for (int i = 0; i < QueryReportData.Rows.Count; i++)
                {
                    list.Add(QueryReportData.Rows[i][5].ToString());
                }
                return list;
            }
        }


        public virtual ArrayList StatName
        {
            get
            {
                ArrayList list = new ArrayList();

                for (int i = 0; i < StatsTable.Rows.Count; i++)
                {
                    list.Add(StatsTable.Rows[i][1].ToString());
                }
                return list;
            }
        }

        public virtual ArrayList StatReturnName
        {
            get
            {
                ArrayList list = new ArrayList();

                for (int i = 0; i < StatsTable.Rows.Count; i++)
                {
                    list.Add(StatsTable.Rows[i][2].ToString());
                }
                return list;
            }
        }

        public virtual ArrayList StatMax
        {
            get
            {
                ArrayList list = new ArrayList();

                for (int i = 0; i < StatsTable.Rows.Count; i++)
                {
                    list.Add(StatsTable.Rows[i][3].ToString());
                }
                return list;
            }
        }

        public virtual ArrayList StatMin
        {
            get
            {
                ArrayList list = new ArrayList();

                for (int i = 0; i < StatsTable.Rows.Count; i++)
                {
                    list.Add(StatsTable.Rows[i][4].ToString());
                }
                return list;
            }
        }

        public virtual ArrayList StatAverage
        {
            get
            {
                ArrayList list = new ArrayList();

                for (int i = 0; i < StatsTable.Rows.Count; i++)
                {
                    list.Add(StatsTable.Rows[i][5].ToString());
                }
                return list;
            }
        }

        public virtual ArrayList StatLatest
        {
            get
            {
                ArrayList list = new ArrayList();

                for (int i = 0; i < StatsTable.Rows.Count; i++)
                {
                    list.Add(StatsTable.Rows[i][6].ToString());
                }
                return list;
            }
        }

        public virtual ArrayList LastDescriptionTopN
        {
            get
            {
                ArrayList list = new ArrayList();
                for (int i = 0; i < StatsTable.Rows.Count; i++)
                {
                    for (int _i = 0; _i < this.QueryReportData.Rows.Count; _i++)
                    {
                        if (StatsTable.Rows[i][1].ToString().Trim() == QueryReportData.Rows[_i][0].ToString().Trim())
                        {
                            list.Add(QueryReportData.Rows[_i][6].ToString());
                        }
                    }
                }
                return list;
            }
        }

        #endregion

        #region TopN报告 Property

        public ArrayList TopNEqName
        {
            set
            {

            }
            get
            {
                ArrayList list = new ArrayList();
                ////////////////////////////////////////////
                return list;
            }
        }
        public ArrayList TopNMonitorName
        {
            get
            {
                ArrayList list = new ArrayList();
                ////////////////////////////////////////////
                return list;
            }
        }
        public ArrayList TopNMax
        {
            get
            {
                ArrayList list = new ArrayList();
                ////////////////////////////////////////////
                return list;
            }
        }
        public ArrayList TopNMin
        {
            get
            {
                ArrayList list = new ArrayList();
                ////////////////////////////////////////////
                return list;
            }
        }
        public ArrayList TopNAvg
        {
            get
            {
                ArrayList list = new ArrayList();
                ////////////////////////////////////////////
                return list;
            }
        }
        public ArrayList TopNLastDescription
        {
            get
            {
                ArrayList list = new ArrayList();
                ////////////////////////////////////////////
                return list;
            }
        }

        #endregion

        #region 压缩并发送邮件的工具

        /// <summary>
        /// 发邮件
        /// </summary>
        /// <param name="strSmtpServer"></param>
        /// <param name="strFrom"></param>
        /// <param name="strFromPass"></param>
        /// <param name="strto"></param>
        /// <param name="strSubject"></param>
        /// <param name="strBody"></param>
        public void SendSMTPEMail(string strSmtpServer, string strFrom, string strFromUser, string strFromPass, string strTo, string strSubject, string strBody, string strAttach)
        {
            try
            {

                if (strSmtpServer == null || strFrom == null || strFromUser == null || strFromPass == null || strTo == null || strSubject == null || strBody == null || strAttach == null)
                    return;

                System.Net.Mail.SmtpClient client = new SmtpClient(strSmtpServer);
                client.UseDefaultCredentials = false;
                client.Credentials = new System.Net.NetworkCredential(strFromUser, strFromPass);
                client.DeliveryMethod = SmtpDeliveryMethod.Network;

                System.Net.Mail.MailMessage message = new System.Net.Mail.MailMessage(strFrom, strTo, strSubject, strBody);

                System.Net.Mail.Attachment attachment = new System.Net.Mail.Attachment(strAttach);
                message.Attachments.Add(attachment);

                message.BodyEncoding = System.Text.Encoding.UTF8;
                message.IsBodyHtml = true;
                client.Send(message);
            }
            catch (System.Exception ex)
            {
                ;
            }
        }

        /// <summary>
        /// 获取所有子目录路径
        /// </summary>
        /// <param name="strBaseDir"></param>
        public void GetAllDirList(string strBaseDir)
        {
            string[] filenames = Directory.GetDirectories(strBaseDir);
            foreach (string file in filenames)
            {
                //某个子目录的绝对地址，把它记录在ArrayList中
                lstDirectories.Add(file);

                //递归调用
                GetAllDirList(file);
            }
        }

        /// <summary>
        /// 压缩报告目录
        /// </summary>
        /// <param name="args"></param>
        public void ZipFileMain(string strBaseDirectory, string strZipFile)
        {
            //
            if (File.Exists(strZipFile))
            {
                File.Delete(strZipFile);
            }

            lstDirectories.Clear();

            lstDirectories.Add(strBaseDirectory);
            GetAllDirList(strBaseDirectory);

            ZipOutputStream s = new ZipOutputStream(File.Create(strZipFile));

            foreach (string strDirectory in lstDirectories)
            {
                string[] filenames = Directory.GetFiles(strDirectory);

                Crc32 crc = new Crc32();
                s.SetLevel(6); // 0 - store only to 9 - means best compression

                foreach (string file in filenames)
                {
                    //打开压缩文件
                    FileStream fs = File.OpenRead(file);

                    byte[] buffer = new byte[fs.Length];
                    fs.Read(buffer, 0, buffer.Length);

                    int n = file.LastIndexOf('\\');
                    string strTmp1 = file.Substring(n);

                    //n = strDirectory.Substring(strBaseDirectory.Length);
                    string strTmp2 = strDirectory.Substring(strBaseDirectory.Length);

                    string strTmp3 = "";
                    if (strTmp2 != "")
                    {
                        strTmp3 = strTmp2 + "\\" + strTmp1;
                    }
                    else
                    {
                        strTmp3 = strTmp1;
                    }

                    ZipEntry entry = new ZipEntry(strTmp3);

                    entry.DateTime = DateTime.Now;

                    // set Size and the crc, because the information
                    // about the size and crc should be stored in the header
                    // if it is not set it is automatically written in the footer.
                    // (in this case size == crc == -1 in the header)
                    // Some ZIP programs have problems with zip files that don't store
                    // the size and crc in the header.
                    entry.Size = fs.Length;
                    fs.Close();

                    crc.Reset();
                    crc.Update(buffer);

                    entry.Crc = crc.Value;

                    s.PutNextEntry(entry);

                    s.Write(buffer, 0, buffer.Length);
                }
            }

            s.Finish();
            s.Close();
        }

        /// <summary>
        /// 压缩
        /// </summary>
        /// <param name="FileToZip"></param>
        /// <param name="ZipedFile"></param>
        /// <param name="CompressionLevel"></param>
        /// <param name="BlockSize"></param>
        public void ZipFile(string FileToZip, string ZipedFile, int CompressionLevel, int BlockSize)
        {
            //如果文件没有找到，则报错
            if (!System.IO.File.Exists(FileToZip))
            {
                throw new System.IO.FileNotFoundException("The specified file " + FileToZip + " could not be found. Zipping aborderd");
            }

            System.IO.FileStream StreamToZip = new System.IO.FileStream(FileToZip, System.IO.FileMode.Open, System.IO.FileAccess.Read);
            System.IO.FileStream ZipFile = System.IO.File.Create(ZipedFile);
            ZipOutputStream ZipStream = new ZipOutputStream(ZipFile);
            ZipEntry ZipEntry = new ZipEntry("ZippedFile");
            ZipStream.PutNextEntry(ZipEntry);
            ZipStream.SetLevel(CompressionLevel);
            byte[] buffer = new byte[BlockSize];
            System.Int32 size = StreamToZip.Read(buffer, 0, buffer.Length);
            ZipStream.Write(buffer, 0, size);

            try
            {
                while (size < StreamToZip.Length)
                {
                    int sizeRead = StreamToZip.Read(buffer, 0, buffer.Length);
                    ZipStream.Write(buffer, 0, sizeRead);
                    size += sizeRead;
                }
            }
            catch (System.Exception ex)
            {
                throw ex;
            }

            ZipStream.Finish();
            ZipStream.Close();
            StreamToZip.Close();
        }

        /// <summary>
        /// 
        /// </summary>
        public void GetEmailInfo()
        {
            //获取邮件发送所需基本参数
            IDictionary<string, NameValueCollection> tmpfile = iniFileDao.GetIniFile("email.ini");

            strSmtpServerInfo = tmpfile["email_config"]["server"];
            //strToInfo = tmpfile["email_config"]["to"];
            strFromInfo = tmpfile["email_config"]["from"];
            strFromUserInfo = tmpfile["email_config"]["user"];
            strFromPwdInfo = tmpfile["email_config"]["password"];

            if (strFromPwdInfo != null || strFromPwdInfo != "")
            {
                string[] txts = new string[1];
                txts[0] = strFromPwdInfo;

                IDictionary<string, NameValueCollection> valueDict = crypt.Decrypt(txts);
                if (valueDict.ContainsKey("return"))
                    strFromPwdInfo = valueDict["return"][txts[0]];
                else
                    strFromPwdInfo = "";

                //CryptDaoImpl crypt = new CryptDaoImpl();
                //string[] txts = { this.tbPassword.Text };
                //IDictionary<string, NameValueCollection> valueDict = crypt.Encrypt(txts);

                //this.userInfo["Password"] = valueDict["return"][txts[0]];

            }

            //strSmtpServerInfo = "mail.dragonflow.com";
            //strToInfo = "xingyu.cheng@dragonflow.com";
            //strFromInfo = "xingyu.cheng@dragonflow.com";
            //strFromUserInfo = "xingyu.cheng@dragonflow.com";
            //strFromPwdInfo = "xingyu.cheng";
        }

        /// <summary>
        /// 删除目录
        /// </summary>
        /// <param name="dir"></param>
        private void CleanFiles(string dir)
        {
            if (!Directory.Exists(dir))
            {
                File.Delete(dir);
                return;
            }
            else
            {
                string[] dirs = Directory.GetDirectories(dir);
                string[] files = Directory.GetFiles(dir);

                if (0 != dirs.Length)
                {
                    foreach (string subDir in dirs)
                    {
                        if (null == Directory.GetFiles(subDir))
                        {
                            Directory.Delete(subDir);
                            return;
                        }
                        else 
                            CleanFiles(subDir);
                    }
                }

                if (0 != files.Length)
                {
                    foreach (string file in files)
                    {
                        File.Delete(file);
                    }
                }
                else 
                    Directory.Delete(dir);
            }
        }

        #endregion


        public void DataTableToExcel(DataTable grid, string filename)
        {
            if (grid == null || string.IsNullOrEmpty(filename)) throw new ArgumentNullException();

            try
            {
                FileStream fs = new FileStream(filename, FileMode.Create, FileAccess.Write, FileShare.None);
                StreamWriter sw = new StreamWriter(fs);

                Encoding ascII = Encoding.Default;

                StringBuilder sb_Items = new StringBuilder();
                for (int i = 0; i < grid.Columns.Count; i++)
                {
                    sb_Items.Append(grid.Columns[i].Caption + ",");
                }
                sb_Items.Remove(sb_Items.Length - 1, 1);
                sb_Items.Append("\r\n");

                for (int i = 0; i < grid.Rows.Count; i++)
                {
                    for (int j = 0; j < grid.Columns.Count; j++)
                    {
                        string text = String.IsNullOrEmpty(grid.Rows[i][j].ToString()) == true ? "" : grid.Rows[i][j].ToString().Replace(',', '.');
                        sb_Items.Append(text.Replace(Environment.NewLine, "") + ",");
                    }
                    sb_Items.Remove(sb_Items.Length - 1, 1);
                    sb_Items.Append("\r\n");
                }

                Byte[] encodedBytes = ascII.GetBytes(sb_Items.ToString());
                fs.Write(encodedBytes, 0, encodedBytes.Length);

                fs.Flush();
                sw.Close();
                fs.Close();

                //MessageBox.Show("数据导出成功！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
            catch
            {
                MessageBox.Show("数据导出失败！", "提示", MessageBoxButtons.OK, MessageBoxIcon.Warning);
            }
        }

        /// <summary>
        /// 改变表的数据类型，以方便按INT排序。
        /// </summary>
        /// <param name="tb"></param>
        private void ChangeColumnType(DataTable tb)
        {
            for (int a = 0; a < tb.Rows.Count; a++)
            {
                DataRow dr = this.StatsTable.NewRow();
                for (int b = 0; b < tb.Columns.Count; b++)
                {
                    if (b == 5)
                    {
                        dr[b] = double.Parse(tb.Rows[a][b].ToString());
                    }
                    else
                    {
                        dr[b] = tb.Rows[a][b].ToString();
                    }
                }
                this.StatsTable.Rows.Add(dr);
            }
        }
    }
}
