package com.siteview.eccservice;

import java.util.Map;
import java.util.Vector;

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
	public Map<String, String> test2(Map<String, Map<String, String>> fmap);

	
	public keyValue[] test3(keyValue[] inwhat);
	
	
	public RetMapInVector php_GetUnivData2(keyValue[] inwhat);
	public RetMapInVector php_SubmitUnivData2(keyValue[][] inlist, keyValue[] inwhat);
	public RetMapInVector php_GetForestData(keyValue[] inwhat);
	
	
	/**
	 * �˺�����Ӧ�� Jsvapi.GetUnivData
	 * 
	 * @return �� ecc api(ҵ���߼���)���ص����� RetMapInMap
	 * @inwhat ���������
	 * 
	 */
	public RetMapInMap GetUnivData(Map<String, String> inwhat);

	/**
	 * �˺�����Ӧ�� Jsvapi.GetUnivData
	 * 
	 * @return �� ecc api(ҵ���߼���)���ص����� RetMapInMap
	 * @inwhat ���������
	 * 
	 */
	public RetMapInVector GetUnivData2(Map<String, String> inwhat);
	
	/**
	 * �˺�����Ӧ�� Jsvapi.SubmitUnivData
	 * 
	 * @return �� ecc api(ҵ���߼���)���ص����� RetMapInMap
	 * @fmap Ҫ�ύ�� ecc api ������
	 * @inwhat ���������
	 * 
	 */
	public RetMapInMap SubmitUnivData(Map<String, Map<String, String>> fmap, Map<String, String> inwhat);
	
	/**
	 * �˺�����Ӧ�� Jsvapi.SubmitUnivData
	 * 
	 * @return �� ecc api(ҵ���߼���)���ص����� RetMapInMap
	 * @invmap Ҫ�ύ�� ecc api ������
	 * @inwhat ���������
	 * 
	 */
	public RetMapInVector SubmitUnivData2(Vector<Map<String, String>> invmap,Map<String, String> inwhat);
	
	/**
	 * �˺�����Ӧ�� Jsvapi.GetForestData
	 * 
	 * @return �� ecc api(ҵ���߼���)�����õ������� RetMapInVector
	 * @inwhat ���������
	 * 
	 */
	public RetMapInVector GetForestData(Map<String, String> inwhat);
	
	
	
}

