package com.siteview.ecc.alert.dao;

public interface IQueueDao {
    /// <summary>
    /// 创建消息队列
    /// </summary>
    /// <param name="queuename"></param>
    void createQueue(String queuename)throws Exception;
    /// <summary>
    /// 发送信息
    /// </summary>
    /// <param name="queuename"></param>
    /// <param name="label"></param>
    /// <param name="content"></param>
    boolean pushStringMessage(String queuename, String label, String content)throws Exception;
    /// <summary>
    /// 
    /// </summary>
    /// <param name="queuename"></param>
    void getMQRecordCount(String queuename)throws Exception;
    /// <summary>
    /// 
    /// </summary>
    void getAllQueueNames()throws Exception;
    /// <summary>
    /// 
    /// </summary>
    /// <param name="queuename"></param>
    void deleteQueue(String queuename)throws Exception;
    /// <summary>
    /// 
    /// </summary>
    /// <param name="queuename"></param>
    void clearQueueMessage(String queuename)throws Exception;

}
