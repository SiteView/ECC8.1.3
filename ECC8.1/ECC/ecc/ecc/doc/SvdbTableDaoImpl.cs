	using System;
	using System.Collections.Generic;
	using System.Collections.Specialized;
	using System.Text;
	using System.Data;
	
	using SiteView.Ecc.Core.Dao;
	using SiteView.Ecc.Core.Models;
	//using SiteView.Ecc.WSClient.EccWebService;
	using SiteView.Ecc.Core;
	using SiteView.Ecc.WSClient.XFireWebService;
	namespace SiteView.Ecc.WSClient
	{
	    public class SvdbTableDaoImpl : ISvdbTableDao
	    {
	        //private ECCWebService service = new ECCWebService();
	        private XFireWebService.InterfaceEccService service = Helper.service;
	        private log4net.ILog logger = log4net.LogManager.GetLogger(typeof(SvdbTableDaoImpl));
	        private DataTable ImageTable;
	        private DataTable primaryTable;
	        private DataTable measureTable;
	        private string headtitle;
	        public string HeadTitle
	        {
	            get { return headtitle; }
	            set { headtitle = value; }
	        }
	        private string name;
	        public string Name
	        {
	            get { return name; }
	            set { name = value; }
	        }
	
	        public DataTable ImgTable
	        {
	
	            get { return ImageTable; }
	            set { ImageTable = value; }
	        }
	        /// <summary>
	        /// 基础表
	        /// </summary>
	        public DataTable PrimaryTable
	        {
	            get { return this.primaryTable; }
	            set { this.primaryTable = value; }
	        }
	        /// <summary>
	        /// 统计表
	        /// </summary>
	        public DataTable MeasureTable
	        {
	            get { return this.measureTable; }
	            set { this.measureTable = value; }
	        }
	
	        #region ISvdbTableDao 成员
	
	        public DataTable CreateDataTable(MonitorTemplet templet, anyType2anyTypeMapEntry[][] data)
	        {
	            DataTable dt = new DataTable();
	            DataTable Imgdt = new DataTable();
	            NameValueCollection TextName = new NameValueCollection();
	            NameValueCollection ImgTextName = new NameValueCollection();
	            dt.Columns.Add(new DataColumn("时间", typeof(DateTime)));
	            dt.Columns.Add(new DataColumn("记录状态", typeof(String)));
	            dt.Columns.Add(new DataColumn("描述", typeof(String)));
	            dt.Columns.Add(new DataColumn("名称", typeof(String)));
	            Imgdt.Columns.Add(new DataColumn("时间", typeof(DateTime)));
	            Imgdt.Columns.Add(new DataColumn("记录状态", typeof(String)));
	            Imgdt.Columns.Add(new DataColumn("描述", typeof(String)));
	            TextName.Add("creat_time", "时间");
	            TextName.Add("record_status", "记录状态");
	            TextName.Add("dstr", "描述");
	            ImgTextName.Add("creat_time", "时间");
	            ImgTextName.Add("record_status", "记录状态");
	            ImgTextName.Add("dstr", "描述");
	            foreach (NameValueCollection returnItem in templet.Returns.Values)
	            {
	                ///bin.liusv_type
	                ///2008.8.11
	                if (returnItem.Get("sv_type") == null || returnItem["sv_type"] == null || returnItem["sv_type"] == string.Empty || returnItem["sv_type"].ToLower() == "string")
	                {
	                    continue;
	                }
	
	                dt.Columns.Add(new DataColumn(returnItem["sv_label"], typeof(Double)));
	                TextName.Add(returnItem["sv_name"], returnItem["sv_label"]);
	                if (returnItem.Get("sv_drawimage") != null)
	                {
	                    if (returnItem["sv_drawimage"] == "1")
	                    {
	                        ImgTextName.Add(returnItem["sv_name"], returnItem["sv_label"]);
	                        Imgdt.Columns.Add(new DataColumn(returnItem["sv_label"], typeof(Double)));
	                    }
	                }
	            }
	
	            DataRow dr = null;
	            IList<NameValueCollection> resultData = Helper.XfireConventMapEntryToList(data);
	
	            foreach (NameValueCollection nameValueCollection in resultData)
	            {
	                dr = dt.NewRow();
	                dr["名称"] = this.Name;
	                foreach (string key in nameValueCollection.AllKeys)
	                {
	                    string clumnname = TextName[key];
	                    if (clumnname != null && dt.Columns.Contains(clumnname))  //判断是否存在此列，否则有的情况会报错 add by huangyimei
	                    {
	                        if (dt.Columns[clumnname].DataType.Equals(typeof(Double)))
	                        {
	                            double value = 0;
	                            Double.TryParse(nameValueCollection[key], out value);
	                            dr[clumnname] = value;
	                        }
	                        else
	                        {
	                            dr[clumnname] = nameValueCollection[key];
	                        }
	                    }
	                }
	                dt.Rows.Add(dr);
	            }
	
	            /*
	            foreach (MapType map in data)
	            {
	                dr = dt.NewRow();
	                dr["名称"] = this.Name;
	                foreach (KeyValue keyvalue in map.Map)
	                {
	                    string clumnname = TextName[keyvalue.Key];
	                    if (dt.Columns[clumnname].DataType.Equals(typeof(Double)))
	                    {
	                        double value = 0;
	                        Double.TryParse(keyvalue.Value, out value);
	                        dr[clumnname] = value;
	                    }
	                    else
	                    {
	                        dr[clumnname] = keyvalue.Value;
	                    }
	                }
	                dt.Rows.Add(dr);
	            }
	             */
	
	            DataRow dr1 = null;
	            foreach (NameValueCollection nameValueCollection in resultData)
	            {
	                dr1 = Imgdt.NewRow();
	                foreach (string key in nameValueCollection.Keys)
	                {
	                    if (ImgTextName.Get(key) != null)
	                    {
	                        string clumnname = ImgTextName[key];
	                        if (Imgdt.Columns[clumnname].DataType.Equals(typeof(Double)))
	                        {
	                            double value = 0;
	                            Double.TryParse(nameValueCollection[key], out value);
	                            dr1[clumnname] = value;
	                        }
	                        else
	                        {
	                            dr1[clumnname] = nameValueCollection[key];
	                        }
	                    }
	                }
	                Imgdt.Rows.Add(dr1);
	            }
	
	
	            /*
	            DataRow dr1 = null;
	            foreach (MapType map in data)
	            {
	                dr1 = Imgdt.NewRow();
	                foreach (KeyValue keyvalue in map.Map)
	                {
	                    if (ImgTextName.Get(keyvalue.Key) != null)
	                    {
	                        string clumnname = ImgTextName[keyvalue.Key];
	                        if (Imgdt.Columns[clumnname].DataType.Equals(typeof(Double)))
	                        {
	                            double value = 0;
	                            Double.TryParse(keyvalue.Value, out value);
	                            dr1[clumnname] = value;
	                        }
	                        else
	                        {
	                            dr1[clumnname] = keyvalue.Value;
	                        }
	                    }
	                }
	                Imgdt.Rows.Add(dr1);
	            }
	            */
	
	            ImgTable = Imgdt;
	
	            return dt;
	        }
	
	        /// <summary>
	        /// 
	        /// </summary>
	        /// <param name="id"></param>
	        /// <param name="count"></param>
	        /// <returns></returns>
	        public DataTable Query(MonitorTemplet templet, string id, int count)
	        {
	            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
	            {
	                Helper.XfireCreateKeyValue("dowhat","QueryRecordsByCount"),
	                Helper.XfireCreateKeyValue("id",id),
	                Helper.XfireCreateKeyValue("count",count.ToString())
	            };
	            this.service.Url = UserPermissionContext.Instance.Url;
	            RetMapInVector result = ServiceClient.GetForestData(requestData);
	            //RetMapInVector result = this.service.GetForestData(requestData);
	
	            if (!result.retbool)
	            {
	                if (logger.IsWarnEnabled)
	                {
	                    logger.Warn(result.estr);
	                }
	                return null;
	            }
	
	            DataTable dt = CreateDataTable(templet, result.vmap);
	            return dt;
	        }
	
	        public DataTable Query(MonitorTemplet templet, string id, DateTime beginTime, DateTime endTime)
	        {
	            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
	            {
	                Helper.XfireCreateKeyValue("dowhat","QueryRecordsByTime"),
	                Helper.XfireCreateKeyValue("id",id),
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
	            this.service.Url = UserPermissionContext.Instance.Url;
	            RetMapInVector result = ServiceClient.GetForestData(requestData);
	            //RetMapInVector result = this.service.GetForestData(requestData);
	
	            if (!result.retbool)
	            {
	                if (logger.IsWarnEnabled)
	                {
	                    logger.Warn(result.estr);
	                }
	                return null;
	            }
	
	            DataTable dt = CreateDataTable(templet, result.vmap);
	            return dt;
	        }
	        #endregion
	
	        #region 生成基本统计
	        /// <summary>
	        /// 1
	        /// </summary>
	        /// <param name="templet"></param>
	        /// <param name="data"></param>
	        /// <returns></returns>
	        public DataTable CreateDataTable1(MonitorTemplet templet, anyType2anyTypeMapEntry[][] data)
	        {
	            DataTable Primarydt = new DataTable();
	            DataTable Measuredt = new DataTable();
	            DataTable Imgdt = new DataTable();
	            Imgdt.Columns.Add(new DataColumn("时间", typeof(DateTime)));
	            Imgdt.Columns.Add(new DataColumn("记录状态", typeof(String)));
	            Imgdt.Columns.Add(new DataColumn("描述", typeof(String)));
	            NameValueCollection ImgTextName = new NameValueCollection();
	            ImgTextName.Add("creat_time", "时间");
	            ImgTextName.Add("record_status", "记录状态");
	            ImgTextName.Add("dstr", "描述");
	            int j = 0;
	            foreach (NameValueCollection returnItem in templet.Returns.Values)
	            {
	                if (returnItem.Get("sv_drawimage") != null)
	                {
	                    if (j < 1)
	                    {
	                        if ((returnItem["sv_drawimage"] == "1") && (returnItem["sv_primary"] == "1"))
	                        {
	                            headtitle = returnItem["sv_label"];
	                            ImgTextName.Add(returnItem["sv_name"], returnItem["sv_label"]);
	                            Imgdt.Columns.Add(new DataColumn(returnItem["sv_label"], typeof(Double)));
	                            j = j + 1;
	                        }
	                    }
	                }
	            }
	
	
	            NameValueCollection PrimaryTextName = new NameValueCollection();
	            NameValueCollection MeasureTextName = new NameValueCollection();
	
	            Primarydt.Columns.Add(new DataColumn("名称", typeof(String)));
	            Primarydt.Columns.Add(new DataColumn("时间", typeof(DateTime)));
	            Primarydt.Columns.Add(new DataColumn("记录状态", typeof(String)));
	            PrimaryTextName.Add("creat_time", "时间");
	            PrimaryTextName.Add("record_status", "记录状态");
	
	            Measuredt.Columns.Add(new DataColumn("record_status", typeof(String)));
	            MeasureTextName.Add("record_status", "记录状态");
	
	            foreach (NameValueCollection returnItem in templet.Returns.Values)
	            {
	                if (returnItem.Get("sv_primary") != null)
	                {
	                    if (returnItem["sv_primary"] == "1")
	                    {
	                        PrimaryTextName.Add(returnItem["sv_name"], returnItem["sv_label"]);
	                        Primarydt.Columns.Add(new DataColumn(returnItem["sv_label"], typeof(Double)));
	                    }
	                }
	                // sv_drawmeasure
	                if (returnItem.Get("sv_drawimage") != null)
	                {
	                    if (returnItem["sv_drawimage"] == "1")
	                    {
	                        if (returnItem["sv_name"] != null)
	                        {
	                            MeasureTextName.Add(returnItem["sv_name"], returnItem["sv_label"]);
	                            Measuredt.Columns.Add(new DataColumn(returnItem["sv_name"], typeof(Double)));
	                        }
	                    }
	                }
	            }
	
	            DataRow dr = null;
	            IList<NameValueCollection> resultData = Helper.XfireConventMapEntryToList(data);
	
	
	            foreach (NameValueCollection nameValueCollection in resultData)
	            {
	                dr = Primarydt.NewRow();
	                dr["名称"] = string.IsNullOrEmpty(this.Name) ? "" : this.Name;
	                foreach (string key in nameValueCollection.AllKeys)
	                {
	                    if (PrimaryTextName.Get(key) != null)
	                    {
	                        string clumnname = PrimaryTextName[key];
	                        if (Primarydt.Columns[clumnname].DataType.Equals(typeof(Double)))
	                        {
	                            double value = 0;
	                            Double.TryParse(nameValueCollection[key], out value);
	                            dr[clumnname] = value;
	                        }
	                        else
	                        {
	                            dr[clumnname] = nameValueCollection[key];
	                        }
	                    }
	                }
	                Primarydt.Rows.Add(dr);
	            }
	            //foreach (MapType map in data)
	            //{
	            //    dr = Primarydt.NewRow();
	            //    dr["名称"] = string.IsNullOrEmpty(this.Name) ? "" : this.Name;
	            //    foreach (KeyValue keyvalue in map.Map)
	            //    {
	            //        if (PrimaryTextName.Get(keyvalue.Key) != null)
	            //        {
	            //            string clumnname = PrimaryTextName[keyvalue.Key];
	            //            if (Primarydt.Columns[clumnname].DataType.Equals(typeof(Double)))
	            //            {
	            //                double value = 0;
	            //                Double.TryParse(keyvalue.Value, out value);
	            //                dr[clumnname] = value;
	            //            }
	            //            else
	            //            {
	            //                dr[clumnname] = keyvalue.Value;
	            //            }
	            //        }
	            //    }
	            //    Primarydt.Rows.Add(dr);
	            //}
	            //foreach (NameValueCollection nameValueCollection in resultData)
	            //{
	            //    dr = dt.NewRow();
	            //    dr["名称"] = this.Name;
	            //    foreach (string key in nameValueCollection.AllKeys)
	            //    {
	            //        string clumnname = TextName[key];
	            //        if (dt.Columns[clumnname].DataType.Equals(typeof(Double)))
	            //        {
	            //            double value = 0;
	            //            Double.TryParse(nameValueCollection[key], out value);
	            //            dr[clumnname] = value;
	            //        }
	            //        else
	            //        {
	            //            dr[clumnname] = nameValueCollection[key];
	            //        }
	            //    }
	            //    dt.Rows.Add(dr);
	            //}
	
	
	
	            DataRow dr1 = null;
	            foreach (NameValueCollection nameValueCollection in resultData)
	            {
	                dr1 = Measuredt.NewRow();
	                for (int i = 0; i < Measuredt.Columns.Count; i++)
	                {
	                    dr1[i] = 0.00;
	                }
	                bool rowadd = false;
	                foreach (string key in nameValueCollection.AllKeys)
	                {
	                    if (MeasureTextName.Get(key) != null)
	                    {
	                        if (Measuredt.Columns[key].DataType.Equals(typeof(Double)))
	                        {
	                            double value = 0;
	                            rowadd = Double.TryParse(nameValueCollection[key], out value);
	                            dr1[key] = value;
	                        }
	                        else
	                        {
	                            dr1[key] = nameValueCollection[key];
	                        }
	                        //if (nameValueCollection[key].Equals("ok"))
	                        //{
	                        //    rowadd = true;
	                        //}
	                    }
	
	                }
	                if (rowadd)
	                {
	                    Measuredt.Rows.Add(dr1);
	                }
	            }
	
	
	            foreach (NameValueCollection nameValueCollection in resultData)
	            {
	                dr1 = Imgdt.NewRow();
	                foreach (string key in nameValueCollection.Keys)
	                {
	                    if (ImgTextName.Get(key) != null)
	                    {
	                        string clumnname = ImgTextName[key];
	                        if (Imgdt.Columns[clumnname].DataType.Equals(typeof(Double)))
	                        {
	                            double value = 0;
	                            Double.TryParse(nameValueCollection[key], out value);
	                            dr1[clumnname] = value;
	                        }
	                        else
	                        {
	                            dr1[clumnname] = nameValueCollection[key];
	                        }
	                    }
	                }
	                Imgdt.Rows.Add(dr1);
	            }
	
	            //DataRow dr1 = null;
	            //foreach (MapType map in data)
	            //{
	            //    dr1 = Measuredt.NewRow();
	            //    for (int i = 0; i < Measuredt.Columns.Count; i++)
	            //    {
	            //        dr1[i] = 0.00;
	            //    }
	            //    bool rowadd = false;
	            //    foreach (KeyValue keyvalue in map.Map)
	            //    {
	            //        if (MeasureTextName.Get(keyvalue.Key) != null)
	            //        {
	            //            if (Measuredt.Columns[keyvalue.Key].DataType.Equals(typeof(Double)))
	            //            {
	            //                double value = 0;
	            //                Double.TryParse(keyvalue.Value, out value);
	            //                dr1[keyvalue.Key] = value;
	            //            }
	            //            else
	            //            {
	            //                dr1[keyvalue.Key] = keyvalue.Value;
	            //            }
	            //            if (keyvalue.Value.Equals("ok"))
	            //            {
	            //                rowadd = true;
	            //            }
	            //        }
	
	            //    }
	            //    if (rowadd)
	            //    {
	            //        Measuredt.Rows.Add(dr1);
	            //    }
	            //}
	
	            if (Measuredt.Rows.Count == 0)
	            {
	                DataRow dr2 = null;
	                dr2 = Measuredt.NewRow();
	                foreach (string key in MeasureTextName.Keys)
	                {
	                    if (string.IsNullOrEmpty(key)) { continue; }
	                    if (Measuredt.Columns[key].DataType.Equals(typeof(Double)))
	                    {
	                        dr2[key] = 0.0;
	                    }
	                    else
	                    {
	                        dr2[key] = MeasureTextName[key];
	                    }
	                }
	                Measuredt.Rows.Add(dr2);
	            }
	
	            this.PrimaryTable = Primarydt;
	            this.MeasureTable = Measuredt;
	            this.ImgTable = Imgdt;
	            return Primarydt;
	        }
	        /// <summary>
	        /// 1
	        /// </summary>
	        /// <param name="templet"></param>
	        /// <param name="id"></param>
	        /// <param name="beginTime"></param>
	        /// <param name="endTime"></param>
	        /// <param name="tj"></param>
	        /// <returns></returns>
	        public DataTable Query1(MonitorTemplet templet, string id, DateTime beginTime, DateTime endTime)
	        {
	            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
	            {
	                Helper.XfireCreateKeyValue("dowhat","QueryRecordsByTime"),
	                Helper.XfireCreateKeyValue("id",id),
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
	            this.service.Url = UserPermissionContext.Instance.Url;
	            RetMapInVector result = ServiceClient.GetForestData(requestData);
	            //RetMapInVector result = this.service.GetForestData(requestData);
	
	            if (!result.retbool)
	            {
	                if (logger.IsWarnEnabled)
	                {
	                    logger.Warn(result.estr);
	                }
	                return null;
	            }
	
	            DataTable dt = CreateDataTable1(templet, result.vmap);
	            return dt;
	        }
	        /// <summary>
	        /// 
	        /// </summary>
	        /// <param name="id"></param>
	        /// <param name="count"></param>
	        /// <returns></returns>
	        public DataTable Query1(MonitorTemplet templet, string id, int count)
	        {
	            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
	            {
	                Helper.XfireCreateKeyValue("dowhat","QueryRecordsByCount"),
	                Helper.XfireCreateKeyValue("id",id),
	                Helper.XfireCreateKeyValue("count",count.ToString())
	            };
	            this.service.Url = UserPermissionContext.Instance.Url;
	            RetMapInVector result = ServiceClient.GetForestData(requestData);
	            //RetMapInVector result = this.service.GetForestData(requestData);
	
	            if (!result.retbool)
	            {
	                if (logger.IsWarnEnabled)
	                {
	                    logger.Warn(result.estr);
	                }
	                if (measureTable != null) measureTable.Clear();
	                if (ImageTable != null) ImageTable.Clear();
	                if (primaryTable != null) primaryTable.Clear();
	                return null;
	            }
	
	            DataTable dt = CreateDataTable1(templet, result.vmap);
	            return dt;
	        }
	
	        #endregion
	
	
	    }
	}
