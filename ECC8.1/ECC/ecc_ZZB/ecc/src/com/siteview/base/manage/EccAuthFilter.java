package com.siteview.base.manage;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sun.misc.BASE64Decoder;

public class EccAuthFilter implements Filter {
	private FilterConfig config;

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		String requestUri = ((HttpServletRequest)request).getRequestURI();
		if (requestUri!=null){				
			if(requestUri.contains("/ecc/zkau/view/g4dl/")){
				return;
			}
			if (requestUri.startsWith("/ecc/zkau/") || (requestUri.startsWith("/ecc/main/tuoplist/") && !requestUri.contains("main_2.htm"))){
				chain.doFilter(request, response);
			 	return;
			}
		}
		String enabledurls = config.getInitParameter("EnabledUrls");
		if (enabledurls!=null){
			String[] exts = enabledurls.split(";");
			if (requestUri!=null){
				for(String ext : exts){
					if (requestUri.endsWith(ext)){
						chain.doFilter(request, response);
					 	return;
					}
				}
			}
		}
		HttpSession session = ((HttpServletRequest) request).getSession();
 	 	HttpServletResponse resp = (HttpServletResponse)response;

		String recvText = request.getParameter("username");
	 	if (recvText ==null){
	 		recvText = (String)session.getAttribute("Session_Username");
	 	}else{
			session.setAttribute("Session_Username", recvText);
	 	}
	 	if (recvText != null){
	 		recvText = new String(new BASE64Decoder().decodeBuffer(recvText));
	 	 	String[] recvArray = recvText.split(",");
			String user = null;
		 	String right= null;
		 	String lt= null;
		 	String st= null;
	 	 	for (String kvText : recvArray){
	 	 		kvText = kvText.trim();
	 	 		String[] kv = kvText.split("=");
	 	 		if (kv.length == 2){
	 	 			if ("CN".equalsIgnoreCase(kv[0])){
	 	 				user = kv[1];
	 	 			}
	 	 			if ("UID".equalsIgnoreCase(kv[0])){
	 	 				right = kv[1];
	 	 			}if("L".equalsIgnoreCase(kv[0])){
	 	 				if(lt!=null){
	 	 					lt=kv[1]+lt;
	 	 				}else {
	 	 					lt= kv[1];
	 	 				}
	 	 			}if("ST".equalsIgnoreCase(kv[0])){
	 	 				st= kv[1];
	 	 			}
	 	 		}
	 	 	}
			try {
		 	 	String strSession = Manager.createView_zhongZuBu(st+lt,user);
		 	 	if (strSession !=null && "".equals(strSession) == false){
		 	 		session.setAttribute("usersessionid", strSession);

		 	 	}
			 	chain.doFilter(request, response);
			 	return;
			} catch (Exception e) {
			}
	 	}
	 	request.getRequestDispatcher("/").forward(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.config = filterConfig;

	}

}
