package com.siteview.ecc.alert.dao;

import java.util.HashMap;

import com.siteview.base.manage.View;

public interface IViewDataDao {
    /// <summary>
    /// ��ȡ����������ͼ�����֣�����ÿ�� view �� viewName= XXX,  fileName= XXX,
    /// </summary>
    /// <returns></returns>
	HashMap<String, HashMap<String, String>> getAllView(View view)throws Exception;

}
