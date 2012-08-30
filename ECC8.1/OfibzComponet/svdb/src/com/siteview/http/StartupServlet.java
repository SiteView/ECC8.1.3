package com.siteview.http;

import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;

import com.siteview.controller.Httpserver;
import com.siteview.cwmp.AlertManager;
import com.siteview.jsvapi.Jsvapi;
import com.siteview.mq.MQManager;
import com.siteview.mq.RabbitProxy;


public class StartupServlet extends HttpServlet {
	private static final long serialVersionUID = 3966818109992769920L;
	public static final String module = StartupServlet.class.getName();
	private static Httpserver dcmHttpserver = null;
    public StartupServlet() {
    }

    public void destroy() {
    }

    /*
     * ofbiz 启动时自动启动的服务
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {

         try {
        	String configfile = config.getInitParameter("config-file");
        	Properties properties = UtilProperties.getProperties(configfile);
        	MQManager.setConfigfile(configfile);
        	

        	String enabled = properties.getProperty("DCMEnabled");
        	if ("true".equals(enabled)) {
        		String port = properties.getProperty("DCMPort");
        		try{
            		int iport = Integer.parseInt(port);
                	if (dcmHttpserver == null) dcmHttpserver = new Httpserver(iport);
        		}catch(Exception e){
                	if (dcmHttpserver == null) dcmHttpserver = new Httpserver();
        		}
        		if (dcmHttpserver != null) dcmHttpserver.start();
        	}
    		if (dcmHttpserver == null) Debug.logError("DCM HttpServer start fail : " + enabled, module);
    		
        	String rabbitEnabled = properties.getProperty("RabbitServerEnabled");
        	if ("true".equals(rabbitEnabled)) {
        		MQManager.getProxy(properties.getProperty("RabbitServerHostName"), properties.getProperty("RabbitServerPort"),properties.getProperty("RabbitExchangeName"),properties.getProperty("RabbitQueueName"));
        		Debug.logInfo("RabbitMQ service started! remote host : " + ((RabbitProxy)MQManager.getProxy()).getHostName() + ":" + ((RabbitProxy)MQManager.getProxy()).getPort(), module);
        	}

         	AlertManager.setInterval(properties.getProperty("SIPQueueQueryTimeInterval"));
        	AlertManager.setIntervalType(properties.getProperty("SIPQueueQueryTimeIntervalType"));
    		
 			Debug.logInfo("Pre-Started SVDB service !", module);
  			Jsvapi.test();
            Debug.logInfo("Successfully Started SVDB service", module);
            
		} catch (Exception e) {
			e.printStackTrace();
		}

     }
}
