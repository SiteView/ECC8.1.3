package com.siteview.cxf;

import java.util.List;
import java.util.Map;

import javax.jws.WebService;


@WebService(name = "eccapi", targetNamespace = "http://com.siteview.cxf")
public interface EccService {
	/**
	 * ���Խӿ�
	 */
	public String test1(String str);
	
	/**
	 * ���Խӿ�
	 */
	public Map<String,String> test2(Map<String,Map<String,String>> fmap);

	
	public Map<String,String> test3(Map<String,String> inwhat);
	
	
	public RetMapInVector php_GetUnivData2(Map<String,String> inwhat);
	public RetMapInVector php_SubmitUnivData2(List<Map<String,String>> inlist, Map<String,String> inwhat);
	public RetMapInVector php_GetForestData(Map<String,String> inwhat);
	
	
	/**
	 * �˺�����Ӧ�� Jsvapi.GetUnivData
	 * 
	 * @return �� ecc api(ҵ���߼���)���ص����� RetMapInMap
	 * @inwhat ���������
	 * 
	 */
	public RetMapInMap GetUnivData(Map<String,String> inwhat);

	/**
	 * �˺�����Ӧ�� Jsvapi.GetUnivData
	 * 
	 * @return �� ecc api(ҵ���߼���)���ص����� RetMapInMap
	 * @inwhat ���������
	 * 
	 */
	public RetMapInVector GetUnivData2(Map<String,String> inwhat);
	
	/**
	 * �˺�����Ӧ�� Jsvapi.SubmitUnivData
	 * 
	 * @return �� ecc api(ҵ���߼���)���ص����� RetMapInMap
	 * @fmap Ҫ�ύ�� ecc api ������
	 * @inwhat ���������
	 * 
	 */
	public RetMapInMap SubmitUnivData(Map<String,Map<String,String>> fmap, Map<String,String> inwhat);
	
	/**
	 * �˺�����Ӧ�� Jsvapi.SubmitUnivData
	 * 
	 * @return �� ecc api(ҵ���߼���)���ص����� RetMapInMap
	 * @invmap Ҫ�ύ�� ecc api ������
	 * @inwhat ���������
	 * 
	 */
	public RetMapInVector SubmitUnivData2(List<Map<String,String>> invmap,Map<String,String> inwhat);
	
	/**
	 * �˺�����Ӧ�� Jsvapi.GetForestData
	 * 
	 * @return �� ecc api(ҵ���߼���)�����õ������� RetMapInVector
	 * @inwhat ���������
	 * 
	 */
	public RetMapInVector GetForestData(Map<String,String> inwhat);
}
