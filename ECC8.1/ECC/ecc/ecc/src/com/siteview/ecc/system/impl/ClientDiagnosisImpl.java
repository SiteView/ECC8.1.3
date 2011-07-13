package com.siteview.ecc.system.impl;

import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.siteview.ecc.system.Diagnosis;

public class ClientDiagnosisImpl extends Diagnosis {
	public static final int BROWSER_TYPE_MSIE = 0;
	public static final int BROWSER_TYPE_FIREFOX = 1;
	public static final int BROWSER_TYPE_OPERA = 2;
	public static final int BROWSER_TYPE_MOZILLA  = 3;
	public static final int BROWSER_TYPE_WEBTV = 4;
	public static final int BROWSER_TYPE_CHROME = 5;
	public static final int BROWSER_TYPE_UNKNOWN = -1;
	
	private HttpServletRequest request = null;
	private String agent = null;
	private Map<String,Object> map = new LinkedHashMap<String,Object>();
	public ClientDiagnosisImpl(HttpServletRequest request){
		this.request = request;
		agent = this.request.getHeader("User-Agent");

		if (request.getRemoteAddr()!=null)map.put("Request RemoteAddr" , request.getRemoteAddr());
		if (request.getRemotePort() > 0)map.put("Request RemotePort" , request.getRemotePort());
		if (request.getMethod()!=null) map.put("Request Method" , request.getMethod());
		if (request.getRequestURI()!=null)map.put("Request URI" , request.getRequestURI());
		if (request.getProtocol()!=null)map.put("Request Protocol" , request.getProtocol());

		Enumeration<?> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			Object headerName = headerNames.nextElement();
			if (headerName == null || !(headerName instanceof String)) continue;
			map.put((String)headerName, request.getHeader((String)headerName));
		}
	}

	@Override
	public void execute() throws Exception {

		for (String key : map.keySet()){
			getResultList().add(key + ":" + map.get(key));
		}
//		if (getBrowserType() == BROWSER_TYPE_MSIE && agent.indexOf("6.0") != -1) {
//			throw new Exception("微软浏览器IE6需要升级到IE7.0以上");
//		}
		if (getBrowserType() == BROWSER_TYPE_MSIE ) {
			if(agent.indexOf("MSIE 6.0")!=-1){
				throw new Exception("微软浏览器IE6.0需要升级到IE7.0以上");
			}
		}
	}

	
	public int getBrowserType() {
		if (agent == null) return BROWSER_TYPE_UNKNOWN;
		agent = agent.toLowerCase();
		if (agent.indexOf("msie") > -1) {
			return BROWSER_TYPE_MSIE;
		} else if (agent.indexOf("firefox") > -1) {
			return BROWSER_TYPE_FIREFOX;
		} else if (agent.indexOf("opera") > -1) {
			return BROWSER_TYPE_OPERA;
		} else if (agent.indexOf("chrome") > -1) {
			return BROWSER_TYPE_CHROME;
		} else if (agent.indexOf("webtv") > -1) {
			return BROWSER_TYPE_WEBTV;
		}else if (agent.indexOf("mozilla") > -1) {
				return BROWSER_TYPE_MOZILLA;
		}
		return BROWSER_TYPE_UNKNOWN;
	}

	@Override
	public String getDescription() {
		return "检测客户端浏览器的配置是否合乎要求";
	}

	@Override
	public String getName() {
		return "客户端检测";
	}

}
