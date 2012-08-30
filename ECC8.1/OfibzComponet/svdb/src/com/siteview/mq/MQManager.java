package com.siteview.mq;

import java.util.Properties;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilProperties;

public class MQManager {
	public static final String module = MQManager.class.getName();
	private static String configfile = null;

	public static String getConfigfile() {
		return configfile;
	}

	public static void setConfigfile(String configfile) {
		MQManager.configfile = configfile;
	}

	private static MQProxy proxy = null;
	public static final String ExchangeName = "eventBoardCast";
	public static final String QueueName = "SiteView";
	public static final String HostName = "localhost";
	public static final int Port = 5672;
	public static MQProxy getProxy(String hostname, String port,
			String exchangeName, String queueName) throws Exception {
		return MQManager.getProxy(hostname, port, exchangeName, queueName, false);
	}

	public static MQProxy getProxy(String hostname, String port,
			String exchangeName, String queueName,boolean isClear) throws Exception {
		return getProxy(hostname == null ? HostName : hostname,
					getPort(port), exchangeName == null ? ExchangeName
							: exchangeName, queueName == null ? QueueName
							: queueName,isClear);
	}

	public static int getPort(String port) {
		try {
			return Integer.parseInt(port);
		} catch (Exception e) {
		}
		return Port;
	}
	public synchronized static MQProxy getProxy(String hostname, int port,
			String exchangeName, String queueName,boolean isClear) throws Exception {
		if (proxy == null || isClear) {
			Debug.logInfo("MQProxy remote host : " + hostname + ":" + port + ", exchangeName:" + exchangeName + ",queueName:" + queueName, module);
			MQProxy newProxy = new RabbitProxy();
			((RabbitProxy) newProxy).setHostName(hostname);
			((RabbitProxy) newProxy).setPort(port);
			((RabbitProxy) newProxy).setExchangeName(exchangeName);
			((RabbitProxy) newProxy).setQueueName(queueName);
			((RabbitProxy) newProxy).getConnection();
			UtilProperties.setPropertyValue(getConfigfile(), "RabbitQueueName", ((RabbitProxy) newProxy).getQueueName());
			UtilProperties.setPropertyValue(getConfigfile(), "RabbitExchangeName", ((RabbitProxy) newProxy).getExchangeName());
			UtilProperties.setPropertyValue(getConfigfile(), "RabbitServerHostName", ((RabbitProxy) newProxy).getHostName());
			UtilProperties.setPropertyValue(getConfigfile(), "RabbitServerPort", "" + ((RabbitProxy) newProxy).getPort());
			UtilProperties.setPropertyValue(getConfigfile(), "RabbitServerEnabled", "true");
			try{
				if (proxy!=null) proxy.close();
			}catch(Exception e){
			}
			proxy = newProxy;
		}
		return proxy;
	}

	public static MQProxy getProxy() throws Exception {
		return proxy;
	}
}
