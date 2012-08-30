package com.siteview.mq;

import java.util.Map;

public interface MQProxy {
	public void send(String key ,String message)throws Exception;
	public void send(String key ,Map<String,String> map)throws Exception;
	public void close()throws Exception;
}
