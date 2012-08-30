package com.siteview.mq;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitProxy implements MQProxy {
	private String exchangeName = null;
	private String queueName = null;
	private String hostName = null;
	private int port = 5672;
	
	private String username = null;
	private String password = null;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getExchangeName() {
		return exchangeName;
	}

	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	private Connection conn = null;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Connection getConnection() throws Exception{
		if (conn == null || !conn.isOpen()){
			conn = new ConnectionFactory().newConnection(this.getHostName(), this.getPort());
		}
		return conn;
	}
	private Map<String,Channel> channelMap = new HashMap<String,Channel>();

	public Channel getChannel(String key) throws Exception {
		Channel channel = channelMap.get(key);
		if (channel == null || !channel.isOpen()){
			channel = getConnection().createChannel();
			channel.exchangeDeclare(this.getExchangeName(), "direct");
			channel.queueDeclare(key);
			channel.queueBind(key, this.getExchangeName(), key);
			channelMap.put(key, channel);
		}
		return channel;
	}


	public void send(String key ,String message) throws Exception {
		getChannel(key).basicPublish(this.getExchangeName(), key, null, message.getBytes("UTF-8"));
	}

	@Override
	public void send(String key ,Map<String, String> map) throws Exception {
		JSONObject jsonObject = JSONObject.fromObject(map);
		this.send(key,jsonObject.toString());
	}

	public void close() throws Exception {
		for (String key : channelMap.keySet()){
			Channel channel = channelMap.get(key);
			if (channel==null)continue;
			channel.close();
		}
		channelMap.clear();
		if (getConnection() != null)
			getConnection().close();
	}
}
