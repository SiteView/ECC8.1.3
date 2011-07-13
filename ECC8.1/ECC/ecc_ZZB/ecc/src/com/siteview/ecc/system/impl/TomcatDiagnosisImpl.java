package com.siteview.ecc.system.impl;

import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;

import com.siteview.ecc.system.Diagnosis;

public class TomcatDiagnosisImpl extends Diagnosis {
	private final static Map<String, String> map = new LinkedMap();
	
	public TomcatDiagnosisImpl(){
		if (map.size()==0){
			map.put("os.name","操作系统的名称" );
			map.put("sun.os.patch.level","操作系统版本");
			map.put("java.version","Java 运行时环境版本");
			map.put("java.home","Java 安装目录" );
			map.put("java.class.path","Java 类路径" );
			map.put("java.library.path","加载库时搜索的路径列表 ");
			map.put("java.io.tmpdir","默认的临时文件路径" );
			map.put("user.name","用户的账户名称");
			map.put("user.home","用户的主目录");
			map.put("user.dir","用户的当前工作目录");
		}
	}
	

	@Override
	public String getDescription() {
		return "检测各项运行参数是否符合系统运行要求.";
	}

	@Override
	public String getName() {
		return "WebEcc HttpServer检测";
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

		getResultList().add("分配给  Tomcat 的内存:" + totalMemory / 1024 / 1024 + "M");
		getResultList().add("Tomcat 的最大可使用内存:" + maxMemory / 1024 / 1024 + "M");
		getResultList().add("Tomcat 已经使用的内存:" + usedMemory / 1024 / 1024 + "M");
		getResultList().add("Tomcat 剩余的内存:" + freeMemory / 1024 / 1024 + "M");
		
		if (OS.isWinNT() == false)
			throw new Exception("系统必须在 Windows NT platform (Windows NT/2000/2003/XP)下使用!");
		if ((maxMemory / 1024 / 1024) < 500)
			throw new Exception("Tomcat 的最大可使用内存推荐大于500M!");
		if ((usedMemory / 1024 / 1024) > 128)
			throw new Exception("Tomcat 已经使用的内存太多了!");

	}

}
