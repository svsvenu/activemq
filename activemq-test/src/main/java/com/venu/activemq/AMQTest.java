package com.venu.activemq;

import java.io.IOException;

import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name="amqtest" , urlPatterns ={"/amqtest"})
public class AMQTest extends HttpServlet {
	

    @Resource(mappedName="java:/AMSQonnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(mappedName="java:/queue/test-queue")
   private Queue chatQueue;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
        	
        	System.out.println("called get");

			sendMessage("test");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
			sendMessage("test");
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void sendMessage(String text) throws JMSException {

        Connection connection = null;
        Session session = null;

        try {
            connection = connectionFactory.createConnection();
            connection.start();
            
            System.out.println("connection started");

            // Create a Session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            
            System.out.println("session  created");


            // Create a MessageProducer from the Session to the Topic or Queue
            MessageProducer producer = session.createProducer(chatQueue);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // Create a message
            TextMessage message = session.createTextMessage(text);

            // Tell the producer to send the message
            producer.send(message);
            
            
            System.out.println("messsage  sent");
        } 
        
        catch(Exception e){
        	
        	System.out.println(e.toString());
        	
        }finally {
            // Clean up
            if (session != null) session.close();
            if (connection != null) connection.close();
        }
    }

    public String receiveMessage() throws JMSException {

        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();

            // Create a Session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // Create a MessageConsumer from the Session to the Topic or Queue
            consumer = session.createConsumer(chatQueue);

            // Wait for a message
            TextMessage message = (TextMessage) consumer.receive(1000);

            return message.getText();
        } finally {
            if (consumer != null) consumer.close();
            if (session != null) session.close();
            if (connection != null) connection.close();
        }
    }

}
