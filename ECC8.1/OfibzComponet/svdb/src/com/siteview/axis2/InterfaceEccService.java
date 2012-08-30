package com.siteview.axis2;

import javax.jws.WebService;


@WebService(serviceName = "eccapi", targetNamespace = "http://www.siteview.com")
public interface InterfaceEccService
{
	/**
	 * ���Խӿ�
	 */
	public String test1(String str);
	
	/**
	 * ���Խӿ�
	 */
	public KeyValue[] test2(AnyType2AnyTypeMapEntry[] fmap);

	
	public KeyValue[] test3(KeyValue[] inwhat);
	
	
	public RetMapInVector php_GetUnivData2(KeyValue[] inwhat);
	public RetMapInVector php_SubmitUnivData2(KeyValueArray[] inlist, KeyValue[] inwhat);
	public RetMapInVector php_GetForestData(KeyValue[] inwhat);
	
	
	/**
	 * �˺�����Ӧ�� Jsvapi.GetUnivData
	 * 
	 * @return �� ecc api(ҵ���߼���)���ص����� RetMapInMap
	 * @inwhat ���������
	 * 
	 */
	public RetMapInMap GetUnivData(KeyValue[] inwhat);

	/**
	 * �˺�����Ӧ�� Jsvapi.GetUnivData
	 * 
	 * @return �� ecc api(ҵ���߼���)���ص����� RetMapInMap
	 * @inwhat ���������
	 * 
	 */
	public RetMapInVector GetUnivData2(KeyValue[] inwhat);
	
	/**
	 * �˺�����Ӧ�� Jsvapi.SubmitUnivData
	 * 
	 * @return �� ecc api(ҵ���߼���)���ص����� RetMapInMap
	 * @fmap Ҫ�ύ�� ecc api ������
	 * @inwhat ���������
	 * 
	 */
	public RetMapInMap SubmitUnivData(AnyType2AnyTypeMapEntry[] fmap, KeyValue[] inwhat);
	
	/**
	 * �˺�����Ӧ�� Jsvapi.SubmitUnivData
	 * 
	 * @return �� ecc api(ҵ���߼���)���ص����� RetMapInMap
	 * @invmap Ҫ�ύ�� ecc api ������
	 * @inwhat ���������
	 * 
	 */
	public RetMapInVector SubmitUnivData2(KeyValueArray[] invmap,KeyValue[] inwhat);
	
	/**
	 * �˺�����Ӧ�� Jsvapi.GetForestData
	 * 
	 * @return �� ecc api(ҵ���߼���)�����õ������� RetMapInVector
	 * @inwhat ���������
	 * 
	 */
	public RetMapInVector GetForestData(KeyValue[] inwhat);
	
	
	
}

