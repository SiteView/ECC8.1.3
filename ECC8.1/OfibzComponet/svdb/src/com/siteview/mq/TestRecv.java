package com.siteview.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class TestRecv {
	  public static void main(String[] args)
	  {
	    try
	    {
	    	String hostName = "192.168.1.181";
	      int portNumber = 5672;

	      Connection conn = new ConnectionFactory().newConnection(hostName, portNumber);
	      Channel ch = conn.createChannel();
	      
	      String queueName = "queuenameinaaa";
	      
	      ch.queueDeclare(queueName);
	      ch.queueBind(queueName, "exchangenamecccc", "routingkeyinbbbb");
	      QueueingConsumer consumer = new QueueingConsumer(ch);
	      ch.basicConsume(queueName,true, consumer);
	      while (true) {
	    	    QueueingConsumer.Delivery delivery = null;
	    	    try {
	    	        delivery = consumer.nextDelivery();
	    	    } catch (InterruptedException ie) {
	    	        continue;
	    	    }
	    	    System.out.println(new String(delivery.getBody()));
	    	    ch.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
	    	}
	    }
	    catch (Exception ex) {
	      System.err.println("Main thread caught exception: " + ex);
	      ex.printStackTrace();
	      System.exit(1);
	    }
	  }
}
