package com.venu.activemq;

import java.net.URI;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.network.NetworkConnector;


public class EmbeddedBroker {
	
	public static void main(String[] args)
	{
		try{
		BrokerService broker = new BrokerService();
		// configure the broker
		broker.setBrokerName("localhost");
	//	NetworkConnector connector = broker.addNetworkConnector("static://"+"tcp://localhost:61618");
	//	connector.setDuplex(true);
		
		
		
	//	broker.addConnector("tcp://localhost:61618");
		broker.setUseJmx(false);
		
		
		NetworkConnector connectorRmote = broker.addNetworkConnector("static:(tcp://localhost:61616)?maxReconnectDelay=5000&useExponentialBackOff=false");
		
		connectorRmote.setUserName("admin");
		
		connectorRmote.setPassword("admin");

	
		
	//	broker.addNetworkConnector(new URI("static:(tcp://localhost:61616)?maxReconnectDelay=5000&useExponentialBackOff=false&Username=admin"));
		
		broker.start();
		
		System.out.println( broker.getNetworkConnectorURIs() );
		
		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
		
//
		Connection connection = cf.createConnection(); 
        connection.start(); 
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("TEST.FOO2");
        MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        String text = "Hello world! From: embedded broker in its own jvm " + Thread.currentThread().getName() + " : " ;
        TextMessage message = session.createTextMessage(text);
        System.out.println("Sent message: "+ message.hashCode() + " : " + Thread.currentThread().getName());
        
       
     //   int i=1;
      //  while (i<10) { 
        producer.send(message);
       // i++;
       // } 
        session.close();
        connection.close();
        
        broker.stop();
        
      /*  
        cf = new ActiveMQConnectionFactory("tcp://localhost:61616");
		
//		
		 connection = cf.createConnection(); // exception happens here...
        connection.start();
        //connection.setExceptionListener(this);
         session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         destination = session.createQueue("TEST.FOO1");
        MessageConsumer consumer = session.createConsumer(destination);
         message = (TextMessage) consumer.receive(1000);
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
             text = textMessage.getText();
            System.out.println("*****Received: " + text);
        } else {
            System.out.println("*****Received obj: " + message);
        }
        consumer.close();
        session.close();
        connection.close();*/
		
		}
		
		catch(Exception e){
			
			e.printStackTrace();
		}
		
		
	}
}
