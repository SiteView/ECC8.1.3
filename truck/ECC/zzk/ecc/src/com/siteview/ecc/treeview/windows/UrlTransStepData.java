package com.siteview.ecc.treeview.windows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.siteview.base.manage.ManageSvapi;
import com.siteview.base.manage.RetMapInMap;

public class UrlTransStepData
{
	public Map<String, Map<String, String>>	m_fmap		= new HashMap<String, Map<String, String>>();
	public UrlTransStepData()
	{
		
	}
	public List<UrlTransBean> GetAllUrlSteps(String monitorID) throws Exception
	{
		List<UrlTransBean> urltranslist=new ArrayList<UrlTransBean>();
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "StepUrlSetup");
		ndata.put("monitorID", monitorID);
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception("Failed to load :" + ret.getEstr());
		m_fmap = ret.getFmap();
		if (m_fmap.containsKey("return"))
			m_fmap.remove("return");
		int c=m_fmap.keySet().size()+1;
		UrlTransBean bean1=null;
		for(int i=1;i<c;i++)
		{
			bean1=new UrlTransBean();
			String key="urlStep"+Integer.toString(i);
			Map<String, String> stepdata=m_fmap.get(key);
			String index=stepdata.get("stepIndex");
			String stepname=stepdata.get("stepName");
			String steptype=stepdata.get("stepType");
			bean1.setStepindex(index);
			bean1.setStepname(stepname);
			bean1.setSteptype(steptype);
			urltranslist.add(bean1);
		}
		return urltranslist;
		
	}
	public UrlTransBean AddUrlStep(String monitorID, String stepindex) throws Exception
	{
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "AddUrlStep");
		ndata.put("monitorID", monitorID);
		ndata.put("editIndex", stepindex);
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception("Failed to load :" + ret.getEstr());
		m_fmap = ret.getFmap();
		if (m_fmap.containsKey("return"))
			m_fmap.remove("return");
		Map<String, String> rdata = new HashMap<String, String>();
		rdata=m_fmap.get("urlStep");
		UrlTransBean bean=new UrlTransBean();
		bean.setStepindex(rdata.get("stepIndex"));
		bean.setStepname(rdata.get("stepName"));
		bean.setSteptype(rdata.get("stepType"));
		bean.setPostData(rdata.get("postData"));
		bean.setStepContent(rdata.get("stepContent"));
		bean.setStepErrorContent(rdata.get("stepErrorContent"));
		bean.setLinks(rdata.get("links"));
		bean.setUrl(rdata.get("url"));
		bean.setForms(rdata.get("forms"));
		bean.setStepUserName(rdata.get("stepUserName"));
		bean.setStepUserPwd(rdata.get("stepUserPwd"));
		return bean;
	}
	
	public UrlTransBean EditUrlStep(String monitorID, String stepindex) throws Exception
	{
		Map<String, String> ndata = new HashMap<String, String>();
		ndata.put("dowhat", "EditUrlStep");
		ndata.put("monitorID", monitorID);
		ndata.put("editIndex", stepindex);
		RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
		if (!ret.getRetbool())
			throw new Exception("Failed to load :" + ret.getEstr());
		m_fmap = ret.getFmap();
		if (m_fmap.containsKey("return"))
			m_fmap.remove("return");
		Map<String, String> rdata = new HashMap<String, String>();
		rdata=m_fmap.get("urlStep");
		UrlTransBean bean=new UrlTransBean();
		bean.setStepindex(rdata.get("stepIndex"));
		bean.setStepname(rdata.get("stepName"));
		bean.setSteptype(rdata.get("stepType"));
		bean.setPostData(rdata.get("postData"));
		bean.setStepContent(rdata.get("stepContent"));
		bean.setStepErrorContent(rdata.get("stepErrorContent"));
		bean.setLinks(rdata.get("links"));
		bean.setUrl(rdata.get("url"));
		bean.setForms(rdata.get("forms"));
		bean.setStepUserName(rdata.get("stepUserName"));
		bean.setStepUserPwd(rdata.get("stepUserPwd"));
		return bean;
	}
	
	  public Boolean SaveUrlStep(String monitorID,UrlTransBean savestep)
	  {
		    Map<String, String> ndata = new HashMap<String, String>();
			ndata.put("dowhat", "SaveUrlStep");
			ndata.put("monitorID",monitorID );
			ndata.put("saveIndex", savestep.getStepindex());
			ndata.put("referenceName", savestep.getStepname());
			ndata.put("referenceType", savestep.getSteptype());
			ndata.put("stepContent", savestep.getStepContent());
			ndata.put("stepErrorContent", savestep.getStepErrorContent());
			ndata.put("stepUserName", savestep.getStepUserName());
			ndata.put("stepUserPwd", savestep.getStepUserPwd());
			RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
			if (!ret.getRetbool())
				return false;
		  return true;
	  }
	  
	  public Boolean  DeleteStep(String editindex, String monitorID)
	  {
		  Map<String, String> ndata = new HashMap<String, String>();
			ndata.put("dowhat", "DelUrlStep");
			ndata.put("monitorID",monitorID );
			ndata.put("delIndex", editindex);
			RetMapInMap ret = ManageSvapi.GetUnivData(ndata);
			if (!ret.getRetbool())
				return false;
		  return true;
	  }
}
