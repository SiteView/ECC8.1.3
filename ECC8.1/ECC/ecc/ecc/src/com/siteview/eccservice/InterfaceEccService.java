package com.siteview.eccservice;

import java.util.Map;
import java.util.Vector;

import javax.jws.WebService;


@WebService(serviceName = "eccapi", targetNamespace = "http://www.siteview.com")
public interface InterfaceEccService
{
	/**
	 * 测试接口
	 */
	public String test1(String str);
	
	/**
	 * 测试接口
	 */
	public Map<String, String> test2(Map<String, Map<String, String>> fmap);

	
	public keyValue[] test3(keyValue[] inwhat);
	
	
	public RetMapInVector php_GetUnivData2(keyValue[] inwhat);
	public RetMapInVector php_SubmitUnivData2(keyValue[][] inlist, keyValue[] inwhat);
	public RetMapInVector php_GetForestData(keyValue[] inwhat);
	
	
	/**
	 * 此函数对应于 Jsvapi.GetUnivData
	 * 
	 * @return 从 ecc api(业务逻辑层)返回的数据 RetMapInMap
	 * @inwhat 传入的请求
	 * 
	 */
	public RetMapInMap GetUnivData(Map<String, String> inwhat);

	/**
	 * 此函数对应于 Jsvapi.GetUnivData
	 * 
	 * @return 从 ecc api(业务逻辑层)返回的数据 RetMapInMap
	 * @inwhat 传入的请求
	 * 
	 */
	public RetMapInVector GetUnivData2(Map<String, String> inwhat);
	
	/**
	 * 此函数对应于 Jsvapi.SubmitUnivData
	 * 
	 * @return 从 ecc api(业务逻辑层)返回的数据 RetMapInMap
	 * @fmap 要提交给 ecc api 的数据
	 * @inwhat 传入的请求
	 * 
	 */
	public RetMapInMap SubmitUnivData(Map<String, Map<String, String>> fmap, Map<String, String> inwhat);
	
	/**
	 * 此函数对应于 Jsvapi.SubmitUnivData
	 * 
	 * @return 从 ecc api(业务逻辑层)返回的数据 RetMapInMap
	 * @invmap 要提交给 ecc api 的数据
	 * @inwhat 传入的请求
	 * 
	 */
	public RetMapInVector SubmitUnivData2(Vector<Map<String, String>> invmap,Map<String, String> inwhat);
	
	/**
	 * 此函数对应于 Jsvapi.GetForestData
	 * 
	 * @return 从 ecc api(业务逻辑层)请求获得的树数据 RetMapInVector
	 * @inwhat 传入的请求
	 * 
	 */
	public RetMapInVector GetForestData(Map<String, String> inwhat);
	
	
	
}

