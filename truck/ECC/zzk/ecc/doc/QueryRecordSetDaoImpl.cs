	using System;
	using System.Collections.Generic;
	using System.Collections.Specialized;
	using System.Text;
	using System.Data;
	
	using SiteView.Ecc.Core.Dao;
	using SiteView.Ecc.WSClient.XFireWebService;
	using SiteView.Ecc.Core;
	
	namespace SiteView.Ecc.WSClient
	{
	    public class QueryRecordSetDaoImpl : IQueryRecordSetDao
	    {
	        private InterfaceEccService service = new InterfaceEccService();
	        private log4net.ILog logger = log4net.LogManager.GetLogger(typeof(QueryRecordSetDaoImpl));
	 
	        public DataTable QueryRecordsByTime(
	            string id,
	            string begin_year,
	            string begin_month,
	            string begin_day,
	            string begin_hour,
	            string begin_minute,
	            string begin_second,
	            string end_year,
	            string end_month,
	            string end_day,
	            string end_hour,
	            string end_minute,
	            string end_second)
	        {
	            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[14];
	            requestData[0] = Helper.XfireCreateKeyValue("dowhat", "QueryRecordsByTime");
	            requestData[1] = Helper.XfireCreateKeyValue("id", id);
	            requestData[2] = Helper.XfireCreateKeyValue("begin_year", begin_year);
	            requestData[3] = Helper.XfireCreateKeyValue("begin_month", begin_month);
	            requestData[4] = Helper.XfireCreateKeyValue("begin_day", begin_day);
	            requestData[5] = Helper.XfireCreateKeyValue("begin_hour", begin_hour);
	            requestData[6] = Helper.XfireCreateKeyValue("begin_minute", begin_minute);
	            requestData[7] = Helper.XfireCreateKeyValue("begin_second", begin_second);
	
	            requestData[8] = Helper.XfireCreateKeyValue("end_year", end_year);
	            requestData[9] = Helper.XfireCreateKeyValue("end_month", end_month);
	            requestData[10] = Helper.XfireCreateKeyValue("end_day", end_day);
	            requestData[11] = Helper.XfireCreateKeyValue("end_hour", end_hour);
	            requestData[12] = Helper.XfireCreateKeyValue("end_minute", end_minute);
	            requestData[13] = Helper.XfireCreateKeyValue("end_second", end_second);
	
	            this.service.Url = UserPermissionContext.Instance.Url;
	            RetMapInVector result = ServiceClient.GetForestData(requestData);
	            //RetMapInVector result = this.service.GetForestData(requestData);
	            if (!result.retbool)
	            {
	                if (logger.IsErrorEnabled)
	                {
	                    logger.Error(result.estr);
	                }
	                // ≤‚ ‘ ˝æ›
	//#if DEBUG
	//                DataTable dttemp = new DataTable();
	//                dttemp.Columns.Add("No.");
	//                dttemp.Columns.Add("CreateTime");
	//                dttemp.Columns.Add("RecordState");
	//                dttemp.Columns.Add("_SysLogIndex");
	//                dttemp.Columns.Add("_SysLogTime");
	//                dttemp.Columns.Add("_SourceIp");
	//                dttemp.Columns.Add("_SysLogMsg");
	//                dttemp.Columns.Add("_Facility");
	//                dttemp.Columns.Add("_Level");
	//                dttemp.Columns.Add("dstr");
	//                for (int i = 1; i < 220; i++)
	//                {
	//                    DataRow row = dttemp.NewRow();
	//                    row["No."] = i.ToString();
	//                    row["CreateTime"] = "1";
	//                    row["RecordState"] = "1";
	//                    row["_SysLogIndex"] = "1";
	//                    row["_SysLogTime"] = "1";
	//                    row["_SourceIp"] = "192.168.0." + i.ToString();
	//                    row["_SysLogMsg"] = "1";
	//                    row["_Facility"] = "1";
	//                    row["_Level"] = "1";
	//                    row["dstr"] = "1";
	//                    dttemp.Rows.Add(row);
	//                }
	//                return dttemp;
	//#endif
	                return null;
	
	                // ≤‚ ‘Ω· ¯
	                //throw new Exception(result.estr);
	            }
	
	            //IDictionary<string, NameValueCollection> dict = Helper.XfireConventMapEntryToDictionary(result.vmap);
	
	            return CreateDataTable(id, result.vmap);
	        }
	
	        public DataTable QueryRecordsByCount(string id, string count)
	        {
	            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[3];
	            requestData[0] = Helper.XfireCreateKeyValue("dowhat", "QueryRecordsByCount");
	            requestData[1] = Helper.XfireCreateKeyValue("id", id);
	            requestData[2] = Helper.XfireCreateKeyValue("count", count);
	
	            this.service.Url = UserPermissionContext.Instance.Url;
	            RetMapInVector result = ServiceClient.GetForestData(requestData);
	            //RetMapInVector result = this.service.GetForestData(requestData);
	            if (!result.retbool)
	            {
	                if (logger.IsErrorEnabled)
	                {
	                    logger.Error(result.estr);
	                }
	                throw new Exception(result.estr);
	            }
	
	            
	            return CreateDataTable(id, result.vmap);
	        }
	
	        private DataTable CreateDataTable(string tableId, anyType2anyTypeMapEntry[][] data)
	        {
	            try
	            {
	                if (data == null) return null;
	
	                int lb1 = data.GetLowerBound(0);
	                int ub1 = data.GetUpperBound(0);
	                int lb2 = data[0].GetLowerBound(0);
	                int ub2 = data[0].GetUpperBound(0);
	
	                //IDictionary<string, NameValueCollection> dict = Helper.XfireConventMapEntryToDictionary(data);
	
	                DataTable dt = new DataTable();
	                string fieldNames = "";
	                switch (tableId.ToLower())
	                {
	                    case "syslog":
	                        {
	                            fieldNames = "No.,CreateTime,RecordState,_SysLogIndex,_SysLogTime,_SourceIp,_SysLogMsg,_Facility,_Level,dstr";
	                            dt.Columns.Add("No.");
	                            dt.Columns.Add("CreateTime");
	                            dt.Columns.Add("RecordState");
	                            dt.Columns.Add("_SysLogIndex");
	                            dt.Columns.Add("_SysLogTime");
	                            dt.Columns.Add("_SourceIp");
	                            dt.Columns.Add("_SysLogMsg");
	                            dt.Columns.Add("_Facility");
	                            dt.Columns.Add("_Level");
	                            dt.Columns.Add("dstr");
	                            break;
	                        }
	                    case "useroperatelog":
	                        {
	                            fieldNames = "No.,CreateTime,RecordState,_UserID,_OperateTime,_OperateType,_OperateObjName,_OperateObjInfo,dstr";
	                            dt.Columns.Add("No.");
	                            dt.Columns.Add("CreateTime");
	                            dt.Columns.Add("RecordState");
	                            dt.Columns.Add("_UserID");
	                            dt.Columns.Add("_OperateTime");
	                            dt.Columns.Add("_OperateType");
	                            dt.Columns.Add("_OperateObjName");
	                            dt.Columns.Add("_OperateObjInfo");
	                            dt.Columns.Add("dstr");
	                            break;
	                        }
	                }
	
	                List<string> keyList = new List<string>();
	
	                for (int i = lb2; i <= ub2; i++)
	                {
	                    if (!keyList.Contains(data[0][i].key.ToString()))
	                    {
	                        keyList.Add(data[0][i].key.ToString());
	                    }
	                }
	
	                for (int j = lb1; j <= ub1; j++)
	                {
	                    DataRow row = dt.NewRow();
	                    for (int k = lb2; k <= ub2; k++)
	                    {
	
	                        if (fieldNames.IndexOf(keyList[k]) >= 0)
	                        {
	                            row[keyList[k]] = data[j][k].value.ToString();
	                        }
	                    }
	                    dt.Rows.Add(row);
	                }
	
	                //foreach (string key1 in dict.Keys)
	                //{
	                //    NameValueCollection nvc = dict[key1];
	                //    foreach (string key in nvc.Keys)
	                //    {
	                //        if (fieldNames.IndexOf(key) >= 0)
	                //        {
	                //            DataRow row = dt.NewRow();
	                //            row[key] = nvc[key];
	                //            dt.Rows.Add(row);
	                //        }
	                //    }
	                //}
	
	                return dt;
	            }
	            catch (Exception ex)
	            {
	                return null;
	            }
	        }
	    }
	}
