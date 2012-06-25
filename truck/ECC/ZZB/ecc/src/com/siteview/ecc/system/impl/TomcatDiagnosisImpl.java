package com.siteview.ecc.system.impl;

import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;

import com.siteview.ecc.system.Diagnosis;

public class TomcatDiagnosisImpl extends Diagnosis {
	private final static Map<String, String> map = new LinkedMap();
	
	public TomcatDiagnosisImpl(){
		if (map.size()==0){
			map.put("os.name","����ϵͳ������" );
			map.put("sun.os.patch.level","����ϵͳ�汾");
			map.put("java.version","Java ����ʱ�����汾");
			map.put("java.home","Java ��װĿ¼" );
			map.put("java.class.path","Java ��·��" );
			map.put("java.library.path","���ؿ�ʱ������·���б� ");
			map.put("java.io.tmpdir","Ĭ�ϵ���ʱ�ļ�·��" );
			map.put("user.name","�û����˻�����");
			map.put("user.home","�û�����Ŀ¼");
			map.put("user.dir","�û��ĵ�ǰ����Ŀ¼");
		}
	}
	

	@Override
	public String getDescription() {
		return "���������в����Ƿ����ϵͳ����Ҫ��.";
	}

	@Override
	public String getName() {
		return "WebEcc HttpServer���";
	}

	@Override
	public void execute() throws Exception {
		for (String key : map.keySet()){
			String value = System.getProperty(key);
			getResultList().add(map.get(key) + " : " + value);
		}


		long totalMemory = Runtime.getRuntime().totalMemory();
		long maxMemory = Runtime.getRuntime().maxMemory();
		long freeMemory = Runtime.getRuntime().freeMemory();
		long usedMemory = totalMemory - freeMemory;

		getResultList().add("�����  Tomcat ���ڴ�:" + totalMemory / 1024 / 1024 + "M");
		getResultList().add("Tomcat ������ʹ���ڴ�:" + maxMemory / 1024 / 1024 + "M");
		getResultList().add("Tomcat �Ѿ�ʹ�õ��ڴ�:" + usedMemory / 1024 / 1024 + "M");
		getResultList().add("Tomcat ʣ����ڴ�:" + freeMemory / 1024 / 1024 + "M");
		
		if (OS.isWinNT() == false)
			throw new Exception("ϵͳ������ Windows NT platform (Windows NT/2000/2003/XP)��ʹ��!");
		if ((maxMemory / 1024 / 1024) < 500)
			throw new Exception("Tomcat ������ʹ���ڴ��Ƽ�����500M!");
		if ((usedMemory / 1024 / 1024) > 128)
			throw new Exception("Tomcat �Ѿ�ʹ�õ��ڴ�̫����!");

	}

}
