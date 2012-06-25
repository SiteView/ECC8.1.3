package com.siteview.ecc.alert.dao;

import java.util.HashMap;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInMap;

public class QueueDaoImpl implements IQueueDao {

	@Override
	public void clearQueueMessage(String queuename) throws Exception{
		HashMap<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "ClearQueueMessage");
		ndata.put("queuename", queuename);
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception(ret.getEstr());
	}

	@Override
	public void createQueue(String queuename) throws Exception {
		HashMap<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "CreateQueue");
		ndata.put("queuename", queuename);
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception("Failed to createQueue :" + ret.getEstr());
	}

	@Override
	public void deleteQueue(String queuename) throws Exception{
		HashMap<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "DeleteQueue");
		ndata.put("queuename", queuename);
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception(ret.getEstr());
	}

	@Override
	public void getAllQueueNames() throws Exception{
		HashMap<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetAllQueueNames");
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception(ret.getEstr());
	}

	@Override
	public void getMQRecordCount(String queuename)throws Exception {
		HashMap<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "GetMQRecordCount");
		ndata.put("queuename", queuename);
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception(ret.getEstr());
	}

	@Override
	public boolean pushStringMessage(String queuename, String label,
			String content) throws Exception{
		HashMap<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "PushStringMessage");
		ndata.put("queuename", queuename);
		ndata.put("label", label);
		ndata.put("content", content);
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
        return ret.getRetbool();
	}

}
