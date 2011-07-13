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
    /// 
    /// </summary>
    public class TaskDaoImpl : ITaskDao
    {
        //private ECCWebService service = new ECCWebService();
        private XFireWebService.InterfaceEccService service = Helper.service;
        private static log4net.ILog logger = log4net.LogManager.GetLogger(typeof(TaskDaoImpl));
        private static object lockObj = new object();

     #region ITaskDao ≥…‘±

        /// <summary>
        /// 
        /// </summary>
        /// <param name="task"></param>
        public Task FindByName(string name)
        {
            Task task = new Task();
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","GetTask"),
                Helper.XfireCreateKeyValue("id",name)
            };
            this.service.Url = UserPermissionContext.Instance.Url;
            RetMapInVector result = ServiceClient.GetUnivData2(requestData);
            IDictionary<string, NameValueCollection> resultData = Helper.XfireConventMapEntryToDictionary(result.vmap);

            task.Properties = resultData["property"];
            task.Name = name;

            return task;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public Task[] FindAll()
        {
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","GetAllTask")
            };
            this.service.Url = UserPermissionContext.Instance.Url;
            RetMapInVector result = ServiceClient.GetUnivData2(requestData);
            if (!result.retbool)
            {
                if (logger.IsErrorEnabled)
                {
                    logger.Error(result.estr);
                    throw new Exception(result.estr);
                }
            }

            IDictionary<string, NameValueCollection> resultData = Helper.XfireConventMapEntryToDictionary(result.vmap);
            Task[] tasks = new Task[resultData.Keys.Count - 1];
            int i = 0;
            foreach (string key in resultData.Keys)
            {
                if ("return".Equals(key))
                {
                    continue;
                }
                tasks[i] = new Task();
                tasks[i].Properties = resultData[key];
                tasks[i].Name = key;
                i++;
            }

            return tasks;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="taskType"></param>
        /// <returns></returns>
        public Task[] FindAll(Task.TaskType taskType)
        {
            Task[] allTasks = this.FindAll();
            IList<Task> newTasks = new List<Task>();
            foreach (Task task in allTasks)
            {
                if (task.Type == taskType)
                {
                    newTasks.Add(task);
                }
            }

            Task[] taskArr = new Task[newTasks.Count];
            newTasks.CopyTo(taskArr, 0);
            return taskArr;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="taskName"></param>
        public void DeleteTaskByName(string taskName)
        {
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","DeleteTask"),
                Helper.XfireCreateKeyValue("id",taskName)
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
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="task"></param>
        public void Save(Task task)
        {
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","CreateTask"),
                Helper.XfireCreateKeyValue("id",task.Name)
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

            this.Update(task);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="task"></param>
        public void Update(Task task)
        {
            anyType2anyTypeMapEntry[] requestData = new anyType2anyTypeMapEntry[]
            {
                Helper.XfireCreateKeyValue("dowhat","SubmitTask"),
                Helper.XfireCreateKeyValue("del_supplement","true")
            };

            IDictionary<string, NameValueCollection> taskData = new Dictionary<string, NameValueCollection>();
            taskData["property"] = task.Properties;
            taskData["return"] = new NameValueCollection();
            taskData["return"]["id"] = task.Name;
            this.service.Url=UserPermissionContext.Instance.Url;
            RetMapInVector result = ServiceClient.SubmitUnivData2(Helper.XfireConventDictionaryToMapEntry(taskData), requestData);
            if (!result.retbool)
            {
                if (logger.IsErrorEnabled)
                {
                    logger.Error(result.estr);
                }
                throw new Exception(result.estr);
            }
        }

       #endregion

       
    }
}
