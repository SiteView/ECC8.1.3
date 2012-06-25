package com.siteview.ecc.alert.dao;

import java.util.Map;

public interface ITextTemplateDao {
    /// <summary>
    /// 
    /// </summary>
    /// <returns></returns>
    Map<String, String> getMailTemplet();

    /// <summary>
    /// 
    /// </summary>
    /// <returns></returns>
    Map<String, String> getScriptTemplet();

    /// <summary>
    /// 
    /// </summary>
    /// <returns></returns>
    Map<String, String> getSmsTemplet();
    Map<String, String> getWebSmsTemplet();

}
