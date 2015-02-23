package com.venu.activemq;

import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.ejb.ActivationConfigProperty;
import org.jboss.ejb3.annotation.ResourceAdapter; 

@MessageDriven(activationConfig =
{
@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Queue"),
@ActivationConfigProperty(propertyName="destination", propertyValue="testQueue"),
@ActivationConfigProperty(propertyName="acknowledgeMode", propertyValue="Auto-acknowledge")
})
@ResourceAdapter(value="org.apache.activemq.ra")
public class AMQMDB implements MessageListener {
public AMQMDB() {
}
public void onMessage(Message message) {
	
	
	System.out.println("received message" + message.toString());
}
}