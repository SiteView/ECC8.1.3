package com.siteview.axis2;

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
	public KeyValue[] test2(AnyType2AnyTypeMapEntry[] fmap);

	
	public KeyValue[] test3(KeyValue[] inwhat);
	
	
	public RetMapInVector php_GetUnivData2(KeyValue[] inwhat);
	public RetMapInVector php_SubmitUnivData2(KeyValueArray[] inlist, KeyValue[] inwhat);
	public RetMapInVector php_GetForestData(KeyValue[] inwhat);
	
	
	/**
	 * 此函数对应于 Jsvapi.GetUnivData
	 * 
	 * @return 从 ecc api(业务逻辑层)返回的数据 RetMapInMap
	 * @inwhat 传入的请求
	 * 
	 */
	public RetMapInMap GetUnivData(KeyValue[] inwhat);

	/**
	 * 此函数对应于 Jsvapi.GetUnivData
	 * 
	 * @return 从 ecc api(业务逻辑层)返回的数据 RetMapInMap
	 * @inwhat 传入的请求
	 * 
	 */
	public RetMapInVector GetUnivData2(KeyValue[] inwhat);
	
	/**
	 * 此函数对应于 Jsvapi.SubmitUnivData
	 * 
	 * @return 从 ecc api(业务逻辑层)返回的数据 RetMapInMap
	 * @fmap 要提交给 ecc api 的数据
	 * @inwhat 传入的请求
	 * 
	 */
	public RetMapInMap SubmitUnivData(AnyType2AnyTypeMapEntry[] fmap, KeyValue[] inwhat);
	
	/**
	 * 此函数对应于 Jsvapi.SubmitUnivData
	 * 
	 * @return 从 ecc api(业务逻辑层)返回的数据 RetMapInMap
	 * @invmap 要提交给 ecc api 的数据
	 * @inwhat 传入的请求
	 * 
	 */
	public RetMapInVector SubmitUnivData2(KeyValueArray[] invmap,KeyValue[] inwhat);
	
	/**
	 * 此函数对应于 Jsvapi.GetForestData
	 * 
	 * @return 从 ecc api(业务逻辑层)请求获得的树数据 RetMapInVector
	 * @inwhat 传入的请求
	 * 
	 */
	public RetMapInVector GetForestData(KeyValue[] inwhat);
	
	
	
}

