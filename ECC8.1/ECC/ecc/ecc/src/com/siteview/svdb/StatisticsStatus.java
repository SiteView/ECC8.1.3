package com.siteview.svdb;

import java.util.Map;

import javax.jws.WebService;

@WebService(name = "eccstatapi", targetNamespace = "http://com.siteview.cxf")
public interface StatisticsStatus {
	public Map<String,Integer> getStatisticsStatus(String username,String password,String id) throws Exception;
}
