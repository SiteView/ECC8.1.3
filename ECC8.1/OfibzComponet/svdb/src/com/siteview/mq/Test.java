package com.siteview.mq;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {
	public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {

			String hostName = "localhost";
			int portNumber = 5672;
			RabbitProxy proxy = new RabbitProxy();
			proxy.setHostName(hostName);
			proxy.setPort(portNumber);
			
			proxy.setExchangeName("exchangenamecccc");
			proxy.setQueueName("queuenameoutaaaa");
			Map map = new HashMap();
			map.put("id", "hlyi73@hotmail.com");
			map.put("message", SDF.format(new Date()));
			
			Map map1 = new HashMap();
			map1.put("aaaa", "bbb");
			
			List<Object> list = new ArrayList<Object>();
			list.add(map1);
			map.put("list", list);
			//map.put("retuuu", map1);
			
			proxy.send("routingkeyoutbbb", map);
			proxy.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
class MyThread extends Thread{
	private MQProxy proxy = null;
	public MyThread(MQProxy proxy){
		this.proxy = proxy;
	}
	public void run(){
		for (int i = 0 ; true ; i++){
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("time", Test.SDF.format(new Date()));
				map.put("ThreadName", this.getName());
				map.put("i", "" + i);
				proxy.send("eccqueues",map);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
