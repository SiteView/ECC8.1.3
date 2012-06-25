package com.siteview.ecc.alert.dao;

import java.util.HashMap;

import com.siteview.base.manage.View;

public interface IViewDataDao {
    /// <summary>
    /// 获取所有虚拟视图的名字，返回每个 view 的 viewName= XXX,  fileName= XXX,
    /// </summary>
    /// <returns></returns>
	HashMap<String, HashMap<String, String>> getAllView(View view)throws Exception;

}
